# Prompt para gerar mock robusto de perguntas

Este ficheiro foi pensado para colar noutra IA e gerar um mock que encaixa no backend do projeto sem fugir ao schema da app.

## Como usar

- Escolhe uma disciplina por execucao.
- Para este projeto, as disciplinas validas e mais seguras sao `Matemática` e `Física`.
- Se quiseres dois mocks, executa o prompt duas vezes: uma vez para cada disciplina.
- Usa `24` perguntas para apresentacao rapida ou `48` perguntas para uma base mais completa.
- Se o destino for o backend, pede a saida em JSON.
- Se o destino for um seed direto, usa a variante SQL.

## Prompt principal

```text
Es tu um gerador de base de perguntas para uma aplicacao JavaFX de estudo.

Objetivo:
- gerar um mock robusto, visualmente variado e consistente com o que a aplicacao espera;
- nao criar conteudo genérico, repetitivo ou desalinhado com o schema;
- privilegiar perguntas que parecam reais e boas para demonstracao/presentacao;
- manter a saida limpa, valida e pronta para importacao.

Parametros do lote:
- Disciplina alvo: {{DISCIPLINA}}
- Quantidade de questoes: {{QUANTIDADE}}
- Modo: apresentacao / mock robusto
- Idioma: pt-AO

Regras obrigatorias:
- Usa apenas uma disciplina por saida.
- A disciplina tem de ser exatamente `Matemática` ou `Física`.
- Gera questoes coerentes com o nivel de um candidato real.
- Usa apenas topicos e subtopicos que facam sentido para a disciplina escolhida.
- Nao inventes campos fora do contrato abaixo.
- Nao uses markdown na resposta final.
- Nao envolvas a resposta em explicacoes, introducoes ou observacoes.
- Nao repitas enunciados, alternativas nem estruturas visuais.
- Nao deixes todas as perguntas com o mesmo estilo.
- Nao deixes os graficos todos a passar pela origem.
- Nao uses o mesmo padrao visual em cadeia, como `a=1`, `b=0`, `c=0`, `xMin=0`, `xMax=4` em todas as perguntas.
- Mantem os valores dos graficos variados, com interceptos, concavidades e janelas diferentes.
- Quando a questao nao precisar de grafico, preenche o objeto grafico na mesma, mas com `usar=false` e `tipoCurva="NENHUM"`.
- Quando a questao precisar de grafico, preenche `usar=true` e faz o grafico bater exatamente com o enunciado.
- Em questoes com grafico, varia a janela do eixo X, o ponto de corte no eixo Y e a inclinacao/concavidade.
- Pelo menos metade das questoes com grafico nao pode passar pela origem.
- Usa intervalos de eixo diferentes: `[-6, 6]`, `[-4, 8]`, `[1, 12]`, `[-3, 7]`, etc.
- Usa `xTickUnit` diferente de 1 em parte das questoes, quando fizer sentido.
- Em `Física`, privilegia cinemática simples, leitura de grafico, movimento uniforme, movimento acelerado e relacoes proporcionais.
- Em `Matemática`, privilegia funcao afim, funcao quadratica, porcentagem, proporcionalidade, equacoes, geometria, estatistica e raciocinio algébrico.
- Garante distribuicao equilibrada entre `FACIL`, `MEDIO`, `DESAFIANTE` e `EXTRA`.
- Garante que o `rigor` varie de forma coerente com a dificuldade.
- As alternativas devem ser objetivas, plausiveis e com distratores fortes.
- As alternativas devem ter exatamente 4 itens.
- As alternativas nao devem vir com letras tipo `A)` ou `B)`; usa texto puro.
- `respostaCorreta` tem de ser exatamente igual ao texto de uma das alternativas.
- `pesosAlternativas` deve ter exatamente 4 numeros.
- O peso da alternativa correta tem de ser `1.0`.
- Os pesos das erradas devem ser menores que `1.0` e diferentes entre si sempre que possivel.
- O `topicoPrincipal` deve resumir o eixo principal do conhecimento cobrado.
- O `topico` deve ser mais especifico.
- O `subtopico` deve ser ainda mais preciso quando fizer sentido.
- `referenciaLivro`, `paginaInicio` e `paginaFim` devem existir.
- `paginaFim` deve ser maior ou igual a `paginaInicio`.
- Se estiveres a gerar para o backend do projeto, `referenciaLivro` deve coincidir com o nome de um PDF real da disciplina em `uploads/disciplinas/<uuid>/`.
- Se estiveres a gerar um seed SQL direto, podes usar um titulo coerente de livro, mas mantem paginas realistas.
- `exercicio` deve ser curto, claro e renderizavel.
- Se nao houver exercicio visual, usa string vazia.
- A explicacao deve ser curta, objetiva e util para feedback.

Contrato de saida:
- Responde com um JSON valido e nada mais.
- O JSON tem de seguir esta estrutura:

{
  "titulo": "Mock de apresentacao",
  "disciplina": "Matemática",
  "idioma": "pt-AO",
  "fonteResumo": "Resumo curto do que o lote cobre.",
  "questoes": [
    {
      "numero": 1,
      "enunciado": "texto da questao",
      "topicoPrincipal": "Funcao afim",
      "topico": "Equacoes de reta",
      "subtopico": "Corte no eixo y",
      "dificuldade": "MEDIO",
      "rigor": 0.62,
      "referenciaLivro": "nome-do-pdf-ou-livro",
      "paginaInicio": 12,
      "paginaFim": 14,
      "exercicio": "expressao curta ou vazio",
      "alternativas": [
        "texto da alternativa 1",
        "texto da alternativa 2",
        "texto da alternativa 3",
        "texto da alternativa 4"
      ],
      "pesosAlternativas": [0.2, 1.0, 0.1, 0.4],
      "grafico": {
        "usar": true,
        "tipoCurva": "RETA",
        "a": 2,
        "b": -3,
        "c": 0,
        "eixoX": "tempo (s)",
        "eixoY": "posicao (m)",
        "xMin": -4,
        "xMax": 8,
        "xTickUnit": 2
      },
      "respostaCorreta": "texto da alternativa 2",
      "explicacao": "explicacao curta da resposta"
    }
  ]
}

Regras especificas do grafico:
- Se `tipoCurva` for `RETA`, usa `y = a*x + b`.
- Se `tipoCurva` for `PARABOLA`, usa `y = a*x^2 + b*x + c`.
- Nunca deixes todos os graficos com intercepto zero.
- Em retas, varia `a` entre positivo e negativo.
- Em parabolas, varia concavidade, vertice e interceptos.
- Em fisica, usa eixos com significado fisico claro.
- Em matematica, usa nomes de eixos mais genericos, como `eixo x` e `eixo y`, quando fizer sentido.
- Quando `usar=false`, `tipoCurva` deve ser `NENHUM`.
- Mesmo com `usar=false`, preenche os restantes campos do objeto grafico com valores neutros e consistentes.

Qualidade minima:
- Nenhuma alternativa deve ser absurda.
- Nenhuma questao deve depender de informacao fora do enunciado.
- Nenhuma resposta correta pode ser ambigua.
- Nenhum grafico pode contradizer o enunciado.
- Nenhum valor numerico pode forcar a curva a passar sempre pela origem.
- Nenhuma pagina pode ser invertida (`paginaFim < paginaInicio`).
- Nenhuma dificuldade pode ficar desajustada ao conteudo.

Antes de responder:
- verifica que a quantidade total de questoes e exatamente {{QUANTIDADE}};
- verifica que cada questao tem 4 alternativas e 4 pesos;
- verifica que `respostaCorreta` coincide com uma das alternativas;
- verifica que o objeto grafico tem a forma certa;
- verifica que o JSON e valido;
- responde apenas com o JSON final.
```

