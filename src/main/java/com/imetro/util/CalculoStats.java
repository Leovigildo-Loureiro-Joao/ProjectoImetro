package com.imetro.util;

import java.util.ArrayList;
import java.util.List;

import com.imetro.domain.dto.configuracao.ConfiguracaoDto;
import com.imetro.domain.dto.configuracao.ConfiguracaoTesteAdaptativoDto;
import com.imetro.domain.dto.configuracao.ConfiguracaoTesteAdaptativoNivelDto;
import com.imetro.domain.dto.diagnostico.ProgressoResumo;
import com.imetro.domain.dto.test.ReacaoTeste;
import com.imetro.ui.model.Questao;

public class CalculoStats {
    private static final double PRECISAO_RESPOSTA_CORRETA = 1d;
    private static final double PRECISAO_ALTERNATIVA_INCORRETA = 0.25d;
    private static final double PRECISAO_NAO_SEI = 0.10d;
    private static final double PRECISAO_CONFUSO = 0.20d;
    private static final double PRECISAO_PULAR = 0d;
    private static final ConfiguracaoTesteAdaptativoDto PADRAO_V1 = ConfiguracaoTesteAdaptativoDto.padrao(null);

    public static double calcularVelocidade(int duracaoSegundos, int totalQuestoes) {
        return calcularVelocidade(duracaoSegundos, totalQuestoes, (ConfiguracaoDto) null);
    }

    public static double calcularVelocidade(int duracaoSegundos, int totalQuestoes, ConfiguracaoDto configuracao) {
        if (duracaoSegundos <= 0 || totalQuestoes <= 0) {
            return 0.5d; // TODO CONFIG_ADAPTATIVA: fallback neutro de velocidade ainda fixo.
        }
        double mediaPorQuestao = duracaoSegundos / (double) totalQuestoes;
        double baseline = resolverBaselineVelocidade(configuracao);
        double normalizado = 1d - (mediaPorQuestao / baseline);
        return Math.max(0d, Math.min(1d, normalizado));
    }

    public static double calcularVelocidadePorQuestao(long duracaoSegundos, double tempoSugerido) {
        if (duracaoSegundos <= 0 || tempoSugerido <= 0) {
            return 0.5d; // TODO CONFIG_ADAPTATIVA: fallback neutro por questao ainda fixo.
        }
        double normalizado=duracaoSegundos/tempoSugerido;
        return Math.max(0d, Math.min(1d, normalizado));
    }

    public static double calcularLogica(
        List<Integer> indices,
        List<Questao> questoes,
        List<Character> respostasUsuario
    ) {
        int totalDesafiantes = 0;
        int acertosDesafiantes = 0;

        for (Integer indice : indices) {
            Questao questao = questoes.get(indice);
            if (questao == null || questao.getRigor() < 0.75d) {
                continue;
            }

            totalDesafiantes++;
            if (QuestaoUtil.respostaEstaCorreta(questao, respostasUsuario.get(indice))) {
                acertosDesafiantes++;
            }
        }

        return CalculoStats.calcularPrecisao(acertosDesafiantes, totalDesafiantes);
    }

    public static double calcularPrecisao(int totalAcertos, int totalQuestoes) {
        if (totalAcertos <= 0 || totalQuestoes <= 0) {
            return 0d;
        }
        return limitar01(totalAcertos / (double) totalQuestoes);
    }

    public static double calcularPrecisaoResposta(Questao questao, char respostaUsuario) {
        if (questao == null) {
            return 0d;
        }

        char respostaNormalizada = Character.toUpperCase(respostaUsuario);
        Double pesoConfigurado = questao.getPesoResposta(respostaNormalizada);
        if (pesoConfigurado != null) {
            return limitar01(pesoConfigurado);
        }

        return calcularPrecisaoRespostaFallback(questao, respostaNormalizada);
    }

