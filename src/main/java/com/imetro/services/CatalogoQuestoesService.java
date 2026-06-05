package com.imetro.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.imetro.domain.dto.Topico;
import com.imetro.ui.model.Questao;
import com.imetro.util.TextoUtil;

public class CatalogoQuestoesService {

    private static final List<Questao> BANCO = criarBanco();

    public List<Questao> carregarQuestoes() {
        return BANCO.stream().map(this::copiarQuestao).collect(Collectors.toCollection(ArrayList::new));
    }

    public List<String> carregarDisciplinasDisponiveis() {
        LinkedHashSet<String> disciplinas = new LinkedHashSet<>();
        for (Questao questao : BANCO) {
            disciplinas.add(questao.getDisciplina());
        }
        return new ArrayList<>(disciplinas);
    }

    public List<Topico> carregarTopicosPorDisciplina(String disciplina) {
        Map<String, LinkedHashSet<String>> grupos = new LinkedHashMap<>();
        String disciplinaNormalizada = TextoUtil.normalizarMaiusculo(disciplina);

        for (Questao questao : BANCO) {
            if (!disciplinaNormalizada.equals(TextoUtil.normalizarMaiusculo(questao.getDisciplina()))) {
                continue;
            }

            grupos
                .computeIfAbsent(questao.getTopico(), key -> new LinkedHashSet<>())
                .add(questao.getSubtopico());
        }

        ArrayList<Topico> topicos = new ArrayList<>();
        UUID disciplinaId = idEstavel("disciplina:" + disciplinaNormalizada);
        for (Map.Entry<String, LinkedHashSet<String>> entry : grupos.entrySet()) {
            topicos.add(
                new Topico(
                    disciplinaId,
                    disciplina,
                    entry.getKey(),
                    idEstavel(disciplinaNormalizada + ":" + TextoUtil.normalizarMaiusculo(entry.getKey())),
                    entry.getValue().toArray(String[]::new)
                )
            );
        }
        return topicos;
    }

