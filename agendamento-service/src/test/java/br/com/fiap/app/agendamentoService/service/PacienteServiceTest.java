package br.com.fiap.app.agendamentoService.service;

import br.com.fiap.app.agendamentoService.entity.Paciente;
import br.com.fiap.app.agendamentoService.entity.User;
import br.com.fiap.app.agendamentoService.exception.BusinessException;
import br.com.fiap.app.agendamentoService.exception.ResourceNotFoundException;
import br.com.fiap.app.agendamentoService.repository.PacienteRepository;
import br.com.fiap.app.agendamentoService.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PacienteService Tests")
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PacienteService pacienteService;

    private Paciente paciente;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setNome("João Silva");

        paciente = new Paciente();
        paciente.setId(1L);
        paciente.setUserId(1L);
        paciente.setCpf("12345678901");
        paciente.setDataNascimento(LocalDate.of(1990, 1, 1));
        paciente.setEndereco("Rua das Flores, 123");
        paciente.setNumeroCartaoSus("123456789012345");
        paciente.setConvenioMedico("Unimed");
        paciente.setContatoEmergencia("11999888777");
        paciente.setObservacoesMedicas("Paciente diabético");
        paciente.setAtivo(true);
        paciente.setUser(user);
    }

    @Test
    @DisplayName("Should create paciente successfully")
    void shouldCreatePacienteSuccessfully() {
        // Given
        when(pacienteRepository.existsByCpf("12345678901")).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);

        // When
        Paciente result = pacienteService.createPaciente(paciente);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAtivo()).isTrue();
        assertThat(result.getUser()).isEqualTo(user);
        
        verify(pacienteRepository).existsByCpf("12345678901");
        verify(userRepository).findById(1L);
        verify(pacienteRepository).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Should throw exception when CPF already exists")
    void shouldThrowExceptionWhenCpfAlreadyExists() {
        // Given
        when(pacienteRepository.existsByCpf("12345678901")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> pacienteService.createPaciente(paciente))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("CPF já existe")
                .hasMessageContaining("12345678901");
        
        verify(pacienteRepository).existsByCpf("12345678901");
        verify(userRepository, never()).findById(any());
        verify(pacienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when user not found for paciente creation")
    void shouldThrowExceptionWhenUserNotFoundForPacienteCreation() {
        // Given
        when(pacienteRepository.existsByCpf("12345678901")).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> pacienteService.createPaciente(paciente))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Usuário")
                .hasMessageContaining("ID")
                .hasMessageContaining("1");
        
        verify(pacienteRepository).existsByCpf("12345678901");
        verify(userRepository).findById(1L);
        verify(pacienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should get paciente by id successfully")
    void shouldGetPacienteByIdSuccessfully() {
        // Given
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        // When
        Paciente result = pacienteService.getPacienteById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getCpf()).isEqualTo("12345678901");
        verify(pacienteRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when paciente not found by id")
    void shouldThrowExceptionWhenPacienteNotFoundById() {
        // Given
        when(pacienteRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> pacienteService.getPacienteById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Paciente")
                .hasMessageContaining("ID")
                .hasMessageContaining("1");
    }

    @Test
    @DisplayName("Should get paciente by CPF successfully")
    void shouldGetPacienteByCpfSuccessfully() {
        // Given
        when(pacienteRepository.findByCpf("12345678901")).thenReturn(Optional.of(paciente));

        // When
        Paciente result = pacienteService.getPacienteByCpf("12345678901");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCpf()).isEqualTo("12345678901");
        verify(pacienteRepository).findByCpf("12345678901");
    }

    @Test
    @DisplayName("Should throw exception when paciente not found by CPF")
    void shouldThrowExceptionWhenPacienteNotFoundByCpf() {
        // Given
        when(pacienteRepository.findByCpf("12345678901")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> pacienteService.getPacienteByCpf("12345678901"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Paciente")
                .hasMessageContaining("CPF")
                .hasMessageContaining("12345678901");
    }

    @Test
    @DisplayName("Should get paciente by user id successfully")
    void shouldGetPacienteByUserIdSuccessfully() {
        // Given
        when(pacienteRepository.findByUserId(1L)).thenReturn(Optional.of(paciente));

        // When
        Paciente result = pacienteService.getPacienteByUserId(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(1L);
        verify(pacienteRepository).findByUserId(1L);
    }

    @Test
    @DisplayName("Should throw exception when paciente not found by user id")
    void shouldThrowExceptionWhenPacienteNotFoundByUserId() {
        // Given
        when(pacienteRepository.findByUserId(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> pacienteService.getPacienteByUserId(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Paciente")
                .hasMessageContaining("User ID")
                .hasMessageContaining("1");
    }

    @Test
    @DisplayName("Should get all pacientes successfully")
    void shouldGetAllPacientesSuccessfully() {
        // Given
        List<Paciente> pacientes = Arrays.asList(paciente);
        when(pacienteRepository.findAll()).thenReturn(pacientes);

        // When
        List<Paciente> result = pacienteService.getAllPacientes();

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(paciente);
        verify(pacienteRepository).findAll();
    }

    @Test
    @DisplayName("Should get active pacientes successfully")
    void shouldGetActivePacientesSuccessfully() {
        // Given
        List<Paciente> pacientes = Arrays.asList(paciente);
        when(pacienteRepository.findByAtivoTrue()).thenReturn(pacientes);

        // When
        List<Paciente> result = pacienteService.getActivePacientes();

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(paciente);
        verify(pacienteRepository).findByAtivoTrue();
    }

    @Test
    @DisplayName("Should get pacientes by nome successfully")
    void shouldGetPacientesByNomeSuccessfully() {
        // Given
        List<Paciente> pacientes = Arrays.asList(paciente);
        when(pacienteRepository.findByNomeContaining("João")).thenReturn(pacientes);

        // When
        List<Paciente> result = pacienteService.getPacientesByNome("João");

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(paciente);
        verify(pacienteRepository).findByNomeContaining("João");
    }

    @Test
    @DisplayName("Should update paciente successfully")
    void shouldUpdatePacienteSuccessfully() {
        // Given
        Paciente updateRequest = new Paciente();
        updateRequest.setCpf("98765432101");
        updateRequest.setDataNascimento(LocalDate.of(1985, 5, 15));
        updateRequest.setEndereco("Nova Rua, 456");
        updateRequest.setNumeroCartaoSus("987654321012345");
        updateRequest.setConvenioMedico("Bradesco Saúde");
        updateRequest.setContatoEmergencia("11888777666");
        updateRequest.setObservacoesMedicas("Paciente hipertenso");
        updateRequest.setAtivo(false);

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(pacienteRepository.existsByCpf("98765432101")).thenReturn(false);
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);

        // When
        Paciente result = pacienteService.updatePaciente(1L, updateRequest);

        // Then
        assertThat(result).isNotNull();
        verify(pacienteRepository).findById(1L);
        verify(pacienteRepository).existsByCpf("98765432101");
        verify(pacienteRepository).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Should throw exception when updating with existing CPF")
    void shouldThrowExceptionWhenUpdatingWithExistingCpf() {
        // Given
        Paciente updateRequest = new Paciente();
        updateRequest.setCpf("98765432101");

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(pacienteRepository.existsByCpf("98765432101")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> pacienteService.updatePaciente(1L, updateRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("CPF já existe")
                .hasMessageContaining("98765432101");
        
        verify(pacienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should allow updating paciente with same CPF")
    void shouldAllowUpdatingPacienteWithSameCpf() {
        // Given
        Paciente updateRequest = new Paciente();
        updateRequest.setCpf("12345678901"); // Same CPF as current paciente
        updateRequest.setEndereco("Nova Rua, 456");

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);

        // When
        Paciente result = pacienteService.updatePaciente(1L, updateRequest);

        // Then
        assertThat(result).isNotNull();
        verify(pacienteRepository).findById(1L);
        verify(pacienteRepository, never()).existsByCpf(any()); // Should not check since it's the same CPF
        verify(pacienteRepository).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent paciente")
    void shouldThrowExceptionWhenUpdatingNonExistentPaciente() {
        // Given
        Paciente updateRequest = new Paciente();
        when(pacienteRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> pacienteService.updatePaciente(1L, updateRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Paciente");
        
        verify(pacienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete paciente successfully")
    void shouldDeletePacienteSuccessfully() {
        // Given
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        // When
        pacienteService.deletePaciente(1L);

        // Then
        verify(pacienteRepository).findById(1L);
        verify(pacienteRepository).delete(paciente);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent paciente")
    void shouldThrowExceptionWhenDeletingNonExistentPaciente() {
        // Given
        when(pacienteRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> pacienteService.deletePaciente(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Paciente");
        
        verify(pacienteRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should deactivate paciente successfully")
    void shouldDeactivatePacienteSuccessfully() {
        // Given
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);

        // When
        pacienteService.deactivatePaciente(1L);

        // Then
        verify(pacienteRepository).findById(1L);
        verify(pacienteRepository).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Should throw exception when deactivating non-existent paciente")
    void shouldThrowExceptionWhenDeactivatingNonExistentPaciente() {
        // Given
        when(pacienteRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> pacienteService.deactivatePaciente(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Paciente");
        
        verify(pacienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should activate paciente successfully")
    void shouldActivatePacienteSuccessfully() {
        // Given
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);

        // When
        pacienteService.activatePaciente(1L);

        // Then
        verify(pacienteRepository).findById(1L);
        verify(pacienteRepository).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Should throw exception when activating non-existent paciente")
    void shouldThrowExceptionWhenActivatingNonExistentPaciente() {
        // Given
        when(pacienteRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> pacienteService.activatePaciente(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Paciente");
        
        verify(pacienteRepository, never()).save(any());
    }
}