alter table if exists perguntas
  add column if not exists usa_grafico boolean not null default false;

alter table if exists perguntas
  add column if not exists grafico_tipo_curva text null;

alter table if exists perguntas
  add column if not exists grafico_a double precision null;

alter table if exists perguntas
  add column if not exists grafico_b double precision null;

alter table if exists perguntas
  add column if not exists grafico_c double precision null;

alter table if exists perguntas
  add column if not exists grafico_eixo_x text null;

alter table if exists perguntas
  add column if not exists grafico_eixo_y text null;

alter table if exists perguntas
  add column if not exists grafico_x_min double precision null;

alter table if exists perguntas
  add column if not exists grafico_x_max double precision null;

alter table if exists perguntas
  add column if not exists grafico_x_tick_unit double precision null;

create index if not exists idx_perguntas_usa_grafico on perguntas (usa_grafico);
