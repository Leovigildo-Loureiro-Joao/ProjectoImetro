package com.imetro.util;

import java.util.List;

public class ResultadoPayload {
    private final String tipoAvaliacao;
    private final String disciplina;
    private final int acertos;
    private final int erros;
    private final int totalQuestoes;
    private final double percentual;
    private final String tempo;
    private final String nivel;
    private final String perfil;
    private final String recomendacao;
    private final String retryPath;
    private final List<QuestaoResultado> questoesResultado;

    public ResultadoPayload(
            String tipoAvaliacao,
            String disciplina,
            int acertos,
            int erros,
            int totalQuestoes,
            double percentual,
            String tempo,
            String nivel,
            String perfil,
            String recomendacao,
            String retryPath,
            List<QuestaoResultado> questoesResultado
    ) {
        this.tipoAvaliacao = tipoAvaliacao;
        this.disciplina = disciplina;
        this.acertos = acertos;
        this.erros = erros;
        this.totalQuestoes = totalQuestoes;
        this.percentual = percentual;
        this.tempo = tempo;
        this.nivel = nivel;
        this.perfil = perfil;
        this.recomendacao = recomendacao;
        this.retryPath = retryPath;
        this.questoesResultado = questoesResultado == null ? List.of() : List.copyOf(questoesResultado);
    }

    public String getTipoAvaliacao() {
        return tipoAvaliacao;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public int getAcertos() {
        return acertos;
    }

    public int getErros() {
        return erros;
    }

    public int getTotalQuestoes() {
        return totalQuestoes;
    }

    public double getPercentual() {
        return percentual;
    }

    public String getTempo() {
        return tempo;
    }

    public String getNivel() {
        return nivel;
    }

    public String getPerfil() {
        return perfil;
    }

    public String getRecomendacao() {
        return recomendacao;
    }

    public String getRetryPath() {
        return retryPath;
    }

    public List<QuestaoResultado> getQuestoesResultado() {
        return questoesResultado;
    }

    
}