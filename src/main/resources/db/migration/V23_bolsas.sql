CREATE TABLE IF NOT EXISTS bolsas(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    candidato_id UUID NOT NULL,
    matches INTEGER NOT NULL,
    nome TEXT NOT NULL,
    risco TEXT NOT NULL,
    cobertura TEXT NOT NULL,
    CONSTRAINT bolsa_candidato_simulado_id_fkey FOREIGN key(candidato_id) REFERENCES users(id),
);


CREATE TABLE IF NOT EXISTS score_bolsas(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    candidato_id UUID NOT NULL,
    bolsa_id UUID NOT NULL,
    score DOUBLE DEFAULT 0 NOT NULL,
    destaque TEXT NOT NULL,
    CONSTRAINT candidato_simulado_id_fkey FOREIGN key(candidato_id) REFERENCES users(id),
    CONSTRAINT score_bolsa_candidato_simulado_id_fkey FOREIGN key(bolsa_id) REFERENCES bolsas(id),
);


