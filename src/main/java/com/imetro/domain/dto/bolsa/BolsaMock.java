package com.imetro.domain.dto.bolsa;

 public record BolsaMock(
        String nome,
        String tipo,
        int match,
        int vagas,
        String cobertura,
        String prazo,
        String destaque,
        String risco,
        String pillClass
    ) {

    }