# Uploads de disciplinas

Esta pasta guarda os PDFs-base usados para extrair topicos e gerar perguntas reais com o Gemini.

## Escopo atual

- o bootstrap automatico so atende `Matematica` e `Fisica`
- cada disciplina usa a sua pasta em `uploads/disciplinas/<uuid-da-disciplina>`
- o produto nao depende mais de `orientador` para liberar processamento

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

## O que o sistema faz

### Preparacao das pastas

Quando o onboarding ou o bootstrap prepara as disciplinas:

- o sistema le as disciplinas cadastradas
- cria as pastas em `uploads/disciplinas/<uuid>`
- deixa a estrutura pronta para receber os PDFs

### Extracao de topicos

Quando ha PDFs validos e o Gemini esta configurado:

- o sistema le os livros da disciplina
- extrai topicos e subtopicos reais
- grava o resultado em `topicos-extraidos.json`

### Geracao de perguntas

Depois da extracao:

- o sistema reparte a geracao em lotes
- gera `questoes-geradas.json`
- insere as perguntas resultantes na tabela `perguntas`

## Quando o bootstrap pode arrancar

O processamento automatico pode ser disparado por mais de um ponto do fluxo:

- onboarding de disciplinas
- entrada no fluxo de primeiro diagnostico, quando ainda nao ha base real suficiente
- chamadas explicitas do bootstrap em services/controladores

## Regras praticas

- coloca apenas PDFs validos da disciplina correspondente
- usa o `UUID` real da disciplina no nome da pasta
- nao mistures livros de disciplinas diferentes na mesma pasta
- `topicos-extraidos.json` e `questoes-geradas.json` sao artefactos do processo e podem ser regenerados
- disciplinas fora de `Matematica` e `Fisica` nao entram no bootstrap atual

## Estados comuns do processamento

- `JA_EXISTENTE`: a disciplina ja tem perguntas reais na base
- `SEM_PDFS`: a pasta existe, mas nao ha PDFs para ler
- `GEMINI_NAO_CONFIGURADO`: falta `GEMINI_API_KEY`
- `PROCESSADO_AUTOMATICAMENTE`: topicos e perguntas foram gerados
- `ERRO`: o fluxo falhou em algum ponto e precisa de nova tentativa

## Fluxo simples de uso

1. Abre o onboarding ou prepara a disciplina pela app.
2. Deixa o sistema criar `uploads/disciplinas/<uuid>`.
3. Coloca os PDFs de Matematica ou Fisica nessa pasta.
4. Garante que `GEMINI_API_KEY` esta configurada.
5. Entra no diagnostico ou dispara o bootstrap.
6. Confirma a geracao de `topicos-extraidos.json`.
7. Confirma a geracao de `questoes-geradas.json`.
8. Verifica se a tabela `perguntas` recebeu os registos.

## Services envolvidos

- `DisciplinaUploadBootstrapService`
  - prepara as pastas de upload
  - extrai topicos e grava `topicos-extraidos.json`
- `PerguntasBootstrapService`
  - gera perguntas em lotes
  - insere perguntas na BD
- `PerguntasBootstrapAsyncService`
  - acompanha o estado de processamento em background
- `GeminiService`
  - comunica com a API Gemini

## Requisitos

- `DB_ENABLED=true` ou `TESTE=true`
- disciplinas existentes na tabela `disciplinas`
- `GEMINI_API_KEY` ou `GEMENI_API_KEY`
- PDFs validos nas pastas corretas

## Observacao

O produto ja nao usa fallback principal para seeds mockadas nesse fluxo. Se o objetivo for produzir base real, o caminho esperado e sempre BD + PDFs + Gemini.
