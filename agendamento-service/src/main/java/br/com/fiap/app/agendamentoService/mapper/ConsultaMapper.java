package br.com.fiap.app.agendamentoService.mapper;

import br.com.fiap.app.agendamentoService.dto.ConsultaResponseDTO;
import br.com.fiap.app.agendamentoService.dto.EnfermeiroSimpleDTO;
import br.com.fiap.app.agendamentoService.dto.MedicoSimpleDTO;
import br.com.fiap.app.agendamentoService.dto.PacienteSimpleDTO;
import br.com.fiap.app.agendamentoService.entity.Consulta;
import br.com.fiap.app.agendamentoService.entity.Enfermeiro;
import br.com.fiap.app.agendamentoService.entity.Medico;
import br.com.fiap.app.agendamentoService.entity.Paciente;

public class ConsultaMapper {

    public static ConsultaResponseDTO toDTO(Consulta consulta) {
        if (consulta == null) {
            return null;
        }

        ConsultaResponseDTO dto = new ConsultaResponseDTO();
        dto.setId(consulta.getId());
        dto.setMedico(toMedicoSimpleDTO(consulta.getMedico()));
        dto.setPaciente(toPacienteSimpleDTO(consulta.getPaciente()));
        dto.setEnfermeiro(toEnfermeiroSimpleDTO(consulta.getEnfermeiro()));
        dto.setDataHora(consulta.getDataHora());
        dto.setStatus(consulta.getStatus());
        dto.setMotivo(consulta.getMotivo());
        dto.setObservacoes(consulta.getObservacoes());
        dto.setDiagnostico(consulta.getDiagnostico());
        dto.setPrescricao(consulta.getPrescricao());
        dto.setDataCriacao(consulta.getDataCriacao());
        dto.setDataAlteracao(consulta.getDataAlteracao());

        return dto;
    }

    public static MedicoSimpleDTO toMedicoSimpleDTO(Medico medico) {
        if (medico == null) {
            return null;
        }
        return new MedicoSimpleDTO(
                medico.getId(),
                medico.getUser() != null ? medico.getUser().getNome() : null,
                medico.getCrm(),
                medico.getEspecialidade()
        );
    }

    public static PacienteSimpleDTO toPacienteSimpleDTO(Paciente paciente) {
        if (paciente == null) {
            return null;
        }
        return new PacienteSimpleDTO(
                paciente.getId(),
                paciente.getUser() != null ? paciente.getUser().getNome() : null,
                paciente.getCpf()
        );
    }

    public static EnfermeiroSimpleDTO toEnfermeiroSimpleDTO(Enfermeiro enfermeiro) {
        if (enfermeiro == null) {
            return null;
        }
        return new EnfermeiroSimpleDTO(
                enfermeiro.getId(),
                enfermeiro.getUser() != null ? enfermeiro.getUser().getNome() : null,
                enfermeiro.getCoren()
        );
    }
}
