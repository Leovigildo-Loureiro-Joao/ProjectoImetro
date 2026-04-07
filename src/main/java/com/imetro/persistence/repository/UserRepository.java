package com.imetro.persistence.repository;

import java.sql.SQLException;
import java.util.UUID;

public final class UserRepository extends JdbcBasicSqlRepository {

    public UserRepository() {
        super("users", "id");
    }

    public UUID getIdByEmail(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";
        try (var conn = openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Object value = rs.getObject("id");
                    if (value instanceof UUID uuid) {
                        return uuid;
                    }
                    if (value != null) {
                        return UUID.fromString(value.toString());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPasswordHashByEmail(String email) {
        String sql = "SELECT senha_hash FROM users WHERE email = ?";
        try (var conn = openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("senha_hash");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getRoleByEmail(String email) {
        String sql = "SELECT role FROM users WHERE email = ?";
        try (var conn = openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getAvatarUrlByEmail(String email) {
        String sql = "SELECT avatar_url FROM users WHERE email = ?";
        try (var conn = openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("avatar_url");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateAvatarUrlByEmail(String email, String avatarUrl) {
        String sql = "UPDATE users SET avatar_url = ? WHERE email = ?";
        try (var conn = openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, avatarUrl);
            stmt.setString(2, email);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return false;
    }

}
