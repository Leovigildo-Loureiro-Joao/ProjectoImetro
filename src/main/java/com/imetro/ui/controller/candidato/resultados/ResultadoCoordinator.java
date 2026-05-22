package com.imetro.ui.controller.candidato.resultados;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public final class ResultadoCoordinator {

    public interface ResultadoHost {

    }

    private static final AtomicReference<ResultadoHost> HOST = new AtomicReference<>();

    private ResultadoCoordinator(){

    }

    public static void setHost(ResultadoHost host) {
        HOST.set(Objects.requireNonNull(host, "host"));
    }

    public static void clearHost(ResultadoHost host) {
        HOST.compareAndSet(host, null);
    }

}
