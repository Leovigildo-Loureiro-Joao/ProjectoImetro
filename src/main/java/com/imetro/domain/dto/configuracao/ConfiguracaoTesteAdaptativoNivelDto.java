package com.imetro.domain.dto.configuracao;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public record ConfiguracaoTesteAdaptativoNivelDto(
    UUID id,
    UUID configuracaoId,
    int nivel,
    String codigo,
    double tempoSugeridoSegundos,
    double rigorBase,
    double limiteInferior,
    double limiteSuperior,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm
) {

    public static ConfiguracaoTesteAdaptativoNivelDto fromMap(Map<String, ?> map) {
        Objects.requireNonNull(map, "map");

        return new ConfiguracaoTesteAdaptativoNivelDto(
            ConfiguracaoDtoMapperSupport.parseUuid(map.get("id")),
            ConfiguracaoDtoMapperSupport.parseUuid(map.get("configuracao_id")),
            valueOrDefault(ConfiguracaoDtoMapperSupport.parseInteger(map.get("nivel")), 0),
            ConfiguracaoDtoMapperSupport.parseText(map.get("codigo")),
            valueOrDefault(ConfiguracaoDtoMapperSupport.parseDouble(map.get("tempo_sugerido_segundos")), 0d),
            valueOrDefault(ConfiguracaoDtoMapperSupport.parseDouble(map.get("rigor_base")), 0d),
            valueOrDefault(ConfiguracaoDtoMapperSupport.parseDouble(map.get("limite_inferior")), 0d),
            valueOrDefault(ConfiguracaoDtoMapperSupport.parseDouble(map.get("limite_superior")), 0d),
            ConfiguracaoDtoMapperSupport.parseDateTime(map.get("criado_em")),
            ConfiguracaoDtoMapperSupport.parseDateTime(map.get("atualizado_em"))
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
