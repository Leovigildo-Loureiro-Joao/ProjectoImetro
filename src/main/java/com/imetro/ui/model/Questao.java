package com.imetro.ui.model;

import javafx.scene.image.Image;

public class Questao {
    private String id;
    private String disciplina;
    private String topico;
    private String subtopico;
    private String enunciado;
    private String bloco2; // texto adicional
    private Image imagem; // imagem opcional
    private String opcaoA;
    private String opcaoB;
    private String opcaoC;
    private String opcaoD;
    private String opcaoE; // "Não sei como fazer"
    private String opcaoF; // "To confuso"
    private String opcaoG; // "Prefiro pular"
    private char respostaCorreta; // A, B, C, D
    private int nivelDificuldade; // 1-5
    private double tempoSugerido; // segundos

    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDisciplina() {
        return disciplina;
    }
    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }
    public String getTopico() {
        return topico;
    }
    public void setTopico(String topico) {
        this.topico = topico;
    }
    public String getSubtopico() {
        return subtopico;
    }
    public void setSubtopico(String subtopico) {
        this.subtopico = subtopico;
    }
    public String getEnunciado() {
        return enunciado;
    }
    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }
    public String getBloco2() {
        return bloco2;
    }
    public void setBloco2(String bloco2) {
        this.bloco2 = bloco2;
    }
    public Image getImagem() {
        return imagem;
    }
    public void setImagem(Image imagem) {
        this.imagem = imagem;
    }
    public String getOpcaoA() {
        return opcaoA;
    }
    public void setOpcaoA(String opcaoA) {
        this.opcaoA = opcaoA;
    }
    public String getOpcaoB() {
        return opcaoB;
    }
    public void setOpcaoB(String opcaoB) {
        this.opcaoB = opcaoB;
    }
    public String getOpcaoC() {
        return opcaoC;
    }
    public void setOpcaoC(String opcaoC) {
        this.opcaoC = opcaoC;
    }
    public String getOpcaoD() {
        return opcaoD;
    }
    public void setOpcaoD(String opcaoD) {
        this.opcaoD = opcaoD;
    }
    public String getOpcaoE() {
        return opcaoE;
    }
    public void setOpcaoE(String opcaoE) {
        this.opcaoE = opcaoE;
    }
    public String getOpcaoF() {
        return opcaoF;
    }
    public void setOpcaoF(String opcaoF) {
        this.opcaoF = opcaoF;
    }
    public String getOpcaoG() {
        return opcaoG;
    }
    public void setOpcaoG(String opcaoG) {
        this.opcaoG = opcaoG;
    }
    public char getRespostaCorreta() {
        return respostaCorreta;
    }
    public void setRespostaCorreta(char respostaCorreta) {
        this.respostaCorreta = respostaCorreta;
    }
    public int getNivelDificuldade() {
        return nivelDificuldade;
    }
    public void setNivelDificuldade(int nivelDificuldade) {
        this.nivelDificuldade = nivelDificuldade;
    }
    public double getTempoSugerido() {
        return tempoSugerido;
    }
    public void setTempoSugerido(double tempoSugerido) {
        this.tempoSugerido = tempoSugerido;
    }
    
    // Construtores, getters e setters...
    
} 
