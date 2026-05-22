package com.imetro.ui.controller.candidato.resultados;

import java.net.URL;
import java.util.ResourceBundle;

import com.imetro.ui.controller.lifecycle.DisposableController;
import com.imetro.util.ResultadoPayload;
import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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
    private ListView<?> questoesMenu;

    @FXML
    private Label questoesResumoValue;

    @FXML
    private ScrollPane questoesScroll;

    @FXML
    void scrollQuestoesNext(ActionEvent event) {

    }

    @FXML
    void scrollQuestoesPrev(ActionEvent event) {

    }

    @Override
    public void dispose() {
      
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
