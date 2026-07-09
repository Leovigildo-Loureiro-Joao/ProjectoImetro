package com.imetro.ui.components.relatorio;

import com.imetro.domain.dto.planejamento.LeituraRecomendada;
import com.jfoenix.controls.JFXButton;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class LeituraCard extends VBox {

    private final LeituraRecomendada leitura;
    private final JFXButton miniTesteButton;

    public LeituraCard(LeituraRecomendada leitura) {
        this.leitura = leitura;

        setCursor(javafx.scene.Cursor.HAND);

        Label tituloLabel = new Label(leitura.tituloLivro());
        tituloLabel.getStyleClass().add("h3-thin-big");

        Label topicoLabel = new Label(leitura.topico() + " (" + leitura.formatarPaginas() + ")");
        topicoLabel.getStyleClass().add("muted");
        topicoLabel.setWrapText(true);

        VBox infoBox = new VBox(2, tituloLabel, topicoLabel);

        double progresso = Math.max(0, Math.min(1, leitura.progressoLeitura() / 100.0));

        ProgressBar bar = new ProgressBar(progresso);
        bar.setPrefWidth(Double.MAX_VALUE);
        bar.getStyleClass().add("progresso-bar");

        Label percentLabel = new Label(String.format("%.0f%%", leitura.progressoLeitura()));
        percentLabel.getStyleClass().add("progresso-texto");

        HBox progressoBox = new HBox(8, bar, percentLabel);
        progressoBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(bar, Priority.ALWAYS);

        miniTesteButton = new JFXButton("Fazer mini teste");
        miniTesteButton.getStyleClass().add("btn-ler");
        miniTesteButton.setPrefHeight(30);
        miniTesteButton.setDisable(progresso < 0.5);

        HBox acoesBox = new HBox(miniTesteButton);
        acoesBox.setAlignment(Pos.CENTER_RIGHT);

        getChildren().addAll(infoBox, progressoBox, acoesBox);
        setSpacing(8);
        setPadding(new Insets(12));
        setMaxWidth(Double.MAX_VALUE);
        getStyleClass().addAll("weekly-mini-card", "report-note-card");
    }

    public LeituraRecomendada getLeitura() {
        return leitura;
    }

    public JFXButton getMiniTesteButton() {
        return miniTesteButton;
    }

    public void setOnOpenBook(Runnable action) {
        setOnMouseClicked(e -> {
            if (e.getTarget() != miniTesteButton) {
                action.run();
            }
        });
    }
}
