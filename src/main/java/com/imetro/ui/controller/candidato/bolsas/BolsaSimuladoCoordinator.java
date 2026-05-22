package com.imetro.ui.controller.candidato.bolsas;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import com.imetro.domain.dto.bolsa.BolsaDto;

public final class BolsaSimuladoCoordinator {

    public record BolsaSelection(
        BolsaDto bolsa,
        int prontidaoAtual,
        boolean elegivel,
        String criterioResumo
    ) {
    }

    private static final AtomicReference<BolsaSelection> SELECTION = new AtomicReference<>();

    private BolsaSimuladoCoordinator() {
    }

    public static void definir(BolsaSelection selection) {
        SELECTION.set(Objects.requireNonNull(selection, "selection"));
    }

    public static BolsaSelection getAtual() {
        return SELECTION.get();
    }

    public static void limpar() {
        SELECTION.set(null);
    }
}
