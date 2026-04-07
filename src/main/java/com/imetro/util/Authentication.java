package com.imetro.util;

import com.imetro.domain.interfaces.User;
import com.imetro.persistence.repository.UserRepository;

public class Authentication {
    private static String currentUserId;
    private static String currentUserEmail;
    private static String currentUserRole;
    private static UserRepository userRepository;

    public static boolean login(String email, String password) {
        currentUserEmail = email;
        userRepository = new UserRepository();
        String storedHash = userRepository.getPasswordHashByEmail(email);
        String providedHash = PasswordHasher.sha256Base64(password);
        if (storedHash != null && storedHash.equals(providedHash)) {
            currentUserRole = userRepository.getRoleByEmail(email);
            var id = userRepository.getIdByEmail(email);
            currentUserId = id == null ? null : id.toString();
            return true;
        } else {
            currentUserId = null;
            currentUserEmail = null;
            currentUserRole = null;
            return false;
        }
    }

    public static void logout() {
        currentUserId = null;
        currentUserEmail = null;
        currentUserRole = null;
    }

    public static boolean isAuthenticated() {
        return currentUserEmail != null && !currentUserEmail.isBlank();
    }

    public static String getCurrentUserEmail() {
        return currentUserEmail;
    }

    public static String getCurrentUserId() {
        return currentUserId;
    }

    public static String getCurrentUserRole() {
        return currentUserRole;
    }
}
