package com.imetro.domain;

import java.time.Duration;
import java.util.ArrayList;

public class Pergunta {

    private String questao;
    private ArrayList<String> respostas;
    private Duration tempo;
    private float precisao;
    private float velocidade;
    private String resposta;
    


    public Pergunta() {
        respostas = new ArrayList<>();
    }


    public String getQuestao() {
        return questao;
    }
    public void setQuestao(String questao) {
        this.questao = questao;
    }
   
    public Duration getTempo() {
        return tempo;
    }
    public void setTempo(Duration tempo) {
        this.tempo = tempo;
    }
    public float getPrecisao() {
        return precisao;
    }
    public void setPrecisao(float precisao) {
        this.precisao = precisao;
    }
    public float getVelocidade() {
        return velocidade;
    }
    public void setVelocidade(float velocidade) {
        this.velocidade = velocidade;
    }


    public ArrayList<String> getRespostas() {
        return respostas;
    }


    public void setRespostas(ArrayList<String> respostas) {
        this.respostas = respostas;
    }


    public String getResposta() {
        return resposta;
    }


    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    

}
