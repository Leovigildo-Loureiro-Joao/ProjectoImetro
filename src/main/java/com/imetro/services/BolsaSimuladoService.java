package com.imetro.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.imetro.domain.dto.bolsa.BolsaDto;
import com.imetro.ui.model.Questao;
import com.imetro.util.QuestaoUtil;
import com.imetro.util.TextoUtil;

public class BolsaSimuladoService {

    private final TesteAdaptativoService testeAdaptativoService;

    public BolsaSimuladoService() {
        this.testeAdaptativoService = new TesteAdaptativoService();
    }

    public List<Questao> carregarQuestoesBolsa(BolsaDto bolsa, int limite) {
        String disciplina = resolverDisciplinaFoco(bolsa);
        int quantidade = Math.max(6, limite);

        List<Questao> questoes = testeAdaptativoService.carregarQuestoesDisponiveis(
            disciplina,
            List.of(),
            List.of()
        );

        ArrayList<Questao> elegiveis = questoes.stream()
            .filter(this::aceitaRespostaPorTexto)
            .sorted(Comparator
                .comparingDouble(Questao::getRigor).reversed()
                .thenComparing(Questao::getNivelDificuldade, Comparator.reverseOrder())
                .thenComparing(Questao::getTopico, String.CASE_INSENSITIVE_ORDER))
            .collect(java.util.stream.Collectors.toCollection(ArrayList::new));

        ArrayList<Questao> selecionadas = new ArrayList<>();
        Set<String> subtopicosUsados = new LinkedHashSet<>();

        for (Questao questao : elegiveis) {
            String chave = TextoUtil.normalizarMaiusculo(
                questao.getDisciplina() + ":" + QuestaoUtil.safeText(questao.getSubtopico(), questao.getTopico())
            );
            if (subtopicosUsados.add(chave)) {
                selecionadas.add(questao);
            }
            if (selecionadas.size() >= quantidade) {
                return List.copyOf(selecionadas);
            }
        }

        for (Questao questao : elegiveis) {
            if (selecionadas.contains(questao)) {
                continue;
            }
            selecionadas.add(questao);
            if (selecionadas.size() >= quantidade) {
                break;
            }
        }

        return List.copyOf(selecionadas);
    }

    public int resolverQuantidadeQuestoes(BolsaDto bolsa) {
        int duracao = bolsa == null || bolsa.duracaoMinutos() == null ? 45 : bolsa.duracaoMinutos();
        return Math.max(6, Math.min(16, Math.round(duracao / 4f)));
    }

    public char resolverRespostaDigitada(Questao questao, String respostaDigitada) {
        if (questao == null || respostaDigitada == null) {
            return '\0';
        }

        String limpa = respostaDigitada.trim();
        if (limpa.isBlank()) {
            return '\0';
        }

        String textoNormalizado = normalizarRespostaDigitada(limpa);
        if (textoNormalizado.length() == 1) {
            char letra = Character.toUpperCase(textoNormalizado.charAt(0));
            if (letra >= 'A' && letra <= 'D') {
                return letra;
            }
        }

        for (char letra = 'A'; letra <= 'D'; letra++) {
            String textoOpcao = QuestaoUtil.resolverTextoOpcao(questao, letra);
            if (textoNormalizado.equals(normalizarRespostaDigitada(textoOpcao))) {
                return letra;
            }
        }

        return '\0';
    }

    public boolean aceitaRespostaPorTexto(Questao questao) {
        if (questao == null) {
            return false;
        }

        if (isBlank(questao.getEnunciado())
            || isBlank(questao.getOpcaoA())
            || isBlank(questao.getOpcaoB())
            || isBlank(questao.getOpcaoC())
            || isBlank(questao.getOpcaoD())
            || questao.isUsaGrafico()) {
            return false;
        }

        return isFallbackOption(questao.getOpcaoE(), "nao sei")
            && isFallbackOption(questao.getOpcaoF(), "estou em duvida")
            && isFallbackOption(questao.getOpcaoG(), "prefiro pular");
    }

    public String resolverDisciplinaFoco(BolsaDto bolsa) {
        if (bolsa == null || bolsa.disciplinaFoco() == null || bolsa.disciplinaFoco().isBlank()) {
            return "";
        }
        return bolsa.disciplinaFoco();
    }

    private boolean isFallbackOption(String value, String expected) {
        if (value == null || value.isBlank()) {
            return true;
        }
        return TextoUtil.normalizarMinusculo(value).equals(expected);
    }

    private String normalizarRespostaDigitada(String value) {
        return TextoUtil.normalizarMinusculo(value)
            .replaceAll("[^a-z0-9]+", " ")
            .trim()
            .replaceAll("\\s+", " ");
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
