package com.imetro.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.imetro.domain.dto.diagnostico.DiagnosticoDto;
import com.imetro.domain.dto.stats.StatsProgress;
import com.imetro.domain.dto.stats.Teste_Stat;
import com.imetro.domain.dto.test.TestDtoAll;
import com.imetro.domain.dto.test.Teste_Pergunta;
import com.imetro.domain.dto.test.ReacaoTeste;
import com.imetro.persistence.repository.DiagnosticoRepository;
import com.imetro.persistence.repository.JdbcBasicSqlRepository;
import com.imetro.persistence.repository.TestePerguntasRepository;
import com.imetro.persistence.repository.TesteRepository;
import com.imetro.persistence.repository.TesteStatsRepository;
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

    public TesteService() {
        this.testeRepository = new TesteRepository();
        this.testeStatsRepository = new TesteStatsRepository();
        this.testePerguntasRepository = new TestePerguntasRepository();
        this.diagnosticoRepository=new DiagnosticoRepository();
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
        UUID candidatoId,
        UUID diagnosticoId,
        List<Questao> questoes,
        List<Character> respostasUsuario,
        List <ReacaoTeste> questoesTest,
        String nivelInicial,
        String tempoFormatado,
        String recomendacao
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
                .computeIfAbsent(QuestaoUtil.normalizar(questao.getDisciplina()), ignored -> new ArrayList<>())
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
                    ArrayList<Integer> indices = entry.getValue();
                    Questao questaoBase = questoes.get(indices.getFirst());
                    String nomeDisciplina = QuestaoUtil.formatarDisciplina(questaoBase.getDisciplina());
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
                    String nivelFinal = QuestaoUtil.resolverNivelDiagnostico(percentualAcerto);
                    double precisao = CalculoStats.calcularPrecisao(totalAcertos, totalQuestoes);
                    double consistencia = 0d;
                    double logica = CalculoStats.calcularLogica(indices, questoes, respostasUsuario);
                    double resiliencia = 0d;
                    double velocidade = CalculoStats.calcularVelocidade(duracaoSegundos, totalQuestoes);
                    String topicosJson = construirJsonResumoQuestoes(indices, questoes, true);
                    String subtopicosJson = construirJsonResumoQuestoes(indices, questoes, false);

                    UUID id =testeRepository.inserir(
                        conn,
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
                        0d,
                        1d,
                        topicosJson,
                        subtopicosJson,
                        duracaoSegundos,
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
                        concluidoEm
                    );
                    questoesTest.forEach(t -> {
                        try {
                            testePerguntasRepository.inserir(Teste_Pergunta.fromQuestao(t), id);
                        } catch (SQLException e) {
                            System.err.println(e);
                            e.printStackTrace();
                        }
                    });

                    testeStatsRepository.insert(
                        new Teste_Stat(
                            UUID.randomUUID(),
                            id,
                            diagnosticoId,
                            candidatoId,
                            disciplinaId,
                            nomeDisciplina,
                            null,
                            null,
                            null,
                            totalQuestoes,
                            totalAcertos,
                            totalErros,
                            null,
                            velocidade,
                            precisao,
                            consistencia,
                            logica,
                            resiliencia,
                            null,
                            null,
                            null,
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
        }
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
