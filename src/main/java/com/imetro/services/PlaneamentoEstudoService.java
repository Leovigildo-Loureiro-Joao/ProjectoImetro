package com.imetro.services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.imetro.config.RuntimeConfig;
import com.imetro.domain.CacheService;
import com.imetro.domain.dto.Topico;
import com.imetro.domain.dto.diagnostico.DiagnosticoDisciplinaResumo;
import com.imetro.domain.dto.disciplina.DisciplinaDto;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoDisciplina;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoEtapa;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoInsight;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoPonto;
import com.imetro.domain.dto.planejamento.LeituraRecomendada;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoRegistro;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoResumo;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoEstado;
import com.imetro.domain.dto.progresso.ProgressoAlunoDisciplinaDto;
import com.imetro.domain.enums.Foco;
import com.imetro.persistence.repository.LivroMapaTopicosRepository;
import com.imetro.persistence.repository.PlaneamentoEstudoRepository;
import com.imetro.persistence.repository.TesteRepository;
import com.imetro.util.Authentication;
import com.imetro.util.ParseTimeStampLocalDate;
import com.imetro.util.QuestaoUtil;

public class  PlaneamentoEstudoService {

    private static final Locale LOCALE_PT = new Locale("pt", "AO");
    private static final DateTimeFormatter DATA_CURTA = DateTimeFormatter.ofPattern("dd/MM", LOCALE_PT);
    private static final DateTimeFormatter DATA_POR_EXTENSO = DateTimeFormatter.ofPattern("dd 'de' MMM", LOCALE_PT);

    private final DiagnosticoService diagnosticoService = new DiagnosticoService();
    private final TesteRepository testeRepository = new TesteRepository();
    private Map<String, Double> progressoPorSubtopico = Map.of();
    private final PlaneamentoEstudoRepository planeamentoRepository = new PlaneamentoEstudoRepository();
    private final LivroMapaTopicosRepository livroMapaTopicosRepository = new LivroMapaTopicosRepository();
    private final TrilhoLeituraService trilhoLeituraService = new TrilhoLeituraService();

    public PlaneamentoEstudoResumo gerarResumo(UUID candidatoId) {


        List<ProgressoAlunoDisciplinaDto> progressos = DisciplinaService.getProgressoDisciplinasCandidatoSafe();
        List<DiagnosticoDisciplinaResumo> diagnosticos = carregarDiagnosticosSeguros(candidatoId);
        List<Map<String, Object>> testesRows = carregarTestesSeguros(candidatoId);


        Map<String, DisciplinaDto> disciplinasBase = DisciplinaService.discCategoria().stream()
            .collect(Collectors.toMap(
                disciplina -> normalizarChave(disciplina.nome()),
                disciplina -> disciplina,
                (left, right) -> left,
                LinkedHashMap::new
            ));

        Map<String, AgrupadorDisciplina> agrupadores = new LinkedHashMap<>();

        for (DisciplinaDto disciplina : disciplinasBase.values()) {
            agrupadores.putIfAbsent(normalizarChave(disciplina.nome()), new AgrupadorDisciplina(disciplina.nome()));
        }

        for (ProgressoAlunoDisciplinaDto progresso : progressos) {
            String chave = normalizarChave(progresso.disciplina());
            AgrupadorDisciplina agrupador = agrupadores.computeIfAbsent(chave, key -> new AgrupadorDisciplina(progresso.disciplina()));
            agrupador.integrarProgresso(progresso);
            DisciplinaDto definicao = disciplinasBase.get(chave);
            if (definicao != null) {
                agrupador.integrarDefinicao(definicao);
            }
        }

        for (DiagnosticoDisciplinaResumo diagnostico : diagnosticos) {
            String chave = normalizarChave(diagnostico.nomeDisciplina());
            AgrupadorDisciplina agrupador = agrupadores.computeIfAbsent(chave, key -> new AgrupadorDisciplina(diagnostico.nomeDisciplina()));
            agrupador.integrarDiagnostico(diagnostico);
            DisciplinaDto definicao = disciplinasBase.get(chave);
            if (definicao != null) {
                agrupador.integrarDefinicao(definicao);
            }
        }

        for (Map<String, Object> row : testesRows) {
            String nomeDisciplina = safeText(row.get("disciplina_nome"), "Disciplina");
            String chave = normalizarChave(nomeDisciplina);
            AgrupadorDisciplina agrupador = agrupadores.computeIfAbsent(chave, key -> new AgrupadorDisciplina(nomeDisciplina));
            agrupador.adicionarTeste(parseTeste(row));
            DisciplinaDto definicao = disciplinasBase.get(chave);
            if (definicao != null) {
                agrupador.integrarDefinicao(definicao);
            }
        }

        List<PlaneamentoEstudoDisciplina> disciplinas = agrupadores.values().stream()
            .map(AgrupadorDisciplina::paraResumo)
            .filter(Objects::nonNull)
            .sorted(Comparator.comparingDouble(PlaneamentoEstudoDisciplina::prioridade).reversed())
            .toList();


        PlaneamentoEstudoDisciplina foco = disciplinas.getFirst();
        PlaneamentoEstudoDisciplina segundo = disciplinas.size() > 1 ? disciplinas.get(1) : foco;

        double heroScore = calcularHeroScore(disciplinas);
        String resumoHero = construirResumoHero(disciplinas, heroScore);
        String acertoMedio = formatarPercentual(calcularMedia(disciplinas.stream().mapToDouble(PlaneamentoEstudoDisciplina::precisao).boxed().toList()));
        String ritmoMedio = formatarTempoMedio(testesRows);
        String consistenciaMedia = classificarConsistencia(calcularMedia(disciplinas.stream().mapToDouble(PlaneamentoEstudoDisciplina::consistencia).boxed().toList()));
        String focoPrincipal = montarFocoPrincipal(foco);
        String focoSecundario = montarFocoPrincipal(segundo);

        String focoAtual = montarFocoAtual(foco);
        String focoAtual2 = montarFocoAtual(segundo);

        List<LeituraRecomendada> leituras = recomendarLeituras(disciplinas);

        List<PlaneamentoEstudoInsight> insights = new ArrayList<>(construirInsights(disciplinas));
        List<PlaneamentoEstudoEtapa> etapas = new ArrayList<>(construirEtapas(disciplinas));
        List<PlaneamentoEstudoRegistro> registros = new ArrayList<>(construirRegistros(testesRows));
        List<PlaneamentoEstudoPonto> evolucao = new ArrayList<>(construirEvolucao(testesRows));

        adicionarInsightsDeLeitura(insights, etapas, leituras);

        PlaneamentoEstudoResumo resumo = new PlaneamentoEstudoResumo(
            heroScore,
            resumoHero,
            acertoMedio,
            ritmoMedio,
            consistenciaMedia,
            focoPrincipal,
            focoSecundario,
            focoAtual,
            focoAtual2,
            insights,
            etapas,
            registros,
            disciplinas,
            evolucao,
            leituras
        );

        return finalizarResumo(candidatoId, resumo);
    }

