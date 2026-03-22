package br.com.fiap.app.agendamentoService.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import br.com.fiap.app.agendamentoService.dto.ConsultaInput;
import br.com.fiap.app.agendamentoService.dto.ConsultaResponseDTO;
import br.com.fiap.app.agendamentoService.entity.Consulta;
import br.com.fiap.app.agendamentoService.enums.StatusConsulta;
import br.com.fiap.app.agendamentoService.mapper.ConsultaMapper;
import br.com.fiap.app.agendamentoService.service.ConsultaService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ConsultaGraphQLController {

    private final ConsultaService consultaService;

    @QueryMapping
    public List<ConsultaResponseDTO> consultas() {
        return consultaService.getAllConsultasDTO();
    }

    @QueryMapping
    public ConsultaResponseDTO consulta(@Argument Long id) {
        return consultaService.getAllConsultasDTO().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @QueryMapping
    public List<ConsultaResponseDTO> consultasByMedico(@Argument Long medicoId) {
        return consultaService.getConsultasByMedicoDTO(medicoId);
    }

    @QueryMapping
    public List<ConsultaResponseDTO> consultasByPaciente(@Argument Long pacienteId) {
        return consultaService.getConsultasByPacienteDTO(pacienteId);
    }

    @QueryMapping
    public List<ConsultaResponseDTO> consultasFuturasByPaciente(@Argument Long pacienteId) {
        return consultaService.getConsultasFuturasPorPacienteDTO(pacienteId);
    }

    @QueryMapping
    public List<ConsultaResponseDTO> historicoCompletoPaciente(@Argument Long pacienteId) {
        return consultaService.getHistoricoCompletoPacienteDTO(pacienteId);
    }

    @QueryMapping
    public List<ConsultaResponseDTO> consultasByStatus(@Argument StatusConsulta status) {
        return consultaService.getConsultasByStatusDTO(status);
    }

    @MutationMapping
    public ConsultaResponseDTO agendarConsulta(@Argument ConsultaInput input) {
        Consulta consulta = new Consulta();
        consulta.setMedicoId(input.medicoId());
        consulta.setPacienteId(input.pacienteId());
        consulta.setEnfermeiroId(input.enfermeiroId());
        consulta.setDataHora(LocalDateTime.parse(input.dataHora())); // expects ISO: "2026-04-01T10:00:00"
        consulta.setMotivo(input.motivo());
        consulta.setObservacoes(input.observacoes());

        Consulta saved = consultaService.createConsulta(consulta);
        return ConsultaMapper.toDTO(saved);
    }

    @MutationMapping
    public ConsultaResponseDTO atualizarStatusConsulta(@Argument Long id, @Argument StatusConsulta status) {
        return ConsultaMapper.toDTO(consultaService.updateStatusConsulta(id, status));
    }

    @MutationMapping
    public Boolean cancelarConsulta(@Argument Long id, @Argument String motivo) {
        consultaService.cancelarConsulta(id, motivo);
        return true;
    }

}