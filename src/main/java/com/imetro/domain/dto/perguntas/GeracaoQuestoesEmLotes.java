package com.imetro.domain.dto.perguntas;

import java.util.List;

public record GeracaoQuestoesEmLotes(
    List<String> jsonLotesComSucesso,
    String jsonAgregado,
    int totalLotes,
    int lotesSucesso,
    int lotesFalha,
    int perguntasInseridas
) {
}
