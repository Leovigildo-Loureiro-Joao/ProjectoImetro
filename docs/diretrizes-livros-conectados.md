# Directrizes: Livros Conectados ao Sistema

## 1. Objectivo

Integrar os livros (PDFs carregados na biblioteca) no ecossistema de estudo do Imetro de forma bidirecional:
- O plano de estudo, recomendações e trilhos devem referenciar diretamente os livros e páginas específicas.
- Após a leitura de um livro/trecho, o aluno pode fazer um mini teste contextual.
- Ao passar o mini teste, o sistema recomenda um teste adaptativo de confirmação para validar a precisão do conhecimento.

---

## 2. Estado Actual (Já Existe)

| Componente | Status | Detalhes |
|---|---|---|
| **Upload de livros** | ✅ Implementado | `BibliotecaLivroService.sincronizarArquivo()` extrai texto por página, gera checksum SHA-256 |
| **Páginas extraídas** | ✅ Implementado | `biblioteca_livro_paginas` com `pagina_numero` e `texto_pagina` |
| **Livro → Disciplina** | ✅ Implementado | FK `disciplina_id` em `biblioteca_livros` |
| **Questões → Livro/Páginas** | ✅ Implementado | `perguntas.referencia_livro`, `perguntas.pagina_inicio`, `perguntas.pagina_fim` |
| **Recomendação de livro/páginas** | ✅ Implementado | `progressao_rigor.recomendacao_livro` e `recomendacao_paginas` |
| **Teste adaptativo** | ✅ Implementado | `TesteAdaptativoService` com seleção por rigor, tópico, subtópico |
| **Plano de estudo** | ✅ Implementado | `PlaneamentoEstudoService` gera etapas, focos e insights |
| **Progresso do aluno** | ✅ Implementado | `progresso_aluno_disciplina` e `progressao_rigor` |

---

## 3. O Que Falta Implementar

### 3.1. Trilho de Leitura (Reading Trail)

**Problema:** O plano de estudo sugere "estudar tópico X" mas não liga diretamente às páginas do livro que o aluno deve ler.

**Solução:** Adicionar ao `PlaneamentoEstudoEtapa` (ou criar `TrilhoLeitura`) uma lista de sessões de leitura com:

```
TrilhoLeitura:
  - disciplinaId: UUID
  - livroId: UUID (referência a biblioteca_livros)
  - sessoes: [
      {
        ordem: 1,
        livroId: UUID,
        tituloLivro: String,
        paginasInicio: int,
        paginasFim: int,
        topico: String,
        subtopico: String,
        estado: "PENDENTE" | "A_LER" | "LIDO",
        dataConclusao: LocalDateTime?
      }
    ]
```

**Onde integrar:**
- `PlaneamentoEstudoService.gerarResumo()` — ao gerar o plano, cruzar os subtópicos com fraca performance com os livros disponíveis na disciplina e criar sessões de leitura.
- Nova tabela `trilho_leitura` (Flyway migration V34+) para persistir o progresso de leitura.

### 3.2. Marcação de Páginas Lidas / Progresso de Leitura

**Problema:** Não há tracking de quantas páginas o aluno já leu nem se concluiu a leitura recomendada.

**Solução:**

Nova tabela `leitura_progresso`:

```sql
CREATE TABLE leitura_progresso (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    aluno_id UUID NOT NULL REFERENCES candidatos(id),
    livro_id UUID NOT NULL REFERENCES biblioteca_livros(id),
    pagina_atual INT NOT NULL DEFAULT 0,
    total_paginas INT NOT NULL,
    paginas_lidas INT[] DEFAULT '{}',  -- array de números de páginas concluídas
    estado VARCHAR(20) NOT NULL DEFAULT 'NAO_INICIADO',  -- NAO_INICIADO, EM_LEITURA, CONCLUIDO
    sessoes_leitura JSONB DEFAULT '[]',  -- histórico de sessões (timestamp, paginas_lidas_na_sessao)
    criado_em TIMESTAMP DEFAULT now(),
    atualizado_em TIMESTAMP DEFAULT now(),
    UNIQUE(aluno_id, livro_id)
);
```

