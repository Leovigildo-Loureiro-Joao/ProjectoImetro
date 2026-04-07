package com.imetro.ui.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.imetro.persistence.repository.UserRepository;
import com.imetro.ui.OnboardingRouter;
import com.imetro.util.Authentication;
import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

public class AddImageOnboardingController implements Initializable {

    @FXML
    private StackPane telaAddImage;

    @FXML
    private ImageView avatarPreview;

    @FXML
    private Label statusLabel;

    private File selectedImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (statusLabel != null) {
            statusLabel.setText("");
        }
    }

    @FXML
    private void onChooseImage() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Selecionar imagem");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );
        File file = chooser.showOpenDialog(telaAddImage == null ? null : telaAddImage.getScene().getWindow());
        if (file == null) {
            return;
        }

        try {
            Image image = new Image(file.toURI().toString(), true);
            if (avatarPreview != null) {
                avatarPreview.setImage(image);
            }
            selectedImage = file;
            if (statusLabel != null) {
                statusLabel.setText("");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            if (statusLabel != null) {
                statusLabel.setText("Não foi possível carregar a imagem.");
            }
        }
    }

    

    @FXML
    private void onSkip(ActionEvent actionEvent) {
        StackPane contentHost = (StackPane) telaAddImage.getParent();
        OnboardingRouter.goToCandidateDisciplinas(contentHost);
    }

    @FXML
    private void onContinue(ActionEvent actionEvent) {
        JFXButton source = actionEvent.getSource() instanceof JFXButton btn ? btn : null;
        if (source != null) {
            source.setDisable(true);
        }

        if (selectedImage != null) {
            try {
                String email = Authentication.getCurrentUserEmail();
                if (email != null && !email.isBlank()) {
                    new UserRepository().updateAvatarUrlByEmail(email, selectedImage.toURI().toString());
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }

        StackPane contentHost = (StackPane) telaAddImage.getParent();
        OnboardingRouter.goToCandidateDisciplinas(contentHost);

        if (source != null) {
            source.setDisable(false);
        }
    }
}

