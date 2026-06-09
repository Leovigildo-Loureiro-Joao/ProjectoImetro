package com.imetro.ui.modals;

import com.jfoenix.controls.JFXButton;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class PausaModalController extends ModalController {

    @FXML
    private Label tituloLabel;

    @FXML
    private Label mensagemLabel;

    @FXML
    private Label detalheLabel;

    @FXML
    private JFXButton continuarButton;

    @FXML
    private JFXButton desistirButton;

    @FXML
    private JFXButton recomecarButton;

    @Override
    public void init() {
        PauseSessionContext.PauseRequest request = PauseSessionContext.getRequest();

        if (tituloLabel != null) {
            tituloLabel.setText(request == null || request.titulo() == null || request.titulo().isBlank()
                ? "Pausa da sessao"
                : request.titulo());
        }
        if (mensagemLabel != null) {
            mensagemLabel.setText(request == null || request.mensagem() == null || request.mensagem().isBlank()
                ? "O tempo ficou congelado."
                : request.mensagem());
        }
        if (detalheLabel != null) {
            detalheLabel.setText("A pausa so pode ser usada uma vez nesta sessao.");
        }

        if (request != null) {
            if (continuarButton != null && request.continuarTexto() != null && !request.continuarTexto().isBlank()) {
                continuarButton.setText(request.continuarTexto());
            }
            if (desistirButton != null && request.desistirTexto() != null && !request.desistirTexto().isBlank()) {
                desistirButton.setText(request.desistirTexto());
            }
            if (recomecarButton != null && request.recomecarTexto() != null && !request.recomecarTexto().isBlank()) {
                recomecarButton.setText(request.recomecarTexto());
            }
        }

        super.init();
    }

    @FXML
    private void Continuar(ActionEvent event) {
        executarAcao(PauseSessionContext::continuar);
    }

    @FXML
    private void Desistir(ActionEvent event) {
        executarAcao(PauseSessionContext::desistir);
    }

    @FXML
    private void Recomecar(ActionEvent event) {
        executarAcao(PauseSessionContext::recomecar);
    }

    private void executarAcao(Runnable action) {
        if (continuarButton != null) {
            continuarButton.setDisable(true);
        }
        if (desistirButton != null) {
            desistirButton.setDisable(true);
        }
        if (recomecarButton != null) {
            recomecarButton.setDisable(true);
        }

        closeModal();

        PauseTransition delay = new PauseTransition(Duration.millis(320));
        delay.setOnFinished(e -> {
            if (action != null) {
                action.run();
            }
        });
        delay.play();
    }
}
