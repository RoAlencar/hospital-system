package br.com.fiap.app.agendamentoService.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import br.com.fiap.app.agendamentoService.dto.MedicoResponseDTO;
import br.com.fiap.app.agendamentoService.dto.PacienteResponseDTO;
import br.com.fiap.app.agendamentoService.enums.Especialidade;
import br.com.fiap.app.agendamentoService.mapper.MedicoMapper;
import br.com.fiap.app.agendamentoService.mapper.PacienteMapper;
import br.com.fiap.app.agendamentoService.service.MedicoService;
import br.com.fiap.app.agendamentoService.service.PacienteService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MedicoGraphQLController {

    private final MedicoService medicoService;
    private final PacienteService pacienteService;

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public List<MedicoResponseDTO> medicos() {
        return medicoService.getAllMedicos().stream()
                .map(MedicoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public MedicoResponseDTO medico(@Argument Long id) {
        return MedicoMapper.toDTO(medicoService.getMedicoById(id));
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public List<MedicoResponseDTO> medicosByEspecialidade(@Argument Especialidade especialidade) {
        return medicoService.getMedicosByEspecialidade(especialidade).stream()
                .map(MedicoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public List<PacienteResponseDTO> pacientes() {
        return pacienteService.getAllPacientes().stream()
                .map(PacienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public PacienteResponseDTO paciente(@Argument Long id) {
        return PacienteMapper.toDTO(pacienteService.getPacienteById(id));
    }
}