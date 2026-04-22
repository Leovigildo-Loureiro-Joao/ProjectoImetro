# Projecto Imetro

Aplicacao desktop em JavaFX para apoio academico de candidatos e orientadores, com onboarding por perfil, diagnosticos por disciplina, estatisticas de desempenho e persistencia em PostgreSQL.

## Estado atual

- navegacao base entre autenticacao, area do candidato e area do orientador
- onboarding com selecao de disciplinas e avatar
- modulo de diagnostico academico no candidato com:
  - lista de diagnosticos
  - linha do tempo
  - estatisticas
  - fluxo de perguntas e resultado final
- persistencia JDBC para utilizadores, disciplinas, perguntas, testes e relatorios
- schema SQL versionado em `src/main/resources/db/migration`

## Stack

- Java 21
- JavaFX 23
- Maven
- JFoenix, ControlsFX, Ikonli e TilesFX
- PostgreSQL
- MyBatis
- Flyway (dependencias adicionadas; execucao automatica ainda nao esta ligada no arranque da app)

## Requisitos

- JDK 21
- Maven 3.9+ para executar localmente
- Docker Desktop (opcional, para base local e para usar a imagem de Maven)

## Como executar

```bash
mvn clean javafx:run
```

### Modo BD vs modo navegação

A app pode rodar em 2 modos:

- **BD ligada**: usa Postgres (onboarding completo, registo/login real, persistência).
- **Modo navegação (sem BD)**: não tenta conectar na BD e serve para navegar/validar UI quando o ambiente (WSL/Docker/Postgres) estiver instável.

#### Como ligar/desligar

O projeto lê variáveis de ambiente e também `.env.local` / `.env` (ver `com.imetro.config.Env`).

- `DB_ENABLED=true|false` (se existir, **tem prioridade**)
- `TESTE=true|false` (alias simples: `true` liga BD; `false` entra em modo navegação)

Exemplos:

```env
# Modo navegação (sem BD)
TESTE=false
```

```env
# BD ligada
TESTE=true
DB_URL=jdbc:postgresql://localhost:5432/simulatorbolsastudy
DB_USER=simulator
DB_PASSWORD=simulator
```

Quando a BD estiver desligada, a UI mostra um banner “Modo navegação: BD desligada” e o registo fica desativado para evitar erros.

## Maven sem instalacao local

Para PCs que ainda nao tem Maven instalado, o projeto agora inclui uma imagem Docker propria em `docker/maven/Dockerfile` e um servico `maven` no `docker-compose.yml`.

### Construir a imagem

```bash
docker compose --profile tools build maven
```

### Executar comandos Maven via Docker

```bash
docker compose --profile tools run --rm maven clean compile
docker compose --profile tools run --rm maven clean package -DskipTests
```

As dependencias baixadas pelo Maven ficam persistidas no volume `maven_cache`, o que evita downloads completos a cada execucao.

Nota: esta imagem foi preparada para compilar, testar e empacotar o projeto. A execucao da interface JavaFX desktop a partir do container nao esta configurada neste momento.

## Base de dados

### Subir Postgres local

```bash
docker compose up -d
```

Na primeira criacao do volume, o container executa automaticamente `scripts/db/001_schema.sql`.

### Variaveis de ambiente

Usa `.env.example` como referencia:

```env
DB_URL=jdbc:postgresql://localhost:5432/simulatorbolsastudy
DB_USER=simulator
DB_PASSWORD=simulator
DB_ENABLED=true
# Alternativa simples:
#TESTE=true
```

### Migrations

- schema inicial do Docker: `scripts/db/001_schema.sql`
- migrations versionadas: `src/main/resources/db/migration`
- nova migration adicionada para diagnosticos: `V5__diagnosticos.sql`
- nova migration adicionada para configuracoes: `V6__configuracoes.sql`

Observacao importante: embora o projeto ja tenha as dependencias de `Flyway`, nao encontrei no codigo atual a execucao automatica de `migrate()` no arranque. Hoje, o Docker usa o schema inicial no primeiro boot, e migrations incrementais precisam ser aplicadas manualmente numa base ja existente.

Atualizacao: a app agora chama `Flyway.migrate()` no arranque quando a BD estiver ligada. Para compatibilidade com o schema criado via Docker, o Flyway faz `baseline` em V6 quando encontra uma base nao vazia sem historico, e aplica apenas migrations futuras. Se quiser desligar isso, define `DB_MIGRATE=false`.

### Aplicar SQL manualmente

Se a base ja existia antes desta alteracao, aplica a migration nova manualmente:

```bash
psql -h localhost -p 5432 -U simulator -d simulatorbolsastudy -f src/main/resources/db/migration/V5__diagnosticos.sql
```

Se preferires recriar uma base local do zero, remove o volume do Docker e sobe novamente para que `scripts/db/001_schema.sql` seja reexecutado.

## Tabela de diagnosticos

A tabela `diagnosticos` foi preparada para guardar o historico do modulo de diagnostico academico:

- candidato e disciplina do diagnostico
- horario de inicio e conclusao
- duracao, total de questoes, acertos e erros
- percentual de acerto e evolucao
- nivel final
- metricas de velocidade, precisao, consistencia, logica e resiliencia
- respostas em `jsonb`

## Estrutura principal

- `src/main/java/com/imetro/App.java`: arranque da aplicacao JavaFX
- `src/main/java/com/imetro/persistence`: conexao e repositorios JDBC
- `src/main/java/com/imetro/services`: regras de negocio e cargas de perguntas
- `src/main/java/com/imetro/ui/controller`: controllers JavaFX
- `src/main/resources/com/imetro/views`: layouts, paginas e componentes FXML
- `src/main/resources/db/migration`: migrations SQL versionadas
- `scripts/db`: schema usado pelo container Postgres no primeiro boot

## Proximos passos recomendados

- ligar `Flyway.migrate()` no arranque para aplicar `V1...V5` automaticamente
- persistir o resultado real do diagnostico da UI na tabela `diagnosticos`
- criar repositorio/servico para alimentar a linha do tempo e estatisticas a partir da base

## Licenca

Licenca ainda nao definida.
