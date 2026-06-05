package com.imetro.ui.controller.candidato;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import com.imetro.config.RuntimeConfig;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoEtapa;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoInsight;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoRegistro;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoResumo;
import com.imetro.persistence.repository.UserRepository;
import com.imetro.services.PlaneamentoEstudoService;
import com.imetro.ui.components.CircleProgress;
import com.imetro.ui.components.relatorio.InsightCard;
import com.imetro.ui.components.relatorio.ReportCard;
import com.imetro.ui.components.relatorio.SectionTitle;
import com.imetro.ui.components.relatorio.TimelineStep;
import com.imetro.util.Authentication;
import com.imetro.util.ProfileSessionState;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class RelatoriosController implements Initializable {

    private static final Locale LOCALE_PT = new Locale("pt", "AO");
    private static final DateTimeFormatter DATA_EXTENSA = DateTimeFormatter.ofPattern("dd 'de' MMMM", LOCALE_PT);

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

    private final PlaneamentoEstudoService planeamentoService = new PlaneamentoEstudoService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String primeiroNome = resolvePrimeiroNome();
        String hoje = LocalDate.now().format(DATA_EXTENSA);

        periodLabel.setText("Plano de estudo de " + primeiroNome + " atualizado em " + hoje + ".");

        PlaneamentoEstudoResumo resumo = planeamentoService.gerarResumo(Authentication.getCurrentUserId());

        heroScoreLabel.setText(formatPercentual(resumo.pontuacaoHero()));
        heroSummaryLabel.setText(resumo.resumoHero());
        accuracyStatLabel.setText(resumo.acertoMedio());
        paceStatLabel.setText(resumo.ritmoMedio());
        consistencyStatLabel.setText(resumo.consistenciaMedia());
        focusStatLabel.setText(resumo.focoAtual());

        setupRing(resumo.pontuacaoHero());
        setupCharts(resumo);
        setupPatterns(resumo);
        setupPlan(resumo);
        setupRecentReports(resumo);
    }

    private void setupRing(double score) {
        CircleProgress progress = new CircleProgress(54, 54, 54, (float) Math.max(0d, Math.min(1d, score / 100d)));
        progress.setSubtitle("Confianca");
        confiancaRingHost.getChildren().setAll(progress);
    }

    private void setupCharts(PlaneamentoEstudoResumo resumo) {
        evolucaoChart.setAnimated(false);
        evolucaoChart.setLegendVisible(false);
        evolucaoChart.setCreateSymbols(true);
        evolucaoChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        resumo.evolucao().forEach(ponto ->
            series.getData().add(new XYChart.Data<>(ponto.rotulo(), ponto.valor()))
        );
        evolucaoChart.getData().setAll(series);

        disciplinasChart.setAnimated(false);
        disciplinasChart.setLegendVisible(false);
        disciplinasChart.setCategoryGap(18);
        disciplinasChart.setBarGap(6);
        disciplinasChart.getData().clear();

        XYChart.Series<String, Number> barSeries = new XYChart.Series<>();
        resumo.disciplinas().forEach(disciplina ->
            barSeries.getData().add(new XYChart.Data<>(abreviarDisciplina(disciplina.disciplina()), disciplina.pontuacao()))
        );
        disciplinasChart.getData().setAll(barSeries);
    }

    private void setupPatterns(PlaneamentoEstudoResumo resumo) {
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(new SectionTitle("Sinais de estudo", "A politica usa o teu historico real para decidir o que vem primeiro."));
        for (PlaneamentoEstudoInsight insight : resumo.insights()) {
            nodes.add(new InsightCard(insight.titulo(), insight.descricao()));
        }
        patternsBox.getChildren().setAll(nodes);
    }

    private void setupPlan(PlaneamentoEstudoResumo resumo) {
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(new SectionTitle("Plano inteligente", "Blocos curtos, revisao espaçada e confirmacao final com base no teu desempenho real."));
        for (PlaneamentoEstudoEtapa etapa : resumo.etapas()) {
            nodes.add(new TimelineStep(etapa.janela(), etapa.acao() + " " + etapa.detalhe()));
        }
        planoBox.getChildren().setAll(nodes);
    }

    private void setupRecentReports(PlaneamentoEstudoResumo resumo) {
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(new SectionTitle("Historico recente", "Os ultimos ciclos que alimentam o planeamento desta semana."));
        for (PlaneamentoEstudoRegistro registro : resumo.registros()) {
            nodes.add(new ReportCard(registro.tipo(), registro.disciplina(), registro.resumo(), registro.momento(), registro.pillClass()));
        }
        reportsTimelineBox.getChildren().setAll(nodes);
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

    private String formatPercentual(double valor) {
        return Math.round(Math.max(0d, Math.min(100d, valor))) + "%";
    }

    private String abreviarDisciplina(String disciplina) {
        if (disciplina == null || disciplina.isBlank()) {
            return "Disciplina";
        }
        return disciplina;
    }
}
