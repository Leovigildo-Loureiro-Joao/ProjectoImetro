package com.imetro.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.imetro.App;
import com.imetro.ui.OnboardingRouter;
import com.imetro.util.Authentication;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class LoginController implements Initializable {

    private static final Duration CAROUSEL_INTERVAL = Duration.seconds(4);
    private static final Duration CAROUSEL_ANIMATION = Duration.millis(420);

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label statusLabel;

    @FXML
    private StackPane telaLogin;

    @FXML
    private ScrollPane carouselScroll;

    @FXML
    private HBox carouselContent;

    @FXML
    private HBox carouselDots;

    private Timeline carouselTimeline;
    private int carouselIndex = 0;
    private int realPageCount = 0;
    private int extendedPageCount = 0;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        setupCarousel();
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

    private void setupCarousel() {
        if (carouselScroll == null || carouselContent == null || carouselDots == null) {
            return;
        }

        realPageCount = carouselContent.getChildren().size();
        if (realPageCount <= 1) {
            return;
        }

        setupInfiniteCarouselPages();
        buildDots(realPageCount);
        setCarouselIndex(1, false);

        carouselTimeline = new Timeline(new KeyFrame(CAROUSEL_INTERVAL, e -> {
            int nextIndex = carouselIndex + 1;
            setCarouselIndex(nextIndex, true);
        }));
        carouselTimeline.setCycleCount(Timeline.INDEFINITE);

        carouselScroll.setOnMouseEntered(e -> stopCarousel());
        carouselScroll.setOnMouseExited(e -> startCarousel());

        telaLogin.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene == null) {
                stopCarousel();
            } else {
                startCarousel();
            }
        });
    }

    private void setupInfiniteCarouselPages() {
        List<Node> realPages = new ArrayList<>(carouselContent.getChildren());
        if (realPages.size() <= 1) {
            return;
        }

        Node firstClone = cloneCarouselPage(realPages.get(0));
        Node lastClone = cloneCarouselPage(realPages.get(realPages.size() - 1));

        List<Node> extendedPages = new ArrayList<>(realPages.size() + 2);
        extendedPages.add(lastClone);
        extendedPages.addAll(realPages);
        extendedPages.add(firstClone);

        carouselContent.getChildren().setAll(extendedPages);
        extendedPageCount = extendedPages.size();
    }

    private Node cloneCarouselPage(Node original) {
        if (!(original instanceof StackPane originalPane)) {
            return new StackPane();
        }

        StackPane clonePane = new StackPane();
        clonePane.getStyleClass().setAll(originalPane.getStyleClass());
        clonePane.setMaxWidth(originalPane.getMaxWidth());
        clonePane.setMinWidth(originalPane.getMinWidth());
        clonePane.setPrefWidth(originalPane.getPrefWidth());

        clonePane.setTranslateX(originalPane.getTranslateX());
        clonePane.setTranslateY(originalPane.getTranslateY());
        clonePane.setScaleX(originalPane.getScaleX());
        clonePane.setScaleY(originalPane.getScaleY());
        clonePane.setRotate(originalPane.getRotate());
        clonePane.setOpacity(originalPane.getOpacity());

        for (Node child : originalPane.getChildren()) {
            if (child instanceof ImageView imageView) {
                ImageView cloneImageView = new ImageView();
                cloneImageView.getStyleClass().setAll(imageView.getStyleClass());
                cloneImageView.setFitWidth(imageView.getFitWidth());
                cloneImageView.setFitHeight(imageView.getFitHeight());
                cloneImageView.setPreserveRatio(imageView.isPreserveRatio());
                cloneImageView.setSmooth(imageView.isSmooth());
                cloneImageView.setOpacity(imageView.getOpacity());

                Image image = imageView.getImage();
                if (image != null) {
                    cloneImageView.setImage(image);
                }

                clonePane.getChildren().add(cloneImageView);
                continue;
            }

            if (child instanceof Region region) {
                Region cloneRegion = new Region();
                cloneRegion.getStyleClass().setAll(region.getStyleClass());
                cloneRegion.setMinWidth(region.getMinWidth());
                cloneRegion.setMinHeight(region.getMinHeight());
                cloneRegion.setPrefWidth(region.getPrefWidth());
                cloneRegion.setPrefHeight(region.getPrefHeight());
                cloneRegion.setMaxWidth(region.getMaxWidth());
                cloneRegion.setMaxHeight(region.getMaxHeight());
                cloneRegion.setOpacity(region.getOpacity());
                cloneRegion.setTranslateX(region.getTranslateX());
                cloneRegion.setTranslateY(region.getTranslateY());
                clonePane.getChildren().add(cloneRegion);
                continue;
            }

            if (child instanceof Text text) {
                Text cloneText = new Text(text.getText());
                cloneText.getStyleClass().setAll(text.getStyleClass());
                cloneText.setFont(text.getFont());
                cloneText.setFill(text.getFill());
                cloneText.setOpacity(text.getOpacity());
                cloneText.setTranslateX(text.getTranslateX());
                cloneText.setTranslateY(text.getTranslateY());
                clonePane.getChildren().add(cloneText);
            }
        }

        return clonePane;
    }

    private void buildDots(int pageCount) {
        carouselDots.getChildren().clear();
        for (int i = 0; i < pageCount; i++) {
            int dotIndex = i;
            Circle dot = new Circle(4.2);
            dot.getStyleClass().add("carousel-dot");
            dot.setOnMouseClicked(e -> {
                setCarouselIndex(dotIndex + 1, true);
                restartCarousel();
            });
            carouselDots.getChildren().add(dot);
        }
    }

    private void setCarouselIndex(int index, boolean animate) {
        if (realPageCount <= 1 || extendedPageCount <= 1) {
            return;
        }

        carouselIndex = Math.max(0, Math.min(index, extendedPageCount - 1));
        updateDots();

        double targetHValue = (extendedPageCount == 1) ? 0.0 : ((double) carouselIndex / (extendedPageCount - 1));
        if (!animate) {
            carouselScroll.setHvalue(targetHValue);
            return;
        }

        Timeline slide = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(carouselScroll.hvalueProperty(), carouselScroll.getHvalue())),
                new KeyFrame(CAROUSEL_ANIMATION, new KeyValue(carouselScroll.hvalueProperty(), targetHValue, Interpolator.EASE_BOTH))
        );
        slide.setOnFinished(e -> {
            if (carouselIndex == 0) {
                setCarouselIndex(realPageCount, false);
            } else if (carouselIndex == extendedPageCount - 1) {
                setCarouselIndex(1, false);
            }
        });
        slide.play();
    }

    private void updateDots() {
        int activeRealIndex;
        if (carouselIndex == 0) {
            activeRealIndex = realPageCount - 1;
        } else if (carouselIndex == extendedPageCount - 1) {
            activeRealIndex = 0;
        } else {
            activeRealIndex = carouselIndex - 1;
        }

        for (int i = 0; i < carouselDots.getChildren().size(); i++) {
            if (!(carouselDots.getChildren().get(i) instanceof Circle dot)) {
                continue;
            }

            dot.getStyleClass().remove("carousel-dot-active");
            if (i == activeRealIndex) {
                dot.getStyleClass().add("carousel-dot-active");
            }
        }
    }

    private void startCarousel() {
        if (carouselTimeline != null) {
            carouselTimeline.play();
        }
    }

    private void stopCarousel() {
        if (carouselTimeline != null) {
            carouselTimeline.stop();
        }
    }

    private void restartCarousel() {
        stopCarousel();
        startCarousel();
    }

    @FXML
    private void onRegister() throws IOException {
        StackPane contentHost =(StackPane) telaLogin.getParent();
        App.swapContent(contentHost, "views/pages/auth/register");
    }
    
}
