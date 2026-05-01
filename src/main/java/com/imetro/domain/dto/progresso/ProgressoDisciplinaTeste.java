package com.imetro.domain.dto.progresso;

import com.imetro.domain.enums.NivelDisciplina;

public record ProgressoDisciplinaTeste(String disciplina,double progresso,double pesoAtual,NivelDisciplina nivel, float velocudade, float consistencia,int tempo){

}
