package com.imetro.ui.controller.diagnosticos;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;

/**
 * Canal simples de comunicação entre os componentes do Diagnóstico (views/components)
 * e a página principal (views/pages/candidato/diagnostico.fxml).
 */
public final class DiagnosticoCoordinator {




    public interface DiagnosticoHost {
        void startDiagnostico();
        void ModalOpen();
        void StartInteligente();

    }

    private static final AtomicReference<DiagnosticoHost> HOST = new AtomicReference<>();

    private DiagnosticoCoordinator() {
    }

    public static void setHost(DiagnosticoHost host) {
        HOST.set(Objects.requireNonNull(host, "host"));
    }

    public static void clearHost(DiagnosticoHost host) {
        HOST.compareAndSet(host, null);
    }

    public static void requestStart() {
        DiagnosticoHost host = HOST.get();
        if (host != null) {
            host.ModalOpen();
        }
       
    }

    public static void requestStartInteligente() {
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
   
}

