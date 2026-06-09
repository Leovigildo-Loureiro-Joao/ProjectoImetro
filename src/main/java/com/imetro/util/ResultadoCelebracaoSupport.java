package com.imetro.util;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import com.imetro.config.RuntimeConfig;
import com.imetro.persistence.repository.MedalhaRepository;
import com.imetro.util.MedalSupport.MedalDefinition;
import com.imetro.util.MedalSupport.MedalSkill;

public final class ResultadoCelebracaoSupport {

    public record CelebrationSummary(
        String badgeLabel,
        String headline,
        String summary,
        String encouragement,
        double performancePercent,
        String performanceDetail,
        String medalTitle,
        String medalMessage,
        double medalProgress,
        String medalProgressDetail,
        String scholarshipTitle,
        String scholarshipMessage,
        double scholarshipProgress,
        String scholarshipProgressDetail,
        String actionLabel,
        boolean compacto
    ) {
    }

    private record MedalPreview(
        String title,
        String message,
        double progress,
        String detail
    ) {
    }

    private record ScholarshipMeta(
        String nome,
        int targetMatch,
        String cobertura,
        String destaque
    ) {
    }

    private record ScholarshipPreview(
        String title,
        String message,
        double progress,
        String detail
    ) {
    }

    private static final MedalhaRepository MEDALHA_REPOSITORY = new MedalhaRepository();
    private static final List<ScholarshipMeta> SCHOLARSHIPS = List.of(
        new ScholarshipMeta(
            "Beca Impacto Local",
            60,
            "Cobertura media com bonus por impacto",
            "Boa para transformar progresso academico em narrativa forte."
        ),
        new ScholarshipMeta(
            "Fundo Impulso Academico",
            68,
            "Apoio modular por semestre",
            "Boa opcao para ganhar tracao rapida no nosso radar interno."
        ),
        new ScholarshipMeta(
            "Programa Horizonte STEM",
            76,
            "Parcial + laboratorio",
            "Grande encaixe para perfis tecnicos com melhoria continua."
        ),
        new ScholarshipMeta(
            "Bolsa Merito Atlas",
            85,
            "Cobertura quase total da propina",
            "A sessao de hoje ja te empurra para uma bolsa de topo."
        )
    );

    private ResultadoCelebracaoSupport() {
    }

    public static CelebrationSummary criarResumo(
        UUID candidatoId,
        String jornadaLabel,
        String contextoLabel,
        int acertos,
        int totalQuestoes,
        double desempenhoPercentual,
        String tempoFormatado,
        double tempoMedioSegundos,
        boolean diagnostico
    ) {
        double percentual = limitarFaixaCem(desempenhoPercentual);
        String jornada = valorOuPadrao(jornadaLabel, "Resultado");
        String contexto = valorOuPadrao(contextoLabel, "a tua jornada");

        MedalPreview medalPreview = construirMedalPreview(candidatoId, percentual, tempoMedioSegundos, diagnostico);
        ScholarshipPreview scholarshipPreview = construirBolsaPreview(percentual, tempoMedioSegundos, diagnostico);

        return new CelebrationSummary(
            jornada.toUpperCase(Locale.ROOT),
            resolverHeadline(percentual),
            "Terminaste " + jornada + " em " + contexto + " com " + Math.round(percentual) + "% de aproveitamento.",
            resolverEncorajamento(percentual, contexto),
            percentual,
            construirDetalheDesempenho(acertos, totalQuestoes, tempoFormatado, tempoMedioSegundos),
            medalPreview.title(),
            medalPreview.message(),
            medalPreview.progress(),
            medalPreview.detail(),
            scholarshipPreview.title(),
            scholarshipPreview.message(),
            scholarshipPreview.progress(),
            scholarshipPreview.detail(),
            "Ver meu resultado",
            false
        );
    }

    public static CelebrationSummary criarResumoTesteCompacto(
        double desempenhoPercentual,
        String areaProgresso,
        String pontoForte,
        String proximoDesafio
    ) {
        double percentual = limitarFaixaCem(desempenhoPercentual);
        int ganhoProgresso = percentual >= 75d ? 3 : percentual >= 60d ? 2 : 1;
        int diasSequencia = percentual >= 75d ? 5 : percentual >= 60d ? 4 : 3;

        return new CelebrationSummary(
            "Teste Concluído",
            Math.round(percentual) + "% de acerto",
            "+" + ganhoProgresso + "% de progresso em " + valorOuPadrao(areaProgresso, "Cinemática"),
            "Nova sequência:\n" + diasSequencia + " dias",
            percentual,
            "",
            "Ponto Forte",
            valorOuPadrao(pontoForte, "Movimento Uniforme"),
            0d,
            "",
            "Próximo Desafio",
            valorOuPadrao(proximoDesafio, "Leis de Newton"),
            0d,
            "",
            "Continuar",
            true
        );
    }

