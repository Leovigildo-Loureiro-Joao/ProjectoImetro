package com.imetro.util;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public final class AvatarSupport {

    private static final String AVATAR_RESOURCE_DIR = "/com/imetro/assets/imgs/avatar/";
    public static final String INITIALS_TOKEN = "INITIALS";
    private static final String[] AVAILABLE_AVATARS = discoverAvailableAvatars();
    private static final List<String> AVAILABLE_AVATAR_LIST = List.of(AVAILABLE_AVATARS);
    public static final int PRESET_AVATAR_COUNT = AVAILABLE_AVATAR_LIST.size();

    private AvatarSupport() {
    }

    public static List<String> availableAvatars() {
        return AVAILABLE_AVATAR_LIST;
    }

    public static boolean isPresetAvatar(String avatarRef) {
        if (avatarRef == null || avatarRef.isBlank()) {
            return false;
        }
        return AVAILABLE_AVATAR_LIST.contains(avatarRef.trim());
    }

    public static boolean usesInitials(String avatarRef) {
        return avatarRef == null
            || avatarRef.isBlank()
            || INITIALS_TOKEN.equalsIgnoreCase(avatarRef.trim());
    }

    public static String normalizeAvatarRef(String avatarRef) {
        return isPresetAvatar(avatarRef) ? avatarRef.trim() : INITIALS_TOKEN;
    }

    public static String previewFallbackLabel(String avatarRef, String fullName, String email) {
        if (isPresetAvatar(avatarRef)) {
            String number = avatarNumber(avatarRef);
            if (!number.isBlank()) {
                return number;
            }
        }

        return extractInitials(fullName, email);
    }

    public static String avatarNumber(String avatarRef) {
        if (avatarRef == null || avatarRef.isBlank()) {
            return "";
        }

        String trimmed = avatarRef.trim();
        int slashIndex = Math.max(trimmed.lastIndexOf('/'), trimmed.lastIndexOf('\\'));
        if (slashIndex >= 0 && slashIndex + 1 < trimmed.length()) {
            trimmed = trimmed.substring(slashIndex + 1);
        }

        int dotIndex = trimmed.lastIndexOf('.');
        if (dotIndex > 0) {
            trimmed = trimmed.substring(0, dotIndex);
        }

        String digits = trimmed.replaceAll("\\D+", "");
        if (!digits.isBlank()) {
            return digits;
        }

        return "";
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

    public static StackPane createAvatarOption(String avatarRef, double cardSize, double imageSize, EventHandler<MouseEvent> onClick) {
        StackPane option = new StackPane();
        option.getStyleClass().add("avatar-option");
        option.setPrefSize(cardSize, cardSize);
        option.setMinSize(cardSize, cardSize);
        option.setMaxSize(cardSize, cardSize);
        option.setUserData(avatarRef);

        if (onClick != null) {
            option.setOnMouseClicked(onClick);
        }

        Image image = loadAvatarImage(avatarRef);
        if (image != null) {
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(imageSize);
            imageView.setFitWidth(imageSize);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            option.getChildren().add(imageView);
        } else {
            VBox placeholder = new VBox(2);
            placeholder.setAlignment(Pos.CENTER);

            Label numberLabel = new Label(avatarNumber(avatarRef));
            numberLabel.getStyleClass().addAll("avatar-initials", "avatar-initials-medium");

            Label caption = new Label("avatar");
            caption.getStyleClass().add("avatar-option-caption");

            placeholder.getChildren().addAll(numberLabel, caption);
            option.getChildren().add(placeholder);
            option.getStyleClass().add("avatar-option-placeholder");
        }

        return option;
    }

    private static String[] discoverAvailableAvatars() {
        URL directoryUrl = AvatarSupport.class.getResource(AVATAR_RESOURCE_DIR);
        if (directoryUrl == null) {
            return new String[0];
        }

        try {
            URI directoryUri = directoryUrl.toURI();
            if ("jar".equalsIgnoreCase(directoryUri.getScheme())) {
                return discoverFromJar(directoryUri);
            }
            Path directory = Paths.get(directoryUri);
            return discoverFromDirectory(directory);
        } catch (Exception e) {
            System.err.println("Nao foi possivel ler os avatares da pasta: " + e.getMessage());
            return new String[0];
        }
    }

    private static String[] discoverFromDirectory(Path directory) throws IOException {
        if (directory == null || Files.notExists(directory) || !Files.isDirectory(directory)) {
            return new String[0];
        }

        try (Stream<Path> stream = Files.list(directory)) {
            return stream
                .filter(Files::isRegularFile)
                .filter(AvatarSupport::isSupportedAvatarFile)
                .sorted(AvatarSupport::compareAvatarFiles)
                .map(path -> AVATAR_RESOURCE_DIR + path.getFileName().toString())
                .toArray(String[]::new);
        }
    }

    private static String[] discoverFromJar(URI directoryUri) throws IOException {
        String rawUri = directoryUri.toString();
        int separator = rawUri.indexOf("!/");
        if (separator < 0) {
            return new String[0];
        }

        URI jarUri = URI.create(rawUri.substring(0, separator));
        String directoryPart = rawUri.substring(separator + 2);

        FileSystem fileSystem = null;
        boolean created = false;
        try {
            try {
                fileSystem = FileSystems.newFileSystem(jarUri, Map.of());
                created = true;
            } catch (FileSystemAlreadyExistsException ignored) {
                fileSystem = FileSystems.getFileSystem(jarUri);
            }

            Path directory = fileSystem.getPath("/" + directoryPart);
            return discoverFromDirectory(directory);
        } finally {
            if (created && fileSystem != null && fileSystem.isOpen()) {
                fileSystem.close();
            }
        }
    }

    private static boolean isSupportedAvatarFile(Path path) {
        if (path == null || path.getFileName() == null) {
            return false;
        }

        String fileName = path.getFileName().toString().toLowerCase(Locale.ROOT);
        return fileName.endsWith(".png")
            || fileName.endsWith(".jpg")
            || fileName.endsWith(".jpeg")
            || fileName.endsWith(".gif")
            || fileName.endsWith(".webp");
    }

    private static int compareAvatarFiles(Path left, Path right) {
        String leftName = left.getFileName().toString();
        String rightName = right.getFileName().toString();

        int sortKeyCompare = Integer.compare(extractAvatarSortKey(leftName), extractAvatarSortKey(rightName));
        if (sortKeyCompare != 0) {
            return sortKeyCompare;
        }

        return String.CASE_INSENSITIVE_ORDER.compare(leftName, rightName);
    }

    private static int extractAvatarSortKey(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return Integer.MAX_VALUE;
        }

        String digits = fileName.replaceAll("\\D+", "");
        if (digits.isBlank()) {
            return Integer.MAX_VALUE;
        }

        try {
            return Integer.parseInt(digits);
        } catch (NumberFormatException ignored) {
            return Integer.MAX_VALUE;
        }
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
