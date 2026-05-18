package com.imetro.ui.controller.candidato.testes;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.imetro.App;
import com.imetro.domain.dto.Topico;
import com.imetro.domain.dto.diagnostico.DiagnosticoDto;
import com.imetro.domain.dto.test.Percent;
import com.imetro.domain.dto.test.TesteDto;
import com.imetro.domain.dto.test.ReacaoTeste;
import com.imetro.domain.enums.NivelDificuldadeAdaptativa;
import com.imetro.services.DiagnosticoService;
import com.imetro.services.TesteAdaptativoService;
import com.imetro.services.TesteService;
import com.imetro.ui.components.CircleProgress;
import com.imetro.ui.components.PlanoCartesianoPane;
import com.imetro.ui.components.TesteCard;
import com.imetro.ui.controller.candidato.ResultadoAvaliacaoController;
import com.imetro.ui.controller.lifecycle.DisposableController;
import com.imetro.ui.model.Questao;
import com.imetro.ui.modals.ModalAlert;
import com.imetro.ui.modals.ModalController;
import com.imetro.ui.modals.TopicModalController;
import com.imetro.util.Authentication;
import com.imetro.util.QuestaoGraficoSupport;
import com.imetro.util.QuestaoResultado;
import com.imetro.util.QuestaoUtil;
import com.imetro.util.ResultadoPayload;
import com.imetro.util.TextoUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleNode;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.util.Duration;

public class TesteAdaptativoController implements DisposableController, TesteAdaptativoCoordinator.TesteHost {

    @FXML private AnchorPane testeField;
    @FXML private StackPane modalPai;
    @FXML private StackPane circleProgressContainer;
    @FXML private Label nomeDisc;
    @FXML private Label nPergunta;
    @FXML private Label bloco1;
    @FXML private Label bloco2;
    @FXML private Label ResA;
    @FXML private Label ResB;
    @FXML private Label ResC;
    @FXML private Label ResD;
    @FXML private Label ResE;
    @FXML private Label ResF;
    @FXML private Label ResG;
    @FXML private JFXToggleNode toggleA;
    @FXML private JFXToggleNode toggleB;
    @FXML private JFXToggleNode toggleC;
    @FXML private JFXToggleNode toggleD;
    @FXML private JFXToggleNode toggleE;
    @FXML private JFXToggleNode toggleF;
    @FXML private JFXToggleNode toggleG;
    @FXML private ToggleGroup alternativas;
    @FXML private VBox testeContainer;
    @FXML private VBox start;
    @FXML private VBox tela;
    @FXML private VBox disciplinasContainer;
    @FXML private ImageView imgBloco2;
    @FXML private JFXButton btnConfirmar;
    @FXML private JFXButton btnProximo;
    @FXML private StackPane loadingOverlay;
    @FXML private ProgressBar loadingProgress;
    @FXML private Label loadingMessage;
    @FXML private Label nivelAtual;
    @FXML private Label dificuldadeAtual;
    @FXML private Label tempo;
    @FXML private VBox feedbackContainer;
    @FXML private Label feedbackIcon;
    @FXML private Label feedbackMessage;

    private final VBox botoesDisciplinasBox = new VBox(12);
    private final List<Character> respostasUsuario = new ArrayList<>();
    private final List<Long> temposResposta = new ArrayList<>();
    private final List<String> topicosSelecionados = new ArrayList<>();
    private final List<String> subtopicosSelecionados = new ArrayList<>();
    private final List<ReacaoTeste> reacao = new ArrayList<>();

