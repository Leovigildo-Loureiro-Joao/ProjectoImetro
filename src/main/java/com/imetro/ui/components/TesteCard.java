package com.imetro.ui.components;

import java.util.List;

import org.kordamp.ikonli.javafx.FontIcon;

import com.imetro.domain.dto.test.Percent;
import com.imetro.domain.dto.test.TesteDto;
import com.jfoenix.controls.JFXButton;

import javafx.animation.RotateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;

public class TesteCard extends VBox {

    private final VBox expandedContent = new VBox(16);
    private boolean expanded = false;

    public TesteCard(
            TesteDto teste,
            boolean inteligenteDisponivel,
            Runnable onPadrao,
            Runnable onInteligente
    ) {

        getStyleClass().addAll("card", "teste-card");

        setSpacing(14);
        setPadding(new Insets(18));

        // =====================================================
        // HEADER
        // =====================================================

        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);

        StackPane iconBox = new StackPane();
        iconBox.getStyleClass().add("teste-icon-box");
        iconBox.setPrefSize(48, 48);

        FontIcon icon = new FontIcon("fas-book");
        icon.getStyleClass().add("teste-icon");

        iconBox.getChildren().add(icon);

        VBox titleBox = new VBox(4);

        Label disciplina = new Label(teste.disciplina());
        disciplina.getStyleClass().add("teste-title");

        Label resumo = new Label(
                teste.totalSubtopicos() +
                        " testes • " +
                        teste.totalQuestoes() +
                        " questões"
        );
        resumo.getStyleClass().add("teste-subtitle");

        titleBox.getChildren().addAll(disciplina, resumo);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label percent = new Label(formatPercent(teste.percent()));
        percent.getStyleClass().add("teste-percent");

        FontIcon arrow = new FontIcon("fas-chevron-down");
        arrow.getStyleClass().add("teste-arrow");

        header.getChildren().addAll(
                iconBox,
                titleBox,
                spacer,
                percent,
                arrow
        );

        // =====================================================
        // QUICK STATS
        // =====================================================

        HBox stats = new HBox(12);

        stats.getChildren().addAll(
                statCard(
                        "Acerto Médio",
                        formatPercent(teste.ritmoEvolutivo())
                ),
                statCard(
                        "Precisão",
                        formatPercent(teste.errosComuns())
                ),
                statCard(
                        "Cobertura",
                        formatPercent(teste.melhoria())
                )
        );

        // =====================================================
        // EXPANDED CONTENT
        // =====================================================

        VBox metricas = buildMetricas(teste);

        VBox topicos = buildTopicos(teste.topicos());

        HBox actions = buildActions(
                inteligenteDisponivel,
                onPadrao,
                onInteligente
        );

        expandedContent.getChildren().addAll(
                metricas,
                topicos,
                actions
        );

        expandedContent.setVisible(false);
        expandedContent.setManaged(false);

        header.setOnMouseClicked(e ->
                toggle(expandedContent, arrow));

        getChildren().addAll(
                header,
                stats,
                expandedContent
        );
    }

    private VBox buildMetricas(TesteDto t) {

        VBox box = new VBox(12);

        box.getChildren().addAll(

                metricRow(
                        "Acerto médio",
                        t.ritmoEvolutivo()
                ),

                metricRow(
                        "Precisão média",
                        t.errosComuns()
                ),

                metricRow(
                        "Cobertura curricular",
                        t.melhoria()
                )
        );

        box.getStyleClass().add("teste-metrics");

        return box;
    }

    private VBox metricRow(
            String title,
            float value
    ) {

        Label left = new Label(title);

        Label right = new Label(
                formatPercent(value)
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox top = new HBox(
                left,
                spacer,
                right
        );

        ProgressBar bar =
                new ProgressBar(limit(value));

        bar.setMaxWidth(Double.MAX_VALUE);

        VBox box = new VBox(
                6,
                top,
                bar
        );

        box.getStyleClass().add("teste-metric-row");

        return box;
    }

    private VBox buildTopicos(
            List<Percent> topicos
    ) {

        Label title = new Label("Focos sugeridos");
        title.getStyleClass().add("teste-section-title");

        FlowPane flow = new FlowPane();
        flow.setHgap(8);
        flow.setVgap(8);

        topicos.stream()
                .limit(8)
                .forEach(t -> {

                    Label chip =
                            new Label(
                                    t.topico() +
                                    " • " +
                                    Math.round(
                                            t.evolucao() * 100
                                    ) + "%"
                            );

                    chip.getStyleClass()
                            .add("teste-chip");

                    flow.getChildren()
                            .add(chip);
                });

        VBox box =
                new VBox(8, title, flow);

        box.getStyleClass()
                .add("teste-section");

        return box;
    }

    private HBox buildActions(
            boolean intel,
            Runnable padrao,
            Runnable inteligente
    ) {

        JFXButton continuar =
                new JFXButton(
                        "Continuar Plano"
                );

        continuar.getStyleClass()
                .add("teste-primary-btn");

        continuar.setOnAction(
                e -> padrao.run()
        );

        JFXButton inteligenteBtn =
                new JFXButton(
                        "Foco Inteligente"
                );

        inteligenteBtn.getStyleClass()
                .add("teste-secondary-btn");

        inteligenteBtn.setDisable(!intel);

        inteligenteBtn.setOnAction(
                e -> inteligente.run()
        );

        HBox box =
                new HBox(
                        10,
                        continuar,
                        inteligenteBtn
                );

        return box;
    }

    private VBox statCard(
            String title,
            String value
    ) {

        Label t = new Label(title);
        t.getStyleClass()
                .add("teste-stat-title");

        Label v = new Label(value);
        v.getStyleClass()
                .add("teste-stat-value");

        VBox box =
                new VBox(4, t, v);

        box.getStyleClass()
                .add("teste-stat-card");

        return box;
    }

    private void toggle(
            VBox content,
            FontIcon arrow
    ) {

        expanded = !expanded;

        content.setVisible(expanded);
        content.setManaged(expanded);

        RotateTransition rt =
                new RotateTransition(
                        Duration.millis(200),
                        arrow
                );

        rt.setToAngle(
                expanded ? 180 : 0
        );

        rt.play();
    }

    private String formatPercent(float v) {
        return Math.round(limit(v) * 100) + "%";
    }

    private float limit(float v) {
        return Math.max(
                0f,
                Math.min(1f, v)
        );
    }
}