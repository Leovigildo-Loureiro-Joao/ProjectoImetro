--- ====================================================
--- SEEDS PARA DISCIPLINAS 
--- ====================================================
insert into disciplinas (id, nome, peso, nivel, objectivo) values
  (uuid_generate_v4(), 'Matemática', 1.5, 'INICIANTE', 'Desenvolver raciocínio lógico-matemático e capacidade de resolução de problemas'),
  (uuid_generate_v4(), 'Português', 1.5, 'INICIANTE', 'Aprimorar compreensão textual, gramática e expressão escrita'),
  (uuid_generate_v4(), 'Física', 1.2, 'INICIANTE', 'Desenvolver raciocínio científico e aplicação de conceitos físicos'),
  (uuid_generate_v4(), 'Raciocínio Lógico', 1.3, 'INICIANTE', 'Aprimorar capacidade de análise, dedução e resolução de problemas lógicos');


-- =====================================================
-- SEEDS PARA PERGUNTAS - MATEMÁTICA (15 questões)
-- =====================================================

-- 1. Fácil - Porcentagem
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Uma loja oferece 20% de desconto em um produto que custa R$ 250,00. Qual é o valor do desconto?',
  '["A) R$ 30,00", "B) R$ 40,00", "C) R$ 50,00", "D) R$ 60,00", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) R$ 50,00',
  'Porcentagem',
  'FACIL'
);

-- 2. Fácil - Operações Básicas
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Qual é o resultado de 15 × 8 ÷ 4?',
  '["A) 20", "B) 25", "C) 30", "D) 35", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) 30',
  'Operações Aritméticas',
  'FACIL'
);

-- 3. Fácil - Frações
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Qual é o valor de 1/2 + 1/3?',
  '["A) 2/5", "B) 3/5", "C) 5/6", "D) 1/5", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) 5/6',
  'Frações',
  'FACIL'
);

-- 4. Fácil - Média Aritmética
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Qual é a média aritmética dos números 8, 10, 12, 14 e 16?',
  '["A) 10", "B) 11", "C) 12", "D) 13", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) 12',
  'Estatística Básica',
  'FACIL'
);

-- 5. Fácil - Potenciação
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Qual é o valor de 2⁵?',
  '["A) 16", "B) 24", "C) 32", "D) 40", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) 32',
  'Potenciação',
  'FACIL'
);

-- 6. Médio - Equações
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Se 3x + 7 = 22, qual é o valor de x?',
  '["A) 3", "B) 4", "C) 5", "D) 6", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) 5',
  'Equações',
  'MEDIO'
);

-- 7. Médio - Regra de Três
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Se 5 operários constroem um muro em 12 dias, quantos dias 8 operários levarão para construir o mesmo muro?',
  '["A) 6 dias", "B) 7,5 dias", "C) 8 dias", "D) 9 dias", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'B) 7,5 dias',
  'Regra de Três',
  'MEDIO'
);

-- 8. Médio - Juros Simples
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Um capital de R$ 1000,00 aplicado a juros simples de 5% ao mês por 6 meses rende quanto de juros?',
  '["A) R$ 200,00", "B) R$ 250,00", "C) R$ 300,00", "D) R$ 350,00", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) R$ 300,00',
  'Juros Simples',
  'MEDIO'
);

-- 9. Médio - Geometria (Área)
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Qual é a área de um círculo cujo raio mede 5 cm? (Use π = 3,14)',
  '["A) 15,7 cm²", "B) 31,4 cm²", "C) 62,8 cm²", "D) 78,5 cm²", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'D) 78,5 cm²',
  'Geometria',
  'MEDIO'
);

-- 10. Médio - Razão e Proporção
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'A razão entre dois números é 3/5 e sua soma é 64. Qual é o maior número?',
  '["A) 24", "B) 32", "C) 40", "D) 48", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) 40',
  'Razão e Proporção',
  'MEDIO'
);

-- 11. Desafiante - Progressão Aritmética
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Uma progressão aritmética tem primeiro termo 5 e razão 3. Qual é o 10º termo?',
  '["A) 29", "B) 30", "C) 31", "D) 32", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'D) 32',
  'Progressões',
  'DESAFIANTE'
);

