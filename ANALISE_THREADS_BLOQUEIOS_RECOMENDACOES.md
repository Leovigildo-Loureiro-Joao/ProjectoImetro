# Analise de desempenho: threads, UI e bloqueios

Data de atualizacao: `2026-05-24`

Escopo: leitura estatica do codigo JavaFX, JDBC, bootstrap de perguntas e fecho dos fluxos de diagnostico/teste.

## 1. O que mudou desde a analise antiga

Melhorias ja introduzidas:

- `DiagnosticoService.carregarQuestoesReais()` deixou de disparar bootstrap pesado
- o bootstrap automatico passou a ser agendado por `agendarSincronizacaoSeNecessario(...)`
- `DiagnosticoListController` ja carrega a lista em background com `CompletableFuture`
- o runtime foi simplificado para candidato unico, reduzindo um fluxo inteiro de UI e BD

Conclusao:

- o principal gargalo antigo de "leitura que dispara processamento pesado" foi reduzido
- ainda existem pontos importantes de bloqueio de UI e de atomicidade incompleta

## 2. Mapa atual de threads

### JavaFX Application Thread

- renderizacao
- eventos da interface
- ainda recebe trabalho sincrono em pontos de diagnostico e teste

### `imetro-app-executor`

- executor global single-thread definido em `App`
- bom para serializar tarefas simples
- pode virar fila unica quando muito fluxo concorre no mesmo executor

### `EXECUTOR_DIAGNOSTICO`

- executor dedicado do diagnostico, tambem single-thread
- usado para cargas async do diagnostico e para agendar bootstrap automatico

### Workers de lotes Gemini

- `PerguntasBootstrapService` cria pool fixo para lotes Gemini
- o numero de workers esta limitado e o comportamento e previsivel

### Common pool do `CompletableFuture`

- `TesteAdaptativoController` ainda usa `CompletableFuture.supplyAsync(...)` sem executor explicito em alguns pontos
- isso deixa parte do comportamento dependente do common pool da JVM

## 3. Bloqueios e riscos atuais

## Critico

### C1) Fecho de diagnostico ainda roda em fluxo sincrono de UI

- `DiagnosticoCandidatoController.finalizarDiagnostico()` chama `diagnosticoService.registrarDiagnosticoConcluido(...)` diretamente
- `DiagnosticoService.registrarDiagnosticoConcluido(...)` abre conexao, inicia transacao e grava tudo no banco

Impacto:

- risco de congelamento perceptivel ao concluir diagnostico

### C2) Fecho de teste ainda roda em fluxo sincrono de UI

- `TesteAdaptativoController` chama `testeService.registrarTesteConcluido(...)` diretamente antes de navegar
- `TesteService` abre conexao, grava `testes`, `stats` e `teste_perguntas`

Impacto:

- risco de freeze no fim do teste
- o utilizador espera gravacao e troca de tela no mesmo handler

### C3) `TestePerguntasRepository` ainda quebra a transacao unica

- `TesteService` abre transacao principal com `Connection`
- `TesteStatsRepository` ja aceita `Connection`
- `TestePerguntasRepository.inserir(...)` ainda usa o caminho que abre ligacao propria

Impacto:

- atomicidade incompleta
- latencia extra por abertura de conexoes adicionais
- possibilidade de ficar `testes`/`stats` gravados sem todo o detalhe de `teste_perguntas`

## Alto

### A1) Carregamento inicial do diagnostico ainda faz leitura pesada na UI

- `DiagnosticoCandidatoController` ainda chama `diagnosticoService.carregarQuestoesReais()` no fluxo de inicializacao/preparacao
- embora hoje seja leitura pura, continua a ser I/O de BD na thread de interface

### A2) Executores single-thread podem acumular fila

- `EXECUTOR`
- `EXECUTOR_DIAGNOSTICO`

Impacto:

- previsibilidade boa
- throughput limitado quando houver varias acoes concorrentes

### A3) Ainda nao ha pool de conexoes JDBC

- o projeto continua a usar abertura direta de conexao

Impacto:

- overhead extra
- maior custo em fluxos que fazem varias escritas ou leituras em sequencia

## Medio

### M1) `TesteAdaptativoController` mistura UI, carga async e salvamento

- a tela carrega disciplinas de forma async
- mas ainda conclui o teste de forma sincrona
- isso deixa o fluxo inconsistente do ponto de vista de responsividade

### M2) Bootstrap e diagnostico partilham um executor single-thread

- bom para evitar concorrencia excessiva
- ruim se o bootstrap demorar e atrasar cargas do diagnostico

## 4. Recomendacoes priorizadas

### Fase 1

1. mover `registrarDiagnosticoConcluido(...)` para `Task` ou `CompletableFuture` com overlay de "A guardar"
2. mover `registrarTesteConcluido(...)` para background antes da troca de tela
3. criar `TestePerguntasRepository.inserir(Connection, ...)` e usar a mesma transacao principal
4. escolher executor explicito para os `CompletableFuture` do teste

### Fase 2

1. retirar `carregarQuestoesReais()` da inicializacao sincrona do diagnostico
2. separar melhor executores de UI, diagnostico e bootstrap
3. introduzir pool de conexoes JDBC

### Fase 3

1. medir tempos de bootstrap por disciplina
2. medir tempos de finalizacao de diagnostico/teste
3. alertar quando handlers de UI passarem de uma janela razoavel

## 5. Resultado esperado

Se estes pontos forem fechados:

- menos congelamentos no fim de diagnosticos e testes
- gravacao de teste realmente atomica
- menor acoplamento entre UI e persistencia
- comportamento mais previsivel quando a base real e o Gemini estiverem em uso
