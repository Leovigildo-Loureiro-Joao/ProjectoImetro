package com.imetro.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.remixicon.RemixiconAL;

import com.imetro.App;
import com.imetro.ui.components.Item_Cell;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;

public class CandidatoLayoutController implements Initializable {
    private record MenuEntry(String key, String title, Ikon icon) {}

    @FXML
    private StackPane contentHost;

    @FXML 
    private ListView<MenuEntry> menu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menu.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(MenuEntry item, boolean empty) {
                super.updateItem(item, empty);
                setText(null);
                setGraphic(empty || item == null ? null : new Item_Cell(item.title(), item.icon()));
            }
        });

        menu.getItems().setAll(
            new MenuEntry("dashboard", "Dashboard", RemixiconAL.LAYOUT_GRID_FILL),
            new MenuEntry("diagnostico", "Diagnóstico", FontAwesomeSolid.BOLT),
            new MenuEntry("exame_adaptativo", "Exame Adaptativo", FontAwesomeSolid.FILE_ALT),
            new MenuEntry("relatorios", "Relatórios", FontAwesomeSolid.CHART_LINE),
            new MenuEntry("bolsas", "Recomendações", FontAwesomeSolid.HAND_HOLDING_USD),
            new MenuEntry("perfil", "Perfil", FontAwesomeSolid.USER),
            new MenuEntry("logout", "Logout", FontAwesomeSolid.SIGN_OUT_ALT)
        );

        menu.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                navigate(newValue.key());
            }
        });

        menu.getSelectionModel().selectFirst();
    }

    @FXML
    private void openDashboard() throws IOException {
        App.swapContent(contentHost, "views/pages/candidato/dashboard");
    }

    @FXML
    private void openTestes() throws IOException {
        App.swapContent(contentHost, "views/pages/candidato/testes");
    }

    @FXML
    private void logout() throws IOException {
        App.setRoot("views/layouts/AuthLayout");
    }

    private void navigate(String key) {
        try {
            switch (key) {
                case "dashboard" -> openDashboard();
                case "diagnostico" -> App.swapContent(contentHost, "views/pages/candidato/diagnostico");
                case "exame_adaptativo" -> openTestes();
                case "relatorios" -> App.swapContent(contentHost, "views/pages/candidato/relatorios");
                case "bolsas" -> App.swapContent(contentHost, "views/pages/candidato/bolsas");
                case "perfil" -> App.swapContent(contentHost, "views/pages/candidato/perfil");
                case "logout" -> logout();
                default -> openDashboard();
            }
        } catch (IOException ignored) {
        }
    }
}