-- 12. Desafiante - Função Quadrática
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Qual é a soma das raízes da equação x² - 7x + 12 = 0?',
  '["A) 5", "B) 6", "C) 7", "D) 8", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) 7',
  'Função Quadrática',
  'DESAFIANTE'
);

-- 13. Desafiante - Análise Combinatória
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Quantos anagramas tem a palavra "MATEMÁTICA"?',
  '["A) 151200", "B) 181440", "C) 362880", "D) 453600", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'A) 151200',
  'Análise Combinatória',
  'DESAFIANTE'
);

-- 14. Extra - Logaritmos
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Qual é o valor de log₂(32) + log₃(81)?',
  '["A) 7", "B) 8", "C) 9", "D) 10", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) 9',
  'Logaritmos',
  'EXTRA'
);

-- 15. Extra - Matrizes
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Se A = [[2, 1], [3, 4]] e B = [[1, 0], [2, 1]], qual é o determinante de A × B?',
  '["A) 2", "B) 5", "C) 8", "D) 10", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'B) 5',
  'Matrizes',
  'EXTRA'
);

-- =====================================================
-- SEEDS PARA PERGUNTAS - PORTUGUÊS (15 questões)
-- =====================================================

-- 1. Fácil - Acentuação
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Qual das palavras abaixo está corretamente acentuada?',
  '["A) Ideia", "B) Assembléia", "C) Pôr (verbo)", "D) Heroíco", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) Pôr (verbo)',
  'Acentuação',
  'FACIL'
);

-- 2. Fácil - Ortografia
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Assinale a palavra escrita corretamente:',
  '["A) Xerox", "B) Excesso", "C) Exceção", "D) Todas estão corretas", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'D) Todas estão corretas',
  'Ortografia',
  'FACIL'
);

-- 3. Fácil - Sinônimos
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Qual é o sinônimo de "Íntegro"?',
  '["A) Corrupto", "B) Honesto", "C) Incompleto", "D) Fraco", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'B) Honesto',
  'Semântica',
  'FACIL'
);

-- 4. Fácil - Separação Silábica
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Como se separa a palavra "PSICOLOGIA"?',
  '["A) Psi-co-lo-gi-a", "B) Ps-i-co-lo-gi-a", "C) P-s-i-c-o-l-o-g-i-a", "D) Não se separa", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'A) Psi-co-lo-gi-a',
  'Separação Silábica',
  'FACIL'
);

-- 5. Fácil - Substantivos
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Qual é o coletivo de "ABELHAS"?',
  '["A) Cardume", "B) Manada", "C) Enxame", "D) Alcateia", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) Enxame',
  'Substantivos Coletivos',
  'FACIL'
);

-- 6. Médio - Concordância Verbal
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Assinale a alternativa correta quanto à concordância verbal:',
  '["A) Fazem dois anos que não o vejo", "B) Haviam muitos candidatos na sala", "C) Mais de um aluno faltaram", "D) Faz dois anos que não o vejo", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'D) Faz dois anos que não o vejo',
  'Concordância Verbal',
  'MEDIO'
);

-- 7. Médio - Regência Verbal
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Assinale a alternativa em que a regência verbal está correta:',
  '["A) Ele assistiu o filme", "B) Eu obedeço o regulamento", "C) Ela namora com ele", "D) Prefiro estudar do que trabalhar", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) Ela namora com ele',
  'Regência Verbal',
  'MEDIO'
);

-- 8. Médio - Colocação Pronominal
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Assinale a alternativa com próclise correta:',
  '["A) Me empreste o livro", "B) Não se esqueça de mim", "C) O livro me foi dado", "D) Dir-se-ia que é verdade", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'B) Não se esqueça de mim',
  'Colocação Pronominal',
  'MEDIO'
);

-- 9. Médio - Período Composto
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Identifique a oração subordinada adjetiva: "O aluno que estudou passou na prova."',
  '["A) O aluno", "B) que estudou", "C) passou na prova", "D) estudou passou", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'B) que estudou',
  'Período Composto',
  'MEDIO'
);

