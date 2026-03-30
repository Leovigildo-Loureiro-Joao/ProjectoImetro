package com.imetro.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.imetro.App;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;

public class OrientadorLayoutController implements Initializable {

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
        contentHost.getChildren().setAll(App.loadView("views/pages/orientador/dashboard"));
    }

    @FXML
    private void openRelatorios() throws IOException {
        contentHost.getChildren().setAll(App.loadView("views/pages/orientador/relatorios"));
    }

    @FXML
    private void logout() throws IOException {
        App.setRoot("views/layouts/AuthLayout");
    }
}
