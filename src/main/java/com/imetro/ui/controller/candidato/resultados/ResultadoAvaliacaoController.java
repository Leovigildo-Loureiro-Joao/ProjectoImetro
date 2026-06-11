package com.imetro.ui.controller.candidato.resultados;

import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.remixicon.RemixiconMZ;

import com.imetro.App;
import com.imetro.domain.dto.MenuEntry;
import com.imetro.ui.components.Item_Cell;
import com.imetro.ui.controller.lifecycle.DisposableController;
import com.imetro.util.ResultadoPayload;
import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;



public class ResultadoAvaliacaoController implements DisposableController,ResultadoCoordinator.ResultadoHost{

    public static ResultadoPayload ultimoResultado;



    public static void setResultado(ResultadoPayload payload) {
        ultimoResultado = payload;
    }

    @FXML
    private JFXButton btnRefazer;

    @FXML
    private ListView<MenuEntry> sublist;

    @FXML
    private StackPane swcDiagnos;

    @FXML
    private Label tituloAvaliacao;

    @FXML
    private void initialize() {
        ResultadoPayload payload = ultimoResultado;

        sublist.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(MenuEntry item, boolean empty) {
                super.updateItem(item, empty);
                setText(null);
                setGraphic(empty || item == null ? null : new Item_Cell(item.title(), item.icon()));
            }
        });

        sublist.getItems().setAll(
            new MenuEntry("statics", "Estatisticas", FontAwesomeSolid.DATABASE),
            new MenuEntry("revisao", "Revisão das respostas", RemixiconMZ.QUESTION_ANSWER_FILL),
            new MenuEntry("recomendacao", "Recomendação", FontAwesomeSolid.INFO_CIRCLE)
        );

        sublist.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                navigate(newValue.key());
            }
        });
        System.out.println("Cheguei");
        sublist.getSelectionModel().selectFirst();
        System.out.println("Passou");


        tituloAvaliacao.setText("Resultado - " + payload.getTipoAvaliacao());

        btnRefazer.setUserData(payload.getRetryPath());


    }

    @FXML
    private void abrirDashboard() {
        navegarPara("views/pages/candidato/dashboard");
    }

    @FXML
    private void refazerAvaliacao() {
        Object path = btnRefazer.getUserData();
        if (path instanceof String route && !route.isBlank()) {
            navegarPara(route);
            return;
        }
        navegarPara("views/pages/candidato/dashboard");
    }
/*


    @FXML
    private void togglePainelRecomendacao() {
        /*if (painelRecomendacao == null || btnToggleRecomendacao == null || btnToggleRecomendacao.isDisable()) {
            return;
        }

        painelRecomendacaoVisivel = !painelRecomendacaoVisivel;
        painelRecomendacao.setVisible(painelRecomendacaoVisivel);
        painelRecomendacao.setManaged(painelRecomendacaoVisivel);
        btnToggleRecomendacao.setText(painelRecomendacaoVisivel ? "Ocultar recomendacao" : "Ver recomendacao");

        if (painelRecomendacaoVisivel && recommendationAccordion != null && !recommendationAccordion.getPanes().isEmpty()) {
            recommendationAccordion.setExpandedPane(recommendationAccordion.getPanes().get(0));
        }
    }*/

    private void navegarPara(String fxmlPath) {
        if (tituloAvaliacao == null || tituloAvaliacao.getScene() == null) {
            return;
        }
        StackPane contentHost = (StackPane) tituloAvaliacao.getScene().lookup("#contentHost");
        if (contentHost != null) {
            App.swapContent(contentHost, fxmlPath);
        }
    }
