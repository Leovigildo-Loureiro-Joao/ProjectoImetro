# SimulatorBolsaStudy

Simulador de exames de bolsas (MVP) para apoiar a preparação de estudantes com **análise adaptativa** e **feedback orientado a dados** para Estudante e Orientador.

> Nota (estado atual): este repositório contém um esqueleto JavaFX (telas `primary/secondary`) para arrancar a interface. As funcionalidades de simulador, banco de questões e análise ainda estão em desenvolvimento.

## Objetivo

- Ajudar o **Estudante** a treinar para testes/exames de bolsas (genérico) em áreas como: Matemática, Português, Física, Química, Inglês e Cultura Geral.
- Dar ao **Orientador** visibilidade do progresso e dos padrões de erro, para orientar o estudante com base em evidências.

## Perfis no sistema

- **Estudante**: realiza simulados/exames, responde questões e acompanha o próprio desempenho ao longo do tempo.
- **Orientador**: consulta relatórios e valida recomendações sugeridas pelo sistema (human-in-the-loop), tornando a intervenção mais certeira.

## Métricas mínimas (MVP)

- **Tempo médio** por questão e por sessão
- **Taxa de acerto por tópico**
- **Evolução semanal** (tendência de acerto/tempo)
- **Erros recorrentes** (padrões por tópico e tipo de questão)
- **Dificuldade atingida** (progressão e estabilização)

## Como funciona (visão de produto)

O sistema recolhe dados de desempenho (ex.: acerto/erro, tempo, tópicos) e usa esses sinais para:

- Ajustar a seleção de questões (dificuldade/tópicos) de forma **adaptativa**
- Gerar recomendações de estudo e exercícios
- Produzir relatórios para Estudante e Orientador, destacando pontos fortes e fracos

## Armazenamento de dados

- **Inicialmente (MVP):** multiutilizador no **mesmo PC** (perfis locais).
- **Evolução possível:** sincronização entre dois PCs usando **Supabase** (armazenamento/autenticação), mantendo a análise e a experiência focadas no simulador.

## Tecnologias

- Java 11
- JavaFX
- Maven

## Executar o projeto

### Pré-requisitos

- JDK 11 instalado
- Maven instalado

### Comando

```bash
mvn clean javafx:run
```

## Estrutura do projeto

- `src/main/java/com/imetro/App.java`: entrypoint JavaFX
- `src/main/java/com/imetro/PrimaryController.java`: controller da view primária (exemplo)
- `src/main/java/com/imetro/SecondaryController.java`: controller da view secundária (exemplo)
- `src/main/resources/com/imetro/primary.fxml`: layout da view primária (exemplo)
- `src/main/resources/com/imetro/secondary.fxml`: layout da view secundária (exemplo)

## Roadmap (sugestão)

1. **Modelo de domínio**
   - Perfis (Estudante/Orientador), Sessão, Questão, Resposta, Tópico, Métricas
2. **Banco de questões**
   - Importação/CRUD, tags por tópico, nível de dificuldade, explicações
3. **Simulador**
   - Configuração de prova, temporizador, submissão, correção e revisão
4. **Análise**
   - Cálculo de métricas, detecção de erros recorrentes, evolução semanal
5. **Recomendações**
   - Sugestões automáticas + validação do orientador
6. **Persistência**
   - Local (ex.: SQLite) e opcional sync com Supabase

## Contribuição

Sugestões e PRs são bem-vindos. Para contribuir:

- Cria uma branch com a tua mudança
- Mantém o escopo pequeno e bem descrito
- Explica o motivo (problema) e a solução (como foi resolvido)

## Licença

Ainda não foi definida uma licença para o projeto.

