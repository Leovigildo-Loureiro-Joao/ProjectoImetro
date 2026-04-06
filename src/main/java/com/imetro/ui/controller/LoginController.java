package com.imetro.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.imetro.App;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label statusLabel;

    @FXML
    private StackPane telaLogin;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // no-op
    }

    @FXML
    private void onLogin() throws IOException {
        String role = "";
        if (role == null || role.isBlank()) {
            statusLabel.setText("Seleciona o perfil para continuar.");
            return;
        }

        if ("ORIENTADOR".equalsIgnoreCase(role)) {
            App.setRoot("views/layouts/OrientadorLayout");
        } else {
            App.setRoot("views/layouts/CandidatoLayout");
        }
    }

    
    @FXML
    private void onRegister() throws IOException {
        StackPane contentHost =(StackPane) telaLogin.getParent();
        App.swapContent(contentHost, "views/pages/auth/register");
    }
    
}
