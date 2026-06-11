package com.imetro.domain.enums;

/**
 * Enum de tópicos disponíveis para testes de admissão.
 *
 * Cada constante carrega:
 *  - disciplina   → MATEMATICA ou FISICA
 *  - area         → agrupamento temático (ex: "Álgebra", "Mecânica")
 *  - label        → nome amigável para exibição na UI
 *  - nivel        → BASICO | INTERMEDIARIO | AVANCADO
 *  - frequencia   → quão comum é no exame: MUITO_COMUM | COMUM | RARO
 *
 * Baseado nos livros:
 * - Fundamentos de Matemática Elementar (Vol. 1 e 2) - Iezzi & Murakami
 * - Fundamentos de Física (Vol. 1) - Halliday, Resnick & Walker
 */
public enum TopicoExame {

    // =========================================================
    //  MATEMÁTICA — Volume 1: Conjuntos e Funções
    // =========================================================
    LOGICA_PROPOSICOES(
            Disciplina.MATEMATICA, "Lógica",
            "Proposições e conectivos", Nivel.BASICO, Frequencia.MUITO_COMUM),

    LOGICA_CONDICIONAIS(
            Disciplina.MATEMATICA, "Lógica",
            "Condicionais e bicondicionais", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    LOGICA_TAUTOLOGIAS(
            Disciplina.MATEMATICA, "Lógica",
            "Tautologias e contradições", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    LOGICA_QUANTIFICADORES(
            Disciplina.MATEMATICA, "Lógica",
            "Quantificadores e negação", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    CONJUNTOS_OPERACOES(
            Disciplina.MATEMATICA, "Conjuntos",
            "União, interseção e diferença", Nivel.BASICO, Frequencia.MUITO_COMUM),

    CONJUNTOS_SUBCONJUNTOS(
            Disciplina.MATEMATICA, "Conjuntos",
            "Subconjuntos e inclusão", Nivel.BASICO, Frequencia.MUITO_COMUM),

    CONJUNTOS_COMPLEMENTAR(
            Disciplina.MATEMATICA, "Conjuntos",
            "Complementar e diferença simétrica", Nivel.BASICO, Frequencia.MUITO_COMUM),

    CONJUNTOS_PARTES(
            Disciplina.MATEMATICA, "Conjuntos",
            "Conjunto das partes", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    CONJUNTOS_NUMERICOS(
            Disciplina.MATEMATICA, "Conjuntos Numéricos",
            "Conjuntos N, Z, Q, R", Nivel.BASICO, Frequencia.MUITO_COMUM),

    DIVISIBILIDADE_MDC_MMC(
            Disciplina.MATEMATICA, "Conjuntos Numéricos",
            "Divisibilidade, MDC e MMC", Nivel.BASICO, Frequencia.MUITO_COMUM),

    NUMEROS_PRIMOS(
            Disciplina.MATEMATICA, "Conjuntos Numéricos",
            "Números primos e compostos", Nivel.BASICO, Frequencia.MUITO_COMUM),

    FRACOES_DECIMAIS_DIZIMAS(
            Disciplina.MATEMATICA, "Conjuntos Numéricos",
            "Frações, decimais e dízimas", Nivel.BASICO, Frequencia.MUITO_COMUM),

    NUMEROS_IRRACIONAIS(
            Disciplina.MATEMATICA, "Conjuntos Numéricos",
            "Números irracionais", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    INTERVALOS_REAIS(
            Disciplina.MATEMATICA, "Conjuntos Numéricos",
            "Intervalos reais", Nivel.BASICO, Frequencia.MUITO_COMUM),

    INDUCAO_FINITA(
            Disciplina.MATEMATICA, "Conjuntos Numéricos",
            "Princípio da indução finita", Nivel.AVANCADO, Frequencia.RARO),

    RELACOES_PRODUTO_CARTESIANO(
            Disciplina.MATEMATICA, "Relações",
            "Produto cartesiano", Nivel.BASICO, Frequencia.MUITO_COMUM),

    RELACOES_BINARIAS(
            Disciplina.MATEMATICA, "Relações",
            "Relação binária e inversa", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    FUNCOES_DEFINICAO(
            Disciplina.MATEMATICA, "Funções",
            "Definição de função", Nivel.BASICO, Frequencia.MUITO_COMUM),

    FUNCOES_DOMINIO_IMAGEM(
            Disciplina.MATEMATICA, "Funções",
            "Domínio e imagem", Nivel.BASICO, Frequencia.MUITO_COMUM),

    FUNCAO_AFIM(
            Disciplina.MATEMATICA, "Funções",
            "Função afim (1º grau)", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    FUNCAO_LINEAR_CONSTANTE(
            Disciplina.MATEMATICA, "Funções",
            "Função linear e constante", Nivel.BASICO, Frequencia.MUITO_COMUM),

    ZERO_DA_FUNCAO_AFIM(
            Disciplina.MATEMATICA, "Funções",
            "Zero da função afim", Nivel.BASICO, Frequencia.MUITO_COMUM),

    SINAL_DA_FUNCAO_AFIM(
            Disciplina.MATEMATICA, "Funções",
            "Sinal da função afim", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    INEQUACOES_1_GRAU(
            Disciplina.MATEMATICA, "Funções",
            "Inequações do 1º grau", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    FUNCAO_QUADRATICA(
            Disciplina.MATEMATICA, "Funções",
            "Função quadrática (2º grau)", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    CONCAVIDADE_PARABOLA(
            Disciplina.MATEMATICA, "Funções",
            "Concavidade da parábola", Nivel.BASICO, Frequencia.MUITO_COMUM),

    ZEROS_FUNCAO_QUADRATICA(
            Disciplina.MATEMATICA, "Funções",
            "Zeros da função quadrática", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    VERTICE_PARABOLA(
            Disciplina.MATEMATICA, "Funções",
            "Vértice da parábola", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    VALOR_MAXIMO_MINIMO(
            Disciplina.MATEMATICA, "Funções",
            "Máximo e mínimo da quadrática", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    SINAL_FUNCAO_QUADRATICA(
            Disciplina.MATEMATICA, "Funções",
            "Sinal da função quadrática", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    INEQUACOES_2_GRAU(
            Disciplina.MATEMATICA, "Funções",
            "Inequações do 2º grau", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    FUNCAO_MODULAR(
            Disciplina.MATEMATICA, "Funções",
            "Função modular", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    EQUACOES_MODULARES(
            Disciplina.MATEMATICA, "Funções",
            "Equações modulares", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    INEQUACOES_MODULARES(
            Disciplina.MATEMATICA, "Funções",
            "Inequações modulares", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    FUNCAO_RECIPROCA(
            Disciplina.MATEMATICA, "Funções",
            "Função recíproca (1/x)", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    FUNCAO_MAXIMO_INTEIRO(
            Disciplina.MATEMATICA, "Funções",
            "Função máximo inteiro", Nivel.INTERMEDIARIO, Frequencia.RARO),

    FUNCAO_COMPOSTA(
            Disciplina.MATEMATICA, "Funções",
            "Função composta", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    FUNCAO_INVERSA(
            Disciplina.MATEMATICA, "Funções",
            "Função inversa", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    FUNCAO_INJETORA(
            Disciplina.MATEMATICA, "Funções",
            "Função injetora", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    FUNCAO_SOBREJETORA(
            Disciplina.MATEMATICA, "Funções",
            "Função sobrejetora", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    FUNCAO_BIJETORA(
            Disciplina.MATEMATICA, "Funções",
            "Função bijetora", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    // =========================================================
    //  MATEMÁTICA — Volume 2: Potências e Logaritmos
    // =========================================================
    POTENCIACAO(
            Disciplina.MATEMATICA, "Potências e raízes",
            "Potenciação (expoentes naturais)", Nivel.BASICO, Frequencia.MUITO_COMUM),

    POTENCIA_EXPOENTE_NEGATIVO(
            Disciplina.MATEMATICA, "Potências e raízes",
            "Potências com expoente negativo", Nivel.BASICO, Frequencia.MUITO_COMUM),

    RADICIACAO(
            Disciplina.MATEMATICA, "Potências e raízes",
            "Radiciação e raízes", Nivel.BASICO, Frequencia.MUITO_COMUM),

    RACIONALIZACAO(
            Disciplina.MATEMATICA, "Potências e raízes",
            "Racionalização de denominadores", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    POTENCIA_EXPOENTE_RACIONAL(
            Disciplina.MATEMATICA, "Potências e raízes",
            "Potências com expoente racional", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    POTENCIA_EXPOENTE_REAL(
            Disciplina.MATEMATICA, "Potências e raízes",
            "Potências com expoente real", Nivel.AVANCADO, Frequencia.RARO),

    FUNCAO_EXPONENCIAL(
            Disciplina.MATEMATICA, "Função exponencial",
            "Função exponencial", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    EQUACOES_EXPONENCIAIS(
            Disciplina.MATEMATICA, "Função exponencial",
            "Equações exponenciais", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    INEQUACOES_EXPONENCIAIS(
            Disciplina.MATEMATICA, "Função exponencial",
            "Inequações exponenciais", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    LOGARITMOS_DEFINICAO(
            Disciplina.MATEMATICA, "Logaritmos",
            "Definição de logaritmo", Nivel.BASICO, Frequencia.MUITO_COMUM),

    LOGARITMOS_PROPRIEDADES(
            Disciplina.MATEMATICA, "Logaritmos",
            "Propriedades dos logaritmos", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    MUDANCA_BASE_LOGARITMO(
            Disciplina.MATEMATICA, "Logaritmos",
            "Mudança de base", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    COLOGARITMO(
            Disciplina.MATEMATICA, "Logaritmos",
            "Cologaritmo", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    FUNCAO_LOGARITMICA(
            Disciplina.MATEMATICA, "Função logarítmica",
            "Função logarítmica", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    DOMINIO_FUNCAO_LOGARITMICA(
            Disciplina.MATEMATICA, "Função logarítmica",
            "Domínio da função logarítmica", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    EQUACOES_LOGARITMICAS(
            Disciplina.MATEMATICA, "Função logarítmica",
            "Equações logarítmicas", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    INEQUACOES_LOGARITMICAS(
            Disciplina.MATEMATICA, "Função logarítmica",
            "Inequações logarítmicas", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    LOGARITMOS_DECIMAIS(
            Disciplina.MATEMATICA, "Logaritmos",
            "Logaritmos decimais (tábuas)", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    CARACTERISTICA_MANTISSA(
            Disciplina.MATEMATICA, "Logaritmos",
            "Característica e mantissa", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    // =========================================================
    //  MATEMÁTICA — Tópicos existentes (mantidos)
    // =========================================================
    PORCENTAGEM(
            Disciplina.MATEMATICA, "Aritmética e Números",
            "Porcentagem", Nivel.BASICO, Frequencia.MUITO_COMUM),

    JUROS_SIMPLES_COMPOSTOS(
            Disciplina.MATEMATICA, "Aritmética e Números",
            "Juros simples e compostos", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    PROGRESSAO_ARITMETICA(
            Disciplina.MATEMATICA, "Aritmética e Números",
            "Progressão Aritmética (PA)", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    PROGRESSAO_GEOMETRICA(
            Disciplina.MATEMATICA, "Aritmética e Números",
            "Progressão Geométrica (PG)", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    EXPRESSOES_ALGEBRICAS(
            Disciplina.MATEMATICA, "Álgebra",
            "Expressões algébricas", Nivel.BASICO, Frequencia.MUITO_COMUM),

    PRODUTOS_NOTAVEIS(
            Disciplina.MATEMATICA, "Álgebra",
            "Produtos notáveis", Nivel.BASICO, Frequencia.MUITO_COMUM),

    FATORACAO(
            Disciplina.MATEMATICA, "Álgebra",
            "Fatoração", Nivel.BASICO, Frequencia.MUITO_COMUM),

    EQUACOES_1_GRAU(
            Disciplina.MATEMATICA, "Álgebra",
            "Equações do 1.º grau", Nivel.BASICO, Frequencia.MUITO_COMUM),

    EQUACOES_2_GRAU(
            Disciplina.MATEMATICA, "Álgebra",
            "Equações do 2.º grau", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    SISTEMAS_DE_EQUACOES(
            Disciplina.MATEMATICA, "Álgebra",
            "Sistemas de equações", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    INEQUACOES(
            Disciplina.MATEMATICA, "Álgebra",
            "Inequações", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    MODULO_VALOR_ABSOLUTO(
            Disciplina.MATEMATICA, "Álgebra",
            "Módulo e valor absoluto", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    // =========================================================
    //  MATEMÁTICA — Geometria Plana
    // =========================================================
    ANGULOS_RETAS(
            Disciplina.MATEMATICA, "Geometria Plana",
            "Ângulos e retas", Nivel.BASICO, Frequencia.MUITO_COMUM),

    TRIANGULOS(
            Disciplina.MATEMATICA, "Geometria Plana",
            "Triângulos", Nivel.BASICO, Frequencia.MUITO_COMUM),

    QUADRILATEROS(
            Disciplina.MATEMATICA, "Geometria Plana",
            "Quadriláteros", Nivel.BASICO, Frequencia.MUITO_COMUM),

    CIRCULO_CIRCUNFERENCIA(
            Disciplina.MATEMATICA, "Geometria Plana",
            "Círculo e circunferência", Nivel.BASICO, Frequencia.MUITO_COMUM),

    SEMELHANCA_FIGURAS(
            Disciplina.MATEMATICA, "Geometria Plana",
            "Semelhança de figuras", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    TEOREMA_PITAGORAS(
            Disciplina.MATEMATICA, "Geometria Plana",
            "Teorema de Pitágoras", Nivel.BASICO, Frequencia.MUITO_COMUM),

    AREAS_PERIMETROS(
            Disciplina.MATEMATICA, "Geometria Plana",
            "Áreas e perímetros", Nivel.BASICO, Frequencia.MUITO_COMUM),

    // =========================================================
    //  MATEMÁTICA — Geometria Espacial
    // =========================================================
    PRISMAS(
            Disciplina.MATEMATICA, "Geometria Espacial",
            "Prismas", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    PIRAMIDES(
            Disciplina.MATEMATICA, "Geometria Espacial",
            "Pirâmides", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    CILINDRO_CONE_ESFERA(
            Disciplina.MATEMATICA, "Geometria Espacial",
            "Cilindro, cone e esfera", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    VOLUMES_AREAS_TOTAIS(
            Disciplina.MATEMATICA, "Geometria Espacial",
            "Volumes e áreas totais", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    // =========================================================
    //  MATEMÁTICA — Geometria Analítica
    // =========================================================
    PLANO_CARTESIANO(
            Disciplina.MATEMATICA, "Geometria Analítica",
            "Plano cartesiano", Nivel.BASICO, Frequencia.MUITO_COMUM),

    DISTANCIA_ENTRE_PONTOS(
            Disciplina.MATEMATICA, "Geometria Analítica",
            "Distância entre pontos", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    PONTO_MEDIO(
            Disciplina.MATEMATICA, "Geometria Analítica",
            "Ponto médio", Nivel.BASICO, Frequencia.MUITO_COMUM),

    EQUACAO_DA_RETA(
            Disciplina.MATEMATICA, "Geometria Analítica",
            "Equação da reta", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    EQUACAO_CIRCUNFERENCIA(
            Disciplina.MATEMATICA, "Geometria Analítica",
            "Equação da circunferência", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    CONICAS(
            Disciplina.MATEMATICA, "Geometria Analítica",
            "Cônicas (elipse, parábola, hipérbole)", Nivel.AVANCADO, Frequencia.RARO),

    // =========================================================
    //  MATEMÁTICA — Trigonometria
    // =========================================================
    RAZOES_TRIGONOMETRICAS(
            Disciplina.MATEMATICA, "Trigonometria",
            "Razões trigonométricas no triângulo", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    FUNCOES_TRIG_SENO_COS_TAN(
            Disciplina.MATEMATICA, "Trigonometria",
            "Funções seno, cosseno e tangente", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    IDENTIDADES_TRIGONOMETRICAS(
            Disciplina.MATEMATICA, "Trigonometria",
            "Identidades trigonométricas", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    LEI_SENOS_COSSENOS(
            Disciplina.MATEMATICA, "Trigonometria",
            "Lei dos senos e cossenos", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    ANGULOS_NOTAVEIS(
            Disciplina.MATEMATICA, "Trigonometria",
            "Ângulos notáveis (30°, 45°, 60°, 90°)", Nivel.BASICO, Frequencia.MUITO_COMUM),

    TRANSFORMACOES_TRIGONOMETRICAS(
            Disciplina.MATEMATICA, "Trigonometria",
            "Transformações trigonométricas", Nivel.AVANCADO, Frequencia.RARO),

    // =========================================================
    //  MATEMÁTICA — Estatística e Combinatória
    // =========================================================
    MEDIA_MODA_MEDIANA(
            Disciplina.MATEMATICA, "Estatística",
            "Média, moda e mediana", Nivel.BASICO, Frequencia.MUITO_COMUM),

    DESVIO_PADRAO_VARIANCIA(
            Disciplina.MATEMATICA, "Estatística",
            "Desvio padrão e variância", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    PRINCIPIO_CONTAGEM(
            Disciplina.MATEMATICA, "Combinatória",
            "Princípio fundamental da contagem", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    PERMUTACOES(
            Disciplina.MATEMATICA, "Combinatória",
            "Permutações", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    COMBINACOES(
            Disciplina.MATEMATICA, "Combinatória",
            "Combinações", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    ARRANJOS(
            Disciplina.MATEMATICA, "Combinatória",
            "Arranjos", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    PROBABILIDADE_BASICA(
            Disciplina.MATEMATICA, "Combinatória",
            "Probabilidade básica", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    PROBABILIDADE_CONDICIONAL(
            Disciplina.MATEMATICA, "Combinatória",
            "Probabilidade condicional", Nivel.AVANCADO, Frequencia.COMUM),

    // =========================================================
    //  MATEMÁTICA — Análise (nível superior)
    // =========================================================
    LIMITES(
            Disciplina.MATEMATICA, "Análise",
            "Limites", Nivel.AVANCADO, Frequencia.COMUM),

    DERIVADAS(
            Disciplina.MATEMATICA, "Análise",
            "Derivadas", Nivel.AVANCADO, Frequencia.COMUM),

    INTEGRAIS(
            Disciplina.MATEMATICA, "Análise",
            "Integrais (introdução)", Nivel.AVANCADO, Frequencia.RARO),

    MAXIMOS_MINIMOS(
            Disciplina.MATEMATICA, "Análise",
            "Máximos e mínimos", Nivel.AVANCADO, Frequencia.COMUM),

    // =========================================================
    //  FÍSICA — Mecânica (Halliday Vol. 1)
    // =========================================================
    GRANDEZAS_UNIDADES(
            Disciplina.FISICA, "Mecânica",
            "Grandezas e unidades (SI)", Nivel.BASICO, Frequencia.MUITO_COMUM),

    VETORES_OPERACOES(
            Disciplina.FISICA, "Mecânica",
            "Vetores e operações", Nivel.BASICO, Frequencia.MUITO_COMUM),

    CINEMATICA_ESCALAR(
            Disciplina.FISICA, "Mecânica",
            "Cinemática escalar", Nivel.BASICO, Frequencia.MUITO_COMUM),

    CINEMATICA_VETORIAL(
            Disciplina.FISICA, "Mecânica",
            "Cinemática vetorial", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    MRU_MRUV(
            Disciplina.FISICA, "Mecânica",
            "MRU e MRUV", Nivel.BASICO, Frequencia.MUITO_COMUM),

    QUEDA_LIVRE(
            Disciplina.FISICA, "Mecânica",
            "Queda livre", Nivel.BASICO, Frequencia.MUITO_COMUM),

    LANCAMENTO_PROJETEIS(
            Disciplina.FISICA, "Mecânica",
            "Lançamento de projéteis", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    LEIS_DE_NEWTON(
            Disciplina.FISICA, "Mecânica",
            "Leis de Newton", Nivel.BASICO, Frequencia.MUITO_COMUM),

    ATRITO(
            Disciplina.FISICA, "Mecânica",
            "Atrito", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    TRABALHO_ENERGIA(
            Disciplina.FISICA, "Mecânica",
            "Trabalho e energia", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    POTENCIA(
            Disciplina.FISICA, "Mecânica",
            "Potência", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    CONSERVACAO_ENERGIA(
            Disciplina.FISICA, "Mecânica",
            "Conservação de energia", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    QUANTIDADE_MOVIMENTO(
            Disciplina.FISICA, "Mecânica",
            "Quantidade de movimento", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    IMPULSO(
            Disciplina.FISICA, "Mecânica",
            "Impulso", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    COLISOES(
            Disciplina.FISICA, "Mecânica",
            "Colisões", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    ESTATICA_EQUILIBRIO(
            Disciplina.FISICA, "Mecânica",
            "Estática e equilíbrio", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    TORQUE(
            Disciplina.FISICA, "Mecânica",
            "Torque", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    CENTRO_DE_MASSA(
            Disciplina.FISICA, "Mecânica",
            "Centro de massa", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    MOVIMENTO_CIRCULAR_UNIFORME(
            Disciplina.FISICA, "Mecânica",
            "Movimento circular uniforme", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    FORCA_CENTRIPETA(
            Disciplina.FISICA, "Mecânica",
            "Força centrípeta", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    GRAFICOS_CINEMATICA(
            Disciplina.FISICA, "Mecânica",
            "Gráficos de posição, velocidade e aceleração", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    PLANO_INCLINADO(
            Disciplina.FISICA, "Mecânica",
            "Plano inclinado", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    FORCA_NORMAL(
            Disciplina.FISICA, "Mecânica",
            "Força normal", Nivel.BASICO, Frequencia.MUITO_COMUM),

    // =========================================================
    //  FÍSICA — Termologia
    // =========================================================
    TEMPERATURA_ESCALAS(
            Disciplina.FISICA, "Termologia",
            "Temperatura e escalas", Nivel.BASICO, Frequencia.MUITO_COMUM),

    DILATACAO_TERMICA(
            Disciplina.FISICA, "Termologia",
            "Dilatação térmica", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    CALOR_CALORIMETRIA(
            Disciplina.FISICA, "Termologia",
            "Calor e calorimetria", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    MUDANCA_DE_ESTADO(
            Disciplina.FISICA, "Termologia",
            "Mudança de estado", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    TRANSMISSAO_DE_CALOR(
            Disciplina.FISICA, "Termologia",
            "Transmissão de calor", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    GASES_IDEAIS(
            Disciplina.FISICA, "Termologia",
            "Gases ideais", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    PRIMEIRA_LEI_TERMODINAMICA(
            Disciplina.FISICA, "Termologia",
            "1.ª lei da Termodinâmica", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    SEGUNDA_LEI_TERMODINAMICA(
            Disciplina.FISICA, "Termologia",
            "2.ª lei da Termodinâmica", Nivel.AVANCADO, Frequencia.COMUM),

    CICLO_CARNOT(
            Disciplina.FISICA, "Termologia",
            "Ciclo de Carnot", Nivel.AVANCADO, Frequencia.RARO),

    // =========================================================
    //  FÍSICA — Ondulatória e Acústica
    // =========================================================
    NATUREZA_DAS_ONDAS(
            Disciplina.FISICA, "Ondulatória e Acústica",
            "Natureza das ondas", Nivel.BASICO, Frequencia.MUITO_COMUM),

    COMPRIMENTO_ONDA_FREQUENCIA(
            Disciplina.FISICA, "Ondulatória e Acústica",
            "Comprimento de onda e frequência", Nivel.BASICO, Frequencia.MUITO_COMUM),

    REFLEXAO_REFRACAO(
            Disciplina.FISICA, "Ondulatória e Acústica",
            "Reflexão e refração", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    DIFRACAO_INTERFERENCIA(
            Disciplina.FISICA, "Ondulatória e Acústica",
            "Difração e interferência", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    SOM_PROPAGACAO(
            Disciplina.FISICA, "Ondulatória e Acústica",
            "Som e propagação", Nivel.BASICO, Frequencia.MUITO_COMUM),

    EFEITO_DOPPLER(
            Disciplina.FISICA, "Ondulatória e Acústica",
            "Efeito Doppler", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    RESSONANCIA(
            Disciplina.FISICA, "Ondulatória e Acústica",
            "Ressonância", Nivel.INTERMEDIARIO, Frequencia.RARO),

    // =========================================================
    //  FÍSICA — Óptica
    // =========================================================
    REFLEXAO_DA_LUZ(
            Disciplina.FISICA, "Óptica",
            "Reflexão da luz", Nivel.BASICO, Frequencia.MUITO_COMUM),

    ESPELHOS_PLANOS_ESFERICOS(
            Disciplina.FISICA, "Óptica",
            "Espelhos planos e esféricos", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    REFRACAO(
            Disciplina.FISICA, "Óptica",
            "Refração", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    LENTES(
            Disciplina.FISICA, "Óptica",
            "Lentes convergentes e divergentes", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    EQUACAO_GAUSS(
            Disciplina.FISICA, "Óptica",
            "Equação de Gauss (óptica)", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    DISPERSAO_PRISMAS(
            Disciplina.FISICA, "Óptica",
            "Dispersão e prismas", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    INSTRUMENTOS_OPTICOS(
            Disciplina.FISICA, "Óptica",
            "Instrumentos ópticos", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    // =========================================================
    //  FÍSICA — Eletricidade
    // =========================================================
    CARGA_ELETRICA_COULOMB(
            Disciplina.FISICA, "Eletricidade",
            "Carga elétrica e força de Coulomb", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    CAMPO_ELETRICO(
            Disciplina.FISICA, "Eletricidade",
            "Campo elétrico", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    POTENCIAL_ELETRICO(
            Disciplina.FISICA, "Eletricidade",
            "Potencial elétrico", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    CAPACITORES(
            Disciplina.FISICA, "Eletricidade",
            "Capacitores", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    CORRENTE_ELETRICA(
            Disciplina.FISICA, "Eletricidade",
            "Corrente elétrica", Nivel.BASICO, Frequencia.MUITO_COMUM),

    LEI_DE_OHM(
            Disciplina.FISICA, "Eletricidade",
            "Lei de Ohm", Nivel.BASICO, Frequencia.MUITO_COMUM),

    RESISTENCIA_RESISTIVIDADE(
            Disciplina.FISICA, "Eletricidade",
            "Resistência e resistividade", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    CIRCUITOS_SERIE_PARALELO(
            Disciplina.FISICA, "Eletricidade",
            "Circuitos série e paralelo", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    LEIS_DE_KIRCHHOFF(
            Disciplina.FISICA, "Eletricidade",
            "Leis de Kirchhoff", Nivel.AVANCADO, Frequencia.COMUM),

    POTENCIA_ELETRICA(
            Disciplina.FISICA, "Eletricidade",
            "Potência elétrica", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    EFEITO_JOULE(
            Disciplina.FISICA, "Eletricidade",
            "Efeito Joule", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    // =========================================================
    //  FÍSICA — Magnetismo e Eletromagnetismo
    // =========================================================
    CAMPO_MAGNETICO(
            Disciplina.FISICA, "Magnetismo e Eletromagnetismo",
            "Campo magnético", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    FORCA_MAGNETICA_LORENTZ(
            Disciplina.FISICA, "Magnetismo e Eletromagnetismo",
            "Força magnética (Lorentz)", Nivel.INTERMEDIARIO, Frequencia.MUITO_COMUM),

    INDUCAO_ELETROMAGNETICA(
            Disciplina.FISICA, "Magnetismo e Eletromagnetismo",
            "Indução eletromagnética", Nivel.AVANCADO, Frequencia.COMUM),

    LEI_FARADAY_LENZ(
            Disciplina.FISICA, "Magnetismo e Eletromagnetismo",
            "Lei de Faraday e Lenz", Nivel.AVANCADO, Frequencia.COMUM),

    TRANSFORMADORES(
            Disciplina.FISICA, "Magnetismo e Eletromagnetismo",
            "Transformadores", Nivel.AVANCADO, Frequencia.COMUM),

    CORRENTE_ALTERNADA(
            Disciplina.FISICA, "Magnetismo e Eletromagnetismo",
            "Corrente alternada", Nivel.AVANCADO, Frequencia.COMUM),

    ONDAS_ELETROMAGNETICAS(
            Disciplina.FISICA, "Magnetismo e Eletromagnetismo",
            "Ondas eletromagnéticas", Nivel.AVANCADO, Frequencia.RARO),

    // =========================================================
    //  FÍSICA — Física Moderna
    // =========================================================
    RELATIVIDADE_RESTRITA(
            Disciplina.FISICA, "Física Moderna",
            "Relatividade restrita", Nivel.AVANCADO, Frequencia.RARO),

    EFEITO_FOTOELETRICO(
            Disciplina.FISICA, "Física Moderna",
            "Efeito fotoelétrico", Nivel.AVANCADO, Frequencia.COMUM),

    DUALIDADE_ONDA_PARTICULA(
            Disciplina.FISICA, "Física Moderna",
            "Dualidade onda-partícula", Nivel.AVANCADO, Frequencia.RARO),

    MODELOS_ATOMICOS(
            Disciplina.FISICA, "Física Moderna",
            "Modelos atômicos", Nivel.INTERMEDIARIO, Frequencia.COMUM),

    RADIOATIVIDADE(
            Disciplina.FISICA, "Física Moderna",
            "Radioatividade", Nivel.AVANCADO, Frequencia.COMUM),

    FISSAO_FUSAO_NUCLEAR(
            Disciplina.FISICA, "Física Moderna",
            "Fissão e fusão nuclear", Nivel.AVANCADO, Frequencia.RARO);

    // =========================================================
    //  Campos
    // =========================================================
    private final Disciplina disciplina;
    private final String area;
    private final String label;
    private final Nivel nivel;
    private final Frequencia frequencia;

    TopicoExame(Disciplina disciplina, String area, String label,
                Nivel nivel, Frequencia frequencia) {
        this.disciplina  = disciplina;
        this.area        = area;
        this.label       = label;
        this.nivel       = nivel;
        this.frequencia  = frequencia;
    }

    // =========================================================
    //  Getters
    // =========================================================
    public Disciplina getDisciplina()  { return disciplina; }
    public String     getArea()        { return area; }
    public String     getLabel()       { return label; }
    public Nivel      getNivel()       { return nivel; }
    public Frequencia getFrequencia()  { return frequencia; }

    // =========================================================
    //  Métodos utilitários
    // =========================================================

    public static java.util.Optional<Disciplina> resolverDisciplina(String nomeDisciplina) {
        if (nomeDisciplina == null || nomeDisciplina.isBlank()) {
            return java.util.Optional.empty();
        }

        String normalizado = com.imetro.util.TextoUtil.normalizarMinusculo(nomeDisciplina);
        if (normalizado.contains("mat") || normalizado.contains("matem")) {
            return java.util.Optional.of(Disciplina.MATEMATICA);
        }
        if (normalizado.contains("fis") || normalizado.contains("física")) {
            return java.util.Optional.of(Disciplina.FISICA);
        }
        return java.util.Optional.empty();
    }

    public static java.util.List<TopicoExame> topicosModoInteligente(Disciplina disciplina) {
        if (disciplina == null) {
            return java.util.List.of();
        }

        return porDisciplinaENivel(disciplina, Nivel.INTERMEDIARIO);
    }

    public static java.util.List<String> labelsModoInteligente(Disciplina disciplina) {
        return topicosModoInteligente(disciplina).stream()
            .map(TopicoExame::getLabel)
            .toList();
    }

    public static String instrucoesModoInteligente(Disciplina disciplina) {
        java.util.List<String> labels = labelsModoInteligente(disciplina);
        if (labels.isEmpty()) {
            return "";
        }

        StringBuilder texto = new StringBuilder();
        texto.append("Lista canonica de topicos permitidos no modo inteligente:\n");
        for (String label : labels) {
            texto.append("- ").append(label).append('\n');
        }
        texto.append("Usa exatamente estes nomes. Se o livro trouxer uma variante, normaliza-a para o nome canonico correspondente.");
        return texto.toString();
    }

    public static java.util.Optional<TopicoExame> resolverTopicoModoInteligente(Disciplina disciplina, String valor) {
        if (disciplina == null || valor == null || valor.isBlank()) {
            return java.util.Optional.empty();
        }

        String normalizado = com.imetro.util.TextoUtil.normalizarMinusculo(valor);
        return topicosModoInteligente(disciplina).stream()
            .filter(topico -> com.imetro.util.TextoUtil.normalizarMinusculo(topico.getLabel()).equals(normalizado))
            .findFirst();
    }

    // =========================================================
    //  Helpers de filtragem estática
    // =========================================================

    public static java.util.List<TopicoExame> porDisciplina(Disciplina d) {
        return java.util.Arrays.stream(values())
                .filter(t -> t.disciplina == d)
                .toList();
    }

    public static java.util.List<TopicoExame> porNivel(Nivel n) {
        return java.util.Arrays.stream(values())
                .filter(t -> t.nivel == n)
                .toList();
    }

    public static java.util.List<TopicoExame> porDisciplinaENivel(Disciplina d, Nivel n) {
        return java.util.Arrays.stream(values())
                .filter(t -> t.disciplina == d && t.nivel == n)
                .toList();
    }

    public static java.util.List<TopicoExame> topicosQuentes() {
        return java.util.Arrays.stream(values())
                .filter(t -> t.frequencia == Frequencia.MUITO_COMUM)
                .toList();
    }

    public static java.util.List<TopicoExame> porArea(String area) {
        if (area == null || area.isBlank()) {
            return java.util.List.of();
        }
        return java.util.Arrays.stream(values())
                .filter(t -> t.area.equalsIgnoreCase(area))
                .toList();
    }

    // =========================================================
    //  Enums internos (mantidos)
    // =========================================================
    public enum Disciplina {
        MATEMATICA("Matemática"),
        FISICA("Física");

        private final String label;
        Disciplina(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    public enum Nivel {
        BASICO("Básico"),
        INTERMEDIARIO("Intermediário"),
        AVANCADO("Avançado");

        private final String label;
        Nivel(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    public enum Frequencia {
        MUITO_COMUM("Muito comum"),
        COMUM("Comum"),
        RARO("Raro");

        private final String label;
        Frequencia(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    @Override
    public String toString() {
        return "[" + disciplina.getLabel() + " | " + area + "] " + label
                + " — " + nivel.getLabel() + " / " + frequencia.getLabel();
    }
}