    private static double calcularPrecisaoRespostaFallback(Questao questao, char respostaNormalizada) {
        char respostaCorreta = QuestaoUtil.resolverAlternativaCorreta(questao);
        if (respostaNormalizada == respostaCorreta) {
            return PRECISAO_RESPOSTA_CORRETA;
        }

        // Respostas de ajuda mantem progresso parcial; chute em alternativa errada vale mais
        // do que desistir da pergunta, mas menos do que acertar.
        return switch (respostaNormalizada) {
            case 'A', 'B', 'C', 'D' -> PRECISAO_ALTERNATIVA_INCORRETA;
            case 'E' -> PRECISAO_NAO_SEI;
            case 'F' -> PRECISAO_CONFUSO;
            case 'G' -> PRECISAO_PULAR;
            default -> 0d;
        };
    }

    public static double calcularPrecisaoResposta(ReacaoTeste reacao) {
        if (reacao == null) {
            return 0d;
        }
        return calcularPrecisaoResposta(reacao.questao(), reacao.respostaDada());
    }

    public static double calcularPrecisaoMediaRespostas(List<ReacaoTeste> reacoes) {
        if (reacoes == null || reacoes.isEmpty()) {
            return 0d;
        }

        double soma = 0d;
        int totalValidos = 0;
        for (ReacaoTeste reacao : reacoes) {
            if (reacao == null || reacao.questao() == null) {
                continue;
            }
            soma += calcularPrecisaoResposta(reacao);
            totalValidos++;
        }

        if (totalValidos == 0) {
            return 0d;
        }

        return limitar01(soma / totalValidos);
    }

    public static double calcularPrecisaoMedia(
        List<Integer> indices,
        List<Questao> questoes,
        List<Character> respostasUsuario
    ) {
        if (indices == null || indices.isEmpty() || questoes == null || respostasUsuario == null) {
            return 0d;
        }

        double soma = 0d;
        int totalValidos = 0;
        for (Integer indice : indices) {
            if (indice == null || indice < 0 || indice >= questoes.size() || indice >= respostasUsuario.size()) {
                continue;
            }

            Questao questao = questoes.get(indice);
            if (questao == null) {
                continue;
            }

            soma += calcularPrecisaoResposta(questao, respostasUsuario.get(indice));
            totalValidos++;
        }

        if (totalValidos == 0) {
            return 0d;
        }

        return limitar01(soma / totalValidos);
    }

    public static double calcularResiliencia(List<ProgressoResumo> list){
        if (list == null || list.isEmpty()) {
            return 0d;
        }

        double resiliencia = 0d;
        int totalValidos = 0;
        for (ProgressoResumo progressoResumo : list) {
            if (progressoResumo == null || progressoResumo.taxaAcertoGeral() == null) {
                continue;
            }
            resiliencia += limitar01(progressoResumo.taxaAcertoGeral());
            totalValidos++;
        }
        if (totalValidos == 0) {
            return 0d;
        }
        return limitar01(resiliencia / totalValidos);
    }

    public static double calcularConsistencia(Double percentualAnterior, double percentualAtual){
        if (percentualAnterior == null) {
            return 0d;
        }

        double anteriorNormalizado = normalizarPercentual(percentualAnterior);
        double atualNormalizado = normalizarPercentual(percentualAtual);
        double variacao = Math.abs(atualNormalizado - anteriorNormalizado);
        return limitar01(1d - variacao);
    }

    public static double calcularConsistenciaTeste(List<ReacaoTeste> reacoes) {
        return calcularConsistenciaTeste(reacoes, (ConfiguracaoTesteAdaptativoDto) null);
    }

