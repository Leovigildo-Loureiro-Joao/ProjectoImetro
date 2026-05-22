alter table if exists bolsas
  add column if not exists disciplina_foco text null;

alter table if exists bolsas
  add column if not exists duracao_minutos integer not null default 45;

alter table if exists bolsas
  add column if not exists criterio_medalhas_min integer not null default 1;

alter table if exists bolsas
  add column if not exists criterio_desempenho_min integer not null default 60;

alter table if exists bolsas
  add column if not exists criterio_evolucao_min integer not null default 55;

alter table if exists bolsas
  add column if not exists criterio_precisao_min integer not null default 60;

alter table if exists bolsas
  add column if not exists criterio_velocidade_min integer not null default 50;

alter table if exists bolsas
  add column if not exists abertura_dia_semana integer not null default 1;

alter table if exists bolsas
  add column if not exists fechamento_dia_semana integer not null default 2;

alter table if exists bolsas
  add column if not exists modo_resposta text not null default 'TEXTFIELD';

alter table if exists bolsas
  add column if not exists ativa boolean not null default true;

alter table if exists bolsas
  add constraint bolsas_duracao_minutos_chk
  check (duracao_minutos between 10 and 180);

alter table if exists bolsas
  add constraint bolsas_medalhas_chk
  check (criterio_medalhas_min between 0 and 20);

alter table if exists bolsas
  add constraint bolsas_percentuais_chk
  check (
    criterio_desempenho_min between 0 and 100
    and criterio_evolucao_min between 0 and 100
    and criterio_precisao_min between 0 and 100
    and criterio_velocidade_min between 0 and 100
  );

alter table if exists bolsas
  add constraint bolsas_dias_semana_chk
  check (
    abertura_dia_semana between 1 and 7
    and fechamento_dia_semana between 1 and 7
  );

alter table if exists bolsas
  add constraint bolsas_modo_resposta_chk
  check (modo_resposta in ('TEXTFIELD', 'MISTO'));

update bolsas
set
  disciplina_foco = case
    when nome = 'Bolsa Merito Atlas' then 'Matematica'
    when nome = 'Programa Horizonte STEM' then 'Fisica'
    when nome = 'Fundo Impulso Academico' then 'Quimica'
    when nome = 'Beca Impacto Local' then 'Raciocinio Logico'
    else coalesce(disciplina_foco, 'Matematica')
  end,
  duracao_minutos = case
    when nome = 'Bolsa Merito Atlas' then 45
    when nome = 'Programa Horizonte STEM' then 50
    when nome = 'Fundo Impulso Academico' then 40
    when nome = 'Beca Impacto Local' then 35
    else duracao_minutos
  end,
  criterio_medalhas_min = case
    when nome = 'Bolsa Merito Atlas' then 1
    when nome = 'Programa Horizonte STEM' then 2
    when nome = 'Fundo Impulso Academico' then 1
    when nome = 'Beca Impacto Local' then 1
    else criterio_medalhas_min
  end,
  criterio_desempenho_min = case
    when nome = 'Bolsa Merito Atlas' then 68
    when nome = 'Programa Horizonte STEM' then 74
    when nome = 'Fundo Impulso Academico' then 62
    when nome = 'Beca Impacto Local' then 58
    else criterio_desempenho_min
  end,
  criterio_evolucao_min = case
    when nome = 'Bolsa Merito Atlas' then 64
    when nome = 'Programa Horizonte STEM' then 68
    when nome = 'Fundo Impulso Academico' then 58
    when nome = 'Beca Impacto Local' then 55
    else criterio_evolucao_min
  end,
  criterio_precisao_min = case
    when nome = 'Bolsa Merito Atlas' then 70
    when nome = 'Programa Horizonte STEM' then 76
    when nome = 'Fundo Impulso Academico' then 64
    when nome = 'Beca Impacto Local' then 60
    else criterio_precisao_min
  end,
  criterio_velocidade_min = case
    when nome = 'Bolsa Merito Atlas' then 58
    when nome = 'Programa Horizonte STEM' then 64
    when nome = 'Fundo Impulso Academico' then 52
    when nome = 'Beca Impacto Local' then 50
    else criterio_velocidade_min
  end,
  abertura_dia_semana = 1,
  fechamento_dia_semana = 2,
  modo_resposta = 'TEXTFIELD',
  ativa = true
where nome in (
  'Bolsa Merito Atlas',
  'Programa Horizonte STEM',
  'Fundo Impulso Academico',
  'Beca Impacto Local'
);

alter table if exists testes
  add column if not exists origem text not null default 'TESTE';

alter table if exists testes
  add column if not exists bolsa_id uuid null references bolsas(id) on delete set null;

create index if not exists idx_testes_origem on testes (origem);
create index if not exists idx_testes_bolsa_id on testes (bolsa_id);

alter table if exists score_bolsas
  add column if not exists teste_id uuid null references testes(id) on delete set null;

alter table if exists score_bolsas
  add column if not exists semana_ref date not null default current_date;

alter table if exists score_bolsas
  add column if not exists total_questoes integer not null default 0;

alter table if exists score_bolsas
  add column if not exists total_acertos integer not null default 0;

alter table if exists score_bolsas
  add column if not exists percentual_acerto double precision not null default 0;

alter table if exists score_bolsas
  add column if not exists tempo_total_segundos integer not null default 0;

alter table if exists score_bolsas
  add column if not exists elegivel boolean not null default true;

alter table if exists score_bolsas
  add column if not exists criterios_json jsonb not null default '{}'::jsonb;

alter table if exists score_bolsas
  add column if not exists criado_em timestamptz not null default now();

alter table if exists score_bolsas
  add column if not exists atualizado_em timestamptz not null default now();

create unique index if not exists idx_score_bolsas_unico_semana
  on score_bolsas (candidato_id, bolsa_id, semana_ref);

create index if not exists idx_score_bolsas_teste_id on score_bolsas (teste_id);
create index if not exists idx_score_bolsas_semana_ref on score_bolsas (semana_ref);
