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
        questoes.add(criarQuestao("MAT002", "MATEMATICA", "Algebra", "Equacoes", "Se 3x + 7 = 22, qual e o valor de x?", "3", "4", "5", "6", "Sei resolver mas demoro", "Nao lembro como faz", "Pular", 'C', 1, 45, null));
        questoes.add(criarQuestao("MAT003", "MATEMATICA", "Geometria", "Circulos", "Qual e a area de um circulo cujo raio mede 5 cm? (Use pi = 3,14)", "78,5 cm2", "31,4 cm2", "15,7 cm2", "314 cm2", "Sei a formula mas vou errar conta", "Confundo area com circunferencia", "Chute educado", 'A', 2, 50, "Formula: Area = pi x r2"));
        questoes.add(criarQuestao("MAT004", "MATEMATICA", "Algebra", "Fracoes", "O resultado da expressao 2/3 + 1/4 e:", "3/7", "3/12", "11/12", "8/12", "Faco mas erro MMC", "Confundo soma com multiplicacao", "Pular", 'C', 2, 55, null));
        questoes.add(criarQuestao("MAT005", "MATEMATICA", "Algebra", "Porcentagem", "Uma loja oferece 20% de desconto em um produto que custa R$ 250,00. Qual o valor com desconto?", "R$ 200,00", "R$ 230,00", "R$ 50,00", "R$ 180,00", "Faco mas erro o calculo do desconto", "Sei mas confundo com acrescimo", "Chutar", 'A', 2, 50, null));
        questoes.add(criarQuestao("MAT006", "MATEMATICA", "Algebra", "Equacoes", "Qual e a soma das raizes da equacao x2 - 7x + 10 = 0?", "5", "7", "10", "-7", "Sei a formula de Bhaskara mas demoro", "Confundo soma com produto", "Muito dificil, vou pular", 'B', 3, 70, null));
        questoes.add(criarQuestao("MAT007", "MATEMATICA", "Raciocinio", "Proporcao", "Em um mapa, a escala e 1:100.000. Se a distancia no mapa e 5 cm, qual a distancia real em km?", "5 km", "50 km", "500 km", "0,5 km", "Sei mas erro na conversao", "Confundo escala", "Chutar", 'A', 3, 65, null));
        questoes.add(criarQuestao("MAT008", "MATEMATICA", "Estatistica", "Mediana", "Qual e a mediana dos numeros: 12, 7, 9, 15, 10?", "9", "10", "10,6", "12", "Sei mas confundo mediana com media", "Nao sei ordenar os numeros", "Pular", 'B', 2, 55, null));

        questoes.add(criarQuestao("FIS001", "FISICA", "Mecanica", "MRU", "Um carro percorre 200 km em 4 horas. Qual e sua velocidade media?", "40 km/h", "45 km/h", "50 km/h", "55 km/h", "Nao sei", "Confundi a formula", "Pular", 'C', 1, 40, null));
        questoes.add(criarQuestao("FIS002", "FISICA", "Mecanica", "Forca", "Segunda Lei de Newton: qual forca acelera um corpo de 5 kg a 4 m/s2?", "10 N", "15 N", "20 N", "25 N", "Nao sei", "Esqueci a formula", "Pular", 'C', 2, 50, null));
        questoes.add(criarQuestao("FIS003", "FISICA", "Mecanica", "Energia", "Um carro de 1000 kg esta a 20 m/s. Qual e sua energia cinetica?", "100.000 J", "200.000 J", "300.000 J", "400.000 J", "Nao sei", "Erro a conta", "Pular", 'B', 2, 55, null));
        questoes.add(criarQuestao("FIS004", "FISICA", "Termologia", "Temperatura", "Qual e o ponto de ebulicao da agua ao nivel do mar em Celsius?", "0C", "50C", "100C", "212C", "Nao sei", "Esqueci", "Pular", 'C', 1, 35, null));
        questoes.add(criarQuestao("FIS005", "FISICA", "Termologia", "Calor", "Quantas calorias sao necessarias para aquecer 100 g de agua de 20C para 30C? (c=1 cal/gC)", "500 cal", "1000 cal", "1500 cal", "2000 cal", "Nao sei", "Esqueci a formula", "Pular", 'B', 3, 65, null));
        questoes.add(criarQuestao("FIS006", "FISICA", "Mecanica", "Forca", "Um corpo de 10 kg na Terra (g=10 m/s2) tem peso de:", "10 N", "50 N", "100 N", "1000 N", "Nao sei", "Confundi massa e peso", "Pular", 'C', 1, 35, null));

        questoes.add(criarQuestao("QUI001", "QUIMICA", "Quimica Geral", "Atomos", "Qual particula possui carga eletrica negativa?", "Proton", "Neutron", "Eletron", "Nucleo", "Nao sei", "Esqueci", "Pular", 'C', 1, 35, null));
        questoes.add(criarQuestao("QUI002", "QUIMICA", "Quimica Geral", "Tabela periodica", "Elementos do mesmo grupo na tabela periodica possuem principalmente:", "Mesmo numero de protons", "Mesmo numero de eletrons na camada de valencia", "Mesmo numero de neutrons", "Mesma massa atomica", "Nao sei", "Esqueci", "Pular", 'B', 2, 50, null));
        questoes.add(criarQuestao("QUI003", "QUIMICA", "Estequiometria", "Mol", "Quantas moleculas existem em 1 mol de qualquer substancia?", "3,02 x 10^23", "6,02 x 10^23", "9,02 x 10^23", "1,20 x 10^24", "Nao sei", "Esqueci a constante", "Pular", 'B', 2, 55, null));
        questoes.add(criarQuestao("QUI004", "QUIMICA", "Quimica Geral", "Ligacoes", "A ligacao formada pelo compartilhamento de eletrons e chamada de:", "Ionica", "Metalica", "Covalente", "Nuclear", "Nao sei", "Confundi os tipos", "Pular", 'C', 1, 40, null));
        questoes.add(criarQuestao("QUI005", "QUIMICA", "Solucoes", "Concentracao", "Uma solucao com 10 g de soluto em 100 mL de solucao tem concentracao de:", "0,1 g/mL", "1 g/mL", "10 g/mL", "100 g/mL", "Nao sei", "Erro a divisao", "Pular", 'A', 3, 60, null));

        questoes.add(criarQuestao("BIO001", "BIOLOGIA", "Citologia", "Celula animal", "Qual organela e responsavel pela respiracao celular?", "Ribossomo", "Mitocondria", "Complexo golgiense", "Lisossomo", "Nao sei", "Esqueci", "Pular", 'B', 1, 35, null));
        questoes.add(criarQuestao("BIO002", "BIOLOGIA", "Citologia", "Celula vegetal", "A estrutura presente na celula vegetal e ausente na animal e:", "Membrana plasmatica", "Mitocondria", "Parede celular", "Citoplasma", "Nao sei", "Esqueci", "Pular", 'C', 1, 35, null));
        questoes.add(criarQuestao("BIO003", "BIOLOGIA", "Genetica", "DNA", "O DNA e composto por unidades chamadas:", "Aminoacidos", "Nucleotideos", "Proteinas", "Lipideos", "Nao sei", "Esqueci", "Pular", 'B', 2, 45, null));
        questoes.add(criarQuestao("BIO004", "BIOLOGIA", "Genetica", "Hereditariedade", "Um gene recessivo so se manifesta quando:", "Esta junto de um gene dominante", "Aparece em homozigose", "Esta no cromossomo Y", "A celula esta em mitose", "Nao sei", "Esqueci", "Pular", 'B', 3, 60, null));
        questoes.add(criarQuestao("BIO005", "BIOLOGIA", "Ecologia", "Cadeia alimentar", "Em uma cadeia alimentar, os produtores sao geralmente:", "Carnivoros", "Herbivoros", "Plantas", "Decompositores", "Nao sei", "Esqueci", "Pular", 'C', 2, 40, null));

        questoes.add(criarQuestao("POR001", "PORTUGUES", "Gramatica", "Concordancia", "Assinale a alternativa correta quanto a concordancia verbal:", "Fazem dois anos que nao o vejo", "Haviam muitos alunos na sala", "Mais de um aluno faltaram", "Faz dois anos que nao o vejo", "Nao sei", "Esqueci a regra", "Pular", 'D', 2, 55, null));
        questoes.add(criarQuestao("POR002", "PORTUGUES", "Gramatica", "Regencia", "Assinale a alternativa em que a regencia verbal esta correta:", "Ele assistiu o filme", "Eu obedeco o regulamento", "Ela namora com ele", "Prefiro estudar do que trabalhar", "Nao sei", "Esqueci a regra", "Pular", 'C', 2, 55, null));
        questoes.add(criarQuestao("POR003", "PORTUGUES", "Interpretacao", "Texto", "Ao interpretar um texto argumentativo, o primeiro passo e identificar:", "A cor da capa", "A tese principal", "O tamanho do titulo", "A fonte utilizada", "Nao sei", "Nunca lembro por onde comecar", "Pular", 'B', 1, 40, null));
        questoes.add(criarQuestao("POR004", "PORTUGUES", "Interpretacao", "Genero textual", "Uma noticia tem como objetivo principal:", "Narrar uma fantasia", "Informar fatos de interesse publico", "Ensinar uma receita", "Fazer propaganda", "Nao sei", "Confundo os generos", "Pular", 'B', 1, 35, null));
        questoes.add(criarQuestao("POR005", "PORTUGUES", "Gramatica", "Pontuacao", "Em qual alternativa a virgula foi empregada corretamente?", "Os alunos chegaram cansados e, sentaram.", "Quando a aula terminou, todos sairam.", "Vamos estudar, matematica hoje.", "Maria comprou cadernos canetas lapis.", "Nao sei", "Esqueci a regra", "Pular", 'B', 3, 60, null));

        questoes.add(criarQuestao("LOG001", "RACIOCINIO LOGICO", "Sequencias", "Padroes numericos", "Qual numero completa a sequencia 2, 4, 8, 16, ?", "18", "24", "32", "34", "Nao sei", "Perco o padrao", "Pular", 'C', 1, 40, null));
        questoes.add(criarQuestao("LOG002", "RACIOCINIO LOGICO", "Logica", "Proposicoes", "Se toda bolsa exige prova e Joana fez a prova, qual afirmacao e necessariamente verdadeira?", "Joana ganhou a bolsa", "Joana estudou", "Joana atendeu a uma exigencia da bolsa", "Toda prova da bolsa foi facil", "Nao sei", "Confundo condicao com conclusao", "Pular", 'C', 2, 50, null));
        questoes.add(criarQuestao("LOG003", "RACIOCINIO LOGICO", "Raciocinio", "Analogia", "Livro esta para leitura assim como mapa esta para:", "Viagem", "Orientacao", "Cidade", "Distancia", "Nao sei", "Fico em duvida entre duas", "Pular", 'B', 2, 45, null));
        questoes.add(criarQuestao("LOG004", "RACIOCINIO LOGICO", "Sequencias", "Figuras", "Numa sequencia em que um triangulo ganha um lado a cada passo, qual figura vem depois do pentagono?", "Hexagono", "Quadrado", "Heptagono", "Circulo", "Nao sei", "Nao visualizei a regra", "Pular", 'A', 2, 55, null));

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

    private Questao copiarQuestao(Questao origem) {
        Questao copia = new Questao();
        copia.setId(origem.getId());
        copia.setDisciplina(origem.getDisciplina());
        copia.setTopico(origem.getTopico());
        copia.setSubtopico(origem.getSubtopico());
        copia.setEnunciado(origem.getEnunciado());
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
        return copia;
    }

    private static UUID idEstavel(String valor) {
        return UUID.nameUUIDFromBytes(valor.getBytes());
    }
}
