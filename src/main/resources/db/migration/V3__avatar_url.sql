-- Adiciona suporte a avatar do utilizador (usado no onboarding).

alter table if exists users
  add column if not exists avatar_url text null;

