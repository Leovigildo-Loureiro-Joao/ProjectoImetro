package com.imetro.util;

public class ConverterSegundoMinutos {
    public static String formatarDuracao(int duracaoSegundos) {
        int totalSegundos = Math.max(0, duracaoSegundos);
        int horas = totalSegundos / 3600;
        int minutos = (totalSegundos % 3600) / 60;
        int segundos = totalSegundos % 60;

        if (horas > 0) {
            return String.format("%dh %02dm", horas, minutos);
        }
        if (minutos > 0) {
            return String.format("%dmin %02ds", minutos, segundos);
        }
        return segundos + "s";
    }
}
