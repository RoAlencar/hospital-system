package br.com.fiap.app.agendamentoService.service;

import br.com.fiap.app.agendamentoService.entity.Enfermeiro;
import br.com.fiap.app.agendamentoService.entity.User;
import br.com.fiap.app.agendamentoService.exception.BusinessException;
import br.com.fiap.app.agendamentoService.exception.ResourceNotFoundException;
import br.com.fiap.app.agendamentoService.repository.EnfermeiroRepository;
import br.com.fiap.app.agendamentoService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EnfermeiroService {

    private final EnfermeiroRepository enfermeiroRepository;
    private final UserRepository userRepository;

    public Enfermeiro createEnfermeiro(Enfermeiro request) {
        if (enfermeiroRepository.existsByCoren(request.getCoren())) {
            throw new BusinessException("COREN já existe: " + request.getCoren());
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "ID", request.getUserId()));

        request.setUser(user);
        request.setAtivo(true);
        return enfermeiroRepository.save(request);
    }

    @Transactional(readOnly = true)
    public Enfermeiro getEnfermeiroById(Long id) {
        return enfermeiroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enfermeiro", "ID", id));
    }

    @Transactional(readOnly = true)
    public Enfermeiro getEnfermeiroByCoren(String coren) {
        return enfermeiroRepository.findByCoren(coren)
                .orElseThrow(() -> new ResourceNotFoundException("Enfermeiro", "COREN", coren));
    }

    @Transactional(readOnly = true)
    public Enfermeiro getEnfermeiroByUserId(Long userId) {
        return enfermeiroRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Enfermeiro", "User ID", userId));
    }

    @Transactional(readOnly = true)
    public List<Enfermeiro> getAllEnfermeiros() {
        return enfermeiroRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Enfermeiro> getEnfermeirosBySetor(String setor) {
        return enfermeiroRepository.findBySetor(setor);
    }

    @Transactional(readOnly = true)
    public List<Enfermeiro> getEnfermeirosByTurno(String turno) {
        return enfermeiroRepository.findByTurno(turno);
    }

    @Transactional(readOnly = true)
    public List<Enfermeiro> getActiveEnfermeiros() {
        return enfermeiroRepository.findByAtivoTrue();
    }

    @Transactional(readOnly = true)
    public List<Enfermeiro> getEnfermeirosByNome(String nome) {
        return enfermeiroRepository.findByNomeContaining(nome);
    }

    @Transactional(readOnly = true)
    public List<Enfermeiro> getEnfermeirosBySetorAndTurno(String setor, String turno) {
        return enfermeiroRepository.findBySetorAndTurno(setor, turno);
    }

    @Transactional(readOnly = true)
    public List<Enfermeiro> getEnfermeirosByEspecializacao(String especializacao) {
        return enfermeiroRepository.findByEspecializacaoAndAtivoTrue(especializacao);
    }

    public Enfermeiro updateEnfermeiro(Long id, Enfermeiro request) {
        Enfermeiro enfermeiro = enfermeiroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enfermeiro", "ID", id));

        if (request.getCoren() != null) {
            if (!enfermeiro.getCoren().equals(request.getCoren()) && enfermeiroRepository.existsByCoren(request.getCoren())) {
                throw new BusinessException("COREN já existe: " + request.getCoren());
            }
            enfermeiro.setCoren(request.getCoren());
        }
        if (request.getSetor() != null) {
            enfermeiro.setSetor(request.getSetor());
        }
        if (request.getTurno() != null) {
            enfermeiro.setTurno(request.getTurno());
        }
        if (request.getEspecializacao() != null) {
            enfermeiro.setEspecializacao(request.getEspecializacao());
        }
        if (request.getDescricao() != null) {
            enfermeiro.setDescricao(request.getDescricao());
        }
        if (request.getAtivo() != null) {
            enfermeiro.setAtivo(request.getAtivo());
        }

        return enfermeiroRepository.save(enfermeiro);
    }

    public void deleteEnfermeiro(Long id) {
        Enfermeiro enfermeiro = enfermeiroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enfermeiro", "ID", id));
        enfermeiroRepository.delete(enfermeiro);
    }

    public void deactivateEnfermeiro(Long id) {
        Enfermeiro enfermeiro = enfermeiroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enfermeiro", "ID", id));
        enfermeiro.setAtivo(false);
        enfermeiroRepository.save(enfermeiro);
    }

    public void activateEnfermeiro(Long id) {
        Enfermeiro enfermeiro = enfermeiroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enfermeiro", "ID", id));
        enfermeiro.setAtivo(true);
        enfermeiroRepository.save(enfermeiro);
    }
}