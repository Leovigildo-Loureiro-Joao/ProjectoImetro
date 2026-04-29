package com.imetro.ui.components;

import com.imetro.util.QuestaoResultado;

import javafx.scene.control.Label;
import jfxtras.scene.layout.VBox;

public class CardQuestao extends VBox{
     public CardQuestao(QuestaoResultado questao) {
        this.setSpacing(8);
        this.setPrefWidth(360);
        this.setMinWidth(320);
        this.getStyleClass().add("sub-card");
        this.setStyle(
            "-fx-padding: 12;" +
            "-fx-border-width: 1.5;" +
            "-fx-border-radius: 12;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: " + (questao.isAcertou() ? "#10b981" : "#ef4444") + ";"
        );

        Label status = new Label(questao.isAcertou() ? "Acertou" : "Errou");
        status.getStyleClass().add("h3-thin");
        status.setStyle("-fx-text-fill: " + (questao.isAcertou() ? "#10b981" : "#ef4444") + "; -fx-font-weight: 700;");

        Label titulo = new Label(
            "Questao " + questao.getOrdem() + " - "
                + questao.getDisciplina()
                + " / "
                + questao.getTopico()
                + " / "
                + questao.getSubtopico()
        );
        titulo.getStyleClass().add("h3-thin");

        Label enunciado = new Label(questao.getEnunciado());
        enunciado.getStyleClass().add("h3-thin-big");
        enunciado.setWrapText(true);

        Label bloco2 = new Label(questao.getBloco2());
        bloco2.getStyleClass().add("muted");
        bloco2.setWrapText(true);
        bloco2.setVisible(!"-".equals(questao.getBloco2()));
        bloco2.setManaged(!"-".equals(questao.getBloco2()));

        Label respostaUsuario = new Label(
            "Sua resposta: " + questao.getRespostaUsuario() + " - " + questao.getTextoRespostaUsuario()
        );
        respostaUsuario.getStyleClass().add("h3-thin");
        respostaUsuario.setWrapText(true);

        Label respostaCorreta = new Label(
            "Resposta correta: " + questao.getRespostaCorreta() + " - " + questao.getTextoRespostaCorreta()
        );
        respostaCorreta.getStyleClass().add("h3-thin");
        respostaCorreta.setStyle("-fx-text-fill: #2563eb;");
        respostaCorreta.setWrapText(true);

        this.getChildren().addAll(status, titulo, enunciado, bloco2, respostaUsuario, respostaCorreta);
    }
}
