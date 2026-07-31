package com.imetro.ui.controller.candidato.diagnosticos;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;

import com.imetro.App;
import com.imetro.domain.CacheService;
import com.imetro.domain.dto.MenuEntry;
import com.imetro.domain.dto.Topico;
import com.imetro.domain.dto.configuracao.ConfiguracaoDto;
import com.imetro.domain.enums.NivelDificuldadeAdaptativa;
import com.imetro.domain.enums.NivelDisciplina;
import com.imetro.persistence.repository.ConfiguracoesRepository;
import com.imetro.services.DiagnosticoService;
import com.imetro.services.DisciplinaService;
import com.imetro.services.PlaneamentoEstudoService;
import com.imetro.ui.components.Item_Cell;
import com.imetro.ui.components.PlanoCartesianoPane;
import com.imetro.ui.controller.candidato.resultados.ResultadoAvaliacaoController;
import com.imetro.ui.controller.lifecycle.DisposableController;
import com.imetro.ui.model.Questao;
import com.imetro.ui.modals.ModalAlert;
import com.imetro.ui.modals.ModalController;
import com.imetro.ui.modals.PausaModalController;
import com.imetro.ui.modals.PauseSessionContext;
import com.imetro.ui.modals.ResultadoCelebracaoContext;
import com.imetro.ui.modals.ResultadoCelebracaoModalController;
import com.imetro.ui.modals.TopicModalController;
import com.imetro.ui.support.PlaneamentoEstudoBannerSupport;
import com.imetro.util.CalculoStats;
import com.imetro.util.Authentication;
import com.imetro.util.QuestaoGraficoSupport;
import com.imetro.util.QuestaoUtil;
import com.imetro.util.QuestaoResultado;
import com.imetro.util.ResultadoCelebracaoSupport;
import com.imetro.util.ResultadoPayload;
import com.imetro.util.TextoUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleNode;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class DiagnosticoCandidatoController implements DisposableController, DiagnosticoCoordinator.DiagnosticoHost {

    private static final String SESSAO_PAUSA_CACHE_PREFIX = "diagnostico.pause.";

    @FXML
    private Label ResA;

    @FXML
    private Label ResB;

    @FXML
    private Label ResC;

    @FXML
    private Label ResD;

    @FXML
    private Label ResE;

    @FXML
    private Label ResF;

    @FXML
    private Label ResG;

    @FXML
    private ToggleGroup alternativas;

    @FXML
    private Label bloco1;

    @FXML
    private StackPane graficoPane;

    @FXML
    private Label bloco21;

    @FXML
    private JFXButton btnConfirmar;

    @FXML
    private JFXButton btnProximo;

    @FXML
    private JFXButton btnPausa;

    @FXML
    private AnchorPane diagnosticoField;

    @FXML
    private VBox end;

    @FXML
    private VBox feedbackContainer;

    @FXML
    private Label feedbackIcon;

    @FXML
    private ImageView feedbackImg;

    @FXML
    private Label feedbackMessage;

    @FXML
    private Label loadingMessage;

    @FXML
    private StackPane loadingOverlay;

    @FXML
    private ProgressBar loadingProgress;

    @FXML
    private StackPane modalPai;

    @FXML
    private Label nPergunta;

    @FXML
    private Label nivelAtual12;

    @FXML
    private Label nivelAtual2;

    @FXML
    private Label nivelAtual21;

    @FXML
    private Label nivelAtual23;

    @FXML
    private Label nivelAtual231;

    @FXML
    private Label nivelAtual232;

    @FXML
    private Label nomeDisc;

    @FXML
    private ProgressBar questionProgressBar;

    @FXML
    private ScrollPane scroll;

    @FXML
    private VBox start;

    @FXML
    private ListView<MenuEntry> sublist;

    @FXML
    private StackPane swcDiagnos;

    @FXML
    private VBox tela;

    @FXML
    private Label tempo;

    @FXML
    private JFXToggleNode toggleA;

    @FXML
    private JFXToggleNode toggleB;

    @FXML
    private JFXToggleNode toggleC;

    @FXML
    private JFXToggleNode toggleD;

    @FXML
    private JFXToggleNode toggleE;

    @FXML
    private JFXToggleNode toggleF;

    @FXML
    private JFXToggleNode toggleG;

    private int h = 0;
    private int m = 0;
    private int s = 0;


    private JFXToggleNode selected;
    private char corretaLetra;
    private Timeline time;
    private List<Questao> questoes = new ArrayList<>();
    private List<Questao> bancoQuestoes = new ArrayList<>();
    private int questaoAtual = 0;
    private int totalQuestoes;
    private char respostaSelecionada;
    private final List<Character> respostasUsuario = new ArrayList<>();
    private Timeline loadingTimeline;
    private FXMLLoader modFxml;
    private Node mod;
    private Node modTop;
    private ModalController cont;
    private HBox linhaQuestaoPane;
    private VBox textoQuestaoPane;
    private VBox apoioVisualBox;
    private Separator apoioVisualSeparator;
    private StackPane planoCartesianoContainer;
    private PlanoCartesianoPane planoCartesianoPane;
    private final PlaneamentoEstudoService planeamentoService = new PlaneamentoEstudoService();
    private final DiagnosticoService diagnosticoService = new DiagnosticoService();
    private final ConfiguracoesRepository configuracoesRepository = new ConfiguracoesRepository();
    private boolean sessaoAtiva;
    private boolean pausaUsada;
    @FXML
    public void initialize() throws IOException {
        DiagnosticoCoordinator.setHost(this);

        sublist.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(MenuEntry item, boolean empty) {
                super.updateItem(item, empty);
                setText(null);
                setGraphic(empty || item == null ? null : new Item_Cell(item.title(), item.icon()));
            }
        });

        sublist.getItems().setAll(
            new MenuEntry("mydiagnostic", "Meus diagnosticos", FontAwesomeSolid.BOLT),
            new MenuEntry("plan", "Plano personalizado", FontAwesomeSolid.BOOK_OPEN),
            new MenuEntry("timeline", "Historico", FontAwesomeSolid.CALENDAR_TIMES)
        );

        sublist.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                navigate(newValue.key());
            }
        });

        sublist.getSelectionModel().selectFirst();
        configurarListenerAlternativas();

        Platform.runLater(() -> {
            configurarPainelApoioVisual();

            if (restaurarSessaoPausada()) {
                return;
            }

            bancoQuestoes = diagnosticoService.carregarQuestoesReais();
            questoes = new ArrayList<>(bancoQuestoes);
            totalQuestoes = questoes.size();

            end.setVisible(false);
            start.setVisible(true);
            tela.setVisible(true);
            loadingOverlay.setVisible(false);
            btnProximo.setDisable(true);
            atualizarEstadoBotaoPausa();
        });
    }

    private void iniciarCronometroDiagnostico(int horas, int minutos, int segundos) {
        if (time != null) {
            time.stop();
        }

        h = Math.max(0, horas);
        m = Math.max(0, minutos);
        s = Math.max(0, segundos);
        tempo.setText(String.format("%02d:%02d:%02d", h, m, s));
        TimerDiagnostic();
    }

    private void atualizarEstadoBotaoPausa() {
        if (btnPausa != null) {
            btnPausa.setDisable(!podePausar());
        }
    }

    private boolean podePausar() {
        return sessaoAtiva && !pausaUsada && !estaCarregando();
    }

    private boolean estaCarregando() {
        return loadingOverlay != null && loadingOverlay.isVisible();
    }

    private void setSidebarVisible(boolean visible) {
        Platform.runLater(() -> {
            if (diagnosticoField == null || diagnosticoField.getScene() == null) {
                return;
            }

            Node sidebarNode = diagnosticoField.getScene().lookup("#sidebar");
            if (sidebarNode != null) {
                sidebarNode.setVisible(visible);
                sidebarNode.setManaged(visible);
            }
        });
    }

    private void abrirMenuPausa() {
        if (modalPai == null) {
            return;
        }

        try {
            PauseSessionContext.setRequest(new PauseSessionContext.PauseRequest(
                "Pausa do diagnostico",
                "O tempo ficou congelado. Escolhe como queres continuar.",
                "Continuar",
                "Desistir",
                "Recomeçar",
                this::continuarDiagnosticoPausado,
                this::desistirDiagnosticoPausado,
                this::recomecarDiagnosticoPausado
            ));

            modalPai.getChildren().clear();
            modFxml = App.loadFXMLModal("Pausa");
            Node modal = modFxml.load();
            modalPai.getChildren().add(modal);
            PausaModalController controller = modFxml.getController();
            controller.init();
        } catch (Exception ex) {
            System.err.println("Falha ao abrir o menu de pausa: " + ex.getMessage());
            PauseSessionContext.clear();
            continuarDiagnosticoPausado();
        }
    }

    private void continuarDiagnosticoPausado() {
        limparSessaoPausada();
        atualizarEstadoBotaoPausa();
        iniciarCronometroDiagnostico(h, m, s);
        setSidebarVisible(false);
    }

    private void desistirDiagnosticoPausado() {
        limparSessaoPausada();
        pausaUsada = false;
        resetarEstadoDiagnostico();
        setDiagnosticMode(false);
        loadingOverlay.setVisible(false);
        setSidebarVisible(true);
        atualizarEstadoBotaoPausa();
    }

    private void recomecarDiagnosticoPausado() {
        limparSessaoPausada();
        prepararDiagnostico();
        setSidebarVisible(false);
        setDiagnosticMode(true);
    }

    private void TimerDiagnostic() {
        time = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            s++;
            if (s == 60) {
                s = 0;
                m++;
                if (m == 60) {
                    m = 0;
                    h++;
                }
            }
            tempo.setText(String.format("%02d:%02d:%02d", h, m, s));
        }));
        time.setCycleCount(Timeline.INDEFINITE);
        time.play();
    }

    private void iniciarLoadingInicial() {
        tela.setVisible(false);
        loadingOverlay.setVisible(true);
        loadingOverlay.setOpacity(1);
        loadingProgress.setProgress(0);

        String[] mensagens = buildMensagensLoading();
        loadingMessage.setText(mensagens[0]);
        loadingTimeline = new Timeline();

        for (int i = 0; i <= 100; i++) {
            final int progresso = i;
            KeyFrame kf = new KeyFrame(Duration.millis(i * 25), ev -> {
                loadingProgress.setProgress(progresso / 100.0);

                if (progresso == 25) {
                    loadingMessage.setText(mensagens[0]);
                }
                if (progresso == 50) {
                    loadingMessage.setText(mensagens[1]);
                }
                if (progresso == 75) {
                    loadingMessage.setText(mensagens[2]);
                }
                if (progresso == 95) {
                    loadingMessage.setText(mensagens[3]);
                }

                if (progresso == 100) {
                    finalizarLoading();
                }
            });
            loadingTimeline.getKeyFrames().add(kf);
        }

        loadingTimeline.play();
    }

    private String[] buildMensagensLoading() {
        DiagnosticoCoordinator.DiagnosticoConfig config = DiagnosticoCoordinator.getConfiguracaoAtual();
        String topicos = DiagnosticoCoordinator.buildResumoSelecao();

        String configuracao = config == null
            ? "Aplicando o diagnostico padrao."
            : "Nivel " + config.nivel() + ", foco em " + config.foco() + " e duracao " + config.duracao() + ".";

        return new String[] {
            "Analisando os topicos selecionados: " + topicos,
            "Preparando questoes para os topicos escolhidos: " + topicos,
            configuracao,
            "Quase la..."
        };
    }

    private String buildResumoSubtopicos() {
        List<String> itens = DiagnosticoCoordinator.getSubtopicosSelecionados()
            .values()
            .stream()
            .flatMap(List::stream)
            .limit(4)
            .collect(Collectors.toList());

        if (itens.isEmpty()) {
            return "todos os topicos";
        }

        return String.join(", ", itens);
    }

    private void finalizarLoading() {
        PauseTransition pause = new PauseTransition(Duration.millis(300));
        pause.setOnFinished(ev -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(200), loadingOverlay);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(e -> {
                loadingOverlay.setVisible(false);
                tela.setVisible(true);
                sessaoAtiva = true;
                iniciarCronometroDiagnostico(0, 0, 0);
                carregarQuestao(0);
                atualizarEstadoBotaoPausa();
            });
            fadeOut.play();
        });
        pause.play();
    }

    private void carregarQuestao(int index) {
        atualizarConteudoQuestao(index);
    }

    private void atualizarConteudoQuestao(int index) {
        if (index >= questoes.size()) {
            return;
        }

        Questao q = questoes.get(index);

        nomeDisc.setText(q.getDisciplina());
        nPergunta.setText("Questao " + (index + 1) + " / " + totalQuestoes);

        bloco1.setText(q.getEnunciado());
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
        selected = null;

        double progresso = totalQuestoes == 0 ? 0 : (double) (index + 1) / totalQuestoes;
        if (questionProgressBar != null) {
            questionProgressBar.setProgress(progresso);
        }

        btnConfirmar.setDisable(false);
        btnProximo.setDisable(true);
        atualizarIndicadoresDesempenho();
    }

    private String montarBlocoSecundarioQuestao(Questao questao) {
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
        if (linhaQuestao.getChildren().size() < 3 || !(linhaQuestao.getChildren().get(1) instanceof Separator separator)) {
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

        apoioVisualSeparator.setVisible(false);
        apoioVisualSeparator.setManaged(false);
        linhaQuestaoPane.getChildren().setAll(textoQuestaoPane, apoioVisualSeparator, apoioVisualBox);
        graficoPane.getChildren().set(0, linhaQuestaoPane);

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

    private void configurarListenerAlternativas() {
        alternativas.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle instanceof JFXToggleNode toggle) {
                respostaSelecionada = toggle.getText() == null || toggle.getText().isBlank()
                    ? '\0'
                    : toggle.getText().charAt(0);
            } else {
                respostaSelecionada = '\0';
            }
        });
    }

    @FXML
    private void confirmarResposta(ActionEvent event) {
        if (alternativas.getSelectedToggle() == null) {
            mostrarAlerta("Atencao", "Selecione uma alternativa antes de confirmar.");
            return;
        }

        selected = (JFXToggleNode) alternativas.getSelectedToggle();
        respostaSelecionada = selected.getText().charAt(0);
        respostasUsuario.add(respostaSelecionada);

        Questao q = questoes.get(questaoAtual);
        char alternativaCorreta = QuestaoUtil.resolverAlternativaCorreta(q);
        boolean acertou = QuestaoUtil.respostaEstaCorreta(q, respostaSelecionada);
        if (acertou) {
            selected.getStyleClass().add("sucess");
            acertos++;
        } else {
            selected.getStyleClass().add("error");
            destacarRespostaCorreta(alternativaCorreta);
        }

        atualizarIndicadoresDesempenho();
        btnProximo.setDisable(false);
        btnConfirmar.setDisable(true);
    }

    private void destacarRespostaCorreta(char letra) {
        corretaLetra = letra;
        JFXToggleNode corretaNode = getToggleByLetra(letra);
        if (corretaNode != null) {
            corretaNode.getStyleClass().add("sucess");
        }
    }

    private void removerDestaqueRespostaCorreta() {
        JFXToggleNode corretaNode = getToggleByLetra(corretaLetra);
        if (corretaNode != null) {
            corretaNode.getStyleClass().remove("sucess");
        }
    }

    private JFXToggleNode getToggleByLetra(char letra) {
        return switch (letra) {
            case 'A' -> toggleA;
            case 'B' -> toggleB;
            case 'C' -> toggleC;
            case 'D' -> toggleD;
            case 'E' -> toggleE;
            case 'F' -> toggleF;
            case 'G' -> toggleG;
            default -> null;
        };
    }

    @FXML
    void proximaQuestao(ActionEvent event) {
        if (selected != null) {
            selected.getStyleClass().removeAll("error", "sucess");
        }
        removerDestaqueRespostaCorreta();

        if (questaoAtual + 1 < totalQuestoes) {
            questaoAtual++;
            carregarQuestao(questaoAtual);
        } else {
            finalizarDiagnostico();
        }
    }
    int acertos = 0;
    private void finalizarDiagnostico() {
        sessaoAtiva = false;
        pausaUsada = false;
        limparSessaoPausada();
        if (time != null) {
            time.stop();
        }
        recalcularAcertosDiagnostico();
        setSidebarVisible(true);
        atualizarEstadoBotaoPausa();

        double porcentagem = totalQuestoes == 0 ? 0 : (acertos * 100.0) / totalQuestoes;
        String nivelFinal = getNivelPorPorcentagem(porcentagem);
        String recomendacao = getMensagemMotivacional(porcentagem);
        List<QuestaoResultado> questoesResultado = construirQuestoesResultado();

        loadingMessage.setText("A guardar resultado...");
        loadingOverlay.setVisible(true);
        loadingOverlay.setOpacity(1);
        loadingProgress.setProgress(-1);

        CompletableFuture.runAsync(() -> {
            diagnosticoService.registrarDiagnosticoConcluido(
                Authentication.getCurrentUserId(),
                questoes,
                respostasUsuario,
                tempo.getText()
            );
        }, App.getExecutorService())
        .thenRunAsync(() -> {
            loadingOverlay.setVisible(false);
            PlaneamentoEstudoBannerSupport.aplicar(
                diagnosticoField == null ? null : diagnosticoField.getScene(),
                planeamentoService.resolverEstadoAtual(Authentication.getCurrentUserId())
            );

            ResultadoPayload payload = new ResultadoPayload(
                "Diagnostico Academico",
                resolverResumoDisciplinas(),
                acertos,
                totalQuestoes - acertos,
                totalQuestoes,
                porcentagem,
                tempo.getText(),
                nivelFinal,
                "Diagnostico",
                recomendacao,
                "views/pages/candidato/diagnostico",
                questoesResultado
            );

            ResultadoCelebracaoSupport.CelebrationSummary celebrationSummary = ResultadoCelebracaoSupport.criarResumo(
                Authentication.getCurrentUserId(),
                "Diagnostico Academico",
                resolverResumoDisciplinas(),
                acertos,
                totalQuestoes,
                porcentagem,
                tempo.getText(),
                calcularTempoMedioSegundos(),
                true
            );

            abrirCelebracaoResultado(
                payload,
                celebrationSummary,
                () -> mostrarResultadoFallbackDiagnostico(acertos, porcentagem, nivelFinal, recomendacao)
            );
        }, Platform::runLater)
        .exceptionally(ex -> {
            loadingOverlay.setVisible(false);
            System.err.println("Erro ao finalizar diagnostico: " + ex.getMessage());
            ex.printStackTrace();
            Platform.runLater(() -> mostrarAlerta("Erro ao guardar", "Ocorreu um erro ao guardar os resultados do diagnostico."));
            return null;
        });
    }

    private String resolverResumoDisciplinas() {
        return questoes.stream()
            .map(Questao::getDisciplina)
            .distinct()
            .collect(Collectors.joining(", "));
    }

    private List<QuestaoResultado> construirQuestoesResultado() {
        List<QuestaoResultado> itens = new ArrayList<>();
        int limite = Math.min(questoes == null ? 0 : questoes.size(), respostasUsuario.size());
        for (int i = 0; i < limite; i++) {
            itens.add(QuestaoResultado.fromQuestao(i + 1, questoes.get(i), respostasUsuario.get(i)));
        }
        return itens;
    }

    private double calcularTempoMedioSegundos() {
        if (respostasUsuario.isEmpty()) {
            return 0;
        }

        return obterTempoDecorridoSegundos() / (double) respostasUsuario.size();
    }

    private void atualizarIndicadoresDesempenho() {
        int totalRespondidas = Math.min(questoes == null ? 0 : questoes.size(), respostasUsuario.size());
        double precisaoAtual = calcularPrecisaoTempoReal(totalRespondidas);
        double consistenciaAtual = calcularConsistenciaTempoReal(totalRespondidas);
        double velocidadeAtual = totalRespondidas <= 0
            ? 0d
            : CalculoStats.calcularVelocidade(obterTempoDecorridoSegundos(), totalRespondidas, getConfigCadidato()) * 100d;

        aplicarPercentual(nivelAtual23, precisaoAtual);
        aplicarPercentual(nivelAtual231, consistenciaAtual);
        aplicarPercentual(nivelAtual232, velocidadeAtual);
    }

    private double calcularPrecisaoTempoReal(int totalRespondidas) {
        if (totalRespondidas <= 0 || questoes == null || respostasUsuario == null) {
            return 0d;
        }

        int limite = Math.min(totalRespondidas, Math.min(questoes.size(), respostasUsuario.size()));
        if (limite <= 0) {
            return 0d;
        }

        double soma = 0d;
        for (int i = 0; i < limite; i++) {
            soma += CalculoStats.calcularPrecisaoResposta(questoes.get(i), respostasUsuario.get(i));
        }
        return (soma / limite) * 100d;
    }

    private double calcularConsistenciaTempoReal(int totalRespondidas) {
        if (totalRespondidas <= 0 || questoes == null || respostasUsuario == null) {
            return 0d;
        }
        if (totalRespondidas == 1) {
            return 50d;
        }

        int limite = Math.min(totalRespondidas, Math.min(questoes.size(), respostasUsuario.size()));
        if (limite < 2) {
            return 50d;
        }

        double precisaoAtual = calcularPrecisaoTempoReal(limite);
        double precisaoAnterior = calcularPrecisaoTempoReal(limite - 1);
        return CalculoStats.calcularConsistencia(precisaoAnterior, precisaoAtual) * 100d;
    }

    private int obterTempoDecorridoSegundos() {
        return Math.max(0, (h * 3600) + (m * 60) + s);
    }

    private void aplicarPercentual(Label label, double valor) {
        if (label == null) {
            return;
        }
        label.setText(Math.round(Math.max(0d, Math.min(100d, valor))) + "%");
    }

    private int recalcularAcertosDiagnostico() {
        int limiteCorrecao = Math.min(questoes == null ? 0 : questoes.size(), respostasUsuario.size());
        int totalAcertos = 0;
        for (int i = 0; i < limiteCorrecao; i++) {
            if (QuestaoUtil.respostaEstaCorreta(questoes.get(i), respostasUsuario.get(i))) {
                totalAcertos++;
            }
        }
        acertos = totalAcertos;
        return totalAcertos;
    }

    private String getNivelPorPorcentagem(double pct) {
        if (pct >= 80) {
            return "Scholarship Ready";
        }
        if (pct >= 60) {
            return "Avancado";
        }
        if (pct >= 40) {
            return "Intermediario";
        }
        if (pct >= 20) {
            return "ISAF";
        }
        return "INAF";
    }

    private String getMensagemMotivacional(double pct) {
        if (pct >= 80) {
            return "Parabens! Voce esta pronto para bolsas de estudo!";
        }
        if (pct >= 60) {
            return "Bom trabalho! Continue praticando para alcancar o proximo nivel.";
        }
        if (pct >= 40) {
            return "Vamos melhorar! Foque nos pontos fracos identificados.";
        }
        if (pct >= 20) {
            return "Voce precisa de mais pratica. Nao desista!";
        }
        return "Vamos recomecar? O diagnostico identificou areas para melhoria.";
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void abrirCelebracaoResultado(
        ResultadoPayload payload,
        ResultadoCelebracaoSupport.CelebrationSummary celebrationSummary,
        Runnable fallback
    ) {
        StackPane contentHost = diagnosticoField == null || diagnosticoField.getScene() == null
            ? null
            : (StackPane) diagnosticoField.getScene().lookup("#contentHost");

        Runnable onContinue = () -> {
            ResultadoAvaliacaoController.setResultado(payload);
            if (contentHost != null) {
                App.swapContent(contentHost, "views/pages/candidato/resultado-avaliacao");
            } else if (fallback != null) {
                fallback.run();
            }
        };

        if (modalPai == null) {
            onContinue.run();
            return;
        }

        try {
            ResultadoCelebracaoContext.definir(
                new ResultadoCelebracaoContext.CelebrationRequest(celebrationSummary, onContinue)
            );
            modalPai.getChildren().clear();
            modFxml = App.loadFXMLModal("CelebracaoResultado");
            Node modalNode = modFxml.load();
            modalPai.getChildren().add(modalNode);
            ResultadoCelebracaoModalController controller = modFxml.getController();
            controller.init();
        } catch (Exception ex) {
            ResultadoCelebracaoContext.limpar();
            onContinue.run();
        }
    }

    private void mostrarResultadoFallbackDiagnostico(
        int acertos,
        double porcentagem,
        String nivelFinal,
        String recomendacao
    ) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Diagnostico concluido");
        alert.setHeaderText("Resultado final");
        alert.setContentText(String.format(
            "Voce acertou %d de %d questoes\nPorcentagem: %.1f%%\nNivel: %s\n%s",
            acertos,
            totalQuestoes,
            porcentagem,
            nivelFinal,
            recomendacao
        ));
        alert.showAndWait();
        setDiagnosticMode(false);
    }

    @Override
    public void startDiagnostico() {
        if (!planeamentoService.podeIniciarDiagnostico(Authentication.getCurrentUserId())) {
            mostrarAlerta(
                "Diagnostico indisponivel",
                "Aguarda 7 dias desde o ultimo diagnostico ou completa os topicos pendentes no plano de estudo. Se quiseres recomecar do zero, limpa os diagnosticos primeiro."
            );
            return;
        }

        limparSessaoPausada();
        prepararDiagnostico();
        if (questoes == null || questoes.isEmpty()) {
            mostrarAlerta(
                "Atencao",
                "Nao encontramos questoes reais no banco para iniciar este diagnostico."
            );
            return;
        }
        pausaUsada = false;
        setSidebarVisible(false);
        setDiagnosticMode(true);
    }

    private void prepararDiagnostico() {
        if (bancoQuestoes.isEmpty()) {
            bancoQuestoes = diagnosticoService.carregarQuestoesReais();
        }
        questoes = aplicarConfiguracaoAoBanco(bancoQuestoes);
        totalQuestoes = questoes.size();
        resetarEstadoDiagnostico();
    }

    private List<Questao> Reorganizar(List<Questao> base){

        Set<String> subtopicos = base.stream().map(a -> a.getSubtopico()).collect(Collectors.toSet());
        List<Questao> result=new ArrayList<>();

        for (String subtop : subtopicos) {
            List<Questao>  resultTemp=base.stream().filter(q -> q.getSubtopico().equals(subtop)).toList();
            Set<Integer> valueSet=new HashSet<>();
            while (valueSet.size()<6 &&  valueSet.size()<resultTemp.size()) {
                valueSet.add(new Random().nextInt(0, resultTemp.size()));
            }
            for (Integer res : valueSet) {
                result.add(resultTemp.get(res));
            }
        }
        return result;
    }

    private List<Questao> aplicarConfiguracaoAoBanco(List<Questao> origem) {
        List<Questao> base = filtrarPorEscopoSelecionado(origem);
        if (base.isEmpty()) {
            base = new ArrayList<>(origem);
        }

        DiagnosticoCoordinator.DiagnosticoConfig config = DiagnosticoCoordinator.getConfiguracaoAtual();

        if (config == null) {
            String[] partes = planeamentoService.gerarResumo().focoAtual().trim().split("-");
            if (partes.length < 2) {
                return base;
            }
            String topico = partes[1].toLowerCase();
            return base.stream().filter(filt -> filt.getSubtopico().toLowerCase().contains(topico.trim())).toList();
        }
        List<Questao> filtradas = List.of();
        int limite=0;
        try {
            filtradas = filtrarPorNivel(base, config.nivel());
            if (filtradas.isEmpty()) {
                filtradas = base;
            }

            limite = resolverLimiteQuestoes(config.duracao(), filtradas.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (limite <= 0) {
            limite = filtradas.size();
        }
        return new ArrayList<>(filtradas.subList(0, Math.min(limite, filtradas.size())));
    }




    private List<Questao> filtrarPorEscopoSelecionado(List<Questao> origem) {
        List<Topico> topicosSelecionados = DiagnosticoCoordinator.getTopicosSelecionados();
        Map<String, List<String>> subtopicosSelecionados = DiagnosticoCoordinator.getSubtopicosSelecionados();

        if (topicosSelecionados.isEmpty()) {
            return new ArrayList<>(origem);
        }

        Set<String> disciplinas = topicosSelecionados.stream()
            .map(Topico::disciplina)
            .map(TextoUtil::normalizarMinusculo)
            .collect(Collectors.toSet());

        Set<String> topicos = topicosSelecionados.stream()
            .map(Topico::topicos)
            .map(TextoUtil::normalizarMinusculo)
            .collect(Collectors.toSet());

        Set<String> subtopicos = subtopicosSelecionados.values()
            .stream()
            .flatMap(List::stream)
            .map(TextoUtil::normalizarMinusculo)
            .collect(Collectors.toSet());

        return origem.stream()
            .filter(questao -> disciplinas.isEmpty() || disciplinas.contains(TextoUtil.normalizarMinusculo(questao.getDisciplina())))
            .filter(questao -> topicos.isEmpty() || topicos.contains(TextoUtil.normalizarMinusculo(questao.getTopico())))
            .filter(questao -> subtopicos.isEmpty() || subtopicos.contains(TextoUtil.normalizarMinusculo(questao.getSubtopico())))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    private List<Questao> filtrarPorNivel(List<Questao> origem, String nivelConfigurado) throws SQLException {
        NivelDificuldadeAdaptativa nivel = NivelDificuldadeAdaptativa.fromTexto(nivelConfigurado);
        List<Questao> filtradas = new ArrayList<>();

        for (Questao questao : origem) {
            NivelDisciplina nivelActual = resolverNivelAtualDisciplina(questao);
            int nivelDisciplinaComoDificuldade = mapearNivelDisciplinaParaDificuldade(nivelActual);
            if (nivel.incluiQuestaoNoFiltroDiagnostico(questao.getNivelDificuldade())
                || nivel.incluiQuestaoNoFiltroDiagnostico(nivelDisciplinaComoDificuldade)) {
                filtradas.add(questao);
            }
        }

        return filtradas;
    }

    private int resolverLimiteQuestoes(String codigoDuracao, int totalDisponivel) {
        ConfiguracaoDto config = configuracoesRepository.findByCandidato(Authentication.getCurrentUserId());
        if (config == null || codigoDuracao == null || codigoDuracao.isBlank()) {
            return totalDisponivel;
        }

        return switch (TextoUtil.normalizarMinusculo(codigoDuracao)) {
            case "curto" -> limitarFaixaQuestoes(config.curto_test_q(), totalDisponivel);
            case "medio" -> limitarFaixaQuestoes(config.norm_test_q(), totalDisponivel);
            default -> limitarFaixaQuestoes(config.long_test_q(), totalDisponivel);
        };
    }

    private ConfiguracaoDto getConfigCadidato() {
        return configuracoesRepository.findByCandidato(Authentication.getCurrentUserId());
    }

    private int limitarFaixaQuestoes(Integer limiteConfigurado, int totalDisponivel) {
        if (limiteConfigurado == null || limiteConfigurado <= 0) {
            return totalDisponivel;
        }
        return Math.min(limiteConfigurado, totalDisponivel);
    }

    private NivelDisciplina resolverNivelAtualDisciplina(Questao questao) throws SQLException {
        if (questao == null || questao.getDisciplina() == null || questao.getDisciplina().isBlank()) {
            return NivelDisciplina.INICIANTE;
        }

        var progresso = DisciplinaService.getDisciplinaCandidato(QuestaoUtil.resolverDisciplinaId(questao.getDisciplina()));
        if (progresso == null || progresso.nivelAtual() == null) {
            return NivelDisciplina.INICIANTE;
        }
        return progresso.nivelAtual();
    }

    private int mapearNivelDisciplinaParaDificuldade(NivelDisciplina nivelDisciplina) {
        return switch (nivelDisciplina == null ? NivelDisciplina.INICIANTE : nivelDisciplina) {
            case INICIANTE -> NivelDificuldadeAdaptativa.FACIL.nivel();
            case INTERMEDIARIO -> NivelDificuldadeAdaptativa.MEDIO.nivel();
            case AVANCADO -> NivelDificuldadeAdaptativa.DIFICIL.nivel();
        };
    }

    private void resetarEstadoDiagnostico() {
        sessaoAtiva = false;
        if (time != null) {
            time.stop();
            time = null;
        }
        if (loadingTimeline != null) {
            loadingTimeline.stop();
            loadingTimeline = null;
        }

        limparEstilosAlternativas();
        respostasUsuario.clear();
        questaoAtual = 0;
        respostaSelecionada = '\0';
        selected = null;
        corretaLetra = '\0';
        acertos = 0;
        h = 0;
        m = 0;
        s = 0;

        tempo.setText("00:00:00");
        btnConfirmar.setDisable(false);
        btnProximo.setDisable(true);
        alternativas.selectToggle(null);
        atualizarIndicadoresDesempenho();
        atualizarEstadoBotaoPausa();

        if (questionProgressBar != null) {
            questionProgressBar.setProgress(0);
        }
    }

    private void limparSessaoPausada() {
        String chave = resolverChaveSessaoPausa();
        if (chave != null) {
            CacheService.remove(chave);
        }
    }

    private void salvarSessaoPausada() {
        String chave = resolverChaveSessaoPausa();
        if (chave == null || questoes == null || questoes.isEmpty()) {
            return;
        }

        CacheService.put(
            chave,
            new SessaoDiagnosticoPausada(
                new ArrayList<>(questoes),
                questaoAtual,
                new ArrayList<>(respostasUsuario),
                h,
                m,
                s,
                respostaSelecionada,
                System.currentTimeMillis()
            )
        );
    }

    private boolean restaurarSessaoPausada() {
        String chave = resolverChaveSessaoPausa();
        if (chave == null) {
            return false;
        }

        Object estadoBruto = CacheService.get(chave);
        if (!(estadoBruto instanceof SessaoDiagnosticoPausada estado)) {
            return false;
        }

        CacheService.remove(chave);
        if (estado.questoes() == null || estado.questoes().isEmpty()) {
            return false;
        }

        questoes = new ArrayList<>(estado.questoes());
        totalQuestoes = questoes.size();
        questaoAtual = Math.max(0, Math.min(estado.questaoAtual(), Math.max(0, totalQuestoes - 1)));
        respostasUsuario.clear();
        respostasUsuario.addAll(estado.respostasUsuario());
        recalcularAcertosDiagnostico();

        h = Math.max(0, estado.h());
        m = Math.max(0, estado.m());
        s = Math.max(0, estado.s());
        char respostaSalva = estado.respostaSelecionada();
        respostaSelecionada = respostaSalva;
        selected = null;

        end.setVisible(true);
        start.setVisible(false);
        tela.setVisible(true);
        loadingOverlay.setVisible(false);

        if (questionProgressBar != null) {
            questionProgressBar.setProgress(totalQuestoes == 0 ? 0 : (double) (questaoAtual + 1) / totalQuestoes);
        }

        if (!questoes.isEmpty()) {
            atualizarConteudoQuestao(questaoAtual);
            restaurarRespostaQuestaoAtual(respostaSalva);
        }

        sessaoAtiva = true;
        pausaUsada = true;
        setSidebarVisible(false);
        iniciarCronometroDiagnostico(h, m, s);
        atualizarEstadoBotaoPausa();
        return true;
    }

    private void restaurarRespostaQuestaoAtual(char respostaSalva) {
        if (questaoAtual < 0 || questaoAtual >= questoes.size()) {
            return;
        }

        char respostaRestaurada = questaoAtual < respostasUsuario.size()
            ? respostasUsuario.get(questaoAtual)
            : respostaSalva;

        if (respostaRestaurada == '\0') {
            btnConfirmar.setDisable(false);
            btnProximo.setDisable(true);
            return;
        }

        JFXToggleNode toggle = getToggleByLetra(respostaRestaurada);
        if (toggle != null) {
            alternativas.selectToggle(toggle);
        }

        selected = toggle;
        respostaSelecionada = respostaRestaurada;

        if (questaoAtual < respostasUsuario.size()) {
            Questao q = questoes.get(questaoAtual);
            char alternativaCorreta = QuestaoUtil.resolverAlternativaCorreta(q);
            boolean acertou = QuestaoUtil.respostaEstaCorreta(q, respostaRestaurada);

            limparEstilosAlternativas();
            if (toggle != null) {
                toggle.getStyleClass().add(acertou ? "sucess" : "error");
            }
            if (!acertou) {
                destacarRespostaCorreta(alternativaCorreta);
            }

            btnConfirmar.setDisable(true);
            btnProximo.setDisable(false);
            return;
        }

        btnConfirmar.setDisable(false);
        btnProximo.setDisable(true);
    }

    private String resolverChaveSessaoPausa() {
        return Authentication.getCurrentUserId() == null
            ? null
            : SESSAO_PAUSA_CACHE_PREFIX + Authentication.getCurrentUserId();
    }

    private record SessaoDiagnosticoPausada(
        List<Questao> questoes,
        int questaoAtual,
        List<Character> respostasUsuario,
        int h,
        int m,
        int s,
        char respostaSelecionada,
        long pausadoEm
    ) {
    }

    private void limparEstilosAlternativas() {
        for (JFXToggleNode toggle : List.of(toggleA, toggleB, toggleC, toggleD, toggleE, toggleF, toggleG)) {
            toggle.getStyleClass().removeAll("error", "sucess");
        }
    }

    private void setDiagnosticMode(boolean iniciar) {
        sessaoAtiva = iniciar;
        end.setVisible(iniciar);
        start.setVisible(!iniciar);
        if (iniciar) {
            iniciarLoadingInicial();
        } else {
            loadingOverlay.setVisible(false);
            setSidebarVisible(true);
        }
        atualizarEstadoBotaoPausa();
    }

    @Override
    public void dispose() {
        DiagnosticoCoordinator.clearHost(this);
        if (sessaoAtiva) {
            salvarSessaoPausada();
        }
        if (time != null) {
            time.stop();
            time = null;
        }
        if (loadingTimeline != null) {
            loadingTimeline.stop();
            loadingTimeline = null;
        }
        setSidebarVisible(true);
    }

    private void navigate(String key) {
        switch (key) {
            case "mydiagnostic" -> App.swapContent(swcDiagnos, "views/components/diagnostico/DiagnosticoList");

            case "plan" -> App.swapContent(swcDiagnos, "views/components/diagnostico/PlanoPersonalizado");

            case "timeline" -> App.swapContent(swcDiagnos, "views/components/diagnostico/TimelineDiagnostic");
            default -> {
            }
        }
    }

    @Override
    public void ModalOpen() {
        try {
            modalPai.getChildren().clear();
            modFxml = App.loadFXMLModal("Dificult");
            mod = modFxml.load();
            modalPai.getChildren().add(mod);
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
            modTop = modFxml.load();
            modalPai.getChildren().add(modTop);
            TopicModalController conts = modFxml.getController();
            conts.init();
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

    @FXML
    public void desisitir(ActionEvent event) {
        limparSessaoPausada();
        pausaUsada = false;
        resetarEstadoDiagnostico();
        setDiagnosticMode(false);
        loadingOverlay.setVisible(false);
    }

    @FXML
    public void pausar(ActionEvent event) {
        if (!sessaoAtiva || pausaUsada || estaCarregando()) {
            return;
        }

        pausaUsada = true;
        atualizarEstadoBotaoPausa();
        salvarSessaoPausada();
        if (time != null) {
            time.stop();
        }
        abrirMenuPausa();
    }
}
