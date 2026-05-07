package com.imetro.services;

import com.imetro.domain.CacheService;
import com.imetro.domain.dto.Stats;
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
import com.imetro.domain.enums.NivelDisciplina;
import com.imetro.persistence.repository.DiagnosticoRepository;
import com.imetro.persistence.repository.JdbcBasicSqlRepository;
import com.imetro.persistence.repository.PerguntasRepository;
import com.imetro.ui.model.Questao;
import com.imetro.util.AppLogger;
import com.imetro.util.Authentication;
import com.imetro.util.ConverterSegundoMinutos;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DiagnosticoService {

    private static final Pattern JSON_STRING_PATTERN = Pattern.compile("\"((?:\\\\.|[^\"\\\\])*)\"");
    private static final DateTimeFormatter DATA_RESUMIDA = DateTimeFormatter.ofPattern("dd/MM");

    private final PerguntasRepository perguntasRepository;
    private final DisciplinaService disciplinaService;
    private final DiagnosticoRepository diagnosticoRepository = new DiagnosticoRepository();
    private final PerguntasBootstrapService perguntasBootstrapService;

    public DiagnosticoService() {
        this.perguntasRepository = new PerguntasRepository();
        this.disciplinaService = new DisciplinaService();
        this.perguntasBootstrapService = new PerguntasBootstrapService();
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

    public List<Questao> carregarQuestoesReais(UUID candidatoId) {
        if (!PerguntasBootstrapAsyncService.getInstance().isRunningFor(candidatoId)) {
            sincronizarDisciplinasAutomaticas(candidatoId);
        }
        return carregarQuestoesReais();
    }

    public List<Topico> carregarTopicosPorDisciplina(String disciplina) {
        String disciplinaNormalizada = normalizar(disciplina);
        List<Questao> questoesDisciplina = carregarQuestoesReais().stream()
            .filter(questao -> disciplinaNormalizada.equals(normalizar(questao.getDisciplina())))
            .collect(Collectors.toCollection(ArrayList::new));

        if (questoesDisciplina.isEmpty()) {
            return List.of();
        }

        String nomeDisciplina = questoesDisciplina.getFirst().getDisciplina();
        UUID disciplinaId = resolverDisciplinaId(nomeDisciplina);
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
                            progressoPorDiagnostico.put(chave, limitarPercentualUnitario(number.doubleValue()));
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

    public List<PerguntasBootstrapService.BootstrapResult> sincronizarDisciplinasAutomaticas(UUID candidatoId) {
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
        List<Questao> questoes = carregarQuestoesReais(candidatoId);
        List<DisciplinaDto> disciplinas = diagnosticoRepository.carregarDisciplinasAtivasDoCandidato(candidatoId);
        if (disciplinas.isEmpty()) {
            disciplinas = DisciplinaService.discCategoria();
        }

        Map<String, List<Questao>> questoesPorDisciplina = questoes.stream()
            .collect(Collectors.groupingBy(
                questao -> normalizar(questao.getDisciplina()),
                LinkedHashMap::new,
                Collectors.toCollection(ArrayList::new)
            ));

        ArrayList<Topico> topicos = new ArrayList<>();
        ArrayList<String> disciplinasSemBase = new ArrayList<>();
        int totalQuestoes = 0;
        int totalDisciplinas = 0;

        for (DisciplinaDto disciplina : disciplinas) {
            List<Questao> questoesDisciplina = questoesPorDisciplina.get(normalizar(disciplina.nome()));
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
        List<Questao> questoes = carregarQuestoesReais(candidatoId);
        if (questoes.isEmpty()) {
            return List.of();
        }

        List<DisciplinaDto> disciplinasAtivas = diagnosticoRepository.carregarDisciplinasAtivasDoCandidato(candidatoId);
        Set<String> disciplinasPermitidas = disciplinasAtivas.stream()
            .map(DisciplinaDto::nome)
            .map(this::normalizar)
            .collect(Collectors.toCollection(LinkedHashSet::new));

        Map<String, DisciplinaDto> disciplinasPorNome = disciplinaService.discCategoria().stream()
            .filter(disciplina -> disciplina.id() != null)
            .collect(Collectors.toMap(
                disciplina -> normalizar(disciplina.nome()),
                disciplina -> disciplina,
                (left, right) -> left,
                LinkedHashMap::new
            ));

        Map<UUID, ProgressoResumo> progressosPorDisciplina = carregarProgressos(candidatoId);
        Map<UUID, HistoricoDiagnosticoResumo> historicosPorDisciplinaId = carregarHistoricosPorDisciplinaId(candidatoId);
        Map<String, HistoricoDiagnosticoResumo> historicosPorNome = carregarHistoricosPorNome(candidatoId);

        Map<String, List<Questao>> questoesPorDisciplina = questoes.stream()
            .collect(Collectors.groupingBy(
                questao -> normalizar(questao.getDisciplina()),
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
                : resolverDisciplinaId(disciplinaDetectada);
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
                ? formatarPercentual(historico.percentualAcerto()) + " de acerto"
                : "Base pronta";
            String resumo = historico != null
                ? montarResumoHistorico(historico, totalQuestoes)
                : totalQuestoes + " questoes reais prontas para o primeiro diagnostico.";
            String tendencia = historico != null && historico.evolucaoPercentual() != null
                ? formatarDelta(historico.evolucaoPercentual())
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
                .computeIfAbsent(normalizar(questao.getDisciplina()), ignored -> new ArrayList<>())
                .add(i);
        }

        if (indicesPorDisciplina.isEmpty()) {
            return;
        }

        int duracaoSegundos = parseTempoEmSegundos(tempoFormatado);
        LocalDateTime concluidoEm = LocalDateTime.now();

        try (Connection conn = JdbcBasicSqlRepository.openRequiredConnection()) {
            conn.setAutoCommit(false);
            try {
                for (Map.Entry<String, ArrayList<Integer>> entry : indicesPorDisciplina.entrySet()) {
                    ArrayList<Integer> indices = entry.getValue();
                    Questao questaoBase = questoes.get(indices.getFirst());
                    String nomeDisciplina = formatarDisciplina(questaoBase.getDisciplina());
                    UUID disciplinaId = resolverDisciplinaId(nomeDisciplina);

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
                    String nivel = resolverNivelDiagnostico(percentualAcerto);
                    double precisao = Math.max(0d, Math.min(1d, percentualAcerto / 100d));
                    double consistencia = precisao;
                    double velocidade = calcularVelocidade(duracaoSegundos, totalQuestoes);

                    UUID diagnosticoId = diagnosticoRepository.inserir(
                        conn,
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
                        consistencia,
                        construirJsonRespostas(indices, questoes, respostasUsuario),
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

            String topicoBase = safeText(
                questao.getTopicoPrincipal(),
                safeText(questao.getTopico(), safeText(questao.getSubtopico(), "Geral"))
            );
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

            double rigorAtualNovo = calcularNovoRigor(atual.rigorAtual(), atual.rigorAlvo(), rigorMedioTentado, taxaAcerto);
            int acertosConsecutivos = taxaAcerto >= 0.8 ? atual.acertosConsecutivos() + acertos : 0;
            int errosConsecutivos = taxaAcerto < 0.5 ? atual.errosConsecutivos() + erros : 0;
            boolean precisaRevisao = taxaAcerto < 0.6 || errosConsecutivos >= 2;
            double rigorRecomendado = precisaRevisao
                ? Math.max(0.05d, rigorAtualNovo - 0.04d)
                : Math.min(atual.rigorAlvo(), Math.max(rigorAtualNovo, rigorMedioTentado) + 0.06d);

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

            upsertProgressaoRigor(
                conn,
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

            inserirRecomendacaoRigor(
                conn,
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
                        rs.getObject("rigor_atual") instanceof Number number ? number.doubleValue() : 0.12d,
                        rs.getObject("rigor_alvo") instanceof Number number ? number.doubleValue() : 0.7d,
                        rs.getObject("tentativas_no_nivel") instanceof Number number ? number.intValue() : 0,
                        rs.getObject("acertos_consecutivos") instanceof Number number ? number.intValue() : 0,
                        rs.getObject("erros_consecutivos") instanceof Number number ? number.intValue() : 0
                    );
                }
            }
        }

        return new ProgressaoRigorAtual(null, 0.12d, 0.7d, 0, 0, 0);
    }

    private void upsertProgressaoRigor(
        Connection conn,
        UUID idAtual,
        UUID candidatoId,
        UUID disciplinaId,
        String subtopico,
        double rigorAtual,
        double rigorAlvo,
        Double ultimoAcertoEmRigor,
        Double ultimoErroEmRigor,
        int tentativasNoNivel,
        int acertosConsecutivos,
        int errosConsecutivos,
        boolean precisaRevisao,
        String recomendacaoLivro,
        String recomendacaoPaginas
    ) throws SQLException {
        String sql = """
            insert into progressao_rigor (
              id,
              aluno_id,
              disciplina_id,
              subtopico,
              rigor_atual,
              rigor_alvo,
              ultimo_acerto_em_rigor,
              ultimo_erro_em_rigor,
              tentativas_no_nivel,
              acertos_consecutivos,
              erros_consecutivos,
              precisa_revisao,
              recomendacao_livro,
              recomendacao_paginas,
              atualizado_em
            ) values (
              ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now()
            )
            on conflict (aluno_id, disciplina_id, subtopico) do update
            set rigor_atual = excluded.rigor_atual,
                rigor_alvo = excluded.rigor_alvo,
                ultimo_acerto_em_rigor = excluded.ultimo_acerto_em_rigor,
                ultimo_erro_em_rigor = excluded.ultimo_erro_em_rigor,
                tentativas_no_nivel = excluded.tentativas_no_nivel,
                acertos_consecutivos = excluded.acertos_consecutivos,
                erros_consecutivos = excluded.erros_consecutivos,
                precisa_revisao = excluded.precisa_revisao,
                recomendacao_livro = excluded.recomendacao_livro,
                recomendacao_paginas = excluded.recomendacao_paginas,
                atualizado_em = now()
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, idAtual == null ? UUID.randomUUID() : idAtual);
            stmt.setObject(2, candidatoId);
            stmt.setObject(3, disciplinaId);
            stmt.setString(4, subtopico);
            stmt.setDouble(5, limitarRigor(rigorAtual));
            stmt.setDouble(6, limitarRigor(rigorAlvo));
            stmt.setObject(7, ultimoAcertoEmRigor == null ? null : limitarRigor(ultimoAcertoEmRigor));
            stmt.setObject(8, ultimoErroEmRigor == null ? null : limitarRigor(ultimoErroEmRigor));
            stmt.setInt(9, Math.max(0, tentativasNoNivel));
            stmt.setInt(10, Math.max(0, acertosConsecutivos));
            stmt.setInt(11, Math.max(0, errosConsecutivos));
            stmt.setBoolean(12, precisaRevisao);
            stmt.setString(13, recomendacaoLivro);
            stmt.setString(14, recomendacaoPaginas);
            stmt.executeUpdate();
        }
    }

    private void inserirRecomendacaoRigor(
        Connection conn,
        UUID diagnosticoId,
        String subtopico,
        double rigorRecomendado,
        double nivelAtual,
        Double progressoAtingido,
        String recomendacaoLivro,
        String recomendacaoPaginas,
        String exerciciosSugeridosJson,
        boolean precisaNovoDiagnostico
    ) throws SQLException {
        String sql = """
            insert into recomendacoes_rigor (
              id,
              diagnostico_id,
              subtopico,
              rigor_recomendado,
              nivel_atual,
              progresso_atingido,
              recomendacao_livro,
              recomendacao_paginas,
              exercicios_sugeridos,
              precisa_novo_diagnostico,
              criado_em
            ) values (
              ?, ?, ?, ?, ?, ?, ?, ?, cast(? as jsonb), ?, now()
            )
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, UUID.randomUUID());
            stmt.setObject(2, diagnosticoId);
            stmt.setString(3, subtopico);
            stmt.setDouble(4, limitarRigor(rigorRecomendado));
            stmt.setDouble(5, limitarRigor(nivelAtual));
            stmt.setObject(6, progressoAtingido == null ? null : Math.max(0d, Math.min(1d, progressoAtingido)));
            stmt.setString(7, recomendacaoLivro);
            stmt.setString(8, recomendacaoPaginas);
            stmt.setString(9, exerciciosSugeridosJson);
            stmt.setBoolean(10, precisaNovoDiagnostico);
            stmt.executeUpdate();
        }
    }

    private double calcularNovoRigor(double rigorAtual, double rigorAlvo, double rigorMedioTentado, double taxaAcerto) {
        double base = Math.max(rigorAtual, rigorMedioTentado);
        if (taxaAcerto >= 0.85d) {
            return Math.min(rigorAlvo, base + 0.08d);
        }
        if (taxaAcerto >= 0.65d) {
            return Math.min(rigorAlvo, base + 0.03d);
        }
        if (taxaAcerto <= 0.35d) {
            return Math.max(0.05d, Math.min(rigorAtual, rigorMedioTentado) - 0.08d);
        }
        return Math.max(0.05d, Math.min(rigorAtual, rigorMedioTentado) - 0.03d);
    }

    private double limitarRigor(double rigor) {
        return Math.max(0d, Math.min(1d, rigor));
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
            json.append("\"").append(escapeJson(ids.get(i))).append("\"");
        }
        json.append("]");
        return json.toString();
    }


    private String resolverNivelDiagnostico(double percentualAcerto) {
        if (percentualAcerto >= 85d) {
            return "EXPERT";
        }
        if (percentualAcerto >= 65d) {
            return "AVANCADO";
        }
        if (percentualAcerto >= 40d) {
            return "INTERMEDIARIO";
        }
        return "INICIANTE";
    }

    public StatsDiagnotico statsDiagnotico(){
        double media=0;
        int total=0;
        double totalAcerto=0;
        Value melhoria=new Value("Sem disciplinas",0);
        Value atencao=new Value("Sem disciplinas",0);
        String nome="";
        List<DiagnosticoDto> list = listDiagnotico();
        for (DiagnosticoDto typed : list) {
            if(typed.candidato_id().equals(Authentication.getCurrentUserId())){
                total++;
                media+=typed.evolucao_percentual();
                totalAcerto+=typed.percentual_acerto()/100;
                if(!nome.contains(typed.disciplina_nome())){
                    nome+="-"+typed.disciplina_nome();
                    if (typed.evolucao_percentual()>melhoria.percemt())
                        melhoria=new Value(typed.disciplina_nome(), typed.evolucao_percentual());
                    if (typed.evolucao_percentual()<atencao.percemt())
                        atencao=new Value(typed.disciplina_nome(), typed.evolucao_percentual());

                }else{
                    if (typed.evolucao_percentual()>melhoria.percemt()) {
                        melhoria=new Value(typed.disciplina_nome(), typed.evolucao_percentual());
                    }
                    if (typed.evolucao_percentual()<atencao.percemt())
                        atencao=new Value(typed.disciplina_nome(), typed.evolucao_percentual());

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
        tempoMedio=ConverterSegundoMinutos.formatarDuracao(Math.round(med/tot));
        tempoMaisRapido=ConverterSegundoMinutos.formatarDuracao(maior);
        tempoMaisLento=ConverterSegundoMinutos.formatarDuracao(menor);

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

    private double calcularVelocidade(int duracaoSegundos, int totalQuestoes) {
        if (duracaoSegundos <= 0 || totalQuestoes <= 0) {
            return 0.5d;
        }
        double mediaPorQuestao = duracaoSegundos / (double) totalQuestoes;
        double normalizado = 1d - (mediaPorQuestao / 120d);
        return Math.max(0d, Math.min(1d, normalizado));
    }

    private double calcularProgressoPorRigor(double rigorAtual, double rigorAlvo) {
        if (rigorAlvo <= 0d) {
            return 0d;
        }
        return limitarPercentualUnitario(rigorAtual / rigorAlvo);
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
            progresso = (progressoRigor * 0.65d) + (progressoDiagnostico * 0.35d);
        } else if (temRigor) {
            progresso = progressoRigor;
        } else if (temDiagnostico) {
            progresso = progressoDiagnostico;
        }

        if (precisaRevisao || precisaNovoDiagnostico) {
            progresso = Math.min(progresso, 0.58d);
        }

        return limitarPercentualUnitario(progresso);
    }

    private String construirJsonRespostas(
        List<Integer> indices,
        List<Questao> questoes,
        List<Character> respostasUsuario
    ) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < indices.size(); i++) {
            int indice = indices.get(i);
            Questao questao = questoes.get(indice);
            char marcada = respostasUsuario.get(indice);
            boolean acertou = marcada == questao.getRespostaCorreta();

            if (i > 0) {
                json.append(", ");
            }

            json.append("{")
                .append("\"questaoId\":\"").append(escapeJson(safeText(questao.getId(), ""))).append("\",")
                .append("\"topico\":\"").append(escapeJson(safeText(questao.getTopico(), ""))).append("\",")
                .append("\"subtopico\":\"").append(escapeJson(safeText(questao.getSubtopico(), ""))).append("\",")
                .append("\"marcada\":\"").append(marcada).append("\",")
                .append("\"correta\":\"").append(questao.getRespostaCorreta()).append("\",")
                .append("\"acertou\":").append(acertou)
                .append("}");
        }
        json.append("]");
        return json.toString();
    }

    private int parseTempoEmSegundos(String tempoFormatado) {
        if (tempoFormatado == null || tempoFormatado.isBlank()) {
            return 0;
        }

        String[] partes = tempoFormatado.split(":");
        try {
            return switch (partes.length) {
                case 3 -> (Integer.parseInt(partes[0]) * 3600)
                    + (Integer.parseInt(partes[1]) * 60)
                    + Integer.parseInt(partes[2]);
                case 2 -> (Integer.parseInt(partes[0]) * 60) + Integer.parseInt(partes[1]);
                default -> Integer.parseInt(tempoFormatado.trim());
            };
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String escapeJson(String valor) {
        return valor
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\r", "\\r")
            .replace("\n", "\\n")
            .replace("\t", "\\t");
    }

    private List<Topico> construirTopicos(UUID disciplinaId, String disciplina, Collection<Questao> questoes) {
        Map<String, LinkedHashSet<String>> grupos = new LinkedHashMap<>();
        for (Questao questao : questoes) {
            String topico = safeText(questao.getTopico(), "Geral");
            String subtopico = safeText(questao.getSubtopico(), topico);
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
                        (normalizar(disciplina) + ":" + normalizar(entry.getKey())).getBytes(StandardCharsets.UTF_8)
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
        questao.setDisciplina(formatarDisciplina(safeText(row.get("disciplina"), "GERAL")));
        questao.setTopico(safeText(row.get("topico"), "Geral"));
        questao.setSubtopico(safeText(row.get("subtopico"), questao.getTopico()));
        questao.setTopicoPrincipal(safeText(row.get("topico_principal"), questao.getTopico()));
        questao.setEnunciado(safeText(row.get("questao"), ""));
        questao.setBloco2(null);

        List<String> respostasOriginais = parseJsonStringArray(row.get("respostas"));
        List<String> respostasCompletasOriginais = completarRespostas(respostasOriginais);
        String textoRespostaCorreta = resolverTextoRespostaCorreta(
            safeText(row.get("resposta_correta"), ""),
            respostasCompletasOriginais
        );
        List<String> respostasEmbaralhadas = completarRespostas(embaralharAlternativas(respostasOriginais));
        List<String> respostasNormalizadas = respostasEmbaralhadas;
        questao.setOpcaoA(respostasNormalizadas.get(0));
        questao.setOpcaoB(respostasNormalizadas.get(1));
        questao.setOpcaoC(respostasNormalizadas.get(2));
        questao.setOpcaoD(respostasNormalizadas.get(3));
        questao.setOpcaoE(respostasNormalizadas.get(4));
        questao.setOpcaoF(respostasNormalizadas.get(5));
        questao.setOpcaoG(respostasNormalizadas.get(6));

        questao.setRespostaCorreta(resolverRespostaCorreta(textoRespostaCorreta, respostasNormalizadas));
        questao.setNivelDificuldade(mapearNivel(safeText(row.get("dificuldade"), "")));
        questao.setRigor(mapearRigor(row.get("rigor")));
        questao.setReferenciaLivro(safeText(row.get("referencia_livro"), null));
        questao.setPaginaInicio(mapearInteiro(row.get("pagina_inicio")));
        questao.setPaginaFim(mapearInteiro(row.get("pagina_fim")));
        questao.setTempoSugerido(mapearTempoSugerido(questao.getNivelDificuldade()));
        return questao;
    }

    private List<String> embaralharAlternativas(List<String> alternativas) {
        if (alternativas == null || alternativas.isEmpty()) {
            return List.of();
        }

        ArrayList<String> embaralhadas = new ArrayList<>(alternativas);
        if (embaralhadas.size() > 1) {
            Collections.shuffle(embaralhadas);
        }
        return embaralhadas;
    }

    private String resolverTextoRespostaCorreta(String respostaCorreta, List<String> respostas) {
        if (respostas == null || respostas.isEmpty()) {
            return "";
        }

        char letraCorreta = resolverRespostaCorreta(respostaCorreta, respostas);
        return resolverTextoOpcao(respostas, letraCorreta);
    }

    private String resolverTextoOpcao(List<String> respostas, char letra) {
        int indice = Character.toUpperCase(letra) - 'A';
        if (indice < 0 || indice >= respostas.size()) {
            return respostas.getFirst();
        }
        return respostas.get(indice);
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

                    historicos.put(normalizar(disciplinaNome), mapearHistorico(rs));
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
            values.add(unescapeJson(matcher.group(1)));
        }
        return values;
    }

    private char resolverRespostaCorreta(String respostaCorreta, List<String> respostas) {
        if (respostaCorreta != null && !respostaCorreta.isBlank()) {
            char primeiraLetra = Character.toUpperCase(respostaCorreta.trim().charAt(0));
            if (primeiraLetra >= 'A' && primeiraLetra <= 'G') {
                return primeiraLetra;
            }

            String normalizada = normalizarTextoLivre(respostaCorreta);
            for (int i = 0; i < respostas.size(); i++) {
                if (normalizarTextoLivre(respostas.get(i)).equals(normalizada)) {
                    return (char) ('A' + i);
                }
            }
        }
        return 'A';
    }

    private int mapearNivel(String dificuldade) {
        return switch (normalizar(dificuldade)) {
            case "facil" -> 1;
            case "medio" -> 2;
            case "desafiante" -> 3;
            case "extra" -> 4;
            default -> 2;
        };
    }

    private double mapearTempoSugerido(int nivel) {
        return switch (nivel) {
            case 1 -> 40d;
            case 2 -> 55d;
            case 3 -> 70d;
            case 4 -> 85d;
            default -> 60d;
        };
    }

    private double mapearRigor(Object rawValue) {
        if (rawValue instanceof Number number) {
            return Math.max(0d, Math.min(1d, number.doubleValue()));
        }
        return 0.5d;
    }

    private Integer mapearInteiro(Object rawValue) {
        if (rawValue instanceof Number number) {
            return number.intValue();
        }
        return null;
    }

    private double resolverIndicadorPrincipal(
        int totalQuestoes,
        ProgressoResumo progresso,
        HistoricoDiagnosticoResumo historico
    ) {
        if (historico != null && historico.percentualAcerto() != null) {
            return limitarPercentual(historico.percentualAcerto());
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
        return normalizar(disciplina) + "::" + normalizar(safeText(subtopico, "Geral"));
    }

    private UUID resolverDisciplinaId(String disciplina) {
        String disciplinaNormalizada = normalizar(disciplina);
        for (DisciplinaDto disciplinaDto : disciplinaService.discCategoria()) {
            if (disciplinaDto.id() != null && normalizar(disciplinaDto.nome()).equals(disciplinaNormalizada)) {
                return disciplinaDto.id();
            }
        }

        return UUID.nameUUIDFromBytes(("disciplina:" + disciplinaNormalizada).getBytes(StandardCharsets.UTF_8));
    }

    private String formatarDisciplina(String valor) {
        return switch (normalizar(valor)) {
            case "matematica" -> "Matematica";
            case "portugues" -> "Portugues";
            case "fisica" -> "Fisica";
            case "quimica" -> "Quimica";
            case "biologia" -> "Biologia";
            case "raciocinio logico" -> "Raciocinio Logico";
            default -> toTitleCase(valor);
        };
    }

    private String toTitleCase(String valor) {
        String[] partes = safeText(valor, "").trim().split("\\s+");
        StringBuilder texto = new StringBuilder();
        for (String parte : partes) {
            if (parte.isBlank()) {
                continue;
            }
            if (texto.length() > 0) {
                texto.append(' ');
            }
            texto.append(parte.substring(0, 1).toUpperCase(Locale.ROOT));
            if (parte.length() > 1) {
                texto.append(parte.substring(1).toLowerCase(Locale.ROOT));
            }
        }
        return texto.isEmpty() ? valor : texto.toString();
    }

    private String formatarPercentual(double percentual) {
        return Math.round(percentual) + "%";
    }

    private String formatarDelta(double valor) {
        long arredondado = Math.round(valor);
        return (arredondado > 0 ? "+" : "") + arredondado + "%";
    }

    private double limitarPercentual(double percentual) {
        return Math.max(0d, Math.min(1d, percentual / 100d));
    }

    private double limitarPercentualUnitario(double valor) {
        return Math.max(0d, Math.min(1d, valor));
    }

    private String safeText(Object value, String defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        String text = value.toString().trim();
        return text.isEmpty() ? defaultValue : text;
    }

    private String normalizar(String valor) {
        return normalizarTextoLivre(safeText(valor, ""));
    }

    private String normalizarTextoLivre(String valor) {
        String semAcento = Normalizer.normalize(valor == null ? "" : valor, Normalizer.Form.NFD)
            .replaceAll("\\p{M}+", "");
        return semAcento.trim().toLowerCase(Locale.ROOT);
    }

    private String unescapeJson(String value) {
        StringBuilder out = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            if (ch != '\\' || i + 1 >= value.length()) {
                out.append(ch);
                continue;
            }

            char next = value.charAt(++i);
            switch (next) {
                case '"' -> out.append('"');
                case '\\' -> out.append('\\');
                case '/' -> out.append('/');
                case 'b' -> out.append('\b');
                case 'f' -> out.append('\f');
                case 'n' -> out.append('\n');
                case 'r' -> out.append('\r');
                case 't' -> out.append('\t');
                case 'u' -> {
                    if (i + 4 >= value.length()) {
                        out.append("\\u");
                        break;
                    }
                    String hex = value.substring(i + 1, i + 5);
                    out.append((char) Integer.parseInt(hex, 16));
                    i += 4;
                }
                default -> out.append(next);
            }
        }
        return out.toString();
    }


}
