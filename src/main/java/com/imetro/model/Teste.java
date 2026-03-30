package com.imetro.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class Teste {
    
    private UUID idTeste;
    private ArrayList<Pergunta> perguntas;
    private LocalDateTime dataTeste;
    private float resultado;
    private Orientador orientador;
    private Candidato candidato;
    private Relatorio relatorio;
    
    

    public Teste() {
        idTeste=UUID.randomUUID();
    }

    

    public LocalDateTime getDataTeste() {
        return dataTeste;
    }

    public void setDataTeste(LocalDateTime dataTeste) {
        this.dataTeste = dataTeste;
    }

    public Orientador getOrientador() {
        return orientador;
    }

    public void setOrientador(Orientador orientador) {
        this.orientador = orientador;
    }

    public Candidato getCandidato() {
        return candidato;
    }

    public void setCandidato(Candidato candidato) {
        this.candidato = candidato;
    }

    public Relatorio getRelatorio() {
        return relatorio;
    }

    public void setRelatorio(Relatorio relatorio) {
        this.relatorio = relatorio;
    }

    public ArrayList<Pergunta> getPerguntas() {
        return perguntas;
    }

    public void setPerguntas(ArrayList<Pergunta> perguntas) {
        this.perguntas = perguntas;   
    }

    public UUID getIdTeste() {
        return idTeste;
    }

    public void setIdTeste(UUID idTeste) {
        this.idTeste = idTeste;
    }



    public float getResultado() {
        return resultado;
    }



    public void setResultado(float resultado) {
        this.resultado = resultado;
    }

    
    
}