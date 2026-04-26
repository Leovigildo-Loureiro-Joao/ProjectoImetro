package com.imetro.ui.modals;

import com.imetro.ui.controller.candidato.diagnosticos.DiagnosticoCoordinator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ModalAlert extends  ModalController{

    @FXML
    private Label tituloLabel;

    @FXML
    private Label mensagemLabel;

    @FXML
    private Label descricaoLabel;

    @Override
    public void init() {
        DiagnosticoCoordinator.AlertRequest alerta = DiagnosticoCoordinator.getAlertaAtual();
        if (alerta != null) {
            tituloLabel.setText(alerta.titulo());
            mensagemLabel.setText(alerta.mensagem());
            descricaoLabel.setText("Esta acao sera aplicada aos diagnosticos selecionados.");
        }
        super.init();
    }



    @FXML
    private void Continuar(ActionEvent event) {
        DiagnosticoCoordinator.confirmarAlertaAtual();
        closeModal();
    }
}
