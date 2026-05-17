package com.imetro.domain.dto.perguntas;

public record BootstrapProgressSnapshot(
        double progress,
        boolean indeterminate,
        String titulo,
        String detalhe
    ) {
    }
