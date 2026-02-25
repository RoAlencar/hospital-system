package br.com.fiap.app.agendamentoService.service;

import br.com.fiap.app.agendamentoService.entity.Medico;
import br.com.fiap.app.agendamentoService.entity.User;
import br.com.fiap.app.agendamentoService.enums.Especialidade;
import br.com.fiap.app.agendamentoService.exception.BusinessException;
import br.com.fiap.app.agendamentoService.exception.ResourceNotFoundException;
import br.com.fiap.app.agendamentoService.repository.MedicoRepository;
import br.com.fiap.app.agendamentoService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final UserRepository userRepository;

    public Medico createMedico(Medico request) {
        if (medicoRepository.existsByCrm(request.getCrm())) {
            throw new BusinessException("CRM já existe: " + request.getCrm());
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "ID", request.getUserId()));

        request.setUser(user);
        request.setAtivo(true);
        return medicoRepository.save(request);
    }

    @Transactional(readOnly = true)
    public Medico getMedicoById(Long id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médico", "ID", id));
    }

    @Transactional(readOnly = true)
    public Medico getMedicoByCrm(String crm) {
        return medicoRepository.findByCrm(crm)
                .orElseThrow(() -> new ResourceNotFoundException("Médico", "CRM", crm));
    }

    @Transactional(readOnly = true)
    public Medico getMedicoByUserId(Long userId) {
        return medicoRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Médico", "User ID", userId));
    }

    @Transactional(readOnly = true)
    public List<Medico> getAllMedicos() {
        return medicoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Medico> getMedicosByEspecialidade(Especialidade especialidade) {
        return medicoRepository.findByEspecialidade(especialidade);
    }

    @Transactional(readOnly = true)
    public List<Medico> getActiveMedicos() {
        return medicoRepository.findByAtivoTrue();
    }

    @Transactional(readOnly = true)
    public List<Medico> getMedicosByNome(String nome) {
        return medicoRepository.findByNomeContaining(nome);
    }

    public Medico updateMedico(Long id, Medico request) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médico", "ID", id));

        if (request.getCrm() != null) {
            if (!medico.getCrm().equals(request.getCrm()) && medicoRepository.existsByCrm(request.getCrm())) {
                throw new BusinessException("CRM já existe: " + request.getCrm());
            }
            medico.setCrm(request.getCrm());
        }
        if (request.getEspecialidade() != null) {
            medico.setEspecialidade(request.getEspecialidade());
        }
        if (request.getDescricao() != null) {
            medico.setDescricao(request.getDescricao());
        }
        if (request.getAtivo() != null) {
            medico.setAtivo(request.getAtivo());
        }

        return medicoRepository.save(medico);
    }

    public void deleteMedico(Long id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médico", "ID", id));
        medicoRepository.delete(medico);
    }

    public void deactivateMedico(Long id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médico", "ID", id));
        medico.setAtivo(false);
        medicoRepository.save(medico);
    }

    public void activateMedico(Long id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médico", "ID", id));
        medico.setAtivo(true);
        medicoRepository.save(medico);
    }
}