## Variante SQL direta

Se preferires gerar seed direto para a tabela `perguntas`, usa esta variante. Ela e util quando nao queres passar pelo importador JSON do backend.

```text
Gera um script SQL puro, pronto para correr na base de dados.

Objetivo:
- criar um mock robusto para apresentacao;
- manter compatibilidade com a tabela `perguntas`;
- variar bastante os graficos para nao ficarem todos na origem;
- evitar repeticao de padroes e perguntas muito parecidas.

Parametros do lote:
- Disciplina alvo: {{DISCIPLINA}}
- Quantidade de questoes: {{QUANTIDADE}}
- Modo: seed SQL
- Idioma do conteudo: pt-AO

Regras de saida:
- Responde apenas com SQL.
- Usa `insert into perguntas (...) values (...);`.
- Usa `uuid_generate_v4()` para o id.
- Preenche as colunas:
  `disciplina`, `topico_principal`, `topico`, `subtopico`, `questao`, `respostas`, `pesos_resposta`,
  `resposta_correta`, `dificuldade`, `rigor`, `referencia_livro`, `pagina_inicio`, `pagina_fim`,
  `exercicio`, `usa_grafico`, `grafico_tipo_curva`, `grafico_a`, `grafico_b`, `grafico_c`,
  `grafico_eixo_x`, `grafico_eixo_y`, `grafico_x_min`, `grafico_x_max`, `grafico_x_tick_unit`.
- Mantem `respostas` como JSONB com 4 strings.
- Mantem `pesos_resposta` como JSONB com 4 numeros.
- Mantem `resposta_correta` como texto puro da alternativa correta.
- Usa dificuldades coerentes: `FACIL`, `MEDIO`, `DESAFIANTE`, `EXTRA`.
- Usa `rigor` entre `0.0` e `1.0`.
- Usa paginas realistas e crescentes.
- Nao deixes o grafico repetir sempre os mesmos parametros.
- Se `usa_grafico` for `false`, preenche os campos do grafico com valores neutros, mas coerentes.
- Se `usa_grafico` for `true`, garante que o grafico e realmente util para resolver a questao.
- Evita a combinacao repetida `a=1`, `b=0`, `c=0`, `xMin=0`, `xMax=4`, `xTickUnit=1`.
- Usa pelo menos alguns graficos com `b != 0`, `c != 0`, `xMin < 0` e `xMax > 0`.
- Usa pelo menos alguns graficos com `xMin` e `xMax` assimetricos.
- Faz com que pelo menos metade dos graficos nao passem pela origem.
- Em `Física`, privilegia graficos de movimento e relacoes proporcionais.
- Em `Matemática`, privilegia retas, parabolas e funcao afim/quadratica.
- Nao incluas explicacoes fora do SQL.
- Nao uses markdown.
- Nao uses blocos de codigo.
- Nao acrescentes texto extra antes ou depois dos `insert`.
```

## Observacoes de compatibilidade

- A app completa as alternativas E/F/G internamente, por isso o mock precisa de 4 alternativas muito boas, nao 7.
- O backend ja sabe ler o objeto `grafico`, entao vale a pena preencher esse campo com cuidado.
- Se o objetivo for demo, `Matemática` e a melhor disciplina para mostrar diversidade visual.
- Se quiseres mostrar o lado mais academico da UI, executa o prompt tambem para `Física`.
- Se fores usar o importador do backend, confirma que o JSON final respeita o schema exato.
- Se fores usar SQL direto, confirma que os nomes de colunas batem com a tabela atual.
