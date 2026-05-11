create table if not exists configuracoes_teste_adaptativo (
  id uuid primary key default uuid_generate_v4(),
  codigo text not null unique,
  descricao text null,
  ativo boolean not null default false,
  tempo_lento_fator double precision not null default 1.25,
  tempo_recuperacao_fator double precision not null default 1.10,
  acertos_subir_rapido integer not null default 2,
  acertos_subir_lento integer not null default 3,
  erros_descer integer not null default 2,
  janela_consistencia integer not null default 3,
  janela_recuperacao integer not null default 2,
  peso_consistencia_acerto double precision not null default 0.70,
  peso_consistencia_ritmo double precision not null default 0.30,
  peso_resiliencia_recuperacao double precision not null default 0.70,
  peso_resiliencia_estabilidade double precision not null default 0.30,
  criado_em timestamptz not null default now(),
  atualizado_em timestamptz not null default now(),
  constraint configuracoes_teste_adaptativo_tempos_chk check (
    tempo_lento_fator > 0
    and tempo_recuperacao_fator > 0
  ),
  constraint configuracoes_teste_adaptativo_regras_chk check (
    acertos_subir_rapido >= 1
    and acertos_subir_lento >= 1
    and erros_descer >= 1
    and janela_consistencia >= 1
    and janela_recuperacao >= 1
  ),
  constraint configuracoes_teste_adaptativo_pesos_chk check (
    peso_consistencia_acerto between 0 and 1
    and peso_consistencia_ritmo between 0 and 1
    and peso_resiliencia_recuperacao between 0 and 1
    and peso_resiliencia_estabilidade between 0 and 1
  )
);

create unique index if not exists idx_configuracoes_teste_adaptativo_ativo
  on configuracoes_teste_adaptativo ((1))
  where ativo;

create table if not exists configuracoes_teste_adaptativo_niveis (
  id uuid primary key default uuid_generate_v4(),
  configuracao_id uuid not null references configuracoes_teste_adaptativo(id) on delete cascade,
  nivel integer not null,
  codigo text not null,
  tempo_sugerido_segundos real not null,
  rigor_base double precision not null,
  limite_inferior double precision not null,
  limite_superior double precision not null,
  criado_em timestamptz not null default now(),
  atualizado_em timestamptz not null default now(),
  constraint configuracoes_teste_adaptativo_niveis_unique_nivel unique (configuracao_id, nivel),
  constraint configuracoes_teste_adaptativo_niveis_unique_codigo unique (configuracao_id, codigo),
  constraint configuracoes_teste_adaptativo_niveis_chk check (
    nivel between 1 and 4
    and codigo in ('FACIL', 'MEDIO', 'DIFICIL', 'EXPERT')
    and tempo_sugerido_segundos > 0
    and rigor_base between 0 and 1
    and limite_inferior between 0 and 1
    and limite_superior between 0 and 1
    and limite_inferior <= limite_superior
  )
);

create index if not exists idx_configuracoes_teste_adaptativo_niveis_configuracao
  on configuracoes_teste_adaptativo_niveis (configuracao_id, nivel);

create table if not exists configuracoes_teste_adaptativo_duracoes (
  id uuid primary key default uuid_generate_v4(),
  configuracao_id uuid not null references configuracoes_teste_adaptativo(id) on delete cascade,
  codigo text not null,
  descricao text null,
  limite_questoes integer not null,
  criado_em timestamptz not null default now(),
  atualizado_em timestamptz not null default now(),
  constraint configuracoes_teste_adaptativo_duracoes_unique unique (configuracao_id, codigo),
  constraint configuracoes_teste_adaptativo_duracoes_chk check (
    codigo in ('CURTO', 'MEDIO', 'LONGO')
    and limite_questoes > 0
  )
);

create index if not exists idx_configuracoes_teste_adaptativo_duracoes_configuracao
  on configuracoes_teste_adaptativo_duracoes (configuracao_id, codigo);

alter table if exists testes
  add column if not exists configuracao_teste_adaptativo_id uuid null
  references configuracoes_teste_adaptativo(id) on delete set null;

