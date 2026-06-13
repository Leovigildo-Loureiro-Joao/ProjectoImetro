-- Seed da configuracao adaptativa global usada pelo teste e diagnostico.
-- Mantem um perfil ativo unico e actualiza os niveis padrao em execucoes repetidas.

update configuracoes_teste_adaptativo
set ativo = false,
    atualizado_em = now()
where ativo = true
  and codigo <> 'PADRAO_V1';

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
  peso_resiliencia_estabilidade,
  resiliencia_questao_base,
  resiliencia_questao_bonus_acerto,
  resiliencia_questao_bonus_ritmo,
  resiliencia_questao_bonus_recuperacao
) values (
  'PADRAO_V1',
  'Perfil inicial do motor adaptativo centralizado na base, alinhado aos hardcodes actuais do projecto.',
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
  0.30,
  0.10,
  0.65,
  0.15,
  0.10
)
on conflict (codigo) do update
set descricao = excluded.descricao,
    ativo = excluded.ativo,
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
    resiliencia_questao_base = excluded.resiliencia_questao_base,
    resiliencia_questao_bonus_acerto = excluded.resiliencia_questao_bonus_acerto,
    resiliencia_questao_bonus_ritmo = excluded.resiliencia_questao_bonus_ritmo,
    resiliencia_questao_bonus_recuperacao = excluded.resiliencia_questao_bonus_recuperacao,
    atualizado_em = now();

with configuracao as (
  select id
  from configuracoes_teste_adaptativo
  where codigo = 'PADRAO_V1'
)
insert into configuracoes_teste_adaptativo_niveis (
  configuracao_id,
  nivel,
  codigo,
  tempo_sugerido_segundos,
  rigor_base,
  limite_inferior,
  limite_superior,
  limiar_acerto,
  limiar_erro,
  tot_erro_revisao
)
select
  configuracao.id,
  valores.nivel,
  valores.codigo,
  valores.tempo_sugerido_segundos,
  valores.rigor_base,
  valores.limite_inferior,
  valores.limite_superior,
  valores.limiar_acerto,
  valores.limiar_erro,
  valores.tot_erro_revisao
from configuracao
cross join (
  values
    (1, 'FACIL', 40::real, 0.18::double precision, 0.05::double precision, 0.30::double precision, 0.45::double precision, 0.40::double precision, 0.25::double precision, 4),
    (2, 'MEDIO', 55::real, 0.35::double precision, 0.20::double precision, 0.50::double precision, 0.55::double precision, 0.35::double precision, 0.35::double precision, 3),
    (3, 'DIFICIL', 70::real, 0.58::double precision, 0.45::double precision, 0.72::double precision, 0.75::double precision, 0.30::double precision, 0.40::double precision, 2),
    (4, 'EXPERT', 85::real, 0.78::double precision, 0.65::double precision, 0.92::double precision, 0.80::double precision, 0.20::double precision, 0.25::double precision, 1)
) as valores (
  nivel,
  codigo,
  tempo_sugerido_segundos,
  rigor_base,
  limite_inferior,
  limite_superior,
  limiar_acerto,
  limiar_erro,
  tot_erro_revisao
)
on conflict (configuracao_id, nivel) do update
set codigo = excluded.codigo,
    tempo_sugerido_segundos = excluded.tempo_sugerido_segundos,
    rigor_base = excluded.rigor_base,
    limite_inferior = excluded.limite_inferior,
    limite_superior = excluded.limite_superior,
    limiar_acerto = excluded.limiar_acerto,
    limiar_erro = excluded.limiar_erro,
    tot_erro_revisao = excluded.tot_erro_revisao,
    atualizado_em = now();

with configuracao as (
  select id
  from configuracoes_teste_adaptativo
  where codigo = 'PADRAO_V1'
)
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
from configuracao
cross join (
  values
    ('CURTO', 'Perfil curto do teste adaptativo.', 5),
    ('MEDIO', 'Perfil medio do teste adaptativo.', 7),
    ('LONGO', 'Perfil longo do teste adaptativo.', 10)
) as valores (
  codigo,
  descricao,
  limite_questoes
)
on conflict (configuracao_id, codigo) do update
set descricao = excluded.descricao,
    limite_questoes = excluded.limite_questoes,
    atualizado_em = now();
