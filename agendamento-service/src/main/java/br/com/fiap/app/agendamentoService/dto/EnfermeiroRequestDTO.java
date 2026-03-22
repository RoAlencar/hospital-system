package br.com.fiap.app.agendamentoService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnfermeiroRequestDTO {

    @NotNull(message = "ID do usuário é obrigatório")
    private Long userId;

    @NotBlank(message = "COREN é obrigatório")
    private String coren;

    private String setor;
    private String turno;
    private String especializacao;
    private String descricao;
}
