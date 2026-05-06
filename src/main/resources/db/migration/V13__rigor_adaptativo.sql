do $$
begin
  if exists (
    select 1
    from information_schema.columns
    where table_name = 'progressao_rigor'
      and column_name = 'topico'
  ) and not exists (
    select 1
    from information_schema.columns
    where table_name = 'progressao_rigor'
      and column_name = 'subtopico'
  ) then
    alter table progressao_rigor rename column topico to subtopico;
  end if;

  if exists (
    select 1
    from information_schema.columns
    where table_name = 'recomendacoes_rigor'
      and column_name = 'topico'
  ) and not exists (
    select 1
    from information_schema.columns
    where table_name = 'recomendacoes_rigor'
      and column_name = 'subtopico'
  ) then
    alter table recomendacoes_rigor rename column topico to subtopico;
  end if;
end $$;

drop index if exists idx_progressao_rigor_topico;
create index if not exists idx_progressao_rigor_subtopico
  on progressao_rigor (subtopico);

drop index if exists idx_recomendacoes_rigor_topico;
create index if not exists idx_recomendacoes_rigor_subtopico
  on recomendacoes_rigor (subtopico);
