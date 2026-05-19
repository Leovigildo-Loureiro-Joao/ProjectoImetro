package com.imetro.ui.controller.candidato.diagnosticos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;

import com.imetro.App;
import com.imetro.domain.dto.MenuEntry;
import com.imetro.domain.dto.Topico;
import com.imetro.domain.enums.NivelDificuldadeAdaptativa;
import com.imetro.services.DiagnosticoService;
import com.imetro.ui.components.CircleProgress;
import com.imetro.ui.components.Item_Cell;
import com.imetro.ui.components.PlanoCartesianoPane;
import com.imetro.ui.controller.candidato.ResultadoAvaliacaoController;
import com.imetro.ui.controller.lifecycle.DisposableController;
import com.imetro.ui.model.Questao;
import com.imetro.ui.modals.ModalAlert;
import com.imetro.ui.modals.ModalController;
import com.imetro.ui.modals.ResultadoCelebracaoContext;
import com.imetro.ui.modals.ResultadoCelebracaoModalController;
import com.imetro.ui.modals.TopicModalController;
import com.imetro.util.Authentication;
import com.imetro.util.QuestaoGraficoSupport;
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
    private Label bloco2;

    @FXML
    private JFXButton btnConfirmar;

    @FXML
    private ListView<MenuEntry> sublist;

    @FXML
    private StackPane swcDiagnos;

    @FXML
    private JFXButton btnProximo;

    @FXML
    private StackPane circleProgressContainer;

    @FXML
    private VBox end;

    @FXML
    private VBox estatisticasPane;

    @FXML
    private ImageView imgBloco2;

    @FXML
    private Label loadingMessage;

    @FXML
    private StackPane loadingOverlay;

    @FXML
    private ProgressBar loadingProgress;

    @FXML
    private Label nPergunta;

    @FXML
    private Label nomeDisc;

    @FXML
    private ProgressBar progressMediaGeral;

    @FXML
    private ProgressBar progressTaxaAcerto;

    @FXML
    private ScrollPane scroll;

    @FXML
    private VBox start;

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

    @FXML
    public StackPane modalPai;

    @FXML
    public AnchorPane diagnosticoField;

    private int h = 0;
    private int m = 0;
    private int s = 0;


    private JFXToggleNode selected;
    private char corretaLetra;
    private Timeline time;
    private CircleProgress circleProgress;
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
    private final DiagnosticoService diagnosticoService = new DiagnosticoService();

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
            new MenuEntry("timeline", "Linha do tempo", FontAwesomeSolid.CALENDAR_TIMES),
            new MenuEntry("statics", "Estatisticas", FontAwesomeSolid.DATABASE)
        );

        sublist.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                navigate(newValue.key());
            }
        });

        sublist.getSelectionModel().selectFirst();

        Platform.runLater(() -> {
            circleProgress = new CircleProgress(35, 35, 35, 0);
            circleProgressContainer.getChildren().add(circleProgress);
            configurarPainelApoioVisual();

            bancoQuestoes = diagnosticoService.carregarQuestoesReais();
            questoes = new ArrayList<>(bancoQuestoes);
            totalQuestoes = questoes.size();

            end.setVisible(false);
            start.setVisible(true);
            tela.setVisible(true);
            btnProximo.setDisable(true);
        });
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
        String subtopicos = buildResumoSubtopicos();

        String configuracao = config == null
            ? "Aplicando o diagnostico padrao."
            : "Nivel " + config.nivel() + ", foco em " + config.foco() + " e duracao " + config.duracao() + ".";

        return new String[] {
            "Analisando os topicos selecionados: " + topicos,
            "Preparando questoes para " + subtopicos,
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
            return "todos os subtopicos";
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
                TimerDiagnostic();
                carregarQuestao(0);
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
        bloco2.setText(montarBlocoSecundarioQuestao(q));

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
        circleProgress.setValue(progresso);

        btnConfirmar.setDisable(false);
        btnProximo.setDisable(true);
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
    private void confirmarResposta(ActionEvent event) {
        if (alternativas.getSelectedToggle() == null) {
            mostrarAlerta("Atencao", "Selecione uma alternativa antes de confirmar.");
            return;
        }

        selected = (JFXToggleNode) alternativas.getSelectedToggle();
        respostaSelecionada = selected.getText().charAt(0);
        respostasUsuario.add(respostaSelecionada);

        Questao q = questoes.get(questaoAtual);
        boolean acertou = respostaSelecionada == q.getRespostaCorreta();
        if (acertou) {
            selected.getStyleClass().add("sucess");
        } else {
            selected.getStyleClass().add("error");
            destacarRespostaCorreta(q.getRespostaCorreta());
        }

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
        if (time != null) {
            time.stop();
        }
        acertos = 0;

        int limiteCorrecao = Math.min(questoes == null ? 0 : questoes.size(), respostasUsuario.size());
        for (int i = 0; i < limiteCorrecao; i++) {
            if (respostasUsuario.get(i) == questoes.get(i).getRespostaCorreta()) {
                acertos++;
            }
        }

        double porcentagem = totalQuestoes == 0 ? 0 : (acertos * 100.0) / totalQuestoes;
        String nivelFinal = getNivelPorPorcentagem(porcentagem);
        String recomendacao = getMensagemMotivacional(porcentagem);
        List<QuestaoResultado> questoesResultado = construirQuestoesResultado();

        diagnosticoService.registrarDiagnosticoConcluido(
            Authentication.getCurrentUserId(),
            questoes,
            respostasUsuario,
            tempo.getText()
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
        if (tempo == null || tempo.getText() == null || tempo.getText().isBlank() || totalQuestoes <= 0) {
            return 0;
        }

        String[] partes = tempo.getText().trim().split(":");
        if (partes.length != 3) {
            return 0;
        }

        try {
            int horas = Integer.parseInt(partes[0]);
            int minutos = Integer.parseInt(partes[1]);
            int segundos = Integer.parseInt(partes[2]);
            int totalSegundos = (horas * 3600) + (minutos * 60) + segundos;
            return totalSegundos / (double) totalQuestoes;
        } catch (NumberFormatException ex) {
            return 0;
        }
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
        prepararDiagnostico();
        if (questoes == null || questoes.isEmpty()) {
            mostrarAlerta(
                "Atencao",
                "Nao encontramos questoes reais no banco para iniciar este diagnostico."
            );
            return;
        }
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

    private List<Questao> aplicarConfiguracaoAoBanco(List<Questao> origem) {
        List<Questao> base = filtrarPorEscopoSelecionado(origem);
        if (base.isEmpty()) {
            base = new ArrayList<>(origem);
        }

        DiagnosticoCoordinator.DiagnosticoConfig config = DiagnosticoCoordinator.getConfiguracaoAtual();

        if (config == null) {
            return base;
        }

        List<Questao> filtradas = filtrarPorNivel(base, config.nivel());
        if (filtradas.isEmpty()) {
            filtradas = base;
        }

        int limite = switch (TextoUtil.normalizarMinusculo(config.duracao())) {
            case "curto" -> 5;
            case "medio" -> 7;
            default -> filtradas.size();
        };

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

    private List<Questao> filtrarPorNivel(List<Questao> origem, String nivelConfigurado) {
        NivelDificuldadeAdaptativa nivel = NivelDificuldadeAdaptativa.fromTexto(nivelConfigurado);
        List<Questao> filtradas = new ArrayList<>();

        for (Questao questao : origem) {
            if (nivel.incluiQuestaoNoFiltroDiagnostico(questao.getNivelDificuldade())) {
                filtradas.add(questao);
            }
        }

        return filtradas;
    }

    private void resetarEstadoDiagnostico() {
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
        h = 0;
        m = 0;
        s = 0;

        tempo.setText("00:00:00");
        btnConfirmar.setDisable(false);
        btnProximo.setDisable(true);
        alternativas.selectToggle(null);

        if (circleProgress != null) {
            circleProgress.setValue(0);
        }
    }

    private void limparEstilosAlternativas() {
        for (JFXToggleNode toggle : List.of(toggleA, toggleB, toggleC, toggleD, toggleE, toggleF, toggleG)) {
            toggle.getStyleClass().removeAll("error", "sucess");
        }
    }

    private void setDiagnosticMode(boolean iniciar) {
        end.setVisible(iniciar);
        start.setVisible(!iniciar);
        if (iniciar) {
            iniciarLoadingInicial();
        }
    }

    @Override
    public void dispose() {
        DiagnosticoCoordinator.clearHost(this);
        if (time != null) {
            time.stop();
            time = null;
        }
        if (loadingTimeline != null) {
            loadingTimeline.stop();
            loadingTimeline = null;
        }
    }

    private void navigate(String key) {
        switch (key) {
            case "mydiagnostic" -> App.swapContent(swcDiagnos, "views/components/DiagnosticoList");
            case "statics" -> App.swapContent(swcDiagnos, "views/components/EstatisticasDiagnostic");
            case "timeline" -> App.swapContent(swcDiagnos, "views/components/TimelineDiagnostic");
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
}
