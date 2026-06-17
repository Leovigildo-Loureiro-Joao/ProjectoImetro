package com.imetro.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.imetro.domain.dto.configuracao.ConfiguracaoTesteAdaptativoDto;
import com.imetro.domain.dto.configuracao.ConfiguracaoDto;
import com.imetro.domain.dto.configuracao.ConfiguracaoTesteAdaptativoNivelDto;
import com.imetro.domain.dto.bolsa.BolsaDto;
import com.imetro.domain.dto.diagnostico.DiagnosticoDto;
import com.imetro.domain.dto.progresso.ProgressoAlunoDisciplinaDto;
import com.imetro.domain.dto.stats.StatsProgress;
import com.imetro.domain.dto.stats.Teste_Stat;
import com.imetro.domain.dto.test.Percent;
import com.imetro.domain.dto.test.TestDtoAll;
import com.imetro.domain.dto.test.ErrosComuns;
import com.imetro.domain.dto.test.Melhorias;
import com.imetro.domain.dto.test.ReacaoTeste;
import com.imetro.domain.dto.test.Teste_Pergunta;
import com.imetro.domain.dto.test.TrilhaAdaptacaoSubtopico;
import com.imetro.domain.enums.NivelDificuldadeAdaptativa;
import com.imetro.persistence.repository.ConfiguracoesTesteAdaptativoRespository;
import com.imetro.persistence.repository.ConfiguracaoTesteAdaptativoNivelRepositorty;
import com.imetro.persistence.repository.ConfiguracoesRepository;
import com.imetro.persistence.repository.DiagnosticoRepository;
import com.imetro.persistence.repository.JdbcBasicSqlRepository;
import com.imetro.persistence.repository.MedalhaRepository;
import com.imetro.persistence.repository.ProgressoALunoDisciplinaRepository;
import com.imetro.persistence.repository.ScoreBolsaRepository;
import com.imetro.persistence.repository.TestePerguntasRepository;
import com.imetro.persistence.repository.TesteRepository;
import com.imetro.persistence.repository.TesteStatsRepository;
import com.imetro.ui.controller.candidato.testes.TesteAdaptativoCoordinator.TesteConfig;
import com.imetro.ui.model.Questao;
import com.imetro.util.Authentication;
import com.imetro.util.CalculoStats;
import com.imetro.util.ConversorTempo;
import com.imetro.util.ParseObject;
import com.imetro.util.QuestaoUtil;

public class TesteService {
    private static final String SQL_NORMALIZE_FROM = "\u00e1\u00e0\u00e2\u00e3\u00e4\u00e9\u00e8\u00ea\u00eb\u00ed\u00ec\u00ee\u00ef\u00f3\u00f2\u00f4\u00f5\u00f6\u00fa\u00f9\u00fb\u00fc\u00e7";
    private static final String SQL_NORMALIZE_TO = "aaaaaeeeeiiiiooooouuuuc";

    private final TesteRepository testeRepository;
    private final TesteStatsRepository testeStatsRepository;
    private final TestePerguntasRepository testePerguntasRepository;
    private final ScoreBolsaRepository scoreBolsaRepository;
    private final DiagnosticoRepository diagnosticoRepository;
    private final ProgressoALunoDisciplinaRepository progressoALunoDisciplinaRepository;
    private final ConfiguracaoTesteAdaptativoNivelRepositorty configuracaoTesteAdaptativoNivelRepositorty;
    private final ConfiguracoesRepository configuracoesRepository;
    private final ConfiguracoesTesteAdaptativoRespository adaptacaoRepository;
    private final MedalhaRepository medalhaRepository;

    public TesteService() {
        this.testeRepository = new TesteRepository();
        this.testeStatsRepository = new TesteStatsRepository();
        this.testePerguntasRepository = new TestePerguntasRepository();
        this.scoreBolsaRepository = new ScoreBolsaRepository();
        this.diagnosticoRepository=new DiagnosticoRepository();
        this.progressoALunoDisciplinaRepository=new ProgressoALunoDisciplinaRepository();
        this.configuracaoTesteAdaptativoNivelRepositorty=new ConfiguracaoTesteAdaptativoNivelRepositorty();
        this.configuracoesRepository = new ConfiguracoesRepository();
        this.adaptacaoRepository = new ConfiguracoesTesteAdaptativoRespository();
        this.medalhaRepository = new MedalhaRepository();

    }

    public List<TestDtoAll> findDtoAll() throws SQLException{
        return testeRepository.findAll().stream()
        .map(TestDtoAll::ParseMapDto)
        .toList();
    }

