package com.imetro.services;

import java.util.List;

import com.imetro.ui.model.Questao;

public class TesteMatematicaService {

    private final CatalogoQuestoesService catalogoQuestoesService = new CatalogoQuestoesService();

    public List<Questao> carregarQuestoes() {
        return catalogoQuestoesService.carregarQuestoes();
    }
}
