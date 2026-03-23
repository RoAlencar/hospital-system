package br.com.fiap.app.agendamentoService.dto;

import br.com.fiap.app.agendamentoService.enums.StatusConsulta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaResponseDTO {
    private Long id;
    private MedicoSimpleDTO medico;
    private PacienteSimpleDTO paciente;
    private EnfermeiroSimpleDTO enfermeiro;
    private LocalDateTime dataHora;
    private StatusConsulta status;
    private String motivo;
    private String observacoes;
    private String diagnostico;
    private String prescricao;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAlteracao;
}
