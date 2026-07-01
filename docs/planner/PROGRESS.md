# PROGRESSO - SimulatorBolsaStudy

Documento de acompanhamento do estado atual do projeto.

## Snapshot atual

Data de referencia: `2026-05-24`

Resumo curto:

- o produto foi alinhado para `CANDIDATO` unico
- o escopo atual de conteudo ficou em `Matematica` e `Fisica`
- o fluxo legado de `orientador` saiu do runtime e do schema corrente
- a base real de perguntas pode nascer de PDFs via Gemini
- o ciclo `diagnostico -> progresso -> teste -> stats` ja esta montado no codigo

## Marcos recentes

### 2026-03-30

- arranque do README, navegacao base e primeiras views

### 2026-04-22

- introduzido modo `navegacao` por flags `TESTE` / `DB_ENABLED`
- `Flyway.migrate()` passou a correr no arranque com baseline configurado

### 2026-05-15

- pesos por alternativa passaram a fazer parte do fluxo real
- `perguntas.pesos_resposta` entrou no schema
- `stats` e `teste_perguntas` ganharam mais detalhe util

### 2026-05-24

- remocao do fluxo ativo de `orientador`
- schema manual reduzido a `CANDIDATO`
- nova migration `V26__remove_orientador.sql`
- seeds e filtros reduzidos a `Matematica` e `Fisica`
- docs e uploads alinhados ao novo escopo

## Estado funcional

### Auth e sessao

- [x] login e registo reais
- [x] role operacional unica para candidato
- [x] modo navegacao sem BD

### Onboarding

- [x] escolha de avatar
- [x] selecao de disciplinas suportadas
- [x] encaminhamento apenas para o layout do candidato
- [x] preparacao das pastas de upload

### Base real de perguntas

- [x] extracao de topicos a partir de PDFs
- [x] geracao de perguntas em lotes com Gemini
- [x] insercao na tabela `perguntas`
- [x] suporte a pesos por alternativa
- [x] suporte a campos de grafico para Matematica/Fisica

### Diagnostico

- [x] lista de disciplinas com base real
- [x] primeiro diagnostico com arranque condicionado pela base
- [x] persistencia em `diagnosticos`
- [x] atualizacao de `progressao_rigor`
- [x] atualizacao de `recomendacoes_rigor`

### Teste adaptativo

- [x] foco por disciplina, topico e subtopico
- [x] fecho com `TesteService`
- [x] gravacao em `testes`
- [x] gravacao em `stats`
- [x] gravacao em `teste_perguntas`

### Candidato

- [x] relatorios em primeira versao visual
- [x] bolsas em primeira versao visual
- [x] perfil com avatar e medalhas

## Estado tecnico

### Migrations

- [x] historico `V1` ate `V26`
- [x] schema manual alinhado com candidato unico
- [x] seed de disciplinas reduzido a Matematica/Fisica

### O que ainda esta aberto

- [ ] mover gravacoes pesadas de diagnostico e teste para background
- [ ] remover hardcodes remanescentes do motor adaptativo
- [ ] fechar transacao unica tambem para `teste_perguntas`
- [ ] endurecer leituras e DTOs de `testes`
- [ ] consolidar relatorios e bolsas com dados reais em mais telas

## Prioridade recomendada

1. endurecer o fluxo de salvamento de `testes`
2. ler configuracao adaptativa real do banco em vez de constantes
3. reduzir pontos de bloqueio na UI
4. consolidar a experiencia do candidato sobre a base real atual
