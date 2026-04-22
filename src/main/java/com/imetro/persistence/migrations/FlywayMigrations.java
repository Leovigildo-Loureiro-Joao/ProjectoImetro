package com.imetro.persistence.migrations;

import com.imetro.config.Env;
import com.imetro.persistence.connection.DbConfig;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;

import java.util.Optional;

public final class FlywayMigrations {

    /**
     * O schema inicial do Docker (`scripts/db/001_schema.sql`) já inclui até V6.
     * Quando o Flyway encontra uma base "não vazia" sem histórico, ele cria um baseline e
     * aplica apenas migrations acima dessa versão.
     */
    private static final String DOCKER_SCHEMA_BASELINE_VERSION = "6";

    private FlywayMigrations() {
    }

    public static void tryMigrateFromEnv() {
        if (!Env.getBoolean("DB_MIGRATE", true)) {
            return;
        }

        Optional<DbConfig> cfgOpt = DbConfig.fromEnv();
        if (cfgOpt.isEmpty()) {
            return;
        }

        DbConfig cfg = cfgOpt.get();
        try {
            Flyway.configure()
                    .dataSource(cfg.getUrl(), cfg.getUser(), cfg.getPassword())
                    .locations("classpath:db/migration")
                    .baselineOnMigrate(true)
                    .baselineVersion(MigrationVersion.fromVersion(DOCKER_SCHEMA_BASELINE_VERSION))
                    .load()
                    .migrate();
        } catch (Exception e) {
            System.err.println("[imetro] Flyway migrate falhou: " + e.getMessage());
        }
    }
}

