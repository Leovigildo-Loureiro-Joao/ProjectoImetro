create table if not exists planeamentos_estudo (
  id uuid primary key default uuid_generate_v4(),
  candidato_id uuid not null references users(id) on delete cascade,
  semana_inicio date not null,
  semana_fim date not null,
  assinatura_fonte text not null,
  pontuacao_hero double precision not null default 0,
  resumo_hero text not null default '',
  acerto_medio text not null default '',
  ritmo_medio text not null default '',
  consistencia_media text not null default '',
  foco_atual text not null default '',
  resumo_json jsonb not null default '{}'::jsonb,
  criado_em timestamptz not null default now(),
  atualizado_em timestamptz not null default now(),
  constraint planeamentos_estudo_semana_chk check (semana_fim >= semana_inicio),
  constraint planeamentos_estudo_pontuacao_chk check (pontuacao_hero between 0 and 100),
  constraint planeamentos_estudo_unico_semana unique (candidato_id, semana_inicio)
);

create index if not exists idx_planeamentos_estudo_candidato_id
  on planeamentos_estudo (candidato_id);

create index if not exists idx_planeamentos_estudo_semana_inicio
  on planeamentos_estudo (semana_inicio desc);

create index if not exists idx_planeamentos_estudo_atualizado_em
  on planeamentos_estudo (atualizado_em desc);
