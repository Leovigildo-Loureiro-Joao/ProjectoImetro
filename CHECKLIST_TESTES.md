# Checklist de endurecimento dos testes

Guia pratico para alinhar o fluxo de `testes` com o estado atual do codigo e fechar as ultimas lacunas.

## Estado atual

Ja esta montado:

- [x] `TesteAdaptativoController` chama `TesteService.registrarTesteConcluido(...)`
- [x] `TesteService` grava o registo principal em `testes`
- [x] `TesteService` grava resumo em `stats`
- [x] `TesteService` grava detalhe em `teste_perguntas`
- [x] `TesteRepository` persiste `diagnostico_id`, `disciplina_id`, `disciplina_nome`, `topicos`, `subtopicos`, limites e `configuracao_teste_adaptativo_id`

O que ainda precisa de endurecimento:

- [ ] o fecho do teste ainda corre no fluxo de UI
- [ ] `TestePerguntasRepository` ainda nao usa a mesma `Connection` da transacao principal
- [ ] existem sinais de duplicacao de caminhos de salvamento em `TesteService`
- [ ] a leitura de DTOs de `testes` ainda precisa de tolerancia melhor para nulos e tipos numericos
- [ ] parte da configuracao adaptativa ainda entra por hardcode

## 1. Prioridade maxima: atomicidade real

Arquivo principal: `TestePerguntasRepository`

- [ ] criar `inserir(Connection conn, Teste_Pergunta testePergunta, UUID testeId)`
- [ ] garantir que `TesteService` usa essa sobrecarga dentro da mesma transacao
- [ ] confirmar que nenhum insert do detalhe abre ligacao propria durante o commit principal

Resultado esperado:

- `testes`, `stats` e `teste_perguntas` passam a nascer ou falhar juntos

## 2. Prioridade maxima: tirar a gravacao da UI

Arquivo principal: `TesteAdaptativoController`

- [ ] mover a chamada de `registrarTesteConcluido(...)` para `Task` ou `CompletableFuture` com executor explicito
- [ ] mostrar overlay de "A guardar resultado..."
- [ ] navegar para a tela final apenas depois de sucesso
- [ ] mostrar erro claro se o salvamento falhar

Resultado esperado:

- sem sucesso falso
- menos freeze no fim do teste

## 3. Reduzir duplicacao em `TesteService`

Arquivo principal: `TesteService`

- [ ] identificar e consolidar os dois caminhos de persistencia que hoje aparecem no service
- [ ] manter um unico caminho oficial para o salvamento completo
- [ ] concentrar validacao, transacao e montagem de `stats` num unico fluxo

Resultado esperado:

- menos risco de divergencia entre dois modos de salvar teste

## 4. Harden da leitura de testes

Arquivo principal: `TestDtoAll`

- [ ] rever parse de `diagnostico_id`, `relatorio_id` e campos opcionais
- [ ] substituir casts diretos por leitura via `Number`
- [ ] garantir tolerancia a `jsonb` em `topicos` e `subtopicos`
- [ ] validar nomes reais das colunas usadas no parse

Resultado esperado:

- DTO robusto para historico real de testes

## 5. Snapshot da configuracao adaptativa

Arquivos principais:

- `TesteRepository`
- `TesteService`
- `TesteAdaptativoController`

- [x] `testes` ja tem `configuracao_teste_adaptativo_id`
- [ ] garantir que esse id e preenchido com a configuracao realmente usada
- [ ] rever se `limite_inferior` e `limite_superior` refletem o nivel/configuracao do momento
- [ ] alinhar `tempo_sugerido_segundos` por pergunta com a configuracao ativa

Resultado esperado:

- historico de teste mais auditavel

## 6. Qualidade dos dados de `stats`

Arquivo principal: `TesteStatsRepository`

- [ ] confirmar se `erros_comuns` e `melhorias` ja estao no formato esperado para todas as telas
- [ ] validar `origem = 'TESTE'`
- [ ] rever coerencia de `tempo_total_segundos`, `tempo_medio_segundos`, `percentual_acerto`, `velocidade`, `precisao`, `consistencia`, `logica` e `resiliencia`
- [ ] confirmar se `observacoes` estao a explicar o foco real do teste

## 7. Validacao final no banco

Depois das correcoes acima:

- [ ] concluir um teste real pela interface
- [ ] confirmar `1` linha em `testes`
- [ ] confirmar `1` linha correspondente em `stats`
- [ ] confirmar `N` linhas em `teste_perguntas`
- [ ] confirmar que `N` bate com o numero de perguntas respondidas
- [ ] confirmar que a transacao falha por completo se uma etapa der erro
- [ ] confirmar que `topicos` e `subtopicos` ficaram coerentes com o teste

## 8. Resultado esperado

Quando este checklist estiver fechado:

- o fluxo de teste fica realmente confiavel
- o historico salvo passa a ser auditavel ponta a ponta
- a UI deixa de bloquear no fecho
- o motor adaptativo ganha uma base melhor para evolucao futura