    private CircleProgress circleProgress;
    private List<Questao> questoes = new ArrayList<>();
    private List<Questao> focoQuestoes = new ArrayList<>();
    private int questaoAtual = 0;
    private int totalQuestoes = 0;
    private char respostaSelecionada;
    private NivelDificuldadeAdaptativa nivelAtualAdaptativo = NivelDificuldadeAdaptativa.padrao();
    private int acertos = 0;
    private int erros = 0;
    private int sequenciaAcertos = 0;
    private int sequenciaErros = 0;
    private long tempoInicioQuestao;
    private Timeline cronometro;
    private Timeline loadingTimeline;
    private int segundos = 0;
    private int minutos = 0;
    private TesteAdaptativoService service;
    private final DiagnosticoService diagnosticoService = new DiagnosticoService();
    private String disciplinaSelecionada;
    private FXMLLoader modFxml;
    private ModalController cont;
    private TesteService testeService;
    private HBox linhaQuestaoPane;
    private VBox textoQuestaoPane;
    private VBox apoioVisualBox;
    private Separator apoioVisualSeparator;
    private StackPane planoCartesianoContainer;
    private PlanoCartesianoPane planoCartesianoPane;
    @FXML
    public void initialize() {
        TesteAdaptativoCoordinator.setHost(this);
        testeService=new TesteService();
        circleProgress = new CircleProgress(35, 35, 35, 0);
        circleProgressContainer.getChildren().add(circleProgress);
        configurarPainelApoioVisual();

        service = new TesteAdaptativoService();
        botoesDisciplinasBox.setFillWidth(false);
        botoesDisciplinasBox.setAlignment(Pos.TOP_LEFT);
        disciplinasContainer.getChildren().setAll(botoesDisciplinasBox);

        if (diagnosticoService.temHistoricoDiagnostico(Authentication.getCurrentUserId())) {
            carregarDisciplinas();
        } else {
            carregarBloqueioPrimeiroDiagnostico();
        }

        feedbackContainer.setVisible(false);
        testeContainer.setVisible(false);
        start.setVisible(true);
        atualizarIndicadoresNivel();
    }

