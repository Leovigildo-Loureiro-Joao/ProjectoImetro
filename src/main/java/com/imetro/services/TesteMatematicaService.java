package com.imetro.services;

import com.imetro.ui.model.Questao;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;

public class TesteMatematicaService {
    
    public List<Questao> carregarQuestoes() {
        List<Questao> questoes = new ArrayList<>();
        
        // Questão 1 - Fácil
        Questao q1 = new Questao();
        q1.setId("MAT001");
        q1.setDisciplina("MATEMÁTICA");
        q1.setEnunciado("Um investidor aplicou R$ 5.000,00 a juros compostos de 2% ao mês. Após 3 meses, qual será o montante?");
        q1.setOpcaoA("R$ 5.300,00");
        q1.setOpcaoB("R$ 5.306,04");
        q1.setOpcaoC("R$ 5.302,00");
        q1.setOpcaoD("R$ 5.310,00");
        q1.setOpcaoE("Não sei fazer conta de juros");
        q1.setOpcaoF("Precisaria de uma calculadora");
        q1.setOpcaoG("Vou pular essa");
        q1.setRespostaCorreta('B');
        q1.setNivelDificuldade(1);
        q1.setTempoSugerido(60);
        questoes.add(q1);
        
        // Questão 2 - Média
        Questao q2 = new Questao();
        q2.setId("MAT002");
        q2.setDisciplina("MATEMÁTICA");
        q2.setEnunciado("Se 3x + 7 = 22, qual é o valor de x?");
        q2.setOpcaoA("3");
        q2.setOpcaoB("4");
        q2.setOpcaoC("5");
        q2.setOpcaoD("6");
        q2.setOpcaoE("Sei resolver mas demoro");
        q2.setOpcaoF("Não lembro como faz");
        q2.setOpcaoG("Pular");
        q2.setRespostaCorreta('C');
        q2.setNivelDificuldade(1);
        q2.setTempoSugerido(45);
        questoes.add(q2);
        
        // Questão 3 - Geometria (com imagem)
        Questao q3 = new Questao();
        q3.setId("MAT003");
        q3.setDisciplina("MATEMÁTICA");
        q3.setEnunciado("Qual é a área de um círculo cujo raio mede 5 cm? (Use π = 3,14)");
        q3.setBloco2("Fórmula: Área = π × r²");
        q3.setOpcaoA("78,5 cm²");
        q3.setOpcaoB("31,4 cm²");
        q3.setOpcaoC("15,7 cm²");
        q3.setOpcaoD("314 cm²");
        q3.setOpcaoE("Sei a fórmula mas vou errar conta");
        q3.setOpcaoF("Confundo área com circunferência");
        q3.setOpcaoG("Chute educado");
        q3.setRespostaCorreta('A');
        q3.setNivelDificuldade(2);
        q3.setTempoSugerido(50);
        // Imagem: circulo com raio 5cm
        // q3.setImagem(new Image("/assets/images/circulo_raio5.png"));
        questoes.add(q3);
        
        // Questão 4 - Frações
        Questao q4 = new Questao();
        q4.setId("MAT004");
        q4.setDisciplina("MATEMÁTICA");
        q4.setEnunciado("O resultado da expressão 2/3 + 1/4 é:");
        q4.setOpcaoA("3/7");
        q4.setOpcaoB("3/12");
        q4.setOpcaoC("11/12");
        q4.setOpcaoD("8/12");
        q4.setOpcaoE(" Faço mas erro MMC");
        q4.setOpcaoF(" Confundo soma com multiplicação");
        q4.setOpcaoG(" Pular");
        q4.setRespostaCorreta('C');
        q4.setNivelDificuldade(2);
        q4.setTempoSugerido(55);
        questoes.add(q4);
        
        // Questão 5 - Porcentagem (imagem de desconto)
        Questao q5 = new Questao();
        q5.setId("MAT005");
        q5.setDisciplina("MATEMÁTICA");
        q5.setEnunciado("Uma loja oferece 20% de desconto em um produto que custa R$ 250,00. Qual o valor com desconto?");
        q5.setBloco2("[Imagem: etiqueta de preço com desconto]");
        q5.setOpcaoA("R$ 200,00");
        q5.setOpcaoB("R$ 230,00");
        q5.setOpcaoC("R$ 50,00");
        q5.setOpcaoD("R$ 180,00");
        q5.setOpcaoE("Faço mas erro o cálculo do desconto");
        q5.setOpcaoF("Sei mas confundo com acréscimo");
        q5.setOpcaoG("Chutar");
        q5.setRespostaCorreta('A');
        q5.setNivelDificuldade(2);
        q5.setTempoSugerido(50);
        questoes.add(q5);
        
        // Questão 6 - Equação 2º grau (média difícil)
        Questao q6 = new Questao();
        q6.setId("MAT006");
        q6.setDisciplina("MATEMÁTICA");
        q6.setEnunciado("Qual é a soma das raízes da equação x² - 7x + 10 = 0?");
        q6.setOpcaoA("5");
        q6.setOpcaoB("7");
        q6.setOpcaoC("10");
        q6.setOpcaoD("-7");
        q6.setOpcaoE("Sei a fórmula de Bhaskara mas demoro");
        q6.setOpcaoF("Confundo soma com produto");
        q6.setOpcaoG("Muito difícil, vou pular");
        q6.setRespostaCorreta('B');
        q6.setNivelDificuldade(3);
        q6.setTempoSugerido(70);
        questoes.add(q6);
        
        // Questão 7 - Razão e proporção
        Questao q7 = new Questao();
        q7.setId("MAT007");
        q7.setDisciplina("MATEMÁTICA");
        q7.setEnunciado("Em um mapa, a escala é 1:100.000. Se a distância no mapa é 5 cm, qual a distância real em km?");
        q7.setOpcaoA("5 km");
        q7.setOpcaoB("50 km");
        q7.setOpcaoC("500 km");
        q7.setOpcaoD("0,5 km");
        q7.setOpcaoE("Sei mas erro na conversão");
        q7.setOpcaoF("Confundo escala");
        q7.setOpcaoG("Chutar");
        q7.setRespostaCorreta('A');
        q7.setNivelDificuldade(3);
        q7.setTempoSugerido(65);
        questoes.add(q7);
        
        // Questão 8 - Estatística básica
        Questao q8 = new Questao();
        q8.setId("MAT008");
        q8.setDisciplina("MATEMÁTICA");
        q8.setEnunciado("Qual é a mediana dos números: 12, 7, 9, 15, 10?");
        q8.setOpcaoA("9");
        q8.setOpcaoB("10");
        q8.setOpcaoC("10,6");
        q8.setOpcaoD("12");
        q8.setOpcaoE("Sei mas confundo mediana com média");
        q8.setOpcaoF("Não sei ordenar os números");
        q8.setOpcaoG("Pular");
        q8.setRespostaCorreta('B');
        q8.setNivelDificuldade(2);
        q8.setTempoSugerido(55);
        questoes.add(q8);
        
        // Questão 9 - Potenciação (com imagem de tabela)
        Questao q9 = new Questao();
        q9.setId("MAT009");
        q9.setDisciplina("MATEMÁTICA");
        q9.setEnunciado("O valor de 2⁵ + 2³ é:");
        q9.setBloco2("Lembre-se: 2⁵ = 32, 2³ = 8");
        q9.setOpcaoA("40");
        q9.setOpcaoB("2⁸");
        q9.setOpcaoC("64");
        q9.setOpcaoD("2¹⁵");
        q9.setOpcaoE("Sei mas erro a soma");
        q9.setOpcaoF("Confundo potenciação com multiplicação");
        q9.setOpcaoG("Chute");
        q9.setRespostaCorreta('A');
        q9.setNivelDificuldade(1);
        q9.setTempoSugerido(40);
        questoes.add(q9);
        
        // Questão 10 - Problema de lógica matemática (difícil)
        Questao q10 = new Questao();
        q10.setId("MAT010");
        q10.setDisciplina("MATEMÁTICA");
        q10.setEnunciado("Um trem viaja a 60 km/h e leva 2 horas para ir da cidade A à cidade B. Se aumentar a velocidade para 80 km/h, quanto tempo levará?");
        q10.setOpcaoA("1 hora");
        q10.setOpcaoB("1,5 horas");
        q10.setOpcaoC("2 horas");
        q10.setOpcaoD("2,5 horas");
        q10.setOpcaoE("Sei mas erro a regra de três inversa");
        q10.setOpcaoF("Confundo proporção direta com inversa");
        q10.setOpcaoG("Muito confuso");
        q10.setRespostaCorreta('B');
        q10.setNivelDificuldade(3);
        q10.setTempoSugerido(75);
        questoes.add(q10);
        
        return questoes;
    }
}