    public PlaneamentoEstudoResumo gerarResumo() {
        return gerarResumo(Authentication.getCurrentUserId());
    }

    public boolean podeIniciarDiagnostico(UUID candidatoId) {
        if (candidatoId == null) {
            return false;
        }

        if (!RuntimeConfig.isDbEnabled()) {
            return true;
        }

        if (!diagnosticoService.temHistoricoDiagnostico(candidatoId)) {
            return true;
        }

        try {
            Optional<Map<String, Object>> ultimoPlaneamento = planeamentoRepository.findLatestByCandidatoId(candidatoId);
            if (ultimoPlaneamento.isEmpty()) {
                return false;
            }

            prolongarPlanejamentoSeNecessario(candidatoId, ultimoPlaneamento.get());
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao validar planeamento antes do diagnostico: " + e.getMessage());
            return false;
        }
    }

    public boolean possuiPlanejamento(UUID candidatoId) {
        if (candidatoId == null || !RuntimeConfig.isDbEnabled()) {
            return false;
        }

        try {
            return planeamentoRepository.findLatestByCandidatoId(candidatoId).isPresent();
        } catch (Exception e) {
            System.err.println("Erro ao verificar planeamento existente: " + e.getMessage());
            return false;
        }
    }

    public void prolongarPlanejamentoSeNecessario(UUID candidatoId) {
        if (candidatoId == null || !RuntimeConfig.isDbEnabled()) {
            return;
        }

        try {
            Optional<Map<String, Object>> ultimoPlaneamento = planeamentoRepository.findLatestByCandidatoId(candidatoId);
            if (ultimoPlaneamento.isEmpty()) {
                return;
            }
            prolongarPlanejamentoSeNecessario(candidatoId, ultimoPlaneamento.get());
        } catch (Exception e) {
            System.err.println("Erro ao prolongar planeamento de estudo: " + e.getMessage());
        }
    }

    public void limparPlaneamentos(UUID candidatoId) {
        if (candidatoId == null || !RuntimeConfig.isDbEnabled()) {
            return;
        }

        try {
            planeamentoRepository.deleteByCandidatoId(candidatoId);
        } catch (Exception e) {
            System.err.println("Erro ao limpar planeamentos de estudo: " + e.getMessage());
        }
    }

    public PlaneamentoEstudoEstado resolverEstadoAtual(UUID candidatoId) {
        if (!RuntimeConfig.isDbEnabled()) {
            return new PlaneamentoEstudoEstado(
                "Planeamento offline",
                "Ative a base de dados para ver e gravar o estado do plano.",
                "study-plan-banner-offline"
            );
        }

        if (candidatoId == null) {
            return new PlaneamentoEstudoEstado(
                "Sem plano ativo",
                "Conclua o diagnostico inicial para gerar o primeiro mapa de estudo.",
                "study-plan-banner-empty"
            );
        }

        try {
            Optional<Map<String, Object>> ultimoPlaneamento = planeamentoRepository.findLatestByCandidatoId(candidatoId);
            if (ultimoPlaneamento.isEmpty()) {
                boolean temHistorico = diagnosticoService.temHistoricoDiagnostico(candidatoId);
                return new PlaneamentoEstudoEstado(
                    "Sem plano ativo",
                    temHistorico
                        ? "Faça reset ou conclua o diagnostico inicial para gerar um novo mapa de estudo."
                        : "Conclua o diagnostico inicial para gerar o primeiro mapa de estudo.",
                    "study-plan-banner-empty"
                );
            }

            Map<String, Object> planeamento = ultimoPlaneamento.get();
            LocalDate semanaInicio = toLocalDate(planeamento.get("semana_inicio"));
            LocalDate semanaFim = toLocalDate(planeamento.get("semana_fim"));

            if (semanaFim != null && LocalDate.now().isAfter(semanaFim)) {
                prolongarPlanejamentoSeNecessario(candidatoId, planeamento);
                planeamento = planeamentoRepository.findLatestByCandidatoId(candidatoId).orElse(planeamento);
                semanaInicio = toLocalDate(planeamento.get("semana_inicio"));
                semanaFim = toLocalDate(planeamento.get("semana_fim"));
            }

            if (semanaFim == null) {
                return new PlaneamentoEstudoEstado(
                    "Sem plano ativo",
                    "Conclua o diagnostico inicial para gerar o primeiro mapa de estudo.",
                    "study-plan-banner-empty"
                );
            }

            boolean prolongado = semanaInicio != null && semanaFim.isAfter(semanaInicio.plusDays(6));
            String dataFim = semanaFim.format(DATA_CURTA);

            if (prolongado) {
                return new PlaneamentoEstudoEstado(
                    "Plano prolongado ate " + dataFim,
                    "A janela foi alargada para manter a rotina viva. Seguir o plano e a rota padrao continua a ser a escolha mais produtiva.",
                    "study-plan-banner-prolonged"
                );
            }

            return new PlaneamentoEstudoEstado(
                "Plano ativo ate " + dataFim,
                "Seguir o plano tende a ser a opcao mais produtiva para ganhar consistencia.",
                "study-plan-banner-active"
            );
        } catch (Exception e) {
            System.err.println("Erro ao resolver o estado do planeamento: " + e.getMessage());
            return new PlaneamentoEstudoEstado(
                "Planeamento indisponivel",
                "Nao foi possivel carregar o estado do plano agora.",
                "study-plan-banner-offline"
            );
        }
    }

