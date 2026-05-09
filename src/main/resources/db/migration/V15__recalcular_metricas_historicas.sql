-- Recalculo historico das metricas deterministicas.
-- Objetivo: alinhar registros antigos com a mesma escala 0..1 usada pela aplicacao.

with diagnosticos_base as (
  select
    d.id,
    coalesce(d.candidato_id::text, d.id::text) as candidato_key,
    coalesce(nullif(lower(trim(d.disciplina_nome)), ''), d.disciplina_id::text, d.id::text) as disciplina_key,
    coalesce(d.concluido_em, d.iniciado_em, d.criado_em) as momento_base,
    d.criado_em,
    greatest(0, coalesce(d.total_questoes, 0)) as total_questoes_base,
    greatest(0, coalesce(d.total_acertos, 0)) as total_acertos_base,
    case
      when coalesce(d.total_questoes, 0) <= 0 then 0::double precision
      else least(100::double precision, greatest(0::double precision, (coalesce(d.total_acertos, 0) * 100.0) / d.total_questoes))
    end as percentual_calc,
    case
      when coalesce(d.total_questoes, 0) <= 0 then 0::double precision
      else least(1::double precision, greatest(0::double precision, coalesce(d.total_acertos, 0)::double precision / d.total_questoes))
    end as precisao_calc,
    case
      when coalesce(d.duracao_segundos, 0) <= 0 or coalesce(d.total_questoes, 0) <= 0 then 0.5::double precision
      else least(1::double precision, greatest(0::double precision, 1 - ((d.duracao_segundos::double precision / d.total_questoes) / 120.0)))
    end as velocidade_calc
  from diagnosticos d
),
diagnosticos_ordenados as (
  select
    base.*,
    lag(base.percentual_calc) over (
      partition by base.candidato_key, base.disciplina_key
      order by base.momento_base, base.criado_em, base.id
    ) as percentual_anterior
  from diagnosticos_base base
),
logica_por_diagnostico as (
  select
    d.id,
    case
      when count(*) filter (where coalesce(p.rigor, 0.5) >= 0.75) = 0 then 0::double precision
      else (
        count(*) filter (
          where coalesce(p.rigor, 0.5) >= 0.75
            and coalesce((resposta.item ->> 'acertou')::boolean, false)
        )::double precision
        /
        count(*) filter (where coalesce(p.rigor, 0.5) >= 0.75)
      )
    end as logica_calc
  from diagnosticos d
  left join lateral jsonb_array_elements(
    case
      when jsonb_typeof(d.respostas) = 'array' then d.respostas
      else '[]'::jsonb
    end
  ) as resposta(item) on true
  left join perguntas p
    on p.id::text = nullif(resposta.item ->> 'questaoId', '')
  group by d.id
)
update diagnosticos d
set
  total_erros = greatest(0, ordenado.total_questoes_base - ordenado.total_acertos_base),
  percentual_acerto = ordenado.percentual_calc,
  evolucao_percentual = case
    when ordenado.percentual_anterior is null then null
    else ordenado.percentual_calc - ordenado.percentual_anterior
  end,
  velocidade = ordenado.velocidade_calc::real,
  precisao = ordenado.precisao_calc::real,
  consistencia = case
    when ordenado.percentual_anterior is null then 0::real
    else least(1::double precision, greatest(0::double precision, 1 - abs((ordenado.percentual_calc - ordenado.percentual_anterior) / 100.0)))::real
  end,
  logica = coalesce(logica.logica_calc, 0)::real,
  resiliencia = 0::real,
  atualizado_em = now()
from diagnosticos_ordenados ordenado
left join logica_por_diagnostico logica
  on logica.id = ordenado.id
where d.id = ordenado.id;

