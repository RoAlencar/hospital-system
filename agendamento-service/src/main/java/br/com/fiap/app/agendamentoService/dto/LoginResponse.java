package br.com.fiap.app.agendamentoService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    
    private String message;
    private Long userId;
    private String username;
    private String role;
}