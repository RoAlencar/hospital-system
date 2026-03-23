package br.com.fiap.app.agendamentoService.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.app.agendamentoService.constants.EntityNames;
import br.com.fiap.app.agendamentoService.dto.ConsultaAgendadaEvent;
import br.com.fiap.app.agendamentoService.dto.ConsultaResponseDTO;
import br.com.fiap.app.agendamentoService.entity.Consulta;
import br.com.fiap.app.agendamentoService.entity.Enfermeiro;
import br.com.fiap.app.agendamentoService.entity.Medico;
import br.com.fiap.app.agendamentoService.entity.Paciente;
import br.com.fiap.app.agendamentoService.enums.StatusConsulta;
import br.com.fiap.app.agendamentoService.exception.BusinessException;
import br.com.fiap.app.agendamentoService.exception.ResourceNotFoundException;
import br.com.fiap.app.agendamentoService.mapper.ConsultaMapper;
import br.com.fiap.app.agendamentoService.repository.ConsultaRepository;
import br.com.fiap.app.agendamentoService.repository.EnfermeiroRepository;
import br.com.fiap.app.agendamentoService.repository.MedicoRepository;
import br.com.fiap.app.agendamentoService.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final EnfermeiroRepository enfermeiroRepository;

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing-key}")
    private String routingKey;

    public Consulta createConsulta(Consulta request) {
        Medico medico = medicoRepository.findById(request.getMedicoId())
                .orElseThrow(() -> new ResourceNotFoundException(EntityNames.MEDICO, "ID", request.getMedicoId()));

        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException(EntityNames.PACIENTE, "ID", request.getPacienteId()));

        if (request.getDataHora().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Data da consulta deve ser futura");
        }

        Consulta consulta = new Consulta();
        consulta.setMedico(medico);
        consulta.setPaciente(paciente);

        if (request.getEnfermeiroId() != null) {
            Enfermeiro enfermeiro = enfermeiroRepository.findById(request.getEnfermeiroId())
                    .orElseThrow(() -> new ResourceNotFoundException(EntityNames.ENFERMEIRO, "ID", request.getEnfermeiroId()));
            consulta.setEnfermeiro(enfermeiro);
        }

        consulta.setDataHora(request.getDataHora());
        consulta.setMotivo(request.getMotivo());
        consulta.setObservacoes(request.getObservacoes());
        consulta.setStatus(StatusConsulta.AGENDADA);
        consulta.setDataCriacao(LocalDateTime.now());

        Consulta savedConsulta = consultaRepository.save(consulta);

        publishConsultaEvent(savedConsulta, savedConsulta.getMotivo());

        return savedConsulta;
    }

    @Transactional(readOnly = true)
    public Consulta getConsultaById(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(EntityNames.CONSULTA, "ID", id));
    }

    @Transactional(readOnly = true)
    public List<Consulta> getAllConsultas() {
        return consultaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Consulta> getConsultasByMedico(Long medicoId) {
        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new ResourceNotFoundException(EntityNames.MEDICO, "ID", medicoId));
        return consultaRepository.findByMedico(medico);
    }

    @Transactional(readOnly = true)
    public List<Consulta> getConsultasByPaciente(Long pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException(EntityNames.PACIENTE, "ID", pacienteId));
        return consultaRepository.findByPaciente(paciente);
    }

    @Transactional(readOnly = true)
    public List<Consulta> getConsultasByStatus(StatusConsulta status) {
        return consultaRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Consulta> getConsultasByPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return consultaRepository.findByPeriodo(inicio, fim);
    }

    @Transactional(readOnly = true)
    public List<Consulta> getConsultasFuturasPorPaciente(Long pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException(EntityNames.PACIENTE, "ID", pacienteId));
        return consultaRepository.findConsultasFuturasPorPaciente(paciente, LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public List<Consulta> getHistoricoCompletoPaciente(Long pacienteId) {
        return consultaRepository.findHistoricoCompletoPaciente(pacienteId);
    }

    @Transactional(readOnly = true)
    public List<Consulta> getConsultasParaNotificacao() {
        List<StatusConsulta> statuses = List.of(StatusConsulta.AGENDADA, StatusConsulta.CONFIRMADA);
        return consultaRepository.findConsultasParaNotificacao(LocalDateTime.now(), statuses);
    }

    public Consulta updateConsulta(Long id, Consulta request) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(EntityNames.CONSULTA, "ID", id));

        if (request.getDataHora() != null) {
            if (request.getDataHora().isBefore(LocalDateTime.now())) {
                throw new BusinessException("Data da consulta deve ser futura");
            }
            consulta.setDataHora(request.getDataHora());
        }
        if (request.getStatus() != null) {
            consulta.setStatus(request.getStatus());
        }
        if (request.getMotivo() != null) {
            consulta.setMotivo(request.getMotivo());
        }
        if (request.getObservacoes() != null) {
            consulta.setObservacoes(request.getObservacoes());
        }
        if (request.getDiagnostico() != null) {
            consulta.setDiagnostico(request.getDiagnostico());
        }
        if (request.getPrescricao() != null) {
            consulta.setPrescricao(request.getPrescricao());
        }

        consulta.setDataAlteracao(LocalDateTime.now());
        Consulta updated = consultaRepository.save(consulta);

        publishConsultaEvent(updated, updated.getMotivo());

        return updated;
    }

    public Consulta updateStatusConsulta(Long id, StatusConsulta status) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(EntityNames.CONSULTA, "ID", id));

        consulta.setStatus(status);
        consulta.setDataAlteracao(LocalDateTime.now());
        Consulta updated = consultaRepository.save(consulta);

        publishConsultaEvent(updated, updated.getMotivo());

        return updated;
    }

    public void deleteConsulta(Long id) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(EntityNames.CONSULTA, "ID", id));
        consultaRepository.delete(consulta);
    }

    public void cancelarConsulta(Long id, String motivo) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(EntityNames.CONSULTA, "ID", id));

        consulta.setStatus(StatusConsulta.CANCELADA);
        String observacoesAtuais = consulta.getObservacoes() != null ? consulta.getObservacoes() : "";
        consulta.setObservacoes(observacoesAtuais + "\nCANCELAMENTO: " + motivo);
        consulta.setDataAlteracao(LocalDateTime.now());
        Consulta cancelada = consultaRepository.save(consulta);

        publishConsultaEvent(cancelada, motivo);
    }

    // DTO Methods for simplified responses
    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> getAllConsultasDTO() {
        return consultaRepository.findAll().stream()
                .map(ConsultaMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> getConsultasByMedicoDTO(Long medicoId) {
        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new ResourceNotFoundException(EntityNames.MEDICO, "ID", medicoId));
        return consultaRepository.findByMedico(medico).stream()
                .map(ConsultaMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> getConsultasByPacienteDTO(Long pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException(EntityNames.PACIENTE, "ID", pacienteId));
        return consultaRepository.findByPaciente(paciente).stream()
                .map(ConsultaMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> getConsultasByStatusDTO(StatusConsulta status) {
        return consultaRepository.findByStatus(status).stream()
                .map(ConsultaMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> getConsultasByPeriodoDTO(LocalDateTime inicio, LocalDateTime fim) {
        return consultaRepository.findByPeriodo(inicio, fim).stream()
                .map(ConsultaMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> getConsultasFuturasPorPacienteDTO(Long pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException(EntityNames.PACIENTE, "ID", pacienteId));
        return consultaRepository.findConsultasFuturasPorPaciente(paciente, LocalDateTime.now()).stream()
                .map(ConsultaMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> getHistoricoCompletoPacienteDTO(Long pacienteId) {
        return consultaRepository.findHistoricoCompletoPaciente(pacienteId).stream()
                .map(ConsultaMapper::toDTO)
                .toList();
    }

    private void publishConsultaEvent(Consulta consulta, String motivo) {
        ConsultaAgendadaEvent event = ConsultaAgendadaEvent.builder()
                .consultaId(consulta.getId())
                .pacienteId(consulta.getPaciente().getId())
                .pacienteNome(consulta.getPaciente().getUser().getNome())
                .medicoNome(consulta.getMedico().getUser().getNome())
                .dataHora(consulta.getDataHora())
                .status(consulta.getStatus().name())
                .motivo(motivo)
                .build();
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }
}
