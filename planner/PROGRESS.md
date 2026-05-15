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

### 2026-04-29
- O onboarding de imagem foi trocado por escolha de avatar predefinido, com fallback automatico para iniciais quando o candidato ignora a etapa.
- O perfil do candidato passou a refletir avatar salvo ou iniciais dinamicas do nome.
- O exame adaptativo voltou a um fluxo orientado por modais, mais proximo da experiencia de diagnostico, com configuracao de foco, dificuldade, duracao e subtopicos.
- Os indicadores do card de teste deixaram de ser aleatorios, mas a dependencia forte de diagnosticos persistidos ainda continua pendente.
- As telas `bolsas` e `relatorios` deixaram de ser placeholder e ganharam uma primeira estrutura visual utilizavel com dados mockados consistentes.
- O perfil ganhou modo de edicao, troca de avatar por modal e um mural visual das 20 medalhas do sistema.
- Foi criada a base SQL de medalhas com catalogo seedado e tabela de conquistas por utilizador (`V9__medalhas.sql`).

### 2026-05-05
> *Horas: 15:49*
- Configurar lista para selecionar de acordo ao key
- Fazer primeiro diagnostico ser requirido ao entrar em uma conta sem diagnostico
- Alteracao da bd para levarem em conta mais os subtopicos adicionando os subtopico da pergunta

### 2026-05-06
> *Horas: 04:35*
- Foi realizada a pesquiza de como funciona as formulas das metricas velocidade, precisao, resiliecia, logica e resiliencia
- Foi adicionada o progresso por subtopico relacionando o progresso_rigor com a recomendacao_rigor assim obterndo um progresso do estado do diagnotico do candidato
- Foi marcado como proximo passo terminar metricas e timeline dos diagnosticos

### 2026-05-06
> *Horas: 15:45*
- Timeline actualizado de diagnotico ligado com a base de dados
- Update design do testes card

### 2026-05-07
> *Horas: 10:51*
- Update prompt of generated ask and answer
- Change use letter on selected corret answer by value of answer

### 2026-05-07
> *Horas: 14:42*
- All pages of diagnostico join on database
- Create dto for all parts of diagnotico

### 2026-05-13
- Revisao tecnica do estado atual do projeto com foco em desempenho, confiabilidade e ritmo de entrega.
- `mvn -q -DskipTests compile` passou nesta maquina.
- Entre `2026-04-20` e `2026-05-12`, o repositorio acumulou `33` commits, `27.364` linhas adicionadas e `8.463` removidas.
- Leitura atual: o lado do candidato esta mais perto de fecho do que o lado do orientador.
- Risco atual mais visivel: o fluxo de conclusao do teste adaptativo ainda pode falhar quando nao encontra o diagnostico-base da disciplina.
- Risco estrutural: o salvamento de `testes`, `stats` e `teste_perguntas` ainda precisa fechar transacao real numa unica ligacao.
- A configuracao adaptativa central (`V17`) ja existe na base, mas o motor ainda usa valores fixos em varios pontos.
- Decisao pratica atual: primeiro deixar o fluxo de `testes` claramente auditavel e localizar os hardcodes; depois ligar a leitura real da configuracao adaptativa e eliminar os valores fixos.
- Correcao de leitura: o onboarding do candidato ja persiste na BD atual por `users.avatar_url` e `progresso_aluno_disciplina`; o ponto em aberto e alinhar a fonte de verdade com `candidato_disciplinas`, se essa tabela continuar no schema.

### 2026-05-15
- O consumo de vetores `integer[]` do PostgreSQL em `progresso_aluno_disciplina` foi reforcado para aceitar `java.sql.Array`, arrays simples, listas e texto serializado.
- O exame adaptativo passou a usar um enum canonico de dificuldade com `FACIL`, `MEDIO`, `DIFICIL` e `EXPERT`, incluindo aliases legados no parse.
- O calculo de precisao deixou de ser apenas binario no fluxo novo: cada resposta pode carregar peso proprio e a melhoria passou a ser medida por precisao anterior vs precisao atual por questao.
- O historico detalhado de `teste_perguntas` e o resumo em `stats` passaram a suportar melhor a leitura de evolucao por pergunta.
- A geracao de perguntas por IA foi alargada para exigir pesos por alternativa e a base ganhou `V20__perguntas_pesos_resposta.sql`, sem remocao destrutiva de colunas antigas.
- `scripts/db/001_schema.sql` e `README.md` foram alinhados com a coluna nova `pesos_resposta` e com a retrocompatibilidade das perguntas legadas.


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
- [x] Avatar do candidato com escolha predefinida e fallback por iniciais.
- [x] Selecao visual de disciplinas do candidato.
- [x] Selecao visual de disciplina do orientador.
- [x] Persistencia real do onboarding com BD disponivel.
- [ ] UX clara quando a BD falhar em runtime.

