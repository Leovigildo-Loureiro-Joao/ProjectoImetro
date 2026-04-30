package com.imetro.ui.components.bolsas;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SectionTitle extends VBox{
     public SectionTitle(String title, String subtitle) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("h1-thin");

        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.getStyleClass().add("muted");
        subtitleLabel.setWrapText(true);

        this.getChildren().addAll( titleLabel, subtitleLabel);
        this.setSpacing(6);
    }
}
