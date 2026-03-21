package br.com.fiap.app.notificacaoService.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaAgendadaEvent {

    private Long consultaId;
    private String pacienteNome;
    private String medicoNome;
    private LocalDateTime dataHora;
    private String status;
    private String motivo;

}
