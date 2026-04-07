package com.imetro.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.imetro.App;
import com.imetro.ui.OnboardingRouter;
import com.imetro.util.Authentication;

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
        String email = usernameField == null ? null : usernameField.getText();
        String password = passwordField == null ? null : passwordField.getText();

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            statusLabel.setText("Preencha o email e a palavra-passe.");
            return;
        }

        boolean ok = Authentication.login(email.trim(), password);
        if (!ok) {
            statusLabel.setText("Credenciais inválidas.");
            return;
        }

        StackPane contentHost = (StackPane) telaLogin.getParent();
        OnboardingRouter.routeAfterAuth(contentHost);
    }

    
    @FXML
    private void onRegister() throws IOException {
        StackPane contentHost =(StackPane) telaLogin.getParent();
        App.swapContent(contentHost, "views/pages/auth/register");
    }
    
}
