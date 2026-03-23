package br.com.fiap.app.agendamentoService.dto;

import br.com.fiap.app.agendamentoService.enums.Especialidade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicoResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String crm;
    private Especialidade especialidade;
    private String descricao;
    private Boolean ativo;
}
