package com.imetro.ui.components;



import java.util.ArrayList;

import com.imetro.domain.dto.Topico;
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
    private ArrayList<Topico> topicos=new ArrayList<>();
    private JFXButton diagnosticoButton;

    public DiagnosticoCard(String disciplina,Topico[]topico,String percent1,double percent,Callback<ArrayList<Topico>,Void> run,Runnable massa){
        for (Topico topico2 : topico) {
            topicos.add(topico2);
        }
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
         // Enable button only when checkbox is selected
        diciplina.selectedProperty().addListener((obs, oldVal, newVal) -> {
            diagnosticoButton.setDisable(!newVal);
            massa.run();
        });
    }

    private void StyleConfig(){
        this.getStyleClass().add("card");
        this.setPadding(new Insets(14));
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        this.setPadding(new Insets(10,0,20,0));
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
        
       
    }

    public void Action(Callback<ArrayList<Topico>,Void> action){
        diagnosticoButton.setOnAction(arg0 -> {
            action.call(topicos);
        });

    }

    public JFXCheckBox getDiciplina() {
        return diciplina;
    }

    public void setDiciplina(JFXCheckBox diciplina) {
        this.diciplina = diciplina;
    }

    public Label getPercentAcerto() {
        return percentAcerto;
    }
    
    public void setPercentAcerto(Label percentAcerto) {
        this.percentAcerto = percentAcerto;
    }

    public CircleProgress getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(CircleProgress progressBar) {
        this.progressBar = progressBar;
    }

    public Label getTitleDesc() {
        return titleDesc;
    }

    public void setTitleDesc(Label titleDesc) {
        this.titleDesc = titleDesc;
    }

    public JFXButton getDiagnosticoButton() {
        return diagnosticoButton;
    }

    public void setDiagnosticoButton(JFXButton diagnosticoButton) {
        this.diagnosticoButton = diagnosticoButton;
    }

    public ArrayList<Topico> getTopicos() {
        return topicos;
    }

    public void setTopicos(ArrayList<Topico> topicos) {
        this.topicos = topicos;
    }

    public void setSelecionado(boolean selecionado) {
        diciplina.setSelected(selecionado);
        diagnosticoButton.setDisable(!selecionado);
    }

    

}
