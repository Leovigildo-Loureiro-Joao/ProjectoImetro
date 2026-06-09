insert into perguntas (
    id, disciplina, topico_principal, topico, subtopico,
    questao, respostas, pesos_resposta, resposta_correta,
    dificuldade, rigor, referencia_livro,
    pagina_inicio, pagina_fim, exercicio,
    usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
)
values (
    uuid_generate_v4(),
    'Matemática',
    'Potências e Raízes',
    'Potências Naturais',
    'Propriedades das Potências',
    'Qual é o valor de 2^3 × 2^4?',
    '["64","128","32","256"]'::jsonb,
    '[0.3,1.0,0.2,0.1]'::jsonb,
    '128',
    'FACIL',
    0.32,
    'fundamentos-da-matematica-elementar-2-.pdf',
    12,14,
    '',
    false,'NENHUM',0,0,0,
    'eixo x','eixo y',0,1,1
);

insert into perguntas (
    id, disciplina, topico_principal, topico, subtopico,
    questao, respostas, pesos_resposta, resposta_correta,
    dificuldade, rigor, referencia_livro,
    pagina_inicio, pagina_fim, exercicio,
    usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
)
values (
    uuid_generate_v4(),
    'Matemática',
    'Potências e Raízes',
    'Potências Naturais',
    'Expoente Zero',
    'Sabendo que a ≠ 0, quanto vale a^0?',
    '["0","a","1","-1"]'::jsonb,
    '[0.1,0.2,1.0,0.3]'::jsonb,
    '1',
    'FACIL',
    0.25,
    'fundamentos-da-matematica-elementar-2-.pdf',
    12,14,
    '',
    false,'NENHUM',0,0,0,
    'eixo x','eixo y',0,1,1
);

insert into perguntas (
    id, disciplina, topico_principal, topico, subtopico,
    questao, respostas, pesos_resposta, resposta_correta,
    dificuldade, rigor, referencia_livro,
    pagina_inicio, pagina_fim, exercicio,
    usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
)
values (
    uuid_generate_v4(),
    'Matemática',
    'Potências e Raízes',
    'Potências Naturais',
    'Base Negativa',
    'Qual é o sinal de (-3)^5?',
    '["Positivo","Nulo","Negativo","Indefinido"]'::jsonb,
    '[0.2,0.1,1.0,0.3]'::jsonb,
    'Negativo',
    'FACIL',
    0.35,
    'fundamentos-da-matematica-elementar-2-.pdf',
    12,14,
    '',
    false,'NENHUM',0,0,0,
    'eixo x','eixo y',0,1,1
);

insert into perguntas (
    id, disciplina, topico_principal, topico, subtopico,
    questao, respostas, pesos_resposta, resposta_correta,
    dificuldade, rigor, referencia_livro,
    pagina_inicio, pagina_fim, exercicio,
    usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
)
values (
    uuid_generate_v4(),
    'Matemática',
    'Potências e Raízes',
    'Expoente Negativo',
    'Definição',
    'Quanto vale 2^-3?',
    '["8","1/8","-8","1/6"]'::jsonb,
    '[0.2,1.0,0.1,0.3]'::jsonb,
    '1/8',
    'FACIL',
    0.40,
    'fundamentos-da-matematica-elementar-2-.pdf',
    14,16,
    '',
    false,'NENHUM',0,0,0,
    'eixo x','eixo y',0,1,1
);

insert into perguntas (
    id, disciplina, topico_principal, topico, subtopico,
    questao, respostas, pesos_resposta, resposta_correta,
    dificuldade, rigor, referencia_livro,
    pagina_inicio, pagina_fim, exercicio,
    usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
)
values (
    uuid_generate_v4(),
    'Matemática',
    'Potências e Raízes',
    'Expoente Negativo',
    'Cálculo',
    'Determine o valor de (−2)^−2.',
    '["1/4","-1/4","4","-4"]'::jsonb,
    '[1.0,0.4,0.2,0.1]'::jsonb,
    '1/4',
    'MEDIO',
    0.55,
    'fundamentos-da-matematica-elementar-2-.pdf',
    14,17,
    '',
    false,'NENHUM',0,0,0,
    'eixo x','eixo y',0,1,1
);

