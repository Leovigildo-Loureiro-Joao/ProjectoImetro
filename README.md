# SimulatorBolsaStudy

Simulador de exames de bolsas (MVP) para apoiar a preparação de estudantes com **análise adaptativa** e **feedback orientado a dados** para Estudante e Orientador.

> Nota (estado atual): este repositório já tem a navegação base por layouts (Auth → Candidato/Orientador) e páginas placeholder. As funcionalidades de simulador, banco de questões e análise ainda estão em desenvolvimento.

## Objetivo

- Ajudar o **Estudante** a treinar para testes/exames de bolsas (genérico) em áreas como: Matemática, Português, Física, Química, Inglês e Cultura Geral.
- Dar ao **Orientador** visibilidade do progresso e dos padrões de erro, para orientar o estudante com base em evidências.

## Perfis no sistema

- **Estudante**: realiza simulados/exames, responde questões e acompanha o próprio desempenho ao longo do tempo.
- **Orientador**: consulta relatórios e valida recomendações sugeridas pelo sistema (human-in-the-loop), tornando a intervenção mais certeira.

## Métricas mínimas (MVP)

- **Tempo médio** por questão e por sessão
- **Taxa de acerto por tópico**
- **Evolução semanal** (tendência de acerto/tempo)
- **Erros recorrentes** (padrões por tópico e tipo de questão)
- **Dificuldade atingida** (progressão e estabilização)

## Como funciona (visão de produto)

O sistema recolhe dados de desempenho (ex.: acerto/erro, tempo, tópicos) e usa esses sinais para:

- Ajustar a seleção de questões (dificuldade/tópicos) de forma **adaptativa**
- Gerar recomendações de estudo e exercícios
- Produzir relatórios para Estudante e Orientador, destacando pontos fortes e fracos

## Armazenamento de dados

- **Inicialmente (MVP):** multiutilizador no **mesmo PC** (perfis locais).
- **Evolução possível:** sincronização entre dois PCs usando **Supabase** (armazenamento/autenticação), mantendo a análise e a experiência focadas no simulador.

## Tecnologias

- Java 11
- JavaFX
- Maven

## Executar o projeto

### Pré-requisitos

- JDK 11 instalado
- Maven instalado

### Comando

```bash
mvn clean javafx:run
```

## Base de dados (PostgreSQL / Supabase)

O projeto está preparado para usar Postgres local no MVP e, no futuro, apontar para o Postgres do Supabase.

> Nota: neste momento o repositório usa **JDBC puro** (sem HikariCP/Flyway no `pom.xml`) para não depender de downloads de dependências no ambiente. Quando quiseres “produção a sério”, adicionamos: PostgreSQL JDBC driver + HikariCP (pool) + Flyway (migrations automáticas).

### Postgres local (recomendado para MVP)

1) Subir o banco:

```bash
docker compose up -d
```

Na primeira vez que o volume estiver vazio, o container executa automaticamente `scripts/db/001_schema.sql`.

2) Configurar variáveis de ambiente (exemplo):

- copia `.env.example` e exporta no teu terminal/OS
- define pelo menos: `DB_ENABLED=true`, `DB_URL`, `DB_USER`, `DB_PASSWORD`

> Quando `DB_ENABLED=true` e as variáveis existirem, o app tenta conectar (warmup). As migrations estão em `src/main/resources/db/migration`.

### Executar o schema manualmente (se já tinhas volume/criado antes)

Se o teu volume já existia antes de montares `scripts/db`, o init automático não corre. Executa manualmente:

```bash
psql -h localhost -p 5432 -U simulator -d simulatorbolsastudy -f scripts/db/001_schema.sql
```

### Supabase (quando for para 2 PCs)

No Supabase, vais usar o Postgres gerido por eles. O fluxo recomendado é:

- criar o projeto no Supabase
- pegar no host/porta/user/password do Postgres
- apontar o `DB_URL` (JDBC) com `sslmode=require`
- manter migrations no repositório (Flyway) para versionar o schema

## Estrutura do projeto

- `src/main/java/com/imetro/App.java`: entrypoint JavaFX
- `src/main/java/com/imetro/app`: fluxo/navegação e controllers de aplicação (não-UI)
- `src/main/java/com/imetro/domain`: modelos puros de domínio (ex.: Pergunta, Teste, Relatorio)
- `src/main/java/com/imetro/services`: casos de uso (análise, recomendações, relatórios)
- `src/main/java/com/imetro/persistence`: acesso a dados (Postgres/Supabase), DataSource e migrations
- `src/main/java/com/imetro/ui/controller`: controllers das views JavaFX (FXML)
- `src/main/resources/com/imetro/views/layouts`: layouts principais (Auth, Candidato, Orientador)
- `src/main/resources/com/imetro/views/pages`: subpáginas de cada perfil (ex.: dashboard, testes, relatórios)
- `src/main/resources/com/imetro/styles`: CSS global e CSS por layout

## Roadmap (sugestão)

1. **Modelo de domínio**
   - Perfis (Estudante/Orientador), Sessão, Questão, Resposta, Tópico, Métricas
2. **Banco de questões**
   - Importação/CRUD, tags por tópico, nível de dificuldade, explicações
3. **Simulador**
   - Configuração de prova, temporizador, submissão, correção e revisão
4. **Análise**
   - Cálculo de métricas, detecção de erros recorrentes, evolução semanal
5. **Recomendações**
   - Sugestões automáticas + validação do orientador
6. **Persistência**
   - Local (ex.: SQLite) e opcional sync com Supabase

## Contribuição

Sugestões e PRs são bem-vindos. Para contribuir:

- Cria uma branch com a tua mudança
- Mantém o escopo pequeno e bem descrito
- Explica o motivo (problema) e a solução (como foi resolvido)

## Licença

Ainda não foi definida uma licença para o projeto.
