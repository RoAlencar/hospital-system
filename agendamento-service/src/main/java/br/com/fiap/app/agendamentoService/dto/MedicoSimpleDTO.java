package br.com.fiap.app.agendamentoService.dto;

import br.com.fiap.app.agendamentoService.enums.Especialidade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicoSimpleDTO {
    private Long id;
    private String nome;
    private String crm;
    private Especialidade especialidade;
}