    private static MedalPreview construirMedalPreview(
        UUID candidatoId,
        double desempenhoPercentual,
        double tempoMedioSegundos,
        boolean diagnostico
    ) {
        MedalSkill skill = resolverSkillDestaque(desempenhoPercentual, tempoMedioSegundos, diagnostico);
        int baseProgress = carregarProgressoBase(candidatoId, skill);
        int ganhoSessao = calcularGanhoSessao(desempenhoPercentual, tempoMedioSegundos, diagnostico);
        int progressoProjetado = Math.max(0, baseProgress + ganhoSessao);

        List<MedalDefinition> trilho = MedalSupport.catalog().stream()
            .filter(definition -> definition.skill() == skill)
            .sorted(Comparator.comparingInt(MedalDefinition::displayOrder))
            .toList();

        MedalDefinition ultima = trilho.get(trilho.size() - 1);
        MedalDefinition alvo = trilho.stream()
            .filter(definition -> progressoProjetado < definition.targetValue())
            .findFirst()
            .orElse(ultima);

        boolean habilidadeCompleta = progressoProjetado >= ultima.targetValue();
        int progressoExibido = habilidadeCompleta
            ? ultima.targetValue()
            : Math.min(alvo.targetValue(), progressoProjetado);
        int restante = Math.max(0, alvo.targetValue() - progressoProjetado);

        String titulo = habilidadeCompleta
            ? "Medalha em destaque: " + ultima.title()
            : "Rumo a " + alvo.title();
        String detalhe = habilidadeCompleta
            ? "Escada desta habilidade concluida: " + ultima.targetValue() + "/" + ultima.targetValue()
            : progressoExibido + "/" + alvo.targetValue() + " " + alvo.targetUnit();

        String mensagem;
        if (habilidadeCompleta) {
            mensagem = "Ja tens margem para sustentar uma habilidade premium nesta escada. Agora e manter a regularidade.";
        } else if (restante <= 1) {
            mensagem = "Falta so mais uma sessao forte para destravar essa medalha.";
        } else if (restante <= 2) {
            mensagem = "Estas muito perto. Mais " + restante + " boas sessoes e essa medalha pode cair.";
        } else {
            mensagem = "A sessao de hoje ja contou para a tua trilha de " + skill.label().toLowerCase(Locale.ROOT) + ".";
        }

        return new MedalPreview(
            titulo,
            mensagem,
            limitarUnitario(alvo.targetValue() == 0 ? 0 : progressoExibido / (double) alvo.targetValue()),
            detalhe
        );
    }

    private static ScholarshipPreview construirBolsaPreview(
        double desempenhoPercentual,
        double tempoMedioSegundos,
        boolean diagnostico
    ) {
        int matchAtual = calcularMatchBolsa(desempenhoPercentual, tempoMedioSegundos, diagnostico);
        ScholarshipMeta alvo = SCHOLARSHIPS.stream()
            .filter(item -> matchAtual < item.targetMatch())
            .findFirst()
            .orElse(SCHOLARSHIPS.get(SCHOLARSHIPS.size() - 1));

        boolean bolsaNoAlvo = matchAtual >= SCHOLARSHIPS.get(SCHOLARSHIPS.size() - 1).targetMatch();
        int referencia = bolsaNoAlvo
            ? SCHOLARSHIPS.get(SCHOLARSHIPS.size() - 1).targetMatch()
            : alvo.targetMatch();
        int matchExibido = bolsaNoAlvo ? referencia : Math.min(matchAtual, referencia);
        int restante = Math.max(0, referencia - matchAtual);

        String titulo = bolsaNoAlvo
            ? "Bolsa ficticia em mira: " + SCHOLARSHIPS.get(SCHOLARSHIPS.size() - 1).nome()
            : "Bolsa ficticia em mira: " + alvo.nome();

        String mensagem;
        if (bolsaNoAlvo) {
            mensagem = "O teu match interno ja entrou em zona premium. A narrativa academica esta a ganhar peso real.";
        } else if (restante <= 4) {
            mensagem = "Estas a poucos pontos da " + alvo.nome() + ". Mais consistencia e ela entra no radar.";
        } else if (restante <= 10) {
            mensagem = "Ja entraste na zona competitiva. Falta pouco para abrir a " + alvo.cobertura() + ".";
        } else {
            mensagem = alvo.destaque();
        }

        return new ScholarshipPreview(
            titulo,
            mensagem,
            limitarUnitario(referencia == 0 ? 0 : matchExibido / (double) referencia),
            matchAtual + "% de match interno | meta " + referencia + "%"
        );
    }