    public static double calcularConsistenciaTeste(List<ReacaoTeste> reacoes, ConfiguracaoTesteAdaptativoDto adaptacao) {
        if (reacoes == null || reacoes.isEmpty()) {
            return 0d;
        }

        List<Double> acertos = new ArrayList<>();
        List<Double> ritmos = new ArrayList<>();
        int janelaConsistencia = resolverJanelaConsistencia(adaptacao);

        for (ReacaoTeste reacao : ultimasReacoesValidas(reacoes, janelaConsistencia)) {
            if (reacao == null || reacao.questao() == null) {
                continue;
            }

            acertos.add(acertou(reacao) ? 1d : 0d);

            Double ritmoNormalizado = calcularRitmoNormalizado(reacao);
            if (ritmoNormalizado != null) {
                ritmos.add(ritmoNormalizado);
            }
        }

        if (acertos.isEmpty()) {
            return 0d;
        }

        double estabilidadeAcertos = 1d - Math.min(1d, calcularDesvioPadrao(acertos) / 0.5d); // TODO CONFIG_ADAPTATIVA: desvio-base de acertos ainda fixo.
        double estabilidadeRitmo = ritmos.size() < 2
            ? 0.5d // TODO CONFIG_ADAPTATIVA: fallback de estabilidade de ritmo ainda fixo.
            : 1d - Math.min(1d, calcularCoeficienteVariacao(ritmos));

        double pesoAcerto = resolverPesoConsistenciaAcerto(adaptacao);
        double pesoRitmo = resolverPesoConsistenciaRitmo(adaptacao);
        double score = (estabilidadeAcertos * pesoAcerto) + (estabilidadeRitmo * pesoRitmo);
        return suavizarPorAmostra(score, acertos.size(), resolverAmostrasConfiancaPlena(adaptacao));
    }

    public static double calcularConsistenciaQuestao(List<ReacaoTeste> historico, ReacaoTeste atual) {
        return calcularConsistenciaQuestao(historico, atual, (ConfiguracaoTesteAdaptativoDto) null);
    }

    public static double calcularConsistenciaQuestao(List<ReacaoTeste> historico, ReacaoTeste atual, ConfiguracaoTesteAdaptativoDto adaptacao) {
        if (atual == null || atual.questao() == null) {
            return 0d;
        }

        List<ReacaoTeste> janela = ultimasReacoesValidas(historico, resolverJanelaConsistencia(adaptacao));
        if (janela.isEmpty()) {
            return 0.5d; // TODO CONFIG_ADAPTATIVA: fallback de consistencia sem historico ainda fixo.
        }

        double mediaAcertos = janela.stream()
            .mapToDouble(reacao -> acertou(reacao) ? 1d : 0d)
            .average()
            .orElse(0.5d); // TODO CONFIG_ADAPTATIVA: media neutra de acertos ainda fixa.

        double desvioAcerto = Math.abs((acertou(atual) ? 1d : 0d) - mediaAcertos);

        Double ritmoAtual = calcularRitmoNormalizado(atual);
        List<Double> ritmosHistoricos = janela.stream()
            .map(CalculoStats::calcularRitmoNormalizado)
            .filter(java.util.Objects::nonNull)
            .toList();

        double desvioRitmo = 0.5d; // TODO CONFIG_ADAPTATIVA: desvio-base de ritmo ainda fixo.
        if (ritmoAtual != null && !ritmosHistoricos.isEmpty()) {
            double mediaRitmo = ritmosHistoricos.stream().mapToDouble(Double::doubleValue).average().orElse(1d);
            desvioRitmo = Math.min(1d, Math.abs(ritmoAtual - mediaRitmo));
        }

        double pesoAcerto = resolverPesoConsistenciaAcerto(adaptacao);
        double pesoRitmo = resolverPesoConsistenciaRitmo(adaptacao);
        double score = ((1d - desvioAcerto) * pesoAcerto) + ((1d - desvioRitmo) * pesoRitmo);
        return limitar01(score);
    }

    public static double calcularResilienciaTeste(List<ReacaoTeste> reacoes) {
        return calcularResilienciaTeste(reacoes, (ConfiguracaoTesteAdaptativoDto) null);
    }

