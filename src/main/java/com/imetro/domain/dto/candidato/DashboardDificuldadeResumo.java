package com.imetro.domain.dto.candidato;

import java.util.List;

public record DashboardDificuldadeResumo(
    double mediaDificuldadePercentual,
    List<DashboardDificuldadeDia> semana
) {
    public static DashboardDificuldadeResumo empty() {
        return new DashboardDificuldadeResumo(0.0, List.of());
    }
}
