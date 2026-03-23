package br.com.fiap.app.agendamentoService.controller;

import br.com.fiap.app.agendamentoService.entity.Enfermeiro;
import br.com.fiap.app.agendamentoService.service.EnfermeiroService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class EnfermeiroGraphQLController {

    private final EnfermeiroService enfermeiroService;

    @QueryMapping
    public Enfermeiro enfermeiroById(@Argument Long id) {
        return enfermeiroService.getEnfermeiroById(id);
    }

    @QueryMapping
    public List<Enfermeiro> allEnfermeiros() {
        return enfermeiroService.getAllEnfermeiros();
    }

    @QueryMapping
    public List<Enfermeiro> enfermeirosAtivos() {
        return enfermeiroService.getActiveEnfermeiros();
    }

    @QueryMapping
    public List<Enfermeiro> enfermeirosBySetor(@Argument String setor) {
        return enfermeiroService.getEnfermeirosBySetor(setor);
    }

    @QueryMapping
    public List<Enfermeiro> enfermeirosByTurno(@Argument String turno) {
        return enfermeiroService.getEnfermeirosByTurno(turno);
    }
}