package com.imetro.persistence.connection;

import com.imetro.config.Env;
import com.imetro.config.RuntimeConfig;

import java.util.Optional;

public final class DbConfig {

    private final boolean enabled;
    private final String url;
    private final String user;
    private final String password;

    private DbConfig(boolean enabled, String url, String user, String password) {
        this.enabled = enabled;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public static Optional<DbConfig> fromEnv() {
        boolean enabled = RuntimeConfig.isDbEnabled();
        if (!enabled) {
            return Optional.empty();
        }

        String url = Env.get("DB_URL", "jdbc:postgresql://localhost:5432/simulatorbolsastudy");
        String user = Env.get("DB_USER", "simulator");
        String password = Env.get("DB_PASSWORD", "simulator");

        if (isBlank(url) || isBlank(user) || isBlank(password)) {
            return Optional.empty();
        }

        return Optional.of(new DbConfig(true, url, user, password));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
