package com.imetro.ui.controller.candidato;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import com.imetro.domain.dto.Topico;
import com.imetro.ui.modals.FluxoModalContext;

public final class TesteAdaptativoCoordinator {

    public record TesteConfig(String duracao, String foco, String nivel) {
    }

    public record AlertRequest(String titulo, String mensagem, Runnable onConfirm) {
    }

    public interface TesteHost {
        void startTesteAdaptativo();
        void ModalOpen();
        void Alert();
        void StartInteligente();
    }

    private static final AtomicReference<TesteHost> HOST = new AtomicReference<>();
    private static final AtomicReference<String> DISCIPLINA_SELECIONADA = new AtomicReference<>();
    private static final AtomicReference<ArrayList<Topico>> TOPICOS_SELECIONADOS =
        new AtomicReference<>(new ArrayList<>());
    private static final AtomicReference<Map<String, List<String>>> SUBTOPICOS_SELECIONADOS =
        new AtomicReference<>(new LinkedHashMap<>());
    private static final AtomicReference<TesteConfig> CONFIG_ATUAL = new AtomicReference<>();
    private static final AtomicReference<AlertRequest> ALERTA_ATUAL = new AtomicReference<>();

    private TesteAdaptativoCoordinator() {
    }

    public static void setHost(TesteHost host) {
        HOST.set(Objects.requireNonNull(host, "host"));
    }

    public static void clearHost(TesteHost host) {
        HOST.compareAndSet(host, null);
    }

    public static void requestStart(String disciplina, ArrayList<Topico> topicos) {
        FluxoModalContext.setOrigem(FluxoModalContext.Origem.TESTE_ADAPTATIVO);
        DISCIPLINA_SELECIONADA.set(disciplina);
        TOPICOS_SELECIONADOS.set(new ArrayList<>(topicos));
        SUBTOPICOS_SELECIONADOS.set(new LinkedHashMap<>());
        CONFIG_ATUAL.set(null);

        TesteHost host = HOST.get();
        if (host != null) {
            host.ModalOpen();
        }
    }

    public static void requestAlert(String titulo, String mensagem, Runnable onConfirm) {
        FluxoModalContext.setOrigem(FluxoModalContext.Origem.TESTE_ADAPTATIVO);
        ALERTA_ATUAL.set(new AlertRequest(titulo, mensagem, onConfirm));

        TesteHost host = HOST.get();
        if (host != null) {
            host.Alert();
        }
    }

    public static void requestStartInteligente(Map<String, String> configuracao) {
        FluxoModalContext.setOrigem(FluxoModalContext.Origem.TESTE_ADAPTATIVO);
        CONFIG_ATUAL.set(
            new TesteConfig(
                configuracao.getOrDefault("duracao", "Curto"),
                configuracao.getOrDefault("foco", "Pontos fracos"),
                configuracao.getOrDefault("nivel", "Normal")
            )
        );

        TesteHost host = HOST.get();
        if (host != null) {
            host.StartInteligente();
        }
    }

    public static void requestStartSoRun() {
        FluxoModalContext.setOrigem(FluxoModalContext.Origem.TESTE_ADAPTATIVO);
        TesteHost host = HOST.get();
        if (host != null) {
            host.startTesteAdaptativo();
        }
    }

    public static String getDisciplinaSelecionada() {
        return DISCIPLINA_SELECIONADA.get();
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

    public static TesteConfig getConfiguracaoAtual() {
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