    public static double calcularResilienciaTeste(List<ReacaoTeste> reacoes, ConfiguracaoTesteAdaptativoDto adaptacao) {
        if (reacoes == null || reacoes.isEmpty()) {
            return 0d;
        }

        int totalValidos = 0;
        int eventosAdversos = 0;
        int recuperacoes = 0;
        int maiorSequenciaErros = 0;
        int sequenciaErros = 0;
        int janelaRecuperacao = resolverJanelaRecuperacao(adaptacao);
        double tempoLentoFator = resolverTempoLentoFator(adaptacao);

        for (int i = 0; i < reacoes.size(); i++) {
            ReacaoTeste reacao = reacoes.get(i);
            if (reacao == null || reacao.questao() == null) {
                continue;
            }

            totalValidos++;

            if (acertou(reacao)) {
                sequenciaErros = 0;
            } else {
                sequenciaErros++;
                maiorSequenciaErros = Math.max(maiorSequenciaErros, sequenciaErros);
            }

            if (!ehEventoAdverso(reacao, tempoLentoFator)) {
                continue;
            }

            eventosAdversos++;
            if (recuperouNasProximasTentativas(reacoes, i, janelaRecuperacao, resolverTempoRecuperacaoFator(adaptacao))) {
                recuperacoes++;
            }
        }

        if (totalValidos == 0) {
            return 0d;
        }

        double taxaRecuperacao = eventosAdversos == 0
            ? resolverPesoResilienciaRecuperacao(adaptacao)
            : recuperacoes / (double) eventosAdversos;
        double estabilidadeAposQueda = 1d - (maiorSequenciaErros / (double) totalValidos);
        double score = (taxaRecuperacao * resolverPesoResilienciaRecuperacao(adaptacao))
            + (estabilidadeAposQueda * resolverPesoResilienciaEstabilidade(adaptacao));
        return suavizarPorAmostra(score, totalValidos, resolverAmostrasConfiancaPlena(adaptacao));
    }

    public static double calcularResilienciaQuestao(List<ReacaoTeste> historico, ReacaoTeste atual) {
        return calcularResilienciaQuestao(historico, atual, (ConfiguracaoTesteAdaptativoDto) null);
    }

    public static double calcularResilienciaQuestao(List<ReacaoTeste> historico, ReacaoTeste atual, ConfiguracaoTesteAdaptativoDto adaptacao) {
        if (atual == null || atual.questao() == null) {
            return 0d;
        }

        ReacaoTeste ultimaReacao = ultimaReacaoValida(historico);
        if (ultimaReacao == null) {
            return 0.5d; // TODO CONFIG_ADAPTATIVA: fallback neutro sem reacao anterior ainda fixo.
        }

        double tempoLentoFator = resolverTempoLentoFator(adaptacao);
        if (!ehEventoAdverso(ultimaReacao, tempoLentoFator)) {
            return 0.5d; // TODO CONFIG_ADAPTATIVA: fallback neutro sem evento adverso ainda fixo.
        }

        int adversidadesConsecutivas = contarAdversidadesConsecutivas(historico, tempoLentoFator);
        double score = resolverResilienciaQuestaoBase(adaptacao);

        if (acertou(atual)) {
            score += resolverResilienciaQuestaoBonusAcerto(adaptacao);
        }

        if (!foiMuitoLento(atual, resolverTempoRecuperacaoFator(adaptacao))) {
            score += resolverResilienciaQuestaoBonusRitmo(adaptacao);
        }

        if (adversidadesConsecutivas >= resolverJanelaRecuperacao(adaptacao) && acertou(atual)) {
            score += resolverResilienciaQuestaoBonusRecuperacao(adaptacao);
        }

        return limitar01(score);
    }

    private static double normalizarPercentual(double percentual) {
        return limitar01(percentual / 100d);
    }

    private static double limitar01(double valor) {
        return Math.max(0d, Math.min(1d, valor));
    }

    public static double calcularNovoRigor(double rigorAtual, double rigorAlvo, double rigorMedioTentado, double taxaAcerto) {
        return calcularNovoRigor(rigorAtual, rigorAlvo, rigorMedioTentado, taxaAcerto, null);
    }

    public static double calcularNovoRigor(
        double rigorAtual,
        double rigorAlvo,
        double rigorMedioTentado,
        double taxaAcerto,
        ConfiguracaoTesteAdaptativoNivelDto config
    ) {
        double base = Math.max(rigorAtual, rigorMedioTentado);
        double limiarAcertoForte = config == null ? 0.85d : limitar01(config.limiar_acerto());
        double limiarErroForte = config == null ? 0.35d : limitar01(config.limiar_erro());
        double limiarAcertoSuave = config == null
            ? 0.65d
            : Math.max(limiarErroForte, (limiarAcertoForte + limiarErroForte) / 2d);
        double piso = config == null
            ? 0.05d
            : Math.max(0.05d, limitar01(config.limiteInferior()));

        if (taxaAcerto >= limiarAcertoForte) {
            return Math.min(rigorAlvo, base + 0.08d);
        }
        if (taxaAcerto >= limiarAcertoSuave) {
            return Math.min(rigorAlvo, base + 0.03d);
        }
        if (taxaAcerto <= limiarErroForte) {
            return Math.max(piso, Math.min(rigorAtual, rigorMedioTentado) - 0.08d);
        }
        return Math.max(piso, Math.min(rigorAtual, rigorMedioTentado) - 0.03d);
    }