    private void carregarBloqueioPrimeiroDiagnostico() {
        botoesDisciplinasBox.getChildren().clear();

        Label badge = new Label("Fluxo inicial");
        badge.getStyleClass().add("diagnostico-first-badge");

        Label titulo = new Label("Fazer primeiro diagnostico");
        titulo.getStyleClass().add("diagnostico-first-title");

        Label descricao = new Label(
            "O exame adaptativo abre depois do primeiro diagnostico real. Assim o sistema aprende o teu ponto de partida antes de subir a dificuldade."
        );
        descricao.getStyleClass().add("diagnostico-card-summary");
        descricao.setWrapText(true);

        Label apoio = new Label(
            "Vai ao diagnostico, escolhe os topicos e conclui a primeira rodada. Depois desta etapa, os testes ficam liberados aqui."
        );
        apoio.getStyleClass().add("diagnostico-card-note");
        apoio.setWrapText(true);

        JFXButton iniciar = new JFXButton("Ir para o primeiro diagnostico");
        iniciar.getStyleClass().addAll("btn-primary", "diagnostico-first-action");
        iniciar.setOnAction(event -> {
            StackPane contentHost = testeField == null || testeField.getScene() == null
                ? null
                : (StackPane) testeField.getScene().lookup("#contentHost");
            if (contentHost != null) {
                App.swapContent(contentHost, "views/pages/candidato/diagnostico");
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox topo = new HBox(12, titulo, spacer, badge);
        topo.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(16, topo, descricao, apoio, iniciar);
        card.getStyleClass().addAll("placeholder-card", "diagnostico-first-card");
        card.setPadding(new Insets(22));
        card.setAlignment(Pos.CENTER_LEFT);

        botoesDisciplinasBox.getChildren().add(card);
    }

    private void carregarDisciplinas() {
        botoesDisciplinasBox.getChildren().clear();

        Label titulo = new Label("Escolha uma disciplina para ver os topicos ja testados e iniciar a proxima rodada.");
        titulo.getStyleClass().add("h3-thin");
        titulo.setWrapText(true);
        titulo.setMaxWidth(720);
        botoesDisciplinasBox.getChildren().add(titulo);

        List<String> disciplinas = service.carregarDisciplinasDisponiveis();
        Map<String, TesteService.ResumoHistoricoDisciplina> resumos = testeService
            .carregarResumoHistoricoDisciplinas(disciplinas, 4);

        for (String disciplina : disciplinas) {
            List<Topico> topicos = service.carregarTopicosPorDisciplina(disciplina);
            String chaveResumo = QuestaoUtil.normalizar(formatarDisciplina(disciplina));
            TesteService.ResumoHistoricoDisciplina historico = resumos.getOrDefault(
                chaveResumo,
                TesteService.ResumoHistoricoDisciplina.vazio()
            );
            TesteCard teste = new TesteCard(
                construirResumoDisciplina(disciplina, topicos, historico),
                topicos != null && !topicos.isEmpty(),
                () -> iniciarTestePadrao(disciplina, topicos),
                () -> abrirConfiguracaoInteligente(disciplina, topicos)
            );
            botoesDisciplinasBox.getChildren().add(teste);
        }
    }

    private TesteDto construirResumoDisciplina(
        String disciplina,
        List<Topico> topicos,
        TesteService.ResumoHistoricoDisciplina historico
    ) {
        String nomeDisciplina = formatarDisciplina(disciplina);

        List<Percent> percentuaisTopicos = historico.topicosTestados();
        float coberturaTopicos = topicos == null || topicos.isEmpty()
            ? 0f
            : limitarUnitario((float) percentuaisTopicos.size() / topicos.size());
        float acertoMedio = limitarUnitario(historico.acertoMedio());
        float precisaoMedia = limitarUnitario(historico.precisaoMedia());
        float evolucao = acertoMedio;

        return new TesteDto(
            nomeDisciplina,
            acertoMedio,
            precisaoMedia,
            coberturaTopicos,
            evolucao,
            historico.totalQuestoesRespondidas(),
            historico.totalTestes(),
            percentuaisTopicos,
            List.of()
        );
    }

    private void iniciarTestePadrao(String disciplina, List<Topico> topicos) {
        disciplinaSelecionada = disciplina;
        aplicarFocoSelecionado(topicos, Map.of());
        iniciarTesteComConfiguracao(null);
    }

    private void abrirConfiguracaoInteligente(String disciplina, List<Topico> topicos) {
        if (topicos == null || topicos.isEmpty()) {
            mostrarAlerta("Atencao", "Nao encontramos topicos para iniciar o teste dessa disciplina.");
            return;
        }

        TesteAdaptativoCoordinator.requestStart(disciplina, new ArrayList<>(topicos));
    }

    @Override
    public void startTesteAdaptativo() {
        disciplinaSelecionada = TesteAdaptativoCoordinator.getDisciplinaSelecionada();
        List<Topico> topicos = TesteAdaptativoCoordinator.getTopicosSelecionados();

        if (disciplinaSelecionada == null || disciplinaSelecionada.isBlank()) {
            mostrarAlerta("Atencao", "Selecione uma disciplina para continuar.");
            return;
        }
        if (topicos.isEmpty()) {
            mostrarAlerta("Atencao", "Nao encontramos topicos para essa disciplina.");
            return;
        }

        aplicarFocoSelecionado(topicos, TesteAdaptativoCoordinator.getSubtopicosSelecionados());
        iniciarTesteComConfiguracao(TesteAdaptativoCoordinator.getConfiguracaoAtual());
    }

    private void aplicarFocoSelecionado(List<Topico> topicosDisponiveis, Map<String, List<String>> subtopicosPorTopico) {
        topicosSelecionados.clear();
        subtopicosSelecionados.clear();

        if (subtopicosPorTopico == null || subtopicosPorTopico.isEmpty()) {
            for (Topico topico : topicosDisponiveis) {
                topicosSelecionados.add(topico.topicos());
                if (topico.subTopicos() != null) {
                    subtopicosSelecionados.addAll(List.of(topico.subTopicos()));
                }
            }
            return;
        }

        for (Topico topico : topicosDisponiveis) {
            List<String> selecionados = subtopicosPorTopico.get(topico.topicos());
            if (selecionados == null || selecionados.isEmpty()) {
                continue;
            }

            topicosSelecionados.add(topico.topicos());
            subtopicosSelecionados.addAll(selecionados);
        }
    }

    private void iniciarTesteComConfiguracao(TesteAdaptativoCoordinator.TesteConfig config) {
        nivelAtualAdaptativo = resolverNivelInicial(config);
        focoQuestoes = service.carregarQuestoesDisponiveis(
            disciplinaSelecionada,
            topicosSelecionados,
            subtopicosSelecionados,
            nivelAtualAdaptativo.nivel()
        );
        if (focoQuestoes.isEmpty()) {
            mostrarAlerta("Atencao", "Nao encontramos questoes para esse foco. Tente outro recorte.");
            return;
        }


        questoes.clear();
        totalQuestoes = Math.min(resolverLimiteQuestoes(config, focoQuestoes.size()), focoQuestoes.size());
        resetarMetricas();

        Questao primeiraQuestao = obterProximaQuestao();
        if (primeiraQuestao == null) {
            mostrarAlerta("Atencao", "Nao foi possivel preparar o exame.");
            return;
        }

        questoes.add(primeiraQuestao);
        nomeDisc.setText(formatarDisciplina(disciplinaSelecionada));

        start.setVisible(false);
        testeContainer.setVisible(true);
        iniciarLoading(config);
    }

    private NivelDificuldadeAdaptativa resolverNivelInicial(TesteAdaptativoCoordinator.TesteConfig config) {
        return service.resolverNivelAtual(
            disciplinaSelecionada,
            topicosSelecionados,
            subtopicosSelecionados,
            config == null ? null : config.nivel()
        );
    }

    private int resolverLimiteQuestoes(TesteAdaptativoCoordinator.TesteConfig config, int totalDisponivel) {
        if (config == null || config.duracao() == null) {
            return Math.min(7, totalDisponivel); // TODO CONFIG_ADAPTATIVA: fallback MEDIO = 7 questoes ainda fixo.
        }

        return switch (TextoUtil.normalizarMinusculo(config.duracao())) {
            case "curto" -> 5; // TODO CONFIG_ADAPTATIVA: `CURTO = 5` ainda fixo; alinhar com `configuracoes_teste_adaptativo_duracoes`.
            case "medio" -> 7; // TODO CONFIG_ADAPTATIVA: `MEDIO = 7` ainda fixo; alinhar com `configuracoes_teste_adaptativo_duracoes`.
            default -> Math.min(10, totalDisponivel); // TODO CONFIG_ADAPTATIVA: `LONGO = 10` ainda fixo; alinhar com `configuracoes_teste_adaptativo_duracoes`.
        };
    }

    private void iniciarLoading(TesteAdaptativoCoordinator.TesteConfig config) {
        if (loadingTimeline != null) {
            loadingTimeline.stop();
        }

        tela.setVisible(false);
        loadingOverlay.setVisible(true);
        loadingOverlay.setOpacity(1);
        loadingProgress.setProgress(0);

        String configuracao = config == null
            ? "Aplicando o fluxo padrao do teste."
            : "Nivel " + config.nivel() + ", foco em " + config.foco() + " e duracao " + config.duracao() + ".";

        String[] mensagens = {
            "Organizando o foco em " + String.join(", ", topicosSelecionados),
            "Separando questoes para " + buildResumoSubtopicos(),
            configuracao,
            "Teste adaptativo pronto!"
        };

        loadingMessage.setText(mensagens[0]);
        loadingTimeline = new Timeline();
        for (int i = 0; i <= 100; i++) {
            final int progresso = i;
            KeyFrame kf = new KeyFrame(Duration.millis(i * 25), e -> {
                loadingProgress.setProgress(progresso / 100.0);
                if (progresso == 25) loadingMessage.setText(mensagens[0]);
                if (progresso == 50) loadingMessage.setText(mensagens[1]);
                if (progresso == 75) loadingMessage.setText(mensagens[2]);
                if (progresso == 100) {
                    loadingMessage.setText(mensagens[3]);
                    finalizarLoading();
                }
            });
            loadingTimeline.getKeyFrames().add(kf);
        }
        loadingTimeline.play();
    }

    private String buildResumoSubtopicos() {
        if (subtopicosSelecionados.isEmpty()) {
            return "todos os subtopicos";
        }
        return subtopicosSelecionados.stream().limit(4).collect(Collectors.joining(", "));
    }

    private void finalizarLoading() {
        PauseTransition pause = new PauseTransition(Duration.millis(300));
        pause.setOnFinished(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(200), loadingOverlay);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> {
                loadingOverlay.setVisible(false);
                tela.setVisible(true);
                iniciarCronometro();
                carregarQuestao(0);
            });
            fadeOut.play();
        });
        pause.play();
    }

