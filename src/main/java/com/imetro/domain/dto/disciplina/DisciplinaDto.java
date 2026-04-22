package com.imetro.domain.dto.disciplina;

import java.util.UUID;
import com.imetro.domain.enums.NivelDisciplina;

// Usando RECORD (recomendado para Java 17+)
public record DisciplinaDto(
    UUID id,              // Adicionei o ID
    String nome,          // Nome da disciplina
    Float peso,           // Peso (importante para cálculo)
    NivelDisciplina nivel, // Nível (BASICO, INTERMEDIARIO, AVANCADO)
    String objectivo      // Objetivo da disciplina
) {
    // Construtor compacto para validação (opcional)
    public DisciplinaDto {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da disciplina não pode ser vazio");
        }
        if (peso == null || peso <= 0) {
            throw new IllegalArgumentException("Peso deve ser maior que zero");
        }
        if (nivel == null) {
            throw new IllegalArgumentException("Nível da disciplina é obrigatório");
        }
    }
    
    // Construtor alternativo para casos onde não temos ID ainda
    public static DisciplinaDto semId(String nome, Float peso, NivelDisciplina nivel, String objectivo) {
        return new DisciplinaDto(null, nome, peso, nivel, objectivo);
    }

        // Construtor alternativo para casos onde não temos ID ainda
    public static DisciplinaDto soNecessaio(String nome,  NivelDisciplina nivel) {
        return new DisciplinaDto(null, nome, null, nivel, null);
    }
}