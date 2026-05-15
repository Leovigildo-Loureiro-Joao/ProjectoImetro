package com.imetro.ui.modals;

import java.util.Map;

import com.imetro.domain.enums.NivelDificuldadeAdaptativa;
import com.imetro.ui.controller.candidato.diagnosticos.DiagnosticoCoordinator;
import com.imetro.ui.controller.candidato.testes.TesteAdaptativoCoordinator;
import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

public class DificultModalController extends ModalController {

    @FXML
    private Label tituloLabel;

    @FXML
    private JFXButton iniciarButton;

    @FXML
    private JFXButton padraoButton;

    @FXML
    private ToggleGroup duracao;

    @FXML
    private ToggleGroup foco;

    @FXML
    private ToggleGroup nivel;

    @Override
    public void init() {
        if (FluxoModalContext.isTesteAdaptativo()) {
            tituloLabel.setText("Configurando Teste...");
            iniciarButton.setText("Continuar com foco inteligente");
            padraoButton.setText("Usar configuracao padrao");
        } else {
            tituloLabel.setText("Iniciando Diagnostico...");
            iniciarButton.setText("Iniciar diagnostico inteligente");
            padraoButton.setText("Pular e fazer apenas o padrao");
        }
        super.init();
    }

    @FXML
    public Map<String, String> InteligentDiagnostic(ActionEvent event) {
        String durac = getToggleText(duracao, "Curto");
        String niv = getToggleText(nivel, NivelDificuldadeAdaptativa.padrao().rotulo());
        String foc = getToggleText(foco, "Pontos fracos");

        if (event != null) {
            Map<String, String> configuracao = Map.of("duracao", durac, "nivel", niv, "foco", foc);
            if (FluxoModalContext.isTesteAdaptativo()) {
                TesteAdaptativoCoordinator.requestStartInteligente(configuracao);
            } else {
                DiagnosticoCoordinator.requestStartInteligente(configuracao);
            }
        }
        return Map.of("duracao", durac, "nivel", niv, "foco", foc);
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
        closeModal();
        if (FluxoModalContext.isTesteAdaptativo()) {
            TesteAdaptativoCoordinator.updateSubtopicosSelecionados(Map.of());
            TesteAdaptativoCoordinator.requestStartSoRun();
            return;
        }
        DiagnosticoCoordinator.updateSubtopicosSelecionados(Map.of());
        DiagnosticoCoordinator.requestStartSoRun();
    }
}
