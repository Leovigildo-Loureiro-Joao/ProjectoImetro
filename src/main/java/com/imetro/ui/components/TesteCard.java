package com.imetro.ui.components;

import java.util.Comparator;
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

    private final ProgressBar coberturaBar = new ProgressBar();
    private final ProgressBar desafioBar = new ProgressBar();
    private final ProgressBar baseBar = new ProgressBar();
    private final VBox passos;
    private final FlowPane topicoBadge;

    public TesteCard(TesteDto teste, Runnable onPadrao, Runnable onInteligente) {
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
            criarBadge(teste.totalQuestoes() + " questoes reais"),
            criarBadge(teste.topicos().size() + " topicos"),
            criarBadge(teste.totalSubtopicos() + " subtopicos")
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
                "Subtopicos cobertos",
                teste.melhoria(),
                coberturaBar,
                teste.totalSubtopicos() > 0
                    ? teste.totalSubtopicos() + " subtopicos mapeados para foco inteligente."
                    : "Ainda sem subtopicos suficientes para orientar o foco."
            ),
            indicador(
                "Mix desafiante",
                teste.errosComuns(),
                desafioBar,
                "Mostra quanto do banco ja puxa dificuldade media/alta."
            ),
            indicador(
                "Base introdutoria",
                teste.ritmoEvolutivo(),
                baseBar,
                "Revela quanto do banco ainda permite uma entrada mais leve."
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
        buttonInteligente.setDisable(teste.topicos() == null || teste.topicos().isEmpty());
        buttonInteligente.setOnAction(event -> onInteligente.run());

        Label actionTitle = new Label("Como quer entrar");
        actionTitle.getStyleClass().add("teste-card-section-title");

        Label actionNote = new Label(
            "Padrao usa a disciplina inteira. Inteligente abre a selecao de topicos e subtopicos antes de comecar."
        );
        actionNote.getStyleClass().add("teste-card-action-note");
        actionNote.setWrapText(true);

        VBox acoes = new VBox(12, actionTitle, actionNote, buttonPadrao, buttonInteligente);
        acoes.getStyleClass().add("teste-card-surface");
        acoes.setPadding(new Insets(16));
        acoes.setPrefWidth(260);

        HBox resumo = new HBox(16, hero, indicadores, acoes);
        resumo.setAlignment(Pos.TOP_LEFT);

        Label topicosTitulo = new Label("Topicos que mais conseguem puxar este teste");
        topicosTitulo.getStyleClass().add("teste-card-section-title");

        Label topicosSubtitulo = new Label(
            "Estes focos sao montados a partir das questoes reais ja disponiveis para a disciplina."
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
        HBox.setHgrow(topicosBox, Priority.ALWAYS);

        Label passosTitulo = new Label("Leitura rapida do fluxo");
        passosTitulo.getStyleClass().add("teste-card-section-title");

        Label passosSubtitulo = new Label("O sistema ja consegue fazer isto agora, sem prometer mais do que entrega.");
        passosSubtitulo.getStyleClass().add("teste-card-section-subtitle");
        passosSubtitulo.setWrapText(true);

        passos = new VBox(10);
        popularPassos(teste.Passos());

        VBox passosBox = new VBox(10, passosTitulo, passosSubtitulo, passos);
        passosBox.getStyleClass().add("teste-card-surface");
        passosBox.setPadding(new Insets(16));
        passosBox.setPrefWidth(330);

        HBox detalhe = new HBox(16, topicosBox, passosBox);
        detalhe.setAlignment(Pos.TOP_LEFT);

        getChildren().addAll(header, resumo, detalhe);
    }

    private VBox criarHero(TesteDto teste, CircleProgress progresso) {
        Label titulo = new Label("Variedade do banco");
        titulo.getStyleClass().add("teste-card-hero-title");

        Label valor = new Label(formatPercent(teste.percent()));
        valor.getStyleClass().add("teste-card-hero-value");

        Label descricao = new Label(
            "Quanto mais variedade, mais caminhos o teste adaptativo tem para ajustar o percurso dentro desta disciplina."
        );
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

    private VBox indicador(String titulo, float valor, ProgressBar barra, String legenda) {
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

        Label legendaLabel = new Label(legenda);
        legendaLabel.getStyleClass().add("teste-card-metric-caption");
        legendaLabel.setWrapText(true);

        VBox box = new VBox(6, topo, barra, legendaLabel);
        box.getStyleClass().add("teste-card-metric");
        return box;
    }

    private void adicionarTopicos(List<Percent> topicos) {
        topicoBadge.getChildren().clear();

        if (topicos == null || topicos.isEmpty()) {
            Label vazio = new Label("Os topicos aparecem aqui assim que a disciplina tiver focos suficientes.");
            vazio.getStyleClass().add("teste-card-section-subtitle");
            vazio.setWrapText(true);
            topicoBadge.getChildren().add(vazio);
            return;
        }

        List<Percent> ordenados = topicos.stream()
            .sorted(Comparator.comparing(Percent::evolucao).reversed())
            .toList();

        int limite = Math.min(6, ordenados.size());
        for (int i = 0; i < limite; i++) {
            Percent topico = ordenados.get(i);
            Label label = new Label(topico.topico() + "  " + Math.round(topico.evolucao()) + "%");
            label.getStyleClass().add("teste-card-topic");
            topicoBadge.getChildren().add(label);
        }

        if (ordenados.size() > limite) {
            Label extra = new Label("+" + (ordenados.size() - limite) + " focos");
            extra.getStyleClass().add("teste-card-topic");
            topicoBadge.getChildren().add(extra);
        }
    }

    private void popularPassos(List<String> itens) {
        passos.getChildren().clear();

        List<String> conteudo = (itens == null || itens.isEmpty())
            ? List.of("Abra o modo inteligente para definir o foco antes do teste.")
            : itens.stream().limit(4).toList();

        int indice = 1;
        for (String item : conteudo) {
            Label numero = new Label(String.valueOf(indice++));
            numero.getStyleClass().add("teste-card-step-index");

            Label texto = new Label(item);
            texto.getStyleClass().add("teste-card-step-text");
            texto.setWrapText(true);

            HBox linha = new HBox(10, numero, texto);
            linha.getStyleClass().add("teste-card-step");
            linha.setAlignment(Pos.TOP_LEFT);
            passos.getChildren().add(linha);
        }
    }

    private String construirSubtitulo(TesteDto teste) {
        return "Banco real pronto para teste adaptativo, com "
            + teste.totalQuestoes()
            + " questoes distribuidas por "
            + teste.topicos().size()
            + " topicos.";
    }

    private String formatPercent(float valor) {
        return Math.round(limitar(valor) * 100f) + "%";
    }

    private float limitar(float valor) {
        return Math.max(0f, Math.min(1f, valor));
    }
}
