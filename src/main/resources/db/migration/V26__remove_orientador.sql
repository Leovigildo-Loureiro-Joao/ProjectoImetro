-- Remove o schema legado de orientador.
-- Mantem compatibilidade com bases antigas, convertendo contas ORIENTADOR para CANDIDATO.

drop index if exists idx_relatorios_orientador_id;
drop index if exists idx_testes_orientador_id;
drop index if exists idx_orientador_disciplinas_orientador_id;

drop table if exists orientador_disciplinas;

do $$
begin
  if to_regclass('public.relatorios') is not null then
    alter table relatorios drop constraint if exists relatorios_orientador_id_fkey;
    alter table relatorios drop column if exists orientador_id;
    alter table relatorios drop column if exists nota_orientador;
  end if;

  if to_regclass('public.testes') is not null then
    alter table testes drop constraint if exists testes_orientador_id_fkey;
    alter table testes drop column if exists orientador_id;
  end if;

  if to_regclass('public.users') is not null then
    update users
    set role = 'CANDIDATO'
    where upper(btrim(role)) = 'ORIENTADOR';

    alter table users drop constraint if exists users_role_chk;
    alter table users
      add constraint users_role_chk check (role in ('CANDIDATO'));
  end if;
end $$;
