# Checklist da Configuracao Adaptativa

Objetivo: tirar os valores fixos do motor adaptativo do codigo, padronizar a origem e evitar `dados voadores`.

Este ficheiro e o proximo alvo. Usa-o como trilho quando fores pegar os valores com calma.

## Regra simples antes de mexer

- configuracao base = fica no banco e vale para o motor
- estado do aluno = muda com o desempenho e fica em tabelas de progresso
- snapshot do teste = e o valor realmente usado naquele teste e fica salvo em `testes` / `teste_perguntas`

---

## 1. O que ja foi preparado

- [x] Criar migration `V17__configuracoes_teste_adaptativo.sql`
- [x] Criar tabela `configuracoes_teste_adaptativo`
- [x] Criar tabela `configuracoes_teste_adaptativo_niveis`
- [x] Criar tabela `configuracoes_teste_adaptativo_duracoes`
- [x] Criar `configuracao_teste_adaptativo_id` em `testes`
- [x] Seed inicial com perfil `PADRAO_V1`

---

## 2. Mapa dos valores para nao te perderes

| Bloco | Valor | Hoje no codigo | Destino no banco | Seed inicial |
| --- | --- | --- | --- | --- |
| Nivel | `tempo_sugerido_segundos` FACIL | `40` | `configuracoes_teste_adaptativo_niveis` | `40` |
| Nivel | `tempo_sugerido_segundos` MEDIO | `55` | `configuracoes_teste_adaptativo_niveis` | `55` |
| Nivel | `tempo_sugerido_segundos` DIFICIL | `70` | `configuracoes_teste_adaptativo_niveis` | `70` |
| Nivel | `tempo_sugerido_segundos` EXPERT | `85` | `configuracoes_teste_adaptativo_niveis` | `85` |
| Nivel | `rigor_base` FACIL | `0.18` | `configuracoes_teste_adaptativo_niveis` | `0.18` |
| Nivel | `rigor_base` MEDIO | `0.35` | `configuracoes_teste_adaptativo_niveis` | `0.35` |
| Nivel | `rigor_base` DIFICIL | `0.58` | `configuracoes_teste_adaptativo_niveis` | `0.58` |
| Nivel | `rigor_base` EXPERT | `0.78` | `configuracoes_teste_adaptativo_niveis` | `0.78` |
| Nivel | `limite_inferior` FACIL | nao padronizado | `configuracoes_teste_adaptativo_niveis` | `0.05` |
| Nivel | `limite_superior` FACIL | nao padronizado | `configuracoes_teste_adaptativo_niveis` | `0.30` |
| Nivel | `limite_inferior` MEDIO | nao padronizado | `configuracoes_teste_adaptativo_niveis` | `0.20` |
| Nivel | `limite_superior` MEDIO | nao padronizado | `configuracoes_teste_adaptativo_niveis` | `0.50` |
| Nivel | `limite_inferior` DIFICIL | nao padronizado | `configuracoes_teste_adaptativo_niveis` | `0.45` |
| Nivel | `limite_superior` DIFICIL | nao padronizado | `configuracoes_teste_adaptativo_niveis` | `0.72` |
| Nivel | `limite_inferior` EXPERT | nao padronizado | `configuracoes_teste_adaptativo_niveis` | `0.65` |
| Nivel | `limite_superior` EXPERT | nao padronizado | `configuracoes_teste_adaptativo_niveis` | `0.92` |
| Duracao | `CURTO` | `5` questoes | `configuracoes_teste_adaptativo_duracoes` | `5` |
| Duracao | `MEDIO` | `7` questoes | `configuracoes_teste_adaptativo_duracoes` | `7` |
| Duracao | `LONGO` | `10` questoes | `configuracoes_teste_adaptativo_duracoes` | `10` |
| Motor | `tempo_lento_fator` | `1.25` | `configuracoes_teste_adaptativo` | `1.25` |
| Motor | `tempo_recuperacao_fator` | `1.10` | `configuracoes_teste_adaptativo` | `1.10` |
| Motor | `acertos_subir_rapido` | `2` | `configuracoes_teste_adaptativo` | `2` |
| Motor | `acertos_subir_lento` | `3` | `configuracoes_teste_adaptativo` | `3` |
| Motor | `erros_descer` | `2` | `configuracoes_teste_adaptativo` | `2` |
| Motor | `janela_consistencia` | `3` | `configuracoes_teste_adaptativo` | `3` |
| Motor | `janela_recuperacao` | `2` | `configuracoes_teste_adaptativo` | `2` |
| Motor | `peso_consistencia_acerto` | `0.70` | `configuracoes_teste_adaptativo` | `0.70` |
| Motor | `peso_consistencia_ritmo` | `0.30` | `configuracoes_teste_adaptativo` | `0.30` |
| Motor | `peso_resiliencia_recuperacao` | `0.70` | `configuracoes_teste_adaptativo` | `0.70` |
| Motor | `peso_resiliencia_estabilidade` | `0.30` | `configuracoes_teste_adaptativo` | `0.30` |

