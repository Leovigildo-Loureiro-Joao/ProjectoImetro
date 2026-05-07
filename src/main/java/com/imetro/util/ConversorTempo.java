package com.imetro.util;

public class ConversorTempo {
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

    public static int parseTempoEmSegundos(String tempoFormatado) {
        if (tempoFormatado == null || tempoFormatado.isBlank()) {
            return 0;
        }

        String[] partes = tempoFormatado.split(":");
        try {
            return switch (partes.length) {
                case 3 -> (Integer.parseInt(partes[0]) * 3600)
                    + (Integer.parseInt(partes[1]) * 60)
                    + Integer.parseInt(partes[2]);
                case 2 -> (Integer.parseInt(partes[0]) * 60) + Integer.parseInt(partes[1]);
                default -> Integer.parseInt(tempoFormatado.trim());
            };
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
