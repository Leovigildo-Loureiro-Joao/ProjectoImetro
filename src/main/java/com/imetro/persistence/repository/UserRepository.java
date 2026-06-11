package com.imetro.persistence.repository;

import com.imetro.domain.dto.candidato.UserRegister;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public final class UserRepository extends JdbcBasicSqlRepository {

    private final ConfiguracoesRepository configuracoesRepository = new ConfiguracoesRepository();

    public UserRepository() {
        super("users", "id");
    }

    public boolean insertWithDefaultConfig(UserRegister register) throws SQLException {
        if (register == null || !register.ValidateData()) {
            return false;
        }

        String sql = """
            insert into users (nome, email, senha_hash, role, criado_em)
            values (?, ?, ?, ?, ?)
            returning id
            """;

        try (Connection conn = openRequiredConnection()) {
            boolean previousAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try {
                UUID userId;
                try (var stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, register.nome());
                    stmt.setString(2, register.email());
                    stmt.setString(3, register.senha_hash());
                    stmt.setString(4, register.role().toUpperCase());
                    stmt.setTimestamp(5, Timestamp.valueOf(resolveCreatedAt(register.criado_em())));

                    try (var rs = stmt.executeQuery()) {
                        if (!rs.next()) {
                            conn.rollback();
                            return false;
                        }
                        userId = extractUuid(rs.getObject("id"));
                    }
                }

                if (userId == null) {
                    conn.rollback();
                    return false;
                }

                configuracoesRepository.ensureDefaultsForUser(conn, userId);
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(previousAutoCommit);
            }
        }
    }


    public boolean insertFocos(String focos,UUID user) throws SQLException {
        if ((focos == null&&user == null) || !focos.trim().isEmpty()) {
            return false;
        }

        String sql = """
            update users set foco = ? where id = ?
            """;

        try (Connection conn = openRequiredConnection()) {
            boolean previousAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try {
                try (var stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, focos);
                    stmt.setObject(2, user);

                    try (var rs = stmt.executeQuery()) {
                        if (!rs.next()) {
                            conn.rollback();
                            return false;
                        }
                    }
                }
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(previousAutoCommit);
            }
        }
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

    public String getNomeByEmail(String email) {
        String sql = "SELECT nome FROM users WHERE email = ?";
        try (var conn = openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nome");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
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

    public boolean updateNomeByEmail(String email, String nome) {
        String sql = "UPDATE users SET nome = ? WHERE email = ?";
        try (var conn = openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, email);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePasswordHashByEmail(String email, String passwordHash) {
        String sql = "UPDATE users SET senha_hash = ? WHERE email = ?";
        try (var conn = openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, passwordHash);
            stmt.setString(2, email);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static UUID extractUuid(Object value) {
        if (value instanceof UUID uuid) {
            return uuid;
        }
        if (value == null) {
            return null;
        }
        try {
            return UUID.fromString(value.toString());
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    private static LocalDateTime resolveCreatedAt(LocalDateTime createdAt) {
        return createdAt == null ? LocalDateTime.now() : createdAt;
    }

}
