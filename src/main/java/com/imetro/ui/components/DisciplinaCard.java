package com.imetro.ui.components;


import java.util.Map;

import com.imetro.util.DisciplinaCatalog.DisciplinaSeed;
import com.jfoenix.controls.JFXCheckBox;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DisciplinaCard extends HBox{
    public DisciplinaCard(DisciplinaSeed seed, Map<String, Integer> counts) {
        JFXCheckBox cb = new JFXCheckBox(seed.nome());
        cb.getStyleClass().add("disciplina-check");
        cb.setStyle("-fx-font-weight: 700;");

        Label meta = new Label("Nível: " + seed.nivel() + "  •  Peso: " + seed.peso());
        meta.getStyleClass().add("muted");
        meta.setStyle("-fx-font-size: 11px;");

        VBox left = new VBox(2, cb, meta);
        left.setPrefWidth(260);

        int total = counts.getOrDefault(seed.nome(), 0);
        Label badge = new Label(total > 0 ? "Orientador disponível" : "Sem orientador");
        badge.setStyle(total > 0
                ? "-fx-text-fill: #0f5132; -fx-background-color: rgba(25,135,84,0.18); -fx-padding: 4 8; -fx-background-radius: 999;"
                : "-fx-text-fill: #842029; -fx-background-color: rgba(220,53,69,0.14); -fx-padding: 4 8; -fx-background-radius: 999;");

       this.getChildren().addAll(left,badge);
       this.setSpacing(10);
        this.setAlignment(Pos.TOP_LEFT);
        this.setStyle("-fx-padding: 10 10;");
    }
}
