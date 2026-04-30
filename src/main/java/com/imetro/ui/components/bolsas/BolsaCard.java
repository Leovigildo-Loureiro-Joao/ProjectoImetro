package com.imetro.ui.components.bolsas;

import com.imetro.domain.dto.BolsaMock;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class BolsaCard extends VBox{
    public  BolsaCard(BolsaMock bolsa) {
        Label tipoLabel = new Label(bolsa.tipo());
        tipoLabel.getStyleClass().add("h3-thin");

        Label nomeLabel = new Label(bolsa.nome());
        nomeLabel.getStyleClass().add("h1");
        nomeLabel.setWrapText(true);

        Label matchLabel = new Label(bolsa.match() + "% match");
        matchLabel.getStyleClass().add(bolsa.pillClass());

        Label coberturaLabel = new Label(bolsa.cobertura());
        coberturaLabel.getStyleClass().add("h3-thin-big");
        coberturaLabel.setWrapText(true);

        Label prazoLabel = new Label(bolsa.prazo());
        prazoLabel.getStyleClass().add("timeline-pill");

        Label destaqueLabel = new Label(bolsa.destaque());
        destaqueLabel.getStyleClass().add("muted");
        destaqueLabel.setWrapText(true);

        Label riscoLabel = new Label("Ponto de atencao: " + bolsa.risco());
        riscoLabel.getStyleClass().add("muted");
        riscoLabel.setWrapText(true);

        ProgressBar matchBar = new ProgressBar(bolsa.match() / 100.0);
        matchBar.setPrefWidth(260);
        matchBar.getStyleClass().add("report-progress");

        HBox topRow = new HBox(10, tipoLabel, matchLabel);
        VBox.setVgrow(matchBar, Priority.NEVER);

        this.getChildren().addAll(topRow, nomeLabel, coberturaLabel, prazoLabel, matchBar, destaqueLabel, riscoLabel);
        this.setSpacing(10);
        this.getStyleClass().addAll("card-blur", "shadow", "scholarship-card");
        if (bolsa.match() >= 85) {
            this.getStyleClass().add("scholarship-card-strong");
        }
        this.setPadding(new Insets(16));
        this.setPrefWidth(248);
        this.setMinHeight(250);
    }
}
