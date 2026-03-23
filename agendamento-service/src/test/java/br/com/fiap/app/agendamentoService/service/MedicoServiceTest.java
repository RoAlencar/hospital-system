package br.com.fiap.app.agendamentoService.service;

import br.com.fiap.app.agendamentoService.entity.Medico;
import br.com.fiap.app.agendamentoService.entity.User;
import br.com.fiap.app.agendamentoService.enums.Especialidade;
import br.com.fiap.app.agendamentoService.exception.BusinessException;
import br.com.fiap.app.agendamentoService.exception.ResourceNotFoundException;
import br.com.fiap.app.agendamentoService.repository.MedicoRepository;
import br.com.fiap.app.agendamentoService.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MedicoService Tests")
class MedicoServiceTest {

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MedicoService medicoService;

    private Medico medico;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setNome("Dr. João Silva");

        medico = new Medico();
        medico.setId(1L);
        medico.setUserId(1L);
        medico.setCrm("123456");
        medico.setEspecialidade(Especialidade.CARDIOLOGIA);
        medico.setDescricao("Cardiologista com 10 anos de experiência");
        medico.setAtivo(true);
        medico.setUser(user);
    }

    @Test
    @DisplayName("Should create medico successfully")
    void shouldCreateMedicoSuccessfully() {
        // Given
        when(medicoRepository.existsByCrm("123456")).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(medicoRepository.save(any(Medico.class))).thenReturn(medico);

        // When
        Medico result = medicoService.createMedico(medico);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAtivo()).isTrue();
        assertThat(result.getUser()).isEqualTo(user);
        
        verify(medicoRepository).existsByCrm("123456");
        verify(userRepository).findById(1L);
        verify(medicoRepository).save(any(Medico.class));
    }

    @Test
    @DisplayName("Should throw exception when CRM already exists")
    void shouldThrowExceptionWhenCrmAlreadyExists() {
        // Given
        when(medicoRepository.existsByCrm("123456")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> medicoService.createMedico(medico))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("CRM já existe")
                .hasMessageContaining("123456");
        
        verify(medicoRepository).existsByCrm("123456");
        verify(userRepository, never()).findById(any());
        verify(medicoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when user not found for medico creation")
    void shouldThrowExceptionWhenUserNotFoundForMedicoCreation() {
        // Given
        when(medicoRepository.existsByCrm("123456")).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> medicoService.createMedico(medico))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Usuário")
                .hasMessageContaining("ID")
                .hasMessageContaining("1");
        
        verify(medicoRepository).existsByCrm("123456");
        verify(userRepository).findById(1L);
        verify(medicoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should get medico by id successfully")
    void shouldGetMedicoByIdSuccessfully() {
        // Given
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));

        // When
        Medico result = medicoService.getMedicoById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getCrm()).isEqualTo("123456");
        verify(medicoRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when medico not found by id")
    void shouldThrowExceptionWhenMedicoNotFoundById() {
        // Given
        when(medicoRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> medicoService.getMedicoById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Médico")
                .hasMessageContaining("ID")
                .hasMessageContaining("1");
    }

    @Test
    @DisplayName("Should get medico by CRM successfully")
    void shouldGetMedicoByCrmSuccessfully() {
        // Given
        when(medicoRepository.findByCrm("123456")).thenReturn(Optional.of(medico));

        // When
        Medico result = medicoService.getMedicoByCrm("123456");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCrm()).isEqualTo("123456");
        verify(medicoRepository).findByCrm("123456");
    }

    @Test
    @DisplayName("Should throw exception when medico not found by CRM")
    void shouldThrowExceptionWhenMedicoNotFoundByCrm() {
        // Given
        when(medicoRepository.findByCrm("123456")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> medicoService.getMedicoByCrm("123456"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Médico")
                .hasMessageContaining("CRM")
                .hasMessageContaining("123456");
    }

    @Test
    @DisplayName("Should get medico by user id successfully")
    void shouldGetMedicoByUserIdSuccessfully() {
        // Given
        when(medicoRepository.findByUserId(1L)).thenReturn(Optional.of(medico));

        // When
        Medico result = medicoService.getMedicoByUserId(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(1L);
        verify(medicoRepository).findByUserId(1L);
    }

    @Test
    @DisplayName("Should throw exception when medico not found by user id")
    void shouldThrowExceptionWhenMedicoNotFoundByUserId() {
        // Given
        when(medicoRepository.findByUserId(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> medicoService.getMedicoByUserId(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Médico")
                .hasMessageContaining("User ID")
                .hasMessageContaining("1");
    }

    @Test
    @DisplayName("Should get all medicos successfully")
    void shouldGetAllMedicosSuccessfully() {
        // Given
        List<Medico> medicos = Arrays.asList(medico);
        when(medicoRepository.findAll()).thenReturn(medicos);

        // When
        List<Medico> result = medicoService.getAllMedicos();

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(medico);
        verify(medicoRepository).findAll();
    }

    @Test
    @DisplayName("Should get medicos by especialidade successfully")
    void shouldGetMedicosByEspecialidadeSuccessfully() {
        // Given
        List<Medico> medicos = Arrays.asList(medico);
        when(medicoRepository.findByEspecialidade(Especialidade.CARDIOLOGIA)).thenReturn(medicos);

        // When
        List<Medico> result = medicoService.getMedicosByEspecialidade(Especialidade.CARDIOLOGIA);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(medico);
        verify(medicoRepository).findByEspecialidade(Especialidade.CARDIOLOGIA);
    }

    @Test
    @DisplayName("Should get active medicos successfully")
    void shouldGetActiveMedicosSuccessfully() {
        // Given
        List<Medico> medicos = Arrays.asList(medico);
        when(medicoRepository.findByAtivoTrue()).thenReturn(medicos);

        // When
        List<Medico> result = medicoService.getActiveMedicos();

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(medico);
        verify(medicoRepository).findByAtivoTrue();
    }

    @Test
    @DisplayName("Should get medicos by nome successfully")
    void shouldGetMedicosByNomeSuccessfully() {
        // Given
        List<Medico> medicos = Arrays.asList(medico);
        when(medicoRepository.findByNomeContaining("João")).thenReturn(medicos);

        // When
        List<Medico> result = medicoService.getMedicosByNome("João");

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(medico);
        verify(medicoRepository).findByNomeContaining("João");
    }

    @Test
    @DisplayName("Should update medico successfully")
    void shouldUpdateMedicoSuccessfully() {
        // Given
        Medico updateRequest = new Medico();
        updateRequest.setCrm("654321");
        updateRequest.setEspecialidade(Especialidade.NEUROLOGIA);
        updateRequest.setDescricao("Neurologista experiente");
        updateRequest.setAtivo(false);

        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(medicoRepository.existsByCrm("654321")).thenReturn(false);
        when(medicoRepository.save(any(Medico.class))).thenReturn(medico);

        // When
        Medico result = medicoService.updateMedico(1L, updateRequest);

        // Then
        assertThat(result).isNotNull();
        verify(medicoRepository).findById(1L);
        verify(medicoRepository).existsByCrm("654321");
        verify(medicoRepository).save(any(Medico.class));
    }

    @Test
    @DisplayName("Should throw exception when updating with existing CRM")
    void shouldThrowExceptionWhenUpdatingWithExistingCrm() {
        // Given
        Medico updateRequest = new Medico();
        updateRequest.setCrm("654321");

        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(medicoRepository.existsByCrm("654321")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> medicoService.updateMedico(1L, updateRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("CRM já existe")
                .hasMessageContaining("654321");
        
        verify(medicoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should allow updating medico with same CRM")
    void shouldAllowUpdatingMedicoWithSameCrm() {
        // Given
        Medico updateRequest = new Medico();
        updateRequest.setCrm("123456"); // Same CRM as current medico
        updateRequest.setDescricao("Descrição atualizada");

        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(medicoRepository.save(any(Medico.class))).thenReturn(medico);

        // When
        Medico result = medicoService.updateMedico(1L, updateRequest);

        // Then
        assertThat(result).isNotNull();
        verify(medicoRepository).findById(1L);
        verify(medicoRepository, never()).existsByCrm(any()); // Should not check since it's the same CRM
        verify(medicoRepository).save(any(Medico.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent medico")
    void shouldThrowExceptionWhenUpdatingNonExistentMedico() {
        // Given
        Medico updateRequest = new Medico();
        when(medicoRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> medicoService.updateMedico(1L, updateRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Médico");
        
        verify(medicoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete medico successfully")
    void shouldDeleteMedicoSuccessfully() {
        // Given
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));

        // When
        medicoService.deleteMedico(1L);

        // Then
        verify(medicoRepository).findById(1L);
        verify(medicoRepository).delete(medico);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent medico")
    void shouldThrowExceptionWhenDeletingNonExistentMedico() {
        // Given
        when(medicoRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> medicoService.deleteMedico(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Médico");
        
        verify(medicoRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should deactivate medico successfully")
    void shouldDeactivateMedicoSuccessfully() {
        // Given
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(medicoRepository.save(any(Medico.class))).thenReturn(medico);

        // When
        medicoService.deactivateMedico(1L);

        // Then
        verify(medicoRepository).findById(1L);
        verify(medicoRepository).save(any(Medico.class));
    }

    @Test
    @DisplayName("Should throw exception when deactivating non-existent medico")
    void shouldThrowExceptionWhenDeactivatingNonExistentMedico() {
        // Given
        when(medicoRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> medicoService.deactivateMedico(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Médico");
        
        verify(medicoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should activate medico successfully")
    void shouldActivateMedicoSuccessfully() {
        // Given
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(medicoRepository.save(any(Medico.class))).thenReturn(medico);

        // When
        medicoService.activateMedico(1L);

        // Then
        verify(medicoRepository).findById(1L);
        verify(medicoRepository).save(any(Medico.class));
    }

    @Test
    @DisplayName("Should throw exception when activating non-existent medico")
    void shouldThrowExceptionWhenActivatingNonExistentMedico() {
        // Given
        when(medicoRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> medicoService.activateMedico(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Médico");
        
        verify(medicoRepository, never()).save(any());
    }
}