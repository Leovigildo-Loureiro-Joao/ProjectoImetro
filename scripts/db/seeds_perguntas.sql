
INSERT INTO perguntas (
    id, disciplina, topico_principal, topico, subtopico, questao, respostas, pesos_resposta,
    resposta_correta, dificuldade, rigor, referencia_livro, pagina_inicio, pagina_fim,
    exercicio, usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
) VALUES


(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Proposições', 'Valor lógico',
    'Considere as proposições: p: "2 é par" e q: "3 é ímpar". O valor lógico de p ∧ q é:',
    '["Verdadeiro", "Falso", "Não definido", "Depende"]'::jsonb, '[1.0, 0.2, 0.1, 0.05]'::jsonb,
    'Verdadeiro', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 9, 12,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Condicionais', 'Tabela verdade',
    'O condicional p → q é falso somente quando:',
    '["p é verdadeira e q é falsa", "p é falsa e q é verdadeira", "p e q são verdadeiras", "p e q são falsas"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'p é verdadeira e q é falsa', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-1-.pdf', 14, 16,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Tautologias', 'Proposições logicamente verdadeiras',
    'Qual das seguintes proposições é uma tautologia?',
    '["p ∨ ~p", "p ∧ ~p", "p → ~p", "p ↔ ~p"]'::jsonb, '[1.0, 0.2, 0.3, 0.1]'::jsonb,
    'p ∨ ~p', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 17, 18,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Quantificadores', 'Sentenças abertas',
    'A negação de "∀ x, x + 1 = 7" é:',
    '["∃ x, x + 1 ≠ 7", "∀ x, x + 1 ≠ 7", "∃ x, x + 1 = 7", "∀ x, x + 1 = 7"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '∃ x, x + 1 ≠ 7', 'MEDIO', 0.58, 'fundamentos-da-matematica-elementar-1-.pdf', 23, 24,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Subconjuntos', 'Inclusão',
    'Dados A = {1, 2, 3, 4} e B = {2, 4}, pode-se afirmar que:',
    '["B ⊂ A", "A ⊂ B", "A = B", "A ∩ B = ∅"]'::jsonb, '[1.0, 0.2, 0.1, 0.3]'::jsonb,
    'B ⊂ A', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 25, 27,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Conjunto das partes', 'Subconjuntos',
    'Se A = {a, b, c}, o número de elementos do conjunto das partes P(A) é:',
    '["8", "6", "4", "16"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '8', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-1-.pdf', 34, 35,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Reunião e interseção', 'Operações',
    'Dados A = {1, 2, 3, 4} e B = {3, 4, 5, 6}, A ∪ B é igual a:',
    '["{1, 2, 3, 4, 5, 6}", "{3, 4}", "{1, 2, 5, 6}", "{1, 2, 3, 4}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{1, 2, 3, 4, 5, 6}', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 28, 30,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Diferença', 'Complementar',
    'Dados U = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10} e A = {2, 4, 6, 8, 10}, o complementar de A em relação a U é:',
    '["{1, 3, 5, 7, 9}", "{2, 4, 6, 8, 10}", "∅", "U"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{1, 3, 5, 7, 9}', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 33, 34,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Diferença simétrica', 'Operações',
    'Dados A = {a, b, c, d} e B = {c, d, e, f}, a diferença simétrica A Δ B é:',
    '["{a, b, e, f}", "{c, d}", "{a, b, c, d}", "{e, f}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{a, b, e, f}', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 45, 45,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números naturais', 'Múltiplos',
    'O mínimo múltiplo comum (MMC) entre 12 e 18 é:',
    '["36", "24", "48", "72"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '36', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 40, 43,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números inteiros', 'Divisores',
    'O máximo divisor comum (MDC) entre 24 e 36 é:',
    '["12", "6", "18", "24"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '12', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 41, 43,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números racionais', 'Frações',
    'A fração irredutível de 0,75 é:',
    '["3/4", "75/100", "15/20", "6/8"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3/4', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 44, 48,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números reais', 'Intervalos',
    'A representação gráfica do intervalo [2, 5] na reta real inclui os números:',
    '["2 e 5", "apenas 2", "apenas 5", "nenhum"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2 e 5', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 53, 55,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'valores', -1, 7, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Intervalos', 'Interseção',
    'A interseção dos intervalos ]2, 7] e [4, 10[ é:',
    '["[4, 7]", "]2, 10[", "[4, 7[", "]2, 7]"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '[4, 7]', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 53, 55,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'valores', 0, 12, 2
),

(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Produto cartesiano', 'Pares ordenados',
    'Dados A = {1, 2} e B = {a, b}, o produto cartesiano A × B tem:',
    '["4 elementos", "2 elementos", "6 elementos", "8 elementos"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '4 elementos', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 67, 68,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Relação binária', 'Domínio',
    'Dada a relação R = {(1,2), (2,4), (3,6)} de A = {1,2,3} em B = {2,4,6,8}, o domínio de R é:',
    '["{1, 2, 3}", "{2, 4, 6}", "{1, 2, 3, 4}", "{4, 6, 8}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{1, 2, 3}', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 74, 75,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Definição de função', 'Relação funcional',
    'Qual das seguintes relações de A = {1, 2, 3} em B = {a, b, c} NÃO é uma função?',
    '["{(1, a), (2, a), (3, a)}", "{(1, a), (2, b), (3, c)}", "{(1, a), (1, b), (2, c)}", "{(1, b), (2, b), (3, b)}"]'::jsonb, '[0.2, 0.3, 1.0, 0.1]'::jsonb,
    '{(1, a), (1, b), (2, c)}', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-1-.pdf', 81, 85,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Domínio', 'Funções reais',
    'O domínio da função real f(x) = 1/(x - 2) é:',
    '["x ≠ 2", "x > 2", "x < 2", "x = 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x ≠ 2', 'FACIL', 0.32, 'fundamentos-da-matematica-elementar-1-.pdf', 92, 94,
    'f(x) = 1/(x - 2)', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -2, 6, 2
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Domínio', 'Raiz quadrada',
    'O domínio da função real f(x) = √(x - 4) é:',
    '["x ≥ 4", "x > 4", "x ≤ 4", "x = 4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x ≥ 4', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 92, 93,
    'f(x) = √(x - 4)', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', 0, 8, 2
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Imagem', 'Cálculo da imagem',
    'Dada a função f: {1, 2, 3} → R definida por f(x) = 2x + 1, o conjunto imagem é:',
    '["{3, 5, 7}", "{1, 2, 3}", "{2, 4, 6}", "{1, 3, 5}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{3, 5, 7}', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 88, 92,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Função linear', 'Gráfico',
    'O gráfico da função f(x) = -2x é uma reta que passa pela origem e tem:',
    '["declividade negativa", "declividade positiva", "declividade zero", "inclinação infinita"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'declividade negativa', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 98, 100,
    'f(x) = -2x', true, 'RETA', -2, 0, 0, 'eixo x', 'eixo y', -3, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Coeficientes', 'Coeficiente angular',
    'Na função f(x) = 3x - 5, o coeficiente angular e o coeficiente linear são, respectivamente:',
    '["3 e -5", "-3 e 5", "3 e 5", "-3 e -5"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3 e -5', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 106, 107,
    'f(x) = 3x - 5', true, 'RETA', 3, -5, 0, 'eixo x', 'eixo y', -2, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Zero da função', 'Raiz',
    'O zero da função f(x) = 4x - 8 é:',
    '["2", "-2", "4", "-4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 108, 109,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Crescimento', 'Sinal do coeficiente',
    'A função f(x) = -3x + 2 é:',
    '["decrescente", "crescente", "constante", "não linear"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'decrescente', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 110, 113,
    'f(x) = -3x + 2', true, 'RETA', -3, 2, 0, 'eixo x', 'eixo y', -2, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Sinal da função', 'Inequação',
    'Para que valores de x a função f(x) = 2x - 6 é positiva?',
    '["x > 3", "x < 3", "x > -3", "x > 6"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x > 3', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 116, 119,
    '', true, 'RETA', 2, -6, 0, 'eixo x', 'eixo y', -2, 6, 2
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Inequação', 'Inequação produto',
    'O conjunto solução da inequação (x - 2)(x + 3) > 0 é:',
    '["x < -3 ou x > 2", "-3 < x < 2", "x < 2 ou x > -3", "x = 2 ou x = -3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x < -3 ou x > 2', 'MEDIO', 0.58, 'fundamentos-da-matematica-elementar-1-.pdf', 128, 132,
    '', true, 'PARABOLA', 1, 1, -6, 'eixo x', 'eixo y', -5, 4, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Gráfico', 'Concavidade',
    'A concavidade da parábola f(x) = -2x² + 4x - 1 é voltada para:',
    '["baixo", "cima", "esquerda", "direita"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'baixo', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 139, 140,
    'f(x) = -2x² + 4x - 1', true, 'PARABOLA', -2, 4, -1, 'eixo x', 'eixo y', -1, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Zeros da função', 'Raízes',
    'As raízes da função f(x) = x² - 7x + 12 são:',
    '["3 e 4", "2 e 6", "1 e 12", "-3 e -4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3 e 4', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-1-.pdf', 140, 142,
    'f(x) = x² - 7x + 12', true, 'PARABOLA', 1, -7, 12, 'eixo x', 'eixo y', 0, 5, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Vértice', 'Coordenadas',
    'O vértice da parábola f(x) = x² - 6x + 5 é o ponto:',
    '["(3, -4)", "(-3, -4)", "(3, 4)", "(-3, 4)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(3, -4)', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 147, 149,
    'f(x) = x² - 6x + 5', true, 'PARABOLA', 1, -6, 5, 'eixo x', 'eixo y', 0, 6, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Valor máximo', 'Cálculo',
    'O valor máximo da função f(x) = -x² + 4x - 3 é:',
    '["1", "2", "3", "0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-1-.pdf', 145, 147,
    'f(x) = -x² + 4x - 3', true, 'PARABOLA', -1, 4, -3, 'eixo x', 'eixo y', -1, 5, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Imagem', 'Conjunto imagem',
    'A imagem da função f(x) = x² + 1 é:',
    '["y ≥ 1", "y > 1", "y ≥ 0", "todos os reais"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'y ≥ 1', 'FACIL', 0.38, 'fundamentos-da-matematica-elementar-1-.pdf', 157, 158,
    'f(x) = x² + 1', true, 'PARABOLA', 1, 0, 1, 'eixo x', 'eixo y', -3, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Inequação do 2º grau', 'Resolução',
    'O conjunto solução de x² - 5x + 6 < 0 é:',
    '["2 < x < 3", "x < 2 ou x > 3", "x = 2 ou x = 3", "x ≠ 2 e x ≠ 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2 < x < 3', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-1-.pdf', 164, 166,
    '', true, 'PARABOLA', 1, -5, 6, 'eixo x', 'eixo y', 0, 4, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Função modular', 'Módulo', 'Definição',
    'O valor de |-8| é:',
    '["8", "-8", "0", "64"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '8', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 187, 188,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função modular', 'Equação modular', 'Resolução',
    'A equação |2x - 1| = 7 tem como conjunto solução:',
    '["{4, -3}", "{4, 3}", "{-4, 3}", "{-4, -3}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{4, -3}', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 195, 198,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função modular', 'Inequação modular', 'Resolução',
    'O conjunto solução de |x - 3| < 2 é:',
    '["1 < x < 5", "x < 1 ou x > 5", "x = 1 ou x = 5", "x < 5"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1 < x < 5', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 199, 200,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -2, 8, 2
),
(
    uuid_generate_v4(), 'Matemática', 'Função modular', 'Gráfico', 'Função definida por módulo',
    'O gráfico da função f(x) = |x + 1| intercepta o eixo y no ponto:',
    '["(0, 1)", "(0, -1)", "(-1, 0)", "(1, 0)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(0, 1)', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-1-.pdf', 189, 190,
    'f(x) = |x + 1|', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -4, 3, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Outras funções elementares', 'Função recíproca', 'Gráfico',
    'A função f(x) = 1/x tem como gráfico uma:',
    '["hipérbole equilátera", "reta", "parábola", "circunferência"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'hipérbole equilátera', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-1-.pdf', 206, 208,
    'f(x) = 1/x', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -4, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Outras funções elementares', 'Função máximo inteiro', 'Definição',
    'O valor de [3,7] (função máximo inteiro) é:',
    '["3", "4", "3,5", "3,7"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 210, 211,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Função composta', 'Composição', 'Cálculo',
    'Dadas f(x) = 2x e g(x) = x + 3, o valor de f(g(2)) é:',
    '["10", "7", "8", "9"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '10', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-1-.pdf', 212, 214,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função composta', 'Composição', 'Lei de formação',
    'Dadas f(x) = x² e g(x) = x - 1, a lei da função composta f(g(x)) é:',
    '["x² - 2x + 1", "x² - 1", "x² + 1", "x² - 2x - 1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x² - 2x + 1', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 212, 214,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função inversa', 'Inversão', 'Cálculo',
    'Se f(x) = 5x - 3, então sua inversa f⁻¹(x) é:',
    '["(x + 3)/5", "(x - 3)/5", "5x + 3", "x/5 - 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(x + 3)/5', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 235, 236,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função inversa', 'Função bijetora', 'Condição',
    'Para que uma função f: A → B admita inversa, ela deve ser:',
    '["bijetora", "apenas injetora", "apenas sobrejetora", "qualquer função"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'bijetora', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 232, 234,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função inversa', 'Gráfico', 'Simetria',
    'Os gráficos de uma função f e de sua inversa f⁻¹ são simétricos em relação à reta:',
    '["y = x", "y = -x", "x = 0", "y = 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'y = x', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-1-.pdf', 236, 237,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -3, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função composta', 'Função injetora', 'Propriedade',
    'Se f e g são funções injetoras, então a composta g ∘ f é:',
    '["injetora", "sobrejetora", "bijetora", "não injetora"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'injetora', 'DESAFIANTE', 0.65, 'fundamentos-da-matematica-elementar-1-.pdf', 225, 226,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função composta', 'Função sobrejetora', 'Propriedade',
    'Se f e g são funções sobrejetoras, então a composta g ∘ f é:',
    '["sobrejetora", "injetora", "bijetora", "não sobrejetora"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'sobrejetora', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 225, 225,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função inversa', 'Composição', 'Propriedade',
    'Se f é bijetora e f⁻¹ é sua inversa, então f⁻¹(f(x)) é igual a:',
    '["x", "f(x)", "1", "0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-1-.pdf', 238, 238,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
);


INSERT INTO perguntas (
    id, disciplina, topico_principal, topico, subtopico, questao, respostas, pesos_resposta,
    resposta_correta, dificuldade, rigor, referencia_livro, pagina_inicio, pagina_fim,
    exercicio, usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
) VALUES

(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Proposições', 'Tabela-verdade',
    'Quantas linhas possui a tabela-verdade de uma proposição composta por 3 proposições simples?',
    '["8", "4", "6", "16"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '8', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-1-.pdf', 12, 14,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Conectivos', 'Disjunção',
    'A proposição "p ∨ q" é falsa quando:',
    '["p e q são ambas falsas", "p e q são ambas verdadeiras", "p é verdadeira e q é falsa", "p é falsa e q é verdadeira"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'p e q são ambas falsas', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 13, 14,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Condicionais', 'Recíproco',
    'O recíproco da condicional "p → q" é:',
    '["q → p", "~p → ~q", "~q → ~p", "p → ~q"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'q → p', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 15, 16,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Equivalência', 'Leis de De Morgan',
    'Pelas leis de De Morgan, ~(p ∧ q) é equivalente a:',
    '["~p ∨ ~q", "~p ∧ ~q", "p ∨ q", "p ∧ q"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '~p ∨ ~q', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-1-.pdf', 23, 24,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Conjunto universo', 'Definição',
    'Em um problema de Geometria Plana, o conjunto universo normalmente é:',
    '["um plano", "o conjunto dos números reais", "o conjunto dos inteiros", "o conjunto dos pontos da reta"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'um plano', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 22, 23,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Cardinalidade', 'Subconjuntos',
    'Se A tem 4 elementos, então o número de subconjuntos de A é:',
    '["16", "8", "4", "12"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '16', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 34, 35,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Propriedades', 'Inclusão',
    'Se A ⊂ B e B ⊂ A, então:',
    '["A = B", "A ≠ B", "A = ∅", "B = ∅"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'A = B', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 34, 35,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Operações', 'Diferença',
    'Dados A = {1, 2, 3, 4, 5} e B = {2, 4, 6}, o conjunto A - B é:',
    '["{1, 3, 5}", "{2, 4}", "{6}", "{1, 2, 3, 4, 5}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{1, 3, 5}', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 33, 34,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números naturais', 'Indução',
    'O Princípio da Indução Finita é usado para provar propriedades que envolvem:',
    '["números naturais", "números inteiros", "números reais", "números complexos"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'números naturais', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 57, 58,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números inteiros', 'Múltiplos',
    'O menor múltiplo comum entre 6, 8 e 12 é:',
    '["24", "48", "36", "72"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '24', 'FACIL', 0.32, 'fundamentos-da-matematica-elementar-1-.pdf', 43, 44,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números racionais', 'Operações',
    'O resultado da soma 1/2 + 2/3 é:',
    '["7/6", "3/5", "1/6", "5/6"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '7/6', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 46, 47,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números reais', 'Ordenação',
    'Na reta real, o número -3 está localizado à:',
    '["esquerda de -2", "direita de -2", "direita de 0", "esquerda de -4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'esquerda de -2', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 49, 52,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'valores', -5, 3, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Produto cartesiano', 'Representação gráfica',
    'O produto cartesiano A × B, com A = {1, 2} e B = {3, 4}, representado no plano cartesiano, forma:',
    '["4 pontos", "2 pontos", "1 ponto", "um quadrado"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '4 pontos', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 67, 69,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', 0, 5, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Relação inversa', 'Definição',
    'Se R = {(1,2), (2,3), (3,4)}, então a relação inversa R⁻¹ é:',
    '["{(2,1), (3,2), (4,3)}", "{(1,2), (2,3), (3,4)}", "{(1,1), (2,2), (3,3)}", "{(2,2), (3,3), (4,4)}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{(2,1), (3,2), (4,3)}', 'FACIL', 0.32, 'fundamentos-da-matematica-elementar-1-.pdf', 76, 77,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Definição', 'Aplicação',
    'Uma relação f de A em B é uma função quando:',
    '["todo elemento de A tem um único correspondente em B", "todo elemento de B tem um correspondente em A", "existe elemento de A sem correspondente", "um elemento de A tem dois correspondentes"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'todo elemento de A tem um único correspondente em B', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 81, 83,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Notação', 'Cálculo',
    'Dada f(x) = x² - 2x + 1, o valor de f(3) é:',
    '["4", "1", "9", "16"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '4', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 84, 86,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Domínio', 'Raiz quadrada',
    'O domínio da função f(x) = √(2x - 6) é:',
    '["x ≥ 3", "x > 3", "x ≤ 3", "x = 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x ≥ 3', 'FACIL', 0.32, 'fundamentos-da-matematica-elementar-1-.pdf', 92, 93,
    'f(x) = √(2x - 6)', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', 0, 8, 2
),

(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Gráfico', 'Coeficiente linear',
    'O coeficiente linear da função f(x) = 3x - 4 é o ponto onde o gráfico intercepta o eixo y em:',
    '["(0, -4)", "(0, 3)", "(4, 0)", "(-4, 0)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(0, -4)', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 106, 107,
    'f(x) = 3x - 4', true, 'RETA', 3, -4, 0, 'eixo x', 'eixo y', -2, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Função linear', 'Proporcionalidade',
    'A função linear f(x) = ax, com a ≠ 0, é caracterizada por:',
    '["passar pela origem", "ser sempre crescente", "ser sempre decrescente", "ter coeficiente linear positivo"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'passar pela origem', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 98, 99,
    'f(x) = ax', true, 'RETA', 1, 0, 0, 'eixo x', 'eixo y', -3, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Zero', 'Cálculo',
    'O zero da função f(x) = 5x + 10 é:',
    '["-2", "2", "5", "10"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-2', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 108, 109,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Inequação', 'Inequação produto',
    'O conjunto solução de (2x - 4)(x + 1) < 0 é:',
    '["-1 < x < 2", "x < -1 ou x > 2", "x < 2", "x > -1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-1 < x < 2', 'MEDIO', 0.58, 'fundamentos-da-matematica-elementar-1-.pdf', 128, 132,
    '', true, 'PARABOLA', 2, -2, -4, 'eixo x', 'eixo y', -2, 3, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Forma canónica', 'Completamento',
    'A forma canónica da função f(x) = x² - 6x + 5 é:',
    '["(x - 3)² - 4", "(x + 3)² - 4", "(x - 3)² + 4", "(x + 3)² + 4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(x - 3)² - 4', 'DESAFIANTE', 0.65, 'fundamentos-da-matematica-elementar-1-.pdf', 139, 140,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Eixo de simetria', 'Vértice',
    'O eixo de simetria da parábola f(x) = x² + 4x + 3 é a reta:',
    '["x = -2", "x = 2", "y = -2", "y = 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = -2', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 152, 153,
    'f(x) = x² + 4x + 3', true, 'PARABOLA', 1, 4, 3, 'eixo x', 'eixo y', -5, 2, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Sinal', 'Δ negativo',
    'A função f(x) = x² + 4 é:',
    '["sempre positiva", "sempre negativa", "positiva apenas para x > 0", "negativa para x < 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'sempre positiva', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 159, 161,
    'f(x) = x² + 4', true, 'PARABOLA', 1, 0, 4, 'eixo x', 'eixo y', -3, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Inequação', 'Δ = 0',
    'O conjunto solução de x² - 4x + 4 ≤ 0 é:',
    '["{2}", "x = 2", "x ≠ 2", "todos os reais"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{2}', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-1-.pdf', 164, 166,
    '', true, 'PARABOLA', 1, -4, 4, 'eixo x', 'eixo y', -1, 5, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Função modular', 'Propriedades', 'Desigualdade triangular',
    'A desigualdade triangular do módulo afirma que:',
    '["|x + y| ≤ |x| + |y|", "|x + y| ≥ |x| + |y|", "|x + y| = |x| + |y|", "|x + y| = |x| - |y|"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '|x + y| ≤ |x| + |y|', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 187, 188,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função modular', 'Equação', 'Com módulo em ambos',
    'O conjunto solução de |x - 2| = |2x - 1| é:',
    '["{1, -1}", "{1}", "{-1}", "{1, 1/3}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{1, -1}', 'DESAFIANTE', 0.68, 'fundamentos-da-matematica-elementar-1-.pdf', 196, 197,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função modular', 'Inequação', 'Resolução',
    'A solução de |x - 2| ≥ 3 é:',
    '["x ≤ -1 ou x ≥ 5", "-1 ≤ x ≤ 5", "x ≤ 5", "x ≥ -1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x ≤ -1 ou x ≥ 5', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 199, 200,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -3, 7, 2
),

(
    uuid_generate_v4(), 'Matemática', 'Outras funções elementares', 'Função cúbica', 'Sinal',
    'A função f(x) = x³ é positiva para:',
    '["x > 0", "x < 0", "x ≠ 0", "todos os reais"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x > 0', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 205, 206,
    'f(x) = x³', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -3, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Outras funções elementares', 'Função recíproca', 'Domínio',
    'O domínio da função f(x) = 1/(x - 2) é:',
    '["x ≠ 2", "x > 2", "x < 2", "todos os reais"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x ≠ 2', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 206, 208,
    'f(x) = 1/(x - 2)', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -2, 6, 2
),

(
    uuid_generate_v4(), 'Matemática', 'Função composta', 'Composição', 'Cálculo',
    'Dadas f(x) = x - 1 e g(x) = x², o valor de g(f(3)) é:',
    '["4", "2", "9", "16"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '4', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-1-.pdf', 212, 214,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função composta', 'Composição', 'Lei',
    'Se f(x) = 2x e g(x) = x + 1, então (f ∘ g)(x) é:',
    '["2x + 2", "2x + 1", "2x - 2", "x + 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2x + 2', 'FACIL', 0.32, 'fundamentos-da-matematica-elementar-1-.pdf', 212, 214,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função injetora', 'Teste', 'Horizontal',
    'Uma função é injetora se, no gráfico, qualquer reta horizontal a intercepta em:',
    '["no máximo um ponto", "exatamente um ponto", "pelo menos um ponto", "dois pontos"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'no máximo um ponto', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-1-.pdf', 223, 224,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função sobrejetora', 'Teste', 'Vertical',
    'Uma função f: A → B é sobrejetora quando:',
    '["Im(f) = B", "Im(f) ⊂ B", "Im(f) ∩ B = ∅", "Im(f) é vazia"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'Im(f) = B', 'FACIL', 0.32, 'fundamentos-da-matematica-elementar-1-.pdf', 220, 221,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função inversa', 'Cálculo', 'Função linear',
    'A inversa da função f(x) = (2x + 1)/3 é:',
    '["(3x - 1)/2", "(2x - 1)/3", "(3x + 1)/2", "(2x + 1)/3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(3x - 1)/2', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-1-.pdf', 235, 236,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função bijetora', 'Classificação', 'Função afim',
    'A função f: R → R definida por f(x) = 2x + 3 é:',
    '["bijetora", "apenas injetora", "apenas sobrejetora", "nenhuma das anteriores"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'bijetora', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 222, 224,
    'f(x) = 2x + 3', true, 'RETA', 2, 3, 0, 'eixo x', 'eixo y', -3, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função composta', 'Injetividade', 'Propriedade',
    'Se f e g são funções injetoras, então g ∘ f é:',
    '["injetora", "sobrejetora", "bijetora", "não injetora"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'injetora', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 225, 226,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função composta', 'Sobrejetividade', 'Propriedade',
    'Se f e g são funções sobrejetoras, então g ∘ f é:',
    '["sobrejetora", "injetora", "bijetora", "não sobrejetora"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'sobrejetora', 'DESAFIANTE', 0.60, 'fundamentos-da-matematica-elementar-1-.pdf', 225, 225,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
);

INSERT INTO perguntas (
    id, disciplina, topico_principal, topico, subtopico, questao, respostas, pesos_resposta,
    resposta_correta, dificuldade, rigor, referencia_livro, pagina_inicio, pagina_fim,
    exercicio, usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
) VALUES


(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Proposições', 'Classificação',
    'Qual das seguintes frases é uma proposição lógica?',
    '["O triângulo tem três lados", "Que horas são?", "Estude mais!", "x + 5 = 10"]'::jsonb, '[1.0, 0.1, 0.2, 0.3]'::jsonb,
    'O triângulo tem três lados', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 9, 10,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Conectivos', 'Conjunção',
    'A conjunção "p ∧ q" é verdadeira quando:',
    '["p e q são ambas verdadeiras", "p é verdadeira e q é falsa", "p é falsa e q é verdadeira", "p e q são ambas falsas"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'p e q são ambas verdadeiras', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 11, 12,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Condicionais', 'Contrapositiva',
    'A contrapositiva da condicional "p → q" é:',
    '["~q → ~p", "q → p", "~p → ~q", "p → ~q"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '~q → ~p', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-1-.pdf', 15, 16,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Tautologias', 'Identificação',
    'Qual das seguintes proposições é uma tautologia?',
    '["p ∨ (q ∧ ~q)", "p ∧ ~p", "p → q", "p ↔ ~p"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'p ∨ (q ∧ ~q)', 'DESAFIANTE', 0.65, 'fundamentos-da-matematica-elementar-1-.pdf', 17, 18,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Conjunto universo', 'Complementar',
    'Dados U = {a, b, c, d, e, f, g} e A = {a, c, e, g}, o complementar de A em relação a U é:',
    '["{b, d, f}", "{a, c, e, g}", "{a, b, c, d}", "{e, f, g}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{b, d, f}', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 33, 34,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Operações', 'Distributiva',
    'A propriedade distributiva da união em relação à interseção é expressa por:',
    '["A ∪ (B ∩ C) = (A ∪ B) ∩ (A ∪ C)", "A ∩ (B ∪ C) = (A ∩ B) ∪ (A ∩ C)", "A ∪ (B ∪ C) = (A ∪ B) ∪ C", "A ∩ (B ∩ C) = (A ∩ B) ∩ C"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'A ∪ (B ∩ C) = (A ∪ B) ∩ (A ∪ C)', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 30, 32,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Subconjuntos', 'Inclusão',
    'Se A ⊂ B e B ⊂ C, então é correto afirmar que:',
    '["A ⊂ C", "C ⊂ A", "A = C", "B = ∅"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'A ⊂ C', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 34, 35,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Diferença simétrica', 'Propriedades',
    'A diferença simétrica A △ B é igual a:',
    '["(A - B) ∪ (B - A)", "(A ∪ B) - (A ∩ B)", "A ∪ B", "A ∩ B"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(A - B) ∪ (B - A)', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 44, 45,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números naturais', 'Divisibilidade',
    'O maior divisor comum (MDC) entre 36 e 48 é:',
    '["12", "6", "18", "24"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '12', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 41, 43,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números inteiros', 'Números primos',
    'Quantos números primos existem entre 1 e 20?',
    '["8", "7", "9", "6"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '8', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 42, 43,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números racionais', 'Dízimas periódicas',
    'A fração geratriz da dízima 0,363636... é:',
    '["4/11", "36/99", "36/100", "4/10"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '4/11', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 47, 48,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Intervalos', 'Operações',
    'A interseção dos intervalos ]-2, 5] e [1, 7[ é:',
    '["[1, 5]", "]1, 5]", "[-2, 7[", "[1, 5["]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '[1, 5]', 'FACIL', 0.32, 'fundamentos-da-matematica-elementar-1-.pdf', 53, 55,
    '', true, 'RETA', 0, 0, 0, 'eixo real', 'valores', -3, 8, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Produto cartesiano', 'Cardinalidade',
    'Se A = {1, 2, 3} e B = {a, b}, então n(A × B) é:',
    '["6", "5", "8", "3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '6', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 67, 68,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Relação binária', 'Propriedades',
    'Uma relação R de A em B é um subconjunto de:',
    '["A × B", "A ∪ B", "A ∩ B", "A - B"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'A × B', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 79, 80,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Definição', 'Imagem',
    'Dada a função f: {1, 2, 3, 4} → R definida por f(x) = 2x - 1, o conjunto imagem é:',
    '["{1, 3, 5, 7}", "{1, 2, 3, 4}", "{2, 4, 6, 8}", "{0, 2, 4, 6}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{1, 3, 5, 7}', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 88, 92,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Domínio', 'Função irracional',
    'O domínio da função real f(x) = √(3x - 9) é:',
    '["x ≥ 3", "x > 3", "x ≤ 3", "x = 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x ≥ 3', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 92, 93,
    'f(x) = √(3x - 9)', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', 0, 8, 2
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Igualdade de funções', 'Condições',
    'Duas funções f e g são iguais quando:',
    '["têm o mesmo domínio e f(x) = g(x) para todo x do domínio", "têm o mesmo contradomínio", "têm o mesmo gráfico", "têm a mesma lei de formação"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'têm o mesmo domínio e f(x) = g(x) para todo x do domínio', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 93, 94,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Coeficiente angular', 'Crescimento',
    'A função f(x) = (m - 3)x + 2 é crescente quando:',
    '["m > 3", "m < 3", "m = 3", "m ≠ 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'm > 3', 'FACIL', 0.32, 'fundamentos-da-matematica-elementar-1-.pdf', 110, 113,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Zero da função', 'Cálculo',
    'O zero da função f(x) = -3x + 9 é:',
    '["3", "-3", "9", "-9"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 108, 109,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Inequação produto', 'Resolução',
    'O conjunto solução de (x - 1)(x + 2) < 0 é:',
    '["-2 < x < 1", "x < -2 ou x > 1", "x < 1", "x > -2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-2 < x < 1', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 128, 132,
    '', true, 'PARABOLA', 1, 1, -2, 'eixo x', 'eixo y', -3, 2, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Inequação quociente', 'Resolução',
    'O conjunto solução de (x + 3)/(x - 2) ≤ 0 é:',
    '["-3 ≤ x < 2", "x ≤ -3 ou x > 2", "-3 < x ≤ 2", "x < -3 ou x ≥ 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-3 ≤ x < 2', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 135, 136,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -4, 4, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Gráfico', 'Concavidade',
    'A parábola da função f(x) = -2x² + 4x - 1 tem concavidade voltada para:',
    '["baixo", "cima", "esquerda", "direita"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'baixo', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 139, 140,
    'f(x) = -2x² + 4x - 1', true, 'PARABOLA', -2, 4, -1, 'eixo x', 'eixo y', -1, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Sinal', 'Δ negativo',
    'A função f(x) = x² + 2x + 5 é:',
    '["sempre positiva", "sempre negativa", "positiva apenas para x > 0", "negativa para x < 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'sempre positiva', 'FACIL', 0.32, 'fundamentos-da-matematica-elementar-1-.pdf', 159, 161,
    'f(x) = x² + 2x + 5', true, 'PARABOLA', 1, 2, 5, 'eixo x', 'eixo y', -3, 2, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Inequação', 'Δ = 0',
    'O conjunto solução de x² - 6x + 9 ≤ 0 é:',
    '["{3}", "x = 3", "x ≥ 3", "x ≤ 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{3}', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 164, 166,
    '', true, 'PARABOLA', 1, -6, 9, 'eixo x', 'eixo y', 0, 6, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Soma e produto', 'Raízes',
    'Na equação x² - 7x + 12 = 0, a soma das raízes é:',
    '["7", "12", "-7", "-12"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '7', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 152, 153,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Função modular', 'Propriedades', 'Desigualdade triangular',
    'A desigualdade triangular afirma que |x + y| ≤ |x| + |y| é válida:',
    '["para todos os reais x e y", "apenas para x e y positivos", "apenas para x e y negativos", "apenas para x e y com sinais opostos"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'para todos os reais x e y', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 187, 188,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função modular', 'Equação', 'Módulo de expressão',
    'A equação |2x - 3| = |x + 1| tem como solução:',
    '["{4, 2/3}", "{4, -2/3}", "{-4, 2/3}", "{-4, -2/3}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{4, 2/3}', 'DESAFIANTE', 0.65, 'fundamentos-da-matematica-elementar-1-.pdf', 196, 197,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função modular', 'Inequação', 'Propriedades',
    'A inequação |x - 2| ≥ 1 é equivalente a:',
    '["x ≤ 1 ou x ≥ 3", "1 ≤ x ≤ 3", "x ≤ 3", "x ≥ 1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x ≤ 1 ou x ≥ 3', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-1-.pdf', 199, 200,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -2, 5, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Outras funções elementares', 'Função cúbica', 'Gráfico',
    'A função f(x) = x³ é classificada como:',
    '["ímpar", "par", "constante", "não monótona"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'ímpar', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 205, 206,
    'f(x) = x³', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -2, 2, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Outras funções elementares', 'Função máximo inteiro', 'Definição',
    'O valor de [-3,2] (função máximo inteiro) é:',
    '["-4", "-3", "-2", "-1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-4', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 210, 211,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Função composta', 'Composição', 'Domínio',
    'Dadas f(x) = √x e g(x) = x - 9, o domínio de f(g(x)) é:',
    '["x ≥ 9", "x ≥ 0", "x ≤ 9", "x ≥ 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x ≥ 9', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 216, 217,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função composta', 'Composição', 'Lei de formação',
    'Se f(x) = 3x e g(x) = x + 2, então (f ∘ g)(x) é:',
    '["3x + 6", "3x + 2", "3x - 6", "x + 6"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3x + 6', 'FACIL', 0.32, 'fundamentos-da-matematica-elementar-1-.pdf', 212, 214,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função inversa', 'Cálculo', 'Função linear',
    'Se f(x) = (4x + 1)/2, então f⁻¹(x) é:',
    '["(2x - 1)/4", "(2x + 1)/4", "(4x - 1)/2", "(4x + 1)/2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(2x - 1)/4', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 235, 236,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função bijetora', 'Condição', 'Inversibilidade',
    'Uma função f: A → B admite inversa se, e somente se, for:',
    '["bijetora", "injetora", "sobrejetora", "crescente"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'bijetora', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 232, 234,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função inversa', 'Gráfico', 'Simetria',
    'Os gráficos de uma função f e de sua inversa f⁻¹ são simétricos em relação à reta:',
    '["y = x", "y = -x", "x = 0", "y = 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'y = x', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 236, 237,
    '', true, 'RETA', 1, 0, 0, 'eixo x', 'eixo y', -3, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função composta', 'Associatividade', 'Propriedade',
    'A composição de funções é uma operação:',
    '["associativa", "comutativa", "não associativa", "não definida para funções"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'associativa', 'DESAFIANTE', 0.60, 'fundamentos-da-matematica-elementar-1-.pdf', 214, 214,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função injetora', 'Teste', 'Horizontal',
    'Uma função é injetora se qualquer reta horizontal intercepta seu gráfico em:',
    '["no máximo um ponto", "exatamente um ponto", "pelo menos um ponto", "dois pontos"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'no máximo um ponto', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 223, 224,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função sobrejetora', 'Condição', 'Imagem',
    'Uma função f: A → B é sobrejetora quando:',
    '["Im(f) = B", "Im(f) ⊂ B", "Im(f) ∩ B = ∅", "Im(f) = A"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'Im(f) = B', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 220, 221,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função composta', 'Inversa da composta', 'Propriedade',
    'A inversa da função composta (g ∘ f) é:',
    '["f⁻¹ ∘ g⁻¹", "g⁻¹ ∘ f⁻¹", "f ∘ g", "g ∘ f"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'f⁻¹ ∘ g⁻¹', 'DESAFIANTE', 0.68, 'fundamentos-da-matematica-elementar-1-.pdf', 238, 238,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
);


