package com.imetro.domain.dto.biblioteca;

import java.time.Instant;
import java.util.UUID;

public record BibliotecaLivroDto(
    UUID id,
    UUID disciplinaId,
    String disciplinaNome,
    String titulo,
    String nomeArquivo,
    String mimeType,
    long tamanhoBytes,
    String checksumSha256,
    String sourcePath,
    boolean ativo,
    Instant criadoEm,
    Instant atualizadoEm,
    int totalPaginas,
    int paginasComTexto
) {
    public boolean possuiPaginasExtraidas() {
        return totalPaginas > 0;
    }
}