-- 10. Médio - Vozes Verbais
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Na frase "O muro foi pintado pelos alunos", a voz verbal é:',
  '["A) Ativa", "B) Passiva analítica", "C) Passiva sintética", "D) Reflexiva", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'B) Passiva analítica',
  'Vozes Verbais',
  'MEDIO'
);

-- 11. Desafiante - Figuras de Linguagem
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Identifique a figura de linguagem presente em: "O vento beijava as flores do campo."',
  '["A) Metáfora", "B) Comparação", "C) Personificação", "D) Hipérbole", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) Personificação',
  'Figuras de Linguagem',
  'DESAFIANTE'
);

-- 12. Desafiante - Funções da Linguagem
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Uma propaganda que foca nas qualidades do produto predomina qual função da linguagem?',
  '["A) Emotiva", "B) Conativa", "C) Referencial", "D) Poética", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'B) Conativa',
  'Funções da Linguagem',
  'DESAFIANTE'
);

-- 13. Desafiante - Morfossintaxe
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Na frase "Preciso de que você me ajude", a expressão "de que" exerce função de:',
  '["A) Conjunção integrante", "B) Preposição + conjunção integrante", "C) Pronome relativo", "D) Conjunção causal", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'B) Preposição + conjunção integrante',
  'Morfossintaxe',
  'DESAFIANTE'
);

-- 14. Extra - Literatura Brasileira
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Qual obra é considerada o marco inicial do Modernismo no Brasil?',
  '["A) O Cortiço", "B) Iracema", "C) Macunaíma", "D) Semana de Arte Moderna de 1922", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'D) Semana de Arte Moderna de 1922',
  'Literatura Brasileira',
  'EXTRA'
);

-- 15. Extra - Estilística
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'A expressão "Estou morrendo de sede" é um exemplo de:',
  '["A) Eufemismo", "B) Ironia", "C) Hipérbole", "D) Prosopopeia", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) Hipérbole',
  'Estilística',
  'EXTRA'
);

-- =====================================================
-- SEEDS PARA PERGUNTAS - FÍSICA (15 questões)
-- =====================================================

-- 1. Fácil - Unidades de Medida
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Qual é a unidade de medida da força no Sistema Internacional (SI)?',
  '["A) Joule", "B) Watt", "C) Newton", "D) Pascal", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) Newton',
  'Mecânica',
  'FACIL'
);

-- 2. Fácil - Velocidade Média
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Um carro percorre 200 km em 4 horas. Qual é sua velocidade média?',
  '["A) 40 km/h", "B) 45 km/h", "C) 50 km/h", "D) 55 km/h", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) 50 km/h',
  'Cinemática',
  'FACIL'
);

-- 3. Fácil - Conversão de Unidades
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Quantos metros equivalem a 3,5 km?',
  '["A) 350 m", "B) 3500 m", "C) 35000 m", "D) 35 m", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'B) 3500 m',
  'Unidades de Medida',
  'FACIL'
);

-- 4. Fácil - Massa e Peso
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Um corpo de 10 kg na Terra (g=10 m/s²) tem peso de:',
  '["A) 10 N", "B) 50 N", "C) 100 N", "D) 1000 N", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) 100 N',
  'Mecânica',
  'FACIL'
);

-- 5. Fácil - Temperatura
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Qual é o ponto de ebulição da água ao nível do mar em Celsius?',
  '["A) 0°C", "B) 50°C", "C) 100°C", "D) 212°C", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) 100°C',
  'Termologia',
  'FACIL'
);

-- 6. Médio - Energia Cinética
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Um carro de 1000 kg está a 20 m/s. Qual é sua energia cinética?',
  '["A) 100.000 J", "B) 200.000 J", "C) 300.000 J", "D) 400.000 J", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'B) 200.000 J',
  'Energia',
  'MEDIO'
);

-- 7. Médio - Leis de Newton
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Segunda Lei de Newton: Força = massa × aceleração. Qual força acelera um corpo de 5 kg a 4 m/s²?',
  '["A) 10 N", "B) 15 N", "C) 20 N", "D) 25 N", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) 20 N',
  'Leis de Newton',
  'MEDIO'
);

