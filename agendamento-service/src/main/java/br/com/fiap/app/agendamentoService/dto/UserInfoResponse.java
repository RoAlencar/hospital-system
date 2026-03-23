package br.com.fiap.app.agendamentoService.dto;

import br.com.fiap.app.agendamentoService.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    
    private Long id;
    private String username;
    private String nome;
    private String email;
    private String telefone;
    private Role role;
    private Boolean active;
}