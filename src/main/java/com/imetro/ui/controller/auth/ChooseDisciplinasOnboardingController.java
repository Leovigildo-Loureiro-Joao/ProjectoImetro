package com.imetro.ui.controller.auth;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;

import com.imetro.App;
import com.imetro.domain.dto.disciplina.DisciplinaDto;
import com.imetro.domain.enums.NivelDisciplina;
import com.imetro.persistence.repository.DisciplinaRepository;
import com.imetro.persistence.repository.OrientadorDisciplinaRepository;
import com.imetro.services.CandidatoService;
import com.imetro.services.DisciplinaService;
import com.imetro.services.DisciplinaUploadBootstrapService;
import com.imetro.services.PerguntasBootstrapAsyncService;
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
    private DisciplinaService disciplinaService;


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
        disciplinaService = new DisciplinaService();
        for (DisciplinaDto seed : disciplinaService.discCategoria()) {
            disciplinasBox.getChildren().add(new DisciplinaCard(seed));
        }

        prepararPastasLivros();
    }

    @FXML
    private void onContinue(ActionEvent actionEvent) {
        UUID candidatoId = Authentication.getCurrentUserId();
        for(var node : disciplinasBox.getChildren()) {
            if (node instanceof DisciplinaCard card) {
                var radio = card.getRadioSelecionado();
                if (radio != null && radio.isSelected()) {
                    var nivel = (String) radio.getText();
                    var disciplinaId = card.getDisciplina().id();
                    candidatoService.AddFirstProgressoDisciplina(candidatoId, disciplinaId, NivelDisciplina.fromDescricao(nivel), card.getDisciplina().peso());
                }
            }
        }

        StackPane contentHost = (StackPane) telaChooseDisciplinas.getParent();
        if (candidatoId != null) {
            PerguntasBootstrapAsyncService.getInstance().start(candidatoId);
        }
        OnboardingRouter.CandidatoRoute(contentHost);

    }

    private void prepararPastasLivros() {
        try {
            DisciplinaUploadBootstrapService bootstrapService = new DisciplinaUploadBootstrapService();
            int totalPastas = bootstrapService.prepararPastasUploads().size();
            if (statusLabel != null) {
                statusLabel.setText(
                    "Disciplinas prontas. Pastas dos livros em uploads/disciplinas (" + totalPastas + "). "
                        + "Depois de selecionar as tuas disciplinas, as que nao tiverem orientacao comecam a gerar topicos e perguntas automaticamente em segundo plano. "
                        + "Podes entrar no sistema e continuar a navegar enquanto a barra de progresso acompanha a leitura dos livros. "
                        + "As que ja tiverem orientador ficam em espera."
                );
            }
        } catch (Exception e) {
            if (statusLabel != null) {
                statusLabel.setText(
                    "Disciplinas carregadas. Nao foi possivel preparar as pastas dos livros ou ligar o processamento automatico agora."
                );
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
