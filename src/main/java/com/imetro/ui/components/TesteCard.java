package com.imetro.ui.components;

import java.util.List;

import com.imetro.domain.dto.test.TesteDto;
import com.jfoenix.controls.JFXButton;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class TesteCard extends FlowPane {

    private final ProgressBar coberturaBar = new ProgressBar();
    private final ProgressBar desafioBar = new ProgressBar();
    private final ProgressBar baseBar = new ProgressBar();
    private final VBox passos;
    private final FlowPane topicoBadge;

    public TesteCard(TesteDto teste, Runnable onPadrao, Runnable onInteligente) {
        CircleProgress progresso = new CircleProgress(75, 75);
        progresso.setValue(teste.percent());
        StackPane circulo = new StackPane(progresso);
        circulo.setPrefSize(200, 200);

        JFXButton buttonPadrao = new JFXButton("Usar foco padrao");
        buttonPadrao.setPrefSize(199, 39);
        buttonPadrao.setOnAction(event -> onPadrao.run());
        buttonPadrao.getStyleClass().add("btn-primary-two");
        buttonPadrao.setTranslateY(35);

        Label disciplina = new Label(teste.disciplina());
        disciplina.getStyleClass().add("h2-thin");
        disciplina.setPadding(new Insets(10));

        VBox resumo = new VBox(disciplina, circulo);
        resumo.setPrefSize(200, 200);
        resumo.setAlignment(Pos.TOP_CENTER);

        VBox indicadores = new VBox(
            10,
            indicador("Cobertura do foco", teste.melhoria()),
            indicador("Nivel de desafio", teste.errosComuns()),
            indicador("Base do percurso", teste.ritmoEvolutivo()),
            buttonPadrao
        );
        indicadores.setPadding(new Insets(10));
        indicadores.setPrefHeight(139);

        topicoBadge = new FlowPane(10, 10);
        topicoBadge.setPadding(new Insets(10, 0, 10, 0));
        Label title = new Label("Topicos que podem guiar o teste");
        title.getStyleClass().add("h3-thin-big");
        ScrollPane pane = new ScrollPane(topicoBadge);
        pane.setFitToHeight(true);
        pane.setFitToWidth(true);
        pane.setHbarPolicy(ScrollBarPolicy.NEVER);
        pane.setPrefSize(401, 121);

        JFXButton buttonInteligente = new JFXButton("Configurar foco inteligente");
        buttonInteligente.setPrefSize(230, 39);
        buttonInteligente.setOnAction(event -> onInteligente.run());
        buttonInteligente.getStyleClass().add("btn-primary");
        VBox foco = new VBox(10, title, pane, buttonInteligente);
        foco.setPadding(new Insets(10));
        foco.setPrefSize(369, 200);

        Label title2 = new Label("Proximos passos");
        title2.getStyleClass().add("h3-thin-big");
        passos = new VBox(10);
        popularPassos(teste.Passos());
        VBox proximosPassos = new VBox(10, title2, passos);
        proximosPassos.setPadding(new Insets(10));
        proximosPassos.setPrefSize(200, 200);

        setPadding(new Insets(10));
        setVgap(10);
        setHgap(10);
        getChildren().addAll(resumo, indicadores, foco, proximosPassos);
        getStyleClass().add("card");

        adicionarTopicos(teste.topicos());
    }

    private void adicionarTopicos(List<com.imetro.domain.dto.test.Percent> topicos) {
        for (com.imetro.domain.dto.test.Percent topico : topicos) {
            Label label = new Label(topico.topico());
            Label percent = new Label(Math.round(topico.evolucao()) + "%");
            label.getStyleClass().add("h3-thin");
            percent.getStyleClass().add("percent-value");
            HBox topicBox = new HBox(label, percent);
            topicBox.getStyleClass().add("badge-test");
            topicoBadge.getChildren().add(topicBox);
        }
    }

    private VBox indicador(String text, float value) {
        Label label = new Label(text);
        label.getStyleClass().add("h3-thin");
        VBox container = new VBox(5, label);

        ProgressBar barra = switch (text) {
            case "Cobertura do foco" -> coberturaBar;
            case "Nivel de desafio" -> desafioBar;
            default -> baseBar;
        };

        barra.setPrefWidth(200);
        barra.setProgress(value);
        container.getChildren().add(barra);
        return container;
    }

    private void popularPassos(List<String> itens) {
        passos.getChildren().clear();

        List<String> conteudo = (itens == null || itens.isEmpty())
            ? List.of("Abra o modo inteligente para definir subtopicos.")
            : itens;

        for (String item : conteudo) {
            Label label = new Label(item);
            label.getStyleClass().add("h3-thin");
            label.setWrapText(true);
            passos.getChildren().add(label);
        }
    }
}
