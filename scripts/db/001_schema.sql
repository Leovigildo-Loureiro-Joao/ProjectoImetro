-- Schema base (PostgreSQL/Supabase) sem ORM/Spring.
-- Este script cria apenas a estrutura de dados (tabelas/índices) do MVP.

create extension if not exists "uuid-ossp";

-- Unifica candidatos/orientadores em uma única tabela `users` com coluna `role`.
create table if not exists users (
  id uuid primary key default uuid_generate_v4(),
  nome text not null,
  email text not null unique,
  senha_hash text null,
  role text not null,
  avatar_url text null,
  constraint users_role_chk check (role in ('CANDIDATO', 'ORIENTADOR')),
  criado_em timestamptz not null default now()
);

-- Relatórios com campos analíticos em JSONB (flexível para MVP).
create table if not exists relatorios (
  id uuid primary key default uuid_generate_v4(),
  candidato_id uuid null references users(id) on delete set null,
  orientador_id uuid null references users(id) on delete set null,
  gerado_em timestamptz not null default now(),
  titulo text null,
  resumo text null,
  tempo_medio_segundos double precision null,
  dificuldade_atingida text null,
  taxa_acerto_por_topico jsonb not null default '{}'::jsonb,
  evolucao_semanal jsonb not null default '[]'::jsonb,
  erros_recorrentes jsonb not null default '[]'::jsonb,
  skills_boas jsonb not null default '[]'::jsonb,
  skills_fracas jsonb not null default '[]'::jsonb,
  recomendacoes_sugeridas jsonb not null default '[]'::jsonb,
  recomendacoes_validadas jsonb not null default '[]'::jsonb,
  nota_orientador text null
);

create index if not exists idx_relatorios_candidato_id on relatorios (candidato_id);
create index if not exists idx_relatorios_orientador_id on relatorios (orientador_id);

-- Banco de questões (MVP)
create table if not exists perguntas (
  id uuid primary key default uuid_generate_v4(),
  questao text not null,
  -- alternativas/respostas possíveis
  respostas jsonb not null default '[]'::jsonb,
  -- opcional: resposta correta (quando aplicável)
  resposta_correta text null,
  topico text null,
  dificuldade text null,
  criado_em timestamptz not null default now()
);

create index if not exists idx_perguntas_topico on perguntas (topico);

-- Sessões/testes/simulados
create table if not exists testes (
  id uuid primary key default uuid_generate_v4(),
  candidato_id uuid null references users(id) on delete set null,
  orientador_id uuid null references users(id) on delete set null,
  relatorio_id uuid null references relatorios(id) on delete set null,
  data_teste timestamptz null,
  resultado real null,
  criado_em timestamptz not null default now()
);

create index if not exists idx_testes_candidato_id on testes (candidato_id);
create index if not exists idx_testes_orientador_id on testes (orientador_id);
create index if not exists idx_testes_relatorio_id on testes (relatorio_id);

-- Perguntas de um teste (com ordem e resposta do candidato)
create table if not exists teste_perguntas (
  teste_id uuid not null references testes(id) on delete cascade,
  pergunta_id uuid not null references perguntas(id) on delete restrict,
  ordem integer not null,
  resposta_dada text null,
  tempo_segundos integer null,
  precisao real null,
  velocidade real null,
  primary key (teste_id, ordem)
);

create index if not exists idx_teste_perguntas_pergunta_id on teste_perguntas (pergunta_id);

-- Disciplinas + preferências do onboarding (MVP).
create table if not exists disciplinas (
  id uuid primary key default uuid_generate_v4(),
  nome text not null unique
);

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
