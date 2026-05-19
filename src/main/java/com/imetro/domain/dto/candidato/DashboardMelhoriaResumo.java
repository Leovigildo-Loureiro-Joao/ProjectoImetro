package com.imetro.domain.dto.candidato;

import java.util.List;

public record DashboardMelhoriaResumo(
    double mediaMelhoriaPercentual,
    double taxaSucessoPercentual,
    List<DashboardMelhoriaDia> semana
) {
    public static DashboardMelhoriaResumo empty() {
        return new DashboardMelhoriaResumo(0.0, 0.0, List.of());
    }
}
