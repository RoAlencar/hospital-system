package br.com.fiap.app.agendamentoService.controller;

import br.com.fiap.app.agendamentoService.dto.ConsultaResponseDTO;
import br.com.fiap.app.agendamentoService.entity.Consulta;
import br.com.fiap.app.agendamentoService.entity.Medico;
import br.com.fiap.app.agendamentoService.entity.Paciente;
import br.com.fiap.app.agendamentoService.enums.Especialidade;
import br.com.fiap.app.agendamentoService.enums.StatusConsulta;
import br.com.fiap.app.agendamentoService.service.ConsultaService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ConsultaGraphQLController Tests")
class ConsultaGraphQLControllerTest {

    @Mock
    private ConsultaService consultaService;

    @InjectMocks
    private ConsultaGraphQLController controller;

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

    @Test
    @DisplayName("Should return consulta by id")
    void shouldReturnConsultaById() {
        when(consultaService.getConsultaById(1L)).thenReturn(consulta);

        Consulta result = controller.consultaById(1L);

        assertThat(result).isEqualTo(consulta);
        verify(consultaService).getConsultaById(1L);
    }

    @Test
    @DisplayName("Should return all consultas as DTOs")
    void shouldReturnAllConsultas() {
        List<ConsultaResponseDTO> dtos = Arrays.asList(consultaDTO);
        when(consultaService.getAllConsultasDTO()).thenReturn(dtos);

        List<ConsultaResponseDTO> result = controller.allConsultas();

        assertThat(result).hasSize(1);
        verify(consultaService).getAllConsultasDTO();
    }

    @Test
    @DisplayName("Should return consultas by medico")
    void shouldReturnConsultasByMedico() {
        List<ConsultaResponseDTO> dtos = Arrays.asList(consultaDTO);
        when(consultaService.getConsultasByMedicoDTO(1L)).thenReturn(dtos);

        List<ConsultaResponseDTO> result = controller.consultasByMedico(1L);

        assertThat(result).hasSize(1);
        verify(consultaService).getConsultasByMedicoDTO(1L);
    }

    @Test
    @DisplayName("Should return consultas by paciente")
    void shouldReturnConsultasByPaciente() {
        List<ConsultaResponseDTO> dtos = Arrays.asList(consultaDTO);
        when(consultaService.getConsultasByPacienteDTO(1L)).thenReturn(dtos);

        List<ConsultaResponseDTO> result = controller.consultasByPaciente(1L);

        assertThat(result).hasSize(1);
        verify(consultaService).getConsultasByPacienteDTO(1L);
    }

    @Test
    @DisplayName("Should return consultas by status")
    void shouldReturnConsultasByStatus() {
        List<ConsultaResponseDTO> dtos = Arrays.asList(consultaDTO);
        when(consultaService.getConsultasByStatusDTO(StatusConsulta.AGENDADA)).thenReturn(dtos);

        List<ConsultaResponseDTO> result = controller.consultasByStatus(StatusConsulta.AGENDADA);

        assertThat(result).hasSize(1);
        verify(consultaService).getConsultasByStatusDTO(StatusConsulta.AGENDADA);
    }

    @Test
    @DisplayName("Should return consultas futuras por paciente")
    void shouldReturnConsultasFuturasPorPaciente() {
        List<ConsultaResponseDTO> dtos = Arrays.asList(consultaDTO);
        when(consultaService.getConsultasFuturasPorPacienteDTO(1L)).thenReturn(dtos);

        List<ConsultaResponseDTO> result = controller.consultasFuturasPorPaciente(1L);

        assertThat(result).hasSize(1);
        verify(consultaService).getConsultasFuturasPorPacienteDTO(1L);
    }

    @Test
    @DisplayName("Should return historico completo paciente")
    void shouldReturnHistoricoCompletoPaciente() {
        List<ConsultaResponseDTO> dtos = Arrays.asList(consultaDTO);
        when(consultaService.getHistoricoCompletoPacienteDTO(1L)).thenReturn(dtos);

        List<ConsultaResponseDTO> result = controller.historicoCompletoPaciente(1L);

        assertThat(result).hasSize(1);
        verify(consultaService).getHistoricoCompletoPacienteDTO(1L);
    }

    @Test
    @DisplayName("Should update status consulta")
    void shouldUpdateStatusConsulta() {
        when(consultaService.updateStatusConsulta(1L, StatusConsulta.CONFIRMADA)).thenReturn(consulta);

        Consulta result = controller.updateStatusConsulta(1L, StatusConsulta.CONFIRMADA);

        assertThat(result).isEqualTo(consulta);
        verify(consultaService).updateStatusConsulta(1L, StatusConsulta.CONFIRMADA);
    }

    @Test
    @DisplayName("Should cancelar consulta")
    void shouldCancelarConsulta() {
        doNothing().when(consultaService).cancelarConsulta(1L, "Motivo cancelamento");

        Boolean result = controller.cancelarConsulta(1L, "Motivo cancelamento");

        assertThat(result).isTrue();
        verify(consultaService).cancelarConsulta(1L, "Motivo cancelamento");
    }

    @Test
    @DisplayName("Should delete consulta")
    void shouldDeleteConsulta() {
        doNothing().when(consultaService).deleteConsulta(1L);

        Boolean result = controller.deleteConsulta(1L);

        assertThat(result).isTrue();
        verify(consultaService).deleteConsulta(1L);
    }
}