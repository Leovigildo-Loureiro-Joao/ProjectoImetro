package com.imetro.domain.dto;

 public record BolsaMock(
        String nome,
        String tipo,
        int match,
        String cobertura,
        String prazo,
        String destaque,
        String risco,
        String pillClass
    ) {
    }