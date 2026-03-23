package br.com.fiap.app.agendamentoService.controller;

import br.com.fiap.app.agendamentoService.entity.Paciente;
import br.com.fiap.app.agendamentoService.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PacienteGraphQLController {

    private final PacienteService pacienteService;

    @QueryMapping
    public Paciente pacienteById(@Argument Long id) {
        return pacienteService.getPacienteById(id);
    }

    @QueryMapping
    public List<Paciente> allPacientes() {
        return pacienteService.getAllPacientes();
    }

    @QueryMapping
    public List<Paciente> pacientesAtivos() {
        return pacienteService.getActivePacientes();
    }
}