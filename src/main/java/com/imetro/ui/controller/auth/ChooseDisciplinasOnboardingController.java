package com.imetro.ui.controller.auth;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

import com.imetro.domain.dto.disciplina.DisciplinaDto;
import com.imetro.services.CandidatoService;
import com.imetro.services.DisciplinaService;
import com.imetro.services.DisciplinaUploadBootstrapService;
import com.imetro.services.PerguntasBootstrapAsyncService;
import com.imetro.ui.OnboardingRouter;
import com.imetro.ui.components.DisciplinaCard;
import com.imetro.util.Authentication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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
        for (DisciplinaDto seed : DisciplinaService.discCategoria()) {
            disciplinasBox.getChildren().add(new DisciplinaCard(seed));
        }

        prepararPastasLivros();
    }

    @FXML
    private void onContinue(ActionEvent actionEvent) {
        UUID candidatoId = Authentication.getCurrentUserId();
        if (candidatoId == null) {
            if (statusLabel != null) {
                statusLabel.setText("Nao foi possivel identificar o candidato atual.");
            }
            return;
        }

        boolean temSelecao = false;

        for (var node : disciplinasBox.getChildren()) {
            if (node instanceof DisciplinaCard card) {
                if (!card.isSelecionada()) {
                    continue;
                }

                temSelecao = true;
                var disciplinaId = card.getDisciplina().id();
                candidatoService.AddFirstProgressoDisciplina(
                    candidatoId,
                    disciplinaId,
                    card.getSubtopicosFoco(),
                    card.getDisciplina().peso()
                );
            }
        }

        if (!temSelecao) {
            if (statusLabel != null) {
                statusLabel.setText("Escolhe pelo menos uma disciplina e escreve os subtopicos prioritarios da bolsa.");
            }
            return;
        }

        StackPane contentHost = (StackPane) telaChooseDisciplinas.getParent();
        if (candidatoId != null) {
            PerguntasBootstrapAsyncService.getInstance().startIfNeeded(candidatoId);
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
                        + "Depois de escolheres as disciplinas, escreve os subtopicos prioritarios para a bolsa. "
                        + "Matematica e Fisica comecam a gerar topicos e perguntas automaticamente em segundo plano. "
                        + "Podes entrar no sistema e continuar a navegar enquanto a barra de progresso acompanha a leitura dos livros. "
                        + "Este fluxo agora depende apenas da tua base de questoes."
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

}
