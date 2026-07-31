No início do segundo semestre de Programação 2, estudámos Java — uma linguagem com a qual já estava familiarizado. Mas, por ser amante de desafios, decidi complicar o projeto final do semestre e transformá-lo em algo mais ambicioso.

Foi assim que nasceu o **KBols**: uma app desktop para ajudar candidatos a prepararem-se para os exames de admissão à universidade. O utilizador faz um diagnóstico inicial, recebe um plano de estudo semanal e acompanha a sua evolução através de testes adaptativos, leitura guiada de PDFs, mini-testes, desafios e até simulações de bolsas de estudo. Conteúdo delimitado a Física e Matemática.

Por baixo do capô, o que mais me entusiasma:

- **Motor de testes adaptativo** — quatro níveis de dificuldade (Fácil a Expert). O nível sobe após 2 acertos consecutivos e desce após 2 erros, ajustando-se em tempo real ao desempenho do aluno.
- **Cinco métricas de avaliação** — precisão, velocidade, consistência, resiliência e lógica, todas calculadas a partir de cada resposta e peso das alternativas.
- **Plano de estudo semanal inteligente** — prioriza disciplinas com base em desempenho, tempo sem estudar e tendência de evolução, com snapshot semanal guardado.
- **Gerador de questões por IA** — o Gemini analisa os PDFs do próprio aluno, extrai os tópicos e gera testes com alternativas ponderadas, equações LaTeX e referências às páginas do livro.
- **Leitor de PDF integrado** — renderização página a página, mapa de tópicos e progresso de leitura por livro.
- **Gamificação** — 20 medalhas (5 competências × 4 níveis) para manter a motivação.
- **Simulados de bolsas** — com critérios reais de elegibilidade, como mínimo de medalhas e desempenho.

Infelizmente, o projeto esbarrou em limitações: a leitura de PDFs não ficou rápida e ágil o suficiente, o pacote gratuito do Gemini não gerava perguntas com a velocidade que eu precisava, e a UI do JavaFX trouxe restrições que dificultaram construir exatamente o sistema que me propus a resolver.

Decidi, então, pausar o desenvolvimento e continuar com uma tecnologia mais atual e capaz de se adaptar aos utilizadores. O que construí até aqui vai servir de mapa para o próximo projeto: pretendo aproveitar a arquitetura — mas desta vez montada de raiz, segundo os princípios de clean architecture que estudei — para criar o sistema novamente, com uma solução mais ágil.

Vou também partilhar a interface Java que consegui criar, mesmo com as limitações que encontrei.