    private static MedalSkill resolverSkillDestaque(
        double desempenhoPercentual,
        double tempoMedioSegundos,
        boolean diagnostico
    ) {
        if (!diagnostico && desempenhoPercentual >= 75 && tempoMedioSegundos > 0 && tempoMedioSegundos <= 30) {
            return MedalSkill.TIME;
        }
        if (desempenhoPercentual >= 85) {
            return diagnostico ? MedalSkill.LOGICA : MedalSkill.PONTARIA;
        }
        if (desempenhoPercentual >= 65) {
            return MedalSkill.CONSISTENCIA;
        }
        if (desempenhoPercentual >= 45) {
            return MedalSkill.RESILIENCIA;
        }
        return MedalSkill.LOGICA;
    }

    private static int carregarProgressoBase(UUID candidatoId, MedalSkill skill) {
        if (!RuntimeConfig.isDbEnabled()) {
            return MedalSupport.navigationPreviewProgress().getOrDefault(skill, 0);
        }

        int progresso = 0;
        for (MedalSupport.MedalAward award : MEDALHA_REPOSITORY.findAwardsByUserId(candidatoId)) {
            MedalDefinition definition = MedalSupport.findByCode(award.medalCode());
            if (definition != null && definition.skill() == skill) {
                progresso = Math.max(progresso, award.progressValue());
            }
        }
        return progresso;
    }

    private static int calcularGanhoSessao(
        double desempenhoPercentual,
        double tempoMedioSegundos,
        boolean diagnostico
    ) {
        int ganho = 1;
        if (desempenhoPercentual >= 80) {
            ganho++;
        }
        if (!diagnostico && tempoMedioSegundos > 0 && tempoMedioSegundos <= 30) {
            ganho++;
        }
        return Math.max(1, Math.min(3, ganho));
    }

    private static int calcularMatchBolsa(
        double desempenhoPercentual,
        double tempoMedioSegundos,
        boolean diagnostico
    ) {
        double base = desempenhoPercentual * 0.72;
        double bonusContexto = diagnostico ? 8 : 12;
        double bonusDesempenho = desempenhoPercentual >= 80 ? 8 : desempenhoPercentual >= 60 ? 4 : 1;
        double bonusTempo = !diagnostico && tempoMedioSegundos > 0 && tempoMedioSegundos <= 35 ? 4 : 0;
        return (int) Math.round(limitarFaixaCem(base + bonusContexto + bonusDesempenho + bonusTempo));
    }

    private static String resolverHeadline(double desempenhoPercentual) {
        if (desempenhoPercentual >= 90) {
            return "Fechaste esta etapa com nivel de destaque.";
        }
        if (desempenhoPercentual >= 75) {
            return "Boa sessao. Ja se nota tracao academica real.";
        }
        if (desempenhoPercentual >= 60) {
            return "Sessao concluida com sinais claros de crescimento.";
        }
        if (desempenhoPercentual >= 40) {
            return "Ha progresso para consolidar e ele ja comecou.";
        }
        return "Cada tentativa concluida empurra o teu crescimento.";
    }

    private static String resolverEncorajamento(double desempenhoPercentual, String contexto) {
        if (desempenhoPercentual >= 85) {
            return "Mantem este ritmo em " + contexto + ". O teu historico ja comeca a parecer de candidato forte.";
        }
        if (desempenhoPercentual >= 65) {
            return "Mais algumas sessoes assim e o teu perfil ganha ainda mais peso em medalhas e bolsas.";
        }
        if (desempenhoPercentual >= 45) {
            return "O mais importante ja aconteceu: terminaste a etapa e geraste dados para evoluir com intencao.";
        }
        return "Nao e sobre perfeicao. E sobre continuar, aprender e voltar mais forte na proxima rodada.";
    }

    private static String construirDetalheDesempenho(
        int acertos,
        int totalQuestoes,
        String tempoFormatado,
        double tempoMedioSegundos
    ) {
        StringBuilder detalhe = new StringBuilder();
        detalhe.append(acertos).append("/").append(Math.max(0, totalQuestoes)).append(" respostas certas");

        if (tempoFormatado != null && !tempoFormatado.isBlank()) {
            detalhe.append(" | tempo ").append(tempoFormatado);
        }
        if (tempoMedioSegundos > 0) {
            detalhe.append(" | media ").append(Math.round(tempoMedioSegundos)).append("s por questao");
        }

        return detalhe.toString();
    }

    private static String valorOuPadrao(String valor, String padrao) {
        return valor == null || valor.isBlank() ? padrao : valor;
    }

    private static double limitarFaixaCem(double valor) {
        return Math.max(0d, Math.min(100d, valor));
    }

    private static double limitarUnitario(double valor) {
        return Math.max(0d, Math.min(1d, valor));
    }
}
