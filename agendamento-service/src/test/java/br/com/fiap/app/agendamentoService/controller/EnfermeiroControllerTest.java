package br.com.fiap.app.agendamentoService.controller;

import br.com.fiap.app.agendamentoService.dto.EnfermeiroRequestDTO;
import br.com.fiap.app.agendamentoService.dto.EnfermeiroResponseDTO;
import br.com.fiap.app.agendamentoService.entity.Enfermeiro;
import br.com.fiap.app.agendamentoService.entity.User;
import br.com.fiap.app.agendamentoService.enums.Role;
import br.com.fiap.app.agendamentoService.service.EnfermeiroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EnfermeiroController Tests")
class EnfermeiroControllerTest {

    @Mock
    private EnfermeiroService enfermeiroService;

    @InjectMocks
    private EnfermeiroController enfermeiroController;

    private Enfermeiro enfermeiro;
    private User user;
    private EnfermeiroRequestDTO enfermeiroRequest;
    private EnfermeiroResponseDTO enfermeiroResponse;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("ana.enfermeira");
        user.setNome("Ana Costa");
        user.setEmail("ana@hospital.com");
        user.setRole(Role.ROLE_ENFERMEIRO);
        user.setActive(true);

        enfermeiro = new Enfermeiro();
        enfermeiro.setId(1L);
        enfermeiro.setUserId(1L);
        enfermeiro.setCoren("COREN-SP-123456");
        enfermeiro.setSetor("UTI");
        enfermeiro.setTurno("MANHA");
        enfermeiro.setEspecializacao("Terapia Intensiva");
        enfermeiro.setAtivo(true);
        enfermeiro.setUser(user);

        enfermeiroRequest = new EnfermeiroRequestDTO();
        enfermeiroRequest.setUserId(1L);
        enfermeiroRequest.setCoren("COREN-SP-123456");
        enfermeiroRequest.setSetor("UTI");
        enfermeiroRequest.setTurno("MANHA");
        enfermeiroRequest.setEspecializacao("Terapia Intensiva");

