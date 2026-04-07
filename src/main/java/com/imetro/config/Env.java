package com.imetro.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Simple .env loader for local development.
 *
 * <p>Precedence: OS environment variables (System.getenv) win over values in .env.</p>
 */
public final class Env {

    private static final AtomicReference<Map<String, String>> DOTENV_CACHE = new AtomicReference<>();

    private Env() {
    }

    public static String get(String key) {
        return get(key, null);
    }

    public static String get(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value != null) {
            return value;
        }
        value = dotenv().get(key);
        return value == null ? defaultValue : value;
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String raw = get(key, null);
        if (raw == null) {
            return defaultValue;
        }

        String normalized = raw.trim().toLowerCase(Locale.ROOT);
        return switch (normalized) {
            case "1", "true", "yes", "y", "on" -> true;
            case "0", "false", "no", "n", "off" -> false;
            default -> defaultValue;
        };
    }

    public static Map<String, String> dotenvSnapshot() {
        return dotenv();
    }

    private static Map<String, String> dotenv() {
        Map<String, String> cached = DOTENV_CACHE.get();
        if (cached != null) {
            return cached;
        }

        Map<String, String> loaded = new HashMap<>();
        // Common dev setup: .env in the working directory (project root when running via Maven/IDE).
        Path userDir = Paths.get(System.getProperty("user.dir"));
        loadIfExists(userDir.resolve(".env.local"), loaded);
        loadIfExists(userDir.resolve(".env"), loaded);

        // Fallback: allow bundling a .env on the classpath (optional).
        loadFromClasspath(".env", loaded);

        Map<String, String> frozen = Collections.unmodifiableMap(loaded);
        DOTENV_CACHE.compareAndSet(null, frozen);
        return DOTENV_CACHE.get();
    }

    private static void loadIfExists(Path path, Map<String, String> out) {
        try {
            if (!Files.exists(path) || !Files.isRegularFile(path)) {
                return;
            }
            try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                parse(reader, out);
            }
        } catch (Exception ignored) {
            // If .env can't be read, we simply behave like a normal Java app (System.getenv only).
        }
    }

    private static void loadFromClasspath(String resourceName, Map<String, String> out) {
        try (InputStream in = Env.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (in == null) {
                return;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                parse(reader, out);
            }
        } catch (IOException ignored) {
        }
    }

    private static void parse(BufferedReader reader, Map<String, String> out) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                continue;
            }

            // Support: export KEY=value
            if (trimmed.startsWith("export ")) {
                trimmed = trimmed.substring("export ".length()).trim();
            }

            int equalsIndex = trimmed.indexOf('=');
            if (equalsIndex <= 0) {
                continue;
            }

            String key = trimmed.substring(0, equalsIndex).trim();
            String valuePart = trimmed.substring(equalsIndex + 1).trim();
            if (key.isEmpty()) {
                continue;
            }

            String value = parseValue(valuePart);
            out.put(key, value);
        }
    }

    private static String parseValue(String valuePart) {
        if (valuePart.isEmpty()) {
            return "";
        }

        char first = valuePart.charAt(0);
        if ((first == '"' || first == '\'') && valuePart.length() >= 2) {
            char quote = first;
            int last = valuePart.lastIndexOf(quote);
            if (last > 0) {
                return valuePart.substring(1, last);
            }
            return valuePart.substring(1);
        }

        // Basic inline comment support: KEY=value # comment
        int commentIndex = valuePart.indexOf(" #");
        if (commentIndex >= 0) {
            return valuePart.substring(0, commentIndex).trim();
        }

        return valuePart;
    }
}
