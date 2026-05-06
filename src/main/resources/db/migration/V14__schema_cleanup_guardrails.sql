-- Limpeza defensiva do schema adaptativo.
-- Objetivo: normalizar vestigios legados sem apagar tabelas core do produto.

do $$
begin
  if to_regclass('public.progressao_rigor') is not null then
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
    elsif exists (
      select 1
      from information_schema.columns
      where table_name = 'progressao_rigor'
        and column_name = 'topico'
    ) and exists (
      select 1
      from information_schema.columns
      where table_name = 'progressao_rigor'
        and column_name = 'subtopico'
    ) then
      update progressao_rigor
      set subtopico = coalesce(nullif(subtopico, ''), nullif(topico, ''), 'Geral')
      where coalesce(subtopico, '') = '';

      if exists (
        select 1
        from pg_constraint
        where conrelid = 'progressao_rigor'::regclass
          and conname = 'progressao_rigor_aluno_id_disciplina_id_topico_key'
      ) then
        alter table progressao_rigor
          drop constraint progressao_rigor_aluno_id_disciplina_id_topico_key;
      end if;

      alter table progressao_rigor drop column if exists topico;
    end if;

    if exists (
      select 1
      from information_schema.columns
      where table_name = 'progressao_rigor'
        and column_name = 'subtopico'
    ) then
      update progressao_rigor
      set subtopico = 'Geral'
      where coalesce(subtopico, '') = '';

      alter table progressao_rigor
        alter column subtopico set not null;

      if exists (
        select 1
        from pg_constraint
        where conrelid = 'progressao_rigor'::regclass
          and conname = 'progressao_rigor_aluno_id_disciplina_id_topico_key'
      ) and not exists (
        select 1
        from pg_constraint
        where conrelid = 'progressao_rigor'::regclass
          and conname = 'progressao_rigor_aluno_disciplina_subtopico_key'
      ) then
        alter table progressao_rigor
          rename constraint progressao_rigor_aluno_id_disciplina_id_topico_key
          to progressao_rigor_aluno_disciplina_subtopico_key;
      end if;

      if not exists (
        select 1
        from pg_constraint
        where conrelid = 'progressao_rigor'::regclass
          and conname = 'progressao_rigor_aluno_disciplina_subtopico_key'
      ) then
        alter table progressao_rigor
          add constraint progressao_rigor_aluno_disciplina_subtopico_key
          unique (aluno_id, disciplina_id, subtopico);
      end if;
    end if;
  end if;

  if to_regclass('public.recomendacoes_rigor') is not null then
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
    elsif exists (
      select 1
      from information_schema.columns
      where table_name = 'recomendacoes_rigor'
        and column_name = 'topico'
    ) and exists (
      select 1
      from information_schema.columns
      where table_name = 'recomendacoes_rigor'
        and column_name = 'subtopico'
    ) then
      update recomendacoes_rigor
      set subtopico = coalesce(nullif(subtopico, ''), nullif(topico, ''), 'Geral')
      where coalesce(subtopico, '') = '';

      alter table recomendacoes_rigor drop column if exists topico;
    end if;

    if exists (
      select 1
      from information_schema.columns
      where table_name = 'recomendacoes_rigor'
        and column_name = 'subtopico'
    ) then
      update recomendacoes_rigor
      set subtopico = 'Geral'
      where coalesce(subtopico, '') = '';

      alter table recomendacoes_rigor
        alter column subtopico set not null;
    end if;
  end if;
end $$;

do $$
begin
  if to_regclass('public.idx_progressao_rigor_topico') is not null then
    drop index idx_progressao_rigor_topico;
  end if;

  if to_regclass('public.progressao_rigor') is not null
     and exists (
       select 1
       from information_schema.columns
       where table_name = 'progressao_rigor'
         and column_name = 'subtopico'
     ) then
    create index if not exists idx_progressao_rigor_subtopico
      on progressao_rigor (subtopico);
  end if;

  if to_regclass('public.idx_recomendacoes_rigor_topico') is not null then
    drop index idx_recomendacoes_rigor_topico;
  end if;

  if to_regclass('public.recomendacoes_rigor') is not null
     and exists (
       select 1
       from information_schema.columns
       where table_name = 'recomendacoes_rigor'
         and column_name = 'subtopico'
     ) then
    create index if not exists idx_recomendacoes_rigor_subtopico
      on recomendacoes_rigor (subtopico);
  end if;
end $$;
