package com.imetro.ui.controller.candidato;

import java.net.URL;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;

import com.imetro.App;
import com.imetro.config.RuntimeConfig;
import com.imetro.domain.dto.bolsa.BolsaDto;
import com.imetro.domain.dto.bolsa.BolsaMock;
import com.imetro.domain.dto.stats.Teste_Stat;
import com.imetro.persistence.repository.BolsaRepository;
import com.imetro.persistence.repository.MedalhaRepository;
import com.imetro.persistence.repository.ScoreBolsaRepository;
import com.imetro.persistence.repository.TesteStatsRepository;
import com.imetro.persistence.repository.UserRepository;
import com.imetro.ui.components.CircleProgress;
import com.imetro.ui.components.bolsas.BolsaCard;
import com.imetro.ui.components.bolsas.FactoryRow;
import com.imetro.ui.components.bolsas.SectionTitle;
import com.imetro.ui.controller.candidato.bolsas.BolsaSimuladoCoordinator;
import com.imetro.util.Authentication;
import com.imetro.util.ProfileSessionState;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class BolsasController implements Initializable {

    private static final Locale LOCALE_PT_AO = new Locale("pt", "AO");
    private static final int DIFFICULT_TEST_DURATION_MINUTES = 45;

    @FXML
    private Label subtitleLabel;

    @FXML
    private Label readinessScoreLabel;

    @FXML
    private Label radarSummaryLabel;

    @FXML
    private Label janelaLabel;

    @FXML
    private ProgressBar prontidaoBar;

    @FXML
    private ProgressBar alinhamentoBar;

    @FXML
    private ProgressBar documentosBar;

    @FXML
    private ProgressBar competitividadeBar;

    @FXML
    private Label prontidaoText;

    @FXML
    private Label alinhamentoText;

    @FXML
    private Label documentosText;

    @FXML
    private Label competitividadeText;

    @FXML
    private StackPane matchRingHost;

    @FXML
    private FlowPane bolsasFlow;

    @FXML
    private VBox fatoresBox;

    @FXML
    private VBox rankingBox;

    private final BolsaRepository bolsaRepository = new BolsaRepository();
    private final MedalhaRepository medalhaRepository = new MedalhaRepository();
    private final ScoreBolsaRepository scoreBolsaRepository = new ScoreBolsaRepository();
    private final TesteStatsRepository testeStatsRepository = new TesteStatsRepository();
    private final UserRepository userRepository = new UserRepository();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CandidateSignals signals = buildCandidateSignals();
        WeeklyWindow weeklyWindow = resolveWeeklyWindow();
        RankingWindow rankingWindow = resolveRankingWindow();

        configureHero(signals, weeklyWindow);
        configureMetricTiles(signals);
        setupRing(signals.readinessProgress());
        setupScholarships(signals, weeklyWindow);
        setupFactors(signals);
        setupRanking(signals, rankingWindow);
    }

    private void configureHero(CandidateSignals signals, WeeklyWindow weeklyWindow) {
        subtitleLabel.setText(
            "Match semanal de bolsas para " + signals.primeiroNome() + "."
        );
        readinessScoreLabel.setText(formatPercent(signals.readinessScore()));
        radarSummaryLabel.setText(buildHeroSummary(signals));
        janelaLabel.setText(buildWindowLabel(weeklyWindow));
    }

    private void configureMetricTiles(CandidateSignals signals) {
        prontidaoBar.setProgress(signals.medalProgress());
        alinhamentoBar.setProgress(progressFromPercent(signals.desempenho()));
        documentosBar.setProgress(progressFromPercent(signals.precisao()));
        competitividadeBar.setProgress(progressFromPercent(signals.velocidade()));

        prontidaoText.setText(signals.medalhas() + " medalhas no perfil");
        alinhamentoText.setText(formatPercent(signals.desempenho()) + " de desempenho recente");
        documentosText.setText(formatPercent(signals.precisao()) + " de precisao nas respostas");
        competitividadeText.setText(formatPercent(signals.velocidade()) + " de velocidade media");
    }

    private void setupRing(double readinessProgress) {
        CircleProgress progress = new CircleProgress(58, 58, 58, (float) clamp(readinessProgress, 0d, 1d));
        progress.setSubtitle("Match");
        matchRingHost.getChildren().setAll(progress);
    }

    private void setupScholarships(CandidateSignals signals, WeeklyWindow weeklyWindow) {
        List<BolsaDto> bolsas = loadScholarships();
        bolsasFlow.getChildren().clear();
        for (int i = 0; i < bolsas.size(); i++) {
            BolsaDto bolsa = bolsas.get(i);
            ScholarshipProjection projection = mapToWeeklyScholarship(bolsa, signals, weeklyWindow, i);
            bolsasFlow.getChildren().add(new BolsaCard(projection.bolsaMock(), () -> abrirSimuladoBolsa(bolsa, projection)));
        }
    }

    private void setupFactors(CandidateSignals signals) {
        fatoresBox.getChildren().setAll(
            new SectionTitle("Entrada da bolsa", "Os 5 sinais abaixo decidem se a prova abre para ti."),
            new FactoryRow(
                "Medalhas",
                signals.medalProgress(),
                signals.medalhas() >= 2
                    ? "Base pronta para bolsas medias e fortes."
                    : "Mais medalhas ajudam a abrir novas bolsas."
            ),
            new FactoryRow("Desempenho", progressFromPercent(signals.desempenho()), "E o filtro mais forte para entrar."),
            new FactoryRow("Evolucao", progressFromPercent(signals.evolucao()), "Mostra se estas a subir semana apos semana."),
            new FactoryRow("Precisao", progressFromPercent(signals.precisao()), "Acerto limpo conta mais nas bolsas."),
            new FactoryRow("Velocidade", progressFromPercent(signals.velocidade()), "Tempo tambem pesa no corte final.")
        );
    }

    private void setupRanking(CandidateSignals signals, RankingWindow rankingWindow) {
        List<ScoreBolsaRepository.WeeklyLeaderboardEntry> leaderboard = loadWeeklyLeaderboard(rankingWindow);
        UUID currentUserId = resolveCurrentUserId();
        ScoreBolsaRepository.WeeklyLeaderboardEntry currentEntry = leaderboard.stream()
            .filter(entry -> entry.candidatoId() != null && entry.candidatoId().equals(currentUserId))
            .findFirst()
            .orElse(null);

        if (leaderboard.isEmpty()) {
            rankingBox.getChildren().setAll(
                new SectionTitle("Ranking semanal", buildRankingSectionCopy(rankingWindow, leaderboard)),
                buildMiniCard(
                    "Teu score previsto",
                    formatScorePoints(signals.readinessScore()),
                    signals.previewMode()
                        ? "Ainda sem scores reais nesta semana."
                        : "A tua prontidao atual vira score previsto ate fechares a prova."
                ),
                buildRankingRow("TOP 10", "Sem corte ainda", "Os primeiros resultados vao fechar esta linha.", "Em espera", "timeline-pill"),
                buildRankingRow("TOP 25", "Faixa principal", "A zona competitiva aparece assim que houver provas.", "Sem dados", "timeline-pill"),
                buildRankingRow("TOP 50", "Zona larga", "Ainda nao ha leitura suficiente nesta janela.", "Aguardando", "pill-warn"),
                buildRankingRow("TU AGORA", buildCurrentZoneTitle(signals), buildCurrentZoneCopy(signals), "Preview", "timeline-pill")
            );
            return;
        }

        rankingBox.getChildren().setAll(
            new SectionTitle("Ranking semanal", buildRankingSectionCopy(rankingWindow, leaderboard)),
            buildMiniCard(
                currentEntry == null ? "Teu score previsto" : "Teu score real da semana",
                currentEntry == null ? formatScorePoints(signals.readinessScore()) : formatScorePoints(currentEntry.score()),
                buildCurrentScoreCopy(currentEntry, signals, leaderboard.size())
            ),
            buildRankingRow("TOP 10", "Shortlist da semana", buildCutCopy(leaderboard, 10), buildCutBadge(leaderboard.size(), 10), buildCutBadgeClass(leaderboard.size(), 10)),
            buildRankingRow("TOP 25", "Mantem candidatura viva", buildCutCopy(leaderboard, 25), buildCutBadge(leaderboard.size(), 25), buildCutBadgeClass(leaderboard.size(), 25)),
            buildRankingRow("TOP 50", "Entra em observacao", buildCutCopy(leaderboard, 50), buildCutBadge(leaderboard.size(), 50), buildCutBadgeClass(leaderboard.size(), 50)),
            buildRankingRow(buildCurrentPositionLabel(currentEntry), buildCurrentRankingTitle(currentEntry, signals), buildCurrentRankingCopy(currentEntry, signals, leaderboard.size()), buildCurrentRankingBadge(currentEntry), buildCurrentRankingBadgeClass(currentEntry))
        );
    }

    private CandidateSignals buildCandidateSignals() {
        String primeiroNome = resolvePrimeiroNome();
        UUID userId = resolveCurrentUserId();

        if (!RuntimeConfig.isDbEnabled() || userId == null) {
            return buildPreviewSignals(primeiroNome);
        }

        try {
            List<Teste_Stat> stats = testeStatsRepository.findByCandidatoId(userId).stream()
                .map(row -> Teste_Stat.ParseDto((java.util.Map<String, Object>) row))
                .sorted(Comparator.comparing(Teste_Stat::criado_em, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();

            if (stats.isEmpty()) {
                return buildPreviewSignals(primeiroNome);
            }

            int medalhas = medalhaRepository.findAwardsByUserId(userId).size();
            double desempenho = average(stats.stream().map(Teste_Stat::percentual_acerto).map(this::normalizePercent).toList());
            double precisao = average(stats.stream().map(Teste_Stat::precisao).map(this::normalizeMetric).toList());
            double velocidade = average(stats.stream().map(Teste_Stat::velocidade).map(this::normalizeMetric).toList());
            double evolucao = computeEvolution(stats);

            return new CandidateSignals(primeiroNome, medalhas, desempenho, precisao, velocidade, evolucao, stats.size(), false);
        } catch (SQLException | RuntimeException e) {
            e.printStackTrace();
            return buildPreviewSignals(primeiroNome);
        }
    }

    private CandidateSignals buildPreviewSignals(String primeiroNome) {
        return new CandidateSignals(primeiroNome, 2, 78, 74, 68, 71, 11, true);
    }

    private WeeklyWindow resolveWeeklyWindow() {
        LocalDate today = LocalDate.now();
        LocalDate mondayThisWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate tuesdayThisWeek = mondayThisWeek.plusDays(1);
        boolean openNow = !today.isBefore(mondayThisWeek) && !today.isAfter(tuesdayThisWeek);

        if (openNow) {
            return new WeeklyWindow(mondayThisWeek, tuesdayThisWeek, true);
        }

        LocalDate nextMonday = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        return new WeeklyWindow(nextMonday, nextMonday.plusDays(1), false);
    }

    private RankingWindow resolveRankingWindow() {
        LocalDate today = LocalDate.now();
        LocalDate mondayThisWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate tuesdayThisWeek = mondayThisWeek.plusDays(1);
        boolean ativaAgora = !today.isAfter(tuesdayThisWeek);
        return new RankingWindow(mondayThisWeek, tuesdayThisWeek, ativaAgora);
    }

    private List<BolsaDto> loadScholarships() {
        if (!RuntimeConfig.isDbEnabled()) {
            return previewScholarships();
        }

        try {
            List<BolsaDto> bolsas = bolsaRepository.findAll().stream()
                .map(BolsaDto::fromMap)
                .toList();
            return bolsas.isEmpty() ? previewScholarships() : bolsas;
        } catch (SQLException | RuntimeException e) {
            e.printStackTrace();
            return previewScholarships();
        }
    }

    private List<BolsaDto> previewScholarships() {
        return List.of(
            new BolsaDto(null, "Bolsa Merito Atlas", "Propina + mentoria", 88, 100, "Cobertura quase total da propina", "Excelente para quem sustenta melhoria continua.", "Precisa ter pelo menos uma medalha.", null, null, "Matematica", 45, 1, 68, 64, 70, 58, 1, 2, "TEXTFIELD", true),
            new BolsaDto(null, "Programa Horizonte STEM", "Parcial + laboratorio", 79, 150, "Apoio parcial e acesso a projetos", "Grande encaixe para Matematica e Fisica.", "Alta concorrencia entre perfis tecnicos.", null, null, "Fisica", 50, 2, 74, 68, 76, 64, 1, 2, "TEXTFIELD", true),
            new BolsaDto(null, "Fundo Impulso Academico", "Auxilio de mensalidade", 71, 50, "Apoio modular por semestre", "Boa opcao para ganhar tracao rapida.", "Documentacao precisa estar impecavel.", null, null, "Quimica", 40, 1, 62, 58, 64, 52, 1, 2, "TEXTFIELD", true),
            new BolsaDto(null, "Beca Impacto Local", "Merito + projeto comunitario", 67, 180, "Cobertura media com bonus por impacto", "Diferencia-te se mostrares lideranca aplicada.", "Exige narrativa social mais madura.", null, null, "Raciocinio Logico", 35, 1, 58, 55, 60, 50, 1, 2, "TEXTFIELD", true)
        );
    }

    private ScholarshipProjection mapToWeeklyScholarship(BolsaDto bolsa, CandidateSignals signals, WeeklyWindow weeklyWindow, int index) {
        ScholarshipRequirements requirements = buildRequirements(bolsa, index);

        double medalRatio = ratio(signals.medalhas(), requirements.medalhas());
        double desempenhoRatio = ratio(signals.desempenho(), requirements.desempenho());
        double evolucaoRatio = ratio(signals.evolucao(), requirements.evolucao());
        double precisaoRatio = ratio(signals.precisao(), requirements.precisao());
        double velocidadeRatio = ratio(signals.velocidade(), requirements.velocidade());

        boolean disponivel = signals.medalhas() >= requirements.medalhas()
            && signals.desempenho() >= requirements.desempenho()
            && signals.evolucao() >= requirements.evolucao()
            && signals.precisao() >= requirements.precisao()
            && signals.velocidade() >= requirements.velocidade();

        int readinessMatch = clampInt((int) Math.round(
            average(List.of(medalRatio, desempenhoRatio, evolucaoRatio, precisaoRatio, velocidadeRatio)) * 100d
        ), 0, 100);
        int blendedMatch = clampInt((int) Math.round((readinessMatch * 0.72d) + (safeInt(bolsa.match()) * 0.28d)), 0, 100);

        String status = disponivel
            ? weeklyWindow.abertaAgora() ? "Aberta agora" : "Elegivel para a proxima"
            : "Bloqueada por criterios";

        String actionLabel = disponivel
            ? weeklyWindow.abertaAgora() ? "Entrar na simulacao" : "Preparar para a semana"
            : "Subir criterios";

        String janela = formatWeekday(weeklyWindow.abertura()) + " " + formatShortDate(weeklyWindow.abertura())
            + " a " + formatWeekday(weeklyWindow.fechamento()) + " " + formatShortDate(weeklyWindow.fechamento());

        String dificuldade = switch (index % 4) {
            case 0 -> "Resposta escrita";
            case 1 -> "Cronometrada";
            case 2 -> "Rigor alto";
            default -> "Corte semanal";
        };

        String destaque = (isBlank(bolsa.destaque()) ? "Bolsa semanal competitiva." : bolsa.destaque())
            + " A prova usa variantes dificeis geradas sobre o banco de exercicios existente em "
            + firstNonBlank(bolsa.disciplinaFoco(), "disciplinas nucleares") + ".";

        String criterioResumo = requirements.medalhas() + " medalha(s), "
            + requirements.desempenho() + "% desempenho, "
            + requirements.evolucao() + "% evolucao, "
            + requirements.precisao() + "% precisao, "
            + requirements.velocidade() + "% velocidade";

        BolsaMock mock = new BolsaMock(
            bolsa.nome(),
            isBlank(bolsa.tipo()) ? "Bolsa simulada semanal" : bolsa.tipo(),
            blendedMatch,
            Math.max(1, safeInt(bolsa.vagas())),
            isBlank(bolsa.cobertura()) ? "Cobertura e premio definidos pela bolsa da semana." : bolsa.cobertura(),
            janela,
            destaque,
            isBlank(bolsa.risco()) ? "Todos concorrem na mesma janela e o corte pode subir rapido." : bolsa.risco(),
            status,
            dificuldade,
            criterioResumo,
            actionLabel,
            disponivel,
            disponivel ? blendedMatch >= 82 ? "pill-good" : "timeline-pill" : "pill-warn"
        );
        return new ScholarshipProjection(mock, blendedMatch, disponivel, criterioResumo);
    }

    private ScholarshipRequirements buildRequirements(BolsaDto bolsa, int index) {
        if (bolsa != null && bolsa.criterioDesempenhoMin() != null) {
            return new ScholarshipRequirements(
                Math.max(0, safeInt(bolsa.criterioMedalhasMin())),
                clampInt(safeInt(bolsa.criterioDesempenhoMin()), 0, 100),
                clampInt(safeInt(bolsa.criterioEvolucaoMin()), 0, 100),
                clampInt(safeInt(bolsa.criterioPrecisaoMin()), 0, 100),
                clampInt(safeInt(bolsa.criterioVelocidadeMin()), 0, 100)
            );
        }

        int baseMatch = clampInt(safeInt(bolsa.match()), 60, 92);
        return new ScholarshipRequirements(
            Math.min(4, 1 + index),
            clampInt(baseMatch - 12, 62, 88),
            clampInt(baseMatch - 16, 55, 84),
            clampInt(baseMatch - 18, 58, 86),
            clampInt(baseMatch - 24, 50, 80)
        );
    }

    private VBox buildMiniCard(String title, String value, String copy) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("h3-thin");

        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("h3-thin-big");
        valueLabel.setWrapText(true);

        Label copyLabel = new Label(copy);
        copyLabel.getStyleClass().add("muted");
        copyLabel.setWrapText(true);

        VBox card = new VBox(6, titleLabel, valueLabel, copyLabel);
        card.getStyleClass().add("weekly-mini-card");
        card.setPadding(new Insets(12));
        return card;
    }

    private HBox buildRankingRow(String position, String title, String copy, String badge, String badgeClass) {
        Label rankLabel = new Label(position);
        rankLabel.getStyleClass().add("leaderboard-rank");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("h3-thin-big");
        titleLabel.setWrapText(true);

        Label copyLabel = new Label(copy);
        copyLabel.getStyleClass().add("muted");
        copyLabel.setWrapText(true);

        VBox centerBox = new VBox(4, titleLabel, copyLabel);
        HBox.setHgrow(centerBox, Priority.ALWAYS);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label badgeLabel = new Label(badge);
        badgeLabel.getStyleClass().add(badgeClass);

        HBox row = new HBox(12, rankLabel, centerBox, spacer, badgeLabel);
        row.getStyleClass().add("leaderboard-row");
        row.setPadding(new Insets(12, 14, 12, 14));
        return row;
    }

    private List<ScoreBolsaRepository.WeeklyLeaderboardEntry> loadWeeklyLeaderboard(RankingWindow rankingWindow) {
        if (!RuntimeConfig.isDbEnabled() || rankingWindow == null) {
            return List.of();
        }

        try {
            return scoreBolsaRepository.findWeeklyLeaderboard(rankingWindow.inicio(), rankingWindow.fim());
        } catch (SQLException | RuntimeException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    private String buildRankingSectionCopy(
        RankingWindow rankingWindow,
        List<ScoreBolsaRepository.WeeklyLeaderboardEntry> leaderboard
    ) {
        String janela = rankingWindow == null
            ? "Janela semanal"
            : (rankingWindow.ativaAgora() ? "Janela ativa" : "Ultima janela fechada") + ": "
                + formatShortDate(rankingWindow.inicio()) + " a " + formatShortDate(rankingWindow.fim());

        if (leaderboard == null || leaderboard.isEmpty()) {
            return janela + ". Sem provas fechadas ainda.";
        }

        ScoreBolsaRepository.WeeklyLeaderboardEntry lider = leaderboard.getFirst();
        return janela + ". Lider: " + shortName(lider.candidatoNome()) + " com " + formatScorePoints(lider.score()) + ".";
    }

    private String buildCurrentScoreCopy(
        ScoreBolsaRepository.WeeklyLeaderboardEntry currentEntry,
        CandidateSignals signals,
        int totalParticipantes
    ) {
        if (currentEntry == null) {
            return "Ja ha " + totalParticipantes + " prova(s) fechadas. Falta a tua para virar posicao real.";
        }

        return "#" + currentEntry.posicao() + " com " + currentEntry.totalAcertos() + "/"
            + currentEntry.totalQuestoes() + " acertos.";
    }

    private String buildCutCopy(List<ScoreBolsaRepository.WeeklyLeaderboardEntry> leaderboard, int cutoff) {
        if (leaderboard == null || leaderboard.isEmpty()) {
            return "Sem scores registados.";
        }

        int cutoffIndex = Math.min(cutoff, leaderboard.size()) - 1;
        ScoreBolsaRepository.WeeklyLeaderboardEntry edge = leaderboard.get(cutoffIndex);
        String copy = formatScorePoints(edge.score()) + " | " + edge.totalAcertos() + "/" + edge.totalQuestoes()
            + " | " + shortName(edge.candidatoNome());
        if (leaderboard.size() < cutoff) {
            return "Parcial com " + leaderboard.size() + " candidato(s): " + copy;
        }
        return "Corte: " + copy;
    }

    private String buildCutBadge(int totalParticipantes, int cutoff) {
        return totalParticipantes >= cutoff ? "Corte real" : "Parcial";
    }

    private String buildCutBadgeClass(int totalParticipantes, int cutoff) {
        if (cutoff <= 10) {
            return "pill-good";
        }
        return totalParticipantes >= cutoff ? "timeline-pill" : "pill-warn";
    }

    private String buildCurrentPositionLabel(ScoreBolsaRepository.WeeklyLeaderboardEntry currentEntry) {
        return currentEntry == null || currentEntry.posicao() <= 0 ? "TU AGORA" : "#" + currentEntry.posicao();
    }

    private String buildCurrentRankingTitle(
        ScoreBolsaRepository.WeeklyLeaderboardEntry currentEntry,
        CandidateSignals signals
    ) {
        if (currentEntry == null) {
            return "Ainda nao entraste no ranking real";
        }
        if (currentEntry.posicao() <= 10) {
            return "Ja estas no top 10 semanal";
        }
        if (currentEntry.posicao() <= 25) {
            return "Ja estas na faixa principal";
        }
        if (currentEntry.posicao() <= 50) {
            return "Segues em observacao competitiva";
        }
        if (signals.readinessScore() >= 70) {
            return "Estas perto de subir no proximo fecho";
        }
        return "Ainda fora do corte principal";
    }

    private String buildCurrentRankingCopy(
        ScoreBolsaRepository.WeeklyLeaderboardEntry currentEntry,
        CandidateSignals signals,
        int totalParticipantes
    ) {
        if (currentEntry == null) {
            return "Projecao atual: " + formatScorePoints(signals.readinessScore()) + ". Ainda sem prova fechada.";
        }

        return formatScorePoints(currentEntry.score()) + " | #" + currentEntry.posicao()
            + " de " + totalParticipantes + " candidato(s).";
    }

    private String buildCurrentRankingBadge(ScoreBolsaRepository.WeeklyLeaderboardEntry currentEntry) {
        if (currentEntry == null) {
            return "Por entrar";
        }
        if (currentEntry.posicao() <= 10) {
            return "Top 10";
        }
        if (currentEntry.posicao() <= 25) {
            return "Top 25";
        }
        if (currentEntry.posicao() <= 50) {
            return "Top 50";
        }
        return "Em corrida";
    }

    private String buildCurrentRankingBadgeClass(ScoreBolsaRepository.WeeklyLeaderboardEntry currentEntry) {
        if (currentEntry == null) {
            return "timeline-pill";
        }
        if (currentEntry.posicao() <= 10) {
            return "pill-good";
        }
        if (currentEntry.posicao() <= 25) {
            return "timeline-pill";
        }
        return "pill-warn";
    }

    private String buildHeroSummary(CandidateSignals signals) {
        if (signals.previewMode()) {
            return "Ves primeiro o teu match, a janela e o que mais pesa para entrar.";
        }
        if (signals.readinessScore() >= 82) {
            return "Ja tens base para disputar as bolsas mais fortes desta semana.";
        }
        if (signals.readinessScore() >= 68) {
            return "Estas perto das bolsas medias. Mais precisao pode abrir mais portas.";
        }
        return "Ainda falta tracao. Foca em subir desempenho e velocidade.";
    }

    private String buildWindowLabel(WeeklyWindow weeklyWindow) {
        String prefix = weeklyWindow.abertaAgora() ? "Aberta" : "Proxima";
        return prefix + ": " + formatLongDate(weeklyWindow.abertura()) + " a " + formatLongDate(weeklyWindow.fechamento())
            + " | " + DIFFICULT_TEST_DURATION_MINUTES + " min";
    }

    private void abrirSimuladoBolsa(BolsaDto bolsa, ScholarshipProjection projection) {
        if (subtitleLabel == null || subtitleLabel.getScene() == null) {
            return;
        }

        StackPane contentHost = (StackPane) subtitleLabel.getScene().lookup("#contentHost");
        if (contentHost == null) {
            return;
        }

        BolsaSimuladoCoordinator.definir(
            new BolsaSimuladoCoordinator.BolsaSelection(
                bolsa,
                projection.prontidaoAtual(),
                projection.elegivel(),
                projection.criterioResumo()
            )
        );
        App.swapContent(contentHost, "views/pages/candidato/bolsa-simulado");
    }

    private String buildCurrentZoneTitle(CandidateSignals signals) {
        if (signals.readinessScore() >= 82) {
            return "Ja bates na zona de shortlist";
        }
        if (signals.readinessScore() >= 70) {
            return "Estas perto da zona competitiva";
        }
        return "Ainda em fase de tracao";
    }

    private String buildCurrentZoneCopy(CandidateSignals signals) {
        if (signals.readinessScore() >= 82) {
            return "Mantem a consistencia no dia da prova.";
        }
        if (signals.readinessScore() >= 70) {
            return "Mais precisao pode empurrar-te para o corte principal.";
        }
        return "Usa os proximos simulados para subir antes da proxima abertura.";
    }

    private UUID resolveCurrentUserId() {
        UUID authenticatedId = Authentication.getCurrentUserId();
        if (authenticatedId != null) {
            return authenticatedId;
        }

        String email = Authentication.getCurrentUserEmail();
        if (email == null || email.isBlank() || !RuntimeConfig.isDbEnabled()) {
            return null;
        }

        try {
            return userRepository.getIdByEmail(email);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String resolvePrimeiroNome() {
        String email = Authentication.getCurrentUserEmail();
        String nome = ProfileSessionState.resolveName(email, null);

        if ((nome == null || nome.isBlank()) && email != null && !email.isBlank() && RuntimeConfig.isDbEnabled()) {
            try {
                nome = userRepository.getNomeByEmail(email);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }

        if (nome == null || nome.isBlank()) {
            nome = email == null || email.isBlank() ? "candidato" : email;
        }

        String[] partes = nome.trim().split("\\s+");
        return partes.length == 0 ? "candidato" : partes[0];
    }

    private double computeEvolution(List<Teste_Stat> stats) {
        if (stats.isEmpty()) {
            return 0d;
        }

        int startIndex = Math.max(0, stats.size() - 6);
        List<Teste_Stat> sample = stats.subList(startIndex, stats.size());
        if (sample.size() < 2) {
            return average(sample.stream().map(Teste_Stat::percentual_acerto).map(this::normalizePercent).toList());
        }

        int split = Math.max(1, sample.size() / 2);
        double firstHalf = average(sample.subList(0, split).stream().map(Teste_Stat::percentual_acerto).map(this::normalizePercent).toList());
        double lastHalf = average(sample.subList(sample.size() - split, sample.size()).stream().map(Teste_Stat::percentual_acerto).map(this::normalizePercent).toList());
        double delta = lastHalf - firstHalf;
        return clamp(58d + (delta * 1.8d), 0d, 100d);
    }

    private double normalizePercent(Double value) {
        if (value == null) {
            return 0d;
        }
        return value <= 1.01d ? value * 100d : clamp(value, 0d, 100d);
    }

    private double normalizeMetric(Double value) {
        if (value == null) {
            return 0d;
        }
        return value <= 1.01d ? value * 100d : clamp(value, 0d, 100d);
    }

    private double average(List<Double> values) {
        if (values == null || values.isEmpty()) {
            return 0d;
        }

        double total = 0d;
        int count = 0;
        for (Double value : values) {
            if (value == null) {
                continue;
            }
            total += value;
            count++;
        }
        return count == 0 ? 0d : total / count;
    }

    private double ratio(double current, double required) {
        if (required <= 0d) {
            return 1d;
        }
        return clamp(current / required, 0d, 1d);
    }

    private double progressFromPercent(double percent) {
        return clamp(percent / 100d, 0d, 1d);
    }

    private String formatPercent(double value) {
        return Math.round(clamp(value, 0d, 100d)) + "%";
    }

    private String formatScorePoints(double value) {
        return Math.round(clamp(value, 0d, 100d)) + " pts";
    }

    private String formatLongDate(LocalDate date) {
        return date.getDayOfWeek().getDisplayName(TextStyle.SHORT, LOCALE_PT_AO) + " " + date.getDayOfMonth()
            + " de " + date.getMonth().getDisplayName(TextStyle.FULL, LOCALE_PT_AO);
    }

    private String formatShortDate(LocalDate date) {
        return date.getDayOfMonth() + " " + date.getMonth().getDisplayName(TextStyle.SHORT, LOCALE_PT_AO);
    }

    private String formatWeekday(LocalDate date) {
        return date.getDayOfWeek().getDisplayName(TextStyle.SHORT, LOCALE_PT_AO);
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return "";
    }

    private String shortName(String fullName) {
        String safe = firstNonBlank(fullName, "Candidato");
        String[] parts = safe.trim().split("\\s+");
        return parts.length == 0 ? safe : parts[0];
    }

    private int safeInt(Integer value) {
        return value == null ? 0 : value;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private int clampInt(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private record CandidateSignals(
        String primeiroNome,
        int medalhas,
        double desempenho,
        double precisao,
        double velocidade,
        double evolucao,
        int simulados,
        boolean previewMode
    ) {
        private double medalProgress() {
            return Math.max(0d, Math.min(1d, medalhas / 4d));
        }

        private double readinessProgress() {
            double weighted = (medalProgress() * 0.18d)
                + ((desempenho / 100d) * 0.28d)
                + ((precisao / 100d) * 0.20d)
                + ((velocidade / 100d) * 0.16d)
                + ((evolucao / 100d) * 0.18d);
            return Math.max(0d, Math.min(1d, weighted));
        }

        private double readinessScore() {
            return readinessProgress() * 100d;
        }
    }

    private record WeeklyWindow(LocalDate abertura, LocalDate fechamento, boolean abertaAgora) {
    }

    private record RankingWindow(LocalDate inicio, LocalDate fim, boolean ativaAgora) {
    }

    private record ScholarshipRequirements(int medalhas, int desempenho, int evolucao, int precisao, int velocidade) {
    }

    private record ScholarshipProjection(BolsaMock bolsaMock, int prontidaoAtual, boolean elegivel, String criterioResumo) {
    }
}
