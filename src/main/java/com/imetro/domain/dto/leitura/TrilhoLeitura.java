package com.imetro.domain.dto.leitura;

import com.imetro.util.DtoMapperSupport;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public record TrilhoLeitura(
    UUID id,
    UUID alunoId,
    UUID disciplinaId,
    UUID livroId,
    Integer ordem,
    Integer paginaInicio,
    Integer paginaFim,
    String topico,
    String subtopico,
    String estado,
    LocalDateTime dataConclusao,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm
) {
    public Map<String, Object> toMap() {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("id", id);
        values.put("aluno_id", alunoId);
        values.put("disciplina_id", disciplinaId);
        values.put("livro_id", livroId);
        values.put("ordem", ordem);
        values.put("pagina_inicio", paginaInicio);
        values.put("pagina_fim", paginaFim);
        values.put("topico", topico);
        values.put("subtopico", subtopico);
        values.put("estado", estado);
        values.put("data_conclusao", dataConclusao);
        values.put("criado_em", criadoEm);
        values.put("atualizado_em", atualizadoEm);
        return values;
    }

    public static TrilhoLeitura fromMap(Map<String, ?> map) {
        return new TrilhoLeitura(
            DtoMapperSupport.parseUuid(map.get("id")),
            DtoMapperSupport.parseUuid(map.get("aluno_id")),
            DtoMapperSupport.parseUuid(map.get("disciplina_id")),
            DtoMapperSupport.parseUuid(map.get("livro_id")),
            DtoMapperSupport.parseInteger(map.get("ordem")),
            DtoMapperSupport.parseInteger(map.get("pagina_inicio")),
            DtoMapperSupport.parseInteger(map.get("pagina_fim")),
            DtoMapperSupport.parseText(map.get("topico")),
            DtoMapperSupport.parseText(map.get("subtopico")),
            DtoMapperSupport.parseText(map.get("estado")),
            DtoMapperSupport.parseDateTime(map.get("data_conclusao")),
            DtoMapperSupport.parseDateTime(map.get("criado_em")),
            DtoMapperSupport.parseDateTime(map.get("atualizado_em"))
        );
    }
}
