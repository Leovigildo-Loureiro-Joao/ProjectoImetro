package com.imetro.ui.controller.candidato;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.imetro.config.RuntimeConfig;
import com.imetro.domain.dto.bolsa.BolsaDto;
import com.imetro.domain.dto.bolsa.BolsaMock;
import com.imetro.persistence.repository.BolsaRepository;
import com.imetro.persistence.repository.UserRepository;
import com.imetro.ui.components.CircleProgress;
import com.imetro.ui.components.bolsas.BolsaCard;
import com.imetro.ui.components.bolsas.FactoryRow;
import com.imetro.ui.components.bolsas.SectionTitle;
import com.imetro.ui.components.bolsas.StepRow;
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
    private BolsaRepository bolsaRepository;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bolsaRepository=new BolsaRepository();
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
        List<BolsaDto> bolsas;
        try {
            bolsas = bolsaRepository.findAll().stream()
            .map(BolsaDto::fromMap).toList();
            bolsasFlow.getChildren().clear();
            for (BolsaDto bolsa : bolsas) {
                bolsasFlow.getChildren().add(new BolsaCard(bolsa));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    private void setupFactors() {
        fatoresBox.getChildren().setAll(
            new SectionTitle("O que mais pesa no teu match", "Leituras para priorizar antes da candidatura."),
            new FactoryRow("Historico recente", 0.84, "A melhoria sustentada esta a teu favor."),
            new FactoryRow("Narrativa pessoal", 0.61, "Ja tens base, mas falta mais clareza de objetivo."),
            new FactoryRow("Documentos-chave", 0.58, "Ainda e o teu principal gargalo operacional."),
            new FactoryRow("Consistencia academica", 0.79, "Boa regularidade em testes e diagnosticos.")
        );
    }

    private void setupSteps() {
        passosBox.getChildren().setAll(
            new SectionTitle("Passos sugeridos", "Sequencia simples para sair do modo ideia e entrar em candidatura."),
            new StepRow("1", "Escolher 2 bolsas-alvo e travar um prazo interno para cada uma."),
            new StepRow("2", "Fechar historico, carta curta e comprovativos num unico pacote."),
            new StepRow("3", "Usar o proximo teste adaptativo como reforco de narrativa academica."),
            new StepRow("4", "Rever a candidatura com foco em impacto, constancia e clareza.")
        );
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
