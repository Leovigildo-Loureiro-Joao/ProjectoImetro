-- Seed base de disciplinas para onboarding e simulados.
-- Tambem normaliza valores legados que ainda usem BASICO.

alter table if exists disciplinas
  alter column nivel set default 'INICIANTE';

update disciplinas
set nivel = 'INICIANTE'
where nivel is null
   or btrim(nivel) = ''
   or upper(btrim(nivel)) = 'BASICO';

insert into disciplinas (nome, peso, nivel, objectivo)
values
  ('Matemática', 1.5, 'INICIANTE', 'Desenvolver raciocínio lógico-matemático e capacidade de resolução de problemas'),
  ('Português', 1.5, 'INICIANTE', 'Aprimorar compreensão textual, gramática e expressão escrita'),
  ('Física', 1.2, 'INICIANTE', 'Desenvolver raciocínio científico e aplicação de conceitos físicos'),
  ('Raciocínio Lógico', 1.3, 'INICIANTE', 'Aprimorar capacidade de análise, dedução e resolução de problemas lógicos')
on conflict (nome) do update
set
  peso = excluded.peso,
  nivel = case
    when disciplinas.nivel is null
      or btrim(disciplinas.nivel) = ''
      or upper(btrim(disciplinas.nivel)) = 'BASICO'
    then excluded.nivel
    else disciplinas.nivel
  end,
  objectivo = case
    when disciplinas.objectivo is null or btrim(disciplinas.objectivo) = ''
    then excluded.objectivo
    else disciplinas.objectivo
  end;
