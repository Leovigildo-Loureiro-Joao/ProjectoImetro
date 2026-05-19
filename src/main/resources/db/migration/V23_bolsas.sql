CREATE TABLE IF NOT EXISTS bolsas(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    matches INTEGER NOT NULL,
    nome TEXT NOT NULL,
    destaque TEXT NOT NULL,
    risco TEXT NOT NULL,
    cobertura TEXT NOT NULL

);
