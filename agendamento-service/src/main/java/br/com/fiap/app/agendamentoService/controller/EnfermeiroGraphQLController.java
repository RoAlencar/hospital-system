package br.com.fiap.app.agendamentoService.controller;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import br.com.fiap.app.agendamentoService.entity.Enfermeiro;
import br.com.fiap.app.agendamentoService.service.EnfermeiroService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class EnfermeiroGraphQLController {

    private final EnfermeiroService enfermeiroService;

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public Enfermeiro enfermeiroById(@Argument Long id) {
        return enfermeiroService.getEnfermeiroById(id);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public List<Enfermeiro> allEnfermeiros() {
        return enfermeiroService.getAllEnfermeiros();
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public List<Enfermeiro> enfermeirosAtivos() {
        return enfermeiroService.getActiveEnfermeiros();
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public List<Enfermeiro> enfermeirosBySetor(@Argument String setor) {
        return enfermeiroService.getEnfermeirosBySetor(setor);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public List<Enfermeiro> enfermeirosByTurno(@Argument String turno) {
        return enfermeiroService.getEnfermeirosByTurno(turno);
    }
}