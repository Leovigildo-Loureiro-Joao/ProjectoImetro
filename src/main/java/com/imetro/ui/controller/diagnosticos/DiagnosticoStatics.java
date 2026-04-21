package com.imetro.ui.controller.diagnosticos;

import java.net.URL;
import java.util.ResourceBundle;

import com.imetro.domain.dto.Stats;

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

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // Placeholder (até ligar com dados reais)
        veloChart = new XYChart.Series<>();
        resChart = new XYChart.Series<>();
        logChart = new XYChart.Series<>();
        conChart = new XYChart.Series<>();
        preChart = new XYChart.Series<>();
        setPercent(lblMediaGeral, progressMediaGeral, 0.58);
        if (lblTotalTestes != null) lblTotalTestes.setText("3");
        if (lblMelhorPontuacao != null) lblMelhorPontuacao.setText("78%");
        if (lblMelhorDisciplina != null) lblMelhorDisciplina.setText("Matemática");
        if (lblPiorPontuacao != null) lblPiorPontuacao.setText("46%");
        if (lblPiorDisciplina != null) lblPiorDisciplina.setText("Biologia");
        setPercent(lblTaxaAcerto, progressTaxaAcerto, 0.61);

        if (lblTempoMedio != null) lblTempoMedio.setText("04:12");
        if (lblTempoMaisRapido != null) lblTempoMaisRapido.setText("02:45");
        if (lblDisciplinaRapida != null) lblDisciplinaRapida.setText("Português");
        if (lblTempoMaisLento != null) lblTempoMaisLento.setText("06:30");
        if (lblDisciplinaLenta != null) lblDisciplinaLenta.setText("Física");

        if (lblTotalAcertos != null) lblTotalAcertos.setText("18");
        if (lblTotalErros != null) lblTotalErros.setText("12");
        if (lblTotalQuestoes != null) lblTotalQuestoes.setText("30");
        veloChart.setName("Velocidade");
        resChart.setName("Resistencia");
        logChart.setName("Logica");
        conChart.setName("Consistencia");
        preChart.setName("Precisao");
        if (barDisciplina != null) {
           veloChart.getData().add(new XYChart.Data<>("Matematica", 10));
           resChart.getData().add(new XYChart.Data<>("Matematica", 50));
           logChart.getData().add(new XYChart.Data<>("Matematica", 75));
           conChart.getData().add(new XYChart.Data<>("Matematica", 25));
           preChart.getData().add(new XYChart.Data<>("Matematica", 35));

            veloChart.getData().add(new XYChart.Data<>("Ling. Portuguesa", 80));
           resChart.getData().add(new XYChart.Data<>("Ling. Portuguesa", 40));
           logChart.getData().add(new XYChart.Data<>("Ling. Portuguesa", 55));
           conChart.getData().add(new XYChart.Data<>("Ling. Portuguesa", 95));
           preChart.getData().add(new XYChart.Data<>("Ling. Portuguesa", 15));

              veloChart.getData().add(new XYChart.Data<>("Quimica", 100));
           resChart.getData().add(new XYChart.Data<>("Quimica", 80));
           logChart.getData().add(new XYChart.Data<>("Quimica", 55));
           conChart.getData().add(new XYChart.Data<>("Quimica", 5));
           preChart.getData().add(new XYChart.Data<>("Quimica", 1));

              veloChart.getData().add(new XYChart.Data<>("Fisica", 10));
           resChart.getData().add(new XYChart.Data<>("Fisica", 40));
           logChart.getData().add(new XYChart.Data<>("Fisica", 85));
           conChart.getData().add(new XYChart.Data<>("Fisica", 15));
           preChart.getData().add(new XYChart.Data<>("Fisica", 15));

        }

        barDisciplina.getData().clear();
        barDisciplina.getData().add(veloChart);
        barDisciplina.getData().add(resChart);
        barDisciplina.getData().add(logChart);
        barDisciplina.getData().add(conChart);
        barDisciplina.getData().add(preChart);
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
