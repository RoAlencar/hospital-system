package br.com.fiap.app.agendamentoService.service;

import br.com.fiap.app.agendamentoService.entity.Consulta;
import br.com.fiap.app.agendamentoService.entity.Medico;
import br.com.fiap.app.agendamentoService.entity.Paciente;
import br.com.fiap.app.agendamentoService.enums.StatusConsulta;
import br.com.fiap.app.agendamentoService.exception.BusinessException;
import br.com.fiap.app.agendamentoService.exception.ResourceNotFoundException;
import br.com.fiap.app.agendamentoService.repository.ConsultaRepository;
import br.com.fiap.app.agendamentoService.repository.MedicoRepository;
import br.com.fiap.app.agendamentoService.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ConsultaService Tests")
class ConsultaServiceTest {

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private ConsultaService consultaService;

    private Consulta consulta;
    private Medico medico;
    private Paciente paciente;
    private LocalDateTime futureDate;
    private LocalDateTime pastDate;

    @BeforeEach
    void setUp() {
        futureDate = LocalDateTime.now().plusDays(1);
        pastDate = LocalDateTime.now().minusDays(1);

        medico = new Medico();
        medico.setId(1L);

        paciente = new Paciente();
        paciente.setId(1L);

        consulta = new Consulta();
        consulta.setId(1L);
        consulta.setMedicoId(1L);
        consulta.setPacienteId(1L);
        consulta.setDataHora(futureDate);
        consulta.setMotivo("Consulta de rotina");
        consulta.setObservacoes("Paciente em bom estado geral");
        consulta.setStatus(StatusConsulta.AGENDADA);
        consulta.setMedico(medico);
        consulta.setPaciente(paciente);
    }

    @Test
    @DisplayName("Should create consulta successfully")
    void shouldCreateConsultaSuccessfully() {
        // Given
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(consultaRepository.save(any(Consulta.class))).thenReturn(consulta);

        // When
        Consulta result = consultaService.createConsulta(consulta);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(StatusConsulta.AGENDADA);
        assertThat(result.getMedico()).isEqualTo(medico);
        assertThat(result.getPaciente()).isEqualTo(paciente);
        assertThat(result.getDataCriacao()).isNotNull();
        
        verify(medicoRepository).findById(1L);
        verify(pacienteRepository).findById(1L);
        verify(consultaRepository).save(any(Consulta.class));
    }

