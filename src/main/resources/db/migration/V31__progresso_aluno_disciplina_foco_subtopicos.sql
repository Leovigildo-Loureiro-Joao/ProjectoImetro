-- Focos de estudo definidos pelo candidato no onboarding.
-- Guarda os subtopicos prioritarios por disciplina para orientar a bolsa.

alter table if exists progresso_aluno_disciplina
  add column if not exists foco_subtopicos text null;
