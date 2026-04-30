package com.imetro.ui.controller.candidato;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.imetro.config.RuntimeConfig;
import com.imetro.persistence.repository.UserRepository;
import com.imetro.ui.components.CircleProgress;
import com.imetro.ui.components.relatorio.InsightCard;
import com.imetro.ui.components.relatorio.ReportCard;
import com.imetro.ui.components.relatorio.SectionTitle;
import com.imetro.ui.components.relatorio.TimelineStep;
import com.imetro.util.Authentication;
import com.imetro.util.ProfileSessionState;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class RelatoriosController implements Initializable {

    @FXML
    private Label periodLabel;

    @FXML
    private Label heroScoreLabel;

    @FXML
    private Label heroSummaryLabel;

    @FXML
    private Label accuracyStatLabel;

    @FXML
    private Label paceStatLabel;

    @FXML
    private Label consistencyStatLabel;

    @FXML
    private Label focusStatLabel;

    @FXML
    private StackPane confiancaRingHost;

    @FXML
    private AreaChart<String, Number> evolucaoChart;

    @FXML
    private BarChart<String, Number> disciplinasChart;

    @FXML
    private VBox patternsBox;

    @FXML
    private VBox planoBox;

    @FXML
    private VBox reportsTimelineBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String primeiroNome = resolvePrimeiroNome();
        String hoje = LocalDate.now().format(DateTimeFormatter.ofPattern("dd 'de' MMMM", new Locale("pt", "AO")));

        periodLabel.setText("Panorama semanal de " + primeiroNome + " atualizado em " + hoje + ".");
        heroScoreLabel.setText("81%");
        heroSummaryLabel.setText("O teu desempenho esta estavel, com ganho claro em precisao e melhor leitura de questoes extensas.");
        accuracyStatLabel.setText("84%");
        paceStatLabel.setText("42 s");
        consistencyStatLabel.setText("Alta");
        focusStatLabel.setText("Algebra");

        setupRing();
        setupCharts();
        setupPatterns();
        setupPlan();
        setupRecentReports();
    }

    private void setupRing() {
        CircleProgress progress = new CircleProgress(54, 54, 54, 0.81f);
        progress.setSubtitle("Confianca");
        confiancaRingHost.getChildren().setAll(progress);
    }

    private void setupCharts() {
        evolucaoChart.setAnimated(false);
        evolucaoChart.setLegendVisible(false);
        evolucaoChart.setCreateSymbols(true);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Sem 1", 56));
        series.getData().add(new XYChart.Data<>("Sem 2", 61));
        series.getData().add(new XYChart.Data<>("Sem 3", 68));
        series.getData().add(new XYChart.Data<>("Sem 4", 66));
        series.getData().add(new XYChart.Data<>("Sem 5", 74));
        series.getData().add(new XYChart.Data<>("Sem 6", 81));
        evolucaoChart.getData().setAll(series);

        disciplinasChart.setAnimated(false);
        disciplinasChart.setLegendVisible(false);
        disciplinasChart.setCategoryGap(18);
        disciplinasChart.setBarGap(6);

        XYChart.Series<String, Number> barSeries = new XYChart.Series<>();
        barSeries.getData().add(new XYChart.Data<>("Mat", 86));
        barSeries.getData().add(new XYChart.Data<>("Fis", 71));
        barSeries.getData().add(new XYChart.Data<>("Qui", 64));
        barSeries.getData().add(new XYChart.Data<>("Bio", 78));
        barSeries.getData().add(new XYChart.Data<>("Por", 82));
        disciplinasChart.getData().setAll(barSeries);
    }

    private void setupPatterns() {
        patternsBox.getChildren().setAll(
            new SectionTitle("Padroes encontrados", "Leituras que se repetem no teu historico recente."),
            new InsightCard("Pico de rendimento", "O teu melhor bloco costuma surgir entre a 2a e a 4a questao."),
            new InsightCard("Erro recorrente", "As quedas aparecem quando a pergunta mistura calculo e interpretacao."),
            new InsightCard("Forca silenciosa", "Quando revisas o enunciado, a taxa de acerto sobe de forma consistente."),
            new InsightCard("Alerta util", "Fisica precisa de mais repeticao em questoes com duas etapas.")
        );
    }

    private void setupPlan() {
        planoBox.getChildren().setAll(
            new SectionTitle("Plano sugerido", "Ritmo recomendado para a proxima janela de estudo."),
            new TimelineStep("Hoje", "Fechar 1 bloco curto de Algebra com foco em velocidade limpa."),
            new TimelineStep("Amanha", "Executar 1 diagnostico curto de Fisica e rever apenas os erros."),
            new TimelineStep("48h", "Refazer 5 questoes mistas para medir retencao e consistencia."),
            new TimelineStep("Fim da semana", "Entrar num teste adaptativo longo para validar ganho real.")
        );
    }

    private void setupRecentReports() {
        reportsTimelineBox.getChildren().setAll(
            new SectionTitle("Ultimos retratos", "Resumo rapido dos relatorios que mais contam agora."),
            new ReportCard("Teste adaptativo", "Matematica", "84% de acerto, nivel dificil sustentado.", "Ha 2 dias", "pill-good"),
            new ReportCard("Diagnostico", "Fisica", "Boa base conceitual, mas ainda oscilas no tempo.", "Ha 5 dias", "pill-warn"),
            new ReportCard("Teste adaptativo", "Portugues", "Leitura melhorou e os erros de pressao cairam.", "Ha 8 dias", "pill-good")
        );
    }


    private String resolvePrimeiroNome() {
        String email = Authentication.getCurrentUserEmail();
        String nome = ProfileSessionState.resolveName(email, null);

        if ((nome == null || nome.isBlank()) && email != null && !email.isBlank() && RuntimeConfig.isDbEnabled()) {
            try {
                nome = new UserRepository().getNomeByEmail(email);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }

        if (nome == null || nome.isBlank()) {
            nome = email == null || email.isBlank() ? "candidato" : email;
        }

        String[] partes = nome.trim().split("\\s+");
        return partes.length == 0 ? "candidato" : partes[0];
    }
}