-- 8. Médio - Trabalho e Potência
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Uma força de 50 N desloca um objeto por 3 metros. Qual é o trabalho realizado?',
  '["A) 50 J", "B) 100 J", "C) 150 J", "D) 200 J", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) 150 J',
  'Trabalho e Energia',
  'MEDIO'
);

-- 9. Médio - Pressão
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Uma força de 100 N é aplicada sobre uma área de 2 m². Qual é a pressão exercida?',
  '["A) 20 Pa", "B) 40 Pa", "C) 50 Pa", "D) 100 Pa", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) 50 Pa',
  'Hidrostática',
  'MEDIO'
);

-- 10. Médio - Densidade
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Um corpo tem massa 200 g e volume 100 cm³. Qual é sua densidade?',
  '["A) 1 g/cm³", "B) 1,5 g/cm³", "C) 2 g/cm³", "D) 2,5 g/cm³", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) 2 g/cm³',
  'Densidade',
  'MEDIO'
);

-- 11. Desafiante - Queda Livre
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Um corpo é lançado verticalmente para cima com velocidade de 30 m/s. Considerando g = 10 m/s², qual a altura máxima atingida?',
  '["A) 35 m", "B) 40 m", "C) 45 m", "D) 50 m", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) 45 m',
  'Movimento Uniformemente Variado',
  'DESAFIANTE'
);

-- 12. Desafiante - Calorimetria
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Quantas calorias são necessárias para aquecer 100 g de água de 20°C para 30°C? (calor específico da água = 1 cal/g°C)',
  '["A) 500 cal", "B) 1000 cal", "C) 1500 cal", "D) 2000 cal", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'B) 1000 cal',
  'Calorimetria',
  'DESAFIANTE'
);

-- 13. Desafiante - Eletrodinâmica
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Um resistor de 10 Ω é percorrido por uma corrente de 2 A. Qual é a tensão aplicada?',
  '["A) 5 V", "B) 10 V", "C) 15 V", "D) 20 V", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'D) 20 V',
  'Eletrodinâmica',
  'DESAFIANTE'
);

-- 14. Extra - Óptica
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Qual fenômeno explica a formação do arco-íris?',
  '["A) Reflexão total", "B) Difração", "C) Refração e dispersão", "D) Interferência", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) Refração e dispersão',
  'Óptica',
  'EXTRA'
);

-- 15. Extra - Física Moderna
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Quem propôs a Teoria da Relatividade?',
  '["A) Isaac Newton", "B) Niels Bohr", "C) Albert Einstein", "D) Marie Curie", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) Albert Einstein',
  'Física Moderna',
  'EXTRA'
);

-- =====================================================
-- SEEDS PARA PERGUNTAS - RACIOCÍNIO LÓGICO (15 questões)
-- =====================================================

-- 1. Fácil - Sequências
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Complete a sequência: 2, 4, 8, 16, __',
  '["A) 18", "B) 20", "C) 24", "D) 32", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'D) 32',
  'Sequências',
  'FACIL'
);

-- 2. Fácil - Padrões
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Qual é o próximo número? 1, 4, 9, 16, __',
  '["A) 20", "B) 24", "C) 25", "D) 30", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) 25',
  'Padrões Numéricos',
  'FACIL'
);

-- 3. Fácil - Sequência Alternada
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Complete: 3, 6, 5, 10, 9, 18, __',
  '["A) 15", "B) 16", "C) 17", "D) 20", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) 17',
  'Sequências Lógicas',
  'FACIL'
);

-- 4. Fácil - Anagramas
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Qual palavra NÃO é um anagrama de "AMOR"?',
  '["A) ROMA", "B) RAMO", "C) OMAR", "D) MORA", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'D) MORA',
  'Anagramas',
  'FACIL'
);

-- 5. Fácil - Verdadeiro/Falso
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Se hoje é quarta-feira, que dia será daqui a 10 dias?',
  '["A) Sábado", "B) Domingo", "C) Segunda-feira", "D) Terça-feira", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'A) Sábado',
  'Raciocínio Temporal',
  'FACIL'
);

