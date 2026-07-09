package com.imetro.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.imetro.persistence.repository.MiniTesteRepository;
import com.imetro.persistence.repository.PerguntasRepository;
import com.imetro.ui.model.Questao;
import com.imetro.util.QuestaoUtil;

public class MiniTesteService {

    private final PerguntasRepository perguntasRepository;
    private final MiniTesteRepository miniTesteRepository;
    private final DiagnosticoService diagnosticoService;

    public MiniTesteService() {
        this.perguntasRepository = new PerguntasRepository();
        this.miniTesteRepository = new MiniTesteRepository();
        this.diagnosticoService = new DiagnosticoService();
    }

    public List<Questao> carregarMiniTeste(UUID livroId, String tituloLivro, int paginaInicio, int paginaFim, int limite) {
        List<Questao> todas = diagnosticoService.carregarQuestoesReais();
        String livroLower = tituloLivro != null ? tituloLivro.toLowerCase().trim() : "";

        return todas.stream()
            .filter(q -> {
                String ref = q.getReferenciaLivro();
                if (ref == null || ref.isBlank()) return false;
                String refLower = ref.toLowerCase().trim();
                boolean livroMatch = livroLower.isEmpty() || refLower.contains(livroLower) || livroLower.contains(refLower);
                boolean paginaMatch = paginaEmIntervalo(q.getPaginaInicio(), q.getPaginaFim(), paginaInicio, paginaFim);
                return livroMatch && paginaMatch;
            })
            .limit(limite > 0 ? limite : 5)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<Questao> carregarMiniTeste(UUID livroId, String tituloLivro, int paginaInicio, int paginaFim) {
        return carregarMiniTeste(livroId, tituloLivro, paginaInicio, paginaFim, 5);
    }

    private boolean paginaEmIntervalo(Integer qInicio, Integer qFim, int refInicio, int refFim) {
        if (qInicio == null || qFim == null) return false;
        if (refInicio <= 0 && refFim <= 0) return true;
        return qInicio >= refInicio && qFim <= refFim;
    }

    public ResultadoMiniTeste avaliarMiniTeste(List<Questao> questoes, List<Character> respostas) {
        int acertos = 0;
        int total = Math.min(questoes.size(), respostas.size());
        List<Boolean> resultados = new ArrayList<>();

        for (int i = 0; i < total; i++) {
            boolean correto = QuestaoUtil.respostaEstaCorreta(questoes.get(i), respostas.get(i));
            resultados.add(correto);
            if (correto) acertos++;
        }

        double percentual = total > 0 ? (acertos * 100.0 / total) : 0;
        return new ResultadoMiniTeste(acertos, total - acertos, total, percentual, resultados);
    }

    public record ResultadoMiniTeste(int acertos, int erros, int total, double percentual, List<Boolean> resultados) {
        public boolean aprovado() {
            return percentual >= 70.0;
        }
    }
}
