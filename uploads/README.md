# Uploads de disciplinas

Estrutura base para os livros e PDFs usados pelo Gemini.

## O que acontece automaticamente

Quando o onboarding de disciplinas abre:

- o sistema carrega as disciplinas cadastradas no banco;
- cria automaticamente as pastas em `uploads/disciplinas/<uuid-da-disciplina>`;
- mostra no `statusLabel` que as pastas dos livros foram preparadas.

Quando o candidato conclui a escolha de disciplinas:

- as disciplinas sem orientacao tentam processar automaticamente os PDFs;
- o sistema gera `topicos-extraidos.json`;
- o sistema gera `questoes-geradas.json`;
- as perguntas reais entram na tabela `perguntas`;
- se a disciplina ja tiver orientador cadastrado, esse processamento automatico fica em espera.

## Estrutura esperada

```text
uploads/
  disciplinas/
    <disciplina_id>/
      livro-1.pdf
      livro-2.pdf
      topicos-extraidos.json
      questoes-geradas.json
```

## Regras

- Cada pasta filha de `uploads/disciplinas` deve usar o `UUID` da disciplina cadastrada no banco.
- Coloque nessa pasta apenas os PDFs-base daquela disciplina.
- `topicos-extraidos.json` guarda os topicos e subtopicos devolvidos pelo Gemini.
- `questoes-geradas.json` guarda o JSON bruto das perguntas geradas.
- Nao existe mais fallback para seed mockada no fluxo principal.

## Fluxo simples de uso

1. Abre o onboarding.
2. Deixa o sistema criar as pastas automaticamente.
3. Coloca os livros PDF dentro da pasta da disciplina.
4. Se a disciplina nao tiver orientador, o sistema tenta processar os livros automaticamente.
5. O sistema grava os JSONs e insere perguntas reais na tabela `perguntas`.

## Services envolvidos

- `GeminiService`
  - `extrairTopicosJson(...)`: le um ou mais PDFs e devolve topicos e subtopicos em JSON.
  - `gerarSimuladoJson(...)`: gera perguntas em JSON a partir dos PDFs.
- `DisciplinaUploadBootstrapService`
  - `prepararPastasUploads()`: cria as pastas `uploads/disciplinas/<uuid>`.
  - `processarCargaInicial()`: gera `topicos-extraidos.json`.
  - `processarCargaInicial(UUID disciplinaId)`: processa apenas uma disciplina.
- `PerguntasBootstrapService`
  - `processarDisciplinasAutomaticasDoCandidato(...)`: gera perguntas reais para disciplinas sem orientacao.

## Exemplo rapido

```java
DisciplinaUploadBootstrapService bootstrap = new DisciplinaUploadBootstrapService();
bootstrap.prepararPastasUploads();
bootstrap.processarCargaInicial();
```

## Exemplo por disciplina

```java
UUID disciplinaId = UUID.fromString("coloca-aqui-o-uuid-da-disciplina");

DisciplinaUploadBootstrapService bootstrap = new DisciplinaUploadBootstrapService();
bootstrap.prepararPastasUploads();
bootstrap.processarCargaInicial(disciplinaId, true);
```

## Requisitos

- `DB_ENABLED=true` ou `TESTE=true`
- disciplinas cadastradas no banco
- `GEMINI_API_KEY` ou `GEMENI_API_KEY` configurada no `.env`
- PDFs validos dentro das pastas de disciplina
