package com.imetro.ui.components;

import com.imetro.domain.dto.disciplina.DisciplinaDto;
import com.jfoenix.controls.JFXCheckBox;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public class DisciplinaCard extends HBox {

    private final DisciplinaDto disciplina;
    private final JFXCheckBox nomeLabel;
    private final TextArea subtopicosField;

    public DisciplinaCard(DisciplinaDto disciplina) {
        super();
        this.disciplina = disciplina;

        nomeLabel = new JFXCheckBox(disciplina.nome());
        nomeLabel.getStyleClass().add("muted");
        nomeLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Label objetivoLabel = new Label(normalizarObjetivo(disciplina.objectivo()));
        objetivoLabel.setWrapText(true);
        objetivoLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #666;");

        Label subtopicosLabel = new Label("Subtopicos prioritarios para a bolsa");
        subtopicosLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #888;");

        subtopicosField = new TextArea();
        subtopicosField.setPromptText("Ex: Fracoes, Equacoes, Geometria");
        subtopicosField.setPrefRowCount(3);
        subtopicosField.setPrefHeight(82);
        subtopicosField.setWrapText(true);
        subtopicosField.setMaxWidth(Double.MAX_VALUE);
        subtopicosField.setDisable(true);
        subtopicosField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isBlank()) {
                nomeLabel.setSelected(true);
            }
        });

        nomeLabel.selectedProperty().addListener((obs, oldValue, selected) -> {
            subtopicosField.setDisable(!selected);
            if (selected && subtopicosField.getText() != null && subtopicosField.getText().isBlank()) {
                subtopicosField.requestFocus();
            }
        });

        VBox left = new VBox(6, nomeLabel, objetivoLabel, subtopicosLabel, subtopicosField);
        left.setPrefWidth(320);
        left.setSpacing(6);
        left.setFillWidth(true);

        this.getChildren().add(left);
        this.setSpacing(10);
        this.setAlignment(Pos.TOP_LEFT);
        this.setStyle("-fx-padding: 10 10; -fx-border-color: #ddd; -fx-border-radius: 5;");
    }

    public DisciplinaDto getDisciplina() {
        return disciplina;
    }

    public boolean isSelecionada() {
        return nomeLabel.isSelected();
    }

    public String getSubtopicosFoco() {
        if (subtopicosField.getText() == null) {
            return "";
        }

        LinkedHashSet<String> subtopicos = Arrays.stream(subtopicosField.getText().split("[\\n,;]+"))
            .map(String::trim)
            .filter(item -> !item.isBlank())
            .collect(Collectors.toCollection(LinkedHashSet::new));

        return String.join(", ", subtopicos);
    }

    private String normalizarObjetivo(String objetivo) {
        if (objetivo == null || objetivo.isBlank()) {
            return "Escolhe os subtopicos que queres consolidar nesta disciplina.";
        }
        return objetivo;
    }
}
