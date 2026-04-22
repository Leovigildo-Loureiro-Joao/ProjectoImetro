-- Configuracoes por utilizador (preferencias do simulador/diagnostico).
-- Mantem defaults e thresholds fora do codigo para facilitar ajustes.

create table if not exists configuracoes (
  id uuid primary key default uuid_generate_v4(),
  user_id uuid not null references users(id) on delete cascade,

  temp_adapt_val integer null,
  temp_adapt_unit text null,
  speed_temp_val integer null,
  speed_temp_unit text null,
  long_test_q integer null,
  norm_test_q integer null,
  desaf_test_q integer null,
  extra_test_q integer null,
  nivel_dificuldade_padrao text null,
  modo_escolhas text null,
  velocidade_segundos_por_percent integer null,
  resiliencia_repeticoes_por_dia integer null,
  precisao_consecutivas integer null,
  logica_qtd_desafiante_extra integer null,
  consistencia_percentual_min double precision null,

  criado_em timestamptz not null default now(),
  atualizado_em timestamptz not null default now(),

  constraint configuracoes_user_unique unique (user_id),
  constraint configuracoes_tempos_chk check (
    (temp_adapt_val is null or temp_adapt_val >= 0)
    and (speed_temp_val is null or speed_temp_val >= 0)
  ),
  constraint configuracoes_questoes_chk check (
    (long_test_q is null or long_test_q >= 0)
    and (norm_test_q is null or norm_test_q >= 0)
    and (desaf_test_q is null or desaf_test_q >= 0)
    and (extra_test_q is null or extra_test_q >= 0)
  ),
  constraint configuracoes_dificuldade_chk check (
    nivel_dificuldade_padrao is null
    or nivel_dificuldade_padrao in ('FACIL', 'MEDIO', 'DESAFIANTE', 'EXTRA')
  ),
  constraint configuracoes_modo_escolhas_chk check (
    modo_escolhas is null
    or modo_escolhas in ('NAO_PERMITIR', 'AMBIGUAS', 'DIAGNOSTICAS')
  ),
  constraint configuracoes_diag_chk check (
    (velocidade_segundos_por_percent is null or velocidade_segundos_por_percent >= 0)
    and (resiliencia_repeticoes_por_dia is null or resiliencia_repeticoes_por_dia >= 0)
    and (precisao_consecutivas is null or precisao_consecutivas >= 0)
    and (logica_qtd_desafiante_extra is null or logica_qtd_desafiante_extra >= 0)
    and (consistencia_percentual_min is null or consistencia_percentual_min between 0 and 100)
  )
);

create index if not exists idx_configuracoes_user_id on configuracoes (user_id);

