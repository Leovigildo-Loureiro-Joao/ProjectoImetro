package com.imetro.services;

import java.util.List;

import com.imetro.ui.model.Questao;
import com.imetro.util.Authentication;

public class TesteMatematicaService {

    private final DiagnosticoService diagnosticoService = new DiagnosticoService();

    public List<Questao> carregarQuestoes() {
        return diagnosticoService.carregarQuestoesReais(Authentication.getCurrentUserId());
    }
}
