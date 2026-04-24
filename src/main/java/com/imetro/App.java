package com.imetro;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.InputStream;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.imetro.config.RuntimeConfig;
import com.imetro.persistence.connection.Database;
import com.imetro.persistence.migrations.FlywayMigrations;
import com.imetro.ui.controller.lifecycle.DisposableController;

/**
 * JavaFX App
 */
public class App extends Application {

    public static Scene scene;
    private static Stage stage;
    private static final String CONTROLLER_PROPERTY_KEY = "__imetro_controller__";

    private static final ExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor(runnable -> {
        Thread thread = new Thread(runnable);
        thread.setName("imetro-app-executor");
        thread.setDaemon(true);
        return thread;
    });

    @Override 
    public void start(Stage stage) throws IOException {
        if (RuntimeConfig.isDbEnabled()) {
            FlywayMigrations.tryMigrateFromEnv();
            Database.tryWarmup();
        } else {
            System.err.println("[imetro] Modo navegação: BD desligada (ative TESTE=true ou DB_ENABLED=true).");
        }
        loadAppFonts();

        App.stage= stage;
        App.stage.setTitle("SimulatorBolsaStudy");
        App.stage.setMinWidth(1100);
        App.stage.setMinHeight(600);
        stage.setMaximized(true);

        scene = new Scene(loadView("views/layouts/AuthLayout"), 1100, 600);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        EXECUTOR.shutdownNow();
    }

    private static void loadAppFonts() {
        loadFontResource("/com/imetro/assets/fonts/roboto.ttf");
    }

    private static void loadFontResource(String resourcePath) {
        try (InputStream in = App.class.getResourceAsStream(resourcePath)) {
            if (in == null) {
                System.err.println("Font resource not found: " + resourcePath);
                return;
            }

            Font loaded = Font.loadFont(in, 12);
            if (loaded == null) {
                System.err.println("Failed to load font: " + resourcePath);
            }
        } catch (IOException e) {
            System.err.println("Failed to load font: " + resourcePath + " (" + e.getMessage() + ")");
        }
    }

    public static void setRoot(String fxml) throws IOException {
        if (scene != null) {
            disposeIfPossible(scene.getRoot());
        }
        scene.setRoot(loadView(fxml));
    }

    public static Parent loadView(String fxml) throws IOException {
        String resourcePath = "/com/imetro/" + fxml + ".fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(resourcePath));
        Parent root = fxmlLoader.load();
        Object controller = fxmlLoader.getController();
        if (root != null && controller != null) {
            root.getProperties().put(CONTROLLER_PROPERTY_KEY, controller);
        }
        return root;
    }

    public static void swapContent(StackPane host, String fxml) {
        if (host == null) {
            return;
        }

        Node previous = host.getChildren().isEmpty() ? null : host.getChildren().getFirst();
        
        FadeTransition fadeOut = new FadeTransition(Duration.millis(140), host);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            disposeIfPossible(previous);

            ProgressIndicator progress = new ProgressIndicator();
            progress.setMaxSize(30, 30);
            progress.getStyleClass().add("loading-progress");
            VBox vb=new VBox();
            vb.setSpacing(1);
            vb.setAlignment(Pos.CENTER);
            Text txt=new Text("Processando...");
            txt.getStyleClass().add("loading-text");
            vb.getChildren().addAll(txt,progress);
            StackPane loading = new StackPane(vb);
            loading.getStyleClass().add("loading-overlay");
            loading.setMinHeight(host.getHeight());
            loading.setPrefHeight(host.getHeight());
            
            host.getChildren().setAll(loading);
            host.setOpacity(1.0);

            // Defer the FXML load so the UI can paint the loading state first.
            Platform.runLater(() -> {
                try {
                    Parent next = loadView(fxml);
                    next.setOpacity(0.0);
                    host.getChildren().setAll(next);

                    FadeTransition fadeIn = new FadeTransition(Duration.millis(180), next);
                    fadeIn.setFromValue(0.0);
                    fadeIn.setToValue(1.0);
                    fadeIn.play();
                } catch (IOException ex) {
                    host.getChildren().clear();
                }
            });
        });
        fadeOut.play();
    }

    private static void disposeIfPossible(Node node) {
        if (!(node instanceof Parent parent)) {
            return;
        }
        Object controller = parent.getProperties().get(CONTROLLER_PROPERTY_KEY);
        if (controller instanceof DisposableController disposable) {
            try {
                disposable.dispose();
            } catch (Exception ignored) {
            }
        }
    }

    public static  DialogPane loadFXMLDialog(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                App.class.getResource("/com/imetro/components/dialogs/" + fxml + ".fxml")
        );
        return fxmlLoader.load();
    }

    public static  FXMLLoader loadFXMLModal(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                App.class.getResource("/com/imetro/views/modals/" + fxml + ".fxml")
        );
        return fxmlLoader;
    }

    public static void main(String[] args) {
        launch();
    }

    public static Executor getExecutorService() {
        return EXECUTOR;
    }

}