**Serviço:** `LeituraProgressoService` com métodos:
- `iniciarLeitura(UUID alunoId, UUID livroId)` — cria/retoma registo
- `marcarPaginasLidas(UUID alunoId, UUID livroId, int[] paginas)` — marca páginas como lidas
- `obterProgresso(UUID alunoId, UUID livroId)` — devolve `LeituraProgressoDto`
- `concluirLeitura(UUID alunoId, UUID livroId)` — muda estado para CONCLUIDO

**UI:**
- No visor do livro (PDF viewer), barra de progresso com páginas lidas/total.
- Botão "Marcar como lida" ao lado de cada página.
- Indicador visual de quais páginas já foram lidas.

### 3.3. Mini Teste Contextual (Pós-Leitura)

**Problema:** O aluno lê mas não valida se aprendeu.

**Solução:**

**3.3.1. Gerar Mini Teste a partir das páginas lidas**

Novo método em `GeminiService` ou `PerguntasBootstrapService`:

```java
public List<Questao> gerarMiniTeste(
    UUID livroId,
    int paginaInicio,
    int paginaFim,
    int quantidadeQuestoes  // 3 a 5 questões
)
```

- Usa o texto extraído das páginas (`biblioteca_livro_paginas.texto_pagina`) como contexto.
- Envia ao Gemini com prompt específico para gerar N questões objetivas (múltipla escolha) sobre aquele trecho.
- As questões geradas têm `referencia_livro`, `pagina_inicio` e `pagina_fim` preenchidos.
- **Cache:** Se já existirem mini testes para aquele intervalo de páginas, reutilizar em vez de chamar o Gemini novamente.

**3.3.2. Armazenar Mini Testes**

Nova tabela `mini_testes`:

```sql
CREATE TABLE mini_testes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    livro_id UUID NOT NULL REFERENCES biblioteca_livros(id),
    pagina_inicio INT NOT NULL,
    pagina_fim INT NOT NULL,
    questoes JSONB NOT NULL,  -- array de {enunciado, opcoes, resposta_correta, explicacao}
    checksum_conteudo VARCHAR(64),  -- SHA-256 do texto das páginas (para saber se o conteúdo mudou)
    criado_em TIMESTAMP DEFAULT now()
);
```

**3.3.3. Botão "Fazer Mini Teste" na UI**

- Aparece quando o aluno conclui a leitura de um intervalo de páginas (ou de um capítulo).
- Leva a uma tela de mini teste com 3-5 questões.
- Após responder, mostra resultado imediato (quantas acertou, quais errou, explicação).

**3.3.4. Critério de Aprovação**

- Aprovação: ≥ 70% de acerto no mini teste.
- Se falhar: recomendar revisão das páginas específicas onde errou e repetir o mini teste.
- Se passar: habilitar o passo seguinte (teste adaptativo de confirmação).

### 3.4. Teste Adaptativo de Confirmação

**Problema:** O mini teste é curto e pode não refletir a real retenção do conhecimento.

**Solução:**

**3.4.1. Disparo automático após aprovação no mini teste**

No `MiniTesteService` ou no `TesteService`, método:

```java
public UUID recomendarTesteConfirmacao(
    UUID alunoId,
    UUID disciplinaId,
    UUID livroId,
    int paginaInicio,
    int paginaFim,
    String topico,
    String subtopico
)
```

- Cria uma `ConfiguracaoTesteAdaptativo` específica para confirmação:
  - `limite_questoes`: 10
  - `nivel_inicial`: o rigor atual do aluno para aquele subtópico (`progressao_rigor.rigor_atual`)
  - `topicos/subtopicos`: focados no trecho lido
- Regista na tabela `testes` com `configuracao_teste_adaptativo_id` apontando para a config de confirmação.
- O sistema notifica o aluno: "Passaste no mini teste! Queres confirmar o teu conhecimento com um teste adaptativo?"

**3.4.2. Registo de origem**

Adicionar campo `origem` na tabela `testes` com valor `CONFIRMACAO_LEITURA` para distinguir de testes normais ou diagnósticos.

**3.4.3. Pós-teste de confirmação**

- Se o aluno for aprovado (≥ 60% no teste adaptativo de confirmação):
  - Actualizar `progressao_rigor.rigor_atual` para o nível seguinte.
  - Actualizar `progressao_rigor.precisa_revisao = false`.
  - Marcar o trecho do livro como "dominado" em `trilho_leitura`.
  - Gerar insight positivo no `PlaneamentoEstudoService`.
- Se falhar:
  - Identificar subtópicos específicos com menor precisão.
  - Recomendar releitura das páginas específicas desses subtópicos.
  - Não avançar no rigor.

