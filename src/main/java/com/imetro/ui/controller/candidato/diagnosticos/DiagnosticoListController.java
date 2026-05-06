package com.imetro.ui.controller.candidato.diagnosticos;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import com.imetro.domain.dto.Topico;
import com.imetro.domain.dto.diagnostico.DiagnosticoDisciplinaResumo;
import com.imetro.domain.dto.diagnostico.PrimeiroDiagnosticoResumo;
import com.imetro.services.DiagnosticoService;
import com.imetro.services.PerguntasBootstrapAsyncService;
import com.imetro.ui.controller.lifecycle.DisposableController;
import com.imetro.ui.components.diagnostico.DiagnosticoCard;
import com.imetro.ui.components.diagnostico.FirsCardDiagnostico;
import com.imetro.util.Authentication;
import com.jfoenix.controls.JFXButton;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;


public class DiagnosticoListController implements Initializable, DisposableController {

    @FXML
    private FlowPane diagnosticosPane;


    @FXML
    private JFXButton massButton;

    @FXML
    private JFXButton resetButton;

    @FXML
    private Label pageTitleLabel;

    @FXML
    private Label pageSubtitleLabel;

    private final ArrayList<Topico> topicosSelecionados = new ArrayList<>();
    private final DiagnosticoService diagnosticoService = new DiagnosticoService();
    private final PerguntasBootstrapAsyncService perguntasBootstrapAsyncService =
        PerguntasBootstrapAsyncService.getInstance();

    private final ChangeListener<PerguntasBootstrapAsyncService.BootstrapUiState> bootstrapStateListener =
        (obs, oldState, newState) -> {
            if (oldState == PerguntasBootstrapAsyncService.BootstrapUiState.RUNNING
                && newState != PerguntasBootstrapAsyncService.BootstrapUiState.RUNNING) {
                Platform.runLater(this::carregarConteudo);
            }
        };

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        perguntasBootstrapAsyncService.stateProperty().addListener(bootstrapStateListener);
        carregarConteudo();
    }

    private void carregarConteudo() {
        if (diagnosticosPane == null) {
            return;
        }

        diagnosticosPane.getChildren().clear();
        UUID candidatoId = Authentication.getCurrentUserId();
        boolean processamentoLivros = perguntasBootstrapAsyncService.isRunningFor(candidatoId);
        boolean temHistorico = diagnosticoService.temHistoricoDiagnostico(candidatoId);

        if (!temHistorico) {
            PrimeiroDiagnosticoResumo primeiro = diagnosticoService
                .carregarPrimeiroDiagnosticoResumo(candidatoId);
            diagnosticosPane.getChildren().add(new FirsCardDiagnostico(primeiro));
            configurarCabecalhoPrimeiraVez(processamentoLivros);
            configurarAcoesLote(false);
        } else {
            List<DiagnosticoDisciplinaResumo> resumos = diagnosticoService
                .carregarDiagnosticosDisponiveis(candidatoId);
            if (resumos.isEmpty()) {
                diagnosticosPane.getChildren().add(criarEstadoVazio(processamentoLivros));
            } else {
                for (DiagnosticoDisciplinaResumo resumo : resumos) {
                    diagnosticosPane.getChildren().add(criarCard(resumo));
                }
            }
            configurarCabecalhoHistorico();
            configurarAcoesLote(true);
        }
        atualizarEstadoBotoes();
    }

    @Override
    public void dispose() {
        perguntasBootstrapAsyncService.stateProperty().removeListener(bootstrapStateListener);
    }

    private void configurarCabecalhoPrimeiraVez(boolean processamentoLivros) {
        if (pageTitleLabel != null) {
            pageTitleLabel.setText("FAZER O PRIMEIRO DIAGNOSTICO");
        }
        if (pageSubtitleLabel != null) {
            pageSubtitleLabel.setText(
                processamentoLivros
                    ? "Os livros estao a ser lidos em segundo plano. O diagnostico libera quando a base ficar pronta."
                    : "Escolha os topicos iniciais e crie o primeiro historico real."
            );
        }
    }

    private void configurarCabecalhoHistorico() {
        if (pageTitleLabel != null) {
            pageTitleLabel.setText("FACA AGORA SEU DIAGNOSTICO");
        }
        if (pageSubtitleLabel != null) {
            pageSubtitleLabel.setText("Seus diagnosticos");
        }
    }

    private void configurarAcoesLote(boolean visivel) {
        if (massButton != null) {
            massButton.setVisible(visivel);
            massButton.setManaged(visivel);
        }
        if (resetButton != null) {
            resetButton.setVisible(visivel);
            resetButton.setManaged(visivel);
        }
    }


    private DiagnosticoCard criarCard(DiagnosticoDisciplinaResumo resumo) {
        return new DiagnosticoCard(
            resumo,
            param -> {
                DiagnosticoCoordinator.requestStart(new ArrayList<>(param));
                return null;
            },
            this::atualizarEstadoBotoes
        );
    }

    public void atualizarEstadoBotoes() {
        int selecionados = 0;
        topicosSelecionados.clear();

        for (Node node : diagnosticosPane.getChildren()) {
            if (!(node instanceof DiagnosticoCard card)) {
                continue;
            }
            if (card.getDiciplina().isSelected()) {
                selecionados++;
                topicosSelecionados.addAll(card.getTopicos());
            }
        }

        if (massButton != null && massButton.isManaged()) {
            massButton.setDisable(selecionados <= 1);
        }
        if (resetButton != null && resetButton.isManaged()) {
            resetButton.setDisable(selecionados == 0);
        }
    }

    @FXML
    private void DiagnosticoMassa(ActionEvent event) {
        if (!topicosSelecionados.isEmpty()) {
            DiagnosticoCoordinator.requestStart(new ArrayList<>(topicosSelecionados));
        }
    }

    @FXML
    private void ResetData(ActionEvent event) {
        DiagnosticoCoordinator.requestAlert(
            "Resetar diagnosticos",
            "Deseja limpar a selecao atual e recomecar o fluxo?",
            this::limparSelecaoAtual
        );
    }

    private void limparSelecaoAtual() {
        for (Node node : diagnosticosPane.getChildren()) {
            if (node instanceof DiagnosticoCard card) {
                card.setSelecionado(false);
            }
        }
        topicosSelecionados.clear();
        atualizarEstadoBotoes();
    }

    private VBox criarEstadoVazio(boolean processamentoLivros) {
        Label titulo = new Label("Nenhuma disciplina com questoes reais disponivel.");
        titulo.getStyleClass().add("h1-thin");

        Label descricao = new Label(
            processamentoLivros
                ? "Os livros continuam a ser processados em segundo plano. Podes navegar noutras abas e acompanhar a barra de progresso no topo enquanto a tabela `perguntas` e preenchida."
                : "Ainda nao surgiram perguntas reais na tabela `perguntas`. Confirma os PDFs em `uploads/disciplinas/<uuid>`, a chave do Gemini e se a disciplina esta sem orientacao para o processamento automatico."
        );
        descricao.getStyleClass().add("muted");
        descricao.setWrapText(true);

        VBox box = new VBox(10, titulo, descricao);
        box.getStyleClass().addAll("placeholder-card", "diagnostico-empty-state");
        box.setAlignment(Pos.CENTER_LEFT);
        box.setMaxWidth(540);
        return box;
    }
}
