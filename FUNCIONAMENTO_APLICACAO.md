# Funcionamento da Aplicacao Projecto Imetro

## Visao Geral

O Projecto Imetro e uma aplicacao desktop em JavaFX para estudo individual de candidatos a exames de acceso ao ensino superior em Angola.
O sistema trabalha com foco em `Matematica` e `Fisica`, usa um banco de perguntas reais e cria, a partir do desempenho do candidato, um ciclo adaptativo de diagnostico, teste, leitura e planeamento.

A ideia central e simples:

- o diagnostico e o mapa do estado atual do candidato
- o planeamento e gerado pelo sistema a partir desse diagnostico
- os testes adaptativos validam se o plano esta a ser cumprido
- a biblioteca de livros fornece material de estudo personalizado
- os trilhos de leitura recomendam leituras especificas por topico/subtopico
- os mini-testes validam apos a leitura
- os desafios adaptativos motivam o candidato
- as bolsas simuladas criam competicao semanal
- os relatorios mostram apenas o resultado do que o backend ja calculou

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

### 3. Biblioteca de Livros

- O candidato pode fazer upload de PDFs de livros por disciplina.
- O sistema extrai texto pagina-a-pagina usando PDFBox.
- Gera automaticamente uma thumbnail/capa do PDF.
- O **Gemini (IA)** extrai topicos e subtopicos do conteudo do livro.
- O candidato pode ler o PDF diretamente na aplicacao com viewer integrado.
- A navegacao e feita por topicos e subtopicos extraidos.
- Cada livro fica associado a disciplina e guardado na tabela `biblioteca_livros`.

### 4. Trilhos de Leitura

- O sistema recomenda leituras especificas para topicos/subtopicos onde o candidato tem dificuldade.
- Cada trilho e uma sequencia ordemada de passos com estados: `PENDENTE`, `A_LER`, `LIDO`.
- O progresso de leitura e rastreado por livro: pagina atual, paginas lidas, sessoes de leitura.
- As recomendacoes de leitura sao integradas no Planeamento de Estudo.
- O candidato pode iniciar a leitura diretamente do plano personalizado.

### 5. Primeiro Diagnostico

- Se o candidato ainda nao tem historico, o sistema permite o primeiro diagnostico normalmente.
- Esse primeiro diagnostico e a fotografia inicial do candidato.
- No fim do diagnostico, o backend grava:
  - `diagnosticos`
  - `progressao_aluno_disciplina`
  - `progressao_rigor`
  - `recomendacoes_rigor`
  - `planeamentos_estudo`

### 6. Planeamento De Estudo

- O planeamento e gerado por `PlaneamentoEstudoService`.
- O plano usa:
  - progresso por disciplina
  - diagnosticos recentes
  - testes anteriores
  - dias sem estudo
  - precisao, velocidade e consistencia
  - leituras recomendadas (via `TrilhoLeituraService`)
  - desafios adaptativos (via `DesafioService`)

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
  - leituras recomendadas

### 7. Mini-Testes

- Testes curtos (5 questões) associados a livros e faixas de paginas.
- Carrega questoes reais do banco filtradas por titulo do livro e intervalo de paginas.
- Apresenta interface de teste com 4 alternativas (A-D).
- Avalia respostas e calcula percentual de acerto.
- Criterio de aprovacao: >= 70% de acerto.
- Usado para validar conhecimento apos a leitura de um topico.

### 8. Teste Adaptativo

- O teste adaptativo trabalha em cima do estado construido pelo diagnostico.
- O motor ajusta a dificuldade de acordo com o desempenho.
- O resultado final e salvo em:
  - `testes`
  - `teste_perguntas`
  - `stats`

### 9. Desafios Adaptativos

- O sistema gera desafios personalizados baseados no desempenho do candidato.
- Tipos de desafio:
  - `NOVO_DIAGNOSTICO`: Para alunos sem dados suficientes
  - `RECUPERACAO`: Quando pontuacao < 45%, identifica subtopicos com mais erros
  - `DESAFIO_AVANCADO`: Quando pontuacao > 90%, pede subida de dificuldade
  - `VALIDACAO_PRECISAO`: Quando ha evolucao positiva, valida com exercicios
  - `REVISAO_GERAL`: Caso geral para manutencao
- Os desafios sao derivados de erros comuns encontrados nos testes anteriores.

### 10. Bolsas Simuladas (Competicao Semanal)