-- 6. Médio - Lógica de Argumentação
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Se todos os A são B, e alguns B são C, então podemos afirmar que:',
  '["A) Todos os A são C", "B) Alguns A são C", "C) Nenhum A é C", "D) Nada se pode concluir com certeza", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'D) Nada se pode concluir com certeza',
  'Lógica de Argumentação',
  'MEDIO'
);

-- 7. Médio - Operadores Lógicos
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Dado que P é verdadeiro e Q é falso, qual o valor de (P ∧ Q) → (P ∨ Q)?',
  '["A) Verdadeiro", "B) Falso", "C) Não pode determinar", "D) Contradição", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'A) Verdadeiro',
  'Lógica Proposicional',
  'MEDIO'
);

-- 8. Médio - Sequência de Figuras
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Em uma sequência de figuras, cada figura tem um triângulo a mais que a anterior. Se a 1ª tem 1 triângulo, quantos terá a 5ª?',
  '["A) 3", "B) 4", "C) 5", "D) 6", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) 5',
  'Raciocínio Espacial',
  'MEDIO'
);

-- 9. Médio - Problemas de Idade
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Hoje, João tem o dobro da idade de Maria. Há 5 anos, a soma das idades era 20. Qual a idade de João hoje?',
  '["A) 15", "B) 20", "C) 25", "D) 30", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'B) 20',
  'Raciocínio Matemático',
  'MEDIO'
);

-- 10. Médio - Diagramas Lógicos
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Em uma sala, 30 pessoas gostam de café, 20 gostam de chá e 10 gostam de ambos. Quantas pessoas gostam de café ou chá?',
  '["A) 30", "B) 40", "C) 50", "D) 60", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'B) 40',
  'Conjuntos',
  'MEDIO'
);

-- 11. Desafiante - Raciocínio Matemático
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Um relógio digital marca 12:34. Quantas vezes os algarismos se repetirão em 24 horas?',
  '["A) 2 vezes", "B) 4 vezes", "C) 6 vezes", "D) 8 vezes", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) 6 vezes',
  'Raciocínio Matemático',
  'DESAFIANTE'
);

-- 12. Desafiante - Problemas de Lógica
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Ana, Bia e Carla são médica, engenheira e advogada, não respectivamente. Sabe-se que: (1) A médica é amiga de Ana. (2) Carla é engenheira. (3) Bia é mais velha que a advogada. Quem é médica?',
  '["A) Ana", "B) Bia", "C) Carla", "D) Não se pode determinar", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'B) Bia',
  'Lógica Dedutiva',
  'DESAFIANTE'
);

-- 13. Desafiante - Sequência Complexa
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Qual é o próximo termo da sequência: 1, 3, 7, 15, 31, __',
  '["A) 47", "B) 55", "C) 63", "D) 71", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) 63',
  'Sequências Avançadas',
  'DESAFIANTE'
);

-- 14. Extra - Combinatória
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Em uma sala, há 5 pessoas. Cada uma cumprimenta as outras apenas uma vez. Quantos cumprimentos ocorrem?',
  '["A) 5", "B) 8", "C) 10", "D) 15", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'C) 10',
  'Combinatória',
  'EXTRA'
);

-- 15. Extra - Problema de Lógica Complexo
insert into perguntas (id, questao, respostas, resposta_correta, topico, dificuldade) values (
  uuid_generate_v4(),
  'Três caixas, uma com maçãs, uma com laranjas e uma com ambas, estão todas etiquetadas incorretamente. Você pode tirar uma fruta de uma caixa sem ver o conteúdo. Qual a menor quantidade de frutas para determinar o conteúdo de todas?',
  '["A) 1", "B) 2", "C) 3", "D) 4", "E) Não sei", "F) Me esqueci", "G) Não entendi como se faz"]'::jsonb,
  'A) 1',
  'Lógica Clássica',
  'EXTRA'
);
 
-- =====================================================
-- MIGRACAO DOS METADADOS DE FOCO
-- =====================================================

