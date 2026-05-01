package com.imetro.services;

import com.imetro.persistence.repository.JdbcBasicSqlRepository;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.imetro.domain.dto.Topico;
import com.imetro.ui.model.Questao;
import com.imetro.util.Authentication;

public class TesteAdaptativoService {

    private final DiagnosticoService diagnosticoService;

    public TesteAdaptativoService() {
        this.diagnosticoService = new DiagnosticoService();
    }

    public List<String> carregarDisciplinasDisponiveis() {
        return diagnosticoService.carregarQuestoesReais(Authentication.getCurrentUserId()).stream()
            .map(Questao::getDisciplina)
            .filter(disciplina -> disciplina != null && !disciplina.isBlank())
            .distinct()
            .sorted(String.CASE_INSENSITIVE_ORDER)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<Topico> carregarTopicosPorDisciplina(String disciplina) {
        diagnosticoService.sincronizarDisciplinasAutomaticas(Authentication.getCurrentUserId());
        return diagnosticoService.carregarTopicosPorDisciplina(disciplina);
    }

    public List<Questao> carregarQuestoesDisponiveis(
        String disciplina,
        Collection<String> topicos,
        Collection<String> subtopicos
    ) {
        return ordenarPorRigorAdaptativo(disciplina, filtrarQuestoesReais(disciplina, topicos, subtopicos, null), null);
    }

    public List<Questao> carregarQuestoesAdaptativas(
        String disciplina,
        int nivel,
        Collection<String> topicos,
        Collection<String> subtopicos
    ) {
        List<Questao> questoes = filtrarQuestoesReais(disciplina, topicos, subtopicos, nivel);
        if (questoes.isEmpty()) {
            questoes = filtrarQuestoesReais(disciplina, topicos, subtopicos, null);
        }
        return ordenarPorRigorAdaptativo(disciplina, new ArrayList<>(questoes), nivel);
    }

    public Questao getProximaQuestaoAdaptativa(
        String disciplina,
        int nivel,
        Collection<String> topicos,
        Collection<String> subtopicos,
        Collection<String> idsIgnorados
    ) {
        Set<String> ignorados = idsIgnorados == null ? Set.of() : new LinkedHashSet<>(idsIgnorados);
        Questao questao = ordenarPorRigorAdaptativo(
                disciplina,
                filtrarQuestoesReais(disciplina, topicos, subtopicos, nivel),
                nivel
            ).stream()
            .filter(item -> !ignorados.contains(item.getId()))
            .findFirst()
            .orElse(null);

        if (questao != null) {
            return questao;
        }

        return ordenarPorRigorAdaptativo(
                disciplina,
                filtrarQuestoesReais(disciplina, topicos, subtopicos, null),
                null
            ).stream()
            .filter(item -> !ignorados.contains(item.getId()))
            .findFirst()
            .orElse(null);
    }

    private List<Questao> ordenarPorRigorAdaptativo(String disciplina, List<Questao> questoes, Integer nivelDificuldade) {
        UUID candidatoId = Authentication.getCurrentUserId();
        Map<String, Double> rigorAtualPorTopico = carregarRigorAtualPorTopico(candidatoId, disciplina);
        double rigorBase = resolverRigorBase(nivelDificuldade);

        return questoes.stream()
            .sorted(Comparator
                .comparingDouble((Questao questao) -> distanciaDeRigor(questao, rigorAtualPorTopico, rigorBase))
                .thenComparingDouble(Questao::getRigor)
                .thenComparing(Questao::getTopico, String.CASE_INSENSITIVE_ORDER))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    private Map<String, Double> carregarRigorAtualPorTopico(UUID candidatoId, String disciplina) {
        if (candidatoId == null || disciplina == null || disciplina.isBlank()) {
            return Map.of();
        }

        String sql = """
            select pr.topico, pr.rigor_atual
            from progressao_rigor pr
            join disciplinas d on d.id = pr.disciplina_id
            where pr.aluno_id = ?
              and lower(coalesce(d.nome, '')) = lower(?)
            """;

        LinkedHashMap<String, Double> rigores = new LinkedHashMap<>();
        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, candidatoId);
            stmt.setString(2, disciplina);
            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String topico = normalizar(rs.getString("topico"));
                    double rigorAtual = rs.getObject("rigor_atual") instanceof Number number
                        ? Math.max(0d, Math.min(1d, number.doubleValue()))
                        : 0.12d;
                    if (!topico.isBlank()) {
                        rigores.put(topico, rigorAtual);
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return Map.copyOf(rigores);
    }

    private double distanciaDeRigor(Questao questao, Map<String, Double> rigorAtualPorTopico, double rigorBase) {
        String chaveTopico = normalizar(
            questao.getTopicoPrincipal() == null || questao.getTopicoPrincipal().isBlank()
                ? questao.getTopico()
                : questao.getTopicoPrincipal()
        );
        double alvo = rigorAtualPorTopico.getOrDefault(chaveTopico, rigorBase);
        return Math.abs(questao.getRigor() - alvo);
    }

    private double resolverRigorBase(Integer nivelDificuldade) {
        if (nivelDificuldade == null) {
            return 0.35d;
        }
        return switch (nivelDificuldade) {
            case 1 -> 0.18d;
            case 2 -> 0.35d;
            case 3 -> 0.58d;
            case 4 -> 0.78d;
            default -> 0.35d;
        };
    }

    private List<Questao> filtrarQuestoesReais(
        String disciplina,
        Collection<String> topicos,
        Collection<String> subtopicos,
        Integer nivelDificuldade
    ) {
        String disciplinaNormalizada = normalizar(disciplina);
        Set<String> topicosNormalizados = normalizarColecao(topicos);
        Set<String> subtopicosNormalizados = normalizarColecao(subtopicos);

        return diagnosticoService.carregarQuestoesReais(Authentication.getCurrentUserId()).stream()
            .filter(questao -> disciplinaNormalizada.isBlank()
                || disciplinaNormalizada.equals(normalizar(questao.getDisciplina())))
            .filter(questao -> topicosNormalizados.isEmpty()
                || topicosNormalizados.contains(normalizar(questao.getTopico())))
            .filter(questao -> subtopicosNormalizados.isEmpty()
                || subtopicosNormalizados.contains(normalizar(questao.getSubtopico())))
            .filter(questao -> nivelDificuldade == null || questao.getNivelDificuldade() == nivelDificuldade)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    private Set<String> normalizarColecao(Collection<String> valores) {
        if (valores == null) {
            return Set.of();
        }

        return valores.stream()
            .filter(valor -> valor != null && !valor.isBlank())
            .map(this::normalizar)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private String normalizar(String valor) {
        if (valor == null) {
            return "";
        }

        String semAcento = Normalizer.normalize(valor, Normalizer.Form.NFD)
            .replaceAll("\\p{M}+", "");
        return semAcento.trim().toUpperCase(Locale.ROOT);
    }
}
