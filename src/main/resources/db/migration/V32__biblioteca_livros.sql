create table if not exists biblioteca_livros (
  id uuid primary key default uuid_generate_v4(),
  disciplina_id uuid not null references disciplinas(id) on delete cascade,
  titulo text not null,
  nome_arquivo text not null,
  mime_type text not null default 'application/pdf',
  tamanho_bytes bigint not null default 0,
  checksum_sha256 text not null,
  source_path text null,
  conteudo_pdf bytea not null,
  ativo boolean not null default true,
  criado_em timestamptz not null default now(),
  atualizado_em timestamptz not null default now(),
  constraint uq_biblioteca_livros_disciplina_checksum unique (disciplina_id, checksum_sha256)
);

create table if not exists biblioteca_livro_paginas (
  id uuid primary key default uuid_generate_v4(),
  livro_id uuid not null references biblioteca_livros(id) on delete cascade,
  pagina_numero integer not null,
  texto_pagina text not null default '',
  criado_em timestamptz not null default now(),
  constraint uq_biblioteca_livro_paginas unique (livro_id, pagina_numero)
);
