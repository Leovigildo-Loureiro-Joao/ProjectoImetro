package com.imetro.domain.dto.planejamento;

public record PlaneamentoEstudoDisciplina(
    String disciplina,
    double pontuacao,
    double precisao,
    double velocidade,
    double consistencia,
    int diasSemEstudo,
    String foco,
    String observacao,
    double prioridade
) {
}