    public static double limitarRigor(double rigor) {
        return Math.max(0d, Math.min(1d, rigor));
    }

    private static boolean acertou(ReacaoTeste reacao) {
        if (reacao == null || reacao.questao() == null) {
            return false;
        }

        return Character.toUpperCase(reacao.respostaDada())
            == Character.toUpperCase(reacao.questao().getRespostaCorreta());
    }

    private static Double calcularRitmoNormalizado(ReacaoTeste reacao) {
        if (reacao == null || reacao.questao() == null || reacao.questao().getTempoSugerido() <= 0d) {
            return null;
        }

        double tempoResposta = normalizarTempoResposta(reacao.tempoSegundos(), reacao.questao().getTempoSugerido());
        if (tempoResposta <= 0d) {
            return null;
        }

        return tempoResposta / reacao.questao().getTempoSugerido();
    }

    private static double normalizarTempoResposta(long valorBruto, double tempoSugeridoSegundos) {
        if (valorBruto <= 0) {
            return 0d;
        }

        if (tempoSugeridoSegundos > 0d && valorBruto > 1000L && valorBruto > (tempoSugeridoSegundos * 8d)) {
            return valorBruto / 1000d;
        }

        return valorBruto;
    }

    private static double calcularDesvioPadrao(List<Double> valores) {
        if (valores == null || valores.isEmpty()) {
            return 0d;
        }

        double media = valores.stream().mapToDouble(Double::doubleValue).average().orElse(0d);
        double variancia = valores.stream()
            .mapToDouble(valor -> Math.pow(valor - media, 2))
            .average()
            .orElse(0d);
        return Math.sqrt(variancia);
    }

    private static double calcularCoeficienteVariacao(List<Double> valores) {
        if (valores == null || valores.size() < 2) {
            return 0d;
        }

        double media = valores.stream().mapToDouble(Double::doubleValue).average().orElse(0d);
        if (media <= 0d) {
            return 0d;
        }

        return calcularDesvioPadrao(valores) / media;
    }

    private static double suavizarPorAmostra(double score, int totalAmostras) {
        return suavizarPorAmostra(score, totalAmostras, resolverAmostrasConfiancaPlena(null));
    }

    private static double suavizarPorAmostra(double score, int totalAmostras, int amostrasConfiancaPlena) {
        if (totalAmostras <= 0) {
            return 0d;
        }

        double confianca = Math.min(1d, totalAmostras / (double) Math.max(1, amostrasConfiancaPlena));
        return limitar01((0.5d * (1d - confianca)) + (limitar01(score) * confianca)); // TODO CONFIG_ADAPTATIVA: blending com score neutro ainda fixo.
    }

    private static boolean ehEventoAdverso(ReacaoTeste reacao) {
        return ehEventoAdverso(reacao, resolverTempoLentoFator(null));
    }

    private static boolean ehEventoAdverso(ReacaoTeste reacao, double tempoLentoFator) {
        return reacao != null && (!acertou(reacao) || foiMuitoLento(reacao, tempoLentoFator));
    }

    private static boolean foiMuitoLento(ReacaoTeste reacao, double tolerancia) {
        Double ritmo = calcularRitmoNormalizado(reacao);
        return ritmo != null && ritmo > tolerancia;
    }

    private static boolean recuperouNasProximasTentativas(List<ReacaoTeste> reacoes, int indiceBase, int alcance) {
        return recuperouNasProximasTentativas(reacoes, indiceBase, alcance, resolverTempoRecuperacaoFator(null));
    }