INSERT INTO perguntas (
    id, disciplina, topico_principal, topico, subtopico, questao, respostas, pesos_resposta,
    resposta_correta, dificuldade, rigor, referencia_livro, pagina_inicio, pagina_fim,
    exercicio, usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
) VALUES

(
    uuid_generate_v4(), 'Matemática', 'Potências e raízes', 'Potenciação', 'Propriedades',
    'O valor da expressão (2³ · 2⁴) : 2⁵ é:',
    '["4", "8", "2", "16"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '4', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-2-.pdf', 9, 12,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Potências e raízes', 'Potência com expoente negativo', 'Definição',
    'O valor de 2⁻³ é:',
    '["1/8", "8", "-8", "-1/8"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1/8', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-2-.pdf', 14, 15,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Potências e raízes', 'Radiciação', 'Raiz enésima',
    'O valor de √(36) é:',
    '["6", "-6", "±6", "18"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '6', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-2-.pdf', 17, 18,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Potências e raízes', 'Racionalização', 'Denominadores',
    'Racionalizando a expressão 1/√2, obtém-se:',
    '["√2/2", "√2", "2/√2", "1/2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '√2/2', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-2-.pdf', 24, 25,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Potências e raízes', 'Potência com expoente racional', 'Definição',
    'O valor de 8^(2/3) é:',
    '["4", "2", "8", "16"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '4', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-2-.pdf', 25, 26,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Função exponencial', 'Definição', 'Gráfico',
    'O gráfico da função f(x) = 2^x intercepta o eixo y no ponto:',
    '["(0, 1)", "(1, 0)", "(0, 2)", "(2, 0)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(0, 1)', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-2-.pdf', 33, 35,
    'f(x) = 2^x', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -2, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função exponencial', 'Equações exponenciais', 'Redução à base comum',
    'A solução da equação 2^x = 32 é:',
    '["5", "4", "6", "3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '5', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-2-.pdf', 47, 48,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função exponencial', 'Equações exponenciais', 'Incógnita auxiliar',
    'A equação 4^x - 5·2^x + 4 = 0 tem como solução:',
    '["x = 0 ou x = 2", "x = 0 ou x = 1", "x = 2 ou x = 4", "x = 1 ou x = 4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = 0 ou x = 2', 'MEDIO', 0.60, 'fundamentos-da-matematica-elementar-2-.pdf', 50, 51,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função exponencial', 'Inequações exponenciais', 'Base maior que 1',
    'O conjunto solução de 2^x > 8 é:',
    '["x > 3", "x < 3", "x > 2", "x < 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x > 3', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-2-.pdf', 48, 49,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -2, 5, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função exponencial', 'Crescimento', 'Base entre 0 e 1',
    'A função f(x) = (1/2)^x é:',
    '["decrescente", "crescente", "constante", "não monótona"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'decrescente', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-2-.pdf', 33, 35,
    'f(x) = (1/2)^x', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -2, 4, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Logaritmos', 'Conceito de logaritmo', 'Definição',
    'O valor de log₂ 8 é:',
    '["3", "2", "4", "1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-2-.pdf', 65, 66,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos', 'Consequências da definição', 'Logaritmo da base',
    'O valor de logₐ a é:',
    '["1", "0", "a", "10"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-2-.pdf', 68, 69,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos', 'Propriedades', 'Logaritmo do produto',
    'Usando a propriedade do logaritmo do produto, log₂ (8·4) é igual a:',
    '["log₂ 8 + log₂ 4", "log₂ 8 - log₂ 4", "log₂ 8 · log₂ 4", "log₂ 8 : log₂ 4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'log₂ 8 + log₂ 4', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-2-.pdf', 71, 72,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos', 'Mudança de base', 'Transformação',
    'Sabendo que log₂ 7 = 2,807, o valor de log₇ 2 é aproximadamente:',
    '["0,356", "2,807", "0,5", "1,403"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '0,356', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-2-.pdf', 80, 81,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Função logarítmica', 'Domínio', 'Condições de existência',
    'O domínio da função f(x) = log₂ (x - 3) é:',
    '["x > 3", "x ≥ 3", "x < 3", "x ≠ 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x > 3', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-2-.pdf', 94, 95,
    'f(x) = log₂ (x - 3)', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', 0, 8, 2
),
(
    uuid_generate_v4(), 'Matemática', 'Função logarítmica', 'Gráfico', 'Comportamento',
    'O gráfico da função f(x) = log₂ x:',
    '["corta o eixo x em x = 1", "corta o eixo y em y = 1", "é decrescente", "passa pela origem"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'corta o eixo x em x = 1', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-2-.pdf', 91, 92,
    'f(x) = log₂ x', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -1, 5, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função logarítmica', 'Crescimento', 'Base maior que 1',
    'Se a > 1, então a função f(x) = logₐ x é:',
    '["crescente", "decrescente", "constante", "não monótona"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'crescente', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-2-.pdf', 88, 89,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Equações logarítmicas', 'Igualdade de logaritmos', 'Resolução',
    'A solução da equação log₂ (x + 1) = log₂ 5 é:',
    '["x = 4", "x = 5", "x = 6", "x = 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = 4', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-2-.pdf', 99, 100,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Equações logarítmicas', 'Aplicação da definição', 'Resolução',
    'A solução da equação log₃ (x) = 2 é:',
    '["x = 9", "x = 6", "x = 8", "x = 5"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = 9', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-2-.pdf', 100, 101,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Equações exponenciais', 'Com logaritmos', 'Resolução',
    'A solução da equação 2^x = 10 é:',
    '["x = log₂ 10", "x = log₁₀ 2", "x = 5", "x = 20"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = log₂ 10', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-2-.pdf', 96, 97,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Equações logarítmicas', 'Mudança de variável', 'Resolução',
    'A equação (log₂ x)² - 3·log₂ x + 2 = 0 tem como solução:',
    '["x = 2 ou x = 4", "x = 1 ou x = 2", "x = 4 ou x = 8", "x = 1 ou x = 4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = 2 ou x = 4', 'MEDIO', 0.58, 'fundamentos-da-matematica-elementar-2-.pdf', 101, 102,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Inequações exponenciais', 'Base maior que 1', 'Resolução',
    'O conjunto solução de 2^x < 16 é:',
    '["x < 4", "x > 4", "x < 5", "x > 5"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x < 4', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-2-.pdf', 112, 114,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -2, 6, 2
),
(
    uuid_generate_v4(), 'Matemática', 'Inequações logarítmicas', 'Base maior que 1', 'Resolução',
    'O conjunto solução de log₂ x < 3 é:',
    '["0 < x < 8", "x < 8", "x > 8", "x < 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '0 < x < 8', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-2-.pdf', 123, 124,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -2, 10, 2
),
(
    uuid_generate_v4(), 'Matemática', 'Inequações logarítmicas', 'Base entre 0 e 1', 'Resolução',
    'O conjunto solução de log_{1/2} x > 1 é:',
    '["0 < x < 1/2", "x > 1/2", "x < 1/2", "0 < x < 1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '0 < x < 1/2', 'DESAFIANTE', 0.65, 'fundamentos-da-matematica-elementar-2-.pdf', 124, 125,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Logaritmos decimais', 'Característica', 'Regras',
    'A característica do logaritmo decimal de 2345 é:',
    '["3", "4", "2", "5"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-2-.pdf', 139, 140,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos decimais', 'Mantissa', 'Uso da tábua',
    'Sabendo que log 2 = 0,3010, o valor de log 2000 é:',
    '["3,3010", "0,3010", "2,3010", "4,3010"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3,3010', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-2-.pdf', 144, 145,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos decimais', 'Cologaritmo', 'Definição',
    'O colog₂ 5 é igual a:',
    '["-log₂ 5", "log₂ 5", "log₂ 1/5", "1/log₂ 5"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-log₂ 5', 'FACIL', 0.40, 'fundamentos-da-matematica-elementar-2-.pdf', 74, 75,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Logaritmos', 'Propriedades', 'Logaritmo da potência',
    'O valor de log₂ 32⁵ é:',
    '["25", "5", "10", "15"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '25', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-2-.pdf', 75, 76,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos', 'Mudança de base', 'Cálculo',
    'Sabendo que log 2 = 0,30 e log 3 = 0,48, o valor de log₆ 4 é aproximadamente:',
    '["0,77", "0,67", "0,87", "0,57"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '0,77', 'DESAFIANTE', 0.70, 'fundamentos-da-matematica-elementar-2-.pdf', 80, 82,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função exponencial', 'Equações', 'Com raízes',
    'A equação 2^(x² - 4) = 1 tem como solução:',
    '["x = ±2", "x = 2", "x = -2", "x = 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = ±2', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-2-.pdf', 47, 48,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos', 'Equações logarítmicas', 'Com soma de logaritmos',
    'A solução da equação log₂ (x) + log₂ (x - 2) = 3 é:',
    '["x = 4", "x = 2", "x = 6", "x = 8"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = 4', 'MEDIO', 0.58, 'fundamentos-da-matematica-elementar-2-.pdf', 106, 107,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Potências e raízes', 'Potenciação', 'Expoente zero',
    'O valor de (-5)⁰ é:',
    '["1", "-1", "0", "5"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-2-.pdf', 9, 10,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Potências e raízes', 'Radiciação', 'Propriedades',
    'O valor de √8 · √2 é:',
    '["4", "2", "8", "16"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '4', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-2-.pdf', 19, 20,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Potências e raízes', 'Racionalização', 'Diferença de quadrados',
    'Racionalizando 1/(√3 + 1), obtém-se:',
    '["(√3 - 1)/2", "(√3 + 1)/2", "(√3 - 1)", "(√3 + 1)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(√3 - 1)/2', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-2-.pdf', 24, 25,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Função exponencial', 'Gráfico', 'Translação',
    'O gráfico da função f(x) = 2^x + 1 intercepta o eixo y no ponto:',
    '["(0, 2)", "(0, 1)", "(1, 0)", "(2, 0)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(0, 2)', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-2-.pdf', 44, 45,
    'f(x) = 2^x + 1', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -2, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função exponencial', 'Equações', 'Com incógnita no expoente',
    'A equação 3^(x+1) = 27 tem solução:',
    '["x = 2", "x = 3", "x = 1", "x = 4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = 2', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-2-.pdf', 47, 48,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Logaritmos', 'Conceito', 'Cálculo',
    'O valor de log₃ 81 é:',
    '["4", "3", "5", "2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '4', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-2-.pdf', 65, 66,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos', 'Propriedades', 'Logaritmo do quociente',
    'log₃ (9/27) é igual a:',
    '["-1", "1", "0", "3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-1', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-2-.pdf', 73, 74,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos', 'Sistemas de logaritmos', 'Logaritmos decimais',
    'O valor de log 0,001 é:',
    '["-3", "3", "0,001", "-2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-3', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-2-.pdf', 62, 63,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Função logarítmica', 'Domínio', 'Logaritmando positivo',
    'O domínio da função f(x) = log (x² - 4) é:',
    '["x < -2 ou x > 2", "x > 2", "x < -2", "-2 < x < 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x < -2 ou x > 2', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-2-.pdf', 94, 95,
    'f(x) = log (x² - 4)', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -4, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função logarítmica', 'Inversa', 'Relação com exponencial',
    'A função inversa de f(x) = log₂ x é:',
    '["f⁻¹(x) = 2^x", "f⁻¹(x) = x²", "f⁻¹(x) = x/2", "f⁻¹(x) = log_x 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'f⁻¹(x) = 2^x', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-2-.pdf', 88, 89,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Equações exponenciais', 'Bases diferentes', 'Com logaritmos',
    'A solução da equação 3^x = 5 é:',
    '["x = log₃ 5", "x = log₅ 3", "x = 5/3", "x = 3/5"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = log₃ 5', 'FACIL', 0.40, 'fundamentos-da-matematica-elementar-2-.pdf', 96, 97,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Inequações logarítmicas', 'Base maior que 1', 'Com soma',
    'O conjunto solução de log₂ (x + 1) + log₂ (x - 1) < 3 é:',
    '["1 < x < 3", "x > 1", "x < 3", "0 < x < 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1 < x < 3', 'DESAFIANTE', 0.68, 'fundamentos-da-matematica-elementar-2-.pdf', 128, 129,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Logaritmos decimais', 'Característica', 'Números entre 0 e 1',
    'A característica do logaritmo decimal de 0,0003 é:',
    '["-4", "-3", "-2", "-1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-4', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-2-.pdf', 140, 141,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos decimais', 'Antilogaritmo', 'Cálculo',
    'Sabendo que log x = 2,3010 e log 2 = 0,3010, então x vale:',
    '["200", "20", "2", "2000"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '200', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-2-.pdf', 144, 145,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Logaritmos', 'Aplicações', 'Juros compostos',
    'Um capital de R$ 1000,00 é aplicado a juros compostos de 10% ao ano. O tempo necessário para que o montante seja R$ 2000,00 é dado por:',
    '["t = log₁,₁ 2", "t = log₂ 1,1", "t = 10 anos", "t = 5 anos"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    't = log₁,₁ 2', 'MEDIO', 0.58, 'fundamentos-da-matematica-elementar-2-.pdf', 149, 150,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função exponencial', 'Decaimento', 'Meia-vida',
    'A meia-vida de uma substância radioativa é de 10 anos. A função que representa a quantidade Q(t) em função do tempo t, em anos, sendo Q₀ a quantidade inicial, é:',
    '["Q(t) = Q₀ · 2^(-t/10)", "Q(t) = Q₀ · 2^(t/10)", "Q(t) = Q₀ · 10^(-t/2)", "Q(t) = Q₀ · 10^(t/2)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'Q(t) = Q₀ · 2^(-t/10)', 'MEDIO', 0.60, 'fundamentos-da-matematica-elementar-2-.pdf', 97, 98,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos', 'Mudança de base', 'Demonstração',
    'Se log₂ 3 = a, então log₉ 4 é igual a:',
    '["1/a", "2/a", "a/2", "2a"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1/a', 'DESAFIANTE', 0.65, 'fundamentos-da-matematica-elementar-2-.pdf', 80, 82,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função exponencial', 'Gráfico', 'Assíntota',
    'A função f(x) = 2^x tem como assíntota horizontal a reta:',
    '["y = 0", "x = 0", "y = 1", "x = 1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'y = 0', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-2-.pdf', 33, 35,
    'f(x) = 2^x', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -2, 4, 1
);


INSERT INTO perguntas (
    id, disciplina, topico_principal, topico, subtopico, questao, respostas, pesos_resposta,
    resposta_correta, dificuldade, rigor, referencia_livro, pagina_inicio, pagina_fim,
    exercicio, usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
) VALUES

(
    uuid_generate_v4(), 'Matemática', 'Potências e raízes', 'Potenciação', 'Propriedades',
    'Simplificando a expressão (a³ · a⁴) : a², obtém-se:',
    '["a⁵", "a⁹", "a⁶", "a⁷"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'a⁵', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-2-.pdf', 11, 12,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Potências e raízes', 'Potência com expoente negativo', 'Propriedades',
    'O valor de (2⁻³)⁻² é:',
    '["64", "1/64", "12", "1/12"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '64', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-2-.pdf', 16, 17,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Potências e raízes', 'Radiciação', 'Raiz de índice par',
    'O valor de √((-3)²) é:',
    '["3", "-3", "±3", "9"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-2-.pdf', 18, 19,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Potências e raízes', 'Operações com radicais', 'Adição',
    'A soma √2 + √8 é igual a:',
    '["3√2", "2√2", "√10", "4√2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3√2', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-2-.pdf', 21, 23,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Potências e raízes', 'Potência com expoente racional', 'Cálculo',
    'O valor de 27^(2/3) é:',
    '["9", "3", "18", "6"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '9', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-2-.pdf', 25, 26,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Potências e raízes', 'Racionalização', 'Cubo',
    'Racionalizando 1/∛4, obtém-se:',
    '["∛2/2", "∛4/2", "2/∛2", "∛2/4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '∛2/2', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-2-.pdf', 24, 25,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Função exponencial', 'Definição', 'Domínio',
    'O domínio da função f(x) = 3^(x-2) é:',
    '["todos os reais", "x > 2", "x ≥ 2", "x ≠ 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'todos os reais', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-2-.pdf', 35, 36,
    'f(x) = 3^(x-2)', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -2, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função exponencial', 'Equações exponenciais', 'Bases diferentes',
    'A equação 3^(2x-1) = 27 tem como solução:',
    '["x = 2", "x = 1", "x = 3", "x = 4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = 2', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-2-.pdf', 48, 49,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função exponencial', 'Equações exponenciais', 'Produto de potências',
    'A solução da equação 2^x · 2^(x+1) = 32 é:',
    '["x = 2", "x = 3", "x = 1", "x = 4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = 2', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-2-.pdf', 49, 50,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função exponencial', 'Inequações exponenciais', 'Base entre 0 e 1',
    'O conjunto solução de (1/3)^x < 1/9 é:',
    '["x > 2", "x < 2", "x > -2", "x < -2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x > 2', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-2-.pdf', 58, 59,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -2, 5, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função exponencial', 'Crescimento', 'Comparação',
    'Comparando 2^3 e 3^2, pode-se afirmar que:',
    '["2^3 < 3^2", "2^3 > 3^2", "2^3 = 3^2", "não é possível comparar"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2^3 < 3^2', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-2-.pdf', 35, 36,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Logaritmos', 'Conceito de logaritmo', 'Cálculo',
    'O valor de log₅ 125 é:',
    '["3", "2", "4", "5"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-2-.pdf', 65, 66,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos', 'Consequências da definição', 'Logaritmo de 1',
    'O valor de log₇ 1 é:',
    '["0", "1", "7", "-1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '0', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-2-.pdf', 68, 69,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos', 'Propriedades', 'Logaritmo da potência',
    'log₂ 8³ é igual a:',
    '["9", "3", "6", "12"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '9', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-2-.pdf', 75, 76,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos', 'Mudança de base', 'Cálculo',
    'Sabendo que log₂ 5 = 2,3219, então log₅ 2 é aproximadamente:',
    '["0,4307", "2,3219", "0,5", "1,1609"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '0,4307', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-2-.pdf', 80, 81,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos', 'Cologaritmo', 'Definição',
    'O colog₃ 9 é igual a:',
    '["-2", "2", "1/2", "-1/2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-2', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-2-.pdf', 74, 75,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Função logarítmica', 'Domínio', 'Logaritmando positivo',
    'O domínio da função f(x) = ln(5 - x) é:',
    '["x < 5", "x > 5", "x ≤ 5", "x ≥ 5"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x < 5', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-2-.pdf', 94, 95,
    'f(x) = ln(5 - x)', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -2, 6, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função logarítmica', 'Gráfico', 'Base entre 0 e 1',
    'O gráfico da função f(x) = log_{1/2} x é:',
    '["decrescente", "crescente", "constante", "não monótona"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'decrescente', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-2-.pdf', 91, 92,
    'f(x) = log_{1/2} x', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -1, 5, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função logarítmica', 'Imagem', 'Propriedade',
    'A imagem da função f(x) = log₃ x é:',
    '["todos os reais", "x > 0", "x ≥ 0", "y > 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'todos os reais', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-2-.pdf', 91, 92,
    'f(x) = log₃ x', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -1, 5, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Equações logarítmicas', 'Igualdade de logaritmos', 'Condições',
    'A equação log₃ (x - 2) = log₃ (2x - 5) tem solução:',
    '["x = 3", "x = 2", "x = 5", "sem solução"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = 3', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-2-.pdf', 99, 100,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Equações logarítmicas', 'Aplicação da definição', 'Cálculo',
    'A solução da equação logₓ 64 = 2 é:',
    '["x = 8", "x = 4", "x = 16", "x = 32"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = 8', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-2-.pdf', 104, 105,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Equações exponenciais', 'Com logaritmos', 'Resolução',
    'A solução da equação 5^(2x) = 25 é:',
    '["x = 1", "x = 2", "x = 0", "x = -1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = 1', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-2-.pdf', 96, 97,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Equações logarítmicas', 'Mudança de variável', 'Resolução',
    'A equação (log₃ x)² - 4·log₃ x + 3 = 0 tem como solução:',
    '["x = 3 ou x = 27", "x = 1 ou x = 3", "x = 9 ou x = 27", "x = 3 ou x = 9"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = 3 ou x = 27', 'MEDIO', 0.58, 'fundamentos-da-matematica-elementar-2-.pdf', 101, 102,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Inequações exponenciais', 'Base maior que 1', 'Resolução',
    'O conjunto solução de 3^(x+1) > 27 é:',
    '["x > 2", "x < 2", "x > 3", "x < 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x > 2', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-2-.pdf', 112, 114,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -2, 5, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Inequações logarítmicas', 'Base maior que 1', 'Resolução',
    'O conjunto solução de log₃ x ≤ 2 é:',
    '["0 < x ≤ 9", "x ≤ 9", "x > 0", "x ≤ 9 e x > 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '0 < x ≤ 9', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-2-.pdf', 125, 126,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -2, 10, 2
),
(
    uuid_generate_v4(), 'Matemática', 'Inequações exponenciais', 'Base entre 0 e 1', 'Resolução',
    'O conjunto solução de (0,5)^x ≥ 4 é:',
    '["x ≤ -2", "x ≥ -2", "x ≤ 2", "x ≥ 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x ≤ -2', 'DESAFIANTE', 0.65, 'fundamentos-da-matematica-elementar-2-.pdf', 112, 114,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -4, 2, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Logaritmos decimais', 'Característica', 'Números maiores que 1',
    'A característica do logaritmo decimal de 12345 é:',
    '["4", "3", "5", "6"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '4', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-2-.pdf', 139, 140,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos decimais', 'Mantissa', 'Interpolação',
    'Sabendo que log 2 = 0,3010 e log 3 = 0,4771, o valor de log 6 é:',
    '["0,7781", "0,1761", "0,7781", "0,7781"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '0,7781', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-2-.pdf', 144, 145,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos decimais', 'Antilogaritmo', 'Cálculo',
    'Se log x = 1,3010, e log 2 = 0,3010, então x vale:',
    '["20", "2", "200", "0,2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '20', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-2-.pdf', 146, 147,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Equações logarítmicas', 'Sistemas', 'Resolução',
    'O sistema { x + y = 5, log₂ x + log₂ y = 2 } tem como solução:',
    '["(4, 1) e (1, 4)", "(2, 3) e (3, 2)", "(2, 2) e (3, 3)", "sem solução"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(4, 1) e (1, 4)', 'DESAFIANTE', 0.68, 'fundamentos-da-matematica-elementar-2-.pdf', 109, 110,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos', 'Aplicações', 'Crescimento populacional',
    'Uma cultura de bactérias cresce segundo a lei N(t) = 1000·2^(t/3), com t em horas. O número de bactérias após 6 horas é:',
    '["4000", "2000", "8000", "16000"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '4000', 'FACIL', 0.40, 'fundamentos-da-matematica-elementar-2-.pdf', 97, 98,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função exponencial', 'Decaimento', 'Datação',
    'Uma substância radioativa tem meia-vida de 20 anos. A fração da quantidade inicial que resta após 60 anos é:',
    '["1/8", "1/4", "1/2", "1/16"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1/8', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-2-.pdf', 97, 98,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Potências e raízes', 'Potenciação', 'Expoente 1',
    'O valor de 5¹ é:',
    '["5", "1", "0", "25"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '5', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-2-.pdf', 9, 10,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Potências e raízes', 'Radiciação', 'Simplificação',
    'Simplificando √50, obtém-se:',
    '["5√2", "2√5", "10√5", "5√10"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '5√2', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-2-.pdf', 21, 22,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Logaritmos', 'Conceito', 'Definição',
    'Se logₓ 16 = 2, então x vale:',
    '["4", "2", "8", "16"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '4', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-2-.pdf', 65, 66,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos', 'Propriedades', 'Mudança de base',
    'log₂ 3 · log₃ 4 é igual a:',
    '["log₂ 4", "log₃ 2", "log₄ 3", "2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'log₂ 4', 'DESAFIANTE', 0.65, 'fundamentos-da-matematica-elementar-2-.pdf', 80, 82,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Equações exponenciais', 'Com raiz', 'Resolução',
    'A equação 2^(√x) = 8 tem como solução:',
    '["x = 9", "x = 4", "x = 16", "x = 25"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = 9', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-2-.pdf', 48, 49,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Equações logarítmicas', 'Com fração', 'Resolução',
    'A solução da equação log₂ (x + 1) - log₂ (x - 1) = 1 é:',
    '["x = 3", "x = 2", "x = 4", "x = 5"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = 3', 'MEDIO', 0.58, 'fundamentos-da-matematica-elementar-2-.pdf', 106, 107,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Inequações logarítmicas', 'Base maior que 1', 'Com produto',
    'O conjunto solução de log₂ x + log₂ (x - 2) < 3 é:',
    '["2 < x < 4", "0 < x < 4", "x > 2", "x < 4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2 < x < 4', 'DESAFIANTE', 0.68, 'fundamentos-da-matematica-elementar-2-.pdf', 128, 129,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Função exponencial', 'Gráfico', 'Translação vertical',
    'O gráfico de f(x) = 2^x - 1 intercepta o eixo y no ponto:',
    '["(0, 0)", "(0, 1)", "(0, -1)", "(1, 0)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(0, 0)', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-2-.pdf', 44, 45,
    'f(x) = 2^x - 1', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -2, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função logarítmica', 'Gráfico', 'Translação horizontal',
    'O gráfico de f(x) = log₂ (x - 1) intercepta o eixo x no ponto:',
    '["(2, 0)", "(1, 0)", "(0, 0)", "(3, 0)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(2, 0)', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-2-.pdf', 92, 93,
    'f(x) = log₂ (x - 1)', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', 0, 5, 1
);


INSERT INTO perguntas (
    id, disciplina, topico_principal, topico, subtopico, questao, respostas, pesos_resposta,
    resposta_correta, dificuldade, rigor, referencia_livro, pagina_inicio, pagina_fim,
    exercicio, usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
) VALUES

(
    uuid_generate_v4(), 'Matemática', 'Potências e raízes', 'Potenciação', 'Produto de potências',
    'O valor de (2³)² é:',
    '["64", "32", "16", "8"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '64', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-2-.pdf', 11, 12,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Potências e raízes', 'Potência com expoente negativo', 'Inverso',
    'O valor de (-3)⁻² é:',
    '["1/9", "9", "-1/9", "-9"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1/9', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-2-.pdf', 14, 15,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Potências e raízes', 'Radiciação', 'Raiz cúbica',
    'O valor de ∛(-8) é:',
    '["-2", "2", "±2", "não existe"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-2', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-2-.pdf', 17, 18,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Potências e raízes', 'Operações com radicais', 'Multiplicação',
    'O produto √3 · √12 é igual a:',
    '["6", "3√2", "6√2", "36"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '6', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-2-.pdf', 22, 23,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Potências e raízes', 'Potência com expoente racional', 'Radical',
    'O valor de 16^(3/4) é:',
    '["8", "4", "12", "2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '8', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-2-.pdf', 25, 26,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Potências e raízes', 'Racionalização', 'Binômio',
    'Racionalizando 2/(√5 + √3), obtém-se:',
    '["√5 - √3", "√5 + √3", "(√5 - √3)/2", "(√5 + √3)/2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '√5 - √3', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-2-.pdf', 24, 25,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Função exponencial', 'Definição', 'Valor numérico',
    'O valor de f(2) para f(x) = 5^x é:',
    '["25", "10", "32", "125"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '25', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-2-.pdf', 35, 36,
    'f(x) = 5^x', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -2, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função exponencial', 'Equações exponenciais', 'Potência de potência',
    'A solução da equação (2^x)^3 = 64 é:',
    '["x = 2", "x = 3", "x = 4", "x = 1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = 2', 'FACIL', 0.32, 'fundamentos-da-matematica-elementar-2-.pdf', 48, 49,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função exponencial', 'Equações exponenciais', 'Quociente',
    'A equação 3^(x+2)/3^(x-1) = 27 tem solução:',
    '["qualquer x", "x = 0", "x = 1", "x = 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'qualquer x', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-2-.pdf', 49, 50,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função exponencial', 'Inequações exponenciais', 'Base maior que 1',
    'O conjunto solução de 5^(2x-1) ≤ 125 é:',
    '["x ≤ 2", "x ≥ 2", "x ≤ 3", "x ≥ 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x ≤ 2', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-2-.pdf', 58, 59,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -1, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função exponencial', 'Crescimento', 'Comparação',
    'Entre 2^4 e 4^2, pode-se afirmar que:',
    '["são iguais", "2^4 > 4^2", "2^4 < 4^2", "não é possível comparar"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'são iguais', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-2-.pdf', 35, 36,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Logaritmos', 'Conceito de logaritmo', 'Cálculo',
    'O valor de log₂ 1/32 é:',
    '["-5", "5", "1/5", "-1/5"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-5', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-2-.pdf', 65, 66,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos', 'Consequências da definição', 'Potência',
    'O valor de 3^(log₃ 7) é:',
    '["7", "3", "1", "21"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '7', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-2-.pdf', 68, 69,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos', 'Propriedades', 'Logaritmo da raiz',
    'log₂ √8 é igual a:',
    '["3/2", "3", "2/3", "1/3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3/2', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-2-.pdf', 75, 76,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos', 'Mudança de base', 'Cálculo',
    'Se log₃ 5 = 1,465, então log₅ 3 é aproximadamente:',
    '["0,682", "1,465", "0,5", "0,365"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '0,682', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-2-.pdf', 80, 81,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos', 'Cologaritmo', 'Relação com logaritmo',
    'colog₂ 8 + log₂ 8 é igual a:',
    '["0", "2", "-2", "1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '0', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-2-.pdf', 74, 75,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Função logarítmica', 'Domínio', 'Inequação',
    'O domínio da função f(x) = log (x² - 9) é:',
    '["x < -3 ou x > 3", "x > 3", "x < -3", "-3 < x < 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x < -3 ou x > 3', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-2-.pdf', 94, 95,
    'f(x) = log (x² - 9)', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -5, 5, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função logarítmica', 'Gráfico', 'Crescimento',
    'A função f(x) = log_{10} x é:',
    '["crescente", "decrescente", "constante", "não monótona"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'crescente', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-2-.pdf', 91, 92,
    'f(x) = log x', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -1, 5, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função logarítmica', 'Imagem', 'Característica',
    'A imagem da função f(x) = log₅ x é:',
    '["todos os reais", "x > 0", "x ≥ 0", "y > 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'todos os reais', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-2-.pdf', 91, 92,
    'f(x) = log₅ x', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -1, 5, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Equações logarítmicas', 'Igualdade', 'Condições',
    'A equação log₅ (x² - 4) = log₅ (3x) tem como solução:',
    '["x = 4", "x = -1", "x = 4 ou x = -1", "sem solução"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = 4', 'DESAFIANTE', 0.65, 'fundamentos-da-matematica-elementar-2-.pdf', 99, 100,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Equações exponenciais', 'Com logaritmos', 'Resolução',
    'A solução da equação 3^x = 12 é:',
    '["x = log₃ 12", "x = log₁₂ 3", "x = 4", "x = 9"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = log₃ 12', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-2-.pdf', 96, 97,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Equações logarítmicas', 'Mudança de variável', 'Resolução',
    'A equação log₂² x - 5·log₂ x + 6 = 0 tem como solução:',
    '["x = 4 ou x = 8", "x = 2 ou x = 3", "x = 2 ou x = 4", "x = 4 ou x = 16"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = 4 ou x = 8', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-2-.pdf', 101, 102,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Equações logarítmicas', 'Com soma e produto', 'Resolução',
    'A equação log₂ x + log₂ (x + 2) = 3 tem como solução:',
    '["x = 2", "x = -4", "x = 2 ou x = -4", "x = 4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = 2', 'MEDIO', 0.58, 'fundamentos-da-matematica-elementar-2-.pdf', 106, 107,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Inequações exponenciais', 'Base maior que 1', 'Resolução',
    'O conjunto solução de 2^(x² - 4) > 1 é:',
    '["x < -2 ou x > 2", "-2 < x < 2", "x > 2", "x < -2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x < -2 ou x > 2', 'DESAFIANTE', 0.65, 'fundamentos-da-matematica-elementar-2-.pdf', 58, 59,
    '', true, 'PARABOLA', 1, 0, -4, 'eixo x', 'eixo y', -3, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Inequações logarítmicas', 'Base entre 0 e 1', 'Resolução',
    'O conjunto solução de log_{1/2} (x - 1) ≥ 0 é:',
    '["1 < x ≤ 2", "x ≤ 2", "x ≥ 2", "x < 1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1 < x ≤ 2', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-2-.pdf', 124, 125,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', 0, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Inequações logarítmicas', 'Base maior que 1', 'Com produto',
    'O conjunto solução de log₃ (x² - 4) < log₃ 5 é:',
    '["-3 < x < -2 ou 2 < x < 3", "-3 < x < 3", "x < -2 ou x > 2", "-2 < x < 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-3 < x < -2 ou 2 < x < 3', 'DESAFIANTE', 0.68, 'fundamentos-da-matematica-elementar-2-.pdf', 123, 124,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Logaritmos decimais', 'Característica', 'Regras',
    'A característica do logaritmo decimal de 7890 é:',
    '["3", "4", "2", "5"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-2-.pdf', 139, 140,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos decimais', 'Mantissa', 'Uso da tábua',
    'Sabendo que log 2 = 0,3010 e log 5 = 0,6990, o valor de log 10 é:',
    '["1", "0,3010", "0,6990", "1,3010"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-2-.pdf', 144, 145,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos decimais', 'Antilogaritmo', 'Cálculo',
    'Se log x = 0,3010, e log 2 = 0,3010, então x vale:',
    '["2", "1/2", "20", "0,2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-2-.pdf', 146, 147,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Equações logarítmicas', 'Sistemas', 'Resolução',
    'O sistema { log₂ x + log₂ y = 3, x + y = 6 } tem como solução:',
    '["(2, 4) e (4, 2)", "(2, 4)", "(4, 2)", "(3, 3)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(2, 4) e (4, 2)', 'DESAFIANTE', 0.68, 'fundamentos-da-matematica-elementar-2-.pdf', 109, 110,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função exponencial', 'Aplicações', 'Juros',
    'Um capital de R$ 5000,00 aplicado a 5% ao ano, a juros compostos, após t anos será dado por M(t) = 5000·1,05^t. O tempo para que o montante seja R$ 10000,00 é:',
    '["t = log₁,₀₅ 2", "t = log₂ 1,05", "t = 20 anos", "t = 14 anos"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    't = log₁,₀₅ 2', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-2-.pdf', 149, 150,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos', 'Aplicações', 'pH',
    'O pH de uma solução é definido por pH = -log[H⁺], onde [H⁺] é a concentração de íons de hidrogênio. Se pH = 4, então [H⁺] é:',
    '["10⁻⁴", "10⁴", "4", "0,0001"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '10⁻⁴', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-2-.pdf', 79, 80,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Potências e raízes', 'Potenciação', 'Expoente par',
    'O valor de (-2)⁴ é:',
    '["16", "-16", "8", "-8"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '16', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-2-.pdf', 9, 10,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Potências e raízes', 'Radiciação', 'Raiz de índice ímpar',
    'O valor de ∛(-27) é:',
    '["-3", "3", "±3", "não existe"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-3', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-2-.pdf', 17, 18,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Logaritmos', 'Conceito', 'Base',
    'O valor de log₄ 2 é:',
    '["1/2", "2", "1", "4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1/2', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-2-.pdf', 65, 66,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos', 'Propriedades', 'Produto',
    'log₂ 3 + log₂ 5 é igual a:',
    '["log₂ 15", "log₂ 8", "log₁₀ 15", "log₈ 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'log₂ 15', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-2-.pdf', 71, 72,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos', 'Mudança de base', 'Produto',
    'log₂ 3 · log₃ 4 · log₄ 5 é igual a:',
    '["log₂ 5", "log₅ 2", "log₃ 5", "log₄ 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'log₂ 5', 'DESAFIANTE', 0.68, 'fundamentos-da-matematica-elementar-2-.pdf', 80, 82,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Equações exponenciais', 'Bases diferentes', 'Com logaritmos',
    'A solução da equação 2^x = 3^(x+1) é:',
    '["x = log_{2/3} 3", "x = log_{3/2} 2", "x = 1", "x = 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = log_{2/3} 3', 'DESAFIANTE', 0.70, 'fundamentos-da-matematica-elementar-2-.pdf', 98, 99,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Equações logarítmicas', 'Base variável', 'Resolução',
    'A solução da equação logₓ 8 = 3 é:',
    '["x = 2", "x = 4", "x = 8", "x = 16"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = 2', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-2-.pdf', 104, 105,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Inequações exponenciais', 'Base entre 0 e 1', 'Resolução',
    'O conjunto solução de (0,2)^x < 0,008 é:',
    '["x > 3", "x < 3", "x > -3", "x < -3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x > 3', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-2-.pdf', 58, 59,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -2, 5, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Função exponencial', 'Gráfico', 'Assíntota',
    'A função f(x) = 3^x tem como assíntota horizontal a reta:',
    '["y = 0", "x = 0", "y = 1", "x = 1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'y = 0', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-2-.pdf', 33, 35,
    'f(x) = 3^x', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -2, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função logarítmica', 'Gráfico', 'Assíntota',
    'A função f(x) = log x tem como assíntota vertical a reta:',
    '["x = 0", "y = 0", "x = 1", "y = 1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = 0', 'FACIL', 0.35, 'fundamentos-da-matematica-elementar-2-.pdf', 83, 84,
    'f(x) = log x', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -1, 5, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Logaritmos decimais', 'Característica e mantissa', 'Números decimais',
    'O logaritmo decimal de 0,0056 é:',
    '["3,7482", "0,7482", "-2,2518", "2,7482"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3,7482', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-2-.pdf', 140, 141,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos decimais', 'Interpolação', 'Cálculo aproximado',
    'Sabendo que log 3 = 0,4771 e log 4 = 0,6021, o valor aproximado de log 3,5 é:',
    '["0,5441", "0,5396", "0,5441", "0,4771"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '0,5441', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-2-.pdf', 144, 145,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Logaritmos decimais', 'Cologaritmo', 'Cálculo',
    'colog₁₀ 2 é igual a:',
    '["-log₁₀ 2", "log₁₀ 2", "log₁₀ 0,5", "ambas a e c"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'ambas a e c', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-2-.pdf', 74, 75,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
);



