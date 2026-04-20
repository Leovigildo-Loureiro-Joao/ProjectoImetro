package com.imetro.ui.controller.lifecycle;

/**
 * Controllers that hold resources (e.g. Timelines, listeners, threads) should implement this
 * so the app can release them when navigating away.
 */
public interface DisposableController {
    void dispose();
}

