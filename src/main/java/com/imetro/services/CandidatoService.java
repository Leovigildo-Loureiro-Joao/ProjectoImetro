package com.imetro.services;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

import com.imetro.domain.dto.candidato.UserRegister;
import com.imetro.domain.dto.progresso.ProgressoAlunoDisciplinaDto;
import com.imetro.domain.enums.NivelDisciplina;
import com.imetro.domain.interfaces.User;
import com.imetro.domain.model.Candidato;
import com.imetro.persistence.repository.ProgressoALunoDisciplinaRepository;
import com.imetro.persistence.repository.UserRepository;

public class CandidatoService implements User {

    private final UserRepository userRepository;
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

    @Override
    public void VerPerfil() {
        throw new UnsupportedOperationException("Unimplemented method 'VerPerfil'");
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

            userRepository.insert(conta.toMap());
            return true;
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
