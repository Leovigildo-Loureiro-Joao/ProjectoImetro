# Checklist da configuracao adaptativa

Objetivo: tirar regras fixas do codigo, ler a configuracao real do banco e deixar o motor auditavel.

## Estado atual

Ja existe base de configuracao no schema:

- `configuracoes_teste_adaptativo`
- `configuracoes_teste_adaptativo_niveis`
- `configuracoes_teste_adaptativo_duracoes`
- `V17__configuracoes_teste_adaptativo.sql`
- `V25__configuracoes_limiar_revisao.sql`

Ja existe consumo parcial dessa configuracao:

- `DiagnosticoService` usa limiares por nivel para atualizar revisao e progresso
- DTO `ConfiguracaoTesteAdaptativoNivelDto` ja existe
- repositorios de configuracao ja existem no projeto

O que ainda continua hardcoded:

- limites de questoes por duracao em `TesteAdaptativoController`
- regras de subida e descida de nivel em `TesteAdaptativoController`
- faixas de leitura do resultado final em `TesteAdaptativoController`
- varios baselines e pesos em `CalculoStats`
- fallbacks de rigor em `DiagnosticoService` e `TesteAdaptativoService`

## 1. Confirmar o modelo de dados

- [x] manter `V17` como base do perfil adaptativo
- [x] manter `V25` para limiares de acerto, erro e revisao
- [ ] confirmar se a tabela de duracoes usada pelo codigo aponta para o nome correto da tabela do banco
- [ ] decidir se vai existir so um perfil ativo ou varios perfis por disciplina no futuro

## 2. Fechar a leitura central da configuracao

- [ ] criar um facade/service unico para devolver a configuracao ativa do motor
- [ ] ler tempos sugeridos por nivel a partir do banco
- [ ] ler limites por duracao a partir do banco
- [ ] ler thresholds de subida e descida a partir do banco
- [ ] ler pesos de consistencia e resiliencia a partir do banco
- [ ] deixar a UI e os services dependerem desse facade em vez de constantes espalhadas

## 3. Remover hardcodes da UI do teste

Arquivo principal: `TesteAdaptativoController`

- [ ] trocar `CURTO = 5`, `MEDIO = 7`, `LONGO = 10`
- [ ] trocar `acertos_subir_rapido = 2`
- [ ] trocar `acertos_subir_lento = 3`
- [ ] trocar `erros_descer = 2`
- [ ] trocar limites fixos de "rapido" e "muito lento"
- [ ] trocar faixas fixas da mensagem final de resultado
- [ ] trocar recomendacoes finais baseadas em percentuais fixos

## 4. Remover hardcodes do calculo de metricas

Arquivo principal: `CalculoStats`

- [ ] trocar baseline fixo de velocidade por valor vindo da configuracao
- [ ] trocar janelas fixas de consistencia
- [ ] trocar pesos fixos de consistencia
- [ ] trocar pesos fixos de resiliencia
- [ ] trocar limiares fixos de subida/descida de rigor
- [ ] trocar floors e fallbacks neutros que ainda estao marcados com `TODO CONFIG_ADAPTATIVA`

## 5. Persistir o snapshot real usado no teste

- [x] `testes` ja tem `configuracao_teste_adaptativo_id`
- [ ] garantir que o valor real da configuracao ativa e sempre salvo
- [ ] garantir que `limite_inferior` e `limite_superior` gravados batem com a configuracao usada
- [ ] garantir que `tempo_sugerido_segundos` por pergunta reflete o nivel/configuracao do momento
- [ ] decidir se vale salvar mais campos do perfil adaptativo diretamente no historico para auditoria futura

## 6. Validacao funcional

- [ ] alterar valores no banco e confirmar mudanca sem recompilar
- [ ] confirmar que diagnostico e teste leem a mesma configuracao ativa
- [ ] confirmar que mudar o perfil nao quebra historico antigo
- [ ] confirmar que `stats` e `teste_perguntas` continuam coerentes com os novos thresholds

## 7. Resultado esperado

Quando este checklist estiver fechado:

- o motor adaptativo para de depender de numeros magicos espalhados
- o comportamento pode ser calibrado pelo banco
- o historico de testes fica mais auditavel
- Matematica e Fisica passam a responder a ajustes finos sem mexer no codigo
