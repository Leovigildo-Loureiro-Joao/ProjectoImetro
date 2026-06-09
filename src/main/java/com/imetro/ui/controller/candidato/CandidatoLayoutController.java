package com.imetro.ui.controller.candidato;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import com.imetro.domain.CacheService;
import com.imetro.domain.model.Candidato;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.remixicon.RemixiconAL;
import com.imetro.App;
import com.imetro.config.RuntimeConfig;
import com.imetro.domain.dto.candidato.DashboardMelhoriaResumo;
import com.imetro.domain.dto.MenuEntry;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoEstado;
import com.imetro.domain.dto.stats.Stats;
import com.imetro.domain.enums.NivelDisciplina;
import com.imetro.persistence.repository.UserRepository;
import com.imetro.services.CandidatoService;
import com.imetro.services.DiagnosticoService;
import com.imetro.services.PerguntasBootstrapAsyncService;
import com.imetro.services.PlaneamentoEstudoService;
import com.imetro.ui.components.Item_Cell;
import com.imetro.ui.OnboardingRouter;
import com.imetro.ui.support.PlaneamentoEstudoBannerSupport;
import com.imetro.util.Authentication;
import com.imetro.util.AvatarSupport;
import com.imetro.util.ProfileSessionState;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class CandidatoLayoutController implements Initializable {
    private static final String BANNER_RUNNING_CLASS = "bootstrap-banner-running";
    private static final String BANNER_SUCCESS_CLASS = "bootstrap-banner-success";
    private static final String BANNER_WARNING_CLASS = "bootstrap-banner-warning";
    private static final String BANNER_ERROR_CLASS = "bootstrap-banner-error";
    private final DiagnosticoService diagnosticoService=new DiagnosticoService();
    private final CandidatoService candidatoService = new CandidatoService();
    private final PlaneamentoEstudoService planeamentoService = new PlaneamentoEstudoService();

    @FXML
    private VBox bootstrapBanner;

    @FXML
    private Label bootstrapDetailLabel;

    @FXML
    private Label bootstrapPercentLabel;

    @FXML
    private ProgressBar bootstrapProgressBar;

    @FXML
    private Label bootstrapTitleLabel;

    @FXML
    private StackPane contentHost;

    @FXML
    private Label dbModeBanner;

    @FXML
    private VBox layoutPlanBanner;

    @FXML
    private Label layoutPlanDetailLabel;

    @FXML
    private Label layoutPlanTitleLabel;

    @FXML
    private ListView<MenuEntry> menu;

    @FXML
    private VBox sidebar;

    @FXML
    private Label sidebarDaysValue;

    @FXML
    private Label sidebarLevelValue;

    @FXML
    private ImageView sidebarLogo;

    @FXML
    private Label sidebarMatchValue;

    @FXML
    private ImageView topbarAvatarImage;

    @FXML
    private Label topbarAvatarInitialsLabel;

    @FXML
    private HBox sidebarSummary;

    private final UserRepository userRepository = new UserRepository();
    private final PerguntasBootstrapAsyncService perguntasBootstrapAsyncService =
        PerguntasBootstrapAsyncService.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boolean dbEnabled = RuntimeConfig.isDbEnabled();
        if (dbModeBanner != null) {
            dbModeBanner.setVisible(!dbEnabled);
            dbModeBanner.setManaged(!dbEnabled);
        }

        configureBootstrapBanner();
        configureTopBar();
        refreshSidebarSummaryAsync();

        menu.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(MenuEntry item, boolean empty) {
                super.updateItem(item, empty);
                setText(null);
                setGraphic(empty || item == null ? null : new Item_Cell(item.title(), item.icon()));
            }
        });

        menu.getItems().setAll(
            new MenuEntry("dashboard", "Dashboard", RemixiconAL.LAYOUT_GRID_FILL),
            new MenuEntry("add_livro", "Add Livro", FontAwesomeSolid.FILE_ALT),
            new MenuEntry("diagnostico", "Diagnóstico", FontAwesomeSolid.BOLT),
            new MenuEntry("exame_adaptativo", "Exame Adaptativo", FontAwesomeSolid.FIRE),
            new MenuEntry("relatorios", "Relatórios", FontAwesomeSolid.CHART_LINE),
            new MenuEntry("bolsas", "Recomendações", FontAwesomeSolid.STAR),
            new MenuEntry("perfil", "Perfil", FontAwesomeSolid.USER),
            new MenuEntry("configuracao", "Configurações", RemixiconAL.FILE_SETTINGS_FILL),
            new MenuEntry("logout", "Logout", FontAwesomeSolid.SIGN_OUT_ALT)
        );

        menu.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                navigate(newValue.key());
            }
        });

        UUID candidatoId = Authentication.getCurrentUserId();
        if (candidatoId != null) {
            perguntasBootstrapAsyncService.startIfNeeded(candidatoId);
        }
        Platform.runLater(this::openInitialContent);
        Platform.runLater(this::atualizarBannerPlaneamento);
    }

    private void FirstDiagnostic(){
        UUID candidatoId = Authentication.getCurrentUserId();
        if (candidatoId != null && !diagnosticoService.temHistoricoDiagnostico(candidatoId)) {
            navigate("diagnostico");
        }
    }

    private void configureBootstrapBanner() {
        if (bootstrapBanner == null) {
            return;
        }

        bootstrapBanner.managedProperty().bind(bootstrapBanner.visibleProperty());
        bootstrapBanner.visibleProperty().bind(perguntasBootstrapAsyncService.showBannerProperty());

        if (bootstrapTitleLabel != null) {
            bootstrapTitleLabel.textProperty().bind(perguntasBootstrapAsyncService.titleProperty());
        }
        if (bootstrapDetailLabel != null) {
            bootstrapDetailLabel.textProperty().bind(perguntasBootstrapAsyncService.detailProperty());
        }
        if (bootstrapProgressBar != null) {
            bootstrapProgressBar.progressProperty().bind(perguntasBootstrapAsyncService.progressProperty());
        }
        if (bootstrapPercentLabel != null) {
            bootstrapPercentLabel.textProperty().bind(Bindings.createStringBinding(() -> {
                double value = perguntasBootstrapAsyncService.progressProperty().get();
                if (value < 0) {
                    return "Em curso";
                }
                return Math.round(value * 100.0) + "%";
            }, perguntasBootstrapAsyncService.progressProperty()));
        }

        updateBootstrapBannerStyle(perguntasBootstrapAsyncService.getState());
        perguntasBootstrapAsyncService.stateProperty().addListener((obs, oldState, newState) ->
            updateBootstrapBannerStyle(newState)
        );
    }

    private void updateBootstrapBannerStyle(PerguntasBootstrapAsyncService.BootstrapUiState state) {
        if (bootstrapBanner == null) {
            return;
        }

        bootstrapBanner.getStyleClass().removeAll(
            BANNER_RUNNING_CLASS,
            BANNER_SUCCESS_CLASS,
            BANNER_WARNING_CLASS,
            BANNER_ERROR_CLASS
        );

        if (state == null) {
            return;
        }

        switch (state) {
            case RUNNING -> bootstrapBanner.getStyleClass().add(BANNER_RUNNING_CLASS);
            case SUCCESS -> bootstrapBanner.getStyleClass().add(BANNER_SUCCESS_CLASS);
            case WARNING -> bootstrapBanner.getStyleClass().add(BANNER_WARNING_CLASS);
            case ERROR -> bootstrapBanner.getStyleClass().add(BANNER_ERROR_CLASS);
            case IDLE -> {
            }
        }
    }

    private void atualizarBannerPlaneamento() {
        if (contentHost == null || contentHost.getScene() == null) {
            return;
        }

        PlaneamentoEstudoEstado estado = planeamentoService.resolverEstadoAtual(Authentication.getCurrentUserId());
        PlaneamentoEstudoBannerSupport.aplicar(contentHost.getScene(), estado);
    }

    private void configureTopBar() {
        if (sidebarLogo != null) {
            Image logo = loadSidebarLogo();
            if (logo != null) {
                sidebarLogo.setImage(logo);
            }
        }

        updateTopBarAvatar();
    }

    public void refreshTopBarProfile() {
        configureTopBar();
    }

    private void refreshSidebarSummaryAsync() {
        App.getExecutorService().execute(() -> {
            try {
                UUID candidatoId = Authentication.getCurrentUserId();
                DashboardMelhoriaResumo melhoriaResumo = candidatoService.calcularResumoMelhorias(candidatoId);
                Stats stats = candidatoService.CalcularStats();

                long diasPraticados = melhoriaResumo.semana().stream()
                    .filter(dia -> dia.melhorias() > 0)
                    .count();
                String diasText = diasPraticados + "/7";
                String matchText = Math.round(clamp(melhoriaResumo.taxaSucessoPercentual(), 0d, 100d)) + "%";
                String levelText = resolveSidebarLevel(stats);

                Platform.runLater(() -> {
                    if (sidebarDaysValue != null) {
                        sidebarDaysValue.setText(diasText);
                    }
                    if (sidebarMatchValue != null) {
                        sidebarMatchValue.setText(matchText);
                    }
                    if (sidebarLevelValue != null) {
                        sidebarLevelValue.setText(levelText);
                    }
                    configureTopBar();
                });
            } catch (Exception e) {
                System.err.println("Falha ao atualizar o resumo do topBar: " + e.getMessage());
            }
        });
    }

    private void openInitialContent() {
        if (contentHost == null) {
            return;
        }

        if (OnboardingRouter.isCandidateOnboardingPending()) {
            if (!OnboardingRouter.hasAvatarConfigured()) {
                App.swapContent(contentHost, OnboardingRouter.FXML_ADD_IMAGE);
            } else {
                App.swapContent(contentHost, OnboardingRouter.FXML_CHOOSE_DISCIPLINAS);
            }
            return;
        }

        if (menu != null) {
            menu.getSelectionModel().selectFirst();
        } else {
            try {
                openDashboard();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void openDashboard() throws IOException {
        App.swapContent(contentHost, "views/pages/candidato/dashboard");
    }

    @FXML
    private void openTestes() throws IOException {
        App.swapContent(contentHost, "views/pages/candidato/testes");
    }

    @FXML
    private void logout() throws IOException {
        App.setRoot("views/layouts/AuthLayout");
    }

    @FXML
    private void PopPup(ActionEvent a){
        Platform.runLater(() ->   {
            if (menu.getStyleClass().contains("min")) {
                menu.getStyleClass().remove("min");

                Timeline p = new Timeline(
                    new KeyFrame(Duration.seconds(.3), new KeyValue(sidebar.prefWidthProperty(),69),new KeyValue(sidebar.prefWidthProperty(),240)));
                    p.play();

            }else{
                menu.getStyleClass().add("min");
                Timeline p = new Timeline(
                    new KeyFrame(Duration.seconds(.3), new KeyValue(sidebar.prefWidthProperty(),240),new KeyValue(sidebar.prefWidthProperty(),64)));
                    p.play();

            }
        });
    }

    private void navigate(String key) {
        List<String> keys= List.of(
            "dashboard",
            "add_livro",
            "diagnostico",
            "exame_adaptativo",
            "relatorios",
            "bolsas",
            "perfil",
            "configuracao",
            "logout"
        );
        menu.getSelectionModel().select(keys.indexOf(key));
        try {
            switch (key) {
                case "dashboard" -> openDashboard();
                case "add_livro" -> App.swapContent(contentHost, "views/pages/candidato/add-livro");
                case "diagnostico" -> App.swapContent(contentHost, "views/pages/candidato/diagnostico");
                case "exame_adaptativo" -> openTestes();
                case "relatorios" -> App.swapContent(contentHost, "views/pages/candidato/relatorios");
                case "bolsas" -> App.swapContent(contentHost, "views/pages/candidato/bolsas");
                case "perfil" -> App.swapContent(contentHost, "views/pages/candidato/perfil");
                case "configuracao" -> App.swapContent(contentHost, "views/pages/candidato/configuracao");
                case "logout" -> logout();
                default -> openDashboard();
            }
            if (!"logout".equals(key)) {
                refreshSidebarSummaryAsync();
                atualizarBannerPlaneamento();
            }
        } catch (IOException ignored) {

        }
    }

    private Image loadSidebarLogo() {
        return loadImage("/com/imetro/assets/imgs/icone_solid.png");
    }

    private Image loadImage(String resourcePath) {
        URL resource = App.class.getResource(resourcePath);
        if (resource == null) {
            return null;
        }
        return new Image(resource.toExternalForm());
    }

    private void updateTopBarAvatar() {
        if (topbarAvatarImage == null && topbarAvatarInitialsLabel == null) {
            return;
        }

        String email = Authentication.getCurrentUserEmail();
        String avatarRef = resolveStoredAvatar(email);
        String displayName = resolveTopBarDisplayName(email);
        Image image = AvatarSupport.loadAvatarImage(avatarRef);
        boolean hasImage = image != null;

        if (topbarAvatarImage != null) {
            topbarAvatarImage.setImage(image);
            topbarAvatarImage.setVisible(hasImage);
            topbarAvatarImage.setManaged(hasImage);
        }

        if (topbarAvatarInitialsLabel != null) {
            topbarAvatarInitialsLabel.setText(AvatarSupport.previewFallbackLabel(avatarRef, displayName, email));
            topbarAvatarInitialsLabel.setVisible(!hasImage);
            topbarAvatarInitialsLabel.setManaged(!hasImage);
        }
    }

    private String resolveStoredAvatar(String email) {
        String rememberedAvatar = ProfileSessionState.resolveAvatar(email, AvatarSupport.INITIALS_TOKEN);
        if (!AvatarSupport.INITIALS_TOKEN.equals(rememberedAvatar) || !RuntimeConfig.isDbEnabled()) {
            return rememberedAvatar;
        }

        if (email == null || email.isBlank()) {
            return rememberedAvatar;
        }

        try {
            String fallbackAvatar = userRepository.getAvatarUrlByEmail(email);
            return ProfileSessionState.resolveAvatar(email, fallbackAvatar);
        } catch (RuntimeException ignored) {
            return rememberedAvatar;
        }
    }

    private String resolveTopBarDisplayName(String email) {
        Object cachedUser = CacheService.get("currentUser");
        String fallbackName = cachedUser instanceof Candidato candidato ? candidato.getNome() : null;
        String resolvedName = ProfileSessionState.resolveName(email, fallbackName);
        if (resolvedName != null && !resolvedName.isBlank()) {
            return resolvedName;
        }

        if (RuntimeConfig.isDbEnabled() && email != null && !email.isBlank()) {
            try {
                String nome = userRepository.getNomeByEmail(email);
                if (nome != null && !nome.isBlank()) {
                    return nome;
                }
            } catch (RuntimeException ignored) {
            }
        }

        if (email != null && !email.isBlank()) {
            return email;
        }

        return "Candidato";
    }

    private String resolveSidebarLevel(Stats stats) {
        double media = average(
            stats.velocidade(),
            stats.precisao(),
            stats.consistencia(),
            stats.logica(),
            stats.resiliencia()
        );

        double percentual = clamp(media * 100d, 0d, 100d);
        if (percentual < 35d) {
            return NivelDisciplina.INICIANTE.getDescricao().toUpperCase();
        }
        if (percentual < 70d) {
            return NivelDisciplina.INTERMEDIARIO.getDescricao().toUpperCase();
        }
        return NivelDisciplina.AVANCADO.getDescricao().toUpperCase();
    }

    private double average(double... values) {
        double total = 0d;
        int count = 0;
        for (double value : values) {
            total += value;
            count++;
        }
        return count == 0 ? 0d : total / count;
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
