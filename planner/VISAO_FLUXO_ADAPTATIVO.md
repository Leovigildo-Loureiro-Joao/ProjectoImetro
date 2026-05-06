# Visao do fluxo adaptativo

## Regra central

O diagnostico e a origem do percurso. Ele identifica debilidades reais por disciplina, topico e subtopico. O teste adaptativo nao substitui esse diagnostico; ele existe para trabalhar em cima dele e provar evolucao.

## Papel de cada tabela

- `diagnosticos`: fotografia de entrada. Guarda o ponto de partida do aluno e nao deve ser reescrito pelo teste.
- `progressao_rigor`: estado vivo por `aluno + disciplina + subtopico`. Diz em que nivel de rigor o aluno esta naquele subtopico.
- `recomendacoes_rigor`: saida do diagnostico. Define o foco que o proximo ciclo de testes deve atacar e marca se ainda ha debilidade aberta.
- `testes`: historico de cada sessao adaptativa feita em cima de um diagnostico.
- `stats`: resumo consolidado de cada teste para comparar ganho real contra o diagnostico-base.

## Ciclo correto do produto

1. O aluno faz o primeiro diagnostico.
2. O sistema grava debilidades e recomenda subtopicos com rigor inicial.
3. O teste adaptativo nasce desse diagnostico e deve ficar ligado a ele por `diagnostico_id`.
4. Cada novo teste mede se o aluno esta a superar as debilidades, sem alterar o diagnostico original.
5. O sistema so pede novo diagnostico quando o ciclo atual estiver suficientemente vencido ou esgotado.

## Regra de negocio que deve guiar a implementacao

- Todo teste adaptativo deve apontar para um `diagnostico_id`.
- O teste pode atualizar `progressao_rigor`, `testes` e `stats`, mas nao deve editar a linha original de `diagnosticos`.
- O foco principal do teste deve sair de `recomendacoes_rigor` e de `progressao_rigor`, nao de escolhas soltas sem historico.
- Um novo diagnostico so deve ser desbloqueado quando as debilidades abertas do diagnostico atual forem superadas.
- "Superadas" aqui significa, no minimo, que os subtopicos em revisao deixaram de pedir reforco (`precisa_revisao = false`) e o desempenho dos ultimos testes sustentou o rigor recomendado.

## Metricas oficiais do motor

Estas formulas sao a referencia oficial do fluxo adaptativo e devem substituir qualquer calculo simplificado ainda presente no codigo.

### Convencoes

- Escala padrao: `[0, 1]`, exceto metricas `raw`.
- `i`: subtopico.
- `n`: ordem do teste dentro da serie historica.
- Em denominadores com risco de zero, usar `max(epsilon, denominador)`.

### Precisao por subtopico

`P_i = acertos_i / totais_i`

Este e o nucleo do progresso por subtopico. Todo crescimento local deve nascer daqui.

### Precisao global

Media simples:

`P_geral = (P_1 + P_2 + ... + P_n) / n`

Media ponderada:

`P_ponderada = (sum acertos_i) / (sum totais_i)`

Regra:

- `P_geral` mede equilibrio entre subtopicos.
- `P_ponderada` mede desempenho real agregado e deve pesar mais nas decisoes do motor.

### Logica

`L = acertos_baixa_estruturacao / total_baixa_estruturacao`

`L` nao deve nascer do nivel nominal da questao, mas sim do conjunto de questoes marcadas como menos estruturadas e mais inferenciais.

### Resiliencia

`R = ((P_2 + P_3) / 2) / P_1`

Leitura:

- `P_1`: base inicial do foco observado
- `P_2` e `P_3`: respostas seguintes do mesmo ciclo
- `R > 1`: recuperacao acima da base
- `R < 1`: recuperacao abaixo da base

Versao operacional:

`R = ((P_2 + P_3) / 2) / max(epsilon, P_1)`

### Velocidade

Medida bruta:

`V_raw = tempo_medio_por_acerto`

Operacionalmente:

`V_raw = tempo_total / max(1, total_acertos)`

Forma relativa:

`V_rel = T_base / T_usuario`

Forma inversa:

`V_inv = 1 / V_raw`

Regra:

- guardar `V_raw` como observavel de base
- usar `V_rel` como score comparavel

### Consistencia

Ganhos sucessivos:

`Delta_P_n = P_(n+1) - P_n`

Correlacao positiva entre ordem do teste e desempenho:

`corr+(n, P_n) = max(0, corr_Pearson(n, P_n))`

Coeficiente de variacao dos ganhos:

`CV_ganhos = stddev(Delta_P) / media(Delta_P)`

Formula conceitual:

`C = corr+(n, P_n) * (1 - CV_ganhos)`

Versao operacional recomendada:

`CV_ganhos = stddev(Delta_P) / max(epsilon, abs(media(Delta_P)))`

`C = corr+(n, P_n) * (1 - min(1, CV_ganhos))`

Interpretacao:

- `C` alto: crescimento suave e confiavel
- `C` baixo: crescimento irregular, com ruido ou regressao

### Ganho normalizado

`G = (P_atual - P_anterior) / (1 - P_anterior)`

Versao operacional:

`G = (P_atual - P_anterior) / max(epsilon, 1 - P_anterior)`

`G` deve ser a metrica de melhoria relativa entre dois estados consecutivos do mesmo foco.

## Como essas metricas entram no ciclo

- `diagnosticos` devem guardar a base inicial de desempenho, principalmente `P_i`, `P_ponderada`, `L` e os sinais iniciais do foco.
- `testes` devem guardar a sessao executada em cima de um `diagnostico_id`.
- `stats` devem consolidar `P_i`, `P_geral`, `P_ponderada`, `R`, `V_raw`, `V_rel`, `C`, `L` e `G`.
- `progressao_rigor` nao deve inventar progresso; deve refletir a evolucao observada por essas metricas.
- `recomendacoes_rigor` devem apontar para os subtopicos onde `P_i`, `G`, `R` e `C` ainda nao sustentam dominacao.
- O desbloqueio de novo diagnostico deve nascer da superacao sustentada das debilidades, e nao apenas de uma subida isolada.

## Leitura do estado atual do codigo

- O diagnostico ja grava resultado real em `diagnosticos`.
- O diagnostico ja atualiza `progressao_rigor` e `recomendacoes_rigor` por `subtopico`.
- O teste adaptativo ja usa foco por disciplina/topico/subtopico na UI.
- O teste adaptativo ainda nao persiste `testes` e `stats`, por isso ainda nao fecha o ciclo de evidencias.
- Havia um desencontro entre a BD nova por `subtopico` e a leitura do teste adaptativo, que precisa ficar alinhada em todo o fluxo.
- As metricas atuais no codigo ainda sao simplificadas em alguns pontos e devem convergir para este contrato matematico.

## Proximo passo tecnico recomendado

1. Fechar a migration de `subtopico` para que `progressao_rigor` e `recomendacoes_rigor` falem a mesma lingua.
2. Persistir o fim do teste adaptativo em `testes` e `stats`, sempre ligado ao diagnostico-base.
3. Criar a regra de desbloqueio: enquanto houver debilidade aberta, o sistema continua no ciclo de teste; quando o ciclo for vencido, libera novo diagnostico.
