package br.com.fiap.app.agendamentoService.mapper;

import br.com.fiap.app.agendamentoService.dto.EnfermeiroRequestDTO;
import br.com.fiap.app.agendamentoService.dto.EnfermeiroResponseDTO;
import br.com.fiap.app.agendamentoService.entity.Enfermeiro;

public class EnfermeiroMapper {

    public static EnfermeiroResponseDTO toDTO(Enfermeiro enfermeiro) {
        if (enfermeiro == null) return null;
        String nome = enfermeiro.getUser() != null ? enfermeiro.getUser().getNome() : null;
        String email = enfermeiro.getUser() != null ? enfermeiro.getUser().getEmail() : null;
        String telefone = enfermeiro.getUser() != null ? enfermeiro.getUser().getTelefone() : null;
        return new EnfermeiroResponseDTO(
                enfermeiro.getId(),
                nome,
                email,
                telefone,
                enfermeiro.getCoren(),
                enfermeiro.getSetor(),
                enfermeiro.getTurno(),
                enfermeiro.getEspecializacao(),
                enfermeiro.getDescricao(),
                enfermeiro.getAtivo()
        );
    }

    public static Enfermeiro fromDTO(EnfermeiroRequestDTO dto) {
        if (dto == null) return null;
        Enfermeiro enfermeiro = new Enfermeiro();
        enfermeiro.setUserId(dto.getUserId());
        enfermeiro.setCoren(dto.getCoren());
        enfermeiro.setSetor(dto.getSetor());
        enfermeiro.setTurno(dto.getTurno());
        enfermeiro.setEspecializacao(dto.getEspecializacao());
        enfermeiro.setDescricao(dto.getDescricao());
        return enfermeiro;
    }

    public static void updateFromDTO(Enfermeiro enfermeiro, EnfermeiroRequestDTO dto) {
        enfermeiro.setCoren(dto.getCoren());
        enfermeiro.setSetor(dto.getSetor());
        enfermeiro.setTurno(dto.getTurno());
        enfermeiro.setEspecializacao(dto.getEspecializacao());
        enfermeiro.setDescricao(dto.getDescricao());
    }
}
