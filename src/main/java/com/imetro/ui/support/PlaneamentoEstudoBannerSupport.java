package com.imetro.ui.support;

import java.util.List;

import com.imetro.domain.dto.planejamento.PlaneamentoEstudoEstado;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public final class PlaneamentoEstudoBannerSupport {
    private static final List<String> STATE_CLASSES = List.of(
        "study-plan-banner-active",
        "study-plan-banner-prolonged",
        "study-plan-banner-empty",
        "study-plan-banner-offline"
    );

    private PlaneamentoEstudoBannerSupport() {
    }

    public static void aplicar(Scene scene, PlaneamentoEstudoEstado estado) {
        if (scene == null) {
            return;
        }

        var bannerNode = scene.lookup("#layoutPlanBanner");
        var titleNode = scene.lookup("#layoutPlanTitleLabel");
        var detailNode = scene.lookup("#layoutPlanDetailLabel");

        if (!(bannerNode instanceof VBox banner) || !(titleNode instanceof Label title) || !(detailNode instanceof Label detail)) {
            return;
        }

        PlaneamentoEstudoEstado safeEstado = estado == null
            ? new PlaneamentoEstudoEstado(
                "Sem plano ativo",
                "Conclua o diagnostico inicial para gerar o primeiro mapa de estudo.",
                "study-plan-banner-empty"
            )
            : estado;

        banner.getStyleClass().removeAll(STATE_CLASSES);
        if (!banner.getStyleClass().contains("study-plan-banner")) {
            banner.getStyleClass().add("study-plan-banner");
        }
        if (safeEstado.styleClass() != null && !safeEstado.styleClass().isBlank()) {
            banner.getStyleClass().add(safeEstado.styleClass());
        }

        title.setText(safeEstado.titulo());
        detail.setText(safeEstado.detalhe());
    }
}
