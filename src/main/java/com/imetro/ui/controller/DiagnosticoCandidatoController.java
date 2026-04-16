package com.imetro.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.imetro.ui.components.DiagnosticoCard;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;


public class DiagnosticoCandidatoController implements Initializable{
    @FXML
    private FlowPane diagnoticos;
    @FXML
    private VBox end;

    @FXML
    private VBox start;



    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
       Diagnosticar(false);
       BuscarDiagnsoticos();
    }

    public void BuscarDiagnsoticos(){
        for (int i = 0; i < 5; i++) {
            diagnoticos.getChildren().add(new DiagnosticoCard("Matematica", "6%+ do anterior",0.1,()-> Diagnosticar(true)));

        }
    }

    private void Diagnosticar(boolean p){
        start.setVisible(!p);
        end.setVisible(p);
    }
    
}
