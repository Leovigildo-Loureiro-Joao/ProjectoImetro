package com.imetro.util;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javafx.scene.image.Image;

public final class MedalSupport {

    private static final int BRONZE_TARGET = 5;
    private static final int PRATA_TARGET = 15;
    private static final int OURO_TARGET = 30;
    private static final int PLATINA_TARGET = 50;

    public enum MedalSkill {
        TIME("TIME", "Velocidade", "testes velozes", "time"),
        PONTARIA("PONTARIA", "Pontaria", "testes precisos", "pontaria"),
        LOGICA("LOGICA", "Logica", "desafios logicos", "logica"),
        RESILIENCIA("RESILIENCIA", "Resiliencia", "retomas fortes", "resiliencia"),
        CONSISTENCIA("CONSISTENCIA", "Consistencia", "series consistentes", "consistencia");

        private final String code;
        private final String label;
        private final String progressUnit;
        private final String assetPrefix;

        MedalSkill(String code, String label, String progressUnit, String assetPrefix) {
            this.code = code;
            this.label = label;
            this.progressUnit = progressUnit;
            this.assetPrefix = assetPrefix;
        }

        public String code() {
            return code;
        }

        public String label() {
            return label;
        }

        public String progressUnit() {
            return progressUnit;
        }

        public String assetPrefix() {
            return assetPrefix;
        }
    }

    public enum MedalTier {
        BRONZE("BRONZE", "Bronze", BRONZE_TARGET),
        PRATA("PRATA", "Prata", PRATA_TARGET),
        OURO("OURO", "Ouro", OURO_TARGET),
        PLATINA("PLATINA", "Platina", PLATINA_TARGET);

        private final String code;
        private final String label;
        private final int target;

        MedalTier(String code, String label, int target) {
            this.code = code;
            this.label = label;
            this.target = target;
        }

        public String code() {
            return code;
        }

        public String label() {
            return label;
        }

        public int target() {
            return target;
        }
    }

    public record MedalDefinition(
        String code,
        MedalSkill skill,
        MedalTier tier,
        String title,
        String description,
        String imageRef,
        int targetValue,
        String targetUnit,
        int displayOrder
    ) {
    }

    public record MedalAward(
        String medalCode,
        int progressValue,
        Integer recordValue,
        LocalDateTime earnedAt
    ) {
    }

    public record MedalViewModel(
        MedalDefinition definition,
        boolean unlocked,
        int progressValue,
        Integer recordValue,
        LocalDateTime earnedAt
    ) {
        public int remainingToUnlock() {
            return Math.max(0, definition.targetValue() - progressValue);
        }
    }

    private static final List<MedalDefinition> CATALOG = buildCatalog();
    private static final Map<MedalSkill, Integer> NAVIGATION_PREVIEW_PROGRESS = Map.of(
        MedalSkill.TIME, 18,
        MedalSkill.PONTARIA, 11,
        MedalSkill.LOGICA, 6,
        MedalSkill.RESILIENCIA, 4,
        MedalSkill.CONSISTENCIA, 22
    );

    private MedalSupport() {
    }

    public static List<MedalDefinition> catalog() {
        return CATALOG;
    }

    public static Map<MedalSkill, Integer> navigationPreviewProgress() {
        return NAVIGATION_PREVIEW_PROGRESS;
    }

    public static MedalDefinition findByCode(String code) {
        if (code == null || code.isBlank()) {
            return null;
        }

        String normalized = code.trim().toUpperCase(Locale.ROOT);
        for (MedalDefinition definition : CATALOG) {
            if (definition.code().equals(normalized)) {
                return definition;
            }
        }
        return null;
    }

    public static Image loadMedalImage(String imageRef) {
        if (imageRef == null || imageRef.isBlank()) {
            return null;
        }

        URL resource = MedalSupport.class.getResource(imageRef.trim());
        if (resource == null) {
            return null;
        }

        return new Image(resource.toExternalForm(), true);
    }

    private static List<MedalDefinition> buildCatalog() {
        return List.of(
            createDefinition(MedalSkill.TIME, MedalTier.BRONZE, 1),
            createDefinition(MedalSkill.TIME, MedalTier.PRATA, 2),
            createDefinition(MedalSkill.TIME, MedalTier.OURO, 3),
            createDefinition(MedalSkill.TIME, MedalTier.PLATINA, 4),

            createDefinition(MedalSkill.PONTARIA, MedalTier.BRONZE, 5),
            createDefinition(MedalSkill.PONTARIA, MedalTier.PRATA, 6),
            createDefinition(MedalSkill.PONTARIA, MedalTier.OURO, 7),
            createDefinition(MedalSkill.PONTARIA, MedalTier.PLATINA, 8),

            createDefinition(MedalSkill.LOGICA, MedalTier.BRONZE, 9),
            createDefinition(MedalSkill.LOGICA, MedalTier.PRATA, 10),
            createDefinition(MedalSkill.LOGICA, MedalTier.OURO, 11),
            createDefinition(MedalSkill.LOGICA, MedalTier.PLATINA, 12),

            createDefinition(MedalSkill.RESILIENCIA, MedalTier.BRONZE, 13),
            createDefinition(MedalSkill.RESILIENCIA, MedalTier.PRATA, 14),
            createDefinition(MedalSkill.RESILIENCIA, MedalTier.OURO, 15),
            createDefinition(MedalSkill.RESILIENCIA, MedalTier.PLATINA, 16),

            createDefinition(MedalSkill.CONSISTENCIA, MedalTier.BRONZE, 17),
            createDefinition(MedalSkill.CONSISTENCIA, MedalTier.PRATA, 18),
            createDefinition(MedalSkill.CONSISTENCIA, MedalTier.OURO, 19),
            createDefinition(MedalSkill.CONSISTENCIA, MedalTier.PLATINA, 20)
        );
    }

    private static MedalDefinition createDefinition(MedalSkill skill, MedalTier tier, int displayOrder) {
        String code = skill.code() + "_" + tier.code();
        String imageRef = "/com/imetro/assets/imgs/" + skill.assetPrefix() + "_" + tier.code().toLowerCase(Locale.ROOT) + ".png";
        String title = skill.label() + " " + tier.label();
        String description = "Conquista ao acumular " + tier.target() + " " + skill.progressUnit() + ".";

        return new MedalDefinition(
            code,
            skill,
            tier,
            title,
            description,
            imageRef,
            tier.target(),
            skill.progressUnit(),
            displayOrder
        );
    }
}
