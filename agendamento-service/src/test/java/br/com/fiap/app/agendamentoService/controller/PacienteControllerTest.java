package br.com.fiap.app.agendamentoService.controller;

import br.com.fiap.app.agendamentoService.entity.Paciente;
import br.com.fiap.app.agendamentoService.entity.User;
import br.com.fiap.app.agendamentoService.enums.Role;
import br.com.fiap.app.agendamentoService.service.PacienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PacienteController Tests")
class PacienteControllerTest {

    @Mock
    private PacienteService pacienteService;

    @InjectMocks
    private PacienteController pacienteController;

    private Paciente paciente;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("maria.paciente");
        user.setNome("Maria da Silva");
        user.setEmail("maria@email.com");
        user.setRole(Role.ROLE_PACIENTE);
        user.setActive(true);

        paciente = new Paciente();
        paciente.setId(1L);
        paciente.setUserId(1L);
        paciente.setCpf("12345678901");
        paciente.setDataNascimento(LocalDate.of(1990, 5, 15));
        paciente.setAtivo(true);
        paciente.setUser(user);
    }

    @Test
    @DisplayName("Should create paciente and return 201")
    void shouldCreatePacienteAndReturn201() {
        when(pacienteService.createPaciente(any(Paciente.class))).thenReturn(paciente);

        ResponseEntity<Paciente> response = pacienteController.createPaciente(paciente);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(paciente);
        verify(pacienteService).createPaciente(any(Paciente.class));
    }

    @Test
    @DisplayName("Should get paciente by id and return 200")
    void shouldGetPacienteByIdAndReturn200() {
        when(pacienteService.getPacienteById(1L)).thenReturn(paciente);

        ResponseEntity<Paciente> response = pacienteController.getPacienteById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(paciente);
        verify(pacienteService).getPacienteById(1L);
    }

    @Test
    @DisplayName("Should get paciente by CPF and return 200")
    void shouldGetPacienteByCpfAndReturn200() {
        when(pacienteService.getPacienteByCpf("12345678901")).thenReturn(paciente);

        ResponseEntity<Paciente> response = pacienteController.getPacienteByCpf("12345678901");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(paciente);
        verify(pacienteService).getPacienteByCpf("12345678901");
    }

    @Test
    @DisplayName("Should get paciente by user id and return 200")
    void shouldGetPacienteByUserIdAndReturn200() {
        when(pacienteService.getPacienteByUserId(1L)).thenReturn(paciente);

        ResponseEntity<Paciente> response = pacienteController.getPacienteByUserId(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(paciente);
        verify(pacienteService).getPacienteByUserId(1L);
    }

    @Test
    @DisplayName("Should get all pacientes and return 200")
    void shouldGetAllPacientesAndReturn200() {
        List<Paciente> pacientes = Arrays.asList(paciente);
        when(pacienteService.getAllPacientes()).thenReturn(pacientes);

        ResponseEntity<List<Paciente>> response = pacienteController.getAllPacientes();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(pacienteService).getAllPacientes();
    }

    @Test
    @DisplayName("Should get active pacientes and return 200")
    void shouldGetActivePacientesAndReturn200() {
        List<Paciente> pacientes = Arrays.asList(paciente);
        when(pacienteService.getActivePacientes()).thenReturn(pacientes);

        ResponseEntity<List<Paciente>> response = pacienteController.getActivePacientes();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(pacienteService).getActivePacientes();
    }

    @Test
    @DisplayName("Should search pacientes by nome and return 200")
    void shouldSearchPacientesByNomeAndReturn200() {
        List<Paciente> pacientes = Arrays.asList(paciente);
        when(pacienteService.getPacientesByNome("Maria")).thenReturn(pacientes);

        ResponseEntity<List<Paciente>> response = pacienteController.searchPacientesByNome("Maria");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(pacienteService).getPacientesByNome("Maria");
    }

    @Test
    @DisplayName("Should update paciente and return 200")
    void shouldUpdatePacienteAndReturn200() {
        when(pacienteService.updatePaciente(eq(1L), any(Paciente.class))).thenReturn(paciente);

        ResponseEntity<Paciente> response = pacienteController.updatePaciente(1L, paciente);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(paciente);
        verify(pacienteService).updatePaciente(eq(1L), any(Paciente.class));
    }

    @Test
    @DisplayName("Should deactivate paciente and return 200")
    void shouldDeactivatePacienteAndReturn200() {
        doNothing().when(pacienteService).deactivatePaciente(1L);

        ResponseEntity<Void> response = pacienteController.deactivatePaciente(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(pacienteService).deactivatePaciente(1L);
    }

    @Test
    @DisplayName("Should activate paciente and return 200")
    void shouldActivatePacienteAndReturn200() {
        doNothing().when(pacienteService).activatePaciente(1L);

        ResponseEntity<Void> response = pacienteController.activatePaciente(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(pacienteService).activatePaciente(1L);
    }

    @Test
    @DisplayName("Should delete paciente and return 204")
    void shouldDeletePacienteAndReturn204() {
        doNothing().when(pacienteService).deletePaciente(1L);

        ResponseEntity<Void> response = pacienteController.deletePaciente(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(pacienteService).deletePaciente(1L);
    }
}
