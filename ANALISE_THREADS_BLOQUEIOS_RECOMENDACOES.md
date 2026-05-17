# Analise de Desempenho: Threads e Bloqueios

Data: 2026-05-17  
Escopo: avaliacao estatica do codigo JavaFX/JDBC/Gemini para identificar gargalos de concorrencia, bloqueios de UI e pontos de contencao.

## 1) Mapa atual de threads

### JavaFX Application Thread (UI)
- Responsavel por renderizacao e eventos de interface.
- Hoje esta executando operacoes pesadas de BD/rede em varios fluxos.

### `imetro-app-executor` (pool global da app)
- Definido como single-thread em `src/main/java/com/imetro/App.java:40`.
- Usado pelo `PerguntasBootstrapAsyncService` para `Task` de bootstrap em `src/main/java/com/imetro/services/PerguntasBootstrapAsyncService.java:101`.

### Workers de lotes Gemini
- `newFixedThreadPool(MAX_GEMINI_WORKERS)` em `src/main/java/com/imetro/services/PerguntasBootstrapService.java:438`.
- Espera bloqueante por `Future` em `src/main/java/com/imetro/services/PerguntasBootstrapService.java:377` e `:426`.

### Thread manual de cadastro
- `new Thread(registerTask)` em `src/main/java/com/imetro/ui/controller/auth/RegisterController.java:191`.

## 2) Principais bloqueios encontrados

## Critico

### C1) Bootstrap sincronizado disparado em caminho de leitura
- `carregarQuestoesReais(UUID)` chama `sincronizarDisciplinasAutomaticas` quando *nao* esta rodando (`!isRunningFor`) em:
  - `src/main/java/com/imetro/services/DiagnosticoService.java:86-90`
  - `src/main/java/com/imetro/services/DiagnosticoService.java:255-263`
- Esse bootstrap percorre disciplinas, I/O de ficheiros, BD e pode chamar Gemini (rede + retries + polling):
  - `src/main/java/com/imetro/services/PerguntasBootstrapService.java:73-191`
  - `src/main/java/com/imetro/services/PerguntasBootstrapService.java:193-317`
  - `src/main/java/com/imetro/services/GeminiService.java:449`
  - `src/main/java/com/imetro/services/GeminiService.java:650`
- Impacto: travamento perceptivel da UI ao abrir telas/acoes que so deveriam ler dados.

### C2) Chamadas de UI acionam esse caminho pesado repetidamente
- Diagnostico carrega banco no `Platform.runLater` (continua UI thread):
  - `src/main/java/com/imetro/ui/controller/candidato/diagnosticos/DiagnosticoCandidatoController.java:230-237`
- Teste adaptativo chama multiplas cargas sincronas na montagem de cards:
  - `src/main/java/com/imetro/ui/controller/candidato/testes/TesteAdaptativoController.java:216-224`
  - `src/main/java/com/imetro/ui/controller/candidato/testes/TesteAdaptativoController.java:227-270`
- Service adaptativo reforca chamadas repetidas:
  - `src/main/java/com/imetro/services/TesteAdaptativoService.java:30-42`
  - `src/main/java/com/imetro/services/TesteAdaptativoService.java:228-236`

## Alto

### A1) Persistencia final de diagnostico/teste ocorre na UI thread
- Diagnostico finaliza e grava tudo de forma sincrona:
  - `src/main/java/com/imetro/ui/controller/candidato/diagnosticos/DiagnosticoCandidatoController.java:562-567`
  - transacao em `src/main/java/com/imetro/services/DiagnosticoService.java:460-545`
- Teste adaptativo finaliza e grava de forma sincrona:
  - `src/main/java/com/imetro/ui/controller/candidato/testes/TesteAdaptativoController.java:793-802`
  - transacao em `src/main/java/com/imetro/services/TesteService.java:180-312`
- Impacto: freeze na transicao final e maior janela de lock no banco.

### A2) Consultas amplas e repetidas (full scan + filtro em memoria)
- `findAll()` em repositorio base:
  - `src/main/java/com/imetro/persistence/repository/JdbcBasicSqlRepository.java:96-103`
- `listDiagnotico()` puxa tudo e depois filtra:
  - `src/main/java/com/imetro/services/DiagnosticoService.java:551-563`
