package com.imetro.services;

import com.imetro.domain.dto.biblioteca.BibliotecaLivroDto;
import com.imetro.domain.dto.biblioteca.LivroMapaTopicos;
import com.imetro.domain.dto.leitura.TrilhoLeitura;
import com.imetro.domain.dto.planejamento.LeituraRecomendada;
import com.imetro.persistence.repository.BibliotecaLivroRepository;
import com.imetro.persistence.repository.LivroMapaTopicosRepository;
import com.imetro.persistence.repository.TrilhoLeituraRepository;
import com.imetro.util.AppLogger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class TrilhoLeituraService {

    private static final Logger LOGGER = AppLogger.getLogger(TrilhoLeituraService.class);

    private final LivroMapaTopicosRepository livroMapaTopicosRepository;
    private final BibliotecaLivroRepository bibliotecaLivroRepository;
    private final TrilhoLeituraRepository trilhoLeituraRepository;

    public TrilhoLeituraService() {
        this.livroMapaTopicosRepository = new LivroMapaTopicosRepository();
        this.bibliotecaLivroRepository = new BibliotecaLivroRepository();
        this.trilhoLeituraRepository = new TrilhoLeituraRepository();
    }

    public List<LeituraRecomendada> recomendarLeiturasParaSubtopicos(
        UUID disciplinaId,
        String disciplinaNome,
        List<String> topicos,
        List<String> subtopicos
    ) {
        LinkedHashSet<String> chavesVistas = new LinkedHashSet<>();
        ArrayList<LeituraRecomendada> leituras = new ArrayList<>();

        for (int i = 0; i < Math.max(topicos.size(), subtopicos.size()); i++) {
            String topico = i < topicos.size() ? topicos.get(i) : null;
            String subtopico = i < subtopicos.size() ? subtopicos.get(i) : null;

            List<LivroMapaTopicos> resultados = new ArrayList<>();

            if (subtopico != null && !subtopico.isBlank()) {
                resultados = livroMapaTopicosRepository.findSubTopicos(subtopico);
            }
            if (resultados.isEmpty() && topico != null && !topico.isBlank()) {
                resultados = livroMapaTopicosRepository.findTopicos(topico);
            }

            for (LivroMapaTopicos item : resultados) {
                String chave = item.livroId() + "::" + item.topico() + "::" + item.subtopico();
                if (!chavesVistas.add(chave)) continue;

                String tituloLivro = buscarTituloLivro(item.livroId());
                leituras.add(new LeituraRecomendada(
                    item.livroId(),
                    tituloLivro,
                    disciplinaNome,
                    item.topico(),
                    item.subtopico(),
                    item.paginaInicio(),
                    item.paginaFim(),
                    calcularTotalPaginas(item.paginaInicio(), item.paginaFim()),
                    0.0
                ));
            }
        }

        return List.copyOf(leituras);
    }

    public List<LeituraRecomendada> recomendarLeiturasParaDisciplina(
        UUID disciplinaId,
        String disciplinaNome
    ) {
        ArrayList<LeituraRecomendada> leituras = new ArrayList<>();
        LinkedHashSet<String> chavesVistas = new LinkedHashSet<>();

        try {
            List<BibliotecaLivroDto> livros = bibliotecaLivroRepository.listarPorDisciplina(disciplinaId);
            for (BibliotecaLivroDto livro : livros) {
                List<LivroMapaTopicos> topicosDoLivro = livroMapaTopicosRepository.findTopicos("");
                if (!topicosDoLivro.isEmpty()) {
                    for (LivroMapaTopicos item : topicosDoLivro) {
                        String chave = item.livroId() + "::" + item.topico() + "::" + item.subtopico();
                        if (!chavesVistas.add(chave)) continue;
                        leituras.add(new LeituraRecomendada(
                            item.livroId(),
                            livro.titulo(),
                            disciplinaNome,
                            item.topico(),
                            item.subtopico(),
                            item.paginaInicio(),
                            item.paginaFim(),
                            calcularTotalPaginas(item.paginaInicio(), item.paginaFim()),
                            0.0
                        ));
                    }
                } else {
                    leituras.add(new LeituraRecomendada(
                        livro.id(),
                        livro.titulo(),
                        disciplinaNome,
                        "Geral",
                        "Geral",
                        1,
                        Math.max(1, livro.totalPaginas()),
                        livro.totalPaginas(),
                        0.0
                    ));
                }
            }
        } catch (Exception e) {
            LOGGER.warning("Falha ao recomendar leituras para disciplina " + disciplinaNome + ": " + e.getMessage());
        }

        return List.copyOf(leituras);
    }

    public void gerarTrilhoLeitura(UUID alunoId, UUID disciplinaId, List<LeituraRecomendada> leituras) {
        int ordem = 0;
        for (LeituraRecomendada leitura : leituras) {
            ordem++;
            try {
                TrilhoLeitura trilho = new TrilhoLeitura(
                    null,
                    alunoId,
                    disciplinaId,
                    leitura.livroId(),
                    ordem,
                    leitura.paginaInicio(),
                    leitura.paginaFim(),
                    leitura.topico(),
                    leitura.subtopico(),
                    "PENDENTE",
                    null,
                    null,
                    null
                );
                trilhoLeituraRepository.insertDto(trilho);
            } catch (SQLException e) {
                LOGGER.warning("Falha ao inserir passo do trilho de leitura: " + e.getMessage());
            }
        }
    }

    private String buscarTituloLivro(UUID livroId) {
        try {
            return bibliotecaLivroRepository.findById(livroId)
                .map(BibliotecaLivroDto::titulo)
                .orElse("Livro");
        } catch (Exception e) {
            return "Livro";
        }
    }

    private int calcularTotalPaginas(int inicio, int fim) {
        return Math.max(1, fim - inicio + 1);
    }
}
