# PROGRESSO — SimulatorBolsaStudy

Documento de acompanhamento do projeto (MVP → versão utilizável).

## Histórico (o que já foi feito)

### 2026-03-30
- Criado `README.md` com visão do produto, métricas do MVP e como executar.
- Implementada navegação por layouts JavaFX: Auth (Login/Registo) → Candidato/Orientador.
- Criadas páginas placeholder (dashboard/testes/relatórios) e CSS base.

### 2026-03-31
- Refactor para estrutura de packages mais “Java”:
  - `com.imetro.domain` (modelos) + `com.imetro.domain.enums`
  - `com.imetro.app` (fluxos/controllers de aplicação)
  - `com.imetro.ui.controller` (controllers FXML)
- Implementada classe `Relatorio` com campos para Candidato e Orientador.
- Adicionado Postgres local via `docker-compose.yml` + `.env.example`.
- Criado schema SQL do MVP (tabelas de candidatos/orientadores/relatórios/perguntas/testes) e integração de init no Docker.
- Adicionado as dependencias Postgres, Jfxextrax, Jfoenix

## Objetivo de “projeto terminado”

Para considerar o projeto “terminado” (MVP), o sistema deve permitir:

- Estudante entrar, fazer um simulado completo e ver o resultado + evolução.
- Orientador entrar, ver os relatórios do(s) estudante(s) e validar recomendações sugeridas.
- Dados persistirem (Postgres local) e o sistema funcionar sem perder histórico.

## Checklist de conclusão (passos recomendados)

### 1) Persistência real (Postgres)
- [ ] Definir se o MVP vai usar apenas SQL manual ou migrations (Flyway) quando o ambiente permitir dependências.
- [ ] Criar camada `persistence` com Repositories/DAOs:
  - [ ] `CandidatoRepository`
  - [ ] `OrientadorRepository`
  - [ ] `PerguntaRepository`
  - [ ] `TesteRepository`
  - [ ] `RelatorioRepository`
- [ ] Garantir operações mínimas: criar, ler por id, listar por utilizador, atualizar.
- [ ] Criar seeds mínimos (algumas perguntas por disciplina) para testar o fluxo.

### 2) Autenticação e perfis (local)
- [ ] Definir modelo de login (MVP):
  - [ ] email + password (hash) local no Postgres, ou
  - [ ] login “simples” (sem password) só para navegar (apenas para demo)
- [ ] Implementar fluxo de registo e login no UI.
- [ ] Criar sessão do utilizador (quem está logado + papel).

### 3) Banco de questões
- [ ] Modelar pergunta: disciplina/tópico/dificuldade/respostas/resposta correta/explicação.
- [ ] CRUD mínimo de perguntas (mesmo que só via script/import).
- [ ] Importação por JSON/CSV (opcional, mas acelera muito o MVP).

### 4) Simulador (core)
- [ ] Criar tela de configuração do teste (disciplinas, nº questões, tempo, dificuldade).
- [ ] Execução do teste:
  - [ ] mostrar pergunta
  - [ ] capturar resposta
  - [ ] medir tempo por pergunta
  - [ ] navegar próximo/anterior (se permitido)
- [ ] Finalização:
  - [ ] cálculo de resultado
  - [ ] revisão das perguntas (o que errou e porquê)

### 5) Análise e Relatórios
- [ ] Calcular as 5 métricas do MVP:
  - [ ] tempo médio
  - [ ] taxa de acerto por tópico
  - [ ] evolução semanal
  - [ ] erros recorrentes
  - [ ] dificuldade atingida
- [ ] Gerar `Relatorio` para cada teste e/ou por período.
- [ ] UI do candidato: ver evolução e histórico.
- [ ] UI do orientador: filtrar por candidato e comparar evolução.

### 6) Recomendações (human-in-the-loop)
- [ ] Gerar recomendações sugeridas (por tópico/dificuldade/erros).
- [ ] Orientador valida/rejeita recomendações (ficar guardado no relatório).
- [ ] Mostrar recomendações validadas ao candidato.

### 7) Qualidade e entrega
- [ ] Definir “dados mínimos” para demo (ex.: 50 perguntas por disciplina).
- [ ] Criar testes de unidade para a análise (serviços) e persistência (onde fizer sentido).
- [ ] Tratar erros e UX:
  - [ ] mensagens de validação no login/registo
  - [ ] estado de carregamento durante o teste
  - [ ] prevenir perda de progresso
- [ ] Gerar build executável (jpackage/installer) ou instruções claras de distribuição.

### 8) (Opcional) Supabase — sincronização / 2 PCs
- [ ] Definir o que sincroniza (utilizadores, testes, relatórios, perguntas).
- [ ] Criar projeto no Supabase e configurar acesso ao Postgres.
- [ ] Rever segurança (RLS no Supabase) para separar dados por utilizador/orientador.
- [ ] Ajustar `DB_URL` para `sslmode=require` e validar conexão.
