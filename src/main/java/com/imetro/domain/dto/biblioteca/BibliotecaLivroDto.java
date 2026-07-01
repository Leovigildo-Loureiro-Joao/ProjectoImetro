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
    byte[] capaThumbnail,
    String checksumSha256,
    String sourcePath,
    boolean ativo,
    Instant criadoEm,
    Instant atualizadoEm,
    int totalPaginas,
    int paginasComTexto,
    String geminiFileUri,
    String geminiFileName,
    Instant geminiUploadedEm
) {
    public boolean possuiPaginasExtraidas() {
        return totalPaginas > 0;
    }

    public boolean possuiGeminiUpload() {
        return geminiFileUri != null && !geminiFileUri.isBlank();
    }
}
