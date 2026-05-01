package com.imetro.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import com.imetro.App;
import com.imetro.domain.dto.disciplina.DisciplinaDto;
import com.imetro.persistence.repository.DisciplinaRepository;
import com.imetro.persistence.repository.OrientadorDisciplinaRepository;
import com.imetro.services.DisciplinaService;
import com.imetro.services.DisciplinaUploadBootstrapService;
import com.imetro.util.Authentication;
import com.jfoenix.controls.JFXButton;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class OrientadorDisciplinaOnboardingController implements Initializable {

    @FXML
    private StackPane telaOrientadorDisciplina;

    @FXML
    private ComboBox<String> disciplinaCombo;

    @FXML
    private Label statusLabel;

    private final DisciplinaService disciplinaService = new DisciplinaService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (statusLabel != null) {
            statusLabel.setText("");
        }
        if (disciplinaCombo != null) {
            List<String> nomes = disciplinaService.discCategoria().stream()
                .map(DisciplinaDto::nome)
                .sorted(Comparator.naturalOrder())
                .toList();
            disciplinaCombo.setItems(FXCollections.observableArrayList(nomes));
        }
        prepararPastasLivros();
    }

    @FXML
    private void onContinue(ActionEvent actionEvent) {
        JFXButton source = actionEvent.getSource() instanceof JFXButton btn ? btn : null;
        if (source != null) {
            source.setDisable(true);
        }

        String nome = disciplinaCombo == null ? null : disciplinaCombo.getValue();
        if (nome == null || nome.isBlank()) {
            if (statusLabel != null) {
                statusLabel.setText("Seleciona uma disciplina.");
            }
            if (source != null) {
                source.setDisable(false);
            }
            return;
        }

        UUID orientadorId = Authentication.getCurrentUserId();
        if (orientadorId != null) {
            UUID disciplinaId = new DisciplinaRepository().ensureAndGetIdByName(nome);
            if (disciplinaId != null) {
                new OrientadorDisciplinaRepository().add(orientadorId, disciplinaId);
            }
        }

        try {
            App.setRoot("views/layouts/OrientadorLayout");
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

    private void prepararPastasLivros() {
        try {
            DisciplinaUploadBootstrapService bootstrapService = new DisciplinaUploadBootstrapService();
            int totalPastas = bootstrapService.prepararPastasUploads().size();
            if (statusLabel != null) {
                statusLabel.setText(
                    "Pastas dos livros preparadas em uploads/disciplinas (" + totalPastas + "). As disciplinas com orientacao ficam em espera pelo fluxo do orientador."
                );
            }
        } catch (Exception e) {
            if (statusLabel != null) {
                statusLabel.setText(
                    "Nao foi possivel preparar as pastas dos livros agora."
                );
            }
        }
    }
}

