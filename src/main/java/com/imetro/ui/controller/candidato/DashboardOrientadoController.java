package com.imetro.ui.controller.candidato;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

import com.imetro.App;
import com.imetro.domain.enums.NivelDisciplina;
import com.imetro.ui.components.CircleProgress;
import com.imetro.ui.components.ResultData;


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
    private AreaChart<String,Integer> areaActivityChart;

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
    private ListView<DisciplineStatus> status_disciplina;

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
    @FXML
    private VBox tela;


    private XYChart.Series<String,Integer> dificuldadesSeries;
    private XYChart.Series<String,Integer> evolucoesSeries;

    private Timeline startupTimeline;

    private static final int[] DIFICULDADES_TARGET = {32, 40, 70, 55, 60};
    private static final int[] EVOLUCOES_TARGET = {25, 45, 80, 60, 75};
    private static final double VELOCIDADE_TARGET = 0.75;
    private static final double LOGICA_TARGET = 0.68;
    private static final double PRECISAO_TARGET = 0.55;
    private static final double RESILIENCIA_TARGET = 0.82;
    private static final double CONSISTENCIA_TARGET = 0.78;
    private static final double PROGRESSO_TARGET = 0.68;

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
        setupDemo();
    }

    private void setupDemo() {
        updateHeader();
        setupAreaChart();
        setupRadar();
        setupDisciplineStatus();
        setupImprovementData();
        setupLastResults();
        animateStartup();
    }

    private void setupDisciplineStatus() {
        if (status_disciplina == null) {
            return;
        }

        List<DisciplineStatus> demoStatuses = List.of(
            new DisciplineStatus("Matemática", 0.85, 4.5f, NivelDisciplina.AVANCADO, 0.8, 0.75, 0.9),
            new DisciplineStatus("Português", 0.72, 3.0f, NivelDisciplina.INTERMEDIARIO, 0.7, 0.8, 0.85),
            new DisciplineStatus("Física", 0.68, 4.0f, NivelDisciplina.AVANCADO, 0.6, 0.7, 0.75),
            new DisciplineStatus("Química", 0.54, 3.5f, NivelDisciplina.INTERMEDIARIO, 0.5, 0.65, 0.7),
            new DisciplineStatus("Biologia", 0.91, 2.5f, NivelDisciplina.INICIANTE, 0.9, 0.85, 0.95),
            new DisciplineStatus("História", 0.76, 2.0f, NivelDisciplina.INICIANTE, 0.75, 0.8, 0.8),
            new DisciplineStatus("Geografia", 0.63, 2.5f, NivelDisciplina.INICIANTE, 0.65, 0.7, 0.75),
            new DisciplineStatus("Inglês", 0.79, 3.0f, NivelDisciplina.INTERMEDIARIO, 0.8, 0.75, 0.85)
        );

        status_disciplina.setCellFactory(list -> new DisciplineStatusCell());
        status_disciplina.setItems(FXCollections.observableArrayList(demoStatuses));
    }

    private void setupImprovementData() {
    // Array de dados fictícios que mudam com o tempo
    String[] melhorias = {
        "85%", "Excelente! Você está acima da média",
        "72%", "Bom progresso, continue assim!",
        "91%", "Incrível! Melhor performance do mês",
        "64%", "Foco! Você pode melhorar ainda mais",
        "78%", "Consistente! Mantenha o ritmo"
    };
    
    // Escolhe um aleatório
    Random rand = new Random();
    int idx = rand.nextInt(5) * 2;
    
    percentMelhoria.setText(melhorias[idx]);
    descMelhoRia.setText(melhorias[idx + 1]);
    
    // Cor dinâmica baseada na porcentagem
    String percentText = melhorias[idx].replace("%", "");
    int percentValue = Integer.parseInt(percentText);
    
    if (percentValue >= 80) {
        percentMelhoria.setStyle("-fx-text-fill: #059669;");
        descMelhoRia.setStyle("-fx-background-color: #d1fae5; -fx-text-fill: #059669;");
    } else if (percentValue >= 70) {
        percentMelhoria.setStyle("-fx-text-fill: #d97706; ");
        descMelhoRia.setStyle("-fx-background-color: #fed7aa; -fx-text-fill: #c2410c;");
    } else {
        percentMelhoria.setStyle("-fx-text-fill: #dc2626; ");
        descMelhoRia.setStyle("-fx-background-color: #fee2e2; -fx-text-fill: #dc2626;");
    }
    CircleProgress circleProgress = new CircleProgress(30, 30);
    melhoria.getChildren().add(circleProgress);
    circleProgress.setValue(percentValue / 100.0);

    // Mesmo para sucesso
    int idxSucesso = rand.nextInt(5) * 2;
    percentSucesso.setText(melhorias[idxSucesso]);
    descSucesso.setText(melhorias[idxSucesso + 1]);
    
    String percentTextSucesso = melhorias[idxSucesso].replace("%", "");
    int percentValueSucesso = Integer.parseInt(percentTextSucesso);
    
    if (percentValueSucesso >= 80) {
        percentSucesso.setStyle("-fx-text-fill: #059669;");
        descSucesso.setStyle("-fx-background-color: #d1fae5; -fx-text-fill: #059669;");
    } else if (percentValueSucesso >= 70) {
        percentSucesso.setStyle("-fx-text-fill: #d97706; ");
        descSucesso.setStyle("-fx-background-color: #fed7aa; -fx-text-fill: #c2410c;");
    } else {
        percentSucesso.setStyle("-fx-text-fill: #dc2626; ");
        descSucesso.setStyle("-fx-background-color: #fee2e2; -fx-text-fill: #dc2626;");
    }
    CircleProgress circleProgressSucesso = new CircleProgress(30, 30);
    sucesso.getChildren().add(circleProgressSucesso);
    circleProgressSucesso.setValue(percentValueSucesso / 100.0);
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
        areaActivityChart.setOpacity(0);

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
        List<ResultData> resultados = Arrays.asList(
            new ResultData("Exame Adaptativo", "89%", LocalDate.now().minusDays(1), "excelente", "+12%"),
            new ResultData("Diagnóstico Rápido", "76%", LocalDate.now().minusDays(3), "bom", "+5%"),
            new ResultData("Simulado Final", "94%", LocalDate.now().minusDays(5), "excelente", "+18%"),
            new ResultData("Quiz Semanal", "68%", LocalDate.now().minusDays(7), "regular", "-3%")
        );
        
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

        startupTimeline = new Timeline();
        startupTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1.2),
                new KeyValue(velocidade.progressProperty(), VELOCIDADE_TARGET, Interpolator.EASE_BOTH),
                new KeyValue(logica.progressProperty(), LOGICA_TARGET, Interpolator.EASE_BOTH),
                new KeyValue(precisao.progressProperty(), PRECISAO_TARGET, Interpolator.EASE_BOTH),
                new KeyValue(resiliencia.progressProperty(), RESILIENCIA_TARGET, Interpolator.EASE_BOTH),
                new KeyValue(consistencia.progressProperty(), CONSISTENCIA_TARGET, Interpolator.EASE_BOTH),
                new KeyValue(progresso.progressProperty(), PROGRESSO_TARGET, Interpolator.EASE_BOTH),
                new KeyValue(dificuldadesSeries.getData().get(0).YValueProperty(), DIFICULDADES_TARGET[0], Interpolator.EASE_BOTH),
                new KeyValue(dificuldadesSeries.getData().get(1).YValueProperty(), DIFICULDADES_TARGET[1], Interpolator.EASE_BOTH),
                new KeyValue(dificuldadesSeries.getData().get(2).YValueProperty(), DIFICULDADES_TARGET[2], Interpolator.EASE_BOTH),
                new KeyValue(dificuldadesSeries.getData().get(3).YValueProperty(), DIFICULDADES_TARGET[3], Interpolator.EASE_BOTH),
                new KeyValue(dificuldadesSeries.getData().get(4).YValueProperty(), DIFICULDADES_TARGET[4], Interpolator.EASE_BOTH),
                new KeyValue(evolucoesSeries.getData().get(0).YValueProperty(), EVOLUCOES_TARGET[0], Interpolator.EASE_BOTH),
                new KeyValue(evolucoesSeries.getData().get(1).YValueProperty(), EVOLUCOES_TARGET[1], Interpolator.EASE_BOTH),
                new KeyValue(evolucoesSeries.getData().get(2).YValueProperty(), EVOLUCOES_TARGET[2], Interpolator.EASE_BOTH),
                new KeyValue(evolucoesSeries.getData().get(3).YValueProperty(), EVOLUCOES_TARGET[3], Interpolator.EASE_BOTH),
                new KeyValue(evolucoesSeries.getData().get(4).YValueProperty(), EVOLUCOES_TARGET[4], Interpolator.EASE_BOTH)
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

    private void setChartValue(eu.hansolo.tilesfx.chart.ChartData data, double value) {
        try {
            data.setValue(value);
        } catch (Exception ignored) {
            // TilesFX versions vary; fail gracefully without crashing a UI.
        }
    }

    private record DisciplineStatus(String name, double progress, float peso, NivelDisciplina nivel, double velocidade, double consistencia, double precisao) {}

    private static final class DisciplineStatusCell extends ListCell<DisciplineStatus> {
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
        public void updateItem(DisciplineStatus item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
                setText(null);
                return;
            }

            name.setText(item.name());
            progress.setValue(item.progress());
            percent.setText((int) Math.round(item.progress() * 100) + "%");
            peso.setText("Peso: " + item.peso());
            nivel.setText("Nível: " + item.nivel().getDescricao());
            velocidade.setProgress(item.velocidade());
            consistencia.setProgress(item.consistencia());
            precisao.setProgress(item.precisao());
            setText(null);
            setGraphic(root);
        }
    }

}

