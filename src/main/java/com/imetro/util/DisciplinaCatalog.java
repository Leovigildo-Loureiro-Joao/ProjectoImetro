package com.imetro.util;

import java.util.List;

public final class DisciplinaCatalog {

    private DisciplinaCatalog() {
    }

    public static List<String> defaultNomes() {
        return List.of(
                "Matemática",
                "Português",
                "Física",
                "Química",
                "Biologia",
                "História",
                "Geografia",
                "Inglês"
        );
    }
}

