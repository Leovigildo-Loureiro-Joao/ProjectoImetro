  -- Schema base (PostgreSQL/Supabase) sem ORM/Spring.
  -- Este script cria apenas a estrutura de dados (tabelas/índices) do MVP.

  create extension if not exists "uuid-ossp";

  -- Contas unificadas do produto, centradas no candidato.
  create table if not exists users (
    id uuid primary key default uuid_generate_v4(),
    nome text not null,
    email text not null unique,
    senha_hash text null,
    role text not null,
    avatar_url text null,
    constraint users_role_chk check (role in ('CANDIDATO')),
    criado_em timestamptz not null default now()
  );

  -- Relatórios com campos analíticos em JSONB (flexível para MVP).
  create table if not exists relatorios (
    id uuid primary key default uuid_generate_v4(),
    candidato_id uuid null references users(id) on delete set null,
    gerado_em timestamptz not null default now(),
    titulo text null,
    resumo text null,
    tempo_medio_segundos double precision null,
    dificuldade_atingida text null,
    taxa_acerto_por_topico jsonb not null default '{}'::jsonb,
    evolucao_semanal jsonb not null default '[]'::jsonb,
    erros_recorrentes jsonb not null default '[]'::jsonb,
    skills_boas jsonb not null default '[]'::jsonb,
    skills_fracas jsonb not null default '[]'::jsonb,
    recomendacoes_sugeridas jsonb not null default '[]'::jsonb,
    recomendacoes_validadas jsonb not null default '[]'::jsonb
  );

  create index if not exists idx_relatorios_candidato_id on relatorios (candidato_id);

  -- Banco de questões (MVP)
  create table if not exists perguntas (
    id uuid primary key default uuid_generate_v4(),
    disciplina text null,
    questao text not null,
    -- alternativas/respostas possíveis
    respostas jsonb not null default '[]'::jsonb,
    pesos_resposta jsonb not null default '[]'::jsonb,
    -- opcional: resposta correta (quando aplicável)
    resposta_correta text null,
    topico text null,
    subtopico text null,
    topico_principal text null,
    dificuldade text null,
    rigor double precision not null default 0.5,
    referencia_livro text null,
    pagina_inicio integer null,
    pagina_fim integer null,
    usa_grafico boolean not null default false,
    grafico_tipo_curva text null,
    grafico_a double precision null,
    grafico_b double precision null,
    grafico_c double precision null,
    grafico_eixo_x text null,
    grafico_eixo_y text null,
    grafico_x_min double precision null,
    grafico_x_max double precision null,
    grafico_x_tick_unit double precision null,
    criado_em timestamptz not null default now()
  );

  create index if not exists idx_perguntas_disciplina on perguntas (disciplina);
  create index if not exists idx_perguntas_topico on perguntas (topico);
  create index if not exists idx_perguntas_subtopico on perguntas (subtopico);
  create index if not exists idx_perguntas_rigor on perguntas (rigor);
  create index if not exists idx_perguntas_topico_rigor on perguntas (topico, rigor);
  create index if not exists idx_perguntas_topico_principal on perguntas (topico_principal);
  create index if not exists idx_perguntas_usa_grafico on perguntas (usa_grafico);

  -- Sessões/testes/simulados
  create table if not exists testes (
    id uuid primary key default uuid_generate_v4(),
    candidato_id uuid null references users(id) on delete set null,
    relatorio_id uuid null references relatorios(id) on delete set null,
    data_teste timestamptz null,
    resultado real null,
    criado_em timestamptz not null default now()
  );

  create index if not exists idx_testes_candidato_id on testes (candidato_id);
  create index if not exists idx_testes_relatorio_id on testes (relatorio_id);

  -- Perguntas de um teste (com ordem e resposta do candidato)
  create table if not exists teste_perguntas (
    teste_id uuid not null references testes(id) on delete cascade,
    pergunta_id uuid not null references perguntas(id) on delete restrict,
    ordem integer not null,
    resposta_dada text null,
    tempo_segundos integer null,
    precisao real null,
    velocidade real null,
    primary key (teste_id, ordem)
  );

  create index if not exists idx_teste_perguntas_pergunta_id on teste_perguntas (pergunta_id);

  -- Disciplinas suportadas no onboarding (Matematica/Fisica).
  create table if not exists disciplinas (
    id uuid primary key default uuid_generate_v4(),
    nome text not null unique,
    peso double precision not null default 1.0,
    nivel text not null default 'INICIANTE',
    objectivo text null
  );

  create table if not exists candidato_disciplinas (
    candidato_id uuid not null references users(id) on delete cascade,
    disciplina_id uuid not null references disciplinas(id) on delete cascade,
    primary key (candidato_id, disciplina_id)
  );

  create index if not exists idx_candidato_disciplinas_candidato_id on candidato_disciplinas (candidato_id);

  -- Historico de diagnosticos academicos por candidato.
  create table if not exists diagnosticos (
    id uuid primary key default uuid_generate_v4(),
    candidato_id uuid not null references users(id) on delete cascade,
    disciplina_id uuid null references disciplinas(id) on delete set null,
    relatorio_id uuid null references relatorios(id) on delete set null,
    disciplina_nome text not null,
    iniciado_em timestamptz not null default now(),
    concluido_em timestamptz null,
    duracao_segundos integer not null default 0,
    total_questoes integer not null default 0,
    total_acertos integer not null default 0,
    total_erros integer not null default 0,
    percentual_acerto double precision null,
    evolucao_percentual double precision null,
    nivel text null,
    velocidade real null,
    precisao real null,
    consistencia real null,
    logica real null,
    resiliencia real null,
    respostas jsonb not null default '[]'::jsonb,
    observacoes text null,
    criado_em timestamptz not null default now(),
    atualizado_em timestamptz not null default now(),
    constraint diagnosticos_totais_chk check (
      duracao_segundos >= 0
      and total_questoes >= 0
      and total_acertos >= 0
      and total_erros >= 0
      and total_acertos + total_erros <= total_questoes
    ),
    constraint diagnosticos_percentuais_chk check (
      (percentual_acerto is null or percentual_acerto between 0 and 100)
      and (evolucao_percentual is null or evolucao_percentual between -100 and 100)
    ),
    constraint diagnosticos_metricas_chk check (
      (velocidade is null or velocidade between 0 and 1)
      and (precisao is null or precisao between 0 and 1)
      and (consistencia is null or consistencia between 0 and 1)
      and (logica is null or logica between 0 and 1)
      and (resiliencia is null or resiliencia between 0 and 1)
    )
  );

  create index if not exists idx_diagnosticos_candidato_id on diagnosticos (candidato_id);
  create index if not exists idx_diagnosticos_disciplina_id on diagnosticos (disciplina_id);
  create index if not exists idx_diagnosticos_relatorio_id on diagnosticos (relatorio_id);
  create index if not exists idx_diagnosticos_iniciado_em on diagnosticos (iniciado_em desc);

  -- Configuracoes por utilizador (preferencias do simulador/diagnostico).
  create table if not exists configuracoes (
    id uuid primary key default uuid_generate_v4(),
    user_id uuid not null references users(id) on delete cascade,

    -- Tempo (valor + unidade)
    temp_adapt_val integer null,
    temp_adapt_unit text null,
    speed_temp_val integer null,
    speed_temp_unit text null,

    -- Quantidade de questoes por tipo de teste
    long_test_q integer null,
    norm_test_q integer null,
    desaf_test_q integer null,
    extra_test_q integer null,

    -- Dificuldade padrao e comportamento das escolhas
    nivel_dificuldade_padrao text null,
    modo_escolhas text null,

    -- Variaveis/thresholds do diagnostico
    velocidade_segundos_por_percent integer null,
    resiliencia_repeticoes_por_dia integer null,
    precisao_consecutivas integer null,
    logica_qtd_desafiante_extra integer null,
    consistencia_percentual_min double precision null,

    criado_em timestamptz not null default now(),
    atualizado_em timestamptz not null default now(),

    constraint configuracoes_user_unique unique (user_id),
    constraint configuracoes_tempos_chk check (
      (temp_adapt_val is null or temp_adapt_val >= 0)
      and (speed_temp_val is null or speed_temp_val >= 0)
    ),
    constraint configuracoes_questoes_chk check (
      (long_test_q is null or long_test_q >= 0)
      and (norm_test_q is null or norm_test_q >= 0)
      and (desaf_test_q is null or desaf_test_q >= 0)
      and (extra_test_q is null or extra_test_q >= 0)
    ),
    constraint configuracoes_dificuldade_chk check (
      nivel_dificuldade_padrao is null
      or nivel_dificuldade_padrao in ('FACIL', 'MEDIO', 'DESAFIANTE', 'EXTRA')
    ),
    constraint configuracoes_modo_escolhas_chk check (
      modo_escolhas is null
      or modo_escolhas in ('NAO_PERMITIR', 'AMBIGUAS', 'DIAGNOSTICAS')
    ),
    constraint configuracoes_diag_chk check (
      (velocidade_segundos_por_percent is null or velocidade_segundos_por_percent >= 0)
      and (resiliencia_repeticoes_por_dia is null or resiliencia_repeticoes_por_dia >= 0)
      and (precisao_consecutivas is null or precisao_consecutivas >= 0)
      and (logica_qtd_desafiante_extra is null or logica_qtd_desafiante_extra >= 0)
      and (consistencia_percentual_min is null or consistencia_percentual_min between 0 and 100)
    )
  );

  create index if not exists idx_configuracoes_user_id on configuracoes (user_id);

