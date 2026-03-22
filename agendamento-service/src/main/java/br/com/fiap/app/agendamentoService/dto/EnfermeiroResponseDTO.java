package br.com.fiap.app.agendamentoService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnfermeiroResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String coren;
    private String setor;
    private String turno;
    private String especializacao;
    private String descricao;
    private Boolean ativo;
}
