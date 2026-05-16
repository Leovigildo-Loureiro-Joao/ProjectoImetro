-- Reset parcial para voltar o projeto ao estado inicial de uso.
-- Preserva:
--   - disciplinas
--   - configuracoes_teste_adaptativo
--   - configuracoes_teste_adaptativo_niveis
--   - configuracoes_teste_adaptativo_duracoes
--   - medalhas_catalogo
--   - flyway_schema_history
--
-- Remove:
--   - users
--   - perguntas
--   - e todas as tabelas relacionadas por foreign keys

begin;

truncate table users, perguntas restart identity cascade;

commit;
