package br.com.fiap.app.agendamentoService.service;

import br.com.fiap.app.agendamentoService.entity.Enfermeiro;
import br.com.fiap.app.agendamentoService.entity.User;
import br.com.fiap.app.agendamentoService.exception.BusinessException;
import br.com.fiap.app.agendamentoService.exception.ResourceNotFoundException;
import br.com.fiap.app.agendamentoService.repository.EnfermeiroRepository;
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
@DisplayName("EnfermeiroService Tests")
class EnfermeiroServiceTest {

    @Mock
    private EnfermeiroRepository enfermeiroRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EnfermeiroService enfermeiroService;

    private Enfermeiro enfermeiro;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setNome("Maria Silva");

        enfermeiro = new Enfermeiro();
        enfermeiro.setId(1L);
        enfermeiro.setUserId(1L);
        enfermeiro.setCoren("123456");
        enfermeiro.setSetor("UTI");
        enfermeiro.setTurno("Noturno");
        enfermeiro.setEspecializacao("Cardiologia");
        enfermeiro.setDescricao("Enfermeira especializada em UTI Cardiológica");
        enfermeiro.setAtivo(true);
        enfermeiro.setUser(user);
    }

    @Test
    @DisplayName("Should create enfermeiro successfully")
    void shouldCreateEnfermeiroSuccessfully() {
        // Given
        when(enfermeiroRepository.existsByCoren("123456")).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(enfermeiroRepository.save(any(Enfermeiro.class))).thenReturn(enfermeiro);

        // When
        Enfermeiro result = enfermeiroService.createEnfermeiro(enfermeiro);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAtivo()).isTrue();
        assertThat(result.getUser()).isEqualTo(user);
        
        verify(enfermeiroRepository).existsByCoren("123456");
        verify(userRepository).findById(1L);
        verify(enfermeiroRepository).save(any(Enfermeiro.class));
    }

    @Test
    @DisplayName("Should throw exception when COREN already exists")
    void shouldThrowExceptionWhenCorenAlreadyExists() {
        // Given
        when(enfermeiroRepository.existsByCoren("123456")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> enfermeiroService.createEnfermeiro(enfermeiro))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("COREN já existe")
                .hasMessageContaining("123456");
        
        verify(enfermeiroRepository).existsByCoren("123456");
        verify(userRepository, never()).findById(any());
        verify(enfermeiroRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when user not found for enfermeiro creation")
    void shouldThrowExceptionWhenUserNotFoundForEnfermeiroCreation() {
        // Given
        when(enfermeiroRepository.existsByCoren("123456")).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> enfermeiroService.createEnfermeiro(enfermeiro))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Usuário")
                .hasMessageContaining("ID")
                .hasMessageContaining("1");
        
        verify(enfermeiroRepository).existsByCoren("123456");
        verify(userRepository).findById(1L);
        verify(enfermeiroRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should get enfermeiro by id successfully")
    void shouldGetEnfermeiroByIdSuccessfully() {
        // Given
        when(enfermeiroRepository.findById(1L)).thenReturn(Optional.of(enfermeiro));

        // When
        Enfermeiro result = enfermeiroService.getEnfermeiroById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getCoren()).isEqualTo("123456");
        verify(enfermeiroRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when enfermeiro not found by id")
    void shouldThrowExceptionWhenEnfermeiroNotFoundById() {
        // Given
        when(enfermeiroRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> enfermeiroService.getEnfermeiroById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Enfermeiro")
                .hasMessageContaining("ID")
                .hasMessageContaining("1");
    }

    @Test
    @DisplayName("Should get enfermeiro by COREN successfully")
    void shouldGetEnfermeiroByCorenSuccessfully() {
        // Given
        when(enfermeiroRepository.findByCoren("123456")).thenReturn(Optional.of(enfermeiro));

        // When
        Enfermeiro result = enfermeiroService.getEnfermeiroByCoren("123456");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCoren()).isEqualTo("123456");
        verify(enfermeiroRepository).findByCoren("123456");
    }

    @Test
    @DisplayName("Should throw exception when enfermeiro not found by COREN")
    void shouldThrowExceptionWhenEnfermeiroNotFoundByCoren() {
        // Given
        when(enfermeiroRepository.findByCoren("123456")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> enfermeiroService.getEnfermeiroByCoren("123456"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Enfermeiro")
                .hasMessageContaining("COREN")
                .hasMessageContaining("123456");
    }

    @Test
    @DisplayName("Should get enfermeiro by user id successfully")
    void shouldGetEnfermeiroByUserIdSuccessfully() {
        // Given
        when(enfermeiroRepository.findByUserId(1L)).thenReturn(Optional.of(enfermeiro));

        // When
        Enfermeiro result = enfermeiroService.getEnfermeiroByUserId(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(1L);
        verify(enfermeiroRepository).findByUserId(1L);
    }

    @Test
    @DisplayName("Should throw exception when enfermeiro not found by user id")
    void shouldThrowExceptionWhenEnfermeiroNotFoundByUserId() {
        // Given
        when(enfermeiroRepository.findByUserId(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> enfermeiroService.getEnfermeiroByUserId(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Enfermeiro")
                .hasMessageContaining("User ID")
                .hasMessageContaining("1");
    }

    @Test
    @DisplayName("Should get all enfermeiros successfully")
    void shouldGetAllEnfermeirosSuccessfully() {
        // Given
        List<Enfermeiro> enfermeiros = Arrays.asList(enfermeiro);
        when(enfermeiroRepository.findAll()).thenReturn(enfermeiros);

        // When
        List<Enfermeiro> result = enfermeiroService.getAllEnfermeiros();

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(enfermeiro);
        verify(enfermeiroRepository).findAll();
    }

    @Test
    @DisplayName("Should get enfermeiros by setor successfully")
    void shouldGetEnfermeirosBySetorSuccessfully() {
        // Given
        List<Enfermeiro> enfermeiros = Arrays.asList(enfermeiro);
        when(enfermeiroRepository.findBySetor("UTI")).thenReturn(enfermeiros);

        // When
        List<Enfermeiro> result = enfermeiroService.getEnfermeirosBySetor("UTI");

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(enfermeiro);
        verify(enfermeiroRepository).findBySetor("UTI");
    }

    @Test
    @DisplayName("Should get enfermeiros by turno successfully")
    void shouldGetEnfermeirosByTurnoSuccessfully() {
        // Given
        List<Enfermeiro> enfermeiros = Arrays.asList(enfermeiro);
        when(enfermeiroRepository.findByTurno("Noturno")).thenReturn(enfermeiros);

        // When
        List<Enfermeiro> result = enfermeiroService.getEnfermeirosByTurno("Noturno");

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(enfermeiro);
        verify(enfermeiroRepository).findByTurno("Noturno");
    }

    @Test
    @DisplayName("Should get active enfermeiros successfully")
    void shouldGetActiveEnfermeirosSuccessfully() {
        // Given
        List<Enfermeiro> enfermeiros = Arrays.asList(enfermeiro);
        when(enfermeiroRepository.findByAtivoTrue()).thenReturn(enfermeiros);

        // When
        List<Enfermeiro> result = enfermeiroService.getActiveEnfermeiros();

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(enfermeiro);
        verify(enfermeiroRepository).findByAtivoTrue();
    }

    @Test
    @DisplayName("Should get enfermeiros by nome successfully")
    void shouldGetEnfermeirosByNomeSuccessfully() {
        // Given
        List<Enfermeiro> enfermeiros = Arrays.asList(enfermeiro);
        when(enfermeiroRepository.findByNomeContaining("Maria")).thenReturn(enfermeiros);

        // When
        List<Enfermeiro> result = enfermeiroService.getEnfermeirosByNome("Maria");

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(enfermeiro);
        verify(enfermeiroRepository).findByNomeContaining("Maria");
    }

    @Test
    @DisplayName("Should get enfermeiros by setor and turno successfully")
    void shouldGetEnfermeirosBySetorAndTurnoSuccessfully() {
        // Given
        List<Enfermeiro> enfermeiros = Arrays.asList(enfermeiro);
        when(enfermeiroRepository.findBySetorAndTurno("UTI", "Noturno")).thenReturn(enfermeiros);

        // When
        List<Enfermeiro> result = enfermeiroService.getEnfermeirosBySetorAndTurno("UTI", "Noturno");

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(enfermeiro);
        verify(enfermeiroRepository).findBySetorAndTurno("UTI", "Noturno");
    }

    @Test
    @DisplayName("Should get enfermeiros by especializacao successfully")
    void shouldGetEnfermeirosByEspecializacaoSuccessfully() {
        // Given
        List<Enfermeiro> enfermeiros = Arrays.asList(enfermeiro);
        when(enfermeiroRepository.findByEspecializacaoAndAtivoTrue("Cardiologia")).thenReturn(enfermeiros);

        // When
        List<Enfermeiro> result = enfermeiroService.getEnfermeirosByEspecializacao("Cardiologia");

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(enfermeiro);
        verify(enfermeiroRepository).findByEspecializacaoAndAtivoTrue("Cardiologia");
    }

    @Test
    @DisplayName("Should update enfermeiro successfully")
    void shouldUpdateEnfermeiroSuccessfully() {
        // Given
        Enfermeiro updateRequest = new Enfermeiro();
        updateRequest.setCoren("654321");
        updateRequest.setSetor("Emergência");
        updateRequest.setTurno("Diurno");
        updateRequest.setEspecializacao("Emergência");
        updateRequest.setDescricao("Enfermeiro especializado em emergência");
        updateRequest.setAtivo(false);

        when(enfermeiroRepository.findById(1L)).thenReturn(Optional.of(enfermeiro));
        when(enfermeiroRepository.existsByCoren("654321")).thenReturn(false);
        when(enfermeiroRepository.save(any(Enfermeiro.class))).thenReturn(enfermeiro);

        // When
        Enfermeiro result = enfermeiroService.updateEnfermeiro(1L, updateRequest);

        // Then
        assertThat(result).isNotNull();
        verify(enfermeiroRepository).findById(1L);
        verify(enfermeiroRepository).existsByCoren("654321");
        verify(enfermeiroRepository).save(any(Enfermeiro.class));
    }

    @Test
    @DisplayName("Should throw exception when updating with existing COREN")
    void shouldThrowExceptionWhenUpdatingWithExistingCoren() {
        // Given
        Enfermeiro updateRequest = new Enfermeiro();
        updateRequest.setCoren("654321");

        when(enfermeiroRepository.findById(1L)).thenReturn(Optional.of(enfermeiro));
        when(enfermeiroRepository.existsByCoren("654321")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> enfermeiroService.updateEnfermeiro(1L, updateRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("COREN já existe")
                .hasMessageContaining("654321");
        
        verify(enfermeiroRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should allow updating enfermeiro with same COREN")
    void shouldAllowUpdatingEnfermeiroWithSameCoren() {
        // Given
        Enfermeiro updateRequest = new Enfermeiro();
        updateRequest.setCoren("123456"); // Same COREN as current enfermeiro
        updateRequest.setDescricao("Descrição atualizada");

        when(enfermeiroRepository.findById(1L)).thenReturn(Optional.of(enfermeiro));
        when(enfermeiroRepository.save(any(Enfermeiro.class))).thenReturn(enfermeiro);

        // When
        Enfermeiro result = enfermeiroService.updateEnfermeiro(1L, updateRequest);

        // Then
        assertThat(result).isNotNull();
        verify(enfermeiroRepository).findById(1L);
        verify(enfermeiroRepository, never()).existsByCoren(any()); // Should not check since it's the same COREN
        verify(enfermeiroRepository).save(any(Enfermeiro.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent enfermeiro")
    void shouldThrowExceptionWhenUpdatingNonExistentEnfermeiro() {
        // Given
        Enfermeiro updateRequest = new Enfermeiro();
        when(enfermeiroRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> enfermeiroService.updateEnfermeiro(1L, updateRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Enfermeiro");
        
        verify(enfermeiroRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete enfermeiro successfully")
    void shouldDeleteEnfermeiroSuccessfully() {
        // Given
        when(enfermeiroRepository.findById(1L)).thenReturn(Optional.of(enfermeiro));

        // When
        enfermeiroService.deleteEnfermeiro(1L);

        // Then
        verify(enfermeiroRepository).findById(1L);
        verify(enfermeiroRepository).delete(enfermeiro);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent enfermeiro")
    void shouldThrowExceptionWhenDeletingNonExistentEnfermeiro() {
        // Given
        when(enfermeiroRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> enfermeiroService.deleteEnfermeiro(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Enfermeiro");
        
        verify(enfermeiroRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should deactivate enfermeiro successfully")
    void shouldDeactivateEnfermeiroSuccessfully() {
        // Given
        when(enfermeiroRepository.findById(1L)).thenReturn(Optional.of(enfermeiro));
        when(enfermeiroRepository.save(any(Enfermeiro.class))).thenReturn(enfermeiro);

        // When
        enfermeiroService.deactivateEnfermeiro(1L);

        // Then
        verify(enfermeiroRepository).findById(1L);
        verify(enfermeiroRepository).save(any(Enfermeiro.class));
    }

    @Test
    @DisplayName("Should throw exception when deactivating non-existent enfermeiro")
    void shouldThrowExceptionWhenDeactivatingNonExistentEnfermeiro() {
        // Given
        when(enfermeiroRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> enfermeiroService.deactivateEnfermeiro(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Enfermeiro");
        
        verify(enfermeiroRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should activate enfermeiro successfully")
    void shouldActivateEnfermeiroSuccessfully() {
        // Given
        when(enfermeiroRepository.findById(1L)).thenReturn(Optional.of(enfermeiro));
        when(enfermeiroRepository.save(any(Enfermeiro.class))).thenReturn(enfermeiro);

        // When
        enfermeiroService.activateEnfermeiro(1L);

        // Then
        verify(enfermeiroRepository).findById(1L);
        verify(enfermeiroRepository).save(any(Enfermeiro.class));
    }

    @Test
    @DisplayName("Should throw exception when activating non-existent enfermeiro")
    void shouldThrowExceptionWhenActivatingNonExistentEnfermeiro() {
        // Given
        when(enfermeiroRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> enfermeiroService.activateEnfermeiro(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Enfermeiro");
        
        verify(enfermeiroRepository, never()).save(any());
    }
}