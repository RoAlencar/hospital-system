package br.com.fiap.app.agendamentoService.controller;

import br.com.fiap.app.agendamentoService.dto.ConsultaResponseDTO;
import br.com.fiap.app.agendamentoService.entity.Consulta;
import br.com.fiap.app.agendamentoService.enums.StatusConsulta;
import br.com.fiap.app.agendamentoService.service.ConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ConsultaGraphQLController {

    private final ConsultaService consultaService;

    @QueryMapping
    public Consulta consultaById(@Argument Long id) {
        return consultaService.getConsultaById(id);
    }

    @QueryMapping
    public List<ConsultaResponseDTO> allConsultas() {
        return consultaService.getAllConsultasDTO();
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
    public List<ConsultaResponseDTO> consultasByStatus(@Argument StatusConsulta status) {
        return consultaService.getConsultasByStatusDTO(status);
    }

    @QueryMapping
    public List<ConsultaResponseDTO> consultasByPeriodo(@Argument String inicio, @Argument String fim) {
        return consultaService.getConsultasByPeriodoDTO(
                LocalDateTime.parse(inicio),
                LocalDateTime.parse(fim)
        );
    }

    @QueryMapping
    public List<ConsultaResponseDTO> consultasFuturasPorPaciente(@Argument Long pacienteId) {
        return consultaService.getConsultasFuturasPorPacienteDTO(pacienteId);
    }

    @QueryMapping
    public List<ConsultaResponseDTO> historicoCompletoPaciente(@Argument Long pacienteId) {
        return consultaService.getHistoricoCompletoPacienteDTO(pacienteId);
    }

    @MutationMapping
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
    public Consulta updateStatusConsulta(@Argument Long id, @Argument StatusConsulta status) {
        return consultaService.updateStatusConsulta(id, status);
    }

    @MutationMapping
    public Boolean cancelarConsulta(@Argument Long id, @Argument String motivo) {
        consultaService.cancelarConsulta(id, motivo);
        return true;
    }

    @MutationMapping
    public Boolean deleteConsulta(@Argument Long id) {
        consultaService.deleteConsulta(id);
        return true;
    }
}