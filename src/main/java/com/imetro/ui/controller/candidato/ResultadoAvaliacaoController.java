package com.imetro.ui.controller.candidato;

import com.imetro.App;
import com.imetro.domain.dto.MenuEntry;
import com.imetro.ui.components.Item_Cell;
import com.imetro.ui.model.Questao;
import com.imetro.util.QuestaoResultado;
import com.imetro.util.ResultadoPayload;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;

import java.util.List;

public class ResultadoAvaliacaoController {

    private static ResultadoPayload ultimoResultado;

    public static void setResultado(ResultadoPayload payload) {
        ultimoResultado = payload;
    }

    @FXML
    private Label tituloAvaliacao;

    @FXML
    private Label disciplinaValue;

    @FXML
    private Label acertosValue;

    @FXML
    private Label errosValue;

    @FXML
    private Label percentualValue;

    @FXML
    private Label tempoValue;

    @FXML
    private Label nivelValue;

    @FXML
    private Label perfilValue;

    @FXML
    private Label recomendacaoValue;

    @FXML
    private ProgressBar percentualProgress;

    @FXML
    private JFXButton btnRefazer;

    @FXML
    private ListView<MenuEntry> questoesMenu;

    @FXML
    private FlowPane questoesCarousel;

    @FXML
    private ScrollPane questoesScroll;

    @FXML
    private Label questoesResumoValue;

    @FXML
    private JFXButton btnPrevQuestao;

    @FXML
    private JFXButton btnNextQuestao;

    private List<QuestaoResultado> questoesResultado = List.of();

    @FXML
    private void initialize() {
        ResultadoPayload payload = ultimoResultado;
        if (payload == null) {
            payload = new ResultadoPayload(
                "Avaliacao",
                "-",
                0,
                0,
                0,
                0,
                "00:00",
                "-",
                "-",
                "Conclua uma avaliacao para ver os resultados aqui.",
                "views/pages/candidato/dashboard",
                List.of()
            );
        }

        tituloAvaliacao.setText("Resultado - " + payload.getTipoAvaliacao());
        disciplinaValue.setText(payload.getDisciplina());
        acertosValue.setText(payload.getAcertos() + " / " + payload.getTotalQuestoes());
        errosValue.setText(String.valueOf(payload.getErros()));
        percentualValue.setText(String.format("%.1f%%", payload.getPercentual()));
        percentualProgress.setProgress(Math.max(0, Math.min(payload.getPercentual() / 100.0, 1)));
        tempoValue.setText(payload.getTempo());
        nivelValue.setText(payload.getNivel());
        perfilValue.setText(payload.getPerfil());
        recomendacaoValue.setText(payload.getRecomendacao());
        btnRefazer.setUserData(payload.getRetryPath());

        questoesResultado = payload.getQuestoesResultado();
        configurarMenuQuestoes();
        if (questoesScroll != null) {
            questoesScroll.hvalueProperty().addListener((obs, oldVal, newVal) -> atualizarNavegacaoCarrossel());
        }
        Platform.runLater(this::atualizarNavegacaoCarrossel);
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

    @FXML
    private void scrollQuestoesPrev() {
        ajustarScrollCarrossel(-0.33);
    }

    @FXML
    private void scrollQuestoesNext() {
        ajustarScrollCarrossel(0.33);
    }

    private void navegarPara(String fxmlPath) {
        if (tituloAvaliacao == null || tituloAvaliacao.getScene() == null) {
            return;
        }
        StackPane contentHost = (StackPane) tituloAvaliacao.getScene().lookup("#contentHost");
        if (contentHost != null) {
            App.swapContent(contentHost, fxmlPath);
        }
    }

    private void configurarMenuQuestoes() {
        if (questoesMenu == null) {
            return;
        }

        questoesMenu.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(MenuEntry item, boolean empty) {
                super.updateItem(item, empty);
                setText(null);
                setGraphic(empty || item == null ? null : new Item_Cell(item.title(), item.icon()));
            }
        });

        questoesMenu.getItems().setAll(
            new MenuEntry("todas", "Todas", FontAwesomeSolid.LIST_UL),
            new MenuEntry("acertos", "Acertos", FontAwesomeSolid.CHECK_CIRCLE),
            new MenuEntry("erros", "Erros", FontAwesomeSolid.TIMES_CIRCLE)
        );

