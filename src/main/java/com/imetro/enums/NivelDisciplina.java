package com.imetro.enums;

public enum NivelDisciplina {
    BAIXO("Baixo"),
    MEDIO("Médio"),
    BASICO("Básico"),
    PROFISSIONAL("Profissional"),
    NORMAL("Normal"),
    ALTO("Alto");

    protected String descricao;

    NivelDisciplina(String descricao){
        this.descricao = descricao;
    }

}
