package com.imetro.ui.modals;

import java.util.Map;

import com.imetro.ui.controller.candidato.diagnosticos.DiagnosticoCoordinator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Labeled;
import javafx.scene.control.Toggle;
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
        String durac = getToggleText(duracao, "Curto");
        String niv = getToggleText(nivel, "Normal");
        String foc = getToggleText(foco, "Pontos fracos");

        if (event != null) {
            DiagnosticoCoordinator.requestStartInteligente(
                Map.of("duracao", durac, "nivel", niv, "foco", foc)
            );
        }
        return Map.of("duracao", durac, "nivel",niv, "foco", foc);
    }

    private String getToggleText(ToggleGroup group, String fallback) {
        Toggle selected = group == null ? null : group.getSelectedToggle();
        if (selected instanceof Labeled labeled) {
            return labeled.getText();
        }
        return fallback;
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
        DiagnosticoCoordinator.updateSubtopicosSelecionados(Map.of());
        DiagnosticoCoordinator.requestStartSoRun();
    }
}
