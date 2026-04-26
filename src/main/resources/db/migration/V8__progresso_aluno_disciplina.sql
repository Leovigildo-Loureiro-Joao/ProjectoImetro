-- Historico de progresso do aluno por disciplina.
-- Esta migration fecha a lacuna entre o historico Flyway e o schema base
-- definido em `scripts/db/001_schema.sql`.

create table if not exists progresso_aluno_disciplina (
  id uuid primary key default uuid_generate_v4(),
  aluno_id uuid not null references users(id) on delete cascade,
  disciplina_id uuid not null references disciplinas(id) on delete cascade,
  nivel_atual text not null default 'INICIANTE',
  nivel_anterior text null,
  data_mudanca_nivel timestamptz null,
  peso_atual double precision not null default 1.0,
  total_questoes_resolvidas integer not null default 0,
  total_acertos integer not null default 0,
  total_erros integer not null default 0,
  taxa_acerto_geral double precision null,
  ultimos_3_diagnosticos_acertos integer[] default '{}',
  ultimos_3_diagnosticos_total integer[] default '{}',
  ultimo_estudo timestamptz null,
  dias_sem_estudo integer null,
  streak_dias_consecutivos integer not null default 0,
  criado_em timestamptz not null default now(),
  atualizado_em timestamptz not null default now(),
  unique (aluno_id, disciplina_id),
  constraint progresso_aluno_disciplina_nivel_chk check (
    nivel_atual in ('INICIANTE', 'INTERMEDIARIO', 'AVANCADO', 'EXPERT')
  ),
  constraint progresso_aluno_disciplina_peso_chk check (
    peso_atual > 0
  )
);

create index if not exists idx_progresso_aluno_disciplinas_aluno_id
  on progresso_aluno_disciplina (aluno_id);

create index if not exists idx_progresso_aluno_disciplinas_disciplina_id
  on progresso_aluno_disciplina (disciplina_id);
