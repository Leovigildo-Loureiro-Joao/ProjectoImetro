package com.imetro.ui.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.SplittableRandom;

import eu.hansolo.tilesfx.chart.ChartData;
import eu.hansolo.tilesfx.chart.RadarChart;
import eu.hansolo.tilesfx.chart.RadarChartMode;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class DashboardOrientadoController implements Initializable{

    @FXML
    private AreaChart<String,Integer> areaActivityChart;

    @FXML
    private Label descMelhoRia;

    private RadarChart radarPerformace;

    @FXML
    private StackPane radarHost;

    @FXML
    private VBox lastResult;

    @FXML
    private Label localDate;

    @FXML
    private Label next_level;

    @FXML
    private Label nivel_actual;

    @FXML
    private Label percentMelhoria;

    @FXML
    private ProgressBar progresso;

    @FXML
    private ListView<DisciplineStatus> status_disciplina;

    @FXML
    private Label progressText;

    @FXML
    private Label welcome;

    private XYChart.Series<String,Integer> dificuldadesSeries;
    private XYChart.Series<String,Integer> evolucoesSeries;

    private final SplittableRandom random = new SplittableRandom();
    private Timeline demoTimeline;
    private int demoTick = 0;

    private ChartData velocidade;
    private ChartData logica;
    private ChartData precisao;
    private ChartData resiliencia;
    private ChartData consistencia;

    @FXML
    public void StartDiagnostic(ActionEvent event) {

    }

    @FXML
    public void StartExam(ActionEvent event) {

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        setupDemo();
        setupLifecycle();
    }

    private void setupDemo() {
        updateHeader();
        setupAreaChart();
        setupRadar();
        setupStatusDisciplina();
        setupLastResults();
        startDemoTimeline();
    }
 
    private void updateHeader() {
        if (welcome != null) {
            welcome.setText("Bem-vindo novamente, Candidato");
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
        areaActivityChart.setCreateSymbols(false);

        dificuldadesSeries = new XYChart.Series<>();
        dificuldadesSeries.setName("Dificuldades");
        evolucoesSeries = new XYChart.Series<>();
        evolucoesSeries.setName("Evoluções");

        String[] monthLabels = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dec"};
        int startIndex = (LocalDate.now().getMonthValue() - 5 + 12) % 12;
        for (int i = 0; i < 5; i++) {
            String label = monthLabels[startIndex];
            dificuldadesSeries.getData().add(new XYChart.Data<>(label, 0));
            evolucoesSeries.getData().add(new XYChart.Data<>(label, 0));
            startIndex = (startIndex + 1) % 12;
        }

        areaActivityChart.getData().clear();
        areaActivityChart.getData().add(dificuldadesSeries);
        areaActivityChart.getData().add(evolucoesSeries);
        seedAreaValues();
    }

    private void seedAreaValues() {
        if (dificuldadesSeries == null || evolucoesSeries == null) {
            return;
        }
        for (int i = 0; i < 5; i++) {
            dificuldadesSeries.getData().get(i).setYValue(random.nextInt(15, 90));
            evolucoesSeries.getData().get(i).setYValue(random.nextInt(10, 100));
        }
    }

    private void setupRadar() {
        
      Platform.runLater(()->{
          if (radarHost == null) {
            return;
        }

        velocidade = new ChartData("Velocidade", 40d,Color.GREEN);
        logica = new ChartData("Lógica", 55d,Color.BLUE);
        precisao = new ChartData("Precisão", 68d,Color.RED);
        resiliencia = new ChartData("Resiliência", 85d,Color.VIOLET);
        consistencia = new ChartData("Consistência", 22d,Color.ORANGE);
        
        radarPerformace = new RadarChart(List.of(
                velocidade, logica, precisao, resiliencia, consistencia
        ));
        radarPerformace.getStyleClass().add("radar-demo");
      
        radarPerformace.setMinSize(270, 270);
        radarPerformace.setPrefSize(270, 270);
        radarPerformace.setMaxSize(270, 270);
       

        // Make labels + legend readable on a light card
        Color text = Color.web("#0f172a");
        radarPerformace.setChartForegroundColor(text);
        radarPerformace.setChartTextColor(text);
        radarPerformace.setGridColor(Color.web("#e2e8f0"));
        radarPerformace.setChartBackgroundColor(Color.TRANSPARENT);
        radarPerformace.setChartFill(Color.TRANSPARENT);

        velocidade.setTextColor(text);
        logica.setTextColor(text);
        precisao.setTextColor(text);
        resiliencia.setTextColor(text);
        consistencia.setTextColor(text);

        radarPerformace.setMode(RadarChartMode.SECTOR);
        radarPerformace.setLegendVisible(false);
        radarHost.getChildren().setAll(radarPerformace);
      });
    }

    private void setupStatusDisciplina() {
        if (status_disciplina == null) {
            return;
        }

        status_disciplina.getItems().setAll(
                new DisciplineStatus("Matemática", 0.72),
                new DisciplineStatus("Programação", 0.61),
                new DisciplineStatus("Lógica", 0.68),
                new DisciplineStatus("Inglês", 0.54),
                new DisciplineStatus("Ciências", 0.45)
        );

        status_disciplina.setCellFactory(list -> new DisciplineStatusCell());
    }

    private void setupLastResults() {
        if (lastResult == null) {
            return;
        }
        lastResult.getChildren().clear();

        lastResult.getChildren().addAll(
                buildResultRow("Exame adaptativo", "82%", true),
                buildResultRow("Diagnóstico", "76%", true),
                buildResultRow("Treino rápido", "58%", false)
        );
    }

    private HBox buildResultRow(String title, String score, boolean good) {
        Label label = new Label(title);
        label.getStyleClass().add("result-title");

        Label value = new Label(score);
        value.getStyleClass().addAll("pill", good ? "pill-good" : "pill-warn");

        HBox row = new HBox(10, label, value);
        row.getStyleClass().add("result-row");
        return row;
    }

    private void startDemoTimeline() {
        stopDemoTimeline();

        demoTick = 0;
        demoTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> onDemoTick()),
                new KeyFrame(Duration.seconds(1))
        );
        demoTimeline.setCycleCount(Timeline.INDEFINITE);
        demoTimeline.play();
    }

    private void stopDemoTimeline() {
        if (demoTimeline != null) {
            demoTimeline.stop();
            demoTimeline = null;
        }
    }

    private void onDemoTick() {
        demoTick++;
        updateAreaValues();
        updateRadarValues();
        updateProgress();
    }

    private void updateAreaValues() {
        if (dificuldadesSeries == null || evolucoesSeries == null) {
            return;
        }

        for (int i = 0; i < 5; i++) {
            int v1 = curve(55, 32, demoTick + i, 0.55);
            int v2 = curve(45, 40, demoTick + i + 3, 0.45);
            dificuldadesSeries.getData().get(i).setYValue(v1);
            evolucoesSeries.getData().get(i).setYValue(v2);
        }
    }

    private void updateRadarValues() {
        if (velocidade == null) {
            return;
        }

        setChartValue(velocidade, curve(60, 25, demoTick, 0.65));
        setChartValue(logica, curve(55, 22, demoTick + 2, 0.55));
        setChartValue(precisao, curve(50, 18, demoTick + 4, 0.50));
        setChartValue(resiliencia, curve(45, 28, demoTick + 6, 0.45));
        setChartValue(consistencia, curve(58, 16, demoTick + 8, 0.52));
    }

    private void updateProgress() {
        if (progresso == null) {
            return;
        }

        double progress = (Math.sin(demoTick * 0.35) + 1.0) / 2.0; // 0..1
        progresso.setProgress(progress);

        int percent = (int) Math.round(progress * 100);
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
    }

    private int curve(int base, int amplitude, int tick, double speed) {
        double value = base + amplitude * Math.sin(tick * speed);
        int jitter = random.nextInt(-4, 5);
        int result = (int) Math.round(value + jitter);
        return Math.max(0, Math.min(100, result));
    }

    private void setChartValue(ChartData data, double value) {
        try {
            data.setValue(value);
        } catch (Exception ignored) {
            // TilesFX versions vary; fail gracefully without crashing the UI.
        }
    }

    private void setupLifecycle() {
        if (areaActivityChart == null) {
            return;
        }
        areaActivityChart.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene == null) {
                stopDemoTimeline();
            } else if (demoTimeline == null) {
                startDemoTimeline();
            }
        });
    }

    private record DisciplineStatus(String name, double progress) {}

    private static final class DisciplineStatusCell extends ListCell<DisciplineStatus> {
        private final Label name = new Label();
        private final ProgressBar progress = new ProgressBar();
        private final Label percent = new Label();
        private final HBox root = new HBox(12, name, progress, percent);

        private DisciplineStatusCell() {
            root.getStyleClass().add("status-row");
            name.getStyleClass().add("status-name");
            progress.getStyleClass().add("status-progress");
            progress.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(progress, javafx.scene.layout.Priority.ALWAYS);
            percent.getStyleClass().add("status-percent");
        }

        @Override
        public void updateItem(DisciplineStatus item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
                setText(null);
                return;
            }

            name.setText(item.name());
            progress.setProgress(item.progress());
            percent.setText((int) Math.round(item.progress() * 100) + "%");
            setText(null);
            setGraphic(root);
        }
    }

}
