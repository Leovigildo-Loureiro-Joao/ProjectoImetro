CREATE TABLE IF NOT EXISTS bolsas(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    tipo TEXT NOT NULL,
    matches INTEGER NOT NULL,
    nome TEXT NOT NULL,
    vagas INTEGER NOT NULL,
    risco TEXT NOT NULL,
    cobertura TEXT NOT NULL,
    descricao TEXT NOT NULL,
    abertura TIMESTAMP,
    fechamento TIMESTAMP,
    PRIMARY KEY(id)
);


CREATE TABLE IF NOT EXISTS score_bolsas(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    candidato_id UUID NOT NULL,
    bolsa_id UUID NOT NULL,
    score  double precision DEFAULT 0 NOT NULL,
    destaque TEXT NOT NULL,
    CONSTRAINT candidato_simulado_id_fkey FOREIGN key(candidato_id) REFERENCES users(id),
    PRIMARY KEY(id)
);


  insert into
    bolsas (
    nome,
    tipo,
    matches,
    vagas,
    cobertura,
    descricao,
      risco)
  values
    (
     'Bolsa Merito Atlas',
    'Propina + mentoria',
    88,
    100,
    'Cobertura quase total da propina',
    'Excelente para quem sustenta melhoria continua.',
    'Precisa ter pelo menos uma medalha.'
    ),
    (
        'Programa Horizonte STEM',
        'Parcial + laboratorio',
        79,
        150,
        'Apoio parcial e acesso a projetos',
        'Grande encaixe para Matematica e Fisica.',
        'Alta concorrencia entre perfis tecnicos.'
    ),(
        'Fundo Impulso Academico',
        'Auxilio de mensalidade',
        71,
        50,
        'Apoio modular por semestre',
        'Boa opcao para ganhar tracao rapida.',
        'Documentacao precisa estar impecavel.'
    ),
    (
        'Beca Impacto Local',
        'Merito + projeto comunitario',
        67,
        180,
        'Cobertura media com bonus por impacto',
        'Diferencia-te se mostrares lideranca aplicada.',
        'Exige narrativa social mais madura.'
    );