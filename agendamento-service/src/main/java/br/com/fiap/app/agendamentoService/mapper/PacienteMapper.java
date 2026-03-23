package br.com.fiap.app.agendamentoService.mapper;

import br.com.fiap.app.agendamentoService.dto.PacienteRequestDTO;
import br.com.fiap.app.agendamentoService.dto.PacienteResponseDTO;
import br.com.fiap.app.agendamentoService.entity.Paciente;

public class PacienteMapper {

    public static PacienteResponseDTO toDTO(Paciente paciente) {
        if (paciente == null) return null;
        String nome = paciente.getUser() != null ? paciente.getUser().getNome() : null;
        String email = paciente.getUser() != null ? paciente.getUser().getEmail() : null;
        String telefone = paciente.getUser() != null ? paciente.getUser().getTelefone() : null;
        return new PacienteResponseDTO(
                paciente.getId(),
                nome,
                email,
                telefone,
                paciente.getCpf(),
                paciente.getDataNascimento(),
                paciente.getEndereco(),
                paciente.getNumeroCartaoSus(),
                paciente.getConvenioMedico(),
                paciente.getContatoEmergencia(),
                paciente.getObservacoesMedicas(),
                paciente.getAtivo()
        );
    }

    public static Paciente fromDTO(PacienteRequestDTO dto) {
        if (dto == null) return null;
        Paciente paciente = new Paciente();
        paciente.setUserId(dto.getUserId());
        paciente.setCpf(dto.getCpf());
        paciente.setDataNascimento(dto.getDataNascimento());
        paciente.setEndereco(dto.getEndereco());
        paciente.setNumeroCartaoSus(dto.getNumeroCartaoSus());
        paciente.setConvenioMedico(dto.getConvenioMedico());
        paciente.setContatoEmergencia(dto.getContatoEmergencia());
        paciente.setObservacoesMedicas(dto.getObservacoesMedicas());
        return paciente;
    }

    public static void updateFromDTO(Paciente paciente, PacienteRequestDTO dto) {
        paciente.setCpf(dto.getCpf());
        paciente.setDataNascimento(dto.getDataNascimento());
        paciente.setEndereco(dto.getEndereco());
        paciente.setNumeroCartaoSus(dto.getNumeroCartaoSus());
        paciente.setConvenioMedico(dto.getConvenioMedico());
        paciente.setContatoEmergencia(dto.getContatoEmergencia());
        paciente.setObservacoesMedicas(dto.getObservacoesMedicas());
    }
}
