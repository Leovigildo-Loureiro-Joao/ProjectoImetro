
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