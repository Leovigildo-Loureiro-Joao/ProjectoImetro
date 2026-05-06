package com.imetro.domain.dto.diagnostico;

import java.util.ArrayList;
import java.util.List;

import com.imetro.domain.dto.Topico;

public  record PrimeiroDiagnosticoResumo(
    int totalDisciplinas,
    int totalTopicos,
    int totalQuestoes,
    ArrayList<Topico> topicos,
    List<String> disciplinasSemBase,
    boolean pronto,
    String detalhe
) {
}
