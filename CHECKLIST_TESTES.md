# Checklist de Finalizacao dos Testes

Guia pratico para fechar a funcionalidade de `testes` sem misturar responsabilidades com `diagnosticos`.

Este ficheiro foi organizado por ordem de execucao e por arquivo, para poderes seguir passo a passo sem te perderes.

## Estado atual

- [x] O `insert` principal de teste foi corrigido para gravar em `testes`.
- [ ] A responsabilidade de `diagnostico` foi removida do fluxo principal de `TesteService`.
- [ ] Falta salvar `stats`.
- [ ] Falta salvar `teste_perguntas`.
- [x] Falta ligar o fim do teste adaptativo ao salvamento real no banco.
- [ ] Falta revisar leitura dos dados reais de `testes`.

## Ordem recomendada de execucao

Segue esta ordem. Ela foi pensada para evitar retrabalho:

1. Fechar os dados que o controlador precisa enviar.
2. Ajustar o `TesteService` para receber tudo.
3. Fechar o `TesteRepository` com os campos reais finais.
4. Criar o salvamento de `stats`.
5. Criar o salvamento de `teste_perguntas`.
6. Ligar tudo no fluxo final da tela.
7. Revisar os DTOs e mapeamentos.
8. Testar no banco e na interface.

---

## 1. Arquivo: `src/main/java/com/imetro/ui/controller/candidato/TesteAdaptativoController.java`

Objetivo simples: garantir que quando o teste termina, os dados reais chegam ao service antes da troca de tela.

- [x] Confirmar onde o teste termina de verdade no metodo `finalizarTesteAdaptativo()`.
- [x] Antes de abrir a tela de resultado, chamar `TesteService.registrarTesteConcluido(...)`.
- [x] Passar o `candidatoId` real.
- [ ] Passar a lista `questoes`.
- [x] Passar a lista `respostasUsuario`.
- [x] Passar o tempo total formatado (`tempo.getText()` ou equivalente).
- [ ] Passar `temposResposta`.
  Explicacao simples: isso e o tempo gasto em cada questao, nao so o tempo total.
- [ ] Passar `topicosSelecionados`.
- [ ] Passar `subtopicosSelecionados`.
- [ ] Passar o `nivelInicial`.
  Explicacao simples: o nivel em que o teste comecou.
- [ ] Passar o `nivelFinal`.
  Explicacao simples: o nivel em que o teste terminou.
- [x] Se existir contexto de diagnostico anterior, decidir se tambem vai passar `diagnosticoId`.
- [x] Garantir que o salvar acontece antes de `App.swapContent(...)`.
- [ ] Garantir que, se o salvamento falhar, o utilizador nao veja sucesso falso.

### Resultado esperado desta etapa

- O controlador deixa de ser so visual e passa a disparar o salvamento real.
- O service passa a receber tudo o que precisa para persistir o teste completo.

---

## 2. Arquivo: `src/main/java/com/imetro/services/TesteService.java`

Objetivo simples: transformar o service no orquestrador (quem coordena a ordem do salvamento) do teste.

- [x] Rever a assinatura de `registrarTesteConcluido(...)`.
- [ ] Adicionar os parametros que ainda faltam:
  `List<Long> temposResposta`, `List<String> topicosSelecionados`, `List<String> subtopicosSelecionados`, `String nivelInicial`, `String nivelFinal`, `UUID diagnosticoId` se fizer sentido.
- [x] Manter a validacao inicial para evitar salvar listas vazias ou dados nulos importantes.
- [x] Continuar agrupando por disciplina, se esse comportamento ainda for o desejado.
  Explicacao simples: se um teste misturar questoes de varias disciplinas, hoje o codigo separa por disciplina e salva uma entrada para cada grupo.
- [ ] Decidir se isso vai continuar assim ou se um teste deve gerar apenas um registo.
- [ ] Definir `resultado`.
  Explicacao simples: muito provavelmente sera o mesmo valor de `percentualAcerto`, mas isso deve ficar padronizado.
- [ ] Definir `nivel_inicial` com base no nivel de arranque do teste.
- [ ] Definir `nivel_final` com base no nivel final alcancado.
- [ ] Montar o JSON de `topicosSelecionados`.
- [ ] Montar o JSON de `subtopicosSelecionados`.
- [x] Chamar `testeRepository.inserir(...)` com todos os campos finais.
- [x] Guardar o `testeId` retornado.
- [x] Logo a seguir, chamar o repository de `stats`.
- [x] Logo a seguir, chamar o repository de `teste_perguntas`.
- [ ] Fazer tudo dentro da mesma transacao (transacao = salvar tudo em bloco; se uma parte falhar, nada fica salvo pela metade).
- [ ] Atualizar a mensagem de erro para refletir exatamente a fase que falhou, se quiseres logs mais claros.

