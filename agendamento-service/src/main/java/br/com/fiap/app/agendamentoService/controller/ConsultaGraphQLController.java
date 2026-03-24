package br.com.fiap.app.agendamentoService.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import br.com.fiap.app.agendamentoService.dto.ConsultaResponseDTO;
import br.com.fiap.app.agendamentoService.entity.Consulta;
import br.com.fiap.app.agendamentoService.enums.StatusConsulta;
import br.com.fiap.app.agendamentoService.service.ConsultaService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ConsultaGraphQLController {

    private final ConsultaService consultaService;

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO') or (hasAuthority('ROLE_PACIENTE') and @consultaService.isConsultaOwnedByUser(#id, authentication.principal.id))")
    public Consulta consultaById(@Argument Long id) {
        return consultaService.getConsultaById(id);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public List<ConsultaResponseDTO> allConsultas() {
        return consultaService.getAllConsultasDTO();
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public List<ConsultaResponseDTO> consultasByMedico(@Argument Long medicoId) {
        return consultaService.getConsultasByMedicoDTO(medicoId);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO') or (hasAuthority('ROLE_PACIENTE') and @pacienteService.isOwnedByUser(#pacienteId, authentication.principal.id))")
    public List<ConsultaResponseDTO> consultasByPaciente(@Argument Long pacienteId) {
        return consultaService.getConsultasByPacienteDTO(pacienteId);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public List<ConsultaResponseDTO> consultasByStatus(@Argument StatusConsulta status) {
        return consultaService.getConsultasByStatusDTO(status);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public List<ConsultaResponseDTO> consultasByPeriodo(@Argument String inicio, @Argument String fim) {
        return consultaService.getConsultasByPeriodoDTO(
                LocalDateTime.parse(inicio),
                LocalDateTime.parse(fim)
        );
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO') or (hasAuthority('ROLE_PACIENTE') and @pacienteService.isOwnedByUser(#pacienteId, authentication.principal.id))")
    public List<ConsultaResponseDTO> consultasFuturasPorPaciente(@Argument Long pacienteId) {
        return consultaService.getConsultasFuturasPorPacienteDTO(pacienteId);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO') or (hasAuthority('ROLE_PACIENTE') and @pacienteService.isOwnedByUser(#pacienteId, authentication.principal.id))")
    public List<ConsultaResponseDTO> historicoCompletoPaciente(@Argument Long pacienteId) {
        return consultaService.getHistoricoCompletoPacienteDTO(pacienteId);
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public Consulta createConsulta(@Argument Map<String, Object> input) {
        Consulta request = new Consulta();
        request.setMedicoId(Long.valueOf(input.get("medicoId").toString()));
        request.setPacienteId(Long.valueOf(input.get("pacienteId").toString()));
        if (input.get("enfermeiroId") != null) {
            request.setEnfermeiroId(Long.valueOf(input.get("enfermeiroId").toString()));
        }
        request.setDataHora(LocalDateTime.parse(input.get("dataHora").toString()));
        request.setMotivo((String) input.get("motivo"));
        request.setObservacoes((String) input.get("observacoes"));
        return consultaService.createConsulta(request);
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public Consulta updateConsulta(@Argument Long id, @Argument Map<String, Object> input) {
        Consulta request = new Consulta();
        if (input.get("dataHora") != null) {
            request.setDataHora(LocalDateTime.parse(input.get("dataHora").toString()));
        }
        if (input.get("status") != null) {
            request.setStatus(StatusConsulta.valueOf(input.get("status").toString()));
        }
        request.setMotivo((String) input.get("motivo"));
        request.setObservacoes((String) input.get("observacoes"));
        request.setDiagnostico((String) input.get("diagnostico"));
        request.setPrescricao((String) input.get("prescricao"));
        return consultaService.updateConsulta(id, request);
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public Consulta updateStatusConsulta(@Argument Long id, @Argument StatusConsulta status) {
        return consultaService.updateStatusConsulta(id, status);
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public Boolean cancelarConsulta(@Argument Long id, @Argument String motivo) {
        consultaService.cancelarConsulta(id, motivo);
        return true;
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public Boolean deleteConsulta(@Argument Long id) {
        consultaService.deleteConsulta(id);
        return true;
    }
}