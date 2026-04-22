package com.imetro.domain.enums;

public enum NivelDisciplina {
    BAIXO("Baixo"),
    MEDIO("Médio"),
    BASICO("Básico"),
    ALTO("Alto");

    protected String descricao;

    NivelDisciplina(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

}
