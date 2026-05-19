package com.imetro.domain.dto.candidato;

import java.time.LocalDate;

public record DashboardDificuldadeDia(
    LocalDate data,
    int totalErros,
    double mediaDificuldadePercentual
) {
}
