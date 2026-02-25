package br.com.fiap.app.agendamentoService.service;

import br.com.fiap.app.agendamentoService.entity.Paciente;
import br.com.fiap.app.agendamentoService.entity.User;
import br.com.fiap.app.agendamentoService.exception.BusinessException;
import br.com.fiap.app.agendamentoService.exception.ResourceNotFoundException;
import br.com.fiap.app.agendamentoService.repository.PacienteRepository;
import br.com.fiap.app.agendamentoService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final UserRepository userRepository;

    public Paciente createPaciente(Paciente request) {
        if (pacienteRepository.existsByCpf(request.getCpf())) {
            throw new BusinessException("CPF já existe: " + request.getCpf());
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "ID", request.getUserId()));

        request.setUser(user);
        request.setAtivo(true);
        return pacienteRepository.save(request);
    }

    @Transactional(readOnly = true)
    public Paciente getPacienteById(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "ID", id));
    }

    @Transactional(readOnly = true)
    public Paciente getPacienteByCpf(String cpf) {
        return pacienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "CPF", cpf));
    }

    @Transactional(readOnly = true)
    public Paciente getPacienteByUserId(Long userId) {
        return pacienteRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "User ID", userId));
    }

    @Transactional(readOnly = true)
    public List<Paciente> getAllPacientes() {
        return pacienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Paciente> getActivePacientes() {
        return pacienteRepository.findByAtivoTrue();
    }

    @Transactional(readOnly = true)
    public List<Paciente> getPacientesByNome(String nome) {
        return pacienteRepository.findByNomeContaining(nome);
    }

    public Paciente updatePaciente(Long id, Paciente request) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "ID", id));

        if (request.getCpf() != null) {
            if (!paciente.getCpf().equals(request.getCpf()) && pacienteRepository.existsByCpf(request.getCpf())) {
                throw new BusinessException("CPF já existe: " + request.getCpf());
            }
            paciente.setCpf(request.getCpf());
        }
        if (request.getDataNascimento() != null) {
            paciente.setDataNascimento(request.getDataNascimento());
        }
        if (request.getEndereco() != null) {
            paciente.setEndereco(request.getEndereco());
        }
        if (request.getNumeroCartaoSus() != null) {
            paciente.setNumeroCartaoSus(request.getNumeroCartaoSus());
        }
        if (request.getConvenioMedico() != null) {
            paciente.setConvenioMedico(request.getConvenioMedico());
        }
        if (request.getContatoEmergencia() != null) {
            paciente.setContatoEmergencia(request.getContatoEmergencia());
        }
        if (request.getObservacoesMedicas() != null) {
            paciente.setObservacoesMedicas(request.getObservacoesMedicas());
        }
        if (request.getAtivo() != null) {
            paciente.setAtivo(request.getAtivo());
        }

        return pacienteRepository.save(paciente);
    }

    public void deletePaciente(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "ID", id));
        pacienteRepository.delete(paciente);
    }

    public void deactivatePaciente(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "ID", id));
        paciente.setAtivo(false);
        pacienteRepository.save(paciente);
    }

    public void activatePaciente(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "ID", id));
        paciente.setAtivo(true);
        pacienteRepository.save(paciente);
    }
}
