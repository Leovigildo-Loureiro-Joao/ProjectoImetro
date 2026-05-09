package com.imetro.util;

import java.util.List;

import com.imetro.domain.dto.diagnostico.ProgressoResumo;
import com.imetro.ui.model.Questao;

public class CalculoStats {
    public static double calcularVelocidade(int duracaoSegundos, int totalQuestoes) {
        if (duracaoSegundos <= 0 || totalQuestoes <= 0) {
            return 0.5d;
        }
        double mediaPorQuestao = duracaoSegundos / (double) totalQuestoes;
        double normalizado = 1d - (mediaPorQuestao / 120d);
        return Math.max(0d, Math.min(1d, normalizado));
    }

    public static double calcularVelocidadePorQuestao(int duracaoSegundos, double tempoSugerido) {
        if (duracaoSegundos <= 0 || tempoSugerido <= 0) {
            return 0.5d;
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

    private static double normalizarPercentual(double percentual) {
        return limitar01(percentual / 100d);
    }

    private static double limitar01(double valor) {
        return Math.max(0d, Math.min(1d, valor));
    }

    public static double calcularNovoRigor(double rigorAtual, double rigorAlvo, double rigorMedioTentado, double taxaAcerto) {
        double base = Math.max(rigorAtual, rigorMedioTentado);
        if (taxaAcerto >= 0.85d) {
            return Math.min(rigorAlvo, base + 0.08d);
        }
        if (taxaAcerto >= 0.65d) {
            return Math.min(rigorAlvo, base + 0.03d);
        }
        if (taxaAcerto <= 0.35d) {
            return Math.max(0.05d, Math.min(rigorAtual, rigorMedioTentado) - 0.08d);
        }
        return Math.max(0.05d, Math.min(rigorAtual, rigorMedioTentado) - 0.03d);
    }

    public static double limitarRigor(double rigor) {
        return Math.max(0d, Math.min(1d, rigor));
    }
}