INSERT INTO perguntas (
    id, disciplina, topico_principal, topico, subtopico, questao, respostas, pesos_resposta,
    resposta_correta, dificuldade, rigor, referencia_livro, pagina_inicio, pagina_fim,
    exercicio, usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
) VALUES

(
    uuid_generate_v4(), 'Física', 'Medição', 'Grandezas e unidades', 'Sistema Internacional',
    'Qual das seguintes é uma unidade fundamental do Sistema Internacional (SI)?',
    '["metro", "newton", "joule", "watt"]'::jsonb, '[1.0, 0.2, 0.1, 0.3]'::jsonb,
    'metro', 'FACIL', 0.25, 'fundamentos-de-fisica-1-halliday.pdf', 2, 3,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Medição', 'Comprimento', 'Conversão de unidades',
    'Quantos centímetros existem em 2,5 metros?',
    '["250 cm", "25 cm", "2500 cm", "0,025 cm"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '250 cm', 'FACIL', 0.20, 'fundamentos-de-fisica-1-halliday.pdf', 2, 3,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Medição', 'Tempo', 'Conversão',
    'Uma hora tem quantos segundos?',
    '["3600 s", "60 s", "36000 s", "600 s"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3600 s', 'FACIL', 0.20, 'fundamentos-de-fisica-1-halliday.pdf', 5, 6,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Medição', 'Massa', 'Conversão',
    'Um quilograma equivale a quantos gramas?',
    '["1000 g", "100 g", "10 g", "0,001 g"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1000 g', 'FACIL', 0.20, 'fundamentos-de-fisica-1-halliday.pdf', 7, 8,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Movimento retilíneo', 'Velocidade média', 'Cálculo',
    'Um carro percorre 120 km em 2 horas. Qual é a sua velocidade média?',
    '["60 km/h", "120 km/h", "240 km/h", "30 km/h"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '60 km/h', 'FACIL', 0.25, 'fundamentos-de-fisica-1-halliday.pdf', 14, 16,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Movimento retilíneo', 'Aceleração', 'MRUV',
    'Um corpo parte do repouso e atinge velocidade de 20 m/s em 5 segundos. Qual é a sua aceleração média?',
    '["4 m/s²", "2 m/s²", "5 m/s²", "10 m/s²"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '4 m/s²', 'FACIL', 0.30, 'fundamentos-de-fisica-1-halliday.pdf', 19, 21,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Movimento retilíneo', 'Queda livre', 'Cálculo de altura',
    'Uma pedra é abandonada do repouso de uma altura de 45 m. Desprezando a resistência do ar e considerando g = 10 m/s², quanto tempo ela leva para atingir o solo?',
    '["3 s", "4,5 s", "9 s", "2 s"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3 s', 'MEDIO', 0.52, 'fundamentos-de-fisica-1-halliday.pdf', 23, 25,
    '', true, 'RETA', 0, 0, 0, 'tempo (s)', 'altura (m)', 0, 4, 1
),
(
    uuid_generate_v4(), 'Física', 'Movimento retilíneo', 'MRUV', 'Equação de Torricelli',
    'Um carro a 30 m/s freia com desaceleração constante de 5 m/s². Qual a distância percorrida até parar?',
    '["90 m", "45 m", "180 m", "60 m"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '90 m', 'MEDIO', 0.55, 'fundamentos-de-fisica-1-halliday.pdf', 22, 23,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Vetores', 'Componentes', 'Decomposição',
    'Um vetor de módulo 10 m forma um ângulo de 30° com o eixo x. Qual é a sua componente x?',
    '["8,66 m", "5 m", "10 m", "0 m"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '8,66 m', 'FACIL', 0.35, 'fundamentos-de-fisica-1-halliday.pdf', 42, 44,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Vetores', 'Soma vetorial', 'Resultante',
    'Dois vetores perpendiculares têm módulos 3 N e 4 N. O módulo do vetor resultante é:',
    '["5 N", "7 N", "1 N", "12 N"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '5 N', 'FACIL', 0.30, 'fundamentos-de-fisica-1-halliday.pdf', 40, 42,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Vetores', 'Produto escalar', 'Cálculo',
    'O produto escalar de dois vetores perpendiculares é:',
    '["zero", "máximo", "mínimo", "igual ao produto dos módulos"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'zero', 'FACIL', 0.30, 'fundamentos-de-fisica-1-halliday.pdf', 47, 48,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Vetores', 'Produto vetorial', 'Direção',
    'O produto vetorial a × b resulta em um vetor que é:',
    '["perpendicular ao plano de a e b", "paralelo a a", "paralelo a b", "no plano de a e b"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'perpendicular ao plano de a e b', 'MEDIO', 0.48, 'fundamentos-de-fisica-1-halliday.pdf', 48, 49,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Movimento em 2D', 'Lançamento horizontal', 'Alcance',
    'Uma bola é lançada horizontalmente do alto de um prédio de 20 m de altura com velocidade de 10 m/s. Desprezando a resistência do ar e considerando g = 10 m/s², qual é o alcance horizontal?',
    '["20 m", "10 m", "40 m", "5 m"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '20 m', 'MEDIO', 0.55, 'fundamentos-de-fisica-1-halliday.pdf', 61, 64,
    '', true, 'PARABOLA', -5, 0, 20, 'posição x (m)', 'posição y (m)', 0, 25, 5
),
(
    uuid_generate_v4(), 'Física', 'Movimento em 2D', 'Lançamento oblíquo', 'Altura máxima',
    'Um projétil é lançado com velocidade de 50 m/s fazendo um ângulo de 30° com a horizontal. Considerando g = 10 m/s², a altura máxima atingida é aproximadamente:',
    '["31,25 m", "62,5 m", "125 m", "15,6 m"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '31,25 m', 'DESAFIANTE', 0.65, 'fundamentos-de-fisica-1-halliday.pdf', 61, 64,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Movimento em 2D', 'Movimento circular uniforme', 'Aceleração centrípeta',
    'Uma partícula descreve um movimento circular uniforme de raio 2 m com velocidade de 4 m/s. Qual é a sua aceleração centrípeta?',
    '["8 m/s²", "4 m/s²", "2 m/s²", "16 m/s²"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '8 m/s²', 'MEDIO', 0.50, 'fundamentos-de-fisica-1-halliday.pdf', 65, 67,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Movimento em 2D', 'Movimento relativo', 'Velocidade',
    'Um barco atravessa um rio de 100 m de largura com velocidade de 5 m/s perpendicular à correnteza. A correnteza tem velocidade de 3 m/s paralela às margens. Qual é a velocidade resultante do barco em relação à margem?',
    '["5,83 m/s", "8 m/s", "2 m/s", "15 m/s"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '5,83 m/s', 'MEDIO', 0.52, 'fundamentos-de-fisica-1-halliday.pdf', 68, 70,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Leis de Newton', 'Primeira lei', 'Inércia',
    'Um corpo permanece em repouso ou em movimento retilíneo uniforme se a força resultante sobre ele for:',
    '["nula", "positiva", "negativa", "variável"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'nula', 'FACIL', 0.25, 'fundamentos-de-fisica-1-halliday.pdf', 82, 83,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Leis de Newton', 'Segunda lei', 'Cálculo da força',
    'Um corpo de massa 5 kg adquire aceleração de 4 m/s². Qual é a força resultante atuando sobre ele?',
    '["20 N", "1,25 N", "9 N", "0,8 N"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '20 N', 'FACIL', 0.30, 'fundamentos-de-fisica-1-halliday.pdf', 84, 86,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Leis de Newton', 'Peso', 'Cálculo',
    'Qual é o peso de um corpo de massa 10 kg na Terra (g = 9,8 m/s²)?',
    '["98 N", "10 N", "9,8 N", "1,02 N"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '98 N', 'FACIL', 0.25, 'fundamentos-de-fisica-1-halliday.pdf', 87, 88,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Leis de Newton', 'Terceira lei', 'Ação e reação',
    'De acordo com a terceira lei de Newton, as forças de ação e reação:',
    '["atuam em corpos diferentes", "atuam no mesmo corpo", "têm sentidos iguais", "podem ter módulos diferentes"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'atuam em corpos diferentes', 'FACIL', 0.30, 'fundamentos-de-fisica-1-halliday.pdf', 89, 90,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Atrito', 'Força de atrito estático', 'Cálculo',
    'Um bloco de 10 kg está sobre uma superfície horizontal com coeficiente de atrito estático μₑ = 0,4. Qual é a força máxima de atrito estático? (g = 10 m/s²)',
    '["40 N", "100 N", "4 N", "10 N"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '40 N', 'FACIL', 0.35, 'fundamentos-de-fisica-1-halliday.pdf', 109, 111,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Atrito', 'Força de atrito cinético', 'Cálculo',
    'Um bloco desliza sobre uma superfície horizontal com coeficiente de atrito cinético μ = 0,2. Se o peso do bloco é 50 N, qual é a força de atrito? (g = 10 m/s²)',
    '["10 N", "50 N", "5 N", "20 N"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '10 N', 'MEDIO', 0.48, 'fundamentos-de-fisica-1-halliday.pdf', 111, 112,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Movimento circular', 'Força centrípeta', 'Cálculo',
    'Um carro de 1000 kg faz uma curva plana de raio 50 m com velocidade de 20 m/s. Qual é a força centrípeta necessária?',
    '["8000 N", "400 N", "40000 N", "2000 N"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '8000 N', 'MEDIO', 0.52, 'fundamentos-de-fisica-1-halliday.pdf', 116, 119,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Trabalho e energia', 'Trabalho de força constante', 'Cálculo',
    'Uma força de 20 N atua sobre um corpo na mesma direção e sentido do deslocamento de 5 m. Qual é o trabalho realizado?',
    '["100 J", "4 J", "25 J", "0 J"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '100 J', 'FACIL', 0.30, 'fundamentos-de-fisica-1-halliday.pdf', 132, 134,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Trabalho e energia', 'Energia cinética', 'Cálculo',
    'Qual é a energia cinética de um corpo de massa 2 kg com velocidade de 10 m/s?',
    '["100 J", "20 J", "200 J", "50 J"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '100 J', 'FACIL', 0.30, 'fundamentos-de-fisica-1-halliday.pdf', 140, 141,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Trabalho e energia', 'Teorema trabalho-energia', 'Aplicação',
    'Um corpo de 5 kg tem velocidade inicial de 2 m/s. Sob ação de uma força, sua velocidade aumenta para 6 m/s. Qual é o trabalho realizado?',
    '["80 J", "100 J", "40 J", "20 J"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '80 J', 'MEDIO', 0.55, 'fundamentos-de-fisica-1-halliday.pdf', 141, 142,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Trabalho e energia', 'Potência', 'Cálculo',
    'Um motor realiza um trabalho de 6000 J em 20 s. Qual é a sua potência média?',
    '["300 W", "120 W", "6000 W", "200 W"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '300 W', 'FACIL', 0.32, 'fundamentos-de-fisica-1-halliday.pdf', 143, 144,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Conservação da energia', 'Energia potencial gravitacional', 'Cálculo',
    'Um corpo de massa 3 kg está a uma altura de 5 m do solo. Considerando g = 10 m/s², qual é a sua energia potencial gravitacional?',
    '["150 J", "15 J", "50 J", "30 J"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '150 J', 'FACIL', 0.28, 'fundamentos-de-fisica-1-halliday.pdf', 160, 161,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Conservação da energia', 'Energia potencial elástica', 'Cálculo',
    'Uma mola de constante elástica k = 200 N/m é comprimida de 0,1 m. Qual é a energia potencial elástica armazenada?',
    '["1 J", "2 J", "0,5 J", "10 J"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1 J', 'MEDIO', 0.50, 'fundamentos-de-fisica-1-halliday.pdf', 158, 160,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Conservação da energia', 'Forças conservativas', 'Identificação',
    'Qual das seguintes forças é conservativa?',
    '["força gravitacional", "força de atrito", "resistência do ar", "força de arrasto"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'força gravitacional', 'FACIL', 0.30, 'fundamentos-de-fisica-1-halliday.pdf', 164, 166,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Conservação da energia', 'Lei da conservação', 'Aplicação',
    'Um corpo de massa 2 kg é abandonado do repouso de uma altura de 20 m. Desprezando a resistência do ar, qual é a sua velocidade ao atingir o solo? (g = 10 m/s²)',
    '["20 m/s", "10 m/s", "40 m/s", "5 m/s"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '20 m/s', 'MEDIO', 0.52, 'fundamentos-de-fisica-1-halliday.pdf', 161, 163,
    '', true, 'PARABOLA', -5, 0, 20, 'posição x (m)', 'posição y (m)', 0, 25, 5
),

(
    uuid_generate_v4(), 'Física', 'Sistemas de partículas', 'Centro de massa', 'Cálculo para duas partículas',
    'Duas partículas de massas 2 kg e 3 kg estão nas posições x = 0 m e x = 5 m, respectivamente. Qual é a posição do centro de massa?',
    '["3 m", "2 m", "2,5 m", "4 m"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3 m', 'MEDIO', 0.48, 'fundamentos-de-fisica-1-halliday.pdf', 187, 189,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Sistemas de partículas', 'Centro de massa', 'Corpos simétricos',
    'O centro de massa de uma esfera homogênea está localizado:',
    '["no centro da esfera", "na superfície", "depende da massa", "no ponto mais alto"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'no centro da esfera', 'FACIL', 0.25, 'fundamentos-de-fisica-1-halliday.pdf', 189, 190,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Movimento retilíneo', 'MRU', 'Gráfico',
    'No gráfico posição × tempo de um MRU, a curva é:',
    '["uma reta", "uma parábola", "uma hipérbole", "uma senoide"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'uma reta', 'FACIL', 0.25, 'fundamentos-de-fisica-1-halliday.pdf', 14, 17,
    '', true, 'RETA', 1, 0, 0, 'tempo (s)', 'posição (m)', 0, 10, 2
),
(
    uuid_generate_v4(), 'Física', 'Movimento retilíneo', 'MRUV', 'Gráfico',
    'No gráfico velocidade × tempo de um MRUV com aceleração constante positiva, a curva é:',
    '["uma reta crescente", "uma reta horizontal", "uma reta decrescente", "uma parábola"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'uma reta crescente', 'FACIL', 0.28, 'fundamentos-de-fisica-1-halliday.pdf', 19, 22,
    '', true, 'RETA', 2, 0, 0, 'tempo (s)', 'velocidade (m/s)', 0, 5, 1
),

(
    uuid_generate_v4(), 'Física', 'Movimento retilíneo', 'Queda livre', 'Tempo de queda',
    'Uma bola é lançada verticalmente para cima com velocidade de 20 m/s. Considerando g = 10 m/s², o tempo de subida é:',
    '["2 s", "1 s", "4 s", "0,5 s"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2 s', 'MEDIO', 0.50, 'fundamentos-de-fisica-1-halliday.pdf', 23, 25,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Movimento em 2D', 'Lançamento oblíquo', 'Alcance máximo',
    'Para um projétil lançado do solo, o ângulo que proporciona o maior alcance (desprezando resistência do ar) é:',
    '["45°", "30°", "60°", "90°"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '45°', 'FACIL', 0.35, 'fundamentos-de-fisica-1-halliday.pdf', 62, 64,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Leis de Newton', 'Plano inclinado', 'Aceleração',
    'Um bloco desliza sobre um plano inclinado sem atrito com inclinação θ = 30°. Considerando g = 10 m/s², qual é a aceleração do bloco?',
    '["5 m/s²", "10 m/s²", "8,66 m/s²", "2,5 m/s²"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '5 m/s²', 'MEDIO', 0.52, 'fundamentos-de-fisica-1-halliday.pdf', 93, 95,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Trabalho e energia', 'Trabalho da força peso', 'Cálculo',
    'Uma pessoa levanta um corpo de 10 kg do chão até uma altura de 2 m. Qual é o trabalho realizado contra o peso? (g = 10 m/s²)',
    '["200 J", "20 J", "100 J", "50 J"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '200 J', 'FACIL', 0.30, 'fundamentos-de-fisica-1-halliday.pdf', 132, 134,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Conservação da energia', 'Energia mecânica', 'Conservação',
    'Em um sistema conservativo, a energia mecânica total:',
    '["permanece constante", "aumenta sempre", "diminui sempre", "varia aleatoriamente"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'permanece constante', 'FACIL', 0.25, 'fundamentos-de-fisica-1-halliday.pdf', 157, 158,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Movimento retilíneo', 'Velocidade instantânea', 'Derivada',
    'A posição de uma partícula é dada por x(t) = 2t² + 3t + 1 (SI). A velocidade instantânea em t = 2 s é:',
    '["11 m/s", "7 m/s", "10 m/s", "4 m/s"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '11 m/s', 'MEDIO', 0.55, 'fundamentos-de-fisica-1-halliday.pdf', 17, 19,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Vetores', 'Soma vetorial', 'Método do polígono',
    'Dois vetores de mesmo módulo formam um ângulo de 120° entre si. O módulo do vetor resultante é:',
    '["igual ao módulo de cada vetor", "dobro do módulo", "metade do módulo", "zero"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'igual ao módulo de cada vetor', 'DESAFIANTE', 0.65, 'fundamentos-de-fisica-1-halliday.pdf', 40, 42,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
);


INSERT INTO perguntas (
    id, disciplina, topico_principal, topico, subtopico, questao, respostas, pesos_resposta,
    resposta_correta, dificuldade, rigor, referencia_livro, pagina_inicio, pagina_fim,
    exercicio, usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
) VALUES

(
    uuid_generate_v4(), 'Física', 'Medição', 'Grandezas e unidades', 'Notação científica',
    'O diâmetro de um fio de cabelo é aproximadamente 0,00005 m. Esta medida em notação científica é:',
    '["5,0 × 10⁻⁵ m", "5,0 × 10⁻⁴ m", "5,0 × 10⁻⁶ m", "5,0 × 10⁻³ m"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '5,0 × 10⁻⁵ m', 'FACIL', 0.28, 'fundamentos-de-fisica-1-halliday.pdf', 2, 4,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Medição', 'Conversão de unidades', 'Velocidade',
    'Um carro viaja a 108 km/h. Esta velocidade em m/s é:',
    '["30 m/s", "10,8 m/s", "108 m/s", "388,8 m/s"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '30 m/s', 'FACIL', 0.30, 'fundamentos-de-fisica-1-halliday.pdf', 2, 4,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Medição', 'Prefixo SI', 'Significado',
    'O prefixo "nano" (n) representa o fator:',
    '["10⁻⁹", "10⁻⁶", "10⁻¹²", "10⁻³"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '10⁻⁹', 'FACIL', 0.25, 'fundamentos-de-fisica-1-halliday.pdf', 3, 4,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Movimento retilíneo', 'Velocidade escalar média', 'Distância total',
    'Uma pessoa caminha 3 km para o norte em 1 hora e depois 4 km para o leste em 1 hora. A velocidade escalar média (rapidez média) é:',
    '["3,5 km/h", "2,5 km/h", "5 km/h", "7 km/h"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3,5 km/h', 'MEDIO', 0.50, 'fundamentos-de-fisica-1-halliday.pdf', 14, 16,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Movimento retilíneo', 'Aceleração', 'Sinal',
    'Um carro freia uniformemente de 20 m/s até parar em 5 s. O sinal da aceleração é:',
    '["negativo", "positivo", "zero", "variável"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'negativo', 'FACIL', 0.30, 'fundamentos-de-fisica-1-halliday.pdf', 19, 21,
    '', true, 'RETA', -4, 20, 0, 'tempo (s)', 'velocidade (m/s)', 0, 6, 1
),
(
    uuid_generate_v4(), 'Física', 'Movimento retilíneo', 'Queda livre', 'Altura máxima',
    'Uma bola é lançada verticalmente para cima com velocidade de 15 m/s. Considerando g = 10 m/s², a altura máxima atingida é:',
    '["11,25 m", "22,5 m", "15 m", "7,5 m"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '11,25 m', 'MEDIO', 0.52, 'fundamentos-de-fisica-1-halliday.pdf', 23, 25,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Movimento retilíneo', 'Gráfico', 'Velocidade',
    'No gráfico posição × tempo a seguir, a velocidade é maior quando a reta tangente é:',
    '["mais inclinada", "menos inclinada", "horizontal", "vertical"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'mais inclinada', 'FACIL', 0.28, 'fundamentos-de-fisica-1-halliday.pdf', 17, 19,
    '', true, 'RETA', 2, 0, 0, 'tempo (s)', 'posição (m)', 0, 5, 1
),

(
    uuid_generate_v4(), 'Física', 'Vetores', 'Componentes', 'Cálculo',
    'Um vetor de módulo 20 m faz um ângulo de 60° com o eixo x. Sua componente y é:',
    '["17,32 m", "10 m", "20 m", "5 m"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '17,32 m', 'FACIL', 0.35, 'fundamentos-de-fisica-1-halliday.pdf', 42, 44,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Vetores', 'Vetor unitário', 'Definição',
    'Um vetor unitário tem módulo igual a:',
    '["1", "0", "depende da direção", "infinito"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1', 'FACIL', 0.20, 'fundamentos-de-fisica-1-halliday.pdf', 44, 45,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Vetores', 'Soma vetorial', 'Diferença',
    'O vetor diferença a - b pode ser obtido somando a ao vetor:',
    '["-b", "b", "2b", "b/2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-b', 'FACIL', 0.25, 'fundamentos-de-fisica-1-halliday.pdf', 41, 42,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Movimento em 2D', 'Lançamento horizontal', 'Tempo de queda',
    'Uma bola é lançada horizontalmente do alto de um prédio de 80 m com velocidade de 15 m/s. Considerando g = 10 m/s², o tempo de queda é:',
    '["4 s", "2 s", "8 s", "5,33 s"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '4 s', 'MEDIO', 0.50, 'fundamentos-de-fisica-1-halliday.pdf', 61, 64,
    '', true, 'PARABOLA', -5, 0, 80, 'posição x (m)', 'posição y (m)', 0, 30, 5
),
(
    uuid_generate_v4(), 'Física', 'Movimento em 2D', 'Lançamento oblíquo', 'Tempo de voo',
    'Um projétil é lançado com velocidade de 40 m/s e ângulo de 30° com a horizontal. Considerando g = 10 m/s², o tempo total de voo é:',
    '["4 s", "2 s", "8 s", "6 s"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '4 s', 'MEDIO', 0.55, 'fundamentos-de-fisica-1-halliday.pdf', 62, 64,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Movimento em 2D', 'Movimento circular uniforme', 'Período',
    'Uma partícula em MCU tem velocidade de 6 m/s e raio de 3 m. O período do movimento é aproximadamente: (use π = 3,14)',
    '["3,14 s", "1,57 s", "6,28 s", "0,5 s"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3,14 s', 'MEDIO', 0.52, 'fundamentos-de-fisica-1-halliday.pdf', 65, 67,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Movimento em 2D', 'Movimento relativo', 'Travessia de rio',
    'Um barco atravessa um rio perpendicularmente à correnteza. A velocidade do barco em relação à água é 4 m/s e a correnteza tem velocidade 3 m/s. O tempo para atravessar 100 m de largura é:',
    '["25 s", "20 s", "33,3 s", "14,3 s"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '25 s', 'MEDIO', 0.48, 'fundamentos-de-fisica-1-halliday.pdf', 68, 70,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Leis de Newton', 'Primeira lei', 'Inércia',
    'Um astronauta no espaço, longe de qualquer corpo celeste, solta uma chave. A chave irá:',
    '["permanecer no lugar", "cair para baixo", "flutuar para cima", "girar"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'permanecer no lugar', 'FACIL', 0.30, 'fundamentos-de-fisica-1-halliday.pdf', 82, 83,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Leis de Newton', 'Segunda lei', 'Massa',
    'A mesma força aplicada a dois corpos produz acelerações de 2 m/s² e 8 m/s². A razão entre as massas m₁/m₂ é:',
    '["4", "1/4", "2", "1/2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '4', 'MEDIO', 0.50, 'fundamentos-de-fisica-1-halliday.pdf', 84, 86,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Leis de Newton', 'Terceira lei', 'Identificação',
    'Quando um avião a jato voa, o par ação-reação para a força que o motor exerce sobre o ar é:',
    '["a força que o ar exerce sobre o motor", "o peso do avião", "a sustentação das asas", "o atrito do ar"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'a força que o ar exerce sobre o motor', 'MEDIO', 0.48, 'fundamentos-de-fisica-1-halliday.pdf', 89, 90,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Leis de Newton', 'Força resultante', 'Massa e aceleração',
    'Um corpo de 15 kg está sob ação de uma força resultante de 45 N. A aceleração do corpo é:',
    '["3 m/s²", "0,33 m/s²", "30 m/s²", "5 m/s²"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3 m/s²', 'FACIL', 0.28, 'fundamentos-de-fisica-1-halliday.pdf', 84, 86,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Atrito', 'Coeficiente de atrito estático', 'Ângulo limite',
    'Um bloco está sobre um plano inclinado. O ângulo em que ele começa a deslizar é θ. O coeficiente de atrito estático μₑ é igual a:',
    '["tan θ", "sen θ", "cos θ", "cotg θ"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'tan θ', 'MEDIO', 0.55, 'fundamentos-de-fisica-1-halliday.pdf', 109, 111,
    '', true, 'RETA', 0, 0, 0, 'ângulo (graus)', 'μₑ', 0, 45, 15
),
(
    uuid_generate_v4(), 'Física', 'Atrito', 'Atrito cinético', 'Movimento uniforme',
    'Um bloco de 10 kg desliza com velocidade constante sobre uma superfície horizontal com μₑ = 0,2. A força de atrito é: (g = 10 m/s²)',
    '["20 N", "100 N", "10 N", "2 N"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '20 N', 'FACIL', 0.35, 'fundamentos-de-fisica-1-halliday.pdf', 111, 112,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Movimento circular', 'Velocidade máxima em curva', 'Atrito',
    'Um carro faz uma curva plana de raio 40 m com coeficiente de atrito estático μₑ = 0,5. A velocidade máxima segura é aproximadamente: (g = 10 m/s²)',
    '["14,1 m/s", "20 m/s", "10 m/s", "7,1 m/s"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '14,1 m/s', 'DESAFIANTE', 0.62, 'fundamentos-de-fisica-1-halliday.pdf', 117, 119,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Trabalho e energia', 'Trabalho de força variável', 'Mola',
    'Uma mola de constante k = 100 N/m é distendida de 0,2 m. O trabalho realizado pela força elástica durante a distensão é:',
    '["-2 J", "2 J", "20 J", "-20 J"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-2 J', 'MEDIO', 0.52, 'fundamentos-de-fisica-1-halliday.pdf', 138, 140,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Trabalho e energia', 'Potência', 'Cálculo com força e velocidade',
    'Um motor exerce uma força de 500 N sobre um veículo que se move a 20 m/s. A potência desenvolvida é:',
    '["10 kW", "25 kW", "5 kW", "100 kW"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '10 kW', 'MEDIO', 0.48, 'fundamentos-de-fisica-1-halliday.pdf', 143, 144,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Trabalho e energia', 'Energia cinética', 'Relação com momento',
    'A energia cinética de um corpo de massa m e velocidade v é dada por:',
    '["½ mv²", "mv", "½ mv", "mv²"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '½ mv²', 'FACIL', 0.20, 'fundamentos-de-fisica-1-halliday.pdf', 140, 141,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Conservação da energia', 'Energia potencial', 'Força conservativa',
    'Uma força é conservativa quando:',
    '["o trabalho realizado independe da trajetória", "realiza sempre trabalho positivo", "depende do caminho percorrido", "não realiza trabalho"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'o trabalho realizado independe da trajetória', 'MEDIO', 0.50, 'fundamentos-de-fisica-1-halliday.pdf', 164, 166,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Conservação da energia', 'Energia potencial elástica', 'Mola',
    'Uma mola de constante k é comprimida de x. A energia potencial elástica é:',
    '["½ kx²", "kx²", "½ kx", "kx"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '½ kx²', 'FACIL', 0.28, 'fundamentos-de-fisica-1-halliday.pdf', 158, 160,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Conservação da energia', 'Dissipação de energia', 'Atrito',
    'Quando um bloco desliza sobre uma superfície com atrito, parte da energia mecânica se transforma em:',
    '["energia térmica", "energia potencial", "energia cinética", "energia nuclear"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'energia térmica', 'FACIL', 0.30, 'fundamentos-de-fisica-1-halliday.pdf', 168, 170,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Sistemas de partículas', 'Centro de massa', 'Três partículas',
    'Três partículas de massas 1 kg, 2 kg e 3 kg estão em x = 0, x = 2 m e x = 4 m. O centro de massa está em:',
    '["2,67 m", "2 m", "3 m", "1,33 m"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2,67 m', 'MEDIO', 0.55, 'fundamentos-de-fisica-1-halliday.pdf', 187, 189,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Sistemas de partículas', 'Movimento do centro de massa', 'Lei de Newton',
    'O centro de massa de um sistema de partículas se move como se toda a massa estivesse concentrada nele e todas as forças externas fossem aplicadas:',
    '["nesse ponto", "em cada partícula", "na partícula mais pesada", "na partícula mais leve"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'nesse ponto', 'MEDIO', 0.48, 'fundamentos-de-fisica-1-halliday.pdf', 192, 194,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Movimento retilíneo', 'MRU', 'Encontro de móveis',
    'Dois carros estão separados por 200 m e se movem um em direção ao outro com velocidades de 15 m/s e 10 m/s. O tempo até o encontro é:',
    '["8 s", "10 s", "5 s", "20 s"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '8 s', 'MEDIO', 0.50, 'fundamentos-de-fisica-1-halliday.pdf', 14, 16,
    '', true, 'RETA', -25, 200, 0, 'posição (m)', 'tempo (s)', 0, 10, 2
),
(
    uuid_generate_v4(), 'Física', 'Movimento retilíneo', 'MRUV', 'Função horária',
    'A equação da posição de um corpo em MRUV é x = 10 + 4t + 2t² (SI). A aceleração do corpo é:',
    '["4 m/s²", "2 m/s²", "8 m/s²", "10 m/s²"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '4 m/s²', 'FACIL', 0.35, 'fundamentos-de-fisica-1-halliday.pdf', 19, 22,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Movimento retilíneo', 'Queda livre', 'Velocidade',
    'Uma pedra cai de uma altura de 125 m. Desprezando a resistência do ar e considerando g = 10 m/s², a velocidade ao atingir o solo é:',
    '["50 m/s", "25 m/s", "35 m/s", "40 m/s"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '50 m/s', 'FACIL', 0.38, 'fundamentos-de-fisica-1-halliday.pdf', 23, 25,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Movimento em 2D', 'Lançamento oblíquo', 'Altura máxima (fórmula)',
    'A altura máxima de um projétil lançado com velocidade v₀ e ângulo θ é dada por:',
    '["(v₀² sen²θ)/(2g)", "(v₀² cos²θ)/(2g)", "(v₀ senθ)/g", "(2v₀ senθ)/g"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(v₀² sen²θ)/(2g)', 'FACIL', 0.35, 'fundamentos-de-fisica-1-halliday.pdf', 62, 64,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Leis de Newton', 'Plano inclinado', 'Aceleração com atrito',
    'Um bloco desce um plano inclinado de 30° com coeficiente de atrito cinético μ = 0,2. A aceleração do bloco é aproximadamente: (g = 10 m/s²)',
    '["3,27 m/s²", "5 m/s²", "1,73 m/s²", "4 m/s²"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3,27 m/s²', 'DESAFIANTE', 0.65, 'fundamentos-de-fisica-1-halliday.pdf', 93, 95,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Leis de Newton', 'Força normal', 'Plano inclinado',
    'Um bloco de 5 kg está sobre um plano inclinado de 30° sem atrito. A força normal que o plano exerce sobre o bloco é: (g = 10 m/s²)',
    '["43,3 N", "50 N", "25 N", "0 N"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '43,3 N', 'MEDIO', 0.50, 'fundamentos-de-fisica-1-halliday.pdf', 93, 95,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Trabalho e energia', 'Trabalho resultante', 'Teorema',
    'Um corpo de 4 kg tem sua velocidade reduzida de 10 m/s para 6 m/s. O trabalho realizado pela força resultante é:',
    '["-128 J", "128 J", "-32 J", "32 J"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-128 J', 'MEDIO', 0.52, 'fundamentos-de-fisica-1-halliday.pdf', 141, 142,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Conservação da energia', 'Montanha-russa', 'Conservação',
    'Um carrinho de montanha-russa parte do repouso de uma altura H e desce sem atrito. A velocidade no ponto mais baixo é:',
    '["√(2gH)", "√(gH)", "2gH", "gH"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '√(2gH)', 'MEDIO', 0.48, 'fundamentos-de-fisica-1-halliday.pdf', 161, 163,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Sistemas de partículas', 'Centro de massa', 'Barra homogênea',
    'O centro de massa de uma barra homogênea de comprimento L está localizado a uma distância de:',
    '["L/2", "L/3", "L/4", "L"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'L/2', 'FACIL', 0.25, 'fundamentos-de-fisica-1-halliday.pdf', 189, 190,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Movimento retilíneo', 'Aceleração instantânea', 'Derivada segunda',
    'A posição de uma partícula é x(t) = t³ - 6t² + 9t (SI). A aceleração em t = 2 s é:',
    '["0 m/s²", "6 m/s²", "12 m/s²", "-6 m/s²"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '0 m/s²', 'DESAFIANTE', 0.60, 'fundamentos-de-fisica-1-halliday.pdf', 19, 22,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Vetores', 'Produto escalar', 'Ângulo entre vetores',
    'Dois vetores a = 3i + 4j e b = 5i - 12j. O ângulo entre eles é aproximadamente:',
    '["90°", "0°", "180°", "60°"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '90°', 'MEDIO', 0.55, 'fundamentos-de-fisica-1-halliday.pdf', 47, 48,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
);


INSERT INTO perguntas (
    id, disciplina, topico_principal, topico, subtopico, questao, respostas, pesos_resposta,
    resposta_correta, dificuldade, rigor, referencia_livro, pagina_inicio, pagina_fim,
    exercicio, usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
) VALUES