alter table if exists perguntas add column if not exists disciplina text null;
alter table if exists perguntas add column if not exists subtopico text null;

update perguntas
set subtopico = coalesce(nullif(subtopico, ''), topico)
where subtopico is null or subtopico = '';

update perguntas
set disciplina = case
  when topico in ('Porcentagem', 'OperaÃ§Ãµes AritmÃ©ticas', 'FraÃ§Ãµes', 'EstatÃ­stica BÃ¡sica', 'PotenciaÃ§Ã£o', 'EquaÃ§Ãµes', 'Regra de TrÃªs', 'Juros Simples', 'Geometria', 'RazÃ£o e ProporÃ§Ã£o', 'ProgressÃµes', 'FunÃ§Ã£o QuadrÃ¡tica', 'AnÃ¡lise CombinatÃ³ria', 'Logaritmos', 'Matrizes')
    then 'MATEMATICA'
  when topico in ('AcentuaÃ§Ã£o', 'Ortografia', 'SemÃ¢ntica', 'SeparaÃ§Ã£o SilÃ¡bica', 'Substantivos Coletivos', 'ConcordÃ¢ncia Verbal', 'RegÃªncia Verbal', 'ColocaÃ§Ã£o Pronominal', 'PerÃ­odo Composto', 'Vozes Verbais', 'Figuras de Linguagem', 'FunÃ§Ãµes da Linguagem', 'Morfossintaxe', 'Literatura Brasileira', 'EstilÃ­stica')
    then 'PORTUGUES'
  when topico in ('MecÃ¢nica', 'CinemÃ¡tica', 'Unidades de Medida', 'Termologia', 'Energia', 'Leis de Newton', 'Trabalho e Energia', 'HidrostÃ¡tica', 'Densidade', 'Movimento Uniformemente Variado', 'Calorimetria', 'EletrodinÃ¢mica', 'Ã“ptica', 'FÃ­sica Moderna')
    then 'FISICA'
  when topico in ('SequÃªncias', 'PadrÃµes NumÃ©ricos', 'SequÃªncias LÃ³gicas', 'Anagramas', 'RaciocÃ­nio Temporal', 'LÃ³gica de ArgumentaÃ§Ã£o', 'LÃ³gica Proposicional', 'RaciocÃ­nio Espacial', 'RaciocÃ­nio MatemÃ¡tico', 'Conjuntos', 'LÃ³gica Dedutiva', 'SequÃªncias AvanÃ§adas', 'CombinatÃ³ria', 'LÃ³gica ClÃ¡ssica')
    then 'RACIOCINIO LOGICO'
  else coalesce(disciplina, 'GERAL')
end
where disciplina is null or disciplina = '';

update perguntas
set topico = case
  when disciplina = 'MATEMATICA' and subtopico in ('Geometria') then 'Geometria'
  when disciplina = 'MATEMATICA' and subtopico in ('EstatÃ­stica BÃ¡sica') then 'Estatistica'
  when disciplina = 'MATEMATICA' and subtopico in ('RazÃ£o e ProporÃ§Ã£o') then 'Raciocinio'
  when disciplina = 'MATEMATICA' then 'Algebra'

  when disciplina = 'PORTUGUES' and subtopico in ('ConcordÃ¢ncia Verbal', 'RegÃªncia Verbal', 'ColocaÃ§Ã£o Pronominal', 'Vozes Verbais') then 'Gramatica'
  when disciplina = 'PORTUGUES' and subtopico in ('PerÃ­odo Composto', 'FunÃ§Ãµes da Linguagem', 'Figuras de Linguagem', 'Morfossintaxe', 'SemÃ¢ntica', 'EstilÃ­stica', 'Literatura Brasileira') then 'Interpretacao'
  when disciplina = 'PORTUGUES' then 'Gramatica'

  when disciplina = 'FISICA' and subtopico in ('Termologia', 'Calorimetria') then 'Termologia'
  when disciplina = 'FISICA' then 'Mecanica'

  when disciplina = 'RACIOCINIO LOGICO' then 'Raciocinio'
  else topico
end;
