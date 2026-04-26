package com.imetro.ui.controller.candidato.diagnosticos;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import com.imetro.domain.dto.Topico;

/**
 * Canal simples de comunicacao entre os componentes do diagnostico
 * (views/components) e a pagina principal
 * (views/pages/candidato/diagnostico.fxml).
 */
public final class DiagnosticoCoordinator {

    public record DiagnosticoConfig(String duracao, String foco, String nivel) {
    }

    public record AlertRequest(String titulo, String mensagem, Runnable onConfirm) {
    }

    public interface DiagnosticoHost {
        void startDiagnostico();
        void ModalOpen();
        void Alert();
        void StartInteligente();
    }

    private static final AtomicReference<DiagnosticoHost> HOST = new AtomicReference<>();
    private static final AtomicReference<ArrayList<Topico>> TOPICOS_SELECIONADOS =
        new AtomicReference<>(new ArrayList<>());
    private static final AtomicReference<Map<String, List<String>>> SUBTOPICOS_SELECIONADOS =
        new AtomicReference<>(new LinkedHashMap<>());
    private static final AtomicReference<DiagnosticoConfig> CONFIG_ATUAL = new AtomicReference<>();
    private static final AtomicReference<AlertRequest> ALERTA_ATUAL = new AtomicReference<>();

    private DiagnosticoCoordinator() {
    }

    public static void setHost(DiagnosticoHost host) {
        HOST.set(Objects.requireNonNull(host, "host"));
    }

    public static void clearHost(DiagnosticoHost host) {
        HOST.compareAndSet(host, null);
    }

    public static void requestStart(ArrayList<Topico> topicos) {
        TOPICOS_SELECIONADOS.set(new ArrayList<>(topicos));
        SUBTOPICOS_SELECIONADOS.set(new LinkedHashMap<>());
        CONFIG_ATUAL.set(null);

        DiagnosticoHost host = HOST.get();
        if (host != null) {
            host.ModalOpen();
        }
    }

    public static void requestAlert(String titulo, String mensagem, Runnable onConfirm) {
        ALERTA_ATUAL.set(new AlertRequest(titulo, mensagem, onConfirm));

        DiagnosticoHost host = HOST.get();
        if (host != null) {
            host.Alert();
        }
    }

    public static void requestStartInteligente(Map<String, String> configuracao) {
        CONFIG_ATUAL.set(
            new DiagnosticoConfig(
                configuracao.getOrDefault("duracao", "Curto"),
                configuracao.getOrDefault("foco", "Pontos fracos"),
                configuracao.getOrDefault("nivel", "Normal")
            )
        );

        DiagnosticoHost host = HOST.get();
        if (host != null) {
            host.StartInteligente();
        }
    }

    public static void requestStartSoRun() {
        DiagnosticoHost host = HOST.get();
        if (host != null) {
            host.startDiagnostico();
        }
    }

    public static ArrayList<Topico> getTopicosSelecionados() {
        return new ArrayList<>(TOPICOS_SELECIONADOS.get());
    }

    public static void updateSubtopicosSelecionados(Map<String, List<String>> subtopicos) {
        SUBTOPICOS_SELECIONADOS.set(new LinkedHashMap<>(subtopicos));
    }

    public static Map<String, List<String>> getSubtopicosSelecionados() {
        return new LinkedHashMap<>(SUBTOPICOS_SELECIONADOS.get());
    }

    public static DiagnosticoConfig getConfiguracaoAtual() {
        return CONFIG_ATUAL.get();
    }

    public static AlertRequest getAlertaAtual() {
        return ALERTA_ATUAL.get();
    }

    public static void confirmarAlertaAtual() {
        AlertRequest alerta = ALERTA_ATUAL.getAndSet(null);
        if (alerta != null && alerta.onConfirm() != null) {
            alerta.onConfirm().run();
        }
    }

    public static void limparAlertaAtual() {
        ALERTA_ATUAL.set(null);
    }

    public static String buildResumoSelecao() {
        ArrayList<Topico> topicos = TOPICOS_SELECIONADOS.get();
        if (topicos.isEmpty()) {
            return "Nenhum topico selecionado";
        }

        List<String> nomes = new ArrayList<>();
        for (Topico topico : topicos) {
            nomes.add(topico.disciplina() + " / " + topico.topicos());
        }
        return String.join(", ", nomes);
    }
}
