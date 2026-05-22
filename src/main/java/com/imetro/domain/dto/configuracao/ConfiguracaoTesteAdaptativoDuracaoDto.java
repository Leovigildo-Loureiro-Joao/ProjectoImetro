package com.imetro.domain.dto.configuracao;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.imetro.util.DtoMapperSupport;

public record ConfiguracaoTesteAdaptativoDuracaoDto(
    UUID id,
    UUID configuracaoId,
    String codigo,
    String descricao,
    int limiteQuestoes,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm
) {

    public static ConfiguracaoTesteAdaptativoDuracaoDto fromMap(Map<String, ?> map) {
        Objects.requireNonNull(map, "map");

        return new ConfiguracaoTesteAdaptativoDuracaoDto(
            DtoMapperSupport.parseUuid(map.get("id")),
            DtoMapperSupport.parseUuid(map.get("configuracao_id")),
            DtoMapperSupport.parseText(map.get("codigo")),
            DtoMapperSupport.parseText(map.get("descricao")),
            valueOrDefault(DtoMapperSupport.parseInteger(map.get("limite_questoes")), 0),
            DtoMapperSupport.parseDateTime(map.get("criado_em")),
            DtoMapperSupport.parseDateTime(map.get("atualizado_em"))
        );
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("id", id());
        values.put("configuracao_id", configuracaoId());
        values.put("codigo", codigo());
        values.put("descricao", descricao());
        values.put("limite_questoes", limiteQuestoes());
        values.put("criado_em", criadoEm());
        values.put("atualizado_em", atualizadoEm());
        return values;
    }

    private static int valueOrDefault(Integer value, int fallback) {
        return value == null ? fallback : value;
    }
}
