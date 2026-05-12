alter table if exists teste_perguntas
  add column if not exists topico varchar NOT NULL;

alter table if exists teste_perguntas
  add column if not exists subtopico varchar NOT NULL;

alter table if exists teste_perguntas
  add column if not exists enunciado text NOT NULL;

alter table if exists teste_perguntas
  add column if not exists resposta_dada text NOT NULL;

alter table if exists teste_perguntas
  add column if not exists resposta_correta text NOT NULL;

alter table if exists teste_perguntas
  add column if not exists resposta_dada_texto text NOT NULL;

alter table if exists teste_perguntas
  add column if not exists resposta_correta_texto text NOT NULL;

alter table if exists teste_perguntas
  add column if not exists disciplina_nome varchar NOT NULL;

alter table if exists teste_perguntas
  add column if not exists tempo_sugerido_segundos real NOT NULL;

alter table if exists teste_perguntas
  add column if not exists nivel_dificuldade real NOT null;

alter table if exists teste_perguntas
  add column if not exists rigor real NOT null;

alter table if exists teste_perguntas
  add column if not exists referencia_livro VARCHAR NOT null;

alter table if exists teste_perguntas
  add column if not exists pagina_inicio INTEGER NOT null;

alter table if exists teste_perguntas
  add column if not exists pagina_fim INTEGER NOT null;

