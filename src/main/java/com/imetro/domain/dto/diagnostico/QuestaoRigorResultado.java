package com.imetro.domain.dto.diagnostico;

import com.imetro.ui.model.Questao;

public record QuestaoRigorResultado(
    Questao questao,
    boolean acertou
) {
}