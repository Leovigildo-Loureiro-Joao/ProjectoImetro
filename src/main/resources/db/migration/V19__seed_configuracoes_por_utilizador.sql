insert into configuracoes (
  user_id,
  temp_adapt_val,
  temp_adapt_unit,
  speed_temp_val,
  speed_temp_unit,
  long_test_q,
  norm_test_q,
  desaf_test_q,
  extra_test_q,
  nivel_dificuldade_padrao,
  modo_escolhas,
  velocidade_segundos_por_percent,
  resiliencia_repeticoes_por_dia,
  precisao_consecutivas,
  logica_qtd_desafiante_extra,
  consistencia_percentual_min
)
select
  users.id,
  20,
  'MINUTOS',
  60,
  'SEGUNDOS',
  10,
  7,
  7,
  5,
  'MEDIO',
  'DIAGNOSTICAS',
  120,
  2,
  3,
  2,
  70.0
from users
left join configuracoes
  on configuracoes.user_id = users.id
where configuracoes.user_id is null;