    private List<PlaneamentoEstudoInsight> construirInsights(
        List<PlaneamentoEstudoDisciplina> disciplinas) {

    PlaneamentoEstudoDisciplina principal = disciplinas.getFirst();

    PlaneamentoEstudoDisciplina secundario =
            disciplinas.size() > 1
                    ? disciplinas.get(1)
                    : principal;

    return List.of(

        new PlaneamentoEstudoInsight(
                "Melhor oportunidade",
                principal.disciplina()
                        + " é atualmente a disciplina com maior potencial de evolução."
        ),

        new PlaneamentoEstudoInsight(
                "Conhecimento a arrefecer",
                principal.diasSemEstudo() > 0
                        ? "Já passaram "
                        + principal.diasSemEstudo()
                        + " dias sem contacto com este conteúdo."
                        + " Uma revisão agora pode evitar perdas futuras."
                        : "O conteúdo continua fresco. Este é o momento ideal para consolidar."
        ),

        new PlaneamentoEstudoInsight(
                "Combinação recomendada",
                secundario == principal
                        ? "Mistura exercícios novos com revisão de erros para acelerar a retenção."
                        : "Alternar "
                        + principal.disciplina()
                        + " e "
                        + secundario.disciplina()
                        + " ajuda a reduzir a fadiga mental."
        )
    );
}

private List<PlaneamentoEstudoEtapa> construirEtapas(
    List<PlaneamentoEstudoDisciplina> disciplinas) {

PlaneamentoEstudoDisciplina principal = disciplinas.getFirst();

PlaneamentoEstudoDisciplina secundario =
        disciplinas.size() > 1
                ? disciplinas.get(1)
                : principal;
if(principal.foco()!=null&& principal.foco().topico()==null){
     return  List.of(  new PlaneamentoEstudoEtapa(
            "Agora",
            "Missão principal",
            "Realizar o primeiro diagnostico "
                    + ". O objetivo é ver qual é o teu nivel actual nos subtopicos selecionados."
    )
    ); 
}
return List.of(

    new PlaneamentoEstudoEtapa(
            "Hoje",
            "Missão principal",
            "25 minutos focados em "
                    + principal.foco().subtopico()
                    + ". O objetivo é recuperar confiança rapidamente."
    ),

    new PlaneamentoEstudoEtapa(
            "Amanhã",
            "Consolidação",
            "Reforça o conteúdo estudado hoje e corrige os erros encontrados."
    ),

    new PlaneamentoEstudoEtapa(
            "48h",
            "Desafio combinado",
            "Alterna "
                    + principal.disciplina()
                    + " e "
                    + secundario.disciplina()
                    + " através de questões mistas."
    ),

    new PlaneamentoEstudoEtapa(
            "Fim da semana",
            "Checkpoint semanal",
            "Realiza um mini diagnóstico para medir a evolução real."
    )
);
}

    private List<PlaneamentoEstudoRegistro> construirRegistros(List<Map<String, Object>> testesRows) {
        return testesRows.stream()
            .map(this::parseTeste)
            .sorted(Comparator.comparing(
                (TesteSnapshot teste) -> teste.momento() == null ? LocalDateTime.MIN : teste.momento()
            ).reversed())
            .limit(3)
            .map(teste -> new PlaneamentoEstudoRegistro(
                teste.diagnostico()
                    ? "Teste adaptativo"
                    : "Sessão de treino",
                teste.disciplina(),
                construirResumoRegistro(teste),
                formatarMomentoRelativo(teste.momento()),
                teste.acerto() >= 75d ? "pill-good" : "pill-warn"
            ))
            .toList();
    }

    private List<PlaneamentoEstudoPonto> construirEvolucao(List<Map<String, Object>> testesRows) {
        Map<LocalDate, List<Double>> porDia = new TreeMap<>();

        for (Map<String, Object> row : testesRows) {
            TesteSnapshot teste = parseTeste(row);
            if (teste.momento() == null) {
                continue;
            }
            porDia.computeIfAbsent(teste.momento().toLocalDate(), key -> new ArrayList<>()).add(teste.acerto());
        }

        if (porDia.isEmpty()) {
            List<PlaneamentoEstudoPonto> fallback = new ArrayList<>();
            double base = 56d;
            for (int i = 0; i < 6; i++) {
                fallback.add(new PlaneamentoEstudoPonto("Sem " + (i + 1), base + (i * 5d)));
            }
            return fallback;
        }

        List<Map.Entry<LocalDate, List<Double>>> ultimosDias = new ArrayList<>(porDia.entrySet());
        if (ultimosDias.size() > 6) {
            ultimosDias = ultimosDias.subList(ultimosDias.size() - 6, ultimosDias.size());
        }

        List<PlaneamentoEstudoPonto> evolucao = new ArrayList<>();
        for (Map.Entry<LocalDate, List<Double>> entry : ultimosDias) {
            evolucao.add(new PlaneamentoEstudoPonto(
                entry.getKey().format(DATA_CURTA),
                calcularMedia(entry.getValue())
            ));
        }
        return evolucao;
    }


    private PlaneamentoEstudoResumo finalizarResumo(UUID candidatoId, PlaneamentoEstudoResumo resumo) {
        persistirResumo(candidatoId, resumo);
        return resumo;
    }

    private void persistirResumo(UUID candidatoId, PlaneamentoEstudoResumo resumo) {
        if (candidatoId == null || resumo == null || !RuntimeConfig.isDbEnabled()) {
            return;
        }

        try {
            LocalDate semanaInicio = inicioSemanaAtual();
            LocalDate semanaFim = semanaInicio.plusDays(6);
            String resumoJson = serializarResumo(resumo);
            String assinatura = calcularAssinatura(resumoJson);

            planeamentoRepository.upsertSnapshot(
                candidatoId,
                semanaInicio,
                semanaFim,
                assinatura,
                resumo.pontuacaoHero(),
                resumo.resumoHero(),
                resumo.acertoMedio(),
                resumo.ritmoMedio(),
                resumo.consistenciaMedia(),
                resumo.focoAtual(),
                resumoJson
            );
        } catch (Exception e) {
            System.err.println("Erro ao persistir planeamento de estudo: " + e.getMessage());
        }
    }

