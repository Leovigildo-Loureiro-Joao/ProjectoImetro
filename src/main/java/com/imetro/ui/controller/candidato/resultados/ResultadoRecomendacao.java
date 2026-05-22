package com.imetro.ui.controller.candidato.resultados;

import java.net.URL;
import java.util.ResourceBundle;

import com.imetro.ui.controller.lifecycle.DisposableController;
import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ResultadoRecomendacao  implements Initializable,DisposableController{

    private JFXButton btnToggleRecomendacao;

    @FXML
    private Label leituraDetalheValue;

    @FXML
    private Label observacoesDetalheValue;

    @FXML
    private VBox painelRecomendacao;

    @FXML
    private Label recomendacaoValue;

    @FXML
    private Accordion recommendationAccordion;

    @FXML
    private VBox trilhaDetalheBox;

    @FXML
    void togglePainelRecomendacao(ActionEvent event) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
      
    }

}
