package com.imetro.services;

import com.imetro.domain.dto.disciplina.DisciplinaDto;
import com.imetro.persistence.repository.JdbcBasicSqlRepository;
import com.imetro.persistence.repository.OrientadorDisciplinaRepository;
import com.imetro.util.AppLogger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PerguntasBootstrapService {

    private static final int DEFAULT_QUESTOES_INICIAIS = 72;
    private static final String GENERATED_QUESTIONS_FILE = "questoes-geradas.json";
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

        LOGGER.info("A preparar processamento automatico das disciplinas do candidato " + candidatoId + ".");

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
            DisciplinaDto disciplina = disciplinas.get(indice);
            LOGGER.info("A avaliar a disciplina " + disciplina.nome() + " (" + disciplina.id() + ").");
            emitirProgresso(
                progressListener,
                calcularProgresso(indice, totalDisciplinas, 0.12),
                false,
                "A verificar " + disciplina.nome(),
                "A confirmar se a disciplina ja tem perguntas ou se precisa de ler os livros."
            );

            int perguntasExistentes = contarPerguntasPorDisciplina(disciplina.nome());
            if (perguntasExistentes > 0) {
                LOGGER.info("A disciplina " + disciplina.nome() + " ja possui " + perguntasExistentes + " perguntas.");
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
        LOGGER.info("Processamento automatico concluido para o candidato " + candidatoId + ".");
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
            String jsonQuestoes = geminiService.gerarSimuladoJsonAPartirDeTopicos(
                jsonTopicos,
                new GeminiService.GeracaoSimuladoRequest(
                    disciplina.nome(),
                    "pt-AO",
                    DEFAULT_QUESTOES_INICIAIS,
                    "MISTO",
                    """
                    Gera uma base inicial de estudo para uso individual do candidato.
                    Distribui as questoes pelos principais topicos e subtopicos do material.
                    Produz cobertura suficiente para testes curtos, medios e longos sem ficar presa a uma amostra pequena.
                    Sempre que houver cobertura suficiente, gera varias questoes por subtopico em niveis graduais.
                    Evita repetir enunciados e cobre o conteudo programatico central identificado nos livros.
                    """
                )
            );

            Path pastaDisciplina = uploadBootstrapService.pastaDisciplina(disciplina.id());
            Path arquivoQuestoes = pastaDisciplina.resolve(GENERATED_QUESTIONS_FILE);
            Files.writeString(
                arquivoQuestoes,
                jsonQuestoes,
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

            int inseridas = inserirPerguntasGeradas(disciplina, jsonQuestoes);
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
                        + " E a base recebeu "
                        + inseridas
                        + " perguntas reais."
                    : "Os livros foram lidos, mas nenhuma pergunta nova foi inserida."
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
              resposta_correta,
              dificuldade,
              rigor,
              referencia_livro,
              pagina_inicio,
              pagina_fim,
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
              upper(coalesce(nullif(q->>'respostaCorreta', ''), 'A')),
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

    public record BootstrapResult(
        UUID disciplinaId,
        String nomeDisciplina,
        BootstrapStatus status,
        int totalPdfs,
        int totalPerguntas,
        String detalhe
    ) {
    }

    public enum BootstrapStatus {
        PROCESSADO_AUTOMATICAMENTE,
        AGUARDANDO_ORIENTACAO,
        SEM_PDFS,
        GEMINI_NAO_CONFIGURADO,
        JA_EXISTENTE,
        ERRO
    }

    public record BootstrapProgressSnapshot(
        double progress,
        boolean indeterminate,
        String titulo,
        String detalhe
    ) {
    }

    @FunctionalInterface
    public interface BootstrapProgressListener {
        void onProgress(BootstrapProgressSnapshot snapshot);
    }
}
