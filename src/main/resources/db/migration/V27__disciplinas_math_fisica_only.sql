-- Mantém o histórico do Flyway imutável.
-- A redução para Matemática e Física precisa acontecer numa nova migration,
-- e não por edição retroativa da V10 já aplicada em bases existentes.

delete from disciplinas d
where d.nome in ('Português', 'Raciocínio Lógico')
  and not exists (
    select 1
    from progresso_aluno_disciplina pad
    where pad.disciplina_id = d.id
  )
  and not exists (
    select 1
    from progressao_rigor pr
    where pr.disciplina_id = d.id
  )
  and not exists (
    select 1
    from diagnosticos diag
    where diag.disciplina_id = d.id
  )
  and not exists (
    select 1
    from testes t
    where t.disciplina_id = d.id
  )
  and not exists (
    select 1
    from stats s
    where s.disciplina_id = d.id
  );
