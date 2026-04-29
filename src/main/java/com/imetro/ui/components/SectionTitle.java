package com.imetro.ui.components;



import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;

public class SectionTitle extends VBox{

    public SectionTitle(String title, String subtitle) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("h1-thin");

        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.getStyleClass().add("muted");
        subtitleLabel.setWrapText(true);

        Separator separator = new Separator();
        separator.getStyleClass().add("subtle-separator");

        this.getChildren().addAll( titleLabel, subtitleLabel, separator);
        this.setSpacing(6);
    }
}
