package com.imetro.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.imetro.domain.dto.configuracao.ConfiguracaoTesteAdaptativoNivelDto;
import com.imetro.domain.dto.diagnostico.DiagnosticoDto;
import com.imetro.domain.dto.progresso.ProgressoAlunoDisciplinaDto;
import com.imetro.domain.dto.stats.StatsProgress;
import com.imetro.domain.dto.stats.Teste_Stat;
import com.imetro.domain.dto.test.TestDtoAll;
import com.imetro.domain.dto.test.ErrosComuns;
import com.imetro.domain.dto.test.Melhorias;
import com.imetro.domain.dto.test.ReacaoTeste;
import com.imetro.domain.dto.test.Teste_Pergunta;
import com.imetro.domain.enums.NivelDificuldadeAdaptativa;
import com.imetro.persistence.repository.ConfiguracaoTesteAdaptativoNivelRepositorty;
import com.imetro.persistence.repository.DiagnosticoRepository;
import com.imetro.persistence.repository.JdbcBasicSqlRepository;
import com.imetro.persistence.repository.ProgressoALunoDisciplinaRepository;
import com.imetro.persistence.repository.TestePerguntasRepository;
import com.imetro.persistence.repository.TesteRepository;
import com.imetro.persistence.repository.TesteStatsRepository;
import com.imetro.ui.controller.candidato.testes.TesteAdaptativoCoordinator.TesteConfig;
import com.imetro.ui.model.Questao;
import com.imetro.util.Authentication;
import com.imetro.util.CalculoStats;
import com.imetro.util.ConversorTempo;
import com.imetro.util.QuestaoUtil;

public class TesteService {
    private final TesteRepository testeRepository;
    private final TesteStatsRepository testeStatsRepository;
    private final TestePerguntasRepository testePerguntasRepository;
    private final DiagnosticoRepository diagnosticoRepository;
    private final ProgressoALunoDisciplinaRepository progressoALunoDisciplinaRepository;
    private final ConfiguracaoTesteAdaptativoNivelRepositorty configuracaoTesteAdaptativoNivelRepositorty;

    public TesteService() {
        this.testeRepository = new TesteRepository();
        this.testeStatsRepository = new TesteStatsRepository();
        this.testePerguntasRepository = new TestePerguntasRepository();
        this.diagnosticoRepository=new DiagnosticoRepository();
        this.progressoALunoDisciplinaRepository=new ProgressoALunoDisciplinaRepository();
        this.configuracaoTesteAdaptativoNivelRepositorty=new ConfiguracaoTesteAdaptativoNivelRepositorty();

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
                        if (respostasUsuario.get(indice) == questoes.get(indice).getRespostaCorreta()) {
                            totalAcertos++;
                        }
                    }
                    int totalSeg = reacoesDisciplina.stream()
                        .mapToInt(reacao -> safeInt(reacao.tempoSegundos()))
                        .sum();

                    int totalErros = Math.max(0, totalQuestoes - totalAcertos);
                    double tempoMedioSegundos = totalQuestoes == 0 ? 0d : totalSeg / (double) totalQuestoes;
                    double percentualAcerto = totalQuestoes == 0 ? 0d : (totalAcertos * 100.0) / totalQuestoes;
                    String nivelFinal = QuestaoUtil.resolverNivelDiagnostico(percentualAcerto);
                    double precisao = CalculoStats.calcularPrecisaoMediaRespostas(reacoesDisciplina);
                    double consistencia = CalculoStats.calcularConsistenciaTeste(reacoesDisciplina);
                    double logica = CalculoStats.calcularLogica(indices, questoes, respostasUsuario);
                    double resiliencia = CalculoStats.calcularResilienciaTeste(reacoesDisciplina);
                    double velocidade = CalculoStats.calcularVelocidade(totalSeg, totalQuestoes);
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
                    UUID id =testeRepository.inserir(
                        candidatoId,
                       null,
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
                            testePerguntasRepository.inserir(Teste_Pergunta.fromQuestao(t), id);
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
        } catch (Exception e) {
            System.err.println("Erro ao registrar teste concluido: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String NivelActual(UUID candidatoId,UUID disciplinaID) throws SQLException{
        List<ProgressoAlunoDisciplinaDto> lista=progressoALunoDisciplinaRepository.findAllByField("aluno_id", candidatoId)
        .stream().map(ProgressoAlunoDisciplinaDto::fromMap)
        .filter(t -> t.disciplinaId().equals(disciplinaID)).toList();
        return lista.get(0).nivelAtual().name();
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
            itens.addAll(parseMelhoriasJson(json));
        }
        return itens;
    }

