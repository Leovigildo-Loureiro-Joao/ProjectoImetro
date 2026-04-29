package com.imetro.ui.controller.candidato;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.imetro.config.RuntimeConfig;
import com.imetro.domain.dto.BolsaMock;
import com.imetro.persistence.repository.UserRepository;
import com.imetro.ui.components.BolsaCard;
import com.imetro.ui.components.CircleProgress;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class BolsasController implements Initializable {

   

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
    private VBox passosBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String primeiroNome = resolvePrimeiroNome();
        String hoje = LocalDate.now().format(DateTimeFormatter.ofPattern("dd 'de' MMMM", new Locale("pt", "AO")));

        subtitleLabel.setText("Radar criativo de oportunidades para " + primeiroNome + ", atualizado em " + hoje + ".");
        readinessScoreLabel.setText("76%");
        radarSummaryLabel.setText("O teu perfil ja mostra competitividade real para bolsas de merito, principalmente quando o historico recente sustenta consistencia.");
        janelaLabel.setText("Melhor janela: proximos 30 dias");

        prontidaoBar.setProgress(0.76);
        alinhamentoBar.setProgress(0.82);
        documentosBar.setProgress(0.58);
        competitividadeBar.setProgress(0.67);

        prontidaoText.setText("Perfil academico competitivo");
        alinhamentoText.setText("Boa aderencia a STEM e desempenho quantitativo");
        documentosText.setText("Falta fechar carta, historico e comprovativos");
        competitividadeText.setText("Concorres bem, mas precisas de narrativa forte");

        setupRing();
        setupScholarships();
        setupFactors();
        setupSteps();
    }

    private void setupRing() {
        CircleProgress progress = new CircleProgress(58, 58, 58, 0.76f);
        progress.setSubtitle("Match");
        matchRingHost.getChildren().setAll(progress);
    }

    private void setupScholarships() {
        List<BolsaMock> bolsas = List.of(
            new BolsaMock(
                "Bolsa Merito Atlas",
                "Propina + mentoria",
                88,
                "Cobertura quase total da propina",
                "Fecha em 21 dias",
                "Excelente para quem sustenta melhoria continua.",
                "Precisa de carta pessoal forte.",
                "pill-good"
            ),
            new BolsaMock(
                "Programa Horizonte STEM",
                "Parcial + laboratorio",
                79,
                "Apoio parcial e acesso a projetos",
                "Fecha em 34 dias",
                "Grande encaixe para Matematica e Fisica.",
                "Alta concorrencia entre perfis tecnicos.",
                "pill-good"
            ),
            new BolsaMock(
                "Fundo Impulso Academico",
                "Auxilio de mensalidade",
                71,
                "Apoio modular por semestre",
                "Fecha em 16 dias",
                "Boa opcao para ganhar tracao rapida.",
                "Documentacao precisa estar impecavel.",
                "pill-warn"
            ),
            new BolsaMock(
                "Beca Impacto Local",
                "Merito + projeto comunitario",
                67,
                "Cobertura media com bonus por impacto",
                "Fecha em 40 dias",
                "Diferencia-te se mostrares lideranca aplicada.",
                "Exige narrativa social mais madura.",
                "pill-warn"
            )
        );

        bolsasFlow.getChildren().clear();
        for (BolsaMock bolsa : bolsas) {
            bolsasFlow.getChildren().add(new BolsaCard(bolsa));
        }
    }

    

    private void setupFactors() {
        fatoresBox.getChildren().setAll(
            buildSectionTitle("O que mais pesa no teu match", "Leituras para priorizar antes da candidatura."),
            buildFactorRow("Historico recente", 0.84, "A melhoria sustentada esta a teu favor."),
            buildFactorRow("Narrativa pessoal", 0.61, "Ja tens base, mas falta mais clareza de objetivo."),
            buildFactorRow("Documentos-chave", 0.58, "Ainda e o teu principal gargalo operacional."),
            buildFactorRow("Consistencia academica", 0.79, "Boa regularidade em testes e diagnosticos.")
        );
    }

    private void setupSteps() {
        passosBox.getChildren().setAll(
            buildSectionTitle("Passos sugeridos", "Sequencia simples para sair do modo ideia e entrar em candidatura."),
            buildStepRow("1", "Escolher 2 bolsas-alvo e travar um prazo interno para cada uma."),
            buildStepRow("2", "Fechar historico, carta curta e comprovativos num unico pacote."),
            buildStepRow("3", "Usar o proximo teste adaptativo como reforco de narrativa academica."),
            buildStepRow("4", "Rever a candidatura com foco em impacto, constancia e clareza.")
        );
    }

    private VBox buildSectionTitle(String title, String subtitle) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("h1-thin");

        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.getStyleClass().add("muted");
        subtitleLabel.setWrapText(true);

        VBox box = new VBox(6, titleLabel, subtitleLabel);
        return box;
    }

    private VBox buildFactorRow(String title, double progress, String description) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("h3-thin-big");

        Label percentLabel = new Label(Math.round(progress * 100) + "%");
        percentLabel.getStyleClass().add("percent-value");

        HBox header = new HBox(10, titleLabel, percentLabel);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);

        ProgressBar bar = new ProgressBar(progress);
        bar.setPrefWidth(360);
        bar.getStyleClass().add("report-progress");

        Label descLabel = new Label(description);
        descLabel.getStyleClass().add("muted");
        descLabel.setWrapText(true);

        VBox row = new VBox(5, header, bar, descLabel);
        row.getStyleClass().add("factor-row");
        row.setPadding(new Insets(12, 0, 12, 0));
        return row;
    }

    private HBox buildStepRow(String number, String description) {
        Label numberLabel = new Label(number);
        numberLabel.getStyleClass().add("insight-bullet");

        Label descriptionLabel = new Label(description);
        descriptionLabel.getStyleClass().add("h3-thin-big");
        descriptionLabel.setWrapText(true);

        HBox row = new HBox(12, numberLabel, descriptionLabel);
        row.getStyleClass().add("timeline-step");
        row.setPadding(new Insets(12, 14, 12, 14));
        return row;
    }

    private String resolvePrimeiroNome() {
        String email = Authentication.getCurrentUserEmail();
        String nome = ProfileSessionState.resolveName(email, null);

        if ((nome == null || nome.isBlank()) && email != null && !email.isBlank() && RuntimeConfig.isDbEnabled()) {
            try {
                nome = new UserRepository().getNomeByEmail(email);
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
}
