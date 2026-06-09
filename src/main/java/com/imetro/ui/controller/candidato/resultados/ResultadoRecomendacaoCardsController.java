package com.imetro.ui.controller.candidato.resultados;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

import com.imetro.domain.dto.planejamento.PlaneamentoEstudoEtapa;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoResumo;
import com.imetro.services.PlaneamentoEstudoService;
import com.imetro.ui.controller.lifecycle.DisposableController;
import com.imetro.util.Authentication;
import com.imetro.util.ResultadoPayload;
import com.imetro.util.TextoUtil;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ResultadoRecomendacaoCardsController implements Initializable, DisposableController {

    private static final double CARD_WIDTH = 560d;

    @FXML
    private Label subtituloLabel;

    @FXML
    private VBox cardsBox;

    private final PlaneamentoEstudoService planeamentoService = new PlaneamentoEstudoService();
    private PlaneamentoEstudoResumo resumo;
    private ResultadoPayload resultado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resultado = ResultadoAvaliacaoController.ultimoResultado;

        UUID candidatoId = Authentication.getCurrentUserId();
        resumo = planeamentoService.gerarResumo(candidatoId);
        var estado = planeamentoService.resolverEstadoAtual(candidatoId);

        if (subtituloLabel != null) {
            subtituloLabel.setText(construirSubtitulo(estado == null ? null : estado.detalhe()));
        }

        renderizarCards();
    }

    @Override
    public void dispose() {
    }

    private void renderizarCards() {
        if (cardsBox == null) {
            return;
        }

        cardsBox.getChildren().setAll(
            criarCardFoco(),
            criarCardRitmo(),
            criarCardProximaEtapa()
        );
        cardsBox.setAlignment(Pos.TOP_LEFT);
        cardsBox.setFillWidth(false);
    }

    private VBox criarCardFoco() {
        String focoAtual = textoOuPadrao(resumo == null ? null : resumo.focoAtual(), "Sem foco definido");
        String focoSecundario = textoOuPadrao(resumo == null ? null : resumo.focoAtual2(), "Sem foco secundário");
        String resumoHero = textoOuPadrao(
            resumo == null ? null : resumo.resumoHero(),
            "O planeamento vai ficar mais preciso à medida que o histórico cresce."
        );

        return criarCard(
            "Plano",
            "result-trail-badge-good",
            "Foco atual",
            focoAtual,
            resumoHero + "\nSegundo foco: " + focoSecundario
        );
    }

    private VBox criarCardRitmo() {
        String acertoMedio = textoOuPadrao(resumo == null ? null : resumo.acertoMedio(), "-");
        String ritmoMedio = textoOuPadrao(resumo == null ? null : resumo.ritmoMedio(), "-");
        String consistenciaMedia = textoOuPadrao(resumo == null ? null : resumo.consistenciaMedia(), "-");

        return criarCard(
            "Métrica",
            "result-trail-badge",
            "Ritmo e consistência",
            acertoMedio + " de acerto",
            "Consistência " + consistenciaMedia + "  |  Ritmo " + ritmoMedio
        );
    }

    private VBox criarCardProximaEtapa() {
        PlaneamentoEstudoEtapa etapa = primeiraEtapa();
        String janela = etapa == null ? "Hoje" : textoOuPadrao(etapa.janela(), "Hoje");
        String acao = etapa == null ? "Rever a base" : textoOuPadrao(etapa.acao(), "Rever a base");
        String detalhe = etapa == null ? "Mantém a rotina curta e consistente." : textoOuPadrao(etapa.detalhe(), "");

        return criarCard(
            "Ação",
            "result-trail-badge-warn",
            "Próxima etapa",
            janela,
            detalhe.isBlank() ? acao : acao + "  " + detalhe
        );
    }

    private VBox criarCard(
        String badgeTexto,
        String badgeClasse,
        String titulo,
        String valor,
        String detalhe
    ) {
        Label badge = new Label(textoOuPadrao(badgeTexto, ""));
        badge.getStyleClass().add("result-trail-badge");
        if (badgeClasse != null && !badgeClasse.isBlank()) {
            badge.getStyleClass().add(badgeClasse);
        }

        Label tituloLabel = new Label(textoOuPadrao(titulo, "Recomendação"));
        tituloLabel.getStyleClass().add("question-side-title");

        HBox header = new HBox(10, tituloLabel, badge);
        HBox.setHgrow(tituloLabel, Priority.ALWAYS);
        header.setAlignment(Pos.CENTER_LEFT);

        Label valorLabel = new Label(textoOuPadrao(valor, "-"));
        valorLabel.getStyleClass().add("h3-thin-big");
        valorLabel.setWrapText(true);

        Label detalheLabel = new Label(textoOuPadrao(detalhe, ""));
        detalheLabel.getStyleClass().add("question-side-copy");
        detalheLabel.setWrapText(true);

        VBox card = new VBox(8, header, valorLabel, detalheLabel);
        card.getStyleClass().add("result-recommendation-card");
        card.setPadding(new Insets(14));
        card.setMinWidth(CARD_WIDTH);
        card.setPrefWidth(CARD_WIDTH);
        card.setMaxWidth(CARD_WIDTH);
        return card;
    }

    private PlaneamentoEstudoEtapa primeiraEtapa() {
        if (resumo == null || resumo.etapas() == null || resumo.etapas().isEmpty()) {
            return null;
        }

        for (PlaneamentoEstudoEtapa etapa : resumo.etapas()) {
            if (etapa != null) {
                return etapa;
            }
        }
        return null;
    }

    private String construirSubtitulo(String estadoDetalhe) {
        String detalheEstado = textoOuPadrao(estadoDetalhe, "");
        if (!detalheEstado.isBlank()) {
            return detalheEstado;
        }

        if (resultado != null && textoReal(resultado.getRecomendacao())) {
            return resultado.getRecomendacao();
        }

        return "Cards simples gerados a partir do teu planeamento atual.";
    }

    private String textoOuPadrao(String valor, String padrao) {
        String texto = TextoUtil.safeText(valor, padrao);
        if (texto == null || texto.isBlank() || "-".equals(texto)) {
            return padrao;
        }
        return texto;
    }

    private boolean textoReal(String valor) {
        String texto = TextoUtil.safeText(valor, "");
        return !texto.isBlank() && !"-".equals(texto);
    }
}
