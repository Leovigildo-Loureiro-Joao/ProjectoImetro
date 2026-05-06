package com.imetro.domain.dto.diagnostico;

import java.util.UUID;

public record ProgressoResumo(
    UUID disciplinaId,
    Double taxaAcertoGeral,
    String nivelAtual
) {
}
