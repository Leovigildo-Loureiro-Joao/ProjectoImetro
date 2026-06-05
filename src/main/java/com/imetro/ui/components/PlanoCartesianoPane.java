package com.imetro.ui.components;

import java.util.List;

import com.imetro.util.QuestaoGraficoSupport.PlanoCartesianoConfig;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class PlanoCartesianoPane extends VBox {

    private static final int TOTAL_AMOSTRAS = 61;

    private final Label badgeLabel = new Label("Apoio visual");
    private final Label tituloLabel = new Label("Grafico de apoio");
    private final Label subtituloLabel = new Label();
    private final Label eixoXLabel = new Label("eixo x");
    private final Label eixoYLabel = new Label("eixo y");
    private final Label dicaLabel = new Label();
    private final NumberAxis eixoX = new NumberAxis();
    private final NumberAxis eixoY = new NumberAxis();
    private final LineChart<Number, Number> chart = new LineChart<>(eixoX, eixoY);

    private String accentHex = "#202e3d";

    public PlanoCartesianoPane() {
        getStyleClass().addAll("question-side-card", "plano-cartesiano-card");
        setSpacing(12);
        setPadding(new Insets(14));
        setFillWidth(true);
        setMinWidth(0);
        setPrefWidth(0);
        setMaxWidth(Double.MAX_VALUE);
        setMinHeight(360);
        setPrefHeight(360);
        setMaxHeight(Double.MAX_VALUE);

        HBox topo = new HBox(badgeLabel);
        topo.setAlignment(Pos.CENTER_LEFT);

        badgeLabel.getStyleClass().add("plano-cartesiano-badge");
        tituloLabel.getStyleClass().add("question-side-title");
        subtituloLabel.getStyleClass().add("question-side-copy");
        subtituloLabel.setWrapText(true);

        eixoXLabel.getStyleClass().add("question-side-caption");
        eixoXLabel.setMaxWidth(Double.MAX_VALUE);
        eixoXLabel.setAlignment(Pos.CENTER);

        eixoYLabel.getStyleClass().add("question-side-caption");
        eixoYLabel.setRotate(-90);

        dicaLabel.getStyleClass().add("question-side-caption");
        dicaLabel.setWrapText(true);

        prepararChart();

        StackPane chartShell = new StackPane(chart);
        chartShell.getStyleClass().add("plano-cartesiano-shell");
        chartShell.setPadding(new Insets(6));
        chartShell.setMinWidth(0);
        chartShell.setPrefHeight(290);
        chartShell.setMaxWidth(Double.MAX_VALUE);
        chartShell.setMaxHeight(Double.MAX_VALUE);
        Rectangle shellClip = new Rectangle();
        shellClip.setArcWidth(28);
        shellClip.setArcHeight(28);
        shellClip.widthProperty().bind(chartShell.widthProperty());
        shellClip.heightProperty().bind(chartShell.heightProperty());
        chartShell.setClip(shellClip);

        chart.prefWidthProperty().bind(Bindings.max(0d, chartShell.widthProperty().subtract(12d)));
        chart.maxWidthProperty().bind(Bindings.max(0d, chartShell.widthProperty().subtract(12d)));
        chart.prefHeightProperty().bind(Bindings.max(220d, chartShell.heightProperty().subtract(12d)));
        chart.maxHeightProperty().bind(Bindings.max(220d, chartShell.heightProperty().subtract(12d)));

        StackPane eixoYHolder = new StackPane(eixoYLabel);
        eixoYHolder.setMinWidth(24);
        eixoYHolder.setPrefWidth(24);
        eixoYHolder.setMaxWidth(24);
        eixoYHolder.setAlignment(Pos.CENTER);

        BorderPane planoBox = new BorderPane();
        planoBox.setMinWidth(0);
        planoBox.setMaxWidth(Double.MAX_VALUE);
        planoBox.setLeft(eixoYHolder);
        planoBox.setCenter(chartShell);
        planoBox.setBottom(eixoXLabel);
        BorderPane.setMargin(eixoXLabel, new Insets(8, 0, 0, 0));
        VBox.setVgrow(planoBox, Priority.ALWAYS);

        getChildren().addAll(topo, tituloLabel, subtituloLabel, planoBox, dicaLabel);
    }

    @SuppressWarnings("unchecked")
    public void aplicarConfig(PlanoCartesianoConfig config) {
        if (config == null) {
            return;
        }

        accentHex = config.accentHex();
        tituloLabel.setText(config.titulo());
        tituloLabel.setStyle("-fx-text-fill: " + accentHex + ";");
        subtituloLabel.setText(config.subtitulo());
        eixoXLabel.setText(config.eixoX());
        eixoYLabel.setText(config.eixoY());
        dicaLabel.setText(config.dica());
        badgeLabel.setStyle(
            "-fx-text-fill: " + accentHex + ";" +
            "-fx-border-color: " + accentHex + ";" +
            "-fx-background-color: rgba(255,255,255,0.75);"
        );

        ObservableList<XYChart.Data<Number, Number>> pontos = gerarPontos(config);
        configurarEixos(config, pontos);

        XYChart.Series<Number, Number> serie = new XYChart.Series<>();
        serie.setData(pontos);
        chart.setData(FXCollections.observableArrayList(serie));

        Platform.runLater(this::aplicarEstiloDinamico);
    }

    private void prepararChart() {
        chart.getStyleClass().add("plano-cartesiano-chart");
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.setCreateSymbols(false);
        chart.setAlternativeColumnFillVisible(false);
        chart.setAlternativeRowFillVisible(false);
        chart.setHorizontalGridLinesVisible(true);
        chart.setVerticalGridLinesVisible(true);
        chart.setHorizontalZeroLineVisible(true);
        chart.setVerticalZeroLineVisible(true);
        chart.setMinHeight(220);
        chart.setMinWidth(0);
        chart.setPrefHeight(220);
        chart.setMaxHeight(Double.MAX_VALUE);
        chart.setPrefWidth(0);
        chart.setMaxWidth(Double.MAX_VALUE);

        eixoX.setAnimated(false);
        eixoY.setAnimated(false);
        eixoX.setForceZeroInRange(true);
        eixoY.setForceZeroInRange(true);
        eixoX.setMinorTickVisible(false);
        eixoY.setMinorTickVisible(false);
    }

    private ObservableList<XYChart.Data<Number, Number>> gerarPontos(PlanoCartesianoConfig config) {
        ObservableList<XYChart.Data<Number, Number>> pontos = FXCollections.observableArrayList();
        double passo = (config.xMax() - config.xMin()) / (TOTAL_AMOSTRAS - 1);

        for (int i = 0; i < TOTAL_AMOSTRAS; i++) {
            double x = config.xMin() + (passo * i);
            pontos.add(new XYChart.Data<>(arredondar(x), arredondar(config.calcularY(x))));
        }

        return pontos;
    }

    private void configurarEixos(PlanoCartesianoConfig config, List<XYChart.Data<Number, Number>> pontos) {
        double menorY = 0d;
        double maiorY = 0d;
        for (XYChart.Data<Number, Number> ponto : pontos) {
            double y = ponto.getYValue().doubleValue();
            menorY = Math.min(menorY, y);
            maiorY = Math.max(maiorY, y);
        }

        double margemY = Math.max(1d, (maiorY - menorY) * 0.18d);
        double yMin = Math.floor(menorY - margemY);
        double yMax = Math.ceil(maiorY + margemY);
        if (yMin == yMax) {
            yMin -= 1d;
            yMax += 1d;
        }

        double intervaloY = yMax - yMin;
        double yTick = Math.max(1d, Math.ceil(intervaloY / 6d));

        eixoX.setAutoRanging(false);
        eixoX.setLowerBound(config.xMin());
        eixoX.setUpperBound(config.xMax());
        eixoX.setTickUnit(config.xTickUnit());

        eixoY.setAutoRanging(false);
        eixoY.setLowerBound(yMin);
        eixoY.setUpperBound(yMax);
        eixoY.setTickUnit(yTick);
    }

    private void aplicarEstiloDinamico() {
        Node linha = chart.lookup(".default-color0.chart-series-line");
        if (linha != null) {
            linha.setStyle("-fx-stroke: " + accentHex + "; -fx-stroke-width: 2.8px;");
        }

        for (String seletor : List.of(".chart-vertical-zero-line", ".chart-horizontal-zero-line")) {
            Node zeroLine = chart.lookup(seletor);
            if (zeroLine != null) {
                zeroLine.setStyle("-fx-stroke: " + accentHex + "; -fx-stroke-width: 1.6px; -fx-opacity: 0.75;");
            }
        }
    }

    private double arredondar(double valor) {
        return Math.round(valor * 100d) / 100d;
    }
}
