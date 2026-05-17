package com.imetro.domain.dto.perguntas;

import java.util.List;

public record GeracaoLote(
    int indice,
    int totalLotes,
    List<TopicoSubtopico> focos,
    int quantidadeQuestoes
) {
}