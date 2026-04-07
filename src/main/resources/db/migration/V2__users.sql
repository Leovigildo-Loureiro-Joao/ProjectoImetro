-- Unifica candidatos/orientadores em uma única tabela `users` com coluna `role`.
-- Regra de negócio: um email só pode ter 1 role (enforced por unique(email)).

create table if not exists users (
  id uuid primary key default uuid_generate_v4(),
  nome text not null,
  email text not null unique,
  senha_hash text null,
  role text not null,
  criado_em timestamptz not null default now(),
  constraint users_role_chk check (role in ('CANDIDATO', 'ORIENTADOR'))
);

do $$
begin
  if to_regclass('public.candidatos') is not null
     and to_regclass('public.orientadores') is not null
     and exists (
        select 1
        from candidatos c
        join orientadores o on lower(c.email) = lower(o.email)
     ) then
    raise exception 'Existem emails duplicados entre candidatos e orientadores. Resolva antes da migração para users.';
  end if;
end $$;

-- Migra dados mantendo o mesmo `id` para preservar FKs (relatorios/testes).
insert into users (id, nome, email, senha_hash, role, criado_em)
select id, nome, email, senha_hash, 'CANDIDATO', criado_em
from candidatos
on conflict (email) do nothing;

insert into users (id, nome, email, senha_hash, role, criado_em)
select id, nome, email, senha_hash, 'ORIENTADOR', criado_em
from orientadores
on conflict (email) do nothing;

-- Reaponta FKs para `users`
alter table relatorios drop constraint if exists relatorios_candidato_id_fkey;
alter table relatorios drop constraint if exists relatorios_orientador_id_fkey;

alter table testes drop constraint if exists testes_candidato_id_fkey;
alter table testes drop constraint if exists testes_orientador_id_fkey;

alter table relatorios
  add constraint relatorios_candidato_id_fkey foreign key (candidato_id) references users(id) on delete set null;
alter table relatorios
  add constraint relatorios_orientador_id_fkey foreign key (orientador_id) references users(id) on delete set null;

alter table testes
  add constraint testes_candidato_id_fkey foreign key (candidato_id) references users(id) on delete set null;
alter table testes
  add constraint testes_orientador_id_fkey foreign key (orientador_id) references users(id) on delete set null;

drop table if exists candidatos;
drop table if exists orientadores;
