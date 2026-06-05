package com.imetro.ui.controller.candidato.bolsas;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import com.imetro.App;
import com.imetro.domain.dto.bolsa.BolsaDto;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoEstado;
import com.imetro.domain.dto.test.ReacaoTeste;
import com.imetro.services.BolsaSimuladoService;
import com.imetro.services.PlaneamentoEstudoService;
import com.imetro.services.TesteService;
import com.imetro.ui.controller.candidato.resultados.ResultadoAvaliacaoController;
import com.imetro.ui.controller.lifecycle.DisposableController;
import com.imetro.ui.model.Questao;
import com.imetro.util.Authentication;
import com.imetro.util.QuestaoResultado;
import com.imetro.util.QuestaoUtil;
import com.imetro.util.ResultadoPayload;
import com.jfoenix.controls.JFXButton;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class BolsaSimuladoController implements Initializable, DisposableController {

    @FXML
    private StackPane rootPane;

    @FXML
    private Label heroTitleLabel;

    @FXML
    private Label heroSubtitleLabel;

    @FXML
    private Label heroStatusLabel;

    

    @FXML
    private Label heroDisciplineLabel;

    @FXML
    private Label heroCriteriaLabel;

    @FXML
    private Label heroDurationLabel;

    @FXML
    private Label heroReadinessLabel;

    @FXML
    private Label heroPlanLabel;

    @FXML
    private VBox introPane;

    @FXML
    private VBox examPane;

    @FXML
    private VBox emptyPane;

    @FXML
    private Label timerLabel;

    @FXML
    private Label questionCounterLabel;

    @FXML
    private Label progressLabel;

    @FXML
    private ProgressBar examProgressBar;

    @FXML
    private Label topicoLabel;

    @FXML
    private Label subtopicoLabel;

    @FXML
    private Label enunciadoLabel;

    @FXML
    private Label apoioLabel;

    @FXML
    private Label optionALabel;

    @FXML
    private Label optionBLabel;

    @FXML
    private Label optionCLabel;

    @FXML
    private Label optionDLabel;

    @FXML
    private TextField respostaField;

    @FXML
    private Label inputHintLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private JFXButton startButton;

    @FXML
    private JFXButton submitButton;

    @FXML
    private JFXButton finalizarButton;

    private final BolsaSimuladoService bolsaSimuladoService = new BolsaSimuladoService();
    private final TesteService testeService = new TesteService();
    private final PlaneamentoEstudoService planeamentoService = new PlaneamentoEstudoService();
    private final List<Questao> questoes = new ArrayList<>();
    private final List<Character> respostasUsuario = new ArrayList<>();
    private final List<ReacaoTeste> reacoes = new ArrayList<>();

    private BolsaSimuladoCoordinator.BolsaSelection selection;
    private BolsaDto bolsa;
    private Timeline countdown;
    private int questaoAtual;
    private int totalQuestoes;
    private int totalAcertos;
    private int totalErros;
    private int duracaoTotalSegundos;
    private int segundosRestantes;
    private long inicioQuestaoMillis;
    private boolean provaIniciada;
    private boolean provaFinalizada;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selection = BolsaSimuladoCoordinator.getAtual();
        bolsa = selection == null ? null : selection.bolsa();

        configurarEstadoInicial();
        configurarHero();
        Platform.runLater(this::atualizarEstadoPlanejamento);
        if (respostaField != null) {
            respostaField.setOnAction(event -> onSubmitAnswer());
        }
    }

    private void atualizarEstadoPlanejamento() {
        if (heroPlanLabel == null) {
            return;
        }

        PlaneamentoEstudoEstado estado = planeamentoService.resolverEstadoAtual(Authentication.getCurrentUserId());
        heroPlanLabel.setText(estado.titulo());
    }

    @FXML
    private void onBack() {
        navegarPara("views/pages/candidato/bolsas");
    }

    @FXML
    private void onStartExam() {
        if (bolsa == null || provaIniciada) {
            return;
        }

        questoes.clear();
        questoes.addAll(
            bolsaSimuladoService.carregarQuestoesBolsa(
                bolsa,
                bolsaSimuladoService.resolverQuantidadeQuestoes(bolsa)
            )
        );

        if (questoes.isEmpty()) {
            setStatus("Ainda nao encontramos perguntas dificeis suficientes para esta bolsa.", true);
            return;
        }

        respostasUsuario.clear();
        reacoes.clear();
        questaoAtual = 0;
        totalAcertos = 0;
        totalErros = 0;
        totalQuestoes = questoes.size();
        duracaoTotalSegundos = Math.max(600, safeInt(bolsa.duracaoMinutos()) * 60);
        segundosRestantes = duracaoTotalSegundos;
        provaIniciada = true;
        provaFinalizada = false;

        introPane.setVisible(false);
        introPane.setManaged(false);
        emptyPane.setVisible(false);
        emptyPane.setManaged(false);
        examPane.setVisible(true);
        examPane.setManaged(true);

        iniciarCronometro();
        carregarQuestaoAtual();
    }

    @FXML
    private void onSubmitAnswer() {
        if (!provaIniciada || provaFinalizada || questaoAtual >= questoes.size()) {
            return;
        }

        String respostaDigitada = respostaField == null ? "" : respostaField.getText();
        Questao questao = questoes.get(questaoAtual);
        char respostaMapeada = bolsaSimuladoService.resolverRespostaDigitada(questao, respostaDigitada);
        if (respostaMapeada == '\0') {
            setStatus("Escreve A, B, C ou D, ou cola o texto exato de uma das opcoes.", true);
            return;
        }

        long tempoRespostaSegundos = Math.max(1L, Math.round((System.currentTimeMillis() - inicioQuestaoMillis) / 1000.0));
        respostasUsuario.add(respostaMapeada);
        reacoes.add(new ReacaoTeste(
            questao,
            questaoAtual,
            respostaMapeada,
            tempoRespostaSegundos,
            0.15d,
            0.15d,
            LocalDateTime.now()
        ));

        if (QuestaoUtil.respostaEstaCorreta(questao, respostaMapeada)) {
            totalAcertos++;
        } else {
            totalErros++;
        }

        if (questaoAtual >= totalQuestoes - 1) {
            finalizarProva("Prova concluida.");
            return;
        }

        questaoAtual++;
        setStatus("Resposta registada. Seguimos para a proxima questao.", false);
        carregarQuestaoAtual();
    }

    @FXML
    private void onFinalizeExam() {
        if (!provaIniciada || provaFinalizada) {
            return;
        }
        finalizarProva("Submissao manual concluida.");
    }

    private void configurarEstadoInicial() {
        boolean temBolsa = bolsa != null;
        introPane.setVisible(temBolsa);
        introPane.setManaged(temBolsa);
        examPane.setVisible(false);
        examPane.setManaged(false);
        emptyPane.setVisible(!temBolsa);
        emptyPane.setManaged(!temBolsa);
    }

    private void configurarHero() {
        if (bolsa == null) {
            heroTitleLabel.setText("Sem bolsa selecionada");
            heroSubtitleLabel.setText("Volta ao painel de bolsas e escolhe uma simulacao para abrir.");
            heroStatusLabel.setText("Fluxo pendente");
            heroDisciplineLabel.setText("Disciplina foco: -");
            heroCriteriaLabel.setText("Criterios: -");
        heroDurationLabel.setText("Duracao: -");
        heroReadinessLabel.setText("Prontidao: -");
        if (heroPlanLabel != null) {
            heroPlanLabel.setText("Sem plano ativo");
        }
        if (startButton != null) {
            startButton.setDisable(true);
        }
        return;
        }

        heroTitleLabel.setText(bolsa.nome());
        heroSubtitleLabel.setText(
            "Simulado de bolsa com resposta digitada, cronometro global e rigor alto em "
                + firstNonBlank(bolsa.disciplinaFoco(), "disciplina foco") + "."
        );
        heroStatusLabel.setText(selection != null && selection.elegivel() ? "Elegivel" : "Bloqueada");
        heroDisciplineLabel.setText("Disciplina foco: " + firstNonBlank(bolsa.disciplinaFoco(), "Matematica"));
        heroCriteriaLabel.setText("Criterios: " + firstNonBlank(selection == null ? null : selection.criterioResumo(), "-"));
        heroDurationLabel.setText("Duracao: " + safeInt(bolsa.duracaoMinutos()) + " min - modo " + firstNonBlank(bolsa.modoResposta(), "TEXTFIELD"));
        heroReadinessLabel.setText("Prontidao atual: " + (selection == null ? "-" : selection.prontidaoAtual() + "%"));
        if (heroPlanLabel != null) {
            heroPlanLabel.setText(planeamentoService.resolverEstadoAtual(Authentication.getCurrentUserId()).titulo());
        }
        if (startButton != null) {
            startButton.setDisable(selection != null && !selection.elegivel());
        }
    }

    private void iniciarCronometro() {
        pararCronometro();
        atualizarTimerLabel();
        countdown = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            segundosRestantes = Math.max(0, segundosRestantes - 1);
            atualizarTimerLabel();
            if (segundosRestantes <= 0) {
                finalizarProva("Tempo esgotado. As respostas registadas foram submetidas.");
            }
        }));
        countdown.setCycleCount(Timeline.INDEFINITE);
        countdown.play();
    }

    private void carregarQuestaoAtual() {
        if (questaoAtual < 0 || questaoAtual >= questoes.size()) {
            return;
        }

        Questao questao = questoes.get(questaoAtual);
        questionCounterLabel.setText("Questao " + (questaoAtual + 1) + " / " + totalQuestoes);
        progressLabel.setText((questaoAtual + 1) + " de " + totalQuestoes + " respondidas nesta ronda");
        examProgressBar.setProgress(totalQuestoes == 0 ? 0d : (double) (questaoAtual + 1) / totalQuestoes);
        topicoLabel.setText("Topico: " + firstNonBlank(questao.getTopico(), "Geral"));
        subtopicoLabel.setText("Subtopico: " + firstNonBlank(questao.getSubtopico(), "Base"));
        enunciadoLabel.setText(firstNonBlank(questao.getEnunciado(), "Sem enunciado"));
        apoioLabel.setText(construirApoioQuestao(questao));
        optionALabel.setText("A. " + firstNonBlank(questao.getOpcaoA(), "-"));
        optionBLabel.setText("B. " + firstNonBlank(questao.getOpcaoB(), "-"));
        optionCLabel.setText("C. " + firstNonBlank(questao.getOpcaoC(), "-"));
        optionDLabel.setText("D. " + firstNonBlank(questao.getOpcaoD(), "-"));
        if (respostaField != null) {
            respostaField.clear();
        }
        inputHintLabel.setText("Escreve A-D ou o texto exato da resposta final.");
        inicioQuestaoMillis = System.currentTimeMillis();
        Platform.runLater(() -> {
            if (respostaField != null) {
                respostaField.requestFocus();
            }
        });
    }

    private void finalizarProva(String mensagemFinal) {
        if (provaFinalizada) {
            return;
        }

        provaFinalizada = true;
        provaIniciada = false;
        pararCronometro();

        int totalRespondido = respostasUsuario.size();
        if (totalRespondido <= 0) {
            setStatus("Nenhuma resposta valida foi registada antes do fim da prova.", true);
            navegarPara("views/pages/candidato/bolsas");
            return;
        }

        double percentual = totalRespondido == 0 ? 0d : (totalAcertos * 100.0) / totalRespondido;
        double tempoMedioSegundos = reacoes.stream().mapToLong(ReacaoTeste::tempoSegundos).average().orElse(0d);
        String perfil = determinarPerfil(percentual, tempoMedioSegundos);
        String recomendacao = construirRecomendacao(percentual, totalRespondido);

        UUID candidatoId = Authentication.getCurrentUserId();
        if (candidatoId != null) {
            testeService.registrarSimuladoBolsaConcluido(
                bolsa,
                candidatoId,
                questoes.subList(0, totalRespondido),
                new ArrayList<>(respostasUsuario),
                new ArrayList<>(reacoes),
                formatarTempoDecorrido(),
                recomendacao
            );
        }

        ResultadoPayload payload = new ResultadoPayload(
            "Simulado de Bolsa",
            firstNonBlank(bolsa.disciplinaFoco(), "Bolsa Semanal"),
            totalAcertos,
            Math.max(0, totalRespondido - totalAcertos),
            totalRespondido,
            percentual,
            formatarTempoDecorrido(),
            perfil,
            perfil,
            mensagemFinal + " " + recomendacao,
            "views/pages/candidato/bolsa-simulado",
            construirQuestoesResultado()
        );

        ResultadoAvaliacaoController.setResultado(payload);
        navegarPara("views/pages/candidato/resultado-avaliacao");
    }

    private List<QuestaoResultado> construirQuestoesResultado() {
        List<QuestaoResultado> itens = new ArrayList<>();
        int limite = Math.min(questoes.size(), respostasUsuario.size());
        for (int i = 0; i < limite; i++) {
            itens.add(QuestaoResultado.fromQuestao(i + 1, questoes.get(i), respostasUsuario.get(i)));
        }
        return itens;
    }

    private String construirApoioQuestao(Questao questao) {
        return "Rigor " + Math.round(questao.getRigor() * 100d) + "% - dificuldade " + questao.getNivelDificuldade()
            + " - tempo sugerido " + Math.round(questao.getTempoSugerido()) + "s";
    }

    private String determinarPerfil(double percentual, double tempoMedioSegundos) {
        if (percentual >= 85d && tempoMedioSegundos <= 40d) {
            return "Corte forte";
        }
        if (percentual >= 70d) {
            return "Competitivo";
        }
        if (percentual >= 50d) {
            return "Em crescimento";
        }
        return "Precisa reforco";
    }

    private String construirRecomendacao(double percentual, int totalRespondido) {
        if (percentual >= 85d) {
            return "Mantiveste um score forte em " + totalRespondido + " questoes. Perfil muito competitivo para a proxima janela.";
        }
        if (percentual >= 70d) {
            return "Boa base. Mais precisao nas questoes longas pode empurrar-te para a shortlist semanal.";
        }
        if (percentual >= 50d) {
            return "Ja existe tracao. Vale repetir com foco em ritmo e leitura final da resposta.";
        }
        return "Ainda precisas consolidar rigor e velocidade antes de bater o corte principal.";
    }

    private void atualizarTimerLabel() {
        int minutos = segundosRestantes / 60;
        int segundos = segundosRestantes % 60;
        timerLabel.setText(String.format("%02d:%02d", minutos, segundos));
    }

    private String formatarTempoDecorrido() {
        int tempoDecorrido = Math.max(0, duracaoTotalSegundos - segundosRestantes);
        int minutos = tempoDecorrido / 60;
        int segundos = tempoDecorrido % 60;
        return String.format("%02d:%02d", minutos, segundos);
    }

    private void setStatus(String mensagem, boolean erro) {
        if (statusLabel == null) {
            return;
        }
        statusLabel.setText(mensagem == null ? "" : mensagem);
        statusLabel.getStyleClass().removeAll("profile-feedback-info", "profile-feedback-error");
        statusLabel.getStyleClass().add(erro ? "profile-feedback-error" : "profile-feedback-info");
    }

    private void navegarPara(String fxml) {
        if (rootPane == null || rootPane.getScene() == null) {
            return;
        }
        StackPane contentHost = (StackPane) rootPane.getScene().lookup("#contentHost");
        if (contentHost != null) {
            App.swapContent(contentHost, fxml);
        }
    }

    private void pararCronometro() {
        if (countdown != null) {
            countdown.stop();
            countdown = null;
        }
    }

    private int safeInt(Integer value) {
        return value == null ? 0 : value;
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return "-";
    }

    @Override
    public void dispose() {
        pararCronometro();
    }
}
