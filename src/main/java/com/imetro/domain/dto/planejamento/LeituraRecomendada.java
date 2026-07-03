package com.imetro.domain.dto.planejamento;

import java.util.UUID;

public record LeituraRecomendada(
    UUID livroId,
    String tituloLivro,
    String disciplina,
    String topico,
    String subtopico,
    int paginaInicio,
    int paginaFim,
    int totalPaginas,
    double progressoLeitura
) {
    public String formatarPaginas() {
        return paginaInicio + " - " + paginaFim;
    }

    public String formatarReferencia() {
        if (paginaInicio == paginaFim) {
            return tituloLivro + ", pagina " + paginaInicio;
        }
        return tituloLivro + ", paginas " + paginaInicio + " a " + paginaFim;
    }
}
