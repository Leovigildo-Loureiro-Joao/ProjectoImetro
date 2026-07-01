do $$
begin
  if exists (
    select 1
    from pg_constraint
    where conname = 'stats_origem_chk'
  ) then
    alter table stats drop constraint stats_origem_chk;
  end if;
end $$;

alter table stats
  add constraint stats_origem_chk check (origem in ('TESTE', 'DIAGNOSTICO', 'MISTO', 'CONFIRMACAO_LEITURA'));
