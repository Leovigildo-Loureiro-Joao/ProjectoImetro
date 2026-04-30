package com.imetro.ui.components.diagnostico;



import java.util.ArrayList;

import com.imetro.domain.dto.Topico;
import com.imetro.ui.components.CircleProgress;
import com.imetro.util.ImagePath;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class DiagnosticoCard extends VBox {

    private JFXCheckBox diciplina;
    private Label percentAcerto;
    private Label variacaoErro;
    private Label percentTempo;
    private Label percentEvolucion;
    private CircleProgress progressBar;
    private Label titleDesc;
    private ArrayList<Topico> topicos=new ArrayList<>();
    private JFXButton diagnosticoButton;
    private static final String bolt = "/com/imetro/assets/imgs/perc1.png";
    private static final String time = "/com/imetro/assets/imgs/perc2.png";
    private static final String evolution = "/com/imetro/assets/imgs/perc3.png";
    private static final String erro = "/com/imetro/assets/imgs/perc4.png";

    public DiagnosticoCard(String disciplina,Topico[]topico,String variacaoAcer,String variacaoErr,String variacaoTime,String variacao,double percent,Callback<ArrayList<Topico>,Void> run,Runnable massa){
        for (Topico topico2 : topico) {
            topicos.add(topico2);
        }
        this.diciplina=new JFXCheckBox(disciplina);
        percentAcerto=new Label(variacaoAcer);
        percentAcerto.getStyleClass().add("badge-new");
        progressBar=new CircleProgress(35, 35);
        
        titleDesc=new Label("Último diagnóstico");
        diciplina.getStyleClass().add("h3-thin-big");
        progressBar.setValue(percent);
        progressBar.setTranslateY(-5);
        diagnosticoButton = new JFXButton("Fazer diagnóstico");
        diagnosticoButton.getStyleClass().add("btn-primary");
        diagnosticoButton.setDisable(true);
        
        variacaoErro=new Label(variacaoErr);
        variacaoErro.getStyleClass().add("badge-new");

        percentEvolucion=new Label(variacao);
        percentEvolucion.getStyleClass().add("badge-new");

        percentTempo=new Label(variacaoTime);
        percentTempo.getStyleClass().add("badge-new");
        ImageView ptime=new ImageView(ImagePath.load(time));
        ptime.setFitWidth(16);
        ptime.setFitHeight(16);
        ImageView perro=new ImageView(ImagePath.load(erro));
        perro.setFitWidth(16);
        perro.setFitHeight(16);
        ImageView pevolution=new ImageView(ImagePath.load(evolution));
        pevolution.setFitWidth(16);
        pevolution.setFitHeight(16);
        ImageView pbolt=new ImageView(ImagePath.load(bolt));
        pbolt.setFitWidth(16);
        pbolt.setFitHeight(16);

        HBox p = new HBox(10,new VBox(5,percentTempo,ptime),new VBox(5,variacaoErro,perro),new VBox(5,percentAcerto,pbolt),new VBox(5,percentEvolucion,pevolution));
        p.setAlignment(Pos.CENTER);
        for (Object vb : p.getChildren().toArray()) {
            VBox node=(VBox)vb;
            node.setAlignment(Pos.CENTER); 
        }
        this.getChildren().addAll(diciplina,titleDesc,p,progressBar,diagnosticoButton);
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
        titleDesc.getStyleClass().addAll("card-title-secondary","h3-thin-big");
        
        //------------------------
        diciplina.setMinWidth(150);
        diciplina.setPadding(new Insets(10, 10, 10, 10));
        
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
