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

## Objetivo de "projeto terminado"

Para considerar o projeto "terminado" (MVP), o sistema deve permitir:

- Estudante entrar, fazer um simulado completo e ver o resultado + evolucao.
- Orientador entrar, ver os relatorios do(s) estudante(s) e validar recomendacoes sugeridas.
- Dados persistirem (Postgres local) e o sistema funcionar sem perder historico.

## Checklist de conclusao (passos recomendados)

### 1) Persistencia real (Postgres)
- [x] Definir se o MVP vai usar apenas SQL manual ou migrations (Flyway) quando o ambiente permitir dependencias.
- [x] Criar camada `persistence` com Repositories/DAOs:
  - [x] `CandidatoRepository`
  - [x] `OrientadorRepository`
  - [x] `PerguntaRepository`
  - [x] `TesteRepository`
  - [x] `RelatorioRepository`
- [ ] Garantir operacoes minimas: criar, ler por id, listar por utilizador, atualizar.
- [ ] Criar seeds minimos (algumas perguntas por disciplina) para testar o fluxo.

### 2) Autenticacao e perfis (local)
- [x] Definir modelo de login (MVP):
  - [x] email + password (hash) local no Postgres, ou
  - [x] login "simples" (sem password) so para navegar (apenas para demo)
- [x] Implementar fluxo de registo e login no UI.
- [x] Criar sessao do utilizador (quem esta logado + papel).

### 3) Banco de questoes
- [ ] Modelar pergunta: disciplina/topico/dificuldade/respostas/resposta correta/explicacao.
- [ ] CRUD minimo de perguntas (mesmo que so via script/import).
- [ ] Importacao por JSON/CSV (opcional, mas acelera muito o MVP).

### 4) Simulador (core)
- [x] Criar tela de configuracao do teste (disciplinas, n questoes, tempo, dificuldade).
- [ ] Execucao do teste:
  - [ ] mostrar pergunta
  - [ ] capturar resposta
  - [ ] medir tempo por pergunta
  - [ ] navegar proximo/anterior (se permitido)
- [ ] Finalizacao:
  - [ ] calculo de resultado
  - [ ] revisao das perguntas (o que errou e porque)
  - [ ] criar uma tela de "Revisao do Diagnostico" antes de voltar ao menu

### 5) Analise e Relatorios
- [ ] Fechar o ciclo do diagnostico como base do sistema:
  - [ ] persistir cada diagnostico concluido
  - [ ] guardar sinais por questao (resposta, tempo, topico, dificuldade e tipo de erro)
  - [ ] gerar relatorio derivado do diagnostico
  - [ ] alimentar lista/timeline/estatisticas com dados reais
- [ ] Calcular as 5 metricas do MVP:
  - [ ] tempo medio
  - [ ] taxa de acerto por topico
  - [ ] evolucao semanal
  - [ ] erros recorrentes
  - [ ] dificuldade atingida
- [ ] Gerar `Relatorio` para cada teste e/ou por periodo.
- [ ] UI do candidato: ver evolucao e historico.
- [ ] UI do orientador: filtrar por candidato e comparar evolucao.

### 6) Recomendacoes (human-in-the-loop)
- [ ] Gerar recomendacoes sugeridas (por topico/dificuldade/erros).
- [ ] Orientador valida/rejeita recomendacoes (ficar guardado no relatorio).
- [ ] Mostrar recomendacoes validadas ao candidato.
- [ ] Ligar as recomendacoes e o relatorio ao exame adaptativo seguinte.

### 7) Qualidade e entrega
- [ ] Definir "dados minimos" para demo (ex.: 50 perguntas por disciplina).
- [ ] Criar testes de unidade para a analise (servicos) e persistencia (onde fizer sentido).
- [ ] Tratar erros e UX:
  - [ ] mensagens de validacao no login/registo
  - [ ] estado de carregamento durante o teste
  - [ ] prevenir perda de progresso
- [ ] Gerar build executavel (jpackage/installer) ou instrucoes claras de distribuicao.

### 8) (Opcional) Supabase - sincronizacao / 2 PCs
- [ ] Definir o que sincroniza (utilizadores, testes, relatorios, perguntas).
- [ ] Criar projeto no Supabase e configurar acesso ao Postgres.
- [ ] Rever seguranca (RLS no Supabase) para separar dados por utilizador/orientador.
- [ ] Ajustar `DB_URL` para `sslmode=require` e validar conexao.
