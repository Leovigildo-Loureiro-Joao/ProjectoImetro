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
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.imetro.App;
import com.imetro.config.RuntimeConfig;
import com.imetro.domain.CacheService;
import com.imetro.domain.dto.candidato.DashboardMelhoriaDia;
import com.imetro.domain.dto.candidato.DashboardMelhoriaResumo;
import com.imetro.domain.dto.planejamento.LeituraRecomendada;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoDisciplina;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoEtapa;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoInsight;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoPonto;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoRegistro;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoResumo;
import com.imetro.domain.dto.progresso.ProgressoAlunoDisciplinaDto;
import com.imetro.domain.dto.stats.Stats;
import com.imetro.domain.enums.Foco;
import com.imetro.domain.model.Candidato;
import com.imetro.persistence.repository.MedalhaRepository;
import com.imetro.services.CandidatoService;
import com.imetro.services.DiagnosticoService;
import com.imetro.services.DisciplinaService;
import com.imetro.services.PlaneamentoEstudoService;
import com.imetro.ui.components.CircleProgress;
import com.imetro.util.Authentication;
import com.imetro.util.MedalSupport;

import javafx.animation.FadeTransition;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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

    @SuppressWarnings("deprecation")
    private static final Locale LOCALE_PT = new Locale("pt", "AO");
    private static final DateTimeFormatter HEADER_DATE_FORMAT = DateTimeFormatter.ofPattern("HH:mm EEEE dd MMM yyyy", LOCALE_PT);
    private static final DateTimeFormatter CARD_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy", LOCALE_PT);
    private static final DateTimeFormatter CHART_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM", LOCALE_PT);
    private static final long CACHE_TTL_SECONDS = 60;
    private static final double MAX_PROGRESS = 100.0;

    private static final Map<UUID, CacheEntry<PlaneamentoEstudoResumo>> PLAN_CACHE = new HashMap<>();
    private static final Map<UUID, CacheEntry<DashboardMelhoriaResumo>> MELHORIA_CACHE = new HashMap<>();

    @FXML private Label welcome;
    @FXML private Label localDate;
    @FXML private Label progressText;
    @FXML private Label next_level;
    @FXML private StackPane progresso;
    @FXML private Label heroSummary;
    @FXML private Label heroFocus;
    @FXML private Label achievementBadge;
    @FXML private Label achievementTitle;
    @FXML private Label achievementSummary;
    @FXML private Label achievementMeta;
    @FXML private Label planBadge;
    @FXML private Label planTitle;
    @FXML private Label planSummary;
    @FXML private Label planFocus;
    @FXML private Label planMeta;
    @FXML private Label disciplineBadge;
    @FXML private Label disciplineSummary;
    @FXML private HBox achievementCardsBox;
    @FXML private ListView<ProgressoAlunoDisciplinaDto> status_disciplina;
    @FXML private Label sequenceBadge;
    @FXML private Label desc;
    @FXML private Label sequenceTitle;
    @FXML private Label sequenceSummary;
    @FXML private AreaChart<String, Number> areaActivityChart;
    @FXML private VBox tela;
    @FXML private HBox guidedFlowBox;
    @FXML private StackPane stepCircle;
    @FXML private Label stepNumber;
    @FXML private Label stepTitle;
    @FXML private Label stepDescription;
    @FXML private JFXButton stepActionButton;

    private final CandidatoService candidatoService = new CandidatoService();
    private final DiagnosticoService diagnosticoService = new DiagnosticoService();
    private final PlaneamentoEstudoService planeamentoService = new PlaneamentoEstudoService();
    private final MedalhaRepository medalhaRepository = new MedalhaRepository();

    private Candidato candidato;
    private PlaneamentoEstudoResumo planeamentoResumo;
    private DashboardMelhoriaResumo dashboardMelhoriaResumo = DashboardMelhoriaResumo.empty();

    private record CacheEntry<T>(T data, long timestamp) {
        boolean isExpired() {
            return System.currentTimeMillis() - timestamp > CACHE_TTL_SECONDS * 1000;
        }
    }

    private record ChartStats(int activeDays, double averageRate) {}

    private record AchievementCardData(String badge, String title, String summary, String meta) {}

    @FXML
    public void StartDiagnostic(javafx.event.ActionEvent event) {
        CandidatoLayoutController.navegar("diagnostico");
    }

    @FXML
    public void StartExam(javafx.event.ActionEvent event) {
        CandidatoLayoutController.navegar("exame_adaptativo");
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
        UUID candidatoId = candidato.getIdCandidato();

        CompletableFuture.allOf(
            CompletableFuture.runAsync(() -> planeamentoResumo = getPlaneamentoCached(candidatoId), App.getExecutorService()),
            CompletableFuture.runAsync(() -> dashboardMelhoriaResumo = getMelhoriaCached(candidatoId), App.getExecutorService())
        ).thenRunAsync(() -> Platform.runLater(this::renderDashboard), App.getExecutorService())
         .exceptionally(throwable -> {
            System.err.println("Erro ao carregar dados do dashboard: " + throwable);
            Platform.runLater(this::renderFallback);
            return null;
        });
    }

    private void renderDashboard() {
        updateHeader();
        renderGuidedFlow();
        setupHeroProgress();
        setupHeroCopy();
        setupAchievementCard();
        setupSequenceChart();
        setupDisciplineProgress();
        animateSections();
    }

    private void renderFallback() {
        planeamentoResumo =  new PlaneamentoEstudoResumo(
            0,
            "O sistema ainda nao tem base suficiente para um plano personalizado completo, mas já deixa a rotina organizada com blocos curtos e revisão espaçada.",
            "0",
            "0 s",
            "Sem dados para medir",
            "Realizar um diagnostico",
            "Sem foco secundario",
            "",
            "",
            List.of(
                new PlaneamentoEstudoInsight("Primeiro passo", "Começa com blocos curtos e foco numa unica area para reduzir dispersao."),
                new PlaneamentoEstudoInsight("Regra de ritmo", "Divide o estudo em ciclos curtos, correcao imediata e uma revisao 24h depois."),
                new PlaneamentoEstudoInsight("Confirmacao final", "Fecha a semana com um teste curto para medir o que realmente ficou.")
            ),
            List.of(
                new PlaneamentoEstudoEtapa("Hoje", "Bloco curto", "25 min em Álgebra, 10 min de revisão e uma pausa curta."),
                new PlaneamentoEstudoEtapa("Amanhã", "Bloco leve", "Reforça Fisica com problemas simples e correção imediata."),
                new PlaneamentoEstudoEtapa("48h", "Treino misto", "Mistura Matematica e Fisica para consolidar sem cansar."),
                new PlaneamentoEstudoEtapa("Fim da semana", "Teste curto", "Valida a evolução com um diagnóstico pequeno e objetivo.")
            ),
            List.of(
                new PlaneamentoEstudoRegistro("Teste adaptativo", "Matemática", "84% de acerto, consistência alta e ritmo estável.", "Há 2 dias", "pill-good"),
                new PlaneamentoEstudoRegistro("Diagnóstico", "Física", "Base boa, mas o tempo ainda precisa de ajuste.", "Há 5 dias", "pill-warn"),
                new PlaneamentoEstudoRegistro("Teste adaptativo", "Português", "Leitura firme e menos erros sob pressão.", "Há 8 dias", "pill-good")
            ),
            List.of(
                new PlaneamentoEstudoDisciplina("Matemática", 0, 0, 0, 0, 0, new Foco(null,null), "Otima iniciativa! desenvolva sua base", 14d),
                new PlaneamentoEstudoDisciplina("Física", 0, 0, 0, 0, 0, new Foco(null,null), "Otima decisao para melhor desempenho!", 29d)
            ),
            List.of(
                new PlaneamentoEstudoPonto("Sem 1", 56d),
                new PlaneamentoEstudoPonto("Sem 2", 61d),
                new PlaneamentoEstudoPonto("Sem 3", 68d),
                new PlaneamentoEstudoPonto("Sem 4", 66d),
                new PlaneamentoEstudoPonto("Sem 5", 74d),
                new PlaneamentoEstudoPonto("Sem 6", 81d)
            ),
            List.of()
        );
        
        dashboardMelhoriaResumo = DashboardMelhoriaResumo.empty();
        renderDashboard();
    }

    private PlaneamentoEstudoResumo getPlaneamentoCached(UUID candidatoId) {
        CacheEntry<PlaneamentoEstudoResumo> entry = PLAN_CACHE.get(candidatoId);
        if (entry == null || entry.isExpired()) {
            PlaneamentoEstudoResumo novo = planeamentoService.gerarResumo(candidatoId);
            PLAN_CACHE.put(candidatoId, new CacheEntry<>(novo, System.currentTimeMillis()));
            return novo;
        }
        return entry.data();
    }

    private DashboardMelhoriaResumo getMelhoriaCached(UUID candidatoId) {
        CacheEntry<DashboardMelhoriaResumo> entry = MELHORIA_CACHE.get(candidatoId);
        if (entry == null || entry.isExpired()) {
            DashboardMelhoriaResumo novo = candidatoService.calcularResumoMelhorias(candidatoId);
            MELHORIA_CACHE.put(candidatoId, new CacheEntry<>(novo, System.currentTimeMillis()));
            return novo;
        }
        return entry.data();
    }

    private void updateHeader() {
        if (welcome != null) {
            int hour = LocalTime.now().getHour();
            String nome = safeName(candidato.getNome());
            if (hour >= 0 && hour <= 5) {
                welcome.setText("Oi Batman dos estudos!");
                desc.setText("Humm! vejo que estas bem disposto hoje.");
            } else if (hour >= 6 && hour <= 11) {
                welcome.setText("Ola " + nome + "!");
                desc.setText("Pronto para mais um salto de conhecimento.");
            } else if (hour >= 12 && hour <= 18) {
                welcome.setText("Dobre a Rotina " + nome + "!");
                desc.setText("Seja preciso seu tempo é valioso.");
            } else {
                welcome.setText("Boa noite! " + nome + "!");
                desc.setText("Seja equilibrado consigo mesmo se foque no necessario.");
            }
        }
        if (localDate != null) {
            localDate.setText(LocalDateTime.now().format(HEADER_DATE_FORMAT));
        }
    }

    private void setupHeroProgress() {
        Stats stats = candidatoService.CalcularStats();
        double progresso = calculateAverageProgress(stats);
        updateHeroProgressUI(progresso);
    }

    private double calculateAverageProgress(Stats stats) {
        return (stats.velocidade() + stats.logica() + stats.precisao() +
                stats.resiliencia() + stats.consistencia()) / 5.0;
    }

    private void updateHeroProgressUI(double progressos) {
        if (progressText != null) {
            progressText.setText(Math.round(clamp(progressos, 0, 1) * 100) + "% progresso");
        }
        if (next_level != null) {
            next_level.setText(resolveNextLevel(progressos));
        }
        if (progresso != null) {
            progresso.getChildren().clear();
            CircleProgress circleProgress = new CircleProgress(60, 60);
            circleProgress.setValue(progressos);
            progresso.getChildren().add(circleProgress);
        }
    }

    private void setupHeroCopy() {
        if (heroSummary != null) {
            heroSummary.setText(planeamentoResumo.resumoHero());
        }
        if (heroFocus != null) {
            heroFocus.setText("Foco actual: " + safeText(planeamentoResumo.focoAtual(), "—") +
                " · ritmo " + safeText(planeamentoResumo.ritmoMedio(), "—"));
        }
    }

    private void setupAchievementCard() {
        UUID userId = Authentication.getCurrentUserId();
        if (userId == null) {
            setAchievementEmptyState();
            return;
        }

        List<MedalSupport.MedalAward> awards = medalhaRepository.findAwardsByUserId(userId);
        List<MedalSupport.MedalViewModel> viewModels = buildMedalViewModels(userId, awards);
        Optional<MedalSupport.MedalAward> recentAward = findRecentAward(awards);

        List<MedalSupport.MedalViewModel> upcoming = viewModels.stream()
            .filter(medal -> !medal.unlocked())
            .limit(3)
            .collect(Collectors.toList());

        if (upcoming.size() < 3) {
            upcoming = mergeUpcomingWithOthers(upcoming, viewModels);
        }

        AchievementCardData data = buildAchievementCardData(upcoming, recentAward);
        applyAchievementData(data);
        achievementCardsBox.getChildren().setAll(buildAchievementCards(upcoming));
    }

    private void setAchievementEmptyState() {
        applyAchievementData(new AchievementCardData(
            "Conquistas", "Sem medalhas ainda",
            "A tua primeira conquista vai aparecer assim que o histórico começar.",
            "Liga uma sessão para alimentar este card."
        ));
        achievementCardsBox.getChildren().clear();
    }

    private void applyAchievementData(AchievementCardData data) {
        if (achievementBadge != null) achievementBadge.setText(data.badge());
        if (achievementTitle != null) achievementTitle.setText(data.title());
        if (achievementSummary != null) achievementSummary.setText(data.summary());
        if (achievementMeta != null) achievementMeta.setText(data.meta());
    }

    private List<MedalSupport.MedalViewModel> mergeUpcomingWithOthers(
        List<MedalSupport.MedalViewModel> upcoming,
        List<MedalSupport.MedalViewModel> all) {
        List<MedalSupport.MedalViewModel> merged = new ArrayList<>(upcoming);
        var existingCodes = upcoming.stream()
            .map(m -> m.definition().code())
            .collect(Collectors.toSet());

        for (MedalSupport.MedalViewModel medal : all) {
            if (merged.size() >= 3) break;
            if (!existingCodes.contains(medal.definition().code())) {
                merged.add(medal);
                existingCodes.add(medal.definition().code());
            }
        }
        return merged;
    }

    private AchievementCardData buildAchievementCardData(
        List<MedalSupport.MedalViewModel> upcoming,
        Optional<MedalSupport.MedalAward> recentAward) {

        if (upcoming.isEmpty()) {
            return new AchievementCardData(
                "Conquistas", "Sem medalhas ainda",
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
                    "Conquista recente", "Próximas conquistas",
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
        String meta = "Primeira meta: " + definition.title() + " · faltam " +
            first.remainingToUnlock() + " " + definition.targetUnit();

        return new AchievementCardData(badge, title, summary, meta);
    }

    private List<MedalSupport.MedalViewModel> buildMedalViewModels(
        UUID userId,
        List<MedalSupport.MedalAward> awards) {

        Map<String, MedalSupport.MedalAward> awardsByCode = awards.stream()
            .collect(Collectors.toMap(MedalSupport.MedalAward::medalCode, Function.identity()));

        Map<MedalSupport.MedalSkill, Integer> previewProgress = RuntimeConfig.isDbEnabled()
            ? Map.of()
            : MedalSupport.navigationPreviewProgress();

        return MedalSupport.catalog().stream()
            .map(definition -> {
                MedalSupport.MedalAward award = awardsByCode.get(definition.code());
                boolean unlocked = award != null;
                int progressValue = unlocked ? Math.max(award.progressValue(), definition.targetValue())
                                             : previewProgress.getOrDefault(definition.skill(), 0);
                boolean previewUnlocked = !unlocked && progressValue >= definition.targetValue();

                return new MedalSupport.MedalViewModel(
                    definition,
                    unlocked || previewUnlocked,
                    progressValue,
                    award != null ? award.recordValue() : null,
                    award != null ? award.earnedAt() : null
                );
            })
            .collect(Collectors.toList());
    }

    private Optional<MedalSupport.MedalAward> findRecentAward(List<MedalSupport.MedalAward> awards) {
        if (awards.isEmpty()) return Optional.empty();

        LocalDate hoje = LocalDate.now();
        return awards.stream()
            .filter(a -> a.actualizadaAt() != null && a.actualizadaAt().toLocalDate().equals(hoje))
            .max(Comparator.comparing(MedalSupport.MedalAward::actualizadaAt));
    }

    private List<Node> buildAchievementCards(List<MedalSupport.MedalViewModel> upcoming) {
        List<Node> cards = new ArrayList<>();
        for (int index = 0; index < 3; index++) {
            MedalSupport.MedalViewModel medal = index < upcoming.size() ? upcoming.get(index) : null;
            cards.add(AchievementCardFactory.create(index + 1, medal));
        }
        return cards;
    }

    private void setupPlanCard() {
        if (planBadge != null) planBadge.setText("Foco");
        if (planTitle != null) planTitle.setText("Foco actual");
        if (planSummary != null) planSummary.setText(planeamentoResumo == null ? "—" : planeamentoResumo.resumoHero());
        if (planFocus != null) {
            planFocus.setText("Foco actual: " + safeText(planeamentoResumo == null ? null : planeamentoResumo.focoAtual(), "—"));
        }
        if (planMeta != null) {
            planMeta.setText("Acerto médio " + safeText(planeamentoResumo == null ? null : planeamentoResumo.acertoMedio(), "—") +
                " · ritmo " + safeText(planeamentoResumo == null ? null : planeamentoResumo.ritmoMedio(), "—"));
        }
    }

    private void setupSequenceChart() {
        if (areaActivityChart == null) return;

        configureChartAxis();

        List<DashboardMelhoriaDia> semana = dashboardMelhoriaResumo.semana();
        XYChart.Series<String, Number> series = buildChartSeries(semana);

        areaActivityChart.getData().clear();
        areaActivityChart.getData().add(series);

        ChartStats stats = calculateChartStats(semana);
        updateSequenceLabels(stats);

        fadeIn(areaActivityChart);
    }

    private void configureChartAxis() {
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
    }

    private XYChart.Series<String, Number> buildChartSeries(List<DashboardMelhoriaDia> semana) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Taxa de sucesso");

        for (DashboardMelhoriaDia dia : semana) {
            String rotulo = dia.data().format(CHART_DATE_FORMAT);
            double valor = dia.melhorias() <= 0 ? 0d : (dia.sucessos() * 100.0) / dia.melhorias();
            series.getData().add(new XYChart.Data<>(rotulo, valor));
        }

        return series;
    }

    private ChartStats calculateChartStats(List<DashboardMelhoriaDia> semana) {
        int activeDays = 0;
        double totalRate = 0d;

        for (DashboardMelhoriaDia dia : semana) {
            if (dia.melhorias() > 0) {
                activeDays++;
                totalRate += (dia.sucessos() * 100.0) / dia.melhorias();
            }
        }

        return new ChartStats(activeDays, activeDays > 0 ? totalRate / activeDays : 0);
    }

    private void updateSequenceLabels(ChartStats stats) {
        if (sequenceBadge != null) {
            sequenceBadge.setText(stats.activeDays + (stats.activeDays == 1 ? " dia activo" : " dias activos"));
        }
        if (sequenceSummary != null) {
            if (stats.activeDays > 0) {
                sequenceSummary.setText("Taxa média de sucesso de " + Math.round(stats.averageRate) +
                    "% nos últimos 7 dias. O gráfico destaca a continuidade do ritmo.");
            } else {
                sequenceSummary.setText("Ainda não há sessões suficientes para medir a sequência. " +
                    "Faz o primeiro teste e este gráfico ganha vida.");
            }
        }
        if (sequenceTitle != null) {
            sequenceTitle.setText("Ritmo dos últimos 7 dias");
        }
    }

    private void setupDisciplineProgress() {
        List<ProgressoAlunoDisciplinaDto> progressos = DisciplinaService.getProgressoDisciplinasCandidatoSafe()
            .stream()
            .sorted(Comparator.comparingDouble((ProgressoAlunoDisciplinaDto item) ->
                normalizeProgress(item.progresso())).reversed()
                .thenComparing(item -> safeText(item.disciplina(), ""), String.CASE_INSENSITIVE_ORDER))
            .collect(Collectors.toList());

        updateDisciplineLabels(progressos);

        if (status_disciplina != null) {
            status_disciplina.setCellFactory(list -> new DisciplineProgressCell());
            status_disciplina.setItems(FXCollections.observableArrayList(progressos));
            status_disciplina.setPlaceholder(buildEmptyPlaceholder("Ainda não há progresso por disciplina."));
            fadeIn(status_disciplina);
        }
    }

    private void updateDisciplineLabels(List<ProgressoAlunoDisciplinaDto> progressos) {
        if (disciplineBadge != null) {
            disciplineBadge.setText(progressos.size() + (progressos.size() == 1 ? " disciplina" : " disciplinas"));
        }
        if (disciplineSummary != null) {
            disciplineSummary.setText(progressos.isEmpty()
                ? "Ainda não há disciplina activa no histórico."
                : "Progressão actual por disciplina. Só progresso, sem consistência, velocidade ou métricas extra.");
        }
    }

    private void animateSections() {
        fadeIn(progresso);
        fadeIn(areaActivityChart);
        fadeIn(status_disciplina);
    }

    private void fadeIn(Node node) {
        if (node == null) return;
        FadeTransition fade = new FadeTransition(Duration.millis(700), node);
        fade.setFromValue(0d);
        fade.setToValue(1d);
        fade.play();
    }

    private String resolveNextLevel(double progressRatio) {
        double percent = clamp(progressRatio * 100d, 0d, 100d);
        if (percent < 35d) return "INTERMEDIÁRIO";
        if (percent < 70d) return "AVANÇADO";
        return "EXCELENTE";
    }

    private Label buildEmptyPlaceholder(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("muted");
        label.setWrapText(true);
        return label;
    }

    private double normalizeProgress(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) return 0d;
        if (value <= 1d) return clamp(value * 100d, 0d, 100d);
        return clamp(value, 0d, 100d);
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private String formatDate(LocalDateTime value) {
        return value == null ? "sem data" : value.format(CARD_DATE_FORMAT);
    }

    private String safeName(String value) {
        return safeText(value, "Candidato");
    }

    private String safeText(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }

    private void renderGuidedFlow() {
        if (guidedFlowBox == null) return;
        UUID candidatoId = candidato.getIdCandidato();

        boolean temDiagnostico = diagnosticoService.temHistoricoDiagnostico(candidatoId);

        if (!temDiagnostico) {
            stepNumber.setText("1");
            stepTitle.setText("Fazer o Diagnóstico Inicial");
            stepDescription.setText("Mapeia o teu conhecimento atual para o sistema criar um plano de estudo personalizado.");
            stepActionButton.setText("EXECUTAR DIAGNÓSTICO");
            stepActionButton.setUserData("diagnostico");
            stepCircle.getStyleClass().add("guided-step-active");
        } else if (diagnosticoService.diagnosticoEmCooldown(candidatoId)) {
            stepNumber.setText("2");
            stepTitle.setText("Estudar os Tópicos Pendentes");
            stepDescription.setText("Revisa os tópicos recomendados no plano de estudo antes de fazeres um novo diagnóstico.");
            stepActionButton.setText("VER PLANO DE ESTUDO");
            stepActionButton.setUserData("plano");
        } else {
            stepNumber.setText("3");
            stepTitle.setText("Validar Conhecimento");
            stepDescription.setText("Faz um teste adaptativo para validar o que aprendeste e subir de nível.");
            stepActionButton.setText("INICIAR EXAME ADAPTATIVO");
            stepActionButton.setUserData("teste");
        }
    }

    @FXML
    private void onStepAction(javafx.event.ActionEvent event) {
        String action = stepActionButton.getUserData().toString();
        if ("teste".equals(action)) {
            CandidatoLayoutController.navegar("exame_adaptativo");
        } else {
            CandidatoLayoutController.navegar("diagnostico");
        }
    }

    private static final class AchievementCardFactory {
        private static final String[] COLORS = {"success", "warning", "primary"};

        private AchievementCardFactory() {}

        static VBox create(int position, MedalSupport.MedalViewModel medal) {
            StackPane imageShell = new StackPane();
            imageShell.getStyleClass().add("achievement-mini-shell");

            Label badge = createBadge(position);
            Label title = new Label();
            Label detail = new Label();

            configureLabels(title, detail, medal);

            VBox textBox = new VBox(3.0, title, detail);
            textBox.setFillWidth(true);
            textBox.setMaxWidth(Double.MAX_VALUE);

            if (medal != null) {
                setMedalImage(imageShell, medal.definition());
            }

            VBox card = new VBox(6.0, badge, imageShell, textBox);
            card.getStyleClass().addAll("achievement-mini-card", "achievement-mini-card-" + COLORS[position % COLORS.length]);
            card.setAlignment(Pos.TOP_LEFT);
            card.setFillWidth(true);
            card.setMinWidth(0);
            card.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(card, Priority.ALWAYS);

            return card;
        }

        private static Label createBadge(int position) {
            Label badge = new Label(String.format("%02d", position));
            badge.getStyleClass().addAll("achievement-mini-badge", "achievement-mini-badge-" + position);
            return badge;
        }

        private static void configureLabels(Label title, Label detail, MedalSupport.MedalViewModel medal) {
            title.getStyleClass().add("achievement-mini-title");
            title.setWrapText(true);
            title.setMinWidth(0);
            title.setMaxWidth(Double.MAX_VALUE);

            detail.getStyleClass().add("achievement-mini-copy");
            detail.setWrapText(true);
            detail.setMinWidth(0);
            detail.setMaxWidth(Double.MAX_VALUE);

            if (medal != null) {
                title.setText(medal.definition().title());
                detail.setText(medal.unlocked()
                    ? "Já desbloqueada"
                    : "Faltam " + medal.remainingToUnlock() + " " + medal.definition().targetUnit());
            } else {
                title.setText("Mais por vir");
                detail.setText("Liga uma sessão para revelar a próxima conquista.");
            }
        }

        private static void setMedalImage(StackPane container, MedalSupport.MedalDefinition definition) {
            Image image = MedalSupport.loadMedalImage(definition.imageRef());
            if (image != null) {
                ImageView icon = new ImageView(image);
                icon.setFitHeight(44.0);
                icon.setFitWidth(44.0);
                icon.setPreserveRatio(true);
                icon.setSmooth(true);
                container.getChildren().add(icon);
            }
        }
    }

    private static final class DisciplineProgressCell extends ListCell<ProgressoAlunoDisciplinaDto> {
        private final Label name = new Label();
        private final Label percent = new Label();
        private final ProgressBar bar = new ProgressBar();
        private final Region spacer = new Region();
        private final HBox header = new HBox(10, name, spacer, percent);
        private final VBox root = new VBox(8, header, bar);

        DisciplineProgressCell() {
            configureStyles();
            configureLayout();
        }

        private void configureStyles() {
            root.getStyleClass().add("discipline-progress-row");
            name.getStyleClass().add("discipline-progress-title");
            percent.getStyleClass().add("discipline-progress-percent");
        }

        private void configureLayout() {
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
                return;
            }

            double progressoNormalizado = clamp(normalizeProgress(item.progresso()), 0, 100);
            name.setText(safeText(item.disciplina(), "Disciplina"));
            percent.setText(Math.round(progressoNormalizado) + "%");
            bar.setProgress(progressoNormalizado / 100d);

            setGraphic(root);
        }

        private double normalizeProgress(double value) {
            if (Double.isNaN(value) || Double.isInfinite(value)) return 0;
            return value <= 1 ? value * 100 : Math.max(0, Math.min(MAX_PROGRESS, value));
        }

        private String safeText(String value, String fallback) {
            return value == null || value.isBlank() ? fallback : value;
        }

        private double clamp(double value, double min, double max) {
            return Math.max(min, Math.min(max, value));
        }
    }
}