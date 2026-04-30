package com.imetro.ui.components.relatorio;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import jfxtras.scene.layout.VBox;

public class InsightCard extends HBox {
    public InsightCard(String title, String description) {
        Label bullet = new Label(title.substring(0, 1));
        bullet.getStyleClass().add("insight-bullet");
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("h3-thin-big");
        
        Label descriptionLabel = new Label(description);
        descriptionLabel.getStyleClass().add("muted");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxHeight(100);
        descriptionLabel.setMinHeight(40);
        
        VBox content = new VBox(4, titleLabel, descriptionLabel);
        HBox.setHgrow(content, Priority.ALWAYS);
        
        getChildren().addAll(bullet, content);
        getStyleClass().add("insight-row");
        setSpacing(10);
        setPadding(new Insets(12));
    }
}

