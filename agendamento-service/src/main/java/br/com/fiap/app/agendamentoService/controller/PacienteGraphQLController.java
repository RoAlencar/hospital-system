package br.com.fiap.app.agendamentoService.controller;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import br.com.fiap.app.agendamentoService.entity.Paciente;
import br.com.fiap.app.agendamentoService.service.PacienteService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PacienteGraphQLController {

    private final PacienteService pacienteService;

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public Paciente pacienteById(@Argument Long id) {
        return pacienteService.getPacienteById(id);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public List<Paciente> allPacientes() {
        return pacienteService.getAllPacientes();
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public List<Paciente> pacientesAtivos() {
        return pacienteService.getActivePacientes();
    }
}