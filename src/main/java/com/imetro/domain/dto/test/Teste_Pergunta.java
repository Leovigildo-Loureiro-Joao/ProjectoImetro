package com.imetro.domain.dto.test;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import com.imetro.ui.model.Questao;
import com.imetro.util.CalculoStats;
import com.imetro.util.ParseTimeStampLocalDate;
import com.imetro.util.QuestaoUtil;

public record Teste_Pergunta(
    UUID teste_id,
    UUID pergunta_id,
    Integer ordem,
    String disciplina_nome,
    String topico,
    String subtopico,
    String enunciado,
    String resposta_dada,
    String resposta_correta,
    String resposta_dada_texto,
    String resposta_correta_texto,
    Long tempo_segundos,
    Double tempo_sugerido_segundos,
    Integer nivel_dificuldade,
    Double rigor,
    Double precisao,
    Double velocidade,
    Boolean acertou,
    Double consistencia,
    Double resiliencia,
    String referencia_livro,
    Integer pagina_inicio,
    Integer pagina_fim,
    LocalDateTime respondido_em
) {

    public static Teste_Pergunta fromQuestao(
       ReacaoTeste reacao
    ) {
        Questao questao= reacao.questao();
        char respostaUsuario = Character.toUpperCase(reacao.respostaDada());
        char respostaCorreta = Character.toUpperCase(reacao.questao().getRespostaCorreta());
        boolean acertou = respostaUsuario == respostaCorreta;
        double precisaoResposta = CalculoStats.calcularPrecisaoResposta(reacao);

        return new Teste_Pergunta(
            parseUuid(questao.getId()),
            parseUuid(questao.getId()),
            Integer.valueOf( reacao.ordem()),
            QuestaoUtil.formatarDisciplina(QuestaoUtil.safeText(questao.getDisciplina(), "")),
            QuestaoUtil.safeText(questao.getTopico(), null),
            QuestaoUtil.safeText(questao.getSubtopico(), null),
            QuestaoUtil.safeText(questao.getEnunciado(), null),
            String.valueOf(respostaUsuario),
            String.valueOf(respostaCorreta),
            QuestaoUtil.resolverTextoOpcao(questao, respostaUsuario),
            QuestaoUtil.resolverTextoOpcao(questao, respostaCorreta),
            Math.max(0, reacao.tempoSegundos()),
            questao.getTempoSugerido(),
            questao.getNivelDificuldade(),
            questao.getRigor(),
            precisaoResposta,
            CalculoStats.calcularVelocidadePorQuestao( reacao.tempoSegundos(), questao.getTempoSugerido()),
            acertou,
            Double.valueOf(reacao.consistencia()),
            Double.valueOf(reacao.resiliencia()),
            QuestaoUtil.safeText(questao.getReferenciaLivro(), null),
            questao.getPaginaInicio(),
            questao.getPaginaFim(),
            reacao.respondidoEm()
        );
    }

    public Teste_Pergunta withTesteId(UUID testeId) {
        return new Teste_Pergunta(
            testeId,
            pergunta_id,
            ordem,
            disciplina_nome,
            topico,
            subtopico,
            enunciado,
            resposta_dada,
            resposta_correta,
            resposta_dada_texto,
            resposta_correta_texto,
            tempo_segundos,
            tempo_sugerido_segundos,
            nivel_dificuldade,
            rigor,
            precisao,
            velocidade,
            acertou,
            consistencia,
            resiliencia,
            referencia_livro,
            pagina_inicio,
            pagina_fim,
            respondido_em
        );
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("teste_id", teste_id());
        values.put("pergunta_id", pergunta_id());
        values.put("ordem", ordem());
        values.put("disciplina_nome", disciplina_nome());
        values.put("topico", topico());
        values.put("subtopico", subtopico());
        values.put("enunciado", enunciado());
        values.put("resposta_dada", resposta_dada());
        values.put("resposta_correta", resposta_correta());
        values.put("resposta_dada_texto", resposta_dada_texto());
        values.put("resposta_correta_texto", resposta_correta_texto());
        values.put("tempo_segundos", tempo_segundos());
        values.put("tempo_sugerido_segundos", tempo_sugerido_segundos());
        values.put("nivel_dificuldade", nivel_dificuldade());
        values.put("rigor", rigor());
        values.put("precisao", precisao());
        values.put("velocidade", velocidade());
        values.put("acertou", acertou());
        values.put("consistencia", consistencia());
        values.put("resiliencia", resiliencia());
        values.put("referencia_livro", referencia_livro());
        values.put("pagina_inicio", pagina_inicio());
        values.put("pagina_fim", pagina_fim());
        values.put("respondido_em", respondido_em());
        return values;
    }

    public static Teste_Pergunta toDto(Map<String, ?> map) {
        return new Teste_Pergunta(
            parseUuid(map.get("teste_id")),
            parseUuid(map.get("pergunta_id")),
            parseInteger(map.get("ordem")),
            parseText(map.get("disciplina_nome")),
            parseText(map.get("topico")),
            parseText(map.get("subtopico")),
            parseText(map.get("enunciado")),
            parseText(map.get("resposta_dada")),
            parseText(map.get("resposta_correta")),
            parseText(map.get("resposta_dada_texto")),
            parseText(map.get("resposta_correta_texto")),
            parseLorg(map.get("tempo_segundos")),
            parseDouble(map.get("tempo_sugerido_segundos")),
            parseInteger(map.get("nivel_dificuldade")),
            parseDouble(map.get("rigor")),
            parseDouble(map.get("precisao")),
            parseDouble(map.get("velocidade")),
            parseBoolean(map.get("acertou")),
            parseDouble(map.get("consistencia")),
            parseDouble(map.get("resiliencia")),
            parseText(map.get("referencia_livro")),
            parseInteger(map.get("pagina_inicio")),
            parseInteger(map.get("pagina_fim")),
            ParseTimeStampLocalDate.mapearDataHora(map.get("respondido_em"))
        );
    }

    private static String parseText(Object value) {
        if (value == null) {
            return null;
        }
        String text = value.toString();
        return text.isBlank() ? null : text;
    }

    private static UUID parseUuid(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof UUID uuid) {
            return uuid;
        }
        String text = value.toString();
        if (text.isBlank()) {
            return null;
        }
        try {
            return UUID.fromString(text);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    private static Integer parseInteger(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        if (value == null) {
            return null;
        }
        String text = value.toString();
        if (text.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private static Long parseLorg(Object value) {
        if (value instanceof Number number) {
            return number.longValue();
        }
        if (value == null) {
            return null;
        }
        String text = value.toString();
        if (text.isBlank()) {
            return null;
        }
        try {
            return Long.parseLong(text);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private static Double parseDouble(Object value) {
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        if (value == null) {
            return null;
        }
        String text = value.toString();
        if (text.isBlank()) {
            return null;
        }
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private static Boolean parseBoolean(Object value) {
        if (value instanceof Boolean bool) {
            return bool;
        }
        if (value == null) {
            return null;
        }
        String text = value.toString();
        if (text.isBlank()) {
            return null;
        }
        return Boolean.parseBoolean(text);
    }
}
