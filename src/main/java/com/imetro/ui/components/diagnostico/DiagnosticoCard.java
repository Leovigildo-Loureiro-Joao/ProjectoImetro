package com.imetro.ui.components.diagnostico;

import com.imetro.domain.dto.Topico;
import com.imetro.services.DiagnosticoService;
import com.imetro.ui.components.CircleProgress;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.ArrayList;

public class DiagnosticoCard extends VBox {

    private final JFXCheckBox diciplina;
    private final CircleProgress progressBar;
    private final ArrayList<Topico> topicos = new ArrayList<>();
    private final JFXButton diagnosticoButton;

    public DiagnosticoCard(
        DiagnosticoService.DiagnosticoDisciplinaResumo resumo,
        Callback<ArrayList<Topico>, Void> run,
        Runnable massa
    ) {
        this.topicos.addAll(resumo.topicos());

        this.diciplina = new JFXCheckBox(resumo.nomeDisciplina());
        this.diciplina.getStyleClass().add("diagnostico-card-check");

        Label badge = new Label(resumo.totalQuestoes() + " questoes");
        badge.getStyleClass().add("diagnostico-card-badge");

        Label destaque = new Label(resumo.destaque());
        destaque.getStyleClass().add("diagnostico-card-highlight");

        Label resumoLabel = new Label(resumo.resumo());
        resumoLabel.getStyleClass().add("diagnostico-card-summary");
        resumoLabel.setWrapText(true);

        Label observacaoLabel = new Label(resumo.observacao());
        observacaoLabel.getStyleClass().add("diagnostico-card-note");
        observacaoLabel.setWrapText(true);

        Label progressoLabel = new Label(resumo.legendaIndicador());
        progressoLabel.getStyleClass().add("diagnostico-card-progress-label");

        progressBar = new CircleProgress(38, 38);
        progressBar.setValue(resumo.indicador());

        Label tendenciaValor = new Label(resumo.tendencia());
        tendenciaValor.getStyleClass().add("diagnostico-card-chip-value");
        Label tendenciaTitulo = new Label("Tendencia");
        tendenciaTitulo.getStyleClass().add("diagnostico-card-chip-title");

        Label nivelValor = new Label(resumo.nivel());
        nivelValor.getStyleClass().add("diagnostico-card-chip-value");
        Label nivelTitulo = new Label("Nivel");
        nivelTitulo.getStyleClass().add("diagnostico-card-chip-title");

        HBox header = new HBox(10, diciplina, criarSpacer(), badge);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox progressoInfo = new VBox(4, progressoLabel, destaque);
        progressoInfo.setAlignment(Pos.CENTER_LEFT);
        HBox progressoRow = new HBox(14, progressBar, progressoInfo);
        progressoRow.setAlignment(Pos.CENTER_LEFT);

        HBox metricas = new HBox(
            10,
            criarMetrica("Topicos", String.valueOf(resumo.totalTopicos())),
            criarMetrica("Subtopicos", String.valueOf(resumo.totalSubtopicos())),
            criarMetrica("Tendencia", resumo.tendencia()),
            criarMetrica("Nivel", resumo.nivel())
        );
        metricas.setAlignment(Pos.CENTER_LEFT);

        diagnosticoButton = new JFXButton("Iniciar diagnostico");
        diagnosticoButton.getStyleClass().addAll("btn-primary", "diagnostico-card-button");
        diagnosticoButton.setDisable(true);
        diagnosticoButton.setMaxWidth(Double.MAX_VALUE);

        getChildren().addAll(header, progressoRow, resumoLabel, metricas, observacaoLabel, diagnosticoButton);
        configurarEstilo();
        configurarAcoes(run, massa);
    }

    private VBox criarMetrica(String titulo, String valor) {
        Label valorLabel = new Label(valor);
        valorLabel.getStyleClass().add("diagnostico-card-chip-value");

        Label tituloLabel = new Label(titulo);
        tituloLabel.getStyleClass().add("diagnostico-card-chip-title");

        VBox box = new VBox(4, valorLabel, tituloLabel);
        box.getStyleClass().add("diagnostico-card-chip");
        box.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(box, Priority.ALWAYS);
        box.setMaxWidth(Double.MAX_VALUE);
        return box;
    }

    private Region criarSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    private void configurarEstilo() {
        getStyleClass().addAll("card", "diagnostico-card");
        setPadding(new Insets(18));
        setSpacing(14);
        setAlignment(Pos.TOP_LEFT);
        setPrefWidth(320);
        setMinWidth(300);
        setMaxWidth(340);
    }

    private void configurarAcoes(Callback<ArrayList<Topico>, Void> action, Runnable massa) {
        diagnosticoButton.setOnAction(event -> action.call(new ArrayList<>(topicos)));
        diciplina.selectedProperty().addListener((obs, oldValue, newValue) -> {
            diagnosticoButton.setDisable(!newValue);
            if (newValue) {
                if (!getStyleClass().contains("diagnostico-card-active")) {
                    getStyleClass().add("diagnostico-card-active");
                }
            } else {
                getStyleClass().remove("diagnostico-card-active");
            }
            massa.run();
        });
    }

    public JFXCheckBox getDiciplina() {
        return diciplina;
    }

    public ArrayList<Topico> getTopicos() {
        return topicos;
    }

    public void setSelecionado(boolean selecionado) {
        diciplina.setSelected(selecionado);
        diagnosticoButton.setDisable(!selecionado);
        if (selecionado) {
            if (!getStyleClass().contains("diagnostico-card-active")) {
                getStyleClass().add("diagnostico-card-active");
            }
        } else {
            getStyleClass().remove("diagnostico-card-active");
        }
    }
}