create table if not exists progresso_aluno_disciplina (
  id uuid primary key default uuid_generate_v4(),
  aluno_id uuid not null references users(id) on delete cascade,
  disciplina_id uuid not null references disciplinas(id) on delete cascade,
  nivel_atual text not null default 'INICIANTE',
  nivel_anterior text,
  data_mudanca_nivel timestamptz,
  peso_atual double precision not null default 1.0,
  total_questoes_resolvidas integer not null default 0,
  total_acertos integer not null default 0,
  total_erros integer not null default 0,
  taxa_acerto_geral double precision, 
  ultimos_3_diagnosticos_acertos integer[] default '{}',
  ultimos_3_diagnosticos_total integer[] default '{}',

  ultimo_estudo timestamptz,
  dias_sem_estudo integer,
  streak_dias_consecutivos integer not null default 0,
  
  criado_em timestamptz not null default now(),
  atualizado_em timestamptz not null default now(),
  
  unique(aluno_id, disciplina_id),
  
  constraint nivel_valido check (nivel_atual in ('INICIANTE', 'INTERMEDIARIO', 'AVANCADO', 'EXPERT')),
  constraint peso_positivo check (peso_atual > 0)
);

create index if not exists idx_progresso_aluno_disciplinas_aluno_id on progresso_aluno_disciplina (aluno_id);
create index if not exists idx_progresso_aluno_disciplinas_disciplina_id on progresso_aluno_disciplina (disciplina_id);