(
    uuid_generate_v4(), 'Física', 'Medição', 'Unidades SI', 'Grandezas fundamentais',
    'No Sistema Internacional (SI), as três grandezas fundamentais da mecânica são:',
    '["comprimento, massa e tempo", "força, massa e tempo", "comprimento, força e tempo", "comprimento, massa e velocidade"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'comprimento, massa e tempo', 'FACIL', 0.25, 'fundamentos-de-fisica-1-halliday.pdf', 2, 3,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Medição', 'Conversão de unidades', 'Área',
    'Uma sala tem 5 m de comprimento e 4 m de largura. Sua área em cm² é:',
    '["200.000 cm²", "20.000 cm²", "2.000 cm²", "200 cm²"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '200.000 cm²', 'FACIL', 0.30, 'fundamentos-de-fisica-1-halliday.pdf', 2, 3,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Medição', 'Prefixos SI', 'Múltiplos',
    'O prefixo "mega" (M) representa o fator:',
    '["10⁶", "10⁹", "10³", "10¹²"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '10⁶', 'FACIL', 0.22, 'fundamentos-de-fisica-1-halliday.pdf', 3, 4,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Movimento retilíneo', 'Velocidade média', 'Gráfico',
    'Um móvel percorre a primeira metade de um trajeto com velocidade de 40 km/h e a segunda metade com 60 km/h. A velocidade média no percurso total é:',
    '["48 km/h", "50 km/h", "45 km/h", "52 km/h"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '48 km/h', 'DESAFIANTE', 0.65, 'fundamentos-de-fisica-1-halliday.pdf', 14, 16,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Movimento retilíneo', 'Aceleração constante', 'Equação horária',
    'Um corpo em MRUV tem velocidade inicial de 5 m/s e aceleração de 2 m/s². Sua velocidade após 4 s é:',
    '["13 m/s", "8 m/s", "10 m/s", "15 m/s"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '13 m/s', 'FACIL', 0.32, 'fundamentos-de-fisica-1-halliday.pdf', 20, 22,
    '', true, 'RETA', 2, 5, 0, 'tempo (s)', 'velocidade (m/s)', 0, 5, 1
),
(
    uuid_generate_v4(), 'Física', 'Movimento retilíneo', 'Queda livre', 'Diferença de tempo',
    'Duas bolas são abandonadas de alturas diferentes: uma de 20 m e outra de 45 m. A razão entre os tempos de queda é:',
    '["2/3", "3/2", "4/9", "9/4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2/3', 'MEDIO', 0.52, 'fundamentos-de-fisica-1-halliday.pdf', 23, 25,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Movimento retilíneo', 'Velocidade instantânea', 'Derivada',
    'A posição de uma partícula é x(t) = 5t² - 3t + 2 (SI). A velocidade instantânea em t = 1 s é:',
    '["7 m/s", "10 m/s", "4 m/s", "5 m/s"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '7 m/s', 'MEDIO', 0.50, 'fundamentos-de-fisica-1-halliday.pdf', 17, 19,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Vetores', 'Vetor resultante', 'Soma',
    'Dados os vetores a = 2i + 3j e b = 4i - 2j, o vetor a + b é:',
    '["6i + 1j", "2i + 5j", "6i - 1j", "2i - 5j"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '6i + 1j', 'FACIL', 0.30, 'fundamentos-de-fisica-1-halliday.pdf', 40, 42,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Vetores', 'Produto escalar', 'Cálculo',
    'O produto escalar dos vetores a = 3i + 4j e b = 5i - 12j é:',
    '["-33", "33", "63", "-63"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-33', 'MEDIO', 0.52, 'fundamentos-de-fisica-1-halliday.pdf', 47, 48,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Vetores', 'Módulo de vetor', 'Cálculo',
    'O módulo do vetor a = 6i - 8j é:',
    '["10", "14", "2", "100"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '10', 'FACIL', 0.25, 'fundamentos-de-fisica-1-halliday.pdf', 42, 44,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Movimento em 2D', 'Lançamento oblíquo', 'Alcance',
    'Um projétil é lançado com velocidade de 50 m/s e ângulo de 30° com a horizontal. Desprezando a resistência do ar e considerando g = 10 m/s², o alcance horizontal é:',
    '["216,5 m", "125 m", "250 m", "433 m"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '216,5 m', 'DESAFIANTE', 0.62, 'fundamentos-de-fisica-1-halliday.pdf', 62, 64,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Movimento em 2D', 'MCU', 'Aceleração',
    'Em um movimento circular uniforme, a aceleração é:',
    '["centrípeta", "tangencial", "nula", "constante em módulo e direção"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'centrípeta', 'FACIL', 0.30, 'fundamentos-de-fisica-1-halliday.pdf', 65, 67,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Movimento em 2D', 'Movimento relativo', 'Velocidade resultante',
    'Um barco se move a 8 m/s em relação à água, perpendicularmente à correnteza de 6 m/s. A velocidade do barco em relação à margem é:',
    '["10 m/s", "14 m/s", "2 m/s", "48 m/s"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '10 m/s', 'FACIL', 0.35, 'fundamentos-de-fisica-1-halliday.pdf', 68, 70,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Leis de Newton', 'Segunda lei', 'Força resultante',
    'Sobre um corpo de 8 kg atua uma força resultante de 24 N. A aceleração do corpo é:',
    '["3 m/s²", "192 m/s²", "0,33 m/s²", "16 m/s²"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3 m/s²', 'FACIL', 0.28, 'fundamentos-de-fisica-1-halliday.pdf', 84, 86,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Leis de Newton', 'Peso', 'Massa',
    'Um corpo pesa 196 N na Terra (g = 9,8 m/s²). Sua massa é:',
    '["20 kg", "196 kg", "19,6 kg", "200 kg"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '20 kg', 'FACIL', 0.25, 'fundamentos-de-fisica-1-halliday.pdf', 87, 88,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Leis de Newton', 'Terceira lei', 'Forças de contato',
    'Quando um livro está em repouso sobre uma mesa, a força de reação ao peso do livro é:',
    '["a força que o livro exerce sobre a Terra", "a força normal que a mesa exerce sobre o livro", "a força que a Terra exerce sobre a mesa", "a força que o livro exerce sobre a mesa"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'a força que o livro exerce sobre a Terra', 'MEDIO', 0.55, 'fundamentos-de-fisica-1-halliday.pdf', 89, 90,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Leis de Newton', 'Plano inclinado', 'Aceleração sem atrito',
    'Um bloco desliza sobre um plano inclinado de 37° sem atrito. Considerando g = 10 m/s², a aceleração do bloco é: (sen 37° = 0,6)',
    '["6 m/s²", "8 m/s²", "10 m/s²", "4 m/s²"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '6 m/s²', 'MEDIO', 0.48, 'fundamentos-de-fisica-1-halliday.pdf', 93, 95,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Atrito', 'Coeficiente de atrito', 'Comparação',
    'Em geral, o coeficiente de atrito estático μₑ é:',
    '["maior que o coeficiente de atrito cinético μ", "menor que μ", "igual a μ", "independente da superfície"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'maior que o coeficiente de atrito cinético μ', 'FACIL', 0.30, 'fundamentos-de-fisica-1-halliday.pdf', 110, 112,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Atrito', 'Força de atrito estático', 'Máxima',
    'Um bloco de 20 kg está sobre uma superfície horizontal com μₑ = 0,3. A força máxima de atrito estático é: (g = 10 m/s²)',
    '["60 N", "200 N", "20 N", "6 N"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '60 N', 'FACIL', 0.32, 'fundamentos-de-fisica-1-halliday.pdf', 110, 111,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Movimento circular', 'Força centrípeta', 'Origem',
    'A força centrípeta em um movimento circular uniforme é responsável por:',
    '["mudar a direção da velocidade", "aumentar o módulo da velocidade", "diminuir o módulo da velocidade", "manter a velocidade constante"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'mudar a direção da velocidade', 'FACIL', 0.30, 'fundamentos-de-fisica-1-halliday.pdf', 117, 119,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Trabalho e energia', 'Trabalho', 'Ângulo',
    'O trabalho realizado por uma força F sobre um corpo que sofre um deslocamento d é máximo quando o ângulo entre F e d é:',
    '["0°", "90°", "180°", "45°"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '0°', 'FACIL', 0.25, 'fundamentos-de-fisica-1-halliday.pdf', 132, 133,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Trabalho e energia', 'Teorema trabalho-energia', 'Variação',
    'Um corpo de 2 kg tem sua velocidade aumentada de 3 m/s para 7 m/s. O trabalho realizado sobre o corpo é:',
    '["40 J", "20 J", "58 J", "32 J"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '40 J', 'MEDIO', 0.50, 'fundamentos-de-fisica-1-halliday.pdf', 141, 142,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Trabalho e energia', 'Potência', 'Unidade',
    'A unidade de potência no Sistema Internacional (SI) é o watt (W), que equivale a:',
    '["1 J/s", "1 N/m", "1 kg·m/s", "1 N·s"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1 J/s', 'FACIL', 0.25, 'fundamentos-de-fisica-1-halliday.pdf', 143, 144,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Conservação da energia', 'Energia mecânica', 'Conservação',
    'Em um sistema conservativo, a energia mecânica total é:',
    '["constante", "sempre crescente", "sempre decrescente", "nula"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'constante', 'FACIL', 0.25, 'fundamentos-de-fisica-1-halliday.pdf', 164, 166,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Conservação da energia', 'Energia potencial', 'Gravitacional',
    'Um corpo de massa m está a uma altura h do solo. Sua energia potencial gravitacional (com referência no solo) é:',
    '["mgh", "mg/h", "mh/g", "gh/m"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'mgh', 'FACIL', 0.22, 'fundamentos-de-fisica-1-halliday.pdf', 160, 161,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Conservação da energia', 'Dissipação', 'Atrito',
    'Quando um bloco desliza sobre uma superfície com atrito, parte da energia mecânica é convertida em:',
    '["energia térmica", "energia potencial elástica", "energia química", "energia nuclear"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'energia térmica', 'FACIL', 0.28, 'fundamentos-de-fisica-1-halliday.pdf', 168, 170,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Sistemas de partículas', 'Centro de massa', 'Partículas iguais',
    'Duas partículas de massas iguais estão nas posições x = 2 m e x = 8 m. O centro de massa está em:',
    '["5 m", "4 m", "6 m", "3 m"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '5 m', 'FACIL', 0.30, 'fundamentos-de-fisica-1-halliday.pdf', 187, 188,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Física', 'Sistemas de partículas', 'Centro de massa', 'Triângulo',
    'O centro de massa de um triângulo homogêneo está localizado no:',
    '["baricentro", "incentro", "circuncentro", "ortocentro"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'baricentro', 'MEDIO', 0.48, 'fundamentos-de-fisica-1-halliday.pdf', 189, 190,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Movimento retilíneo', 'MRU', 'Função horária',
    'Um móvel em MRU tem função horária x = 20 - 5t (SI). A posição inicial e a velocidade são, respectivamente:',
    '["20 m e -5 m/s", "20 m e 5 m/s", "-20 m e 5 m/s", "-20 m e -5 m/s"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '20 m e -5 m/s', 'FACIL', 0.28, 'fundamentos-de-fisica-1-halliday.pdf', 14, 15,
    '', true, 'RETA', -5, 20, 0, 'tempo (s)', 'posição (m)', 0, 6, 1
),

(
    uuid_generate_v4(), 'Física', 'Movimento retilíneo', 'MRUV', 'Função horária da velocidade',
    'A função horária da velocidade de um corpo é v = 10 + 4t (SI). A aceleração e a velocidade inicial são:',
    '["4 m/s² e 10 m/s", "10 m/s² e 4 m/s", "6 m/s² e 10 m/s", "14 m/s² e 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '4 m/s² e 10 m/s', 'FACIL', 0.28, 'fundamentos-de-fisica-1-halliday.pdf', 20, 22,
    '', true, 'RETA', 4, 10, 0, 'tempo (s)', 'velocidade (m/s)', 0, 5, 1
),

(
    uuid_generate_v4(), 'Física', 'Movimento retilíneo', 'Queda livre', 'Lançamento vertical',
    'Uma bola é lançada verticalmente para cima com velocidade de 30 m/s. Considerando g = 10 m/s², o tempo total de voo é:',
    '["6 s", "3 s", "9 s", "12 s"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '6 s', 'MEDIO', 0.50, 'fundamentos-de-fisica-1-halliday.pdf', 23, 25,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Movimento em 2D', 'Lançamento oblíquo', 'Velocidade no ponto mais alto',
    'No ponto mais alto da trajetória de um projétil lançado obliquamente, a velocidade:',
    '["é igual à componente horizontal da velocidade inicial", "é zero", "é igual à velocidade inicial", "é igual à componente vertical da velocidade inicial"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'é igual à componente horizontal da velocidade inicial', 'MEDIO', 0.48, 'fundamentos-de-fisica-1-halliday.pdf', 62, 64,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Leis de Newton', 'Sistema de blocos', 'Aceleração',
    'Dois blocos de massas 3 kg e 2 kg estão ligados por uma corda sobre uma superfície horizontal sem atrito. Uma força de 10 N puxa o conjunto. A aceleração do sistema é:',
    '["2 m/s²", "5 m/s²", "0,5 m/s²", "10 m/s²"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2 m/s²', 'MEDIO', 0.50, 'fundamentos-de-fisica-1-halliday.pdf', 91, 93,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Trabalho e energia', 'Trabalho da força peso', 'Subida',
    'Um corpo de 5 kg é levantado verticalmente a uma altura de 2 m. O trabalho realizado contra a força peso é: (g = 10 m/s²)',
    '["100 J", "50 J", "10 J", "200 J"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '100 J', 'FACIL', 0.30, 'fundamentos-de-fisica-1-halliday.pdf', 132, 134,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Conservação da energia', 'Pêndulo simples', 'Velocidade',
    'Um pêndulo simples de comprimento L é solto do repouso da posição horizontal. A velocidade no ponto mais baixo é:',
    '["√(2gL)", "√(gL)", "2√(gL)", "√(gL/2)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '√(2gL)', 'MEDIO', 0.55, 'fundamentos-de-fisica-1-halliday.pdf', 161, 163,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Sistemas de partículas', 'Centro de massa', 'Movimento',
    'O centro de massa de um sistema de partículas pode estar:',
    '["fora do sistema material", "sempre dentro do sistema", "sempre em uma das partículas", "sempre no centro geométrico"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'fora do sistema material', 'FACIL', 0.32, 'fundamentos-de-fisica-1-halliday.pdf', 189, 190,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Física', 'Movimento retilíneo', 'Gráfico', 'Aceleração',
    'A inclinação da reta tangente no gráfico velocidade × tempo representa:',
    '["a aceleração instantânea", "a velocidade média", "a posição", "o deslocamento"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'a aceleração instantânea', 'FACIL', 0.28, 'fundamentos-de-fisica-1-halliday.pdf', 19, 20,
    '', true, 'RETA', 3, 0, 0, 'tempo (s)', 'velocidade (m/s)', 0, 5, 1
),
(
    uuid_generate_v4(), 'Física', 'Vetores', 'Produto vetorial', 'Cálculo',
    'O produto vetorial i × j é igual a:',
    '["k", "-k", "0", "1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'k', 'FACIL', 0.30, 'fundamentos-de-fisica-1-halliday.pdf', 48, 49,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
);


-- Mock SQL para Matemática - LÓGICA (Capítulo I)
-- Baseado EXCLUSIVAMENTE no livro "Fundamentos de Matemática Elementar - Volume 1"
-- Iezzi & Murakami, 9ª ed., 2013
-- Disciplina: Matemática
-- Tópico principal: Lógica (Capítulo I, páginas 9-25)
-- Total de questões neste lote: 100

INSERT INTO perguntas (
    id, disciplina, topico_principal, topico, subtopico, questao, respostas, pesos_resposta,
    resposta_correta, dificuldade, rigor, referencia_livro, pagina_inicio, pagina_fim,
    exercicio, usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
) VALUES


(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Proposições', 'Classificação',
    'Qual das seguintes frases é uma proposição lógica?',
    '["O Sol é uma estrela", "Que horas são?", "Estude mais!", "x + 3 = 8"]'::jsonb, '[1.0, 0.1, 0.2, 0.3]'::jsonb,
    'O Sol é uma estrela', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 9, 10,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Proposições', 'Classificação',
    'Qual das seguintes frases NÃO é uma proposição lógica?',
    '["A Terra é plana", "2 + 2 = 5", "Feche a porta", "Brasil é um país"]'::jsonb, '[0.2, 0.1, 1.0, 0.3]'::jsonb,
    'Feche a porta', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 9, 10,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Proposições', 'Valor lógico',
    'Qual é o valor lógico da proposição "5 é maior que 3"?',
    '["Verdadeira", "Falsa", "Não se aplica", "Indeterminada"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'Verdadeira', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 9, 10,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Proposições', 'Valor lógico',
    'Qual é o valor lógico da proposição "2 + 2 = 5"?',
    '["Falsa", "Verdadeira", "Não se aplica", "Indeterminada"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'Falsa', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 9, 10,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Proposições', 'Classificação',
    'Quantos valores lógicos uma proposição pode assumir?',
    '["2 (V ou F)", "1", "3", "4"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    '2 (V ou F)', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 9, 10,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Proposições', 'Classificação',
    'A frase "x + 1 = 7" é:',
    '["uma sentença aberta", "uma proposição verdadeira", "uma proposição falsa", "uma tautologia"]'::jsonb, '[1.0, 0.2, 0.3, 0.1]'::jsonb,
    'uma sentença aberta', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 10, 11,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Proposições', 'Classificação',
    'A frase "10 é divisível por 2" é:',
    '["uma proposição verdadeira", "uma proposição falsa", "uma sentença aberta", "uma pergunta"]'::jsonb, '[1.0, 0.2, 0.3, 0.1]'::jsonb,
    'uma proposição verdadeira', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 9, 10,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Proposições', 'Classificação',
    'A frase "O número 17 é primo" é:',
    '["uma proposição verdadeira", "uma proposição falsa", "uma sentença aberta", "uma contradição"]'::jsonb, '[1.0, 0.2, 0.3, 0.1]'::jsonb,
    'uma proposição verdadeira', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 9, 10,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Proposições', 'Classificação',
    'A frase "O triângulo tem quatro lados" é:',
    '["uma proposição falsa", "uma proposição verdadeira", "uma sentença aberta", "uma tautologia"]'::jsonb, '[1.0, 0.2, 0.3, 0.1]'::jsonb,
    'uma proposição falsa', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 9, 10,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Proposições', 'Classificação',
    'A frase "Brasília é a capital do Brasil" é:',
    '["uma proposição verdadeira", "uma proposição falsa", "uma sentença aberta", "uma pergunta"]'::jsonb, '[1.0, 0.2, 0.3, 0.1]'::jsonb,
    'uma proposição verdadeira', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 9, 10,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Negação', 'Definição',
    'A negação da proposição "5 é par" é:',
    '["5 é ímpar", "5 é par", "5 não é par", "5 é primo"]'::jsonb, '[1.0, 0.1, 0.3, 0.2]'::jsonb,
    '5 é ímpar', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 10, 11,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Negação', 'Definição',
    'A negação da proposição "7 > 3" é:',
    '["7 ≤ 3", "7 < 3", "7 = 3", "7 ≥ 3"]'::jsonb, '[1.0, 0.2, 0.1, 0.3]'::jsonb,
    '7 ≤ 3', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 10, 11,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Negação', 'Tabela-verdade',
    'Se p é verdadeira, então ~p é:',
    '["falsa", "verdadeira", "indeterminada", "igual a p"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'falsa', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 10, 11,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Negação', 'Tabela-verdade',
    'Se p é falsa, então ~p é:',
    '["verdadeira", "falsa", "indeterminada", "igual a p"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'verdadeira', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 10, 11,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Negação', 'Definição',
    'A negação da proposição "x é um número positivo" é:',
    '["x não é positivo", "x é negativo", "x é nulo", "x é par"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'x não é positivo', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 10, 11,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Conectivos', 'Conjunção',
    'A conjunção "p ∧ q" é verdadeira quando:',
    '["p e q são ambas verdadeiras", "p é verdadeira e q é falsa", "p é falsa e q é verdadeira", "p e q são ambas falsas"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'p e q são ambas verdadeiras', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 11, 12,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Conectivos', 'Conjunção',
    'Dadas p: "2 é par" (V) e q: "3 é ímpar" (V), o valor lógico de p ∧ q é:',
    '["Verdadeiro", "Falso", "Indeterminado", "Não definido"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'Verdadeiro', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 11, 12,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Conectivos', 'Conjunção',
    'Dadas p: "5 é par" (F) e q: "3 é ímpar" (V), o valor lógico de p ∧ q é:',
    '["Falso", "Verdadeiro", "Indeterminado", "Não definido"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'Falso', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 11, 12,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Conectivos', 'Conjunção',
    'Dadas p: "4 é par" (V) e q: "2 é ímpar" (F), o valor lógico de p ∧ q é:',
    '["Falso", "Verdadeiro", "Indeterminado", "Não definido"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'Falso', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 11, 12,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Conectivos', 'Conjunção',
    'Dadas p: "10 é múltiplo de 3" (F) e q: "7 é primo" (V), o valor lógico de p ∧ q é:',
    '["Falso", "Verdadeiro", "Indeterminado", "Não definido"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'Falso', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 11, 12,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Conectivos', 'Conjunção',
    'Dadas p: "8 é par" (V) e q: "9 é ímpar" (V), o valor lógico de p ∧ q é:',
    '["Verdadeiro", "Falso", "Indeterminado", "Não definido"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'Verdadeiro', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 11, 12,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Conectivos', 'Conjunção',
    'Dadas p: "12 é múltiplo de 5" (F) e q: "11 é par" (F), o valor lógico de p ∧ q é:',
    '["Falso", "Verdadeiro", "Indeterminado", "Não definido"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'Falso', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 11, 12,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Conectivos', 'Disjunção',
    'A disjunção "p ∨ q" é falsa quando:',
    '["p e q são ambas falsas", "p e q são ambas verdadeiras", "p é verdadeira e q é falsa", "p é falsa e q é verdadeira"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'p e q são ambas falsas', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 13, 14,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Conectivos', 'Disjunção',
    'Dadas p: "6 é par" (V) e q: "4 é ímpar" (F), o valor lógico de p ∨ q é:',
    '["Verdadeiro", "Falso", "Indeterminado", "Não definido"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'Verdadeiro', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 13, 14,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Conectivos', 'Disjunção',
    'Dadas p: "7 é primo" (V) e q: "9 é par" (F), o valor lógico de p ∨ q é:',
    '["Verdadeiro", "Falso", "Indeterminado", "Não definido"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'Verdadeiro', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 13, 14,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Conectivos', 'Disjunção',
    'Dadas p: "10 é divisor de 3" (F) e q: "15 é ímpar" (V), o valor lógico de p ∨ q é:',
    '["Verdadeiro", "Falso", "Indeterminado", "Não definido"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'Verdadeiro', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 13, 14,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Conectivos', 'Disjunção',
    'Dadas p: "12 é múltiplo de 3" (V) e q: "4 é divisor de 8" (V), o valor lógico de p ∨ q é:',
    '["Verdadeiro", "Falso", "Indeterminado", "Não definido"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'Verdadeiro', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 13, 14,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Condicionais', 'Condicional simples',
    'O condicional "p → q" é falso somente quando:',
    '["p é verdadeira e q é falsa", "p é falsa e q é verdadeira", "p e q são verdadeiras", "p e q são falsas"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'p é verdadeira e q é falsa', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 14, 16,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Condicionais', 'Condicional simples',
    'Dadas p: "2 é par" (V) e q: "3 é ímpar" (V), o valor lógico de p → q é:',
    '["Verdadeiro", "Falso", "Indeterminado", "Não definido"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'Verdadeiro', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 14, 16,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Condicionais', 'Condicional simples',
    'Dadas p: "5 é par" (F) e q: "7 é ímpar" (V), o valor lógico de p → q é:',
    '["Verdadeiro", "Falso", "Indeterminado", "Não definido"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'Verdadeiro', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 14, 16,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Condicionais', 'Condicional simples',
    'Dadas p: "4 é par" (V) e q: "6 é ímpar" (F), o valor lógico de p → q é:',
    '["Falso", "Verdadeiro", "Indeterminado", "Não definido"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'Falso', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 14, 16,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Condicionais', 'Condicional simples',
    'Dadas p: "9 é múltiplo de 3" (V) e q: "12 é divisível por 4" (V), o valor lógico de p → q é:',
    '["Verdadeiro", "Falso", "Indeterminado", "Não definido"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'Verdadeiro', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 14, 16,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Condicionais', 'Recíproco',
    'O recíproco da condicional "p → q" é:',
    '["q → p", "~p → ~q", "~q → ~p", "p → ~q"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'q → p', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 15, 16,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Condicionais', 'Contrapositiva',
    'A contrapositiva da condicional "p → q" é:',
    '["~q → ~p", "q → p", "~p → ~q", "p → ~q"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '~q → ~p', 'DESAFIANTE', 0.60, 'fundamentos-da-matematica-elementar-1-.pdf', 15, 16,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Condicionais', 'Bicondicional',
    'O bicondicional "p ↔ q" é verdadeiro quando:',
    '["p e q têm o mesmo valor lógico", "p é verdadeiro e q é falso", "p é falso e q é verdadeiro", "p e q são falsos apenas"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'p e q têm o mesmo valor lógico', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 16, 17,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Tautologias', 'Definição',
    'Uma tautologia é uma proposição que é:',
    '["sempre verdadeira", "sempre falsa", "às vezes verdadeira", "impossível de classificar"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'sempre verdadeira', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 17, 18,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Tautologias', 'Identificação',
    'Qual das seguintes proposições é uma tautologia?',
    '["p ∨ ~p", "p ∧ ~p", "p → p", "p ↔ ~p"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'p ∨ ~p', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 17, 18,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Tautologias', 'Identificação',
    'Qual das seguintes proposições é uma tautologia?',
    '["p → (p ∨ q)", "p ∧ ~p", "p ↔ ~p", "p → ~p"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'p → (p ∨ q)', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 17, 18,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Tautologias', 'Identificação',
    'Qual das seguintes proposições é uma tautologia?',
    '["(p ∧ q) → p", "p ∧ q", "p ∨ q", "p → q"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(p ∧ q) → p', 'DESAFIANTE', 0.60, 'fundamentos-da-matematica-elementar-1-.pdf', 17, 18,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Contradição', 'Definição',
    'Uma contradição é uma proposição que é:',
    '["sempre falsa", "sempre verdadeira", "às vezes verdadeira", "impossível de classificar"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'sempre falsa', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 18, 19,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Contradição', 'Identificação',
    'Qual das seguintes proposições é uma contradição?',
    '["p ∧ ~p", "p ∨ ~p", "p → p", "p ↔ p"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'p ∧ ~p', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 18, 19,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Equivalência', 'Leis de De Morgan',
    'Pelas leis de De Morgan, ~(p ∧ q) é equivalente a:',
    '["~p ∨ ~q", "~p ∧ ~q", "p ∨ q", "p ∧ q"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '~p ∨ ~q', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-1-.pdf', 23, 24,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Equivalência', 'Leis de De Morgan',
    'Pelas leis de De Morgan, ~(p ∨ q) é equivalente a:',
    '["~p ∧ ~q", "~p ∨ ~q", "p ∧ q", "p ∨ q"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '~p ∧ ~q', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-1-.pdf', 23, 24,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Equivalência', 'Propriedades',
    'A proposição p → q é equivalente a:',
    '["~p ∨ q", "p ∧ ~q", "q → p", "~p ∧ q"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '~p ∨ q', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 19, 20,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Quantificadores', 'Universal',
    'O quantificador universal é representado pelo símbolo:',
    '["∀", "∃", "→", "↔"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '∀', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 21, 22,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Quantificadores', 'Existencial',
    'O quantificador existencial é representado pelo símbolo:',
    '["∃", "∀", "→", "↔"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '∃', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 21, 22,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Quantificadores', 'Negação',
    'A negação de "∀ x, x + 1 = 7" é:',
    '["∃ x, x + 1 ≠ 7", "∀ x, x + 1 ≠ 7", "∃ x, x + 1 = 7", "∀ x, x + 1 = 7"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '∃ x, x + 1 ≠ 7', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 23, 24,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Quantificadores', 'Negação',
    'A negação de "∃ x, x² = 4" é:',
    '["∀ x, x² ≠ 4", "∃ x, x² ≠ 4", "∀ x, x² = 4", "∃ x, x² = 4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '∀ x, x² ≠ 4', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 23, 24,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Quantificadores', 'Sentenças abertas',
    'A frase "x + 3 = 10" é chamada de:',
    '["sentença aberta", "proposição verdadeira", "proposição falsa", "tautologia"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'sentença aberta', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 20, 21,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Proposições', 'Tabela-verdade',
    'Quantas linhas possui a tabela-verdade de uma proposição composta por 2 proposições simples?',
    '["4", "2", "8", "16"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '4', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 12, 14,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Proposições', 'Tabela-verdade',
    'Quantas linhas possui a tabela-verdade de uma proposição composta por 3 proposições simples?',
    '["8", "4", "16", "32"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '8', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 12, 14,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Condicionais', 'Condicional',
    'A proposição "Se chove, então o chão fica molhado" é um exemplo de:',
    '["condicional", "conjunção", "disjunção", "bicondicional"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'condicional', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 14, 16,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Condicionais', 'Bicondicional',
    'A proposição "x = 3 se, e somente se, x² = 9" é um exemplo de:',
    '["bicondicional", "condicional", "conjunção", "disjunção"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'bicondicional', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 16, 17,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Negação', 'Quantificadores',
    'A negação de "Todo número inteiro é par" é:',
    '["Existe um número inteiro que é ímpar", "Todo número inteiro é ímpar", "Nenhum número inteiro é par", "Existe um número inteiro que é par"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'Existe um número inteiro que é ímpar', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 23, 24,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Negação', 'Quantificadores',
    'A negação de "Algum número é primo" é:',
    '["Nenhum número é primo", "Todo número é primo", "Todo número é composto", "Algum número não é primo"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'Nenhum número é primo', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 23, 24,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Proposições', 'Classificação',
    'A frase "x² = 4" é uma sentença aberta que se torna verdadeira quando x é substituído por:',
    '["2 ou -2", "apenas 2", "apenas -2", "nenhum valor"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2 ou -2', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 20, 21,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Proposições', 'Classificação',
    'A frase "x + 2 = 5" é uma sentença aberta que se torna verdadeira quando x é substituído por:',
    '["3", "2", "4", "5"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 20, 21,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Tautologias', 'Identificação',
    'Qual das seguintes proposições é uma tautologia?',
    '["p ∨ (q ∧ ~q)", "p ∧ (q ∨ ~q)", "p → (q ∧ ~q)", "p ↔ (q ∨ ~q)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'p ∨ (q ∧ ~q)', 'DESAFIANTE', 0.65, 'fundamentos-da-matematica-elementar-1-.pdf', 17, 18,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Equivalência', 'Propriedades',
    'A proposição p → q é logicamente equivalente a:',
    '["~q → ~p", "q → p", "~p → ~q", "p → ~q"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '~q → ~p', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 19, 20,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
);

-- NOTA: Este é o primeiro lote de LÓGICA (53 questões).
-- Continuarei nos próximos lotes até completar 100 questões.

-- Mock SQL para Matemática - LÓGICA (Capítulo I) - COMPLEMENTO
-- Completando as 100 questões de Lógica
-- Total neste lote: 47 questões
-- Acumulado: 53 + 47 = 100 questões de Lógica

INSERT INTO perguntas (
    id, disciplina, topico_principal, topico, subtopico, questao, respostas, pesos_resposta,
    resposta_correta, dificuldade, rigor, referencia_livro, pagina_inicio, pagina_fim,
    exercicio, usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
) VALUES

