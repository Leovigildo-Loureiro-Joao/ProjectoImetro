package com.imetro.persistence.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public final class ConfiguracoesRepository extends JdbcBasicSqlRepository {

    private static final int DEFAULT_TEMP_ADAPT_VAL = 20;
    private static final String DEFAULT_TEMP_ADAPT_UNIT = "MINUTOS";
    private static final int DEFAULT_SPEED_TEMP_VAL = 60;
    private static final String DEFAULT_SPEED_TEMP_UNIT = "SEGUNDOS";
    private static final int DEFAULT_LONG_TEST_Q = 10;
    private static final int DEFAULT_NORM_TEST_Q = 7;
    private static final int DEFAULT_DESAF_TEST_Q = 7;
    private static final int DEFAULT_EXTRA_TEST_Q = 5;
    private static final String DEFAULT_NIVEL_DIFICULDADE = "MEDIO";
    private static final String DEFAULT_MODO_ESCOLHAS = "DIAGNOSTICAS";
    private static final int DEFAULT_VELOCIDADE_SEGUNDOS_POR_PERCENT = 120;
    private static final int DEFAULT_RESILIENCIA_REPETICOES_POR_DIA = 2;
    private static final int DEFAULT_PRECISAO_CONSECUTIVAS = 3;
    private static final int DEFAULT_LOGICA_QTD_DESAFIANTE_EXTRA = 2;
    private static final double DEFAULT_CONSISTENCIA_PERCENTUAL_MIN = 70.0d;

    public ConfiguracoesRepository() {
        super("configuracoes", "id");
    }

    public int ensureDefaultsForUserId(UUID userId) throws SQLException {
        if (userId == null) {
            return 0;
        }

        try (Connection conn = openRequiredConnection()) {
            return ensureDefaultsForUser(conn, userId);
        }
    }

    public int ensureDefaultsForUser(Connection conn, UUID userId) throws SQLException {
        if (conn == null || userId == null) {
            return 0;
        }

        String sql = """
            insert into configuracoes (
              user_id,
              temp_adapt_val,
              temp_adapt_unit,
              speed_temp_val,
              speed_temp_unit,
              long_test_q,
              norm_test_q,
              desaf_test_q,
              extra_test_q,
              nivel_dificuldade_padrao,
              modo_escolhas,
              velocidade_segundos_por_percent,
              resiliencia_repeticoes_por_dia,
              precisao_consecutivas,
              logica_qtd_desafiante_extra,
              consistencia_percentual_min
            ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            on conflict (user_id) do nothing
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, userId);
            stmt.setInt(2, DEFAULT_TEMP_ADAPT_VAL);
            stmt.setString(3, DEFAULT_TEMP_ADAPT_UNIT);
            stmt.setInt(4, DEFAULT_SPEED_TEMP_VAL);
            stmt.setString(5, DEFAULT_SPEED_TEMP_UNIT);
            stmt.setInt(6, DEFAULT_LONG_TEST_Q);
            stmt.setInt(7, DEFAULT_NORM_TEST_Q);
            stmt.setInt(8, DEFAULT_DESAF_TEST_Q);
            stmt.setInt(9, DEFAULT_EXTRA_TEST_Q);
            stmt.setString(10, DEFAULT_NIVEL_DIFICULDADE);
            stmt.setString(11, DEFAULT_MODO_ESCOLHAS);
            stmt.setInt(12, DEFAULT_VELOCIDADE_SEGUNDOS_POR_PERCENT);
            stmt.setInt(13, DEFAULT_RESILIENCIA_REPETICOES_POR_DIA);
            stmt.setInt(14, DEFAULT_PRECISAO_CONSECUTIVAS);
            stmt.setInt(15, DEFAULT_LOGICA_QTD_DESAFIANTE_EXTRA);
            stmt.setDouble(16, DEFAULT_CONSISTENCIA_PERCENTUAL_MIN);
            return stmt.executeUpdate();
        }
    }
}
