package br.com.fiap.app.agendamentoService.controller;

import br.com.fiap.app.agendamentoService.entity.Medico;
import br.com.fiap.app.agendamentoService.enums.Especialidade;
import br.com.fiap.app.agendamentoService.service.MedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MedicoGraphQLController {

    private final MedicoService medicoService;

    @QueryMapping
    public Medico medicoById(@Argument Long id) {
        return medicoService.getMedicoById(id);
    }

    @QueryMapping
    public List<Medico> allMedicos() {
        return medicoService.getAllMedicos();
    }

    @QueryMapping
    public List<Medico> medicosByEspecialidade(@Argument Especialidade especialidade) {
        return medicoService.getMedicosByEspecialidade(especialidade);
    }

    @QueryMapping
    public List<Medico> medicosAtivos() {
        return medicoService.getActiveMedicos();
    }
}