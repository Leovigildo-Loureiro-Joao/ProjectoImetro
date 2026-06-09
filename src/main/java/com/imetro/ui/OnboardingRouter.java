package com.imetro.ui;

import java.io.IOException;
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
            String email = Authentication.getCurrentUserEmail();
            Candidato candidato = carregarCandidato(userId, email);
            CacheService.put("currentUser", candidato);
            App.setRoot("views/layouts/CandidatoLayout");
        } catch (Exception e) {
            try {
                CacheService.put("currentUser", criarCandidatoFallback(
                    Authentication.getCurrentUserId(),
                    Authentication.getCurrentUserEmail()
                ));
                App.setRoot("views/layouts/CandidatoLayout");
            } catch (IOException ioException) {
                if (contentHost != null) {
                    contentHost.getChildren().setAll(new Label("Erro inesperado ao continuar."));
                }
            }
        }
    }

    private static Candidato carregarCandidato(UUID userId, String email) {
        if (!RuntimeConfig.isDbEnabled()) {
            return criarCandidatoFallback(userId, email);
        }

        try {
            if (userId != null) {
                Candidato candidato = CANDIDATO_SERVICE.getCandidatoById(userId);
                if (candidato != null) {
                    return candidato;
                }
            }

            if (email != null && !email.isBlank()) {
                String nome = new UserRepository().getNomeByEmail(email);
                if (nome != null && !nome.isBlank()) {
                    return new Candidato(userId, nome, email, null, LocalDateTime.now());
                }
            }
        } catch (Exception ignored) {
        }

        return criarCandidatoFallback(userId, email);
    }

    private static Candidato criarCandidatoFallback(UUID userId, String email) {
        String emailNormalizado = email == null ? "" : email;
        String nome = email == null || email.isBlank() ? "Candidato" : email;
        return new Candidato(
            userId,
            nome,
            emailNormalizado,
            null,
            LocalDateTime.now()
        );
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
        try {
            if ((role == null || role.isBlank()) && email != null && !email.isBlank()) {
                role = new UserRepository().getRoleByEmail(email);
            }
            role = role == null ? "" : role.trim();

            if ("CANDIDATO".equalsIgnoreCase(role)) {
                CandidatoRoute(contentHost);
                return;
            }

            contentHost.getChildren().setAll(new Label("Este perfil ja nao e suportado. Usa uma conta de candidato."));
        } catch (RuntimeException e) {
            e.printStackTrace();
            contentHost.getChildren().setAll(new Label("Erro inesperado ao continuar."));
        }
    }

    public static boolean isCandidateOnboardingPending() {
        if (!RuntimeConfig.isDbEnabled()) {
            return false;
        }

        return !hasAvatarConfigured() || !hasDisciplinasConfigured();
    }

    public static boolean hasAvatarConfigured() {
        if (!RuntimeConfig.isDbEnabled()) {
            return true;
        }

        String email = Authentication.getCurrentUserEmail();
        if (email == null || email.isBlank()) {
            return false;
        }

        try {
            String avatarUrl = new UserRepository().getAvatarUrlByEmail(email);
            return avatarUrl != null && !avatarUrl.isBlank();
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static boolean hasDisciplinasConfigured() {
        if (!RuntimeConfig.isDbEnabled()) {
            return true;
        }

        UUID userId = Authentication.getCurrentUserId();
        if (userId == null) {
            return false;
        }

        try {
            return new ProgressoALunoDisciplinaRepository().hasAny(userId);
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void goToCandidateDisciplinas(StackPane contentHost) {
        if (contentHost == null) {
            return;
        }
        App.swapContent(contentHost, FXML_CHOOSE_DISCIPLINAS);
    }
}
