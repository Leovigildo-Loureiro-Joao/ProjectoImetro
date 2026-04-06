package com.imetro.persistence.connection;

import java.util.Optional;

public final class DbConfig {

    private final boolean enabled;
    private final String url;
    private final String user;
    private final String password;

    private DbConfig(boolean enabled, String url, String user, String password) {
        this.enabled = enabled;
        this.url = "jdbc:postgresql://localhost:5432/simulatorbolsastudy";
        this.user = "simulator";
        this.password = "simulator";
    }

    public static Optional<DbConfig> fromEnv() {
        boolean enabled = true;

        String url = "jdbc:postgresql://localhost:5432/simulatorbolsastudy";
        String user = "simulator";
        String password = "simulator";

        if (!enabled || isBlank(url) || isBlank(user) || password == null) {
            System.out.println("vazio");
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

