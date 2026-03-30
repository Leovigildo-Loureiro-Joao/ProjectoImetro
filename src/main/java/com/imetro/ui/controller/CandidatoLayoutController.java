package com.imetro.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.imetro.App;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;

public class CandidatoLayoutController implements Initializable {

    @FXML
    private StackPane contentHost;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            openDashboard();
        } catch (IOException ignored) {
        }
    }

    @FXML
    private void openDashboard() throws IOException {
        contentHost.getChildren().setAll(App.loadView("views/pages/candidato/dashboard"));
    }

    @FXML
    private void openTestes() throws IOException {
        contentHost.getChildren().setAll(App.loadView("views/pages/candidato/testes"));
    }

    @FXML
    private void logout() throws IOException {
        App.setRoot("views/layouts/AuthLayout");
    }
}