    public List<Questao> filtrarQuestoes(
        String disciplina,
        Collection<String> topicos,
        Collection<String> subtopicos,
        Integer nivelDificuldade
    ) {
        Set<String> topicosNormalizados = normalizarColecao(topicos);
        Set<String> subtopicosNormalizados = normalizarColecao(subtopicos);
        String disciplinaNormalizada = TextoUtil.normalizarMaiusculo(disciplina);

        return BANCO.stream()
            .filter(questao -> disciplinaNormalizada.isBlank() || disciplinaNormalizada.equals(TextoUtil.normalizarMaiusculo(questao.getDisciplina())))
            .filter(questao -> topicosNormalizados.isEmpty() || topicosNormalizados.contains(TextoUtil.normalizarMaiusculo(questao.getTopico())))
            .filter(questao -> subtopicosNormalizados.isEmpty() || subtopicosNormalizados.contains(TextoUtil.normalizarMaiusculo(questao.getSubtopico())))
            .filter(questao -> nivelDificuldade == null || questao.getNivelDificuldade() == nivelDificuldade)
            .map(this::copiarQuestao)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public Questao proximaQuestao(
        String disciplina,
        Collection<String> topicos,
        Collection<String> subtopicos,
        Integer nivelDificuldade,
        Collection<String> idsIgnorados
    ) {
        Set<String> ignorados = idsIgnorados == null ? Set.of() : new LinkedHashSet<>(idsIgnorados);
        return filtrarQuestoes(disciplina, topicos, subtopicos, nivelDificuldade)
            .stream()
            .filter(questao -> !ignorados.contains(questao.getId()))
            .findFirst()
            .orElse(null);
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

    private static List<Questao> criarBanco() {
        List<Questao> questoes = new ArrayList<>();

        questoes.add(criarQuestao("MAT001", "MATEMATICA", "Algebra", "Juros", "Um investidor aplicou R$ 5.000,00 a juros compostos de 2% ao mes. Apos 3 meses, qual sera o montante?", "R$ 5.300,00", "R$ 5.306,04", "R$ 5.302,00", "R$ 5.310,00", "Nao sei fazer conta de juros", "Precisaria de uma calculadora", "Vou pular essa", 'B', 1, 60, null));
        questoes.add(criarQuestaoReta("MAT002", "MATEMATICA", "Algebra", "Equacoes", "Se 3x + 7 = 22, qual e o valor de x?", "3", "4", "5", "6", "Sei resolver mas demoro", "Nao lembro como faz", "Pular", 'C', 1, 45, null, 3d, 7d, "x", "y"));
        questoes.add(criarQuestao("MAT003", "MATEMATICA", "Geometria", "Circulos", "Qual e a area de um circulo cujo raio mede 5 cm? (Use pi = 3,14)", "78,5 cm2", "31,4 cm2", "15,7 cm2", "314 cm2", "Sei a formula mas vou errar conta", "Confundo area com circunferencia", "Chute educado", 'A', 2, 50, "Formula: Area = pi x r2"));
        questoes.add(criarQuestao("MAT004", "MATEMATICA", "Algebra", "Fracoes", "O resultado da expressao 2/3 + 1/4 e:", "3/7", "3/12", "11/12", "8/12", "Faco mas erro MMC", "Confundo soma com multiplicacao", "Pular", 'C', 2, 55, null));
        questoes.add(criarQuestao("MAT005", "MATEMATICA", "Algebra", "Porcentagem", "Uma loja oferece 20% de desconto em um produto que custa R$ 250,00. Qual o valor com desconto?", "R$ 200,00", "R$ 230,00", "R$ 50,00", "R$ 180,00", "Faco mas erro o calculo do desconto", "Sei mas confundo com acrescimo", "Chutar", 'A', 2, 50, null));
        questoes.add(criarQuestaoParabola("MAT006", "MATEMATICA", "Algebra", "Equacoes", "Qual e a soma das raizes da equacao x2 - 7x + 10 = 0?", "5", "7", "10", "-7", "Sei a formula de Bhaskara mas demoro", "Confundo soma com produto", "Muito dificil, vou pular", 'B', 3, 70, null, 1d, -7d, 10d, "x", "y"));
        questoes.add(criarQuestao("MAT007", "MATEMATICA", "Raciocinio", "Proporcao", "Em um mapa, a escala e 1:100.000. Se a distancia no mapa e 5 cm, qual a distancia real em km?", "5 km", "50 km", "500 km", "0,5 km", "Sei mas erro na conversao", "Confundo escala", "Chutar", 'A', 3, 65, null));
        questoes.add(criarQuestao("MAT008", "MATEMATICA", "Estatistica", "Mediana", "Qual e a mediana dos numeros: 12, 7, 9, 15, 10?", "9", "10", "10,6", "12", "Sei mas confundo mediana com media", "Nao sei ordenar os numeros", "Pular", 'B', 2, 55, null));

        questoes.add(criarQuestaoReta("FIS001", "FISICA", "Mecanica", "MRU", "Um carro percorre 200 km em 4 horas. Qual e sua velocidade media?", "40 km/h", "45 km/h", "50 km/h", "55 km/h", "Nao sei", "Confundi a formula", "Pular", 'C', 1, 40, null, 50d, 0d, "tempo (h)", "distancia (km)"));
        questoes.add(criarQuestaoReta("FIS002", "FISICA", "Mecanica", "Forca", "Segunda Lei de Newton: qual forca acelera um corpo de 5 kg a 4 m/s2?", "10 N", "15 N", "20 N", "25 N", "Nao sei", "Esqueci a formula", "Pular", 'C', 2, 50, null, 5d, 0d, "aceleracao (m/s2)", "forca (N)"));
        questoes.add(criarQuestao("FIS003", "FISICA", "Mecanica", "Energia", "Um carro de 1000 kg esta a 20 m/s. Qual e sua energia cinetica?", "100.000 J", "200.000 J", "300.000 J", "400.000 J", "Nao sei", "Erro a conta", "Pular", 'B', 2, 55, null));
        questoes.add(criarQuestao("FIS004", "FISICA", "Termologia", "Temperatura", "Qual e o ponto de ebulicao da agua ao nivel do mar em Celsius?", "0C", "50C", "100C", "212C", "Nao sei", "Esqueci", "Pular", 'C', 1, 35, null));
        questoes.add(criarQuestao("FIS005", "FISICA", "Termologia", "Calor", "Quantas calorias sao necessarias para aquecer 100 g de agua de 20C para 30C? (c=1 cal/gC)", "500 cal", "1000 cal", "1500 cal", "2000 cal", "Nao sei", "Esqueci a formula", "Pular", 'B', 3, 65, null));
        questoes.add(criarQuestao("FIS006", "FISICA", "Mecanica", "Forca", "Um corpo de 10 kg na Terra (g=10 m/s2) tem peso de:", "10 N", "50 N", "100 N", "1000 N", "Nao sei", "Confundi massa e peso", "Pular", 'C', 1, 35, null));

        return questoes;
    }

    private static Questao criarQuestao(
        String id,
        String disciplina,
        String topico,
        String subtopico,
        String enunciado,
        String opcaoA,
        String opcaoB,
        String opcaoC,
        String opcaoD,
        String opcaoE,
        String opcaoF,
        String opcaoG,
        char respostaCorreta,
        int nivel,
        double tempoSugerido,
        String bloco2
    ) {
        Questao questao = new Questao();
        questao.setId(id);
        questao.setDisciplina(disciplina);
        questao.setTopico(topico);
        questao.setSubtopico(subtopico);
        questao.setEnunciado(enunciado);
        questao.setBloco2(bloco2);
        questao.setOpcaoA(opcaoA);
        questao.setOpcaoB(opcaoB);
        questao.setOpcaoC(opcaoC);
        questao.setOpcaoD(opcaoD);
        questao.setOpcaoE(opcaoE);
        questao.setOpcaoF(opcaoF);
        questao.setOpcaoG(opcaoG);
        questao.setRespostaCorreta(respostaCorreta);
        questao.setNivelDificuldade(nivel);
        questao.setTempoSugerido(tempoSugerido);
        return questao;
    }

    private static Questao criarQuestaoReta(
        String id,
        String disciplina,
        String topico,
        String subtopico,
        String enunciado,
        String opcaoA,
        String opcaoB,
        String opcaoC,
        String opcaoD,
        String opcaoE,
        String opcaoF,
        String opcaoG,
        char respostaCorreta,
        int nivel,
        double tempoSugerido,
        String bloco2,
        double a,
        double b,
        String eixoX,
        String eixoY
    ) {
        Questao questao = criarQuestao(
            id,
            disciplina,
            topico,
            subtopico,
            enunciado,
            opcaoA,
            opcaoB,
            opcaoC,
            opcaoD,
            opcaoE,
            opcaoF,
            opcaoG,
            respostaCorreta,
            nivel,
            tempoSugerido,
            bloco2
        );
        aplicarGrafico(questao, "RETA", a, b, 0d, eixoX, eixoY, -4d, 4d, 1d);
        return questao;
    }

    private static Questao criarQuestaoParabola(
        String id,
        String disciplina,
        String topico,
        String subtopico,
        String enunciado,
        String opcaoA,
        String opcaoB,
        String opcaoC,
        String opcaoD,
        String opcaoE,
        String opcaoF,
        String opcaoG,
        char respostaCorreta,
        int nivel,
        double tempoSugerido,
        String bloco2,
        double a,
        double b,
        double c,
        String eixoX,
        String eixoY
    ) {
        Questao questao = criarQuestao(
            id,
            disciplina,
            topico,
            subtopico,
            enunciado,
            opcaoA,
            opcaoB,
            opcaoC,
            opcaoD,
            opcaoE,
            opcaoF,
            opcaoG,
            respostaCorreta,
            nivel,
            tempoSugerido,
            bloco2
        );
        aplicarGrafico(questao, "PARABOLA", a, b, c, eixoX, eixoY, -4d, 8d, 1d);
        return questao;
    }

    private static void aplicarGrafico(
        Questao questao,
        String tipoCurva,
        double a,
        double b,
        double c,
        String eixoX,
        String eixoY,
        double xMin,
        double xMax,
        double xTickUnit
    ) {
        questao.setUsaGrafico(true);
        questao.setGraficoTipoCurva(tipoCurva);
        questao.setGraficoA(a);
        questao.setGraficoB(b);
        questao.setGraficoC(c);
        questao.setGraficoEixoX(eixoX);
        questao.setGraficoEixoY(eixoY);
        questao.setGraficoXMin(xMin);
        questao.setGraficoXMax(xMax);
        questao.setGraficoXTickUnit(xTickUnit);
    }

    private Questao copiarQuestao(Questao origem) {
        Questao copia = new Questao();
        copia.setId(origem.getId());
        copia.setDisciplina(origem.getDisciplina());
        copia.setTopico(origem.getTopico());
        copia.setSubtopico(origem.getSubtopico());
        copia.setEnunciado(origem.getEnunciado());
        copia.setExercicio(origem.getExercicio());
        copia.setBloco2(origem.getBloco2());
        copia.setImagem(origem.getImagem());
        copia.setOpcaoA(origem.getOpcaoA());
        copia.setOpcaoB(origem.getOpcaoB());
        copia.setOpcaoC(origem.getOpcaoC());
        copia.setOpcaoD(origem.getOpcaoD());
        copia.setOpcaoE(origem.getOpcaoE());
        copia.setOpcaoF(origem.getOpcaoF());
        copia.setOpcaoG(origem.getOpcaoG());
        copia.setRespostaCorreta(origem.getRespostaCorreta());
        copia.setNivelDificuldade(origem.getNivelDificuldade());
        copia.setTempoSugerido(origem.getTempoSugerido());
        copia.setPesosResposta(origem.getPesosResposta());
        copia.setUsaGrafico(origem.isUsaGrafico());
        copia.setGraficoTipoCurva(origem.getGraficoTipoCurva());
        copia.setGraficoA(origem.getGraficoA());
        copia.setGraficoB(origem.getGraficoB());
        copia.setGraficoC(origem.getGraficoC());
        copia.setGraficoEixoX(origem.getGraficoEixoX());
        copia.setGraficoEixoY(origem.getGraficoEixoY());
        copia.setGraficoXMin(origem.getGraficoXMin());
        copia.setGraficoXMax(origem.getGraficoXMax());
        copia.setGraficoXTickUnit(origem.getGraficoXTickUnit());
        return copia;
    }

    private static UUID idEstavel(String valor) {
        return UUID.nameUUIDFromBytes(valor.getBytes());
    }
}
