package com.imetro.ui.controller.auth;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import com.imetro.domain.dto.disciplina.DisciplinaDto;
import com.imetro.domain.dto.progresso.ProgressoAlunoDisciplinaDto;
import com.imetro.services.CandidatoService;
import com.imetro.services.DisciplinaService;
import com.imetro.services.DisciplinaUploadBootstrapService;
import com.imetro.services.PerguntasBootstrapAsyncService;
import com.imetro.ui.OnboardingRouter;
import com.imetro.ui.components.DisciplinaCard;
import com.imetro.util.Authentication;
import com.imetro.util.TextoUtil;

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
        List<DisciplinaDto> disciplinas = DisciplinaService.discCategoria();
        prepararPastasLivros();
        Map<String, List<String>> focosExistentes = carregarFocosExistentes();
        for (DisciplinaDto seed : disciplinas) {
            disciplinasBox.getChildren().add(
                new DisciplinaCard(seed, focosExistentes.getOrDefault(TextoUtil.normalizarMinusculo(seed.nome()), List.of()))
            );
        }
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

        List<DisciplinaCard> cardsComSelecao = new ArrayList<>();
        List<DisciplinaCard> cardsSemSelecao = new ArrayList<>();

        for (var node : disciplinasBox.getChildren()) {
            if (node instanceof DisciplinaCard card) {
                if (!card.getTopicosSelecionados().isEmpty()) {
                    cardsComSelecao.add(card);
                } else {
                    cardsSemSelecao.add(card);
                }
            }
        }

        if (cardsComSelecao.isEmpty()) {
            if (statusLabel != null) {
                statusLabel.setText("Escolhe pelo menos um topico que vais estudar.");
            }
            return;
        }
        String focos="";
        for (DisciplinaCard card : cardsComSelecao) {
            var disciplinaId = card.getDisciplina().id();
            focos+=card.getTopicosFocoPersistencia();
            candidatoService.AddFirstProgressoDisciplina(
                candidatoId,
                disciplinaId,
                card.getTopicosFocoPersistencia(),
                card.getDisciplina().peso()
            );
        }
        candidatoService.insertFocos(focos, candidatoId);
        for (DisciplinaCard card : cardsSemSelecao) {
            candidatoService.RemoverProgressoDisciplina(candidatoId, card.getDisciplina().id());
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
            bootstrapService.prepararPastasUploads();
            if (statusLabel != null) {
                statusLabel.setText(
                    ""
                        + "Seleciona os topicos que queres estudar e o sistema guarda esse escopo. "
                        + "Depois podes voltar ao Perfil para alterar ou adicionar novos topicos de estudo. "
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

    private Map<String, List<String>> carregarFocosExistentes() {
        Map<String, List<String>> focosExistentes = new LinkedHashMap<>();
        for (ProgressoAlunoDisciplinaDto progresso : DisciplinaService.getProgressoDisciplinasCandidatoSafe()) {
            if (progresso == null || progresso.disciplina() == null || progresso.disciplina().isBlank()) {
                continue;
            }

            focosExistentes.put(TextoUtil.normalizarMinusculo(progresso.disciplina()), progresso.focoSubtopicosLista());
        }
        return focosExistentes;
    }

}
