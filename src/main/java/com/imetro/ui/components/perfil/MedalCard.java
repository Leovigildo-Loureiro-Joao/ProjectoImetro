package com.imetro.ui.components.perfil;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.imetro.config.RuntimeConfig;
import com.imetro.util.MedalSupport;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MedalCard extends VBox{
    
    private static final String VERIFIED_BADGE_REF = "/com/imetro/assets/imgs/verified_badge_96px.png";
    private static final DateTimeFormatter MEDAL_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public  MedalCard(MedalSupport.MedalViewModel medal) {
        MedalSupport.MedalDefinition definition = medal.definition();

        StackPane artShell = new StackPane();
        artShell.getStyleClass().add("medal-art-shell");
        artShell.setPrefSize(116, 116);

        Image medalImage = MedalSupport.loadMedalImage(definition.imageRef());
        if (medalImage != null) {
            ImageView medalImageView = new ImageView(medalImage);
            medalImageView.setFitHeight(88);
            medalImageView.setFitWidth(88);
            medalImageView.setPreserveRatio(true);
            if (!medal.unlocked()) {
                medalImageView.setOpacity(0.45);
                medalImageView.setEffect(new ColorAdjust(0, -1, -0.10, 0));
            }
            artShell.getChildren().add(medalImageView);
        }

        if (medal.unlocked()) {
            Image badgeImage = loadVerifiedBadgeImage();
            if (badgeImage != null) {
                ImageView badgeView = new ImageView(badgeImage);
                badgeView.setFitHeight(28);
                badgeView.setFitWidth(28);
                badgeView.setPreserveRatio(true);
                StackPane.setAlignment(badgeView, Pos.TOP_RIGHT);
                badgeView.setTranslateX(8);
                badgeView.setTranslateY(-8);
                artShell.getChildren().add(badgeView);
            }
        }

        Label skillLabel = new Label(definition.skill().label());
        skillLabel.getStyleClass().add("timeline-pill");

        Label tierLabel = new Label(definition.tier().label());
        tierLabel.getStyleClass().addAll("medal-tier", "medal-tier-" + definition.tier().code().toLowerCase(Locale.ROOT));

        HBox pillRow = new HBox(8, skillLabel, tierLabel);

        Label titleLabel = new Label(definition.title());
        titleLabel.getStyleClass().add("h3-thin-big");
        titleLabel.setWrapText(true);

        Label descriptionLabel = new Label(definition.description());
        descriptionLabel.getStyleClass().add("muted");
        descriptionLabel.setWrapText(true);

        Label metaLabel = new Label("Meta: " + definition.targetValue() + " " + definition.targetUnit());
        metaLabel.getStyleClass().add("profile-metric-chip");

        Label statusPill = new Label(medal.unlocked() ? "Desbloqueada" : medal.remainingToUnlock() + " em falta");
        statusPill.getStyleClass().add(medal.unlocked() ? "pill-good" : "pill-warn");

        Label footerLabel = new Label(buildMedalFooter(medal));
        footerLabel.getStyleClass().add("muted");
        footerLabel.setWrapText(true);

        this.getChildren().addAll( artShell, pillRow, titleLabel, descriptionLabel, metaLabel, statusPill, footerLabel);
        this.getStyleClass().addAll("card-blur", "shadow", "medal-card");
        this.getStyleClass().add(medal.unlocked() ? "medal-card-unlocked" : "medal-card-locked");
        this.setPadding(new Insets(16));
        this.setPrefWidth(230);
        this.setMinHeight(288);
        this.setSpacing(12);

        Tooltip tooltip = new Tooltip(definition.description() + "\nMeta atual: " + definition.targetValue() + " " + definition.targetUnit() + ".");
        Tooltip.install(this, tooltip);
    }

    private String buildMedalFooter(MedalSupport.MedalViewModel medal) {
        if (medal.unlocked()) {
            if (medal.earnedAt() != null) {
                return "Conquistada em " + medal.earnedAt().format(MEDAL_DATE_FORMAT) + ".";
            }
            if (!RuntimeConfig.isDbEnabled()) {
                return "Preview desbloqueada com progresso demonstrativo desta sessao.";
            }
            return "Conquista registada no mural do candidato.";
        }

        if (!RuntimeConfig.isDbEnabled()) {
            return "Preview atual: " + medal.progressValue() + " " + medal.definition().targetUnit() + ".";
        }

        return "Ainda bloqueada. Vai abrir quando o recorde dessa habilidade entrar em sucessao.";
    }

     private Image loadVerifiedBadgeImage() {
        try {
            return new Image(getClass().getResource(VERIFIED_BADGE_REF).toExternalForm(), true);
        } catch (Exception e) {
            return null;
        }
    }

}
