package com.imetro.domain.dto.test;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.imetro.util.ParseObject;
import com.imetro.util.ParseTimeStampLocalDate;

public record TestDtoAll(
    UUID id,
    UUID candidato_id,
    UUID disciplina_id,
    UUID diagnostico_id,
    UUID relatorio_id,
    String disciplina_nome,
    LocalDateTime data_teste,
    float resultado,
    String nivel_inicial,
    String nivel_final,
    int limite_questoes,
    double limite_inferior,
    double limite_superior,
    Object[] topicos,
    Object[] subtopicos,
    int duracao_segundos,
    int total_questoes,
    int total_acertos,
    int total_erros,
    double percentual_acerto,
    float velocidade,
    float precisao,
    float consistencia,
    float logica,
    float resiliencia,
    String observacoes,
    LocalDateTime criado_em,
    LocalDateTime atualizado_em
) {
    public static TestDtoAll ParseMapDto(Map<String, Object> link) {
        UUID id = ParseObject.parseUuid(link.get("id"));
        UUID candidato_id = ParseObject.parseUuid(link.get("candidato_id"));
        UUID disciplina_id = ParseObject.parseUuid(link.get("disciplina_id"));
        UUID relatorio_id = ParseObject.parseUuid(link.get("relatorio_id"));
        UUID diagnostico_id = ParseObject.parseUuid(link.get("diagnostico_id"));
        String nome = Objects.toString(link.get("disciplina_nome"), "");
        String nivel_inicial = Objects.toString(link.get("nivel_inicial"), "");
        String nivel_final = Objects.toString(link.get("nivel_final"), "");
        int duracao_seg = toInt(link.get("duracao_segundos"));
        int total_questoes = toInt(link.get("total_questoes"));
        int total_acertos = toInt(link.get("total_acertos"));
        int total_erros = toInt(link.get("total_erros"));
        double percentual_acerto = ParseObject.parseDouble(link.get("percentual_acerto"));
        int limite_questoes = toInt(link.get("limite_questoes"));
        double limite_inferior = ParseObject.parseDouble(link.get("limite_inferior"));
        double limite_superior = ParseObject.parseDouble(link.get("limite_superior"));
        float velocidade = toFloat(link.get("velocidade"));
        float precisao = toFloat(link.get("precisao"));
        float consistencia = toFloat(link.get("consistencia"));
        float logica = toFloat(link.get("logica"));
        float resiliencia = toFloat(link.get("resiliencia"));
        float resultado = toFloat(link.get("resultado"));
        String observacoes = Objects.toString(link.get("observacoes"), "");
        Object[] topicos = parseJsonbArray(link.get("topicos"));
        Object[] subtopicos = parseJsonbArray(link.get("subtopicos"));
        LocalDateTime criado_em = ParseTimeStampLocalDate.mapearDataHora(link.get("criado_em"));
        LocalDateTime data_teste = ParseTimeStampLocalDate.mapearDataHora(link.get("data_teste"));
        LocalDateTime atualizado_em = ParseTimeStampLocalDate.mapearDataHora(link.get("atualizado_em"));
        return new TestDtoAll(
            id,
            candidato_id,
            disciplina_id,
            diagnostico_id,
            relatorio_id,
            nome,
            data_teste,
            resultado,
            nivel_inicial,
            nivel_final,
            limite_questoes,
            limite_inferior,
            limite_superior,
            topicos,
            subtopicos,
            duracao_seg,
            total_questoes,
            total_acertos,
            total_erros,
            percentual_acerto,
            velocidade,
            precisao,
            consistencia,
            logica,
            resiliencia,
            observacoes,
            criado_em,
            atualizado_em
        );
    }

    private static int toInt(Object value) {
        if (value instanceof Number num) {
            return num.intValue();
        }
        if (value == null) {
            return 0;
        }
        try {
            return Integer.parseInt(value.toString().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static float toFloat(Object value) {
        if (value instanceof Number num) {
            return num.floatValue();
        }
        if (value == null) {
            return 0f;
        }
        try {
            return Float.parseFloat(value.toString().trim());
        } catch (NumberFormatException e) {
            return 0f;
        }
    }

    private static Object[] parseJsonbArray(Object value) {
        if (value instanceof Object[] arr) {
            return arr;
        }
        if (value == null) {
            return new Object[0];
        }
        String raw = value.toString();
        if (raw == null || raw.isBlank() || raw.equals("[]")) {
            return new Object[0];
        }
        return new Object[]{raw};
    }
}
