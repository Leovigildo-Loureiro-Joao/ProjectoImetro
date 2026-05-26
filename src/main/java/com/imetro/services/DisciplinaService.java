package com.imetro.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.imetro.domain.dto.diagnostico.DiagnosticoDisciplinaResumo;
import com.imetro.domain.dto.disciplina.DisciplinaDto;
import com.imetro.domain.dto.progresso.ProgressoAlunoDisciplinaDto;
import com.imetro.domain.dto.progresso.ProgressoDisciplinaTeste;
import com.imetro.domain.dto.test.TestDtoAll;
import com.imetro.domain.enums.NivelDisciplina;
import com.imetro.persistence.repository.DisciplinaRepository;
import com.imetro.persistence.repository.ProgressoALunoDisciplinaRepository;
import com.imetro.persistence.repository.TesteRepository;
import com.imetro.util.Authentication;
import com.imetro.util.TextoUtil;

public class DisciplinaService {
    private static final DisciplinaRepository disciplinaRepository=new DisciplinaRepository();
    private static final ProgressoALunoDisciplinaRepository progressoRepository=new ProgressoALunoDisciplinaRepository();
    private static final Set<String> DISCIPLINAS_SUPORTADAS = Set.of("matematica", "fisica");

    public static ArrayList<DisciplinaDto> discCategoria(){
        ArrayList<DisciplinaDto> disc=new ArrayList<>();

        try {
            for (Object elObject : disciplinaRepository.findAll()) {
                if (elObject instanceof LinkedHashMap) {
                    disc.add(ParseDto(elObject));
                }
            }
        } catch (Exception e) {
             System.err.println("Erro ao buscar disciplinas: " + e.getMessage());
        };
        return disc.stream()
            .filter(disciplina -> isDisciplinaSuportada(disciplina.nome()))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public static String findByNomeIdSearch(UUID id){
        return discCategoria().stream()
            .filter(disciplina -> disciplina.id() != null && disciplina.id().equals(id))
            .map(DisciplinaDto::nome)
            .findFirst()
            .orElse("Disciplina");
    }

    public static boolean isDisciplinaSuportada(String nome) {
        return DISCIPLINAS_SUPORTADAS.contains(TextoUtil.normalizarMinusculo(nome));
    }

    public static List<DisciplinaDto> filtrarDisciplinasSuportadas(List<DisciplinaDto> disciplinas) {
        if (disciplinas == null || disciplinas.isEmpty()) {
            return List.of();
        }

        return disciplinas.stream()
            .filter(disciplina -> disciplina != null && isDisciplinaSuportada(disciplina.nome()))
            .toList();
    }

    @SuppressWarnings("unchecked")
    private static DisciplinaDto ParseDto(Object elObject ){
        LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) elObject;
        UUID id = (UUID) map.get("id");
        String nome = (String) map.get("nome");
        Float peso = ((Number) map.get("peso")).floatValue();
        String nivelStr = (String) map.get("nivel");
        String objectivo = (String) map.get("objectivo");
        NivelDisciplina nivelDisciplina=NivelDisciplina.fromDescricao(nivelStr);
        return new DisciplinaDto(id,nome,peso,nivelDisciplina,objectivo);
    }

    public static void associarDisciplinaCandidato(UUID disciplinaId) throws SQLException {
        UUID candidatoId = Authentication.getCurrentUserId();
        progressoRepository.associarDisciplinaCandidato(candidatoId, disciplinaId);
    }

    public static List<ProgressoAlunoDisciplinaDto> getProgressoDisciplinasCandidato() throws SQLException {
        UUID candidatoId = Authentication.getCurrentUserId();
        List<DisciplinaDto> disciplinas = discCategoria();
        List<ProgressoAlunoDisciplinaDto> progressoList = new ArrayList<>();

        for (DisciplinaDto disciplina : disciplinas) {
            ProgressoAlunoDisciplinaDto progresso = progressoRepository.getDto(candidatoId, disciplina.id());
            if (progresso != null) {
                progressoList.add(progresso);
            }
        }
        return progressoList;
    }

