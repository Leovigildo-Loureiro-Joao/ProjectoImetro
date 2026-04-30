package com.imetro.ui.components.relatorio;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import jfxtras.scene.layout.VBox;

public class TimelineStep extends HBox {
    public TimelineStep(String when, String action) {
        Label whenLabel = new Label(when);
        whenLabel.getStyleClass().add("timeline-pill");
        
        Label actionLabel = new Label(action);
        actionLabel.getStyleClass().add("h3-thin-big");
        actionLabel.setWrapText(true);
        actionLabel.setPadding(new Insets(0, 0, 0, 5));
        actionLabel.setMaxHeight(100);
        actionLabel.setMinHeight(50);
        VBox content = new VBox(4, whenLabel, actionLabel);
        HBox.setHgrow(content, Priority.ALWAYS);
        
        getChildren().add(content);
        getStyleClass().add("timeline-step");
        setPadding(new Insets(12, 14, 12, 14));
    }
}
