package br.com.fiap.app.agendamentoService.mapper;

import br.com.fiap.app.agendamentoService.dto.MedicoRequestDTO;
import br.com.fiap.app.agendamentoService.dto.MedicoResponseDTO;
import br.com.fiap.app.agendamentoService.entity.Medico;

public class MedicoMapper {

    public static MedicoResponseDTO toDTO(Medico medico) {
        if (medico == null) return null;
        String nome = medico.getUser() != null ? medico.getUser().getNome() : null;
        String email = medico.getUser() != null ? medico.getUser().getEmail() : null;
        String telefone = medico.getUser() != null ? medico.getUser().getTelefone() : null;
        return new MedicoResponseDTO(
                medico.getId(),
                nome,
                email,
                telefone,
                medico.getCrm(),
                medico.getEspecialidade(),
                medico.getDescricao(),
                medico.getAtivo()
        );
    }

    public static Medico fromDTO(MedicoRequestDTO dto) {
        if (dto == null) return null;
        Medico medico = new Medico();
        medico.setUserId(dto.getUserId());
        medico.setCrm(dto.getCrm());
        medico.setEspecialidade(dto.getEspecialidade());
        medico.setDescricao(dto.getDescricao());
        return medico;
    }

    public static void updateFromDTO(Medico medico, MedicoRequestDTO dto) {
        medico.setCrm(dto.getCrm());
        medico.setEspecialidade(dto.getEspecialidade());
        medico.setDescricao(dto.getDescricao());
    }
}
