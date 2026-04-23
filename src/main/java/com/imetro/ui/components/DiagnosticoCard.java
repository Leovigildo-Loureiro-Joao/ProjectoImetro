package com.imetro.ui.components;



import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class DiagnosticoCard extends VBox {

    private JFXCheckBox diciplina;
    private Label percentAcerto;
    private CircleProgress progressBar;
    private Label titleDesc;
    
    private JFXButton diagnosticoButton;

    public DiagnosticoCard(String disciplina,String percent1,double percent,Runnable run){
        this.diciplina=new JFXCheckBox(disciplina);
        percentAcerto=new Label(percent1);
        progressBar=new CircleProgress(35, 35);
        titleDesc=new Label("Último diagnóstico");
        progressBar.setValue(percent);
        
        diagnosticoButton = new JFXButton("Fazer diagnóstico");
        diagnosticoButton.getStyleClass().add("btn-primary");
        diagnosticoButton.setDisable(true);
        
        this.getChildren().addAll(diciplina,titleDesc,percentAcerto,progressBar,diagnosticoButton);
        StyleConfig();
        Action(run);
    }

    private void StyleConfig(){
        this.getStyleClass().add("card");
        this.setPadding(new Insets(14));
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        this.setPadding(new Insets(0,0,40,0));
        this.setMaxWidth(200);
        
        this.setMinWidth(200);
         //-----------------------
        titleDesc.getStyleClass().add("card-title-secondary");
        
        
        //------------------------
        diciplina.setMinWidth(150);
        diciplina.setPadding(new Insets(10, 10, 10, 10));
        diciplina.setAlignment(Pos.CENTER);
        
        // Configure button
        diagnosticoButton.setMaxWidth(150);
        diagnosticoButton.setPrefHeight(35);
        
        // Enable button only when checkbox is selected
        diciplina.selectedProperty().addListener((obs, oldVal, newVal) -> {
            diagnosticoButton.setDisable(!newVal);
        });
    }

    public void Action(Runnable action){
        diagnosticoButton.setOnAction(arg0 -> {
            action.run();
        });

    }

}
