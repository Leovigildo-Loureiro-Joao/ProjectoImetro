create table if not exists medalhas_catalogo (
  codigo text primary key,
  habilidade text not null,
  nivel text not null,
  titulo text not null,
  descricao text not null,
  imagem_ref text not null,
  meta_valor integer not null,
  meta_unidade text not null,
  ordem integer not null unique,
  criado_em timestamptz not null default now(),
  constraint medalhas_catalogo_habilidade_chk check (
    habilidade in ('TIME', 'PONTARIA', 'LOGICA', 'RESILIENCIA', 'CONSISTENCIA')
  ),
  constraint medalhas_catalogo_nivel_chk check (
    nivel in ('BRONZE', 'PRATA', 'OURO', 'PLATINA')
  ),
  constraint medalhas_catalogo_meta_chk check (
    meta_valor > 0 and ordem > 0
  )
);

create table if not exists user_medalhas (
  id uuid primary key default uuid_generate_v4(),
  user_id uuid not null references users(id) on delete cascade,
  medalha_codigo text not null references medalhas_catalogo(codigo) on delete cascade,
  progresso_atual integer not null default 0,
  recorde_valor integer null,
  origem text null,
  conquistada_em timestamptz not null default now(),
  atualizado_em timestamptz not null default now(),
  constraint user_medalhas_unique unique (user_id, medalha_codigo),
  constraint user_medalhas_progress_chk check (
    progresso_atual >= 0 and (recorde_valor is null or recorde_valor >= 0)
  )
);

create index if not exists idx_user_medalhas_user_id on user_medalhas (user_id);
create index if not exists idx_user_medalhas_codigo on user_medalhas (medalha_codigo);

insert into medalhas_catalogo (codigo, habilidade, nivel, titulo, descricao, imagem_ref, meta_valor, meta_unidade, ordem)
values
  ('TIME_BRONZE', 'TIME', 'BRONZE', 'Velocidade Bronze', 'Conquista ao acumular 5 testes velozes.', '/com/imetro/assets/imgs/time_bronze.png', 5, 'testes velozes', 1),
  ('TIME_PRATA', 'TIME', 'PRATA', 'Velocidade Prata', 'Conquista ao acumular 15 testes velozes.', '/com/imetro/assets/imgs/time_prata.png', 15, 'testes velozes', 2),
  ('TIME_OURO', 'TIME', 'OURO', 'Velocidade Ouro', 'Conquista ao acumular 30 testes velozes.', '/com/imetro/assets/imgs/time_ouro.png', 30, 'testes velozes', 3),
  ('TIME_PLATINA', 'TIME', 'PLATINA', 'Velocidade Platina', 'Conquista ao acumular 50 testes velozes.', '/com/imetro/assets/imgs/time_platina.png', 50, 'testes velozes', 4),

  ('PONTARIA_BRONZE', 'PONTARIA', 'BRONZE', 'Pontaria Bronze', 'Conquista ao acumular 5 testes precisos.', '/com/imetro/assets/imgs/pontaria_bronze.png', 5, 'testes precisos', 5),
  ('PONTARIA_PRATA', 'PONTARIA', 'PRATA', 'Pontaria Prata', 'Conquista ao acumular 15 testes precisos.', '/com/imetro/assets/imgs/pontaria_prata.png', 15, 'testes precisos', 6),
  ('PONTARIA_OURO', 'PONTARIA', 'OURO', 'Pontaria Ouro', 'Conquista ao acumular 30 testes precisos.', '/com/imetro/assets/imgs/pontaria_ouro.png', 30, 'testes precisos', 7),
  ('PONTARIA_PLATINA', 'PONTARIA', 'PLATINA', 'Pontaria Platina', 'Conquista ao acumular 50 testes precisos.', '/com/imetro/assets/imgs/pontaria_platina.png', 50, 'testes precisos', 8),

  ('LOGICA_BRONZE', 'LOGICA', 'BRONZE', 'Logica Bronze', 'Conquista ao acumular 5 desafios logicos.', '/com/imetro/assets/imgs/logica_bronze.png', 5, 'desafios logicos', 9),
  ('LOGICA_PRATA', 'LOGICA', 'PRATA', 'Logica Prata', 'Conquista ao acumular 15 desafios logicos.', '/com/imetro/assets/imgs/logica_prata.png', 15, 'desafios logicos', 10),
  ('LOGICA_OURO', 'LOGICA', 'OURO', 'Logica Ouro', 'Conquista ao acumular 30 desafios logicos.', '/com/imetro/assets/imgs/logica_ouro.png', 30, 'desafios logicos', 11),
  ('LOGICA_PLATINA', 'LOGICA', 'PLATINA', 'Logica Platina', 'Conquista ao acumular 50 desafios logicos.', '/com/imetro/assets/imgs/logica_platina.png', 50, 'desafios logicos', 12),

  ('RESILIENCIA_BRONZE', 'RESILIENCIA', 'BRONZE', 'Resiliencia Bronze', 'Conquista ao acumular 5 retomas fortes.', '/com/imetro/assets/imgs/resiliencia_bronze.png', 5, 'retomas fortes', 13),
  ('RESILIENCIA_PRATA', 'RESILIENCIA', 'PRATA', 'Resiliencia Prata', 'Conquista ao acumular 15 retomas fortes.', '/com/imetro/assets/imgs/resiliencia_prata.png', 15, 'retomas fortes', 14),
  ('RESILIENCIA_OURO', 'RESILIENCIA', 'OURO', 'Resiliencia Ouro', 'Conquista ao acumular 30 retomas fortes.', '/com/imetro/assets/imgs/resiliencia_ouro.png', 30, 'retomas fortes', 15),
  ('RESILIENCIA_PLATINA', 'RESILIENCIA', 'PLATINA', 'Resiliencia Platina', 'Conquista ao acumular 50 retomas fortes.', '/com/imetro/assets/imgs/resiliencia_platina.png', 50, 'retomas fortes', 16),

  ('CONSISTENCIA_BRONZE', 'CONSISTENCIA', 'BRONZE', 'Consistencia Bronze', 'Conquista ao acumular 5 series consistentes.', '/com/imetro/assets/imgs/consistencia_bronze.png', 5, 'series consistentes', 17),
  ('CONSISTENCIA_PRATA', 'CONSISTENCIA', 'PRATA', 'Consistencia Prata', 'Conquista ao acumular 15 series consistentes.', '/com/imetro/assets/imgs/consistencia_prata.png', 15, 'series consistentes', 18),
  ('CONSISTENCIA_OURO', 'CONSISTENCIA', 'OURO', 'Consistencia Ouro', 'Conquista ao acumular 30 series consistentes.', '/com/imetro/assets/imgs/consistencia_ouro.png', 30, 'series consistentes', 19),
  ('CONSISTENCIA_PLATINA', 'CONSISTENCIA', 'PLATINA', 'Consistencia Platina', 'Conquista ao acumular 50 series consistentes.', '/com/imetro/assets/imgs/consistencia_platina.png', 50, 'series consistentes', 20)
on conflict (codigo) do update
set
  habilidade = excluded.habilidade,
  nivel = excluded.nivel,
  titulo = excluded.titulo,
  descricao = excluded.descricao,
  imagem_ref = excluded.imagem_ref,
  meta_valor = excluded.meta_valor,
  meta_unidade = excluded.meta_unidade,
  ordem = excluded.ordem;
