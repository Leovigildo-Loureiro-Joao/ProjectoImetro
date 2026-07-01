package com.imetro.domain.dto.biblioteca;

import com.imetro.util.DtoMapperSupport;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public record LivroMapaTopicos(
    UUID id,
    UUID livroId,
    String topico,
    String subtopico,
    Integer paginaInicio,
    Integer paginaFim,
    LocalDateTime criadoEm
) {
    public Map<String, Object> toMap() {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("id", id);
        values.put("livro_id", livroId);
        values.put("topico", topico);
        values.put("subtopico", subtopico);
        values.put("pagina_inicio", paginaInicio);
        values.put("pagina_fim", paginaFim);
        values.put("criado_em", criadoEm);
        return values;
    }

    public static LivroMapaTopicos fromMap(Map<String, ?> map) {
        return new LivroMapaTopicos(
            DtoMapperSupport.parseUuid(map.get("id")),
            DtoMapperSupport.parseUuid(map.get("livro_id")),
            DtoMapperSupport.parseText(map.get("topico")),
            DtoMapperSupport.parseText(map.get("subtopico")),
            DtoMapperSupport.parseInteger(map.get("pagina_inicio")),
            DtoMapperSupport.parseInteger(map.get("pagina_fim")),
            DtoMapperSupport.parseDateTime(map.get("criado_em"))
        );
    }
}
