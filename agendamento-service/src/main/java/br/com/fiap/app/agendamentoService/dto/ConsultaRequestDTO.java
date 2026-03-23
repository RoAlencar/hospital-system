package br.com.fiap.app.agendamentoService.dto;

import br.com.fiap.app.agendamentoService.enums.StatusConsulta;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaRequestDTO {

    @NotNull(message = "ID do médico é obrigatório")
    private Long medicoId;

    @NotNull(message = "ID do paciente é obrigatório")
    private Long pacienteId;

    private Long enfermeiroId;

    @NotNull(message = "Data e hora é obrigatória")
    private LocalDateTime dataHora;

    private StatusConsulta status;
    private String motivo;
    private String observacoes;
    private String diagnostico;
    private String prescricao;
}
