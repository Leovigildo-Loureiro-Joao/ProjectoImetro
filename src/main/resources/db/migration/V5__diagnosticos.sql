-- Persistencia dos diagnosticos academicos do candidato.
-- Estrutura alinhada com a UI atual: disciplina, pontuacao, duracao e metricas.

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
