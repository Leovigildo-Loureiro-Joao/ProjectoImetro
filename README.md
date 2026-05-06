# Projecto Imetro

Aplicacao desktop em JavaFX para apoio academico de candidatos e orientadores, com onboarding por perfil, diagnosticos por disciplina, exame adaptativo guiado por foco, relatorios visuais de desempenho e persistencia em PostgreSQL.

## Estado atual

- navegacao base entre autenticacao, area do candidato e area do orientador
- onboarding com escolha de avatar predefinido, fallback por iniciais e selecao visual de disciplinas
- modulo de diagnostico no candidato com lista, timeline, estatisticas, perguntas e resultado final
- exame adaptativo com configuracao por modal, foco por disciplina/topico/subtopico e arranque alinhado ao fluxo de diagnostico
- tela de relatorios do candidato com resumo, graficos e insights iniciais
- tela de bolsas do candidato com prontidao, match visual e proximos passos
- perfil do candidato com troca de avatar por modal e mural inicial de 20 medalhas do sistema
- persistencia JDBC para `users`, `disciplinas`, `perguntas`, `testes` e `relatorios`
- schema SQL versionado em `src/main/resources/db/migration`

## Stack

- Java 21
- JavaFX 23
- Maven
- JFoenix, ControlsFX, Ikonli e TilesFX
- PostgreSQL
- MyBatis
- Flyway

## Formulas oficiais do motor adaptativo

Estas formulas passam a ser a referencia oficial do projecto para calculo de metricas. Elas substituem as simplificacoes temporarias que ainda existam no codigo.

### Convencoes

- Todas as metricas de desempenho devem trabalhar, por padrao, na escala `[0, 1]`, exceto quando explicitamente marcadas como `raw`.
- Para qualquer divisao com risco de denominador zero, usar `epsilon > 0` na implementacao pratica.
- `i` representa um subtopico.
- `n` representa a ordem cronologica do teste dentro de uma sequencia.

### Precisao por subtopico

`P_i = acertos_i / totais_i`

Onde:

- `acertos_i`: quantidade de respostas corretas no subtopico `i`
- `totais_i`: quantidade total de questoes respondidas no subtopico `i`

### Precisao geral

Media simples entre subtopicos:

`P_geral = (P_1 + P_2 + ... + P_n) / n`

Media ponderada por volume real de questoes:

`P_ponderada = (sum acertos_i) / (sum totais_i)`

Regra de uso:

- `P_geral` serve para comparar equilibrio entre subtopicos.
- `P_ponderada` serve para decisao global, porque respeita o peso real do volume resolvido.

### Logica

`L = acertos_baixa_estruturacao / total_baixa_estruturacao`

Onde:

- `baixa_estruturacao` representa questoes com menor apoio de padrao direto, maior exigencia de inferencia ou maior liberdade de raciocinio.
- `L` mede a capacidade de resolver questoes menos mecanicas.

### Resiliencia

`R = ((P_2 + P_3) / 2) / P_1`

Leitura:

- `P_1`: desempenho inicial de referencia
- `P_2` e `P_3`: desempenhos seguintes no mesmo foco de recuperacao
- `R > 1`: houve recuperacao acima da linha de base
- `R = 1`: recuperacao neutra
- `R < 1`: recuperacao incompleta

Versao operacional segura:

`R = ((P_2 + P_3) / 2) / max(epsilon, P_1)`

### Velocidade

Medida bruta:

`V_raw = tempo_medio_por_acerto`

Se `tempo_total` estiver em segundos:

`V_raw = tempo_total / max(1, total_acertos)`

Forma relativa contra uma referencia:

`V_rel = T_base / T_usuario`

Forma inversa:

`V_inv = 1 / V_raw`

Regra de uso:

- `V_raw` e a medida operacional principal.
- `V_rel` e a forma recomendada para score comparavel entre candidatos, disciplinas ou fases.
- `V_inv` e util quando o modelo precisar tratar velocidade como taxa em vez de tempo.

