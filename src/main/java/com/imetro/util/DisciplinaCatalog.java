package com.imetro.util;

import java.util.List;

public final class DisciplinaCatalog {

    private DisciplinaCatalog() {
    }

    public record DisciplinaSeed(String nome, double peso, String nivel) {
    }

    public static List<DisciplinaSeed> defaultSeeds() {
        return List.of(
                new DisciplinaSeed("Matemática", 1.0, "BASICO"),
                new DisciplinaSeed("Português", 1.0, "BASICO"),
                new DisciplinaSeed("Física", 1.0, "BASICO"),
                new DisciplinaSeed("Química", 1.0, "BASICO"),
                new DisciplinaSeed("Biologia", 1.0, "BASICO"),
                new DisciplinaSeed("História", 1.0, "BASICO"),
                new DisciplinaSeed("Geografia", 1.0, "BASICO"),
                new DisciplinaSeed("Inglês", 1.0, "BASICO")
        );
    }

    public static List<String> defaultNomes() {
        return defaultSeeds().stream().map(DisciplinaSeed::nome).toList();
    }
}
