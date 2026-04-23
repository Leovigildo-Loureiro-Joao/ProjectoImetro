package com.imetro.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import com.imetro.App;
import com.imetro.domain.dto.disciplina.DisciplinaDto;
import com.imetro.domain.enums.NivelDisciplina;
import com.imetro.persistence.repository.DisciplinaRepository;
import com.imetro.persistence.repository.OrientadorDisciplinaRepository;
import com.imetro.services.CandidatoService;
import com.imetro.services.DisciplinaService;
import com.imetro.ui.OnboardingRouter;
import com.imetro.ui.components.DisciplinaCard;
import com.imetro.util.Authentication;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ChooseDisciplinasOnboardingController implements Initializable {

    @FXML
    private VBox disciplinasBox;

    @FXML
    private Label statusLabel;

    @FXML
    private StackPane telaChooseDisciplinas;

    private CandidatoService candidatoService;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (statusLabel != null) {
            statusLabel.setText("");
        }
        if (disciplinasBox == null) {
            return;
        }
        disciplinasBox.getChildren().clear();
        candidatoService = new CandidatoService();
        DisciplinaService dService=new DisciplinaService();
        for (DisciplinaDto seed : dService.discCategoria()) {
            disciplinasBox.getChildren().add(new DisciplinaCard(seed));
        }

        
    }

    @FXML
    private void onContinue(ActionEvent actionEvent) {
        for(var node : disciplinasBox.getChildren()) {
            if (node instanceof DisciplinaCard card) {
                var radio = card.getRadioSelecionado();
                if (radio != null && radio.isSelected()) {
                    var nivel = (String) radio.getUserData();
                    var disciplinaId = card.getDisciplina().id();
                    candidatoService.AddFirstProgressoDisciplina(Authentication.getCurrentUserId(), disciplinaId, NivelDisciplina.fromDescricao(nivel), card.getDisciplina().peso());
                }
            }
        }
        StackPane contentHost = (StackPane) telaChooseDisciplinas.getParent();
        OnboardingRouter.CandidatoRoute(contentHost);

    }

    private static UUID parseUuid(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
