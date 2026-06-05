package com.imetro.ui.controller.candidato;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

import com.imetro.App;
import com.imetro.config.RuntimeConfig;
import com.imetro.domain.CacheService;
import com.imetro.domain.dto.candidato.DashboardMelhoriaDia;
import com.imetro.domain.dto.candidato.DashboardMelhoriaResumo;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoEtapa;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoResumo;
import com.imetro.domain.dto.progresso.ProgressoAlunoDisciplinaDto;
import com.imetro.domain.dto.stats.Stats;
import com.imetro.domain.model.Candidato;
import com.imetro.persistence.repository.MedalhaRepository;
import com.imetro.services.CandidatoService;
import com.imetro.services.DisciplinaService;
import com.imetro.services.PlaneamentoEstudoService;
import com.imetro.ui.components.CircleProgress;
import com.imetro.util.Authentication;
import com.imetro.util.MedalSupport;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class DashboardOrientadoController implements Initializable {

    private static final Locale LOCALE_PT = new Locale("pt", "AO");
    private static final DateTimeFormatter HEADER_DATE_FORMAT = DateTimeFormatter.ofPattern("HH:mm EEEE dd MMM yyyy", LOCALE_PT);
    private static final DateTimeFormatter CARD_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy", LOCALE_PT);
    private static final DateTimeFormatter CHART_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM", LOCALE_PT);

    @FXML
    private Label welcome;

    @FXML
    private Label localDate;

    @FXML
    private Label progressText;

    @FXML
    private Label next_level;

    @FXML
    private StackPane progresso;

    @FXML
    private Label heroSummary;

    @FXML
    private Label heroFocus;

    @FXML
    private Label achievementBadge;

    @FXML
    private Label achievementTitle;

    @FXML
    private Label achievementSummary;

    @FXML
    private Label achievementMeta;

    @FXML
    private Label planBadge;

    @FXML
    private Label planTitle;

    @FXML
    private Label planSummary;

    @FXML
    private Label planFocus;

    @FXML
    private Label planMeta;

    @FXML
    private VBox planStepsBox;

    @FXML
    private Label disciplineBadge;

    @FXML
    private Label disciplineSummary;

    @FXML
    private HBox achievementCardsBox;

    @FXML
    private ListView<ProgressoAlunoDisciplinaDto> status_disciplina;

    @FXML
    private Label sequenceBadge;
    @FXML
    private Label desc;

    @FXML
    private Label sequenceTitle;

    @FXML
    private Label sequenceSummary;

    @FXML
    private AreaChart<String, Number> areaActivityChart;

    @FXML
    private VBox tela;

    private final CandidatoService candidatoService = new CandidatoService();
    private final PlaneamentoEstudoService planeamentoService = new PlaneamentoEstudoService();
    private final MedalhaRepository medalhaRepository = new MedalhaRepository();

    private Candidato candidato;
    private PlaneamentoEstudoResumo planeamentoResumo;
    private DashboardMelhoriaResumo dashboardMelhoriaResumo = DashboardMelhoriaResumo.empty();

    private double VELOCIDADE_TARGET = 0d;
    private double LOGICA_TARGET = 0d;
    private double PRECISAO_TARGET = 0d;
    private double RESILIENCIA_TARGET = 0d;
    private double CONSISTENCIA_TARGET = 0d;
    private double PROGRESSO_TARGET = 0d;

    @FXML
    public void StartDiagnostic(javafx.event.ActionEvent event) {
        StackPane contentHost = getContentHost();
        if (contentHost != null) {
            App.swapContent(contentHost, "views/pages/candidato/diagnostico");
        }
    }

    @FXML
    public void StartExam(javafx.event.ActionEvent event) {
        StackPane contentHost = getContentHost();
        if (contentHost != null) {
            App.swapContent(contentHost, "views/pages/candidato/testes");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Object currentUser = CacheService.get("currentUser");
        if (currentUser instanceof Candidato cachedCandidato) {
            candidato = cachedCandidato;
        } else {
            candidato = new Candidato();
            String email = Authentication.getCurrentUserEmail();
            candidato.setNome("Candidato");
            candidato.setEmail(email == null ? "" : email);
        }

        setup();
    }

    private void setup() {
        updateHeader();
        dashboardMelhoriaResumo = candidatoService.calcularResumoMelhorias(candidato.getIdCandidato());
        planeamentoResumo = planeamentoService.gerarResumo(candidato.getIdCandidato());
        setupHeroProgress();
        setupHeroCopy();
        setupAchievementCard();
        setupPlanCard();
        setupSequenceChart();
        setupDisciplineProgress();
        animateSections();
    }

    private void updateHeader() {
        if (welcome != null) {
            int hour=LocalTime.now().getHour();
            if (hour>=0 && hour<=5) {
                welcome.setText("Oi Batman dos estudos!");
                desc.setText("Humm! vejo que estas bem disposto hoje.");

            }
            if (hour>=6 && hour<=11) {
                welcome.setText("Ola " + safeName(candidato.getNome()) + "!");
                desc.setText("Pronto para mais um salto de conhecimento.");
            }
            if (hour>=12 && hour<=18) {
                welcome.setText("Dobre a Rotina " + safeName(candidato.getNome()) + "!");
                desc.setText("Seja preciso seu tempo é valioso.");
            }
            if (hour>=19 && hour<=23) {
                welcome.setText("Boa noite! " + safeName(candidato.getNome()) + "!");
                desc.setText("Seja rquilibrado consiguo mesmo se foque no necessario ");
            }
        }
        if (localDate != null) {
            localDate.setText(LocalDateTime.now().format(HEADER_DATE_FORMAT));
        }
    }

    private void setupHeroProgress() {
        Stats stats = candidatoService.CalcularStats();
        VELOCIDADE_TARGET = stats.velocidade();
        LOGICA_TARGET = stats.logica();
        PRECISAO_TARGET = stats.precisao();
        RESILIENCIA_TARGET = stats.resiliencia();
        CONSISTENCIA_TARGET = stats.consistencia();
        PROGRESSO_TARGET = (VELOCIDADE_TARGET + LOGICA_TARGET + PRECISAO_TARGET + RESILIENCIA_TARGET + CONSISTENCIA_TARGET) / 5.0;

        if (progressText != null) {
            progressText.setText(formatPercent(PROGRESSO_TARGET) + " progresso");
        }

        if (next_level != null) {
            next_level.setText(resolveNextLevel(PROGRESSO_TARGET));
        }

        if (progresso != null) {
            progresso.getChildren().clear();
            CircleProgress circleProgress = new CircleProgress(60, 60);
            circleProgress.setValue(PROGRESSO_TARGET);
            progresso.getChildren().add(circleProgress);
        }
    }

    private void setupHeroCopy() {
        if (heroSummary != null) {
            heroSummary.setText(planeamentoResumo.resumoHero());
        }
        if (heroFocus != null) {
            heroFocus.setText("Foco actual: " + safeText(planeamentoResumo.focoAtual(), "—") + " · ritmo " + safeText(planeamentoResumo.ritmoMedio(), "—"));
        }
    }

    private void setupAchievementCard() {
        UUID userId = Authentication.getCurrentUserId();
        List<MedalSupport.MedalViewModel> upcoming = buildUpcomingAchievementModels(userId);
        Optional<MedalSupport.MedalAward> recentAward = findRecentAward(userId);
        AchievementCardData data = buildAchievementCardData(upcoming, recentAward);

        if (achievementBadge != null) {
            achievementBadge.setText(data.badge());
        }
        if (achievementTitle != null) {
            achievementTitle.setText(data.title());
        }
        if (achievementSummary != null) {
            achievementSummary.setText(data.summary());
        }
        if (achievementMeta != null) {
            achievementMeta.setText(data.meta());
        }
        if (achievementCardsBox != null) {
            achievementCardsBox.getChildren().setAll(buildAchievementCards(upcoming));
        }
    }

    private AchievementCardData buildAchievementCardData(List<MedalSupport.MedalViewModel> upcoming, Optional<MedalSupport.MedalAward> recentAward) {
        if (upcoming.isEmpty()) {
            return new AchievementCardData(
                "Conquistas",
                "Sem medalhas ainda",
                "A tua primeira conquista vai aparecer assim que o histórico começar.",
                "Liga uma sessão para alimentar este card."
            );
        }

        if (recentAward.isPresent()) {
            MedalSupport.MedalAward award = recentAward.get();
            MedalSupport.MedalDefinition definition = MedalSupport.findByCode(award.medalCode());
            if (definition != null) {
                String recordText = award.recordValue() == null ? "" : " · recorde " + award.recordValue();
                return new AchievementCardData(
                    "Conquista recente",
                    "Próximas conquistas",
                    "A última medalha foi " + definition.title() + ". As três próximas aparecem abaixo, sem scroll.",
                    "Actualizada em " + formatDate(award.actualizadaAt()) + recordText
                );
            }
        }

        MedalSupport.MedalViewModel first = upcoming.getFirst();
        MedalSupport.MedalDefinition definition = first.definition();
        String badge = upcoming.size() + (upcoming.size() == 1 ? " meta" : " metas");
        String title = "Próximas conquistas";
        String summary = "As três mais próximas de desbloquear aparecem abaixo, sem scroll.";
        String meta = "Primeira meta: " + definition.title() + " · faltam " + first.remainingToUnlock() + " " + definition.targetUnit();

        return new AchievementCardData(badge, title, summary, meta);
    }

    private List<MedalSupport.MedalViewModel> buildUpcomingAchievementModels(UUID userId) {
        List<MedalSupport.MedalViewModel> medals = buildMedalViewModels(userId);
        List<MedalSupport.MedalViewModel> upcoming = medals.stream()
            .filter(medal -> !medal.unlocked())
            .limit(3)
            .toList();

        if (upcoming.size() >= 3) {
            return upcoming;
        }

        List<MedalSupport.MedalViewModel> merged = new ArrayList<>(upcoming);
        for (MedalSupport.MedalViewModel medal : medals) {
            if (merged.size() >= 3) {
                break;
            }
            if (!merged.contains(medal)) {
                merged.add(medal);
            }
        }

        return merged;
    }

    private List<Node> buildAchievementCards(List<MedalSupport.MedalViewModel> upcoming) {
        List<Node> cards = new ArrayList<>();
        for (int index = 0; index < 3; index++) {
            MedalSupport.MedalViewModel medal = index < upcoming.size() ? upcoming.get(index) : null;
            cards.add(buildAchievementCard(index + 1, medal));
        }
        return cards;
    }

    private VBox buildAchievementCard(int position, MedalSupport.MedalViewModel medal) {
        StackPane imageShell = new StackPane();
        imageShell.getStyleClass().add("achievement-mini-shell");

        Label badge = new Label(String.format("%02d", position));
        badge.getStyleClass().addAll("achievement-mini-badge", "achievement-mini-badge-" + position);

        Label title = new Label();
        title.getStyleClass().add("achievement-mini-title");
        title.setWrapText(true);
        title.setMinWidth(0);
        title.setMaxWidth(Double.MAX_VALUE);

        Label detail = new Label();
        detail.getStyleClass().add("achievement-mini-copy");
        detail.setWrapText(true);
        detail.setMinWidth(0);
        detail.setMaxWidth(Double.MAX_VALUE);

        VBox textBox = new VBox(3.0, title, detail);
        textBox.setFillWidth(true);
        textBox.setMaxWidth(Double.MAX_VALUE);

        if (medal != null) {
            MedalSupport.MedalDefinition definition = medal.definition();
            Image image = MedalSupport.loadMedalImage(definition.imageRef());
            if (image != null) {
                ImageView icon = new ImageView(image);
                icon.setFitHeight(44.0);
                icon.setFitWidth(44.0);
                icon.setPreserveRatio(true);
                icon.setSmooth(true);
                imageShell.getChildren().add(icon);
            }

            title.setText(definition.title());
            detail.setText(medal.unlocked()
                ? "Já desbloqueada"
                : "Faltam " + medal.remainingToUnlock() + " " + definition.targetUnit());
        } else {
            title.setText("Mais por vir");
            detail.setText("Liga uma sessão para revelar a próxima conquista.");
        }

        VBox card = new VBox(6.0, badge, imageShell, textBox);
        card.getStyleClass().addAll("achievement-mini-card", "achievement-mini-card-" + position);
        card.setAlignment(Pos.TOP_LEFT);
        card.setFillWidth(true);
        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(card, Priority.ALWAYS);
        return card;
    }

    private void setupPlanCard() {
        List<PlaneamentoEstudoEtapa> etapas = planeamentoResumo == null || planeamentoResumo.etapas() == null
            ? List.of()
            : planeamentoResumo.etapas();

        if (planBadge != null) {
            planBadge.setText(etapas.isEmpty() ? "Hoje" : etapas.size() + (etapas.size() == 1 ? " passo" : " passos"));
        }
        if (planTitle != null) {
            planTitle.setText(etapas.isEmpty() ? "Plano de hoje" : "Plano diário em 3 blocos");
        }
        if (planSummary != null) {
            planSummary.setText(etapas.isEmpty()
                ? planeamentoResumo.resumoHero()
                : "Mostrando só as 3 etapas pendentes do plano diário.");
        }
        if (planFocus != null) {
            planFocus.setText("Foco actual: " + safeText(planeamentoResumo.focoAtual(), "—"));
        }
        if (planMeta != null) {
            planMeta.setText("Acerto médio " + safeText(planeamentoResumo.acertoMedio(), "—") + " · ritmo " + safeText(planeamentoResumo.ritmoMedio(), "—"));
        }
        if (planStepsBox != null) {
            planStepsBox.getChildren().setAll(buildPlanStepCards(etapas));
        }
    }

    private List<Node> buildPlanStepCards(List<PlaneamentoEstudoEtapa> etapas) {
        List<Node> cards = new ArrayList<>();
        for (int index = 0; index < 3; index++) {
            PlaneamentoEstudoEtapa etapa = index < etapas.size() ? etapas.get(index) : null;
            cards.add(buildPlanStepCard(index + 1, etapa));
        }
        return cards;
    }

    private HBox buildPlanStepCard(int number, PlaneamentoEstudoEtapa etapa) {
        String window = etapa == null ? fallbackPlanWindow(number) : safeText(etapa.janela(), fallbackPlanWindow(number));
        String title = etapa == null ? fallbackPlanTitle(number) : safeText(etapa.acao(), fallbackPlanTitle(number));
        String detail = etapa == null ? fallbackPlanDetail(number) : safeText(etapa.detalhe(), fallbackPlanDetail(number));

        Label stepNumber = new Label(String.valueOf(number));
        stepNumber.getStyleClass().addAll("plan-step-number", "plan-step-number-" + number);
        stepNumber.setAlignment(Pos.CENTER);
        stepNumber.setMinSize(38.0, 38.0);
        stepNumber.setPrefSize(38.0, 38.0);
        stepNumber.setMaxSize(38.0, 38.0);

        Label windowLabel = new Label(window);
        windowLabel.getStyleClass().add("plan-step-window");
        windowLabel.setWrapText(true);
        windowLabel.setMaxWidth(Double.MAX_VALUE);
        windowLabel.setMinWidth(0);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("plan-step-title");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setMinWidth(0);

        Label detailLabel = new Label(detail);
        detailLabel.getStyleClass().add("plan-step-detail");
        detailLabel.setWrapText(true);
        detailLabel.setMaxWidth(Double.MAX_VALUE);
        detailLabel.setMinWidth(0);

        VBox textBox = new VBox(2.0, windowLabel, titleLabel, detailLabel);
        textBox.setFillWidth(true);
        textBox.setMaxWidth(Double.MAX_VALUE);
        textBox.setMinWidth(0);

        HBox card = new HBox(10.0, stepNumber, textBox);
        card.getStyleClass().addAll("plan-step-card", "plan-step-card-" + number);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setFillHeight(false);
        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(textBox, Priority.ALWAYS);
        return card;
    }

    private String fallbackPlanWindow(int number) {
        return switch (number) {
            case 1 -> "Agora";
            case 2 -> "Depois";
            case 3 -> "Fecho";
            default -> "Passo " + number;
        };
    }

    private String fallbackPlanTitle(int number) {
        return switch (number) {
            case 1 -> "Arranque";
            case 2 -> "Ritmo";
            case 3 -> "Revisao";
            default -> "Bloco " + number;
        };
    }

    private String fallbackPlanDetail(int number) {
        return switch (number) {
            case 1 -> safeText(planeamentoResumo.resumoHero(), "Comeca com o plano base do dia.");
            case 2 -> "Mantem a execucao com um bloco curto e focado.";
            case 3 -> "Fecha o dia com uma revisao rapida e mede o progresso.";
            default -> "Mantem a sequencia do dia.";
        };
    }

    private void setupSequenceChart() {
        if (sequenceTitle != null) {
            sequenceTitle.setText("Ritmo dos últimos 7 dias");
        }

        if (areaActivityChart == null) {
            return;
        }

        areaActivityChart.setAnimated(false);
        areaActivityChart.setLegendVisible(false);
        areaActivityChart.setCreateSymbols(true);
        areaActivityChart.setOpacity(0d);

        if (areaActivityChart.getYAxis() instanceof NumberAxis eixoY) {
            eixoY.setAutoRanging(false);
            eixoY.setLowerBound(0d);
            eixoY.setUpperBound(100d);
            eixoY.setTickUnit(20d);
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Taxa de sucesso");

        List<DashboardMelhoriaDia> semana = dashboardMelhoriaResumo.semana();
        int activeDays = 0;
        double totalRate = 0d;

        for (DashboardMelhoriaDia dia : semana) {
            String rotulo = dia.data().format(CHART_DATE_FORMAT);
            double valor = dia.melhorias() <= 0 ? 0d : (dia.sucessos() * 100.0) / dia.melhorias();
            if (dia.melhorias() > 0) {
                activeDays++;
                totalRate += valor;
            }
            series.getData().add(new XYChart.Data<>(rotulo, valor));
        }

        areaActivityChart.getData().clear();
        areaActivityChart.getData().add(series);

        if (sequenceBadge != null) {
            sequenceBadge.setText(activeDays + (activeDays == 1 ? " dia activo" : " dias activos"));
        }
        if (sequenceSummary != null) {
            if (activeDays > 0) {
                long average = Math.round(totalRate / activeDays);
                sequenceSummary.setText("Taxa média de sucesso de " + average + "% nos últimos 7 dias. O gráfico destaca a continuidade do ritmo.");
            } else {
                sequenceSummary.setText("Ainda não há sessões suficientes para medir a sequência. Faz o primeiro teste e este gráfico ganha vida.");
            }
        }
    }

    private void setupDisciplineProgress() {
        List<ProgressoAlunoDisciplinaDto> progressos = new ArrayList<>(DisciplinaService.getProgressoDisciplinasCandidatoSafe());
        progressos.sort(Comparator.comparingDouble((ProgressoAlunoDisciplinaDto item) -> normalizeProgress(item.progresso())).reversed()
            .thenComparing(item -> safeText(item.disciplina(), ""), String.CASE_INSENSITIVE_ORDER));

        if (disciplineBadge != null) {
            disciplineBadge.setText(progressos.size() + (progressos.size() == 1 ? " disciplina" : " disciplinas"));
        }
        if (disciplineSummary != null) {
            disciplineSummary.setText(progressos.isEmpty()
                ? "Ainda não há disciplina activa no histórico."
                : "Progressão actual por disciplina. Só progresso, sem consistência, velocidade ou métricas extra.");
        }

        if (status_disciplina != null) {
            status_disciplina.setCellFactory(list -> new DisciplineProgressCell());
            status_disciplina.setItems(FXCollections.observableArrayList(progressos));
            status_disciplina.setPlaceholder(buildEmptyPlaceholder("Ainda não há progresso por disciplina."));
            status_disciplina.setOpacity(0d);
        }
    }

    private void animateSections() {
        fadeIn(progresso);
        fadeIn(areaActivityChart);
        fadeIn(status_disciplina);
    }

    private void fadeIn(Node node) {
        if (node == null) {
            return;
        }

        FadeTransition fade = new FadeTransition(Duration.millis(700), node);
        fade.setFromValue(0d);
        fade.setToValue(1d);
        fade.play();
    }

    private Optional<MedalSupport.MedalAward> findRecentAward(UUID userId) {
        if (userId == null || !RuntimeConfig.isDbEnabled()) {
            return Optional.empty();
        }

        List<MedalSupport.MedalAward> awards = medalhaRepository.findAwardsByUserId(userId);
        if (awards.isEmpty()) {
            return Optional.empty();
        }

        return awards.stream()
            .filter(award -> award.actualizadaAt() != null && award.actualizadaAt().toLocalDate().equals(LocalDate.now()))
            .max(Comparator.comparing(MedalSupport.MedalAward::actualizadaAt));
    }

    private List<MedalSupport.MedalViewModel> buildMedalViewModels(UUID userId) {
        Map<String, MedalSupport.MedalAward> awardsByCode = new HashMap<>();
        Map<MedalSupport.MedalSkill, Integer> previewProgress = RuntimeConfig.isDbEnabled()
            ? Map.of()
            : MedalSupport.navigationPreviewProgress();

        if (RuntimeConfig.isDbEnabled() && userId != null) {
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
                LocalDateTime earnedAt = null;

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

    private String resolveNextLevel(double progressRatio) {
        double percent = clamp(progressRatio * 100d, 0d, 100d);
        if (percent < 35d) {
            return "INTERMEDIÁRIO";
        }
        if (percent < 70d) {
            return "AVANÇADO";
        }
        return "EXCELENTE";
    }

    private Label buildEmptyPlaceholder(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("muted");
        label.setWrapText(true);
        return label;
    }

    private double normalizeProgress(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return 0d;
        }
        if (value <= 1d) {
            return clamp(value * 100d, 0d, 100d);
        }
        return clamp(value, 0d, 100d);
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private String formatPercent(double ratio) {
        return Math.round(clamp(ratio, 0d, 1d) * 100d) + "%";
    }

    private String formatDate(LocalDateTime value) {
        if (value == null) {
            return "sem data";
        }
        return value.format(CARD_DATE_FORMAT);
    }

    private String safeName(String value) {
        return safeText(value, "Candidato");
    }

    private String safeText(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        return value;
    }

    private StackPane getContentHost() {
        if (tela == null || tela.getParent() == null) {
            return null;
        }
        if (tela.getParent() instanceof StackPane host) {
            return host;
        }
        return null;
    }

    private record AchievementCardData(String badge, String title, String summary, String meta) {
    }

    private static final class DisciplineProgressCell extends ListCell<ProgressoAlunoDisciplinaDto> {
        private final Label name = new Label();
        private final Label percent = new Label();
        private final ProgressBar bar = new ProgressBar();
        private final Region spacer = new Region();
        private final HBox header = new HBox(10, name, spacer, percent);
        private final VBox root = new VBox(8, header, bar);

        private DisciplineProgressCell() {
            root.getStyleClass().add("discipline-progress-row");
            name.getStyleClass().add("discipline-progress-title");
            percent.getStyleClass().add("discipline-progress-percent");
            header.setAlignment(Pos.CENTER_LEFT);
            root.setFillWidth(true);
            HBox.setHgrow(spacer, Priority.ALWAYS);
            bar.setMaxWidth(Double.MAX_VALUE);
        }

        @Override
        protected void updateItem(ProgressoAlunoDisciplinaDto item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
                setText(null);
                return;
            }

            double progressoNormalizado = normalize(item.progresso());
            name.setText(item.disciplina() == null || item.disciplina().isBlank() ? "Disciplina" : item.disciplina());
            percent.setText(Math.round(progressoNormalizado) + "%");
            bar.setProgress(clamp(progressoNormalizado / 100d, 0d, 1d));

            setText(null);
            setGraphic(root);
        }

        private double normalize(double value) {
            if (Double.isNaN(value) || Double.isInfinite(value)) {
                return 0d;
            }
            if (value <= 1d) {
                return Math.max(0d, Math.min(100d, value * 100d));
            }
            return Math.max(0d, Math.min(100d, value));
        }

        private double clamp(double value, double min, double max) {
            return Math.max(min, Math.min(max, value));
        }
    }
}
