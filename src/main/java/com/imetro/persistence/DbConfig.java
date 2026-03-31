package com.imetro.persistence;

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
        boolean enabled = Boolean.parseBoolean(envOrDefault("DB_ENABLED", "false"));
        String url = envOrNull("DB_URL");
        String user = envOrNull("DB_USER");
        String password = envOrNull("DB_PASSWORD");

        if (!enabled || isBlank(url) || isBlank(user) || password == null) {
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

    private static String envOrNull(String key) {
        return System.getenv(key);
    }

    private static String envOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return value == null ? defaultValue : value;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

