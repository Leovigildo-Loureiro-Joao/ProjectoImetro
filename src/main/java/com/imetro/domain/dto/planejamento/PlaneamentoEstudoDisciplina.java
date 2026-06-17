package com.imetro.domain.dto.planejamento;

import com.imetro.domain.dto.Topico;
import com.imetro.domain.enums.Foco;

public record PlaneamentoEstudoDisciplina(
    String disciplina,
    double pontuacao,
    double precisao,
    double velocidade,
    double consistencia,
    int diasSemEstudo,
    Foco foco,
    String observacao,
    double prioridade
) {
}
