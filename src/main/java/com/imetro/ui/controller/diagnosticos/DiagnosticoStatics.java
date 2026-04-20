package com.imetro.ui.controller.diagnosticos;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DiagnosticoStatics implements Initializable {

    @FXML
    private Label lblMediaGeral;
    @FXML
    private ProgressBar progressMediaGeral;
    @FXML
    private Label lblTotalTestes;
    @FXML
    private Label lblMelhorPontuacao;
    @FXML
    private Label lblMelhorDisciplina;
    @FXML
    private Label lblPiorPontuacao;
    @FXML
    private Label lblPiorDisciplina;
    @FXML
    private ProgressBar progressTaxaAcerto;
    @FXML
    private Label lblTaxaAcerto;
    @FXML
    private VBox detalhamentoDisciplinas;
    @FXML
    private Label lblTempoMedio;
    @FXML
    private Label lblTempoMaisRapido;
    @FXML
    private Label lblDisciplinaRapida;
    @FXML
    private Label lblTempoMaisLento;
    @FXML
    private Label lblDisciplinaLenta;
    @FXML
    private Label lblTotalAcertos;
    @FXML
    private Label lblTotalErros;
    @FXML
    private Label lblTotalQuestoes;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // Placeholder (até ligar com dados reais)
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

        if (detalhamentoDisciplinas != null) {
            detalhamentoDisciplinas.getChildren().clear();
            detalhamentoDisciplinas.getChildren().addAll(
                disciplinaRow("Matemática", "78%"),
                disciplinaRow("Português", "64%"),
                disciplinaRow("Física", "52%")
            );
        }
    }

    private static void setPercent(Label label, ProgressBar bar, double value) {
        if (bar != null) {
            bar.setProgress(Math.max(0, Math.min(1, value)));
        }
        if (label != null) {
            label.setText(Math.round(value * 100) + "%");
        }
    }

    private static HBox disciplinaRow(String disciplina, String media) {
        Label left = new Label(disciplina);
        left.getStyleClass().add("muted");
        Label right = new Label(media);
        right.getStyleClass().add("h3-thin");

        HBox row = new HBox(left, right);
        row.setSpacing(12);
        row.setPadding(new Insets(6, 0, 6, 0));
        HBox.setMargin(right, new Insets(0, 0, 0, 12));
        return row;
    }
}
