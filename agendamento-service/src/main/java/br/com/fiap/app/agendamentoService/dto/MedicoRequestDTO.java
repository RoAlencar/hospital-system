package br.com.fiap.app.agendamentoService.dto;

import br.com.fiap.app.agendamentoService.enums.Especialidade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicoRequestDTO {

    @NotNull(message = "ID do usuário é obrigatório")
    private Long userId;

    @NotBlank(message = "CRM é obrigatório")
    private String crm;

    @NotNull(message = "Especialidade é obrigatória")
    private Especialidade especialidade;

    private String descricao;
}
