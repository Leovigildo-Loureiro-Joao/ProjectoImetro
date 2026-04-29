package com.imetro.ui.modals;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import com.imetro.util.AvatarSupport;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class AvatarPickerModalController extends ModalController implements Initializable {

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
    private String currentUserName;
    private String currentUserEmail;
    private Predicate<String> onConfirm;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void configure(String userName, String userEmail, String currentAvatarRef, Predicate<String> onConfirm) {
        this.currentUserName = userName;
        this.currentUserEmail = userEmail;
        this.onConfirm = onConfirm;
        this.selectedAvatarRef = AvatarSupport.usesInitials(currentAvatarRef)
            ? AvatarSupport.INITIALS_TOKEN
            : AvatarSupport.normalizeAvatarRef(currentAvatarRef);

        updatePreview(this.selectedAvatarRef);
        updateOptionSelection();
        if (statusLabel != null) {
            statusLabel.setText("Escolhe um dos 4 avatares ou mantem as iniciais.");
        }
    }

    @Override
    public void init() {
        if (selectedAvatarRef == null) {
            selectedAvatarRef = AvatarSupport.INITIALS_TOKEN;
            updatePreview(selectedAvatarRef);
            updateOptionSelection();
        }
        super.init();
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
        updatePreview(selectedAvatarRef);
        updateOptionSelection();
        if (statusLabel != null) {
            statusLabel.setText("Avatar pronto. Se gostaste, confirma.");
        }
    }

    @FXML
    private void onUseInitials() {
        selectedAvatarRef = AvatarSupport.INITIALS_TOKEN;
        updatePreview(selectedAvatarRef);
        updateOptionSelection();
        if (statusLabel != null) {
            statusLabel.setText("As iniciais vao continuar como teu avatar.");
        }
    }

    @FXML
    private void onConfirmSelection() {
        String normalizedRef = AvatarSupport.normalizeAvatarRef(selectedAvatarRef);
        boolean accepted = onConfirm == null || onConfirm.test(normalizedRef);
        if (!accepted) {
            if (statusLabel != null) {
                statusLabel.setText("Nao foi possivel guardar o avatar agora.");
            }
            return;
        }
        closeModal();
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
}
