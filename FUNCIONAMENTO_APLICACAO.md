# Funcionamento Real da Aplicação Projecto Imetro

## Índice
1. [Missão e Objetivos](#missão-e-objetivos)
2. [Propósito Geral](#propósito-geral)
3. [Escopo Operacional](#escopo-operacional)
4. [Arquitetura Geral](#arquitetura-geral)
5. [Fluxo Adaptativo Completo](#fluxo-adaptativo-completo)
6. [Componentes Principais](#componentes-principais)
7. [Ciclo de Vida do Candidato](#ciclo-de-vida-do-candidato)
8. [Sistema de Persistência](#sistema-de-persistência)
9. [Motor Adaptativo](#motor-adaptativo)
10. [Processamento de Perguntas](#processamento-de-perguntas)
11. [Metricas e Analytics](#metricas-e-analytics)
12. [Funcionalidades Avançadas](#funcionalidades-avançadas)

---

## Missão e Objetivos

### Missão
Fornecer uma plataforma de estudo assistida e adaptativa para candidatos em preparação acadêmica, utilizando inteligência artificial para personalizar a experiência de aprendizagem com base no desempenho real do aluno.

### Objetivos Principais

1. **Estudo Individual Personalizado**: Oferecer testes e diagnósticos adaptados ao nível de compreensão de cada candidato
2. **Avaliação Diagnóstica Inicial**: Medir o estado de conhecimento do aluno em cada disciplina e subtópico
3. **Teste Adaptativo**: Gerar sessões de testes que se ajustam ao desempenho em tempo real
4. **Progresso Rastreável**: Manter histórico completo de desempenho para visualizar evolução
5. **Suporte a Múltiplas Disciplinas**: Permitir estudo paralelo de Matemática e Física (escopo atual)
6. **Base de Questões Dinâmica**: Utilizar PDFs reais e gerar perguntas inteligentes com IA Gemini
7. **Bolsas de Estudo Simuladas**: Acompanhar potencial de elegibilidade para bolsas com base em desempenho

---

## Propósito Geral

O Projecto Imetro é uma **aplicação desktop em JavaFX** criada para:

- **Candidatos**: Estudar de forma independente e adaptar seu ritmo de aprendizagem
- **Produção de Base de Dados**: Gerar base real de questões a partir de livros (PDFs) usando IA
- **Análise de Progresso**: Acompanhar métricas detalhadas de desempenho, fraquezas e evolução
- **Concessão de Bolsas**: Simular cenários de bolsas de estudo baseados em desempenho

O sistema trabalha com **dois modos principais**:
- **Modo de Navegação**: UI sem banco de dados (apenas prototipagem)
- **Modo com BD**: Completo com persistência em PostgreSQL

---

## Escopo Operacional

### Limitações e Focos Atuais

#### Disciplinas Suportadas
- **Matemática**
- **Física**

Qualquer outra disciplina é filtrada automaticamente no onboarding, diagnóstico e bootstrap.

#### Tipos de Usuários
- **CANDIDATO**: Único papel ativo no runtime atual
  - O fluxo antigo de `orientador` foi removido do código ativo, views e schema
  - Suporta apenas conta de estudante individual

#### Perguntão: Origem das Questões
1. **Base Existente**: Perguntas já cadastradas no banco de dados
2. **Gerada com IA**: Novas questões extraídas de PDFs via Gemini API

---

## Arquitetura Geral

### Camadas da Aplicação

```
┌─────────────────────────────────────────────┐
│        Camada de Apresentação (UI)          │
│  JavaFX Controllers, Components, FXML       │
├─────────────────────────────────────────────┤
│         Camada de Negócio (Services)        │
│  TesteAdaptativoService, DiagnosticoService│
├─────────────────────────────────────────────┤
│      Camada de Persistência (Repository)    │
│  JDBC com custom repositories               │
├─────────────────────────────────────────────┤
│           Camada de Dados (BD)              │
│  PostgreSQL com schema Flyway               │
└─────────────────────────────────────────────┘
```

### Stack Técnico

| Componente | Tecnologia | Versão |
|------------|-----------|--------|
| Linguagem | Java | 21 |
| Interface | JavaFX | 23.0.1 |
| Build Tool | Maven | - |
| Banco de Dados | PostgreSQL | - |
| Migrations | Flyway | 10.20.0 |
| IA / Bootstrap | Gemini API | - |
| Persistência | JDBC Customizado | - |
| UI Components | ControlsFX, JFreeChart | - |

---

## Fluxo Adaptativo Completo

### Ciclo Principal

O sistema segue um ciclo cíclico de melhoria contínua:

```
┌──────────────────┐
│  1. Onboarding   │ Candidato escolhe disciplinas (suportadas)
└────────┬─────────┘
         ↓
┌────────────────────────────┐
│  2. Bootstrap Automático   │ Prepara pastas, extrai tópicos, gera perguntas
└────────┬────────────────────┘
         ↓
┌────────────────────────────┐
│  3. Diagnóstico Inicial    │ Mede estado de entrada por disciplina/subtópico
└────────┬────────────────────┘
         ↓
┌────────────────────────────┐
│  4. Atualização Adaptativa │ Grava progressão e recomendações
└────────┬────────────────────┘
         ↓
┌────────────────────────────┐
│  5. Teste Adaptativo       │ Testa com perguntas ajustadas ao nível
└────────┬────────────────────┘
         ↓
┌────────────────────────────┐
│  6. Persistência de Resultado│ Salva em testes, stats, teste_perguntas
└────────┬────────────────────┘
         ↓
         │
         └──→ Volta ao passo 5 ou 3 conforme progresso
```

### Entidades-Chave do Fluxo

| Tabela | Propósito |
|--------|----------|
| `diagnosticos` | Fotografia de entrada do candidato em cada disciplina |
| `progressao_rigor` | Estado vivo do subtópico (evolução) |
| `recomendacoes_rigor` | Debilidades abertas e direção do próximo ciclo |
| `testes` | Sessão adaptativa concluída |
| `teste_perguntas` | Detalhe de cada pergunta respondida em um teste |
| `stats` | Resumo consolidado do teste |

---

## Componentes Principais

### 1. Controllers de Autenticação (`ui/controller/auth/`)

**LoginController**
- Valida credenciais do candidato
- Acesso apenas com email e senha válidos
- Integração com `CandidatoRepository`

**RegisterController**
- Criação de nova conta de candidato
- Validação de dados
- Hash de senha com `PasswordHasher`

**ChooseDisciplinasOnboardingController**
- Apresenta disciplinas suportadas (Matemática e Física)
- Permite seleção múltipla
- Filtra automaticamente disciplinas inativas

**AddImageOnboardingController**
- Seleção de avatar do candidato
- Suportado por `AvatarSupport`

### 2. Controllers de Candidato (`ui/controller/candidato/`)

**DashboardOrientadoController**
- Tela principal pós-autenticação
- Exibe disciplinas ativas, últimos testes, progresso

**DiagnosticoCoordinator** e **DiagnosticoCandidatoController**
- Orquestra o fluxo de diagnóstico
- Apresenta perguntas por disciplina e subtópico
- Integra com `DiagnosticoService` e `DiagnosticoRepository`

**TesteAdaptativoCoordinator** e **TesteAdaptativoController**
- Orquestra testes adaptativos
- Ajusta dificuldade baseado em respostas
- Coordena com `TesteAdaptativoService`

**PerfilController**
- Exibe dados do candidato
- Mostra medalhas conquistadas (`MedalhaRepository`)

**RelatoriosController**
- Visualiza histórico completo de testes
- Gráficos de progresso por disciplina
- Integra com `RelatorioService` e `RelatoriosRepository`

**BolsasController**
- Simula elegibilidade para bolsas
- Calcula scores baseado em desempenho
- Usa `BolsaRepository` e `ScoreBolsaRepository`

### 3. Services (Camada de Negócio)

#### TesteAdaptativoService
- Orquestra a lógica do teste adaptativo
- Define próxima pergunta baseado no desempenho atual
- Integra métricas de dificuldade e acerto
- **Ponto de atenção**: Parte da lógica ainda é hardcoded

#### DiagnosticoService
- Executa o fluxo de diagnóstico
- Calcula `progressao_rigor` após término
- Gera `recomendacoes_rigor` automáticas

#### CatalogoQuestoesService
- Carrega perguntas conforme critérios (disciplina, tópico, subtópico, dificuldade)
- Suporta filtragem por pesos e graficos

#### PerguntasBootstrapService
- Gera perguntas em lotes usando Gemini
- Insere perguntas na tabela `perguntas`
- Trabalha apenas com Matemática e Física

#### PerguntasBootstrapAsyncService
- Acompanha estado de processamento em background
- Notifica quando bootstrap está concluído

#### GeminiService
- Interface com Gemini API
- Extrai tópicos de PDFs
- Gera questões com base em conteúdo extraído

#### DisciplinaUploadBootstrapService
- Prepara pastas de upload por disciplina (`uploads/disciplinas/<uuid>`)
- Gerencia ciclo de arquivo para PDFs

#### RelatorioService
- Consolida dados de testes históricos
- Calcula tendências e métricas agregadas
- Alimenta visualizações em `RelatoriosController`

#### BolsaSimuladoService
- Calcula score de bolsa para candidato
- Simula diferentes cenários de desempenho

### 4. Repositories (Camada de Persistência)

**Padrão Customizado**: JDBC com SQL nativo (não usa ORM como Hibernate)

| Repository | Entidade | Operações |
|------------|----------|-----------|
| `CandidatoRepository` | Candidato (Users) | CRUD, autenticação |
| `DisciplinaRepository` | Disciplina | Leitura, filtro |
| `PerguntasRepository` | Pergunta | Leitura, filtro por critérios |
| `DiagnosticoRepository` | Diagnóstico | CRUD, histórico |
| `TesteRepository` | Teste | CRUD, histórico |
| `TestePerguntasRepository` | Detalhe de pergunta em teste | Inserção (atômico) |
| `TesteStatsRepository` | Estatísticas de teste | CRUD |
| `ProgressaoRigorRepository` | Progresso por subtópico | CRUD |
| `RecomendacaoRepository` | Recomendações abertas | CRUD |
| `RelatoriosRepository` | Relatórios consolidados | Queries complexas |
| `BolsaRepository` | Bolsas disponíveis | Leitura |
| `ScoreBolsaRepository` | Score de bolsa por candidato | CRUD |
| `MedalhaRepository` | Medalhas e achievements | CRUD |

---

## Ciclo de Vida do Candidato

### 1. Registro e Onboarding

```
Login → Register → Escolher Avatar → Escolher Disciplinas → Dashboard
```

**Detalhes:**
- Email único validado
- Senha hasheada com `PasswordHasher` (PBKDF2)
- Avatar selecionado de conjunto pré-definido (`AvatarSupport`)
- Disciplinas filtradas para apenas Matemática e Física
- Bootstrap automático disparado se PDFs existirem

### 2. Bootstrap de Questões (Automático)

**Quando dispara:**
- Onboarding completo com disciplinas
- Entrada no fluxo de primeiro diagnóstico sem base suficiente
- Chamadas explícitas de bootstrap

**Processo:**
1. Sistema verifica `uploads/disciplinas/<disciplina_uuid>` para PDFs
2. Gemini extrai tópicos e subtópicos → `topicos-extraidos.json`
3. Gemini gera perguntas por tópico → `questoes-geradas.json`
4. Perguntas inseridas na tabela `perguntas` com pesos por alternativa
5. Sistema marca disciplina como preparada

### 3. Diagnóstico Inicial

**Entrada:**
- Candidato seleciona disciplina
- Sistema prepara perguntas do diagnóstico (uma por subtópico)

**Fluxo:**
- Apresenta perguntas sequencialmente
- Registra resposta, tempo e confiança
- Ao final, calcula `progressao_rigor` para cada subtópico
- Gera `recomendacoes_rigor` automáticas (subtópicos com baixo desempenho)
- Salva tudo em `diagnosticos` (não reescreve diagnóstico anterior)

**Output:**
- `progressao_rigor`: % acerto por subtópico
- `recomendacoes_rigor`: subtópicos abertos para revisão

### 4. Teste Adaptativo

**Contexto:**
- Usa estado de `progressao_rigor` e `recomendacoes_rigor` como base
- Ataca subtópicos abertos prioritariamente

**Algoritmo de Seleção:**
- Se P (precisão) < 60%: prioriza perguntas desse subtópico
- Se 60% ≤ P < 80%: mixes perguntas de diferentes níveis
- Se P ≥ 80%: apresenta perguntas de dificuldade aumentada

**Adaptação em Tempo Real:**
- Cada resposta correta → aumenta dificuldade
- Cada resposta errada → mantém ou reduz dificuldade
- Motor calcula próxima pergunta baseado em desempenho acumulado

**Finalização:**
- Teste tem limite de tempo configurável por nível adaptativo
- Ao final, registra em `testes`, `teste_perguntas` e `stats`

### 5. Resultado e Feedback

**Após Teste:**
- Mostra celebração (se performance > threshold)
- Exibe resumo de acertos/erros
- Apresenta recomendações para revisão
- Mostra tempo total e velocidade média

**Persisted Stats:**
- Acurácia global
- Acurácia por subtópico
- Erros comuns (extraído de `teste_perguntas`)
- Dificuldade percentual
- Tempo total
- Ganho normalizado vs. diagnóstico anterior

### 6. Relatórios e Progresso

**Relatórios Disponíveis:**
- Timeline completa de todos os testes
- Gráficos de acurácia por disciplina
- Trends de desempenho
- Pontos fortes e fracos por subtópico
- Medalhas conquistadas

---

## Sistema de Persistência

### Banco de Dados

**DBMS**: PostgreSQL

**Configuração via `.env`:**
```env
DB_URL=jdbc:postgresql://localhost:5432/simulatorbolsastudy
DB_USER=simulator
DB_PASSWORD=simulator
DB_MIGRATE=true
TESTE=true
```

### Schema

#### Tabelas Principais

**users** (antes candidatos)
- `id`: PK
- `email`: Unique
- `password_hash`: PBKDF2 hashed
- `nome`: Nome completo
- `avatar`: Identificador do avatar
- `role`: Enum (CANDIDATO)
- `created_at`: Timestamp

**disciplinas**
- `id`: PK (UUID)
- `nome`: Nome da disciplina
- `descricao`: Descrição
- `status`: Ativo/Inativo
- Seedada com Matemática e Física

**perguntas**
- `id`: PK
- `disciplina_id`: FK para disciplinas
- `topico_id`: FK para tópicos
- `subtopico_id`: FK para subtópicos
- `texto`: Texto da pergunta
- `tipo_pergunta`: Múltipla escolha, etc.
- `pesos_resposta`: JSON com pesos por alternativa (V20)
- `campos_grafico`: JSON com dados para gráfico (V21)
- `dificuldade`: 1-5
- `criado_em`: Timestamp

**diagnosticos**
- `id`: PK
- `candidato_id`: FK para users
- `disciplina_id`: FK para disciplinas
- `precisao_inicial`: % acerto no diagnóstico
- `data_diagnostico`: Timestamp
- `estado`: Completo/Incompleto
- **Importante**: Nunca reescrito, apenas novo inserido

**progressao_rigor**
- `id`: PK
- `candidato_id`: FK para users
- `subtopico_id`: FK para subtópicos
- `precisao_atual`: % acerto atual
- `ultima_atualizacao`: Timestamp
- **Importante**: Atualizado após cada diagnóstico/teste

**recomendacoes_rigor**
- `id`: PK
- `candidato_id`: FK para users
- `subtopico_id`: FK para subtópicos
- `tipo_recomendacao`: Ex. "REVISAO", "PRATICA_INTENSIVA"
- `forca_recomendacao`: 0-1 (força da recomendação)
- `criada_em`: Timestamp

**testes**
- `id`: PK
- `candidato_id`: FK para users
- `disciplina_id`: FK para disciplinas
- `data_teste`: Timestamp
- `tempo_total_segundos`: Integer
- `estado`: Completo/Incompleto
- `precisao_geral`: % acerto global

**teste_perguntas**
- `id`: PK
- `teste_id`: FK para testes
- `pergunta_id`: FK para perguntas
- `resposta_candidato`: Alternativa escolhida
- `correta`: Boolean
- `tempo_resposta`: Segundos
- `confianca`: 0-1 (auto-avaliação do candidato)
- **Importante**: Deve usar mesma Connection da transação principal para atomicidade

**stats**
- `id`: PK
- `teste_id`: FK para testes
- `acertos_por_subtopico`: JSON
- `erros_comuns`: JSON (V22)
- `dificuldade_percentual`: JSON (V22)
- `ganho_normalizado`: Float
- `resiliencia`: Float
- `velocidade_relativa`: Float
- `consistencia`: Float
- `logica`: Float

**bolsas**
- `id`: PK
- `nome`: Nome da bolsa
- `descricao`: Descrição
- `criterios`: JSON
- `vigencia`: Data até
- `disponivel`: Boolean

**score_bolsas** (V23)
- `id`: PK
- `candidato_id`: FK para users
- `bolsa_id`: FK para bolsas
- `score_simulado`: Float (0-100)
- `elegivel`: Boolean
- `calculado_em`: Timestamp

**configuracoes_teste_adaptativo** (V25)
- `id`: PK
- `nivel`: Enum (INICIANTE, INTERMEDIARIO, AVANCADO)
- `limiar_revisao`: Float
- `limiar_progressao`: Float
- `tempo_maximo_minutos`: Integer

### Migrations (Flyway)

**Histórico:**
- V1-V4: Schema base com orientador (legado, mantido para histórico)
- V5-V19: Refinamentos incrementais
- V20: Adiciona `pesos_resposta` em perguntas
- V21: Adiciona campos de gráfico (`campos_grafico`)
- V22: Recalcula `erros_comuns` e `dificuldade_percentual` em stats
- V23: Cria `bolsas` e `score_bolsas`
- V24: Amplia regras de bolsas (semanais, simulados)
- V25: Adiciona `configuracoes_teste_adaptativo` com limiares por nível
- V26: Remove colunas, índices e tabelas legado de orientador

**Estado Final Suportado**: Determinado pelo conjunto completo de migrations, especialmente V26

---

## Motor Adaptativo

### Fundamentos Matemáticos

#### 1. Precisão por Resposta

`p_resposta_j ∈ [0, 1]`

**Regra:**
- Se pergunta tem pesos por alternativa → usa peso da alternativa marcada
- Se pergunta legada → usa fallback do projeto (ex. 0 ou 1 para múltipla escolha)

#### 2. Precisão por Subtópico

`P_i = (Σ p_resposta_j) / total_i`

Esta é a unidade mínima de progresso. Rastreada em `progressao_rigor`.

#### 3. Precisão Global

**Média Simples:**
`P_geral = (P_1 + P_2 + ... + P_n) / n`
- Mede equilíbrio entre subtópicos

**Média Ponderada:**
`P_ponderada = (Σ acertos_i) / (Σ total_i)`
- Mede desempenho agregado real

#### 4. Ganho Normalizado

`G = (P_atual - P_anterior) / max(ε, 1 - P_anterior)`

- Compara dois estados consecutivos do mesmo foco
- Normaliza pelo espaço de melhoria restante

#### 5. Resiliência

`R = ((P_2 + P_3) / 2) / max(ε, P_1)`

- Mede recuperação após erro ou série adversa
- Usado para detectar candidatos que aprendem com falhas

#### 6. Velocidade

**Velocidade Raw:**
`V_raw = tempo_total / max(1, total_acertos)`

**Velocidade Relativa:**
`V_rel = T_base / T_usuario`

- V_raw como observável bruto
- V_rel como score comparável entre candidatos

#### 7. Consistência

`Delta_P_n = P_(n+1) - P_n`

`CV_ganhos = stddev(Delta_P) / max(ε, abs(média(Delta_P)))`

`C = corr+(n, P_n) * (1 - min(1, CV_ganhos))`

- Distingue crescimento sustentado de crescimento ruidoso
- Identifica candidatos com desempenho previsível

#### 8. Lógica Inferencial

`L = acertos_baixa_estruturacao / total_baixa_estruturacao`

- Mede desempenho em perguntas menos mecânicas
- Mais inferenciais e abertas

### Configuração Adaptativa

**Tabela:** `configuracoes_teste_adaptativo_nivel` e `configuracoes_teste_adaptativo_duracao`

**Limiares por Nível:**
| Nível | Limiar Revisão | Limiar Progressão | Tempo Máximo |
|-------|---|---|---|
| INICIANTE | 40% | 70% | 20 min |
| INTERMEDIÁRIO | 50% | 75% | 25 min |
| AVANÇADO | 60% | 80% | 30 min |

**Lógica Atual (Parcialmente Hardcoded em TesteAdaptativoController):**
- Se P < limiar_revisão: oferece mais perguntas de revisão
- Se limiar_revisão ≤ P < limiar_progressão: mix equilibrado
- Se P ≥ limiar_progressão: aumenta dificuldade

### Próximos Passos Recomendados

1. **Remover Hardcodes**: Mover regras de `TesteAdaptativoController` para banco
2. **Atomicidade Total**: Fechar transação completa de `teste`, `teste_perguntas`, `stats`
3. **Background Saves**: Mover gravações finais para threads background

---

## Processamento de Perguntas

### Fluxo Geral de Questões

```
┌──────────────────┐
│   PDFs em pasta  │ uploads/disciplinas/<uuid>/*.pdf
└────────┬─────────┘
         ↓
┌──────────────────────────┐
│  GeminiService           │ Extrai tópicos e subtópicos
│  (extractTopics)         │
└────────┬─────────────────┘
         ↓
┌────────────────────────────────┐
│  topicos-extraidos.json        │ Armazenado em pasta
└────────┬───────────────────────┘
         ↓
┌────────────────────────────┐
│  PerguntasBootstrapService │ Gera perguntas em lotes
│  (generateQuestionsInBatches)
└────────┬───────────────────┘
         ↓
┌────────────────────────────┐
│  questoes-geradas.json     │ Armazenado em pasta
└────────┬───────────────────┘
         ↓
┌────────────────────────────┐
│  PerguntasRepository       │ Insere em BD
│  .insertPerguntas()        │
└────────┬───────────────────┘
         ↓
┌────────────────────────────┐
│  Tabela perguntas          │ Pronto para uso
└─────────────────────────────┘
```

### Seleção de Questões para Teste

**CatalogoQuestoesService.carregaPerguntas()**

```java
Critérios: {
  disciplinaId,
  topicoId,      // Opcional
  subtopicoId,   // Opcional
  dificuldade,   // 1-5, Opcional
  limite         // Quantidade
}
```

**Retorna:**
- Array de `Pergunta` com pesos por alternativa preenchidos
- Campos de gráfico se aplicável

### Estrutura de Pergunta (Modelo)

```java
class Pergunta {
  id: Long
  disciplinaId: UUID
  topicoId: UUID
  subtopicoId: UUID
  texto: String
  tipo: TipoPergunta (MULTIPLA_ESCOLHA, etc.)
  alternativas: List<String>
  pesosResposta: Map<Integer, Float> // Peso por índice de alternativa (V20)
  camposGrafico: Map<String, Object> // Dados para gráfico (V21)
  dificuldade: Integer (1-5)
  criadoEm: LocalDateTime
}
```

### Decisão de Próxima Pergunta (Motor Adaptativo)

**TesteAdaptativoService.proximaPergunta()**

```
Input: {
  desempenho_atual,
  subtopico_atual,
  perguntas_respondidas,
  tempo_decorrido
}

Lógica:
1. Calcular P (precisão) no contexto atual
2. Se P < 60%:
   → Perguntas de revisão (dificuldade 1-2) do mesmo subtópico
3. Se 60% ≤ P < 80%:
   → Mix de dificuldades (2-3) do subtópico
   → Alguns de subtópicos correlatos
4. Se P ≥ 80%:
   → Dificuldade aumentada (4-5)
   → Novos subtópicos

5. Evitar repetições:
   → Não repete pergunta respondida na mesma sessão
   → Consulta teste_perguntas

6. Respeitar tempo:
   → Se tempo_decorrido > limiar:
     → Reduz dificuldade
     → Oferece questões mais rápidas

Output: Pergunta selecionada
```

---

## Metricas e Analytics

### Dashboard Candidato

**Informações Exibidas:**

1. **Disciplinas Ativas**: Cards mostrando cada disciplina com:
   - Progresso geral (%)
   - Último teste realizado
   - Data

2. **Últimos Testes**: Timeline com:
   - Data e hora
   - Disciplina
   - Acurácia
   - Tempo total

3. **Progresso por Subtópico**: Gráfico de barras:
   - Y: % Precisão
   - X: Cada subtópico
   - Cor: Verde (>75%), Amarelo (60-75%), Vermelho (<60%)

4. **Medalhas**: Grid de achievements conquistados

### Relatórios Detalhados

**RelatorioService.gerarRelatorio(candidatoId, disciplinaId)**

**Seções:**

1. **Timeline de Testes**
   - Todos os testes históricos
   - Gráfico de linha: Acurácia ao longo do tempo

2. **Análise por Subtópico**
   - Cards individuais por subtópico:
     - Acurácia média
     - Número de perguntas
     - Tendência (↑ ou ↓)

3. **Erros Comuns**
   - Top 5 perguntas com mais erros
   - Frequência de erro

4. **Insights**
   - "Você melhorou 15% em Cálculo"
   - "Precisa revisar Derivadas"

### Bolsas e Scores

**BolsaSimuladoService.calcularScore()**

Calcula elegibilidade baseado em:
- Acurácia geral ≥ X%
- Acurácia mínima por disciplina
- Consistência (sem quedas drásticas)
- Velocidade (resolvendo rápido)

**Output**: Score 0-100 e status de elegibilidade

---

## Funcionalidades Avançadas

### 1. Pesos por Alternativa (V20)

**Problema**: Nem toda resposta errada vale zero pontos. Algumas alternativas parcialmente corretas.

**Solução**: Campo `pesos_resposta` em `perguntas`

```json
{
  "1": 0.0,      // Alternativa A: 0 pontos
  "2": 0.5,      // Alternativa B: 50% dos pontos
  "3": 1.0,      // Alternativa C: 100% dos pontos (correta)
  "4": 0.25      // Alternativa D: 25% dos pontos
}
```

Usado no cálculo de `p_resposta_j`.

### 2. Campos de Gráfico para Matemática/Física (V21)

**Problema**: Perguntas de gráfico precisam de dados estruturados (coordenadas, fórmulas).

**Solução**: Campo `campos_grafico` em `perguntas`

```json
{
  "tipo": "PLANO_CARTESIANO",
  "pontos": [[1, 2], [2, 4], [3, 6]],
  "reta": {
    "coef_angular": 2,
    "intercepto": 0
  },
  "titulo": "Função Linear"
}
```

Renderizado com `PlanoCartesianoPane` na UI.

### 3. Configuração Adaptativa por Nível (V25)

**Problema**: Candidatos iniciantes vs. avançados precisam de diferentes regras.

**Solução**: Tabelas `configuracoes_teste_adaptativo_nivel` e `_duracao`

Permite ajustar sem hardcoding.

### 4. Bolsas Semanais e Simulados (V24)

**BolsaSimuladoCoordinator**: Simula desempenho em cenários:
- "E se eu acertasse 80% em Física?"
- Mostra impacto em elegibilidade

### 5. Remocao de Orientador (V26)

**Histórico**: Projeto começou com modelo orientador-candidato.

**Refactoring**: V26 removeu:
- Tabelas: `orientadores`, `orientador_disciplinas`
- Colunas: `orientador_id` em `candidatos`, etc.
- Views e controllers legado

**Estado Final**: Apenas fluxo CANDIDATO-solo.

---

## Fluxo Detalhado de Um Teste Adaptativo

### Exemplo Passo-a-Passo

**Candidato**: João
**Disciplina**: Matemática
**Contexto**: Diagnóstico anterior mostrou P=65% em Cálculo, P=40% em Álgebra

### Passo 1: Iniciar Teste
```
TesteAdaptativoController.iniciarTeste(candidatoId, disciplinaId)
↓
Cria Teste (estado: INICIADO)
↓
Carrega progressao_rigor e recomendacoes_rigor
```

### Passo 2: Primeira Pergunta
```
TesteAdaptativoService.proximaPergunta()

Critério: recomendacoes_rigor mostra Álgebra com alta prioridade

CatalogoQuestoesService.carregaPerguntas({
  disciplinaId: math_uuid,
  subtopicoId: algebra_uuid,
  dificuldade: 2,  // Início fácil
  limite: 1
})

Retorna: Pergunta("Resolva: 2x + 3 = 7", dificuldade=2)

Exibe em CardQuestao
```

### Passo 3: Resposta
```
Candidato escolhe: "x = 2"

TesteAdaptativoController.registrarResposta()

Verifica peso: pesos_resposta[3] = 1.0 → Correto

p_resposta_1 = 1.0

stats.acertos_algebra += 1
```

### Passo 4: Próxima Pergunta (Adaptação)
```
P_algebra = 1 / 1 = 100% (1 acerto de 1)

Critério: P ≥ 80% → aumentar dificuldade

CatalogoQuestoesService.carregaPerguntas({
  disciplinaId: math_uuid,
  subtopicoId: algebra_uuid,
  dificuldade: 3,  // Aumentado
  limite: 1,
  excludeIds: [pergunta_1]  // Não repetir
})

Retorna: Pergunta("Fatore: x² + 5x + 6", dificuldade=3)
```

### Passo 5: Resposta Errada
```
Candidato escolhe: "ERRO"

p_resposta_2 = 0.0

P_algebra = 1 / 2 = 50%

Critério: P < 60% → voltar para revisão

Próxima pergunta terá dificuldade 2 ou menos
```

### Passo 6: Término
```
Tempo ≥ limiar_tempo (ex. 20 min)

TesteAdaptativoController.finalizarTeste()

Calcula:
  - stats (acertos, erros, ganho, resiliência, etc.)
  - Atualiza progressao_rigor com novos dados
  - Gera recomendacoes_rigor

Salva atomicamente:
  - testes (estado: CONCLUÍDO)
  - teste_perguntas (lista completa de Q/A)
  - stats (resumo consolidado)

Exibe: ResultadoCelebracaoModal
```

---

## Pontos Técnicos Importantes

### 1. Atomicidade em Teste

**Problema**: Se salvar `testes` mas falhar em `teste_perguntas`, dados inconsistentes.

**Status Atual**: Gravação em fluxo síncrono de UI (possível deadlock).

**Solução Recomendada**: Usar transação única com rollback automático:

```java
conn.setAutoCommit(false);
try {
  testeRepository.insert(teste, conn);
  testePerguntasRepository.insertAll(listaPergunta, conn);
  statsRepository.insert(stats, conn);
  conn.commit();
} catch (Exception e) {
  conn.rollback();
  throw e;
}
```

### 2. Hardcodes no Motor Adaptativo

**Localização**: `TesteAdaptativoController`, `CalculoStats`

**Exemplos**:
```java
if (p < 0.60) {
  dificuldade = 1;
} else if (p < 0.80) {
  dificuldade = 2;
} else {
  dificuldade = 3;
}
```

**Solução**: Mover para `configuracoes_teste_adaptativo`

### 3. Background Saves

**Problema**: Gravação de stats pesada bloqueia UI.

**Status Atual**: Síncrona no controller.

**Solução**: Usar `PerguntasBootstrapAsyncService` como padrão:

```java
Task<Void> saveTask = new Task<Void>() {
  @Override
  protected Void call() throws Exception {
    testeRepository.insert(teste);
    return null;
  }
};
new Thread(saveTask).start();
```

### 4. Perguntas com Gráficos

**Para Matemática/Física**: Use `PlanoCartesianoPane` para renderizar.

**Dados**: Armazenados em `campos_grafico` JSON.

**Exemplo Uso**:
```java
PlanoCartesianoPane pane = new PlanoCartesianoPane();
pane.desenharPontos(pergunta.getCamposGrafico().getPontos());
pane.desenharReta(pergunta.getCamposGrafico().getReta());
```

---

## Conclusão

O Projecto Imetro é um **sistema robusto e adaptativo** de estudo individual, com foco em:

1. **Personalização**: Cada candidato recebe teste ajustado ao seu nível
2. **Inteligência**: Uso de IA para gerar base real de questões
3. **Análise**: Métricas matemáticas avançadas para medir progresso
4. **Persistência**: Histórico completo em PostgreSQL
5. **Escalabilidade**: Arquitetura em camadas com repositórios customizados

**Próximas Melhorias**:
- Remover hardcodes do motor adaptativo
- Atomicidade completa em transações
- Mover processamento pesado para background
- Refinamento de critério para novo diagnóstico

**Stack Moderno**: Java 21, JavaFX, PostgreSQL, Gemini API — preparado para evolução contínua.
