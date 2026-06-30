package com.imetro.util;

import com.imetro.ui.model.Questao;

public  class QuestaoResultado {
    private final int ordem;
    private final String disciplina;
    private final String topico;
    private final String subtopico;
    private final String enunciado;
    private final String bloco2;
    private final char respostaUsuario;
    private final char respostaCorreta;
    private final String textoRespostaUsuario;
    private final String textoRespostaCorreta;
    private final boolean acertou;
    private final String referenciaLivro;
    private final int paginaInicio;
    private final int paginaFim;

    public QuestaoResultado(
            int ordem,
            String disciplina,
            String topico,
            String subtopico,
            String enunciado,
            String bloco2,
            char respostaUsuario,
            char respostaCorreta,
            String textoRespostaUsuario,
            String textoRespostaCorreta,
            boolean acertou,
            String referenciaLivro,
            int paginaInicio,
            int paginaFim
    ) {
        this.ordem = ordem;
        this.disciplina = disciplina;
        this.topico = topico;
        this.subtopico = subtopico;
        this.enunciado = enunciado;
        this.bloco2 = bloco2;
        this.respostaUsuario = respostaUsuario;
        this.respostaCorreta = respostaCorreta;
        this.textoRespostaUsuario = textoRespostaUsuario;
        this.textoRespostaCorreta = textoRespostaCorreta;
        this.acertou = acertou;
        this.referenciaLivro = referenciaLivro;
        this.paginaInicio = paginaInicio;
        this.paginaFim = paginaFim;
    }

    public static QuestaoResultado fromQuestao(int ordem, Questao questao, char respostaUsuario) {
        char usuario = Character.toUpperCase(respostaUsuario);
        char correta = QuestaoUtil.resolverAlternativaCorreta(questao);
        boolean acertou = QuestaoUtil.respostaEstaCorreta(questao, usuario);
        String refLivro = questao.getReferenciaLivro();
        int pagInicio = questao.getPaginaInicio() != null ? questao.getPaginaInicio() : 0;
        int pagFim = questao.getPaginaFim() != null ? questao.getPaginaFim() : 0;
        return new QuestaoResultado(
            ordem,
            valueOrDash(questao.getDisciplina()),
            valueOrDash(questao.getTopico()),
            valueOrDash(questao.getSubtopico()),
            valueOrDash(questao.getEnunciado()),
            valueOrDash(questao.getBloco2()),
            usuario == '\0' ? '-' : usuario,
            correta == '\0' ? '-' : correta,
            resolveTextoOpcao(questao, usuario),
            resolveTextoOpcao(questao, correta),
            acertou,
            refLivro != null && !refLivro.isBlank() ? refLivro : null,
            pagInicio,
            pagFim
        );
    }

    private static String resolveTextoOpcao(Questao questao, char letra) {
        return switch (Character.toUpperCase(letra)) {
            case 'A' -> valueOrDash(questao.getOpcaoA());
            case 'B' -> valueOrDash(questao.getOpcaoB());
            case 'C' -> valueOrDash(questao.getOpcaoC());
            case 'D' -> valueOrDash(questao.getOpcaoD());
            case 'E' -> valueOrDash(questao.getOpcaoE());
            case 'F' -> valueOrDash(questao.getOpcaoF());
            case 'G' -> valueOrDash(questao.getOpcaoG());
            default -> "Sem resposta";
        };
    }

    public int getOrdem() {
        return ordem;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public String getTopico() {
        return topico;
    }

    public String getSubtopico() {
        return subtopico;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public String getBloco2() {
        return bloco2;
    }

    public char getRespostaUsuario() {
        return respostaUsuario;
    }

    public char getRespostaCorreta() {
        return respostaCorreta;
    }

    public String getTextoRespostaUsuario() {
        return textoRespostaUsuario;
    }

    public String getTextoRespostaCorreta() {
        return textoRespostaCorreta;
    }

    public boolean isAcertou() {
        return acertou;
    }

    public String getReferenciaLivro() {
        return referenciaLivro;
    }

    public int getPaginaInicio() {
        return paginaInicio;
    }

    public int getPaginaFim() {
        return paginaFim;
    }

     private static String valueOrDash(String value) {
        if (value == null || value.isBlank()) {
            return "-";
        }
        return value;
    }

    
}
