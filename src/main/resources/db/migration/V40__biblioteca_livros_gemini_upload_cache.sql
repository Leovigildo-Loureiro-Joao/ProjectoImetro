alter table if exists biblioteca_livros
  add column if not exists gemini_file_uri text null,
  add column if not exists gemini_file_name text null,
  add column if not exists gemini_uploaded_em timestamptz null;
