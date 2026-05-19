package com.imetro.domain.dto.candidato;

import java.time.LocalDate;

public record DashboardMelhoriaDia(
    LocalDate data,
    int melhorias,
    int sucessos
) {
}
