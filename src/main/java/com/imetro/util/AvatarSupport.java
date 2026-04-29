package com.imetro.util;

import java.net.URL;
import java.util.List;
import java.util.Locale;

import javafx.scene.image.Image;

public final class AvatarSupport {

    public static final String INITIALS_TOKEN = "INITIALS";

    private static final List<String> AVAILABLE_AVATARS = List.of(
        "/com/imetro/assets/imgs/avatar1.png",
        "/com/imetro/assets/imgs/avatar2.png",
        "/com/imetro/assets/imgs/avatar3.png",
        "/com/imetro/assets/imgs/avatar4.png"
    );

    private AvatarSupport() {
    }

    public static List<String> availableAvatars() {
        return AVAILABLE_AVATARS;
    }

    public static boolean isPresetAvatar(String avatarRef) {
        if (avatarRef == null || avatarRef.isBlank()) {
            return false;
        }
        return AVAILABLE_AVATARS.contains(avatarRef.trim());
    }

    public static boolean usesInitials(String avatarRef) {
        return avatarRef == null
            || avatarRef.isBlank()
            || INITIALS_TOKEN.equalsIgnoreCase(avatarRef.trim());
    }

    public static String normalizeAvatarRef(String avatarRef) {
        return isPresetAvatar(avatarRef) ? avatarRef.trim() : INITIALS_TOKEN;
    }

    public static Image loadAvatarImage(String avatarRef) {
        if (!isPresetAvatar(avatarRef)) {
            return null;
        }

        URL resource = AvatarSupport.class.getResource(avatarRef.trim());
        if (resource == null) {
            return null;
        }

        return new Image(resource.toExternalForm(), true);
    }

    public static String extractInitials(String fullName, String email) {
        String initialsFromName = extractInitials(fullName);
        if (!initialsFromName.isBlank()) {
            return initialsFromName;
        }

        if (email == null || email.isBlank()) {
            return "U";
        }

        String localPart = email.trim();
        int atIndex = localPart.indexOf('@');
        if (atIndex > -1) {
            localPart = localPart.substring(0, atIndex);
        }
        localPart = localPart.replaceAll("[^\\p{L}\\p{Nd}]+", " ").trim();

        String initialsFromEmail = extractInitials(localPart);
        return initialsFromEmail.isBlank() ? "U" : initialsFromEmail;
    }

    private static String extractInitials(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            return "";
        }

        String cleaned = rawValue.replaceAll("[^\\p{L}\\p{Nd}]+", " ").trim();
        if (cleaned.isBlank()) {
            return "";
        }

        String[] parts = cleaned.split("\\s+");
        if (parts.length == 1) {
            String single = parts[0];
            return single.substring(0, Math.min(2, single.length())).toUpperCase(Locale.ROOT);
        }

        String first = parts[0].substring(0, 1);
        String last = parts[parts.length - 1].substring(0, 1);
        return (first + last).toUpperCase(Locale.ROOT);
    }
}