- Simula concorrencia por bolsas de estudo reais (Bolsa Merito Atlas, Programa Horizonte STEM, etc.).
- Cada bolsa tem criterios de elegibilidade (medalhas, desempenho, evolucao, precisao, velocidade).
- Prova cronometrada com resposta digitada (A-D ou texto).
- Calculo de "match" ponderado entre perfil do candidato e criterios da bolsa.
- Ranking semanal com leaderboard.
- Registro de scores e perfil de resultado.

### 11. Relatorios E Acompanhamento

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
Onboarding -> Bootstrap -> Biblioteca -> Diagnostico -> Planeamento -> Leitura -> Mini-Testes -> Teste Adaptativo -> Desafios -> Bolsas -> Relatorios
```

Depois do primeiro diagnostico:

- o sistema conhece melhor as fraquezas reais do candidato
- o planeamento passa a ser mais inteligente
- cada novo diagnostico reforca ou ajusta o plano
- os trilhos de leitura recomendam materias especificas
- os mini-testes validam apos a leitura
- os desafios motivam a continuidade
- as bolsas criam competicao semanal
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
| `biblioteca_livros` | Guarda os livros PDF com metadados e cache Gemini |
| `biblioteca_livro_paginas` | Guarda o texto extraído pagina a pagina |
| `livro_mapa_topicos` | Guarda o mapa de topicos extraidos por IA |
| `trilho_leitura` | Guarda as recomendacoes de leitura por topico |
| `leitura_progresso` | Guarda o progresso de leitura por livro |
| `mini_testes` | Cache de mini-testes por livro e faixa de paginas |
| `bolsas` | Definicao das bolsas e criterios de elegibilidade |
| `score_bolsas` | Scores semanais dos candidatos por bolsa |

### Migrations Mais Recentes

| # | Arquivo | Tema |
|---|---------|------|
| V32 | `V32__biblioteca_livros.sql` | Biblioteca de livros e paginas |
| V35 | `V35__biblioteca_add_capa.sql` | Capa thumbnail |
| V36 | `V36__trilho_leitura.sql` | Trilhos de leitura |
| V37 | `V37__leitura_progresso.sql` | Progresso de leitura |
| V38 | `V38__mini_testes.sql` | Mini-testes |
| V39 | `V39__stats_origem_confirmacao_leitura.sql` | CONFIRMACAO_LEITURA como origem |
| V40 | `V40__biblioteca_livros_gemini_upload_cache.sql` | Cache Gemini |
| V41 | `V41__livro_mapa_topicos.sql` | Mapa de topicos dos livros |

## Componentes Principais

### Services

- `DiagnosticoService`
- `PlaneamentoEstudoService`
- `TesteAdaptativoService`
- `TesteService`
- `DisciplinaService`
- `GeminiService`
- `BibliotecaLivroService`
- `TrilhoLeituraService`
- `MiniTesteService`
- `DesafioService`
- `BolsaSimuladoService`
- `CatalogoQuestoesService`

### Repositories

- `DiagnosticoRepository`
- `PlaneamentoEstudoRepository`
- `ProgressaoRigorRepository`
- `ProgressoALunoDisciplinaRepository`
- `TesteRepository`
- `TesteStatsRepository`
- `RecomendacaoRepository`
- `BibliotecaLivroRepository`
- `LivroMapaTopicosRepository`
- `TrilhoLeituraRepository`
- `LeituraProgressoRepository`
- `MiniTesteRepository`
- `BolsaRepository`
- `ScoreBolsaRepository`

### Controllers

- `DiagnosticoCandidatoController`
- `DiagnosticoListController`
- `RelatoriosController`
- `TesteAdaptativoController`
- `BibliotecaController`
- `BolsasController`
- `BolsaSimuladoController`
- `PlanoPersonalizadoController`

### Modals

- `AddLivroModalController`
- `MiniTesteModalController`
- `TopicModalController`
- `AvatarPickerModalController`

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
- a biblioteca de livros fornece material de estudo com extracao de topicos por IA
- os trilhos de leitura recomendam leituras especificas baseadas nas fraquezas do candidato
- os mini-testes validam o conhecimento apos a leitura
- os desafios adaptativos motivam o candidato com missoes personalizadas
- as bolsas simuladas criam competicao semanal com ranking
- se o candidato quiser recomecar, basta limpar os diagnosticos e o estado derivado
