alter table if exists perguntas
  add column if not exists pesos_resposta jsonb not null default '[]'::jsonb;
