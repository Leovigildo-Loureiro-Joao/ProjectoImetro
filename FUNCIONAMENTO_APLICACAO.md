# Funcionamento da Aplicacao Projecto Imetro

## Visao Geral

O Projecto Imetro e uma aplicacao desktop em JavaFX para estudo individual de candidatos.
O sistema trabalha hoje com foco em `Matematica` e `Fisica`, usa um banco de perguntas reais e cria, a partir do desempenho do candidato, um ciclo adaptativo de diagnostico, teste e planeamento.

A ideia central e simples:

- o diagnostico e o mapa do estado atual do candidato
- o planeamento e gerado pelo sistema a partir desse diagnostico
- os testes adaptativos validam se o plano esta a ser cumprido
- os relatarios mostram apenas o resultado do que o backend ja calculou

## Principios Do Sistema

- O candidato e o centro de tudo.
- O sistema decide o que precisa ser estudado com base em dados reais.
- O planeamento nao e manual: e calculado pelo backend.
- Cada diagnostico concluido gera ou atualiza um plano de estudo.
- O plano fica guardado na base de dados como snapshot semanal.
- Se o utilizador quiser recomecar do zero, o reset limpa os diagnosticos e o estado derivado.

## Fluxo Principal

### 1. Onboarding

- O candidato cria a conta.
- Escolhe o avatar.
- Seleciona as disciplinas suportadas.
- O sistema filtra qualquer disciplina fora do escopo atual.

### 2. Bootstrap Automatico

- O sistema prepara as pastas de upload em `uploads/disciplinas/<uuid>`.
- Os PDFs sao analisados para extrair topicos.
- O Gemini pode gerar perguntas reais a partir desses PDFs.
- A base de perguntas fica pronta para diagnostico e teste.

### 3. Primeiro Diagnostico

- Se o candidato ainda nao tem historico, o sistema permite o primeiro diagnostico normalmente.
- Esse primeiro diagnostico e a fotografia inicial do candidato.
- No fim do diagnostico, o backend grava:
  - `diagnosticos`
  - `progressao_aluno_disciplina`
  - `progressao_rigor`
  - `recomendacoes_rigor`
  - `planeamentos_estudo`

### 4. Planeamento De Estudo

- O planeamento e gerado por `PlaneamentoEstudoService`.
- O plano usa:
  - progresso por disciplina
  - diagnosticos recentes
  - testes anteriores
  - dias sem estudo
  - precisao, velocidade e consistencia

- O resultado e gravado em `planeamentos_estudo`.
- Cada linha representa um snapshot semanal do candidato.
- O snapshot inclui:
  - score principal
  - resumo do plano
  - foco atual
  - insights
  - etapas da semana
  - registros recentes
  - disciplinas priorizadas
  - evolucao

### 5. Teste Adaptativo

- O teste adaptativo trabalha em cima do estado construido pelo diagnostico.
- O motor ajusta a dificuldade de acordo com o desempenho.
- O resultado final e salvo em:
  - `testes`
  - `teste_perguntas`
  - `stats`

### 6. Relatorios E Acompanhamento

- A tela de relatorios nao cria o plano.
- Ela apenas le o resumo ja calculado pelo backend.
- Os graficos e cards refletem o estado real do candidato.

## Regra De Controle Do Diagnostico

### Quando o diagnostico pode comecar

- Se o candidato ainda nao tem historico, o primeiro diagnostico pode comecar.
- Se o candidato ja tem historico, o sistema exige um plano ativo.
- Se nao existir planeamento ativo para um candidato com historico, o inicio do diagnostico e bloqueado.

### Quando o plano e prolongado

- O sistema verifica o ultimo plano ativo.
- Se o plano ja expirou, o backend pode prolongar o periodo do ultimo planeamento.
- A ideia de negocio e manter o candidato dentro do ciclo ate o plano ser concluido.

### Quando o candidato quer recomecar

- O reset oficial apaga os diagnosticos.
- Ao apagar os diagnosticos, o sistema tambem limpa o estado derivado.
- Isso inclui:
  - `diagnosticos`
  - `progressao_rigor`
  - `progresso_aluno_disciplina`
  - `planeamentos_estudo`

## Ciclo De Vida Do Candidato

```text
Onboarding -> Bootstrap -> Primeiro Diagnostico -> Planeamento -> Teste Adaptativo -> Relatorios
```

Depois do primeiro diagnostico:

- o sistema conhece melhor as fraquezas reais do candidato
- o planeamento passa a ser mais inteligente
- cada novo diagnostico reforca ou ajusta o plano
- o historico fica guardado para comparacao futura

## Persistencia Em Base De Dados

### Tabelas Principais

| Tabela | Funcao |
|---|---|
| `diagnosticos` | Guarda a fotografia de entrada do candidato |
| `progressao_aluno_disciplina` | Mantem o progresso geral por disciplina |
| `progressao_rigor` | Mantem o estado vivo por topico/subtopico |
| `recomendacoes_rigor` | Guarda as recomendacoes abertas do ciclo |
| `planeamentos_estudo` | Guarda o plano semanal gerado pelo sistema |
| `testes` | Guarda cada teste adaptativo concluido |
| `teste_perguntas` | Guarda o detalhe de cada resposta |
| `stats` | Guarda o resumo consolidado do teste |

### Migration Mais Recente

- `V29__planeamento_estudo.sql`

Essa migration cria a tabela `planeamentos_estudo` e os indices para o plano semanal do candidato.

## Componentes Principais

### Services

- `DiagnosticoService`
- `PlaneamentoEstudoService`
- `TesteAdaptativoService`
- `TesteService`
- `DisciplinaService`
- `GeminiService`

### Repositories

- `DiagnosticoRepository`
- `PlaneamentoEstudoRepository`
- `ProgressaoRigorRepository`
- `ProgressoALunoDisciplinaRepository`
- `TesteRepository`
- `TesteStatsRepository`
- `RecomendacaoRepository`

### Controllers

- `DiagnosticoCandidatoController`
- `DiagnosticoListController`
- `RelatoriosController`
- `TesteAdaptativoController`

## Como Executar

- O arranque recomendado e `mvn clean javafx:run`.
- Se a app for aberta diretamente pelo IDE sem o module-path do JavaFX, pode surgir o erro de runtime ausente.
- O launcher do projeto ja aponta para o JavaFX local configurado no ambiente.

## Resumo Final

O comportamento atual do sistema e este:

- o diagnostico e a fonte de verdade
- cada diagnostico concluido gera planeamento
- o planeamento fica gravado na base de dados
- o sistema bloqueia novos diagnosticos quando nao ha plano ativo para quem ja tem historico
- se o candidato quiser recomecar, basta limpar os diagnosticos e o estado derivado

