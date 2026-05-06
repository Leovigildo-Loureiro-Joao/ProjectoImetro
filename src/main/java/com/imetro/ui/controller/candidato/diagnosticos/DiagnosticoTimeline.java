package com.imetro.ui.controller.candidato.diagnosticos;

import java.net.URL;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;

import com.imetro.domain.dto.Stats;
import com.imetro.domain.dto.diagnostico.DiagnosticoDto;
import com.imetro.domain.dto.diagnostico.TimelineDTO;
import com.imetro.services.DiagnosticoService;
import com.imetro.ui.components.TimelineCard;
import com.imetro.util.Authentication;
import com.jfoenix.controls.JFXComboBox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DiagnosticoTimeline implements Initializable {

    @FXML
    private DatePicker data;

    @FXML
    private JFXComboBox<String> disciplina;

    @FXML
    private JFXComboBox<String> horas;

    @FXML
    private JFXComboBox<String> minutos;

    @FXML
    private VBox timelineContent;

    private final DiagnosticoService diagnosticoService = new DiagnosticoService();
    private final ArrayList<DiagnosticoDto> historicoCompleto = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (timelineContent == null) {
            return;
        }

        prepararFiltros();
        carregarHistorico();
        renderizarTimeline(historicoCompleto);
    }

    private void prepararFiltros() {
        if (disciplina != null) {
            disciplina.getItems().clear();
            disciplina.setValue(null);
        }
        if (horas != null) {
            horas.getItems().clear();
            for (int hora = 0; hora < 24; hora++) {
                horas.getItems().add(String.format("%02d", hora));
            }
            horas.setValue(null);
        }
        if (minutos != null) {
            minutos.getItems().clear();
            for (int minuto = 0; minuto < 60; minuto++) {
                minutos.getItems().add(String.format("%02d", minuto));
            }
            minutos.setValue(null);
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

        ArrayList<VBox> cardsTimeline = new ArrayList<>();
        for (var entry : diagnosticosPorData.entrySet()) {
            cardsTimeline.add(criarCardTimeline(entry.getKey(), entry.getValue()));
        }

        if (cardsTimeline.isEmpty()) {
            timelineContent.getChildren().add(
                criarEstadoVazio(
                    "Nao foi possivel montar o timeline.",
                    "Os diagnosticos encontrados estao sem data valida para exibicao."
                )
            );
            return;
        }

        timelineContent.getChildren().setAll(cardsTimeline);
    }

    private VBox criarCardTimeline(LocalDate dataReferencia, List<DiagnosticoDto> diagnosticosDoDia) {
        ArrayList<LocalTime> horarios = new ArrayList<>();
        ArrayList<Float> acertos = new ArrayList<>();
        ArrayList<Float> erros = new ArrayList<>();
        ArrayList<Float> evolucoes = new ArrayList<>();
        ArrayList<Stats> metricas = new ArrayList<>();
        ArrayList<String> disciplinas = new ArrayList<>();
        ArrayList<String> duracoes = new ArrayList<>();

        for (DiagnosticoDto diagnostico : diagnosticosDoDia) {
            LocalDateTime momento = resolverMomento(diagnostico);
            if (momento == null) {
                continue;
            }

            horarios.add(momento.toLocalTime());
            disciplinas.add(textoSeguro(diagnostico.disciplina_nome(), "Sem disciplina"));
            duracoes.add(formatarDuracao(diagnostico.duracao_segundos()));
            acertos.add((float) diagnostico.total_acertos());
            erros.add((float) diagnostico.total_erros());
            evolucoes.add((float) diagnostico.evolucao_percentual());
            metricas.add(
                new Stats(
                    limitarProgresso(diagnostico.velocidade()),
                    limitarProgresso(diagnostico.precisao()),
                    limitarProgresso(diagnostico.consistencia()),
                    limitarProgresso(diagnostico.logica()),
                    limitarProgresso(diagnostico.resiliencia())
                )
            );
        }

        return new TimelineCard(
            new TimelineDTO(
                dataReferencia,
                horarios,
                disciplinas.toArray(String[]::new),
                duracoes.toArray(String[]::new),
                acertos,
                erros,
                evolucoes,
                metricas
            )
        ).getRoot();
    }

    private List<DiagnosticoDto> filtrarDiagnosticos() {
        return historicoCompleto.stream()
            .filter(this::correspondeDisciplina)
            .filter(this::correspondeData)
            .filter(this::correspondeHora)
            .filter(this::correspondeMinuto)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    private boolean correspondeDisciplina(DiagnosticoDto diagnostico) {
        String disciplinaSelecionada = disciplina == null ? null : disciplina.getValue();
        if (disciplinaSelecionada == null || disciplinaSelecionada.isBlank()) {
            return true;
        }
        return normalizar(textoSeguro(diagnostico.disciplina_nome(), ""))
            .equals(normalizar(disciplinaSelecionada));
    }

    private boolean correspondeData(DiagnosticoDto diagnostico) {
        LocalDate dataSelecionada = data == null ? null : data.getValue();
        if (dataSelecionada == null) {
            return true;
        }

        LocalDateTime momento = resolverMomento(diagnostico);
        return momento != null && dataSelecionada.equals(momento.toLocalDate());
    }

    private boolean correspondeHora(DiagnosticoDto diagnostico) {
        String horaSelecionada = horas == null ? null : horas.getValue();
        if (horaSelecionada == null || horaSelecionada.isBlank()) {
            return true;
        }

        LocalDateTime momento = resolverMomento(diagnostico);
        return momento != null && String.format("%02d", momento.getHour()).equals(horaSelecionada);
    }

    private boolean correspondeMinuto(DiagnosticoDto diagnostico) {
        String minutoSelecionado = minutos == null ? null : minutos.getValue();
        if (minutoSelecionado == null || minutoSelecionado.isBlank()) {
            return true;
        }

        LocalDateTime momento = resolverMomento(diagnostico);
        return momento != null && String.format("%02d", momento.getMinute()).equals(minutoSelecionado);
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

    private float limitarProgresso(float valor) {
        return Math.max(0f, Math.min(1f, valor));
    }

    private String formatarDuracao(int duracaoSegundos) {
        int totalSegundos = Math.max(0, duracaoSegundos);
        int horas = totalSegundos / 3600;
        int minutos = (totalSegundos % 3600) / 60;
        int segundos = totalSegundos % 60;

        if (horas > 0) {
            return String.format("%dh %02dm", horas, minutos);
        }
        if (minutos > 0) {
            return String.format("%dmin %02ds", minutos, segundos);
        }
        return segundos + "s";
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

    private String textoSeguro(String valor, String padrao) {
        if (valor == null) {
            return padrao;
        }
        String texto = valor.trim();
        return texto.isEmpty() ? padrao : texto;
    }

    private String normalizar(String valor) {
        String texto = valor == null ? "" : valor;
        String semAcento = Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}+", "");
        return semAcento.trim().toLowerCase(Locale.ROOT);
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
        if (horas != null) {
            horas.getSelectionModel().clearSelection();
            horas.setValue(null);
        }
        if (minutos != null) {
            minutos.getSelectionModel().clearSelection();
            minutos.setValue(null);
        }

        renderizarTimeline(historicoCompleto);
    }

    @FXML
    public void Procurar(ActionEvent event) {
        renderizarTimeline(filtrarDiagnosticos());
    }
}
