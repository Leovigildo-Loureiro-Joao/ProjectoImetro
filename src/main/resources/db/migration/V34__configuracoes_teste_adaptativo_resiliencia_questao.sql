alter table if exists configuracoes_teste_adaptativo
  add column if not exists resiliencia_questao_base double precision not null default 0.10,
  add column if not exists resiliencia_questao_bonus_acerto double precision not null default 0.65,
  add column if not exists resiliencia_questao_bonus_ritmo double precision not null default 0.15,
  add column if not exists resiliencia_questao_bonus_recuperacao double precision not null default 0.10;

update configuracoes_teste_adaptativo
set resiliencia_questao_base = 0.10,
    resiliencia_questao_bonus_acerto = 0.65,
    resiliencia_questao_bonus_ritmo = 0.15,
    resiliencia_questao_bonus_recuperacao = 0.10,
    atualizado_em = now()
where codigo = 'PADRAO_V1';