insert into perguntas (
    id, disciplina, topico_principal, topico, subtopico,
    questao, respostas, pesos_resposta, resposta_correta,
    dificuldade, rigor, referencia_livro,
    pagina_inicio, pagina_fim, exercicio,
    usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
)
values (
    uuid_generate_v4(),
    'Matemática',
    'Potências e Raízes',
    'Raiz Enésima',
    'Definição',
    'Qual é a raiz cúbica de 125?',
    '["4","25","5","15"]'::jsonb,
    '[0.2,0.1,1.0,0.3]'::jsonb,
    '5',
    'FACIL',
    0.30,
    'fundamentos-da-matematica-elementar-2-.pdf',
    17,19,
    '',
    false,'NENHUM',0,0,0,
    'eixo x','eixo y',0,1,1
);

insert into perguntas (
    id, disciplina, topico_principal, topico, subtopico,
    questao, respostas, pesos_resposta, resposta_correta,
    dificuldade, rigor, referencia_livro,
    pagina_inicio, pagina_fim, exercicio,
    usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
)
values (
    uuid_generate_v4(),
    'Matemática',
    'Potências e Raízes',
    'Raiz Quadrada',
    'Valor Absoluto',
    'O valor de √((-5)^2) é:',
    '["-5","5","25","0"]'::jsonb,
    '[0.3,1.0,0.1,0.2]'::jsonb,
    '5',
    'MEDIO',
    0.58,
    'fundamentos-da-matematica-elementar-2-.pdf',
    18,20,
    '',
    false,'NENHUM',0,0,0,
    'eixo x','eixo y',0,1,1
);

insert into perguntas (
    id, disciplina, topico_principal, topico, subtopico,
    questao, respostas, pesos_resposta, resposta_correta,
    dificuldade, rigor, referencia_livro,
    pagina_inicio, pagina_fim, exercicio,
    usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
)
values (
    uuid_generate_v4(),
    'Matemática',
    'Potências e Raízes',
    'Simplificação de Radicais',
    'Fatoração',
    'Simplifique √72.',
    '["6√2","8√2","4√2","3√2"]'::jsonb,
    '[1.0,0.2,0.3,0.1]'::jsonb,
    '6√2',
    'MEDIO',
    0.60,
    'fundamentos-da-matematica-elementar-2-.pdf',
    21,23,
    '',
    false,'NENHUM',0,0,0,
    'eixo x','eixo y',0,1,1
);

insert into perguntas (
    id, disciplina, topico_principal, topico, subtopico,
    questao, respostas, pesos_resposta, resposta_correta,
    dificuldade, rigor, referencia_livro,
    pagina_inicio, pagina_fim, exercicio,
    usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
)
values (
    uuid_generate_v4(),
    'Matemática',
    'Potências e Raízes',
    'Racionalização',
    'Denominadores Irracionais',
    'Racionalize 1/√3.',
    '["√3","√3/3","3√3","1/3"]'::jsonb,
    '[0.2,1.0,0.1,0.3]'::jsonb,
    '√3/3',
    'MEDIO',
    0.64,
    'fundamentos-da-matematica-elementar-2-.pdf',
    24,25,
    '',
    false,'NENHUM',0,0,0,
    'eixo x','eixo y',0,1,1
);

insert into perguntas (
    id, disciplina, topico_principal, topico, subtopico,
    questao, respostas, pesos_resposta, resposta_correta,
    dificuldade, rigor, referencia_livro,
    pagina_inicio, pagina_fim, exercicio,
    usa_grafico, grafico_tipo_curva, grafico_a, grafico_b, grafico_c,
    grafico_eixo_x, grafico_eixo_y, grafico_x_min, grafico_x_max, grafico_x_tick_unit
)
values (
    uuid_generate_v4(),
    'Matemática',
    'Potências e Raízes',
    'Expoente Racional',
    'Conversão',
    'O valor de 27^(1/3) é:',
    '["9","6","3","1"]'::jsonb,
    '[0.2,0.1,1.0,0.3]'::jsonb,
    '3',
    'MEDIO',
    0.62,
    'fundamentos-da-matematica-elementar-2-.pdf',
    25,28,
    '',
    false,'NENHUM',0,0,0,
    'eixo x','eixo y',0,1,1
);