    private static boolean recuperouNasProximasTentativas(List<ReacaoTeste> reacoes, int indiceBase, int alcance, double tempoRecuperacaoFator) {
        int limite = Math.min(reacoes.size() - 1, indiceBase + alcance);
        for (int i = indiceBase + 1; i <= limite; i++) {
            ReacaoTeste proxima = reacoes.get(i);
            if (proxima == null || proxima.questao() == null) {
                continue;
            }

            if (acertou(proxima) && !foiMuitoLento(proxima, tempoRecuperacaoFator)) {
                return true;
            }
        }
        return false;
    }

    private static int resolverJanelaConsistencia(ConfiguracaoTesteAdaptativoDto adaptacao) {
        if (adaptacao == null) {
            return PADRAO_V1.janelaConsistencia();
        }
        if (adaptacao.janelaConsistencia() <= 0) {
            return PADRAO_V1.janelaConsistencia();
        }
        return adaptacao.janelaConsistencia();
    }

    private static int resolverJanelaRecuperacao(ConfiguracaoTesteAdaptativoDto adaptacao) {
        if (adaptacao == null) {
            return PADRAO_V1.janelaRecuperacao();
        }
        if (adaptacao.janelaRecuperacao() <= 0) {
            return PADRAO_V1.janelaRecuperacao();
        }
        return adaptacao.janelaRecuperacao();
    }

    private static int resolverAmostrasConfiancaPlena(ConfiguracaoTesteAdaptativoDto adaptacao) {
        return Math.max(1, resolverJanelaConsistencia(adaptacao) + resolverJanelaRecuperacao(adaptacao));
    }

    private static double resolverPesoConsistenciaAcerto(ConfiguracaoTesteAdaptativoDto adaptacao) {
        double pesoAcerto = adaptacao == null ? PADRAO_V1.pesoConsistenciaAcerto() : adaptacao.pesoConsistenciaAcerto();
        double pesoRitmo = adaptacao == null ? PADRAO_V1.pesoConsistenciaRitmo() : adaptacao.pesoConsistenciaRitmo();
        return normalizarPesos(pesoAcerto, pesoRitmo)[0];
    }

    private static double resolverPesoConsistenciaRitmo(ConfiguracaoTesteAdaptativoDto adaptacao) {
        double pesoAcerto = adaptacao == null ? PADRAO_V1.pesoConsistenciaAcerto() : adaptacao.pesoConsistenciaAcerto();
        double pesoRitmo = adaptacao == null ? PADRAO_V1.pesoConsistenciaRitmo() : adaptacao.pesoConsistenciaRitmo();
        return normalizarPesos(pesoAcerto, pesoRitmo)[1];
    }

    private static double resolverPesoResilienciaRecuperacao(ConfiguracaoTesteAdaptativoDto adaptacao) {
        double pesoRecuperacao = adaptacao == null ? PADRAO_V1.pesoResilienciaRecuperacao() : adaptacao.pesoResilienciaRecuperacao();
        double pesoEstabilidade = adaptacao == null ? PADRAO_V1.pesoResilienciaEstabilidade() : adaptacao.pesoResilienciaEstabilidade();
        return normalizarPesos(pesoRecuperacao, pesoEstabilidade)[0];
    }

    private static double resolverPesoResilienciaEstabilidade(ConfiguracaoTesteAdaptativoDto adaptacao) {
        double pesoRecuperacao = adaptacao == null ? PADRAO_V1.pesoResilienciaRecuperacao() : adaptacao.pesoResilienciaRecuperacao();
        double pesoEstabilidade = adaptacao == null ? PADRAO_V1.pesoResilienciaEstabilidade() : adaptacao.pesoResilienciaEstabilidade();
        return normalizarPesos(pesoRecuperacao, pesoEstabilidade)[1];
    }

    private static double resolverTempoLentoFator(ConfiguracaoTesteAdaptativoDto adaptacao) {
        if (adaptacao == null) {
            return PADRAO_V1.tempoLentoFator();
        }
        if (adaptacao.tempoLentoFator() <= 0d) {
            return PADRAO_V1.tempoLentoFator();
        }
        return adaptacao.tempoLentoFator();
    }

