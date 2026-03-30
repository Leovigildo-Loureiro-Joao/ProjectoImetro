package com.imetro.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.imetro.App;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> roleCombo;

    @FXML
    private Label statusLabel;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        roleCombo.setItems(FXCollections.observableArrayList("CANDIDATO", "ORIENTADOR"));
    }

    @FXML
    private void onLogin() throws IOException {
        String role = roleCombo.getValue();
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
    
}