    public List<ErrosComuns> parseErrosComunsJson(String json) {
        ArrayList<ErrosComuns> itens = new ArrayList<>();
        for (Map<String, String> valores : parseJsonObjectArray(json)) {
            itens.add(
                new ErrosComuns(
                    parseUuid(valores.get("questaoId")),
                    firstNonBlank(valores.get("enuciado"), valores.get("enunciado")),
                    firstNonBlank(valores.get("marcada"), valores.get("resposta")),
                    valores.get("topico"),
                    valores.get("subtopico"),
                    firstNonBlank(valores.get("resposta"), valores.get("correta"))
                )
            );
        }
        return itens;
    }

    public List<Melhorias> parseMelhoriasJson(String json) {
        ArrayList<Melhorias> itens = new ArrayList<>();
        for (Map<String, String> valores : parseJsonObjectArray(json)) {
            itens.add(
                new Melhorias(
                    parseUuid(valores.get("questaoId")),
                    firstNonBlank(valores.get("enuciado"), valores.get("enunciado")),
                    valores.get("correta"),
                    firstNonBlank(valores.get("resposta"), valores.get("marcada")),
                    parseInteger(valores.get("tempoSegundos")),
                    valores.get("topico"),
                    valores.get("subtopico"),
                    parseInteger(valores.get("qtdAcerto")),
                    parseInteger(valores.get("qtdErros")),
                    parseDouble(valores.get("precisaoAnteriorPercentual")),
                    parseDouble(valores.get("precisaoAtualPercentual")),
                    parseDouble(valores.get("melhoriaPercentual"))
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
            boolean acertou = Character.toUpperCase(marcada) == Character.toUpperCase(questao.getRespostaCorreta());

            UUID questaoId = parseUuid(questao.getId());
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
                if (!Objects.equals(candidatoId, parseUuid(row.get("candidato_id")))) {
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
              and lower(coalesce(tp.disciplina_nome, '')) = lower(coalesce(t.disciplina_nome, ''))
            """;

        LinkedHashMap<UUID, HistoricoQuestao> historico = new LinkedHashMap<>();
        try (Connection conn = JdbcBasicSqlRepository.openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, candidatoId);
            stmt.setObject(2, disciplinaId);

            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    UUID perguntaId = parseUuid(rs.getObject("pergunta_id"));
                    Boolean acertou = parseBoolean(rs.getObject("acertou"));
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
                    double somaPrecisao = atual.somaPrecisao() + parseDouble(rs.getObject("precisao"));
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

            if (Character.toUpperCase(respostasUsuario.get(indice)) == Character.toUpperCase(questao.getRespostaCorreta())) {
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

    private List<Map<String, String>> parseJsonObjectArray(String json) {
        if (json == null || json.isBlank()) {
            return List.of();
        }

        ArrayList<Map<String, String>> itens = new ArrayList<>();
        int cursor = skipWhitespace(json, 0);
        if (cursor >= json.length() || json.charAt(cursor) != '[') {
            return List.of();
        }

        cursor++;
        while (cursor < json.length()) {
            cursor = skipWhitespace(json, cursor);
            if (cursor >= json.length() || json.charAt(cursor) == ']') {
                break;
            }
            if (json.charAt(cursor) == ',') {
                cursor++;
                continue;
            }
            if (json.charAt(cursor) != '{') {
                cursor++;
                continue;
            }

            ParsedJsonObject parsed = parseJsonObject(json, cursor);
            itens.add(parsed.values());
            cursor = parsed.nextIndex();
        }

        return itens;
    }

    private ParsedJsonObject parseJsonObject(String json, int startIndex) {
        LinkedHashMap<String, String> valores = new LinkedHashMap<>();
        int cursor = startIndex + 1;

        while (cursor < json.length()) {
            cursor = skipWhitespace(json, cursor);
            if (cursor >= json.length()) {
                break;
            }

            char atual = json.charAt(cursor);
            if (atual == '}') {
                return new ParsedJsonObject(valores, cursor + 1);
            }
            if (atual == ',') {
                cursor++;
                continue;
            }
            if (atual != '"') {
                cursor++;
                continue;
            }

            ParsedJsonToken chave = parseJsonStringToken(json, cursor);
            cursor = skipWhitespace(json, chave.nextIndex());
            if (cursor < json.length() && json.charAt(cursor) == ':') {
                cursor++;
            }
            cursor = skipWhitespace(json, cursor);

            ParsedJsonToken valor = cursor < json.length() && json.charAt(cursor) == '"'
                ? parseJsonStringToken(json, cursor)
                : parseJsonLiteralToken(json, cursor);

            valores.put(chave.value(), valor.value());
            cursor = skipWhitespace(json, valor.nextIndex());
            if (cursor < json.length() && json.charAt(cursor) == ',') {
                cursor++;
            }
        }

        return new ParsedJsonObject(valores, cursor);
    }

    private ParsedJsonToken parseJsonStringToken(String json, int startIndex) {
        StringBuilder out = new StringBuilder();
        int cursor = startIndex + 1;

        while (cursor < json.length()) {
            char atual = json.charAt(cursor);
            if (atual == '"') {
                return new ParsedJsonToken(out.toString(), cursor + 1);
            }
            if (atual == '\\' && cursor + 1 < json.length()) {
                char proximo = json.charAt(++cursor);
                switch (proximo) {
                    case '"' -> out.append('"');
                    case '\\' -> out.append('\\');
                    case '/' -> out.append('/');
                    case 'b' -> out.append('\b');
                    case 'f' -> out.append('\f');
                    case 'n' -> out.append('\n');
                    case 'r' -> out.append('\r');
                    case 't' -> out.append('\t');
                    case 'u' -> {
                        if (cursor + 4 < json.length()) {
                            String hex = json.substring(cursor + 1, cursor + 5);
                            out.append((char) Integer.parseInt(hex, 16));
                            cursor += 4;
                        }
                    }
                    default -> out.append(proximo);
                }
            } else {
                out.append(atual);
            }
            cursor++;
        }

        return new ParsedJsonToken(out.toString(), json.length());
    }

    private ParsedJsonToken parseJsonLiteralToken(String json, int startIndex) {
        int cursor = startIndex;
        while (cursor < json.length()) {
            char atual = json.charAt(cursor);
            if (atual == ',' || atual == '}') {
                break;
            }
            cursor++;
        }

        String literal = json.substring(startIndex, cursor).trim();
        if (literal.isBlank() || "null".equalsIgnoreCase(literal)) {
            return new ParsedJsonToken(null, cursor);
        }
        return new ParsedJsonToken(literal, cursor);
    }

    private int skipWhitespace(String value, int startIndex) {
        int cursor = startIndex;
        while (cursor < value.length() && Character.isWhitespace(value.charAt(cursor))) {
            cursor++;
        }
        return cursor;
    }

    private UUID parseUuid(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof UUID uuid) {
            return uuid;
        }

        String text = value.toString().trim();
        if (text.isBlank()) {
            return null;
        }

        try {
            return UUID.fromString(text);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    private Boolean parseBoolean(Object value) {
        if (value instanceof Boolean bool) {
            return bool;
        }
        if (value == null) {
            return null;
        }

        String text = value.toString().trim();
        if (text.isBlank()) {
            return null;
        }

        return Boolean.parseBoolean(text);
    }

    private int parseInteger(String value) {
        if (value == null || value.isBlank()) {
            return 0;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    private double parseDouble(Object value) {
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        if (value == null) {
            return 0d;
        }

        String text = value.toString().trim();
        if (text.isBlank()) {
            return 0d;
        }

        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException ignored) {
            return 0d;
        }
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
        return parseInteger(value.toString());
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }

    private String formatJsonDouble(double value) {
        return String.format(Locale.ROOT, "%.2f", value);
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

    private record ParsedJsonObject(Map<String, String> values, int nextIndex) {
    }

    private record ParsedJsonToken(String value, int nextIndex) {
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
