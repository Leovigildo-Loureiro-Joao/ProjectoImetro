package com.imetro.ui.controller.diagnosticos;

import java.net.URL;
import java.util.ResourceBundle;

import com.imetro.ui.components.DiagnosticoCard;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;

public class DiagnosticoListController implements Initializable {

    @FXML
    private FlowPane diagnosticosPane;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        if (diagnosticosPane == null) {
            return;
        }

        diagnosticosPane.getChildren().clear();
        diagnosticosPane.getChildren().addAll(
            new DiagnosticoCard("Matemática", "-6%", 0.50, DiagnosticoCoordinator::requestStart),
            new DiagnosticoCard("Física", "+4%", 0.62, DiagnosticoCoordinator::requestStart),
            new DiagnosticoCard("Química", "+1%", 0.58, DiagnosticoCoordinator::requestStart),
            new DiagnosticoCard("Biologia", "-2%", 0.46, DiagnosticoCoordinator::requestStart),
            new DiagnosticoCard("Português", "+7%", 0.71, DiagnosticoCoordinator::requestStart)
        );
    }
    
}
