package com.imetro.ui.modals;

import java.util.concurrent.atomic.AtomicReference;

import com.imetro.util.ResultadoCelebracaoSupport.CelebrationSummary;

public final class ResultadoCelebracaoContext {

    public record CelebrationRequest(CelebrationSummary summary, Runnable onContinue) {
    }

    private static final AtomicReference<CelebrationRequest> REQUEST_ATUAL = new AtomicReference<>();

    private ResultadoCelebracaoContext() {
    }

    public static void definir(CelebrationRequest request) {
        REQUEST_ATUAL.set(request);
    }

    public static CelebrationRequest obterAtual() {
        return REQUEST_ATUAL.get();
    }

    public static CelebrationRequest consumirAtual() {
        return REQUEST_ATUAL.getAndSet(null);
    }

    public static void limpar() {
        REQUEST_ATUAL.set(null);
    }
}