create table if not exists progressao_rigor (
  id uuid primary key default uuid_generate_v4(),
  aluno_id uuid not null references users(id) on delete cascade,
  disciplina_id uuid not null references disciplinas(id) on delete cascade,
  topico text not null,
  rigor_atual double precision not null default 0.12,
  rigor_alvo double precision not null default 0.7,
  ultimo_acerto_em_rigor double precision null,
  ultimo_erro_em_rigor double precision null,
  tentativas_no_nivel integer not null default 0,
  acertos_consecutivos integer not null default 0,
  erros_consecutivos integer not null default 0,
  precisa_revisao boolean not null default false,
  recomendacao_livro text null,
  recomendacao_paginas text null,
  atualizado_em timestamptz not null default now(),
  unique(aluno_id, disciplina_id, topico),
  constraint progressao_rigor_rigor_chk check (
    rigor_atual between 0 and 1
    and rigor_alvo between 0 and 1
    and (ultimo_acerto_em_rigor is null or ultimo_acerto_em_rigor between 0 and 1)
    and (ultimo_erro_em_rigor is null or ultimo_erro_em_rigor between 0 and 1)
  ),
  constraint progressao_rigor_tentativas_chk check (
    tentativas_no_nivel >= 0
    and acertos_consecutivos >= 0
    and erros_consecutivos >= 0
  )
);

create index if not exists idx_progressao_rigor_aluno_disciplina on progressao_rigor (aluno_id, disciplina_id);
create index if not exists idx_progressao_rigor_topico on progressao_rigor (topico);

