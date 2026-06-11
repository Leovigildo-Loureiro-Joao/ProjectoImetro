package com.imetro.domain.dto.biblioteca;

import java.time.Instant;
import java.util.UUID;

public record BibliotecaLivroPaginaDto(
    UUID id,
    UUID livroId,
    int paginaNumero,
    String textoPagina,
    Instant criadoEm
) {
    public boolean possuiTexto() {
        return textoPagina != null && !textoPagina.isBlank();
    }
}
