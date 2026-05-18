package com.imetro.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.imetro.domain.dto.candidato.UserRegister;
import com.imetro.domain.dto.progresso.ProgressoAlunoDisciplinaDto;
import com.imetro.domain.dto.stats.Stats;
import com.imetro.domain.dto.stats.Teste_Stat;
import com.imetro.domain.enums.NivelDisciplina;
import com.imetro.domain.interfaces.User;
import com.imetro.domain.model.Candidato;
import com.imetro.persistence.repository.JdbcBasicSqlRepository;
import com.imetro.persistence.repository.ProgressoALunoDisciplinaRepository;
import com.imetro.persistence.repository.TesteStatsRepository;
import com.imetro.persistence.repository.UserRepository;
import com.imetro.ui.components.ResultData;
import com.imetro.util.Authentication;

public class CandidatoService implements User {

    private final UserRepository userRepository;
    private final TesteStatsRepository testeStatsRepository=new TesteStatsRepository();
    private ProgressoALunoDisciplinaRepository progresso;

    public CandidatoService() {
        userRepository = new UserRepository();
        progresso = new ProgressoALunoDisciplinaRepository();
    }

    @Override
    public void Login() {
        throw new UnsupportedOperationException("Unimplemented method 'Login'");
    }

    @Override
    public void Logout() {
        throw new UnsupportedOperationException("Unimplemented method 'Logout'");
    }

    @Override
    public void RemoverConta() {
        throw new UnsupportedOperationException("Unimplemented method 'RemoverConta'");
    }

    @Override
    public void VerRelatorios() {
        throw new UnsupportedOperationException("Unimplemented method 'VerRelatorios'");
    }

