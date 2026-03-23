package br.com.fiap.app.agendamentoService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    private LocalDate dataNascimento;
    private String endereco;
    private String numeroCartaoSus;
    private String convenioMedico;
    private String contatoEmergencia;
    private String observacoesMedicas;
    private Boolean ativo;
}
