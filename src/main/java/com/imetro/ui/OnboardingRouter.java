package com.imetro.ui;

import java.io.IOException;
import java.util.UUID;

import com.imetro.App;
import com.imetro.persistence.repository.CandidatoDisciplinaRepository;
import com.imetro.persistence.repository.OrientadorDisciplinaRepository;
import com.imetro.persistence.repository.UserRepository;
import com.imetro.util.Authentication;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public final class OnboardingRouter {

    public static final String FXML_ADD_IMAGE = "views/pages/auth/onboarding/add-image";
    public static final String FXML_CHOOSE_DISCIPLINAS = "views/pages/auth/onboarding/choose-disciplinas";
    public static final String FXML_ORIENTADOR_DISCIPLINA = "views/pages/auth/onboarding/orientador-disciplina";

    private OnboardingRouter() {
    }

    public static void routeAfterAuth(StackPane contentHost) {
        if (contentHost == null) {
            return;
        }

        String role = Authentication.getCurrentUserRole();
        String email = Authentication.getCurrentUserEmail();
        UUID userId = parseUuid(Authentication.getCurrentUserId());

        try {
            if ("CANDIDATO".equalsIgnoreCase(role)) {
                boolean hasAvatar = false;
                boolean hasDisciplinas = false;

                if (email != null && !email.isBlank()) {
                    String avatarUrl = new UserRepository().getAvatarUrlByEmail(email);
                    hasAvatar = avatarUrl != null && !avatarUrl.isBlank();
                }
                if (userId != null) {
                    hasDisciplinas = new CandidatoDisciplinaRepository().hasAny(userId);
                }

                if (!hasDisciplinas) {
                    if (!hasAvatar) {
                        App.swapContent(contentHost, FXML_ADD_IMAGE);
                        return;
                    }
                    App.swapContent(contentHost, FXML_CHOOSE_DISCIPLINAS);
                    return;
                }
                App.setRoot("views/layouts/CandidatoLayout");
                return;
            }

            if ("ORIENTADOR".equalsIgnoreCase(role)) {
                boolean hasDisciplina = userId != null && new OrientadorDisciplinaRepository().hasAny(userId);
                if (!hasDisciplina) {
                    App.swapContent(contentHost, FXML_ORIENTADOR_DISCIPLINA);
                    return;
                }
                App.setRoot("views/layouts/OrientadorLayout");
                return;
            }

            contentHost.getChildren().setAll(new Label("Perfil inválido. Faça login novamente."));
        } catch (IOException e) {
            System.err.println("Falha ao navegar após autenticação.");
            e.printStackTrace();
            contentHost.getChildren().setAll(new Label("Erro ao abrir a próxima tela."));
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

    private static UUID parseUuid(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
