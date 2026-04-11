-- Disciplinas e relações do onboarding (candidato/orientador)
-- Inclui campos: nome, peso, nivel, objectivo.

create table if not exists disciplinas (
  id uuid primary key default uuid_generate_v4(),
  nome text not null unique,
  peso double precision not null default 1.0,
  nivel text not null default 'BASICO',
  objectivo text null
);

-- Se a tabela já existir (ou foi criada manualmente), garante colunas e constraints.
alter table if exists disciplinas
  add column if not exists peso double precision;
alter table if exists disciplinas
  add column if not exists nivel text;
alter table if exists disciplinas
  add column if not exists objectivo text;

do $$
begin
  if to_regclass('public.disciplinas') is not null then
    alter table disciplinas alter column peso set default 1.0;
    update disciplinas set peso = 1.0 where peso is null;
    alter table disciplinas alter column peso set not null;

    alter table disciplinas alter column nivel set default 'BASICO';
    update disciplinas set nivel = 'BASICO' where nivel is null or btrim(nivel) = '';
    alter table disciplinas alter column nivel set not null;
  end if;
end $$;

create table if not exists candidato_disciplinas (
  candidato_id uuid not null references users(id) on delete cascade,
  disciplina_id uuid not null references disciplinas(id) on delete cascade,
  primary key (candidato_id, disciplina_id)
);

create table if not exists orientador_disciplinas (
  orientador_id uuid not null references users(id) on delete cascade,
  disciplina_id uuid not null references disciplinas(id) on delete cascade,
  primary key (orientador_id, disciplina_id)
);

create index if not exists idx_candidato_disciplinas_candidato_id on candidato_disciplinas (candidato_id);
create index if not exists idx_orientador_disciplinas_orientador_id on orientador_disciplinas (orientador_id);