    private void iniciarCronometro() {
        if (cronometro != null) {
            cronometro.stop();
        }

        segundos = 0;
        minutos = 0;
        cronometro = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            segundos++;
            if (segundos == 60) {
                minutos++;
                segundos = 0;
            }
            tempo.setText(String.format("%02d:%02d", minutos, segundos));
        }));
        cronometro.setCycleCount(Timeline.INDEFINITE);
        cronometro.play();
    }

    private void carregarQuestao(int index) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(150), tela);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        fadeOut.setOnFinished(e -> {
            atualizarConteudoQuestao(index);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), tela);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        });

        fadeOut.play();
        tempoInicioQuestao = System.currentTimeMillis();
    }

    private void atualizarConteudoQuestao(int index) {
        Questao q = questoes.get(index);

        nPergunta.setText("Questao " + (index + 1) + " / " + totalQuestoes);
        bloco1.setText(q.getEnunciado());
        bloco2.setText(montarBlocoSecundario(q));

        ResA.setText(q.getOpcaoA());
        ResB.setText(q.getOpcaoB());
        ResC.setText(q.getOpcaoC());
        ResD.setText(q.getOpcaoD());
        ResE.setText(q.getOpcaoE());
        ResF.setText(q.getOpcaoF());
        ResG.setText(q.getOpcaoG());

        atualizarApoioVisual(q);

        alternativas.selectToggle(null);
        respostaSelecionada = '\0';
        feedbackContainer.setVisible(false);
        feedbackContainer.setOpacity(1);

        double progresso = totalQuestoes == 0 ? 0 : (double) (index + 1) / totalQuestoes;
        circleProgress.setValue(progresso);

        btnConfirmar.setDisable(false);
        btnProximo.setDisable(true);
    }

    private String montarBlocoSecundario(Questao questao) {
        String foco = "Topico: " + questao.getTopico() + " | Subtopico: " + questao.getSubtopico();
        if (questao.getBloco2() == null || questao.getBloco2().isBlank()) {
            return foco;
        }
        return questao.getBloco2() + "\n" + foco;
    }

    private void configurarPainelApoioVisual() {
        if (bloco1 == null || !(bloco1.getParent() instanceof VBox textoPane) || !(textoPane.getParent() instanceof HBox linhaQuestao)) {
            return;
        }
        if (linhaQuestao.getChildren().size() < 5 || !(linhaQuestao.getChildren().get(1) instanceof Separator separator)) {
            return;
        }

        linhaQuestaoPane = linhaQuestao;
        textoQuestaoPane = textoPane;
        apoioVisualSeparator = separator;
        textoQuestaoPane.setFillWidth(true);
        textoQuestaoPane.setMinWidth(0);
        textoQuestaoPane.setPrefWidth(0);
        textoQuestaoPane.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(textoQuestaoPane, Priority.ALWAYS);
        bloco1.setMaxWidth(Double.MAX_VALUE);
        bloco2.setMaxWidth(Double.MAX_VALUE);

        planoCartesianoPane = new PlanoCartesianoPane();
        planoCartesianoContainer = new StackPane(planoCartesianoPane);
        planoCartesianoContainer.setMinWidth(0);
        planoCartesianoContainer.setPrefWidth(0);
        planoCartesianoContainer.setMaxWidth(Double.MAX_VALUE);
        planoCartesianoContainer.setVisible(false);
        planoCartesianoContainer.setManaged(false);

        apoioVisualBox = new VBox(planoCartesianoContainer);
        apoioVisualBox.setMinWidth(0);
        apoioVisualBox.setPrefWidth(0);
        apoioVisualBox.setMaxWidth(Double.MAX_VALUE);
        apoioVisualBox.setVisible(false);
        apoioVisualBox.setManaged(false);
        HBox.setHgrow(apoioVisualBox, Priority.ALWAYS);

        imgBloco2.setImage(null);
        imgBloco2.setVisible(false);
        imgBloco2.setManaged(false);

        apoioVisualSeparator.setVisible(false);
        apoioVisualSeparator.setManaged(false);
        linhaQuestaoPane.getChildren().setAll(textoQuestaoPane, apoioVisualSeparator, apoioVisualBox);
    }

    private void atualizarApoioVisual(Questao questao) {
        var planoConfig = QuestaoGraficoSupport.resolver(questao);
        boolean graficoVisivel = planoConfig.isPresent();
        if (graficoVisivel && planoCartesianoPane != null) {
            planoCartesianoPane.aplicarConfig(planoConfig.get());
        }
        setNodeVisivel(planoCartesianoContainer, graficoVisivel);
        setNodeVisivel(apoioVisualBox, graficoVisivel);
        setNodeVisivel(apoioVisualSeparator, graficoVisivel);
    }

    private void setNodeVisivel(Node node, boolean visivel) {
        if (node == null) {
            return;
        }
        node.setVisible(visivel);
        node.setManaged(visivel);
    }

    @FXML
    private void confirmarResposta() {
        if (alternativas.getSelectedToggle() == null) {
            mostrarAlerta("Atencao", "Selecione uma alternativa antes de confirmar.");
            return;
        }

        JFXToggleNode selected = (JFXToggleNode) alternativas.getSelectedToggle();
        respostaSelecionada = selected.getText().charAt(0);

        long tempoResposta = System.currentTimeMillis() - tempoInicioQuestao;
        long tempoRespostaSegundos = Math.max(1L, Math.round(tempoResposta / 1000.0));
        temposResposta.add(tempoResposta);
        respostasUsuario.add(respostaSelecionada);

        Questao q = questoes.get(questaoAtual);
        boolean acertou = respostaSelecionada == q.getRespostaCorreta();
        reacao.add(
            new ReacaoTeste(
                q,
                questaoAtual,
                respostaSelecionada,
                tempoRespostaSegundos,
                0.1d, // TODO CONFIG_ADAPTATIVA: placeholder de consistencia por questao; substituir pelo valor real calculado.
                0.1d, // TODO CONFIG_ADAPTATIVA: placeholder de resiliencia por questao; substituir pelo valor real calculado.
                LocalDateTime.now())
            );
        if (acertou) {
            acertos++;
            sequenciaAcertos++;
            sequenciaErros = 0;
            selected.setStyle("-fx-background-color: #10b981; -fx-border-color: #10b981; -fx-text-fill: white;");
            mostrarFeedbackAdaptativo(true, tempoResposta);
        } else {
            erros++;
            sequenciaErros++;
            sequenciaAcertos = 0;
            selected.setStyle("-fx-background-color: #ef4444; -fx-border-color: #ef4444; -fx-text-fill: white;");
            mostrarFeedbackAdaptativo(false, tempoResposta);
            destacarRespostaCorreta(q.getRespostaCorreta());
        }

        ajustarNivelAdaptativo(acertou, tempoResposta);
        atualizarIndicadoresNivel();

        btnProximo.setDisable(false);
        btnConfirmar.setDisable(true);
    }

    private void mostrarFeedbackAdaptativo(boolean acertou, long tempoResposta) {
        boolean foiRapido = tempoResposta < 30000; // TODO CONFIG_ADAPTATIVA: limite de "rapido" ainda fixo em 30s.
        feedbackContainer.setVisible(true);
        feedbackContainer.setOpacity(1);

        if (acertou && foiRapido) {
            feedbackIcon.setText("OK");
            feedbackMessage.setText("Excelente! Rapido e preciso. Vamos subir a exigencia.");
            feedbackContainer.setStyle("-fx-background-color: #ecfdf5; -fx-border-color: #10b981;");
        } else if (acertou) {
            feedbackIcon.setText("OK");
            feedbackMessage.setText("Correta! O foco continua nos mesmos subtopicos.");
            feedbackContainer.setStyle("-fx-background-color: #ecfdf5; -fx-border-color: #10b981;");
        } else if (tempoResposta > 60000) { // TODO CONFIG_ADAPTATIVA: limite de "muito lento" ainda fixo em 60s.
            feedbackIcon.setText("!");
            feedbackMessage.setText("Demorou bastante. Vou manter o foco e reduzir a pressao.");
            feedbackContainer.setStyle("-fx-background-color: #fef2f2; -fx-border-color: #ef4444;");
        } else {
            feedbackIcon.setText("X");
            feedbackMessage.setText("Errada. A resposta correta e " + questoes.get(questaoAtual).getRespostaCorreta() + ".");
            feedbackContainer.setStyle("-fx-background-color: #fef2f2; -fx-border-color: #ef4444;");
        }

        Timeline hideFeedback = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            FadeTransition fade = new FadeTransition(Duration.millis(300), feedbackContainer);
            fade.setFromValue(1);
            fade.setToValue(0);
            fade.setOnFinished(ev -> feedbackContainer.setVisible(false));
            fade.play();
        }));
        hideFeedback.play();
    }

    private void ajustarNivelAdaptativo(boolean acertou, long tempoResposta) {
        boolean foiRapido = tempoResposta < 30000; // TODO CONFIG_ADAPTATIVA: tempo rapido ainda fixo em 30s.

        if (acertou && foiRapido) {
            if (sequenciaAcertos >= 2) { // TODO CONFIG_ADAPTATIVA: `acertos_subir_rapido = 2` ainda fixo.
                nivelAtualAdaptativo = nivelAtualAdaptativo.subir();
            }
        } else if (acertou) {
            if (sequenciaAcertos >= 3) { // TODO CONFIG_ADAPTATIVA: `acertos_subir_lento = 3` ainda fixo.
                nivelAtualAdaptativo = nivelAtualAdaptativo.subir();
            }
        } else if (tempoResposta > 60000) { // TODO CONFIG_ADAPTATIVA: tempo lento ainda fixo em 60s.
            nivelAtualAdaptativo = nivelAtualAdaptativo.descer();
            sequenciaErros = 0;
        } else if (sequenciaErros >= 2) { // TODO CONFIG_ADAPTATIVA: `erros_descer = 2` ainda fixo.
            nivelAtualAdaptativo = nivelAtualAdaptativo.descer();
            sequenciaErros = 0;
        }
    }

    private void atualizarIndicadoresNivel() {
        nivelAtual.setText(nivelAtualAdaptativo.codigo());
        dificuldadeAtual.setText(nivelAtualAdaptativo.estrelas());
        nivelAtual.setStyle("-fx-text-fill: " + nivelAtualAdaptativo.corHex() + ";");
    }

    private void destacarRespostaCorreta(char letra) {
        JFXToggleNode correta = switch (letra) {
            case 'A' -> toggleA;
            case 'B' -> toggleB;
            case 'C' -> toggleC;
            case 'D' -> toggleD;
            case 'E' -> toggleE;
            case 'F' -> toggleF;
            case 'G' -> toggleG;
            default -> null;
        };
        if (correta != null) {
            correta.setStyle("-fx-background-color: #10b981; -fx-border-color: #10b981; -fx-text-fill: white;");
        }
    }

    @FXML
    private void proximaQuestao() {
        limparEstilosToggles();

        if (questaoAtual + 1 < totalQuestoes) {
            Questao proxima = obterProximaQuestao();
            if (proxima == null) {
                finalizarTesteAdaptativo();
                return;
            }
            questoes.add(proxima);
            questaoAtual++;
            carregarQuestao(questaoAtual);
        } else {
            finalizarTesteAdaptativo();
        }
    }

    private Questao obterProximaQuestao() {
        Set<String> idsUsados = questoes.stream()
            .map(Questao::getId)
            .collect(Collectors.toCollection(LinkedHashSet::new));

        return service.getProximaQuestaoAdaptativa(
            disciplinaSelecionada,
            nivelAtualAdaptativo.nivel(),
            topicosSelecionados,
            subtopicosSelecionados,
            idsUsados
        );
    }

    private void limparEstilosToggles() {
        toggleA.setStyle("");
        toggleB.setStyle("");
        toggleC.setStyle("");
        toggleD.setStyle("");
        toggleE.setStyle("");
        toggleF.setStyle("");
        toggleG.setStyle("");
    }

    private void finalizarTesteAdaptativo() {
        if (cronometro != null) {
            cronometro.stop();
        }
        if (loadingTimeline != null) {
            loadingTimeline.stop();
        }

        double mediaTempo = temposResposta.stream().mapToLong(Long::longValue).average().orElse(0);
        double porcentagemAcertos = totalQuestoes == 0 ? 0 : (acertos * 100.0) / totalQuestoes;
        String perfil = determinarPerfil(porcentagemAcertos, mediaTempo);
        String recomendacao = getRecomendacao(porcentagemAcertos);
        UUID candidatoID= Authentication.getCurrentUserId();
        List<QuestaoResultado> questoesResultado = construirQuestoesResultado();
        DiagnosticoDto diagnos;
        try {
            UUID disciplinaId = QuestaoUtil.resolverDisciplinaId(disciplinaSelecionada);
            diagnos = diagnosticoService.getDiagnosticoRepository()
                .buscarUltimoDiagnostico(candidatoID, disciplinaId, disciplinaSelecionada);

            testeService.registrarTesteConcluido(
                nivelAtualAdaptativo,
                candidatoID,
                diagnos == null ? null : diagnos.id(),
                focoQuestoes,
                respostasUsuario,
                reacao,
                tempo.getText(),
                recomendacao
            );
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        ResultadoAvaliacaoController.setResultado(
            new ResultadoPayload(
                "Exame Adaptativo",
                formatarDisciplina(disciplinaSelecionada),
                acertos,
                erros,
                totalQuestoes,
                porcentagemAcertos,
                tempo.getText(),
                nivelAtualAdaptativo.codigo(),
                perfil,
                recomendacao,
                "views/pages/candidato/testes",
                questoesResultado
            )
        );

        StackPane contentHost = testeField == null || testeField.getScene() == null
            ? null
            : (StackPane) testeField.getScene().lookup("#contentHost");
        if (contentHost != null) {
            App.swapContent(contentHost, "views/pages/candidato/resultado-avaliacao");
            return;
        }

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Teste Adaptativo Concluido");
        alert.setHeaderText("Resultado Final Adaptativo");
        alert.setContentText(String.format(
            "RESULTADOS:\n- Acertos: %d/%d (%.1f%%)\n- Nivel alcancado: %s\n- Tempo medio por questao: %.1f segundos\n- Perfil: %s\n\nRecomendacao: %s",
            acertos,
            totalQuestoes,
            porcentagemAcertos,
            nivelAtualAdaptativo.codigo(),
            mediaTempo / 1000,
            perfil,
            recomendacao
        ));
        alert.showAndWait();
    }

    private String determinarPerfil(double porcentagem, double tempoMedio) {
        if (porcentagem >= 80 && tempoMedio < 30000) return "Rapido e preciso"; // TODO CONFIG_ADAPTATIVA: faixa fixa de leitura do resultado (80% / 30s).
        if (porcentagem >= 80) return "Preciso mas lento"; // TODO CONFIG_ADAPTATIVA: faixa fixa de leitura do resultado (80%).
        if (porcentagem >= 60 && tempoMedio < 30000) return "Seguro e agil"; // TODO CONFIG_ADAPTATIVA: faixa fixa de leitura do resultado (60% / 30s).
        if (porcentagem >= 60) return "Cauteloso"; // TODO CONFIG_ADAPTATIVA: faixa fixa de leitura do resultado (60%).
        if (porcentagem >= 40) return "Intermediario"; // TODO CONFIG_ADAPTATIVA: faixa fixa de leitura do resultado (40%).
        return "Em consolidacao";
    }

    private String getRecomendacao(double porcentagem) {
        String foco = subtopicosSelecionados.isEmpty()
            ? "os subtopicos escolhidos"
            : subtopicosSelecionados.stream().limit(3).collect(Collectors.joining(", "));

        if (porcentagem >= 80) { // TODO CONFIG_ADAPTATIVA: faixa fixa de recomendacao (80%).
            return "Parabens! Voce esta pronto para desafios mais avancados em " + foco + ".";
        } else if (porcentagem >= 60) { // TODO CONFIG_ADAPTATIVA: faixa fixa de recomendacao (60%).
            return "Bom trabalho! Continue praticando " + foco + " para subir de nivel.";
        } else if (porcentagem >= 40) { // TODO CONFIG_ADAPTATIVA: faixa fixa de recomendacao (40%).
            return "Vamos melhorar! Foque nos subtopicos " + foco + ".";
        } else {
            return "Vale revisar os fundamentos de " + foco + " antes da proxima tentativa.";
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        TesteAdaptativoCoordinator.requestAlert(titulo, mensagem, null);
    }

    private List<QuestaoResultado> construirQuestoesResultado() {
        List<QuestaoResultado> itens = new ArrayList<>();
        int limite = Math.min(questoes == null ? 0 : questoes.size(), respostasUsuario.size());
        for (int i = 0; i < limite; i++) {
            itens.add(QuestaoResultado.fromQuestao(i + 1, questoes.get(i), respostasUsuario.get(i)));
        }
        return itens;
    }

    private void resetarMetricas() {
        acertos = 0;
        erros = 0;
        sequenciaAcertos = 0;
        sequenciaErros = 0;
        respostasUsuario.clear();
        temposResposta.clear();
        questaoAtual = 0;
        respostaSelecionada = '\0';
        segundos = 0;
        minutos = 0;
        tempo.setText("00:00");
        atualizarIndicadoresNivel();
        limparEstilosToggles();
        alternativas.selectToggle(null);
        btnConfirmar.setDisable(false);
        btnProximo.setDisable(true);
        if (circleProgress != null) {
            circleProgress.setValue(0);
        }
    }

    private String formatarDisciplina(String disciplina) {
        if (disciplina == null) {
            return "-";
        }

        return switch (disciplina) {
            case "MATEMATICA" -> "Matematica";
            case "FISICA" -> "Fisica";
            case "QUIMICA" -> "Quimica";
            case "BIOLOGIA" -> "Biologia";
            case "PORTUGUES" -> "Portugues";
            default -> disciplina;
        };
    }

    private float limitarUnitario(float valor) {
        return Math.max(0f, Math.min(1f, valor));
    }

    @Override
    public void ModalOpen() {
        try {
            modalPai.getChildren().clear();
            modFxml = App.loadFXMLModal("Dificult");
            Node modal = modFxml.load();
            modalPai.getChildren().add(modal);
            cont = modFxml.getController();
            cont.init();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void StartInteligente() {
        try {
            modalPai.getChildren().clear();
            modFxml = App.loadFXMLModal("Topicos");
            Node modal = modFxml.load();
            modalPai.getChildren().add(modal);
            TopicModalController controller = modFxml.getController();
            controller.init();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void Alert() {
        try {
            modalPai.getChildren().clear();
            modFxml = App.loadFXMLModal("Alert");
            Node alertNode = modFxml.load();
            modalPai.getChildren().add(alertNode);
            cont = modFxml.getController();
            ((ModalAlert) cont).init();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void dispose() {
        TesteAdaptativoCoordinator.clearHost(this);
        if (cronometro != null) {
            cronometro.stop();
            cronometro = null;
        }
        if (loadingTimeline != null) {
            loadingTimeline.stop();
            loadingTimeline = null;
        }
    }
}