        questoesMenu.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                renderizarCarrossel(newVal.key());
            }
        });

        questoesMenu.getSelectionModel().selectFirst();
    }

    private void renderizarCarrossel(String filtro) {
        if (questoesCarousel == null) {
            return;
        }

        questoesCarousel.getChildren().clear();
        List<QuestaoResultado> filtradas = filtrarQuestoes(filtro);

        if (questoesResumoValue != null) {
            questoesResumoValue.setText(String.format(
                "Mostrando %d de %d questoes",
                filtradas.size(),
                questoesResultado.size()
            ));
        }

        if (filtradas.isEmpty()) {
            Label vazio = new Label("Nenhuma questao encontrada neste filtro.");
            vazio.getStyleClass().add("muted");
            questoesCarousel.getChildren().add(vazio);
        } else {
            for (QuestaoResultado questao : filtradas) {
                questoesCarousel.getChildren().add(criarCardQuestao(questao));
            }
        }

        if (questoesScroll != null) {
            questoesScroll.setHvalue(0);
        }
        Platform.runLater(this::atualizarNavegacaoCarrossel);
    }

    private List<QuestaoResultado> filtrarQuestoes(String filtro) {
        if ("acertos".equals(filtro)) {
            return questoesResultado.stream().filter(q -> q.isAcertou()).toList();
        }
        if ("erros".equals(filtro)) {
            return questoesResultado.stream().filter(q -> !q.isAcertou()).toList();
        }
        return questoesResultado;
    }

    private VBox criarCardQuestao(QuestaoResultado questao) {
        VBox card = new VBox(8);
        card.setPrefWidth(360);
        card.setMinWidth(320);
        card.getStyleClass().add("sub-card");
        card.setStyle(
            "-fx-padding: 12;" +
            "-fx-border-width: 1.5;" +
            "-fx-border-radius: 12;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: " + (questao.isAcertou() ? "#10b981" : "#ef4444") + ";"
        );

        Label status = new Label(questao.isAcertou() ? "Acertou" : "Errou");
        status.getStyleClass().add("h3-thin");
        status.setStyle("-fx-text-fill: " + (questao.isAcertou() ? "#10b981" : "#ef4444") + "; -fx-font-weight: 700;");

        Label titulo = new Label("Questao " + questao.getOrdem() + " - " + questao.getDisciplina());
        titulo.getStyleClass().add("h3-thin");

        Label enunciado = new Label(questao.getEnunciado());
        enunciado.getStyleClass().add("h3-thin-big");
        enunciado.setWrapText(true);

        Label bloco2 = new Label(questao.getBloco2());
        bloco2.getStyleClass().add("muted");
        bloco2.setWrapText(true);
        bloco2.setVisible(!"-".equals(questao.getBloco2()));
        bloco2.setManaged(!"-".equals(questao.getBloco2()));

        Label respostaUsuario = new Label(
            "Sua resposta: " + questao.getRespostaUsuario() + " - " + questao.getRespostaUsuario()
        );
        respostaUsuario.getStyleClass().add("h3-thin");
        respostaUsuario.setWrapText(true);

        Label respostaCorreta = new Label(
            "Resposta correta: " + questao.getRespostaUsuario() + " - " + questao.getTextoRespostaCorreta()
        );
        respostaCorreta.getStyleClass().add("h3-thin");
        respostaCorreta.setStyle("-fx-text-fill: #2563eb;");
        respostaCorreta.setWrapText(true);

        card.getChildren().addAll(status, titulo, enunciado, bloco2, respostaUsuario, respostaCorreta);
        return card;
    }

    private void ajustarScrollCarrossel(double delta) {
        if (questoesScroll == null) {
            return;
        }
        double novoValor = Math.max(0, Math.min(1, questoesScroll.getHvalue() + delta));
        questoesScroll.setHvalue(novoValor);
        atualizarNavegacaoCarrossel();
    }

    private void atualizarNavegacaoCarrossel() {
        if (btnPrevQuestao == null || btnNextQuestao == null || questoesScroll == null || questoesCarousel == null) {
            return;
        }

        boolean semConteudo = questoesCarousel.getChildren().isEmpty();
        double scrollAtual = questoesScroll.getHvalue();
        btnPrevQuestao.setDisable(semConteudo || scrollAtual <= 0.01);
        btnNextQuestao.setDisable(semConteudo || scrollAtual >= 0.99);
    }

    
}
