package com.imetro.domain.dto.bolsa;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.imetro.util.DtoMapperSupport;

public record BolsaDto(
        UUID id,
        String nome,
        String tipo,
        Integer match,
        Integer vagas,
        String cobertura,
        String destaque,
        String risco,
        LocalDateTime abertura,
        LocalDateTime fechamento
    ) {

    public static BolsaDto fromMap(Map<String, ?> map) {
        Objects.requireNonNull(map, "map");

        return new BolsaDto(
            DtoMapperSupport.parseUuid(map.get("id")),
            DtoMapperSupport.parseText(map.get("nome")),
            DtoMapperSupport.parseText(map.get("tipo")),
            DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseInteger(map.get("match")), 0),
            DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseInteger(map.get("vagas")), 0),
            DtoMapperSupport.parseText(map.get("cobertura")),
            DtoMapperSupport.parseText(map.get("destaque")),
            DtoMapperSupport.parseText(map.get("risco")),
            DtoMapperSupport.parseDateTime(map.get("criado_em")),
            DtoMapperSupport.parseDateTime(map.get("atualizado_em"))

        );
    }

     public Map<String, ?> toMap() {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("id", id());
        values.put("nome", nome());
        values.put("tipo", tipo());
        values.put("match", match());
        values.put("vagas", vagas());
        values.put("cobertura", cobertura());
        values.put("destaque", destaque());
        values.put("acertos_subir_lento", risco());
        values.put("erros_descer", abertura());
        values.put("janela_consistencia", fechamento());
        return values;
    }


}