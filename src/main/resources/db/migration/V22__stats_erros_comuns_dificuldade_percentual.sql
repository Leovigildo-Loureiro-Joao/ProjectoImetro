update stats s
set erros_comuns = coalesce(erros.erros_comuns, '[]'::jsonb),
    atualizado_em = now()
from (
  select
    tp.teste_id,
    coalesce(
      jsonb_agg(
        jsonb_build_object(
          'questaoId', tp.pergunta_id,
          'topico', coalesce(tp.topico, ''),
          'subtopico', coalesce(tp.subtopico, ''),
          'marcada', coalesce(tp.resposta_dada, ''),
          'nivelDificuldade', greatest(0, coalesce(tp.nivel_dificuldade, 0)),
          'rigor', round((least(1.0, greatest(0.0, coalesce(tp.rigor, 0.0))))::numeric, 4),
          'percentualDificuldade', round((
            coalesce(
              nullif(least(1.0, greatest(0.0, tp.rigor)), 0.0),
              greatest(0.0, least(1.0, coalesce(tp.nivel_dificuldade, 0) / 5.0))
            ) * 100.0
          )::numeric, 2),
          'enuciado', coalesce(tp.enunciado, ''),
          'resposta', coalesce(tp.resposta_correta, '')
        )
        order by tp.ordem asc
      ) filter (
        where coalesce(
          tp.acertou,
          upper(coalesce(tp.resposta_dada, '')) = upper(coalesce(tp.resposta_correta, ''))
        ) = false
      ),
      '[]'::jsonb
    ) as erros_comuns
  from teste_perguntas tp
  group by tp.teste_id
) erros
where s.teste_id = erros.teste_id;