---

## 3. Decisoes que ainda precisas validar

- [ ] Confirmar se os limites por nivel fazem sentido pedagogicamente
- [ ] Confirmar se `tempo_sugerido_segundos` por nivel deve continuar unico para todas as disciplinas
- [ ] Decidir se vai existir override por disciplina no futuro
- [ ] Decidir se `LONGO = 10` continua suficiente ou se precisas de mais uma faixa
- [ ] Confirmar se queres um unico perfil ativo (`PADRAO_V1`) ou multiplos perfis

---

## 4. Ordem recomendada para fechar isto

1. Validar os valores seed no banco
- [ ] Abrir a tabela `configuracoes_teste_adaptativo`
- [ ] Abrir a tabela `configuracoes_teste_adaptativo_niveis`
- [ ] Abrir a tabela `configuracoes_teste_adaptativo_duracoes`

2. Criar leitura da configuracao ativa
- [ ] Criar repository para buscar o perfil ativo
- [ ] Criar metodo para buscar configuracao por nivel
- [ ] Criar metodo para buscar configuracao por duracao

3. Remover hardcodes do codigo
- [ ] Trocar `mapearTempoSugerido(...)` para ler do banco
- [ ] Trocar `resolverRigorBase(...)` para ler do banco
- [ ] Trocar `resolverLimiteQuestoes(...)` para ler do banco
- [ ] Trocar thresholds de subida/descida para ler do banco
- [ ] Trocar fatores usados em resiliencia e consistencia para ler do banco

4. Persistir o snapshot real do teste
- [ ] Salvar `configuracao_teste_adaptativo_id` em `testes`
- [ ] Salvar `limite_inferior` real usado no teste
- [ ] Salvar `limite_superior` real usado no teste
- [ ] Salvar `tempo_sugerido_segundos` real em `teste_perguntas`

5. Validar o comportamento
- [ ] Confirmar que trocar valores no banco muda o motor sem recompilar
- [ ] Confirmar que um teste novo usa a configuracao ativa
- [ ] Confirmar que um teste antigo continua auditavel pelo snapshot salvo

---

## 5. O que nao deve virar dado voador

- [ ] Nao deixar `tempo_sugerido` hardcoded em service
- [ ] Nao deixar `rigor_base` hardcoded em service
- [ ] Nao deixar `limite_inferior` e `limite_superior` como `0` e `1` por defeito sem contexto
- [ ] Nao espalhar pesos e thresholds por varios ficheiros
- [ ] Nao depender de variaveis globais mutaveis para regras do motor

---

## 6. Estrutura mental para amanha

- se for regra do motor, vai para `configuracoes_teste_adaptativo`
- se variar por nivel, vai para `configuracoes_teste_adaptativo_niveis`
- se variar por duracao do teste, vai para `configuracoes_teste_adaptativo_duracoes`
- se variar por aluno, continua em progresso/rigor
- se for o valor realmente usado num teste, salva no historico do teste