    private LocalDate inicioSemanaAtual() {
        return LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    private void prolongarPlanejamentoSeNecessario(UUID candidatoId, Map<String, Object> planeamento) throws Exception {
        LocalDate fimAtual = toLocalDate(planeamento.get("semana_fim"));
        if (fimAtual == null) {
            return;
        }

        LocalDate hoje = LocalDate.now();
        if (!hoje.isAfter(fimAtual)) {
            return;
        }

        planeamentoRepository.prolongarUltimoPlanejamento(candidatoId, hoje.plusDays(6));
    }

    private LocalDate toLocalDate(Object value) {
        if (value instanceof LocalDate localDate) {
            return localDate;
        }
        if (value instanceof java.sql.Date sqlDate) {
            return sqlDate.toLocalDate();
        }
        if (value == null) {
            return null;
        }
        try {
            return LocalDate.parse(value.toString());
        } catch (RuntimeException ignored) {
            return null;
        }
    }

    private String serializarResumo(PlaneamentoEstudoResumo resumo) {
        StringBuilder json = new StringBuilder(1024);
        json.append('{');
        json.append("\"pontuacaoHero\":").append(formatJsonDouble(resumo.pontuacaoHero())).append(',');
        json.append("\"resumoHero\":\"").append(QuestaoUtil.escapeJson(safeText(resumo.resumoHero(), ""))).append("\",");
        json.append("\"acertoMedio\":\"").append(QuestaoUtil.escapeJson(safeText(resumo.acertoMedio(), ""))).append("\",");
        json.append("\"ritmoMedio\":\"").append(QuestaoUtil.escapeJson(safeText(resumo.ritmoMedio(), ""))).append("\",");
        json.append("\"consistenciaMedia\":\"").append(QuestaoUtil.escapeJson(safeText(resumo.consistenciaMedia(), ""))).append("\",");
        json.append("\"focoAtual\":\"").append(QuestaoUtil.escapeJson(safeText(resumo.focoAtual(), ""))).append("\",");
        json.append("\"insights\":").append(serializarLista(resumo.insights(), this::serializarInsight)).append(',');
        json.append("\"etapas\":").append(serializarLista(resumo.etapas(), this::serializarEtapa)).append(',');
        json.append("\"registros\":").append(serializarLista(resumo.registros(), this::serializarRegistro)).append(',');
        json.append("\"disciplinas\":").append(serializarLista(resumo.disciplinas(), this::serializarDisciplina)).append(',');
        json.append("\"evolucao\":").append(serializarLista(resumo.evolucao(), this::serializarPonto));
        json.append('}');
        return json.toString();
    }

    private <T> String serializarLista(List<T> itens, Function<T, String> serializador) {
        List<T> valores = itens == null ? List.of() : itens;
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < valores.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            String item = serializador.apply(valores.get(i));
            json.append(item == null ? "null" : item);
        }
        return json.append(']').toString();
    }

    private String serializarInsight(PlaneamentoEstudoInsight insight) {
        if (insight == null) {
            return "null";
        }
        return "{"
            + "\"titulo\":\"" + QuestaoUtil.escapeJson(safeText(insight.titulo(), "")) + "\","
            + "\"descricao\":\"" + QuestaoUtil.escapeJson(safeText(insight.descricao(), "")) + "\""
            + "}";
    }

    private String serializarEtapa(PlaneamentoEstudoEtapa etapa) {
        if (etapa == null) {
            return "null";
        }
        return "{"
            + "\"janela\":\"" + QuestaoUtil.escapeJson(safeText(etapa.janela(), "")) + "\","
            + "\"acao\":\"" + QuestaoUtil.escapeJson(safeText(etapa.acao(), "")) + "\","
            + "\"detalhe\":\"" + QuestaoUtil.escapeJson(safeText(etapa.detalhe(), "")) + "\""
            + "}";
    }

    private String serializarRegistro(PlaneamentoEstudoRegistro registro) {
        if (registro == null) {
            return "null";
        }
        return "{"
            + "\"tipo\":\"" + QuestaoUtil.escapeJson(safeText(registro.tipo(), "")) + "\","
            + "\"disciplina\":\"" + QuestaoUtil.escapeJson(safeText(registro.disciplina(), "")) + "\","
            + "\"resumo\":\"" + QuestaoUtil.escapeJson(safeText(registro.resumo(), "")) + "\","
            + "\"momento\":\"" + QuestaoUtil.escapeJson(safeText(registro.momento(), "")) + "\","
            + "\"pillClass\":\"" + QuestaoUtil.escapeJson(safeText(registro.pillClass(), "")) + "\""
            + "}";
    }

    private String serializarDisciplina(PlaneamentoEstudoDisciplina disciplina) {
        if (disciplina == null) {
            return "null";
        }
        return "{"
            + "\"disciplina\":\"" + QuestaoUtil.escapeJson(safeText(disciplina.disciplina(), "")) + "\","
            + "\"pontuacao\":" + formatJsonDouble(disciplina.pontuacao()) + ","
            + "\"precisao\":" + formatJsonDouble(disciplina.precisao()) + ","
            + "\"velocidade\":" + formatJsonDouble(disciplina.velocidade()) + ","
            + "\"consistencia\":" + formatJsonDouble(disciplina.consistencia()) + ","
            + "\"diasSemEstudo\":" + Math.max(0, disciplina.diasSemEstudo()) + ","
            + "\"foco\":\"" + QuestaoUtil.escapeJson(safeText(disciplina.foco(), "")) + "\","
            + "\"observacao\":\"" + QuestaoUtil.escapeJson(safeText(disciplina.observacao(), "")) + "\","
            + "\"prioridade\":" + formatJsonDouble(disciplina.prioridade())
            + "}";
    }

    private String serializarPonto(PlaneamentoEstudoPonto ponto) {
        if (ponto == null) {
            return "null";
        }
        return "{"
            + "\"rotulo\":\"" + QuestaoUtil.escapeJson(safeText(ponto.rotulo(), "")) + "\","
            + "\"valor\":" + formatJsonDouble(ponto.valor())
            + "}";
    }

    private String formatJsonDouble(double valor) {
        if (Double.isNaN(valor) || Double.isInfinite(valor)) {
            valor = 0d;
        }
        return String.format(Locale.ROOT, "%.4f", valor);
    }

