create table if not exists trilho_leitura (
  id uuid primary key default uuid_generate_v4(),
  aluno_id uuid not null references users(id) on delete cascade,
  disciplina_id uuid not null references disciplinas(id) on delete cascade,
  livro_id uuid not null references biblioteca_livros(id) on delete cascade,
  ordem integer not null default 0,
  pagina_inicio integer not null,
  pagina_fim integer not null,
  topico text not null default '',
  subtopico text not null default '',
  estado text not null default 'PENDENTE',
  data_conclusao timestamptz null,
  criado_em timestamptz not null default now(),
  atualizado_em timestamptz not null default now(),
  constraint trilho_leitura_estado_chk check (estado in ('PENDENTE', 'A_LER', 'LIDO')),
  constraint trilho_leitura_paginas_chk check (
    pagina_inicio >= 0
    and pagina_fim >= pagina_inicio
  )
);

create index if not exists idx_trilho_leitura_aluno on trilho_leitura (aluno_id);
create index if not exists idx_trilho_leitura_disciplina on trilho_leitura (disciplina_id);
create index if not exists idx_trilho_leitura_livro on trilho_leitura (livro_id);
create index if not exists idx_trilho_leitura_estado on trilho_leitura (aluno_id, estado);
