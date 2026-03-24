package br.com.fiap.app.agendamentoService.controller;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import br.com.fiap.app.agendamentoService.entity.Medico;
import br.com.fiap.app.agendamentoService.enums.Especialidade;
import br.com.fiap.app.agendamentoService.service.MedicoService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MedicoGraphQLController {

    private final MedicoService medicoService;

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public Medico medicoById(@Argument Long id) {
        return medicoService.getMedicoById(id);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public List<Medico> allMedicos() {
        return medicoService.getAllMedicos();
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public List<Medico> medicosByEspecialidade(@Argument Especialidade especialidade) {
        return medicoService.getMedicosByEspecialidade(especialidade);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public List<Medico> medicosAtivos() {
        return medicoService.getActiveMedicos();
    }
}