/*
    private void configurarPainelRecomendacao(ResultadoPayload payload) {
       /*  if (painelRecomendacao == null || btnToggleRecomendacao == null) {
            return;
        }

        UUID candidatoId = Authentication.getCurrentUserId();
        LinkedHashSet<String> disciplinas = new LinkedHashSet<>();
        LinkedHashSet<String> subtopicosRelacionados = new LinkedHashSet<>();
        for (QuestaoResultado questao : questoesResultado) {
            if (questao.getDisciplina() != null && !questao.getDisciplina().isBlank() && !"-".equals(questao.getDisciplina())) {
                disciplinas.add(questao.getDisciplina());
            }
            if (questao.getSubtopico() != null && !questao.getSubtopico().isBlank() && !"-".equals(questao.getSubtopico())) {
                subtopicosRelacionados.add(QuestaoUtil.normalizar(questao.getSubtopico()));
            }
        }

        ArrayList<String> observacoes = new ArrayList<>();
        ArrayList<TrilhaAdaptacaoSubtopico> trilhas = new ArrayList<>();
        boolean diagnostico = isDiagnostico(payload);

        for (String disciplina : disciplinas) {
            UUID disciplinaId = QuestaoUtil.resolverDisciplinaId(disciplina);
            String observacao = diagnostico
                ? diagnosticoService.carregarUltimaObservacaoDiagnostico(candidatoId, disciplinaId, disciplina)
                : testeService.carregarUltimaObservacaoTeste(candidatoId, disciplinaId);
            adicionarLinhaUnica(observacoes, observacao);
            trilhas.addAll(testeService.carregarTrilhaAdaptacao(candidatoId, disciplinaId, disciplina));
        }

        List<TrilhaAdaptacaoSubtopico> trilhasRelacionadas = filtrarTrilhasRelacionadas(trilhas, subtopicosRelacionados);
        String textoObservacoes = montarTextoObservacoes(observacoes, payload);
        String textoLeitura = montarTextoLeitura(trilhasRelacionadas, payload);

        if (observacoesDetalheValue != null) {
            observacoesDetalheValue.setText(textoObservacoes);
        }
        if (leituraDetalheValue != null) {
            leituraDetalheValue.setText(textoLeitura);
        }
        renderizarTrilhaDetalhe(trilhasRelacionadas);

        boolean temConteudoExtra = !trilhasRelacionadas.isEmpty()
            || (textoObservacoes != null && !textoObservacoes.isBlank())
            || (textoLeitura != null && !textoLeitura.isBlank());

        painelRecomendacaoVisivel = false;
        painelRecomendacao.setVisible(false);
        painelRecomendacao.setManaged(false);
        btnToggleRecomendacao.setDisable(!temConteudoExtra);
        btnToggleRecomendacao.setText(temConteudoExtra ? "Ver recomendacao" : "Sem recomendacao extra");
    }

    private List<TrilhaAdaptacaoSubtopico> filtrarTrilhasRelacionadas(
        List<TrilhaAdaptacaoSubtopico> trilhas,
        Set<String> subtopicosRelacionados
    ) {
        if (trilhas == null || trilhas.isEmpty()) {
            return List.of();
        }

        List<TrilhaAdaptacaoSubtopico> filtradas = trilhas.stream()
            .filter(item -> subtopicosRelacionados.isEmpty() || subtopicosRelacionados.contains(QuestaoUtil.normalizar(item.subtopico())))
            .limit(6)
            .toList();

        if (!filtradas.isEmpty()) {
            return filtradas;
        }

        return trilhas.stream().limit(4).toList();
    }

    private String montarTextoObservacoes(List<String> observacoes, ResultadoPayload payload) {
        if (observacoes != null && !observacoes.isEmpty()) {
            return String.join("\n\n", observacoes);
        }
        return firstNonBlank(
            payload.getRecomendacao(),
            "Sem observacoes detalhadas registadas para esta tentativa."
        );
    }

    private String montarTextoLeitura(List<TrilhaAdaptacaoSubtopico> trilhas, ResultadoPayload payload) {
        ArrayList<String> linhas = new ArrayList<>();
        for (TrilhaAdaptacaoSubtopico item : trilhas) {
            String livro = firstNonBlank(item.recomendacaoLivro(), "Livro nao identificado");
            String paginas = firstNonBlank(item.recomendacaoPaginas(), "Paginas por definir");
            if ("Livro nao identificado".equals(livro) && "Paginas por definir".equals(paginas)) {
                continue;
            }
            linhas.add(item.subtopico() + ": " + livro + " | " + paginas);
        }

        if (!linhas.isEmpty()) {
            return String.join("\n", linhas);
        }

        return firstNonBlank(
            payload.getRecomendacao(),
            "Ainda nao temos leitura guiada especifica para o foco desta tentativa."
        );
    }

    private void renderizarTrilhaDetalhe(List<TrilhaAdaptacaoSubtopico> trilhas) {
       /*  if (trilhaDetalheBox == null) {
            return;
        }

        trilhaDetalheBox.getChildren().clear();
        if (trilhas == null || trilhas.isEmpty()) {
            Label vazio = new Label("Sem trilho detalhado disponivel para esta tentativa.");
            vazio.getStyleClass().add("muted");
            vazio.setWrapText(true);
            trilhaDetalheBox.getChildren().add(vazio);
            return;
        }

        for (TrilhaAdaptacaoSubtopico item : trilhas) {
            trilhaDetalheBox.getChildren().add(criarCardTrilha(item));
        }
    }

    private VBox criarCardTrilha(TrilhaAdaptacaoSubtopico item) {
        Label titulo = new Label(item.subtopico());
        titulo.getStyleClass().add("question-side-title");

        Label badge = new Label(item.precisaRevisao() ? "Rever" : "Em progresso");
        badge.getStyleClass().add("result-trail-badge");
        badge.getStyleClass().add(item.precisaRevisao() ? "result-trail-badge-warn" : "result-trail-badge-good");

        HBox header = new HBox(10, titulo, badge);
        header.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(titulo, Priority.ALWAYS);

        ProgressBar progresso = new ProgressBar(QuestaoUtil.limitarPercentual(item.progressoPercentual()));
        progresso.getStyleClass().add("result-trail-progress");
        progresso.setMaxWidth(Double.MAX_VALUE);

        Label progressoLabel = new Label(String.format(
            Locale.ROOT,
            "Progresso %.0f%% | Rigor atual %.0f%% de %.0f%%",
            item.progressoPercentual(),
            item.rigorAtualPercentual(),
            item.rigorAlvoPercentual()
        ));
        progressoLabel.getStyleClass().add("question-side-caption");

        Label metricas = new Label(String.format(
            Locale.ROOT,
            "Avancos: %d | Quedas: %d | Dificuldade media: %.0f%%",
            item.avancosRecentes(),
            item.quedasRecentes(),
            item.dificuldadeMediaPercentual()
        ));
        metricas.getStyleClass().add("question-side-copy");
        metricas.setWrapText(true);

        String leitura = firstNonBlank(item.recomendacaoLivro(), "Sem livro definido")
            + " | "
            + firstNonBlank(item.recomendacaoPaginas(), "Paginas por definir");
        Label leituraLabel = new Label(leitura);
        leituraLabel.getStyleClass().add("question-side-caption");
        leituraLabel.setWrapText(true);

        Label observacao = new Label(firstNonBlank(item.observacao(), "Sem observacao adicional."));
        observacao.getStyleClass().add("question-side-copy");
        observacao.setWrapText(true);

        VBox card = new VBox(8, header, progresso, progressoLabel, metricas, leituraLabel, observacao);
        card.getStyleClass().add("result-trail-card");
        return card;
    }

    private void adicionarLinhaUnica(List<String> destino, String valor) {
        if (destino == null || valor == null || valor.isBlank() || destino.contains(valor)) {
            return;
        }
        destino.add(valor);
    }

    private boolean isDiagnostico(ResultadoPayload payload) {
        return payload != null
            && payload.getTipoAvaliacao() != null
            && payload.getTipoAvaliacao().toLowerCase(Locale.ROOT).contains("diagnostico");
    }



    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return "";
    }*/

    private void navigate(String key) {
        switch (key) {
            case "statics" -> App.swapContent(swcDiagnos, "views/components/resultado/Stats");
            case "revisao" -> App.swapContent(swcDiagnos, "views/components/resultado/Revisao");
            case "recomendacao" -> App.swapContent(swcDiagnos, "views/components/resultado/Recomendacao");
            default -> {
            }
        }
    }

    @Override
    public void dispose() {
        ResultadoCoordinator.clearHost(this);

    }
}
