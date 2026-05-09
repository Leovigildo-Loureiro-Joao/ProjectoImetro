package com.imetro.ui.controller.candidato.diagnosticos;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.imetro.domain.dto.diagnostico.DiagnosticoDto;
import com.imetro.domain.dto.diagnostico.StatsDiagnotico;
import com.imetro.domain.dto.diagnostico.StatsQuestaoQtd;
import com.imetro.domain.dto.diagnostico.TempoStatsDiagnostico;
import com.imetro.domain.dto.stats.Stats;
import com.imetro.services.DiagnosticoService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DiagnosticoStatics implements Initializable {
    @FXML
    private BarChart<String, Integer> barDisciplina;

    @FXML
    private VBox estatisticasPane;

    @FXML
    private Label lblDisciplinaLenta;

    @FXML
    private Label lblDisciplinaRapida;

    @FXML
    private Label lblMediaGeral;

    @FXML
    private Label lblMelhorDisciplina;

    @FXML
    private Label lblMelhorPontuacao;

    @FXML
    private Label lblPiorDisciplina;

    @FXML
    private Label lblPiorPontuacao;

    @FXML
    private Label lblTaxaAcerto;

    @FXML
    private Label lblTempoMaisLento;

    @FXML
    private Label lblTempoMaisRapido;

    @FXML
    private Label lblTempoMedio;

    @FXML
    private Label lblTotalAcertos;

    @FXML
    private Label lblTotalErros;

    @FXML
    private Label lblTotalQuestoes;

    @FXML
    private Label lblTotalTestes;

    @FXML
    private ProgressBar progressMediaGeral;

    @FXML
    private ProgressBar progressTaxaAcerto;

    private XYChart.Series<String,Integer> veloChart;
    private XYChart.Series<String,Integer> resChart;
    private XYChart.Series<String,Integer> logChart;
    private XYChart.Series<String,Integer> conChart;
    private XYChart.Series<String,Integer> preChart;
    private DiagnosticoService diagnosticoService;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // Placeholder (até ligar com dados reais)
        diagnosticoService=new DiagnosticoService();
        setupStatics();
    }

    private void setupStatics(){
        veloChart = new XYChart.Series<>();
        resChart = new XYChart.Series<>();
        logChart = new XYChart.Series<>();
        conChart = new XYChart.Series<>();
        preChart = new XYChart.Series<>();
        veloChart.setName("Velocidade");
        resChart.setName("Resistencia");
        logChart.setName("Logica");
        conChart.setName("Consistencia");
        preChart.setName("Precisao");

        setupStats();
        setupAreaChart();
        setupTime();
        setupQuestaoMetric();

    }

    private void setupAreaChart(){
        barDisciplina.getData().clear();
        List<Map<String,?>> list = diagnosticoService.statsDisciplina();
        for (Map<String,?> stat : list) {
            Stats stats=(Stats)stat.get("value");
            String key=stat.get("key").toString();
            veloChart.getData().add(new XYChart.Data<>(key, (int)(stats.velocidade()*100)));
            resChart.getData().add(new XYChart.Data<>(key, (int)(stats.resiliencia()*100)));
            logChart.getData().add(new XYChart.Data<>(key, (int)(stats.logica()*100)));
            conChart.getData().add(new XYChart.Data<>(key, (int)(stats.consistencia()*100)));
            preChart.getData().add(new XYChart.Data<>(key, (int)(stats.precisao()*100)));
        }
        barDisciplina.getData().add(veloChart);
        barDisciplina.getData().add(resChart);
        barDisciplina.getData().add(logChart);
        barDisciplina.getData().add(conChart);
        barDisciplina.getData().add(preChart);
    }

    private void setupStats(){
        StatsDiagnotico stats=diagnosticoService.statsDiagnotico();

        if (lblTotalTestes != null) lblTotalTestes.setText(stats.totalTeste()+"");
        if (lblMelhorPontuacao != null) lblMelhorPontuacao.setText((stats.melhor().percemt()*100)+"%");
        if (lblMelhorDisciplina != null) lblMelhorDisciplina.setText(stats.melhor().title());
        if (lblPiorPontuacao != null) lblPiorPontuacao.setText((stats.atencao().percemt()*100)+"%");
        if (lblPiorDisciplina != null) lblPiorDisciplina.setText(stats.atencao().title());

        setPercent(lblMediaGeral, progressMediaGeral, stats.mediaGeral());

        setPercent(lblTaxaAcerto, progressTaxaAcerto, stats.toxaAcerto());
    }

    private void setupTime(){
        TempoStatsDiagnostico  stats=diagnosticoService.statsTempoDiagnotic();
        if (lblTempoMedio != null) lblTempoMedio.setText(stats.tempoMedio());
        if (lblTempoMaisRapido != null) lblTempoMaisRapido.setText(stats.tempoMaisRapido());
        if (lblDisciplinaRapida != null) lblDisciplinaRapida.setText(stats.discRapida());
        if (lblTempoMaisLento != null) lblTempoMaisLento.setText(stats.tempoMaisLento());
        if (lblDisciplinaLenta != null) lblDisciplinaLenta.setText(stats.discLenta());
    }

    private void setupQuestaoMetric(){
        StatsQuestaoQtd  stats=diagnosticoService.statsQuestaoQtd();
        if (lblTotalAcertos != null) lblTotalAcertos.setText(""+stats.totAcertos());
        if (lblTotalErros != null) lblTotalErros.setText(""+stats.totErros());
        if (lblTotalQuestoes != null) lblTotalQuestoes.setText(""+stats.totQuestao());
    }

    private static void setPercent(Label label, ProgressBar bar, double value) {
        if (bar != null) {
            bar.setProgress(Math.max(0, Math.min(1, value)));
        }
        if (label != null) {
            label.setText(Math.round(value * 100) + "%");
        }
    }

}
