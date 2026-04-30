package com.imetro.ui.components.bolsas;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
;

public class FactoryRow extends VBox{
    
    public  FactoryRow(String title, double progress, String description) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("h3-thin-big");

        Label percentLabel = new Label(Math.round(progress * 100) + "%");
        percentLabel.getStyleClass().add("percent-value");

        HBox header = new HBox(10, titleLabel, percentLabel);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);

        ProgressBar bar = new ProgressBar(progress);
        bar.setPrefWidth(360);
        bar.getStyleClass().add("report-progress");

        Label descLabel = new Label(description);
        descLabel.getStyleClass().add("muted");
        descLabel.setWrapText(true);

        this.getChildren().addAll(header, bar, descLabel);
        this.getStyleClass().add("factor-row");
        this.setPadding(new Insets(12, 0, 12, 0));
        this.setSpacing(6);
    }


}