        enfermeiroResponse = new EnfermeiroResponseDTO(
                1L, "Ana Costa", "ana@hospital.com", null,
                "COREN-SP-123456", "UTI", "MANHA", "Terapia Intensiva", null, true);
    }

    @Test
    @DisplayName("Should create enfermeiro and return 201")
    void shouldCreateEnfermeiroAndReturn201() {
        when(enfermeiroService.createEnfermeiro(any(Enfermeiro.class))).thenReturn(enfermeiro);

        ResponseEntity<EnfermeiroResponseDTO> response = enfermeiroController.createEnfermeiro(enfermeiroRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(enfermeiroResponse);
        verify(enfermeiroService).createEnfermeiro(any(Enfermeiro.class));
    }

    @Test
    @DisplayName("Should get enfermeiro by id and return 200")
    void shouldGetEnfermeiroByIdAndReturn200() {
        when(enfermeiroService.getEnfermeiroById(1L)).thenReturn(enfermeiro);

        ResponseEntity<EnfermeiroResponseDTO> response = enfermeiroController.getEnfermeiroById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(enfermeiroResponse);
        verify(enfermeiroService).getEnfermeiroById(1L);
    }

    @Test
    @DisplayName("Should get enfermeiro by COREN and return 200")
    void shouldGetEnfermeiroByCorenAndReturn200() {
        when(enfermeiroService.getEnfermeiroByCoren("COREN-SP-123456")).thenReturn(enfermeiro);

        ResponseEntity<EnfermeiroResponseDTO> response = enfermeiroController.getEnfermeiroByCoren("COREN-SP-123456");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(enfermeiroResponse);
        verify(enfermeiroService).getEnfermeiroByCoren("COREN-SP-123456");
    }

    @Test
    @DisplayName("Should get enfermeiro by user id and return 200")
    void shouldGetEnfermeiroByUserIdAndReturn200() {
        when(enfermeiroService.getEnfermeiroByUserId(1L)).thenReturn(enfermeiro);

        ResponseEntity<EnfermeiroResponseDTO> response = enfermeiroController.getEnfermeiroByUserId(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(enfermeiroResponse);
        verify(enfermeiroService).getEnfermeiroByUserId(1L);
    }

    @Test
    @DisplayName("Should get all enfermeiros and return 200")
    void shouldGetAllEnfermeirosAndReturn200() {
        List<Enfermeiro> enfermeiros = Arrays.asList(enfermeiro);
        when(enfermeiroService.getAllEnfermeiros()).thenReturn(enfermeiros);

        ResponseEntity<List<EnfermeiroResponseDTO>> response = enfermeiroController.getAllEnfermeiros();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(enfermeiroService).getAllEnfermeiros();
    }

    @Test
    @DisplayName("Should get enfermeiros by setor and return 200")
    void shouldGetEnfermeirosBySetorAndReturn200() {
        List<Enfermeiro> enfermeiros = Arrays.asList(enfermeiro);
        when(enfermeiroService.getEnfermeirosBySetor("UTI")).thenReturn(enfermeiros);

        ResponseEntity<List<EnfermeiroResponseDTO>> response = enfermeiroController.getEnfermeirosBySetor("UTI");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(enfermeiroService).getEnfermeirosBySetor("UTI");
    }

    @Test
    @DisplayName("Should get enfermeiros by turno and return 200")
    void shouldGetEnfermeirosByTurnoAndReturn200() {
        List<Enfermeiro> enfermeiros = Arrays.asList(enfermeiro);
        when(enfermeiroService.getEnfermeirosByTurno("MANHA")).thenReturn(enfermeiros);

        ResponseEntity<List<EnfermeiroResponseDTO>> response = enfermeiroController.getEnfermeirosByTurno("MANHA");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(enfermeiroService).getEnfermeirosByTurno("MANHA");
    }

    @Test
    @DisplayName("Should get enfermeiros by setor and turno and return 200")
    void shouldGetEnfermeirosBySetorAndTurnoAndReturn200() {
        List<Enfermeiro> enfermeiros = Arrays.asList(enfermeiro);
        when(enfermeiroService.getEnfermeirosBySetorAndTurno("UTI", "MANHA")).thenReturn(enfermeiros);

        ResponseEntity<List<EnfermeiroResponseDTO>> response = enfermeiroController.getEnfermeirosBySetorAndTurno("UTI", "MANHA");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(enfermeiroService).getEnfermeirosBySetorAndTurno("UTI", "MANHA");
    }

    @Test
    @DisplayName("Should get enfermeiros by especializacao and return 200")
    void shouldGetEnfermeirosByEspecializacaoAndReturn200() {
        List<Enfermeiro> enfermeiros = Arrays.asList(enfermeiro);
        when(enfermeiroService.getEnfermeirosByEspecializacao("Terapia Intensiva")).thenReturn(enfermeiros);

        ResponseEntity<List<EnfermeiroResponseDTO>> response = enfermeiroController.getEnfermeirosByEspecializacao("Terapia Intensiva");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(enfermeiroService).getEnfermeirosByEspecializacao("Terapia Intensiva");
    }

    @Test
    @DisplayName("Should get active enfermeiros and return 200")
    void shouldGetActiveEnfermeirosAndReturn200() {
        List<Enfermeiro> enfermeiros = Arrays.asList(enfermeiro);
        when(enfermeiroService.getActiveEnfermeiros()).thenReturn(enfermeiros);

        ResponseEntity<List<EnfermeiroResponseDTO>> response = enfermeiroController.getActiveEnfermeiros();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(enfermeiroService).getActiveEnfermeiros();
    }

    @Test
    @DisplayName("Should search enfermeiros by nome and return 200")
    void shouldSearchEnfermeirosByNomeAndReturn200() {
        List<Enfermeiro> enfermeiros = Arrays.asList(enfermeiro);
        when(enfermeiroService.getEnfermeirosByNome("Ana")).thenReturn(enfermeiros);

        ResponseEntity<List<EnfermeiroResponseDTO>> response = enfermeiroController.searchEnfermeirosByNome("Ana");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(enfermeiroService).getEnfermeirosByNome("Ana");
    }

    @Test
    @DisplayName("Should update enfermeiro and return 200")
    void shouldUpdateEnfermeiroAndReturn200() {
        when(enfermeiroService.getEnfermeiroById(1L)).thenReturn(enfermeiro);
        when(enfermeiroService.updateEnfermeiro(eq(1L), any(Enfermeiro.class))).thenReturn(enfermeiro);

        ResponseEntity<EnfermeiroResponseDTO> response = enfermeiroController.updateEnfermeiro(1L, enfermeiroRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(enfermeiroResponse);
        verify(enfermeiroService).updateEnfermeiro(eq(1L), any(Enfermeiro.class));
    }

    @Test
    @DisplayName("Should deactivate enfermeiro and return 200")
    void shouldDeactivateEnfermeiroAndReturn200() {
        doNothing().when(enfermeiroService).deactivateEnfermeiro(1L);

        ResponseEntity<Void> response = enfermeiroController.deactivateEnfermeiro(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(enfermeiroService).deactivateEnfermeiro(1L);
    }

    @Test
    @DisplayName("Should activate enfermeiro and return 200")
    void shouldActivateEnfermeiroAndReturn200() {
        doNothing().when(enfermeiroService).activateEnfermeiro(1L);

        ResponseEntity<Void> response = enfermeiroController.activateEnfermeiro(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(enfermeiroService).activateEnfermeiro(1L);
    }

    @Test
    @DisplayName("Should delete enfermeiro and return 204")
    void shouldDeleteEnfermeiroAndReturn204() {
        doNothing().when(enfermeiroService).deleteEnfermeiro(1L);

        ResponseEntity<Void> response = enfermeiroController.deleteEnfermeiro(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(enfermeiroService).deleteEnfermeiro(1L);
    }
}
