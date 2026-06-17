package com.imetro.domain.dto;

import java.time.LocalDate;

import com.imetro.domain.enums.DesafioStatus;
import com.imetro.domain.enums.TipoDesafio;

public record Desafio(
    TipoDesafio tipo,
    String titulo,
    String descricao,
    String disciplina,
    String topico
) {}