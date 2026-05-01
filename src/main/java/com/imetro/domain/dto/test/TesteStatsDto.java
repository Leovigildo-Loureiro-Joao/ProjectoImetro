package com.imetro.domain.dto.test;

import java.time.LocalDateTime;
import java.util.UUID;

public record TesteStatsDto(
    UUID id,
    UUID testeId,
    UUID diagnosticoId,
    UUID candidatoId,
    UUID disciplinaId,
    String disciplinaNome,
    String origem,
    Integer tempoTotalSegundos,
    Double tempoMedioSegundos,
    Integer totalQuestoes,
    Integer totalAcertos,
    Integer totalErros,
    Double percentualAcerto,
    Float velocidade,
    Float precisao,
    Float consistencia,
    Float logica,
    Float resiliencia,
    String errosComunsJson,
    String melhoriasJson,
    String observacoes,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm
) {
    public TesteStatsDto {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (origem == null || origem.isBlank()) {
            origem = "TESTE";
        }
        if (tempoTotalSegundos == null) {
            tempoTotalSegundos = 0;
        }
        if (totalQuestoes == null) {
            totalQuestoes = 0;
        }
        if (totalAcertos == null) {
            totalAcertos = 0;
        }
        if (totalErros == null) {
            totalErros = 0;
        }
        if (errosComunsJson == null || errosComunsJson.isBlank()) {
            errosComunsJson = "[]";
        }
        if (melhoriasJson == null || melhoriasJson.isBlank()) {
            melhoriasJson = "[]";
        }
        if (criadoEm == null) {
            criadoEm = LocalDateTime.now();
        }
        if (atualizadoEm == null) {
            atualizadoEm = criadoEm;
        }
    }
}
