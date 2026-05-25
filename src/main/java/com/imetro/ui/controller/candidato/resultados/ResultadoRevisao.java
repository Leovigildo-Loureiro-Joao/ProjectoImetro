package com.imetro.ui.controller.candidato.resultados;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;

import com.imetro.domain.dto.MenuEntry;
import com.imetro.ui.components.CardQuestao;
import com.imetro.ui.components.Item_Cell;
import com.imetro.ui.controller.lifecycle.DisposableController;
import com.imetro.util.QuestaoResultado;
import com.imetro.util.ResultadoPayload;
import com.jfoenix.controls.JFXButton;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;

public class ResultadoRevisao implements Initializable,DisposableController{
        @FXML
    private JFXButton btnNextQuestao;

    @FXML
    private JFXButton btnPrevQuestao;

    @FXML
    private FlowPane questoesCarousel;

    @FXML
    private ListView<MenuEntry> questoesMenu;

    @FXML
    private Label questoesResumoValue;

    @FXML
    private ScrollPane questoesScroll;
    private List<QuestaoResultado> questoesResultado = List.of();

    @FXML
    private void scrollQuestoesPrev() {
        ajustarScrollCarrossel(-0.33);
    }

    @FXML
    private void scrollQuestoesNext() {
        ajustarScrollCarrossel(0.33);
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
                 questoesCarousel.getChildren().add(new CardQuestao(questao));
             }
         }

         if (questoesScroll != null) {
             questoesScroll.setHvalue(0);
         }
         Platform.runLater(this::atualizarNavegacaoCarrossel);
     }

     private List<QuestaoResultado> filtrarQuestoes(String filtro) {
         if ("acertos".equals(filtro)) {
             return questoesResultado.stream().filter(QuestaoResultado::isAcertou).toList();
         }
         if ("erros".equals(filtro)) {
             return questoesResultado.stream().filter(q -> !q.isAcertou()).toList();
         }
         return questoesResultado;
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

    @Override
    public void dispose() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ResultadoPayload payload = ResultadoAvaliacaoController.ultimoResultado;
        questoesResultado = payload.getQuestoesResultado();
        configurarMenuQuestoes();
        if (questoesScroll != null) {
            questoesScroll.hvalueProperty().addListener((obs, oldVal, newVal) -> atualizarNavegacaoCarrossel());
        }
        Platform.runLater(this::atualizarNavegacaoCarrossel);
    }

}
