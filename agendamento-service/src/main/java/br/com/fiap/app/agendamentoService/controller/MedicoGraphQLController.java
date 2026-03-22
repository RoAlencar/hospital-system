package br.com.fiap.app.agendamentoService.controller;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import br.com.fiap.app.agendamentoService.entity.Medico;
import br.com.fiap.app.agendamentoService.entity.Paciente;
import br.com.fiap.app.agendamentoService.enums.Especialidade;
import br.com.fiap.app.agendamentoService.service.MedicoService;
import br.com.fiap.app.agendamentoService.service.PacienteService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MedicoGraphQLController {

    private final MedicoService medicoService;
    private final PacienteService pacienteService;

    @QueryMapping
    public List<Medico> medicos() {
        return medicoService.getAllMedicos();
    }

    @QueryMapping
    public Medico medico(@Argument Long id) {
        return medicoService.getMedicoById(id);
    }

    @QueryMapping
    public List<Medico> medicosByEspecialidade(@Argument Especialidade especialidade) {
        return medicoService.getMedicosByEspecialidade(especialidade);
    }

    @SchemaMapping(typeName = "MedicoType", field = "nome")
    public String nome(Medico medico) {
        return medico.getUser() != null ? medico.getUser().getNome() : null;
    }

    @QueryMapping
    public List<Paciente> pacientes() {
        return pacienteService.getAllPacientes();
    }

    @QueryMapping
    public Paciente paciente(@Argument Long id) {
        return pacienteService.getPacienteById(id);
    }

    @SchemaMapping(typeName = "PacienteType", field = "nome")
    public String pacienteNome(Paciente paciente) {
        return paciente.getUser() != null ? paciente.getUser().getNome() : null;
    }

    @SchemaMapping(typeName = "PacienteType", field = "dataNascimento")
    public String dataNascimento(Paciente paciente) {
        return paciente.getDataNascimento() != null
                ? paciente.getDataNascimento().toString()
                : null;
    }
}