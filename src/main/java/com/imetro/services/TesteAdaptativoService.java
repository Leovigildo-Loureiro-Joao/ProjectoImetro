package com.imetro.services;

import com.imetro.persistence.repository.JdbcBasicSqlRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.imetro.domain.dto.Topico;
import com.imetro.domain.enums.NivelDificuldadeAdaptativa;
import com.imetro.ui.model.Questao;
import com.imetro.util.Authentication;
import com.imetro.util.TextoUtil;

public class TesteAdaptativoService {

    private final DiagnosticoService diagnosticoService;

    public TesteAdaptativoService() {
        this.diagnosticoService = new DiagnosticoService();
    }

    public List<String> carregarDisciplinasDisponiveis() {
        return diagnosticoService.agendarSincronizacaoSeNecessario(Authentication.getCurrentUserId()).stream()
            .map(Questao::getDisciplina)
            .filter(disciplina -> disciplina != null && !disciplina.isBlank())
            .distinct()
            .sorted(String.CASE_INSENSITIVE_ORDER)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<Topico> carregarTopicosPorDisciplina(String disciplina) {
        if (!PerguntasBootstrapAsyncService.getInstance().isRunningFor(Authentication.getCurrentUserId())) {
            diagnosticoService.sincronizarDisciplinasAutomaticas(Authentication.getCurrentUserId());
        }
        return diagnosticoService.carregarTopicosPorDisciplina(disciplina);
    }

    public List<Questao> carregarQuestoesDisponiveis(
        String disciplina,
        Collection<String> topicos,
        Collection<String> subtopicos
    ) {
        return carregarQuestoesDisponiveis(disciplina, topicos, subtopicos, null);
    }

    public List<Questao> carregarQuestoesDisponiveis(
        String disciplina,
        Collection<String> topicos,
        Collection<String> subtopicos,
        Integer niveldifs
    ) {
        return ordenarPorRigorAdaptativo(
            disciplina,
            filtrarQuestoesReais(disciplina, topicos, subtopicos, niveldifs),
            niveldifs
        );
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

    public NivelDificuldadeAdaptativa resolverNivelAtual(
        String disciplina,
        Collection<String> topicos,
        Collection<String> subtopicos,
        String nivelConfigurado
    ) {
        List<Questao> questoes = filtrarQuestoesReais(disciplina, topicos, subtopicos, null);
        if (questoes.isEmpty()) {
            return nivelConfigurado == null || nivelConfigurado.isBlank()
                ? NivelDificuldadeAdaptativa.padrao()
                : NivelDificuldadeAdaptativa.fromTexto(nivelConfigurado);
        }

        double rigorInferido = resolverRigorMedioAtual(questoes, disciplina);
        if (nivelConfigurado != null && !nivelConfigurado.isBlank()) {
            double rigorConfigurado = NivelDificuldadeAdaptativa.fromTexto(nivelConfigurado).rigorBase();
            rigorInferido = ((rigorInferido * 0.75d) + (rigorConfigurado * 0.25d));
        }
        return NivelDificuldadeAdaptativa.resolverNivelPorRigor(rigorInferido);
    }

    public Map<String, NivelDificuldadeAdaptativa> carregarNivelAtualPorSubtopico(
        String disciplina,
        Collection<String> topicos,
        Collection<String> subtopicos
    ) {
        List<Questao> questoes = filtrarQuestoesReais(disciplina, topicos, subtopicos, null);
        Map<String, Double> rigores = carregarRigorAtualPorSubtopico(disciplina, questoes, null);
        if (rigores.isEmpty()) {
            return Map.of();
        }

        LinkedHashMap<String, NivelDificuldadeAdaptativa> niveis = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : rigores.entrySet()) {
            niveis.put(entry.getKey(), NivelDificuldadeAdaptativa.resolverNivelPorRigor(entry.getValue()));
        }
        return Map.copyOf(niveis);
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
        double rigorBase = resolverRigorBase(nivelDificuldade);
        Map<String, Double> rigorAtualPorSubtopico = carregarRigorAtualPorSubtopico(disciplina, questoes, rigorBase);

        return questoes.stream()
            .sorted(Comparator
                .comparingDouble((Questao questao) -> distanciaDeRigor(questao, rigorAtualPorSubtopico, rigorBase))
                .thenComparingDouble(Questao::getRigor)
                .thenComparing(Questao::getTopico, String.CASE_INSENSITIVE_ORDER))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    private Map<String, Double> carregarRigorAtualPorSubtopico(
        String disciplina,
        List<Questao> questoes,
        Double rigorBaseFallback
    ) {
        UUID candidatoId = Authentication.getCurrentUserId();
        if (candidatoId == null || disciplina == null || disciplina.isBlank()) {
            return inferirRigorBasePorSubtopico(questoes, rigorBaseFallback);
        }

        String sql = """
            select pr.subtopico, pr.rigor_atual
            from progressao_rigor pr
            join disciplinas d on d.id = pr.disciplina_id
            where pr.aluno_id = ?
              and lower(coalesce(d.nome, '')) = lower(?)
            """;

        LinkedHashMap<String, Double> rigoresPersistidos = new LinkedHashMap<>();
        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, candidatoId);
            stmt.setString(2, disciplina);
            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String subtopico = normalizarChaveSubtopico(rs.getString("subtopico"), null, null);
                    double rigorAtual = rs.getObject("rigor_atual") instanceof Number number
                        ? Math.max(0d, Math.min(1d, number.doubleValue()))
                        : 0.12d; // TODO CONFIG_ADAPTATIVA: fallback de rigor atual ainda fixo quando a BD nao devolve valor.
                    if (!subtopico.isBlank()) {
                        rigoresPersistidos.put(subtopico, rigorAtual);
                    }
                }
            }
        } catch (Exception ignored) {
        }

        LinkedHashMap<String, Double> rigoresFinais = new LinkedHashMap<>(
            inferirRigorBasePorSubtopico(questoes, rigorBaseFallback)
        );
        rigoresFinais.putAll(rigoresPersistidos);
        return Map.copyOf(rigoresFinais);
    }

    private double distanciaDeRigor(Questao questao, Map<String, Double> rigorAtualPorSubtopico, double rigorBase) {
        String chaveSubtopico = normalizarChaveSubtopico(
            questao.getSubtopico(),
            questao.getTopicoPrincipal(),
            questao.getTopico()
        );
        double alvo = rigorAtualPorSubtopico.getOrDefault(chaveSubtopico, rigorBase);
        return Math.abs(questao.getRigor() - alvo);
    }

    private double resolverRigorBase(Integer nivelDificuldade) {
        return NivelDificuldadeAdaptativa.fromNivel(nivelDificuldade).rigorBase();
    }

    private List<Questao> filtrarQuestoesReais(
        String disciplina,
        Collection<String> topicos,
        Collection<String> subtopicos,
        Integer nivelDificuldade
    ) {
        String disciplinaNormalizada = TextoUtil.normalizarMaiusculo(disciplina);
        Set<String> topicosNormalizados = normalizarColecao(topicos);
        Set<String> subtopicosNormalizados = normalizarColecao(subtopicos);

        return diagnosticoService.carregarQuestoesReais().stream()
            .filter(questao -> disciplinaNormalizada.isBlank()
                || disciplinaNormalizada.equals(TextoUtil.normalizarMaiusculo(questao.getDisciplina())))
            .filter(questao -> topicosNormalizados.isEmpty()
                || topicosNormalizados.contains(TextoUtil.normalizarMaiusculo(questao.getTopico())))
            .filter(questao -> subtopicosNormalizados.isEmpty()
                || subtopicosNormalizados.contains(TextoUtil.normalizarMaiusculo(questao.getSubtopico())))
            .filter(questao -> nivelDificuldade == null || questao.getNivelDificuldade() == nivelDificuldade)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    private Set<String> normalizarColecao(Collection<String> valores) {
        if (valores == null) {
            return Set.of();
        }

        return valores.stream()
            .filter(valor -> valor != null && !valor.isBlank())
            .map(TextoUtil::normalizarMaiusculo)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Map<String, Double> inferirRigorBasePorSubtopico(List<Questao> questoes, Double rigorBaseFallback) {
        if (questoes == null || questoes.isEmpty()) {
            return Map.of();
        }

        double fallback = rigorBaseFallback == null
            ? NivelDificuldadeAdaptativa.padrao().rigorBase()
            : rigorBaseFallback;

        LinkedHashMap<String, List<Questao>> questoesPorSubtopico = questoes.stream()
            .collect(Collectors.groupingBy(
                questao -> normalizarChaveSubtopico(
                    questao.getSubtopico(),
                    questao.getTopicoPrincipal(),
                    questao.getTopico()
                ),
                LinkedHashMap::new,
                Collectors.toCollection(ArrayList::new)
            ));

        LinkedHashMap<String, Double> rigores = new LinkedHashMap<>();
        for (Map.Entry<String, List<Questao>> entry : questoesPorSubtopico.entrySet()) {
            double rigorMedio = entry.getValue().stream()
                .mapToDouble(Questao::getRigor)
                .average()
                .orElse(fallback);
            rigores.put(entry.getKey(), Math.max(0d, Math.min(1d, rigorMedio)));
        }
        return Map.copyOf(rigores);
    }

    private double resolverRigorMedioAtual(List<Questao> questoes, String disciplina) {
        Map<String, Double> rigoresPorSubtopico = carregarRigorAtualPorSubtopico(disciplina, questoes, null);
        if (!rigoresPorSubtopico.isEmpty()) {
            return rigoresPorSubtopico.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(NivelDificuldadeAdaptativa.padrao().rigorBase());
        }

        return questoes.stream()
            .mapToDouble(Questao::getRigor)
            .average()
            .orElse(NivelDificuldadeAdaptativa.padrao().rigorBase());
    }

    private String normalizarChaveSubtopico(String subtopico, String topicoPrincipal, String topico) {
        String valor = subtopico;
        if (valor == null || valor.isBlank()) {
            valor = topicoPrincipal;
        }
        if (valor == null || valor.isBlank()) {
            valor = topico;
        }
        return TextoUtil.normalizarMaiusculo(valor == null || valor.isBlank() ? "GERAL" : valor);
    }
}
