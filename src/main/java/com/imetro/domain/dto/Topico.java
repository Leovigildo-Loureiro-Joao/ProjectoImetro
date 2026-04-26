package com.imetro.domain.dto;

import java.util.UUID;

public record Topico(
    UUID idDisciplina,
    String disciplina,
    String topicos,
    UUID recomendacoes,
    String[] subTopicos
) {
}
