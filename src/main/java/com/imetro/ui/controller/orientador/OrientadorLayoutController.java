package com.imetro.ui.controller.orientador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.remixicon.RemixiconAL;

import com.imetro.App;
import com.imetro.config.RuntimeConfig;
import com.imetro.domain.dto.MenuEntry;
import com.imetro.ui.components.Item_Cell;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class OrientadorLayoutController implements Initializable {

   @FXML
    private StackPane contentHost;

    @FXML
    private Label dbModeBanner;

    @FXML 
    private ListView<MenuEntry> menu;
    @FXML 
    private VBox sidebar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boolean dbEnabled = RuntimeConfig.isDbEnabled();
        if (dbModeBanner != null) {
            dbModeBanner.setVisible(!dbEnabled);
            dbModeBanner.setManaged(!dbEnabled);
        }

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
            new MenuEntry("candidatos", "Candidatos", FontAwesomeSolid.SCHOOL),
            new MenuEntry("relatorios", "Relatórios", FontAwesomeSolid.CHART_LINE),
            new MenuEntry("bolsas", "Partlhar", FontAwesomeSolid.HAND_HOLDING_USD),
            new MenuEntry("perfil", "Perfil", FontAwesomeSolid.USER),
            new MenuEntry("configuracao", "Configurações", RemixiconAL.FILE_SETTINGS_FILL),
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
        App.swapContent(contentHost, "views/pages/orientador/dashboard");
    }

    @FXML
    private void openTestes() throws IOException {
        App.swapContent(contentHost, "views/pages/orientador/testes");
    }

    @FXML
    private void logout() throws IOException {
        App.setRoot("views/layouts/AuthLayout");
    }

    @FXML
    private void PopPup(ActionEvent a){
        Platform.runLater(() ->   {
            if (menu.getStyleClass().contains("min")) {
                menu.getStyleClass().remove("min");
            
                Timeline p = new Timeline(
                    new KeyFrame(Duration.seconds(.3), new KeyValue(sidebar.prefWidthProperty(),69),new KeyValue(sidebar.prefWidthProperty(),240)));
                    p.play();
            
            }else{
                menu.getStyleClass().add("min");
                Timeline p = new Timeline(
                    new KeyFrame(Duration.seconds(.3), new KeyValue(sidebar.prefWidthProperty(),240),new KeyValue(sidebar.prefWidthProperty(),69)));
                    p.play();
                
            }
        });
    }

    private void navigate(String key) {
        try {
            switch (key) {
                case "dashboard" -> openDashboard();
                case "candidatos" -> App.swapContent(contentHost, "views/pages/orientador/candidatos");
                case "exame_adaptativo" -> openTestes();
                case "relatorios" -> App.swapContent(contentHost, "views/pages/orientador/relatorios");
                case "bolsas" -> App.swapContent(contentHost, "views/pages/orientador/bolsas");
                case "perfil" -> App.swapContent(contentHost, "views/pages/orientador/perfil");
                case "configuracao" -> App.swapContent(contentHost, "views/pages/orientador/configuracao");
                case "logout" -> logout();
                default -> openDashboard();
            }
        } catch (IOException ignored) {
        }
    }
}