    public static List<ProgressoAlunoDisciplinaDto> getProgressoDisciplinasCandidatoSafe() {
        try {
            return getProgressoDisciplinasCandidato();
        } catch (SQLException e) {
            System.err.println("Erro ao carregar progresso das disciplinas: " + e.getMessage());
            return List.of();
        }
    }

    public static List<ProgressoDisciplinaTeste> getDisciplinaTestes() throws SQLException{
        DiagnosticoService diagnosticoService=new DiagnosticoService();
        ArrayList<ProgressoDisciplinaTeste> pdtest = new ArrayList<>();
        List<DiagnosticoDisciplinaResumo> resumos = diagnosticoService.carregarDiagnosticosDisponiveis(Authentication.getCurrentUserId());
        TesteRepository test = new TesteRepository();
        for (ProgressoAlunoDisciplinaDto pDto : getProgressoDisciplinasCandidatoSafe()) {
            DiagnosticoDisciplinaResumo resumo = resumos.stream()
                .filter(item -> item.disciplinaId() != null && item.disciplinaId().equals(pDto.disciplinaId()))
                .findFirst()
                .orElse(null);

            String nomeDisciplina = resumo != null
                ? resumo.nomeDisciplina()
                : (pDto.disciplina() == null || pDto.disciplina().isBlank() ? "Disciplina" : pDto.disciplina());
            double progresso = resumo != null ? resumo.indicador() : pDto.calcularTaxaAcerto();
            double pesoAtual = pDto.pesoAtual() == null ? 1.0d : pDto.pesoAtual();
            float velocidade = 0;
            float consistencia = 0;
            float precisao = 0;
            List<Map<String,Object>> listTest=test.findByCandidatoIdDisciplina(Authentication.getCurrentUserId(),pDto.disciplinaId());
            for (Map<String,Object> tes : listTest) {
                TestDtoAll dto=TestDtoAll.ParseMapDto(tes);
                velocidade += dto.velocidade();
                consistencia += dto.consistencia();
                precisao += dto.precisao();
            }

            int totalTestes = listTest.size();
            float mediaVelocidade = totalTestes == 0 ? 0f : velocidade / totalTestes;
            float mediaConsistencia = totalTestes == 0 ? 0f : consistencia / totalTestes;
            float mediaPrecisao = totalTestes == 0 ? 0f : precisao / totalTestes;

            pdtest.add(new ProgressoDisciplinaTeste(
                nomeDisciplina,
                progresso,
                pesoAtual,
                pDto.nivelAtual(),
                mediaVelocidade,
                mediaConsistencia,
                mediaPrecisao
            ));
        }
        return pdtest;
    }

    public static ProgressoAlunoDisciplinaDto getDisciplinaCandidato(UUID disciplinaId) throws SQLException {
        UUID candidatoId = Authentication.getCurrentUserId();
        return progressoRepository.getDto(candidatoId, disciplinaId);
    }

    public static ProgressoAlunoDisciplinaDto getDisciplinaCandidato(String disciplinaNNome) throws SQLException {
        if (disciplinaNNome == null || disciplinaNNome.isBlank()) {
            return null;
        }

        String disciplinaNormalizada = TextoUtil.normalizarMinusculo(disciplinaNNome);
        UUID disciplinaId = discCategoria().stream()
            .filter(disciplina -> disciplina != null && disciplina.id() != null)
            .filter(disciplina -> TextoUtil.normalizarMinusculo(disciplina.nome()).equals(disciplinaNormalizada))
            .map(DisciplinaDto::id)
            .findFirst()
            .orElse(null);

        if (disciplinaId != null) {
            return getDisciplinaCandidato(disciplinaId);
        }

        UUID candidatoId = Authentication.getCurrentUserId();
        return progressoRepository.getDto(candidatoId, disciplinaNNome);
    }


}