    public List<ResultData> ListarResultados(){
        UUID candidatoId = Authentication.getCurrentUserId();
        if (candidatoId == null) {
            return List.of();
        }

        String sql = """
            with historico as (
              select
                'TESTE' as origem,
                coalesce(data_teste, criado_em) as evento_em,
                coalesce(resultado, percentual_acerto, 0) as score
              from testes
              where candidato_id = ?

              union all

              select
                'DIAGNOSTICO' as origem,
                coalesce(concluido_em, iniciado_em, criado_em) as evento_em,
                coalesce(percentual_acerto, 0) as score
              from diagnosticos
              where candidato_id = ?
            ),
            ordenado as (
              select
                origem,
                evento_em,
                score,
                lag(score) over (order by evento_em asc, origem asc) as score_anterior
              from historico
            )
            select origem, evento_em, score, score_anterior
            from ordenado
            order by evento_em desc, origem desc
            limit 4
            """;

        ArrayList<ResultData> resultados = new ArrayList<>();
        try (Connection conn = JdbcBasicSqlRepository.openRequiredConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, candidatoId);
            stmt.setObject(2, candidatoId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String origem = asText(rs.getObject("origem"));
                    LocalDateTime eventoEm = mapearDataHora(rs.getObject("evento_em"));
                    double score = parseDouble(rs.getObject("score"));
                    Double scoreAnterior = parseNullableDouble(rs.getObject("score_anterior"));

                    resultados.add(new ResultData(
                        resolverTituloResultado(origem),
                        formatarPercentual(score),
                        eventoEm == null ? LocalDate.now() : eventoEm.toLocalDate(),
                        classificarResultado(score),
                        formatarVariacao(score, scoreAnterior)
                    ));
                }
            }
        } catch (SQLException | RuntimeException e) {
            System.err.println("Erro ao listar resultados do candidato: " + e.getMessage());
            return List.of();
        }

        return List.copyOf(resultados);
    }


    public Stats CalcularStats(){
        UUID candidatoId = Authentication.getCurrentUserId();
        if (candidatoId == null) {
            return new Stats(0.0, 0.0, 0.0, 0.0, 0.0);
        }

        double velocidade = 0.0;
        double precisao = 0.0;
        double consistencia = 0.0;
        double logica = 0.0;
        double resiliencia = 0.0;
        int qtde = 0;
        try {
            List<Map<String, Object>> rows = testeStatsRepository.findByCandidatoId(candidatoId);
            for (Map<String, Object> row : rows) {
                Teste_Stat stats = Teste_Stat.ParseDto(row);
                velocidade += normalizarStats(stats.velocidade());
                precisao += normalizarStats(stats.precisao());
                consistencia += normalizarStats(stats.consistencia());
                logica += normalizarStats(stats.logica());
                resiliencia += normalizarStats(stats.resiliencia());
                qtde++;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao calcular stats do candidato: " + e.getMessage());
        }

        return new Stats(
            qtde > 0 ? velocidade / qtde : 0.0,
            qtde > 0 ? precisao / qtde : 0.0,
            qtde > 0 ? consistencia / qtde : 0.0,
            qtde > 0 ? logica / qtde : 0.0,
            qtde > 0 ? resiliencia / qtde : 0.0
        );
    }

    private double normalizarStats(Double valor) {
        if (valor == null) {
            return 0.0;
        }
        return Math.max(0.0, Math.min(1.0, valor));
    }

    private String resolverTituloResultado(String origem) {
        if ("DIAGNOSTICO".equalsIgnoreCase(origem)) {
            return "Diagnóstico Rápido";
        }
        return "Exame Adaptativo";
    }

    private String classificarResultado(double score) {
        if (score >= 85.0) {
            return "excelente";
        }
        if (score >= 70.0) {
            return "bom";
        }
        return "regular";
    }

    private String formatarPercentual(double valor) {
        long percentual = Math.round(Math.max(0.0, Math.min(100.0, valor)));
        return percentual + "%";
    }

    private String formatarVariacao(double scoreAtual, Double scoreAnterior) {
        if (scoreAnterior == null) {
            return "0%";
        }

        long variacao = Math.round(scoreAtual - scoreAnterior);
        if (variacao > 0) {
            return "+" + variacao + "%";
        }
        return variacao + "%";
    }

    private Double parseNullableDouble(Object value) {
        if (value == null) {
            return null;
        }
        return parseDouble(value);
    }

    private double parseDouble(Object value) {
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        String text = asText(value);
        if (text == null) {
            return 0.0;
        }

        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException ignored) {
            return 0.0;
        }
    }


    public Candidato getCandidatoById(UUID id) {
        try {
            Map<String, Object> map = userRepository.findById(id).orElse(null);
            if (map == null) {
                return null;
            }

            UUID candidatoId = map.get("id") instanceof UUID uuid ? uuid : id;
            String nome = asText(map.get("nome"));
            String email = asText(map.get("email"));
            String senhaHash = asText(map.get("senha_hash"));
            LocalDateTime criadoEm = mapearDataHora(map.get("criado_em"));

            if (candidatoId == null || nome == null || nome.isBlank() || email == null || email.isBlank()) {
                return null;
            }

            return new Candidato(
                candidatoId,
                nome,
                email,
                senhaHash,
                criadoEm == null ? LocalDateTime.now() : criadoEm
            );
        } catch (SQLException | RuntimeException e) {
            System.err.println("Erro ao buscar candidato: " + e.getMessage());
            return null;
        }
    }

    private String asText(Object value) {
        if (value == null) {
            return null;
        }
        String text = value.toString();
        return text.isBlank() ? null : text;
    }

    private LocalDateTime mapearDataHora(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof LocalDateTime dateTime) {
            return dateTime;
        }
        if (value instanceof OffsetDateTime offsetDateTime) {
            return offsetDateTime.toLocalDateTime();
        }
        if (value instanceof Timestamp timestamp) {
            return timestamp.toLocalDateTime();
        }

        String text = asText(value);
        if (text == null) {
            return null;
        }

        try {
            return LocalDateTime.parse(text);
        } catch (RuntimeException ignored) {
        }

        try {
            return OffsetDateTime.parse(text).toLocalDateTime();
        } catch (RuntimeException ignored) {
        }

        return null;
    }

    @Override
    public boolean CriarConta(UserRegister conta) {
        try {
            if (conta == null || !conta.ValidateData()) {
                return false;
            }
            if (!"CANDIDATO".equalsIgnoreCase(conta.role())) {
                return false;
            }

            return userRepository.insertWithDefaultConfig(conta);
        } catch (SQLException e) {
            return false;
        }
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public ProgressoALunoDisciplinaRepository getProgresso() {
        return progresso;
    }

    public void setProgresso(ProgressoALunoDisciplinaRepository progresso) {
        this.progresso = progresso;
    }

    public void AddFirstProgressoDisciplina(UUID candidato, UUID disicplina, NivelDisciplina actual, double peso) {
        try {
            progresso.insert(
                new ProgressoAlunoDisciplinaDto(
                    UUID.randomUUID(),
                    candidato,
                    disicplina,
                    null,
                    0f,
                    actual,
                    actual,
                    LocalDate.now(),
                    peso,
                    0,
                    0,
                    0,
                    0.0,
                    null,
                    null,
                    LocalDateTime.now(),
                    0,
                    0,
                    LocalDateTime.now(),
                    LocalDateTime.now()
                ).toMap()
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
