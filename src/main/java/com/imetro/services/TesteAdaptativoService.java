package com.imetro.services;

import com.imetro.ui.model.Questao;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TesteAdaptativoService {
    
    private List<Questao> bancoQuestoes;
    
    public TesteAdaptativoService() {
        carregarBancoQuestoes();
    }
    
    private void carregarBancoQuestoes() {
        bancoQuestoes = new ArrayList<>();
        
        // Nivel 1 - Facil
        Questao q1 = new Questao();
        q1.setId("MAT001");
        q1.setDisciplina("MATEMATICA");
        q1.setEnunciado("Quanto eh 2 + 2?");
        q1.setOpcaoA("3");
        q1.setOpcaoB("4");
        q1.setOpcaoC("5");
        q1.setOpcaoD("6");
        q1.setRespostaCorreta('B');
        q1.setNivelDificuldade(1);
        bancoQuestoes.add(q1);
        
        // Nivel 2 - Medio
        Questao q2 = new Questao();
        q2.setId("MAT002");
        q2.setDisciplina("MATEMATICA");
        q2.setEnunciado("Se 3x + 7 = 22, qual eh o valor de x?");
        q2.setOpcaoA("3");
        q2.setOpcaoB("4");
        q2.setOpcaoC("5");
        q2.setOpcaoD("6");
        q2.setRespostaCorreta('C');
        q2.setNivelDificuldade(2);
        bancoQuestoes.add(q2);
        
        // Nivel 3 - Dificil
        Questao q3 = new Questao();
        q3.setId("MAT003");
        q3.setDisciplina("MATEMATICA");
        q3.setEnunciado("Qual eh a raiz quadrada de 144?");
        q3.setOpcaoA("10");
        q3.setOpcaoB("11");
        q3.setOpcaoC("12");
        q3.setOpcaoD("13");
        q3.setRespostaCorreta('C');
        q3.setNivelDificuldade(3);
        bancoQuestoes.add(q3);
        
        // Adicione mais questoes conforme necessario...
    }
    
    public List<Questao> carregarQuestoesAdaptativas(String disciplina, int nivel) {
        return bancoQuestoes.stream()
            .filter(q -> q.getDisciplina().equals(disciplina))
            .filter(q -> q.getNivelDificuldade() == nivel)
            .limit(10)
            .collect(Collectors.toList());
    }
    
    public Questao getProximaQuestaoAdaptativa(int nivel) {
        return bancoQuestoes.stream()
            .filter(q -> q.getNivelDificuldade() == nivel)
            .findFirst()
            .orElse(null);
    }
}