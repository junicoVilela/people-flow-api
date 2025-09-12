-- Baseline: schemas e tabelas iniciais
CREATE SCHEMA IF NOT EXISTS app;

-- Tabela de pessoas
CREATE TABLE IF NOT EXISTS app.person (
    id UUID PRIMARY KEY,
    name TEXT NOT NULL,
    document TEXT UNIQUE,
    status TEXT NOT NULL DEFAULT 'ATIVO',
    created_at TIMESTAMPTZ DEFAULT now()
);

-- Índices
CREATE INDEX IF NOT EXISTS idx_person_document ON app.person(document);
CREATE INDEX IF NOT EXISTS idx_person_status ON app.person(status); 