### Resultado esperado desta etapa

- Um unico ponto coordena o salvamento completo de `teste`, `stats` e `teste_perguntas`.
- O fluxo fica mais previsivel e mais facil de depurar.

---

## 3. Arquivo: `src/main/java/com/imetro/persistence/repository/TesteRepository.java`

Objetivo simples: fechar os campos reais da tabela `testes`.

- [x] O `insert` ja grava em `testes`.
- [x] Rever se `diagnostico_id` precisa entrar no `insert`.
- [x] Rever se `topicos` precisa entrar no `insert`.
- [x] Rever se `subtopicos` precisa entrar no `insert`.
- [x] Rever se `resultado` vai mesmo ser `percentualAcerto`.
- [x] Rever se `nivel_inicial` e `nivel_final` estao vindo com valor real e nao so valor repetido.
- [ ] Rever `limite_questoes`.
  Explicacao simples: pode ser o total planeado do teste, nao necessariamente o total respondido.
- [ ] Rever `limite_inferior` e `limite_superior`.
  Explicacao simples: esses campos parecem guardar a faixa adaptativa (intervalo de dificuldade, ou “janela de nivel” do teste).
- [x] Se `topicos` e `subtopicos` forem salvos, usar `cast(? as jsonb)` no SQL.
  Explicacao simples: `jsonb` e o tipo do Postgres para guardar JSON de forma estruturada.
- [x] Remover parametros que nao forem realmente usados.
  Explicacao simples: se um parametro entra no metodo mas nao vai para o banco nem para outra regra, ele so aumenta a confusao.

### Resultado esperado desta etapa

- A tabela `testes` fica com dados reais, coerentes e completos.

---

## 4. Arquivo: `src/main/java/com/imetro/persistence/repository/TesteStatsRepository.java`

Objetivo simples: parar de usar este repository so para leitura e passar a salvar `stats`.

- [x] Criar um metodo `inserir(...)` ou `upsert(...)`.
- [ ] Preferir `upsert(...)` se quiseres proteger contra tentativa de salvar duas vezes o mesmo `teste_id`.
  Explicacao simples: `upsert` significa “se existir, atualiza; se nao existir, insere”.
- [ ] Salvar pelo menos estes campos:
  `teste_id`, `diagnostico_id`, `candidato_id`, `disciplina_id`, `disciplina_nome`, `origem`, `tempo_total_segundos`, `tempo_medio_segundos`, `total_questoes`, `total_acertos`, `total_erros`, `percentual_acerto`, `velocidade`, `precisao`, `consistencia`, `logica`, `resiliencia`, `observacoes`.
- [ ] Definir `origem = 'TESTE'`.
- [ ] Calcular `tempo_medio_segundos`.
  Explicacao simples: tempo total dividido pelo numero de questoes.
- [ ] Guardar `erros_comuns` como `[]` por agora, se ainda nao houver regra pronta.
- [ ] Guardar `melhorias` como `[]` por agora, se ainda nao houver regra pronta.
- [ ] Se quiseres, deixar comentarios curtos no codigo explicando as escolhas temporarias.

### Resultado esperado desta etapa

- Cada teste salvo passa a ter um resumo analitico proprio em `stats`.

---

## 5. Novo arquivo: `src/main/java/com/imetro/persistence/repository/TestePerguntasRepository.java`

Objetivo simples: salvar o detalhe de cada questao respondida no teste.

- [ ] Criar a classe `TestePerguntasRepository`.
- [ ] Fazer a classe seguir o mesmo estilo dos outros repositories do projeto.
- [ ] Criar um metodo para salvar varias linhas de uma vez.
  Explicacao simples: isto e insercao em lote (batch insert), ou seja, varias linhas num unico fluxo.
- [ ] Para cada questao, salvar:
  `teste_id`, `pergunta_id`, `ordem`, `resposta_dada`, `tempo_segundos`, `acertou`.
- [ ] Se ja tiveres regra pronta, salvar tambem:
  `precisao`, `velocidade`, `consistencia`, `resiliencia`.
- [ ] Garantir que a ordem da questao comeca em `1` ou fica no padrao que o projeto ja usa.
- [ ] Garantir que `pergunta_id` vem do `Questao.getId()`.
- [ ] Se o id da questao vier como `String`, converter com cuidado para `UUID` so se o banco realmente guardar UUID nessa coluna.

### Resultado esperado desta etapa

- O projeto passa a guardar o historico detalhado do teste, nao so o resumo geral.

---

## 6. Arquivo: `src/main/java/com/imetro/domain/dto/test/TestDtoAll.java`

Objetivo simples: corrigir a leitura dos dados reais de `testes`.

