package br.com.fiap.app.agendamentoService.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.fiap.app.agendamentoService.dto.ConsultaResponseDTO;
import br.com.fiap.app.agendamentoService.entity.Consulta;
import br.com.fiap.app.agendamentoService.entity.Medico;
import br.com.fiap.app.agendamentoService.entity.Paciente;
import br.com.fiap.app.agendamentoService.enums.Especialidade;
import br.com.fiap.app.agendamentoService.enums.StatusConsulta;
import br.com.fiap.app.agendamentoService.service.ConsultaService;

@ExtendWith(MockitoExtension.class)
@DisplayName("ConsultaController Tests")
class ConsultaControllerTest {

    @Mock
    private ConsultaService consultaService;

    @InjectMocks
    private ConsultaController consultaController;

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
    @DisplayName("Should create consulta and return 201")
    void shouldCreateConsultaAndReturn201() {
        when(consultaService.createConsulta(any(Consulta.class))).thenReturn(consulta);

        ResponseEntity<Consulta> response = consultaController.createConsulta(consulta);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(consulta);
        verify(consultaService).createConsulta(any(Consulta.class));
    }

    @Test
    @DisplayName("Should get consulta by id and return 200")
    void shouldGetConsultaByIdAndReturn200() {
        when(consultaService.getConsultaById(1L)).thenReturn(consulta);

        ResponseEntity<Consulta> response = consultaController.getConsultaById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(consulta);
        verify(consultaService).getConsultaById(1L);
    }

    @Test
    @DisplayName("Should get all consultas as DTOs and return 200")
    void shouldGetAllConsultasAndReturn200() {
        List<ConsultaResponseDTO> dtos = Arrays.asList(consultaDTO);
        when(consultaService.getAllConsultasDTO()).thenReturn(dtos);

        ResponseEntity<List<ConsultaResponseDTO>> response = consultaController.getAllConsultas();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(consultaService).getAllConsultasDTO();
    }

    @Test
    @DisplayName("Should get consultas by medico id and return 200")
    void shouldGetConsultasByMedicoAndReturn200() {
        List<ConsultaResponseDTO> dtos = Arrays.asList(consultaDTO);
        when(consultaService.getConsultasByMedicoDTO(1L)).thenReturn(dtos);

        ResponseEntity<List<ConsultaResponseDTO>> response = consultaController.getConsultasByMedico(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(consultaService).getConsultasByMedicoDTO(1L);
    }

    @Test
    @DisplayName("Should get consultas by paciente id and return 200")
    void shouldGetConsultasByPacienteAndReturn200() {
        List<ConsultaResponseDTO> dtos = Arrays.asList(consultaDTO);
        when(consultaService.getConsultasByPacienteDTO(1L)).thenReturn(dtos);

        ResponseEntity<List<ConsultaResponseDTO>> response = consultaController.getConsultasByPaciente(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(consultaService).getConsultasByPacienteDTO(1L);
    }

    @Test
    @DisplayName("Should get consultas futuras por paciente and return 200")
    void shouldGetConsultasFuturasPorPacienteAndReturn200() {
        List<ConsultaResponseDTO> dtos = Arrays.asList(consultaDTO);
        when(consultaService.getConsultasFuturasPorPacienteDTO(1L)).thenReturn(dtos);

        ResponseEntity<List<ConsultaResponseDTO>> response = consultaController.getConsultasFuturasPorPaciente(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(consultaService).getConsultasFuturasPorPacienteDTO(1L);
    }

    @Test
    @DisplayName("Should get historico completo paciente and return 200")
    void shouldGetHistoricoCompletoPacienteAndReturn200() {
        List<ConsultaResponseDTO> dtos = Arrays.asList(consultaDTO);
        when(consultaService.getHistoricoCompletoPacienteDTO(1L)).thenReturn(dtos);

        ResponseEntity<List<ConsultaResponseDTO>> response = consultaController.getHistoricoCompletoPaciente(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(consultaService).getHistoricoCompletoPacienteDTO(1L);
    }

    @Test
    @DisplayName("Should get consultas by status and return 200")
    void shouldGetConsultasByStatusAndReturn200() {
        List<ConsultaResponseDTO> dtos = Arrays.asList(consultaDTO);
        when(consultaService.getConsultasByStatusDTO(StatusConsulta.AGENDADA)).thenReturn(dtos);

        ResponseEntity<List<ConsultaResponseDTO>> response = consultaController.getConsultasByStatus(StatusConsulta.AGENDADA);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(consultaService).getConsultasByStatusDTO(StatusConsulta.AGENDADA);
    }

    @Test
    @DisplayName("Should get consultas by periodo and return 200")
    void shouldGetConsultasByPeriodoAndReturn200() {
        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fim = LocalDateTime.now().plusDays(7);
        List<ConsultaResponseDTO> dtos = Arrays.asList(consultaDTO);
        when(consultaService.getConsultasByPeriodoDTO(inicio, fim)).thenReturn(dtos);

        ResponseEntity<List<ConsultaResponseDTO>> response = consultaController.getConsultasByPeriodo(inicio, fim);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(consultaService).getConsultasByPeriodoDTO(inicio, fim);
    }

    @Test
    @DisplayName("Should get consultas para notificacao and return 200")
    void shouldGetConsultasParaNotificacaoAndReturn200() {
        List<Consulta> consultas = Arrays.asList(consulta);
        when(consultaService.getConsultasParaNotificacao()).thenReturn(consultas);

        ResponseEntity<List<Consulta>> response = consultaController.getConsultasParaNotificacao();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(consultaService).getConsultasParaNotificacao();
    }

    @Test
    @DisplayName("Should update consulta and return 200")
    void shouldUpdateConsultaAndReturn200() {
        when(consultaService.updateConsulta(eq(1L), any(Consulta.class))).thenReturn(consulta);

        ResponseEntity<Consulta> response = consultaController.updateConsulta(1L, consulta);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(consulta);
        verify(consultaService).updateConsulta(eq(1L), any(Consulta.class));
    }

    @Test
    @DisplayName("Should update status consulta and return 200")
    void shouldUpdateStatusConsultaAndReturn200() {
        when(consultaService.updateStatusConsulta(1L, StatusConsulta.CONFIRMADA)).thenReturn(consulta);

        ResponseEntity<Consulta> response = consultaController.updateStatusConsulta(1L, StatusConsulta.CONFIRMADA);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(consulta);
        verify(consultaService).updateStatusConsulta(1L, StatusConsulta.CONFIRMADA);
    }

    @Test
    @DisplayName("Should cancelar consulta and return 200")
    void shouldCancelarConsultaAndReturn200() {
        doNothing().when(consultaService).cancelarConsulta(1L, "Paciente não compareceu");

        ResponseEntity<Void> response = consultaController.cancelarConsulta(1L, "Paciente não compareceu");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(consultaService).cancelarConsulta(1L, "Paciente não compareceu");
    }

    @Test
    @DisplayName("Should delete consulta and return 204")
    void shouldDeleteConsultaAndReturn204() {
        doNothing().when(consultaService).deleteConsulta(1L);

        ResponseEntity<Void> response = consultaController.deleteConsulta(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(consultaService).deleteConsulta(1L);
    }
}
