package com.imetro.ui.modals;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public final class PauseSessionContext {

    public record PauseRequest(
        String titulo,
        String mensagem,
        String continuarTexto,
        String desistirTexto,
        String recomecarTexto,
        Runnable onContinuar,
        Runnable onDesistir,
        Runnable onRecomecar
    ) {
    }

    private static final AtomicReference<PauseRequest> REQUEST = new AtomicReference<>();

    private PauseSessionContext() {
    }

    public static void setRequest(PauseRequest request) {
        REQUEST.set(request);
    }

    public static PauseRequest getRequest() {
        return REQUEST.get();
    }

    public static void clear() {
        REQUEST.set(null);
    }

    public static void continuar() {
        executarELimpar(PauseRequest::onContinuar);
    }

    public static void desistir() {
        executarELimpar(PauseRequest::onDesistir);
    }

    public static void recomecar() {
        executarELimpar(PauseRequest::onRecomecar);
    }

    private static void executarELimpar(Function<PauseRequest, Runnable> extractor) {
        PauseRequest request = REQUEST.getAndSet(null);
        if (request == null) {
            return;
        }

        Runnable action = extractor.apply(request);
        if (action != null) {
            action.run();
        }
    }
}