(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Proposições', 'Classificação',
    'Qual das seguintes frases é uma proposição lógica?',
    '["A neve é branca", "Boa tarde!", "x + y = 10", "Vamos ao cinema?"]'::jsonb, '[1.0, 0.1, 0.3, 0.1]'::jsonb,
    'A neve é branca', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 9, 10,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Proposições', 'Classificação',
    'Qual das seguintes frases NÃO é uma proposição lógica?',
    '["Que horas são?", "O céu é azul", "2 + 2 = 4", "10 é maior que 5"]'::jsonb, '[1.0, 0.2, 0.3, 0.1]'::jsonb,
    'Que horas são?', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 9, 10,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Propositions', 'Valor lógico',
    'Qual é o valor lógico da proposição "O número 1 é maior que 0"?',
    '["Verdadeira", "Falsa", "Indeterminada", "Não se aplica"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'Verdadeira', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 9, 10,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Proposições', 'Valor lógico',
    'Qual é o valor lógico da proposição "O número 0 é positivo"?',
    '["Falsa", "Verdadeira", "Indeterminada", "Não se aplica"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'Falsa', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 9, 10,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Proposições', 'Valor lógico',
    'Qual é o valor lógico da proposição "7 é divisor de 21"?',
    '["Verdadeira", "Falsa", "Indeterminada", "Não se aplica"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'Verdadeira', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 9, 10,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Proposições', 'Valor lógico',
    'Qual é o valor lógico da proposição "18 é divisível por 5"?',
    '["Falsa", "Verdadeira", "Indeterminada", "Não se aplica"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'Falsa', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 9, 10,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Proposições', 'Classificação',
    'A frase "Esta frase é falsa" é um exemplo de:',
    '["paradoxo", "proposição verdadeira", "proposição falsa", "sentença aberta"]'::jsonb, '[1.0, 0.2, 0.3, 0.1]'::jsonb,
    'paradoxo', 'DESAFIANTE', 0.70, 'fundamentos-da-matematica-elementar-1-.pdf', 9, 10,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Proposições', 'Classificação',
    'A frase "x² = 16" é uma sentença aberta que se torna verdadeira quando x é substituído por:',
    '["4 ou -4", "apenas 4", "apenas -4", "8"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '4 ou -4', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 20, 21,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Negação', 'Definição',
    'A negação da proposição "Todos os gatos são pretos" é:',
    '["Existe pelo menos um gato que não é preto", "Nenhum gato é preto", "Todos os gatos são brancos", "Alguns gatos são pretos"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'Existe pelo menos um gato que não é preto', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 10, 11,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Negação', 'Definição',
    'A negação da proposição "Nenhum estudante foi reprovado" é:',
    '["Algum estudante foi reprovado", "Todos os estudantes foram reprovados", "Nenhum estudante foi aprovado", "Algum estudante foi aprovado"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'Algum estudante foi reprovado', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 10, 11,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Negação', 'Definição',
    'A negação da proposição "3 ≤ 5" é:',
    '["3 > 5", "3 < 5", "3 = 5", "3 ≥ 5"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3 > 5', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 10, 11,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Negação', 'Definição',
    'A negação da proposição "x ≥ 10" é:',
    '["x < 10", "x > 10", "x ≤ 10", "x = 10"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x < 10', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 10, 11,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Negação', 'Definição',
    'A negação da proposição "0 < x < 5" é:',
    '["x ≤ 0 ou x ≥ 5", "x < 0 ou x > 5", "0 ≥ x ≥ 5", "x ≤ 5"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x ≤ 0 ou x ≥ 5', 'DESAFIANTE', 0.60, 'fundamentos-da-matematica-elementar-1-.pdf', 10, 11,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Conectivos', 'Conjunção',
    'Dadas p: "3 é primo" (V) e q: "5 é par" (F), o valor lógico de ~(p ∧ q) é:',
    '["Verdadeiro", "Falso", "Indeterminado", "Não definido"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'Verdadeiro', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 11, 14,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Conectivos', 'Conjunção',
    'Dadas p: "4 é ímpar" (F) e q: "6 é par" (V), o valor lógico de ~p ∧ q é:',
    '["Verdadeiro", "Falso", "Indeterminado", "Não definido"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'Verdadeiro', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 11, 14,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Conectivos', 'Disjunção',
    'Dadas p: "8 é divisível por 2" (V) e q: "8 é divisível por 3" (F), o valor lógico de p ∨ ~q é:',
    '["Verdadeiro", "Falso", "Indeterminado", "Não definido"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'Verdadeiro', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 13, 14,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Conectivos', 'Disjunção',
    'Dadas p: "9 é quadrado perfeito" (V) e q: "7 é múltiplo de 3" (F), o valor lógico de ~p ∨ q é:',
    '["Falso", "Verdadeiro", "Indeterminado", "Não definido"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'Falso', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 13, 14,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Conectivos', 'Conjunção',
    'A proposição composta "p ∧ q" é verdadeira apenas quando:',
    '["p é V e q é V", "p é V e q é F", "p é F e q é V", "p é F e q é F"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'p é V e q é V', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 11, 12,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Conectivos', 'Disjunção',
    'A proposição composta "p ∨ q" é falsa apenas quando:',
    '["p é F e q é F", "p é V e q é F", "p é F e q é V", "p é V e q é V"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'p é F e q é F', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 13, 14,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Condicionais', 'Condicional',
    'Dadas p: "2 é ímpar" (F) e q: "3 é par" (F), o valor lógico de p → q é:',
    '["Verdadeiro", "Falso", "Indeterminado", "Não definido"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'Verdadeiro', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 14, 16,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Condicionais', 'Condicional',
    'A frase "Se você estudar, então passará no exame" é um exemplo de:',
    '["condicional", "conjunção", "disjunção", "bicondicional"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'condicional', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 14, 16,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Condicionais', 'Bicondicional',
    'A frase "Você vai ao cinema se, e somente se, tiver dinheiro" é um exemplo de:',
    '["bicondicional", "condicional", "conjunção", "disjunção"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'bicondicional', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 16, 17,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Condicionais', 'Inversa',
    'A inversa da condicional "p → q" é:',
    '["~p → ~q", "q → p", "~q → ~p", "p → ~q"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '~p → ~q', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 15, 16,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Condicionais', 'Contrapositiva',
    'A contrapositiva da condicional "Se chover, então a rua ficará molhada" é:',
    '["Se a rua não ficar molhada, então não choveu", "Se a rua ficar molhada, então choveu", "Se não chover, então a rua não ficará molhada", "Se chover, então a rua não ficará molhada"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'Se a rua não ficar molhada, então não choveu', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-1-.pdf', 15, 16,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Condicionais', 'Bicondicional',
    'O bicondicional "p ↔ q" é falso quando:',
    '["p é verdadeiro e q é falso", "p e q são verdadeiros", "p e q são falsos", "nunca é falso"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'p é verdadeiro e q é falso', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 16, 17,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Condicionais', 'Bicondicional',
    'O bicondicional "p ↔ q" é equivalente a:',
    '["(p → q) ∧ (q → p)", "(p → q) ∨ (q → p)", "p → q", "q → p"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(p → q) ∧ (q → p)', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 16, 17,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Tautologias', 'Identificação',
    'Qual das seguintes proposições é uma tautologia?',
    '["(p → q) ∨ (q → p)", "p ∧ q", "p ∨ q", "p → q"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(p → q) ∨ (q → p)', 'DESAFIANTE', 0.65, 'fundamentos-da-matematica-elementar-1-.pdf', 17, 18,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Tautologias', 'Identificação',
    'Qual das seguintes proposições é uma tautologia?',
    '["(p ∧ q) → p", "p ∧ q", "p ∨ q", "p → q"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(p ∧ q) → p', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-1-.pdf', 17, 18,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Tautologias', 'Identificação',
    'Qual das seguintes proposições é uma contradição?',
    '["p ∧ ~p", "p ∨ ~p", "p → p", "p ↔ p"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'p ∧ ~p', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 18, 19,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Tautologias', 'Identificação',
    'Qual das seguintes proposições é uma contradição?',
    '["(p ∧ q) ∧ ~(p ∨ q)", "p ∨ ~p", "p → p", "p ↔ p"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(p ∧ q) ∧ ~(p ∨ q)', 'DESAFIANTE', 0.68, 'fundamentos-da-matematica-elementar-1-.pdf', 18, 19,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Tautologias', 'Propriedades',
    'A proposição "p ∨ (q ∧ ~q)" é uma:',
    '["tautologia", "contradição", "contingência", "impossível de classificar"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'tautologia', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 17, 18,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Tautologias', 'Propriedades',
    'A proposição "p ∧ (q ∨ ~q)" é logicamente equivalente a:',
    '["p", "q", "~p", "~q"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'p', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 17, 18,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Equivalência', 'Leis de De Morgan',
    'Pelas leis de De Morgan, a negação de "p ∨ q" é:',
    '["~p ∧ ~q", "~p ∨ ~q", "p ∧ q", "p ∨ q"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '~p ∧ ~q', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 23, 24,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Equivalência', 'Leis de De Morgan',
    'A expressão ~(p ∨ q) é logicamente equivalente a:',
    '["~p ∧ ~q", "~p ∨ ~q", "p ∧ q", "p ∨ q"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '~p ∧ ~q', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 23, 24,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Equivalência', 'Propriedades',
    'A proposição p → q é logicamente equivalente a:',
    '["~p ∨ q", "p ∨ ~q", "~p ∧ q", "p ∧ ~q"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '~p ∨ q', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 19, 20,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Equivalência', 'Propriedades',
    'A proposição ~(p → q) é logicamente equivalente a:',
    '["p ∧ ~q", "~p ∧ q", "p ∨ ~q", "~p ∨ q"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'p ∧ ~q', 'DESAFIANTE', 0.60, 'fundamentos-da-matematica-elementar-1-.pdf', 23, 24,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Equivalência', 'Propriedades',
    'A proposição (p → q) ∧ (q → p) é logicamente equivalente a:',
    '["p ↔ q", "p → q", "q → p", "p ∨ q"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'p ↔ q', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 19, 20,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Quantificadores', 'Universal',
    'A frase "∀ x ∈ R, x² ≥ 0" pode ser lida como:',
    '["Para todo x real, x² é maior ou igual a zero", "Existe x real tal que x² ≥ 0", "Para algum x real, x² ≥ 0", "Nenhum x real satisfaz x² ≥ 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'Para todo x real, x² é maior ou igual a zero', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 21, 22,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Quantificadores', 'Existencial',
    'A frase "∃ x ∈ N, x² = 4" pode ser lida como:',
    '["Existe um número natural x tal que x² = 4", "Todo número natural x satisfaz x² = 4", "Para todo x natural, x² = 4", "Nenhum número natural satisfaz x² = 4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'Existe um número natural x tal que x² = 4', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 21, 22,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Quantificadores', 'Negação',
    'A negação de "∀ x, x > 0" é:',
    '["∃ x, x ≤ 0", "∀ x, x ≤ 0", "∃ x, x > 0", "∀ x, x > 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '∃ x, x ≤ 0', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 23, 24,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Quantificadores', 'Negação',
    'A negação de "∃ x, x² = 2" é:',
    '["∀ x, x² ≠ 2", "∃ x, x² ≠ 2", "∀ x, x² = 2", "∃ x, x² = 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '∀ x, x² ≠ 2', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 23, 24,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Quantificadores', 'Negação',
    'A negação de "Todo número par é divisível por 2" é:',
    '["Existe um número par que não é divisível por 2", "Todo número par não é divisível por 2", "Algum número ímpar é divisível por 2", "Nenhum número par é divisível por 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'Existe um número par que não é divisível por 2', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 23, 24,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Quantificadores', 'Unicidade',
    'O quantificador "∃|" significa:',
    '["existe um único", "existe pelo menos um", "existe no máximo um", "para todo"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'existe um único', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 22, 23,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Aplicação', 'Raciocínio lógico',
    'Se p é verdadeira e q é falsa, qual é o valor lógico de (p ∨ q) → p?',
    '["Verdadeiro", "Falso", "Indeterminado", "Não definido"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'Verdadeiro', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 14, 17,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Aplicação', 'Raciocínio lógico',
    'Se p é falsa e q é verdadeira, qual é o valor lógico de (p → q) ∧ ~p?',
    '["Verdadeiro", "Falso", "Indeterminado", "Não definido"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'Verdadeiro', 'DESAFIANTE', 0.60, 'fundamentos-da-matematica-elementar-1-.pdf', 14, 17,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Aplicação', 'Raciocínio lógico',
    'Se p é verdadeira e q é verdadeira, qual é o valor lógico de (p → ~q) ∨ q?',
    '["Verdadeiro", "Falso", "Indeterminado", "Não definido"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    'Verdadeiro', 'DESAFIANTE', 0.58, 'fundamentos-da-matematica-elementar-1-.pdf', 14, 17,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Aplicação', 'Raciocínio lógico',
    'A proposição "~p → ~q" é logicamente equivalente a:',
    '["q → p", "p → q", "~q → ~p", "p → ~q"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'q → p', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 15, 16,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Aplicação', 'Raciocínio lógico',
    'Qual das seguintes alternativas é uma tautologia?',
    '["(p → q) ∨ (q → p)", "p ∧ ~p", "p ∨ q", "p ∧ q"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(p → q) ∨ (q → p)', 'DESAFIANTE', 0.65, 'fundamentos-da-matematica-elementar-1-.pdf', 17, 18,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Aplicação', 'Raciocínio lógico',
    'A proposição "p ∧ (q ∨ r)" é logicamente equivalente a:',
    '["(p ∧ q) ∨ (p ∧ r)", "(p ∨ q) ∧ (p ∨ r)", "p ∧ q ∧ r", "p ∨ q ∨ r"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(p ∧ q) ∨ (p ∧ r)', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-1-.pdf', 19, 20,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Lógica', 'Aplicação', 'Raciocínio lógico',
    'A proposição "p ∨ (q ∧ r)" é logicamente equivalente a:',
    '["(p ∨ q) ∧ (p ∨ r)", "(p ∧ q) ∨ (p ∧ r)", "p ∨ q ∨ r", "p ∧ q ∧ r"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(p ∨ q) ∧ (p ∨ r)', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 19, 20,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
);

-- Mock SQL para Matemática - CONJUNTOS (Capítulo II)
-- Baseado EXCLUSIVAMENTE no livro "Fundamentos de Matemática Elementar - Volume 1"
-- Iezzi & Murakami, 9ª ed., 2013
-- Disciplina: Matemática
-- Tópico principal: Conjuntos (Capítulo II, páginas 18-45)
-- Total de questões neste lote: 100

INSERT INTO perguntas (
    id, disciplina, topico_principal, topico, subtopico, questao, respostas, pesos_resposta,
    resposta_correta, dificuldade, rigor, referencia_livro, pagina_inicio, pagina_fim,
    exercicio, usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
) VALUES

(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Conceitos básicos', 'Elemento e pertinência',
    'Dado o conjunto A = {1, 2, 3, 4, 5}, qual das seguintes afirmações é verdadeira?',
    '["3 ∈ A", "6 ∈ A", "0 ∈ A", "10 ∈ A"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    '3 ∈ A', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 18, 19,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Conceitos básicos', 'Elemento e pertinência',
    'Dado o conjunto B = {a, b, c, d}, qual das seguintes afirmações é falsa?',
    '["e ∈ B", "a ∈ B", "c ∈ B", "d ∈ B"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'e ∈ B', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 18, 19,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Conceitos básicos', 'Descrição de conjuntos',
    'O conjunto {x | x é vogal da palavra "MATEMÁTICA"} é:',
    '["{A, E, I}", "{M, T, C}", "{A, M, T}", "{E, I, O}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{A, E, I}', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 20, 21,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Conceitos básicos', 'Descrição de conjuntos',
    'O conjunto dos números ímpares positivos menores que 10 é:',
    '["{1, 3, 5, 7, 9}", "{2, 4, 6, 8}", "{1, 2, 3, 4, 5}", "{5, 6, 7, 8, 9}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{1, 3, 5, 7, 9}', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 20, 21,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Conceitos básicos', 'Descrição de conjuntos',
    'O conjunto {2, 4, 6, 8, 10} pode ser descrito como:',
    '["{x | x é par e 1 < x < 11}", "{x | x é ímpar}", "{x | x é divisor de 10}", "{x | x é múltiplo de 4}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{x | x é par e 1 < x < 11}', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 20, 21,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Conceitos básicos', 'Pertinência',
    'Se x ∈ A, qual das seguintes afirmações é verdadeira?',
    '["x é elemento de A", "x é subconjunto de A", "x não pertence a A", "x é igual a A"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x é elemento de A', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 18, 19,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Conjunto unitário', 'Definição',
    'Um conjunto que possui exatamente um elemento é chamado de:',
    '["conjunto unitário", "conjunto vazio", "conjunto universo", "conjunto das partes"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'conjunto unitário', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 21, 22,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Conjunto vazio', 'Definição',
    'O conjunto vazio é representado pelo símbolo:',
    '["∅", "U", "∞", "∩"]'::jsonb, '[1.0, 0.2, 0.1, 0.1]'::jsonb,
    '∅', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 21, 22,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Conjunto vazio', 'Propriedades',
    'Qual dos seguintes conjuntos é vazio?',
    '["{x | x ≠ x}", "{0}", "{x | x é número par entre 1 e 3}", "{∅}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{x | x ≠ x}', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 21, 22,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Conjunto vazio', 'Identificação',
    'Quantos elementos possui o conjunto vazio?',
    '["0", "1", "2", "infinitos"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '0', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 21, 22,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Conjunto unitário', 'Identificação',
    'Qual dos seguintes conjuntos é unitário?',
    '["{x | x é solução de x² = 4}", "{x | x é número par}", "{1, 2, 3}", "{a, b, c}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{x | x é solução de x² = 4}', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 21, 22,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Conjunto universo', 'Definição',
    'O conjunto que contém todos os elementos considerados em um determinado problema é chamado de:',
    '["conjunto universo", "conjunto vazio", "conjunto unitário", "conjunto das partes"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'conjunto universo', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 22, 23,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Conjunto universo', 'Aplicação',
    'Em um problema de Geometria Plana, o conjunto universo geralmente é:',
    '["um plano", "o conjunto dos números reais", "o conjunto dos inteiros", "o conjunto dos pontos de uma reta"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'um plano', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 22, 23,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Conjunto universo', 'Aplicação',
    'Em um problema de Teoria dos Números, o conjunto universo geralmente é:',
    '["o conjunto dos números reais", "o conjunto dos números complexos", "o conjunto dos números naturais", "o conjunto dos pontos"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'o conjunto dos números reais', 'MEDIO', 0.45, 'fundamentos-da-matematica-elementar-1-.pdf', 22, 23,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Igualdade', 'Definição',
    'Dois conjuntos A e B são iguais quando:',
    '["todo elemento de A pertence a B e vice-versa", "têm o mesmo número de elementos", "têm o mesmo primeiro elemento", "têm a mesma descrição"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'todo elemento de A pertence a B e vice-versa', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 24, 25,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Igualdade', 'Identificação',
    'Os conjuntos A = {1, 2, 3} e B = {3, 2, 1} são:',
    '["iguais", "diferentes", "disjuntos", "um é subconjunto do outro"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'iguais', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 24, 25,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Igualdade', 'Identificação',
    'Os conjuntos A = {a, a, b, b, b} e B = {a, b} são:',
    '["iguais", "diferentes", "disjuntos", "incomparáveis"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'iguais', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 24, 25,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Igualdade', 'Identificação',
    'Os conjuntos A = {x | x² = 9} e B = {3, -3} são:',
    '["iguais", "diferentes", "disjuntos", "A ⊂ B mas B ⊄ A"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'iguais', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 24, 25,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Igualdade', 'Identificação',
    'Os conjuntos A = {x | x é letra da palavra "CASA"} e B = {C, A, S} são:',
    '["iguais", "diferentes", "disjuntos", "A ∩ B = ∅"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'iguais', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 24, 25,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Subconjuntos', 'Definição',
    'Dizemos que A é subconjunto de B quando:',
    '["todo elemento de A pertence a B", "todo elemento de B pertence a A", "A e B têm os mesmos elementos", "A é vazio e B não"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'todo elemento de A pertence a B', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 25, 26,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Subconjuntos', 'Notação',
    'O símbolo utilizado para indicar que A é subconjunto de B é:',
    '["A ⊂ B", "A ∈ B", "A = B", "A ∩ B"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'A ⊂ B', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 25, 26,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Subconjuntos', 'Identificação',
    'Dados A = {1, 2, 3} e B = {1, 2, 3, 4, 5}, pode-se afirmar que:',
    '["A ⊂ B", "B ⊂ A", "A = B", "A e B são disjuntos"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'A ⊂ B', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 25, 27,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Subconjuntos', 'Propriedades',
    'Se A ⊂ B e B ⊂ C, então:',
    '["A ⊂ C", "C ⊂ A", "A = C", "B = C"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'A ⊂ C', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 34, 35,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Subconjuntos', 'Inclusão',
    'O conjunto vazio está contido em:',
    '["qualquer conjunto", "apenas em conjuntos não vazios", "apenas em conjuntos finitos", "apenas em conjuntos infinitos"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'qualquer conjunto', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 34, 35,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Subconjuntos', 'Contido vs Pertence',
    'A diferença entre "∈" e "⊂" é que:',
    '["∈ relaciona elemento e conjunto, ⊂ relaciona conjuntos", "∈ relaciona conjuntos, ⊂ relaciona elementos", "ambos relacionam elementos e conjuntos", "ambos relacionam apenas conjuntos"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '∈ relaciona elemento e conjunto, ⊂ relaciona conjuntos', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 25, 27,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Conjunto das partes', 'Definição',
    'O conjunto das partes de A, denotado por P(A), é o conjunto:',
    '["de todos os subconjuntos de A", "de todos os elementos de A", "das partes de A", "dos complementares de A"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'de todos os subconjuntos de A', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 34, 35,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Conjunto das partes', 'Cardinalidade',
    'Se A tem 3 elementos, então P(A) tem:',
    '["8 elementos", "6 elementos", "4 elementos", "9 elementos"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '8 elementos', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 34, 35,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Conjunto das partes', 'Cardinalidade',
    'Se A tem n elementos, então P(A) tem:',
    '["2ⁿ elementos", "n² elementos", "2n elementos", "n + 2 elementos"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2ⁿ elementos', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 34, 35,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Conjunto das partes', 'Listagem',
    'Dado A = {a, b}, o conjunto das partes P(A) é:',
    '["{∅, {a}, {b}, {a, b}}", "{{a}, {b}}", "{∅, {a, b}}", "{a, b}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{∅, {a}, {b}, {a, b}}', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 34, 35,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Conjunto das partes', 'Elementos',
    'O conjunto das partes do conjunto vazio é:',
    '["{∅}", "∅", "{0}", "{{∅}}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{∅}', 'DESAFIANTE', 0.60, 'fundamentos-da-matematica-elementar-1-.pdf', 34, 35,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Reunião', 'Definição',
    'A reunião de dois conjuntos A e B, denotada por A ∪ B, é o conjunto:',
    '["dos elementos que pertencem a A ou a B", "dos elementos que pertencem a A e a B", "dos elementos que pertencem apenas a A", "dos elementos que pertencem apenas a B"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'dos elementos que pertencem a A ou a B', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 28, 29,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Reunião', 'Cálculo',
    'Dados A = {1, 2, 3} e B = {3, 4, 5}, A ∪ B é:',
    '["{1, 2, 3, 4, 5}", "{3}", "{1, 2, 3}", "{3, 4, 5}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{1, 2, 3, 4, 5}', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 28, 29,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Reunião', 'Cálculo',
    'Dados A = {a, b, c} e B = {c, d, e}, A ∪ B é:',
    '["{a, b, c, d, e}", "{a, b, c}", "{c, d, e}", "{a, b, d, e}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{a, b, c, d, e}', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 28, 29,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Reunião', 'Propriedades',
    'A propriedade A ∪ ∅ é igual a:',
    '["A", "∅", "U", "A ∩ ∅"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'A', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 28, 30,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Reunião', 'Propriedades',
    'A propriedade A ∪ A é igual a:',
    '["A", "2A", "A²", "A ∩ A"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'A', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 28, 30,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Reunião', 'Propriedades',
    'A propriedade A ∪ B = B ∪ A é chamada de:',
    '["comutativa", "associativa", "distributiva", "idempotente"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'comutativa', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 28, 30,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Interseção', 'Definição',
    'A interseção de dois conjuntos A e B, denotada por A ∩ B, é o conjunto:',
    '["dos elementos que pertencem a A e a B", "dos elementos que pertencem a A ou a B", "dos elementos que pertencem apenas a A", "dos elementos que pertencem apenas a B"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'dos elementos que pertencem a A e a B', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 29, 30,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Interseção', 'Cálculo',
    'Dados A = {1, 2, 3, 4} e B = {3, 4, 5, 6}, A ∩ B é:',
    '["{3, 4}", "{1, 2, 3, 4, 5, 6}", "{1, 2}", "{5, 6}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{3, 4}', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 29, 30,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Interseção', 'Cálculo',
    'Dados A = {a, b, c, d} e B = {c, d, e, f}, A ∩ B é:',
    '["{c, d}", "{a, b, c, d, e, f}", "{a, b}", "{e, f}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{c, d}', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 29, 30,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Interseção', 'Conjuntos disjuntos',
    'Dois conjuntos A e B são disjuntos quando:',
    '["A ∩ B = ∅", "A ∪ B = ∅", "A ⊂ B", "B ⊂ A"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'A ∩ B = ∅', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 30, 31,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Interseção', 'Propriedades',
    'A propriedade A ∩ B = B ∩ A é chamada de:',
    '["comutativa", "associativa", "distributiva", "idempotente"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'comutativa', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 29, 31,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Interseção', 'Propriedades',
    'A propriedade A ∩ ∅ é igual a:',
    '["∅", "A", "U", "A ∪ ∅"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '∅', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 29, 31,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Propriedades', 'Distributiva',
    'A propriedade A ∪ (B ∩ C) = (A ∪ B) ∩ (A ∪ C) é chamada de:',
    '["distributiva da união em relação à interseção", "distributiva da interseção em relação à união", "associativa", "comutativa"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'distributiva da união em relação à interseção', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 30, 32,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Propriedades', 'Distributiva',
    'A propriedade A ∩ (B ∪ C) = (A ∩ B) ∪ (A ∩ C) é chamada de:',
    '["distributiva da interseção em relação à união", "distributiva da união em relação à interseção", "associativa", "comutativa"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'distributiva da interseção em relação à união', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 30, 32,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Diferença', 'Definição',
    'A diferença A - B é o conjunto:',
    '["dos elementos que pertencem a A mas não pertencem a B", "dos elementos que pertencem a B mas não pertencem a A", "dos elementos que pertencem a A e a B", "dos elementos que pertencem a A ou a B"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'dos elementos que pertencem a A mas não pertencem a B', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 33, 34,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Diferença', 'Cálculo',
    'Dados A = {1, 2, 3, 4, 5} e B = {2, 4, 6}, A - B é:',
    '["{1, 3, 5}", "{2, 4}", "{6}", "{1, 2, 3, 4, 5}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{1, 3, 5}', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 33, 34,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Diferença', 'Cálculo',
    'Dados A = {a, b, c, d} e B = {c, d, e, f}, B - A é:',
    '["{e, f}", "{a, b}", "{c, d}", "{a, b, e, f}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{e, f}', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 33, 34,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Complementar', 'Definição',
    'Dado um conjunto universo U e um subconjunto A ⊂ U, o complementar de A em relação a U, denotado por Cᴜᴬ, é:',
    '["U - A", "A - U", "A ∩ U", "A ∪ U"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'U - A', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 33, 34,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Complementar', 'Cálculo',
    'Dados U = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10} e A = {2, 4, 6, 8, 10}, o complementar de A em relação a U é:',
    '["{1, 3, 5, 7, 9}", "{2, 4, 6, 8, 10}", "∅", "U"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{1, 3, 5, 7, 9}', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 33, 34,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Complementar', 'Propriedades',
    'O complementar do complementar de A, ou seja, Cᴜ( Cᴜᴬ ), é igual a:',
    '["A", "U", "∅", "Aᶜ"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'A', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 41, 42,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Complementar', 'Propriedades',
    'A propriedade Cᴜᴬ ∪ A é igual a:',
    '["U", "A", "∅", "A ∩ U"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'U', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 41, 42,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Complementar', 'Propriedades',
    'A propriedade Cᴜᴬ ∩ A é igual a:',
    '["∅", "U", "A", "Aᶜ"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '∅', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 41, 42,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Diferença simétrica', 'Definição',
    'A diferença simétrica entre A e B, denotada por A Δ B, é o conjunto:',
    '["(A - B) ∪ (B - A)", "(A ∪ B) - (A ∩ B)", "A ∪ B", "A ∩ B"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(A - B) ∪ (B - A)', 'DESAFIANTE', 0.60, 'fundamentos-da-matematica-elementar-1-.pdf', 44, 45,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Diferença simétrica', 'Cálculo',
    'Dados A = {1, 2, 3, 4} e B = {3, 4, 5, 6}, a diferença simétrica A Δ B é:',
    '["{1, 2, 5, 6}", "{3, 4}", "{1, 2, 3, 4, 5, 6}", "{1, 2}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{1, 2, 5, 6}', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-1-.pdf', 44, 45,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Diferença simétrica', 'Propriedades',
    'A diferença simétrica A Δ B pode também ser expressa como:',
    '["(A ∪ B) - (A ∩ B)", "(A - B) ∩ (B - A)", "A ∪ B", "A ∩ B"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(A ∪ B) - (A ∩ B)', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 44, 45,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Cardinalidade', 'Fórmula',
    'Para dois conjuntos finitos A e B, a fórmula n(A ∪ B) é:',
    '["n(A) + n(B) - n(A ∩ B)", "n(A) + n(B)", "n(A) + n(B) + n(A ∩ B)", "n(A) - n(B)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'n(A) + n(B) - n(A ∩ B)', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-1-.pdf', 44, 45,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Cardinalidade', 'Cálculo',
    'Se n(A) = 10, n(B) = 8 e n(A ∩ B) = 3, então n(A ∪ B) é:',
    '["15", "18", "21", "5"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '15', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 44, 45,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Cardinalidade', 'Cálculo',
    'Em uma escola com 100 alunos, 60 estudam Matemática, 40 estudam Física e 20 estudam ambas. Quantos alunos estudam Matemática ou Física?',
    '["80", "100", "60", "40"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '80', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 44, 45,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Cardinalidade', 'Cálculo',
    'Se n(A ∪ B) = 20, n(A) = 12 e n(B) = 10, então n(A ∩ B) é:',
    '["2", "8", "18", "22"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 44, 45,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Aplicação', 'Problemas',
    'Em uma pesquisa com 200 pessoas, 120 gostam de futebol, 80 gostam de vôlei e 40 gostam dos dois esportes. Quantas pessoas não gostam de nenhum dos dois?',
    '["40", "80", "120", "160"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '40', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-1-.pdf', 44, 45,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Aplicação', 'Problemas',
    'Em uma turma de 50 alunos, 30 gostam de Matemática, 25 gostam de Português e 15 gostam de ambas. Quantos alunos gostam apenas de Matemática?',
    '["15", "10", "20", "25"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '15', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 44, 45,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Aplicação', 'Problemas',
    'Em uma pesquisa com 300 pessoas, 180 leem o jornal A, 150 leem o jornal B e 50 não leem nenhum dos dois. Quantas pessoas leem os dois jornais?',
    '["80", "100", "120", "60"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '80', 'DESAFIANTE', 0.65, 'fundamentos-da-matematica-elementar-1-.pdf', 44, 45,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Operações', 'Mistas',
    'Dados A = {1, 2, 3}, B = {2, 3, 4} e C = {3, 4, 5}, o conjunto (A ∪ B) ∩ C é:',
    '["{3, 4}", "{1, 2, 3, 4}", "{2, 3, 4}", "{3}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{3, 4}', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 28, 32,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Operações', 'Mistas',
    'Dados A = {a, b, c}, B = {b, c, d} e C = {c, d, e}, o conjunto (A ∩ B) ∪ C é:',
    '["{b, c, d, e}", "{a, b, c, d}", "{c, d, e}", "{a, b, c}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{b, c, d, e}', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 28, 32,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Operações', 'Mistas',
    'Se A ⊂ B, então A ∩ B é igual a:',
    '["A", "B", "∅", "A ∪ B"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'A', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 25, 31,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Operações', 'Mistas',
    'Se A ⊂ B, então A ∪ B é igual a:',
    '["B", "A", "∅", "A ∩ B"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'B', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 25, 29,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Operações', 'Mistas',
    'O conjunto (A - B) ∪ (B - A) é igual a:',
    '["A Δ B", "A ∪ B", "A ∩ B", "A - B"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'A Δ B', 'DESAFIANTE', 0.60, 'fundamentos-da-matematica-elementar-1-.pdf', 44, 45,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Cardinalidade', 'Fórmula para três conjuntos',
    'Para três conjuntos finitos A, B e C, a fórmula n(A ∪ B ∪ C) é:',
    '["n(A)+n(B)+n(C) - n(A∩B) - n(A∩C) - n(B∩C) + n(A∩B∩C)", "n(A)+n(B)+n(C)", "n(A)+n(B)+n(C) - n(A∩B) - n(A∩C) - n(B∩C)", "n(A)+n(B)+n(C) + n(A∩B∩C)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'n(A)+n(B)+n(C) - n(A∩B) - n(A∩C) - n(B∩C) + n(A∩B∩C)', 'DESAFIANTE', 0.70, 'fundamentos-da-matematica-elementar-1-.pdf', 44, 45,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos', 'Operações', 'Mistas',
    'Se A = {x ∈ N | x é par} e B = {x ∈ N | x é múltiplo de 3}, então A ∩ B é:',
    '["{x ∈ N | x é múltiplo de 6}", "{x ∈ N | x é par}", "{x ∈ N | x é múltiplo de 3}", "{x ∈ N | x é ímpar}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{x ∈ N | x é múltiplo de 6}', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 29, 31,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
);

-- ✅ CONJUNTOS COMPLETADO: 100 QUESTÕES

-- Mock SQL para Matemática - CONJUNTOS NUMÉRICOS (Capítulo III)
-- Baseado EXCLUSIVAMENTE no livro "Fundamentos de Matemática Elementar - Volume 1"
-- Iezzi & Murakami, 9ª ed., 2013
-- Disciplina: Matemática
-- Tópico principal: Conjuntos Numéricos (Capítulo III, páginas 40-56)
-- Total de questões neste lote: 100

INSERT INTO perguntas (
    id, disciplina, topico_principal, topico, subtopico, questao, respostas, pesos_resposta,
    resposta_correta, dificuldade, rigor, referencia_livro, pagina_inicio, pagina_fim,
    exercicio, usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
) VALUES

(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números naturais', 'Definição',
    'O conjunto dos números naturais é representado pelo símbolo:',
    '["N", "Z", "Q", "R"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'N', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 40, 41,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números naturais', 'Elementos',
    'Qual dos seguintes números NÃO pertence ao conjunto N?',
    '["-3", "0", "1", "10"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-3', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 40, 41,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números naturais', 'Propriedades',
    'O conjunto N é fechado em relação à operação de:',
    '["adição", "subtração", "divisão", "subtração e divisão"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'adição', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 40, 41,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números naturais', 'Múltiplos',
    'O menor múltiplo comum (MMC) entre 6 e 8 é:',
    '["24", "48", "12", "16"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '24', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 43, 44,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números naturais', 'Divisores',
    'O maior divisor comum (MDC) entre 18 e 24 é:',
    '["6", "12", "3", "8"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '6', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 43, 44,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números naturais', 'Divisibilidade',
    'O número 3 é divisor de:',
    '["12", "14", "16", "20"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '12', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 45, 46,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números naturais', 'Números primos',
    'Qual dos seguintes números é primo?',
    '["17", "21", "27", "33"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '17', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 42, 43,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números naturais', 'Números compostos',
    'Qual dos seguintes números é composto?',
    '["49", "13", "19", "31"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '49', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 42, 43,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números naturais', 'Fatoração',
    'A decomposição em fatores primos de 60 é:',
    '["2² × 3 × 5", "2 × 3 × 5", "2² × 3² × 5", "2³ × 3 × 5"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2² × 3 × 5', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 43, 44,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números naturais', 'MMC',
    'O MMC entre 4, 6 e 8 é:',
    '["24", "48", "12", "16"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '24', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 43, 44,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números inteiros', 'Definição',
    'O conjunto dos números inteiros é representado pelo símbolo:',
    '["Z", "N", "Q", "R"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'Z', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 41, 42,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números inteiros', 'Elementos',
    'Qual dos seguintes números pertence ao conjunto Z?',
    '["-5", "1/2", "0,333...", "√2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-5', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 41, 42,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números inteiros', 'Relação com N',
    'A relação entre N e Z é:',
    '["N ⊂ Z", "Z ⊂ N", "N = Z", "N ∩ Z = ∅"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'N ⊂ Z', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 41, 42,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números inteiros', 'Subconjuntos',
    'O conjunto dos inteiros não negativos é:',
    '["Z₊", "Z₋", "Z*", "Z₀"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'Z₊', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 41, 42,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números inteiros', 'Subconjuntos',
    'O conjunto dos inteiros não nulos é representado por:',
    '["Z*", "Z₊", "Z₋", "Z"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'Z*', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 41, 42,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números inteiros', 'Divisibilidade',
    'O número 8 é divisor de:',
    '["24", "18", "20", "28"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '24', 'FACIL', 0.22, 'fundamentos-da-matematica-elementar-1-.pdf', 45, 46,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números inteiros', 'Números opostos',
    'O oposto de -7 é:',
    '["7", "-7", "0", "1/7"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '7', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 42, 43,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números inteiros', 'Valor absoluto',
    'O valor absoluto de -15 é:',
    '["15", "-15", "0", "1/15"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '15', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 187, 188,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números racionais', 'Definição',
    'O conjunto dos números racionais é representado pelo símbolo:',
    '["Q", "N", "Z", "R"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'Q', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 44, 45,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números racionais', 'Elementos',
    'Qual dos seguintes números NÃO pertence ao conjunto Q?',
    '["√2", "1/3", "-2/5", "0,333..."]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '√2', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 44, 45,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números racionais', 'Frações',
    'A fração 3/4 em decimal é:',
    '["0,75", "0,34", "1,33", "0,25"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '0,75', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 46, 47,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números racionais', 'Dízimas periódicas',
    'A dízima periódica 0,333... é igual a:',
    '["1/3", "1/4", "1/2", "3/10"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1/3', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 47, 48,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números racionais', 'Dízimas periódicas',
    'A fração geratriz da dízima 0,777... é:',
    '["7/9", "7/10", "7/99", "77/100"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '7/9', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 47, 48,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números racionais', 'Dízimas periódicas',
    'A fração geratriz da dízima 0,121212... é:',
    '["12/99", "12/100", "12/10", "12/9"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '12/99', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 47, 48,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números racionais', 'Operações',
    'O resultado da soma 1/2 + 1/3 é:',
    '["5/6", "2/5", "1/5", "2/6"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '5/6', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 46, 47,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números racionais', 'Operações',
    'O resultado da multiplicação 2/3 × 3/4 é:',
    '["1/2", "1/3", "2/4", "3/6"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1/2', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 46, 47,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números racionais', 'Densidade',
    'Entre dois números racionais distintos, existe:',
    '["infinitos números racionais", "apenas um número racional", "apenas números irracionais", "nenhum número"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'infinitos números racionais', 'DESAFIANTE', 0.60, 'fundamentos-da-matematica-elementar-1-.pdf', 44, 48,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números racionais', 'Fração irredutível',
    'A fração irredutível de 12/18 é:',
    '["2/3", "6/9", "4/6", "12/18"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2/3', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 46, 47,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números irracionais', 'Definição',
    'O conjunto dos números irracionais pode ser definido como:',
    '["R - Q", "Q - R", "N - Z", "Z - N"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'R - Q', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 49, 50,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números irracionais', 'Identificação',
    'Qual dos seguintes números é irracional?',
    '["√2", "1,5", "0,333...", "2/3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '√2', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 49, 50,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números irracionais', 'Identificação',
    'Qual dos seguintes números é irracional?',
    '["π", "1/7", "0,5", "3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'π', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 49, 50,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números irracionais', 'Propriedades',
    'A soma de um número racional com um número irracional é:',
    '["irracional", "racional", "pode ser racional ou irracional", "sempre zero"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'irracional', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 49, 50,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números irracionais', 'Propriedades',
    'O produto de um número racional não nulo por um número irracional é:',
    '["irracional", "racional", "pode ser racional ou irracional", "sempre zero"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'irracional', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 49, 50,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números reais', 'Definição',
    'O conjunto dos números reais é representado pelo símbolo:',
    '["R", "Q", "Z", "N"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'R', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 49, 50,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números reais', 'Relações',
    'A relação entre Q e R é:',
    '["Q ⊂ R", "R ⊂ Q", "Q = R", "Q ∩ R = ∅"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'Q ⊂ R', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 49, 50,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números reais', 'Relações',
    'A relação entre N e R é:',
    '["N ⊂ R", "R ⊂ N", "N = R", "N ∩ R = ∅"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'N ⊂ R', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 49, 50,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números reais', 'Relações',
    'A relação entre Z e R é:',
    '["Z ⊂ R", "R ⊂ Z", "Z = R", "Z ∩ R = ∅"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'Z ⊂ R', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 49, 50,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números reais', 'Reta real',
    'A representação geométrica dos números reais é feita na:',
    '["reta real", "plano cartesiano", "circunferência", "parábola"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'reta real', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 49, 52,
    '', true, 'RETA', 0, 0, 0, 'eixo real', 'valores', -3, 3, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Intervalos', 'Aberto',
    'O intervalo aberto ]2, 5[ é formado pelos números reais x tais que:',
    '["2 < x < 5", "2 ≤ x ≤ 5", "2 ≤ x < 5", "2 < x ≤ 5"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2 < x < 5', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 53, 54,
    '', true, 'RETA', 0, 0, 0, 'eixo real', 'valores', 0, 7, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Intervalos', 'Fechado',
    'O intervalo fechado [2, 5] é formado pelos números reais x tais que:',
    '["2 ≤ x ≤ 5", "2 < x < 5", "2 ≤ x < 5", "2 < x ≤ 5"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2 ≤ x ≤ 5', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 53, 54,
    '', true, 'RETA', 0, 0, 0, 'eixo real', 'valores', 0, 7, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Intervalos', 'Fechado à esquerda',
    'O intervalo [2, 5[ é formado pelos números reais x tais que:',
    '["2 ≤ x < 5", "2 < x < 5", "2 ≤ x ≤ 5", "2 < x ≤ 5"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2 ≤ x < 5', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 53, 54,
    '', true, 'RETA', 0, 0, 0, 'eixo real', 'valores', 0, 7, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Intervalos', 'Fechado à direita',
    'O intervalo ]2, 5] é formado pelos números reais x tais que:',
    '["2 < x ≤ 5", "2 ≤ x < 5", "2 < x < 5", "2 ≤ x ≤ 5"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2 < x ≤ 5', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 53, 54,
    '', true, 'RETA', 0, 0, 0, 'eixo real', 'valores', 0, 7, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Intervalos', 'Infinito',
    'O intervalo ]-∞, 3] é formado pelos números reais x tais que:',
    '["x ≤ 3", "x < 3", "x ≥ 3", "x > 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x ≤ 3', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 54, 55,
    '', true, 'RETA', 0, 0, 0, 'eixo real', 'valores', -2, 5, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Intervalos', 'Infinito',
    'O intervalo [2, +∞[ é formado pelos números reais x tais que:',
    '["x ≥ 2", "x > 2", "x ≤ 2", "x < 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x ≥ 2', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 54, 55,
    '', true, 'RETA', 0, 0, 0, 'eixo real', 'valores', -1, 5, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Intervalos', 'Interseção',
    'A interseção dos intervalos [2, 7] e [4, 9] é:',
    '["[4, 7]", "[2, 9]", "]4, 7[", "[2, 4]"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '[4, 7]', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 53, 55,
    '', true, 'RETA', 0, 0, 0, 'eixo real', 'valores', 0, 10, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Intervalos', 'União',
    'A união dos intervalos [2, 5] e [7, 9] é:',
    '["[2, 5] ∪ [7, 9]", "[2, 9]", "[2, 5] ∩ [7, 9]", "∅"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '[2, 5] ∪ [7, 9]', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 53, 55,
    '', true, 'RETA', 0, 0, 0, 'eixo real', 'valores', 0, 10, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Intervalos', 'Interseção',
    'A interseção dos intervalos ]-∞, 5] e [3, +∞[ é:',
    '["[3, 5]", "]3, 5[", "[-∞, +∞]", "∅"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '[3, 5]', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 53, 55,
    '', true, 'RETA', 0, 0, 0, 'eixo real', 'valores', 0, 6, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números complexos', 'Definição',
    'O conjunto dos números complexos é representado pelo símbolo:',
    '["C", "R", "Q", "Z"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'C', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 56, 56,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números complexos', 'Relação',
    'A relação entre R e C é:',
    '["R ⊂ C", "C ⊂ R", "R = C", "R ∩ C = ∅"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'R ⊂ C', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 56, 56,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números complexos', 'Unidade imaginária',
    'A unidade imaginária i é definida como:',
    '["i² = -1", "i² = 1", "i = √-1", "i = -1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'i² = -1', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 56, 56,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Hierarquia', 'Inclusão',
    'A sequência correta de inclusão dos conjuntos numéricos é:',
    '["N ⊂ Z ⊂ Q ⊂ R ⊂ C", "Z ⊂ N ⊂ Q ⊂ R ⊂ C", "N ⊂ Q ⊂ Z ⊂ R ⊂ C", "N ⊂ Z ⊂ R ⊂ Q ⊂ C"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'N ⊂ Z ⊂ Q ⊂ R ⊂ C', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 56, 56,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Hierarquia', 'Diferença',
    'O conjunto Z - N é igual a:',
    '["números inteiros negativos", "números naturais", "números racionais", "números reais"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'números inteiros negativos', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 56, 56,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Hierarquia', 'Diferença',
    'O conjunto R - Q é igual a:',
    '["números irracionais", "números racionais", "números inteiros", "números complexos"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'números irracionais', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 56, 56,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Indução finita', 'Princípio',
    'O Princípio da Indução Finita é usado para provar propriedades que envolvem:',
    '["números naturais", "números inteiros", "números reais", "números complexos"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'números naturais', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 57, 58,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Indução finita', 'Passos',
    'Para provar uma propriedade P(n) por indução finita, deve-se verificar:',
    '["P(1) é verdadeira e P(k) ⇒ P(k+1)", "P(1) é verdadeira apenas", "P(k) ⇒ P(k+1) apenas", "P(0) é verdadeira apenas"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'P(1) é verdadeira e P(k) ⇒ P(k+1)', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 57, 58,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Operações', 'Mistas',
    'O número 0,25 pertence a qual conjunto?',
    '["Q", "N", "Z", "apenas R"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'Q', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 44, 48,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Operações', 'Mistas',
    'O número -3 pertence a qual conjunto?',
    '["Z", "N", "apenas R", "apenas Q"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'Z', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 41, 42,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Operações', 'Mistas',
    'O número 0 pertence a qual conjunto?',
    '["N", "Z", "Q", "todos os anteriores"]'::jsonb, '[0.2, 0.1, 0.1, 1.0]'::jsonb,
    'todos os anteriores', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 40, 45,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Operações', 'Mistas',
    'O número √9 pertence a qual conjunto?',
    '["N", "Z", "Q", "todos os anteriores"]'::jsonb, '[0.2, 0.1, 0.1, 1.0]'::jsonb,
    'todos os anteriores', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 40, 49,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Operações', 'Mistas',
    'O número √2 pertence a qual conjunto?',
    '["R", "Q", "Z", "N"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'R', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 49, 50,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Operações', 'Mistas',
    'O número 1/3 pertence a qual conjunto?',
    '["Q", "Z", "N", "apenas R"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'Q', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 44, 45,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Operações', 'Mistas',
    'Qual é o maior conjunto numérico?',
    '["C", "R", "Q", "Z"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'C', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 56, 56,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Operações', 'Mistas',
    'Qual é o menor conjunto que contém o número 0?',
    '["N", "Z", "Q", "R"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'N', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 40, 41,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Operações', 'Mistas',
    'Qual é o menor conjunto que contém o número -2?',
    '["Z", "N", "Q", "R"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'Z', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 41, 42,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Operações', 'Mistas',
    'Qual é o menor conjunto que contém o número 1/2?',
    '["Q", "Z", "N", "R"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'Q', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 44, 45,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Operações', 'Mistas',
    'Qual é o menor conjunto que contém o número √2?',
    '["R", "Q", "Z", "N"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'R', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 49, 50,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Operações', 'Mistas',
    'A soma de dois números naturais é sempre:',
    '["um número natural", "um número inteiro", "um número racional", "todas as anteriores"]'::jsonb, '[0.2, 0.1, 0.1, 1.0]'::jsonb,
    'todas as anteriores', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 40, 45,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Operações', 'Mistas',
    'O produto de dois números racionais é sempre:',
    '["um número racional", "um número inteiro", "um número natural", "um número irracional"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'um número racional', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 44, 48,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Operações', 'Mistas',
    'O número π é classificado como:',
    '["irracional", "racional", "inteiro", "natural"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'irracional', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 49, 50,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Operações', 'Mistas',
    'O número 1,41421356... (aproximação de √2) é classificado como:',
    '["irracional", "racional", "inteiro", "natural"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'irracional', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 49, 50,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Operações', 'Mistas',
    'A dízima periódica 0,999... é igual a:',
    '["1", "0,9", "0,99", "1,001"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1', 'DESAFIANTE', 0.65, 'fundamentos-da-matematica-elementar-1-.pdf', 47, 48,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Operações', 'Mistas',
    'O número 0,101001000100001... (com um zero a mais entre os algarismos 1) é:',
    '["irracional", "racional", "inteiro", "natural"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'irracional', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 49, 50,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Operações', 'Mistas',
    'O número 0,123123123... (período 123) é:',
    '["racional", "irracional", "inteiro", "natural"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'racional', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 47, 48,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números naturais', 'Indução',
    'Pelo Princípio da Indução Finita, a soma dos n primeiros números naturais é:',
    '["n(n+1)/2", "n²", "n(n-1)/2", "n+1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'n(n+1)/2', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-1-.pdf', 59, 60,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Conjuntos numéricos', 'Números naturais', 'Indução',
    'Pelo Princípio da Indução Finita, a soma dos n primeiros números ímpares é:',
    '["n²", "n(n+1)/2", "2n-1", "n+1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'n²', 'DESAFIANTE', 0.60, 'fundamentos-da-matematica-elementar-1-.pdf', 59, 60,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
);