---

## 4. Fluxo Completo (User Journey)

```
Plano de Estudo
  │
  ├── Recomenda leitura: Livro X, páginas 10-30 (tópico: "Funções")
  │     │
  │     ▼
  │  Aluno abre o PDF na página 10
  │     │
  │     ▼
  │  LeituraProgressoService.marcarPaginasLidas()
  │     │
  │     ▼
  │  Aluno chega à página 30
  │     │
  │     ▼
  │  Sistema marca páginas 10-30 como lidas
  │     │
  │     ▼
  │  [Botão: Fazer Mini Teste] ← aparece na UI
  │     │
  │     ▼
  │  Mini Teste (3-5 questões sobre páginas 10-30)
  │     │
  │     ├── Falhou (< 70%)
  │     │     └── Recomendar revisão + repetir mini teste
  │     │
  │     └── Passou (≥ 70%)
  │           │
  │           ▼
  │     Teste Adaptativo de Confirmação (10 questões)
  │           │
  │           ├── Falhou (< 60%)
  │           │     └── Identificar subtópicos fracos → recomendar releitura
  │           │
  │           └── Passou (≥ 60%)
  │                 └── Avançar rigor + marcar como dominado
  │
  ▼
Próximo tópico / subtópico no plano de estudo
```

---

## 5. Alterações na Base de Dados (Flyway Migrations)

### V34: `trilho_leitura`
### V35: `leitura_progresso`
### V36: `mini_testes`
### V37: Adicionar `origem` em `testes`

---

## 6. Novos Serviços

| Serviço | Responsabilidade |
|---|---|
| `LeituraProgressoService` | Tracking de páginas lidas por aluno/livro |
| `MiniTesteService` | Geração, armazenamento e correção de mini testes |
| `TrilhoLeituraService` | Geração e gestão do trilho de leitura no plano de estudo |

---

## 7. Alterações em Serviços Existentes

| Serviço | Alteração |
|---|---|
| `PlaneamentoEstudoService.gerarResumo()` | Incluir sessões de leitura no plano, cruzando subtópicos fracos com livros disponíveis |
| `TesteAdaptativoService` | Aceitar parâmetro `origem` (CONFIRMACAO_LEITURA) para configurar teste de confirmação |
| `TesteService.registrarTesteConcluido()` | Após teste de confirmação bem-sucedido, actualizar `progressao_rigor` e `trilho_leitura` |
| `ProgressoAlunoDisciplinaRepository` | Adicionar suporte para actualização a partir de confirmação de leitura |
| `BibliotecaLivroService` | Novo método `buscarTextoPaginas(UUID livroId, int inicio, int fim)` para alimentar mini testes |

---

## 8. Considerações Técnicas

1. **Gemini para mini testes**: Reutilizar `GeminiService.gerarJsonEstruturado()` com um schema JSON específico para mini testes. O prompt deve incluir o texto extraído das páginas como contexto.

2. **Cache de mini testes**: Calcular SHA-256 do texto das páginas para evitar regenerar mini testes idênticos.

3. **Concorrência**: O `LeituraProgressoService` deve usar `SELECT ... FOR UPDATE` ou optimist locking para evitar escrita concorrente nas páginas lidas (raro, mas possível se o aluno tiver múltiplos dispositivos).

4. **PDF Viewer**: O sistema actual não tem um PDF viewer integrado. Será necessário:
   - Opção A: Usar um componente JavaFX de terceiros (ex: `PDFRenderer` do Apache PDFBox).
   - Opção B: Criar uma view que mostre o texto extraído página a página com navegação.
   - Opção C: Abrir o PDF externamente e sincronizar o progresso manualmente.

5. **Notificações**: Usar o sistema de notificações existente (se houver) ou criar um simples callback no `MiniTesteService` para disparar a recomendação do teste adaptativo.

---

## 9. Métricas de Sucesso

- % de alunos que completam a leitura recomendada no plano de estudo
- % de alunos que fazem o mini teste após a leitura
- % de aprovação no mini teste (1ª tentativa)
- % que avança para o teste adaptativo de confirmação
- Correlação entre leitura + mini teste + teste adaptativo e a melhoria no `progressao_rigor`
- Redução no número de dias sem estudo (`dias_sem_estudo` em `progresso_aluno_disciplina`)
