package com.imetro.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ProfileSessionState {

    private static final Map<String, String> AVATARS_BY_EMAIL = new ConcurrentHashMap<>();
    private static final Map<String, String> NAMES_BY_EMAIL = new ConcurrentHashMap<>();

    private ProfileSessionState() {
    }

    public static void rememberAvatar(String email, String avatarRef) {
        String normalizedEmail = normalizeEmail(email);
        if (normalizedEmail == null) {
            return;
        }

        if (avatarRef == null || avatarRef.isBlank()) {
            AVATARS_BY_EMAIL.remove(normalizedEmail);
            return;
        }

        AVATARS_BY_EMAIL.put(normalizedEmail, AvatarSupport.normalizeAvatarRef(avatarRef));
    }

    public static String resolveAvatar(String email, String fallbackAvatarRef) {
        String remembered = AVATARS_BY_EMAIL.get(normalizeEmail(email));
        if (remembered != null && !remembered.isBlank()) {
            return AvatarSupport.normalizeAvatarRef(remembered);
        }
        return AvatarSupport.normalizeAvatarRef(fallbackAvatarRef);
    }

    public static void rememberName(String email, String name) {
        String normalizedEmail = normalizeEmail(email);
        if (normalizedEmail == null) {
            return;
        }

        if (name == null || name.isBlank()) {
            NAMES_BY_EMAIL.remove(normalizedEmail);
            return;
        }

        NAMES_BY_EMAIL.put(normalizedEmail, name.trim());
    }

    public static String resolveName(String email, String fallbackName) {
        String remembered = NAMES_BY_EMAIL.get(normalizeEmail(email));
        if (remembered != null && !remembered.isBlank()) {
            return remembered;
        }
        return fallbackName;
    }

    public static void clearAll() {
        AVATARS_BY_EMAIL.clear();
        NAMES_BY_EMAIL.clear();
    }

    private static String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }
        return email.trim().toLowerCase();
    }
}
