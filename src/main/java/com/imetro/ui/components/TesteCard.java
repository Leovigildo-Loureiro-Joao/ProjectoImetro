package com.imetro.ui.components;

import java.util.List;

import com.imetro.domain.dto.test.Percent;
import com.imetro.domain.dto.test.TesteDto;
import com.jfoenix.controls.JFXButton;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class TesteCard extends VBox {

    private final ProgressBar acertoBar = new ProgressBar();
    private final ProgressBar precisaoBar = new ProgressBar();
    private final ProgressBar coberturaBar = new ProgressBar();
    private final FlowPane topicoBadge;

    public TesteCard(
        TesteDto teste,
        boolean inteligenteDisponivel,
        Runnable onPadrao,
        Runnable onInteligente
    ) {
        getStyleClass().addAll("card", "teste-card");
        setPadding(new Insets(20));
        setSpacing(18);
        setFillWidth(true);
        setMaxWidth(Double.MAX_VALUE);

        Label disciplina = new Label(teste.disciplina());
        disciplina.getStyleClass().add("teste-card-title");

        Label subtitulo = new Label(construirSubtitulo(teste));
        subtitulo.getStyleClass().add("teste-card-subtitle");
        subtitulo.setWrapText(true);

        VBox tituloBox = new VBox(4, disciplina, subtitulo);
        tituloBox.setAlignment(Pos.CENTER_LEFT);

        HBox badges = new HBox(
            8,
            criarBadge(teste.totalSubtopicos() + " testes"),
            criarBadge(teste.totalQuestoes() + " questoes respondidas"),
            criarBadge(teste.topicos().size() + " topicos testados")
        );
        badges.setAlignment(Pos.CENTER_RIGHT);

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        HBox header = new HBox(16, tituloBox, headerSpacer, badges);
        header.setAlignment(Pos.CENTER_LEFT);

        CircleProgress progresso = new CircleProgress(58, 58);
        progresso.setValue(limitar(teste.percent()));
        VBox hero = criarHero(teste, progresso);

        VBox indicadores = new VBox(
            12,
            indicador(
                "Acerto medio",
                teste.ritmoEvolutivo(),
                acertoBar
            ),
            indicador(
                "Precisao media",
                teste.errosComuns(),
                precisaoBar
            ),
            indicador(
                "Cobertura de topicos",
                teste.melhoria(),
                coberturaBar
            )
        );
        indicadores.getStyleClass().add("teste-card-surface");
        indicadores.setPadding(new Insets(16));
        indicadores.setPrefWidth(300);
        indicadores.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(indicadores, Priority.ALWAYS);

        JFXButton buttonPadrao = new JFXButton("Entrar com foco padrao");
        buttonPadrao.getStyleClass().add("btn-primary-two");
        buttonPadrao.setMaxWidth(Double.MAX_VALUE);
        buttonPadrao.setOnAction(event -> onPadrao.run());

        JFXButton buttonInteligente = new JFXButton("Abrir foco inteligente");
        buttonInteligente.getStyleClass().add("btn-primary");
        buttonInteligente.setMaxWidth(Double.MAX_VALUE);
        buttonInteligente.setDisable(!inteligenteDisponivel);
        buttonInteligente.setOnAction(event -> onInteligente.run());

        Label actionTitle = new Label("Iniciar teste");
        actionTitle.getStyleClass().add("teste-card-section-title");

        VBox acoes = new VBox(12, actionTitle, buttonPadrao, buttonInteligente);
        acoes.getStyleClass().add("teste-card-surface");
        acoes.setPadding(new Insets(16));
        acoes.setPrefWidth(260);

        HBox resumo = new HBox(16, hero, indicadores, acoes);
        resumo.setAlignment(Pos.TOP_LEFT);

        Label topicosTitulo = new Label("Topicos ja testados");
        topicosTitulo.getStyleClass().add("teste-card-section-title");

        Label topicosSubtitulo = new Label(
            "Mostramos apenas os topicos que ja entraram nos teus testes e a evolucao de acerto."
        );
        topicosSubtitulo.getStyleClass().add("teste-card-section-subtitle");
        topicosSubtitulo.setWrapText(true);

        topicoBadge = new FlowPane(8, 8);
        topicoBadge.getStyleClass().add("teste-card-topics-wrap");
        topicoBadge.setPrefWrapLength(460);
        adicionarTopicos(teste.topicos());

        VBox topicosBox = new VBox(10, topicosTitulo, topicosSubtitulo, topicoBadge);
        topicosBox.getStyleClass().add("teste-card-surface");
        topicosBox.setPadding(new Insets(16));
        topicosBox.setMaxWidth(Double.MAX_VALUE);
        getChildren().addAll(header, resumo, topicosBox);
    }

    private VBox criarHero(TesteDto teste, CircleProgress progresso) {
        Label titulo = new Label("Evolucao geral");
        titulo.getStyleClass().add("teste-card-hero-title");

        Label valor = new Label(formatPercent(teste.percent()));
        valor.getStyleClass().add("teste-card-hero-value");

        Label descricao = new Label("Resumo baseado no historico ja realizado nesta disciplina.");
        descricao.getStyleClass().add("teste-card-hero-copy");
        descricao.setWrapText(true);

        VBox hero = new VBox(12, titulo, progresso, valor, descricao);
        hero.getStyleClass().add("teste-card-hero");
        hero.setPadding(new Insets(16));
        hero.setAlignment(Pos.TOP_CENTER);
        hero.setPrefWidth(220);
        return hero;
    }

    private Label criarBadge(String texto) {
        Label badge = new Label(texto);
        badge.getStyleClass().add("teste-card-badge");
        return badge;
    }

    private VBox indicador(String titulo, float valor, ProgressBar barra) {
        Label tituloLabel = new Label(titulo);
        tituloLabel.getStyleClass().add("teste-card-metric-title");

        Label valorLabel = new Label(formatPercent(valor));
        valorLabel.getStyleClass().add("teste-card-metric-value");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topo = new HBox(10, tituloLabel, spacer, valorLabel);
        topo.setAlignment(Pos.CENTER_LEFT);

        barra.setProgress(limitar(valor));
        barra.setMaxWidth(Double.MAX_VALUE);

        VBox box = new VBox(6, topo, barra);
        box.getStyleClass().add("teste-card-metric");
        return box;
    }

    private void adicionarTopicos(List<Percent> topicos) {
        topicoBadge.getChildren().clear();

        if (topicos == null || topicos.isEmpty()) {
            Label vazio = new Label("Sem topicos testados nesta disciplina ainda.");
            vazio.getStyleClass().add("teste-card-section-subtitle");
            vazio.setWrapText(true);
            topicoBadge.getChildren().add(vazio);
            return;
        }

        int limite = Math.min(4, topicos.size());
        for (int i = 0; i < limite; i++) {
            Percent topico = topicos.get(i);
            Label label = new Label(topico.topico() + "  " + formatPercentualTopico(topico.evolucao()));
            label.getStyleClass().add("teste-card-topic");
            topicoBadge.getChildren().add(label);
        }

        if (topicos.size() > limite) {
            Label extra = new Label("+" + (topicos.size() - limite) + " focos");
            extra.getStyleClass().add("teste-card-topic");
            topicoBadge.getChildren().add(extra);
        }
    }

    private String construirSubtitulo(TesteDto teste) {
        if (teste.totalSubtopicos() <= 0) {
            return "Ainda sem historico de teste nesta disciplina.";
        }
        return teste.totalSubtopicos() + " testes concluidos nesta disciplina.";
    }

    private String formatPercent(float valor) {
        return Math.round(limitar(valor) * 100f) + "%";
    }

    private String formatPercentualTopico(float valor) {
        return Math.round(Math.max(0f, Math.min(100f, valor))) + "%";
    }

    private float limitar(float valor) {
        return Math.max(0f, Math.min(1f, valor));
    }
}
