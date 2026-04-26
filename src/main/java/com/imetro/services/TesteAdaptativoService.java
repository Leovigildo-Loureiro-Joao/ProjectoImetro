package com.imetro.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.imetro.domain.dto.Topico;
import com.imetro.ui.model.Questao;

public class TesteAdaptativoService {

    private final CatalogoQuestoesService catalogoQuestoesService;

    public TesteAdaptativoService() {
        this.catalogoQuestoesService = new CatalogoQuestoesService();
    }

    public List<String> carregarDisciplinasDisponiveis() {
        return catalogoQuestoesService.carregarDisciplinasDisponiveis();
    }

    public List<Topico> carregarTopicosPorDisciplina(String disciplina) {
        return catalogoQuestoesService.carregarTopicosPorDisciplina(disciplina);
    }

    public List<Questao> carregarQuestoesDisponiveis(
        String disciplina,
        Collection<String> topicos,
        Collection<String> subtopicos
    ) {
        return catalogoQuestoesService.filtrarQuestoes(disciplina, topicos, subtopicos, null);
    }

    public List<Questao> carregarQuestoesAdaptativas(
        String disciplina,
        int nivel,
        Collection<String> topicos,
        Collection<String> subtopicos
    ) {
        List<Questao> questoes = catalogoQuestoesService.filtrarQuestoes(disciplina, topicos, subtopicos, nivel);
        if (questoes.isEmpty()) {
            questoes = catalogoQuestoesService.filtrarQuestoes(disciplina, topicos, subtopicos, null);
        }
        return new ArrayList<>(questoes);
    }

    public Questao getProximaQuestaoAdaptativa(
        String disciplina,
        int nivel,
        Collection<String> topicos,
        Collection<String> subtopicos,
        Collection<String> idsIgnorados
    ) {
        Questao questao = catalogoQuestoesService.proximaQuestao(disciplina, topicos, subtopicos, nivel, idsIgnorados);
        if (questao == null) {
            questao = catalogoQuestoesService.proximaQuestao(disciplina, topicos, subtopicos, null, idsIgnorados);
        }
        return questao;
    }
}
