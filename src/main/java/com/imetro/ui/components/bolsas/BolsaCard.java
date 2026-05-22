package com.imetro.ui.components.bolsas;

import com.imetro.domain.dto.bolsa.BolsaMock;
import com.jfoenix.controls.JFXButton;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class BolsaCard extends VBox {

    public BolsaCard(BolsaMock bolsa, Runnable onAction) {
        Label tipoLabel = new Label(bolsa.tipo());
        tipoLabel.getStyleClass().add("timeline-pill");

        Label statusLabel = new Label(bolsa.status());
        statusLabel.getStyleClass().add(bolsa.disponivel() ? "pill-good" : "pill-warn");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox topRow = new HBox(10, tipoLabel, spacer, statusLabel);

        Label nomeLabel = new Label(bolsa.nome());
        nomeLabel.getStyleClass().add("h1");
        nomeLabel.setWrapText(true);

        Label coberturaLabel = new Label(bolsa.cobertura());
        coberturaLabel.getStyleClass().add("h3-thin-big");
        coberturaLabel.setWrapText(true);

        Label janelaLabel = new Label(bolsa.janela());
        janelaLabel.getStyleClass().add("timeline-pill");

        Label vagasLabel = new Label(bolsa.vagas() + " vagas");
        vagasLabel.getStyleClass().add("timeline-pill");

        Label dificuldadeLabel = new Label(bolsa.dificuldade());
        dificuldadeLabel.getStyleClass().add("timeline-pill");

        FlowPane metaRow = new FlowPane(8, 8, janelaLabel, vagasLabel, dificuldadeLabel);
        metaRow.getStyleClass().add("scholarship-meta-wrap");

        Label matchLabel = new Label(bolsa.match() + "% pronto");
        matchLabel.getStyleClass().add(bolsa.pillClass());

        ProgressBar matchBar = new ProgressBar(bolsa.match() / 100.0);
        matchBar.setPrefWidth(280);
        matchBar.getStyleClass().add("report-progress");

        Label destaqueLabel = new Label(bolsa.destaque());
        destaqueLabel.getStyleClass().add("muted");
        destaqueLabel.setWrapText(true);

        Label criterioLabel = new Label("Entrada: " + bolsa.criterioResumo());
        criterioLabel.getStyleClass().add("muted");
        criterioLabel.setWrapText(true);

        Label riscoLabel = new Label("Atencao: " + bolsa.risco());
        riscoLabel.getStyleClass().add("muted");
        riscoLabel.setWrapText(true);

        JFXButton bolsaButton = new JFXButton(bolsa.acaoLabel());
        bolsaButton.getStyleClass().add(bolsa.disponivel() ? "btn-primary" : "btn-secondary");
        bolsaButton.setDisable(!bolsa.disponivel());
        bolsaButton.setOnAction(event -> {
            if (onAction != null) {
                onAction.run();
            }
        });

        getChildren().addAll(
            topRow,
            nomeLabel,
            coberturaLabel,
            metaRow,
            matchLabel,
            matchBar,
            destaqueLabel,
            criterioLabel,
            riscoLabel,
            bolsaButton
        );
        setSpacing(10);
        getStyleClass().addAll("card-blur", "shadow", "scholarship-card");
        if (bolsa.disponivel() && bolsa.match() >= 85) {
            getStyleClass().add("scholarship-card-strong");
        }
        if (!bolsa.disponivel()) {
            getStyleClass().add("scholarship-card-locked");
        }
        setPadding(new Insets(16));
        setPrefWidth(320);
        setMinHeight(340);
    }
}
