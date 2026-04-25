package com.imetro.ui.modals;

import java.util.Map;

import com.imetro.ui.controller.candidato.diagnosticos.DiagnosticoCoordinator;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;

public class DificultModalController extends ModalController{

    @FXML
    private ToggleGroup duracao;

    @FXML
    private ToggleGroup foco;

    @FXML
    private ToggleGroup nivel;


    @FXML
    public Map<String,String> InteligentDiagnostic(ActionEvent event) {
        System.out.println();
        String durac=duracao.getSelectedToggle().toString().split("]")[1].replace("'", " ").trim();
        String niv=nivel.getSelectedToggle().toString().split("]")[1].replace("'", " ").trim();
        String foc=foco.getSelectedToggle().toString().split("]")[1].replace("'", " ").trim();
        if(event!=null)
            Platform.runLater(DiagnosticoCoordinator::requestStartInteligente);
        Close(event);
        return Map.of("duracao", durac, "nivel",niv, "foco", foc);
    }

    @FXML
    public void SelectDuracao(ActionEvent event) {

    }

    @FXML
    public void SelectFocos(ActionEvent event) {

    }

    @FXML
    public void SelectTesteDificult(ActionEvent event) {

    }

    @FXML
    public void SoRun(ActionEvent event) {
        DiagnosticoCoordinator.requestStartSoRun();
    }
}