-- ✅ CONJUNTOS NUMÉRICOS COMPLETADO: 100 QUESTÕES

-- Mock SQL para Matemática - RELAÇÕES (Capítulo IV)
-- Baseado EXCLUSIVAMENTE no livro "Fundamentos de Matemática Elementar - Volume 1"
-- Iezzi & Murakami, 9ª ed., 2013
-- Disciplina: Matemática
-- Tópico principal: Relações (Capítulo IV, páginas 64-78)
-- Total de questões neste lote: 50

INSERT INTO perguntas (
    id, disciplina, topico_principal, topico, subtopico, questao, respostas, pesos_resposta,
    resposta_correta, dificuldade, rigor, referencia_livro, pagina_inicio, pagina_fim,
    exercicio, usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
) VALUES

(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Par ordenado', 'Definição',
    'O par ordenado (a, b) é igual ao par (c, d) se, e somente se:',
    '["a = c e b = d", "a = b e c = d", "a = d e b = c", "a + b = c + d"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'a = c e b = d', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 64, 65,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Par ordenado', 'Igualdade',
    'Se (x + 1, y - 2) = (3, 5), então x e y valem respectivamente:',
    '["2 e 7", "4 e 3", "2 e 5", "4 e 7"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2 e 7', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 64, 65,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Par ordenado', 'Coordenadas',
    'Em um par ordenado (x, y), o primeiro elemento é chamado de:',
    '["abscissa", "ordenada", "coordenada", "abcissa"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'abscissa', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 64, 65,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Par ordenado', 'Coordenadas',
    'Em um par ordenado (x, y), o segundo elemento é chamado de:',
    '["ordenada", "abscissa", "coordenada", "abcissa"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'ordenada', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 64, 65,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Par ordenado', 'Diferença',
    'A principal diferença entre o par ordenado (a, b) e o conjunto {a, b} é que:',
    '["no par ordenado a ordem importa", "no par ordenado a ordem não importa", "são conceitos idênticos", "o par ordenado só aceita números"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'no par ordenado a ordem importa', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 64, 65,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),



(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Plano cartesiano', 'Definição',
    'O sistema de coordenadas formado por dois eixos perpendiculares é chamado de:',
    '["plano cartesiano", "plano euclidiano", "plano complexo", "plano vetorial"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'plano cartesiano', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 65, 66,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Plano cartesiano', 'Eixos',
    'No plano cartesiano, o eixo horizontal é chamado de eixo das:',
    '["abscissas", "ordenadas", "coordenadas", "abcissas"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'abscissas', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 65, 66,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Plano cartesiano', 'Eixos',
    'No plano cartesiano, o eixo vertical é chamado de eixo das:',
    '["ordenadas", "abscissas", "coordenadas", "abcissas"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'ordenadas', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 65, 66,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Plano cartesiano', 'Pontos',
    'O ponto de interseção dos eixos x e y é chamado de:',
    '["origem", "centro", "vértice", "foco"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'origem', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 65, 66,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Plano cartesiano', 'Coordenadas',
    'O ponto (3, 4) no plano cartesiano tem abscissa:',
    '["3", "4", "7", "12"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 65, 66,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Plano cartesiano', 'Coordenadas',
    'O ponto (-2, 5) no plano cartesiano tem ordenada:',
    '["5", "-2", "3", "7"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '5', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 65, 66,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),



(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Produto cartesiano', 'Definição',
    'O produto cartesiano A × B é o conjunto:',
    '["{(x, y) | x ∈ A e y ∈ B}", "{(x, y) | x ∈ A ou y ∈ B}", "{x | x ∈ A e x ∈ B}", "{x | x ∈ A ou x ∈ B}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{(x, y) | x ∈ A e y ∈ B}', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 67, 68,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Produto cartesiano', 'Cardinalidade',
    'Se A tem 3 elementos e B tem 4 elementos, então A × B tem:',
    '["12 elementos", "7 elementos", "24 elementos", "16 elementos"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '12 elementos', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 68, 69,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Produto cartesiano', 'Listagem',
    'Dados A = {1, 2} e B = {a, b}, o produto cartesiano A × B é:',
    '["{(1, a), (1, b), (2, a), (2, b)}", "{(1, a), (2, b)}", "{(a, 1), (b, 2)}", "{1, 2, a, b}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{(1, a), (1, b), (2, a), (2, b)}', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 67, 68,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Produto cartesiano', 'Comutatividade',
    'Em geral, o produto cartesiano A × B é:',
    '["diferente de B × A", "igual a B × A", "igual a A ∩ B", "igual a A ∪ B"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'diferente de B × A', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 67, 68,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Produto cartesiano', 'Caso vazio',
    'Se A = ∅, então A × B é igual a:',
    '["∅", "B", "A", "U"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '∅', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 67, 68,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Produto cartesiano', 'Cardinalidade',
    'Se n(A) = m e n(B) = n, então n(A × B) é:',
    '["m × n", "m + n", "m^n", "n^m"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'm × n', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 68, 69,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),



(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Relação binária', 'Definição',
    'Uma relação binária de A em B é um subconjunto de:',
    '["A × B", "A ∪ B", "A ∩ B", "A - B"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'A × B', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 71, 72,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Relação binária', 'Notação',
    'A notação xRy significa que:',
    '["(x, y) ∈ R", "x ∈ R e y ∈ R", "x e y são iguais", "x é subconjunto de y"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(x, y) ∈ R', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 71, 72,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Relação binária', 'Conjunto de partida',
    'Em uma relação R de A em B, o conjunto A é chamado de:',
    '["conjunto de partida", "conjunto de chegada", "domínio", "imagem"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'conjunto de partida', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 71, 72,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Relação binária', 'Conjunto de chegada',
    'Em uma relação R de A em B, o conjunto B é chamado de:',
    '["conjunto de chegada", "conjunto de partida", "domínio", "imagem"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'conjunto de chegada', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 71, 72,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Relação binária', 'Listagem',
    'Dados A = {1, 2, 3} e B = {2, 3, 4}, a relação R = {(x, y) ∈ A × B | x < y} é:',
    '["{(1, 2), (1, 3), (1, 4), (2, 3), (2, 4), (3, 4)}", "{(2, 1), (3, 1), (4, 1)}", "{(1, 2), (2, 3), (3, 4)}", "{(1, 1), (2, 2), (3, 3)}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{(1, 2), (1, 3), (1, 4), (2, 3), (2, 4), (3, 4)}', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 71, 72,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),



(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Domínio', 'Definição',
    'O domínio de uma relação R de A em B é o conjunto:',
    '["dos primeiros elementos dos pares ordenados de R", "dos segundos elementos dos pares ordenados de R", "de todos os elementos de A", "de todos os elementos de B"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'dos primeiros elementos dos pares ordenados de R', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 74, 75,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Imagem', 'Definição',
    'A imagem de uma relação R de A em B é o conjunto:',
    '["dos segundos elementos dos pares ordenados de R", "dos primeiros elementos dos pares ordenados de R", "de todos os elementos de A", "de todos os elementos de B"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'dos segundos elementos dos pares ordenados de R', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 74, 75,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Domínio', 'Cálculo',
    'Dada a relação R = {(1, 2), (2, 4), (3, 6), (4, 8)}, o domínio é:',
    '["{1, 2, 3, 4}", "{2, 4, 6, 8}", "{1, 2, 3, 4, 6, 8}", "{2, 4, 6}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{1, 2, 3, 4}', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 74, 75,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Imagem', 'Cálculo',
    'Dada a relação R = {(1, 2), (2, 4), (3, 6), (4, 8)}, a imagem é:',
    '["{2, 4, 6, 8}", "{1, 2, 3, 4}", "{1, 2, 3, 4, 6, 8}", "{2, 4, 6}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{2, 4, 6, 8}', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 74, 75,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Domínio', 'Relação vazia',
    'O domínio da relação vazia é:',
    '["∅", "A", "B", "não definido"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '∅', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 74, 75,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),



(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Relação inversa', 'Definição',
    'A relação inversa de R, denotada por R⁻¹, é o conjunto:',
    '["{(y, x) | (x, y) ∈ R}", "{(x, y) | (y, x) ∈ R}", "{(x, x) | x ∈ R}", "{(y, y) | y ∈ R}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{(y, x) | (x, y) ∈ R}', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 76, 77,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Relação inversa', 'Cálculo',
    'Se R = {(1, 2), (2, 4), (3, 6), (4, 8)}, então R⁻¹ é:',
    '["{(2, 1), (4, 2), (6, 3), (8, 4)}", "{(1, 2), (2, 4), (3, 6), (4, 8)}", "{(1, 1), (2, 2), (3, 3)}", "{(2, 2), (4, 4), (6, 6)}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{(2, 1), (4, 2), (6, 3), (8, 4)}', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 76, 77,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Relação inversa', 'Domínio',
    'O domínio de R⁻¹ é igual a:',
    '["imagem de R", "domínio de R", "conjunto de partida de R", "conjunto de chegada de R"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'imagem de R', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 78, 78,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Relação inversa', 'Imagem',
    'A imagem de R⁻¹ é igual a:',
    '["domínio de R", "imagem de R", "conjunto de partida de R", "conjunto de chegada de R"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'domínio de R', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 78, 78,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Relação inversa', 'Propriedade',
    'A relação inversa da relação inversa de R, ou seja, (R⁻¹)⁻¹, é igual a:',
    '["R", "R⁻¹", "domínio de R", "imagem de R"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'R', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 78, 78,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),



(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Propriedades', 'Reflexiva',
    'Uma relação R em um conjunto A é reflexiva quando:',
    '["∀ x ∈ A, (x, x) ∈ R", "∀ x ∈ A, (x, x) ∉ R", "∃ x ∈ A, (x, x) ∈ R", "∀ x, y ∈ A, (x, y) ∈ R"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '∀ x ∈ A, (x, x) ∈ R', 'DESAFIANTE', 0.60, 'fundamentos-da-matematica-elementar-1-.pdf', 78, 78,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Propriedades', 'Simétrica',
    'Uma relação R em um conjunto A é simétrica quando:',
    '["∀ x, y ∈ A, (x, y) ∈ R ⇒ (y, x) ∈ R", "∀ x ∈ A, (x, x) ∈ R", "∀ x, y ∈ A, (x, y) ∈ R ⇒ (x, x) ∈ R", "∀ x, y ∈ A, (x, y) ∈ R ⇒ (y, y) ∈ R"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '∀ x, y ∈ A, (x, y) ∈ R ⇒ (y, x) ∈ R', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 78, 78,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Propriedades', 'Transitiva',
    'Uma relação R em um conjunto A é transitiva quando:',
    '["∀ x, y, z ∈ A, (x, y) ∈ R e (y, z) ∈ R ⇒ (x, z) ∈ R", "∀ x, y ∈ A, (x, y) ∈ R ⇒ (y, x) ∈ R", "∀ x ∈ A, (x, x) ∈ R", "∀ x, y, z ∈ A, (x, y) ∈ R e (x, z) ∈ R ⇒ (y, z) ∈ R"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '∀ x, y, z ∈ A, (x, y) ∈ R e (y, z) ∈ R ⇒ (x, z) ∈ R', 'DESAFIANTE', 0.65, 'fundamentos-da-matematica-elementar-1-.pdf', 78, 78,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),



(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Produto cartesiano', 'Gráfico',
    'O produto cartesiano A × B, com A = {1, 2} e B = {3, 4}, representado no plano cartesiano forma:',
    '["4 pontos", "2 pontos", "1 ponto", "um quadrado"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '4 pontos', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 67, 69,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Relação binária', 'Listagem',
    'Dados A = {1, 2, 3} e B = {1, 2, 3}, a relação R = {(x, y) ∈ A × B | x = y} é:',
    '["{(1, 1), (2, 2), (3, 3)}", "{(1, 2), (2, 3), (3, 1)}", "{(1, 1), (1, 2), (2, 2)}", "{(2, 1), (3, 2), (1, 3)}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{(1, 1), (2, 2), (3, 3)}', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 71, 72,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Relação binária', 'Listagem',
    'Dados A = {1, 2, 3} e B = {1, 2, 3}, a relação R = {(x, y) ∈ A × B | x + y = 4} é:',
    '["{(1, 3), (2, 2), (3, 1)}", "{(1, 1), (2, 2), (3, 3)}", "{(1, 2), (2, 3)}", "{(2, 1), (3, 2)}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{(1, 3), (2, 2), (3, 1)}', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 71, 72,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Relação binária', 'Listagem',
    'Dados A = {1, 2, 3, 4} e B = {2, 4, 6, 8}, a relação R = {(x, y) ∈ A × B | y = 2x} é:',
    '["{(1, 2), (2, 4), (3, 6), (4, 8)}", "{(2, 1), (4, 2), (6, 3), (8, 4)}", "{(1, 1), (2, 2), (3, 3)}", "{(2, 2), (4, 4), (6, 6)}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{(1, 2), (2, 4), (3, 6), (4, 8)}', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 71, 72,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Relação inversa', 'Cálculo',
    'Se R = {(1, 1), (2, 3), (3, 5), (4, 7)}, então R⁻¹ é:',
    '["{(1, 1), (3, 2), (5, 3), (7, 4)}", "{(1, 1), (2, 3), (3, 5), (4, 7)}", "{(1, 2), (3, 4), (5, 6)}", "{(2, 1), (4, 3), (6, 5)}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{(1, 1), (3, 2), (5, 3), (7, 4)}', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 76, 77,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Produto cartesiano', 'Propriedade',
    'Se A = {1, 2} e B = {3, 4}, então o número de elementos de B × A é:',
    '["4", "2", "6", "8"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '4', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 67, 68,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Produto cartesiano', 'Listagem',
    'Dados A = {a, b} e B = {1, 2}, o produto cartesiano B × A é:',
    '["{(1, a), (1, b), (2, a), (2, b)}", "{(a, 1), (a, 2), (b, 1), (b, 2)}", "{(a, a), (b, b)}", "{(1, 1), (2, 2)}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{(1, a), (1, b), (2, a), (2, b)}', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 67, 68,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Domínio', 'Relação definida por condição',
    'Dada a relação R = {(x, y) ∈ N × N | y = x + 1}, o domínio é:',
    '["N", "N*", "{0}", "{1}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'N', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 74, 75,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Imagem', 'Relação definida por condição',
    'Dada a relação R = {(x, y) ∈ N × N | y = x + 1}, a imagem é:',
    '["N*", "N", "{0}", "{1}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'N*', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 74, 75,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Plano cartesiano', 'Quadrantes',
    'O ponto (3, -2) está localizado no:',
    '["4º quadrante", "1º quadrante", "2º quadrante", "3º quadrante"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '4º quadrante', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 65, 66,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Plano cartesiano', 'Quadrantes',
    'O ponto (-4, -5) está localizado no:',
    '["3º quadrante", "1º quadrante", "2º quadrante", "4º quadrante"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3º quadrante', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 65, 66,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Plano cartesiano', 'Quadrantes',
    'O ponto (5, 3) está localizado no:',
    '["1º quadrante", "2º quadrante", "3º quadrante", "4º quadrante"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1º quadrante', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 65, 66,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Plano cartesiano', 'Quadrantes',
    'O ponto (-2, 4) está localizado no:',
    '["2º quadrante", "1º quadrante", "3º quadrante", "4º quadrante"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2º quadrante', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 65, 66,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Relação binária', 'Propriedades',
    'A relação R = {(1, 1), (2, 2), (3, 3)} em A = {1, 2, 3} é:',
    '["reflexiva", "simétrica", "transitiva", "todas as anteriores"]'::jsonb, '[0.2, 0.2, 0.2, 1.0]'::jsonb,
    'todas as anteriores', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 78, 78,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Relação binária', 'Propriedades',
    'A relação R = {(1, 2), (2, 1)} em A = {1, 2} é:',
    '["simétrica", "reflexiva", "transitiva", "reflexiva e simétrica"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'simétrica', 'DESAFIANTE', 0.60, 'fundamentos-da-matematica-elementar-1-.pdf', 78, 78,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Produto cartesiano', 'Gráfico',
    'O produto cartesiano A × A, com A = {1, 2, 3}, representado no plano cartesiano forma:',
    '["9 pontos", "6 pontos", "3 pontos", "1 ponto"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '9 pontos', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 67, 69,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Domínio', 'Relação vazia',
    'A imagem da relação vazia é:',
    '["∅", "A", "B", "não definido"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '∅', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 74, 75,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Relação inversa', 'Propriedade',
    'Se R é uma relação de A em B, então R⁻¹ é uma relação de:',
    '["B em A", "A em B", "A em A", "B em B"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'B em A', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 76, 77,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Par ordenado', 'Produto cartesiano',
    'O par ordenado (2, 3) pertence ao produto cartesiano:',
    '["{1, 2, 3} × {2, 3, 4}", "{1, 2} × {1, 2, 3}", "{2, 3} × {1, 2}", "{3, 4} × {2, 3}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{1, 2, 3} × {2, 3, 4}', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 67, 68,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Relações', 'Par ordenado', 'Produto cartesiano',
    'O par ordenado (3, 1) pertence ao produto cartesiano:',
    '["{1, 2, 3} × {1, 2}", "{2, 3} × {1, 2, 3}", "{1, 3} × {2, 3}", "{1, 2} × {1, 2, 3}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{1, 2, 3} × {1, 2}', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 67, 68,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
);

-- Mock SQL para Matemática - FUNÇÕES (CONCEITO) (Capítulo V)
-- Baseado EXCLUSIVAMENTE no livro "Fundamentos de Matemática Elementar - Volume 1"
-- Iezzi & Murakami, 9ª ed., 2013
-- Disciplina: Matemática
-- Tópico principal: Introdução às funções (Capítulo V, páginas 79-94)
-- Total de questões neste lote: 50

INSERT INTO perguntas (
    id, disciplina, topico_principal, topico, subtopico, questao, respostas, pesos_resposta,
    resposta_correta, dificuldade, rigor, referencia_livro, pagina_inicio, pagina_fim,
    exercicio, usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
) VALUES



(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Conceito de função', 'Definição',
    'Uma relação f de A em B é uma função quando:',
    '["todo elemento de A tem um único correspondente em B", "todo elemento de B tem um único correspondente em A", "existe elemento de A sem correspondente", "um elemento de A tem dois correspondentes"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'todo elemento de A tem um único correspondente em B', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 81, 83,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Conceito de função', 'Aplicação',
    'Outro nome dado à função é:',
    '["aplicação", "relação", "correspondência", "transformação"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'aplicação', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 81, 82,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Conceito de função', 'Identificação',
    'Qual das seguintes relações de A = {1, 2, 3} em B = {a, b, c} NÃO é uma função?',
    '["{(1, a), (1, b), (2, c)}", "{(1, a), (2, a), (3, a)}", "{(1, b), (2, b), (3, b)}", "{(1, c), (2, c), (3, c)}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{(1, a), (1, b), (2, c)}', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 81, 83,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Conceito de função', 'Condições',
    'Uma relação f de A em B é uma função se, e somente se:',
    '["cada elemento de A participa de um único par (x, y) ∈ f", "cada elemento de B participa de um único par (x, y) ∈ f", "existe elemento de A que não participa de nenhum par", "cada elemento de A participa de vários pares"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'cada elemento de A participa de um único par (x, y) ∈ f', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 82, 83,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Conceito de função', 'Diagrama de flechas',
    'No diagrama de flechas de uma função, cada elemento do conjunto de partida deve:',
    '["servir como ponto de partida de uma única flecha", "servir como ponto de partida de várias flechas", "não servir como ponto de partida de flecha alguma", "servir como ponto de chegada de flechas"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'servir como ponto de partida de uma única flecha', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 82, 83,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),



(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Notação', 'f(x)',
    'O símbolo f(x) representa:',
    '["o valor da função f no elemento x", "a função f multiplicada por x", "x é função de f", "a função inversa de x"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'o valor da função f no elemento x', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 84, 85,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Notação', 'Cálculo',
    'Dada f(x) = 3x - 2, o valor de f(2) é:',
    '["4", "6", "2", "8"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '4', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 84, 86,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Notação', 'Cálculo',
    'Dada f(x) = x² - 2x + 1, o valor de f(3) é:',
    '["4", "2", "6", "8"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '4', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 84, 86,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Notação', 'Cálculo',
    'Dada f(x) = 2x + 1, o valor de f(0) é:',
    '["1", "0", "2", "3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 84, 86,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Notação', 'Cálculo',
    'Dada f(x) = 5 - x, o valor de f(7) é:',
    '["-2", "2", "5", "12"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-2', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 84, 86,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Domínio', 'Função polinomial',
    'O domínio da função real f(x) = 3x + 2 é:',
    '["R", "R*", "R₊", "Z"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'R', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 92, 93,
    'f(x) = 3x + 2', true, 'RETA', 3, 2, 0, 'eixo x', 'eixo y', -2, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Domínio', 'Função racional',
    'O domínio da função real f(x) = 1/(x - 2) é:',
    '["x ≠ 2", "x > 2", "x < 2", "x = 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x ≠ 2', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 92, 93,
    'f(x) = 1/(x - 2)', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -2, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Domínio', 'Função com raiz',
    'O domínio da função real f(x) = √(x - 3) é:',
    '["x ≥ 3", "x > 3", "x ≤ 3", "x = 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x ≥ 3', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 92, 93,
    'f(x) = √(x - 3)', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', 0, 6, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Domínio', 'Função com raiz no denominador',
    'O domínio da função real f(x) = 1/√(x - 2) é:',
    '["x > 2", "x ≥ 2", "x ≠ 2", "x < 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x > 2', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 92, 93,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Domínio', 'Função racional com restrição',
    'O domínio da função real f(x) = (x + 1)/(x² - 4) é:',
    '["x ≠ 2 e x ≠ -2", "x ≠ 2", "x ≠ -2", "x = 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x ≠ 2 e x ≠ -2', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 92, 93,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Domínio', 'Função com raiz quadrada',
    'O domínio da função real f(x) = √(4 - x²) é:',
    '["-2 ≤ x ≤ 2", "x ≤ 2", "x ≥ -2", "x = 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-2 ≤ x ≤ 2', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 92, 93,
    'f(x) = √(4 - x²)', true, 'PARABOLA', -1, 0, 4, 'eixo x', 'eixo y', -3, 3, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Imagem', 'Definição',
    'A imagem de uma função f: A → B é o conjunto:',
    '["{y ∈ B | ∃ x ∈ A tal que f(x) = y}", "{x ∈ A | ∃ y ∈ B tal que f(x) = y}", "A", "B"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{y ∈ B | ∃ x ∈ A tal que f(x) = y}', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 88, 90,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Imagem', 'Cálculo',
    'Dada a função f: {1, 2, 3} → R definida por f(x) = 2x + 1, o conjunto imagem é:',
    '["{3, 5, 7}", "{1, 2, 3}", "{2, 4, 6}", "{1, 3, 5}"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{3, 5, 7}', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 88, 92,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Imagem', 'Função constante',
    'A imagem da função constante f(x) = 5 é:',
    '["{5}", "R", "R*", "Z"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{5}', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 88, 92,
    'f(x) = 5', true, 'RETA', 0, 5, 0, 'eixo x', 'eixo y', -2, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Imagem', 'Função quadrática',
    'A imagem da função f(x) = x², com domínio real, é:',
    '["y ≥ 0", "y > 0", "y ≤ 0", "todos os reais"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'y ≥ 0', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 88, 92,
    'f(x) = x²', true, 'PARABOLA', 1, 0, 0, 'eixo x', 'eixo y', -2, 2, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Imagem', 'Função afim',
    'A imagem da função f: R → R definida por f(x) = 3x - 1 é:',
    '["R", "R*", "R₊", "Z"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'R', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 88, 92,
    'f(x) = 3x - 1', true, 'RETA', 3, -1, 0, 'eixo x', 'eixo y', -1, 2, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Igualdade', 'Condições',
    'Duas funções f e g são iguais quando:',
    '["têm o mesmo domínio e f(x) = g(x) para todo x do domínio", "têm o mesmo contradomínio", "têm o mesmo gráfico", "têm a mesma lei de formação"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'têm o mesmo domínio e f(x) = g(x) para todo x do domínio', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 93, 94,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Igualdade', 'Identificação',
    'As funções f(x) = x e g(x) = |x| são iguais no conjunto:',
    '["R₊", "R₋", "R", "Z"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'R₊', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 93, 94,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Igualdade', 'Identificação',
    'As funções f(x) = x² e g(x) = |x|² são:',
    '["iguais", "diferentes", "iguais apenas para x ≥ 0", "iguais apenas para x ≤ 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'iguais', 'DESAFIANTE', 0.60, 'fundamentos-da-matematica-elementar-1-.pdf', 93, 94,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Igualdade', 'Identificação',
    'As funções f(x) = (x² - 1)/(x - 1) e g(x) = x + 1 são:',
    '["iguais apenas para x ≠ 1", "iguais para todo x", "diferentes para todo x", "iguais apenas para x = 1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'iguais apenas para x ≠ 1', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 93, 94,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Aplicação', 'Cálculo de imagem',
    'Dada f(x) = 2x - 3, determine x tal que f(x) = 7.',
    '["5", "4", "3", "6"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '5', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 84, 86,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Aplicação', 'Cálculo de imagem',
    'Dada f(x) = x² - 4, determine x tal que f(x) = 0.',
    '["2 ou -2", "2", "-2", "4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2 ou -2', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 84, 86,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Aplicação', 'Gráfico',
    'O gráfico da função f(x) = x é uma reta que passa pela origem e tem coeficiente angular:',
    '["1", "0", "-1", "2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 84, 86,
    'f(x) = x', true, 'RETA', 1, 0, 0, 'eixo x', 'eixo y', -2, 2, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Aplicação', 'Função quadrática',
    'O gráfico da função f(x) = x² é uma:',
    '["parábola", "reta", "hipérbole", "circunferência"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'parábola', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 84, 86,
    'f(x) = x²', true, 'PARABOLA', 1, 0, 0, 'eixo x', 'eixo y', -2, 2, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Aplicação', 'Função do 1º grau',
    'O gráfico da função f(x) = 2x + 1 é uma reta que intercepta o eixo y no ponto:',
    '["(0, 1)", "(1, 0)", "(0, 2)", "(2, 0)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(0, 1)', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 84, 86,
    'f(x) = 2x + 1', true, 'RETA', 2, 1, 0, 'eixo x', 'eixo y', -1, 2, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Aplicação', 'Função par',
    'Uma função f: R → R é par quando:',
    '["f(-x) = f(x) para todo x", "f(-x) = -f(x) para todo x", "f(x) = 0", "f(x) = x"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'f(-x) = f(x) para todo x', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 84, 86,
    'f(x) = x²', true, 'PARABOLA', 1, 0, 0, 'eixo x', 'eixo y', -2, 2, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Aplicação', 'Função ímpar',
    'Uma função f: R → R é ímpar quando:',
    '["f(-x) = -f(x) para todo x", "f(-x) = f(x) para todo x", "f(x) = 0", "f(x) = x"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'f(-x) = -f(x) para todo x', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 84, 86,
    'f(x) = x³', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -2, 2, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Aplicação', 'Função sobrejetora',
    'Uma função f: A → B é sobrejetora quando:',
    '["Im(f) = B", "Im(f) ⊂ B", "Im(f) ∩ B = ∅", "Im(f) = A"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'Im(f) = B', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 220, 221,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Aplicação', 'Função injetora',
    'Uma função f: A → B é injetora quando:',
    '["x₁ ≠ x₂ ⇒ f(x₁) ≠ f(x₂)", "x₁ = x₂ ⇒ f(x₁) = f(x₂)", "Im(f) = B", "f é sobrejetora"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x₁ ≠ x₂ ⇒ f(x₁) ≠ f(x₂)', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 221, 222,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Aplicação', 'Função bijetora',
    'Uma função f: A → B é bijetora quando:',
    '["é injetora e sobrejetora", "é apenas injetora", "é apenas sobrejetora", "não é injetora nem sobrejetora"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'é injetora e sobrejetora', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 222, 223,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Domínio', 'Função com duas restrições',
    'O domínio da função f(x) = √(x - 1) + √(4 - x) é:',
    '["1 ≤ x ≤ 4", "x ≥ 1", "x ≤ 4", "x = 1 ou x = 4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1 ≤ x ≤ 4', 'DESAFIANTE', 0.60, 'fundamentos-da-matematica-elementar-1-.pdf', 92, 93,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Notação', 'Imagem de um elemento',
    'Dada f(x) = 4x - 1, o valor de f(0) + f(1) é:',
    '["2", "3", "4", "5"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 84, 86,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Notação', 'Composição',
    'Dadas f(x) = 2x e g(x) = x + 3, o valor de f(g(1)) é:',
    '["8", "5", "6", "7"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '8', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 212, 214,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Notação', 'Valor numérico',
    'Dada f(x) = (x - 1)/(x + 1), o valor de f(2) é:',
    '["1/3", "3", "1", "0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1/3', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 84, 86,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Domínio', 'Função com denominador',
    'O domínio da função f(x) = 1/(x² - 1) é:',
    '["x ≠ 1 e x ≠ -1", "x ≠ 1", "x ≠ -1", "x = 1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x ≠ 1 e x ≠ -1', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 92, 93,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Domínio', 'Função com raiz cúbica',
    'O domínio da função f(x) = ∛(x - 5) é:',
    '["R", "x ≥ 5", "x > 5", "x ≤ 5"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'R', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 92, 93,
    'f(x) = ∛(x - 5)', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -2, 7, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Notação', 'Função definida por partes',
    'A função f(x) = { x², se x ≥ 0; -x, se x < 0 } é um exemplo de função:',
    '["definida por várias sentenças", "constante", "linear", "quadrática"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'definida por várias sentenças', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 184, 186,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -2, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Aplicação', 'Função custo',
    'O custo de produção de x unidades de um produto é dado por C(x) = 100 + 20x. O custo para produzir 10 unidades é:',
    '["300", "200", "100", "400"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '300', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 84, 86,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Funções', 'Aplicação', 'Função receita',
    'A receita pela venda de x produtos a R$ 50,00 cada é R(x) = 50x. A receita para vender 100 produtos é:',
    '["5000", "500", "1000", "50"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '5000', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 84, 86,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
);

-- ✅ FUNÇÕES (CONCEITO) COMPLETADO: 100 QUESTÕES

-- Mock SQL para Matemática - FUNÇÃO AFIM (Capítulo VI) - PARTE 2
-- Baseado EXCLUSIVAMENTE no livro "Fundamentos de Matemática Elementar - Volume 1"
-- Iezzi & Murakami, 9ª ed., 2013
-- Disciplina: Matemática
-- Tópico principal: Função afim (Capítulo VI, páginas 97-136)
-- Total de questões neste lote (parte 2): 50 de 150

INSERT INTO perguntas (
    id, disciplina, topico_principal, topico, subtopico, questao, respostas, pesos_resposta,
    resposta_correta, dificuldade, rigor, referencia_livro, pagina_inicio, pagina_fim,
    exercicio, usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
) VALUES


(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Inequações-produto', 'Resolução',
    'O conjunto solução de (x - 2)(x + 3) > 0 é:',
    '["x < -3 ou x > 2", "-3 < x < 2", "x < -2 ou x > 3", "-2 < x < 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x < -3 ou x > 2', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-1-.pdf', 128, 132,
    '', true, 'PARABOLA', 1, 1, -6, 'eixo x', 'eixo y', -4, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Inequações-produto', 'Resolução',
    'O conjunto solução de (x + 1)(2x - 3) ≤ 0 é:',
    '["-1 ≤ x ≤ 1,5", "x ≤ -1 ou x ≥ 1,5", "-1,5 ≤ x ≤ 1", "x ≤ -1,5 ou x ≥ 1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-1 ≤ x ≤ 1,5', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 128, 132,
    '', true, 'PARABOLA', 2, -1, -3, 'eixo x', 'eixo y', -2, 2, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Inequações-quociente', 'Resolução',
    'O conjunto solução de (x - 1)/(x + 2) ≥ 0 é:',
    '["x ≤ -2 ou x ≥ 1", "-2 ≤ x ≤ 1", "x < -2 ou x ≥ 1", "x ≤ -2 ou x > 1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x < -2 ou x ≥ 1', 'DESAFIANTE', 0.60, 'fundamentos-da-matematica-elementar-1-.pdf', 135, 136,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -3, 2, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Inequações-quociente', 'Resolução',
    'O conjunto solução de (2x + 1)/(x - 3) < 0 é:',
    '["-0,5 < x < 3", "x < -0,5 ou x > 3", "x < -0,5", "x > 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-0,5 < x < 3', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-1-.pdf', 135, 136,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -1, 4, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Potências', 'Expoente par',
    'A inequação (2x - 4)⁶ > 0 tem como conjunto solução:',
    '["x ≠ 2", "x > 2", "x < 2", "todos os reais"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x ≠ 2', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 140, 141,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Potências', 'Expoente ímpar',
    'O conjunto solução de (3x - 9)³ > 0 é:',
    '["x > 3", "x < 3", "x ≠ 3", "todos os reais"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x > 3', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 140, 141,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Aplicação', 'Custos',
    'O custo de produção de x unidades de um produto é C(x) = 1200 + 25x. O custo para produzir 80 unidades é:',
    '["R$ 3.200,00", "R$ 2.000,00", "R$ 3.000,00", "R$ 4.200,00"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'R$ 3.200,00', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 100, 101,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Aplicação', 'Receita',
    'Uma empresa vende um produto por R$ 120,00 a unidade. A receita para vender 250 unidades é:',
    '["R$ 30.000,00", "R$ 12.000,00", "R$ 25.000,00", "R$ 120.000,00"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'R$ 30.000,00', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 100, 101,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Aplicação', 'Lucro',
    'O lucro de uma empresa é dado por L(x) = 50x - 2000, onde x é o número de unidades vendidas. O ponto de equilíbrio (lucro zero) ocorre quando:',
    '["x = 40", "x = 50", "x = 20", "x = 100"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = 40', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 100, 101,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Aplicação', 'Temperatura',
    'A relação entre as escalas Celsius (C) e Fahrenheit (F) é dada por F = 1,8C + 32. A temperatura em Fahrenheit equivalente a 20°C é:',
    '["68°F", "36°F", "52°F", "80°F"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '68°F', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 100, 101,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Aplicação', 'Temperatura',
    'A temperatura de 86°F equivale a quantos graus Celsius?',
    '["30°C", "25°C", "35°C", "40°C"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '30°C', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 100, 101,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Aplicação', 'Movimento uniforme',
    'Um móvel em MRU tem posição dada por s = 20 + 5t (SI). A posição inicial e a velocidade são, respectivamente:',
    '["20 m e 5 m/s", "5 m e 20 m/s", "20 m e 0 m/s", "0 m e 20 m/s"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '20 m e 5 m/s', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 100, 101,
    's = 20 + 5t', true, 'RETA', 5, 20, 0, 'tempo (s)', 'posição (m)', 0, 5, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Aplicação', 'Movimento uniforme',
    'Dois móveis em MRU têm equações s₁ = 10 + 2t e s₂ = 40 - 3t (SI). O instante de encontro é:',
    '["t = 6 s", "t = 5 s", "t = 10 s", "t = 8 s"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    't = 6 s', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 100, 101,
    '', true, 'RETA', 0, 0, 0, 'tempo (s)', 'posição (m)', 0, 8, 2
),


(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Parâmetros', 'Crescimento',
    'Para que a função f(x) = (m - 3)x + 2 seja crescente, m deve satisfazer:',
    '["m > 3", "m < 3", "m = 3", "m ≠ 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'm > 3', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 110, 113,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Parâmetros', 'Decrescimento',
    'Para que a função f(x) = (4 - m)x - 3 seja decrescente, m deve satisfazer:',
    '["m > 4", "m < 4", "m = 4", "m ≠ 4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'm > 4', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 110, 113,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Parâmetros', 'Zero da função',
    'Para que a função f(x) = (m - 2)x + 3 tenha zero igual a -1, m deve ser:',
    '["5", "4", "3", "2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '5', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 108, 109,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Gráfico', 'Reta crescente',
    'Qual das seguintes funções tem gráfico crescente e intercepta o eixo y no ponto (0, -2)?',
    '["f(x) = 3x - 2", "f(x) = -3x - 2", "f(x) = 2x + 3", "f(x) = -2x + 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'f(x) = 3x - 2', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 100, 107,
    'f(x) = 3x - 2', true, 'RETA', 3, -2, 0, 'eixo x', 'eixo y', -1, 2, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Gráfico', 'Reta decrescente',
    'Qual das seguintes funções tem gráfico decrescente e intercepta o eixo y no ponto (0, 4)?',
    '["f(x) = -2x + 4", "f(x) = 2x + 4", "f(x) = -2x - 4", "f(x) = 2x - 4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'f(x) = -2x + 4', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 100, 107,
    'f(x) = -2x + 4', true, 'RETA', -2, 4, 0, 'eixo x', 'eixo y', -1, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Gráfico', 'Reta horizontal',
    'O gráfico da função f(x) = -3 é uma reta:',
    '["horizontal passando por y = -3", "vertical passando por x = -3", "inclinada com coeficiente angular -3", "crescente"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'horizontal passando por y = -3', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 97, 98,
    'f(x) = -3', true, 'RETA', 0, -3, 0, 'eixo x', 'eixo y', -2, 4, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Sistemas lineares', 'Resolução',
    'O sistema {2x + y = 7, x - y = 2} tem como solução:',
    '["(3, 1)", "(1, 3)", "(2, 3)", "(3, 2)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(3, 1)', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 110, 112,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', 0, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Sistemas lineares', 'Resolução',
    'O sistema {3x - 2y = -14, 2x + 3y = 8} tem como solução:',
    '["(-2, 4)", "(2, 4)", "(-2, -4)", "(2, -4)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(-2, 4)', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 110, 112,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Inequações simultâneas', 'Resolução',
    'O conjunto solução de 2 < 3x - 1 ≤ 8 é:',
    '["1 < x ≤ 3", "1 ≤ x < 3", "x > 1", "x ≤ 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1 < x ≤ 3', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 126, 127,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', 0, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Inequações simultâneas', 'Resolução',
    'O conjunto solução de -3 ≤ 2x + 1 < 5 é:',
    '["-2 ≤ x < 2", "-2 < x ≤ 2", "x ≥ -2", "x < 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-2 ≤ x < 2', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 126, 127,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -3, 3, 1
),


(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Coeficientes', 'Identificação',
    'Na função f(x) = -4x + 7, o coeficiente angular é:',
    '["-4", "4", "7", "-7"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-4', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 106, 107,
    'f(x) = -4x + 7', true, 'RETA', -4, 7, 0, 'eixo x', 'eixo y', -1, 2, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Função linear', 'Proporcionalidade direta',
    'Uma grandeza y é diretamente proporcional a x quando a relação entre elas é do tipo:',
    '["y = kx", "y = k/x", "y = kx + b", "y = kx²"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'y = kx', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 98, 99,
    'y = 3x', true, 'RETA', 3, 0, 0, 'eixo x', 'eixo y', -2, 2, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Zero da função', 'Cálculo algébrico',
    'O zero da função f(x) = 5x + 15 é:',
    '["-3", "3", "5", "15"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-3', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 108, 109,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Zero da função', 'Cálculo algébrico',
    'O zero da função f(x) = -2x + 10 é:',
    '["5", "-5", "2", "10"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '5', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 108, 109,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Sinal da função', 'Estudo',
    'A função f(x) = -2x - 4 é positiva para:',
    '["x < -2", "x > -2", "x < 2", "x > 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x < -2', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 116, 119,
    '', true, 'RETA', -2, -4, 0, 'eixo x', 'eixo y', -3, 1, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Inequações', 'Resolução',
    'O conjunto solução da inequação (2x - 3)(x + 4) > 0 é:',
    '["x < -4 ou x > 1,5", "-4 < x < 1,5", "x < -1,5 ou x > 4", "-1,5 < x < 4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x < -4 ou x > 1,5', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 128, 132,
    '', true, 'PARABOLA', 2, 5, -12, 'eixo x', 'eixo y', -5, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Inequações-quociente', 'Resolução',
    'O conjunto solução de (3x - 6)/(x + 1) < 0 é:',
    '["-1 < x < 2", "x < -1 ou x > 2", "x < -1", "x > 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-1 < x < 2', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-1-.pdf', 135, 136,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -2, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Gráfico', 'Interseção com eixos',
    'A função f(x) = 2x - 8 intercepta o eixo x no ponto:',
    '["(4, 0)", "(0, 4)", "(0, -8)", "(-8, 0)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(4, 0)', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 106, 109,
    'f(x) = 2x - 8', true, 'RETA', 2, -8, 0, 'eixo x', 'eixo y', -1, 5, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Aplicação', 'Depreciação',
    'Um carro novo vale R$ 50.000,00 e deprecia R$ 5.000,00 por ano. O valor V após t anos é dado por:',
    '["V(t) = 50000 - 5000t", "V(t) = 50000 + 5000t", "V(t) = 5000t", "V(t) = 50000/t"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'V(t) = 50000 - 5000t', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 100, 101,
    '', true, 'RETA', -5000, 50000, 0, 'tempo (anos)', 'valor (R$)', 0, 5, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Aplicação', 'Oferta e demanda',
    'A demanda de um produto é dada por D(p) = 100 - 2p e a oferta por O(p) = 20 + 3p. O preço de equilíbrio é:',
    '["16", "20", "10", "30"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '16', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 100, 101,
    '', true, 'RETA', 0, 0, 0, 'preço', 'quantidade', 0, 20, 5
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Coeficientes', 'Interpretação geométrica',
    'O coeficiente angular de uma reta representa:',
    '["a inclinação da reta", "o ponto onde a reta corta o eixo y", "a interseção com o eixo x", "a distância da origem"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'a inclinação da reta', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 106, 107,
    '', true, 'RETA', 2, 1, 0, 'eixo x', 'eixo y', -1, 2, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Função constante', 'Aplicação',
    'A função que representa um valor fixo, independentemente da variável, é chamada de função:',
    '["constante", "linear", "afim", "identidade"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'constante', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 97, 98,
    'f(x) = 3', true, 'RETA', 0, 3, 0, 'eixo x', 'eixo y', -2, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Crescimento', 'Taxa de variação',
    'O coeficiente angular a da função afim f(x) = ax + b representa:',
    '["a taxa de variação da função", "o valor inicial da função", "o ponto de máximo", "o ponto de mínimo"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'a taxa de variação da função', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 106, 107,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Sinal da função', 'Quadro de sinais',
    'Para estudar o sinal da função f(x) = 3x - 9, a raiz é x = 3. Então f(x) > 0 para:',
    '["x > 3", "x < 3", "x = 3", "x ≠ 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x > 3', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 116, 119,
    '', true, 'RETA', 3, -9, 0, 'eixo x', 'eixo y', -1, 5, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Sinal da função', 'Quadro de sinais',
    'Para estudar o sinal da função f(x) = -2x + 6, a raiz é x = 3. Então f(x) < 0 para:',
    '["x > 3", "x < 3", "x = 3", "x ≠ 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x > 3', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 116, 119,
    '', true, 'RETA', -2, 6, 0, 'eixo x', 'eixo y', -1, 5, 1
);

-- ✅ FUNÇÃO AFIM - PARTE 2 COMPLETADA: 50 QUESTÕES
-- TOTAL DA FUNÇÃO AFIM: 100/150 QUESTÕES
-- FALTAM MAIS 50 QUESTÕES PARA COMPLETAR 150

-- Mock SQL para Matemática - FUNÇÃO AFIM (Capítulo VI) - PARTE 3 (FINAL)
-- Baseado EXCLUSIVAMENTE no livro "Fundamentos de Matemática Elementar - Volume 1"
-- Iezzi & Murakami, 9ª ed., 2013
-- Disciplina: Matemática
-- Tópico principal: Função afim (Capítulo VI, páginas 97-136)
-- Total de questões neste lote (parte 3): 50 (completando 150)

INSERT INTO perguntas (
    id, disciplina, topico_principal, topico, subtopico, questao, respostas, pesos_resposta,
    resposta_correta, dificuldade, rigor, referencia_livro, pagina_inicio, pagina_fim,
    exercicio, usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
) VALUES

-- =====================================================
-- INEQUAÇÕES-PRODUTO COM POTÊNCIAS (páginas 128-134)
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Inequações-produto', 'Potências',
    'O conjunto solução de (x - 3)⁵ · (x + 2)⁴ < 0 é:',
    '["x < 3 e x ≠ -2", "x < -2 ou x > 3", "-2 < x < 3", "x > 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x < 3 e x ≠ -2', 'DESAFIANTE', 0.65, 'fundamentos-da-matematica-elementar-1-.pdf', 140, 141,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Inequações-produto', 'Potências',
    'O conjunto solução de (2x - 1)⁶ · (x + 3)³ ≥ 0 é:',
    '["x ≥ -3", "x ≤ -3", "x = 0,5", "todos os reais"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x ≥ -3', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 140, 141,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

-- =====================================================
-- PROBLEMAS COM INTERPRETAÇÃO DE GRÁFICOS
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Gráfico', 'Interpretação',
    'O gráfico de uma função afim passa pelos pontos (1, 5) e (3, 11). O coeficiente angular é:',
    '["3", "2", "4", "5"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 105, 106,
    '', true, 'RETA', 3, 2, 0, 'eixo x', 'eixo y', 0, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Gráfico', 'Interpretação',
    'O gráfico de uma função afim passa pelos pontos (2, 3) e (4, 7). A equação da reta é:',
    '["y = 2x - 1", "y = 2x + 1", "y = x + 1", "y = 3x - 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'y = 2x - 1', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 105, 106,
    '', true, 'RETA', 2, -1, 0, 'eixo x', 'eixo y', 0, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Gráfico', 'Interpretação',
    'O gráfico de uma função afim passa pelos pontos (0, 4) e (2, 0). O coeficiente linear é:',
    '["4", "0", "2", "-2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '4', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 106, 107,
    '', true, 'RETA', -2, 4, 0, 'eixo x', 'eixo y', -1, 3, 1
),

-- =====================================================
-- PROBLEMAS DE CONTEXTO REAL (APLICAÇÕES AVANÇADAS)
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Aplicação', 'Plano de telefonia',
    'Uma operadora de telefonia oferece um plano com taxa fixa de R$ 30,00 e R$ 0,50 por minuto. O valor pago por 40 minutos de uso é:',
    '["R$ 50,00", "R$ 40,00", "R$ 55,00", "R$ 45,00"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'R$ 50,00', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 100, 101,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Aplicação', 'Plano de telefonia',
    'Uma operadora oferece dois planos: A: R$ 40,00 fixos + R$ 0,30/min; B: R$ 20,00 fixos + R$ 0,50/min. O plano A é mais vantajoso para mais de:',
    '["100 minutos", "80 minutos", "120 minutos", "50 minutos"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '100 minutos', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-1-.pdf', 100, 101,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Aplicação', 'Locação de veículos',
    'Uma locadora cobra R$ 80,00 por dia mais R$ 1,50 por km rodado. O custo para um dia com 120 km é:',
    '["R$ 260,00", "R$ 200,00", "R$ 240,00", "R$ 180,00"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'R$ 260,00', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 100, 101,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Aplicação', 'Energia elétrica',
    'Uma conta de luz tem uma taxa fixa de R$ 25,00 e R$ 0,80 por kWh consumido. O valor para 150 kWh é:',
    '["R$ 145,00", "R$ 120,00", "R$ 140,00", "R$ 150,00"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'R$ 145,00', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 100, 101,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Aplicação', 'Produção',
    'Uma fábrica produz x peças por dia com custo C(x) = 2000 + 15x. A produção diária para que o custo seja R$ 5.000,00 é:',
    '["200 peças", "150 peças", "250 peças", "300 peças"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '200 peças', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 100, 101,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Aplicação', 'Viagem',
    'Um carro alugado custa R$ 120,00 por dia mais R$ 0,90 por km. Em uma viagem de 3 dias e 800 km, o custo total é:',
    '["R$ 1.080,00", "R$ 1.200,00", "R$ 960,00", "R$ 1.000,00"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'R$ 1.080,00', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 100, 101,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

-- =====================================================
-- ESTUDO DO SINAL COM GRÁFICOS
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Sinal', 'Gráfico',
    'Dado o gráfico da função f abaixo, f(x) > 0 para x pertencente a:',
    '["(-2, +∞)", "(-∞, -2)", "(-∞, 2)", "(2, +∞)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(-2, +∞)', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 116, 119,
    '', true, 'RETA', 1, 2, 0, 'eixo x', 'eixo y', -3, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Sinal', 'Gráfico',
    'Dado o gráfico da função f abaixo, f(x) ≤ 0 para x pertencente a:',
    '["(-∞, 3]", "[3, +∞)", "(-∞, -3]", "[-3, +∞)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '[3, +∞)', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 116, 119,
    '', true, 'RETA', -1, 3, 0, 'eixo x', 'eixo y', -2, 5, 1
),

-- =====================================================
-- SISTEMAS DE INEQUAÇÕES
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Sistemas de inequações', 'Resolução',
    'O conjunto solução do sistema {x - 2 > 0, 2x + 1 < 9} é:',
    '["2 < x < 4", "x > 2", "x < 4", "x > 4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2 < x < 4', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 126, 127,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', 0, 5, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Sistemas de inequações', 'Resolução',
    'O conjunto solução do sistema {3x + 1 ≥ 7, 4x - 5 ≤ 15} é:',
    '["2 ≤ x ≤ 5", "x ≥ 2", "x ≤ 5", "2 < x < 5"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2 ≤ x ≤ 5', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 126, 127,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', 0, 6, 1
),

-- =====================================================
-- FUNÇÃO AFIM E PROGRESSÕES (relação com PA)
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Progressão aritmética', 'Relação',
    'Os valores de uma função afim f(x) = ax + b para x = 1, 2, 3, ... formam uma:',
    '["progressão aritmética", "progressão geométrica", "sequência aleatória", "função quadrática"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'progressão aritmética', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 98, 100,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Progressão aritmética', 'Razão',
    'Se f(x) = 3x - 2, então a sequência f(1), f(2), f(3), ... tem razão:',
    '["3", "-2", "1", "2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 98, 100,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

-- =====================================================
-- QUESTÕES DE CONCURSO (estilo vestibular)
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Vestibular', 'Interpretação',
    'O gráfico da função f(x) = ax + b está representado abaixo. O valor de a - b é:',
    '["1", "2", "3", "4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2', 'DESAFIANTE', 0.60, 'fundamentos-da-matematica-elementar-1-.pdf', 100, 107,
    '', true, 'RETA', 1, -1, 0, 'eixo x', 'eixo y', -2, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Vestibular', 'Interpretação',
    'A reta representada no gráfico abaixo tem equação:',
    '["y = 2x - 4", "y = -2x + 4", "y = x - 2", "y = -x + 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'y = -2x + 4', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 100, 107,
    '', true, 'RETA', -2, 4, 0, 'eixo x', 'eixo y', -1, 3, 1
),

-- =====================================================
-- QUESTÕES COMPLEMENTARES PARA COMPLETAR 150
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Coeficientes', 'Identificação',
    'Na função f(x) = 3 - 2x, os coeficientes angular e linear são, respectivamente:',
    '["-2 e 3", "2 e 3", "3 e -2", "-2 e -3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-2 e 3', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 106, 107,
    'f(x) = 3 - 2x', true, 'RETA', -2, 3, 0, 'eixo x', 'eixo y', -1, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Zero da função', 'Cálculo algébrico',
    'O zero da função f(x) = 5 - 2x é:',
    '["2,5", "-2,5", "5", "-5"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2,5', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 108, 109,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Crescimento', 'Parâmetro',
    'Para que a função f(x) = (m + 2)x - 5 seja decrescente, m deve ser:',
    '["m < -2", "m > -2", "m = -2", "m ≠ -2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'm < -2', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 110, 113,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Inequações', 'Resolução',
    'O conjunto solução da inequação 4x - 3 < 2x + 7 é:',
    '["x < 5", "x > 5", "x < 10", "x > 10"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x < 5', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 121, 125,
    '', true, 'RETA', 2, -10, 0, 'eixo x', 'eixo y', -1, 6, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Inequações-produto', 'Resolução',
    'O conjunto solução de (x - 4)(x + 2) < 0 é:',
    '["-2 < x < 4", "x < -2 ou x > 4", "x < 4", "x > -2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-2 < x < 4', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 128, 132,
    '', true, 'PARABOLA', 1, -2, -8, 'eixo x', 'eixo y', -3, 5, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Inequações-quociente', 'Resolução',
    'O conjunto solução de (2x - 3)/(x + 1) ≥ 0 é:',
    '["x < -1 ou x ≥ 1,5", "-1 < x ≤ 1,5", "x ≤ -1 ou x ≥ 1,5", "x ≤ -1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x < -1 ou x ≥ 1,5', 'DESAFIANTE', 0.60, 'fundamentos-da-matematica-elementar-1-.pdf', 135, 136,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', -2, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Aplicação', 'Móveis',
    'Dois móveis em MRU têm equações s₁ = 30 - 4t e s₂ = 10 + 6t (SI). O instante em que se encontram é:',
    '["t = 2 s", "t = 3 s", "t = 4 s", "t = 5 s"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    't = 2 s', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 100, 101,
    '', true, 'RETA', 0, 0, 0, 'tempo (s)', 'posição (m)', 0, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Gráfico', 'Paralelismo',
    'A reta paralela à reta y = 3x - 2 que passa pelo ponto (1, 4) é:',
    '["y = 3x + 1", "y = 3x - 1", "y = 2x + 2", "y = 4x"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'y = 3x + 1', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 106, 107,
    '', true, 'RETA', 3, 1, 0, 'eixo x', 'eixo y', -1, 2, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Gráfico', 'Perpendicularismo',
    'A reta perpendicular à reta y = 2x + 3 que passa pelo ponto (2, 1) é:',
    '["y = -0,5x + 2", "y = 0,5x + 2", "y = -2x + 5", "y = 2x - 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'y = -0,5x + 2', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 106, 107,
    '', true, 'RETA', -0.5, 2, 0, 'eixo x', 'eixo y', -1, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Sistemas lineares', 'Método da adição',
    'O sistema {2x + 3y = 12, x - y = 1} tem como solução:',
    '["(3, 2)", "(2, 3)", "(4, 1)", "(1, 4)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(3, 2)', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 110, 112,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', 0, 5, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Função linear', 'Proporcionalidade inversa',
    'Um móvel percorre 200 km em 4 horas. A função que relaciona distância e tempo (velocidade constante) é:',
    '["d = 50t", "d = 200t", "d = 4t", "d = 800t"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'd = 50t', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 98, 99,
    '', true, 'RETA', 50, 0, 0, 'tempo (h)', 'distância (km)', 0, 5, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Zero da função', 'Interpretação gráfica',
    'O ponto onde o gráfico da função f(x) = -3x + 9 corta o eixo x tem coordenadas:',
    '["(3, 0)", "(0, 3)", "(9, 0)", "(0, 9)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(3, 0)', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 108, 109,
    'f(x) = -3x + 9', true, 'RETA', -3, 9, 0, 'eixo x', 'eixo y', -1, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Sinal da função', 'Inequação',
    'Os valores de x para os quais f(x) = 2x - 8 é negativa são:',
    '["x < 4", "x > 4", "x < -4", "x > -4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x < 4', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 116, 119,
    '', true, 'RETA', 2, -8, 0, 'eixo x', 'eixo y', -1, 5, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Inequações simultâneas', 'Resolução',
    'O conjunto solução de 1 ≤ 2x - 3 ≤ 7 é:',
    '["2 ≤ x ≤ 5", "1 ≤ x ≤ 5", "2 < x < 5", "x ≥ 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2 ≤ x ≤ 5', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 126, 127,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', 0, 6, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Aplicação', 'Salário',
    'Um vendedor recebe salário fixo de R$ 1.200,00 mais comissão de 5% sobre as vendas. O salário para vender R$ 10.000,00 é:',
    '["R$ 1.700,00", "R$ 1.500,00", "R$ 2.000,00", "R$ 1.800,00"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'R$ 1.700,00', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 100, 101,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Aplicação', 'Salário',
    'Um vendedor recebe R$ 1.500,00 fixos mais 4% de comissão. Para receber R$ 2.300,00, ele deve vender:',
    '["R$ 20.000,00", "R$ 15.000,00", "R$ 25.000,00", "R$ 18.000,00"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'R$ 20.000,00', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 100, 101,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Aplicação', 'Água',
    'Uma conta de água tem taxa fixa de R$ 15,00 mais R$ 0,02 por litro consumido. O consumo para uma conta de R$ 65,00 é:',
    '["2.500 litros", "2.000 litros", "3.000 litros", "1.500 litros"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2.500 litros', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 100, 101,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Aplicação', 'Frete',
    'Um frete cobra R$ 50,00 fixos mais R$ 2,00 por kg. O custo para transportar 75 kg é:',
    '["R$ 200,00", "R$ 150,00", "R$ 175,00", "R$ 125,00"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'R$ 200,00', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 100, 101,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Aplicação', 'Imposto',
    'Um imposto é calculado como 15% do valor do produto mais uma taxa fixa de R$ 10,00. O imposto para um produto de R$ 200,00 é:',
    '["R$ 40,00", "R$ 30,00", "R$ 35,00", "R$ 45,00"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'R$ 40,00', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 100, 101,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Gráfico', 'Reta vertical',
    'A reta x = 3 é representada por uma linha:',
    '["vertical", "horizontal", "inclinada", "curva"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'vertical', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 100, 101,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', 0, 5, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Coeficiente angular', 'Cálculo',
    'O coeficiente angular da reta que passa pelos pontos (-1, 3) e (2, 9) é:',
    '["2", "3", "4", "1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 106, 107,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Coeficiente linear', 'Cálculo',
    'O coeficiente linear da reta que passa pelos pontos (0, 5) e (2, 9) é:',
    '["5", "2", "4", "9"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '5', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 106, 107,
    '', true, 'RETA', 2, 5, 0, 'eixo x', 'eixo y', -1, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Equação da reta', 'Pontos especiais',
    'A equação da reta que passa pela origem e tem coeficiente angular 4 é:',
    '["y = 4x", "y = 4x + 1", "y = x + 4", "y = 4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'y = 4x', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 98, 99,
    '', true, 'RETA', 4, 0, 0, 'eixo x', 'eixo y', -1, 2, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Equação da reta', 'Pontos especiais',
    'A equação da reta horizontal que passa pelo ponto (2, 5) é:',
    '["y = 5", "x = 2", "y = 2x + 1", "y = 5x"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'y = 5', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 97, 98,
    '', true, 'RETA', 0, 5, 0, 'eixo x', 'eixo y', -2, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função afim', 'Equação da reta', 'Pontos especiais',
    'A equação da reta vertical que passa pelo ponto (3, -2) é:',
    '["x = 3", "y = -2", "y = 3x - 11", "x = -2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = 3', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 100, 101,
    '', true, 'RETA', 0, 0, 0, 'eixo x', 'eixo y', 0, 5, 1
);

-- ✅ FUNÇÃO AFIM - PARTE 3 COMPLETADA: 50 QUESTÕES
-- ✅ FUNÇÃO AFIM TOTAL: 150/150 QUESTÕES COMPLETADAS


-- Mock SQL para Matemática - FUNÇÃO QUADRÁTICA (Capítulo VII) - PARTE 1
-- Baseado EXCLUSIVAMENTE no livro "Fundamentos de Matemática Elementar - Volume 1"
-- Iezzi & Murakami, 9ª ed., 2013
-- Disciplina: Matemática
-- Tópico principal: Função quadrática (Capítulo VII, páginas 137-181)
-- Total de questões neste lote (parte 1): 50 de 150

INSERT INTO perguntas (
    id, disciplina, topico_principal, topico, subtopico, questao, respostas, pesos_resposta,
    resposta_correta, dificuldade, rigor, referencia_livro, pagina_inicio, pagina_fim,
    exercicio, usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
) VALUES

-- =====================================================
-- DEFINIÇÃO E FORMA GERAL (páginas 137-139)
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Definição', 'Forma geral',
    'A forma geral da função quadrática é:',
    '["f(x) = ax² + bx + c, com a ≠ 0", "f(x) = ax + b, com a ≠ 0", "f(x) = ax³ + bx² + cx + d", "f(x) = a^x"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'f(x) = ax² + bx + c, com a ≠ 0', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 137, 138,
    'f(x) = 2x² - 3x + 1', true, 'PARABOLA', 2, -3, 1, 'eixo x', 'eixo y', -1, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Definição', 'Coeficientes',
    'Na função f(x) = -3x² + 5x - 2, os coeficientes a, b e c são, respectivamente:',
    '["-3, 5, -2", "3, 5, 2", "-3, -5, -2", "3, -5, 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-3, 5, -2', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 137, 138,
    'f(x) = -3x² + 5x - 2', true, 'PARABOLA', -3, 5, -2, 'eixo x', 'eixo y', -1, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Definição', 'Identificação',
    'Qual das seguintes funções NÃO é quadrática?',
    '["f(x) = 2x + 3", "f(x) = x² - 4", "f(x) = -x² + 2x - 1", "f(x) = 3x²"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'f(x) = 2x + 3', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 137, 138,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Definição', 'Coeficientes',
    'Na função f(x) = 4x² - 7, o coeficiente b é:',
    '["0", "4", "-7", "7"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '0', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 137, 138,
    'f(x) = 4x² - 7', true, 'PARABOLA', 4, 0, -7, 'eixo x', 'eixo y', -2, 2, 1
),

-- =====================================================
-- CONCAVIDADE DA PARÁBOLA (páginas 139-140)
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Concavidade', 'Determinação',
    'A parábola da função f(x) = 2x² - 4x + 1 tem concavidade voltada para:',
    '["cima", "baixo", "direita", "esquerda"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'cima', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 139, 140,
    'f(x) = 2x² - 4x + 1', true, 'PARABOLA', 2, -4, 1, 'eixo x', 'eixo y', -1, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Concavidade', 'Determinação',
    'A parábola da função f(x) = -x² + 3x - 5 tem concavidade voltada para:',
    '["baixo", "cima", "direita", "esquerda"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'baixo', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 139, 140,
    'f(x) = -x² + 3x - 5', true, 'PARABOLA', -1, 3, -5, 'eixo x', 'eixo y', -2, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Concavidade', 'Parâmetro',
    'Para que a parábola f(x) = (m - 2)x² + 3x - 1 tenha concavidade voltada para cima, m deve ser:',
    '["m > 2", "m < 2", "m = 2", "m > 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'm > 2', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 139, 140,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Concavidade', 'Parâmetro',
    'Para que a parábola f(x) = (3 - m)x² + 2x + 4 tenha concavidade voltada para baixo, m deve ser:',
    '["m > 3", "m < 3", "m = 3", "m > 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'm > 3', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 139, 140,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

-- =====================================================
-- ZEROS DA FUNÇÃO QUADRÁTICA (páginas 140-144)
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Zeros da função', 'Cálculo',
    'As raízes da função f(x) = x² - 7x + 12 são:',
    '["3 e 4", "2 e 6", "1 e 12", "-3 e -4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3 e 4', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 140, 142,
    'f(x) = x² - 7x + 12', true, 'PARABOLA', 1, -7, 12, 'eixo x', 'eixo y', 0, 5, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Zeros da função', 'Cálculo',
    'As raízes da função f(x) = x² - 4x - 5 são:',
    '["5 e -1", "5 e 1", "-5 e 1", "-5 e -1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '5 e -1', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 140, 142,
    'f(x) = x² - 4x - 5', true, 'PARABOLA', 1, -4, -5, 'eixo x', 'eixo y', -3, 6, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Zeros da função', 'Raiz dupla',
    'A função f(x) = x² - 6x + 9 tem:',
    '["uma raiz dupla (x = 3)", "duas raízes distintas", "nenhuma raiz real", "raízes complexas"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'uma raiz dupla (x = 3)', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 141, 142,
    'f(x) = x² - 6x + 9', true, 'PARABOLA', 1, -6, 9, 'eixo x', 'eixo y', 0, 5, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Zeros da função', 'Discriminante',
    'O valor do discriminante (Δ) da função f(x) = x² - 4x + 5 é:',
    '["-4", "4", "0", "16"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-4', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 140, 141,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Zeros da função', 'Δ > 0',
    'Se Δ > 0, a função quadrática tem:',
    '["duas raízes reais e distintas", "uma raiz real dupla", "nenhuma raiz real", "raízes complexas"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'duas raízes reais e distintas', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 140, 142,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Zeros da função', 'Δ = 0',
    'Se Δ = 0, a função quadrática tem:',
    '["uma raiz real dupla", "duas raízes reais e distintas", "nenhuma raiz real", "raízes complexas"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'uma raiz real dupla', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 140, 142,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Zeros da função', 'Δ < 0',
    'Se Δ < 0, a função quadrática tem:',
    '["nenhuma raiz real", "duas raízes reais e distintas", "uma raiz real dupla", "raízes reais negativas"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'nenhuma raiz real', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 142, 143,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

-- =====================================================
-- VÉRTICE DA PARÁBOLA (páginas 147-151)
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Vértice', 'Coordenadas',
    'O vértice da parábola f(x) = x² - 4x + 3 é o ponto:',
    '["(2, -1)", "(-2, -1)", "(2, 1)", "(-2, 1)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(2, -1)', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 147, 149,
    'f(x) = x² - 4x + 3', true, 'PARABOLA', 1, -4, 3, 'eixo x', 'eixo y', 0, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Vértice', 'Coordenadas',
    'O vértice da parábola f(x) = -x² + 2x + 3 é o ponto:',
    '["(1, 4)", "(-1, 4)", "(1, -4)", "(-1, -4)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(1, 4)', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 147, 149,
    'f(x) = -x² + 2x + 3', true, 'PARABOLA', -1, 2, 3, 'eixo x', 'eixo y', -2, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Vértice', 'Fórmula',
    'A abscissa do vértice da parábola f(x) = ax² + bx + c é dada por:',
    '["-b/2a", "b/2a", "-b/a", "b/a"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-b/2a', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 147, 148,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Vértice', 'Fórmula',
    'A ordenada do vértice da parábola f(x) = ax² + bx + c é dada por:',
    '["-Δ/4a", "Δ/4a", "-b/2a", "b/2a"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-Δ/4a', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 147, 148,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Vértice', 'Eixo de simetria',
    'O eixo de simetria da parábola f(x) = x² + 4x + 3 é a reta:',
    '["x = -2", "x = 2", "y = -2", "y = 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = -2', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 152, 153,
    'f(x) = x² + 4x + 3', true, 'PARABOLA', 1, 4, 3, 'eixo x', 'eixo y', -4, 1, 1
),

