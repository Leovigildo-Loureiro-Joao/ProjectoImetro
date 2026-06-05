package com.imetro.ui.controller.candidato.testes;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.imetro.App;
import com.imetro.domain.CacheService;
import com.imetro.domain.dto.Topico;
import com.imetro.domain.dto.configuracao.AdaptacaoDto;
import com.imetro.domain.dto.configuracao.ConfiguracaoDto;
import com.imetro.domain.dto.diagnostico.DiagnosticoDto;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoEstado;
import com.imetro.domain.dto.test.Percent;
import com.imetro.domain.dto.test.TesteDto;
import com.imetro.domain.dto.test.ReacaoTeste;
import com.imetro.domain.dto.test.TrilhaAdaptacaoSubtopico;
import com.imetro.domain.enums.NivelDificuldadeAdaptativa;
import com.imetro.persistence.repository.AdaptacaoRepository;
import com.imetro.persistence.repository.ConfiguracoesRepository;
import com.imetro.services.DiagnosticoService;
import com.imetro.services.PlaneamentoEstudoService;
import com.imetro.services.TesteAdaptativoService;
import com.imetro.services.TesteService;
import com.imetro.services.TesteService.ResumoHistoricoDisciplina;
import com.imetro.ui.components.PlanoCartesianoPane;
import com.imetro.ui.components.TesteCard;
import com.imetro.ui.controller.candidato.resultados.ResultadoAvaliacaoController;
import com.imetro.ui.controller.lifecycle.DisposableController;
import com.imetro.ui.model.Questao;
import com.imetro.ui.modals.ModalAlert;
import com.imetro.ui.modals.ModalController;
import com.imetro.ui.modals.ResultadoCelebracaoContext;
import com.imetro.ui.modals.ResultadoCelebracaoModalController;
import com.imetro.ui.modals.TopicModalController;
import com.imetro.util.Authentication;
import com.imetro.util.Loading;
import com.imetro.util.QuestaoGraficoSupport;
import com.imetro.util.QuestaoResultado;
import com.imetro.util.ResultadoCelebracaoSupport;
import com.imetro.util.QuestaoUtil;
import com.imetro.util.ResultadoPayload;
import com.imetro.util.TextoUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
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

    private static final String SESSAO_PAUSA_CACHE_PREFIX = "teste.adaptativo.pause.";

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
    private JFXButton btnProximo;

    @FXML
    private Label dificuldadeAtual;

    @FXML
    private VBox disciplinasContainer;

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
    private Label nivelAtual;

    @FXML
    private Label nivelAtual1;

    @FXML
    private Label nivelAtual11;

    @FXML
    private Label nivelAtual12;

    @FXML
    private Label nivelAtual2;

    @FXML
    private Label nivelAtual23;

    @FXML
    private Label nivelAtual231;

    @FXML
    private Label nivelAtual232;

    @FXML
    private Label nomeDisc;

    @FXML
    private Label planHintLabel;

    @FXML
    private ProgressBar questionProgressBar;

    @FXML
    private VBox start;

    @FXML
    private VBox tela;

    @FXML
    private Label tempo;

    @FXML
    private VBox testeContainer;

    @FXML
    private AnchorPane testeField;

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
    private VBox trilhoAdaptacaoCard;

    @FXML
    private Label trilhoAvancosValue;

    @FXML
    private Label trilhoDificuldadeValue;

    @FXML
    private JFXComboBox<String> trilhoDisciplinaCombo;

    @FXML
    private Label trilhoLivroValue;

    @FXML
    private Label trilhoPaginasValue;

    @FXML
    private ProgressBar trilhoProgressoBar;

    @FXML
    private Label trilhoProgressoValue;

    @FXML
    private Label trilhoQuedasValue;

    @FXML
    private Label trilhoResumoValue;

    @FXML
    private Label trilhoStatusValue;

    @FXML
    private JFXComboBox<String> trilhoSubtopicoCombo;

    private ConfiguracaoDto configCandidato;
    private List<String> disciplinas = List.of();
    private Map<String, ResumoHistoricoDisciplina> resumos = Map.of();
    private ConfiguracoesRepository configuracoesRepository = new ConfiguracoesRepository();
    private AdaptacaoDto adaptacaoDto;
    private AdaptacaoRepository adaptacaoRepository = new AdaptacaoRepository();
    private final VBox botoesDisciplinasBox = new VBox(12);
    private final List<Character> respostasUsuario = new ArrayList<>();
    private final List<Long> temposResposta = new ArrayList<>();
    private final List<String> topicosSelecionados = new ArrayList<>();
    private final List<String> subtopicosSelecionados = new ArrayList<>();
    private final List<ReacaoTeste> reacao = new ArrayList<>();
    private final Map<String, List<TrilhaAdaptacaoSubtopico>> trilhoAdaptacaoCache = new LinkedHashMap<>();

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
    private final PlaneamentoEstudoService planeamentoService = new PlaneamentoEstudoService();
    private HBox linhaQuestaoPane;
    private VBox textoQuestaoPane;
    private VBox apoioVisualBox;
    private Separator apoioVisualSeparator;
    private StackPane planoCartesianoContainer;
    private PlanoCartesianoPane planoCartesianoPane;
    private boolean sessaoAtiva;
    @FXML
    public void initialize() {
        TesteAdaptativoCoordinator.setHost(this);
        testeService=new TesteService();
        configurarPainelApoioVisual();
        configurarTrilhoAdaptacao();
        configurarListenerAlternativas();

        service = new TesteAdaptativoService();
        botoesDisciplinasBox.setFillWidth(false);
        botoesDisciplinasBox.setAlignment(Pos.TOP_LEFT);
        disciplinasContainer.getChildren().setAll(botoesDisciplinasBox);

        if (restaurarSessaoPausada()) {
            Platform.runLater(this::atualizarEstadoPlanejamento);
            return;
        }

        if (diagnosticoService.temHistoricoDiagnostico(Authentication.getCurrentUserId())) {
            carregarDisciplinas();
        } else {
            carregarBloqueioPrimeiroDiagnostico();
        }

        feedbackContainer.setVisible(false);
        testeContainer.setVisible(false);
        start.setVisible(true);
        atualizarIndicadoresNivel();
        Platform.runLater(this::atualizarEstadoPlanejamento);
    }

    private void atualizarEstadoPlanejamento() {
        if (planHintLabel == null) {
            return;
        }

        PlaneamentoEstudoEstado estado = planeamentoService.resolverEstadoAtual(Authentication.getCurrentUserId());
        String texto = estado.titulo();
        if (estado.detalhe() != null && !estado.detalhe().isBlank()) {
            texto += " - " + estado.detalhe();
        }
        planHintLabel.setText(texto);
    }

    private void configurarTrilhoAdaptacao() {
        if (trilhoDisciplinaCombo == null || trilhoSubtopicoCombo == null) {
            return;
        }

        trilhoDisciplinaCombo.valueProperty().addListener((obs, oldValue, newValue) -> atualizarTrilhoSubtopicos(newValue));
        trilhoSubtopicoCombo.valueProperty().addListener((obs, oldValue, newValue) -> atualizarDetalheTrilho(
            trilhoDisciplinaCombo.getValue(),
            newValue
        ));
        configurarTrilhoAdaptacaoBloqueado("Disponivel depois do primeiro diagnostico real.");
    }

    private void atualizarTrilhoDisciplinas(List<String> disciplinas) {
        if (trilhoDisciplinaCombo == null || trilhoSubtopicoCombo == null) {
            return;
        }

        trilhoAdaptacaoCache.clear();
        trilhoDisciplinaCombo.getItems().setAll(disciplinas == null ? List.of() : disciplinas);
        trilhoDisciplinaCombo.setDisable(disciplinas == null || disciplinas.isEmpty());
        trilhoSubtopicoCombo.setDisable(true);

        if (disciplinas == null || disciplinas.isEmpty()) {
            resetarTrilhoDetalhe("Sem trilho disponivel", "Ainda nao encontramos progresso adaptativo por subtópico.");
            return;
        }

        trilhoDisciplinaCombo.getSelectionModel().selectFirst();
        atualizarTrilhoSubtopicos(trilhoDisciplinaCombo.getValue());
    }

    private void atualizarTrilhoSubtopicos(String disciplina) {
        if (trilhoSubtopicoCombo == null) {
            return;
        }
        if (disciplina == null || disciplina.isBlank()) {
            trilhoSubtopicoCombo.getItems().clear();
            trilhoSubtopicoCombo.setDisable(true);
            resetarTrilhoDetalhe("Sem selecao", "Escolha uma disciplina para abrir o trilho.");
            return;
        }

        List<TrilhaAdaptacaoSubtopico> itens = trilhoAdaptacaoCache.computeIfAbsent(
            disciplina,
            testeService::carregarTrilhaAdaptacao
        );

        if (itens.isEmpty()) {
            trilhoSubtopicoCombo.getItems().clear();
            trilhoSubtopicoCombo.setDisable(true);
            resetarTrilhoDetalhe("Sem dados", "Esta disciplina ainda nao tem trilho suficiente por subtópico.");
            return;
        }

        trilhoSubtopicoCombo.getItems().setAll(itens.stream().map(TrilhaAdaptacaoSubtopico::subtopico).toList());
        trilhoSubtopicoCombo.setDisable(false);
        trilhoSubtopicoCombo.getSelectionModel().selectFirst();
        atualizarDetalheTrilho(disciplina, trilhoSubtopicoCombo.getValue());
    }

    private void atualizarDetalheTrilho(String disciplina, String subtopico) {
        if (disciplina == null || disciplina.isBlank() || subtopico == null || subtopico.isBlank()) {
            resetarTrilhoDetalhe("Sem selecao", "Escolha um subtópico para ver a leitura guiada.");
            return;
        }

        List<TrilhaAdaptacaoSubtopico> itens = trilhoAdaptacaoCache.getOrDefault(disciplina, List.of());
        TrilhaAdaptacaoSubtopico item = itens.stream()
            .filter(valor -> subtopico.equalsIgnoreCase(valor.subtopico()))
            .findFirst()
            .orElse(null);

        if (item == null) {
            resetarTrilhoDetalhe("Sem dados", "Nao encontramos detalhes para este subtópico.");
            return;
        }

        trilhoStatusValue.setText(item.precisaRevisao() ? "Revisao recomendada" : "Em adaptacao");
        trilhoResumoValue.setText(item.observacao());
        trilhoLivroValue.setText("Livro: " + firstNonBlank(item.recomendacaoLivro(), "Sem livro definido"));
        trilhoPaginasValue.setText("Paginas: " + firstNonBlank(item.recomendacaoPaginas(), "Paginas por definir"));
        trilhoAvancosValue.setText("Avancos: " + item.avancosRecentes());
        trilhoQuedasValue.setText("Quedas: " + item.quedasRecentes());
        trilhoDificuldadeValue.setText("Dificuldade media: " + Math.round(item.dificuldadeMediaPercentual()) + "%");
        trilhoProgressoBar.setProgress(QuestaoUtil.limitarPercentual(item.progressoPercentual()));
        trilhoProgressoValue.setText(String.format(
            java.util.Locale.ROOT,
            "Progresso %.0f%% | Rigor atual %.0f%% de %.0f%%",
            item.progressoPercentual(),
            item.rigorAtualPercentual(),
            item.rigorAlvoPercentual()
        ));
    }

    private void configurarTrilhoAdaptacaoBloqueado(String mensagem) {
        if (trilhoDisciplinaCombo != null) {
            trilhoDisciplinaCombo.getItems().clear();
            trilhoDisciplinaCombo.setDisable(true);
        }
        if (trilhoSubtopicoCombo != null) {
            trilhoSubtopicoCombo.getItems().clear();
            trilhoSubtopicoCombo.setDisable(true);
        }
        resetarTrilhoDetalhe("Trilho bloqueado", mensagem);
    }

    private void resetarTrilhoDetalhe(String status, String resumo) {
        if (trilhoStatusValue != null) {
            trilhoStatusValue.setText(status);
        }
        if (trilhoResumoValue != null) {
            trilhoResumoValue.setText(resumo);
        }
        if (trilhoLivroValue != null) {
            trilhoLivroValue.setText("Livro: -");
        }
        if (trilhoPaginasValue != null) {
            trilhoPaginasValue.setText("Paginas: -");
        }
        if (trilhoAvancosValue != null) {
            trilhoAvancosValue.setText("Avancos: 0");
        }
        if (trilhoQuedasValue != null) {
            trilhoQuedasValue.setText("Quedas: 0");
        }
        if (trilhoDificuldadeValue != null) {
            trilhoDificuldadeValue.setText("Dificuldade media: 0%");
        }
        if (trilhoProgressoBar != null) {
            trilhoProgressoBar.setProgress(0);
        }
        if (trilhoProgressoValue != null) {
            trilhoProgressoValue.setText("Progresso 0% | Rigor atual 0% de 0%");
        }
    }

    private void carregarBloqueioPrimeiroDiagnostico() {
        botoesDisciplinasBox.getChildren().clear();
        configurarTrilhoAdaptacaoBloqueado("Fica disponivel depois do primeiro diagnostico concluido.");

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
        Label titulo = new Label("Escolha uma disciplina para seguir o plano da semana e iniciar a proxima rodada.");
        titulo.getStyleClass().add("h3-thin");
        titulo.setWrapText(true);
        titulo.setMaxWidth(720);
        botoesDisciplinasBox.getChildren().add(Loading.load());
        CompletableFuture.supplyAsync(() -> service.carregarDisciplinasDisponiveis())
        .thenAcceptAsync(disciplinasDisponiveis -> {
            disciplinas=disciplinasDisponiveis;
            resumos=testeService.carregarResumoHistoricoDisciplinas(disciplinasDisponiveis, 4);
        })
        .whenComplete((t, u) -> {
            Platform.runLater(() -> {
                botoesDisciplinasBox.getChildren().clear();
                botoesDisciplinasBox.getChildren().add(titulo);
                for (String disciplina :  disciplinas ) {
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
            });
        });
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
        limparSessaoPausada();
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
        sessaoAtiva = true;
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
        configCandidato=getConfigCadidato();

        if (config == null || config.duracao() == null) {

            return Math.min(configCandidato.norm_test_q(), totalDisponivel);
        }

        return switch (TextoUtil.normalizarMinusculo(config.duracao())) {
            case "curto" -> configCandidato.curto_test_q();
            case "medio" -> configCandidato.norm_test_q();
            default -> Math.min(configCandidato.long_test_q(), totalDisponivel);
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
                sessaoAtiva = true;
                iniciarCronometro(0, 0);
                carregarQuestao(0);
            });
            fadeOut.play();
        });
        pause.play();
    }

    private void iniciarCronometro(int minutosIniciais, int segundosIniciais) {
        if (cronometro != null) {
            cronometro.stop();
        }

        segundos = Math.max(0, segundosIniciais);
        minutos = Math.max(0, minutosIniciais);
        tempo.setText(String.format("%02d:%02d", minutos, segundos));
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
        if (questionProgressBar != null) {
            questionProgressBar.setProgress(progresso);
        }

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

    private String resolverChaveSessaoPausa() {
        return Authentication.getCurrentUserId() == null
            ? null
            : SESSAO_PAUSA_CACHE_PREFIX + Authentication.getCurrentUserId();
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
            new SessaoTestePausada(
                disciplinaSelecionada,
                new ArrayList<>(topicosSelecionados),
                new ArrayList<>(subtopicosSelecionados),
                new ArrayList<>(questoes),
                new ArrayList<>(focoQuestoes),
                new ArrayList<>(respostasUsuario),
                new ArrayList<>(temposResposta),
                new ArrayList<>(reacao),
                questaoAtual,
                totalQuestoes,
                respostaSelecionada,
                nivelAtualAdaptativo,
                acertos,
                erros,
                sequenciaAcertos,
                sequenciaErros,
                tempoInicioQuestao,
                segundos,
                minutos,
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
        if (!(estadoBruto instanceof SessaoTestePausada estado)) {
            return false;
        }

        CacheService.remove(chave);
        if (estado.questoes() == null || estado.questoes().isEmpty()) {
            return false;
        }

        disciplinaSelecionada = estado.disciplinaSelecionada();

        topicosSelecionados.clear();
        topicosSelecionados.addAll(estado.topicosSelecionados());

        subtopicosSelecionados.clear();
        subtopicosSelecionados.addAll(estado.subtopicosSelecionados());

        questoes = new ArrayList<>(estado.questoes());
        focoQuestoes = new ArrayList<>(estado.focoQuestoes());
        respostasUsuario.clear();
        respostasUsuario.addAll(estado.respostasUsuario());
        temposResposta.clear();
        temposResposta.addAll(estado.temposResposta());
        reacao.clear();
        reacao.addAll(estado.reacao());

        questaoAtual = Math.max(0, Math.min(estado.questaoAtual(), Math.max(0, questoes.size() - 1)));
        totalQuestoes = Math.max(0, estado.totalQuestoes());
        respostaSelecionada = estado.respostaSelecionada();
        nivelAtualAdaptativo = estado.nivelAtualAdaptativo();
        acertos = Math.max(0, estado.acertos());
        erros = Math.max(0, estado.erros());
        sequenciaAcertos = Math.max(0, estado.sequenciaAcertos());
        sequenciaErros = Math.max(0, estado.sequenciaErros());

        long pausa = Math.max(0L, System.currentTimeMillis() - Math.max(0L, estado.pausadoEm()));
        tempoInicioQuestao = estado.tempoInicioQuestao() > 0
            ? estado.tempoInicioQuestao() + pausa
            : System.currentTimeMillis();
        minutos = Math.max(0, estado.minutos());
        segundos = Math.max(0, estado.segundos());

        start.setVisible(false);
        testeContainer.setVisible(true);
        loadingOverlay.setVisible(false);
        feedbackContainer.setVisible(false);
        tela.setVisible(true);
        nomeDisc.setText(formatarDisciplina(disciplinaSelecionada));
        atualizarIndicadoresNivel();

        if (questionProgressBar != null) {
            questionProgressBar.setProgress(totalQuestoes == 0 ? 0 : (double) (questaoAtual + 1) / totalQuestoes);
        }

        if (!questoes.isEmpty()) {
            atualizarConteudoQuestao(questaoAtual);
            restaurarRespostaQuestaoAtual(estado.respostaSelecionada());
        }

        sessaoAtiva = true;
        iniciarCronometro(minutos, segundos);
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

        respostaSelecionada = respostaRestaurada;

        if (questaoAtual < respostasUsuario.size()) {
            Questao q = questoes.get(questaoAtual);
            char alternativaCorreta = QuestaoUtil.resolverAlternativaCorreta(q);
            boolean acertou = QuestaoUtil.respostaEstaCorreta(q, respostaRestaurada);

            limparEstilosToggles();
            if (toggle != null) {
                toggle.setStyle(acertou
                    ? "-fx-background-color: #10b981; -fx-border-color: #10b981; -fx-text-fill: white;"
                    : "-fx-background-color: #ef4444; -fx-border-color: #ef4444; -fx-text-fill: white;");
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

    private void mostrarTelaInicialTeste() {
        feedbackContainer.setVisible(false);
        loadingOverlay.setVisible(false);
        testeContainer.setVisible(false);
        start.setVisible(true);
        tela.setVisible(true);
    }

    private record SessaoTestePausada(
        String disciplinaSelecionada,
        List<String> topicosSelecionados,
        List<String> subtopicosSelecionados,
        List<Questao> questoes,
        List<Questao> focoQuestoes,
        List<Character> respostasUsuario,
        List<Long> temposResposta,
        List<ReacaoTeste> reacao,
        int questaoAtual,
        int totalQuestoes,
        char respostaSelecionada,
        NivelDificuldadeAdaptativa nivelAtualAdaptativo,
        int acertos,
        int erros,
        int sequenciaAcertos,
        int sequenciaErros,
        long tempoInicioQuestao,
        int segundos,
        int minutos,
        long pausadoEm
    ) {
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
        char alternativaCorreta = QuestaoUtil.resolverAlternativaCorreta(q);
        boolean acertou = QuestaoUtil.respostaEstaCorreta(q, respostaSelecionada);
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
            mostrarFeedbackAdaptativo(true, tempoResposta, alternativaCorreta);
        } else {
            erros++;
            sequenciaErros++;
            sequenciaAcertos = 0;
            selected.setStyle("-fx-background-color: #ef4444; -fx-border-color: #ef4444; -fx-text-fill: white;");
            mostrarFeedbackAdaptativo(false, tempoResposta, alternativaCorreta);
            destacarRespostaCorreta(alternativaCorreta);
        }

        ajustarNivelAdaptativo(acertou, tempoResposta);
        atualizarIndicadoresNivel();

        btnProximo.setDisable(false);
        btnConfirmar.setDisable(true);
    }

    private void mostrarFeedbackAdaptativo(boolean acertou, long tempoResposta, char alternativaCorreta) {
        boolean foiRapido = tempoResposta <= resolverLimiteRapidoMs();
        boolean foiMuitoLento = tempoResposta >= resolverLimiteLentoMs();
        feedbackContainer.setVisible(true);
        feedbackContainer.setOpacity(1);

        if (acertou && foiRapido) {
            feedbackIcon.setText("OK");
            feedbackImg.setImage(new Image(App.class.getResourceAsStream("/com/imetro/assets/imgs/incredible.png")));
            feedbackMessage.setText("Excelente! Rapido e preciso. Vamos subir a exigencia.");
            feedbackContainer.setStyle("-fx-background-color: #ecfdf5; -fx-border-color: #10b981;");
        } else if (acertou) {
            feedbackIcon.setText("OK");
            feedbackImg.setImage(new Image(App.class.getResourceAsStream("/com/imetro/assets/imgs/corret.png")));
            feedbackMessage.setText("Correta! O foco continua nos mesmos subtopicos.");
            feedbackContainer.setStyle("-fx-background-color: #ecfdf5; -fx-border-color: #10b981;");
        } else if (foiMuitoLento) {
            feedbackIcon.setText("!");
            feedbackImg.setImage(new Image(App.class.getResourceAsStream("/com/imetro/assets/imgs/info.png")));
            feedbackMessage.setText("Demorou bastante. Vou manter o foco e reduzir a pressao.");
            feedbackContainer.setStyle("-fx-background-color: #fef2f2; -fx-border-color: #ef4444;");
        } else {
            feedbackIcon.setText("X");
            feedbackImg.setImage(new Image(App.class.getResourceAsStream("/com/imetro/assets/imgs/error.png")));
            feedbackMessage.setText("Errada. A resposta correta e " + alternativaCorreta + ".");
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
        AdaptacaoDto adaptacao = getAdaptacaoDto();
        boolean foiRapido = tempoResposta <= resolverLimiteRapidoMs();
        boolean foiMuitoLento = tempoResposta >= resolverLimiteLentoMs();

        if (acertou && foiRapido) {
            if (sequenciaAcertos >= adaptacao.acertosSubirRapido()) {
                nivelAtualAdaptativo = nivelAtualAdaptativo.subir();
            }
        } else if (acertou) {
            if (sequenciaAcertos >= adaptacao.acertosSubirLento()) {
                nivelAtualAdaptativo = nivelAtualAdaptativo.subir();
            }
        } else if (foiMuitoLento) {
            nivelAtualAdaptativo = nivelAtualAdaptativo.descer();
            sequenciaErros = 0;
        } else if (sequenciaErros >= adaptacao.errosDescer()) {
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
        sessaoAtiva = false;
        limparSessaoPausada();
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

        ResultadoPayload payload = new ResultadoPayload(
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
        );

        ResultadoCelebracaoSupport.CelebrationSummary celebrationSummary = ResultadoCelebracaoSupport.criarResumo(
            candidatoID,
            "Exame Adaptativo",
            formatarDisciplina(disciplinaSelecionada),
            acertos,
            totalQuestoes,
            porcentagemAcertos,
            tempo.getText(),
            mediaTempo / 1000d,
            false
        );

        abrirCelebracaoResultado(
            payload,
            celebrationSummary,
            () -> mostrarResultadoFallbackTeste(
                porcentagemAcertos,
                mediaTempo,
                perfil,
                recomendacao
            )
        );
    }

    private String determinarPerfil(double porcentagem, double tempoMedio) {
        AdaptacaoDto adaptacao = getAdaptacaoDto();
        double tempoMedioSegundos = tempoMedio / 1000d;
        if (porcentagem >= 80 && tempoMedioSegundos <= adaptacao.tempAdapt()) return "Estas mais rapido e preciso";
        if (porcentagem >= 80) return "Reduziste a tua velocidade normal porem ainda preciso mas lento";
        if (porcentagem >= 60 && tempoMedioSegundos <= adaptacao.tempAdapt() * 1.15d) return "Seguro e agil";
        if (porcentagem >= 60) return "Cauteloso como sempre";
        if (porcentagem >= 40) return "Intermediario se dedica mais";
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
        sessaoAtiva = false;
        if (cronometro != null) {
            cronometro.stop();
            cronometro = null;
        }
        if (loadingTimeline != null) {
            loadingTimeline.stop();
            loadingTimeline = null;
        }
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
        tempoInicioQuestao = 0L;
        tempo.setText("00:00");
        atualizarIndicadoresNivel();
        limparEstilosToggles();
        alternativas.selectToggle(null);
        btnConfirmar.setDisable(false);
        btnProximo.setDisable(true);
        if (questionProgressBar != null) {
            questionProgressBar.setProgress(0);
        }
    }

    private String formatarDisciplina(String disciplina) {
        return disciplina == null ? "-" : QuestaoUtil.formatarDisciplina(disciplina);
    }

    private float limitarUnitario(float valor) {
        return Math.max(0f, Math.min(1f, valor));
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return "";
    }

    private void abrirCelebracaoResultado(
        ResultadoPayload payload,
        ResultadoCelebracaoSupport.CelebrationSummary celebrationSummary,
        Runnable fallback
    ) {
        StackPane contentHost = testeField == null || testeField.getScene() == null
            ? null
            : (StackPane) testeField.getScene().lookup("#contentHost");

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
            Node modal = modFxml.load();
            modalPai.getChildren().add(modal);
            ResultadoCelebracaoModalController controller = modFxml.getController();
            controller.init();
        } catch (Exception ex) {
            ResultadoCelebracaoContext.limpar();
            onContinue.run();
        }
    }

    private void mostrarResultadoFallbackTeste(
        double porcentagemAcertos,
        double mediaTempo,
        String perfil,
        String recomendacao
    ) {
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
        if (sessaoAtiva) {
            salvarSessaoPausada();
        }
        if (cronometro != null) {
            cronometro.stop();
            cronometro = null;
        }
        if (loadingTimeline != null) {
            loadingTimeline.stop();
            loadingTimeline = null;
        }
    }

    private ConfiguracaoDto getConfigCadidato(){
        if (configCandidato==null) {
            this.configCandidato= configuracoesRepository.findByCandidato(Authentication.getCurrentUserId());
        }
        return configCandidato;
    }

    private AdaptacaoDto getAdaptacaoDto(){
        if (adaptacaoDto==null) {
            try {
                this.adaptacaoDto= adaptacaoRepository.findOrCreateByUserId(Authentication.getCurrentUserId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (adaptacaoDto == null) {
            adaptacaoDto = AdaptacaoDto.padrao(Authentication.getCurrentUserId());
        }
        return adaptacaoDto;
    }

    private long resolverLimiteRapidoMs() {
        return Math.max(1000L, Math.round(getAdaptacaoDto().tempAdapt() * 1000d));
    }

    private long resolverLimiteLentoMs() {
        AdaptacaoDto adaptacao = getAdaptacaoDto();
        return Math.max(
            resolverLimiteRapidoMs() + 1000L,
            Math.round(adaptacao.tempAdapt() * adaptacao.tempoLentoFator() * 1000d)
        );
    }

    @FXML
    void desisitir(ActionEvent event) {
        limparSessaoPausada();
        resetarMetricas();
        mostrarTelaInicialTeste();
    }

    @FXML
    void pausar(ActionEvent event) {
        if (!sessaoAtiva) {
            return;
        }

        salvarSessaoPausada();
        resetarMetricas();
        mostrarTelaInicialTeste();
    }
}
