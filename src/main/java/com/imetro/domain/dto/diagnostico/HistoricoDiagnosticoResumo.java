package com.imetro.domain.dto.diagnostico;

import java.time.LocalDateTime;

public record HistoricoDiagnosticoResumo(
    Double percentualAcerto,
    Double evolucaoPercentual,
    String nivel,
    Integer totalQuestoes,
    Integer totalAcertos,
    Integer totalErros,
    LocalDateTime momento
) {
}