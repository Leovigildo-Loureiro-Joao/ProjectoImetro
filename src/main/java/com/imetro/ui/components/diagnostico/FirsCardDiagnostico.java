package com.imetro.ui.components.diagnostico;

import java.util.ArrayList;

import com.imetro.domain.dto.diagnostico.PrimeiroDiagnosticoResumo;
import com.imetro.ui.controller.candidato.diagnosticos.DiagnosticoCoordinator;
import com.jfoenix.controls.JFXButton;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class FirsCardDiagnostico extends VBox{
    public  FirsCardDiagnostico(PrimeiroDiagnosticoResumo resumo) {
        Label badge = new Label(
            resumo.totalDisciplinas() + " disciplinas | "
                + resumo.totalTopicos() + " topicos | "
                + resumo.totalQuestoes() + " questoes"
        );
        badge.getStyleClass().add("diagnostico-first-badge");

        Label titulo = new Label("Primeiro diagnostico");
        titulo.getStyleClass().add("diagnostico-first-title");

        Label descricao = new Label(resumo.detalhe());
        descricao.getStyleClass().add("diagnostico-card-summary");
        descricao.setWrapText(true);

        Label apoio = new Label(
            resumo.disciplinasSemBase().isEmpty()
                ? "Os topicos vao abrir no modal para voce escolher por onde quer comecar."
                : "Ainda sem base real para: " + String.join(", ", resumo.disciplinasSemBase()) + "."
        );
        apoio.getStyleClass().add("diagnostico-card-note");
        apoio.setWrapText(true);

        JFXButton iniciarButton = new JFXButton("Escolher topicos e comecar");
        iniciarButton.getStyleClass().addAll("btn-primary", "diagnostico-first-action");
        iniciarButton.setDisable(!resumo.pronto());
        iniciarButton.setOnAction(event -> {
            if (resumo.pronto()) {
                DiagnosticoCoordinator.requestStart(new ArrayList<>(resumo.topicos()));
            }
        });

        HBox topo = new HBox(12, titulo, criarSpacer(), badge);
        topo.setAlignment(Pos.CENTER_LEFT);

        this.getChildren().addAll(topo, descricao, apoio, iniciarButton);
        this.setSpacing(16);
        this.getStyleClass().addAll("placeholder-card", "diagnostico-first-card");
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(22));
        this.setMaxWidth(760);
    }

    private Region criarSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

}
