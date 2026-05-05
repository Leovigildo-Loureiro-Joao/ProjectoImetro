package com.imetro.ui.controller.candidato;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.remixicon.RemixiconAL;

import com.imetro.App;
import com.imetro.config.RuntimeConfig;
import com.imetro.domain.CacheService;
import com.imetro.domain.dto.MenuEntry;
import com.imetro.domain.model.Candidato;
import com.imetro.services.PerguntasBootstrapAsyncService;
import com.imetro.ui.components.Item_Cell;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class CandidatoLayoutController implements Initializable {
    private static final String BANNER_RUNNING_CLASS = "bootstrap-banner-running";
    private static final String BANNER_SUCCESS_CLASS = "bootstrap-banner-success";
    private static final String BANNER_WARNING_CLASS = "bootstrap-banner-warning";
    private static final String BANNER_ERROR_CLASS = "bootstrap-banner-error";

    @FXML
    private StackPane contentHost;

    @FXML
    private Label dbModeBanner;

    @FXML
    private VBox bootstrapBanner;

    @FXML
    private Label bootstrapTitleLabel;

    @FXML
    private Label bootstrapDetailLabel;

    @FXML
    private Label bootstrapPercentLabel;

    @FXML
    private ProgressBar bootstrapProgressBar;

    @FXML 
    private ListView<MenuEntry> menu;
    @FXML 
    private VBox sidebar;

    private final PerguntasBootstrapAsyncService perguntasBootstrapAsyncService =
        PerguntasBootstrapAsyncService.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boolean dbEnabled = RuntimeConfig.isDbEnabled();
        if (dbModeBanner != null) {
            dbModeBanner.setVisible(!dbEnabled);
            dbModeBanner.setManaged(!dbEnabled);
        }

        configureBootstrapBanner();

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
            new MenuEntry("configuracao", "Configurações", RemixiconAL.FILE_SETTINGS_FILL),
            new MenuEntry("logout", "Logout", FontAwesomeSolid.SIGN_OUT_ALT)
        );

        menu.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                navigate(newValue.key());
            }
        });

        menu.getSelectionModel().selectFirst();
        FirstDiagnostic();
    }

    private void FirstDiagnostic(){
        Candidato candidato =(Candidato) CacheService.get("currentUser");
        
    }

    private void configureBootstrapBanner() {
        if (bootstrapBanner == null) {
            return;
        }

        bootstrapBanner.managedProperty().bind(bootstrapBanner.visibleProperty());
        bootstrapBanner.visibleProperty().bind(perguntasBootstrapAsyncService.showBannerProperty());

        if (bootstrapTitleLabel != null) {
            bootstrapTitleLabel.textProperty().bind(perguntasBootstrapAsyncService.titleProperty());
        }
        if (bootstrapDetailLabel != null) {
            bootstrapDetailLabel.textProperty().bind(perguntasBootstrapAsyncService.detailProperty());
        }
        if (bootstrapProgressBar != null) {
            bootstrapProgressBar.progressProperty().bind(perguntasBootstrapAsyncService.progressProperty());
        }
        if (bootstrapPercentLabel != null) {
            bootstrapPercentLabel.textProperty().bind(Bindings.createStringBinding(() -> {
                double value = perguntasBootstrapAsyncService.progressProperty().get();
                if (value < 0) {
                    return "Em curso";
                }
                return Math.round(value * 100.0) + "%";
            }, perguntasBootstrapAsyncService.progressProperty()));
        }

        updateBootstrapBannerStyle(perguntasBootstrapAsyncService.getState());
        perguntasBootstrapAsyncService.stateProperty().addListener((obs, oldState, newState) ->
            updateBootstrapBannerStyle(newState)
        );
    }

    private void updateBootstrapBannerStyle(PerguntasBootstrapAsyncService.BootstrapUiState state) {
        if (bootstrapBanner == null) {
            return;
        }

        bootstrapBanner.getStyleClass().removeAll(
            BANNER_RUNNING_CLASS,
            BANNER_SUCCESS_CLASS,
            BANNER_WARNING_CLASS,
            BANNER_ERROR_CLASS
        );

        if (state == null) {
            return;
        }

        switch (state) {
            case RUNNING -> bootstrapBanner.getStyleClass().add(BANNER_RUNNING_CLASS);
            case SUCCESS -> bootstrapBanner.getStyleClass().add(BANNER_SUCCESS_CLASS);
            case WARNING -> bootstrapBanner.getStyleClass().add(BANNER_WARNING_CLASS);
            case ERROR -> bootstrapBanner.getStyleClass().add(BANNER_ERROR_CLASS);
            case IDLE -> {
            }
        }
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
                case "diagnostico" -> App.swapContent(contentHost, "views/pages/candidato/diagnostico");
                case "exame_adaptativo" -> openTestes();
                case "relatorios" -> App.swapContent(contentHost, "views/pages/candidato/relatorios");
                case "bolsas" -> App.swapContent(contentHost, "views/pages/candidato/bolsas");
                case "perfil" -> App.swapContent(contentHost, "views/pages/candidato/perfil");
                case "configuracao" -> App.swapContent(contentHost, "views/pages/candidato/configuracao");
                case "logout" -> logout();
                default -> openDashboard();
            }
        } catch (IOException ignored) {
        }
    }
}
