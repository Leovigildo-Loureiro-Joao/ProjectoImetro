package com.imetro.util;

import java.util.List;

import com.imetro.domain.dto.diagnostico.ProgressoResumo;

public class CalculoStats {
    public static double calcularVelocidade(int duracaoSegundos, int totalQuestoes) {
        if (duracaoSegundos <= 0 || totalQuestoes <= 0) {
            return 0.5d;
        }
        double mediaPorQuestao = duracaoSegundos / (double) totalQuestoes;
        double normalizado = 1d - (mediaPorQuestao / 120d);
        return Math.max(0d, Math.min(1d, normalizado));
    }

    public static double calcularPrecisao(int totalAcertos, int totalQuestoes) {
        return totalAcertos/totalQuestoes;
    }

    public static double calcularResiliencia(List<ProgressoResumo> list){
        //R = ((P_2 + P_3) / 2) / P_1
        double resiliecia=0;
        for (ProgressoResumo progressoResumo : list) {
            resiliecia+=progressoResumo.taxaAcertoGeral();
        }
        return resiliecia;
    }

    public static double calcularConsistencia(double consisteAntes,double consiste){
        //`Delta_P_n = P_(n+1) - P_n`
        return consiste-consisteAntes;
    }
}
