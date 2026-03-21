package br.com.fiap.app.agendamentoService.controller;

import br.com.fiap.app.agendamentoService.entity.Medico;
import br.com.fiap.app.agendamentoService.entity.User;
import br.com.fiap.app.agendamentoService.enums.Especialidade;
import br.com.fiap.app.agendamentoService.enums.Role;
import br.com.fiap.app.agendamentoService.service.MedicoService;
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
@DisplayName("MedicoController Tests")
class MedicoControllerTest {

    @Mock
    private MedicoService medicoService;

    @InjectMocks
    private MedicoController medicoController;

    private Medico medico;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("dr.joao");
        user.setNome("Dr. João Silva");
        user.setEmail("joao@hospital.com");
        user.setRole(Role.ROLE_MEDICO);
        user.setActive(true);

        medico = new Medico();
        medico.setId(1L);
        medico.setUserId(1L);
        medico.setCrm("123456");
        medico.setEspecialidade(Especialidade.CARDIOLOGIA);
        medico.setDescricao("Cardiologista experiente");
        medico.setAtivo(true);
        medico.setUser(user);
    }

    @Test
    @DisplayName("Should create medico and return 201")
    void shouldCreateMedicoAndReturn201() {
        when(medicoService.createMedico(any(Medico.class))).thenReturn(medico);

        ResponseEntity<Medico> response = medicoController.createMedico(medico);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(medico);
        verify(medicoService).createMedico(any(Medico.class));
    }

    @Test
    @DisplayName("Should get medico by id and return 200")
    void shouldGetMedicoByIdAndReturn200() {
        when(medicoService.getMedicoById(1L)).thenReturn(medico);

        ResponseEntity<Medico> response = medicoController.getMedicoById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(medico);
        verify(medicoService).getMedicoById(1L);
    }

    @Test
    @DisplayName("Should get medico by CRM and return 200")
    void shouldGetMedicoByCrmAndReturn200() {
        when(medicoService.getMedicoByCrm("123456")).thenReturn(medico);

        ResponseEntity<Medico> response = medicoController.getMedicoByCrm("123456");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(medico);
        verify(medicoService).getMedicoByCrm("123456");
    }

    @Test
    @DisplayName("Should get medico by user id and return 200")
    void shouldGetMedicoByUserIdAndReturn200() {
        when(medicoService.getMedicoByUserId(1L)).thenReturn(medico);

        ResponseEntity<Medico> response = medicoController.getMedicoByUserId(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(medico);
        verify(medicoService).getMedicoByUserId(1L);
    }

    @Test
    @DisplayName("Should get all medicos and return 200")
    void shouldGetAllMedicosAndReturn200() {
        List<Medico> medicos = Arrays.asList(medico);
        when(medicoService.getAllMedicos()).thenReturn(medicos);

        ResponseEntity<List<Medico>> response = medicoController.getAllMedicos();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(medicoService).getAllMedicos();
    }

    @Test
    @DisplayName("Should get medicos by especialidade and return 200")
    void shouldGetMedicosByEspecialidadeAndReturn200() {
        List<Medico> medicos = Arrays.asList(medico);
        when(medicoService.getMedicosByEspecialidade(Especialidade.CARDIOLOGIA)).thenReturn(medicos);

        ResponseEntity<List<Medico>> response = medicoController.getMedicosByEspecialidade(Especialidade.CARDIOLOGIA);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(medicoService).getMedicosByEspecialidade(Especialidade.CARDIOLOGIA);
    }

    @Test
    @DisplayName("Should get active medicos and return 200")
    void shouldGetActiveMedicosAndReturn200() {
        List<Medico> medicos = Arrays.asList(medico);
        when(medicoService.getActiveMedicos()).thenReturn(medicos);

        ResponseEntity<List<Medico>> response = medicoController.getActiveMedicos();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(medicoService).getActiveMedicos();
    }

    @Test
    @DisplayName("Should search medicos by nome and return 200")
    void shouldSearchMedicosByNomeAndReturn200() {
        List<Medico> medicos = Arrays.asList(medico);
        when(medicoService.getMedicosByNome("João")).thenReturn(medicos);

        ResponseEntity<List<Medico>> response = medicoController.searchMedicosByNome("João");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(medicoService).getMedicosByNome("João");
    }

    @Test
    @DisplayName("Should update medico and return 200")
    void shouldUpdateMedicoAndReturn200() {
        when(medicoService.updateMedico(eq(1L), any(Medico.class))).thenReturn(medico);

        ResponseEntity<Medico> response = medicoController.updateMedico(1L, medico);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(medico);
        verify(medicoService).updateMedico(eq(1L), any(Medico.class));
    }

    @Test
    @DisplayName("Should deactivate medico and return 200")
    void shouldDeactivateMedicoAndReturn200() {
        doNothing().when(medicoService).deactivateMedico(1L);

        ResponseEntity<Void> response = medicoController.deactivateMedico(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(medicoService).deactivateMedico(1L);
    }

    @Test
    @DisplayName("Should activate medico and return 200")
    void shouldActivateMedicoAndReturn200() {
        doNothing().when(medicoService).activateMedico(1L);

        ResponseEntity<Void> response = medicoController.activateMedico(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(medicoService).activateMedico(1L);
    }

    @Test
    @DisplayName("Should delete medico and return 204")
    void shouldDeleteMedicoAndReturn204() {
        doNothing().when(medicoService).deleteMedico(1L);

        ResponseEntity<Void> response = medicoController.deleteMedico(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(medicoService).deleteMedico(1L);
    }
}
