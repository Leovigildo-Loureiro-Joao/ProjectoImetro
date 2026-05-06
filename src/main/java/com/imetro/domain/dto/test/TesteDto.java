package com.imetro.domain.dto.test;

import java.util.List;

public record TesteDto(
    String disciplina,
    float ritmoEvolutivo,
    float errosComuns,
    float melhoria,
    float percent,
    float nivelDificuldade,
    float velocidade,
    float consistencia,
    int totalQuestoes,
    int totalSubtopicos,
    List<Percent> topicos,
    List<String> Passos
) {
    public TesteDto(
        String disciplina,
        float ritmoEvolutivo,
        float errosComuns,
        float melhoria,
        float percent,
        int totalQuestoes,
        int totalSubtopicos,
        List<Percent> topicos,
        List<String> Passos
    ) {
        this(
            disciplina,
            ritmoEvolutivo,
            errosComuns,
            melhoria,
            percent,
            0f,
            0f,
            0f,
            totalQuestoes,
            totalSubtopicos,
            topicos,
            Passos
        );
    }
}
