package com.imetro.domain.dto.diagnostico;

import java.util.ArrayList;
import java.util.UUID;

import com.imetro.domain.dto.Topico;

public  record DiagnosticoDisciplinaResumo(
    UUID disciplinaId,
    String nomeDisciplina,
    String objectivo,
    ArrayList<Topico> topicos,
    int totalQuestoes,
    int totalTopicos,
    int totalSubtopicos,
    double indicador,
    String legendaIndicador,
    String destaque,
    String resumo,
    String tendencia,
    String nivel,
    String observacao
) {
}
