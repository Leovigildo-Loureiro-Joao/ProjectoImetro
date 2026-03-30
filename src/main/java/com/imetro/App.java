package com.imetro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * JavaFX App
 */
public class App extends Application {

    public static Scene scene;
    private static Stage stage;

    @Override 
    public void start(Stage stage) throws IOException {
        App.stage= stage;
        App.stage.setTitle("SimulatorBolsaStudy");
        App.stage.setMinWidth(1100);
        App.stage.setMinHeight(600);
        stage.setMaximized(true);

        scene = new Scene(loadView("views/layouts/AuthLayout"), 1100, 600);
        scene.getStylesheets()
                .add(App.class.getResource("/com/imetro/styles/global.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadView(fxml));
    }

    public static Parent loadView(String fxml) throws IOException {
        String resourcePath = "/com/imetro/" + fxml + ".fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(resourcePath));
        return fxmlLoader.load();
    }

    public static  DialogPane loadFXMLDialog(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                App.class.getResource("/com/imetro/components/dialogs/" + fxml + ".fxml")
        );
        return fxmlLoader.load();
    }

    public static  Node loadFXMLModal(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                App.class.getResource("/com/imetro/modals/" + fxml + ".fxml")
        );
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    public static Executor getExecutorService() {
       return Executors.newSingleThreadScheduledExecutor(
                runnable -> {
                    Thread thread = new Thread(runnable);
                    thread.setDaemon(true); // Set the thread as a daemon thread
                    return thread;
                }
        );
    }

}