with testes_base as (
  select
    t.id,
    coalesce(t.candidato_id::text, t.id::text) as candidato_key,
    coalesce(nullif(lower(trim(t.disciplina_nome)), ''), t.disciplina_id::text, t.id::text) as disciplina_key,
    coalesce(t.data_teste, t.criado_em) as momento_base,
    t.criado_em,
    greatest(0, coalesce(t.total_questoes, 0)) as total_questoes_base,
    greatest(0, coalesce(t.total_acertos, 0)) as total_acertos_base,
    case
      when coalesce(t.total_questoes, 0) <= 0 then 0::double precision
      else least(100::double precision, greatest(0::double precision, (coalesce(t.total_acertos, 0) * 100.0) / t.total_questoes))
    end as percentual_calc,
    case
      when coalesce(t.total_questoes, 0) <= 0 then 0::double precision
      else least(1::double precision, greatest(0::double precision, coalesce(t.total_acertos, 0)::double precision / t.total_questoes))
    end as precisao_calc,
    case
      when coalesce(t.duracao_segundos, 0) <= 0 or coalesce(t.total_questoes, 0) <= 0 then 0.5::double precision
      else least(1::double precision, greatest(0::double precision, 1 - ((t.duracao_segundos::double precision / t.total_questoes) / 120.0)))
    end as velocidade_calc
  from testes t
),
testes_ordenados as (
  select
    base.*,
    lag(base.percentual_calc) over (
      partition by base.candidato_key, base.disciplina_key
      order by base.momento_base, base.criado_em, base.id
    ) as percentual_anterior
  from testes_base base
)
update testes t
set
  total_erros = greatest(0, ordenado.total_questoes_base - ordenado.total_acertos_base),
  percentual_acerto = ordenado.percentual_calc,
  velocidade = ordenado.velocidade_calc::real,
  precisao = ordenado.precisao_calc::real,
  consistencia = case
    when ordenado.percentual_anterior is null then 0::real
    else least(1::double precision, greatest(0::double precision, 1 - abs((ordenado.percentual_calc - ordenado.percentual_anterior) / 100.0)))::real
  end,
  logica = least(1::double precision, greatest(0::double precision, coalesce(t.logica, 0)))::real,
  resiliencia = 0::real,
  atualizado_em = now()
from testes_ordenados ordenado
where t.id = ordenado.id;

with stats_base as (
  select
    s.id,
    coalesce(s.candidato_id::text, s.id::text) as candidato_key,
    coalesce(nullif(lower(trim(s.disciplina_nome)), ''), s.disciplina_id::text, s.id::text) as disciplina_key,
    s.criado_em as momento_base,
    s.criado_em,
    greatest(0, coalesce(s.total_questoes, 0)) as total_questoes_base,
    greatest(0, coalesce(s.total_acertos, 0)) as total_acertos_base,
    case
      when coalesce(s.total_questoes, 0) <= 0 then 0::double precision
      else least(100::double precision, greatest(0::double precision, (coalesce(s.total_acertos, 0) * 100.0) / s.total_questoes))
    end as percentual_calc,
    case
      when coalesce(s.total_questoes, 0) <= 0 then 0::double precision
      else least(1::double precision, greatest(0::double precision, coalesce(s.total_acertos, 0)::double precision / s.total_questoes))
    end as precisao_calc,
    case
      when coalesce(s.tempo_total_segundos, 0) <= 0 or coalesce(s.total_questoes, 0) <= 0 then 0.5::double precision
      else least(1::double precision, greatest(0::double precision, 1 - ((s.tempo_total_segundos::double precision / s.total_questoes) / 120.0)))
    end as velocidade_calc
  from stats s
),
stats_ordenados as (
  select
    base.*,
    lag(base.percentual_calc) over (
      partition by base.candidato_key, base.disciplina_key
      order by base.momento_base, base.criado_em, base.id
    ) as percentual_anterior
  from stats_base base
)
update stats s
set
  total_erros = greatest(0, ordenado.total_questoes_base - ordenado.total_acertos_base),
  tempo_medio_segundos = case
    when ordenado.total_questoes_base <= 0 then 0
    else coalesce(s.tempo_total_segundos, 0)::double precision / ordenado.total_questoes_base
  end,
  percentual_acerto = ordenado.percentual_calc,
  velocidade = ordenado.velocidade_calc::real,
  precisao = ordenado.precisao_calc::real,
  consistencia = case
    when ordenado.percentual_anterior is null then 0::real
    else least(1::double precision, greatest(0::double precision, 1 - abs((ordenado.percentual_calc - ordenado.percentual_anterior) / 100.0)))::real
  end,
  logica = least(1::double precision, greatest(0::double precision, coalesce(s.logica, 0)))::real,
  resiliencia = 0::real,
  atualizado_em = now()
from stats_ordenados ordenado
where s.id = ordenado.id;
