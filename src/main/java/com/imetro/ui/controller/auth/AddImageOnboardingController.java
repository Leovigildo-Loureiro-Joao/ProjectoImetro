package com.imetro.ui.controller.auth;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.imetro.config.RuntimeConfig;
import com.imetro.persistence.repository.UserRepository;
import com.imetro.ui.OnboardingRouter;
import com.imetro.util.Authentication;
import com.imetro.util.AvatarSupport;
import com.imetro.util.ProfileSessionState;
import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class AddImageOnboardingController implements Initializable {

    @FXML
    private StackPane telaAddImage;

    @FXML
    private ImageView avatarPreview;

    @FXML
    private Label avatarInitialsLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private StackPane avatarOption1;

    @FXML
    private StackPane avatarOption2;

    @FXML
    private StackPane avatarOption3;

    @FXML
    private StackPane avatarOption4;

    private String selectedAvatarRef;
    private String currentUserEmail;
    private String currentUserName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentUserEmail = Authentication.getCurrentUserEmail();
        currentUserName = resolveCurrentUserName(currentUserEmail);

        if (statusLabel != null) {
            statusLabel.setText("Se nao escolheres um avatar, vamos usar as iniciais do teu nome.");
        }

        showInitialsPreview();
        updateOptionSelection();
    }

    @FXML
    private void onSelectAvatar(MouseEvent event) {
        if (!(event.getSource() instanceof Node source)) {
            return;
        }

        Object rawUserData = source.getUserData();
        if (rawUserData == null) {
            return;
        }

        String avatarRef = rawUserData.toString();
        if (!AvatarSupport.isPresetAvatar(avatarRef)) {
            return;
        }

        selectedAvatarRef = avatarRef;
        updatePreview(avatarRef);
        updateOptionSelection();

        if (statusLabel != null) {
            statusLabel.setText("Avatar selecionado. Podes continuar quando quiseres.");
        }
    }

    @FXML
    private void onSkip(ActionEvent actionEvent) {
        if (persistAvatarChoice(AvatarSupport.INITIALS_TOKEN)) {
            goToNextStep();
        }
    }

    @FXML
    private void onContinue(ActionEvent actionEvent) {
        JFXButton source = actionEvent.getSource() instanceof JFXButton btn ? btn : null;
        if (source != null) {
            source.setDisable(true);
        }

        try {
            if (persistAvatarChoice(AvatarSupport.normalizeAvatarRef(selectedAvatarRef))) {
                goToNextStep();
            }
        } finally {
            if (source != null) {
                source.setDisable(false);
            }
        }
    }

    private void goToNextStep() {
        StackPane contentHost = (StackPane) telaAddImage.getParent();
        OnboardingRouter.goToCandidateDisciplinas(contentHost);
    }

    private boolean persistAvatarChoice(String avatarRef) {
        String email = currentUserEmail == null ? Authentication.getCurrentUserEmail() : currentUserEmail;
        String normalizedAvatarRef = AvatarSupport.normalizeAvatarRef(avatarRef);
        if (email == null || email.isBlank() || !RuntimeConfig.isDbEnabled()) {
            ProfileSessionState.rememberAvatar(email, normalizedAvatarRef);
            return true;
        }

        try {
            boolean updated = new UserRepository().updateAvatarUrlByEmail(email, normalizedAvatarRef);
            if (!updated && statusLabel != null) {
                statusLabel.setText("Nao foi possivel guardar o avatar agora.");
            }
            if (updated) {
                ProfileSessionState.rememberAvatar(email, normalizedAvatarRef);
            }
            return updated;
        } catch (RuntimeException e) {
            e.printStackTrace();
            if (statusLabel != null) {
                statusLabel.setText("Nao foi possivel guardar o avatar agora.");
            }
            return false;
        }
    }

    private void updatePreview(String avatarRef) {
        Image image = AvatarSupport.loadAvatarImage(avatarRef);
        boolean hasImage = image != null;

        if (avatarPreview != null) {
            avatarPreview.setImage(image);
            avatarPreview.setVisible(hasImage);
            avatarPreview.setManaged(hasImage);
        }

        if (avatarInitialsLabel != null) {
            avatarInitialsLabel.setText(AvatarSupport.extractInitials(currentUserName, currentUserEmail));
            avatarInitialsLabel.setVisible(!hasImage);
            avatarInitialsLabel.setManaged(!hasImage);
        }
    }

    private void showInitialsPreview() {
        selectedAvatarRef = null;
        updatePreview(null);
    }

    private void updateOptionSelection() {
        List<StackPane> options = List.of(avatarOption1, avatarOption2, avatarOption3, avatarOption4);
        for (StackPane option : options) {
            if (option == null) {
                continue;
            }

            option.getStyleClass().remove("avatar-option-selected");
            Object rawUserData = option.getUserData();
            if (rawUserData != null && rawUserData.toString().equals(selectedAvatarRef)) {
                option.getStyleClass().add("avatar-option-selected");
            }
        }
    }

    private String resolveCurrentUserName(String email) {
        if (email == null || email.isBlank()) {
            return email;
        }

        String rememberedName = ProfileSessionState.resolveName(email, null);
        if (rememberedName != null && !rememberedName.isBlank()) {
            return rememberedName;
        }

        if (RuntimeConfig.isDbEnabled()) {
            try {
                String nome = new UserRepository().getNomeByEmail(email);
                if (nome != null && !nome.isBlank()) {
                    return nome;
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }

        return email;
    }
}