    @Test
    @DisplayName("Should throw exception when medico not found")
    void shouldThrowExceptionWhenMedicoNotFound() {
        // Given
        when(medicoRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> consultaService.createConsulta(consulta))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Médico")
                .hasMessageContaining("ID")
                .hasMessageContaining("1");
        
        verify(medicoRepository).findById(1L);
        verify(pacienteRepository, never()).findById(any());
        verify(consultaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when paciente not found")
    void shouldThrowExceptionWhenPacienteNotFound() {
        // Given
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(pacienteRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> consultaService.createConsulta(consulta))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Paciente")
                .hasMessageContaining("ID")
                .hasMessageContaining("1");
        
        verify(medicoRepository).findById(1L);
        verify(pacienteRepository).findById(1L);
        verify(consultaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when consulta date is in the past")
    void shouldThrowExceptionWhenConsultaDateIsInPast() {
        // Given
        consulta.setDataHora(pastDate);
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        // When & Then
        assertThatThrownBy(() -> consultaService.createConsulta(consulta))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Data da consulta deve ser futura");
        
        verify(consultaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should get consulta by id successfully")
    void shouldGetConsultaByIdSuccessfully() {
        // Given
        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));

        // When
        Consulta result = consultaService.getConsultaById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(consultaRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when consulta not found by id")
    void shouldThrowExceptionWhenConsultaNotFoundById() {
        // Given
        when(consultaRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> consultaService.getConsultaById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Consulta")
                .hasMessageContaining("ID")
                .hasMessageContaining("1");
    }

    @Test
    @DisplayName("Should get all consultas successfully")
    void shouldGetAllConsultasSuccessfully() {
        // Given
        List<Consulta> consultas = Arrays.asList(consulta);
        when(consultaRepository.findAll()).thenReturn(consultas);

        // When
        List<Consulta> result = consultaService.getAllConsultas();

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(consulta);
        verify(consultaRepository).findAll();
    }

    @Test
    @DisplayName("Should get consultas by medico successfully")
    void shouldGetConsultasByMedicoSuccessfully() {
        // Given
        List<Consulta> consultas = Arrays.asList(consulta);
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(consultaRepository.findByMedico(medico)).thenReturn(consultas);

        // When
        List<Consulta> result = consultaService.getConsultasByMedico(1L);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(consulta);
        verify(medicoRepository).findById(1L);
        verify(consultaRepository).findByMedico(medico);
    }

    @Test
    @DisplayName("Should throw exception when getting consultas by non-existent medico")
    void shouldThrowExceptionWhenGettingConsultasByNonExistentMedico() {
        // Given
        when(medicoRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> consultaService.getConsultasByMedico(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Médico");
        
        verify(consultaRepository, never()).findByMedico(any());
    }

    @Test
    @DisplayName("Should get consultas by paciente successfully")
    void shouldGetConsultasByPacienteSuccessfully() {
        // Given
        List<Consulta> consultas = Arrays.asList(consulta);
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(consultaRepository.findByPaciente(paciente)).thenReturn(consultas);

        // When
        List<Consulta> result = consultaService.getConsultasByPaciente(1L);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        verify(pacienteRepository).findById(1L);
        verify(consultaRepository).findByPaciente(paciente);
    }

    @Test
    @DisplayName("Should get consultas by status successfully")
    void shouldGetConsultasByStatusSuccessfully() {
        // Given
        List<Consulta> consultas = Arrays.asList(consulta);
        when(consultaRepository.findByStatus(StatusConsulta.AGENDADA)).thenReturn(consultas);

        // When
        List<Consulta> result = consultaService.getConsultasByStatus(StatusConsulta.AGENDADA);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        verify(consultaRepository).findByStatus(StatusConsulta.AGENDADA);
    }

    @Test
    @DisplayName("Should get consultas by periodo successfully")
    void shouldGetConsultasByPeriodoSuccessfully() {
        // Given
        List<Consulta> consultas = Arrays.asList(consulta);
        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fim = LocalDateTime.now().plusDays(7);
        when(consultaRepository.findByPeriodo(inicio, fim)).thenReturn(consultas);

        // When
        List<Consulta> result = consultaService.getConsultasByPeriodo(inicio, fim);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        verify(consultaRepository).findByPeriodo(inicio, fim);
    }

    @Test
    @DisplayName("Should get consultas futuras por paciente successfully")
    void shouldGetConsultasFuturasPorPacienteSuccessfully() {
        // Given
        List<Consulta> consultas = Arrays.asList(consulta);
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(consultaRepository.findConsultasFuturasPorPaciente(eq(paciente), any(LocalDateTime.class)))
                .thenReturn(consultas);

        // When
        List<Consulta> result = consultaService.getConsultasFuturasPorPaciente(1L);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        verify(pacienteRepository).findById(1L);
        verify(consultaRepository).findConsultasFuturasPorPaciente(eq(paciente), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("Should get historico completo paciente successfully")
    void shouldGetHistoricoCompletoPacienteSuccessfully() {
        // Given
        List<Consulta> consultas = Arrays.asList(consulta);
        when(consultaRepository.findHistoricoCompletoPaciente(1L)).thenReturn(consultas);

        // When
        List<Consulta> result = consultaService.getHistoricoCompletoPaciente(1L);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        verify(consultaRepository).findHistoricoCompletoPaciente(1L);
    }

    @Test
    @DisplayName("Should get consultas para notificacao successfully")
    void shouldGetConsultasParaNotificacaoSuccessfully() {
        // Given
        List<Consulta> consultas = Arrays.asList(consulta);
        List<StatusConsulta> statuses = List.of(StatusConsulta.AGENDADA, StatusConsulta.CONFIRMADA);
        when(consultaRepository.findConsultasParaNotificacao(any(LocalDateTime.class), eq(statuses)))
                .thenReturn(consultas);

        // When
        List<Consulta> result = consultaService.getConsultasParaNotificacao();

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        verify(consultaRepository).findConsultasParaNotificacao(any(LocalDateTime.class), eq(statuses));
    }

    @Test
    @DisplayName("Should update consulta successfully")
    void shouldUpdateConsultaSuccessfully() {
        // Given
        LocalDateTime newDate = LocalDateTime.now().plusDays(2);
        Consulta updateRequest = new Consulta();
        updateRequest.setDataHora(newDate);
        updateRequest.setStatus(StatusConsulta.CONFIRMADA);
        updateRequest.setMotivo("Consulta de follow-up");
        updateRequest.setObservacoes("Paciente melhorou");
        updateRequest.setDiagnostico("Diagnóstico atualizado");
        updateRequest.setPrescricao("Nova prescrição");

        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));
        when(consultaRepository.save(any(Consulta.class))).thenReturn(consulta);

        // When
        Consulta result = consultaService.updateConsulta(1L, updateRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getDataAlteracao()).isNotNull();
        verify(consultaRepository).findById(1L);
        verify(consultaRepository).save(any(Consulta.class));
    }

    @Test
    @DisplayName("Should throw exception when updating consulta with past date")
    void shouldThrowExceptionWhenUpdatingConsultaWithPastDate() {
        // Given
        Consulta updateRequest = new Consulta();
        updateRequest.setDataHora(pastDate);
        
        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));

        // When & Then
        assertThatThrownBy(() -> consultaService.updateConsulta(1L, updateRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Data da consulta deve ser futura");
        
        verify(consultaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent consulta")
    void shouldThrowExceptionWhenUpdatingNonExistentConsulta() {
        // Given
        Consulta updateRequest = new Consulta();
        when(consultaRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> consultaService.updateConsulta(1L, updateRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Consulta");
    }

    @Test
    @DisplayName("Should update status consulta successfully")
    void shouldUpdateStatusConsultaSuccessfully() {
        // Given
        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));
        when(consultaRepository.save(any(Consulta.class))).thenReturn(consulta);

        // When
        Consulta result = consultaService.updateStatusConsulta(1L, StatusConsulta.CONFIRMADA);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getDataAlteracao()).isNotNull();
        verify(consultaRepository).findById(1L);
        verify(consultaRepository).save(any(Consulta.class));
    }

    @Test
    @DisplayName("Should delete consulta successfully")
    void shouldDeleteConsultaSuccessfully() {
        // Given
        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));

        // When
        consultaService.deleteConsulta(1L);

        // Then
        verify(consultaRepository).findById(1L);
        verify(consultaRepository).delete(consulta);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent consulta")
    void shouldThrowExceptionWhenDeletingNonExistentConsulta() {
        // Given
        when(consultaRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> consultaService.deleteConsulta(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Consulta");
        
        verify(consultaRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should cancelar consulta successfully")
    void shouldCancelarConsultaSuccessfully() {
        // Given
        String motivo = "Paciente não pode comparecer";
        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));
        when(consultaRepository.save(any(Consulta.class))).thenReturn(consulta);

        // When
        consultaService.cancelarConsulta(1L, motivo);

        // Then
        verify(consultaRepository).findById(1L);
        verify(consultaRepository).save(any(Consulta.class));
    }

    @Test
    @DisplayName("Should throw exception when canceling non-existent consulta")
    void shouldThrowExceptionWhenCancelingNonExistentConsulta() {
        // Given
        when(consultaRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> consultaService.cancelarConsulta(1L, "Motivo"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Consulta");
        
        verify(consultaRepository, never()).save(any());
    }
}