CREATE TABLE IF NOT EXISTS bolsas(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    candidato_id UUID NOT NULL,
    matches INTEGER NOT NULL,
    nome TEXT NOT NULL,
    destaque TEXT NOT NULL,
    risco TEXT NOT NULL,
    cobertura TEXT NOT NULL,
    CONSTRAINT bolsa_candidato_simulado_id_fkey FOREIGN key(candidato_id) REFERENCES users(id),
);


INSERT INTO bolsas(
'nome','destaque','matches'

) VALUES("Bolsa Merito Atlas",
                "Propina + mentoria",
                88,
                "Cobertura quase total da propina",
                "Fecha em 21 dias",
                "Excelente para quem sustenta melhoria continua.",
                "Precisa de carta pessoal forte.",)
