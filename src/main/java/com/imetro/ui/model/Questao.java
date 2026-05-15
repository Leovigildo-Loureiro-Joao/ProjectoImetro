package com.imetro.ui.model;

import javafx.scene.image.Image;

public class Questao {
    private static final int TOTAL_OPCOES = 7;

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
    private double rigor;
    private String referenciaLivro;
    private Integer paginaInicio;
    private Integer paginaFim;
    private String topicoPrincipal;
    private double[] pesosResposta;

    
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
    public double getRigor() {
        return rigor;
    }
    public void setRigor(double rigor) {
        this.rigor = rigor;
    }
    public String getReferenciaLivro() {
        return referenciaLivro;
    }
    public void setReferenciaLivro(String referenciaLivro) {
        this.referenciaLivro = referenciaLivro;
    }
    public Integer getPaginaInicio() {
        return paginaInicio;
    }
    public void setPaginaInicio(Integer paginaInicio) {
        this.paginaInicio = paginaInicio;
    }
    public Integer getPaginaFim() {
        return paginaFim;
    }
    public void setPaginaFim(Integer paginaFim) {
        this.paginaFim = paginaFim;
    }
    public String getTopicoPrincipal() {
        return topicoPrincipal;
    }
    public void setTopicoPrincipal(String topicoPrincipal) {
        this.topicoPrincipal = topicoPrincipal;
    }
    public double[] getPesosResposta() {
        return pesosResposta == null ? null : pesosResposta.clone();
    }
    public void setPesosResposta(double[] pesosResposta) {
        if (pesosResposta == null) {
            this.pesosResposta = null;
            return;
        }

        this.pesosResposta = new double[TOTAL_OPCOES];
        int limite = Math.min(TOTAL_OPCOES, pesosResposta.length);
        for (int i = 0; i < limite; i++) {
            double valor = pesosResposta[i];
            this.pesosResposta[i] = Double.isFinite(valor) ? valor : 0d;
        }
    }
    public Double getPesoResposta(char letra) {
        if (pesosResposta == null) {
            return null;
        }

        int indice = Character.toUpperCase(letra) - 'A';
        if (indice < 0 || indice >= pesosResposta.length) {
            return null;
        }

        return pesosResposta[indice];
    }
    
    // Construtores, getters e setters...
    
} 
