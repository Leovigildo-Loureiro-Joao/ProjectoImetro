create table if not exists livro_mapa_topicos (
  id uuid primary key default uuid_generate_v4(),
  livro_id uuid not null references biblioteca_livros(id) on delete cascade,
  topico text not null,
  subtopico text not null,
  pagina_inicio integer not null default 0,
  pagina_fim integer not null default 0,
  criado_em timestamptz not null default now(),
  constraint uq_livro_mapa_topicos unique (livro_id, topico, subtopico),
  constraint livro_mapa_topicos_paginas_chk check (
    pagina_inicio >= 0
    and pagina_fim >= pagina_inicio
  )
);

create index if not exists idx_livro_mapa_topicos_livro on livro_mapa_topicos (livro_id);
create index if not exists idx_livro_mapa_topicos_topico on livro_mapa_topicos (livro_id, topico);
