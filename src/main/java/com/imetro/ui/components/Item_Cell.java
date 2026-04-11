package com.imetro.ui.components;

import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class Item_Cell extends HBox{
    private FontIcon image;
    private Label label;
    private String src;

    public Item_Cell(String texto, Ikon icon) {
        this.getStyleClass().add("item-cell");
        this.setAlignment(Pos.CENTER_LEFT);
        this.src=texto.toLowerCase().replace(" ", "_");
        image = new FontIcon(icon);
        image.setIconSize(18);
        image.getStyleClass().add("glyph-icon");
        label=new Label(texto);
        
        this.setSpacing(20);
        this.getChildren().addAll(image,label);
    }

    public String selectNow() {
        this.getStyleClass().add("select");
        return src;
    }

    public void deselectNow() {
        this.getStyleClass().remove("select");
    }

}
