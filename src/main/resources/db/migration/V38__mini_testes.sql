create table if not exists mini_testes (
  id uuid primary key default uuid_generate_v4(),
  livro_id uuid not null references biblioteca_livros(id) on delete cascade,
  pagina_inicio integer not null,
  pagina_fim integer not null,
  questoes jsonb not null default '[]'::jsonb,
  checksum_conteudo text null,
  criado_em timestamptz not null default now(),
  constraint mini_testes_paginas_chk check (
    pagina_inicio >= 0
    and pagina_fim >= pagina_inicio
  )
);

create index if not exists idx_mini_testes_livro on mini_testes (livro_id);
create index if not exists idx_mini_testes_livro_paginas on mini_testes (livro_id, pagina_inicio, pagina_fim);
