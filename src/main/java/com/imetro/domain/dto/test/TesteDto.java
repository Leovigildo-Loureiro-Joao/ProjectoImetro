package com.imetro.domain.dto.test;

import java.util.ArrayList;
import java.util.List;

public record TesteDto(
    String disciplina,
    float ritmoEvolutivo,
    float errosComuns,
    float melhoria,
    float percent,
    List<Percent> topicos,
    List<String> Passos
) {
    
}
