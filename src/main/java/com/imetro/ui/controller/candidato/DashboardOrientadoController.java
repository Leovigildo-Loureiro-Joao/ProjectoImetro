package com.imetro.ui.controller.candidato;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.imetro.App;
import com.imetro.domain.CacheService;
import com.imetro.domain.dto.candidato.DashboardDificuldadeDia;
import com.imetro.domain.dto.candidato.DashboardDificuldadeResumo;
import com.imetro.domain.dto.candidato.DashboardMelhoriaDia;
import com.imetro.domain.dto.candidato.DashboardMelhoriaResumo;
import com.imetro.domain.dto.progresso.ProgressoDisciplinaTeste;
import com.imetro.domain.enums.NivelDisciplina;
import com.imetro.domain.model.Candidato;
import com.imetro.services.DiagnosticoService;
import com.imetro.services.CandidatoService;
import com.imetro.services.DisciplinaService;
import com.imetro.ui.components.CircleProgress;
import com.imetro.ui.components.ResultData;
import com.imetro.ui.model.Questao;
import com.imetro.util.Authentication;
import com.imetro.util.QuestaoUtil;
import com.imetro.domain.dto.stats.Stats;



import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class DashboardOrientadoController implements Initializable {

    @FXML
    private AreaChart<String, Number> areaActivityChart;

    @FXML
    private ProgressBar consistencia;

    @FXML
    private Label descMelhoRia;

    @FXML
    private VBox lastResult;

    @FXML
    private Label localDate;

    @FXML
    private ProgressBar logica;

    @FXML
    private StackPane melhoria;

    @FXML
    private Label next_level;

    @FXML
    private Label nivel_actual;

    @FXML
    private Label percentMelhoria;

    @FXML
    private ProgressBar precisao;

    @FXML
    private Label progressText;

    @FXML
    private ProgressBar progresso;

    @FXML
    private ProgressBar resiliencia;

    @FXML
    private ListView<ProgressoDisciplinaTeste> status_disciplina;

    @FXML
    private ProgressBar velocidade;

    @FXML
    private Label welcome;


    @FXML
    private Label descSucesso;

    @FXML
    private Label percentSucesso;

    @FXML
    private StackPane sucesso;
    private Candidato candidato;
    @FXML
    private VBox tela;
// Supondo que exista um serviço para obter dados reais
    private XYChart.Series<String, Number> dificuldadesSeries;
    private XYChart.Series<String, Number> evolucoesSeries;

    private Timeline startupTimeline;
    private DiagnosticoService diagnosticoService;
    private CandidatoService candidatoService = new CandidatoService();
    private DashboardMelhoriaResumo dashboardMelhoriaResumo = DashboardMelhoriaResumo.empty();
    private DashboardDificuldadeResumo dashboardDificuldadeResumo = DashboardDificuldadeResumo.empty();

    private  double VELOCIDADE_TARGET = 0;
    private  double LOGICA_TARGET = 0;
    private  double PRECISAO_TARGET = 0;
    private  double RESILIENCIA_TARGET = 0;
    private  double CONSISTENCIA_TARGET = 0;
    private  double PROGRESSO_TARGET = 0;

    @FXML
    public void StartDiagnostic(javafx.event.ActionEvent event) {
         StackPane contentHost=(StackPane)tela.getParent();
        App.swapContent(contentHost, "views/pages/candidato/diagnostico");
    }

    @FXML
    public void StartExam(javafx.event.ActionEvent event) {
        StackPane contentHost=(StackPane)tela.getParent();
        App.swapContent(contentHost, "views/pages/candidato/testes");
    }


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        Object currentUser = CacheService.get("currentUser");
        if (currentUser instanceof Candidato cachedCandidato) {
            candidato = cachedCandidato;
        } else {
            candidato = new Candidato();
            String email = Authentication.getCurrentUserEmail();
            candidato.setNome("Candidato");
            candidato.setEmail(email == null ? "" : email);
        }
        setup();
    }

    private void setup() {
        updateHeader();
        dashboardMelhoriaResumo = candidatoService.calcularResumoMelhorias(candidato.getIdCandidato());
        dashboardDificuldadeResumo = candidatoService.calcularResumoDificuldades(candidato.getIdCandidato());
        setupAreaChart();
        setupRadar();
        setupDisciplineStatus();
        setupImprovementData();
        setupTargetData();
        setupLastResults();
        animateStartup();
    }

    private void setupDisciplineStatus() {
        if (status_disciplina == null) {
            return;
        }

        List<ProgressoDisciplinaTeste> demoStatuses;
        try {
            demoStatuses = DisciplinaService.getDisciplinaTestes().stream().map(d -> new ProgressoDisciplinaTeste(
                d.disciplina(),
                d.progresso(), // progresso aleatório entre 10% e 90%
                d.pesoAtual(),
                d.nivel(),
                d.velocudade(), // velocidade
                d.consistencia(), // consistencia
                d.precisao()  // precisão
            )).toList();
        } catch (SQLException e) {
            demoStatuses=List.of();
            e.printStackTrace();
        }

        status_disciplina.setCellFactory(list -> new DisciplineStatusCell());
        status_disciplina.setItems(FXCollections.observableArrayList(demoStatuses));
    }

    private void setupTargetData() {

    }

    private void setupImprovementData() {
        double mediaMelhoria = dashboardMelhoriaResumo.mediaMelhoriaPercentual();
        double taxaSucesso = dashboardMelhoriaResumo.taxaSucessoPercentual();
    // Array de dados fictícios que mudam com o tempo
    String[] melhorias = {
        "85%", "Excelente! Você está acima da média",
        "72%", "Bom progresso, continue assim!",
        "91%", "Incrível! Melhor performance do mês",
        "64%", "Foco! Você pode melhorar ainda mais",
        "78%", "Consistente! Mantenha o ritmo"
    };

    // Escolhe um aleatório


    percentMelhoria.setText(QuestaoUtil.formatarPercentual(mediaMelhoria));
    descMelhoRia.setText(descreverMelhoria(mediaMelhoria));

    // Cor dinâmica baseada na porcentagem
    String percentText = Long.toString(Math.round(Math.max(0.0, mediaMelhoria)));
    int percentValue = Integer.parseInt(percentText);

    if (mediaMelhoria >= 10.0) {
        percentMelhoria.setStyle("-fx-text-fill: #059669;");
        descMelhoRia.setStyle("-fx-background-color: #d1fae5; -fx-text-fill: #059669;");
    } else if (mediaMelhoria >= 0.0) {
        percentMelhoria.setStyle("-fx-text-fill: #d97706; ");
        descMelhoRia.setStyle("-fx-background-color: #fed7aa; -fx-text-fill: #c2410c;");
    } else {
        percentMelhoria.setStyle("-fx-text-fill: #dc2626; ");
        descMelhoRia.setStyle("-fx-background-color: #fee2e2; -fx-text-fill: #dc2626;");
    }
    CircleProgress circleProgress = new CircleProgress(30, 30);
    melhoria.getChildren().clear();
    melhoria.getChildren().add(circleProgress);
    circleProgress.setValue(percentValue / 100.0);

    // Mesmo para sucesso
    percentSucesso.setText(QuestaoUtil.formatarPercentual(taxaSucesso));
    descSucesso.setText(descreverSucesso(taxaSucesso));

    String percentTextSucesso = Long.toString(Math.round(Math.max(0.0, taxaSucesso)));
    int percentValueSucesso = Integer.parseInt(percentTextSucesso);

    if (taxaSucesso >= 80.0) {
        percentSucesso.setStyle("-fx-text-fill: #059669;");
        descSucesso.setStyle("-fx-background-color: #d1fae5; -fx-text-fill: #059669;");
    } else if (taxaSucesso >= 60.0) {
        percentSucesso.setStyle("-fx-text-fill: #d97706; ");
        descSucesso.setStyle("-fx-background-color: #fed7aa; -fx-text-fill: #c2410c;");
    } else {
        percentSucesso.setStyle("-fx-text-fill: #dc2626; ");
        descSucesso.setStyle("-fx-background-color: #fee2e2; -fx-text-fill: #dc2626;");
    }
    CircleProgress circleProgressSucesso = new CircleProgress(30, 30);
    sucesso.getChildren().clear();
    sucesso.getChildren().add(circleProgressSucesso);
    circleProgressSucesso.setValue(percentValueSucesso / 100.0);
}

    private void updateHeader() {
        if (welcome != null) {
            welcome.setText("Bem-vindo novamente, "+candidato.getNome() + "!");
        }
        if (localDate != null) {
            Locale locale = Locale.forLanguageTag("pt-PT");
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm EEEE dd MMM yyyy", locale);
            localDate.setText(LocalDateTime.now().format(fmt));
        }
    }

    private void setupAreaChart() {
        if (areaActivityChart == null) {
            return;
        }

        areaActivityChart.setLegendVisible(true);
        areaActivityChart.setAnimated(false);
        areaActivityChart.setCreateSymbols(true);
        areaActivityChart.setOpacity(0);
        if (areaActivityChart.getYAxis() instanceof javafx.scene.chart.NumberAxis eixoY) {
            eixoY.setAutoRanging(false);
            eixoY.setLowerBound(0);
            eixoY.setUpperBound(100);
            eixoY.setTickUnit(20);
        }

        dificuldadesSeries = new XYChart.Series<>();
        dificuldadesSeries.setName("Dificuldades");
        evolucoesSeries = new XYChart.Series<>();
        evolucoesSeries.setName("Evoluções");

        LocalDate inicio = LocalDate.now().minusDays(6);
        DateTimeFormatter chartFormatter = DateTimeFormatter.ofPattern("dd/MM");
        for (int i = 0; i < 7; i++) {
            LocalDate data = inicio.plusDays(i);
            String label = data.format(chartFormatter);
            dificuldadesSeries.getData().add(new XYChart.Data<>(label, resolverDificuldadeDia(data)));
            evolucoesSeries.getData().add(new XYChart.Data<>(label, resolverEvolucaoDia(data)));
        }

        areaActivityChart.getData().clear();
        areaActivityChart.getData().add(dificuldadesSeries);
        areaActivityChart.getData().add(evolucoesSeries);
    }

    private double resolverDificuldadeDia(LocalDate data) {
        for (DashboardDificuldadeDia dia : dashboardDificuldadeResumo.semana()) {
            if (data.equals(dia.data())) {
                return limitarPercentualChart(dia.mediaDificuldadePercentual());
            }
        }
        return 0d;
    }

    private double resolverEvolucaoDia(LocalDate data) {
        for (DashboardMelhoriaDia dia : dashboardMelhoriaResumo.semana()) {
            if (!data.equals(dia.data())) {
                continue;
            }
            if (dia.melhorias() <= 0) {
                return 0d;
            }
            double taxaSucesso = (dia.sucessos() * 100.0) / dia.melhorias();
            return limitarPercentualChart(taxaSucesso);
        }
        return 0d;
    }

    private double limitarPercentualChart(double valor) {
        return Math.max(0d, Math.min(100d, Math.round(valor)));
    }

    private void setupRadar() {

        if (velocidade == null || logica == null || precisao == null || resiliencia == null || consistencia == null) {
            return;
        }

        velocidade.setProgress(0);
        logica.setProgress(0);
        precisao.setProgress(0);
        resiliencia.setProgress(0);
        consistencia.setProgress(0);
    }

    private void setupLastResults() {
        if (lastResult == null) return;
        lastResult.getChildren().clear();

        // Dados fictícios mais detalhados e variados
        List<ResultData> resultados = candidatoService.ListarResultados();
        if (resultados.isEmpty()) {
            Label emptyState = new Label("Ainda nao ha resultados registados.");
            emptyState.getStyleClass().add("muted");
            emptyState.setWrapText(true);
            lastResult.getChildren().add(emptyState);
            return;
        }

        for (ResultData data : resultados) {
            lastResult.getChildren().add(createResultCard(data));
        }
    }

    private VBox createResultCard(ResultData data) {
        VBox card = new VBox(6);
        card.getStyleClass().add("result-item");

        // Linha superior: título e score
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label(data.title);
        titleLabel.getStyleClass().add("result-title");

        Label scoreLabel = new Label(data.score);
        scoreLabel.getStyleClass().addAll("pill", getPillStyle(data.type));

        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        header.getChildren().addAll(titleLabel, scoreLabel);

        // Linha inferior: data e variação
        HBox footer = new HBox(15);
        footer.setAlignment(Pos.CENTER_LEFT);

        Label dateLabel = new Label(data.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dateLabel.getStyleClass().add("result-date");

        Label variationLabel = new Label(data.variation);
        variationLabel.getStyleClass().add("result-date");
        if (data.variation.contains("+")) {
            variationLabel.setTextFill(Color.web("#059669"));
            variationLabel.setGraphic(createIcon("▲"));
        } else if (data.variation.contains("-")) {
            variationLabel.setTextFill(Color.web("#dc2626"));
            variationLabel.setGraphic(createIcon("▼"));
        }

        footer.getChildren().addAll(dateLabel, variationLabel);

        card.getChildren().addAll(header, footer);
        return card;
    }

    public String descreverMelhoria(double taxa){
        if (taxa >= 20.0) {
            return "Excelente ritmo de melhoria.";
        }
        if (taxa >= 10.0) {
            return "Bom progresso nas ultimas tentativas.";
        }
        if (taxa >= 0.0) {
            return "Evolução estavel, com margem para crescer.";
        }
        if (taxa >= -10.0) {
            return "Houve oscilação, vale rever os topicos.";
        }
        return "Queda recente, precisa reforco dirigido.";
    }

    public String descreverSucesso(double taxa){
        if (taxa >= 85.0) {
            return "Taxa de sucesso muito forte.";
        }
        if (taxa >= 70.0) {
            return "Bom nivel de acerto e consistencia.";
        }
        if (taxa >= 50.0) {
            return "Resultado razoavel, mas ainda instavel.";
        }
        if (taxa >= 30.0) {
            return "Sucesso abaixo do esperado.";
        }
        return "Muitos erros recentes, retoma a base.";
    }

    private String getPillStyle(String type) {
        switch (type) {
            case "excelente": return "pill-good";
            case "bom": return "pill-neutral";
            case "regular": return "pill-warn";
            default: return "pill-neutral";
        }
    }

    private Label createIcon(String symbol) {
        Label icon = new Label(symbol);
        icon.setStyle("-fx-font-size: 8px; -fx-font-weight: bold;");
        return icon;
    }

    private void animateStartup() {
        if (startupTimeline != null) {
            startupTimeline.stop();
        }

        if (progresso != null) {
            progresso.setProgress(0);
        }
        if (progressText != null) {
            progressText.setText("0% progresso");
        }
        if (percentMelhoria != null) {
            percentMelhoria.setText("0% de melhoria");
        }
        if (nivel_actual != null) {
            nivel_actual.setText("INICIANTE");
        }
        if (next_level != null) {
            next_level.setText("INTERMEDIÁRIO");
        }
        if (descMelhoRia != null) {
            descMelhoRia.setText("(A ajustar)");
        }
        CalcularStats();

        startupTimeline = new Timeline();
        startupTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1.2),
                new KeyValue(velocidade.progressProperty(), VELOCIDADE_TARGET, Interpolator.EASE_BOTH),
                new KeyValue(logica.progressProperty(), LOGICA_TARGET, Interpolator.EASE_BOTH),
                new KeyValue(precisao.progressProperty(), PRECISAO_TARGET, Interpolator.EASE_BOTH),
                new KeyValue(resiliencia.progressProperty(), RESILIENCIA_TARGET, Interpolator.EASE_BOTH),
                new KeyValue(consistencia.progressProperty(), CONSISTENCIA_TARGET, Interpolator.EASE_BOTH),
                new KeyValue(progresso.progressProperty(), PROGRESSO_TARGET, Interpolator.EASE_BOTH)
                /*new KeyValue(dificuldadesSeries.getData().get(0).YValueProperty(), DIFICULDADES_TARGET[0], Interpolator.EASE_BOTH),
                new KeyValue(dificuldadesSeries.getData().get(1).YValueProperty(), DIFICULDADES_TARGET[1], Interpolator.EASE_BOTH),
                new KeyValue(dificuldadesSeries.getData().get(2).YValueProperty(), DIFICULDADES_TARGET[2], Interpolator.EASE_BOTH),
                new KeyValue(dificuldadesSeries.getData().get(3).YValueProperty(), DIFICULDADES_TARGET[3], Interpolator.EASE_BOTH),
                new KeyValue(dificuldadesSeries.getData().get(4).YValueProperty(), DIFICULDADES_TARGET[4], Interpolator.EASE_BOTH),
                new KeyValue(evolucoesSeries.getData().get(0).YValueProperty(), EVOLUCOES_TARGET[0], Interpolator.EASE_BOTH),
                new KeyValue(evolucoesSeries.getData().get(1).YValueProperty(), EVOLUCOES_TARGET[1], Interpolator.EASE_BOTH),
                new KeyValue(evolucoesSeries.getData().get(2).YValueProperty(), EVOLUCOES_TARGET[2], Interpolator.EASE_BOTH),
                new KeyValue(evolucoesSeries.getData().get(3).YValueProperty(), EVOLUCOES_TARGET[3], Interpolator.EASE_BOTH),
                new KeyValue(evolucoesSeries.getData().get(4).YValueProperty(), EVOLUCOES_TARGET[4], Interpolator.EASE_BOTH)*/
        ));

        startupTimeline.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (progresso == null) {
                return;
            }
            int percent = (int) Math.round(progresso.getProgress() * 100.0);
            if (progressText != null) {
                progressText.setText(percent + "% progresso");
            }
            if (percentMelhoria != null) {
                percentMelhoria.setText(percent + "% de melhoria");
            }
            if (nivel_actual != null && next_level != null) {
                if (percent < 35) {
                    nivel_actual.setText("INICIANTE");
                    next_level.setText("INTERMEDIÁRIO");
                } else if (percent < 70) {
                    nivel_actual.setText("INTERMEDIÁRIO");
                    next_level.setText("AVANÇADO");
                } else {
                    nivel_actual.setText("AVANÇADO");
                    next_level.setText("EXCELENTE");
                }
            }
            if (descMelhoRia != null) {
                descMelhoRia.setText(percent >= 50 ? "(Melhoria)" : "(A ajustar)");
            }
        });

        startupTimeline.play();

        if (areaActivityChart != null) {
            FadeTransition fadeChart = new FadeTransition(Duration.seconds(0.8), areaActivityChart);
            fadeChart.setFromValue(0);
            fadeChart.setToValue(1);
            fadeChart.play();
        }
        if (lastResult != null) {
            FadeTransition fadeResults = new FadeTransition(Duration.seconds(0.8), lastResult);
            fadeResults.setFromValue(0);
            fadeResults.setToValue(1);
            fadeResults.play();
        }
        if (status_disciplina != null) {
            FadeTransition fadeStatus = new FadeTransition(Duration.seconds(0.8), status_disciplina);
            fadeStatus.setFromValue(0);
            fadeStatus.setToValue(1);
            fadeStatus.play();
        }
    }

    private void CalcularStats(){
        Stats stats;
        stats = candidatoService.CalcularStats();
        VELOCIDADE_TARGET = stats.velocidade();
        LOGICA_TARGET = stats.logica();
        PRECISAO_TARGET = stats.precisao();
        RESILIENCIA_TARGET = stats.resiliencia();
        CONSISTENCIA_TARGET = stats.consistencia();
        PROGRESSO_TARGET = (VELOCIDADE_TARGET + LOGICA_TARGET + PRECISAO_TARGET + RESILIENCIA_TARGET + CONSISTENCIA_TARGET) / 5.0;
    }

  

    private static final class DisciplineStatusCell extends ListCell<ProgressoDisciplinaTeste> {
        private final Label name = new Label();
        private final CircleProgress progress = new CircleProgress(30,30);
        private final ProgressBar velocidade = new ProgressBar(0);
        private final ProgressBar consistencia = new ProgressBar(0);
        private final ProgressBar precisao = new ProgressBar(0);
        private final Label percent = new Label();
        private final Label peso = new Label();
        private final Label nivel = new Label();
        private final VBox progressos= new VBox(3, new Label("Velocidade"),velocidade,new Label("Consistência"), consistencia, new Label("Precisão"), precisao);
        private final HBox root = new HBox(10, progress, new VBox(5, name, percent, peso, nivel),progressos);


        private DisciplineStatusCell() {
            root.getStyleClass().add("status-row");
            name.getStyleClass().add("status-name");
            peso.getStyleClass().add("status-detail");
            nivel.getStyleClass().add("status-detail");
            velocidade.getStyleClass().add("status-progress-bar");
            consistencia.getStyleClass().add("status-progress-bar");
            precisao.getStyleClass().add("status-progress-bar");
            velocidade.setPrefWidth(150);
            consistencia.setPrefWidth(150);
            precisao.setPrefWidth(150);
            progressos.setStyle("-fx-font-size:10px;-fx-text-fill:-color-muted ;-fx-translate-x:20px");
            HBox.setHgrow(progress, javafx.scene.layout.Priority.ALWAYS);
        }

        @Override
        public void updateItem(ProgressoDisciplinaTeste item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
                setText(null);
                return;
            }

            name.setText(item.disciplina());
            progress.setValue(item.progresso());
            percent.setText((int) Math.round(item.progresso() * 100) + "%");
            peso.setText("Peso: " + item.pesoAtual());
            nivel.setText("Nível: " + item.nivel().getDescricao());
            velocidade.setProgress(item.velocudade());
            consistencia.setProgress(item.consistencia());
            precisao.setProgress(item.precisao());
            setText(null);
            setGraphic(root);
        }
    }

}
