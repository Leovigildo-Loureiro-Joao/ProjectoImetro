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
- Criado schema SQL do MVP (tabelas de candidatos/orientadores/relatorios/perguntas/testes) e integracao de init no Docker.
- Adicionadas as dependencias Postgres, Jfxextrax e Jfoenix.

### 2026-04-20
- A paralisia recente veio mais de ambiente/WSL do que de produto; por isso o foco foi em fluxos de telas e dados mockados.
- Conclusao atual: o projeto esta a avancar bem e nao esta "preso numa aba", mas o risco agora e ficar forte em UI e fraco em ciclo completo.
- O modulo de diagnostico ja existe como experiencia de uso, mas ainda termina sem fechar persistencia, revisao e reaproveitamento dos dados.
- Lista de diagnosticos, timeline e estatisticas ainda usam dados mockados; precisam passar a ler dados reais da base.
- O exame adaptativo ainda esta pouco acoplado ao diagnostico; hoje ele nao herda o perfil nem os resultados do ultimo diagnostico.
- As areas mais ignoradas neste momento sao:
  - `views/pages/candidato/relatorios.fxml`
  - `views/pages/candidato/bolsas.fxml`
  - uma futura tela de "Revisao do Diagnostico", que ainda nao existe
- Direcao de produto validada: cada diagnostico deve ser a base fluida do projeto para gerar exames adaptativos mais confiaveis.
- Sequencia recomendada para o proximo salto:
  - persistir o resultado real do diagnostico
  - gerar relatorio derivado
  - criar revisao do diagnostico
  - alimentar timeline/estatisticas com dados reais
  - usar o ultimo diagnostico como entrada do exame adaptativo

### 2026-04-22
- Adicionado modo "navegacao" (sem BD) por flags (`TESTE`/`DB_ENABLED`) + banner na UI para nao passar despercebido.
- Flyway `migrate()` no arranque (com `baseline` em V6 para compatibilidade com o schema criado pelo Docker).
- Criada a migration `V6__configuracoes.sql` e adicionada a tabela `configuracoes` no schema inicial do Docker.
- Ajustes na tela de configuracoes (enable/disable menos fragil e textos corrigidos).

## Objetivo de "projeto terminado"

Para considerar o projeto "terminado" (MVP), o sistema deve permitir:

- Estudante entrar, fazer um simulado completo e ver o resultado + evolucao.
- Orientador entrar, ver os relatorios do(s) estudante(s) e validar recomendacoes sugeridas.
- Dados persistirem (Postgres local) e o sistema funcionar sem perder historico.

## Checklist de conclusao (passos recomendados)

### PROGRESS UI (telas/fluxos)

#### Auth + sessao
- [x] Telas Auth: Login/Registo + layouts (Auth/Candidato/Orientador).
- [x] Reativar login real (validar credenciais via `Authentication.login`) e mensagens de erro no `LoginController`.
- [x] Sessao do utilizador (estado de quem esta logado + role).
- [x] Modo navegacao (sem BD): banner visivel e desativacao de registo/onboarding.

#### Onboarding
- [x] Avatar do candidato (upload/skip na UI).
- [x] Selecao de disciplinas (candidato) com cards.
- [x] Selecao de disciplina (orientador).
- [ ] Tratar UX quando BD falhar (mensagem clara + sugestao de ligar modo navegacao).

#### Candidato (core)
- [x] Dashboard (placeholder) + CSS base.
- [x] Diagnostico (experiencia de uso: lista/timeline/estatisticas + fluxo de perguntas).
- [x] Exame/Teste adaptativo (experiencia de uso + loading overlay).
- [x] Tela de configuracoes (editar/salvar + toggles por secao).
- [ ] Tela "Revisao do Diagnostico" (nova) antes de voltar ao menu.
- [ ] `views/pages/candidato/relatorios.fxml` (UI candidato: historico + detalhes).
- [ ] `views/pages/candidato/bolsas.fxml` (UI candidato: recomendacoes).

#### Orientador
- [x] Layout do orientador (dashboard/relatorios placeholder + navegar/sair).
- [ ] UI orientador: filtrar por candidato e comparar evolucao.

#### Qualidade/entrega
- [x] Overlay de carregamento na navegacao (`swapContent`).
- [ ] Prevenir perda de progresso (confirmar sair do teste + autosave quando fizer sentido).
- [ ] Gerar build executavel (jpackage/installer) ou instrucoes claras de distribuicao.

### PROGRESS DB (Postgres/persistencia)

#### Infra + migrations
- [x] Postgres local via `docker-compose.yml` + schema inicial (`scripts/db/001_schema.sql`).
- [x] Migrations Flyway versionadas (`V1...V6`).
- [x] Flyway `migrate()` no arranque (com `baseline` em V6 para bases criadas via Docker).
- [x] Chaves de runtime: `TESTE`/`DB_ENABLED` (ligar/desligar BD) e `DB_MIGRATE` (ligar/desligar migrations).
- [ ] Seeds minimos para demo (disciplinas + perguntas por disciplina).

#### Repositorios/CRUD
- [x] Base JDBC (`JdbcBasicSqlRepository`) + `UserRepository`, `DisciplinaRepository` e relacoes (candidato/orientador).
- [ ] Garantir operacoes minimas usadas no fluxo: criar, ler por id, listar por utilizador, atualizar.
- [ ] Banco de questoes:
  - [ ] Modelar pergunta: disciplina/topico/dificuldade/respostas/resposta correta/explicacao.
  - [ ] CRUD/importacao minima (JSON/CSV ou script).
- [ ] Persistencia do simulador:
  - [ ] Guardar sinais por questao (resposta, tempo, topico, dificuldade e tipo de erro).
  - [ ] Persistir testes concluidos (`testes` + `teste_perguntas`).

#### Ciclo do diagnostico -> relatorios
- [x] Tabela `diagnosticos` (V5) + indices.
- [ ] Tabela `configuracoes` (V6) + indices.
- [ ] Persistir o resultado real do diagnostico (UI -> tabela `diagnosticos`).
- [ ] Alimentar lista/timeline/estatisticas com dados reais (queries por candidato/periodo).
- [ ] Gerar relatorio derivado do diagnostico e persistir (`relatorios`).
- [ ] Usar o ultimo diagnostico como entrada do exame adaptativo.

#### Configuracoes (BD)
- [ ] Criar `ConfiguracaoRepository` (get/upsert por `user_id`).
- [ ] "Salvar alteracoes" na UI gravar em `configuracoes`.
- [ ] "Reiniciar para o padrao" (reset/override por defaults).

#### Recomendacoes (human-in-the-loop)
- [ ] Gerar recomendacoes sugeridas (por topico/dificuldade/erros) e persistir no relatorio.
- [ ] Orientador valida/rejeita recomendacoes (persistir no relatorio).
- [ ] Mostrar recomendacoes validadas ao candidato.

#### Qualidade (BD)
- [ ] Testes de unidade para analise (servicos) e persistencia (onde fizer sentido).


