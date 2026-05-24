package com.imetro.ui;

import java.time.LocalDateTime;
import java.util.UUID;

import com.imetro.App;
import com.imetro.config.RuntimeConfig;
import com.imetro.domain.CacheService;
import com.imetro.domain.model.Candidato;
import com.imetro.persistence.repository.ProgressoALunoDisciplinaRepository;
import com.imetro.persistence.repository.UserRepository;
import com.imetro.services.CandidatoService;
import com.imetro.util.Authentication;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public final class OnboardingRouter {

    public static final String FXML_ADD_IMAGE = "views/pages/auth/onboarding/add-image";
    public static final String FXML_CHOOSE_DISCIPLINAS = "views/pages/auth/onboarding/choose-disciplinas";
    private static final CandidatoService CANDIDATO_SERVICE = new CandidatoService();

    private OnboardingRouter() {
    }

    public static void CandidatoRoute(StackPane contentHost) {
        try {
            UUID userId = Authentication.getCurrentUserId();
            Candidato candidato = CANDIDATO_SERVICE.getCandidatoById(userId);
            if (candidato == null) {
                String email = Authentication.getCurrentUserEmail();
                String nome = email == null || email.isBlank()
                    ? "Candidato"
                    : new UserRepository().getNomeByEmail(email);
                candidato = new Candidato(
                    userId,
                    nome == null || nome.isBlank() ? "Candidato" : nome,
                    email == null ? "" : email,
                    null,
                    LocalDateTime.now()
                );
            }
            CacheService.put("currentUser", candidato);
            App.setRoot("views/layouts/CandidatoLayout");
        } catch (Exception e) {
        }
    }

    public static void routeAfterAuth(StackPane contentHost) {
        if (contentHost == null) {
            return;
        }
        if (!RuntimeConfig.isDbEnabled()) {
            CandidatoRoute(contentHost);
            return;
        }

        String role = Authentication.getCurrentUserRole();
        String email = Authentication.getCurrentUserEmail();
        UUID userId = Authentication.getCurrentUserId();
        try {
            if ((role == null || role.isBlank()) && email != null && !email.isBlank()) {
                role = new UserRepository().getRoleByEmail(email);
            }
            role = role == null ? "" : role.trim();

            if ("CANDIDATO".equalsIgnoreCase(role)) {
                boolean hasAvatar = false;
                boolean hasDisciplinas = false;

                if (email != null && !email.isBlank()) {
                    String avatarUrl = new UserRepository().getAvatarUrlByEmail(email);
                    hasAvatar = avatarUrl != null && !avatarUrl.isBlank();
                }
                if (userId != null) {
                    hasDisciplinas = new ProgressoALunoDisciplinaRepository().hasAny(userId);
                }

                if (!hasDisciplinas) {
                    if (!hasAvatar) {
                        App.swapContent(contentHost, FXML_ADD_IMAGE);
                        return;
                    }
                    App.swapContent(contentHost, FXML_CHOOSE_DISCIPLINAS);
                    return;
                }

                CandidatoRoute(contentHost);
                return;
            }

            contentHost.getChildren().setAll(new Label("Este perfil ja nao e suportado. Usa uma conta de candidato."));
        } catch (RuntimeException e) {
            e.printStackTrace();
            contentHost.getChildren().setAll(new Label("Erro inesperado ao continuar."));
        }
    }

    public static void goToCandidateDisciplinas(StackPane contentHost) {
        if (contentHost == null) {
            return;
        }
        App.swapContent(contentHost, FXML_CHOOSE_DISCIPLINAS);
    }
}