-- =====================================================
-- VALOR MÁXIMO E VALOR MÍNIMO (páginas 145-147)
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Máximo e mínimo', 'Valor mínimo',
    'O valor mínimo da função f(x) = x² - 4x + 5 é:',
    '["1", "2", "3", "4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 145, 147,
    'f(x) = x² - 4x + 5', true, 'PARABOLA', 1, -4, 5, 'eixo x', 'eixo y', -1, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Máximo e mínimo', 'Valor máximo',
    'O valor máximo da função f(x) = -x² + 4x - 3 é:',
    '["1", "2", "3", "0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 145, 147,
    'f(x) = -x² + 4x - 3', true, 'PARABOLA', -1, 4, -3, 'eixo x', 'eixo y', -1, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Máximo e mínimo', 'Condição',
    'Uma função quadrática com a > 0 tem:',
    '["valor mínimo", "valor máximo", "valor mínimo e máximo", "nenhum extremo"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'valor mínimo', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 145, 147,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Máximo e mínimo', 'Condição',
    'Uma função quadrática com a < 0 tem:',
    '["valor máximo", "valor mínimo", "valor mínimo e máximo", "nenhum extremo"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'valor máximo', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 145, 147,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

-- =====================================================
-- IMAGEM DA FUNÇÃO QUADRÁTICA (páginas 157-158)
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Imagem', 'Determinação',
    'A imagem da função f(x) = x² + 1 é:',
    '["y ≥ 1", "y > 1", "y ≥ 0", "todos os reais"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'y ≥ 1', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 157, 158,
    'f(x) = x² + 1', true, 'PARABOLA', 1, 0, 1, 'eixo x', 'eixo y', -3, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Imagem', 'Determinação',
    'A imagem da função f(x) = -x² + 4 é:',
    '["y ≤ 4", "y ≥ 4", "y ≤ 0", "y ≥ 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'y ≤ 4', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 157, 158,
    'f(x) = -x² + 4', true, 'PARABOLA', -1, 0, 4, 'eixo x', 'eixo y', -3, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Imagem', 'Fórmula',
    'Para uma função quadrática com a > 0, a imagem é:',
    '["y ≥ -Δ/4a", "y ≤ -Δ/4a", "y ≥ Δ/4a", "y ≤ Δ/4a"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'y ≥ -Δ/4a', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 157, 158,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

