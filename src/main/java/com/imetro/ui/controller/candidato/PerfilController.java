package com.imetro.ui.controller.candidato;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import com.imetro.App;
import com.imetro.config.RuntimeConfig;
import com.imetro.persistence.repository.MedalhaRepository;
import com.imetro.persistence.repository.UserRepository;
import com.imetro.ui.components.perfil.MedalCard;
import com.imetro.ui.modals.AvatarPickerModalController;
import com.imetro.util.Authentication;
import com.imetro.util.AvatarSupport;
import com.imetro.util.MedalSupport;
import com.imetro.util.PasswordHasher;
import com.imetro.util.ProfileSessionState;
import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class PerfilController implements Initializable {


    @FXML
    private StackPane modalPai;

    @FXML
    private ImageView avatarImage;

    @FXML
    private Label avatarInitialsLabel;

    @FXML
    private Label heroNameLabel;

    @FXML
    private Label heroEmailLabel;

    @FXML
    private Label heroHintLabel;

    @FXML
    private Label unlockedCountLabel;

    @FXML
    private Label nextUnlockLabel;

    @FXML
    private Label thresholdsLabel;

    @FXML
    private TextField nomeField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField senhaField;

    @FXML
    private JFXButton editProfileButton;

    @FXML
    private JFXButton changeAvatarButton;

    @FXML
    private JFXButton saveButton;

    @FXML
    private Label feedbackLabel;

    @FXML
    private FlowPane medalhasFlow;

    private final UserRepository userRepository = new UserRepository();
    private final MedalhaRepository medalhaRepository = new MedalhaRepository();

    private String currentUserEmail;
    private String currentUserName;
    private String currentAvatarRef;
    private boolean editingEnabled;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentUserEmail = Authentication.getCurrentUserEmail();
        currentUserName = resolveCurrentUserName(currentUserEmail);
        currentAvatarRef = resolveCurrentAvatarRef(currentUserEmail);

        populateProfileData();
        renderMedals();
        setEditingEnabled(false);
        setFeedbackMessage(
            RuntimeConfig.isDbEnabled()
                ? "O teu mural de medalhas ja esta pronto para receber conquistas reais."
                : "Modo navegacao ativo. Avatar e nome podem ser ajustados nesta sessao; medalhas usam um preview demonstrativo.",
            "profile-feedback-info"
        );
    }

    @FXML
    private void onToggleEdit() {
        if (editingEnabled) {
            populateProfileData();
            setEditingEnabled(false);
            setFeedbackMessage("Edicao cancelada. O perfil voltou ao ultimo estado guardado.", "profile-feedback-info");
            return;
        }

        setEditingEnabled(true);
        setFeedbackMessage("Modo de edicao ativo. Podes ajustar o nome, a senha e trocar o avatar.", "profile-feedback-info");
    }

    @FXML
    private void onSaveProfile() {
        if (!editingEnabled) {
            setFeedbackMessage("Ativa primeiro o modo de edicao para salvar alteracoes.", "profile-feedback-info");
            return;
        }

        String newName = nomeField == null ? "" : nomeField.getText() == null ? "" : nomeField.getText().trim();
        String newPassword = senhaField == null ? "" : senhaField.getText() == null ? "" : senhaField.getText().trim();

        if (newName.isBlank()) {
            setFeedbackMessage("O nome nao pode ficar vazio.", "profile-feedback-error");
            return;
        }

        boolean wantsPasswordUpdate = !newPassword.isBlank();
        if (!persistProfileChanges(newName, newPassword)) {
            setFeedbackMessage("Nao foi possivel salvar os dados do perfil agora.", "profile-feedback-error");
            return;
        }

        currentUserName = newName;
        ProfileSessionState.rememberName(currentUserEmail, newName);
        populateProfileData();
        setEditingEnabled(false);

        if (!RuntimeConfig.isDbEnabled() && wantsPasswordUpdate) {
            setFeedbackMessage("Nome atualizado nesta sessao. A senha continua apenas visual enquanto a BD estiver desligada.", "profile-feedback-info");
            return;
        }

        setFeedbackMessage(
            wantsPasswordUpdate ? "Perfil atualizado com sucesso, incluindo a nova senha." : "Perfil atualizado com sucesso.",
            "profile-feedback-success"
        );
    }

    @FXML
    private void onChangeAvatar() {
        if (!editingEnabled) {
            setFeedbackMessage("Entra primeiro em modo de edicao para trocar o avatar.", "profile-feedback-info");
            return;
        }

        try {
            modalPai.getChildren().clear();
            FXMLLoader loader = App.loadFXMLModal("AvatarPicker");
            Node modal = loader.load();
            AvatarPickerModalController controller = loader.getController();
            controller.configure(currentUserName, currentUserEmail, currentAvatarRef, this::persistAvatarSelection);
            modalPai.getChildren().add(modal);
            controller.init();
        } catch (Exception e) {
            e.printStackTrace();
            setFeedbackMessage("Nao foi possivel abrir a selecao de avatar agora.", "profile-feedback-error");
        }
    }

    private boolean persistProfileChanges(String newName, String newPassword) {
        if (currentUserEmail == null || currentUserEmail.isBlank()) {
            return false;
        }

        if (!RuntimeConfig.isDbEnabled()) {
            ProfileSessionState.rememberName(currentUserEmail, newName);
            return true;
        }

        boolean updatedName = userRepository.updateNomeByEmail(currentUserEmail, newName);
        if (!updatedName) {
            return false;
        }

        if (newPassword == null || newPassword.isBlank()) {
            return true;
        }

        return userRepository.updatePasswordHashByEmail(currentUserEmail, PasswordHasher.sha256Base64(newPassword));
    }

    private boolean persistAvatarSelection(String avatarRef) {
        String normalizedRef = AvatarSupport.normalizeAvatarRef(avatarRef);

        if (currentUserEmail == null || currentUserEmail.isBlank()) {
            return false;
        }

        if (!RuntimeConfig.isDbEnabled()) {
            ProfileSessionState.rememberAvatar(currentUserEmail, normalizedRef);
            currentAvatarRef = normalizedRef;
            applyAvatar(currentAvatarRef, currentUserName, currentUserEmail);
            setFeedbackMessage("Avatar ajustado nesta sessao. Em modo navegacao ele serve como preview local.", "profile-feedback-info");
            return true;
        }

        try {
            boolean updated = userRepository.updateAvatarUrlByEmail(currentUserEmail, normalizedRef);
            if (updated) {
                ProfileSessionState.rememberAvatar(currentUserEmail, normalizedRef);
                currentAvatarRef = normalizedRef;
                applyAvatar(currentAvatarRef, currentUserName, currentUserEmail);
                setFeedbackMessage("Avatar atualizado com sucesso.", "profile-feedback-success");
            }
            return updated;
        } catch (RuntimeException e) {
            e.printStackTrace();
            setFeedbackMessage("Nao foi possivel guardar o avatar agora.", "profile-feedback-error");
            return false;
        }
    }

    private void populateProfileData() {
        String displayName = currentUserName == null || currentUserName.isBlank() ? "Candidato" : currentUserName;
        String displayEmail = currentUserEmail == null || currentUserEmail.isBlank() ? "sem email em sessao" : currentUserEmail;

        if (heroNameLabel != null) {
            heroNameLabel.setText(displayName);
        }
        if (heroEmailLabel != null) {
            heroEmailLabel.setText(displayEmail);
        }
        if (heroHintLabel != null) {
            heroHintLabel.setText(
                RuntimeConfig.isDbEnabled()
                    ? "Perfil de candidato com mural de recordes, avatar editavel e medalhas por sucessao de habilidades."
                    : "Perfil em modo navegacao com preview de avatar e medalhas ate a BD ficar ligada."
            );
        }

        if (nomeField != null) {
            nomeField.setText(displayName);
        }
        if (emailField != null) {
            emailField.setText(displayEmail);
        }
        if (senhaField != null) {
            senhaField.clear();
        }

        applyAvatar(currentAvatarRef, currentUserName, currentUserEmail);
    }

    private void renderMedals() {
        List<MedalSupport.MedalViewModel> medals = buildMedalViewModels();
        if (medalhasFlow != null) {
            medalhasFlow.getChildren().clear();
            for (MedalSupport.MedalViewModel medal : medals) {
                medalhasFlow.getChildren().add(new MedalCard(medal));
            }
        }

        long unlockedCount = medals.stream().filter(MedalSupport.MedalViewModel::unlocked).count();
        if (unlockedCountLabel != null) {
            unlockedCountLabel.setText(unlockedCount + "/" + medals.size() + " medalhas desbloqueadas");
        }

        MedalSupport.MedalViewModel nextLocked = medals.stream()
            .filter(medal -> !medal.unlocked())
            .findFirst()
            .orElse(null);

        if (nextUnlockLabel != null) {
            nextUnlockLabel.setText(
                nextLocked == null
                    ? "Colecao completa. O candidato ja desbloqueou todas as 20 medalhas."
                    : "Proxima mira: " + nextLocked.definition().title() + " com " + nextLocked.definition().targetValue()
                        + " " + nextLocked.definition().targetUnit() + "."
            );
        }

        if (thresholdsLabel != null) {
            thresholdsLabel.setText("Escada base atual por habilidade: 5, 15, 30 e 50 sucessos.");
        }
    }

    private List<MedalSupport.MedalViewModel> buildMedalViewModels() {
        Map<String, MedalSupport.MedalAward> awardsByCode = new HashMap<>();
        Map<MedalSupport.MedalSkill, Integer> previewProgress = RuntimeConfig.isDbEnabled()
            ? Map.of()
            : MedalSupport.navigationPreviewProgress();

        if (RuntimeConfig.isDbEnabled()) {
            UUID userId = resolveCurrentUserId();
            for (MedalSupport.MedalAward award : medalhaRepository.findAwardsByUserId(userId)) {
                awardsByCode.put(award.medalCode(), award);
            }
        }

        return MedalSupport.catalog().stream()
            .map(definition -> {
                MedalSupport.MedalAward award = awardsByCode.get(definition.code());
                int progressValue = 0;
                boolean unlocked = false;
                Integer recordValue = null;
                java.time.LocalDateTime earnedAt = null;

                if (award != null) {
                    progressValue = Math.max(award.progressValue(), definition.targetValue());
                    unlocked = true;
                    recordValue = award.recordValue();
                    earnedAt = award.earnedAt();
                } else if (!RuntimeConfig.isDbEnabled()) {
                    progressValue = previewProgress.getOrDefault(definition.skill(), 0);
                    unlocked = progressValue >= definition.targetValue();
                }

                return new MedalSupport.MedalViewModel(definition, unlocked, progressValue, recordValue, earnedAt);
            })
            .toList();
    }

    
    

    private void setEditingEnabled(boolean enabled) {
        editingEnabled = enabled;

        if (nomeField != null) {
            nomeField.setEditable(enabled);
        }
        if (emailField != null) {
            emailField.setEditable(false);
        }
        if (senhaField != null) {
            senhaField.setEditable(enabled);
            senhaField.setPromptText(enabled ? "Nova senha opcional" : "Clica em editar para trocar a senha");
            if (!enabled) {
                senhaField.clear();
            }
        }
        if (saveButton != null) {
            saveButton.setDisable(!enabled);
        }
        if (changeAvatarButton != null) {
            changeAvatarButton.setDisable(!enabled);
        }
        if (editProfileButton != null) {
            editProfileButton.setText(enabled ? "Cancelar edicao" : "Editar perfil");
        }
    }

    private void setFeedbackMessage(String message, String toneClass) {
        if (feedbackLabel == null) {
            return;
        }

        feedbackLabel.setText(message == null ? "" : message);
        feedbackLabel.getStyleClass().removeAll("profile-feedback-info", "profile-feedback-success", "profile-feedback-error");
        if (toneClass != null && !toneClass.isBlank()) {
            feedbackLabel.getStyleClass().add(toneClass);
        }
    }

    private void applyAvatar(String avatarRef, String nome, String email) {
        Image image = AvatarSupport.loadAvatarImage(avatarRef);
        boolean hasImage = image != null;

        if (avatarImage != null) {
            avatarImage.setImage(image);
            avatarImage.setVisible(hasImage);
            avatarImage.setManaged(hasImage);
            avatarImage.setClip(new Circle(82, 82, 82));
        }

        if (avatarInitialsLabel != null) {
            avatarInitialsLabel.setText(AvatarSupport.extractInitials(nome, email));
            avatarInitialsLabel.setVisible(!hasImage);
            avatarInitialsLabel.setManaged(!hasImage);
        }
    }

    private String resolveCurrentUserName(String email) {
        if (email == null || email.isBlank()) {
            return "Candidato";
        }

        String remembered = ProfileSessionState.resolveName(email, null);
        if (remembered != null && !remembered.isBlank()) {
            return remembered;
        }

        if (RuntimeConfig.isDbEnabled()) {
            try {
                String nome = userRepository.getNomeByEmail(email);
                if (nome != null && !nome.isBlank()) {
                    return nome;
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }

        return email;
    }

    private String resolveCurrentAvatarRef(String email) {
        if (email == null || email.isBlank()) {
            return AvatarSupport.INITIALS_TOKEN;
        }

        String remembered = ProfileSessionState.resolveAvatar(email, AvatarSupport.INITIALS_TOKEN);
        if (!RuntimeConfig.isDbEnabled()) {
            return remembered;
        }

        try {
            String avatarRef = userRepository.getAvatarUrlByEmail(email);
            return ProfileSessionState.resolveAvatar(email, avatarRef);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return remembered;
        }
    }

    private UUID resolveCurrentUserId() {
        UUID currentUserId = Authentication.getCurrentUserId();
        if (currentUserId != null) {
            return currentUserId;
        }

        if (currentUserEmail == null || currentUserEmail.isBlank() || !RuntimeConfig.isDbEnabled()) {
            return null;
        }

        try {
            return userRepository.getIdByEmail(currentUserEmail);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return null;
        }
    }

   
}
