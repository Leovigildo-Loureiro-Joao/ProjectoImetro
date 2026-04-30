package com.imetro.ui.components.bolsas;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import jfxtras.scene.layout.HBox;

public class StepRow extends HBox{
       
    public StepRow(String number, String description) {
        Label numberLabel = new Label(number);
        numberLabel.getStyleClass().add("insight-bullet");

        Label descriptionLabel = new Label(description);
        descriptionLabel.getStyleClass().add("h3-thin-big");
        descriptionLabel.setWrapText(true);

        this.getChildren().addAll(numberLabel, descriptionLabel);
        this.getStyleClass().add("timeline-step");
        this.setPadding(new Insets(12, 14, 12, 14));
        this.setSpacing(12);
    }
}
