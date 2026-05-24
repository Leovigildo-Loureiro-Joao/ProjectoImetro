# Visao do fluxo adaptativo

## Escopo do produto

O fluxo adaptativo atual foi reduzido ao estudo individual do candidato, com foco operacional em `Matematica` e `Fisica`.

Regras de contexto:

- nao existe mais fluxo ativo de `orientador`
- a base de questoes nasce da BD e dos PDFs da disciplina
- o diagnostico continua a ser a porta de entrada do ciclo adaptativo

## Fontes de verdade

- `perguntas`: banco real de questoes por disciplina, topico e subtopico
- `diagnosticos`: fotografia de entrada do candidato
- `progressao_rigor`: estado vivo do subtopico
- `recomendacoes_rigor`: debilidades abertas e direcao do proximo ciclo
- `testes`: sessao adaptativa concluida
- `teste_perguntas`: detalhe de cada pergunta respondida
- `stats`: resumo consolidado do teste

## Ciclo correto do produto

1. O candidato escolhe `Matematica` e/ou `Fisica`.
2. O sistema prepara uploads e tenta gerar a base real a partir dos PDFs.
3. O candidato faz o diagnostico inicial da disciplina.
4. O sistema grava o diagnostico e atualiza `progressao_rigor` e `recomendacoes_rigor`.
5. O teste adaptativo nasce desse contexto e ataca os subtopicos em aberto.
6. O resultado do teste entra em `testes`, `teste_perguntas` e `stats`.
7. O ciclo continua ate os sinais de revisao perderem forca ou o sistema pedir novo diagnostico.

## Regras de implementacao

- `diagnosticos` guardam o estado de entrada; o teste nao deve reescrever esse registo
- `progressao_rigor` reflete o estado atual do subtopico
- `recomendacoes_rigor` aponta para o que ainda precisa de reforco
- `testes` e `stats` medem evolucao sobre um contexto anterior
- o bootstrap automatico de perguntas so roda para disciplinas suportadas

## Metricas de referencia

### Precisao por resposta

`p_resposta_j in [0, 1]`

Regra:

- se a pergunta tiver pesos por alternativa, usa o peso da alternativa marcada
- se for pergunta legada, usa o fallback do projeto

### Precisao por subtopico

`P_i = (sum p_resposta_j) / totais_i`

Esta e a unidade minima de progresso.

### Precisao global

Media simples:

`P_geral = (P_1 + P_2 + ... + P_n) / n`

Media ponderada:

`P_ponderada = (sum acertos_i) / (sum totais_i)`

Uso:

- `P_geral` mede equilibrio
- `P_ponderada` mede desempenho agregado real

### Ganho normalizado

`G = (P_atual - P_anterior) / max(epsilon, 1 - P_anterior)`

Uso:

- comparar dois estados consecutivos do mesmo foco

### Resiliencia

`R = ((P_2 + P_3) / 2) / max(epsilon, P_1)`

Uso:

- medir recuperacao apos erro, queda de ritmo ou serie adversa

### Velocidade

`V_raw = tempo_total / max(1, total_acertos)`

`V_rel = T_base / T_usuario`

Uso:

- `V_raw` como observavel
- `V_rel` como score comparavel

### Consistencia

`Delta_P_n = P_(n+1) - P_n`

`CV_ganhos = stddev(Delta_P) / max(epsilon, abs(media(Delta_P)))`

`C = corr+(n, P_n) * (1 - min(1, CV_ganhos))`

Uso:

- distinguir crescimento sustentado de crescimento ruidoso

### Logica

`L = acertos_baixa_estruturacao / total_baixa_estruturacao`

Uso:

- medir desempenho em perguntas menos mecanicas e mais inferenciais

## Estado atual do codigo

Ja esta implementado:

- filtro de disciplinas suportadas em `DisciplinaService`
- bootstrap async de base real via `PerguntasBootstrapService`
- extracao de topicos e geracao de perguntas por lotes
- leitura real de perguntas, incluindo pesos e graficos
- persistencia de `diagnosticos`, `progressao_rigor` e `recomendacoes_rigor`
- persistencia de `testes`, `stats` e `teste_perguntas`

Ainda precisa de endurecimento:

- parte dos limiares do motor continua hardcoded em `TesteAdaptativoController` e `CalculoStats`
- a gravacao final de diagnostico e teste ainda acontece em fluxo sincrono da UI
- `TestePerguntasRepository` ainda precisa usar a mesma `Connection` da transacao principal

## Leitura recomendada para o proximo passo

1. Remover hardcodes do motor adaptativo usando as tabelas de configuracao.
2. Fechar atomicidade total do salvamento de teste.
3. Mover gravacoes finais para background com feedback visual claro.
4. Refinar o criterio que decide quando um novo diagnostico deve ser liberado.
