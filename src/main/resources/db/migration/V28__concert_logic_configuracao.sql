create table if not exists adaptacao (
  id uuid primary key default uuid_generate_v4(),
  user_id uuid not null unique references users(id) on delete cascade,
  tempo_lento_fator double precision not null default 1.25,
  tempo_recuperacao_fator double precision not null default 1.10,
  tempo_adapt integer not null default 30,
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
  constraint adaptacao_tempos_chk check (
    tempo_lento_fator > 0
    and tempo_recuperacao_fator > 0
    and tempo_adapt > 0
  ),
  constraint adaptacao_regras_chk check (
    acertos_subir_rapido >= 1
    and acertos_subir_lento >= 1
    and erros_descer >= 1
    and janela_consistencia >= 1
    and janela_recuperacao >= 1
  ),
  constraint adaptacao_pesos_chk check (
    peso_consistencia_acerto between 0 and 1
    and peso_consistencia_ritmo between 0 and 1
    and peso_resiliencia_recuperacao between 0 and 1
    and peso_resiliencia_estabilidade between 0 and 1
  )
);

insert into adaptacao (
  user_id,
  tempo_lento_fator,
  tempo_recuperacao_fator,
  tempo_adapt,
  acertos_subir_rapido,
  acertos_subir_lento,
  erros_descer,
  janela_consistencia,
  janela_recuperacao,
  peso_consistencia_acerto,
  peso_consistencia_ritmo,
  peso_resiliencia_recuperacao,
  peso_resiliencia_estabilidade
)
select
  users.id,
  1.25,
  1.10,
  30,
  2,
  3,
  2,
  3,
  2,
  0.70,
  0.30,
  0.70,
  0.30
from users
left join adaptacao
  on adaptacao.user_id = users.id
where adaptacao.user_id is null;

alter table if exists configuracoes
  add column if not exists curto_test_q integer;

update configuracoes
set curto_test_q = coalesce(curto_test_q, long_test_q, norm_test_q, 5)
where curto_test_q is null;

alter table if exists configuracoes
  alter column curto_test_q set default 5;

update configuracoes
set curto_test_q = 5
where curto_test_q is null;

alter table if exists configuracoes
  alter column curto_test_q set not null;

alter table if exists configuracoes
  drop column if exists extra_test_q;

alter table if exists configuracoes
  drop column if exists desaf_test_q;
