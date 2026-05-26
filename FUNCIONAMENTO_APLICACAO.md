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
|------------|----------|----------|
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
