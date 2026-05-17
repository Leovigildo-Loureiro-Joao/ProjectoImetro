package com.imetro.domain.dto.perguntas;

import java.util.UUID;

import com.imetro.domain.enums.BootstrapStatus;

public record BootstrapResult(
    UUID disciplinaId,
    String nomeDisciplina,
    BootstrapStatus status,
    int totalPdfs,
    int totalPerguntas,
    String detalhe
    ) {
    }
