alter table if exists perguntas
  add column if not exists rigor double precision not null default 0.5;

alter table if exists perguntas
  add column if not exists referencia_livro text null;

alter table if exists perguntas
  add column if not exists pagina_inicio integer null;

alter table if exists perguntas
  add column if not exists pagina_fim integer null;

alter table if exists perguntas
  add column if not exists topico_principal text null;

update perguntas
set topico_principal = coalesce(nullif(topico_principal, ''), nullif(topico, ''), nullif(subtopico, ''), 'Geral')
where topico_principal is null or topico_principal = '';

create index if not exists idx_perguntas_rigor on perguntas (rigor);
create index if not exists idx_perguntas_topico_rigor on perguntas (topico, rigor);
create index if not exists idx_perguntas_topico_principal on perguntas (topico_principal);

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
  unique (aluno_id, disciplina_id, topico),
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

create index if not exists idx_progressao_rigor_aluno_disciplina
  on progressao_rigor (aluno_id, disciplina_id);

create index if not exists idx_progressao_rigor_topico
  on progressao_rigor (topico);

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

create index if not exists idx_recomendacoes_rigor_diagnostico
  on recomendacoes_rigor (diagnostico_id);

create index if not exists idx_recomendacoes_rigor_topico
  on recomendacoes_rigor (topico);
