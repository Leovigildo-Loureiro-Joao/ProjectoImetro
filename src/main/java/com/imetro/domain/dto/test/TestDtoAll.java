package com.imetro.domain.dto.test;

import java.time.LocalDateTime;
import java.util.UUID;

public record TestDtoAll(
    UUID id ,
    UUID candidato_id,
    UUID orientador_id,
    UUID disciplina_id,
    UUID diagnostico_id,
    UUID relatorio_id,
    String disciplina_nome,
    LocalDateTime data_teste,
    float resultado,
    String nivel_inicial,
    String nivel_final,
    int limite_questoes ,
    double limite_inferior,
    double limite_superior,
    Object[] topicos,
    Object[] subtopicos,
    int duracao_segundos,
    int total_questoes,
    int total_acertos,
    int total_erros,
    double percentual_acerto ,
    float velocidade,
    float precisao ,
    float consistencia ,
    float logica ,
    float resiliencia ,
    String observacoes,
    LocalDateTime criado_em,
    LocalDateTime atualizado_em
) {
    
}