    public Optional<Map<String, Object>> getTeste(UUID testeId) {
        try {
            return testeRepository.findById(testeId);
        } catch (SQLException e) {
            System.err.println("Erro ao carregar teste: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Map<String, Object>> getTestesDisciplina(UUID disciplinaId) {
        try {
            return testeRepository.findByDisciplinaId(disciplinaId);
        } catch (SQLException e) {
            System.err.println("Erro ao carregar testes por disciplina: " + e.getMessage());
            return List.of();
        }
    }

    public List<Map<String, Object>> getTestesCandidato(UUID candidatoId) {
        try {
            return testeRepository.findByCandidatoId(candidatoId);
        } catch (SQLException e) {
            System.err.println("Erro ao carregar testes do candidato: " + e.getMessage());
            return List.of();
        }
    }

    public Optional<Map<String, Object>> getStatsDoTeste(UUID testeId) {
        try {
            return testeStatsRepository.findByTesteId(testeId);
        } catch (SQLException e) {
            System.err.println("Erro ao carregar stats do teste: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Map<String, Object>> getStatsDaDisciplina(UUID disciplinaId) {
        try {
            return testeStatsRepository.findByDisciplinaId(disciplinaId);
        } catch (SQLException e) {
            System.err.println("Erro ao carregar stats da disciplina: " + e.getMessage());
            return List.of();
        }
    }

    public List<Map<String, Object>> getStatsDoCandidato(UUID candidatoId) {
        try {
            return testeStatsRepository.findByCandidatoId(candidatoId);
        } catch (SQLException e) {
            System.err.println("Erro ao carregar stats do candidato: " + e.getMessage());
            return List.of();
        }
    }

    public ResumoHistoricoDisciplina carregarResumoHistoricoDisciplina(String disciplina) {
        return carregarResumoHistoricoDisciplina(Authentication.getCurrentUserId(), disciplina);
    }

    public ResumoHistoricoDisciplina carregarResumoHistoricoDisciplina(UUID candidatoId, String disciplina) {
        if (candidatoId == null || disciplina == null || disciplina.isBlank()) {
            return ResumoHistoricoDisciplina.vazio();
        }
        Map<String, ResumoHistoricoDisciplina> resumos = carregarResumoHistoricoDisciplinas(
            candidatoId,
            List.of(disciplina),
            6
        );
        return resumos.getOrDefault(QuestaoUtil.normalizar(disciplina), ResumoHistoricoDisciplina.vazio());
    }

    public Map<String, ResumoHistoricoDisciplina> carregarResumoHistoricoDisciplinas(
        List<String> disciplinas,
        int limiteTopicos
    ) {
        return carregarResumoHistoricoDisciplinas(Authentication.getCurrentUserId(), disciplinas, limiteTopicos);
    }

    public Map<String, ResumoHistoricoDisciplina> carregarResumoHistoricoDisciplinas(
        UUID candidatoId,
        List<String> disciplinas,
        int limiteTopicos
    ) {
        if (candidatoId == null || disciplinas == null || disciplinas.isEmpty()) {
            return Map.of();
        }

        ArrayList<String> disciplinasNormalizadas = new ArrayList<>();
        for (String disciplina : disciplinas) {
            String chave = QuestaoUtil.normalizar(disciplina);
            if (chave == null || chave.isBlank() || disciplinasNormalizadas.contains(chave)) {
                continue;
            }
            disciplinasNormalizadas.add(chave);
        }
        if (disciplinasNormalizadas.isEmpty()) {
            return Map.of();
        }

        int limiteTopicosSeguro = Math.max(1, limiteTopicos);
        String placeholders = String.join(", ", Collections.nCopies(disciplinasNormalizadas.size(), "?"));
        String disciplinaKeySql = sqlNormalizarDisciplina("tp.disciplina_nome, t.disciplina_nome");

        String sqlResumo = """
            select
              %s as disciplina_key,
              count(distinct tp.teste_id) as total_testes,
              count(*) as total_questoes,
              avg(case
                    when tp.acertou is true then 1.0
                    when tp.acertou is false then 0.0
                    else null
                  end) as acerto_medio,
              avg(coalesce(
                    tp.precisao,
                    case
                      when tp.acertou is true then 1.0
                      when tp.acertou is false then 0.0
                      else null
                    end
                  )) as precisao_media
            from teste_perguntas tp
            join testes t on t.id = tp.teste_id
            where t.candidato_id = ?
              and %s in (%s)
            group by 1
            """.formatted(disciplinaKeySql, disciplinaKeySql, placeholders);

        String topicoDisciplinaKeySql = sqlNormalizarDisciplina("tp.disciplina_nome, t.disciplina_nome");
        String sqlTopicos = """
            with topicos as (
              select
                %s as disciplina_key,
                coalesce(nullif(trim(tp.topico), ''), 'Sem topico') as topico,
                count(*) as total_questoes,
                sum(case when tp.acertou is true then 1 else 0 end) as total_acertos
              from teste_perguntas tp
              join testes t on t.id = tp.teste_id
              where t.candidato_id = ?
                and %s in (%s)
              group by 1, 2
            ),
            ranqueados as (
              select
                disciplina_key,
                topico,
                total_questoes,
                total_acertos,
                row_number() over (partition by disciplina_key order by total_questoes desc, topico asc) as posicao
              from topicos
            )
            select disciplina_key, topico, total_questoes, total_acertos
            from ranqueados
            where posicao <= ?
            order by disciplina_key asc, posicao asc
            """.formatted(topicoDisciplinaKeySql, topicoDisciplinaKeySql, placeholders);

        LinkedHashMap<String, ResumoHistoricoDisciplinaBuilder> builders = new LinkedHashMap<>();
        for (String disciplina : disciplinasNormalizadas) {
            builders.put(disciplina, new ResumoHistoricoDisciplinaBuilder());
        }

        try (Connection conn = JdbcBasicSqlRepository.openRequiredConnection()) {
            try (var stmt = conn.prepareStatement(sqlResumo)) {
                stmt.setObject(1, candidatoId);
                int index = 2;
                for (String disciplina : disciplinasNormalizadas) {
                    stmt.setString(index++, disciplina);
                }

                try (var rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String disciplinaKey = QuestaoUtil.safeText(rs.getObject("disciplina_key"), "");
                        ResumoHistoricoDisciplinaBuilder builder = builders.get(disciplinaKey);
                        if (builder == null) {
                            continue;
                        }

                        builder.totalTestes = safeInt(rs.getObject("total_testes"));
                        builder.totalQuestoes = safeInt(rs.getObject("total_questoes"));
                        builder.acertoMedio = limitarUnitario(ParseObject.parseDouble(rs.getObject("acerto_medio")));
                        builder.precisaoMedia = limitarUnitario(ParseObject.parseDouble(rs.getObject("precisao_media")));
                    }
                }
            }

            try (var stmt = conn.prepareStatement(sqlTopicos)) {
                stmt.setObject(1, candidatoId);
                int index = 2;
                for (String disciplina : disciplinasNormalizadas) {
                    stmt.setString(index++, disciplina);
                }
                stmt.setInt(index, limiteTopicosSeguro);

                try (var rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String disciplinaKey = QuestaoUtil.safeText(rs.getObject("disciplina_key"), "");
                        ResumoHistoricoDisciplinaBuilder builder = builders.get(disciplinaKey);
                        if (builder == null) {
                            continue;
                        }

                        int totalTopico = safeInt(rs.getObject("total_questoes"));
                        int acertosTopico = safeInt(rs.getObject("total_acertos"));
                        if (totalTopico <= 0) {
                            continue;
                        }

                        String nomeTopico = QuestaoUtil.safeText(rs.getObject("topico"), "Sem topico");
                        float evolucaoTopico = Math.max(
                            0f,
                            Math.min(100f, (acertosTopico * 100f) / totalTopico)
                        );
                        builder.topicos.add(new Percent(nomeTopico, evolucaoTopico));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar resumo historico em lote: " + e.getMessage());
            return Map.of();
        }

        LinkedHashMap<String, ResumoHistoricoDisciplina> resultado = new LinkedHashMap<>();
        for (Map.Entry<String, ResumoHistoricoDisciplinaBuilder> entry : builders.entrySet()) {
            resultado.put(entry.getKey(), entry.getValue().build());
        }
        return resultado;
    }

    public StatsProgress Stats(){
        float velocidade=0,precisao=0,consistencia=0,resiliencia=0,logica=0,progresso=0;
        try {
            for (Map<String,Object> map : testeRepository.findByCandidatoId(Authentication.getCurrentUserId())) {
                TestDtoAll test=TestDtoAll.ParseMapDto(map);
                Map<String, Object> value=diagnosticoRepository.findById(test.diagnostico_id()).orElseThrow();
                DiagnosticoDto diagnosticoDto=DiagnosticoDto.ParseMapDto(value);
                velocidade+=test.velocidade()-diagnosticoDto.velocidade();
                precisao+=test.precisao()-diagnosticoDto.precisao();
                consistencia+=test.consistencia()-diagnosticoDto.consistencia();
                logica+=test.logica()-diagnosticoDto.logica();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao calcular comparativo de stats dos testes: " + e.getMessage());
        }
        return new StatsProgress(velocidade,precisao,consistencia,resiliencia,logica,progresso);
    }

    private ConfiguracaoDto carregarConfiguracaoDoCandidato(UUID candidatoId) {
        if (candidatoId == null) {
            return null;
        }

        try {
            return configuracoesRepository.findByCandidato(candidatoId);
        } catch (Exception e) {
            return null;
        }
    }

    private ConfiguracaoTesteAdaptativoDto carregarAdaptacaoDoCandidato(UUID candidatoId) {
        if (candidatoId == null) {
            return ConfiguracaoTesteAdaptativoDto.padrao(null);
        }

        try {
            return adaptacaoRepository.findAtiva();
        } catch (Exception e) {
            return ConfiguracaoTesteAdaptativoDto.padrao(candidatoId);
        }
    }



     public void registrarTesteConcluido(
        NivelDificuldadeAdaptativa config,
        UUID candidatoId,
        UUID diagnosticoId,
        List<Questao> questoes,
        List<Character> respostasUsuario,
        List <ReacaoTeste> questoesTest,
        String tempoFormatado,
        String recomendacao
    ) {
       String nivelInicial;

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
                .computeIfAbsent(QuestaoUtil.normalizar(questao.getDisciplina()), ignored -> new ArrayList<>())
                .add(i);
        }

        if (indicesPorDisciplina.isEmpty()) {
            return;
        }

        int duracaoSegundos = ConversorTempo.parseTempoEmSegundos(tempoFormatado);
        LocalDateTime concluidoEm = LocalDateTime.now();
        ConfiguracaoDto configuracaoUsuario = carregarConfiguracaoDoCandidato(candidatoId);
        ConfiguracaoTesteAdaptativoDto adaptacaoUsuario = carregarAdaptacaoDoCandidato(candidatoId);

        try (Connection conn = JdbcBasicSqlRepository.openRequiredConnection()) {
            System.out.println("iniciou");
            conn.setAutoCommit(false);
            try {
                for (Map.Entry<String, ArrayList<Integer>> entry : indicesPorDisciplina.entrySet()) {
                    System.out.println("rodou");
                    ArrayList<Integer> indices = entry.getValue();
                    Questao questaoBase = questoes.get(indices.getFirst());
                    List<ReacaoTeste> reacoesDisciplina = filtrarReacoesPorIndices(questoesTest, indices, questoes);
                    String nomeDisciplina = QuestaoUtil.formatarDisciplina(questaoBase.getDisciplina());
                    ;
                    UUID disciplinaId =  QuestaoUtil.resolverDisciplinaId(nomeDisciplina);
                    nivelInicial=NivelActual(candidatoId, disciplinaId).toUpperCase();
                    ConfiguracaoTesteAdaptativoNivelDto configNivelDto=configuracaoTesteAdaptativoNivelRepositorty.findByCodigo(config.codigo());
                    int totalQuestoes = indices.size();
                    int totalAcertos = 0;

                    for (Integer indice : indices) {
                        if (QuestaoUtil.respostaEstaCorreta(questoes.get(indice), respostasUsuario.get(indice))) {
                            totalAcertos++;
                        }
                    }
                    int totalSeg = reacoesDisciplina.stream()
                        .mapToInt(reacao -> safeInt(reacao.tempoSegundos()))
                        .sum();

                    int totalErros = Math.max(0, totalQuestoes - totalAcertos);
                    double tempoMedioSegundos = totalQuestoes == 0 ? 0d : totalSeg / (double) totalQuestoes;
                    double percentualAcerto = totalQuestoes == 0 ? 0d : (totalAcertos * 100.0) / totalQuestoes;
                    String nivelFinal = QuestaoUtil.resolverNivelDiagnostico(percentualAcerto,true);
                    double precisao = CalculoStats.calcularPrecisaoMediaRespostas(reacoesDisciplina);
                    double consistencia = CalculoStats.calcularConsistenciaTeste(reacoesDisciplina, adaptacaoUsuario);
                    double logica = CalculoStats.calcularLogica(indices, questoes, respostasUsuario);
                    double resiliencia = CalculoStats.calcularResilienciaTeste(reacoesDisciplina, adaptacaoUsuario);
                    double velocidade = CalculoStats.calcularVelocidade(totalSeg, totalQuestoes, configuracaoUsuario);
                    String topicosJson = construirJsonResumoQuestoes(indices, questoes, true);
                    String subtopicosJson = construirJsonResumoQuestoes(indices, questoes, false);
                    String errosComunsJson = QuestaoUtil.construirJsonErrosComuns(indices, questoes, respostasUsuario);
                    String melhoriasJson = construirJsonMelhorias(
                        candidatoId,
                        disciplinaId,
                        indices,
                        questoes,
                        respostasUsuario,
                        questoesTest
                    );
                    String origem = "TESTE";
                    String observacoesStats = construirObservacoesStats(
                        indices,
                        questoes,
                        respostasUsuario,
                        tempoMedioSegundos,
                        recomendacao
                    );

                    System.out.println("chegou");
                    UUID id = testeRepository.inserir(
                        conn,
                        candidatoId,
                       null,
                        concluidoEm,
                        percentualAcerto,
                        concluidoEm,
                        diagnosticoId,
                        disciplinaId,
                        nomeDisciplina,
                        nivelInicial,
                        nivelFinal,
                        totalQuestoes,
                        configNivelDto.limiteInferior(),
                        configNivelDto.limiteSuperior(),
                        topicosJson,
                        subtopicosJson,
                        totalSeg,
                        totalQuestoes,
                        totalAcertos,
                        totalErros,
                        percentualAcerto,
                        velocidade,
                        precisao,
                        consistencia,
                        logica,
                        resiliencia,
                        recomendacao,
                        concluidoEm,
                        configNivelDto.configuracaoId()
                    );
                    reacoesDisciplina.forEach(t -> {
                        try {
                            testePerguntasRepository.inserir(conn, Teste_Pergunta.fromQuestao(t), id);
                        } catch (SQLException e) {
                            System.err.println(e);
                            e.printStackTrace();
                        }
                    });


                    testeStatsRepository.insert(
                        conn,
                        new Teste_Stat(
                            UUID.randomUUID(),
                            id,
                            diagnosticoId,
                            candidatoId,
                            disciplinaId,
                            nomeDisciplina,
                            origem,
                            totalSeg,
                            tempoMedioSegundos,
                            totalQuestoes,
                            totalAcertos,
                            totalErros,
                            percentualAcerto,
                            velocidade,
                            precisao,
                            consistencia,
                            logica,
                            resiliencia,
                            errosComunsJson,
                            melhoriasJson,
                            observacoesStats,
                            LocalDateTime.now(),
                            LocalDateTime.now()));

                }

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }

            sincronizarMedalhasSemQuebrarFluxo(candidatoId);
        } catch (Exception e) {
            System.err.println("Erro ao registrar teste concluido: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public UUID registrarSimuladoBolsaConcluido(
        BolsaDto bolsa,
        UUID candidatoId,
        List<Questao> questoes,
        List<Character> respostasUsuario,
        List<ReacaoTeste> questoesTest,
        String tempoFormatado,
        String recomendacao
    ) {
        if (bolsa == null || candidatoId == null || questoes == null || questoes.isEmpty() || respostasUsuario == null || respostasUsuario.isEmpty()) {
            return null;
        }

        int limite = Math.min(questoes.size(), respostasUsuario.size());
        if (limite <= 0) {
            return null;
        }

        ConfiguracaoDto configuracaoUsuario = carregarConfiguracaoDoCandidato(candidatoId);
        ConfiguracaoTesteAdaptativoDto adaptacaoUsuario = carregarAdaptacaoDoCandidato(candidatoId);

        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < limite; i++) {
            indices.add(i);
        }

        Questao questaoBase = questoes.getFirst();
        String nomeDisciplina = ParseObject.firstNonBlank(
            bolsa.disciplinaFoco(),
            QuestaoUtil.formatarDisciplina(questaoBase.getDisciplina()),
            "Bolsa Semanal"
        );
        UUID disciplinaId = QuestaoUtil.resolverDisciplinaId(nomeDisciplina);
        String nivelInicial = resolverNivelAtualSeguro(candidatoId, disciplinaId);

        List<ReacaoTeste> reacoesDisciplina = filtrarReacoesPorIndices(questoesTest, indices, questoes);
        int totalQuestoes = limite;
        int totalAcertos = 0;

        for (Integer indice : indices) {
            if (QuestaoUtil.respostaEstaCorreta(questoes.get(indice), respostasUsuario.get(indice))) {
                totalAcertos++;
            }
        }

        int totalSeg = reacoesDisciplina.stream()
            .mapToInt(reacao -> safeInt(reacao.tempoSegundos()))
            .sum();
        int totalErros = Math.max(0, totalQuestoes - totalAcertos);
        double tempoMedioSegundos = totalQuestoes == 0 ? 0d : totalSeg / (double) totalQuestoes;
        double percentualAcerto = totalQuestoes == 0 ? 0d : (totalAcertos * 100.0) / totalQuestoes;
        String nivelFinal = QuestaoUtil.resolverNivelDiagnostico(percentualAcerto,true);
        double precisao = CalculoStats.calcularPrecisaoMediaRespostas(reacoesDisciplina);
        double consistencia = CalculoStats.calcularConsistenciaTeste(reacoesDisciplina, adaptacaoUsuario);
        double logica = CalculoStats.calcularLogica(indices, questoes, respostasUsuario);
        double resiliencia = CalculoStats.calcularResilienciaTeste(reacoesDisciplina, adaptacaoUsuario);
        double velocidade = CalculoStats.calcularVelocidade(totalSeg, totalQuestoes, configuracaoUsuario);
        String topicosJson = construirJsonResumoQuestoes(indices, questoes, true);
        String subtopicosJson = construirJsonResumoQuestoes(indices, questoes, false);
        String errosComunsJson = QuestaoUtil.construirJsonErrosComuns(indices, questoes, respostasUsuario);
        String melhoriasJson = construirJsonMelhorias(
            candidatoId,
            disciplinaId,
            indices,
            questoes,
            respostasUsuario,
            questoesTest
        );
        String origem = "BOLSA_SEMANAL";
        String observacoesStats = construirObservacoesStats(
            indices,
            questoes,
            respostasUsuario,
            tempoMedioSegundos,
            recomendacao
        );

        LocalDateTime concluidoEm = LocalDateTime.now();

        try (Connection conn = JdbcBasicSqlRepository.openRequiredConnection()) {
            conn.setAutoCommit(false);
            try {
                UUID testeId = testeRepository.inserir(
                    conn,
                    candidatoId,
                    null,
                    concluidoEm,
                    percentualAcerto,
                    concluidoEm,
                    null,
                    disciplinaId,
                    nomeDisciplina,
                    nivelInicial,
                    nivelFinal,
                    totalQuestoes,
                    null,
                    null,
                    topicosJson,
                    subtopicosJson,
                    totalSeg,
                    totalQuestoes,
                    totalAcertos,
                    totalErros,
                    percentualAcerto,
                    velocidade,
                    precisao,
                    consistencia,
                    logica,
                    resiliencia,
                    recomendacao,
                    concluidoEm,
                    null
                );

                LinkedHashMap<String, Object> camposExtrasTeste = new LinkedHashMap<>();
                camposExtrasTeste.put("origem", origem);
                camposExtrasTeste.put("bolsa_id", bolsa.id());
                testeRepository.updateById(conn, testeId, camposExtrasTeste);

                for (ReacaoTeste reacao : reacoesDisciplina) {
                    testePerguntasRepository.inserir(conn, Teste_Pergunta.fromQuestao(reacao), testeId);
                }

                testeStatsRepository.insert(
                    conn,
                    new Teste_Stat(
                        UUID.randomUUID(),
                        testeId,
                        null,
                        candidatoId,
                        disciplinaId,
                        nomeDisciplina,
                        origem,
                        totalSeg,
                        tempoMedioSegundos,
                        totalQuestoes,
                        totalAcertos,
                        totalErros,
                        percentualAcerto,
                        velocidade,
                        precisao,
                        consistencia,
                        logica,
                        resiliencia,
                        errosComunsJson,
                        melhoriasJson,
                        observacoesStats,
                        concluidoEm,
                        concluidoEm
                    )
                );

                scoreBolsaRepository.upsertWeeklyScore(
                    conn,
                    candidatoId,
                    bolsa.id(),
                    testeId,
                    calcularScoreBolsa(percentualAcerto, precisao, velocidade),
                    "Simulado de " + ParseObject.firstNonBlank(bolsa.nome(), "bolsa") + " com " + totalAcertos + "/" + totalQuestoes + " acertos.",
                    totalQuestoes,
                    totalAcertos,
                    percentualAcerto,
                    totalSeg,
                    true,
                    construirCriteriosBolsaJson(bolsa),
                    LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                );

                conn.commit();
                sincronizarMedalhasSemQuebrarFluxo(candidatoId);
                return testeId;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            System.err.println("Erro ao registrar simulado de bolsa: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public String NivelActual(UUID candidatoId,UUID disciplinaID) throws SQLException{
        List<ProgressoAlunoDisciplinaDto> lista=progressoALunoDisciplinaRepository.findAllByField("aluno_id", candidatoId)
        .stream().map(ProgressoAlunoDisciplinaDto::fromMap)
        .filter(t -> t.disciplinaId().equals(disciplinaID)).toList();
        return lista.get(0).nivelAtual().name();
    }

    private String resolverNivelAtualSeguro(UUID candidatoId, UUID disciplinaId) {
        if (candidatoId == null || disciplinaId == null) {
            return "INTERMEDIARIO";
        }
        try {
            String nivel = NivelActual(candidatoId, disciplinaId);
            return nivel == null || nivel.isBlank() ? "INTERMEDIARIO" : nivel.toUpperCase(Locale.ROOT);
        } catch (Exception ignored) {
            return "INTERMEDIARIO";
        }
    }

    private double calcularScoreBolsa(double percentualAcerto, double precisao, double velocidade) {
        double pesoAcerto = QuestaoUtil.limitarPercentualFaixaCem(percentualAcerto) * 0.65d;
        double pesoPrecisao = QuestaoUtil.limitarPercentualFaixaCem(precisao * 100d) * 0.20d;
        double pesoVelocidade = QuestaoUtil.limitarPercentualFaixaCem(velocidade * 100d) * 0.15d;
        return QuestaoUtil.limitarPercentualFaixaCem(pesoAcerto + pesoPrecisao + pesoVelocidade);
    }

    private void sincronizarMedalhasSemQuebrarFluxo(UUID candidatoId) {
        if (candidatoId == null) {
            return;
        }

        try {
            medalhaRepository.sincronizarMedalhasPorUserId(candidatoId);
        } catch (Exception e) {
            System.err.println("Falha ao sincronizar medalhas do candidato: " + e.getMessage());
        }
    }

    private String construirCriteriosBolsaJson(BolsaDto bolsa) {
        if (bolsa == null) {
            return "{}";
        }

        return "{"
            + "\"disciplinaFoco\":\"" + QuestaoUtil.escapeJson(ParseObject.firstNonBlank(bolsa.disciplinaFoco(), "")) + "\","
            + "\"duracaoMinutos\":" + safeInt(bolsa.duracaoMinutos()) + ","
            + "\"medalhasMin\":" + safeInt(bolsa.criterioMedalhasMin()) + ","
            + "\"desempenhoMin\":" + safeInt(bolsa.criterioDesempenhoMin()) + ","
            + "\"evolucaoMin\":" + safeInt(bolsa.criterioEvolucaoMin()) + ","
            + "\"precisaoMin\":" + safeInt(bolsa.criterioPrecisaoMin()) + ","
            + "\"velocidadeMin\":" + safeInt(bolsa.criterioVelocidadeMin()) + ","
            + "\"modoResposta\":\"" + QuestaoUtil.escapeJson(ParseObject.firstNonBlank(bolsa.modoResposta(), "TEXTFIELD")) + "\""
            + "}";
    }

    public List<ErrosComuns> buscarErrosComuns(UUID disciplinaId) {
        return buscarErrosComuns(Authentication.getCurrentUserId(), disciplinaId);
    }

    public List<ErrosComuns> buscarErrosComuns(UUID candidatoId, UUID disciplinaId) {
        ArrayList<ErrosComuns> itens = new ArrayList<>();
        for (String json : carregarStatsJson(candidatoId, disciplinaId, "erros_comuns")) {
            itens.addAll(parseErrosComunsJson(json));
        }
        return itens;
    }

    public List<Melhorias> buscarMelhorias(UUID disciplinaId) {
        return buscarMelhorias(Authentication.getCurrentUserId(), disciplinaId);
    }

    public List<Melhorias> buscarMelhorias(UUID candidatoId, UUID disciplinaId) {
        ArrayList<Melhorias> itens = new ArrayList<>();
        for (String json : carregarStatsJson(candidatoId, disciplinaId, "melhorias")) {
            itens.addAll(ParseObject.parseMelhoriasJson(json));
        }
        return itens;
    }

    public List<TrilhaAdaptacaoSubtopico> carregarTrilhaAdaptacao(String disciplina) {
        UUID candidatoId = Authentication.getCurrentUserId();
        if (disciplina == null || disciplina.isBlank()) {
            return List.of();
        }
        return carregarTrilhaAdaptacao(candidatoId, QuestaoUtil.resolverDisciplinaId(disciplina), disciplina);
    }

    public List<TrilhaAdaptacaoSubtopico> carregarTrilhaAdaptacao(
        UUID candidatoId,
        UUID disciplinaId,
        String disciplinaNome
    ) {
        if (candidatoId == null || disciplinaId == null || disciplinaNome == null || disciplinaNome.isBlank()) {
            return List.of();
        }

        LinkedHashMap<String, TrilhaHistoricoBuilder> historicoPorSubtopico = carregarHistoricoTrilha(candidatoId, disciplinaId);
        LinkedHashMap<String, TrilhaProgressaoRow> progressoPorSubtopico = carregarProgressaoTrilha(candidatoId, disciplinaId);
        LinkedHashSet<String> chaves = new LinkedHashSet<>();
        chaves.addAll(progressoPorSubtopico.keySet());
        chaves.addAll(historicoPorSubtopico.keySet());
        System.out.println(chaves.size());
        ArrayList<TrilhaAdaptacaoSubtopico> itens = new ArrayList<>();
        for (String chave : chaves) {
            TrilhaProgressaoRow progresso = progressoPorSubtopico.get(chave);
            TrilhaHistoricoBuilder historico = historicoPorSubtopico.get(chave);

            String subtopico = ParseObject.firstNonBlank(
                progresso == null ? null : progresso.subtopico(),
                historico == null ? null : historico.displayName,
                "Geral"
            );
            double rigorAtualPercentual = progresso == null
                ? 0d
                : QuestaoUtil.limitarPercentualFaixaCem(progresso.rigorAtual() * 100d);
            double rigorAlvoPercentual = progresso == null
                ? 0d
                : QuestaoUtil.limitarPercentualFaixaCem(progresso.rigorAlvo() * 100d);
            double progressoPercentual = progresso == null
                ? resolverProgressoSemRigor(historico)
                : calcularProgressoTrilha(progresso.rigorAtual(), progresso.rigorAlvo());
            int avancos = historico == null ? 0 : historico.avancos;
            int quedas = historico == null ? 0 : historico.quedas;
            double dificuldadeMedia = historico == null ? 0d : historico.mediaDificuldade();
            boolean precisaRevisao = progresso != null && progresso.precisaRevisao();
            String observacao = construirObservacaoTrilha(subtopico, avancos, quedas, precisaRevisao, dificuldadeMedia);

            itens.add(new TrilhaAdaptacaoSubtopico(
                QuestaoUtil.formatarDisciplina(disciplinaNome),
                subtopico,
                progressoPercentual,
                rigorAtualPercentual,
                rigorAlvoPercentual,
                avancos,
                quedas,
                dificuldadeMedia,
                precisaRevisao,
                progresso == null ? null : progresso.recomendacaoLivro(),
                progresso == null ? null : progresso.recomendacaoPaginas(),
                observacao,
                progresso != null
            ));
        }

        itens.sort((left, right) -> {
            int comparacaoRevisao = Boolean.compare(right.precisaRevisao(), left.precisaRevisao());
            if (comparacaoRevisao != 0) {
                return comparacaoRevisao;
            }
            int comparacaoQuedas = Integer.compare(right.quedasRecentes(), left.quedasRecentes());
            if (comparacaoQuedas != 0) {
                return comparacaoQuedas;
            }
            int comparacaoProgresso = Double.compare(right.progressoPercentual(), left.progressoPercentual());
            if (comparacaoProgresso != 0) {
                return comparacaoProgresso;
            }
            return String.CASE_INSENSITIVE_ORDER.compare(left.subtopico(), right.subtopico());
        });
        return List.copyOf(itens);
    }

    public String carregarUltimaObservacaoTeste(UUID candidatoId, UUID disciplinaId) {
        if (candidatoId == null || disciplinaId == null) {
            return null;
        }

        try {
            for (Map<String, Object> row : testeStatsRepository.findByDisciplinaId(disciplinaId)) {
                if (!Objects.equals(candidatoId, ParseObject.parseUuid(row.get("candidato_id")))) {
                    continue;
                }
                String observacao = QuestaoUtil.safeText(row.get("observacoes"), "");
                if (!observacao.isBlank()) {
                    return observacao;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar observacoes do teste: " + e.getMessage());
        }

        return null;
    }

    public List<ErrosComuns> parseErrosComunsJson(String json) {
        ArrayList<ErrosComuns> itens = new ArrayList<>();
        for (Map<String, String> valores : ParseObject.parseJsonObjectArray(json)) {
            itens.add(
                new ErrosComuns(
                    ParseObject.parseUuid(valores.get("questaoId")),
                    ParseObject.firstNonBlank(valores.get("enuciado"), valores.get("enunciado")),
                    ParseObject.firstNonBlank(valores.get("marcada"), valores.get("resposta")),
                    valores.get("topico"),
                    valores.get("subtopico"),
                    ParseObject.firstNonBlank(valores.get("resposta"), valores.get("correta")),
                    resolverPercentualDificuldadeErro(valores)
                )
            );
        }
        return itens;
    }



    public String construirJsonMelhorias(
        UUID candidatoId,
        UUID disciplinaId,
        List<Integer> indices,
        List<Questao> questoes,
        List<Character> respostasUsuario,
        List<ReacaoTeste> questoesTest
    ) {
        Map<UUID, HistoricoQuestao> historicoPorQuestao = carregarHistoricoQuestoes(candidatoId, disciplinaId);
        StringBuilder json = new StringBuilder("[");
        boolean primeiroItem = true;

        for (Integer indice : indices) {
            if (indice == null || indice < 0 || indice >= questoes.size() || indice >= respostasUsuario.size()) {
                continue;
            }
            Questao questao = questoes.get(indice);
            if (questao == null) {
                continue;
            }

            char marcada = respostasUsuario.get(indice);
            boolean acertou = QuestaoUtil.respostaEstaCorreta(questao, marcada);

            UUID questaoId = ParseObject.parseUuid(questao.getId());
            HistoricoQuestao historico = historicoPorQuestao.getOrDefault(
                questaoId,
                new HistoricoQuestao(0, 0, 0, 0d, 0)
            );
            ReacaoTeste reacao = encontrarReacao(questoesTest, indice, questao.getId());
            int tempoSegundos = reacao == null ? 0 : safeInt(reacao.tempoSegundos());
            int qtdAcertos = historico.qtdAcertos() + (acertou ? 1 : 0);
            int qtdErros = historico.qtdErros() + (acertou ? 0 : 1);
            double precisaoAnterior = historico.mediaPrecisao() * 100d;
            double precisaoAtual = CalculoStats.calcularPrecisaoResposta(questao, marcada) * 100d;
            double melhoriaPercentual = precisaoAtual - precisaoAnterior;

            if (historico.tentativas() > 0 && Math.abs(melhoriaPercentual) < 0.001d && precisaoAtual >= 100d) {
                continue;
            }

            if (!primeiroItem) {
                json.append(", ");
            }

            json.append("{")
                .append("\"questaoId\":\"").append(QuestaoUtil.escapeJson(QuestaoUtil.safeText(questao.getId(), ""))).append("\",")
                .append("\"enuciado\":\"").append(QuestaoUtil.escapeJson(QuestaoUtil.safeText(questao.getEnunciado(), ""))).append("\",")
                .append("\"correta\":\"").append(questao.getRespostaCorreta()).append("\",")
                .append("\"resposta\":\"").append(marcada).append("\",")
                .append("\"tempoSegundos\":").append(tempoSegundos).append(",")
                .append("\"topico\":\"").append(QuestaoUtil.escapeJson(QuestaoUtil.safeText(questao.getTopico(), ""))).append("\",")
                .append("\"subtopico\":\"").append(QuestaoUtil.escapeJson(QuestaoUtil.safeText(questao.getSubtopico(), ""))).append("\",")
                .append("\"qtdAcerto\":").append(qtdAcertos).append(",")
                .append("\"qtdErros\":").append(qtdErros).append(",")
                .append("\"precisaoAnteriorPercentual\":").append(formatJsonDouble(precisaoAnterior)).append(",")
                .append("\"precisaoAtualPercentual\":").append(formatJsonDouble(precisaoAtual)).append(",")
                .append("\"melhoriaPercentual\":").append(formatJsonDouble(melhoriaPercentual))
                .append("}");

            primeiroItem = false;
        }
        json.append("]");
        return json.toString();
    }

    private List<String> carregarStatsJson(UUID candidatoId, UUID disciplinaId, String campo) {
        if (candidatoId == null || disciplinaId == null || campo == null || campo.isBlank()) {
            return List.of();
        }

        try {
            ArrayList<String> jsons = new ArrayList<>();
            for (Map<String, Object> row : testeStatsRepository.findByDisciplinaId(disciplinaId)) {
                if (!Objects.equals(candidatoId, ParseObject.parseUuid(row.get("candidato_id")))) {
                    continue;
                }

                String json = QuestaoUtil.safeText(row.get(campo), "");
                if (!json.isBlank()) {
                    jsons.add(json);
                }
            }
            return jsons;
        } catch (SQLException e) {
            System.err.println("Erro ao carregar historico JSON do teste: " + e.getMessage());
            return List.of();
        }
    }

    private Map<UUID, HistoricoQuestao> carregarHistoricoQuestoes(UUID candidatoId, UUID disciplinaId) {
        if (candidatoId == null || disciplinaId == null) {
            return Map.of();
        }

        String sql = """
            select tp.pergunta_id,
              tp.acertou,
              tp.tempo_segundos,
              coalesce(tp.precisao, case when tp.acertou then 1 else 0 end) as precisao
            from teste_perguntas tp
            join testes t on t.id = tp.teste_id
            where t.candidato_id = ? and t.disciplina_id = ?
              and %s = %s
            """;
        sql = sql.formatted(
            sqlNormalizarDisciplina("tp.disciplina_nome"),
            sqlNormalizarDisciplina("t.disciplina_nome")
        );

        LinkedHashMap<UUID, HistoricoQuestao> historico = new LinkedHashMap<>();
        try (Connection conn = JdbcBasicSqlRepository.openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, candidatoId);
            stmt.setObject(2, disciplinaId);

            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    UUID perguntaId = ParseObject.parseUuid(rs.getObject("pergunta_id"));
                    Boolean acertou = ParseObject.parseBoolean(rs.getObject("acertou"));
                    if (perguntaId == null || acertou == null) {
                        continue;
                    }

                    HistoricoQuestao atual = historico.getOrDefault(
                        perguntaId,
                        new HistoricoQuestao(0, 0, 0, 0d, 0)
                    );
                    int qtdErros = atual.qtdErros() + (acertou ? 0 : 1);
                    int qtdAcertos = atual.qtdAcertos() + (acertou ? 1 : 0);
                    int tempoAcumulado = atual.tempoSegundos() + safeInt(rs.getObject("tempo_segundos"));
                    double somaPrecisao = atual.somaPrecisao() + ParseObject.parseDouble(rs.getObject("precisao"));
                    int tentativas = atual.tentativas() + 1;

                    historico.put(perguntaId, new HistoricoQuestao(qtdErros, qtdAcertos, tempoAcumulado, somaPrecisao, tentativas));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar historico de questoes do teste: " + e.getMessage());
            return Map.of();
        }

        return historico;
    }

    private List<ReacaoTeste> filtrarReacoesPorIndices(
        List<ReacaoTeste> questoesTest,
        List<Integer> indices,
        List<Questao> questoes
    ) {
        if (questoesTest == null || questoesTest.isEmpty() || indices == null || indices.isEmpty()) {
            return List.of();
        }

        ArrayList<ReacaoTeste> reacoes = new ArrayList<>();
        for (Integer indice : indices) {
            if (indice == null || indice < 0 || indice >= questoes.size()) {
                continue;
            }

            Questao questao = questoes.get(indice);
            ReacaoTeste reacao = encontrarReacao(questoesTest, indice, questao == null ? null : questao.getId());
            if (reacao != null) {
                reacoes.add(reacao);
            }
        }

        return reacoes;
    }

    private String construirObservacoesStats(
        List<Integer> indices,
        List<Questao> questoes,
        List<Character> respostasUsuario,
        double tempoMedioSegundos,
        String recomendacao
    ) {
        LinkedHashMap<String, Integer> errosPorTopico = new LinkedHashMap<>();
        LinkedHashMap<String, Integer> errosPorSubtopico = new LinkedHashMap<>();

        for (Integer indice : indices) {
            if (indice == null || indice < 0 || indice >= questoes.size() || indice >= respostasUsuario.size()) {
                continue;
            }

            Questao questao = questoes.get(indice);
            if (questao == null) {
                continue;
            }

            if (QuestaoUtil.respostaEstaCorreta(questao, respostasUsuario.get(indice))) {
                continue;
            }

            incrementarContagem(errosPorTopico, QuestaoUtil.safeText(questao.getTopico(), "Sem topico"));
            incrementarContagem(errosPorSubtopico, QuestaoUtil.safeText(questao.getSubtopico(), "Sem subtopico"));
        }

        StringBuilder observacao = new StringBuilder();
        if (errosPorTopico.isEmpty()) {
            observacao.append("Sem erros comuns neste teste.");
        } else {
            observacao.append("Erros concentrados em ")
                .append(chaveComMaiorContagem(errosPorTopico));

            String subtopicoCritico = chaveComMaiorContagem(errosPorSubtopico);
            if (subtopicoCritico != null && !subtopicoCritico.isBlank()) {
                observacao.append(" / ").append(subtopicoCritico);
            }
            observacao.append(".");
        }

        observacao.append(" Tempo medio por questao: ")
            .append(String.format(java.util.Locale.ROOT, "%.2f", tempoMedioSegundos))
            .append("s.");

        if (recomendacao != null && !recomendacao.isBlank()) {
            observacao.append(" ").append(recomendacao);
        }

        return observacao.toString();
    }

    private void incrementarContagem(Map<String, Integer> contagem, String chave) {
        contagem.merge(chave, 1, Integer::sum);
    }

    private String chaveComMaiorContagem(Map<String, Integer> contagem) {
        String melhorChave = null;
        int maiorValor = Integer.MIN_VALUE;

        for (Map.Entry<String, Integer> entry : contagem.entrySet()) {
            if (entry.getValue() != null && entry.getValue() > maiorValor) {
                melhorChave = entry.getKey();
                maiorValor = entry.getValue();
            }
        }

        return melhorChave;
    }

    private ReacaoTeste encontrarReacao(List<ReacaoTeste> questoesTest, int indice, String questaoId) {
        if (questoesTest == null || questoesTest.isEmpty()) {
            return null;
        }

        for (ReacaoTeste reacao : questoesTest) {
            if (reacao == null) {
                continue;
            }
            if (reacao.ordem() == indice) {
                return reacao;
            }
            if (reacao.questao() != null && Objects.equals(reacao.questao().getId(), questaoId)) {
                return reacao;
            }
        }

        return null;
    }

    private LinkedHashMap<String, TrilhaHistoricoBuilder> carregarHistoricoTrilha(UUID candidatoId, UUID disciplinaId) {
        LinkedHashMap<String, TrilhaHistoricoBuilder> historico = new LinkedHashMap<>();
        try {
            for (Map<String, Object> row : testeStatsRepository.findByDisciplinaId(disciplinaId)) {
                if (!Objects.equals(candidatoId, ParseObject.parseUuid(row.get("candidato_id")))) {
                    continue;
                }

                for (ErrosComuns erro : parseErrosComunsJson(QuestaoUtil.safeText(row.get("erros_comuns"), "[]"))) {
                    String chave = normalizarChaveSubtopico(erro.subtopico());
                    TrilhaHistoricoBuilder builder = historico.computeIfAbsent(chave, ignored -> new TrilhaHistoricoBuilder());
                    builder.displayName = ParseObject.firstNonBlank(erro.subtopico(), erro.topico(), "Geral");
                    builder.quedas++;
                    builder.somaDificuldade += QuestaoUtil.limitarPercentualFaixaCem(erro.percentualDificuldade());
                    builder.totalDificuldades++;
                }

                for (Melhorias melhoria : ParseObject.parseMelhoriasJson(QuestaoUtil.safeText(row.get("melhorias"), "[]"))) {
                    String chave = normalizarChaveSubtopico(melhoria.subtopico());
                    TrilhaHistoricoBuilder builder = historico.computeIfAbsent(chave, ignored -> new TrilhaHistoricoBuilder());
                    builder.displayName = ParseObject.firstNonBlank(melhoria.subtopico(), melhoria.topico(), "Geral");
                    if (melhoria.melhoriaPercentual() > 0d) {
                        builder.avancos++;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar historico por subtopico: " + e.getMessage());
            return new LinkedHashMap<>();
        }
        return historico;
    }

    private LinkedHashMap<String, TrilhaProgressaoRow> carregarProgressaoTrilha(UUID candidatoId, UUID disciplinaId) {
        String sql = """
            select
              subtopico,
              rigor_atual,
              rigor_alvo,
              acertos_consecutivos,
              erros_consecutivos,
              precisa_revisao,
              recomendacao_livro,
              recomendacao_paginas
            from progressao_rigor
            where aluno_id = ? and disciplina_id = ?
            order by lower(coalesce(subtopico, '')) asc
            """;

        LinkedHashMap<String, TrilhaProgressaoRow> progresso = new LinkedHashMap<>();
        try (Connection conn = JdbcBasicSqlRepository.openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, candidatoId);
            stmt.setObject(2, disciplinaId);

            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String subtopico = ParseObject.firstNonBlank(rs.getString("subtopico"), "Geral");
                    progresso.put(
                        normalizarChaveSubtopico(subtopico),
                        new TrilhaProgressaoRow(
                            subtopico,
                            limitarRigor(ParseObject.parseDouble(rs.getObject("rigor_atual"))),
                            limitarRigor(ParseObject.parseDouble(rs.getObject("rigor_alvo"))),
                            safeInt(rs.getObject("acertos_consecutivos")),
                            safeInt(rs.getObject("erros_consecutivos")),
                            ParseObject.parseBoolean(rs.getObject("precisa_revisao")) == Boolean.TRUE,
                            ParseObject.firstNonBlank(rs.getString("recomendacao_livro"), null),
                            ParseObject.firstNonBlank(rs.getString("recomendacao_paginas"), null)
                        )
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar progressao por subtopico: " + e.getMessage());
            return new LinkedHashMap<>();
        }
        return progresso;
    }

    private String normalizarChaveSubtopico(String subtopico) {
        return QuestaoUtil.normalizar(ParseObject.firstNonBlank(subtopico, "Geral"));
    }

    private double limitarRigor(double valor) {
        return Math.max(0d, Math.min(1d, valor));
    }

    private double calcularProgressoTrilha(double rigorAtual, double rigorAlvo) {
        if (rigorAlvo <= 0d) {
            return QuestaoUtil.limitarPercentualFaixaCem(rigorAtual * 100d);
        }
        return QuestaoUtil.limitarPercentualFaixaCem((rigorAtual / rigorAlvo) * 100d);
    }

    private double resolverProgressoSemRigor(TrilhaHistoricoBuilder historico) {
        if (historico == null) {
            return 0d;
        }
        int total = historico.avancos + historico.quedas;
        if (total <= 0) {
            return 0d;
        }
        return QuestaoUtil.limitarPercentualFaixaCem((historico.avancos * 100d) / total);
    }

    private String construirObservacaoTrilha(
        String subtopico,
        int avancos,
        int quedas,
        boolean precisaRevisao,
        double dificuldadeMedia
    ) {
        if (precisaRevisao || quedas > avancos) {
            return "Subtopico " + subtopico + " com mais quedas recentes. Vale retomar a leitura guiada.";
        }
        if (avancos > 0) {
            return "Subtopico " + subtopico + " em subida, com sinais de consolidacao.";
        }
        if (dificuldadeMedia >= 60d) {
            return "Subtopico " + subtopico + " ainda exige leitura em nivel alto de dificuldade.";
        }
        return "Sem oscilacoes recentes suficientes para fechar uma tendencia.";
    }

    private double resolverPercentualDificuldadeErro(Map<String, String> valores) {
        String percentual = ParseObject.firstNonBlank(
            valores.get("percentualDificuldade"),
            valores.get("dificuldadePercentual"),
            valores.get("percentual_dificuldade")
        );
        if (percentual != null) {
            return QuestaoUtil.limitarPercentualFaixaCem(ParseObject.parseDouble(percentual));
        }

        String rigor = ParseObject.firstNonBlank(valores.get("rigor"), valores.get("rigorBase"));
        if (rigor != null) {
            double percentualPorRigor = ParseObject.parseDouble(rigor) * 100d;
            if (percentualPorRigor > 0d) {
                return QuestaoUtil.limitarPercentualFaixaCem(percentualPorRigor);
            }
        }

        String nivel = ParseObject.firstNonBlank(valores.get("nivelDificuldade"), valores.get("nivel_dificuldade"));
        if (nivel != null) {
            return QuestaoUtil.calcularPercentualDificuldade(ParseObject.parseInteger(nivel), null);
        }

        return 0d;
    }

    private int safeInt(long value) {
        if (value <= 0L) {
            return 0;
        }
        return (int) Math.min(Integer.MAX_VALUE, value);
    }

    private int safeInt(Object value) {
        if (value instanceof Number number) {
            return safeInt(number.longValue());
        }
        if (value == null) {
            return 0;
        }
        return ParseObject.parseInteger(value.toString());
    }



    private String formatJsonDouble(double value) {
        return String.format(Locale.ROOT, "%.2f", value);
    }

    private String sqlNormalizarDisciplina(String expressao) {
        return "translate(lower(coalesce(" + expressao + ", '')), '"
            + SQL_NORMALIZE_FROM
            + "', '"
            + SQL_NORMALIZE_TO
            + "')";
    }

    private float limitarUnitario(double valor) {
        return (float) Math.max(0d, Math.min(1d, valor));
    }

    private static final class ResumoHistoricoDisciplinaBuilder {
        private int totalTestes;
        private int totalQuestoes;
        private float acertoMedio;
        private float precisaoMedia;
        private final ArrayList<Percent> topicos = new ArrayList<>();

        private ResumoHistoricoDisciplina build() {
            return new ResumoHistoricoDisciplina(
                totalTestes,
                totalQuestoes,
                acertoMedio,
                precisaoMedia,
                List.copyOf(topicos)
            );
        }
    }

    public record ResumoHistoricoDisciplina(
        int totalTestes,
        int totalQuestoesRespondidas,
        float acertoMedio,
        float precisaoMedia,
        List<Percent> topicosTestados
    ) {
        public static ResumoHistoricoDisciplina vazio() {
            return new ResumoHistoricoDisciplina(0, 0, 0f, 0f, List.of());
        }
    }

    private record HistoricoQuestao(
        int qtdErros,
        int qtdAcertos,
        int tempoSegundos,
        double somaPrecisao,
        int tentativas
    ) {
        double mediaPrecisao() {
            if (tentativas <= 0) {
                return 0d;
            }
            return QuestaoUtil.limitarPercentualUnitario(somaPrecisao / tentativas);
        }
    }


    private static final class TrilhaHistoricoBuilder {
        private String displayName;
        private int avancos;
        private int quedas;
        private double somaDificuldade;
        private int totalDificuldades;

        private double mediaDificuldade() {
            if (totalDificuldades <= 0) {
                return 0d;
            }
            return QuestaoUtil.limitarPercentualFaixaCem(somaDificuldade / totalDificuldades);
        }
    }

    private record TrilhaProgressaoRow(
        String subtopico,
        double rigorAtual,
        double rigorAlvo,
        int acertosConsecutivos,
        int errosConsecutivos,
        boolean precisaRevisao,
        String recomendacaoLivro,
        String recomendacaoPaginas
    ) {
    }

    private String construirJsonResumoQuestoes(
        List<Integer> indices,
        List<Questao> questoes,
        boolean usarTopico
    ) {
        LinkedHashSet<String> valores = new LinkedHashSet<>();
        for (Integer indice : indices) {
            Questao questao = questoes.get(indice);
            if (questao == null) {
                continue;
            }

            String valor = usarTopico ? questao.getTopico() : questao.getSubtopico();
            if (valor == null || valor.isBlank()) {
                continue;
            }
            valores.add(valor);
        }

        StringBuilder json = new StringBuilder("[");
        int posicao = 0;
        for (String valor : valores) {
            if (posicao++ > 0) {
                json.append(", ");
            }
            json.append('"').append(QuestaoUtil.escapeJson(valor)).append('"');
        }
        json.append(']');
        return json.toString();
    }


}
