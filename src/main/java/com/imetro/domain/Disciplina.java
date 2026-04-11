package com.imetro.domain;

import com.imetro.domain.enums.NivelDisciplina;

public class Disciplina {
    private String nome;
    private float peso;
    private NivelDisciplina nivel;
    private String objectivo;

    public Disciplina(String nome, float peso, NivelDisciplina nivel) {
        this.nome = nome;
        this.peso = peso;
        this.nivel = nivel;
    }

    public Disciplina(String nome, float peso, NivelDisciplina nivel, String objectivo) {
        this.nome = nome;
        this.peso = peso;
        this.nivel = nivel;
        this.objectivo = objectivo;
    }
    
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public float getPeso() {
        return peso;
    }
    public void setPeso(float peso) {
        this.peso = peso;
    }
    public NivelDisciplina getNivel() {
        return nivel;
    }
    public void setNivel(NivelDisciplina nivel) {
        this.nivel = nivel;
    }

    public String getObjectivo() {
        return objectivo;
    }

    public void setObjectivo(String objectivo) {
        this.objectivo = objectivo;
    }

}
