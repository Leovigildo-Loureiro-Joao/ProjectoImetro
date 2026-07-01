package com.imetro.domain.dto.test;

import com.imetro.util.DtoMapperSupport;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public record MiniTeste(
    UUID id,
    UUID livroId,
    Integer paginaInicio,
    Integer paginaFim,
    String questoes,
    String checksumConteudo,
    LocalDateTime criadoEm
) {
    public Map<String, Object> toMap() {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("id", id);
        values.put("livro_id", livroId);
        values.put("pagina_inicio", paginaInicio);
        values.put("pagina_fim", paginaFim);
        values.put("questoes", questoes);
        values.put("checksum_conteudo", checksumConteudo);
        values.put("criado_em", criadoEm);
        return values;
    }

    public static MiniTeste fromMap(Map<String, ?> map) {
        return new MiniTeste(
            DtoMapperSupport.parseUuid(map.get("id")),
            DtoMapperSupport.parseUuid(map.get("livro_id")),
            DtoMapperSupport.parseInteger(map.get("pagina_inicio")),
            DtoMapperSupport.parseInteger(map.get("pagina_fim")),
            DtoMapperSupport.parseText(map.get("questoes")),
            DtoMapperSupport.parseText(map.get("checksum_conteudo")),
            DtoMapperSupport.parseDateTime(map.get("criado_em"))
        );
    }
}