    private static double resolverTempoRecuperacaoFator(ConfiguracaoTesteAdaptativoDto adaptacao) {
        if (adaptacao == null) {
            return PADRAO_V1.tempoRecuperacaoFator();
        }
        if (adaptacao.tempoRecuperacaoFator() <= 0d) {
            return PADRAO_V1.tempoRecuperacaoFator();
        }
        return adaptacao.tempoRecuperacaoFator();
    }

    private static double resolverResilienciaQuestaoBase(ConfiguracaoTesteAdaptativoDto adaptacao) {
        if (adaptacao == null) {
            return PADRAO_V1.resilienciaQuestaoBase();
        }
        return Math.max(0d, Math.min(1d, adaptacao.resilienciaQuestaoBase()));
    }

    private static double resolverResilienciaQuestaoBonusAcerto(ConfiguracaoTesteAdaptativoDto adaptacao) {
        if (adaptacao == null) {
            return PADRAO_V1.resilienciaQuestaoBonusAcerto();
        }
        return Math.max(0d, Math.min(1d, adaptacao.resilienciaQuestaoBonusAcerto()));
    }

    private static double resolverResilienciaQuestaoBonusRitmo(ConfiguracaoTesteAdaptativoDto adaptacao) {
        if (adaptacao == null) {
            return PADRAO_V1.resilienciaQuestaoBonusRitmo();
        }
        return Math.max(0d, Math.min(1d, adaptacao.resilienciaQuestaoBonusRitmo()));
    }

    private static double resolverResilienciaQuestaoBonusRecuperacao(ConfiguracaoTesteAdaptativoDto adaptacao) {
        if (adaptacao == null) {
            return PADRAO_V1.resilienciaQuestaoBonusRecuperacao();
        }
        return Math.max(0d, Math.min(1d, adaptacao.resilienciaQuestaoBonusRecuperacao()));
    }

    private static double resolverBaselineVelocidade(ConfiguracaoDto configuracao) {
        if (configuracao == null || configuracao.velocidade_segundos_por_percent() == null
            || configuracao.velocidade_segundos_por_percent() <= 0) {
            return 120d;
        }
        return configuracao.velocidade_segundos_por_percent();
    }

    private static double[] normalizarPesos(double primeiro, double segundo) {
        double pesoPrimeiro = Double.isFinite(primeiro) ? Math.max(0d, primeiro) : 0d;
        double pesoSegundo = Double.isFinite(segundo) ? Math.max(0d, segundo) : 0d;
        double soma = pesoPrimeiro + pesoSegundo;
        if (soma <= 0d) {
            return new double[] { 0.7d, 0.3d };
        }
        return new double[] { pesoPrimeiro / soma, pesoSegundo / soma };
    }

    private static List<ReacaoTeste> ultimasReacoesValidas(List<ReacaoTeste> historico, int limite) {
        List<ReacaoTeste> valores = new ArrayList<>();
        if (historico == null || historico.isEmpty() || limite <= 0) {
            return valores;
        }

        for (int i = historico.size() - 1; i >= 0 && valores.size() < limite; i--) {
            ReacaoTeste reacao = historico.get(i);
            if (reacao != null && reacao.questao() != null) {
                valores.addFirst(reacao);
            }
        }

        return valores;
    }

    private static ReacaoTeste ultimaReacaoValida(List<ReacaoTeste> historico) {
        if (historico == null || historico.isEmpty()) {
            return null;
        }

        for (int i = historico.size() - 1; i >= 0; i--) {
            ReacaoTeste reacao = historico.get(i);
            if (reacao != null && reacao.questao() != null) {
                return reacao;
            }
        }
        return null;
    }

    private static int contarAdversidadesConsecutivas(List<ReacaoTeste> historico) {
        return contarAdversidadesConsecutivas(historico, resolverTempoLentoFator(null));
    }

    private static int contarAdversidadesConsecutivas(List<ReacaoTeste> historico, double tempoLentoFator) {
        if (historico == null || historico.isEmpty()) {
            return 0;
        }

        int total = 0;
        for (int i = historico.size() - 1; i >= 0; i--) {
            ReacaoTeste reacao = historico.get(i);
            if (reacao == null || reacao.questao() == null) {
                continue;
            }
            if (!ehEventoAdverso(reacao, tempoLentoFator)) {
                break;
            }
            total++;
        }
        return total;
    }
}
