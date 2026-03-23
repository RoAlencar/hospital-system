package br.com.fiap.app.agendamentoService.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaAgendadaEvent {
    private Long consultaId;
    private Long pacienteId;
    private String pacienteNome;
    private String medicoNome;
    private LocalDateTime dataHora;
    private String status;
    private String motivo;
}
