package com.imetro.domain.dto.bolsa;

public record BolsaMock(
    String nome,
    String tipo,
    int match,
    int vagas,
    String cobertura,
    String janela,
    String destaque,
    String risco,
    String status,
    String dificuldade,
    String criterioResumo,
    String acaoLabel,
    boolean disponivel,
    String pillClass
) {
}
