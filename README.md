# Projecto Imetro

Aplicacao desktop em JavaFX para apoio academico de candidatos e orientadores, com onboarding por perfil, diagnosticos por disciplina, estatisticas de desempenho e persistencia em PostgreSQL.

## Estado atual

- navegacao base entre autenticacao, area do candidato e area do orientador
- onboarding com avatar e selecao visual de disciplinas
- modulo de diagnostico no candidato com lista, timeline, estatisticas, perguntas e resultado final
- exame adaptativo com foco por disciplina, topico e subtopico
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
- estado atual das migrations: `V1` ate `V8`

Resumo das mais recentes:

- `V5__diagnosticos.sql`: tabela `diagnosticos`
- `V6__configuracoes.sql`: tabela `configuracoes`
- `V7__perguntas_topico_subtopico.sql`: normalizacao minima de `disciplina` e `subtopico` em `perguntas`
- `V8__progresso_aluno_disciplina.sql`: historico de progresso do aluno por disciplina

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
- melhorar o fluxo de resultado e recomendacoes
- refinar regras do exame adaptativo
- manter `001_schema.sql`, migrations e README sempre alinhados

## O que fica para quando houver Postgres

- validar `V7` e `V8` em base real
- persistir o resultado real do diagnostico
- alimentar timeline e estatisticas com queries reais
- persistir o onboarding de disciplinas do candidato
- ligar relatorios e recomendacoes ao orientador

## Licenca

Licenca ainda nao definida.
