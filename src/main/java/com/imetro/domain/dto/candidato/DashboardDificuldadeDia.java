package com.imetro.domain.dto.candidato;

import java.time.LocalDate;

import com.imetro.ui.model.Questao;

public record DashboardDificuldadeDia(
    LocalDate data,
    int totalErros,
    double mediaDificuldadePercentual
) {
}