create index if not exists idx_testes_configuracao_teste_adaptativo_id
  on testes (configuracao_teste_adaptativo_id);

insert into configuracoes_teste_adaptativo (
  codigo,
  descricao,
  ativo,
  tempo_lento_fator,
  tempo_recuperacao_fator,
  acertos_subir_rapido,
  acertos_subir_lento,
  erros_descer,
  janela_consistencia,
  janela_recuperacao,
  peso_consistencia_acerto,
  peso_consistencia_ritmo,
  peso_resiliencia_recuperacao,
  peso_resiliencia_estabilidade
) values (
  'PADRAO_V1',
  'Perfil inicial do motor adaptativo centralizado na base, alinhado aos hardcodes atuais do projeto.',
  true,
  1.25,
  1.10,
  2,
  3,
  2,
  3,
  2,
  0.70,
  0.30,
  0.70,
  0.30
)
on conflict (codigo) do update
set descricao = excluded.descricao,
    tempo_lento_fator = excluded.tempo_lento_fator,
    tempo_recuperacao_fator = excluded.tempo_recuperacao_fator,
    acertos_subir_rapido = excluded.acertos_subir_rapido,
    acertos_subir_lento = excluded.acertos_subir_lento,
    erros_descer = excluded.erros_descer,
    janela_consistencia = excluded.janela_consistencia,
    janela_recuperacao = excluded.janela_recuperacao,
    peso_consistencia_acerto = excluded.peso_consistencia_acerto,
    peso_consistencia_ritmo = excluded.peso_consistencia_ritmo,
    peso_resiliencia_recuperacao = excluded.peso_resiliencia_recuperacao,
    peso_resiliencia_estabilidade = excluded.peso_resiliencia_estabilidade,
    atualizado_em = now();

insert into configuracoes_teste_adaptativo_niveis (
  configuracao_id,
  nivel,
  codigo,
  tempo_sugerido_segundos,
  rigor_base,
  limite_inferior,
  limite_superior
)
select
  configuracao.id,
  valores.nivel,
  valores.codigo,
  valores.tempo_sugerido_segundos,
  valores.rigor_base,
  valores.limite_inferior,
  valores.limite_superior
from configuracoes_teste_adaptativo configuracao
join (
  values
    (1, 'FACIL', 40::real, 0.18::double precision, 0.05::double precision, 0.30::double precision),
    (2, 'MEDIO', 55::real, 0.35::double precision, 0.20::double precision, 0.50::double precision),
    (3, 'DIFICIL', 70::real, 0.58::double precision, 0.45::double precision, 0.72::double precision),
    (4, 'EXPERT', 85::real, 0.78::double precision, 0.65::double precision, 0.92::double precision)
) as valores (
  nivel,
  codigo,
  tempo_sugerido_segundos,
  rigor_base,
  limite_inferior,
  limite_superior
)
  on true
where configuracao.codigo = 'PADRAO_V1'
on conflict (configuracao_id, nivel) do update
set codigo = excluded.codigo,
    tempo_sugerido_segundos = excluded.tempo_sugerido_segundos,
    rigor_base = excluded.rigor_base,
    limite_inferior = excluded.limite_inferior,
    limite_superior = excluded.limite_superior,
    atualizado_em = now();

insert into configuracoes_teste_adaptativo_duracoes (
  configuracao_id,
  codigo,
  descricao,
  limite_questoes
)
select
  configuracao.id,
  valores.codigo,
  valores.descricao,
  valores.limite_questoes
from configuracoes_teste_adaptativo configuracao
join (
  values
    ('CURTO', 'Perfil curto do teste adaptativo.', 5),
    ('MEDIO', 'Perfil medio do teste adaptativo.', 7),
    ('LONGO', 'Perfil longo do teste adaptativo.', 10)
) as valores (
  codigo,
  descricao,
  limite_questoes
)
  on true
where configuracao.codigo = 'PADRAO_V1'
on conflict (configuracao_id, codigo) do update
set descricao = excluded.descricao,
    limite_questoes = excluded.limite_questoes,
    atualizado_em = now();
