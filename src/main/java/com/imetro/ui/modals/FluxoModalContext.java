package com.imetro.ui.modals;

import java.util.concurrent.atomic.AtomicReference;

public final class FluxoModalContext {

    public enum Origem {
        DIAGNOSTICO,
        TESTE_ADAPTATIVO
    }

    private static final AtomicReference<Origem> ORIGEM_ATUAL =
        new AtomicReference<>(Origem.DIAGNOSTICO);

    private FluxoModalContext() {
    }

    public static void setOrigem(Origem origem) {
        if (origem != null) {
            ORIGEM_ATUAL.set(origem);
        }
    }

    public static Origem getOrigem() {
        return ORIGEM_ATUAL.get();
    }

    public static boolean isTesteAdaptativo() {
        return ORIGEM_ATUAL.get() == Origem.TESTE_ADAPTATIVO;
    }
}
