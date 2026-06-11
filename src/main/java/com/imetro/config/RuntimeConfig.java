package com.imetro.config;

/**
 * Runtime flags to help run the app with or without a working database.
 *
 * <p>Use cases:
 * <ul>
 *   <li>WSL / environment issues: run with navigation-only mode (no DB).</li>
 *   <li>Local dev with Postgres: enable DB-backed flows.</li>
 * </ul>
 *
 * <p>Precedence:
 * <ol>
 *   <li>If {@code DB_ENABLED} is set, it wins.</li>
 *   <li>Otherwise, {@code TESTE} acts as an alias switch for enabling DB.</li>
 * </ol>
 */
public final class RuntimeConfig {

    private static final boolean DB_ENABLED = computeDbEnabled();
    private static final boolean BlockedIA = computeBlockedIA();

    private RuntimeConfig() {
    }

    public static boolean isDbEnabled() {
        return DB_ENABLED;
    }

    public static boolean isBlockedIA() {
        return BlockedIA;
    }


    private static boolean computeDbEnabled() {
        // Explicit config wins.
        String explicit = Env.get("DB_ENABLED", null);
        if (explicit != null) {
            return Env.getBoolean("DB_ENABLED", false);
        }
        // Alias switch (requested): TESTE=true enables DB; false => navigation-only.
        return Env.getBoolean("TESTE", false);
    }

    private static boolean computeBlockedIA() {
        // Explicit config wins.
        String explicit = Env.get("BlockedIA", null);
        if (explicit != null) {
            return Env.getBoolean("BlockedIA", false);
        }
        // Alias switch (requested): TESTE=true enables DB; false => navigation-only.
        return Env.getBoolean("TESTE", false);
    }
}
