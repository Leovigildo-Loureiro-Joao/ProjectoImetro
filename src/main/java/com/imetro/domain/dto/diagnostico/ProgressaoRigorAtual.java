package com.imetro.domain.dto.diagnostico;

import java.util.UUID;

public record ProgressaoRigorAtual(
    UUID id,
    double rigorAtual,
    double rigorAlvo,
    int tentativasNoNivel,  
    int acertosConsecutivos,
    int errosConsecutivos
) {
}