    private String calcularAssinatura(String conteudo) {
        String texto = conteudo == null ? "" : conteudo;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                int valor = b & 0xff;
                hex.append(Character.forDigit((valor >>> 4) & 0x0f, 16));
                hex.append(Character.forDigit(valor & 0x0f, 16));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            return Integer.toHexString(texto.hashCode());
        }
    }

    private List<DiagnosticoDisciplinaResumo> carregarDiagnosticosSeguros(UUID candidatoId) {
        try {
            return diagnosticoService.carregarDiagnosticosDisponiveis(candidatoId);
        } catch (Exception e) {
            System.err.println("Erro ao carregar diagnosticos para o planeamento: " + e.getMessage());
            return List.of();
        }
    }

    private List<Map<String, Object>> carregarTestesSeguros(UUID candidatoId) {
        try {
            return testeRepository.findByCandidatoId(candidatoId);
        } catch (Exception e) {
            System.err.println("Erro ao carregar testes para o planeamento: " + e.getMessage());
            return List.of();
        }
    }

    private TesteSnapshot parseTeste(Map<String, Object> row) {
        String disciplina = safeText(row.get("disciplina_nome"), "Disciplina");
        LocalDateTime momento = primeiroNaoNulo(
            ParseTimeStampLocalDate.mapearDataHora(row.get("data_teste")),
            ParseTimeStampLocalDate.mapearDataHora(row.get("criado_em"))
        );
        return new TesteSnapshot(
            disciplina,
            momento,
            normalizarPercentual(row.get("percentual_acerto")),
            normalizarPercentual(row.get("velocidade")),
            normalizarPercentual(row.get("precisao")),
            normalizarPercentual(row.get("consistencia")),
            normalizarInteiro(row.get("duracao_segundos")),
            normalizarInteiro(row.get("total_questoes")),
            safeText(row.get("nivel_final"), ""),
            safeText(row.get("nivel_inicial"), ""),
            safeText(row.get("observacoes"), ""),
            row.get("diagnostico_id") != null
        );
    }
    private String calcularImpacto(double prioridade) {

        if (prioridade >= 80) {
            return "Muito Alto";
        }

        if (prioridade >= 60) {
            return "Alto";
        }

        if (prioridade >= 40) {
            return "Moderado";
        }

        if (prioridade >= 20) {
            return "Baixo";
        }

        return "Muito Baixo";
    }

    private String construirResumoHero(List<PlaneamentoEstudoDisciplina> disciplinas, double heroScore) {

        PlaneamentoEstudoDisciplina principal = disciplinas.getFirst();
        CacheService.put("disciplina_principal", principal.disciplina());
        String impacto = calcularImpacto(principal.prioridade());

        if (heroScore >= 80) {
            return """
                 Estás numa fase de consolidação.
                O objetivo agora não é estudar mais, mas manter o que já conquistaste.
                %s continua a ser a disciplina com maior potencial de evolução.
                """
                .formatted(principal.disciplina());
        }

        if (principal.diasSemEstudo() >= 7) {
            return """
                %s está a perder frescura.
                Uma revisão curta agora vale muito mais do que reaprender tudo daqui a alguns dias.
                Impacto esperado: %s.
                """
                .formatted(principal.disciplina(), impacto);
        }

        return """
            O sistema identificou %s como a melhor oportunidade de crescimento desta semana.
            Pequenos avanços nesta área podem refletir-se diretamente no teu desempenho geral.
            Impacto esperado: %s.
            """
            .formatted(principal.disciplina(), impacto);
    }
    private double calcularHeroScore(List<PlaneamentoEstudoDisciplina> disciplinas) {
        double somaPonderada = 0d;
        double somaPesos = 0d;

        for (PlaneamentoEstudoDisciplina disciplina : disciplinas) {
            double peso = Math.max(0.5d, disciplina.prioridade() > 0d ? 100d - disciplina.prioridade() : 1d);
            somaPonderada += disciplina.pontuacao() * peso;
            somaPesos += peso;
        }

        if (somaPesos <= 0d) {
            return 0d;
        }

        return clamp(somaPonderada / somaPesos, 0d, 100d);
    }

    private String montarFocoPrincipal(PlaneamentoEstudoDisciplina foco) {
        if (foco == null || (foco.foco() == null&&foco.foco().topico() == null)) {
            return "-"; 
        }

        Foco focoObj = foco.foco();
        String base = primeiroNaoVazio(
            focoObj.subtopico() != null ? focoObj.topico().topicos() + " > ." + focoObj.subtopico() : null,
            focoObj.topico().topicos(),
            foco.disciplina()
        );

        return foco.disciplina() + " . " + base;
    }

    private String montarFocoAtual(PlaneamentoEstudoDisciplina foco) {
        if (foco == null || foco.foco() == null) {
            return "—";
        }

        Foco focoObj = foco.foco();
        Topico topico = focoObj.topico();
        String subtopicoAtual = focoObj.subtopico();

        try {
            // Atualiza o progresso dos subtópicos
            progressoPorSubtopico = diagnosticoService.carregarProgressoSubtopicos(
                Authentication.getCurrentUserId(),
                CacheService.get("nivel").toString().toLowerCase(),
                topico
            );

            // Se já temos um subtópico definido, mostra o progresso atual dele
            if (subtopicoAtual != null && !subtopicoAtual.isBlank()) {
                String chave = chaveSubtopico(topico.disciplina(), subtopicoAtual);
                Double progresso = progressoPorSubtopico.get(chave);

                if (progresso != null) {
                    double percentual = progresso * 100;
                    return foco.disciplina() + "-" + topico.topicos() + "-" +
                           subtopicoAtual + "-(" + String.format("%.0f%%", percentual) + ")";
                }

                return foco.disciplina() + "-" + topico.topicos() + "-" + subtopicoAtual;
            }

            // Se não tem subtópico definido, encontra o de menor progresso
            if (!progressoPorSubtopico.isEmpty()) {
                String subtopicoFoco = encontrarSubtopicoMenorProgresso(progressoPorSubtopico, topico);
                return foco.disciplina() + "-" + topico.topicos() + "-" + subtopicoFoco;
            }

            // Fallback: primeiro subtópico
            String primeiroSubtopico = extrairPrimeiroSubtopico(topico);
            return foco.disciplina() + "-" + topico.topicos() + "-" + primeiroSubtopico;

        } catch (SQLException e) {
            System.err.println("Erro ao carregar progresso dos subtópicos: " + e.getMessage());
            String fallback = subtopicoAtual != null ? subtopicoAtual : topico.topicos();
            return foco.disciplina() + "-" + topico.topicos() + "-" + fallback;
        }
    }

    private List<LeituraRecomendada> recomendarLeituras(List<PlaneamentoEstudoDisciplina> disciplinas) {
        ArrayList<LeituraRecomendada> leituras = new ArrayList<>();
        LinkedHashSet<String> chavesVistas = new LinkedHashSet<>();

        for (PlaneamentoEstudoDisciplina disc : disciplinas) {
            if (disc == null || disc.foco() == null || disc.foco().topico() == null) continue;

            Foco foco = disc.foco();
            String topicoNome = foco.topico().topicos();
            String subtopicoNome = foco.subtopico();

            List<String> topicos = List.of(topicoNome);
            List<String> subtopicos = subtopicoNome != null && !subtopicoNome.isBlank()
                ? List.of(subtopicoNome)
                : List.of();

            try {
                List<LeituraRecomendada> recomendadas = trilhoLeituraService.recomendarLeiturasParaSubtopicos(
                    null,
                    disc.disciplina(),
                    topicos,
                    subtopicos
                );
                for (LeituraRecomendada leitura : recomendadas) {
                    String chave = leitura.livroId() + "::" + leitura.topico() + "::" + leitura.subtopico();
                    if (chavesVistas.add(chave)) {
                        leituras.add(leitura);
                    }
                }
            } catch (Exception e) {
                System.err.println("Erro ao recomendar leituras para " + disc.disciplina() + ": " + e.getMessage());
            }
        }

        return List.copyOf(leituras);
    }

    private void adicionarInsightsDeLeitura(
        List<PlaneamentoEstudoInsight> insights,
        List<PlaneamentoEstudoEtapa> etapas,
        List<LeituraRecomendada> leituras
    ) {
        if (leituras == null || leituras.isEmpty()) return;

        StringBuilder descricao = new StringBuilder();
        int maxLeituras = Math.min(3, leituras.size());
        for (int i = 0; i < maxLeituras; i++) {
            LeituraRecomendada leitura = leituras.get(i);
            if (descricao.length() > 0) descricao.append("\n");
            descricao.append("\u2022 ").append(leitura.tituloLivro())
                .append(": ").append(leitura.topico())
                .append(" (").append(leitura.paginaInicio())
                .append("-").append(leitura.paginaFim()).append(")");
        }
        if (leituras.size() > 3) {
            descricao.append("\n... e mais ").append(leituras.size() - 3).append(" seccoes");
        }

        insights.add(new PlaneamentoEstudoInsight(
            "Leituras recomendadas",
            descricao.toString()
        ));

        for (int i = 0; i < Math.min(2, leituras.size()); i++) {
            LeituraRecomendada leitura = leituras.get(i);
            etapas.add(new PlaneamentoEstudoEtapa(
                "Leitura",
                "Ler " + leitura.topico() + " em " + leitura.tituloLivro(),
                leitura.formatarPaginas()
            ));
        }
    }

    /**
     * Encontra o subtópico com menor percentagem de progresso (maior que 0).
     * Se todos forem 0, retorna o primeiro subtópico disponível.
     */
    private String encontrarSubtopicoMenorProgresso(Map<String, Double> progressos, Topico topico) {
        if (topico.subTopicos() == null || topico.subTopicos().length == 0) {
            return topico.topicos();
        }

        String disciplina = topico.disciplina();
        String subtopicoMenorProgresso = null;
        double menorProgresso = Double.MAX_VALUE;
        boolean todosZero = true;

        for (String subtopico : topico.subTopicos()) {
            String chave = chaveSubtopico(disciplina, subtopico);
            Double progresso = progressos.get(chave);

            if (progresso == null) {
                if (subtopicoMenorProgresso == null) {
                    subtopicoMenorProgresso = subtopico;
                }
                continue;
            }

            double progressoValor = progresso * 100;

            if (progressoValor > 0) {
                todosZero = false;
                if (progressoValor < menorProgresso) {
                    menorProgresso = progressoValor;
                    subtopicoMenorProgresso = subtopico;
                }
            }
        }

        if (todosZero) {
            for (String subtopico : topico.subTopicos()) {
                String chave = chaveSubtopico(disciplina, subtopico);
                Double progresso = progressos.get(chave);

                if (progresso == null || progresso == 0.0) {
                    return subtopico; // Primeiro não iniciado
                }
            }
            return topico.subTopicos()[0];
        }

        if (subtopicoMenorProgresso != null) {
            return subtopicoMenorProgresso + " (" + String.format("%.0f%%", menorProgresso) + ")";
        }

        return topico.subTopicos()[0];
    }


    private String extrairPrimeiroSubtopico(Topico topico) {
        if (topico.subTopicos() != null && topico.subTopicos().length > 0) {
            return topico.subTopicos()[0];
        }
        return topico.topicos();
    }


    private String chaveSubtopico(String disciplina, String subtopico) {
        return QuestaoUtil.normalizar(disciplina) + "::" +
               QuestaoUtil.normalizar(QuestaoUtil.safeText(subtopico, "Geral"));
    }

    private double calcularMedia(List<Double> valores) {
        if (valores == null || valores.isEmpty()) {
            return 0d;
        }
        double soma = 0d;
        int count = 0;
        for (Double valor : valores) {
            if (valor == null || Double.isNaN(valor) || Double.isInfinite(valor)) {
                continue;
            }
            soma += valor;
            count++;
        }
        return count == 0 ? 0d : soma / count;
    }

    private String formatarTempoMedio(List<Map<String, Object>> testesRows) {
        int totalSegundos = 0;
        int totalQuestoes = 0;

        for (Map<String, Object> row : testesRows) {
            int segundos = normalizarInteiro(row.get("duracao_segundos"));
            int questoes = Math.max(1, normalizarInteiro(row.get("total_questoes")));
            totalSegundos += Math.max(0, segundos);
            totalQuestoes += questoes;
        }

        if (totalQuestoes <= 0) {
            return "Sem dados";
        }

        long media = Math.round((double) totalSegundos / totalQuestoes);
        return media + " s";
    }

    private String classificarConsistencia(double consistencia) {
        if (consistencia >= 80d) {
            return "Alta";
        }
        if (consistencia >= 60d) {
            return "Estável";
        }
        if (consistencia >= 40d) {
            return "Irregular";
        }
        return "A reforçar";
    }

    private String construirResumoRegistro(TesteSnapshot teste) {
        StringBuilder resumo = new StringBuilder();
        resumo.append(formatarPercentual(teste.acerto()));
        resumo.append(" de acerto");
        if (teste.consistencia() > 0d) {
            resumo.append(", ").append(formatarPercentual(teste.consistencia())).append(" de consistência");
        }
        if (teste.totalQuestoes() > 0 && teste.duracaoSegundos() > 0) {
            long tempoMedio = Math.round((double) teste.duracaoSegundos() / teste.totalQuestoes());
            resumo.append(", ").append(tempoMedio).append(" s por questão");
        }
        if (!teste.nivelFinal().isBlank()) {
            resumo.append(", nível ").append(teste.nivelFinal());
        }
        return resumo.append('.').toString();
    }

    private double normalizarPercentual(Object value) {
        if (value instanceof Number number) {
            double valor = number.doubleValue();
            if (Double.isNaN(valor) || Double.isInfinite(valor)) {
                return 0d;
            }
            if (valor <= 1d) {
                return clamp(valor * 100d, 0d, 100d);
            }
            return clamp(valor, 0d, 100d);
        }
        return 0d;
    }

    private int normalizarInteiro(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        if (value instanceof String text && !text.isBlank()) {
            try {
                return Integer.parseInt(text.trim());
            } catch (NumberFormatException ignored) {
                return 0;
            }
        }
        return 0;
    }

    private LocalDateTime primeiroNaoNulo(LocalDateTime primeiro, LocalDateTime segundo) {
        return primeiro != null ? primeiro : segundo;
    }

    private String safeText(Object value, String fallback) {
        if (value == null) {
            return fallback;
        }
        String text = value.toString().trim();
        return text.isBlank() ? fallback : text;
    }

    private String normalizarChave(String valor) {
        return QuestaoUtil.normalizar(valor);
    }

    private String formatarPercentual(double valor) {
        return Math.round(clamp(valor, 0d, 100d)) + "%";
    }

    private String formatarMomentoRelativo(LocalDateTime momento) {
        if (momento == null) {
            return "Hoje";
        }

        LocalDate hoje = LocalDate.now();
        LocalDate data = momento.toLocalDate();
        long dias = ChronoUnit.DAYS.between(data, hoje);
        if (dias <= 0) {
            return "Hoje";
        }
        if (dias == 1) {
            return "Ontem";
        }
        if (dias < 7) {
            return "Há " + dias + " dias";
        }
        return data.format(DATA_POR_EXTENSO);
    }

    private String primeiroNaoVazio(String... valores) {
        if (valores == null || valores.length == 0) {
            return "—";
        }

        for (String valor : valores) {
            if (valor != null && !valor.isBlank()) {
                return valor;
            }
        }
        return "—";
    }

    private double clamp(double valor, double min, double max) {
        return Math.max(min, Math.min(max, valor));
    }

    private final class AgrupadorDisciplina {
        private String nome;
        private DisciplinaDto definicao;
        private ProgressoAlunoDisciplinaDto progresso;
        private DiagnosticoDisciplinaResumo diagnostico;
        private final List<TesteSnapshot> testes = new ArrayList<>();

        private AgrupadorDisciplina(String nome) {
            this.nome = nome;
        }

        private void integrarDefinicao(DisciplinaDto definicao) {
            if (definicao == null) {
                return;
            }
            this.definicao = definicao;
            if (this.nome == null || this.nome.isBlank()) {
                this.nome = definicao.nome();
            }
        }

        private void integrarProgresso(ProgressoAlunoDisciplinaDto progresso) {
            this.progresso = progresso;
            if (progresso != null && (this.nome == null || this.nome.isBlank())) {
                this.nome = progresso.disciplina();
            }
        }

        private void integrarDiagnostico(DiagnosticoDisciplinaResumo diagnostico) {
            this.diagnostico = diagnostico;
            if (diagnostico != null && (this.nome == null || this.nome.isBlank())) {
                this.nome = diagnostico.nomeDisciplina();
            }
        }

        private void adicionarTeste(TesteSnapshot teste) {
            if (teste != null) {
                testes.add(teste);
            }
        }

        private PlaneamentoEstudoDisciplina paraResumo() {
            if (progresso == null && diagnostico == null && testes.isEmpty()) {
                return null;
            }

            String disciplina = primeiroNaoVazio(
                progresso == null ? null : progresso.disciplina(),
                diagnostico == null ? null : diagnostico.nomeDisciplina(),
                definicao == null ? null : definicao.nome(),
                nome
            );

            double precisao = calcularPrecisao();
            double velocidade = calcularVelocidade();
            double consistencia = calcularConsistencia();
            int diasSemEstudo = calcularDiasSemEstudo();
            Foco foco = extrairFoco();
            String observacao = extrairObservacao();
            double pontuacao = calcularPontuacao(precisao, velocidade, consistencia, diasSemEstudo);
            double prioridade = calcularPrioridade(pontuacao, diasSemEstudo);

            return new PlaneamentoEstudoDisciplina(
                disciplina,
                pontuacao,
                precisao,
                velocidade,
                consistencia,
                diasSemEstudo,
                foco,
                observacao,
                prioridade
            );
        }

        private double calcularPrecisao() {
            if (progresso != null && progresso.taxaAcertoGeral() != null) {
                return normalizarValor(progresso.taxaAcertoGeral());
            }
            if (diagnostico != null && diagnostico.indicador() > 0d) {
                return clamp(diagnostico.indicador() * 100d, 0d, 100d);
            }
            double media = mediaTestes(TesteSnapshot::acerto);
            if (media > 0d) {
                return media;
            }
            return 0d;
        }

        private double calcularVelocidade() {
            double media = mediaTestes(TesteSnapshot::velocidade);
            if (media > 0d) {
                return media;
            }
            return calcularPrecisao();
        }

        private double calcularConsistencia() {
            double media = mediaTestes(TesteSnapshot::consistencia);
            if (media > 0d) {
                return media;
            }
            return progresso != null && progresso.taxaAcertoGeral() != null ? normalizarValor(progresso.taxaAcertoGeral()) : 0d;
        }

        private int calcularDiasSemEstudo() {
            if (progresso != null && progresso.diasSemEstudo() != null) {
                return Math.max(0, progresso.diasSemEstudo());
            }
            LocalDateTime ultimo = testes.stream()
                .map(TesteSnapshot::momento)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);
            if (ultimo == null) {
                return 0;
            }
            return (int) Math.max(0, ChronoUnit.DAYS.between(ultimo.toLocalDate(), LocalDate.now()));
        }

        private Foco extrairFoco() {
            if (diagnostico != null) {
                if (diagnostico.topicos() != null && !diagnostico.topicos().isEmpty()) {
                    Topico topico = diagnostico.topicos().getFirst();
                    if (topico.topicos() != null && !topico.topicos().isBlank()) {

                        // Identifica o subtópico principal (menor progresso > 0 ou primeiro disponível)
                        String subtopicoPrincipal = identificarSubtopicoPrincipal(topico);

                        return new Foco(topico, subtopicoPrincipal);
                    }
                }
            }
            if (CacheService.get("disciplina_principal")!=null) {
                return new Foco(null, CacheService.get("disciplina_principal").toString());
            }
            return null;
        }

        /**
         * Identifica o subtópico principal baseado no progresso.
         * Prioriza o de menor progresso > 0, ou o primeiro subtópico se todos forem 0.
         */
        private String identificarSubtopicoPrincipal(Topico topico) {
            if (topico.subTopicos() == null || topico.subTopicos().length == 0) {
                return topico.topicos(); // Retorna o nome do tópico se não houver subtópicos
            }

            try {
                // Tenta carregar o progresso dos subtópicos
                Map<String, Double> progressos = diagnosticoService.carregarProgressoSubtopicos(
                    Authentication.getCurrentUserId(),
                    CacheService.get("nivel").toString().toLowerCase(),
                    topico
                );

                if (progressos.isEmpty()) {
                    // Sem dados de progresso, retorna o primeiro subtópico
                    return topico.subTopicos()[0];
                }

                // Encontra o subtópico com menor progresso (maior que 0)
                String subtopicoMenorProgresso = null;
                double menorProgresso = Double.MAX_VALUE;
                boolean todosZero = true;

                for (String subtopico : topico.subTopicos()) {
                    String chave = chaveSubtopico(topico.disciplina(), subtopico);
                    Double progresso = progressos.get(chave);

                    if (progresso == null) {
                        // Subtópico sem registro - candidato a primeiro não iniciado
                        if (subtopicoMenorProgresso == null) {
                            subtopicoMenorProgresso = subtopico;
                        }
                        continue;
                    }

                    double progressoValor = progresso * 100; // Converte para percentagem

                    if (progressoValor > 0) {
                        todosZero = false;
                        if (progressoValor < menorProgresso) {
                            menorProgresso = progressoValor;
                            subtopicoMenorProgresso = subtopico;
                        }
                    }
                }

                if (todosZero) {
                    // Todos com 0% - retorna o primeiro não iniciado ou o primeiro da lista
                    for (String subtopico : topico.subTopicos()) {
                        String chave = chaveSubtopico(topico.disciplina(), subtopico);
                        Double progresso = progressos.get(chave);

                        if (progresso == null || progresso == 0.0) {
                            return subtopico; // Primeiro não iniciado
                        }
                    }
                    return topico.subTopicos()[0]; // Fallback: primeiro da lista
                }

                // Retorna o subtópico com menor progresso (maior que 0)
                return subtopicoMenorProgresso != null ? subtopicoMenorProgresso : topico.subTopicos()[0];

            } catch (SQLException e) {
                System.err.println("Erro ao identificar subtópico principal: " + e.getMessage());
                // Em caso de erro, retorna o primeiro subtópico
                return topico.subTopicos().length > 0 ? topico.subTopicos()[0] : topico.topicos();
            }
        }

        private String extrairObservacao() {
            if (diagnostico != null) {
                if (diagnostico.observacao() != null && !diagnostico.observacao().isBlank()) {
                    return diagnostico.observacao();
                }
                if (diagnostico.resumo() != null && !diagnostico.resumo().isBlank()) {
                    return diagnostico.resumo();
                }
            }
            if (progresso != null && progresso.ultimoEstudo() != null) {
                return "Último estudo em " + progresso.ultimoEstudo().format(DateTimeFormatter.ofPattern("dd/MM"));
            }
            return "Base ativa para o planeamento.";
        }

        private double calcularPontuacao(double precisao, double velocidade, double consistencia, int diasSemEstudo) {
            double frescura = clamp(100d - (diasSemEstudo * 7d), 0d, 100d);
            double tendencia = diagnostico == null ? 0d : calcularAjusteTendencia(diagnostico.tendencia());
            double peso = definicao != null && definicao.peso() != null ? definicao.peso() : 1d;
            double pesoBoost = clamp((peso - 1d) * 8d, -8d, 8d);

            double pontuacao = (precisao * 0.46d)
                + (consistencia * 0.22d)
                + (velocidade * 0.12d)
                + (diagnostico == null ? 0d : clamp(diagnostico.indicador() * 100d, 0d, 100d) * 0.10d)
                + (frescura * 0.05d)
                + tendencia
                + pesoBoost;

            return clamp(pontuacao, 0d, 100d);
        }

        private double calcularPrioridade(double pontuacao, int diasSemEstudo) {
            double recencia = clamp(diasSemEstudo * 2.5d, 0d, 25d);
            double base = 100d - pontuacao;
            double tendencia = diagnostico == null ? 0d : calcularAjusteTendencia(diagnostico.tendencia()) < 0d ? 5d : 0d;
            return clamp(base + recencia + tendencia, 0d, 100d);
        }

        private double calcularAjusteTendencia(String tendencia) {
            if (tendencia == null || tendencia.isBlank()) {
                return 0d;
            }
            String limpa = tendencia.trim();
            try {
                if (limpa.startsWith("+")) {
                    double valor = Double.parseDouble(limpa.replace("%", "")) / 5d;
                    return Math.max(0d, Math.min(6d, valor));
                }
                if (limpa.startsWith("-")) {
                    double valor = Double.parseDouble(limpa.replace("%", "")) / 5d;
                    return Math.max(-6d, Math.min(0d, valor));
                }
            } catch (NumberFormatException ignored) {
            }
            return 0d;
        }

        private double mediaTestes(TesteMetric metric) {
            if (testes.isEmpty()) {
                return 0d;
            }
            double soma = 0d;
            int count = 0;
            for (TesteSnapshot teste : testes) {
                double valor = metric.extrair(teste);
                if (valor <= 0d) {
                    continue;
                }
                soma += valor;
                count++;
            }
            return count == 0 ? 0d : soma / count;
        }

        private String disciplinaNomeCurto() {
            return nome == null || nome.isBlank() ? "Disciplina" : nome;
        }

        private double normalizarValor(Double valor) {
            if (valor == null || Double.isNaN(valor) || Double.isInfinite(valor)) {
                return 0d;
            }
            if (valor <= 1d) {
                return clamp(valor * 100d, 0d, 100d);
            }
            return clamp(valor, 0d, 100d);
        }
    }

    @FunctionalInterface
    private interface TesteMetric {
        double extrair(TesteSnapshot snapshot);
    }

    private record TesteSnapshot(
        String disciplina,
        LocalDateTime momento,
        double acerto,
        double velocidade,
        double precisao,
        double consistencia,
        int duracaoSegundos,
        int totalQuestoes,
        String nivelFinal,
        String nivelInicial,
        String observacoes,
        boolean diagnostico
    ) {
    }

    private String gerarPrevisao(PlaneamentoEstudoDisciplina disciplina) {

        double atual = disciplina.pontuacao();

        double ganhoPotencial =
                (100 - atual) * 0.12;

        double previsto =
                Math.min(100, atual + ganhoPotencial);

        return """
            Previsão

            Se mantiveres a rotina sugerida,
            %s pode evoluir de %.0f%% para %.0f%% nos próximos dias.
            """
            .formatted(
                    disciplina.disciplina(),
                    atual,
                    previsto
            );
    }

    private String gerarRisco(PlaneamentoEstudoDisciplina disciplina) {

        if (disciplina.diasSemEstudo() < 3) {
            return "Conteúdo estável.";
        }

        double perda =
                disciplina.diasSemEstudo() * 1.2;

        double previsto =
                Math.max(
                        0,
                        disciplina.pontuacao() - perda
                );

        return """
            Zona de atenção

            Sem revisão,
            %s pode cair de %.0f%% para %.0f%%.
            """
            .formatted(
                    disciplina.disciplina(),
                    disciplina.pontuacao(),
                    previsto
            );
    }
}
