package com.imetro.domain.dto.test;

public record TrilhaAdaptacaoSubtopico(
    String disciplina,
    String subtopico,
    double progressoPercentual,
    double rigorAtualPercentual,
    double rigorAlvoPercentual,
    int avancosRecentes,
    int quedasRecentes,
    double dificuldadeMediaPercentual,
    boolean precisaRevisao,
    String recomendacaoLivro,
    String recomendacaoPaginas,
    String observacao
) {
}
