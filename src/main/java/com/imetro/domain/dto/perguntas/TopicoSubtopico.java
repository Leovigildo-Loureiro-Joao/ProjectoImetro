package com.imetro.domain.dto.perguntas;

public record TopicoSubtopico(
    String topico,
    String subtopico,
    int paginaInicio,
    int paginaFim
) {
    public TopicoSubtopico(String topico, String subtopico) {
        this(topico, subtopico, 0, 0);
    }

    public boolean temPaginas() {
        return paginaInicio > 0 && paginaFim >= paginaInicio;
    }
}