create table if not exists recomendacoes_rigor (
  id uuid primary key default uuid_generate_v4(),
  diagnostico_id uuid not null references diagnosticos(id) on delete cascade,
  topico text not null,
  rigor_recomendado double precision not null,
  nivel_atual double precision not null,
  progresso_atingido double precision null,
  recomendacao_livro text null,
  recomendacao_paginas text null,
  exercicios_sugeridos jsonb not null default '[]'::jsonb,
  precisa_novo_diagnostico boolean not null default false,
  criado_em timestamptz not null default now(),
  constraint recomendacoes_rigor_metricas_chk check (
    rigor_recomendado between 0 and 1
    and nivel_atual between 0 and 1
    and (progresso_atingido is null or progresso_atingido between 0 and 1)
  )
);

create index if not exists idx_recomendacoes_rigor_diagnostico on recomendacoes_rigor (diagnostico_id);
create index if not exists idx_recomendacoes_rigor_topico on recomendacoes_rigor (topico);

create table if not exists medalhas_catalogo (
  codigo text primary key,
  habilidade text not null,
  nivel text not null,
  titulo text not null,
  descricao text not null,
  imagem_ref text not null,
  meta_valor integer not null,
  meta_unidade text not null,
  ordem integer not null unique,
  criado_em timestamptz not null default now(),
  constraint medalhas_catalogo_habilidade_chk check (
    habilidade in ('TIME', 'PONTARIA', 'LOGICA', 'RESILIENCIA', 'CONSISTENCIA')
  ),
  constraint medalhas_catalogo_nivel_chk check (
    nivel in ('BRONZE', 'PRATA', 'OURO', 'PLATINA')
  ),
  constraint medalhas_catalogo_meta_chk check (
    meta_valor > 0 and ordem > 0
  )
);

create table if not exists user_medalhas (
  id uuid primary key default uuid_generate_v4(),
  user_id uuid not null references users(id) on delete cascade,
  medalha_codigo text not null references medalhas_catalogo(codigo) on delete cascade,
  progresso_atual integer not null default 0,
  recorde_valor integer null,
  origem text null,
  conquistada_em timestamptz not null default now(),
  atualizado_em timestamptz not null default now(),
  constraint user_medalhas_unique unique (user_id, medalha_codigo),
  constraint user_medalhas_progress_chk check (
    progresso_atual >= 0 and (recorde_valor is null or recorde_valor >= 0)
  )
);

create index if not exists idx_user_medalhas_user_id on user_medalhas (user_id);
create index if not exists idx_user_medalhas_codigo on user_medalhas (medalha_codigo);

