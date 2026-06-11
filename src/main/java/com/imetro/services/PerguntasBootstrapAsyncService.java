package com.imetro.services;

import com.imetro.App;
import com.imetro.domain.dto.perguntas.BootstrapResult;
import com.imetro.domain.enums.BootstrapStatus;
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
    private volatile UUID autoStartSuppressedCandidateId;
    private volatile String autoStartSuppressedReason;
    private Task<List<BootstrapResult>> currentTask;

    private PerguntasBootstrapAsyncService() {
    }

    public static PerguntasBootstrapAsyncService getInstance() {
        return INSTANCE;
    }

    public synchronized boolean startIfNeeded(UUID candidatoId) {
        if (candidatoId == null) {
            return false;
        }

        if (isAutoStartSuppressed(candidatoId)) {
            LOGGER.info(
                "Auto bootstrap suspenso para o candidato "
                    + candidatoId
                    + ". Motivo: "
                    + autoStartSuppressedReason
                    + "."
            );
            return false;
        }

        if (currentTask != null && currentTask.isRunning()) {
            if (candidatoId.equals(activeCandidateId)) {
                showBanner.set(true);
            }
            return false;
        }

        if (!bootstrapService.hasDisciplinasPendentes(candidatoId)) {
            return false;
        }

        return start(candidatoId);
    }

    public synchronized boolean start(UUID candidatoId) {
        if (candidatoId == null) {
            LOGGER.warning("Tentativa de iniciar o bootstrap automatico sem candidatoId.");
            return false;
        }

        clearAutoStartSuppression(candidatoId);

        if (currentTask != null && currentTask.isRunning()) {
            if (candidatoId.equals(activeCandidateId)) {
                showBanner.set(true);
                LOGGER.info("Bootstrap automatico ja estava em curso para o candidato " + candidatoId + ".");
            }
            return false;
        }

        LOGGER.info("A iniciar o bootstrap automatico de perguntas para o candidato " + candidatoId + ".");
        activeCandidateId = candidatoId;
        title.set("A preparar a tua base de estudo");
        detail.set("Os livros vao ser lidos em segundo plano e as perguntas vao entrar na base aos poucos.");
        summary.set("");
        progress.set(0.0);
        state.set(BootstrapUiState.RUNNING);
        running.set(true);
        showBanner.set(true);

        Task<List<BootstrapResult>> task = new Task<>() {
            @Override
            protected List<BootstrapResult> call() {
                updateTitle("A preparar a tua base de estudo");
                updateMessage("Os livros vao ser lidos em segundo plano e as perguntas vao entrar na base aos poucos.");
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
        return true;
    }

    public synchronized boolean startDisciplina(
        UUID candidatoId,
        UUID disciplinaId,
        boolean sobrescreverTopicos,
        boolean ignorarPerguntasExistentes
    ) {
        if (candidatoId == null || disciplinaId == null) {
            LOGGER.warning("Tentativa de iniciar o processamento de livro sem candidatoId ou disciplinaId.");
            return false;
        }

        clearAutoStartSuppression(candidatoId);

        if (currentTask != null && currentTask.isRunning()) {
            if (candidatoId.equals(activeCandidateId)) {
                showBanner.set(true);
                LOGGER.info("Ja existe um processamento de livros em curso para o candidato " + candidatoId + ".");
            }
            return false;
        }

        LOGGER.info(
            "A iniciar o processamento do livro para a disciplina "
                + disciplinaId
                + " do candidato "
                + candidatoId
                + "."
        );
        activeCandidateId = candidatoId;
        title.set("A preparar o livro da disciplina");
        detail.set("O PDF sera lido em segundo plano e as perguntas vao ser inseridas na base assim que ficarem prontas.");
        summary.set("");
        progress.set(0.0);
        state.set(BootstrapUiState.RUNNING);
        running.set(true);
        showBanner.set(true);

        Task<List<BootstrapResult>> task = new Task<>() {
            @Override
            protected List<BootstrapResult> call() {
                updateTitle("A preparar o livro da disciplina");
                updateMessage("O PDF sera lido em segundo plano e as perguntas vao ser inseridas na base assim que ficarem prontas.");
                updateProgress(0, 1);

                BootstrapResult result = bootstrapService.processarDisciplinaDoCandidato(
                    candidatoId,
                    disciplinaId,
                    sobrescreverTopicos,
                    ignorarPerguntasExistentes,
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
                return result == null ? List.of() : List.of(result);
            }
        };

        bindToTask(task);
        currentTask = task;

        task.setOnSucceeded(event -> finishSuccessfully(task.getValue()));
        task.setOnFailed(event -> finishWithFailure(task.getException()));
        task.setOnCancelled(event -> finishCancelled());

        App.getExecutorService().execute(task);
        return true;
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

    private void finishSuccessfully(List<BootstrapResult> results) {
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
        atualizarSupensaoAutoarranque(results, null);
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
        atualizarSupensaoAutoarranque(null, error);
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

    private BootstrapUiState resolveState(List<BootstrapResult> results) {
        if (results == null || results.isEmpty()) {
            return BootstrapUiState.WARNING;
        }

        boolean hasProcessed = results.stream()
            .anyMatch(result -> result.status() == BootstrapStatus.PROCESSADO_AUTOMATICAMENTE);
        boolean hasExisting = results.stream()
            .anyMatch(result -> result.status() == BootstrapStatus.JA_EXISTENTE);
        boolean hasErrors = results.stream()
            .anyMatch(result -> result.status() == BootstrapStatus.ERRO);
        boolean hasWarnings = results.stream()
            .anyMatch(result ->
                result.status() == BootstrapStatus.SEM_PDFS
                    || result.status() == BootstrapStatus.GEMINI_NAO_CONFIGURADO
                    || result.status() == BootstrapStatus.GROQ_NAO_CONFIGURADO
            );

        if (hasErrors) {
            return hasProcessed ? BootstrapUiState.WARNING : BootstrapUiState.ERROR;
        }
        if (hasWarnings) {
            return hasProcessed ? BootstrapUiState.SUCCESS : BootstrapUiState.WARNING;
        }
        return hasProcessed || hasExisting ? BootstrapUiState.SUCCESS : BootstrapUiState.WARNING;
    }

    private void atualizarSupensaoAutoarranque(List<BootstrapResult> results, Throwable error) {
        UUID candidatoId = activeCandidateId;
        if (candidatoId == null) {
            return;
        }

        if (error != null) {
            suspenderAutoarranque(candidatoId, mensagemDeErro(error));
            return;
        }

        if (deveSuspenderAutoarranque(results)) {
            suspenderAutoarranque(candidatoId, construirMotivoSuspensao(results));
            return;
        }

        if (candidatoId.equals(autoStartSuppressedCandidateId)) {
            autoStartSuppressedCandidateId = null;
            autoStartSuppressedReason = null;
        }
    }

    private boolean deveSuspenderAutoarranque(List<BootstrapResult> results) {
        if (results == null || results.isEmpty()) {
            return false;
        }

        return results.stream().anyMatch(result ->
            result != null
                && (result.status() == BootstrapStatus.ERRO
                    || result.status() == BootstrapStatus.GEMINI_NAO_CONFIGURADO
                    || result.status() == BootstrapStatus.GROQ_NAO_CONFIGURADO)
        );
    }

    private void suspenderAutoarranque(UUID candidatoId, String motivo) {
        autoStartSuppressedCandidateId = candidatoId;
        autoStartSuppressedReason = motivo == null || motivo.isBlank()
            ? "A ultima tentativa falhou."
            : motivo.trim();
        LOGGER.warning(
            "Auto bootstrap suspenso para o candidato " + candidatoId + ". Motivo: " + autoStartSuppressedReason + "."
        );
    }

    private void clearAutoStartSuppression(UUID candidatoId) {
        if (candidatoId == null) {
            return;
        }

        if (candidatoId.equals(autoStartSuppressedCandidateId)) {
            autoStartSuppressedCandidateId = null;
            autoStartSuppressedReason = null;
        }
    }

    private boolean isAutoStartSuppressed(UUID candidatoId) {
        return candidatoId != null && candidatoId.equals(autoStartSuppressedCandidateId);
    }

    private String construirMotivoSuspensao(List<BootstrapResult> results) {
        if (results == null || results.isEmpty()) {
            return "Nenhum resultado valido foi devolvido.";
        }

        long erros = results.stream()
            .filter(result -> result != null && result.status() == BootstrapStatus.ERRO)
            .count();
        long semGemini = results.stream()
            .filter(result -> result != null && result.status() == BootstrapStatus.GEMINI_NAO_CONFIGURADO)
            .count();
        long semGroq = results.stream()
            .filter(result -> result != null && result.status() == BootstrapStatus.GROQ_NAO_CONFIGURADO)
            .count();

        if (erros > 0 && (semGemini > 0 || semGroq > 0)) {
            return "Houve " + erros + " erro(s), " + semGemini + " disciplina(s) sem Gemini configurado e "
                + semGroq + " disciplina(s) sem Groq configurado.";
        }
        if (erros > 0) {
            return "Houve " + erros + " erro(s) no processamento.";
        }
        if (semGemini > 0) {
            return "O Gemini nao esta configurado.";
        }
        if (semGroq > 0) {
            return "O Groq nao esta configurado.";
        }
        return "A ultima tentativa nao concluiu com sucesso.";
    }

    private String mensagemDeErro(Throwable error) {
        if (error == null || error.getMessage() == null || error.getMessage().isBlank()) {
            return "Falha inesperada no bootstrap automatico.";
        }

        return error.getMessage().trim();
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

    private String buildSummary(List<BootstrapResult> results) {
        if (results == null || results.isEmpty()) {
            return "Nao havia disciplinas para processar nesta sessao.";
        }

        long processadas = results.stream()
            .filter(result -> result.status() == BootstrapStatus.PROCESSADO_AUTOMATICAMENTE)
            .count();
        long semPdfs = results.stream()
            .filter(result -> result.status() == BootstrapStatus.SEM_PDFS)
            .count();
        long erros = results.stream()
            .filter(result -> result.status() == BootstrapStatus.ERRO)
            .count();
        long semGemini = results.stream()
            .filter(result -> result.status() == BootstrapStatus.GEMINI_NAO_CONFIGURADO)
            .count();
        long semGroq = results.stream()
            .filter(result -> result.status() == BootstrapStatus.GROQ_NAO_CONFIGURADO)
            .count();
        long jaExistentes = results.stream()
            .filter(result -> result.status() == BootstrapStatus.JA_EXISTENTE)
            .count();

        int totalPerguntas = results.stream()
            .filter(result ->
                result.status() == BootstrapStatus.PROCESSADO_AUTOMATICAMENTE
                    || result.status() == BootstrapStatus.JA_EXISTENTE
            )
            .mapToInt(BootstrapResult::totalPerguntas)
            .sum();

        StringBuilder resumo = new StringBuilder();
        resumo.append(processadas).append(' ')
            .append(processadas == 1 ? "disciplina processada automaticamente" : "disciplinas processadas automaticamente");
        if (totalPerguntas > 0) {
            resumo.append(" e ").append(totalPerguntas).append(" perguntas reais disponiveis");
        }
        resumo.append(".");

        if (jaExistentes > 0) {
            resumo.append(" ").append(jaExistentes).append(' ')
                .append(jaExistentes == 1 ? "ja tinha base pronta." : "ja tinham base pronta.");
        }

        if (semPdfs > 0) {
            resumo.append(" ").append(semPdfs).append(" ainda sem PDFs.");
        }
        if (semGemini > 0) {
            resumo.append(" ").append(semGemini).append(' ')
                .append(semGemini == 1 ? "precisa da chave do Gemini." : "precisam da chave do Gemini.");
        }
        if (semGroq > 0) {
            resumo.append(" ").append(semGroq).append(' ')
                .append(semGroq == 1 ? "precisa da chave do Groq." : "precisam da chave do Groq.");
        }
        if (erros > 0) {
            resumo.append(" ").append(erros).append(' ')
                .append(erros == 1
                    ? "ficou com falha e pode ser tentada novamente."
                    : "ficaram com falha e podem ser tentadas novamente.");
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
