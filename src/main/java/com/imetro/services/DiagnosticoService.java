package com.imetro.services;

import com.imetro.App;
import com.imetro.domain.CacheService;
import com.imetro.domain.dto.Topico;
import com.imetro.domain.dto.diagnostico.DiagnosticoDisciplinaResumo;
import com.imetro.domain.dto.diagnostico.DiagnosticoDto;
import com.imetro.domain.dto.diagnostico.HistoricoDiagnosticoResumo;
import com.imetro.domain.dto.diagnostico.PrimeiroDiagnosticoResumo;
import com.imetro.domain.dto.diagnostico.ProgressaoRigorAtual;
import com.imetro.domain.dto.diagnostico.ProgressoResumo;
import com.imetro.domain.dto.diagnostico.QuestaoRigorResultado;
import com.imetro.domain.dto.diagnostico.StatsDiagnotico;
import com.imetro.domain.dto.diagnostico.StatsQuestaoQtd;
import com.imetro.domain.dto.diagnostico.TempoStatsDiagnostico;
import com.imetro.domain.dto.diagnostico.Value;
import com.imetro.domain.dto.disciplina.DisciplinaDto;
import com.imetro.domain.dto.perguntas.BootstrapResult;
import com.imetro.domain.enums.NivelDificuldadeAdaptativa;
import com.imetro.domain.dto.stats.Stats;
import com.imetro.persistence.repository.DiagnosticoRepository;
import com.imetro.persistence.repository.JdbcBasicSqlRepository;
import com.imetro.persistence.repository.PerguntasRepository;
import com.imetro.persistence.repository.ProgressaoRigorRepository;
import com.imetro.persistence.repository.RecomendacaoRepository;
import com.imetro.ui.model.Questao;
import com.imetro.util.Authentication;
import com.imetro.util.CalculoStats;
import com.imetro.util.ConversorTempo;
import com.imetro.util.QuestaoUtil;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DiagnosticoService {

    private static final Pattern JSON_STRING_PATTERN = Pattern.compile("\"((?:\\\\.|[^\"\\\\])*)\"");
    private static final Pattern JSON_NUMBER_PATTERN = Pattern.compile("-?\\d+(?:\\.\\d+)?");
    private static final DateTimeFormatter DATA_RESUMIDA = DateTimeFormatter.ofPattern("dd/MM");

    private final PerguntasRepository perguntasRepository;
    private final DiagnosticoRepository diagnosticoRepository = new DiagnosticoRepository();
    private final RecomendacaoRepository recomendacaoRepository = new RecomendacaoRepository();
    private final ProgressaoRigorRepository progressaoRigorRepository = new ProgressaoRigorRepository();
    private final PerguntasBootstrapService perguntasBootstrapService;

    public DiagnosticoService() {
        this.perguntasRepository = new PerguntasRepository();
        this.perguntasBootstrapService = new PerguntasBootstrapService();
    }

    public DiagnosticoRepository getDiagnosticoRepository() {
        return diagnosticoRepository;
    }

    public List<Questao> carregarQuestoesReais() {
        try {
            return perguntasRepository.findAll().stream()
                .map(this::mapearQuestao)
                .filter(questao -> questao.getEnunciado() != null && !questao.getEnunciado().isBlank())
                .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception e) {
            System.err.println("Erro ao carregar questoes reais para o diagnostico: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean agendarSincronizacaoSeNecessario(UUID candidatoId) {
        if (candidatoId == null) {
            return false;
        }
        if (PerguntasBootstrapAsyncService.getInstance().isRunningFor(candidatoId)) {
            return false;
        }

        CompletableFuture.runAsync(() -> sincronizarDisciplinasAutomaticas(candidatoId), App.EXECUTOR_DIAGNOSTICO);
        return true;
    }

    public List<Topico> carregarTopicosPorDisciplina(String disciplina) {
        String disciplinaNormalizada =  QuestaoUtil.normalizar(disciplina);
        List<Questao> questoesDisciplina = carregarQuestoesReais().stream()
            .filter(questao -> disciplinaNormalizada.equals( QuestaoUtil.normalizar(questao.getDisciplina())))
            .collect(Collectors.toCollection(ArrayList::new));

        if (questoesDisciplina.isEmpty()) {
            return List.of();
        }

        String nomeDisciplina = questoesDisciplina.getFirst().getDisciplina();
        UUID disciplinaId =  QuestaoUtil.resolverDisciplinaId(nomeDisciplina);
        return construirTopicos(disciplinaId, nomeDisciplina, questoesDisciplina);
    }

    public Map<String, Double> carregarProgressoSubtopicos(UUID candidatoId, Collection<Topico> topicos) {
        if (candidatoId == null || topicos == null || topicos.isEmpty()) {
            return Map.of();
        }

        LinkedHashSet<String> chavesSelecionadas = new LinkedHashSet<>();
        for (Topico topico : topicos) {
            if (topico == null || topico.subTopicos() == null) {
                continue;
            }
            for (String subtopico : topico.subTopicos()) {
                chavesSelecionadas.add(chaveSubtopico(topico.disciplina(), subtopico));
            }
        }

        if (chavesSelecionadas.isEmpty()) {
            return Map.of();
        }

        LinkedHashMap<String, Double> progressoPorRigor = new LinkedHashMap<>();
        LinkedHashMap<String, Double> progressoPorDiagnostico = new LinkedHashMap<>();
        LinkedHashMap<String, Boolean> precisaRevisaoPorChave = new LinkedHashMap<>();
        LinkedHashMap<String, Boolean> precisaNovoDiagnosticoPorChave = new LinkedHashMap<>();

        String sqlRigor = """
            select coalesce(d.nome, '') as disciplina_nome,
              pr.subtopico,
              pr.rigor_atual,
              pr.rigor_alvo,
              pr.precisa_revisao
            from progressao_rigor pr
            left join disciplinas d on d.id = pr.disciplina_id
            where pr.aluno_id = ?
            """;

        String sqlDiagnostico = """
            select distinct on (
              lower(coalesce(dg.disciplina_nome, '')),
              lower(coalesce(rr.subtopico, ''))
            )
              dg.disciplina_nome,
              rr.subtopico,
              rr.progresso_atingido,
              rr.precisa_novo_diagnostico
            from recomendacoes_rigor rr
            join diagnosticos dg on dg.id = rr.diagnostico_id
            where dg.candidato_id = ?
            order by lower(coalesce(dg.disciplina_nome, '')),
              lower(coalesce(rr.subtopico, '')),
              coalesce(dg.concluido_em, dg.iniciado_em) desc,
              rr.criado_em desc
            """;

        try (Connection conn = JdbcBasicSqlRepository.openRequiredConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sqlRigor)) {
                stmt.setObject(1, candidatoId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String chave = chaveSubtopico(
                            rs.getString("disciplina_nome"),
                            rs.getString("subtopico")
                        );
                        if (!chavesSelecionadas.contains(chave)) {
                            continue;
                        }

                        double rigorAtual = rs.getObject("rigor_atual") instanceof Number number
                            ? number.doubleValue()
                            : 0d;
                        double rigorAlvo = rs.getObject("rigor_alvo") instanceof Number number
                            ? number.doubleValue()
                            : 0d;
                        progressoPorRigor.put(chave, calcularProgressoPorRigor(rigorAtual, rigorAlvo));
                        precisaRevisaoPorChave.put(chave, rs.getBoolean("precisa_revisao"));
                        CacheService.put(chave, NivelDificuldadeAdaptativa.resolverNivelPorRigor(rigorAtual));
                    }
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlDiagnostico)) {
                stmt.setObject(1, candidatoId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String chave = chaveSubtopico(
                            rs.getString("disciplina_nome"),
                            rs.getString("subtopico")
                        );
                        if (!chavesSelecionadas.contains(chave)) {
                            continue;
                        }

                        Object progressoRaw = rs.getObject("progresso_atingido");
                        if (progressoRaw instanceof Number number) {
                            progressoPorDiagnostico.put(chave,  QuestaoUtil.limitarPercentualUnitario(number.doubleValue()));
                        }
                        precisaNovoDiagnosticoPorChave.put(chave, rs.getBoolean("precisa_novo_diagnostico"));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar progresso por subtopico: " + e.getMessage());
            return Map.of();
        }

        LinkedHashMap<String, Double> progressoFinal = new LinkedHashMap<>();
        for (String chave : chavesSelecionadas) {
            Double progressoRigor = progressoPorRigor.get(chave);
            Double progressoDiagnostico = progressoPorDiagnostico.get(chave);
            boolean precisaRevisao = Boolean.TRUE.equals(precisaRevisaoPorChave.get(chave));
            boolean precisaNovoDiagnostico = Boolean.TRUE.equals(precisaNovoDiagnosticoPorChave.get(chave));

            double progresso = resolverProgressoSubtopico(
                progressoRigor,
                progressoDiagnostico,
                precisaRevisao,
                precisaNovoDiagnostico
            );
            progressoFinal.put(chave, progresso);
        }

        return Map.copyOf(progressoFinal);
    }

    public boolean temHistoricoDiagnostico(UUID candidatoId) {
        if (candidatoId == null) {
            return false;
        }

        String sql = """
            select 1
            from diagnosticos
            where candidato_id = ?
            limit 1
            """;

        try (Connection conn = JdbcBasicSqlRepository.openRequiredConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, candidatoId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            System.err.println("Erro ao verificar historico do diagnostico: " + e.getMessage());
            return false;
        }
    }

    public List<BootstrapResult> sincronizarDisciplinasAutomaticas(UUID candidatoId) {
        if (candidatoId == null) {
            return List.of();
        }
        if (PerguntasBootstrapAsyncService.getInstance().isRunningFor(candidatoId)) {
            return List.of();
        }
        return perguntasBootstrapService.processarDisciplinasAutomaticasDoCandidato(candidatoId);
    }

    public PrimeiroDiagnosticoResumo carregarPrimeiroDiagnosticoResumo(UUID candidatoId) {
        boolean processamentoEmCurso = PerguntasBootstrapAsyncService.getInstance().isRunningFor(candidatoId);
        agendarSincronizacaoSeNecessario(candidatoId);
        List<Questao> questoes = carregarQuestoesReais();
        List<DisciplinaDto> disciplinas = diagnosticoRepository.carregarDisciplinasAtivasDoCandidato(candidatoId);
        if (disciplinas.isEmpty()) {
            disciplinas = DisciplinaService.discCategoria();
        }

        Map<String, List<Questao>> questoesPorDisciplina = questoes.stream()
            .collect(Collectors.groupingBy(
                questao -> QuestaoUtil.normalizar(questao.getDisciplina()),
                LinkedHashMap::new,
                Collectors.toCollection(ArrayList::new)
            ));

        ArrayList<Topico> topicos = new ArrayList<>();
        ArrayList<String> disciplinasSemBase = new ArrayList<>();
        int totalQuestoes = 0;
        int totalDisciplinas = 0;

        for (DisciplinaDto disciplina : disciplinas) {
            List<Questao> questoesDisciplina = questoesPorDisciplina.get( QuestaoUtil.normalizar(disciplina.nome()));
            if (questoesDisciplina == null || questoesDisciplina.isEmpty()) {
                disciplinasSemBase.add(disciplina.nome());
                continue;
            }

            totalDisciplinas++;
            totalQuestoes += questoesDisciplina.size();
            topicos.addAll(construirTopicos(disciplina.id(), disciplina.nome(), questoesDisciplina));
        }

        boolean pronto = !topicos.isEmpty();
        String detalhe = pronto
            ? "Escolha os topicos que quer diagnosticar e arranque agora mesmo. Depois deste primeiro passo, os cards normais passam a aparecer com historico real."
            : processamentoEmCurso
                ? "Ainda estamos a ler os teus livros em segundo plano. Podes navegar noutras abas enquanto a barra no topo acompanha a geracao das perguntas."
                : "Ainda nao encontramos questoes reais suficientes para o teu primeiro diagnostico. Os PDFs ficam em `uploads/disciplinas/<uuid>`, os topicos saem em `topicos-extraidos.json` e as disciplinas sem orientacao tentam gerar perguntas automaticamente.";

        return new PrimeiroDiagnosticoResumo(
            totalDisciplinas,
            topicos.size(),
            totalQuestoes,
            new ArrayList<>(topicos),
            List.copyOf(disciplinasSemBase),
            pronto,
            detalhe
        );
    }

    public List<DiagnosticoDisciplinaResumo> carregarDiagnosticosDisponiveis(UUID candidatoId) {
        List<Questao> questoes = carregarQuestoesReais();
        if (questoes.isEmpty()) {
            return List.of();
        }

        List<DisciplinaDto> disciplinasAtivas = diagnosticoRepository.carregarDisciplinasAtivasDoCandidato(candidatoId);
        Set<String> disciplinasPermitidas = disciplinasAtivas.stream()
            .map(DisciplinaDto::nome)
            .map( QuestaoUtil::normalizar)
            .collect(Collectors.toCollection(LinkedHashSet::new));

        Map<String, DisciplinaDto> disciplinasPorNome = DisciplinaService.discCategoria().stream()
            .filter(disciplina -> disciplina.id() != null)
            .collect(Collectors.toMap(
                disciplina ->  QuestaoUtil.normalizar(disciplina.nome()),
                disciplina -> disciplina,
                (left, right) -> left,
                LinkedHashMap::new
            ));

        Map<UUID, ProgressoResumo> progressosPorDisciplina = carregarProgressos(candidatoId);
        Map<UUID, HistoricoDiagnosticoResumo> historicosPorDisciplinaId = carregarHistoricosPorDisciplinaId(candidatoId);
        Map<String, HistoricoDiagnosticoResumo> historicosPorNome = carregarHistoricosPorNome(candidatoId);

        Map<String, List<Questao>> questoesPorDisciplina = questoes.stream()
            .collect(Collectors.groupingBy(
                questao ->  QuestaoUtil.normalizar(questao.getDisciplina()),
                LinkedHashMap::new,
                Collectors.toCollection(ArrayList::new)
            ));

        ArrayList<DiagnosticoDisciplinaResumo> resumos = new ArrayList<>();
        for (Map.Entry<String, List<Questao>> entry : questoesPorDisciplina.entrySet()) {
            if (!disciplinasPermitidas.isEmpty() && !disciplinasPermitidas.contains(entry.getKey())) {
                continue;
            }
            List<Questao> questoesDisciplina = entry.getValue();
            if (questoesDisciplina.isEmpty()) {
                continue;
            }

            String disciplinaDetectada = questoesDisciplina.getFirst().getDisciplina();
            DisciplinaDto disciplinaDto = disciplinasPorNome.get(entry.getKey());
            UUID disciplinaId = disciplinaDto != null
                ? disciplinaDto.id()
                :  QuestaoUtil.resolverDisciplinaId(disciplinaDetectada);
            String nomeDisciplina = disciplinaDto != null ? disciplinaDto.nome() : disciplinaDetectada;
            String objectivo = disciplinaDto == null ? null : disciplinaDto.objectivo();

            List<Topico> topicos = construirTopicos(disciplinaId, nomeDisciplina, questoesDisciplina);
            int totalTopicos = topicos.size();
            int totalSubtopicos = topicos.stream()
                .mapToInt(topico -> topico.subTopicos() == null ? 0 : topico.subTopicos().length)
                .sum();
            int totalQuestoes = questoesDisciplina.size();

            ProgressoResumo progresso = progressosPorDisciplina.get(disciplinaId);
            HistoricoDiagnosticoResumo historico = historicosPorDisciplinaId.get(disciplinaId);
            if (historico == null) {
                historico = historicosPorNome.get(entry.getKey());
            }

            double indicador = resolverIndicadorPrincipal(totalQuestoes, progresso, historico);
            String legendaIndicador = historico != null || (progresso != null && progresso.taxaAcertoGeral() != null)
                ? "Acerto atual"
                : "Cobertura";
            String destaque = historico != null && historico.percentualAcerto() != null
                ?  QuestaoUtil.formatarPercentual(historico.percentualAcerto()) + " de acerto"
                : "Base pronta";
            String resumo = historico != null
                ? montarResumoHistorico(historico, totalQuestoes)
                : totalQuestoes + " questoes reais prontas para o primeiro diagnostico.";
            String tendencia = historico != null && historico.evolucaoPercentual() != null
                ?  QuestaoUtil.formatarDelta(historico.evolucaoPercentual())
                : "NOVO";
            String nivel = historico != null && historico.nivel() != null && !historico.nivel().isBlank()
                ? historico.nivel()
                : progresso != null && progresso.nivelAtual() != null && !progresso.nivelAtual().isBlank()
                    ? progresso.nivelAtual()
                    : "INICIANTE";
            String observacao = historico != null
                ? montarObservacaoHistorico(historico)
                : (objectivo == null || objectivo.isBlank()
                    ? "Selecione esta disciplina para iniciar um diagnostico com questoes reais do banco."
                    : objectivo);

            resumos.add(
                new DiagnosticoDisciplinaResumo(
                    disciplinaId,
                    nomeDisciplina,
                    objectivo,
                    new ArrayList<>(topicos),
                    totalQuestoes,
                    totalTopicos,
                    totalSubtopicos,
                    indicador,
                    legendaIndicador,
                    destaque,
                    resumo,
                    tendencia,
                    nivel,
                    observacao
                )
            );
        }

        resumos.sort(Comparator.comparing(DiagnosticoDisciplinaResumo::nomeDisciplina, String.CASE_INSENSITIVE_ORDER));
        return List.copyOf(resumos);
    }

    public void registrarDiagnosticoConcluido(
        UUID candidatoId,
        List<Questao> questoes,
        List<Character> respostasUsuario,
        String tempoFormatado
    ) {
        if (candidatoId == null || questoes == null || questoes.isEmpty() || respostasUsuario == null || respostasUsuario.isEmpty()) {
            return;
        }

        int limite = Math.min(questoes.size(), respostasUsuario.size());
        if (limite <= 0) {
            return;
        }

        Map<String, ArrayList<Integer>> indicesPorDisciplina = new LinkedHashMap<>();

        for (int i = 0; i < limite; i++) {
            Questao questao = questoes.get(i);
            if (questao == null || questao.getDisciplina() == null || questao.getDisciplina().isBlank()) {
                continue;
            }
            indicesPorDisciplina
                .computeIfAbsent( QuestaoUtil.normalizar(questao.getDisciplina()), ignored -> new ArrayList<>())
                .add(i);
        }

        if (indicesPorDisciplina.isEmpty()) {
            return;
        }

        int duracaoSegundos = ConversorTempo.parseTempoEmSegundos(tempoFormatado);
        LocalDateTime concluidoEm = LocalDateTime.now();

        try (Connection conn = JdbcBasicSqlRepository.openRequiredConnection()) {
            conn.setAutoCommit(false);
            try {
                for (Map.Entry<String, ArrayList<Integer>> entry : indicesPorDisciplina.entrySet()) {
                    System.out.println("Entrando");
                    ArrayList<Integer> indices = entry.getValue();
                    Questao questaoBase = questoes.get(indices.getFirst());
                    String nomeDisciplina =  QuestaoUtil.formatarDisciplina(questaoBase.getDisciplina());
                    UUID disciplinaId =  QuestaoUtil.resolverDisciplinaId(nomeDisciplina);

                    int totalQuestoes = indices.size();
                    int totalAcertos = 0;
                    for (Integer indice : indices) {
                        if (respostasUsuario.get(indice) == questoes.get(indice).getRespostaCorreta()) {
                            totalAcertos++;
                        }
                    }

                    int totalErros = Math.max(0, totalQuestoes - totalAcertos);
                    double percentualAcerto = totalQuestoes == 0 ? 0d : (totalAcertos * 100.0) / totalQuestoes;
                    Double ultimoPercentual = diagnosticoRepository.buscarUltimoPercentualDiagnostico(conn, candidatoId, disciplinaId, nomeDisciplina);

                    Double evolucao = ultimoPercentual == null ? null : percentualAcerto - ultimoPercentual;
                    String nivel = QuestaoUtil.resolverNivelDiagnostico(percentualAcerto);
                    double precisao = CalculoStats.calcularPrecisaoMedia(indices, questoes, respostasUsuario);
                    double consistencia = CalculoStats.calcularConsistencia(ultimoPercentual, percentualAcerto);
                    double logica = CalculoStats.calcularLogica(indices, questoes, respostasUsuario);
                    double resiliencia = 0d;
                    double velocidade = CalculoStats.calcularVelocidade(duracaoSegundos, totalQuestoes);
                    System.out.println("Inserindo");

                    UUID diagnosticoId = diagnosticoRepository.inserir(
                        candidatoId,
                        disciplinaId,
                        nomeDisciplina,
                        concluidoEm.minusSeconds(Math.max(0, duracaoSegundos)),
                        concluidoEm,
                        duracaoSegundos,
                        totalQuestoes,
                        totalAcertos,
                        totalErros,
                        percentualAcerto,
                        evolucao,
                        nivel,
                        velocidade,
                        precisao,
                        logica,
                        consistencia,
                        resiliencia,
                        QuestaoUtil.construirJsonRespostas(indices, questoes, respostasUsuario),
                        ultimoPercentual == null
                            ? "Primeiro diagnostico concluido com dados reais."
                            : "Diagnostico atualizado com nova tentativa."
                    );

                    diagnosticoRepository.atualizarProgressoAposDiagnostico(
                        conn,
                        candidatoId,
                        disciplinaId,
                        nomeDisciplina,
                        totalQuestoes,
                        totalAcertos,
                        totalErros,
                        nivel,
                        concluidoEm
                    );

                    atualizarProgressaoRigor(
                        conn,
                        diagnosticoId,
                        candidatoId,
                        disciplinaId,
                        indices,
                        questoes,
                        respostasUsuario
                    );
                }

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                e.printStackTrace();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            System.err.println("Erro ao registrar diagnostico concluido: " + e.getMessage());
        }
    }

    public List<DiagnosticoDto> listDiagnotico(){
        List<DiagnosticoDto> list= new ArrayList<>();
        try {
            for (Map<String, Object> map : diagnosticoRepository.findAll()) {

                list.add(DiagnosticoDto.ParseMapDto(map));
            }
        } catch (SQLException e) {

            System.err.println(e.getMessage());
        }
        return list;
    }

    private void atualizarProgressaoRigor(
        Connection conn,
        UUID diagnosticoId,
        UUID candidatoId,
        UUID disciplinaId,
        List<Integer> indices,
        List<Questao> questoes,
        List<Character> respostasUsuario
    ) throws SQLException {
        LinkedHashMap<String, ArrayList<QuestaoRigorResultado>> porTopico = new LinkedHashMap<>();

        for (Integer indice : indices) {
            Questao questao = questoes.get(indice);
            if (questao == null) {
                continue;
            }

            boolean acertou = respostasUsuario.get(indice) == questao.getRespostaCorreta();
            porTopico.computeIfAbsent(questao.getSubtopico(), ignored -> new ArrayList<>())
                .add(new QuestaoRigorResultado(questao, acertou));
        }

        for (Map.Entry<String, ArrayList<QuestaoRigorResultado>> entry : porTopico.entrySet()) {
            String subtopico = entry.getKey();
            ArrayList<QuestaoRigorResultado> resultados = entry.getValue();
            if (resultados.isEmpty()) {
                continue;
            }

            ProgressaoRigorAtual atual = carregarProgressaoRigorAtual(conn, candidatoId, disciplinaId, subtopico);

            int total = resultados.size();
            int acertos = (int) resultados.stream().filter(QuestaoRigorResultado::acertou).count();
            int erros = Math.max(0, total - acertos);
            double taxaAcerto = total == 0 ? 0d : (double) acertos / total;
            double rigorMedioTentado = resultados.stream()
                .map(QuestaoRigorResultado::questao)
                .mapToDouble(Questao::getRigor)
                .average()
                .orElse(atual.rigorAtual());

            double rigorAtualNovo = CalculoStats.calcularNovoRigor(atual.rigorAtual(), atual.rigorAlvo(), rigorMedioTentado, taxaAcerto);
            int acertosConsecutivos = taxaAcerto >= 0.8 ? atual.acertosConsecutivos() + acertos : 0; // TODO CONFIG_ADAPTATIVA: limiar de acerto consecutivo ainda fixo em 0.8.
            int errosConsecutivos = taxaAcerto < 0.5 ? atual.errosConsecutivos() + erros : 0; // TODO CONFIG_ADAPTATIVA: limiar de erro consecutivo ainda fixo em 0.5.
            boolean precisaRevisao = taxaAcerto < 0.6 || errosConsecutivos >= 2; // TODO CONFIG_ADAPTATIVA: regra de revisao ainda fixa (0.6 / 2 erros).
            double rigorRecomendado = precisaRevisao
                ? Math.max(0.05d, rigorAtualNovo - 0.04d) // TODO CONFIG_ADAPTATIVA: ajuste de descida e piso minimo ainda fixos.
                : Math.min(atual.rigorAlvo(), Math.max(rigorAtualNovo, rigorMedioTentado) + 0.06d); // TODO CONFIG_ADAPTATIVA: ajuste de subida ainda fixo.

            String recomendacaoLivro = escolherReferenciaLivro(resultados);
            String recomendacaoPaginas = escolherIntervaloPaginas(resultados);
            Double ultimoAcertoEmRigor = resultados.stream()
                .filter(QuestaoRigorResultado::acertou)
                .map(QuestaoRigorResultado::questao)
                .map(Questao::getRigor)
                .max(Double::compareTo)
                .orElse(null);
            Double ultimoErroEmRigor = resultados.stream()
                .filter(resultado -> !resultado.acertou())
                .map(QuestaoRigorResultado::questao)
                .map(Questao::getRigor)
                .max(Double::compareTo)
                .orElse(null);

            progressaoRigorRepository.upsertProgressaoRigor(
                atual.id(),
                candidatoId,
                disciplinaId,
                subtopico,
                rigorAtualNovo,
                atual.rigorAlvo(),
                ultimoAcertoEmRigor,
                ultimoErroEmRigor,
                atual.tentativasNoNivel() + total,
                acertosConsecutivos,
                errosConsecutivos,
                precisaRevisao,
                recomendacaoLivro,
                recomendacaoPaginas
            );

            recomendacaoRepository.inserirRecomendacaoRigor(
                diagnosticoId,
                subtopico,
                rigorRecomendado,
                rigorAtualNovo,
                taxaAcerto,
                recomendacaoLivro,
                recomendacaoPaginas,
                construirJsonExerciciosSugeridos(resultados),
                precisaRevisao || taxaAcerto < 0.45d
            );
        }
    }

    private ProgressaoRigorAtual carregarProgressaoRigorAtual(
        Connection conn,
        UUID candidatoId,
        UUID disciplinaId,
        String subtopico
    ) throws SQLException {
        String sql = """
            select id,
              rigor_atual,
              rigor_alvo,
              tentativas_no_nivel,
              acertos_consecutivos,
              erros_consecutivos
            from progressao_rigor
            where aluno_id = ? and disciplina_id = ? and lower(coalesce(subtopico, '')) = lower(coalesce(?, ''))
            limit 1
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, candidatoId);
            stmt.setObject(2, disciplinaId);
            stmt.setString(3, subtopico);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ProgressaoRigorAtual(
                        rs.getObject("id", UUID.class),
                        rs.getObject("rigor_atual") instanceof Number number ? number.doubleValue() : 0.12d, // TODO CONFIG_ADAPTATIVA: fallback de rigor atual ainda fixo.
                        rs.getObject("rigor_alvo") instanceof Number number ? number.doubleValue() : 0.7d, // TODO CONFIG_ADAPTATIVA: fallback de rigor alvo ainda fixo.
                        rs.getObject("tentativas_no_nivel") instanceof Number number ? number.intValue() : 0,
                        rs.getObject("acertos_consecutivos") instanceof Number number ? number.intValue() : 0,
                        rs.getObject("erros_consecutivos") instanceof Number number ? number.intValue() : 0
                    );
                }
            }
        }

        return new ProgressaoRigorAtual(null, 0.12d, 0.7d, 0, 0, 0); // TODO CONFIG_ADAPTATIVA: progresso inicial ainda nasce com rigores fixos.
    }

    private String escolherReferenciaLivro(List<QuestaoRigorResultado> resultados) {
        return resultados.stream()
            .filter(resultado -> !resultado.acertou())
            .map(QuestaoRigorResultado::questao)
            .map(Questao::getReferenciaLivro)
            .filter(referencia -> referencia != null && !referencia.isBlank())
            .findFirst()
            .orElseGet(() -> resultados.stream()
                .map(QuestaoRigorResultado::questao)
                .map(Questao::getReferenciaLivro)
                .filter(referencia -> referencia != null && !referencia.isBlank())
                .findFirst()
                .orElse(null)
            );
    }

    private String escolherIntervaloPaginas(List<QuestaoRigorResultado> resultados) {
        List<Questao> base = resultados.stream()
            .filter(resultado -> !resultado.acertou())
            .map(QuestaoRigorResultado::questao)
            .filter(questao -> questao.getPaginaInicio() != null || questao.getPaginaFim() != null)
            .toList();

        if (base.isEmpty()) {
            base = resultados.stream()
                .map(QuestaoRigorResultado::questao)
                .filter(questao -> questao.getPaginaInicio() != null || questao.getPaginaFim() != null)
                .toList();
        }

        if (base.isEmpty()) {
            return null;
        }

        int inicio = base.stream()
            .map(Questao::getPaginaInicio)
            .filter(java.util.Objects::nonNull)
            .min(Integer::compareTo)
            .orElse(base.getFirst().getPaginaInicio() == null ? 0 : base.getFirst().getPaginaInicio());
        int fim = base.stream()
            .map(Questao::getPaginaFim)
            .filter(java.util.Objects::nonNull)
            .max(Integer::compareTo)
            .orElse(base.getFirst().getPaginaFim() == null ? inicio : base.getFirst().getPaginaFim());

        if (inicio <= 0 && fim <= 0) {
            return null;
        }
        if (fim <= 0 || fim == inicio) {
            return "p. " + inicio;
        }
        return "pp. " + inicio + "-" + fim;
    }

    private String construirJsonExerciciosSugeridos(List<QuestaoRigorResultado> resultados) {
        ArrayList<String> ids = resultados.stream()
            .sorted(Comparator.comparing(QuestaoRigorResultado::acertou))
            .map(QuestaoRigorResultado::questao)
            .map(Questao::getId)
            .filter(id -> id != null && !id.isBlank())
            .limit(5)
            .collect(Collectors.toCollection(ArrayList::new));

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < ids.size(); i++) {
            if (i > 0) {
                json.append(",");
            }
            json.append("\"").append(QuestaoUtil.escapeJson(ids.get(i))).append("\"");
        }
        json.append("]");
        return json.toString();
    }

    public StatsDiagnotico statsDiagnotico(){
        double media=0;
        int total=0;
        double totalAcerto=0;
        Value melhoria=new Value("Sem disciplinas",0);
        Value atencao=new Value("Sem disciplinas",100);
        String nome="";
        List<DiagnosticoDto> list = listDiagnotico();
        for (DiagnosticoDto typed : list) {
            if(typed.candidato_id().equals(Authentication.getCurrentUserId())){
                total++;
                media+=typed.evolucao_percentual()/100;
                totalAcerto+=typed.percentual_acerto()/100;
                if(!nome.contains(typed.disciplina_nome())){
                    nome+="-"+typed.disciplina_nome();
                    if (typed.evolucao_percentual()>melhoria.percemt())
                        melhoria=new Value(typed.disciplina_nome(), typed.evolucao_percentual()/100);
                    if (typed.evolucao_percentual()<atencao.percemt())
                        atencao=new Value(typed.disciplina_nome(), typed.evolucao_percentual()/100);

                }else{
                    if (typed.evolucao_percentual()>melhoria.percemt()) {
                        melhoria=new Value(typed.disciplina_nome(), typed.evolucao_percentual()/100);
                    }
                    if (typed.evolucao_percentual()<atencao.percemt())
                        atencao=new Value(typed.disciplina_nome(), typed.evolucao_percentual()/100);

                }
            }


        }
        return new StatsDiagnotico(media/total,totalAcerto/total,total,melhoria,atencao);
    }

    public List<Map<String,?>> statsDisciplina(){
        List<Map<String,?>> list2 = new ArrayList<>();
        final List<DiagnosticoDto> list = listDiagnotico();
        for (DiagnosticoDto typed : list) {
            if(typed.candidato_id().equals(Authentication.getCurrentUserId())){
                list2.add(Map.of("key",typed.disciplina_nome(),"value",new Stats(typed.velocidade() , typed.precisao(), typed.consistencia(), typed.logica(), typed.resiliencia())));
            }
        }
        return list2;
    }

    public TempoStatsDiagnostico statsTempoDiagnotic(){

        final List<DiagnosticoDto> list = listDiagnotico();
        String tempoMedio, tempoMaisRapido,tempoMaisLento;
        String discRapida="";String discLenta="";
        int tot=0;
        int med=0;
        int maior=0;
        int menor=50000;
        for (DiagnosticoDto typed : list) {
            if(typed.candidato_id().equals(Authentication.getCurrentUserId())){
                tot++;
                med+=typed.duracao_segundos();
                discRapida=maior<typed.duracao_segundos()?typed.disciplina_nome():discRapida;
                maior=maior<typed.duracao_segundos()?typed.duracao_segundos():maior;
                discLenta=menor>typed.duracao_segundos()?typed.disciplina_nome():discLenta;
                menor=menor>typed.duracao_segundos()?typed.duracao_segundos():menor;
            }
        }
        tempoMedio=ConversorTempo.formatarDuracao(Math.round(med/tot));
        tempoMaisRapido=ConversorTempo.formatarDuracao(maior);
        tempoMaisLento=ConversorTempo.formatarDuracao(menor);

        return new TempoStatsDiagnostico(tempoMedio,tempoMaisLento,discLenta,tempoMaisRapido,discRapida);
    }

    public StatsQuestaoQtd statsQuestaoQtd(){
        final List<DiagnosticoDto> list = listDiagnotico();
        int tot=0;
        int totErro=0;
        int totAcerto=0;
        for (DiagnosticoDto typed : list) {
            if(typed.candidato_id().equals(Authentication.getCurrentUserId())){

                tot+=typed.total_questoes();
                totErro+=typed.total_erros();
                totAcerto+=typed.total_acertos();
            }
        }
        return new StatsQuestaoQtd(totErro,totAcerto,tot);
    }

    private double calcularProgressoPorRigor(double rigorAtual, double rigorAlvo) {
        if (rigorAlvo <= 0d) {
            return 0d;
        }
        return  QuestaoUtil.limitarPercentualUnitario(rigorAtual / rigorAlvo);
    }

    private double resolverProgressoSubtopico(
        Double progressoRigor,
        Double progressoDiagnostico,
        boolean precisaRevisao,
        boolean precisaNovoDiagnostico
    ) {
        double progresso = 0d;
        boolean temRigor = progressoRigor != null;
        boolean temDiagnostico = progressoDiagnostico != null;

        if (temRigor && temDiagnostico) {
            // O rigor atual mostra o estado mais recente; o diagnostico ancora o ponto de partida.
            progresso = (progressoRigor * 0.65d) + (progressoDiagnostico * 0.35d); // TODO CONFIG_ADAPTATIVA: pesos de combinacao ainda fixos.
        } else if (temRigor) {
            progresso = progressoRigor;
        } else if (temDiagnostico) {
            progresso = progressoDiagnostico;
        }

        if (precisaRevisao || precisaNovoDiagnostico) {
            progresso = Math.min(progresso, 0.58d); // TODO CONFIG_ADAPTATIVA: teto de progresso em revisao ainda fixo.
        }

        return  QuestaoUtil.limitarPercentualUnitario(progresso);
    }

    private List<Topico> construirTopicos(UUID disciplinaId, String disciplina, Collection<Questao> questoes) {
        Map<String, LinkedHashSet<String>> grupos = new LinkedHashMap<>();
        for (Questao questao : questoes) {
            String topico =  QuestaoUtil.safeText(questao.getTopico(), "Geral");
            String subtopico =  QuestaoUtil.safeText(questao.getSubtopico(), topico);
            grupos.computeIfAbsent(topico, ignored -> new LinkedHashSet<>()).add(subtopico);
        }

        ArrayList<Topico> topicos = new ArrayList<>();
        for (Map.Entry<String, LinkedHashSet<String>> entry : grupos.entrySet()) {
            topicos.add(
                new Topico(
                    disciplinaId,
                    disciplina,
                    entry.getKey(),
                    UUID.nameUUIDFromBytes(
                        ( QuestaoUtil.normalizar(disciplina) + ":" +  QuestaoUtil.normalizar(entry.getKey())).getBytes(StandardCharsets.UTF_8)
                    ),
                    entry.getValue().toArray(String[]::new)
                )
            );
        }
        return topicos;
    }

    private Questao mapearQuestao(Map<String, Object> row) {
        Questao questao = new Questao();

        questao.setId(String.valueOf(row.get("id")));
        questao.setDisciplina( QuestaoUtil.formatarDisciplina( QuestaoUtil.safeText(row.get("disciplina"), "GERAL")));
        questao.setTopico( QuestaoUtil.safeText(row.get("topico"), "Geral"));
        questao.setSubtopico( QuestaoUtil.safeText(row.get("subtopico"), questao.getTopico()));
        questao.setTopicoPrincipal( QuestaoUtil.safeText(row.get("topico_principal"), questao.getTopico()));
        questao.setEnunciado( QuestaoUtil.safeText(row.get("questao"), ""));
        questao.setBloco2(null);

        List<String> respostasOriginais = parseJsonStringArray(row.get("respostas"));
        List<Double> pesosOriginais = parseJsonDoubleArray(row.get("pesos_resposta"));
        List<String> respostasCompletasOriginais = completarRespostas(respostasOriginais);
        String textoRespostaCorreta = resolverTextoRespostaCorreta(
            QuestaoUtil.safeText(row.get("resposta_correta"), ""),
            respostasCompletasOriginais,
            pesosOriginais
        );
        List<AlternativaComPeso> alternativasOriginais = normalizarAlternativasComPeso(
            respostasOriginais,
            pesosOriginais,
            textoRespostaCorreta
        );
        List<AlternativaComPeso> alternativasEmbaralhadas = completarAlternativas(
            embaralharAlternativas(alternativasOriginais)
        );
        List<String> respostasNormalizadas = alternativasEmbaralhadas.stream()
            .map(AlternativaComPeso::texto)
            .toList();
        questao.setOpcaoA(respostasNormalizadas.get(0));
        questao.setOpcaoB(respostasNormalizadas.get(1));
        questao.setOpcaoC(respostasNormalizadas.get(2));
        questao.setOpcaoD(respostasNormalizadas.get(3));
        questao.setOpcaoE(respostasNormalizadas.get(4));
        questao.setOpcaoF(respostasNormalizadas.get(5));
        questao.setOpcaoG(respostasNormalizadas.get(6));
        questao.setPesosResposta(alternativasEmbaralhadas.stream().mapToDouble(AlternativaComPeso::pesoAcerto).toArray());

        questao.setRespostaCorreta(QuestaoUtil.resolverRespostaCorreta(textoRespostaCorreta, respostasNormalizadas));
        questao.setNivelDificuldade(mapearNivel( QuestaoUtil.safeText(row.get("dificuldade"), "")));
        questao.setRigor(mapearRigor(row.get("rigor")));
        questao.setReferenciaLivro( QuestaoUtil.safeText(row.get("referencia_livro"), null));
        questao.setPaginaInicio(mapearInteiro(row.get("pagina_inicio")));
        questao.setPaginaFim(mapearInteiro(row.get("pagina_fim")));
        questao.setTempoSugerido(mapearTempoSugerido(questao.getNivelDificuldade()));
        questao.setUsaGrafico(mapearBoolean(row.get("usa_grafico")));
        questao.setGraficoTipoCurva( QuestaoUtil.safeText(row.get("grafico_tipo_curva"), null));
        questao.setGraficoA(mapearDoubleNullable(row.get("grafico_a")));
        questao.setGraficoB(mapearDoubleNullable(row.get("grafico_b")));
        questao.setGraficoC(mapearDoubleNullable(row.get("grafico_c")));
        questao.setGraficoEixoX( QuestaoUtil.safeText(row.get("grafico_eixo_x"), null));
        questao.setGraficoEixoY( QuestaoUtil.safeText(row.get("grafico_eixo_y"), null));
        questao.setGraficoXMin(mapearDoubleNullable(row.get("grafico_x_min")));
        questao.setGraficoXMax(mapearDoubleNullable(row.get("grafico_x_max")));
        questao.setGraficoXTickUnit(mapearDoubleNullable(row.get("grafico_x_tick_unit")));
        return questao;
    }

    private List<AlternativaComPeso> embaralharAlternativas(List<AlternativaComPeso> alternativas) {
        if (alternativas == null || alternativas.isEmpty()) {
            return List.of();
        }

        ArrayList<AlternativaComPeso> embaralhadas = new ArrayList<>(alternativas);
        if (embaralhadas.size() > 1) {
            Collections.shuffle(embaralhadas);
        }
        return embaralhadas;
    }


    private Map<UUID, ProgressoResumo> carregarProgressos(UUID candidatoId) {
        if (candidatoId == null) {
            return Map.of();
        }

        String sql = """
            select disciplina_id, taxa_acerto_geral, nivel_atual
            from progresso_aluno_disciplina
            where aluno_id = ?
            """;

        LinkedHashMap<UUID, ProgressoResumo> progressos = new LinkedHashMap<>();
        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, candidatoId);
            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object disciplinaIdValue = rs.getObject("disciplina_id");
                    if (disciplinaIdValue == null) {
                        continue;
                    }

                    UUID disciplinaId = disciplinaIdValue instanceof UUID uuid
                        ? uuid
                        : UUID.fromString(disciplinaIdValue.toString());
                    Double taxa = rs.getObject("taxa_acerto_geral") instanceof Number number
                        ? number.doubleValue()
                        : null;

                    progressos.put(
                        disciplinaId,
                        new ProgressoResumo(disciplinaId, taxa, rs.getString("nivel_atual"))
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar progresso real do diagnostico: " + e.getMessage());
        }

        return progressos;
    }

    private Map<UUID, HistoricoDiagnosticoResumo> carregarHistoricosPorDisciplinaId(UUID candidatoId) {
        if (candidatoId == null) {
            return Map.of();
        }

        String sql = """
            select distinct on (disciplina_id)
              disciplina_id,
              disciplina_nome,
              percentual_acerto,
              evolucao_percentual,
              nivel,
              total_questoes,
              total_acertos,
              total_erros,
              concluido_em,
              iniciado_em
            from diagnosticos
            where candidato_id = ? and disciplina_id is not null
            order by disciplina_id, coalesce(concluido_em, iniciado_em) desc, criado_em desc
            """;

        LinkedHashMap<UUID, HistoricoDiagnosticoResumo> historicos = new LinkedHashMap<>();
        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, candidatoId);
            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object disciplinaIdValue = rs.getObject("disciplina_id");
                    if (disciplinaIdValue == null) {
                        continue;
                    }

                    UUID disciplinaId = disciplinaIdValue instanceof UUID uuid
                        ? uuid
                        : UUID.fromString(disciplinaIdValue.toString());
                    historicos.put(disciplinaId, mapearHistorico(rs));
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar historico real do diagnostico: " + e.getMessage());
        }

        return historicos;
    }

    private Map<String, HistoricoDiagnosticoResumo> carregarHistoricosPorNome(UUID candidatoId) {
        if (candidatoId == null) {
            return Map.of();
        }

        String sql = """
            select distinct on (disciplina_nome)
              disciplina_id,
              disciplina_nome,
              percentual_acerto,
              evolucao_percentual,
              nivel,
              total_questoes,
              total_acertos,
              total_erros,
              concluido_em,
              iniciado_em
            from diagnosticos
            where candidato_id = ? and disciplina_nome is not null and btrim(disciplina_nome) <> ''
            order by disciplina_nome, coalesce(concluido_em, iniciado_em) desc, criado_em desc
            """;

        LinkedHashMap<String, HistoricoDiagnosticoResumo> historicos = new LinkedHashMap<>();
        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, candidatoId);
            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String disciplinaNome = rs.getString("disciplina_nome");
                    if (disciplinaNome == null || disciplinaNome.isBlank()) {
                        continue;
                    }

                    historicos.put( QuestaoUtil.normalizar(disciplinaNome), mapearHistorico(rs));
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar historico por nome do diagnostico: " + e.getMessage());
        }

        return historicos;
    }

    private HistoricoDiagnosticoResumo mapearHistorico(java.sql.ResultSet rs) throws SQLException {
        Double percentual = rs.getObject("percentual_acerto") instanceof Number number
            ? number.doubleValue()
            : null;
        Double evolucao = rs.getObject("evolucao_percentual") instanceof Number number
            ? number.doubleValue()
            : null;
        Integer totalQuestoes = rs.getObject("total_questoes") instanceof Number number
            ? number.intValue()
            : null;
        Integer totalAcertos = rs.getObject("total_acertos") instanceof Number number
            ? number.intValue()
            : null;
        Integer totalErros = rs.getObject("total_erros") instanceof Number number
            ? number.intValue()
            : null;

        Timestamp concluidoEm = rs.getTimestamp("concluido_em");
        Timestamp iniciadoEm = rs.getTimestamp("iniciado_em");
        LocalDateTime momento = concluidoEm != null
            ? concluidoEm.toLocalDateTime()
            : (iniciadoEm != null ? iniciadoEm.toLocalDateTime() : null);

        return new HistoricoDiagnosticoResumo(
            percentual,
            evolucao,
            rs.getString("nivel"),
            totalQuestoes,
            totalAcertos,
            totalErros,
            momento
        );
    }

    private List<String> completarRespostas(List<String> respostas) {
        ArrayList<String> completas = new ArrayList<>(respostas);
        while (completas.size() < 7) {
            completas.add(switch (completas.size()) {
                case 4 -> "Nao sei";
                case 5 -> "Estou em duvida";
                default -> "Prefiro pular";
            });
        }
        if (completas.size() > 7) {
            return completas.subList(0, 7);
        }
        return completas;
    }

    private List<String> parseJsonStringArray(Object rawValue) {
        if (rawValue == null) {
            return List.of();
        }

        String raw = rawValue.toString();
        ArrayList<String> values = new ArrayList<>();
        Matcher matcher = JSON_STRING_PATTERN.matcher(raw);
        while (matcher.find()) {
            values.add( QuestaoUtil.unescapeJson(matcher.group(1)));
        }
        return values;
    }

    private List<Double> parseJsonDoubleArray(Object rawValue) {
        if (rawValue == null) {
            return List.of();
        }

        String raw = rawValue.toString();
        ArrayList<Double> values = new ArrayList<>();
        Matcher matcher = JSON_NUMBER_PATTERN.matcher(raw);
        while (matcher.find()) {
            try {
                values.add(Double.parseDouble(matcher.group()));
            } catch (NumberFormatException ignored) {
            }
        }
        return values;
    }

    private String resolverTextoRespostaCorreta(
        String respostaCorreta,
        List<String> respostas,
        List<Double> pesosAlternativas
    ) {
        if (respostas == null || respostas.isEmpty()) {
            return "";
        }

        String respostaNormalizada = QuestaoUtil.normalizarTextoLivre(respostaCorreta);
        if (!respostaNormalizada.isBlank()) {
            char primeiraLetra = Character.toUpperCase(respostaCorreta.trim().charAt(0));
            int indice = primeiraLetra - 'A';
            if (indice >= 0 && indice < respostas.size()) {
                return respostas.get(indice);
            }

            for (String resposta : respostas) {
                if (QuestaoUtil.normalizarTextoLivre(resposta).equals(respostaNormalizada)) {
                    return resposta;
                }
            }
        }

        int indicePesoMaximo = indicePesoCorreto(pesosAlternativas, respostas.size());
        if (indicePesoMaximo >= 0) {
            return respostas.get(indicePesoMaximo);
        }

        return QuestaoUtil.resolverTextoRespostaCorreta(respostaCorreta, respostas);
    }

    private List<AlternativaComPeso> normalizarAlternativasComPeso(
        List<String> respostasOriginais,
        List<Double> pesosOriginais,
        String textoRespostaCorreta
    ) {
        if (respostasOriginais == null || respostasOriginais.isEmpty()) {
            return List.of();
        }

        String respostaCorretaNormalizada = QuestaoUtil.normalizarTextoLivre(textoRespostaCorreta);
        ArrayList<AlternativaComPeso> alternativas = new ArrayList<>();

        for (int i = 0; i < respostasOriginais.size(); i++) {
            String texto = QuestaoUtil.safeText(respostasOriginais.get(i), "").trim();
            if (texto.isBlank()) {
                continue;
            }

            boolean correta = !respostaCorretaNormalizada.isBlank()
                && QuestaoUtil.normalizarTextoLivre(texto).equals(respostaCorretaNormalizada);
            double peso = pesoOriginalOuFallback(pesosOriginais, i, correta);
            alternativas.add(new AlternativaComPeso(texto, peso));
        }

        return alternativas;
    }

    private List<AlternativaComPeso> completarAlternativas(List<AlternativaComPeso> alternativas) {
        ArrayList<AlternativaComPeso> completas = new ArrayList<>(alternativas);
        while (completas.size() < 7) {
            completas.add(switch (completas.size()) {
                case 4 -> new AlternativaComPeso("Nao sei", 0.10d);
                case 5 -> new AlternativaComPeso("Estou em duvida", 0.20d);
                default -> new AlternativaComPeso("Prefiro pular", 0d);
            });
        }
        if (completas.size() > 7) {
            return new ArrayList<>(completas.subList(0, 7));
        }
        return completas;
    }

    private int indicePesoCorreto(List<Double> pesosAlternativas, int limiteRespostas) {
        if (pesosAlternativas == null || pesosAlternativas.isEmpty() || limiteRespostas <= 0) {
            return -1;
        }

        int indice = -1;
        for (int i = 0; i < pesosAlternativas.size() && i < limiteRespostas; i++) {
            Double peso = pesosAlternativas.get(i);
            if (peso == null || peso < 0.999d) {
                continue;
            }
            if (indice >= 0) {
                return -1;
            }
            indice = i;
        }
        return indice;
    }

    private double pesoOriginalOuFallback(List<Double> pesosOriginais, int indice, boolean correta) {
        if (correta) {
            return 1d;
        }

        if (pesosOriginais != null && indice >= 0 && indice < pesosOriginais.size()) {
            double peso = pesosOriginais.get(indice) == null ? 0.25d : pesosOriginais.get(indice);
            return Math.max(0d, Math.min(0.95d, peso));
        }

        return 0.25d;
    }


    private int mapearNivel(String dificuldade) {
        return NivelDificuldadeAdaptativa.fromTexto(dificuldade).nivel();
    }

    private double mapearTempoSugerido(int nivel) {
        return NivelDificuldadeAdaptativa.fromNivel(nivel).tempoSugeridoSegundos();
    }

    private double mapearRigor(Object rawValue) {
        if (rawValue instanceof Number number) {
            return Math.max(0d, Math.min(1d, number.doubleValue()));
        }
        return 0.5d; // TODO CONFIG_ADAPTATIVA: fallback de rigor da questao ainda fixo quando a origem nao traz valor.
    }

    private Integer mapearInteiro(Object rawValue) {
        if (rawValue instanceof Number number) {
            return number.intValue();
        }
        return null;
    }

    private Double mapearDoubleNullable(Object rawValue) {
        if (rawValue instanceof Number number) {
            double value = number.doubleValue();
            return Double.isFinite(value) ? value : null;
        }
        if (rawValue instanceof String text && !text.isBlank()) {
            try {
                double value = Double.parseDouble(text.trim());
                return Double.isFinite(value) ? value : null;
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        return null;
    }

    private boolean mapearBoolean(Object rawValue) {
        if (rawValue instanceof Boolean bool) {
            return bool;
        }
        if (rawValue instanceof Number number) {
            return number.intValue() != 0;
        }
        if (rawValue instanceof String text && !text.isBlank()) {
            return Boolean.parseBoolean(text.trim());
        }
        return false;
    }

    private double resolverIndicadorPrincipal(
        int totalQuestoes,
        ProgressoResumo progresso,
        HistoricoDiagnosticoResumo historico
    ) {
        if (historico != null && historico.percentualAcerto() != null) {
            return  QuestaoUtil.limitarPercentual(historico.percentualAcerto());
        }
        if (progresso != null && progresso.taxaAcertoGeral() != null) {
            double taxa = progresso.taxaAcertoGeral();
            if (taxa > 1d) {
                return Math.min(1d, taxa / 100d);
            }
            return Math.max(0d, Math.min(1d, taxa));
        }
        return Math.min(1d, totalQuestoes / 12d);
    }

    private String montarResumoHistorico(HistoricoDiagnosticoResumo historico, int totalQuestoesBanco) {
        String data = historico.momento() == null ? "" : " em " + historico.momento().format(DATA_RESUMIDA);
        if (historico.totalQuestoes() != null && historico.totalQuestoes() > 0) {
            return historico.totalQuestoes() + " questoes no ultimo diagnostico" + data
                + " • banco atual com " + totalQuestoesBanco + ".";
        }
        return "Historico encontrado" + data + " • banco atual com " + totalQuestoesBanco + " questoes.";
    }

    private String montarObservacaoHistorico(HistoricoDiagnosticoResumo historico) {
        StringBuilder texto = new StringBuilder("Ultimo desempenho real");
        if (historico.totalAcertos() != null && historico.totalQuestoes() != null && historico.totalQuestoes() > 0) {
            texto.append(": ").append(historico.totalAcertos()).append("/")
                .append(historico.totalQuestoes()).append(" acertos");
        }
        if (historico.nivel() != null && !historico.nivel().isBlank()) {
            texto.append(" • nivel ").append(historico.nivel());
        }
        if (historico.momento() != null) {
            texto.append(" • ").append(historico.momento().format(DATA_RESUMIDA));
        }
        texto.append(".");
        return texto.toString();
    }

    private String chaveSubtopico(String disciplina, String subtopico) {
        return  QuestaoUtil.normalizar(disciplina) + "::" +  QuestaoUtil.normalizar( QuestaoUtil.safeText(subtopico, "Geral"));
    }

    private record AlternativaComPeso(String texto, double pesoAcerto) {
    }

}