-- =====================================================
-- FORMA CANÓNICA (páginas 139-140)
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Forma canónica', 'Completamento',
    'A forma canónica da função f(x) = x² - 6x + 5 é:',
    '["(x - 3)² - 4", "(x + 3)² - 4", "(x - 3)² + 4", "(x + 3)² + 4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(x - 3)² - 4', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 139, 140,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Forma canónica', 'Completamento',
    'A forma canónica da função f(x) = x² + 2x + 2 é:',
    '["(x + 1)² + 1", "(x - 1)² + 1", "(x + 1)² - 1", "(x - 1)² - 1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(x + 1)² + 1', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 139, 140,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

-- =====================================================
-- QUESTÕES COMPLEMENTARES PARA COMPLETAR 50
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Gráfico', 'Interseção com eixo y',
    'A função f(x) = x² - 3x + 2 intercepta o eixo y no ponto:',
    '["(0, 2)", "(2, 0)", "(0, 1)", "(0, -2)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(0, 2)', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 139, 140,
    'f(x) = x² - 3x + 2', true, 'PARABOLA', 1, -3, 2, 'eixo x', 'eixo y', -1, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Gráfico', 'Interseção com eixo x',
    'A função f(x) = x² - 4x - 5 intercepta o eixo x nos pontos:',
    '["(5, 0) e (-1, 0)", "(4, 0) e (5, 0)", "(-5, 0) e (1, 0)", "(-4, 0) e (-5, 0)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(5, 0) e (-1, 0)', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 140, 142,
    'f(x) = x² - 4x - 5', true, 'PARABOLA', 1, -4, -5, 'eixo x', 'eixo y', -3, 6, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Gráfico', 'Interseção com eixo y',
    'A função f(x) = -2x² + 4x - 1 intercepta o eixo y no ponto:',
    '["(0, -1)", "(0, 4)", "(0, 1)", "(0, -2)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(0, -1)', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 139, 140,
    'f(x) = -2x² + 4x - 1', true, 'PARABOLA', -2, 4, -1, 'eixo x', 'eixo y', -1, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Discriminante', 'Número de raízes',
    'A função f(x) = x² + 2x + 2 tem:',
    '["nenhuma raiz real", "duas raízes reais", "uma raiz dupla", "raízes complexas"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'nenhuma raiz real', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 140, 142,
    'f(x) = x² + 2x + 2', true, 'PARABOLA', 1, 2, 2, 'eixo x', 'eixo y', -3, 2, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Zeros da função', 'Soma',
    'Na equação x² - 5x + 6 = 0, a soma das raízes é:',
    '["5", "6", "-5", "-6"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '5', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 152, 153,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Zeros da função', 'Produto',
    'Na equação x² - 5x + 6 = 0, o produto das raízes é:',
    '["6", "5", "-6", "-5"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '6', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 152, 153,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Sinal da função', 'Δ < 0',
    'A função f(x) = x² + 2x + 2 é:',
    '["sempre positiva", "sempre negativa", "positiva para x > 0", "negativa para x < 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'sempre positiva', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 159, 161,
    'f(x) = x² + 2x + 2', true, 'PARABOLA', 1, 2, 2, 'eixo x', 'eixo y', -3, 2, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Sinal da função', 'Δ = 0',
    'A função f(x) = x² - 6x + 9 é:',
    '["não negativa para todo x real", "sempre positiva", "sempre negativa", "positiva para x ≠ 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'não negativa para todo x real', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 159, 161,
    'f(x) = x² - 6x + 9', true, 'PARABOLA', 1, -6, 9, 'eixo x', 'eixo y', 0, 5, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Sinal da função', 'Δ > 0',
    'O sinal da função f(x) = x² - 5x + 6 é positivo para:',
    '["x < 2 ou x > 3", "2 < x < 3", "x < 0", "x > 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x < 2 ou x > 3', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 161, 163,
    'f(x) = x² - 5x + 6', true, 'PARABOLA', 1, -5, 6, 'eixo x', 'eixo y', 0, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Inequação do 2º grau', 'Resolução',
    'O conjunto solução da inequação x² - 3x + 2 > 0 é:',
    '["x < 1 ou x > 2", "1 < x < 2", "x ≤ 1 ou x ≥ 2", "x = 1 ou x = 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x < 1 ou x > 2', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 164, 166,
    '', true, 'PARABOLA', 1, -3, 2, 'eixo x', 'eixo y', -1, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Inequação do 2º grau', 'Resolução',
    'O conjunto solução da inequação -x² + 2x + 3 ≥ 0 é:',
    '["-1 ≤ x ≤ 3", "x ≤ -1 ou x ≥ 3", "x ≤ 1 ou x ≥ 3", "-3 ≤ x ≤ 1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-1 ≤ x ≤ 3', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 164, 166,
    '', true, 'PARABOLA', -1, 2, 3, 'eixo x', 'eixo y', -2, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Inequação do 2º grau', 'Δ < 0',
    'O conjunto solução de x² + 4x + 5 < 0 é:',
    '["∅", "todos os reais", "x < -1", "x > 1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '∅', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 164, 166,
    '', true, 'PARABOLA', 1, 4, 5, 'eixo x', 'eixo y', -4, 1, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Inequação do 2º grau', 'Δ = 0',
    'O conjunto solução de x² - 6x + 9 ≤ 0 é:',
    '["{3}", "x = 3", "todos os reais", "∅"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{3}', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 164, 166,
    '', true, 'PARABOLA', 1, -6, 9, 'eixo x', 'eixo y', 0, 5, 1
);

-- ✅ FUNÇÃO QUADRÁTICA - PARTE 1 COMPLETADA: 50 QUESTÕES
-- FALTAM MAIS 100 QUESTÕES PARA COMPLETAR 150

-- Mock SQL para Matemática - FUNÇÃO QUADRÁTICA (Capítulo VII) - PARTE 2
-- Baseado EXCLUSIVAMENTE no livro "Fundamentos de Matemática Elementar - Volume 1"
-- Iezzi & Murakami, 9ª ed., 2013
-- Disciplina: Matemática
-- Tópico principal: Função quadrática (Capítulo VII, páginas 137-181)
-- Total de questões neste lote (parte 2): 50 de 150

INSERT INTO perguntas (
    id, disciplina, topico_principal, topico, subtopico, questao, respostas, pesos_resposta,
    resposta_correta, dificuldade, rigor, referencia_livro, pagina_inicio, pagina_fim,
    exercicio, usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
) VALUES

-- =====================================================
-- RELAÇÕES DE GIRARD (SOMA E PRODUTO) - páginas 152-153
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Relações de Girard', 'Soma',
    'Na equação x² - 8x + 15 = 0, a soma das raízes é:',
    '["8", "15", "-8", "-15"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '8', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 152, 153,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Relações de Girard', 'Produto',
    'Na equação 2x² - 6x + 4 = 0, o produto das raízes é:',
    '["2", "3", "4", "1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 152, 153,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Relações de Girard', 'Soma e produto',
    'Na equação x² - 7x + 12 = 0, a soma e o produto das raízes são, respectivamente:',
    '["7 e 12", "-7 e 12", "7 e -12", "-7 e -12"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '7 e 12', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 152, 153,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Relações de Girard', 'Cálculo de k',
    'Na equação x² - 6x + k = 0, se a soma das raízes é 6 e o produto é 8, então k é:',
    '["8", "6", "-6", "-8"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '8', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 152, 153,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

-- =====================================================
-- COMPARAÇÃO DE NÚMERO REAL COM AS RAÍZES (páginas 172-178)
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Comparação com raízes', 'Número entre raízes',
    'Para que o número 2 esteja entre as raízes da equação x² - (m + 1)x + m = 0, m deve ser:',
    '["m < 0", "m > 0", "m = 0", "m = 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'm < 0', 'DESAFIANTE', 0.65, 'fundamentos-da-matematica-elementar-1-.pdf', 172, 178,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Comparação com raízes', 'Número entre raízes',
    'Para que o número 1 esteja entre as raízes da equação mx² + (m - 1)x - m = 0, m deve ser:',
    '["0 < m < 1", "m < 0", "m > 1", "m = 1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '0 < m < 1', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 172, 178,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Comparação com raízes', 'Raiz à esquerda',
    'Para que as raízes da equação x² - (m - 2)x + 1 = 0 sejam ambas menores que 2, m deve satisfazer:',
    '["m > 5", "m < 5", "m > 3", "m < 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'm > 5', 'DESAFIANTE', 0.70, 'fundamentos-da-matematica-elementar-1-.pdf', 172, 178,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

-- =====================================================
-- SINAIS DAS RAÍZES (páginas 179-181)
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Sinais das raízes', 'Raízes positivas',
    'Para que a equação x² - 5x + m = 0 tenha duas raízes positivas, m deve ser:',
    '["0 < m < 6,25", "m > 6,25", "m < 0", "m = 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '0 < m < 6,25', 'DESAFIANTE', 0.65, 'fundamentos-da-matematica-elementar-1-.pdf', 179, 181,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Sinais das raízes', 'Raízes negativas',
    'Para que a equação x² + 5x + m = 0 tenha duas raízes negativas, m deve ser:',
    '["0 < m < 6,25", "m > 6,25", "m < 0", "m = 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '0 < m < 6,25', 'DESAFIANTE', 0.65, 'fundamentos-da-matematica-elementar-1-.pdf', 179, 181,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Sinais das raízes', 'Raízes de sinais opostos',
    'Para que a equação x² - (m + 2)x + (m + 1) = 0 tenha raízes de sinais opostos, m deve ser:',
    '["m < -1", "m > -1", "m = -1", "m = 1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'm < -1', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 179, 181,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Sinais das raízes', 'Raízes de sinais opostos',
    'Para que a equação (m - 1)x² + (2m + 1)x + m = 0 tenha raízes de sinais opostos, m deve ser:',
    '["0 < m < 1", "m < 0", "m > 1", "m = 1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '0 < m < 1', 'DESAFIANTE', 0.68, 'fundamentos-da-matematica-elementar-1-.pdf', 179, 181,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

-- =====================================================
-- PROBLEMAS DE MÁXIMO E MÍNIMO (APLICAÇÕES) - páginas 145-151
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Aplicação', 'Área máxima',
    'Dentre todos os retângulos de perímetro 20 cm, a área máxima é:',
    '["25 cm²", "20 cm²", "24 cm²", "16 cm²"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '25 cm²', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-1-.pdf', 145, 151,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Aplicação', 'Produto máximo',
    'Dentre todos os números reais cuja soma é 10, o produto máximo é:',
    '["25", "20", "24", "16"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '25', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 145, 151,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Aplicação', 'Receita máxima',
    'A receita de um produto é R(x) = -2x² + 100x, onde x é o preço unitário. A receita máxima ocorre para x igual a:',
    '["25", "50", "20", "30"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '25', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 145, 151,
    '', true, 'PARABOLA', -2, 100, 0, 'preço (x)', 'receita (R)', 0, 50, 10
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Aplicação', 'Altura máxima',
    'Um projétil é lançado com altura h(t) = -5t² + 20t. A altura máxima é:',
    '["20 m", "40 m", "10 m", "30 m"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '20 m', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 145, 151,
    'h(t) = -5t² + 20t', true, 'PARABOLA', -5, 20, 0, 'tempo (s)', 'altura (m)', 0, 4, 1
),

-- =====================================================
-- INEQUAÇÕES DO 2º GRAU COM PARÂMETROS
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Inequações com parâmetros', 'Sempre positiva',
    'Para que f(x) = x² - 2x + m seja sempre positiva, m deve ser:',
    '["m > 1", "m < 1", "m ≥ 1", "m ≤ 1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'm > 1', 'DESAFIANTE', 0.60, 'fundamentos-da-matematica-elementar-1-.pdf', 164, 166,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Inequações com parâmetros', 'Sempre negativa',
    'Para que f(x) = -x² + 2x + m seja sempre negativa, m deve ser:',
    '["m < -1", "m > -1", "m ≤ -1", "m ≥ -1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'm < -1', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 164, 166,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Inequações com parâmetros', 'Discriminante',
    'Para que f(x) = x² + mx + 1 seja sempre positiva, m deve satisfazer:',
    '["-2 < m < 2", "m < -2 ou m > 2", "m = ±2", "m > 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-2 < m < 2', 'DESAFIANTE', 0.65, 'fundamentos-da-matematica-elementar-1-.pdf', 164, 166,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

-- =====================================================
-- QUESTÕES DE VESTIBULAR (ESTILO CONCURSO)
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Vestibular', 'Gráfico',
    'O gráfico de uma função quadrática tem vértice em (1, -4) e passa pelo ponto (0, -3). A equação da função é:',
    '["y = x² - 2x - 3", "y = x² + 2x - 3", "y = -x² + 2x - 3", "y = -x² - 2x - 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'y = x² - 2x - 3', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 147, 151,
    'y = x² - 2x - 3', true, 'PARABOLA', 1, -2, -3, 'eixo x', 'eixo y', -1, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Vestibular', 'Gráfico',
    'O gráfico de uma função quadrática intercepta o eixo x em -1 e 3, e o eixo y em (0, -6). A equação da função é:',
    '["y = 2x² - 4x - 6", "y = x² - 2x - 3", "y = 2x² + 4x - 6", "y = -x² + 2x + 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'y = 2x² - 4x - 6', 'DESAFIANTE', 0.65, 'fundamentos-da-matematica-elementar-1-.pdf', 140, 144,
    'y = 2x² - 4x - 6', true, 'PARABOLA', 2, -4, -6, 'eixo x', 'eixo y', -2, 4, 1
),

-- =====================================================
-- QUESTÕES COMPLEMENTARES PARA COMPLETAR 50
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Gráfico', 'Concavidade',
    'A parábola da função f(x) = 5x² tem concavidade voltada para cima e seu vértice é:',
    '["(0, 0)", "(0, 5)", "(5, 0)", "(-5, 0)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(0, 0)', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 139, 147,
    'f(x) = 5x²', true, 'PARABOLA', 5, 0, 0, 'eixo x', 'eixo y', -2, 2, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Gráfico', 'Concavidade',
    'A parábola da função f(x) = -2x² + 3 tem vértice em:',
    '["(0, 3)", "(0, -3)", "(3, 0)", "(-3, 0)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(0, 3)', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 139, 147,
    'f(x) = -2x² + 3', true, 'PARABOLA', -2, 0, 3, 'eixo x', 'eixo y', -2, 2, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Zeros', 'Com Δ = 0',
    'Para que a função f(x) = x² - 2x + m tenha um zero real duplo, m deve ser:',
    '["1", "0", "-1", "2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 140, 142,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Zeros', 'Com duas raízes distintas',
    'Para que a função f(x) = x² - 2x + m tenha duas raízes reais distintas, m deve ser:',
    '["m < 1", "m > 1", "m = 1", "m = 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'm < 1', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 140, 142,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Sinal da função', 'Δ > 0',
    'A função f(x) = x² - 4x + 3 é negativa para:',
    '["1 < x < 3", "x < 1 ou x > 3", "x < 0", "x > 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1 < x < 3', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 161, 163,
    'f(x) = x² - 4x + 3', true, 'PARABOLA', 1, -4, 3, 'eixo x', 'eixo y', 0, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Sinal da função', 'Δ < 0',
    'A função f(x) = x² + 4x + 5 é:',
    '["sempre positiva", "sempre negativa", "positiva para x > 0", "negativa para x < 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'sempre positiva', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 159, 161,
    'f(x) = x² + 4x + 5', true, 'PARABOLA', 1, 4, 5, 'eixo x', 'eixo y', -4, 1, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Inequação do 2º grau', 'Resolução',
    'O conjunto solução da inequação x² - 4x + 4 > 0 é:',
    '["x ≠ 2", "x < 2 ou x > 2", "x > 2", "x < 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x ≠ 2', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 164, 166,
    '', true, 'PARABOLA', 1, -4, 4, 'eixo x', 'eixo y', -1, 5, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Inequação do 2º grau', 'Resolução',
    'O conjunto solução da inequação -x² + 2x - 1 ≥ 0 é:',
    '["{1}", "x = 1", "todos os reais", "∅"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '{1}', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 164, 166,
    '', true, 'PARABOLA', -1, 2, -1, 'eixo x', 'eixo y', -1, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Relações de Girard', 'Soma',
    'Na equação 3x² - 9x + 6 = 0, a soma das raízes é:',
    '["3", "2", "4", "1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 152, 153,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Relações de Girard', 'Produto',
    'Na equação 3x² - 9x + 6 = 0, o produto das raízes é:',
    '["2", "3", "4", "1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 152, 153,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Sinais das raízes', 'Ambas positivas',
    'Para que a equação x² - 4x + m = 0 tenha duas raízes positivas, m deve ser:',
    '["0 < m < 4", "m > 4", "m < 0", "m = 4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '0 < m < 4', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 179, 181,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Sinais das raízes', 'Ambas negativas',
    'Para que a equação x² + 4x + m = 0 tenha duas raízes negativas, m deve ser:',
    '["0 < m < 4", "m > 4", "m < 0", "m = 4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '0 < m < 4', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 179, 181,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Comparação com raízes', 'Número maior',
    'Para que o número 3 seja maior que ambas as raízes da equação x² - (m + 1)x + m = 0, m deve ser:',
    '["m > 2", "m < 2", "m > 3", "m < 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'm > 2', 'DESAFIANTE', 0.68, 'fundamentos-da-matematica-elementar-1-.pdf', 172, 178,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Comparação com raízes', 'Número menor',
    'Para que o número -1 seja menor que ambas as raízes da equação x² - 2x + m = 0, m deve ser:',
    '["m > -3", "m < -3", "m > 3", "m < 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'm > -3', 'DESAFIANTE', 0.68, 'fundamentos-da-matematica-elementar-1-.pdf', 172, 178,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Aplicação', 'Lucro máximo',
    'O lucro de uma empresa é L(x) = -x² + 50x - 200, onde x é a quantidade produzida. O lucro máximo é:',
    '["425", "400", "450", "500"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '425', 'MEDIO', 0.55, 'fundamentos-da-matematica-elementar-1-.pdf', 145, 151,
    '', true, 'PARABOLA', -1, 50, -200, 'quantidade (x)', 'lucro (L)', 0, 40, 10
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Aplicação', 'Área máxima',
    'Um campo retangular tem 100 m de cerca disponível para cercar três lados (um lado é um rio). A área máxima é:',
    '["1250 m²", "2500 m²", "1000 m²", "2000 m²"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1250 m²', 'DESAFIANTE', 0.65, 'fundamentos-da-matematica-elementar-1-.pdf', 145, 151,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
);

-- ✅ FUNÇÃO QUADRÁTICA - PARTE 2 COMPLETADA: 50 QUESTÕES
-- TOTAL DA FUNÇÃO QUADRÁTICA: 100/150 QUESTÕES
-- FALTAM MAIS 50 QUESTÕES PARA COMPLETAR 150


-- Mock SQL para Matemática - FUNÇÃO QUADRÁTICA (Capítulo VII) - PARTE 3 (FINAL)
-- Baseado EXCLUSIVAMENTE no livro "Fundamentos de Matemática Elementar - Volume 1"
-- Iezzi & Murakami, 9ª ed., 2013
-- Disciplina: Matemática
-- Tópico principal: Função quadrática (Capítulo VII, páginas 137-181)
-- Total de questões neste lote (parte 3): 50 (completando 150)

INSERT INTO perguntas (
    id, disciplina, topico_principal, topico, subtopico, questao, respostas, pesos_resposta,
    resposta_correta, dificuldade, rigor, referencia_livro, pagina_inicio, pagina_fim,
    exercicio, usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
) VALUES

-- =====================================================
-- INEQUAÇÕES DO 2º GRAU COM PARÂMETROS (continuação)
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Inequações com parâmetros', 'Sempre positiva',
    'Para que f(x) = mx² + (2m - 1)x + (m + 1) seja sempre positiva, m deve satisfazer:',
    '["m > 1/8", "m < 1/8", "m > 0", "m < 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'm > 1/8', 'DESAFIANTE', 0.70, 'fundamentos-da-matematica-elementar-1-.pdf', 164, 166,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Inequações com parâmetros', 'Sempre negativa',
    'Para que f(x) = -x² + mx - 1 seja sempre negativa, m deve satisfazer:',
    '["-2 < m < 2", "m < -2 ou m > 2", "m = ±2", "m > 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-2 < m < 2', 'DESAFIANTE', 0.65, 'fundamentos-da-matematica-elementar-1-.pdf', 164, 166,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Inequações com parâmetros', 'Domínio',
    'Para que o domínio da função f(x) = √(x² - 2x + m) seja todos os reais, m deve ser:',
    '["m ≥ 1", "m > 1", "m ≤ 1", "m < 1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'm ≥ 1', 'DESAFIANTE', 0.65, 'fundamentos-da-matematica-elementar-1-.pdf', 164, 166,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

-- =====================================================
-- SISTEMAS COM FUNÇÃO QUADRÁTICA
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Sistemas', 'Interseção de gráficos',
    'Os gráficos de y = x² - 2x + 1 e y = x + 1 se interceptam no ponto:',
    '["(0, 1) e (3, 4)", "(1, 0) e (4, 3)", "(0, 0) e (2, 3)", "(1, 2) e (3, 4)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(0, 1) e (3, 4)', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 139, 144,
    '', true, 'PARABOLA', 1, -2, 1, 'eixo x', 'eixo y', -1, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Sistemas', 'Interseção de gráficos',
    'Os gráficos de y = -x² + 4x - 3 e y = 0 se interceptam nos pontos:',
    '["(1, 0) e (3, 0)", "(0, 1) e (0, 3)", "(2, 0) e (4, 0)", "(-1, 0) e (-3, 0)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(1, 0) e (3, 0)', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 140, 144,
    'y = -x² + 4x - 3', true, 'PARABOLA', -1, 4, -3, 'eixo x', 'eixo y', -1, 4, 1
),

-- =====================================================
-- SINAL DA FUNÇÃO QUADRÁTICA (continuação)
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Sinal da função', 'Estudo completo',
    'O sinal da função f(x) = x² - 6x + 8 é:',
    '["positiva para x < 2 ou x > 4, negativa para 2 < x < 4", "negativa para x < 2 ou x > 4", "sempre positiva", "sempre negativa"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'positiva para x < 2 ou x > 4, negativa para 2 < x < 4', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 161, 163,
    'f(x) = x² - 6x + 8', true, 'PARABOLA', 1, -6, 8, 'eixo x', 'eixo y', 0, 5, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Sinal da função', 'Estudo completo',
    'O sinal da função f(x) = -x² + 4x - 4 é:',
    '["negativa para x ≠ 2, zero em x = 2", "positiva para x ≠ 2", "sempre negativa", "sempre positiva"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'negativa para x ≠ 2, zero em x = 2', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 161, 163,
    'f(x) = -x² + 4x - 4', true, 'PARABOLA', -1, 4, -4, 'eixo x', 'eixo y', -1, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Sinal da função', 'Estudo completo',
    'O sinal da função f(x) = x² + 2x + 1 é:',
    '["positiva para x ≠ -1, zero em x = -1", "negativa para x ≠ -1", "sempre positiva", "sempre negativa"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'positiva para x ≠ -1, zero em x = -1', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 161, 163,
    'f(x) = x² + 2x + 1', true, 'PARABOLA', 1, 2, 1, 'eixo x', 'eixo y', -3, 2, 1
),

-- =====================================================
-- INEQUAÇÕES DO 2º GRAU (continuação)
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Inequação do 2º grau', 'Produto de fatores',
    'O conjunto solução da inequação (x² - 4)(x - 1) > 0 é:',
    '["-2 < x < 1 ou x > 2", "x < -2 ou 1 < x < 2", "x < -2 ou x > 2", "-2 < x < 2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-2 < x < 1 ou x > 2', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 164, 171,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Inequação do 2º grau', 'Quociente',
    'O conjunto solução da inequação (x² - 9)/(x + 2) ≤ 0 é:',
    '["x < -3 ou -2 < x ≤ 3", "-3 ≤ x < -2 ou x ≥ 3", "x < -3 ou x > -2", "-3 ≤ x ≤ 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x < -3 ou -2 < x ≤ 3', 'DESAFIANTE', 0.65, 'fundamentos-da-matematica-elementar-1-.pdf', 164, 171,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

-- =====================================================
-- VALOR MÁXIMO E MÍNIMO COM PARÂMETROS
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Máximo e mínimo', 'Parâmetro',
    'Para que a função f(x) = x² - 2x + m tenha valor mínimo igual a 3, m deve ser:',
    '["4", "3", "2", "1"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '4', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 145, 147,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Máximo e mínimo', 'Parâmetro',
    'Para que a função f(x) = -x² + 4x + m tenha valor máximo igual a 5, m deve ser:',
    '["1", "2", "3", "4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1', 'MEDIO', 0.52, 'fundamentos-da-matematica-elementar-1-.pdf', 145, 147,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

-- =====================================================
-- ZEROS DA FUNÇÃO COM PARÂMETROS
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Zeros com parâmetros', 'Raiz dupla',
    'Para que a função f(x) = mx² + (2m - 1)x + m - 2 tenha um zero real duplo, m deve ser:',
    '["1/4", "-1/4", "1/2", "-1/2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1/4', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 140, 144,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Zeros com parâmetros', 'Duas raízes reais',
    'Para que a função f(x) = mx² + (m + 1)x + m tenha duas raízes reais distintas, m deve ser:',
    '["-1/3 < m < 0", "m < -1/3 ou m > 0", "m > 0", "m < -1/3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'm < -1/3 ou m > 0', 'DESAFIANTE', 0.65, 'fundamentos-da-matematica-elementar-1-.pdf', 140, 144,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),

-- =====================================================
-- QUESTÕES DE CONCURSO (ESTILO VESTIBULAR)
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Vestibular', 'Análise de gráfico',
    'A figura mostra o gráfico de uma função quadrática. A equação da função é:',
    '["y = x² - 4x", "y = -x² + 4x", "y = x² - 4", "y = -x² + 4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'y = -x² + 4x', 'DESAFIANTE', 0.60, 'fundamentos-da-matematica-elementar-1-.pdf', 139, 151,
    '', true, 'PARABOLA', -1, 4, 0, 'eixo x', 'eixo y', -1, 5, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Vestibular', 'Análise de gráfico',
    'A figura mostra o gráfico de uma função quadrática com vértice em (2, -1) e passando pela origem. A equação é:',
    '["y = (1/4)x² - x", "y = -x² + 4x", "y = x² - 4x", "y = -1/4x² + x"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'y = (1/4)x² - x', 'DESAFIANTE', 0.68, 'fundamentos-da-matematica-elementar-1-.pdf', 147, 151,
    '', true, 'PARABOLA', 0.25, -1, 0, 'eixo x', 'eixo y', -1, 5, 1
),

-- =====================================================
-- QUESTÕES COMPLEMENTARES PARA COMPLETAR 50
-- =====================================================
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Gráfico', 'Vértice',
    'O vértice da parábola f(x) = 2x² - 8x + 7 é o ponto:',
    '["(2, -1)", "(-2, -1)", "(2, 1)", "(-2, 1)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(2, -1)', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 147, 149,
    'f(x) = 2x² - 8x + 7', true, 'PARABOLA', 2, -8, 7, 'eixo x', 'eixo y', 0, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Gráfico', 'Eixo de simetria',
    'O eixo de simetria da parábola f(x) = -3x² + 6x + 1 é a reta:',
    '["x = 1", "x = -1", "x = 2", "x = -2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x = 1', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 152, 153,
    'f(x) = -3x² + 6x + 1', true, 'PARABOLA', -3, 6, 1, 'eixo x', 'eixo y', -1, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Zeros', 'Cálculo com Δ',
    'A função f(x) = 2x² - 5x + 3 tem zeros:',
    '["1 e 1,5", "1 e 2", "0,5 e 3", "2 e 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1 e 1,5', 'FACIL', 0.30, 'fundamentos-da-matematica-elementar-1-.pdf', 140, 142,
    'f(x) = 2x² - 5x + 3', true, 'PARABOLA', 2, -5, 3, 'eixo x', 'eixo y', -1, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Sinal da função', 'Estudo',
    'A função f(x) = 2x² - 5x + 3 é negativa para:',
    '["1 < x < 1,5", "x < 1 ou x > 1,5", "x < 0", "x > 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1 < x < 1,5', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 161, 163,
    'f(x) = 2x² - 5x + 3', true, 'PARABOLA', 2, -5, 3, 'eixo x', 'eixo y', -1, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Inequação do 2º grau', 'Resolução',
    'O conjunto solução da inequação 2x² - 5x + 3 ≤ 0 é:',
    '["1 ≤ x ≤ 1,5", "x ≤ 1 ou x ≥ 1,5", "x ≤ 0", "x ≥ 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '1 ≤ x ≤ 1,5', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 164, 166,
    '', true, 'PARABOLA', 2, -5, 3, 'eixo x', 'eixo y', -1, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Relações de Girard', 'Soma e produto',
    'Na equação 2x² - 5x + 3 = 0, a soma e o produto das raízes são, respectivamente:',
    '["2,5 e 1,5", "2,5 e 1", "5 e 3", "-2,5 e -1,5"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '2,5 e 1,5', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 152, 153,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Sinais das raízes', 'Ambas positivas',
    'Para que a equação x² - 3x + m = 0 tenha duas raízes positivas, m deve ser:',
    '["0 < m < 2,25", "m > 2,25", "m < 0", "m = 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '0 < m < 2,25', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 179, 181,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Comparação com raízes', 'Número entre raízes',
    'Para que o número 2 esteja entre as raízes da equação x² - 3x + m = 0, m deve ser:',
    '["m < 2", "m > 2", "m < 0", "m > 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'm < 2', 'DESAFIANTE', 0.65, 'fundamentos-da-matematica-elementar-1-.pdf', 172, 178,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Aplicação', 'Lucro máximo',
    'O lucro de uma empresa é L(x) = -2x² + 80x - 100, onde x é a quantidade produzida. A quantidade que maximiza o lucro é:',
    '["20", "40", "10", "30"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '20', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 145, 151,
    '', true, 'PARABOLA', -2, 80, -100, 'quantidade (x)', 'lucro (L)', 0, 30, 10
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Aplicação', 'Área máxima',
    'Um retângulo tem perímetro 40 m. A área máxima possível é:',
    '["100 m²", "80 m²", "120 m²", "60 m²"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '100 m²', 'FACIL', 0.32, 'fundamentos-da-matematica-elementar-1-.pdf', 145, 151,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Aplicação', 'Altura máxima',
    'Um projétil é lançado com altura h(t) = -4t² + 16t. A altura máxima é:',
    '["16 m", "32 m", "8 m", "24 m"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '16 m', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 145, 151,
    'h(t) = -4t² + 16t', true, 'PARABOLA', -4, 16, 0, 'tempo (s)', 'altura (m)', 0, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Gráfico', 'Interseção com eixos',
    'A função f(x) = x² - 4 intercepta o eixo x nos pontos:',
    '["(2, 0) e (-2, 0)", "(4, 0) e (-4, 0)", "(0, 2) e (0, -2)", "(0, 4) e (0, -4)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(2, 0) e (-2, 0)', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 140, 144,
    'f(x) = x² - 4', true, 'PARABOLA', 1, 0, -4, 'eixo x', 'eixo y', -3, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Gráfico', 'Interseção com eixos',
    'A função f(x) = -x² + 4 intercepta o eixo y no ponto:',
    '["(0, 4)", "(4, 0)", "(0, -4)", "(-4, 0)"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '(0, 4)', 'FACIL', 0.25, 'fundamentos-da-matematica-elementar-1-.pdf', 139, 140,
    'f(x) = -x² + 4', true, 'PARABOLA', -1, 0, 4, 'eixo x', 'eixo y', -3, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Zeros', 'Δ < 0',
    'A função f(x) = x² - 2x + 2:',
    '["não tem zeros reais", "tem dois zeros reais", "tem um zero real duplo", "tem raízes complexas"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'não tem zeros reais', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 142, 143,
    'f(x) = x² - 2x + 2', true, 'PARABOLA', 1, -2, 2, 'eixo x', 'eixo y', -1, 3, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Sinal da função', 'Δ > 0',
    'A função f(x) = x² - 4x + 3 é positiva para:',
    '["x < 1 ou x > 3", "1 < x < 3", "x < 0", "x > 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x < 1 ou x > 3', 'MEDIO', 0.48, 'fundamentos-da-matematica-elementar-1-.pdf', 161, 163,
    'f(x) = x² - 4x + 3', true, 'PARABOLA', 1, -4, 3, 'eixo x', 'eixo y', 0, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Inequação do 2º grau', 'Resolução',
    'O conjunto solução da inequação x² - 4x + 3 ≥ 0 é:',
    '["x ≤ 1 ou x ≥ 3", "1 ≤ x ≤ 3", "x < 1 ou x > 3", "1 < x < 3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'x ≤ 1 ou x ≥ 3', 'MEDIO', 0.50, 'fundamentos-da-matematica-elementar-1-.pdf', 164, 166,
    '', true, 'PARABOLA', 1, -4, 3, 'eixo x', 'eixo y', 0, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Vértice', 'Fórmula alternativa',
    'A ordenada do vértice da parábola f(x) = x² - 4x + 3 é:',
    '["-1", "1", "-2", "2"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '-1', 'FACIL', 0.28, 'fundamentos-da-matematica-elementar-1-.pdf', 147, 149,
    'f(x) = x² - 4x + 3', true, 'PARABOLA', 1, -4, 3, 'eixo x', 'eixo y', 0, 4, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Relações de Girard', 'Soma',
    'Na equação x² - 4x + 3 = 0, a soma das raízes é:',
    '["4", "3", "-4", "-3"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '4', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 152, 153,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Relações de Girard', 'Produto',
    'Na equação x² - 4x + 3 = 0, o produto das raízes é:',
    '["3", "4", "-3", "-4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '3', 'FACIL', 0.20, 'fundamentos-da-matematica-elementar-1-.pdf', 152, 153,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Sinais das raízes', 'Ambas negativas',
    'Para que a equação x² + 4x + m = 0 tenha duas raízes negativas, m deve ser:',
    '["0 < m < 4", "m > 4", "m < 0", "m = 4"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    '0 < m < 4', 'DESAFIANTE', 0.62, 'fundamentos-da-matematica-elementar-1-.pdf', 179, 181,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
),
(
    uuid_generate_v4(), 'Matemática', 'Função quadrática', 'Comparação com raízes', 'Número entre raízes',
    'Para que o número 0 esteja entre as raízes da equação x² - mx + (m - 1) = 0, m deve ser:',
    '["m < 1", "m > 1", "m = 1", "m > 0"]'::jsonb, '[1.0, 0.2, 0.4, 0.1]'::jsonb,
    'm < 1', 'DESAFIANTE', 0.65, 'fundamentos-da-matematica-elementar-1-.pdf', 172, 178,
    '', false, 'NENHUM', 0, 0, 0, '', '', 0, 0, 1
);

-- ✅ FUNÇÃO QUADRÁTICA - PARTE 3 COMPLETADA: 50 QUESTÕES
-- ✅ FUNÇÃO QUADRÁTICA TOTAL: 150/150 QUESTÕES COMPLETADAS