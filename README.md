# KBols

Aplicacao desktop em JavaFX para estudo individual de candidatos, com foco atual em Matematica e Fisica. O produto usa um banco de perguntas reais, uploads de PDFs por disciplina e geracao assistida pelo Gemini para montar a base inicial quando necessario.

## Escopo atual

- apenas contas `CANDIDATO` estao suportadas no runtime atual
- o fluxo antigo de `orientador` saiu do codigo ativo, das views e do schema corrente
- as disciplinas suportadas neste momento sao `Matematica` e `Fisica`
- o sistema filtra disciplinas fora desse escopo no onboarding, no diagnostico e no bootstrap
- perguntas podem vir da base existente ou ser geradas a partir dos PDFs em `uploads/disciplinas/<uuid>`

## O que ja existe no produto

- autenticacao e registo com fluxo centrado no candidato
- onboarding com escolha de avatar e selecao de disciplinas suportadas
- preparacao automatica das pastas de upload por disciplina
- extracao de topicos e geracao de perguntas reais com Gemini
- diagnostico com historico real por disciplina
- progresso por subtopico em `progressao_rigor`
- recomendacoes por subtopico em `recomendacoes_rigor`
- teste adaptativo com foco por disciplina, topico e subtopico
- persistencia de `testes`, `stats` e `teste_perguntas`
- telas de relatorios, bolsas e perfil do candidato
- suporte a perguntas com pesos por alternativa e campos de grafico para Matematica/Fisica

## Documentos do repositorio

- [uploads/README.md](uploads/README.md): estrutura dos PDFs, arquivos gerados e bootstrap automatico
- [planner/VISAO_FLUXO_ADAPTATIVO.md](planner/VISAO_FLUXO_ADAPTATIVO.md): contrato do ciclo diagnostico -> teste -> progresso
- [planner/PROGRESS.md](planner/PROGRESS.md): fotografia do estado atual e prioridades
- [CHECKLIST_CONFIG_ADAPTATIVA.md](CHECKLIST_CONFIG_ADAPTATIVA.md): trilho para remover hardcodes do motor
- [CHECKLIST_TESTES.md](CHECKLIST_TESTES.md): endurecimento do fluxo de salvamento dos testes
- [ANALISE_THREADS_BLOQUEIOS_RECOMENDACOES.md](ANALISE_THREADS_BLOQUEIOS_RECOMENDACOES.md): leitura tecnica dos gargalos de UI, BD e concorrencia

## Stack

- Java 21
- JavaFX
- Maven
- PostgreSQL
- Flyway
- Gemini API
- JDBC com repositorios proprios

## Como executar

### 1. Configurar ambiente

Cria o `.env` a partir do exemplo:

```powershell
Copy-Item .env.example .env
```

### 2. Escolher o modo de arranque

Modo navegacao, sem BD:

```env
TESTE=false
```

Modo com BD:

```env
TESTE=true
DB_URL=jdbc:postgresql://localhost:5432/simulatorbolsastudy
DB_USER=simulator
DB_PASSWORD=simulator
DB_MIGRATE=true
GEMINI_API_KEY=coloca_aqui
```

Notas:

- `DB_ENABLED=true|false` tambem e suportado
- `DB_MIGRATE=false` desliga o `Flyway.migrate()` no arranque
- sem `GEMINI_API_KEY`, o bootstrap de livros nao gera topicos nem perguntas reais

### 3. Arrancar a app

```bash
mvn clean javafx:run
```

Se aparecer o erro `JavaFX runtime components are missing`, nao execute a classe `com.imetro.App` diretamente no IDE. Use o comando acima, ou configure a execucao do projeto para chamar o goal `javafx:run` do Maven.

## Base de dados

### Schema novo

Para ambientes novos, o schema base esta em:

- `scripts/db/001_schema.sql`

Esse ficheiro ja nasce alinhado com o escopo atual:

- role unica `CANDIDATO`
- sem colunas nem tabelas ativas de `orientador`
- disciplinas seedadas apenas para `Matematica` e `Fisica`
- suporte a pesos por resposta, progresso por subtopico, bolsas e graficos

### Migrations Flyway

As migrations versionadas estao em:

- `src/main/resources/db/migration`

Estado atual:

- `V1` ate `V26`

Migrations recentes mais importantes:

- `V20__perguntas_pesos_resposta.sql`: adiciona `pesos_resposta`
- `V21__perguntas_graficos_matfisica.sql`: adiciona campos de grafico
- `V22__stats_erros_comuns_dificuldade_percentual.sql`: recalcula `erros_comuns` em `stats`
- `V23__create_table_bolsas_and_score_bolsas.sql`: cria `bolsas` e `score_bolsas`
- `V24__bolsas_semanais_simulados.sql`: amplia regras das bolsas
- `V25__configuracoes_limiar_revisao.sql`: adiciona limiares de revisao por nivel adaptativo
- `V26__remove_orientador.sql`: remove colunas, indices e tabela legado de `orientador`

### Nota sobre migrations historicas

As migrations `V1`, `V2` e `V4` ainda mencionam `orientador` porque fazem parte do historico do projeto. Isso e esperado. O estado final suportado hoje e dado pelo conjunto completo das migrations, em especial pela `V26`.

## Uploads e bootstrap de perguntas

O fluxo de livros funciona por disciplina:

1. o sistema prepara `uploads/disciplinas/<uuid>`
2. os PDFs da disciplina sao colocados nessa pasta
3. o Gemini extrai topicos para `topicos-extraidos.json`
4. o Gemini gera perguntas para `questoes-geradas.json`
5. as perguntas entram na tabela `perguntas`

O bootstrap real esta limitado a `Matematica` e `Fisica`.

Detalhes operacionais: [uploads/README.md](uploads/README.md)

## Fluxo adaptativo

Resumo do ciclo atual:

1. o candidato escolhe disciplinas suportadas
2. a base real e preparada a partir da BD e dos PDFs
3. o diagnostico mede o estado inicial por disciplina/subtopico
4. o sistema atualiza `progressao_rigor` e `recomendacoes_rigor`
5. o teste adaptativo trabalha em cima desse estado
6. os resultados entram em `testes`, `stats` e `teste_perguntas`

Contrato tecnico completo: [planner/VISAO_FLUXO_ADAPTATIVO.md](planner/VISAO_FLUXO_ADAPTATIVO.md)

## Estado tecnico atual

Pontos ja consolidados:

- o runtime ja esta alinhado para candidato unico
- o bootstrap de perguntas reais ja trabalha com Matematica/Fisica
- o schema manual e as migrations ja refletem a remocao do fluxo antigo
- o caminho de leitura do diagnostico ja foi separado do bootstrap pesado

Pontos ainda em endurecimento:

- parte das regras do motor adaptativo ainda esta hardcoded em `TesteAdaptativoController` e `CalculoStats`
- o fecho de diagnostico e teste ainda passa por gravacoes sincrona em fluxo de UI
- o detalhe de `teste_perguntas` ainda precisa usar a mesma `Connection` da transacao principal para fechar atomicidade total

## Prioridade recomendada

- consolidar a leitura de configuracao adaptativa a partir do banco
- mover os salvamentos finais pesados para background
- endurecer a transacao completa de `testes`, `stats` e `teste_perguntas`
- continuar a melhorar relatorios e bolsas a partir de dados reais

## Licenca

Licenca ainda nao definida.