### Consistencia

Ganhos entre testes consecutivos:

`Delta_P_n = P_(n+1) - P_n`

Coeficiente de variacao dos ganhos:

`CV_ganhos = stddev(Delta_P) / media(Delta_P)`

Correlacao positiva entre ordem do teste e desempenho:

`corr+(n, P_n) = max(0, corr_Pearson(n, P_n))`

Formula conceitual:

`C = corr+(n, P_n) * (1 - CV_ganhos)`

Versao operacional recomendada:

`CV_ganhos = stddev(Delta_P) / max(epsilon, abs(media(Delta_P)))`

`C = corr+(n, P_n) * (1 - min(1, CV_ganhos))`

Interpretacao:

- `C` proximo de `1`: evolucao suave, progressiva e estavel
- `C` baixo: evolucao erratica, com oscilacoes ou quedas

### Ganho normalizado

`G = (P_atual - P_anterior) / (1 - P_anterior)`

Versao operacional segura:

`G = (P_atual - P_anterior) / max(epsilon, 1 - P_anterior)`

Interpretacao:

- `G = 0`: nao houve ganho real
- `G > 0`: houve crescimento
- `G < 0`: houve regressao

### Uso esperado no produto

- `P_i` deve ser a base do progresso por subtopico.
- `P_ponderada` deve ser a metrica global preferencial para decidir evolucao real.
- `G` deve medir quanto o aluno subiu em relacao ao ponto anterior, sem premiar artificialmente quem ja estava alto.
- `R` deve mostrar capacidade de recuperacao apos falhas ou repeticoes.
- `C` deve separar crescimento real de crescimento instavel.
- `L` deve medir raciocinio em questoes menos estruturadas.
- `V_rel` deve ser a forma principal de velocidade usada em score.

## Cenario atual sem PostgreSQL local

Se neste PC ainda nao tens PostgreSQL disponivel, o caminho recomendado e trabalhar em `modo navegacao`.

Nesse modo, a app:

- nao tenta abrir ligacao JDBC
- permite navegar e validar a UI
- deixa o registo real desligado para evitar erros
- continua util para evoluir controllers, FXML, CSS, payloads e regras em memoria

Isto significa que, neste PC, o trabalho seguro e:

- evoluir UI e UX
- refinar diagnostico e exame adaptativo
- manter migrations e documentacao alinhadas
- preparar DTOs, repositorios e contratos de persistencia

Sem um Postgres real, o que depende de JDBC ou Flyway fica apenas preparado e documentado, mas nao validado ponta a ponta.

## Como executar

```bash
mvn clean javafx:run
```

### Modo BD vs modo navegacao

A app pode rodar em 2 modos:

- **BD ligada**: usa Postgres para login, onboarding persistente, historico e dados reais.
- **Modo navegacao**: nao tenta conectar na BD e serve para navegar e validar a interface.

### Como ligar ou desligar

O projeto le variaveis de ambiente e tambem `.env.local` / `.env`.

- `DB_ENABLED=true|false`: se existir, tem prioridade
- `TESTE=true|false`: alias simples; `true` liga BD e `false` entra em modo navegacao

Exemplo recomendado para este PC:

```env
TESTE=false
```

Exemplo quando o Postgres estiver pronto:

```env
TESTE=true
DB_URL=jdbc:postgresql://localhost:5432/simulatorbolsastudy
DB_USER=simulator
DB_PASSWORD=simulator
DB_MIGRATE=true
```

Observacao pratica:

- no modo navegacao, usa credenciais nao vazias apenas para entrar na interface
- o login nesse modo nao valida utilizador real na BD

## `.env.example`

O ficheiro `.env.example` foi ajustado para um arranque mais seguro em maquinas sem PostgreSQL local.

Passos:

```bash
Copy-Item .env.example .env
```

Depois confirma que estas a usar:

```env
TESTE=false
```

## Maven sem instalacao local