- Metricas chamam `listDiagnotico()` varias vezes:
  - `src/main/java/com/imetro/services/DiagnosticoService.java:773-859`
  - `src/main/java/com/imetro/ui/controller/candidato/diagnosticos/DiagnosticoStatics.java:109-112`
- Timeline idem:
  - `src/main/java/com/imetro/ui/controller/candidato/diagnosticos/DiagnosticoTimeline.java:93-102`

### A3) Sem pool de conexoes JDBC
- Cada operacao abre conexao via `DriverManager`:
  - `src/main/java/com/imetro/persistence/connection/Database.java:13-21`
- Impacto: overhead de handshake/latencia, piora sob carga e em loops.

## Medio

### M1) Executor global unico para tarefas da app
- `newSingleThreadScheduledExecutor`:
  - `src/main/java/com/imetro/App.java:40-48`
- Bom para serializar, mas cria fila unica para tudo que usar esse executor.

### M2) Trabalho de setup em `initialize` na UI
- Pastas/uploads no onboarding:
  - `src/main/java/com/imetro/ui/controller/auth/ChooseDisciplinasOnboardingController.java:69`
  - `src/main/java/com/imetro/ui/controller/auth/ChooseDisciplinasOnboardingController.java:94-98`
- Checagens de historico em inicializacao:
  - `src/main/java/com/imetro/ui/controller/candidato/CandidatoLayoutController.java:115-119`
  - `src/main/java/com/imetro/ui/controller/candidato/diagnosticos/DiagnosticoListController.java:77-87`

### M3) Cache global nao thread-safe
- `HashMap` estatico sem sincronizacao:
  - `src/main/java/com/imetro/domain/CacheService.java:6`
- Risco: comportamento indefinido se acessado por mais de uma thread.

## 3) Recomendacoes priorizadas

## Fase 1 (rapida, alto impacto)
1. Remover efeitos colaterais de bootstrap do caminho de leitura.
   - `DiagnosticoService.carregarQuestoesReais(UUID)` deve apenas ler.
   - Bootstrap automatico fica em trigger explicito (onboarding/botao/worker dedicado).
2. Mover gravacao final de diagnostico/teste para `Task` em background.
   - UI mostra overlay "A guardar resultado..." e so navega quando concluir.
3. Evitar recargas repetidas no `TesteAdaptativoController`.
   - Carregar banco uma vez por disciplina e reutilizar em memoria da tela.
4. Substituir `listDiagnotico()->filtrar em memoria` por query ja filtrada por candidato.

## Fase 2 (estrutura)
1. Introduzir pool JDBC (ex.: HikariCP) no lugar de `DriverManager` direto.
2. Separar executores por tipo de carga:
   - `uiIoExecutor` (DB/rede), `bootstrapExecutor`, `geminiLoteExecutor`.
3. Reusar executor de lotes Gemini em vez de criar/destruir por disciplina.
4. Adicionar cache de leitura por sessao (TTL curto + invalidacao por escrita).

## Fase 3 (observabilidade e governanca)
1. Instrumentar tempos:
   - tempo de query, tempo de bootstrap por disciplina, tempo de render por tela.
2. Logar alertas de UI lenta (>200ms em handlers de evento).
3. Definir SLO interno:
   - troca de tela < 300ms sem rede,
   - finalizacao de teste/diagnostico sem congelar UI.

## 4) Plano sugerido de implementacao (ordem segura)

1. Refatorar `carregarQuestoesReais(UUID)` para leitura pura e criar metodo explicito `agendarSincronizacaoSeNecessario(UUID)`.
2. Atualizar controllers (`DiagnosticoCandidatoController`, `TesteAdaptativoController`, `DiagnosticoListController`) para usar somente carregamento async.
3. Trocar metricas/timeline para repositorios com SQL filtrado por candidato.
4. Introduzir pool de conexoes e medir ganho.

## 5) Resultado esperado apos aplicar

- Menos travamentos na abertura das telas de diagnostico/teste.
- Queda relevante no tempo de resposta percebido na UI.
- Menos contencao de BD por reduzir transacoes longas no thread de interface.
- Comportamento de concorrencia mais previsivel e escalavel.