#### Candidato (core)
- [x] Dashboard placeholder + CSS base.
- [x] Diagnostico com experiencia de uso completa em UI.
- [x] Exame/Teste adaptativo com loading overlay.
- [x] Exame/Teste adaptativo com configuracao por modal alinhada ao diagnostico.
- [x] Exame/Teste adaptativo com niveis canonicos `FACIL`, `MEDIO`, `DIFICIL` e `EXPERT`.
- [x] Tela de configuracoes com modo editar/salvar visual.
- [x] Perfil do candidato com avatar dinamico, modal de troca e mural inicial de medalhas.
- [x] Precisao parcial por resposta e melhoria por questao no fluxo novo de teste/diagnostico.
- [x] Perguntas novas geradas por IA com pesos por alternativa e fallback para legado.
- [ ] Tela "Revisao do Diagnostico".
- [x] `views/pages/candidato/relatorios.fxml` em versao inicial visual.
- [x] `views/pages/candidato/bolsas.fxml` em versao inicial visual.
- [ ] Ligar relatorios a dados reais de diagnostico e teste.
- [ ] Ligar bolsas a regras reais de elegibilidade, match e recomendacao.

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
- [x] Migrations Flyway versionadas (`V1...V20`).
- [x] Flyway `migrate()` no arranque com `baseline` em `V6`.
- [x] Chaves de runtime: `TESTE`, `DB_ENABLED` e `DB_MIGRATE`.
- [x] Documentacao para trabalhar sem BD local.
- [ ] Validar `V7`, `V8`, `V9` e `V20` em Postgres real neste fluxo atual.

#### Repositorios/CRUD
- [x] Base JDBC generica e repositorios baseados em tabela.
- [x] Consumo defensivo de vetores `integer[]` do Postgres no progresso do aluno.
- [ ] Garantir operacoes minimas usadas no fluxo.
- [ ] Banco de questoes unificado entre memoria e BD.
- [ ] Persistencia real do simulador (`testes` + `teste_perguntas`).
- [ ] Persistencia real de `progresso_aluno_disciplina`.

#### Ciclo do diagnostico -> relatorios
- [x] Tabela `diagnosticos` (`V5`).
- [x] Tabela `configuracoes` (`V6`).
- [x] Tabela `progresso_aluno_disciplina` entrou no historico de migrations (`V8`).
- [x] Catalogo de medalhas e conquistas por utilizador entraram no historico de migrations (`V9`).
- [ ] Persistir resultado real do diagnostico.
- [ ] Alimentar lista, timeline e estatisticas com dados reais.
- [ ] Gerar relatorio derivado do diagnostico.
- [ ] Usar o ultimo diagnostico como entrada do exame adaptativo.
- [ ] Automatizar atribuicao das medalhas a partir dos recordes reais do candidato.

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

## Prioridade atual de execucao

- fechar o fluxo de `testes` sem sucesso falso na UI e sem gravacao parcial
- manter os hardcodes do motor visiveis e marcados com `TODO CONFIG_ADAPTATIVA`
- ligar a configuracao adaptativa real do banco logo depois do fluxo de testes ficar estavel
- so depois expandir o lado do orientador com dados reais e comparacao de evolucao

## Foco recomendado enquanto nao houver BD neste PC

- manter `TESTE=false`
- continuar a evoluir diagnostico, exame adaptativo, relatorios e bolsas em memoria
- consolidar a regra de dependencia entre diagnostico e exame adaptativo antes da persistencia final
- fechar lacunas de documentacao e schema
- evitar avancar queries JDBC novas sem um Postgres real para validar

## Foco recomendado assim que houver Postgres

- validar `V7` e `V8`
- gravar diagnosticos reais
- trocar mocks da timeline, estatisticas, relatorios e bolsas por queries reais
- usar dados persistidos do diagnostico para conduzir o exame adaptativo