Para PCs sem Maven instalado, o projeto inclui uma imagem propria em `docker/maven/Dockerfile` e um servico `maven` no `docker-compose.yml`.

### Construir a imagem

```bash
docker compose --profile tools build maven
```

### Executar Maven via Docker

```bash
docker compose --profile tools run --rm maven clean compile
docker compose --profile tools run --rm maven clean package -DskipTests
```

Nota: a execucao da interface JavaFX desktop a partir do container nao esta configurada neste momento.

## Base de dados

### Quando o Postgres estiver disponivel

```bash
docker compose up -d
```

Na primeira criacao do volume, o container executa automaticamente `scripts/db/001_schema.sql`.

### Migrations

- schema inicial do Docker: `scripts/db/001_schema.sql`
- migrations versionadas: `src/main/resources/db/migration`
- estado atual das migrations: `V1` ate `V14`

Resumo das mais recentes:

- `V5__diagnosticos.sql`: tabela `diagnosticos`
- `V6__configuracoes.sql`: tabela `configuracoes`
- `V7__perguntas_topico_subtopico.sql`: normalizacao minima de `disciplina` e `subtopico` em `perguntas`
- `V8__progresso_aluno_disciplina.sql`: historico de progresso do aluno por disciplina
- `V9__medalhas.sql`: catalogo de medalhas e conquistas por utilizador
- `V10__seed_disciplinas.sql`: seed inicial de disciplinas
- `V11__rigor_adaptativo.sql`: base do rigor adaptativo e recomendacoes por foco
- `V12__testes_stats.sql`: extensao de `testes`, `teste_perguntas` e tabela `stats`
- `V13__rigor_adaptativo.sql`: migracao de `topico` para `subtopico` no fluxo adaptativo
- `V14__schema_cleanup_guardrails.sql`: limpeza defensiva de legado e guardrails de schema

### Observacao importante sobre ambiente

Nesta maquina, em `2026-04-26`, as migrations foram alinhadas no repositorio, mas nao foram validadas contra um PostgreSQL real neste proprio PC.

Em outras palavras:

- o historico SQL ficou mais consistente
- a documentacao ficou preparada para desenvolvimento sem BD local
- a validacao JDBC e Flyway real continua pendente ate haver Postgres disponivel

### Flyway no arranque

A app chama `Flyway.migrate()` no arranque quando a BD estiver ligada.

Para compatibilidade com bases criadas a partir de `scripts/db/001_schema.sql`, o projeto usa `baseline` em `V6` e deixa as migrations incrementais acima disso correrem de forma idempotente.

Se precisares desligar migrations automaticas:

```env
DB_MIGRATE=false
```

## Estrutura principal

- `src/main/java/com/imetro/App.java`: arranque da aplicacao JavaFX
- `src/main/java/com/imetro/persistence`: conexao e repositorios JDBC
- `src/main/java/com/imetro/services`: regras de negocio
- `src/main/java/com/imetro/ui/controller`: controllers JavaFX
- `src/main/resources/com/imetro/views`: layouts, paginas e componentes FXML
- `src/main/resources/db/migration`: migrations SQL versionadas
- `scripts/db`: schema usado pelo container Postgres no primeiro boot

## O que faz sentido focar sem BD

- fechar telas e navegacao do candidato
- criar a futura tela de revisao do diagnostico
- ligar relatorios e bolsas a dados em memoria mais proximos do comportamento real
- reforcar a regra de negocio entre diagnostico, exame adaptativo e recomendacoes
- manter `001_schema.sql`, migrations e README sempre alinhados

## O que fica para quando houver Postgres

- validar `V7` e `V8` em base real
- persistir o resultado real do diagnostico
- alimentar timeline, estatisticas, relatorios e bolsas com queries reais
- validar a persistencia real do onboarding de avatar e disciplinas do candidato
- usar o ultimo diagnostico persistido como entrada forte do exame adaptativo
- ligar relatorios e recomendacoes ao orientador

## Licenca

Licenca ainda nao definida.
