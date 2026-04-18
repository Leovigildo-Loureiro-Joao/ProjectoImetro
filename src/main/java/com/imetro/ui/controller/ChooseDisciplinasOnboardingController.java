package com.imetro.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import com.imetro.App;
import com.imetro.persistence.repository.CandidatoDisciplinaRepository;
import com.imetro.persistence.repository.DisciplinaRepository;
import com.imetro.persistence.repository.OrientadorDisciplinaRepository;
import com.imetro.ui.components.DisciplinaCard;
import com.imetro.util.Authentication;
import com.imetro.util.DisciplinaCatalog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ChooseDisciplinasOnboardingController implements Initializable {

    @FXML
    private StackPane telaChooseDisciplinas;

    @FXML
    private VBox disciplinasBox;

    @FXML
    private Label statusLabel;

    private final List<CheckBox> checkBoxes = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (statusLabel != null) {
            statusLabel.setText("");
        }
        if (disciplinasBox == null) {
            return;
        }

        List<DisciplinaCatalog.DisciplinaSeed> seeds = DisciplinaCatalog.defaultSeeds();
        List<String> nomes = seeds.stream().map(DisciplinaCatalog.DisciplinaSeed::nome).toList();
        DisciplinaRepository disciplinaRepository = new DisciplinaRepository();
        for (DisciplinaCatalog.DisciplinaSeed seed : seeds) {
            disciplinaRepository.ensureExists(seed.nome(), seed.peso(), seed.nivel());
        }

        Map<String, Integer> counts = new OrientadorDisciplinaRepository().countOrientadoresByDisciplinaNames(nomes);
        disciplinasBox.getChildren().clear();
        checkBoxes.clear();

        for (DisciplinaCatalog.DisciplinaSeed seed : seeds) {
            disciplinasBox.getChildren().add(new DisciplinaCard(seed,counts));
        }
    }

    @FXML
    private void onContinue(ActionEvent actionEvent) {
        JFXButton source = actionEvent.getSource() instanceof JFXButton btn ? btn : null;
        if (source != null) {
            source.setDisable(true);
        }

        List<String> selected = new ArrayList<>();
        for (CheckBox cb : checkBoxes) {
            if (cb.isSelected()) {
                selected.add(cb.getText());
            }
        }

        if (selected.isEmpty()) {
            if (statusLabel != null) {
                statusLabel.setText("Seleciona pelo menos uma disciplina.");
            }
            if (source != null) {
                source.setDisable(false);
            }
            return;
        }

        UUID candidatoId = parseUuid(Authentication.getCurrentUserId());
        if (candidatoId != null) {
            DisciplinaRepository disciplinaRepository = new DisciplinaRepository();
            CandidatoDisciplinaRepository candidatoDisciplinaRepository = new CandidatoDisciplinaRepository();
            for (String nome : selected) {
                UUID disciplinaId = disciplinaRepository.ensureAndGetIdByName(nome);
                if (disciplinaId != null) {
                    candidatoDisciplinaRepository.add(candidatoId, disciplinaId);
                }
            }
        }

        try {
            App.setRoot("views/layouts/CandidatoLayout");
        } catch (IOException e) {
            e.printStackTrace();
            if (statusLabel != null) {
                statusLabel.setText("Não foi possível abrir o sistema agora.");
            }
        } finally {
            if (source != null) {
                source.setDisable(false);
            }
        }
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
