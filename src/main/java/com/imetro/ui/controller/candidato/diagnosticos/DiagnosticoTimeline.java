package com.imetro.ui.controller.candidato.diagnosticos;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;

import com.imetro.domain.dto.diagnostico.DiagnosticoDto;
import com.imetro.services.DiagnosticoService;
import com.imetro.util.Authentication;
import com.imetro.util.ConversorTempo;
import com.imetro.util.TextoUtil;
import com.jfoenix.controls.JFXComboBox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class DiagnosticoTimeline implements Initializable {

    private static final Locale LOCALE_PT = new Locale("pt", "AO");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", LOCALE_PT);

    @FXML
    private DatePicker data;

    @FXML
    private JFXComboBox<String> disciplina;

    @FXML
    private VBox timelineContent;

    private final DiagnosticoService diagnosticoService = new DiagnosticoService();
    private final ArrayList<DiagnosticoDto> historicoCompleto = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (timelineContent == null) {
            return;
        }

        timelineContent.setFillWidth(true);
        prepararFiltros();
        carregarHistorico();
        renderizarTimeline(historicoCompleto);
    }

    private void prepararFiltros() {
        if (disciplina != null) {
            disciplina.getItems().clear();
            disciplina.setValue(null);
        }
        if (data != null) {
            data.setValue(null);
        }
    }

    private void carregarHistorico() {
        historicoCompleto.clear();

        historicoCompleto.addAll(
            diagnosticoService.listDiagnotico().stream()
                .filter(this::pertenceAoUtilizadorAtual)
                .sorted(
                    Comparator.comparing(
                        this::resolverMomento,
                        Comparator.nullsFirst(Comparator.naturalOrder())
                    ).reversed()
                )
                .collect(Collectors.toCollection(ArrayList::new))
        );

        LinkedHashSet<String> disciplinas = historicoCompleto.stream()
            .map(DiagnosticoDto::disciplina_nome)
            .filter(nome -> nome != null && !nome.isBlank())
            .sorted(String.CASE_INSENSITIVE_ORDER)
            .collect(Collectors.toCollection(LinkedHashSet::new));

        if (this.disciplina != null) {
            this.disciplina.getItems().setAll(disciplinas);
        }
    }

    private boolean pertenceAoUtilizadorAtual(DiagnosticoDto diagnostico) {
        UUID candidatoAtual = Authentication.getCurrentUserId();
        return candidatoAtual == null
            || (diagnostico.candidato_id() != null && candidatoAtual.equals(diagnostico.candidato_id()));
    }

    private void renderizarTimeline(List<DiagnosticoDto> diagnosticos) {
        timelineContent.getChildren().clear();

        if (diagnosticos == null || diagnosticos.isEmpty()) {
            boolean semHistorico = historicoCompleto.isEmpty();
            timelineContent.getChildren().add(
                criarEstadoVazio(
                    semHistorico ? "Ainda nao ha diagnosticos para mostrar." : "Nenhum diagnostico encontrado.",
                    semHistorico
                        ? "Quando terminares os teus diagnosticos, o historico completo vai aparecer aqui."
                        : "Tenta ajustar os filtros ou usa 'Limpar Filtros' para voltar ao historico completo."
                )
            );
            return;
        }

        LinkedHashMap<LocalDate, List<DiagnosticoDto>> diagnosticosPorData = diagnosticos.stream()
            .filter(diagnostico -> resolverMomento(diagnostico) != null)
            .collect(Collectors.groupingBy(
                diagnostico -> resolverMomento(diagnostico).toLocalDate(),
                LinkedHashMap::new,
                Collectors.toCollection(ArrayList::new)
            ));

        ArrayList<VBox> secoes = new ArrayList<>();
        for (var entry : diagnosticosPorData.entrySet()) {
            secoes.add(criarSecaoDia(entry.getKey(), entry.getValue()));
        }

        if (secoes.isEmpty()) {
            timelineContent.getChildren().add(
                criarEstadoVazio(
                    "Nao foi possivel montar a linha do tempo.",
                    "Os diagnosticos encontrados estao sem data valida para exibicao."
                )
            );
            return;
        }

        timelineContent.getChildren().setAll(secoes);
    }

    private VBox criarSecaoDia(LocalDate dataReferencia, List<DiagnosticoDto> diagnosticosDoDia) {
        VBox secao = new VBox(10);
        secao.getStyleClass().add("diagnostico-timeline-section");
        secao.setFillWidth(true);

        HBox header = new HBox(10);
        header.getStyleClass().add("diagnostico-timeline-header");
        header.setAlignment(Pos.CENTER_LEFT);

        Label dataLabel = new Label(formatarData(dataReferencia));
        dataLabel.getStyleClass().add("diagnostico-timeline-date");
        header.getChildren().addAll(dataLabel);

        VBox lista = new VBox(8);
        lista.setFillWidth(true);

        for (int index = 0; index < diagnosticosDoDia.size(); index++) {
            DiagnosticoDto diagnostico = diagnosticosDoDia.get(index);
            LocalDateTime momento = resolverMomento(diagnostico);
            if (momento == null) {
                continue;
            }
            lista.getChildren().add(criarLinhaDiagnostico(index + 1, diagnostico));
        }

        if (lista.getChildren().isEmpty()) {
            return criarEstadoVazio(
                "Sem diagnosticos com data valida.",
                "Os itens desse dia nao possuem informacao suficiente para serem exibidos."
            );
        }

        secao.getChildren().addAll(header, lista);
        return secao;
    }

    private List<DiagnosticoDto> filtrarDiagnosticos() {
        return historicoCompleto.stream()
            .filter(this::correspondeDisciplina)
            .filter(this::correspondeData)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    private boolean correspondeDisciplina(DiagnosticoDto diagnostico) {
        String disciplinaSelecionada = disciplina == null ? null : disciplina.getValue();
        if (disciplinaSelecionada == null || disciplinaSelecionada.isBlank()) {
            return true;
        }
        return TextoUtil.normalizarMinusculo(TextoUtil.safeText(diagnostico.disciplina_nome(), ""))
            .equals(TextoUtil.normalizarMinusculo(disciplinaSelecionada));
    }

    private boolean correspondeData(DiagnosticoDto diagnostico) {
        LocalDate dataSelecionada = data == null ? null : data.getValue();
        if (dataSelecionada == null) {
            return true;
        }

        LocalDateTime momento = resolverMomento(diagnostico);
        return momento != null && dataSelecionada.equals(momento.toLocalDate());
    }

    private HBox criarLinhaDiagnostico(int numero, DiagnosticoDto diagnostico) {
        String disciplinaTexto = TextoUtil.safeText(diagnostico.disciplina_nome(), "Sem disciplina");
        String percentualTexto = formatPercent(diagnostico.percentual_acerto());
        String duracaoTexto = ConversorTempo.formatarDuracao(diagnostico.duracao_segundos());

        Label ordemLabel = new Label("Diagnóstico #" + numero);
        ordemLabel.getStyleClass().add("h2-thin");

        Label disciplinaLabel = new Label(disciplinaTexto);
        disciplinaLabel.getStyleClass().add("diagnostico-timeline-title");
        disciplinaLabel.setWrapText(true);

        Label percentLabel = new Label(percentualTexto);
        percentLabel.getStyleClass().add("big-h2");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label duracaoLabel = new Label("Duração " + duracaoTexto);
        duracaoLabel.getStyleClass().add("diagnostico-timeline-meta");
        duracaoLabel.setWrapText(true);

        VBox card = new VBox(4, ordemLabel,disciplinaLabel, duracaoLabel);
        card.setFillWidth(true);
        card.setMaxWidth(Double.MAX_VALUE);
        HBox cardF=new HBox(10, card,spacer, percentLabel);
        cardF.getStyleClass().add("card");
        cardF.setPadding(new Insets(20));
        cardF.setMaxWidth(400);
        cardF.setMinWidth(500);
        return cardF;
    }

    private LocalDateTime resolverMomento(DiagnosticoDto diagnostico) {
        if (diagnostico == null) {
            return null;
        }
        if (diagnostico.concluido_em() != null) {
            return diagnostico.concluido_em();
        }
        if (diagnostico.iniciado_em() != null) {
            return diagnostico.iniciado_em();
        }
        return diagnostico.criado_em();
    }

    private String formatarData(LocalDate value) {
        if (value == null) {
            return "Sem data";
        }
        return value.format(DATE_FORMAT);
    }

    private String formatPercent(double valor) {
        return Math.round(Math.max(0d, Math.min(100d, valor))) + "%";
    }



    private VBox criarEstadoVazio(String tituloTexto, String descricaoTexto) {
        Label titulo = new Label(tituloTexto);
        titulo.getStyleClass().add("h1-thin");

        Label descricao = new Label(descricaoTexto);
        descricao.getStyleClass().add("muted");
        descricao.setWrapText(true);

        VBox estado = new VBox(10, titulo, descricao);
        estado.getStyleClass().addAll("placeholder-card", "diagnostico-empty-state");
        estado.setAlignment(Pos.CENTER_LEFT);
        estado.setMaxWidth(540);
        return estado;
    }

    @FXML
    public void Limpar(ActionEvent event) {
        if (data != null) {
            data.setValue(null);
        }
        if (disciplina != null) {
            disciplina.getSelectionModel().clearSelection();
            disciplina.setValue(null);
        }

        renderizarTimeline(historicoCompleto);
    }

    @FXML
    public void Procurar(ActionEvent event) {
        renderizarTimeline(filtrarDiagnosticos());
    }
}
