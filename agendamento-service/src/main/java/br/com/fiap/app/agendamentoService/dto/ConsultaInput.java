package br.com.fiap.app.agendamentoService.dto;

public record ConsultaInput(
        Long medicoId,
        Long pacienteId,
        Long enfermeiroId,
        String dataHora,
        String motivo,
        String observacoes) {

}
