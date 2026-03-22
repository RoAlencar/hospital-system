package br.com.fiap.app.agendamentoService.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import br.com.fiap.app.agendamentoService.dto.ConsultaInput;
import br.com.fiap.app.agendamentoService.dto.ConsultaResponseDTO;
import br.com.fiap.app.agendamentoService.entity.Consulta;
import br.com.fiap.app.agendamentoService.enums.StatusConsulta;
import br.com.fiap.app.agendamentoService.exception.BusinessException;
import br.com.fiap.app.agendamentoService.mapper.ConsultaMapper;
import br.com.fiap.app.agendamentoService.service.ConsultaService;
import br.com.fiap.app.agendamentoService.service.PacienteService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ConsultaGraphQLController {

    private final ConsultaService consultaService;
    private final PacienteService pacienteService;

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public List<ConsultaResponseDTO> consultas() {
        return consultaService.getAllConsultasDTO();
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO') or hasAuthority('ROLE_PACIENTE')")
    public ConsultaResponseDTO consulta(@Argument Long id) {
        Consulta found = consultaService.getConsultaById(id);
        return ConsultaMapper.toDTO(found);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public List<ConsultaResponseDTO> consultasByMedico(@Argument Long medicoId) {
        return consultaService.getConsultasByMedicoDTO(medicoId);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO') or " +
            "(hasAuthority('ROLE_PACIENTE') and @pacienteService.isOwnedByUser(#pacienteId, authentication.principal.id))")
    public List<ConsultaResponseDTO> consultasByPaciente(@Argument Long pacienteId, Authentication authentication) {
        return consultaService.getConsultasByPacienteDTO(pacienteId);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO') or " +
            "(hasAuthority('ROLE_PACIENTE') and @pacienteService.isOwnedByUser(#pacienteId, authentication.principal.id))")
    public List<ConsultaResponseDTO> consultasFuturasByPaciente(@Argument Long pacienteId, Authentication authentication) {
        return consultaService.getConsultasFuturasPorPacienteDTO(pacienteId);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO') or " +
            "(hasAuthority('ROLE_PACIENTE') and @pacienteService.isOwnedByUser(#pacienteId, authentication.principal.id))")
    public List<ConsultaResponseDTO> historicoCompletoPaciente(@Argument Long pacienteId, Authentication authentication) {
        return consultaService.getHistoricoCompletoPacienteDTO(pacienteId);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public List<ConsultaResponseDTO> consultasByStatus(@Argument StatusConsulta status) {
        return consultaService.getConsultasByStatusDTO(status);
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ConsultaResponseDTO agendarConsulta(@Argument ConsultaInput input) {
        Consulta consulta = new Consulta();
        consulta.setMedicoId(input.medicoId());
        consulta.setPacienteId(input.pacienteId());
        consulta.setEnfermeiroId(input.enfermeiroId());
        try {
            consulta.setDataHora(LocalDateTime.parse(input.dataHora()));
        } catch (DateTimeParseException e) {
            throw new BusinessException("Formato de data inválido. Use o formato ISO-8601: yyyy-MM-ddTHH:mm:ss (ex: 2026-04-01T10:00:00)");
        }
        consulta.setMotivo(input.motivo());
        consulta.setObservacoes(input.observacoes());

        Consulta saved = consultaService.createConsulta(consulta);
        return ConsultaMapper.toDTO(saved);
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ConsultaResponseDTO atualizarStatusConsulta(@Argument Long id, @Argument StatusConsulta status) {
        return ConsultaMapper.toDTO(consultaService.updateStatusConsulta(id, status));
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public Boolean cancelarConsulta(@Argument Long id, @Argument String motivo) {
        consultaService.cancelarConsulta(id, motivo);
        return true;
    }

}