package com.imetro.util;

import java.util.ArrayList;
import java.util.List;

import com.imetro.domain.dto.diagnostico.ProgressoResumo;
import com.imetro.domain.dto.test.ReacaoTeste;
import com.imetro.ui.model.Questao;

public class CalculoStats {
    public static double calcularVelocidade(int duracaoSegundos, int totalQuestoes) {
        if (duracaoSegundos <= 0 || totalQuestoes <= 0) {
            return 0.5d; // TODO CONFIG_ADAPTATIVA: fallback neutro de velocidade ainda fixo.
        }
        double mediaPorQuestao = duracaoSegundos / (double) totalQuestoes;
        double normalizado = 1d - (mediaPorQuestao / 120d); // TODO CONFIG_ADAPTATIVA: baseline de velocidade ainda fixa em 120s por questao.
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
            if (respostasUsuario.get(indice) == questao.getRespostaCorreta()) {
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
        if (reacoes == null || reacoes.isEmpty()) {
            return 0d;
        }

        List<Double> acertos = new ArrayList<>();
        List<Double> ritmos = new ArrayList<>();

        for (ReacaoTeste reacao : reacoes) {
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

        double score = (estabilidadeAcertos * 0.7d) + (estabilidadeRitmo * 0.3d); // TODO CONFIG_ADAPTATIVA: pesos de consistencia ainda fixos.
        return suavizarPorAmostra(score, acertos.size());
    }

    public static double calcularConsistenciaQuestao(List<ReacaoTeste> historico, ReacaoTeste atual) {
        if (atual == null || atual.questao() == null) {
            return 0d;
        }

        List<ReacaoTeste> janela = ultimasReacoesValidas(historico, 3); // TODO CONFIG_ADAPTATIVA: janela de consistencia ainda fixa em 3 reacoes.
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

        double score = ((1d - desvioAcerto) * 0.65d) + ((1d - desvioRitmo) * 0.35d); // TODO CONFIG_ADAPTATIVA: pesos finos da consistencia ainda fixos.
        return limitar01(score);
    }

    public static double calcularResilienciaTeste(List<ReacaoTeste> reacoes) {
        if (reacoes == null || reacoes.isEmpty()) {
            return 0d;
        }

        int totalValidos = 0;
        int eventosAdversos = 0;
        int recuperacoes = 0;
        int maiorSequenciaErros = 0;
        int sequenciaErros = 0;

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

            if (!ehEventoAdverso(reacao)) {
                continue;
            }

            eventosAdversos++;
            if (recuperouNasProximasTentativas(reacoes, i, 2)) {
                recuperacoes++;
            }
        }

        if (totalValidos == 0) {
            return 0d;
        }

        double taxaRecuperacao = eventosAdversos == 0 ? 0.65d : recuperacoes / (double) eventosAdversos; // TODO CONFIG_ADAPTATIVA: fallback de recuperacao ainda fixo.
        double estabilidadeAposQueda = 1d - (maiorSequenciaErros / (double) totalValidos);
        double score = (taxaRecuperacao * 0.7d) + (estabilidadeAposQueda * 0.3d); // TODO CONFIG_ADAPTATIVA: pesos agregados de resiliencia ainda fixos.
        return suavizarPorAmostra(score, totalValidos);
    }

    public static double calcularResilienciaQuestao(List<ReacaoTeste> historico, ReacaoTeste atual) {
        if (atual == null || atual.questao() == null) {
            return 0d;
        }

        ReacaoTeste ultimaReacao = ultimaReacaoValida(historico);
        if (ultimaReacao == null) {
            return 0.5d; // TODO CONFIG_ADAPTATIVA: fallback neutro sem reacao anterior ainda fixo.
        }

        if (!ehEventoAdverso(ultimaReacao)) {
            return 0.5d; // TODO CONFIG_ADAPTATIVA: fallback neutro sem evento adverso ainda fixo.
        }

        int adversidadesConsecutivas = contarAdversidadesConsecutivas(historico);
        double score = 0.1d; // TODO CONFIG_ADAPTATIVA: base minima de resiliencia por questao ainda fixa.

        if (acertou(atual)) {
            score += 0.65d; // TODO CONFIG_ADAPTATIVA: peso de recuperacao correta ainda fixo.
        }

        if (!foiMuitoLento(atual, 1.10d)) { // TODO CONFIG_ADAPTATIVA: `tempo_recuperacao_fator` ainda fixo em 1.10.
            score += 0.15d; // TODO CONFIG_ADAPTATIVA: peso de estabilidade de ritmo ainda fixo.
        }

        if (adversidadesConsecutivas >= 2 && acertou(atual)) {
            score += 0.10d; // TODO CONFIG_ADAPTATIVA: bonus de recuperacao apos adversidade ainda fixo.
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
        double base = Math.max(rigorAtual, rigorMedioTentado);
        if (taxaAcerto >= 0.85d) { // TODO CONFIG_ADAPTATIVA: limiar de subida forte ainda fixo.
            return Math.min(rigorAlvo, base + 0.08d); // TODO CONFIG_ADAPTATIVA: delta de subida forte ainda fixo.
        }
        if (taxaAcerto >= 0.65d) { // TODO CONFIG_ADAPTATIVA: limiar de subida suave ainda fixo.
            return Math.min(rigorAlvo, base + 0.03d); // TODO CONFIG_ADAPTATIVA: delta de subida suave ainda fixo.
        }
        if (taxaAcerto <= 0.35d) { // TODO CONFIG_ADAPTATIVA: limiar de descida forte ainda fixo.
            return Math.max(0.05d, Math.min(rigorAtual, rigorMedioTentado) - 0.08d); // TODO CONFIG_ADAPTATIVA: piso e delta de descida forte ainda fixos.
        }
        return Math.max(0.05d, Math.min(rigorAtual, rigorMedioTentado) - 0.03d); // TODO CONFIG_ADAPTATIVA: piso e delta de descida suave ainda fixos.
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
        if (totalAmostras <= 0) {
            return 0d;
        }

        double confianca = Math.min(1d, totalAmostras / 5d); // TODO CONFIG_ADAPTATIVA: amostra minima para confianca plena ainda fixa em 5.
        return limitar01((0.5d * (1d - confianca)) + (limitar01(score) * confianca)); // TODO CONFIG_ADAPTATIVA: blending com score neutro ainda fixo.
    }

    private static boolean ehEventoAdverso(ReacaoTeste reacao) {
        return reacao != null && (!acertou(reacao) || foiMuitoLento(reacao, 1.25d)); // TODO CONFIG_ADAPTATIVA: `tempo_lento_fator` ainda fixo em 1.25.
    }

    private static boolean foiMuitoLento(ReacaoTeste reacao, double tolerancia) {
        Double ritmo = calcularRitmoNormalizado(reacao);
        return ritmo != null && ritmo > tolerancia;
    }

    private static boolean recuperouNasProximasTentativas(List<ReacaoTeste> reacoes, int indiceBase, int alcance) {
        int limite = Math.min(reacoes.size() - 1, indiceBase + alcance);
        for (int i = indiceBase + 1; i <= limite; i++) {
            ReacaoTeste proxima = reacoes.get(i);
            if (proxima == null || proxima.questao() == null) {
                continue;
            }

            if (acertou(proxima) && !foiMuitoLento(proxima, 1.10d)) { // TODO CONFIG_ADAPTATIVA: tolerancia de recuperacao ainda fixa em 1.10.
                return true;
            }
        }
        return false;
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
        if (historico == null || historico.isEmpty()) {
            return 0;
        }

        int total = 0;
        for (int i = historico.size() - 1; i >= 0; i--) {
            ReacaoTeste reacao = historico.get(i);
            if (reacao == null || reacao.questao() == null) {
                continue;
            }
            if (!ehEventoAdverso(reacao)) {
                break;
            }
            total++;
        }
        return total;
    }
}
