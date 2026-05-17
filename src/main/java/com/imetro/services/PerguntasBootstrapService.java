package com.imetro.services;

import com.imetro.domain.dto.disciplina.DisciplinaDto;
import com.imetro.domain.dto.gemini.GeracaoSimuladoRequest;
import com.imetro.domain.dto.perguntas.BootstrapProgressSnapshot;
import com.imetro.domain.dto.perguntas.BootstrapResult;
import com.imetro.domain.dto.perguntas.GeracaoLote;
import com.imetro.domain.dto.perguntas.GeracaoLoteResultado;
import com.imetro.domain.dto.perguntas.GeracaoQuestoesEmLotes;
import com.imetro.domain.dto.perguntas.TopicoSubtopico;
import com.imetro.domain.enums.BootstrapStatus;
import com.imetro.persistence.repository.JdbcBasicSqlRepository;
import com.imetro.persistence.repository.OrientadorDisciplinaRepository;
import com.imetro.util.AppLogger;
import com.imetro.util.QuestaoUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PerguntasBootstrapService {

    private static final int DEFAULT_QUESTOES_INICIAIS = 120;
    private static final int QUESTOES_POR_SUBTOPICO = 40;
    private static final int MAX_QUESTOES_INICIAIS = 160;
    private static final int MAX_GEMINI_WORKERS = 2;
    private static final int MAX_LOTES_POR_DISCIPLINA = 8;
    private static final int MIN_SUBTOPICOS_POR_LOTE = 4;
    private static final String GENERATED_QUESTIONS_FILE = "questoes-geradas.json";
    private static final Pattern SUBTOPICOS_ARRAY_PATTERN =
        Pattern.compile("\"subtopicos\"\\s*:\\s*\\[(.*?)]", Pattern.DOTALL);
    private static final Pattern JSON_STRING_PATTERN = Pattern.compile("\"((?:\\\\.|[^\"\\\\])*)\"");
    private static final Logger LOGGER = AppLogger.getLogger(PerguntasBootstrapService.class);

    private final GeminiService geminiService;
    private final DisciplinaUploadBootstrapService uploadBootstrapService;
    private final OrientadorDisciplinaRepository orientadorDisciplinaRepository;

    public PerguntasBootstrapService() {
        this.geminiService = new GeminiService();
        this.uploadBootstrapService = new DisciplinaUploadBootstrapService();
        this.orientadorDisciplinaRepository = new OrientadorDisciplinaRepository();
    }

    public List<BootstrapResult> processarDisciplinasAutomaticasDoCandidato(UUID candidatoId) {
        return processarDisciplinasAutomaticasDoCandidato(candidatoId, false, null);
    }

    public List<BootstrapResult> processarDisciplinasAutomaticasDoCandidato(UUID candidatoId, boolean sobrescreverTopicos) {
        return processarDisciplinasAutomaticasDoCandidato(candidatoId, sobrescreverTopicos, null);
    }

    public List<BootstrapResult> processarDisciplinasAutomaticasDoCandidato(
        UUID candidatoId,
        boolean sobrescreverTopicos,
        BootstrapProgressListener progressListener
    ) {
        if (candidatoId == null) {
            return List.of();
        }


        emitirProgresso(
            progressListener,
            0.02,
            false,
            "A preparar a tua base de estudo",
            "A verificar as disciplinas selecionadas..."
        );

        List<DisciplinaDto> disciplinas = carregarDisciplinasDoCandidato(candidatoId);
        if (disciplinas.isEmpty()) {
            LOGGER.warning("Nenhuma disciplina encontrada para o candidato " + candidatoId + ".");
            return List.of();
        }

        emitirProgresso(
            progressListener,
            0.08,
            false,
            "A preparar as pastas dos livros",
            "A organizar uploads/disciplinas para as disciplinas escolhidas."
        );

        try {
            uploadBootstrapService.prepararPastasUploads();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Falha ao preparar as pastas de uploads para o candidato " + candidatoId + ".", e);
            return List.of(new BootstrapResult(
                null,
                "GERAL",
                BootstrapStatus.ERRO,
                0,
                0,
                "Nao foi possivel preparar as pastas de uploads: " + e.getMessage()
            ));
        }

        List<String> nomes = disciplinas.stream().map(DisciplinaDto::nome).toList();
        Map<String, Integer> orientadoresPorDisciplina = orientadorDisciplinaRepository
            .countOrientadoresByDisciplinaNames(nomes);

        ArrayList<BootstrapResult> resultados = new ArrayList<>();
        int totalDisciplinas = disciplinas.size();
        for (int indice = 0; indice < totalDisciplinas; indice++) {
            if (Thread.currentThread().isInterrupted()) {
                LOGGER.warning("Bootstrap automatico interrompido antes de concluir todas as disciplinas.");
                break;
            }

            DisciplinaDto disciplina = disciplinas.get(indice);
            emitirProgresso(
                progressListener,
                calcularProgresso(indice, totalDisciplinas, 0.12),
                false,
                "A verificar " + disciplina.nome(),
                "A confirmar se a disciplina ja tem perguntas ou se precisa de ler os livros."
            );

            int perguntasExistentes = contarPerguntasPorDisciplina(disciplina.nome());
            if (perguntasExistentes > 0) {

                BootstrapResult result = new BootstrapResult(
                    disciplina.id(),
                    disciplina.nome(),
                    BootstrapStatus.JA_EXISTENTE,
                    contarPdfsDaDisciplina(disciplina.id()),
                    perguntasExistentes,
                    "A disciplina ja tem perguntas reais na base."
                );
                resultados.add(result);
                emitirConclusaoDisciplina(progressListener, indice, totalDisciplinas, result);
                continue;
            }

            int totalOrientadores = orientadoresPorDisciplina.getOrDefault(disciplina.nome(), 0);
            if (totalOrientadores > 0) {
                LOGGER.info("A disciplina " + disciplina.nome() + " ficou em espera por orientacao.");
                BootstrapResult result = new BootstrapResult(
                    disciplina.id(),
                    disciplina.nome(),
                    BootstrapStatus.AGUARDANDO_ORIENTACAO,
                    contarPdfsDaDisciplina(disciplina.id()),
                    0,
                    "Existe orientacao cadastrada para esta disciplina; o processamento automatico ficou em espera."
                );
                resultados.add(result);
                emitirConclusaoDisciplina(progressListener, indice, totalDisciplinas, result);
                continue;
            }

            BootstrapResult result = processarDisciplinaSemOrientacao(
                disciplina,
                sobrescreverTopicos,
                progressListener,
                indice,
                totalDisciplinas
            );
            resultados.add(result);
            emitirConclusaoDisciplina(progressListener, indice, totalDisciplinas, result);
        }

        emitirProgresso(
            progressListener,
            1.0,
            false,
            "Base inicial atualizada",
            "O processamento automatico das disciplinas terminou."
        );
        return List.copyOf(resultados);
    }

    private BootstrapResult processarDisciplinaSemOrientacao(
        DisciplinaDto disciplina,
        boolean sobrescreverTopicos,
        BootstrapProgressListener progressListener,
        int indiceDisciplina,
        int totalDisciplinas
    ) {
        try {
            LOGGER.info("A processar automaticamente a disciplina " + disciplina.nome() + " (" + disciplina.id() + ").");
            emitirProgresso(
                progressListener,
                calcularProgresso(indiceDisciplina, totalDisciplinas, 0.24),
                false,
                "A ler os livros de " + disciplina.nome(),
                "A verificar PDFs disponiveis para a disciplina."
            );

            List<Path> pdfs = uploadBootstrapService.listarPdfs(disciplina.id());
            if (pdfs.isEmpty()) {
                LOGGER.warning("Nenhum PDF encontrado para a disciplina " + disciplina.nome() + ".");
                return new BootstrapResult(
                    disciplina.id(),
                    disciplina.nome(),
                    BootstrapStatus.SEM_PDFS,
                    0,
                    0,
                    "Nenhum PDF encontrado na pasta da disciplina."
                );
            }

            if (!geminiService.isConfigured()) {
                LOGGER.warning("Gemini nao configurado ao processar a disciplina " + disciplina.nome() + ".");
                return new BootstrapResult(
                    disciplina.id(),
                    disciplina.nome(),
                    BootstrapStatus.GEMINI_NAO_CONFIGURADO,
                    pdfs.size(),
                    0,
                    "Define GEMINI_API_KEY para gerar perguntas e topicos reais a partir dos livros."
                );
            }

            emitirProgresso(
                progressListener,
                calcularProgresso(indiceDisciplina, totalDisciplinas, 0.48),
                true,
                "A extrair topicos de " + disciplina.nome(),
                "O Gemini esta a resumir os topicos principais dos livros. Esta etapa pode demorar alguns minutos."
            );

            DisciplinaUploadBootstrapService.DisciplinaTopicosBootstrapResult topicosResult =
                uploadBootstrapService.processarCargaInicial(disciplina.id(), sobrescreverTopicos);
            LOGGER.info(
                "Topicos extraidos para a disciplina " + disciplina.nome()
                    + ". Arquivo: " + topicosResult.arquivoTopicos()
            );

            emitirProgresso(
                progressListener,
                calcularProgresso(indiceDisciplina, totalDisciplinas, 0.72),
                true,
                "A gerar perguntas de " + disciplina.nome(),
                "Os topicos extraidos estao a ser convertidos em questoes iniciais. Esta etapa pode demorar alguns minutos."
            );

            String jsonTopicos = Files.readString(topicosResult.arquivoTopicos(), StandardCharsets.UTF_8);
            int totalSubtopicos = contarSubtopicos(jsonTopicos);
            int quantidadeInicial = calcularQuantidadeInicial(totalSubtopicos);
            GeracaoQuestoesEmLotes geracao = gerarQuestoesEmLotes(
                disciplina,
                jsonTopicos,
                totalSubtopicos,
                quantidadeInicial,
                progressListener,
                indiceDisciplina,
                totalDisciplinas
            );

            Path pastaDisciplina = uploadBootstrapService.pastaDisciplina(disciplina.id());
            Path arquivoQuestoes = pastaDisciplina.resolve(GENERATED_QUESTIONS_FILE);
            Files.writeString(
                arquivoQuestoes,
                geracao.jsonAgregado(),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE
            );
            LOGGER.info("Questoes geradas em JSON para a disciplina " + disciplina.nome() + ": " + arquivoQuestoes);

            emitirProgresso(
                progressListener,
                calcularProgresso(indiceDisciplina, totalDisciplinas, 0.9),
                false,
                "A guardar perguntas de " + disciplina.nome(),
                "A inserir a base inicial de questoes na tabela perguntas."
            );

            int inseridas = 0;
            for (String jsonLote : geracao.jsonLotesComSucesso()) {
                inseridas += inserirPerguntasGeradas(disciplina, jsonLote);
            }
            int totalPerguntas = contarPerguntasPorDisciplina(disciplina.nome());
            LOGGER.info(
                "Disciplina " + disciplina.nome() + " recebeu " + inseridas
                    + " novas perguntas. Total atual: " + totalPerguntas + "."
            );

            return new BootstrapResult(
                disciplina.id(),
                disciplina.nome(),
                inseridas > 0 ? BootstrapStatus.PROCESSADO_AUTOMATICAMENTE : BootstrapStatus.ERRO,
                pdfs.size(),
                totalPerguntas,
                inseridas > 0
                    ? "Livros processados automaticamente. "
                        + topicosResult.detalhe()
                        + " "
                        + construirResumoGeracaoEmLotes(geracao)
                        + " E a base recebeu "
                        + inseridas
                        + " perguntas reais."
                    : "Os livros foram lidos, mas nenhuma pergunta nova foi inserida. "
                        + construirResumoGeracaoEmLotes(geracao)
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.log(
                Level.WARNING,
                "Processamento interrompido para a disciplina " + disciplina.nome() + " (" + disciplina.id() + ").",
                e
            );
            return new BootstrapResult(
                disciplina.id(),
                disciplina.nome(),
                BootstrapStatus.ERRO,
                contarPdfsDaDisciplina(disciplina.id()),
                contarPerguntasPorDisciplina(disciplina.nome()),
                "O processamento foi interrompido antes de concluir a geracao das perguntas."
            );
        } catch (Exception e) {
            LOGGER.log(
                Level.SEVERE,
                "Falha ao processar automaticamente a disciplina " + disciplina.nome() + " (" + disciplina.id() + ").",
                e
            );
            return new BootstrapResult(
                disciplina.id(),
                disciplina.nome(),
                BootstrapStatus.ERRO,
                contarPdfsDaDisciplina(disciplina.id()),
                contarPerguntasPorDisciplina(disciplina.nome()),
                "Falha ao processar os livros automaticamente: " + e.getMessage()
            );
        }
    }

    private GeracaoQuestoesEmLotes gerarQuestoesEmLotes(
        DisciplinaDto disciplina,
        String jsonTopicos,
        int totalSubtopicos,
        int quantidadeInicial,
        BootstrapProgressListener progressListener,
        int indiceDisciplina,
        int totalDisciplinas
    ) throws InterruptedException, IOException {
        List<GeracaoLote> lotes = construirLotesGeracao(jsonTopicos, quantidadeInicial);
        if (lotes.isEmpty()) {
            throw new IOException("Nao foi possivel montar lotes de geracao para a disciplina " + disciplina.nome() + ".");
        }

        ExecutorService executor = criarExecutorLotesGemini();
        ExecutorCompletionService<GeracaoLoteResultado> completionService = new ExecutorCompletionService<>(executor);
        ArrayList<String> jsonLotesComSucesso = new ArrayList<>();
        int concluidos = 0;
        int sucessos = 0;
        int falhas = 0;

        try {
            for (GeracaoLote lote : lotes) {
                completionService.submit(() -> executarGeracaoLote(disciplina, jsonTopicos, lote, totalSubtopicos));
            }

            while (concluidos < lotes.size()) {
                Future<GeracaoLoteResultado> future = completionService.take();
                GeracaoLoteResultado resultado = obterResultadoLote(future);
                concluidos++;

                if (resultado.sucesso()) {
                    jsonLotesComSucesso.add(resultado.jsonQuestoes());
                    sucessos++;
                    LOGGER.info(
                        "Lote " + resultado.lote().indice() + "/" + resultado.lote().totalLotes()
                            + " concluido para " + disciplina.nome() + "."
                    );
                } else {
                    falhas++;
                    LOGGER.warning(
                        "Lote " + resultado.lote().indice() + "/" + resultado.lote().totalLotes()
                            + " falhou para " + disciplina.nome() + ": " + resultado.erro()
                    );
                }

                double etapa = 0.72 + (0.18 * concluidos / lotes.size());
                emitirProgresso(
                    progressListener,
                    calcularProgresso(indiceDisciplina, totalDisciplinas, etapa),
                    false,
                    "A gerar perguntas de " + disciplina.nome(),
                    construirDetalheProgressoLotes(concluidos, lotes.size(), sucessos, falhas)
                );
            }
        } finally {
            executor.shutdownNow();
        }

        if (jsonLotesComSucesso.isEmpty()) {
            throw new IOException(
                "O Gemini nao conseguiu devolver nenhum lote de perguntas aproveitavel para " + disciplina.nome() + "."
            );
        }

        return new GeracaoQuestoesEmLotes(
            List.copyOf(jsonLotesComSucesso),
            montarJsonQuestoesAgregado(disciplina, jsonLotesComSucesso, sucessos, falhas),
            lotes.size(),
            sucessos,
            falhas
        );
    }

    private GeracaoLoteResultado obterResultadoLote(Future<GeracaoLoteResultado> future) throws InterruptedException {
        try {
            return future.get();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            String mensagem = cause.getMessage() == null || cause.getMessage().isBlank()
                ? "Falha inesperada ao concluir o lote."
                : cause.getMessage();
            return new GeracaoLoteResultado(new GeracaoLote(0, 0, List.of(), 0), null, mensagem);
        }
    }

    private ExecutorService criarExecutorLotesGemini() {
        AtomicInteger contador = new AtomicInteger(1);
        return Executors.newFixedThreadPool(MAX_GEMINI_WORKERS, runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("imetro-gemini-lote-" + contador.getAndIncrement());
            thread.setDaemon(true);
            thread.setUncaughtExceptionHandler((worker, throwable) ->
                LOGGER.log(Level.SEVERE, "Excecao nao tratada num worker de lotes do Gemini.", throwable)
            );
            return thread;
        });
    }

    private GeracaoLoteResultado executarGeracaoLote(
        DisciplinaDto disciplina,
        String jsonTopicos,
        GeracaoLote lote,
        int totalSubtopicos
    ) {
        try {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException("Lote interrompido antes de iniciar.");
            }

            String jsonQuestoes = geminiService.gerarSimuladoJsonAPartirDeTopicosEmLote(
                jsonTopicos,
                new GeracaoSimuladoRequest(
                    disciplina.nome(),
                    "pt-AO",
                    lote.quantidadeQuestoes(),
                    "MISTO",
                    construirInstrucaoBaseInicial(totalSubtopicos, lote.quantidadeQuestoes())
                        + construirInstrucaoFocoDoLote(lote)
                )
            );
            return new GeracaoLoteResultado(lote, jsonQuestoes, null);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new GeracaoLoteResultado(lote, null, "Lote interrompido antes de concluir a chamada ao Gemini.");
        } catch (Exception e) {
            return new GeracaoLoteResultado(
                lote,
                null,
                e.getMessage() == null || e.getMessage().isBlank()
                    ? "Falha desconhecida ao gerar o lote."
                    : e.getMessage()
            );
        }
    }

    private List<GeracaoLote> construirLotesGeracao(String jsonTopicos, int quantidadeInicial) {
        ArrayList<TopicoSubtopico> focos = extrairTopicosSubtopicos(jsonTopicos);
        if (focos.isEmpty()) {
            return List.of(new GeracaoLote(1, 1, List.of(), quantidadeInicial));
        }

        int subtopicosPorLote = calcularSubtopicosPorLote(focos.size());
        ArrayList<List<TopicoSubtopico>> grupos = particionarSubtopicos(focos, subtopicosPorLote);
        int totalFocos = focos.size();
        int restantes = quantidadeInicial;
        int focosRestantes = totalFocos;
        ArrayList<GeracaoLote> lotes = new ArrayList<>();

        for (int i = 0; i < grupos.size(); i++) {
            List<TopicoSubtopico> grupo = grupos.get(i);
            int quantidadeLote;
            if (i == grupos.size() - 1) {
                quantidadeLote = Math.max(1, restantes);
            } else {
                quantidadeLote = Math.max(1, (quantidadeInicial * grupo.size()) / Math.max(1, totalFocos));
                int minimoReservado = grupos.size() - i - 1;
                quantidadeLote = Math.min(quantidadeLote, Math.max(1, restantes - minimoReservado));
            }

            lotes.add(new GeracaoLote(i + 1, grupos.size(), List.copyOf(grupo), quantidadeLote));
            restantes -= quantidadeLote;
            focosRestantes -= grupo.size();

            if (focosRestantes <= 0 && restantes > 0 && !lotes.isEmpty()) {
                GeracaoLote ultimo = lotes.removeLast();
                lotes.add(new GeracaoLote(
                    ultimo.indice(),
                    ultimo.totalLotes(),
                    ultimo.focos(),
                    ultimo.quantidadeQuestoes() + restantes
                ));
                restantes = 0;
            }
        }

        return List.copyOf(lotes);
    }

    private int calcularSubtopicosPorLote(int totalFocos) {
        if (totalFocos <= 0) {
            return 0;
        }
        int sugerido = (int) Math.ceil((double) totalFocos / MAX_LOTES_POR_DISCIPLINA);
        return Math.max(1, Math.min(totalFocos, Math.max(MIN_SUBTOPICOS_POR_LOTE, sugerido)));
    }

    private ArrayList<List<TopicoSubtopico>> particionarSubtopicos(
        List<TopicoSubtopico> focos,
        int subtopicosPorLote
    ) {
        ArrayList<List<TopicoSubtopico>> grupos = new ArrayList<>();
        if (focos == null || focos.isEmpty()) {
            return grupos;
        }

        int tamanhoLote = Math.max(1, subtopicosPorLote);
        for (int inicio = 0; inicio < focos.size(); inicio += tamanhoLote) {
            int fim = Math.min(focos.size(), inicio + tamanhoLote);
            grupos.add(new ArrayList<>(focos.subList(inicio, fim)));
        }
        return grupos;
    }

    private ArrayList<TopicoSubtopico> extrairTopicosSubtopicos(String jsonTopicos) {
        String topicosArray = extrairCampoArrayJson(jsonTopicos, "topicos");
        if (topicosArray == null || topicosArray.isBlank()) {
            return new ArrayList<>();
        }

        LinkedHashSet<String> chaves = new LinkedHashSet<>();
        ArrayList<TopicoSubtopico> pares = new ArrayList<>();
        for (String objetoTopico : extrairObjetosJsonArray(topicosArray)) {
            String topico = extrairCampoStringJson(objetoTopico, "nome");
            List<String> subtopicos = extrairCampoArrayStringsJson(objetoTopico, "subtopicos");
            if (subtopicos.isEmpty()) {
                continue;
            }

            for (String subtopico : subtopicos) {
                String nomeTopico = topico == null || topico.isBlank() ? "Geral" : topico.trim();
                String nomeSubtopico = subtopico == null || subtopico.isBlank() ? null : subtopico.trim();
                if (nomeSubtopico == null) {
                    continue;
                }

                String chave = (nomeTopico + "::" + nomeSubtopico).toLowerCase();
                if (!chaves.add(chave)) {
                    continue;
                }
                pares.add(new TopicoSubtopico(nomeTopico, nomeSubtopico));
            }
        }

        return pares;
    }

    private String extrairCampoStringJson(String json, String campo) {
        if (json == null || json.isBlank() || campo == null || campo.isBlank()) {
            return null;
        }

        Pattern pattern = Pattern.compile("\"" + Pattern.quote(campo) + "\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"");
        Matcher matcher = pattern.matcher(json);
        if (!matcher.find()) {
            return null;
        }
        return QuestaoUtil.unescapeJson(matcher.group(1));
    }

    private List<String> extrairCampoArrayStringsJson(String json, String campo) {
        String arrayJson = extrairCampoArrayJson(json, campo);
        if (arrayJson == null || arrayJson.isBlank()) {
            return List.of();
        }

        LinkedHashSet<String> valores = new LinkedHashSet<>();
        Matcher matcher = JSON_STRING_PATTERN.matcher(arrayJson);
        while (matcher.find()) {
            String valor = QuestaoUtil.unescapeJson(matcher.group(1));
            if (valor != null && !valor.isBlank()) {
                valores.add(valor.trim());
            }
        }
        return List.copyOf(valores);
    }

    private String extrairCampoArrayJson(String json, String campo) {
        if (json == null || json.isBlank() || campo == null || campo.isBlank()) {
            return null;
        }

        Pattern pattern = Pattern.compile("\"" + Pattern.quote(campo) + "\"\\s*:\\s*\\[", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(json);
        if (!matcher.find()) {
            return null;
        }

        int inicioArray = matcher.end() - 1;
        int fimArray = localizarFechoJson(json, inicioArray, '[', ']');
        if (fimArray < 0) {
            return null;
        }
        return json.substring(inicioArray, fimArray + 1);
    }

    private ArrayList<String> extrairObjetosJsonArray(String arrayJson) {
        ArrayList<String> objetos = new ArrayList<>();
        if (arrayJson == null || arrayJson.isBlank()) {
            return objetos;
        }

        int cursor = 0;
        while (cursor < arrayJson.length()) {
            char atual = arrayJson.charAt(cursor);
            if (atual != '{') {
                cursor++;
                continue;
            }

            int fimObjeto = localizarFechoJson(arrayJson, cursor, '{', '}');
            if (fimObjeto < 0) {
                break;
            }

            objetos.add(arrayJson.substring(cursor, fimObjeto + 1));
            cursor = fimObjeto + 1;
        }

        return objetos;
    }

    private int localizarFechoJson(String valor, int inicio, char abre, char fecha) {
        boolean emString = false;
        int profundidade = 0;

        for (int i = inicio; i < valor.length(); i++) {
            char atual = valor.charAt(i);
            if (atual == '"' && !isEscaped(valor, i)) {
                emString = !emString;
                continue;
            }
            if (emString) {
                continue;
            }
            if (atual == abre) {
                profundidade++;
            } else if (atual == fecha) {
                profundidade--;
                if (profundidade == 0) {
                    return i;
                }
            }
        }

        return -1;
    }

    private boolean isEscaped(String valor, int indice) {
        int barras = 0;
        for (int i = indice - 1; i >= 0 && valor.charAt(i) == '\\'; i--) {
            barras++;
        }
        return barras % 2 != 0;
    }

    private String construirInstrucaoBaseInicial(int totalSubtopicos, int quantidadeInicial) {
        return """
            Gera uma base inicial de estudo para uso individual do candidato.
            Distribui as questoes pelos principais topicos e subtopicos do material.
            Produz cobertura suficiente para testes curtos, medios e longos sem ficar presa a uma amostra pequena.
            Sempre que houver cobertura suficiente, gera varias questoes por subtopico em niveis graduais.
            Evita repetir enunciados e cobre o conteudo programatico central identificado nos livros.
            """
            + construirInstrucaoDistribuicao(totalSubtopicos, quantidadeInicial);
    }

    private String construirInstrucaoFocoDoLote(GeracaoLote lote) {
        if (lote.focos().isEmpty()) {
            return """

                Este lote nao recebeu uma lista explicita de subtopicos.
                Usa os topicos e subtopicos do JSON completo, mantendo variedade e sem repetir enunciados.
                """;
        }

        StringBuilder instrucao = new StringBuilder();
        instrucao.append('\n');
        instrucao.append("Este e o lote ").append(lote.indice()).append(" de ").append(lote.totalLotes()).append(".\n");
        instrucao.append("Gera aproximadamente ").append(lote.quantidadeQuestoes()).append(" questoes.\n");
        instrucao.append("Foca o lote apenas nestes pares topico/subtopico:\n");
        for (TopicoSubtopico foco : lote.focos()) {
            instrucao.append("- ").append(foco.topico()).append(" -> ").append(foco.subtopico()).append('\n');
        }
        instrucao.append("Nao saias desta lista, exceto se precisares de contexto imediato do mesmo topico.\n");
        instrucao.append("Evita repetir enunciados de outros lotes e reparte as questoes por mais de um item quando houver base suficiente.\n");
        instrucao.append("Se um subtopico tiver pouca base, redistribui dentro desta mesma lista sem inventar conteudo.\n");
        return instrucao.toString();
    }

    private String construirDetalheProgressoLotes(int concluidos, int total, int sucessos, int falhas) {
        StringBuilder detalhe = new StringBuilder();
        detalhe.append("Lotes concluidos: ").append(concluidos).append('/').append(total).append('.');
        detalhe.append(" Sucesso: ").append(sucessos).append('.');
        if (falhas > 0) {
            detalhe.append(" Falhas toleradas: ").append(falhas).append('.');
        }
        detalhe.append(" No maximo ").append(MAX_GEMINI_WORKERS).append(" chamadas ao Gemini em paralelo.");
        return detalhe.toString();
    }

    private String montarJsonQuestoesAgregado(
        DisciplinaDto disciplina,
        List<String> jsonLotesComSucesso,
        int lotesSucesso,
        int lotesFalha
    ) {
        StringBuilder questoes = new StringBuilder();
        boolean primeiro = true;
        for (String jsonLote : jsonLotesComSucesso) {
            String arrayQuestoes = extrairCampoArrayJson(jsonLote, "questoes");
            if (arrayQuestoes == null || arrayQuestoes.length() < 2) {
                continue;
            }

            String conteudo = arrayQuestoes.substring(1, arrayQuestoes.length() - 1).trim();
            if (conteudo.isBlank()) {
                continue;
            }

            if (!primeiro) {
                questoes.append(',');
            }
            questoes.append(conteudo);
            primeiro = false;
        }

        String fonteResumo = lotesFalha > 0
            ? "Base inicial gerada em " + lotesSucesso + " lotes com " + lotesFalha + " falhas toleradas."
            : "Base inicial gerada em " + lotesSucesso + " lotes com ate " + MAX_GEMINI_WORKERS + " workers.";

        return new StringBuilder()
            .append("{\"titulo\":\"Base inicial de estudo - ").append(QuestaoUtil.escapeJson(disciplina.nome())).append("\",")
            .append("\"disciplina\":\"").append(QuestaoUtil.escapeJson(disciplina.nome())).append("\",")
            .append("\"idioma\":\"pt-AO\",")
            .append("\"fonteResumo\":\"").append(QuestaoUtil.escapeJson(fonteResumo)).append("\",")
            .append("\"questoes\":[").append(questoes).append("]}")
            .toString();
    }

    private String construirResumoGeracaoEmLotes(GeracaoQuestoesEmLotes geracao) {
        if (geracao.totalLotes() <= 1) {
            return "A geracao correu num lote unico.";
        }
        if (geracao.lotesFalha() <= 0) {
            return "A geracao foi repartida em " + geracao.totalLotes()
                + " lotes com no maximo " + MAX_GEMINI_WORKERS + " chamadas paralelas.";
        }
        return "A geracao foi repartida em " + geracao.totalLotes()
            + " lotes; " + geracao.lotesSucesso()
            + " concluiram e " + geracao.lotesFalha()
            + " falharam sem interromper o restante fluxo.";
    }

    private void emitirConclusaoDisciplina(
        BootstrapProgressListener progressListener,
        int indiceDisciplina,
        int totalDisciplinas,
        BootstrapResult result
    ) {
        emitirProgresso(
            progressListener,
            calcularProgresso(indiceDisciplina, totalDisciplinas, 1.0),
            false,
            "Concluido: " + result.nomeDisciplina(),
            result.detalhe()
        );
    }

    private double calcularProgresso(int indiceDisciplina, int totalDisciplinas, double etapaDaDisciplina) {
        if (totalDisciplinas <= 0) {
            return 0.0;
        }
        double etapaNormalizada = Math.max(0.0, Math.min(1.0, etapaDaDisciplina));
        return Math.min(1.0, (indiceDisciplina + etapaNormalizada) / totalDisciplinas);
    }

    private void emitirProgresso(
        BootstrapProgressListener listener,
        double progress,
        boolean indeterminate,
        String titulo,
        String detalhe
    ) {
        if (listener == null) {
            return;
        }
        listener.onProgress(new BootstrapProgressSnapshot(
            Math.max(0.0, Math.min(1.0, progress)),
            indeterminate,
            titulo,
            detalhe
        ));
    }

    private List<DisciplinaDto> carregarDisciplinasDoCandidato(UUID candidatoId) {
        String sql = """
            select d.id, d.nome, d.peso, d.nivel, d.objectivo
            from progresso_aluno_disciplina p
            join disciplinas d on d.id = p.disciplina_id
            where p.aluno_id = ?
            order by d.nome
            """;

        ArrayList<DisciplinaDto> disciplinas = new ArrayList<>();
        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, candidatoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    disciplinas.add(
                        new DisciplinaDto(
                            rs.getObject("id", UUID.class),
                            rs.getString("nome"),
                            rs.getObject("peso") instanceof Number number ? number.floatValue() : 1.0f,
                            com.imetro.domain.enums.NivelDisciplina.fromDescricao(rs.getString("nivel")),
                            rs.getString("objectivo")
                        )
                    );
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar disciplinas do candidato para o bootstrap real.", e);
        }

        return List.copyOf(disciplinas);
    }

    private int inserirPerguntasGeradas(DisciplinaDto disciplina, String jsonQuestoes) throws Exception {
        String sql = """
            with payload as (
              select cast(? as jsonb) as doc
            )
            insert into perguntas (
              id,
              disciplina,
              topico_principal,
              topico,
              subtopico,
              questao,
              respostas,
              pesos_resposta,
              resposta_correta,
              dificuldade,
              rigor,
              referencia_livro,
              pagina_inicio,
              pagina_fim,
              usa_grafico,
              grafico_tipo_curva,
              grafico_a,
              grafico_b,
              grafico_c,
              grafico_eixo_x,
              grafico_eixo_y,
              grafico_x_min,
              grafico_x_max,
              grafico_x_tick_unit,
              criado_em
            )
            select
              uuid_generate_v5(
                '6ba7b810-9dad-11d1-80b4-00c04fd430c8'::uuid,
                concat_ws(
                  '|',
                  ?,
                  coalesce(q->>'topico', 'Geral'),
                  coalesce(q->>'subtopico', coalesce(q->>'topico', 'Geral')),
                  coalesce(q->>'enunciado', '')
                )
              ),
              ?,
              coalesce(nullif(q->>'topicoPrincipal', ''), nullif(q->>'topico', ''), nullif(q->>'subtopico', ''), 'Geral'),
              coalesce(nullif(q->>'topico', ''), 'Geral'),
              coalesce(nullif(q->>'subtopico', ''), nullif(q->>'topico', ''), 'Geral'),
              q->>'enunciado',
              case
                when jsonb_typeof(q->'alternativas') = 'array' then q->'alternativas'
                else '[]'::jsonb
              end,
              case
                when jsonb_typeof(q->'pesosAlternativas') = 'array' then q->'pesosAlternativas'
                else '[]'::jsonb
              end,
              coalesce(nullif(q->>'respostaCorreta', ''), 'A'),
              upper(coalesce(nullif(q->>'dificuldade', ''), 'MEDIO')),
              least(1.0, greatest(0.0, coalesce(nullif(q->>'rigor', '')::double precision, 0.5))),
              nullif(q->>'referenciaLivro', ''),
              case
                when coalesce(nullif(q->>'paginaInicio', ''), '') ~ '^[0-9]+$' then (q->>'paginaInicio')::integer
                else null
              end,
              case
                when coalesce(nullif(q->>'paginaFim', ''), '') ~ '^[0-9]+$' then (q->>'paginaFim')::integer
                else null
              end,
              case
                when jsonb_typeof(q->'grafico') = 'object'
                  then coalesce((q->'grafico'->>'usar')::boolean, false)
                else false
              end,
              case
                when jsonb_typeof(q->'grafico') = 'object'
                  then upper(nullif(q->'grafico'->>'tipoCurva', ''))
                else null
              end,
              case
                when jsonb_typeof(q->'grafico') = 'object'
                  and coalesce(nullif(q->'grafico'->>'a', ''), '') ~ '^-?[0-9]+(?:\\.[0-9]+)?$'
                  then (q->'grafico'->>'a')::double precision
                else null
              end,
              case
                when jsonb_typeof(q->'grafico') = 'object'
                  and coalesce(nullif(q->'grafico'->>'b', ''), '') ~ '^-?[0-9]+(?:\\.[0-9]+)?$'
                  then (q->'grafico'->>'b')::double precision
                else null
              end,
              case
                when jsonb_typeof(q->'grafico') = 'object'
                  and coalesce(nullif(q->'grafico'->>'c', ''), '') ~ '^-?[0-9]+(?:\\.[0-9]+)?$'
                  then (q->'grafico'->>'c')::double precision
                else null
              end,
              case
                when jsonb_typeof(q->'grafico') = 'object'
                  then nullif(q->'grafico'->>'eixoX', '')
                else null
              end,
              case
                when jsonb_typeof(q->'grafico') = 'object'
                  then nullif(q->'grafico'->>'eixoY', '')
                else null
              end,
              case
                when jsonb_typeof(q->'grafico') = 'object'
                  and coalesce(nullif(q->'grafico'->>'xMin', ''), '') ~ '^-?[0-9]+(?:\\.[0-9]+)?$'
                  then (q->'grafico'->>'xMin')::double precision
                else null
              end,
              case
                when jsonb_typeof(q->'grafico') = 'object'
                  and coalesce(nullif(q->'grafico'->>'xMax', ''), '') ~ '^-?[0-9]+(?:\\.[0-9]+)?$'
                  then (q->'grafico'->>'xMax')::double precision
                else null
              end,
              case
                when jsonb_typeof(q->'grafico') = 'object'
                  and coalesce(nullif(q->'grafico'->>'xTickUnit', ''), '') ~ '^-?[0-9]+(?:\\.[0-9]+)?$'
                  then (q->'grafico'->>'xTickUnit')::double precision
                else null
              end,
              now()
            from payload
            cross join jsonb_array_elements(coalesce(payload.doc->'questoes', '[]'::jsonb)) as q
            where coalesce(nullif(q->>'enunciado', ''), '') <> ''
            on conflict (id) do nothing
            """;

        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, jsonQuestoes);
            stmt.setString(2, disciplina.nome());
            stmt.setString(3, disciplina.nome());
            return Math.max(stmt.executeUpdate(), 0);
        }
    }

    private int contarPerguntasPorDisciplina(String disciplinaNome) {
        String sql = "select count(*) from perguntas where lower(coalesce(disciplina, '')) = lower(?)";
        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, disciplinaNome == null ? "" : disciplinaNome);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Erro ao contar perguntas da disciplina " + disciplinaNome + ".", e);
            return 0;
        }
    }

    private int contarPdfsDaDisciplina(UUID disciplinaId) {
        if (disciplinaId == null) {
            return 0;
        }
        try {
            return uploadBootstrapService.listarPdfs(disciplinaId).size();
        } catch (Exception e) {
            return 0;
        }
    }

    private int contarSubtopicos(String jsonTopicos) {
        if (jsonTopicos == null || jsonTopicos.isBlank()) {
            return 0;
        }

        int total = 0;
        Matcher arrays = SUBTOPICOS_ARRAY_PATTERN.matcher(jsonTopicos);
        while (arrays.find()) {
            Matcher strings = JSON_STRING_PATTERN.matcher(arrays.group(1));
            while (strings.find()) {
                total++;
            }
        }
        return total;
    }

    private int calcularQuantidadeInicial(int totalSubtopicos) {
        if (totalSubtopicos <= 0) {
            return DEFAULT_QUESTOES_INICIAIS;
        }

        int quantidade = totalSubtopicos * QUESTOES_POR_SUBTOPICO;
        return Math.max(QUESTOES_POR_SUBTOPICO, Math.min(MAX_QUESTOES_INICIAIS, quantidade));
    }

    private String construirInstrucaoDistribuicao(int totalSubtopicos, int quantidadeInicial) {
        StringBuilder instrucao = new StringBuilder();
        instrucao.append('\n');
        instrucao.append("Meta de cobertura desta geracao: ").append(quantidadeInicial).append(" questoes.\n");
        if (totalSubtopicos > 0) {
            instrucao.append("O JSON atual trouxe cerca de ").append(totalSubtopicos).append(" subtopicos.\n");
            instrucao.append("Tenta cobrir cada subtopico com cerca de ")
                .append(QUESTOES_POR_SUBTOPICO)
                .append(" questoes no total, repartidas entre FACIL, MEDIO, DESAFIANTE e EXTRA quando houver base suficiente.\n");
            instrucao.append("Quando nao houver cobertura suficiente para 40 no mesmo subtopico, usa o maximo sustentado pelo material sem inventar conteudo.\n");
        }
        instrucao.append("Mantem variedade de dificuldade e nao concentres quase tudo num unico topico.\n");
        return instrucao.toString();
    }

    @FunctionalInterface
    public interface BootstrapProgressListener {
        void onProgress(BootstrapProgressSnapshot snapshot);
    }
}
