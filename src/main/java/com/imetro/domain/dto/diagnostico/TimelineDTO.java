package com.imetro.domain.dto.diagnostico;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.imetro.domain.dto.Stats;

public record TimelineDTO(LocalDate data,LocalTime[] hora,String[] disciplina, String[] duracao, float[] acertos,float[] erros, float[] evolucao,Stats[] percent) {
    
}
