package br.com.fiap.app.agendamentoService.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.app.agendamentoService.dto.ConsultaInput;
import br.com.fiap.app.agendamentoService.dto.ConsultaResponseDTO;
import br.com.fiap.app.agendamentoService.entity.Consulta;
import br.com.fiap.app.agendamentoService.entity.Medico;
import br.com.fiap.app.agendamentoService.entity.Paciente;
import br.com.fiap.app.agendamentoService.enums.Especialidade;
import br.com.fiap.app.agendamentoService.enums.StatusConsulta;
import br.com.fiap.app.agendamentoService.exception.BusinessException;
import br.com.fiap.app.agendamentoService.service.ConsultaService;
import br.com.fiap.app.agendamentoService.service.PacienteService;

@ExtendWith(MockitoExtension.class)
@DisplayName("ConsultaGraphQLController Tests")
class ConsultaGraphQLControllerTest {

    @Mock
    private ConsultaService consultaService;

    @Mock
    private PacienteService pacienteService;

    @InjectMocks
    private ConsultaGraphQLController consultaGraphQLController;

    private Consulta consulta;
    private ConsultaResponseDTO consultaDTO;

    @BeforeEach
    void setUp() {
        Medico medico = new Medico();
        medico.setId(1L);
        medico.setCrm("123456");
        medico.setEspecialidade(Especialidade.CARDIOLOGIA);

        Paciente paciente = new Paciente();
        paciente.setId(1L);
        paciente.setCpf("12345678901");

        consulta = new Consulta();
        consulta.setId(1L);
        consulta.setMedico(medico);
        consulta.setPaciente(paciente);
        consulta.setDataHora(LocalDateTime.now().plusDays(1));
        consulta.setStatus(StatusConsulta.AGENDADA);
        consulta.setMotivo("Consulta de rotina");

        consultaDTO = new ConsultaResponseDTO();
        consultaDTO.setId(1L);
        consultaDTO.setStatus(StatusConsulta.AGENDADA);
    }

    // --- Queries ---

    @Test
    @DisplayName("Should return all consultas as DTOs")
    void shouldReturnAllConsultas() {
        List<ConsultaResponseDTO> dtos = Arrays.asList(consultaDTO);
        when(consultaService.getAllConsultasDTO()).thenReturn(dtos);

        List<ConsultaResponseDTO> result = consultaGraphQLController.consultas();

        assertThat(result).hasSize(1);
        verify(consultaService).getAllConsultasDTO();
    }

    @Test
    @DisplayName("Should return single consulta by id")
    void shouldReturnConsultaById() {
        when(consultaService.getConsultaById(1L)).thenReturn(consulta);

        ConsultaResponseDTO result = consultaGraphQLController.consulta(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(consultaService).getConsultaById(1L);
    }

    @Test
    @DisplayName("Should return consultas by medico id")
    void shouldReturnConsultasByMedico() {
        List<ConsultaResponseDTO> dtos = Arrays.asList(consultaDTO);
        when(consultaService.getConsultasByMedicoDTO(1L)).thenReturn(dtos);

        List<ConsultaResponseDTO> result = consultaGraphQLController.consultasByMedico(1L);

        assertThat(result).hasSize(1);
        verify(consultaService).getConsultasByMedicoDTO(1L);
    }

    @Test
    @DisplayName("Should return consultas by paciente id")
    void shouldReturnConsultasByPaciente() {
        List<ConsultaResponseDTO> dtos = Arrays.asList(consultaDTO);
        when(consultaService.getConsultasByPacienteDTO(1L)).thenReturn(dtos);

        List<ConsultaResponseDTO> result = consultaGraphQLController.consultasByPaciente(1L, null);

        assertThat(result).hasSize(1);
        verify(consultaService).getConsultasByPacienteDTO(1L);
    }

    @Test
    @DisplayName("Should return consultas futuras by paciente id")
    void shouldReturnConsultasFuturasByPaciente() {
        List<ConsultaResponseDTO> dtos = Arrays.asList(consultaDTO);
        when(consultaService.getConsultasFuturasPorPacienteDTO(1L)).thenReturn(dtos);

        List<ConsultaResponseDTO> result = consultaGraphQLController.consultasFuturasByPaciente(1L, null);

        assertThat(result).hasSize(1);
        verify(consultaService).getConsultasFuturasPorPacienteDTO(1L);
    }

    @Test
    @DisplayName("Should return historico completo by paciente id")
    void shouldReturnHistoricoCompletoPaciente() {
        List<ConsultaResponseDTO> dtos = Arrays.asList(consultaDTO);
        when(consultaService.getHistoricoCompletoPacienteDTO(1L)).thenReturn(dtos);

        List<ConsultaResponseDTO> result = consultaGraphQLController.historicoCompletoPaciente(1L, null);

        assertThat(result).hasSize(1);
        verify(consultaService).getHistoricoCompletoPacienteDTO(1L);
    }

    @Test
    @DisplayName("Should return consultas by status")
    void shouldReturnConsultasByStatus() {
        List<ConsultaResponseDTO> dtos = Arrays.asList(consultaDTO);
        when(consultaService.getConsultasByStatusDTO(StatusConsulta.AGENDADA)).thenReturn(dtos);

        List<ConsultaResponseDTO> result = consultaGraphQLController.consultasByStatus(StatusConsulta.AGENDADA);

        assertThat(result).hasSize(1);
        verify(consultaService).getConsultasByStatusDTO(StatusConsulta.AGENDADA);
    }

    // --- Mutations ---

    @Test
    @DisplayName("Should schedule consulta via mutation with valid ISO date")
    void shouldAgendarConsultaWithValidDate() {
        ConsultaInput input = new ConsultaInput(1L, 1L, null, "2026-04-01T10:00:00", "Consulta de rotina", null);
        when(consultaService.createConsulta(any(Consulta.class))).thenReturn(consulta);

        ConsultaResponseDTO result = consultaGraphQLController.agendarConsulta(input);

        assertThat(result).isNotNull();
        verify(consultaService).createConsulta(any(Consulta.class));
    }

    @Test
    @DisplayName("Should throw BusinessException when dataHora has invalid format")
    void shouldThrowBusinessExceptionForInvalidDateFormat() {
        ConsultaInput input = new ConsultaInput(1L, 1L, null, "01/04/2026", "Consulta de rotina", null);

        assertThatThrownBy(() -> consultaGraphQLController.agendarConsulta(input))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Formato de data inv\u00e1lido");
    }

    @Test
    @DisplayName("Should update consulta status via mutation")
    void shouldAtualizarStatusConsulta() {
        Consulta updated = new Consulta();
        updated.setId(1L);
        updated.setStatus(StatusConsulta.CONFIRMADA);
        when(consultaService.updateStatusConsulta(1L, StatusConsulta.CONFIRMADA)).thenReturn(updated);

        ConsultaResponseDTO result = consultaGraphQLController.atualizarStatusConsulta(1L, StatusConsulta.CONFIRMADA);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(StatusConsulta.CONFIRMADA);
        verify(consultaService).updateStatusConsulta(1L, StatusConsulta.CONFIRMADA);
    }

    @Test
    @DisplayName("Should cancel consulta via mutation and return true")
    void shouldCancelarConsultaAndReturnTrue() {
        Boolean result = consultaGraphQLController.cancelarConsulta(1L, "Paciente desmarcou");

        assertThat(result).isTrue();
        verify(consultaService).cancelarConsulta(eq(1L), eq("Paciente desmarcou"));
    }
}
