package com.imetro.ui.modals;

import com.imetro.ui.controller.candidato.TesteAdaptativoCoordinator;
import com.imetro.ui.controller.candidato.diagnosticos.DiagnosticoCoordinator;
import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ModalAlert extends ModalController {

    @FXML
    private Label tituloLabel;

    @FXML
    private Label mensagemLabel;

    @FXML
    private Label descricaoLabel;

    @FXML
    private JFXButton continuarButton;

    @FXML
    private JFXButton cancelarButton;

    @Override
    public void init() {
        boolean isTeste = FluxoModalContext.isTesteAdaptativo();
        String titulo = "AVISO";
        String mensagem = "Deseja continuar com a operacao?";
        Runnable onConfirm = null;

        if (isTeste) {
            TesteAdaptativoCoordinator.AlertRequest alerta = TesteAdaptativoCoordinator.getAlertaAtual();
            if (alerta != null) {
                titulo = alerta.titulo();
                mensagem = alerta.mensagem();
                onConfirm = alerta.onConfirm();
            }
        } else {
            DiagnosticoCoordinator.AlertRequest alerta = DiagnosticoCoordinator.getAlertaAtual();
            if (alerta != null) {
                titulo = alerta.titulo();
                mensagem = alerta.mensagem();
                onConfirm = alerta.onConfirm();
            }
        }

        tituloLabel.setText(titulo);
        mensagemLabel.setText(mensagem);

        if (onConfirm == null) {
            descricaoLabel.setText("Revise a informacao e feche esta mensagem para continuar.");
            continuarButton.setText("Entendi");
            continuarButton.setStyle("-fx-background-color: -color-primary; -fx-text-fill: white;");
            cancelarButton.setVisible(false);
            cancelarButton.setManaged(false);
        } else if (isTeste) {
            descricaoLabel.setText("Esta acao sera aplicada ao teste adaptativo configurado.");
        } else {
            descricaoLabel.setText("Esta acao sera aplicada aos diagnosticos selecionados.");
        }

        super.init();
    }

    @FXML
    private void Cancelar(ActionEvent event) {
        closeModal();
    }

    @FXML
    private void Continuar(ActionEvent event) {
        if (FluxoModalContext.isTesteAdaptativo()) {
            TesteAdaptativoCoordinator.confirmarAlertaAtual();
        } else {
            DiagnosticoCoordinator.confirmarAlertaAtual();
        }
        closeModal();
    }
}
