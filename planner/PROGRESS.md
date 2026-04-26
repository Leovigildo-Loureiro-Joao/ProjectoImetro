# PROGRESSO - SimulatorBolsaStudy

Documento de acompanhamento do projeto (MVP -> versao utilizavel).

## Historico (o que ja foi feito)

### 2026-03-30
- Criado `README.md` com visao do produto, metricas do MVP e como executar.
- Implementada navegacao por layouts JavaFX: Auth (Login/Registo) -> Candidato/Orientador.
- Criadas paginas placeholder (dashboard/testes/relatorios) e CSS base.

### 2026-03-31
- Refactor para estrutura de packages mais "Java":
  - `com.imetro.domain` (modelos) + `com.imetro.domain.enums`
  - `com.imetro.app` (fluxos/controllers de aplicacao)
  - `com.imetro.ui.controller` (controllers FXML)
- Implementada classe `Relatorio` com campos para Candidato e Orientador.
- Adicionado Postgres local via `docker-compose.yml` + `.env.example`.
- Criado schema SQL do MVP e integracao de init no Docker.
- Adicionadas dependencias Postgres, Jfxtras e JFoenix.

### 2026-04-20
- O foco recente foi manter o produto a andar mesmo com instabilidade de ambiente.
- O modulo de diagnostico ja existe como experiencia de uso, mas ainda termina sem fechar persistencia real.
- Lista de diagnosticos, timeline e estatisticas ainda usam dados mockados.
- O exame adaptativo ainda nao reaproveita historico persistido do diagnostico.
- Direcao de produto validada: o diagnostico deve ser a base do restante fluxo.

### 2026-04-22
- Adicionado modo `navegacao` (sem BD) por flags `TESTE` / `DB_ENABLED`.
- Flyway `migrate()` no arranque com `baseline` em `V6`.
- Criada `V6__configuracoes.sql`.
- Ajustes na tela de configuracoes.

### 2026-04-26
- Formalizado o fluxo de desenvolvimento sem PostgreSQL local neste PC.
- Atualizada a documentacao para assumir `modo navegacao` como caminho principal enquanto nao houver Postgres disponivel.
- Adicionada `V8__progresso_aluno_disciplina.sql` para colocar no historico Flyway uma estrutura que ja existia no `scripts/db/001_schema.sql`.
- O objetivo imediato passou a ser: manter schema e docs consistentes agora, e validar persistencia real assim que o ambiente de BD estiver disponivel.

## Objetivo de "projeto terminado"

Para considerar o projeto "terminado" (MVP), o sistema deve permitir:

- Estudante entrar, fazer um simulado completo e ver o resultado + evolucao.
- Orientador entrar, ver os relatorios do(s) estudante(s) e validar recomendacoes sugeridas.
- Dados persistirem em Postgres e o sistema funcionar sem perder historico.

## Checklist de conclusao (passos recomendados)

### PROGRESS UI (telas/fluxos)

#### Auth + sessao
- [x] Telas Auth: Login/Registo + layouts (Auth/Candidato/Orientador).
- [x] Login real com `Authentication.login`.
- [x] Sessao do utilizador (estado logado + role).
- [x] Modo navegacao visivel e seguro para trabalhar sem BD.

#### Onboarding
- [x] Avatar do candidato (upload/skip na UI).
- [x] Selecao visual de disciplinas do candidato.
- [x] Selecao visual de disciplina do orientador.
- [ ] Persistencia real do onboarding com BD disponivel.
- [ ] UX clara quando a BD falhar em runtime.

#### Candidato (core)
- [x] Dashboard placeholder + CSS base.
- [x] Diagnostico com experiencia de uso completa em UI.
- [x] Exame/Teste adaptativo com loading overlay.
- [x] Tela de configuracoes com modo editar/salvar visual.
- [ ] Tela "Revisao do Diagnostico".
- [ ] `views/pages/candidato/relatorios.fxml`.
- [ ] `views/pages/candidato/bolsas.fxml`.

#### Orientador
- [x] Layout do orientador com navegacao base.
- [ ] Dashboard orientador com dados reais.
- [ ] Comparacao de evolucao por candidato.

#### Qualidade/entrega
- [x] Overlay de carregamento em `swapContent`.
- [ ] Prevenir perda de progresso no teste.
- [ ] Build executavel ou instrucao clara de distribuicao.

### PROGRESS DB (Postgres/persistencia)

#### Infra + migrations
- [x] Postgres local via `docker-compose.yml` + schema inicial `scripts/db/001_schema.sql`.
- [x] Migrations Flyway versionadas (`V1...V8`).
- [x] Flyway `migrate()` no arranque com `baseline` em `V6`.
- [x] Chaves de runtime: `TESTE`, `DB_ENABLED` e `DB_MIGRATE`.
- [x] Documentacao para trabalhar sem BD local.
- [ ] Validar `V7` e `V8` em Postgres real neste fluxo atual.

#### Repositorios/CRUD
- [x] Base JDBC generica e repositorios baseados em tabela.
- [ ] Garantir operacoes minimas usadas no fluxo.
- [ ] Banco de questoes unificado entre memoria e BD.
- [ ] Persistencia real do simulador (`testes` + `teste_perguntas`).
- [ ] Persistencia real de `progresso_aluno_disciplina`.

#### Ciclo do diagnostico -> relatorios
- [x] Tabela `diagnosticos` (`V5`).
- [x] Tabela `configuracoes` (`V6`).
- [x] Tabela `progresso_aluno_disciplina` entrou no historico de migrations (`V8`).
- [ ] Persistir resultado real do diagnostico.
- [ ] Alimentar lista, timeline e estatisticas com dados reais.
- [ ] Gerar relatorio derivado do diagnostico.
- [ ] Usar o ultimo diagnostico como entrada do exame adaptativo.

#### Configuracoes (BD)
- [ ] Criar `ConfiguracaoRepository`.
- [ ] "Salvar alteracoes" gravar em `configuracoes`.
- [ ] "Reiniciar para o padrao" aplicar defaults reais.

#### Recomendacoes
- [ ] Gerar recomendacoes sugeridas por topico/dificuldade/erros.
- [ ] Orientador validar ou rejeitar recomendacoes.
- [ ] Mostrar recomendacoes validadas ao candidato.

#### Qualidade (BD)
- [ ] Testes de unidade para analise e persistencia.

## Foco recomendado enquanto nao houver BD neste PC

- manter `TESTE=false`
- continuar a evoluir diagnostico, exame adaptativo e resultado em memoria
- fechar lacunas de documentacao e schema
- evitar avancar queries JDBC novas sem um Postgres real para validar

## Foco recomendado assim que houver Postgres

- validar onboarding persistente
- validar `V7` e `V8`
- gravar diagnosticos reais
- trocar mocks da timeline e estatisticas por queries reais
