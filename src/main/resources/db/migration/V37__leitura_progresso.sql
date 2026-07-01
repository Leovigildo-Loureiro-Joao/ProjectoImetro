create table if not exists leitura_progresso (
  id uuid primary key default uuid_generate_v4(),
  aluno_id uuid not null references users(id) on delete cascade,
  livro_id uuid not null references biblioteca_livros(id) on delete cascade,
  pagina_atual integer not null default 0,
  total_paginas integer not null default 0,
  paginas_lidas integer[] not null default '{}',
  estado text not null default 'NAO_INICIADO',
  sessoes_leitura jsonb not null default '[]'::jsonb,
  criado_em timestamptz not null default now(),
  atualizado_em timestamptz not null default now(),
  constraint uq_leitura_progresso_aluno_livro unique (aluno_id, livro_id),
  constraint leitura_progresso_estado_chk check (estado in ('NAO_INICIADO', 'EM_LEITURA', 'CONCLUIDO')),
  constraint leitura_progresso_paginas_chk check (
    pagina_atual >= 0
    and total_paginas >= 0
  )
);

create index if not exists idx_leitura_progresso_aluno on leitura_progresso (aluno_id);
create index if not exists idx_leitura_progresso_livro on leitura_progresso (livro_id);
create index if not exists idx_leitura_progresso_estado on leitura_progresso (aluno_id, estado);
