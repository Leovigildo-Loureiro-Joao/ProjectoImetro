alter table if exists testes
  add column if not exists diagnostico_id uuid null references diagnosticos(id) on delete set null;

alter table if exists testes
  add column if not exists disciplina_id uuid null references disciplinas(id) on delete set null;

alter table if exists testes
  add column if not exists disciplina_nome text null;

alter table if exists testes
  add column if not exists nivel_inicial text null;

alter table if exists testes
  add column if not exists nivel_final text null;

alter table if exists testes
  add column if not exists limite_questoes integer null;

alter table if exists testes
  add column if not exists limite_inferior double precision null;

alter table if exists testes
  add column if not exists limite_superior double precision null;

alter table if exists testes
  add column if not exists topicos jsonb not null default '[]'::jsonb;

alter table if exists testes
  add column if not exists subtopicos jsonb not null default '[]'::jsonb;

alter table if exists testes
  add column if not exists duracao_segundos integer not null default 0;

alter table if exists testes
  add column if not exists total_questoes integer not null default 0;

alter table if exists testes
  add column if not exists total_acertos integer not null default 0;

alter table if exists testes
  add column if not exists total_erros integer not null default 0;

alter table if exists testes
  add column if not exists percentual_acerto double precision null;

alter table if exists testes
  add column if not exists velocidade real null;

alter table if exists testes
  add column if not exists precisao real null;

alter table if exists testes
  add column if not exists consistencia real null;

alter table if exists testes
  add column if not exists logica real null;

alter table if exists testes
  add column if not exists resiliencia real null;

alter table if exists testes
  add column if not exists observacoes text null;

alter table if exists testes
  add column if not exists atualizado_em timestamptz not null default now();

alter table if exists teste_perguntas
  add column if not exists acertou boolean null;

alter table if exists teste_perguntas
  add column if not exists consistencia real null;

alter table if exists teste_perguntas
  add column if not exists resiliencia real null;

create table if not exists stats (
  id uuid primary key default uuid_generate_v4(),
  teste_id uuid not null references testes(id) on delete cascade,
  diagnostico_id uuid null references diagnosticos(id) on delete set null,
  candidato_id uuid not null references users(id) on delete cascade,
  disciplina_id uuid null references disciplinas(id) on delete set null,
  disciplina_nome text null,
  origem text not null default 'TESTE',
  tempo_total_segundos integer not null default 0,
  tempo_medio_segundos double precision null,
  total_questoes integer not null default 0,
  total_acertos integer not null default 0,
  total_erros integer not null default 0,
  percentual_acerto double precision null,
  velocidade real null,
  precisao real null,
  consistencia real null,
  logica real null,
  resiliencia real null,
  erros_comuns jsonb not null default '[]'::jsonb,
  melhorias jsonb not null default '[]'::jsonb,
  observacoes text null,
  criado_em timestamptz not null default now(),
  atualizado_em timestamptz not null default now(),
  constraint stats_teste_id_key unique (teste_id),
  constraint stats_origem_chk check (origem in ('TESTE', 'DIAGNOSTICO', 'MISTO')),
  constraint stats_totais_chk check (
    tempo_total_segundos >= 0
    and (tempo_medio_segundos is null or tempo_medio_segundos >= 0)
    and total_questoes >= 0
    and total_acertos >= 0
    and total_erros >= 0
    and total_acertos + total_erros <= total_questoes
  ),
  constraint stats_percentuais_chk check (
    percentual_acerto is null or percentual_acerto between 0 and 100
  ),
  constraint stats_metricas_chk check (
    (velocidade is null or velocidade between 0 and 1)
    and (precisao is null or precisao between 0 and 1)
    and (consistencia is null or consistencia between 0 and 1)
    and (logica is null or logica between 0 and 1)
    and (resiliencia is null or resiliencia between 0 and 1)
  )
);

create index if not exists idx_testes_diagnostico_id on testes (diagnostico_id);
create index if not exists idx_testes_disciplina_id on testes (disciplina_id);
create index if not exists idx_stats_candidato_id on stats (candidato_id);
create index if not exists idx_stats_disciplina_id on stats (disciplina_id);
create index if not exists idx_stats_criado_em on stats (criado_em desc);

do $$
begin
  if not exists (
    select 1
    from pg_constraint
    where conname = 'testes_totais_chk'
  ) then
    alter table testes
      add constraint testes_totais_chk check (
        duracao_segundos >= 0
        and (limite_questoes is null or limite_questoes >= 0)
        and total_questoes >= 0
        and total_acertos >= 0
        and total_erros >= 0
        and total_acertos + total_erros <= total_questoes
      );
  end if;
end $$;

do $$
begin
  if not exists (
    select 1
    from pg_constraint
    where conname = 'testes_percentuais_chk'
  ) then
    alter table testes
      add constraint testes_percentuais_chk check (
        percentual_acerto is null or percentual_acerto between 0 and 100
      );
  end if;
end $$;

do $$
begin
  if not exists (
    select 1
    from pg_constraint
    where conname = 'testes_metricas_chk'
  ) then
    alter table testes
      add constraint testes_metricas_chk check (
        (limite_inferior is null or limite_inferior between 0 and 1)
        and (limite_superior is null or limite_superior between 0 and 1)
        and (
          limite_inferior is null
          or limite_superior is null
          or limite_inferior <= limite_superior
        )
        and (velocidade is null or velocidade between 0 and 1)
        and (precisao is null or precisao between 0 and 1)
        and (consistencia is null or consistencia between 0 and 1)
        and (logica is null or logica between 0 and 1)
        and (resiliencia is null or resiliencia between 0 and 1)
      );
  end if;
end $$;

do $$
begin
  if not exists (
    select 1
    from pg_constraint
    where conname = 'teste_perguntas_metricas_chk'
  ) then
    alter table teste_perguntas
      add constraint teste_perguntas_metricas_chk check (
        (tempo_segundos is null or tempo_segundos >= 0)
        and (precisao is null or precisao between 0 and 1)
        and (velocidade is null or velocidade between 0 and 1)
        and (consistencia is null or consistencia between 0 and 1)
        and (resiliencia is null or resiliencia between 0 and 1)
      );
  end if;
end $$;
