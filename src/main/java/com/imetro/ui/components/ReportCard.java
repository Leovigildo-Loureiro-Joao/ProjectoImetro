package com.imetro.ui.components;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ReportCard extends HBox{
    public  ReportCard(String tipo, String disciplina, String resumo, String momento, String pillClass) {
        Label tipoLabel = new Label(tipo);
        tipoLabel.getStyleClass().add("h3-thin");

        Label disciplinaLabel = new Label(disciplina);
        disciplinaLabel.getStyleClass().add("h3-thin-big");

        Label resumoLabel = new Label(resumo);
        resumoLabel.getStyleClass().add("muted");
        resumoLabel.setWrapText(true);

        VBox textBox = new VBox(4, tipoLabel, disciplinaLabel, resumoLabel);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        Label momentoLabel = new Label(momento);
        momentoLabel.getStyleClass().add(pillClass);
        this.setSpacing(12);
        this.getChildren().addAll(textBox, momentoLabel);
        this.getStyleClass().add("report-card-row");
        this.setPadding(new Insets(12, 14, 12, 14));
    }

}
