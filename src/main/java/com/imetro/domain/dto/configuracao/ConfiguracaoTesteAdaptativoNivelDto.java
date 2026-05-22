package com.imetro.domain.dto.configuracao;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.imetro.util.DtoMapperSupport;

public record ConfiguracaoTesteAdaptativoNivelDto(
    UUID id,
    UUID configuracaoId,
    int nivel,
    String codigo,
    double tempoSugeridoSegundos,
    double rigorBase,
    double limiteInferior,
    double limiteSuperior,
    double limiar_erro,
    double limiar_acerto,
    double resumo_med,
    int tot_erro_revisao,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm
) {

    public static ConfiguracaoTesteAdaptativoNivelDto fromMap(Map<String, ?> map) {
        Objects.requireNonNull(map, "map");

        return new ConfiguracaoTesteAdaptativoNivelDto(
            DtoMapperSupport.parseUuid(map.get("id")),
            DtoMapperSupport.parseUuid(map.get("configuracao_id")),
            valueOrDefault(DtoMapperSupport.parseInteger(map.get("nivel")), 0),
            DtoMapperSupport.parseText(map.get("codigo")),
            valueOrDefault(DtoMapperSupport.parseDouble(map.get("tempo_sugerido_segundos")), 0d),
            valueOrDefault(DtoMapperSupport.parseDouble(map.get("rigor_base")), 0d),
            valueOrDefault(DtoMapperSupport.parseDouble(map.get("limite_inferior")), 0d),
            valueOrDefault(DtoMapperSupport.parseDouble(map.get("limite_superior")), 0d),
            valueOrDefault(DtoMapperSupport.parseDouble(map.get("limiar_acerto")), 0d),
            valueOrDefault(DtoMapperSupport.parseDouble(map.get("limiar_erro")), 0d),
            valueOrDefault(DtoMapperSupport.parseDouble(map.get("resumo_med")), 0d),
            valueOrDefault(DtoMapperSupport.parseInteger(map.get("tot_erro_revisao")), 0),
            DtoMapperSupport.parseDateTime(map.get("criado_em")),
            DtoMapperSupport.parseDateTime(map.get("atualizado_em"))
        );
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("id", id());
        values.put("configuracao_id", configuracaoId());
        values.put("nivel", nivel());
        values.put("codigo", codigo());
        values.put("tempo_sugerido_segundos", tempoSugeridoSegundos());
        values.put("rigor_base", rigorBase());
        values.put("limite_inferior", limiteInferior());
        values.put("limite_superior", limiteSuperior());
        values.put("limiar_acerto", limiteInferior());
        values.put("limiar_erro", limiteSuperior());
        values.put("resumo_med", limiteInferior());
        values.put("tot_erro_revisao", limiteSuperior());
        values.put("criado_em", criadoEm());
        values.put("atualizado_em", atualizadoEm());
        return values;
    }

    private static int valueOrDefault(Integer value, int fallback) {
        return value == null ? fallback : value;
    }

    private static double valueOrDefault(Double value, double fallback) {
        return value == null ? fallback : value;
    }
}
