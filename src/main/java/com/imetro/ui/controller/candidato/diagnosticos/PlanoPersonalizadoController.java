package com.imetro.ui.controller.candidato.diagnosticos;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import com.imetro.App;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoDisciplina;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoEtapa;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoInsight;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoPonto;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoResumo;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoEstado;
import com.imetro.services.DiagnosticoService;
import com.imetro.services.PlaneamentoEstudoService;
import com.imetro.ui.components.relatorio.InsightCard;
import com.imetro.ui.components.relatorio.TimelineStep;
import com.imetro.ui.controller.lifecycle.DisposableController;
import com.imetro.util.Authentication;
import com.imetro.util.TextoUtil;
import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class PlanoPersonalizadoController implements Initializable, DisposableController {

    @FXML
    private VBox planPane;

    @FXML
    private VBox contentBox;

    @FXML
    private VBox emptyStateBox;

    @FXML
    private Label subtitleLabel;

    @FXML
    private Label stateBadgeLabel;

    @FXML
    private Label heroSummaryLabel;

    @FXML
    private Label heroObjectiveLabel;

    @FXML
    private Label herofocoLabel;

    @FXML
    private Label scoreValueLabel;

    @FXML
    private Label accuracyValueLabel;

    @FXML
    private Label paceValueLabel;

    @FXML
    private Label consistencyValueLabel;

    @FXML
    private Label focusValueLabel;

    @FXML
    private Label focus2ValueLabel;

    @FXML
    private Label chartSubtitleLabel;

    @FXML
    private VBox insightsBox;

    @FXML
    private VBox stepsBox;

    @FXML
    private VBox disciplinesBox;

    @FXML
    private AreaChart<String, Number> evolucaoChart;

    @FXML
    private Label emptyTitleLabel;

    @FXML
    private Label emptyDetailLabel;

    @FXML
    private JFXButton emptyActionButton;

    private final PlaneamentoEstudoService planeamentoService = new PlaneamentoEstudoService();
    private final DiagnosticoService diagnosticoService = new DiagnosticoService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (planPane == null) {
            return;
        }

        UUID candidatoId = Authentication.getCurrentUserId();
        if (candidatoId == null || !diagnosticoService.temHistoricoDiagnostico(candidatoId)) {
            mostrarEstadoBloqueado();
            return;
        }

        PlaneamentoEstudoResumo resumo = planeamentoService.gerarResumo(candidatoId);
        PlaneamentoEstudoEstado estado = planeamentoService.resolverEstadoAtual(candidatoId);
        mostrarPlanoPersonalizado(resumo, estado);
    }

    private void mostrarPlanoPersonalizado(PlaneamentoEstudoResumo resumo, PlaneamentoEstudoEstado estado) {
        definirVisibilidade(contentBox, true);
        definirVisibilidade(emptyStateBox, false);

        if (subtitleLabel != null) {
            subtitleLabel.setText(safeText(estado == null ? null : estado.detalhe(), "Gerado a partir do teu diagnostico e do ritmo real."));
        }
        if (stateBadgeLabel != null) {
            stateBadgeLabel.setText(safeText(estado == null ? null : estado.titulo(), "Plano activo"));
        }
        if (heroSummaryLabel != null) {
            heroSummaryLabel.setText(safeText(resumo.resumoHero(), "Resumo indisponivel."));
        }
        if (scoreValueLabel != null) {
            scoreValueLabel.setText(formatPercent(resumo.pontuacaoHero()));
        }
        if (accuracyValueLabel != null) {
            accuracyValueLabel.setText(safeText(resumo.acertoMedio(), "-"));
        }
        if (paceValueLabel != null) {
            paceValueLabel.setText(safeText(resumo.ritmoMedio(), "-"));
        }
        if (consistencyValueLabel != null) {
            consistencyValueLabel.setText(safeText(resumo.consistenciaMedia(), "-"));
        }
        if (focusValueLabel != null) {
            focusValueLabel.setText(safeText(resumo.focoAtual(), "Sem foco definido"));
            if (!focusValueLabel.getText().contains("Sem foco")) {
                heroObjectiveLabel.setText("Aumentar a consistência em "+resumo.focoAtual());
            }

        }
        if (focus2ValueLabel != null) {
            focus2ValueLabel.setText(safeText(resumo.focoAtual2(), "Sem foco definido"));
            if (!resumo.focoAtual().equals(resumo.focoAtual2())&&!focus2ValueLabel.getText().contains("Sem foco")) {
                heroObjectiveLabel.setText(heroObjectiveLabel.getText()+"e melhorar a precisão em "+resumo.focoAtual2());
                focus2ValueLabel.setVisible(false);
            }

        }
        if (chartSubtitleLabel != null) {
            chartSubtitleLabel.setText("Linha de evolucao dos ultimos ciclos que alimentam o plano.");
        }

        preencherGrafico(resumo.evolucao());
        preencherInsights(resumo.insights());
        preencherEtapas(resumo.etapas());
        preencherDisciplinas(resumo.disciplinas());
    }

    private void mostrarEstadoBloqueado() {
        definirVisibilidade(contentBox, false);
        definirVisibilidade(emptyStateBox, true);

        if (subtitleLabel != null) {
            subtitleLabel.setText("O plano aparece depois do primeiro diagnostico real.");
        }
        if (stateBadgeLabel != null) {
            stateBadgeLabel.setText("Bloqueado");
        }
        if (emptyTitleLabel != null) {
            emptyTitleLabel.setText("Conclua o diagnostico para liberar o plano");
        }
        if (emptyDetailLabel != null) {
            emptyDetailLabel.setText(
                "Quando houver historico de diagnosticos, vamos montar o plano com foco, ritmo e disciplinas prioritarias."
            );
        }
        if (emptyActionButton != null) {
            emptyActionButton.setText("Ir para diagnostico");
        }
    }

    private void preencherGrafico(List<PlaneamentoEstudoPonto> pontos) {
        if (evolucaoChart == null) {
            return;
        }

        evolucaoChart.setAnimated(false);
        evolucaoChart.setLegendVisible(false);
        evolucaoChart.setCreateSymbols(true);
        evolucaoChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Evolucao");

        if (pontos != null) {
            for (PlaneamentoEstudoPonto ponto : pontos) {
                if (ponto == null) {
                    continue;
                }
                series.getData().add(new XYChart.Data<>(
                    safeText(ponto.rotulo(), "Sem rotulo"),
                    limitarPercentual(ponto.valor())
                ));
            }
        }

        if (series.getData().isEmpty()) {
            series.getData().add(new XYChart.Data<>("Sem dados", 0d));
        }

        evolucaoChart.getData().setAll(series);
    }

    private void preencherInsights(List<PlaneamentoEstudoInsight> insights) {
        if (insightsBox == null) {
            return;
        }

        ArrayList<Node> nodes = new ArrayList<>();
        if (insights != null) {
            for (int i = 0; i < Math.min(3, insights.size()); i++) {
                PlaneamentoEstudoInsight insight = insights.get(i);
                if (insight == null) {
                    continue;
                }
                nodes.add(new InsightCard(
                    safeText(insight.titulo(), "Insight"),
                    safeText(insight.descricao(), "")
                ));
            }
        }

        if (nodes.isEmpty()) {
            nodes.add(criarEstadoSecaoVazio(
                "Sem sinais ainda",
                "Complete um diagnostico para alimentar esta leitura."
            ));
        }

        insightsBox.getChildren().setAll(nodes);
    }

    private void preencherEtapas(List<PlaneamentoEstudoEtapa> etapas) {
        if (stepsBox == null) {
            return;
        }

        ArrayList<Node> nodes = new ArrayList<>();
        if (etapas != null) {
            for (int i = 0; i < Math.min(3, etapas.size()); i++) {
                PlaneamentoEstudoEtapa etapa = etapas.get(i);
                if (etapa == null) {
                    continue;
                }
                nodes.add(new TimelineStep(
                    safeText(etapa.janela(), "Hoje"),
                    safeText(etapa.acao(), "Bloco") + " " + safeText(etapa.detalhe(), "")
                ));
            }
        }

        if (nodes.isEmpty()) {
            nodes.add(criarEstadoSecaoVazio(
                "Sem blocos definidos",
                "O plano vai ganhar etapas assim que houver dados de diagnostico."
            ));
        }

        stepsBox.getChildren().setAll(nodes);
    }

    private void preencherDisciplinas(List<PlaneamentoEstudoDisciplina> disciplinas) {
        if (disciplinesBox == null) {
            return;
        }

        ArrayList<Node> nodes = new ArrayList<>();
        if (disciplinas != null) {
            for (int i = 0; i < Math.min(3, disciplinas.size()); i++) {
                PlaneamentoEstudoDisciplina disciplina = disciplinas.get(i);
                if (disciplina == null) {
                    continue;
                }
                nodes.add(criarCardDisciplina(i + 1, disciplina));
            }
        }

        if (nodes.isEmpty()) {
            nodes.add(criarEstadoSecaoVazio(
                "Sem disciplinas em foco",
                "Assim que o diagnostico criar base suficiente, vamos priorizar as areas certas."
            ));
        }

        disciplinesBox.getChildren().setAll(nodes);
    }

    private VBox criarCardDisciplina(int ordem, PlaneamentoEstudoDisciplina disciplina) {
        Label ordemLabel = new Label(String.valueOf(ordem));
        ordemLabel.getStyleClass().add("info-badge");

        Label tituloLabel = new Label(safeText(disciplina.disciplina(), "Disciplina"));
        tituloLabel.getStyleClass().add("h3-thin-big");

        Label focoLabel = new Label("Foco: " + safeText(disciplina.foco(), "Sem foco"));
        focoLabel.getStyleClass().add("muted");
        focoLabel.setWrapText(true);

        VBox tituloBox = new VBox(2, tituloLabel, focoLabel);
        HBox topo = new HBox(10, ordemLabel, tituloBox);
        topo.setAlignment(Pos.CENTER_LEFT);

        HBox chips = new HBox(6,
            criarChip("Pontuacao " + formatPercent(disciplina.pontuacao())),
            criarChip("Precisao " + formatPercent(disciplina.precisao())),
            criarChip("Dias " + Math.max(0, disciplina.diasSemEstudo()))
        );
        chips.setAlignment(Pos.CENTER_LEFT);
        chips.setFillHeight(true);

        Label observacaoLabel = new Label(safeText(disciplina.observacao(), "Sem observacao"));
        observacaoLabel.getStyleClass().add("muted");
        observacaoLabel.setWrapText(true);

        VBox card = new VBox(8, topo, chips, observacaoLabel);
        card.getStyleClass().addAll("weekly-mini-card", "report-note-card");
        card.setPadding(new Insets(12));
        card.setMaxWidth(Double.MAX_VALUE);
        return card;
    }

    private Label criarChip(String texto) {
        Label chip = new Label(texto);
        chip.getStyleClass().add("profile-metric-chip");
        chip.setMaxWidth(Double.MAX_VALUE);
        return chip;
    }

    private VBox criarEstadoSecaoVazio(String tituloTexto, String detalheTexto) {
        Label titulo = new Label(tituloTexto);
        titulo.getStyleClass().add("h3-thin-big");

        Label detalhe = new Label(detalheTexto);
        detalhe.getStyleClass().add("muted");
        detalhe.setWrapText(true);

        VBox box = new VBox(6, titulo, detalhe);
        box.getStyleClass().add("report-note-card");
        box.setPadding(new Insets(12));
        return box;
    }

    private void definirVisibilidade(Node node, boolean visivel) {
        if (node == null) {
            return;
        }
        node.setVisible(visivel);
        node.setManaged(visivel);
    }

    private double limitarPercentual(double valor) {
        return Math.max(0d, Math.min(100d, valor));
    }

    private String safeText(String value, String fallback) {
        return TextoUtil.safeText(value, fallback);
    }

    private String formatPercent(double valor) {
        return Math.round(limitarPercentual(valor)) + "%";
    }

    @FXML
    private void abrirDiagnostico(ActionEvent event) {
        StackPane contentHost = planPane == null || planPane.getScene() == null
            ? null
            : (StackPane) planPane.getScene().lookup("#contentHost");

        if (contentHost != null) {
            App.swapContent(contentHost, "views/pages/candidato/diagnostico");
        }
    }

    @Override
    public void dispose() {
        // Sem listeners ou recursos persistentes para libertar.
    }
}
