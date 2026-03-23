package br.com.fiap.app.agendamentoService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnfermeiroSimpleDTO {
    private Long id;
    private String nome;
    private String coren;
}