insert into medalhas_catalogo (codigo, habilidade, nivel, titulo, descricao, imagem_ref, meta_valor, meta_unidade, ordem)
values
  ('TIME_BRONZE', 'TIME', 'BRONZE', 'Velocidade Bronze', 'Conquista ao acumular 5 testes velozes.', '/com/imetro/assets/imgs/time_bronze.png', 5, 'testes velozes', 1),
  ('TIME_PRATA', 'TIME', 'PRATA', 'Velocidade Prata', 'Conquista ao acumular 15 testes velozes.', '/com/imetro/assets/imgs/time_prata.png', 15, 'testes velozes', 2),
  ('TIME_OURO', 'TIME', 'OURO', 'Velocidade Ouro', 'Conquista ao acumular 30 testes velozes.', '/com/imetro/assets/imgs/time_ouro.png', 30, 'testes velozes', 3),
  ('TIME_PLATINA', 'TIME', 'PLATINA', 'Velocidade Platina', 'Conquista ao acumular 50 testes velozes.', '/com/imetro/assets/imgs/time_platina.png', 50, 'testes velozes', 4),

  ('PONTARIA_BRONZE', 'PONTARIA', 'BRONZE', 'Pontaria Bronze', 'Conquista ao acumular 5 testes precisos.', '/com/imetro/assets/imgs/pontaria_bronze.png', 5, 'testes precisos', 5),
  ('PONTARIA_PRATA', 'PONTARIA', 'PRATA', 'Pontaria Prata', 'Conquista ao acumular 15 testes precisos.', '/com/imetro/assets/imgs/pontaria_prata.png', 15, 'testes precisos', 6),
  ('PONTARIA_OURO', 'PONTARIA', 'OURO', 'Pontaria Ouro', 'Conquista ao acumular 30 testes precisos.', '/com/imetro/assets/imgs/pontaria_ouro.png', 30, 'testes precisos', 7),
  ('PONTARIA_PLATINA', 'PONTARIA', 'PLATINA', 'Pontaria Platina', 'Conquista ao acumular 50 testes precisos.', '/com/imetro/assets/imgs/pontaria_platina.png', 50, 'testes precisos', 8),

  ('LOGICA_BRONZE', 'LOGICA', 'BRONZE', 'Logica Bronze', 'Conquista ao acumular 5 desafios logicos.', '/com/imetro/assets/imgs/logica_bronze.png', 5, 'desafios logicos', 9),
  ('LOGICA_PRATA', 'LOGICA', 'PRATA', 'Logica Prata', 'Conquista ao acumular 15 desafios logicos.', '/com/imetro/assets/imgs/logica_prata.png', 15, 'desafios logicos', 10),
  ('LOGICA_OURO', 'LOGICA', 'OURO', 'Logica Ouro', 'Conquista ao acumular 30 desafios logicos.', '/com/imetro/assets/imgs/logica_ouro.png', 30, 'desafios logicos', 11),
  ('LOGICA_PLATINA', 'LOGICA', 'PLATINA', 'Logica Platina', 'Conquista ao acumular 50 desafios logicos.', '/com/imetro/assets/imgs/logica_platina.png', 50, 'desafios logicos', 12),

  ('RESILIENCIA_BRONZE', 'RESILIENCIA', 'BRONZE', 'Resiliencia Bronze', 'Conquista ao acumular 5 retomas fortes.', '/com/imetro/assets/imgs/resiliencia_bronze.png', 5, 'retomas fortes', 13),
  ('RESILIENCIA_PRATA', 'RESILIENCIA', 'PRATA', 'Resiliencia Prata', 'Conquista ao acumular 15 retomas fortes.', '/com/imetro/assets/imgs/resiliencia_prata.png', 15, 'retomas fortes', 14),
  ('RESILIENCIA_OURO', 'RESILIENCIA', 'OURO', 'Resiliencia Ouro', 'Conquista ao acumular 30 retomas fortes.', '/com/imetro/assets/imgs/resiliencia_ouro.png', 30, 'retomas fortes', 15),
  ('RESILIENCIA_PLATINA', 'RESILIENCIA', 'PLATINA', 'Resiliencia Platina', 'Conquista ao acumular 50 retomas fortes.', '/com/imetro/assets/imgs/resiliencia_platina.png', 50, 'retomas fortes', 16),

  ('CONSISTENCIA_BRONZE', 'CONSISTENCIA', 'BRONZE', 'Consistencia Bronze', 'Conquista ao acumular 5 series consistentes.', '/com/imetro/assets/imgs/consistencia_bronze.png', 5, 'series consistentes', 17),
  ('CONSISTENCIA_PRATA', 'CONSISTENCIA', 'PRATA', 'Consistencia Prata', 'Conquista ao acumular 15 series consistentes.', '/com/imetro/assets/imgs/consistencia_prata.png', 15, 'series consistentes', 18),
  ('CONSISTENCIA_OURO', 'CONSISTENCIA', 'OURO', 'Consistencia Ouro', 'Conquista ao acumular 30 series consistentes.', '/com/imetro/assets/imgs/consistencia_ouro.png', 30, 'series consistentes', 19),
  ('CONSISTENCIA_PLATINA', 'CONSISTENCIA', 'PLATINA', 'Consistencia Platina', 'Conquista ao acumular 50 series consistentes.', '/com/imetro/assets/imgs/consistencia_platina.png', 50, 'series consistentes', 20)
on conflict (codigo) do update
set
  habilidade = excluded.habilidade,
  nivel = excluded.nivel,
  titulo = excluded.titulo,
  descricao = excluded.descricao,
  imagem_ref = excluded.imagem_ref,
  meta_valor = excluded.meta_valor,
  meta_unidade = excluded.meta_unidade,
  ordem = excluded.ordem;

--- ====================================================
--- SEEDS PARA DISCIPLINAS 
--- ====================================================
insert into disciplinas (id, nome, peso, nivel, objectivo) values
  (uuid_generate_v4(), 'Matemática', 1.5, 'INICIANTE', 'Desenvolver raciocínio lógico-matemático e capacidade de resolução de problemas'),
  (uuid_generate_v4(), 'Física', 1.2, 'INICIANTE', 'Desenvolver raciocínio científico e aplicação de conceitos físicos');


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

