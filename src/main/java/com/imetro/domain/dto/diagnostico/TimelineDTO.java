package com.imetro.domain.dto.diagnostico;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import com.imetro.domain.dto.Stats;

public record TimelineDTO(LocalDate data, ArrayList<LocalTime> hora,String[] disciplina, String[] duracao, ArrayList<Float> acertos,ArrayList<Float> erros, ArrayList<Float> evolucao,ArrayList<Stats> percent) {

}
