alter table if exists perguntas
  add column if not exists disciplina text null;

alter table if exists perguntas
  add column if not exists subtopico text null;

update perguntas
set subtopico = coalesce(nullif(subtopico, ''), topico)
where subtopico is null or subtopico = '';

update perguntas
set disciplina = case
  when topico in ('Porcentagem', 'Operações Aritméticas', 'Frações', 'Estatística Básica', 'Potenciação', 'Equações', 'Regra de Três', 'Juros Simples', 'Geometria', 'Razão e Proporção', 'Progressões', 'Função Quadrática', 'Análise Combinatória', 'Logaritmos', 'Matrizes')
    then 'MATEMATICA'
  when topico in ('Acentuação', 'Ortografia', 'Semântica', 'Separação Silábica', 'Substantivos Coletivos', 'Concordância Verbal', 'Regência Verbal', 'Colocação Pronominal', 'Período Composto', 'Vozes Verbais', 'Figuras de Linguagem', 'Funções da Linguagem', 'Morfossintaxe', 'Literatura Brasileira', 'Estilística')
    then 'PORTUGUES'
  when topico in ('Mecânica', 'Cinemática', 'Unidades de Medida', 'Termologia', 'Energia', 'Leis de Newton', 'Trabalho e Energia', 'Hidrostática', 'Densidade', 'Movimento Uniformemente Variado', 'Calorimetria', 'Eletrodinâmica', 'Óptica', 'Física Moderna')
    then 'FISICA'
  when topico in ('Sequências', 'Padrões Numéricos', 'Sequências Lógicas', 'Anagramas', 'Raciocínio Temporal', 'Lógica de Argumentação', 'Lógica Proposicional', 'Raciocínio Espacial', 'Raciocínio Matemático', 'Conjuntos', 'Lógica Dedutiva', 'Sequências Avançadas', 'Combinatória', 'Lógica Clássica')
    then 'RACIOCINIO LOGICO'
  else coalesce(disciplina, 'GERAL')
end
where disciplina is null or disciplina = '';

update perguntas
set topico = case
  when disciplina = 'MATEMATICA' and subtopico in ('Geometria') then 'Geometria'
  when disciplina = 'MATEMATICA' and subtopico in ('Estatística Básica') then 'Estatistica'
  when disciplina = 'MATEMATICA' and subtopico in ('Razão e Proporção') then 'Raciocinio'
  when disciplina = 'MATEMATICA' then 'Algebra'

  when disciplina = 'PORTUGUES' and subtopico in ('Concordância Verbal', 'Regência Verbal', 'Colocação Pronominal', 'Vozes Verbais') then 'Gramatica'
  when disciplina = 'PORTUGUES' and subtopico in ('Período Composto', 'Funções da Linguagem', 'Figuras de Linguagem', 'Morfossintaxe', 'Semântica', 'Estilística', 'Literatura Brasileira') then 'Interpretacao'
  when disciplina = 'PORTUGUES' then 'Gramatica'

  when disciplina = 'FISICA' and subtopico in ('Termologia', 'Calorimetria') then 'Termologia'
  when disciplina = 'FISICA' then 'Mecanica'

  when disciplina = 'RACIOCINIO LOGICO' then 'Raciocinio'
  else topico
end;

create index if not exists idx_perguntas_disciplina on perguntas (disciplina);
create index if not exists idx_perguntas_subtopico on perguntas (subtopico);
