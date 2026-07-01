package com.imetro.domain.dto.leitura;

import com.imetro.util.DtoMapperSupport;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public record LeituraProgresso(
    UUID id,
    UUID alunoId,
    UUID livroId,
    Integer paginaAtual,
    Integer totalPaginas,
    String paginasLidas,
    String estado,
    String sessoesLeitura,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm
) {
    public Map<String, Object> toMap() {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("id", id);
        values.put("aluno_id", alunoId);
        values.put("livro_id", livroId);
        values.put("pagina_atual", paginaAtual);
        values.put("total_paginas", totalPaginas);
        values.put("paginas_lidas", paginasLidas);
        values.put("estado", estado);
        values.put("sessoes_leitura", sessoesLeitura);
        values.put("criado_em", criadoEm);
        values.put("atualizado_em", atualizadoEm);
        return values;
    }

    public static LeituraProgresso fromMap(Map<String, ?> map) {
        return new LeituraProgresso(
            DtoMapperSupport.parseUuid(map.get("id")),
            DtoMapperSupport.parseUuid(map.get("aluno_id")),
            DtoMapperSupport.parseUuid(map.get("livro_id")),
            DtoMapperSupport.parseInteger(map.get("pagina_atual")),
            DtoMapperSupport.parseInteger(map.get("total_paginas")),
            DtoMapperSupport.parseText(map.get("paginas_lidas")),
            DtoMapperSupport.parseText(map.get("estado")),
            DtoMapperSupport.parseText(map.get("sessoes_leitura")),
            DtoMapperSupport.parseDateTime(map.get("criado_em")),
            DtoMapperSupport.parseDateTime(map.get("atualizado_em"))
        );
    }
}
