package com.imetro.ui.components;

import java.time.LocalDate;
import java.time.LocalTime;

import com.imetro.domain.dto.diagnostico.TimelineDTO;
import com.imetro.domain.dto.stats.Stats;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class TimelineCard {

    private VBox root;
    private final VBox[] expandedContent;
    private final boolean[] isExpanded;
    private final TimelineDTO timelineData;

    public TimelineCard(TimelineDTO time) {
        this.timelineData = time;
        int totalSessoes = time == null || time.hora() == null ? 0 : time.hora().size();
        this.expandedContent = new VBox[totalSessoes];
        this.isExpanded = new boolean[totalSessoes];
        construirCard();
    }

    private void construirCard() {
        root = new VBox();
        root.setSpacing(10.0);
        root.getStyleClass().add("card-container");

        HBox headerData = criarHeader(
            formatarData(timelineData.data()),
            11.0,
            "DODGERBLUE"
        );

        root.getChildren().add(headerData);

        for (int i = 0; i < timelineData.hora().size(); i++) {
            VBox mainContainer = new VBox();
            mainContainer.setPadding(new Insets(0, 0, 0, 40.0));

            HBox headerHora = criarHeader(
                formatarHora(timelineData.hora().get(i)),
                8.0,
                "#215eff"
            );

            VBox card = new VBox();
            card.getStyleClass().add("card");
            card.setPadding(new Insets(20.0));
            card.setMaxWidth(759.0);
            VBox.setMargin(card, new Insets(10, 0, 10, 40));

            HBox cardHeader = criarCardHeader(
                timelineData.disciplina()[i],
                timelineData.acertos().get(i),
                timelineData.erros().get(i),
                timelineData.evolucao().get(i),
                timelineData.duracao()[i],
                i
            );

            expandedContent[i] = criarConteudoExpansivel(timelineData.percent().get(i));
            expandedContent[i].setVisible(false);
            expandedContent[i].setManaged(false);

            card.getChildren().addAll(cardHeader, expandedContent[i]);
            mainContainer.getChildren().addAll(headerHora, card);
            root.getChildren().add(mainContainer);
        }
    }

    private String formatarData(LocalDate data) {
        if (data == null) {
            return "";
        }
        String[] meses = {
            "Janeiro", "Fevereiro", "Marco", "Abril", "Maio", "Junho",
            "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
        };
        return data.getDayOfMonth() + " de " + meses[data.getMonthValue() - 1] + " de " + data.getYear();
    }

    private String formatarHora(LocalTime hora) {
        if (hora == null) {
            return "";
        }
        return String.format("%02d:%02d", hora.getHour(), hora.getMinute());
    }

    private HBox criarHeader(String texto, double raio, String cor) {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setSpacing(10.0);

        Circle circle = new Circle();
        circle.setRadius(raio);
        circle.setFill(javafx.scene.paint.Color.web(cor));
        circle.setStroke(javafx.scene.paint.Color.web("#0033ff"));
        circle.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);

        Label label = new Label(texto);
        label.getStyleClass().add("h2-thin");

        hbox.getChildren().addAll(circle, label);
        return hbox;
    }

    private HBox criarCardHeader(String disciplina, float acertos, float erros, float evolucao, String duracao, int indice) {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(10);

        Label materia = new Label(disciplina);
        materia.getStyleClass().add("h3-thin-big");
        materia.setPrefWidth(200.0);

        HBox stats = new HBox();
        stats.setSpacing(20.0);
        stats.getChildren().addAll(
            criarLabelStats(String.format("Evolucao: %.0f%%", evolucao)),
            criarLabelStats(String.format("ERROS: %.0f", erros)),
            criarLabelStats(String.format("ACERTOS: %.0f", acertos)),
            criarLabelStats("DURACAO: " + duracao)
        );

        Button arrowButton = new Button("\u25BC");
        arrowButton.getStyleClass().add("arrow-button");
        arrowButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 16px;");
        arrowButton.setOnAction(e -> toggleExpand(indice, arrowButton));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(materia, stats, spacer, arrowButton);
        return header;
    }

    private Label criarLabelStats(String texto) {
        Label label = new Label(texto);
        label.getStyleClass().add("h3-thin");
        return label;
    }

    private VBox criarConteudoExpansivel(Stats stats) {
        VBox content = new VBox();
        content.setSpacing(15.0);
        content.setPadding(new Insets(20.0, 0, 0, 0));

        HBox row1 = new HBox();
        row1.setSpacing(20.0);
        row1.getChildren().addAll(
            criarSkillCompleta("Velocidade", stats.velocidade()),
            criarSkillCompleta("Logica", stats.logica()),
            criarSkillCompleta("Consistencia", stats.consistencia())
        );

        HBox row2 = new HBox();
        row2.setSpacing(20.0);
        row2.getChildren().addAll(
            criarSkillCompleta("Resiliencia", stats.resiliencia()),
            criarSkillCompleta("Precisao", stats.precisao())
        );

        content.getChildren().addAll(row1, row2);
        return content;
    }

    private VBox criarSkillCompleta(String nome, double progresso) {
        VBox vbox = new VBox();
        vbox.setSpacing(8.0);
        vbox.setPrefWidth(200.0);

        Label label = new Label(nome);
        label.getStyleClass().add("h3-thin");

        ProgressBar bar = new ProgressBar();
        bar.setPrefWidth(200.0);
        bar.setProgress(progresso);

        Label percent = new Label(String.format("%.0f%%", progresso * 100));
        percent.getStyleClass().add("percent-label");

        vbox.getChildren().addAll(label, bar, percent);
        return vbox;
    }

    private void toggleExpand(int indice, Button arrowButton) {
        if (indice < 0 || indice >= expandedContent.length) {
            return;
        }

        isExpanded[indice] = !isExpanded[indice];
        arrowButton.setRotate(isExpanded[indice] ? 180 : 0);

        if (isExpanded[indice]) {
            expandedContent[indice].setVisible(true);
            expandedContent[indice].setManaged(true);
            expandedContent[indice].setOpacity(0);

            javafx.animation.Timeline timeline = new javafx.animation.Timeline();
            javafx.animation.KeyValue kv = new javafx.animation.KeyValue(
                expandedContent[indice].opacityProperty(), 1
            );
            javafx.animation.KeyFrame kf = new javafx.animation.KeyFrame(
                javafx.util.Duration.millis(300), kv
            );
            timeline.getKeyFrames().add(kf);
            timeline.play();
        } else {
            javafx.animation.Timeline timeline = new javafx.animation.Timeline();
            javafx.animation.KeyValue kv = new javafx.animation.KeyValue(
                expandedContent[indice].opacityProperty(), 0
            );
            javafx.animation.KeyFrame kf = new javafx.animation.KeyFrame(
                javafx.util.Duration.millis(200),
                e -> {
                    expandedContent[indice].setVisible(false);
                    expandedContent[indice].setManaged(false);
                },
                kv
            );
            timeline.getKeyFrames().add(kf);
            timeline.play();
        }
    }

    public VBox getRoot() {
        return root;
    }
}
