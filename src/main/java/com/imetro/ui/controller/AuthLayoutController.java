package com.imetro.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.imetro.App;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;

public class AuthLayoutController implements Initializable {

    @FXML
    private StackPane contentHost;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            showLogin();
        } catch (IOException ignored) {
            contentHost.getChildren().clear();
        }
    }

    @FXML
    private void showLogin() throws IOException {
        contentHost.getChildren().setAll(App.loadView("views/pages/auth/login"));
    }

    @FXML
    private void showRegister() throws IOException {
        contentHost.getChildren().setAll(App.loadView("views/pages/auth/register"));
    }
}
