alter table if exists teste_perguntas
  add column if not exists respondido_em timestamp null;

alter table if exists teste_perguntas
  alter column respondido_em drop not null;