- [ ] Rever `ParseMapDto(...)`.
- [ ] Corrigir o uso de `link.get("nome")`.
  Explicacao simples: pelo schema atual, o campo parece ser `disciplina_nome`, nao `nome`.
- [ ] Tornar `diagnostico_id` tolerante a `null`.
- [ ] Tornar `orientador_id` tolerante a `null`.
- [ ] Tornar `relatorio_id` tolerante a `null`.
- [ ] Tornar `nivel_inicial` e `nivel_final` tolerantes a `null`.
- [ ] Rever os casts diretos como `(float)`, `(int)` e `(double)`.
  Explicacao simples: se o banco devolver outro tipo numerico, esses casts podem falhar. O mais seguro e usar `instanceof Number`.
- [ ] Rever `topicos` e `subtopicos`.
  Explicacao simples: hoje eles estao como `Object[]`, mas podem estar vindo como JSON do banco.

### Resultado esperado desta etapa

- A leitura de `testes` deixa de depender de suposicoes erradas sobre os nomes e tipos das colunas.

---

## 7. Arquivo: `src/main/java/com/imetro/services/TesteService.java` metodo `Stats()`

Objetivo simples: limpar a parte que ainda pensa como diagnostico.

- [ ] Rever se `Stats()` deve continuar comparando teste com diagnostico.
- [ ] Se a ideia for mostrar evolucao dos testes, ler os dados a partir de `stats` ou do historico de `testes`.
- [ ] Evitar depender de `diagnosticoRepository.findById(test.diagnostico_id())` como regra central.
- [ ] Se `diagnostico_id` for opcional, proteger este fluxo contra `null`.
- [ ] Renomear o metodo `Stats()` para um nome mais claro no futuro, se quiseres.
  Explicacao simples: nomes mais descritivos ajudam muito quando o projeto crescer.

### Resultado esperado desta etapa

- O service de testes deixa de depender mentalmente da logica de diagnosticos para calcular tudo.

---

## 8. Arquivo: `src/main/resources/com/imetro/views/pages/candidato/testes.fxml`

Objetivo simples: garantir que a tela dos testes se apoia em dados reais quando o fluxo estiver completo.

- [ ] Confirmar que a tela mostra ou vai mostrar historico real dos testes.
- [ ] Confirmar que o usuario consegue perceber que o teste foi salvo.
- [ ] Se existir lista de historico, garantir que ela consulta `testes` e `stats`.
- [ ] Se ainda estiver tudo em dados montados na hora, marcar isso como proximo ajuste apos o backend fechar.

### Resultado esperado desta etapa

- A interface passa a refletir o que foi realmente salvo no banco.

---

## 9. Validacao final no banco

Objetivo simples: confirmar que o fluxo esta completo de verdade.

Depois de implementar tudo acima, faz esta verificacao manual:

- [ ] Concluir um teste real pela interface.
- [ ] Confirmar que nasceu `1` linha em `testes`.
- [ ] Confirmar que nasceu `1` linha em `stats`.
- [ ] Confirmar que nasceram `N` linhas em `teste_perguntas`.
- [ ] Confirmar que `N` e igual ao numero de questoes respondidas.
- [ ] Confirmar que `total_acertos + total_erros = total_questoes`.
- [ ] Confirmar que `tempo_medio_segundos` bate com `tempo_total_segundos / total_questoes`.
- [ ] Confirmar que `percentual_acerto` esta coerente.
- [ ] Confirmar que `disciplina_nome` foi salva corretamente.
- [ ] Confirmar que `topicos` e `subtopicos` ficaram certos, se esses campos forem usados.

---

## 10. Sequencia pratica para tu executares sem te perder

Se quiseres seguir quase como receita:

1. Primeiro fecha o `TesteAdaptativoController`.
2. Depois ajusta a assinatura de `registrarTesteConcluido(...)`.
3. Depois fecha o `TesteRepository`.
4. Depois cria o `TesteStatsRepository.inserir(...)` ou `upsert(...)`.
5. Depois cria o `TestePerguntasRepository`.
6. Depois faz o `TesteService` chamar os 3 salvamentos.
7. Depois corrige `TestDtoAll`.
8. Depois testa no banco.
9. Depois testa na tela.

---

## 11. Observacoes de arquitetura em linguagem simples

- `TesteService` deve coordenar, nao carregar regras de diagnostico.
- `TesteRepository` deve gravar o registo principal do teste.
- `TesteStatsRepository` deve gravar o resumo numerico do teste.
- `TestePerguntasRepository` deve gravar o detalhe de cada pergunta respondida.
- `DiagnosticoRepository` deve continuar focado em diagnostico, progresso e rigor.

Se mantiveres esta divisao, o codigo fica mais facil de entender, manter e depurar.
