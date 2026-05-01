package com.imetro.services;

import com.imetro.App;
import com.imetro.util.AppLogger;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class PerguntasBootstrapAsyncService {

    private static final PerguntasBootstrapAsyncService INSTANCE = new PerguntasBootstrapAsyncService();
    private static final Logger LOGGER = AppLogger.getLogger(PerguntasBootstrapAsyncService.class);

    private final PerguntasBootstrapService bootstrapService = new PerguntasBootstrapService();
    private final BooleanProperty running = new SimpleBooleanProperty(false);
    private final BooleanProperty showBanner = new SimpleBooleanProperty(false);
    private final DoubleProperty progress = new SimpleDoubleProperty(0.0);
    private final StringProperty title = new SimpleStringProperty("Base de estudo");
    private final StringProperty detail = new SimpleStringProperty("");
    private final StringProperty summary = new SimpleStringProperty("");
    private final ObjectProperty<BootstrapUiState> state = new SimpleObjectProperty<>(BootstrapUiState.IDLE);

    private volatile UUID activeCandidateId;
    private Task<List<PerguntasBootstrapService.BootstrapResult>> currentTask;

    private PerguntasBootstrapAsyncService() {
    }

    public static PerguntasBootstrapAsyncService getInstance() {
        return INSTANCE;
    }

    public synchronized void start(UUID candidatoId) {
        if (candidatoId == null) {
            LOGGER.warning("Tentativa de iniciar o bootstrap automatico sem candidatoId.");
            return;
        }

        if (currentTask != null && currentTask.isRunning()) {
            if (candidatoId.equals(activeCandidateId)) {
                showBanner.set(true);
                LOGGER.info("Bootstrap automatico ja estava em curso para o candidato " + candidatoId + ".");
            }
            return;
        }

        LOGGER.info("A iniciar o bootstrap automatico de perguntas para o candidato " + candidatoId + ".");
        activeCandidateId = candidatoId;
        title.set("A preparar a tua base de estudo");
        detail.set("Os livros vao ser lidos em segundo plano. Podes navegar noutras abas.");
        summary.set("");
        progress.set(0.0);
        state.set(BootstrapUiState.RUNNING);
        running.set(true);
        showBanner.set(true);

        Task<List<PerguntasBootstrapService.BootstrapResult>> task = new Task<>() {
            @Override
            protected List<PerguntasBootstrapService.BootstrapResult> call() {
                updateTitle("A preparar a tua base de estudo");
                updateMessage("Os livros vao ser lidos em segundo plano. Podes navegar noutras abas.");
                updateProgress(0, 1);

                return bootstrapService.processarDisciplinasAutomaticasDoCandidato(
                    candidatoId,
                    false,
                    snapshot -> {
                        updateTitle(snapshot.titulo());
                        updateMessage(snapshot.detalhe());
                        if (snapshot.indeterminate()) {
                            updateProgress(-1, 1);
                        } else {
                            updateProgress(snapshot.progress(), 1);
                        }
                    }
                );
            }
        };

        bindToTask(task);
        currentTask = task;

        task.setOnSucceeded(event -> finishSuccessfully(task.getValue()));
        task.setOnFailed(event -> finishWithFailure(task.getException()));
        task.setOnCancelled(event -> finishCancelled());

        App.getExecutorService().execute(task);
    }

    public synchronized boolean isRunningFor(UUID candidatoId) {
        return running.get() && candidatoId != null && candidatoId.equals(activeCandidateId);
    }

    public BooleanProperty runningProperty() {
        return running;
    }

    public BooleanProperty showBannerProperty() {
        return showBanner;
    }

    public DoubleProperty progressProperty() {
        return progress;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty detailProperty() {
        return detail;
    }

    public StringProperty summaryProperty() {
        return summary;
    }

    public ObjectProperty<BootstrapUiState> stateProperty() {
        return state;
    }

    public BootstrapUiState getState() {
        return state.get();
    }

    private synchronized void bindToTask(Task<?> task) {
        releaseTaskBindings();
        title.bind(task.titleProperty());
        detail.bind(task.messageProperty());
        progress.bind(task.progressProperty());
    }

    private synchronized void releaseTaskBindings() {
        if (title.isBound()) {
            title.unbind();
        }
        if (detail.isBound()) {
            detail.unbind();
        }
        if (progress.isBound()) {
            progress.unbind();
        }
    }

    private void finishSuccessfully(List<PerguntasBootstrapService.BootstrapResult> results) {
        releaseTaskBindings();
        currentTask = null;
        running.set(false);
        progress.set(1.0);

        BootstrapUiState nextState = resolveState(results);
        state.set(nextState);
        title.set(resolveTitle(nextState));

        String resumo = buildSummary(results);
        detail.set(resumo);
        summary.set(resumo);
        showBanner.set(true);
        LOGGER.info("Bootstrap automatico concluido para o candidato " + activeCandidateId + ". " + resumo);
    }

    private void finishWithFailure(Throwable error) {
        releaseTaskBindings();
        currentTask = null;
        running.set(false);
        progress.set(1.0);
        state.set(BootstrapUiState.ERROR);
        title.set("Falha ao preparar os livros");
        String mensagem = error == null || error.getMessage() == null || error.getMessage().isBlank()
            ? "Nao foi possivel concluir o processamento automatico agora."
            : error.getMessage();
        String detalheComLog = mensagem + " Ver logs em " + AppLogger.getLogFilePath() + ".";
        detail.set(detalheComLog);
        summary.set(detalheComLog);
        showBanner.set(true);
        LOGGER.log(Level.SEVERE, "Bootstrap automatico falhou para o candidato " + activeCandidateId + ".", error);
    }

    private void finishCancelled() {
        releaseTaskBindings();
        currentTask = null;
        running.set(false);
        progress.set(1.0);
        state.set(BootstrapUiState.WARNING);
        title.set("Processamento interrompido");
        detail.set("A leitura dos livros foi interrompida antes de terminar.");
        summary.set(detail.get());
        showBanner.set(true);
        LOGGER.warning("Bootstrap automatico cancelado para o candidato " + activeCandidateId + ".");
    }

    private BootstrapUiState resolveState(List<PerguntasBootstrapService.BootstrapResult> results) {
        if (results == null || results.isEmpty()) {
            return BootstrapUiState.WARNING;
        }

        boolean hasProcessed = results.stream()
            .anyMatch(result -> result.status() == PerguntasBootstrapService.BootstrapStatus.PROCESSADO_AUTOMATICAMENTE);
        boolean hasErrors = results.stream()
            .anyMatch(result -> result.status() == PerguntasBootstrapService.BootstrapStatus.ERRO);
        boolean hasWarnings = results.stream()
            .anyMatch(result ->
                result.status() == PerguntasBootstrapService.BootstrapStatus.SEM_PDFS
                    || result.status() == PerguntasBootstrapService.BootstrapStatus.GEMINI_NAO_CONFIGURADO
                    || result.status() == PerguntasBootstrapService.BootstrapStatus.AGUARDANDO_ORIENTACAO
            );

        if (hasErrors) {
            return hasProcessed ? BootstrapUiState.WARNING : BootstrapUiState.ERROR;
        }
        if (hasWarnings) {
            return hasProcessed ? BootstrapUiState.SUCCESS : BootstrapUiState.WARNING;
        }
        return hasProcessed ? BootstrapUiState.SUCCESS : BootstrapUiState.WARNING;
    }

    private String resolveTitle(BootstrapUiState state) {
        return switch (state) {
            case SUCCESS -> "Base de estudo pronta";
            case WARNING -> "Base de estudo atualizada com pendencias";
            case ERROR -> "Base de estudo com falhas";
            case RUNNING -> "A processar os livros";
            case IDLE -> "Base de estudo";
        };
    }

    private String buildSummary(List<PerguntasBootstrapService.BootstrapResult> results) {
        if (results == null || results.isEmpty()) {
            return "Nao havia disciplinas para processar nesta sessao.";
        }

        long processadas = results.stream()
            .filter(result -> result.status() == PerguntasBootstrapService.BootstrapStatus.PROCESSADO_AUTOMATICAMENTE)
            .count();
        long aguardando = results.stream()
            .filter(result -> result.status() == PerguntasBootstrapService.BootstrapStatus.AGUARDANDO_ORIENTACAO)
            .count();
        long semPdfs = results.stream()
            .filter(result -> result.status() == PerguntasBootstrapService.BootstrapStatus.SEM_PDFS)
            .count();
        long erros = results.stream()
            .filter(result -> result.status() == PerguntasBootstrapService.BootstrapStatus.ERRO)
            .count();
        long semGemini = results.stream()
            .filter(result -> result.status() == PerguntasBootstrapService.BootstrapStatus.GEMINI_NAO_CONFIGURADO)
            .count();

        int totalPerguntas = results.stream()
            .filter(result ->
                result.status() == PerguntasBootstrapService.BootstrapStatus.PROCESSADO_AUTOMATICAMENTE
                    || result.status() == PerguntasBootstrapService.BootstrapStatus.JA_EXISTENTE
            )
            .mapToInt(PerguntasBootstrapService.BootstrapResult::totalPerguntas)
            .sum();

        StringBuilder resumo = new StringBuilder();
        resumo.append(processadas).append(" disciplinas processadas automaticamente");
        if (totalPerguntas > 0) {
            resumo.append(" e ").append(totalPerguntas).append(" perguntas reais disponiveis");
        }
        resumo.append(".");

        if (aguardando > 0) {
            resumo.append(" ").append(aguardando).append(" aguardam orientacao.");
        }
        if (semPdfs > 0) {
            resumo.append(" ").append(semPdfs).append(" ainda sem PDFs.");
        }
        if (semGemini > 0) {
            resumo.append(" ").append(semGemini).append(" precisam da chave do Gemini.");
        }
        if (erros > 0) {
            resumo.append(" ").append(erros).append(" ficaram com falha e podem ser tentadas novamente.");
        }

        return resumo.toString();
    }

    public enum BootstrapUiState {
        IDLE,
        RUNNING,
        SUCCESS,
        WARNING,
        ERROR
    }
}
