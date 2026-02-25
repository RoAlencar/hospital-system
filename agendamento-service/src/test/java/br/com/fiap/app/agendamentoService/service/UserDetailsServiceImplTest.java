package br.com.fiap.app.agendamentoService.service;

import br.com.fiap.app.agendamentoService.entity.User;
import br.com.fiap.app.agendamentoService.enums.Role;
import br.com.fiap.app.agendamentoService.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserDetailsServiceImpl Tests")
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setNome("Test User");
        user.setTelefone("11999999999");
        user.setRole(Role.ROLE_PACIENTE);
        user.setActive(true);
    }

    @Test
    @DisplayName("Should load user by username successfully")
    void shouldLoadUserByUsernameSuccessfully() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // When
        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
        assertThat(result.getPassword()).isEqualTo("encodedPassword");
        assertThat(result.isEnabled()).isTrue();
        assertThat(result.isAccountNonExpired()).isTrue();
        assertThat(result.isAccountNonLocked()).isTrue();
        assertThat(result.isCredentialsNonExpired()).isTrue();
        assertThat(result.getAuthorities()).hasSize(1);
        assertThat(result.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_PACIENTE");
        
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user not found")
    void shouldThrowUsernameNotFoundExceptionWhenUserNotFound() {
        // Given
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("nonexistent"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Usuário não encontrado")
                .hasMessageContaining("nonexistent");

        verify(userRepository).findByUsername("nonexistent");
    }

    @Test
    @DisplayName("Should load user with ROLE_MEDICO role")
    void shouldLoadUserWithMedicoRole() {
        // Given
        user.setRole(Role.ROLE_MEDICO);
        when(userRepository.findByUsername("medico")).thenReturn(Optional.of(user));

        // When
        UserDetails result = userDetailsService.loadUserByUsername("medico");

        // Then
        assertThat(result.getAuthorities()).hasSize(1);
        assertThat(result.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_MEDICO");
    }

    @Test
    @DisplayName("Should load user with ROLE_ENFERMEIRO role")
    void shouldLoadUserWithEnfermeiroRole() {
        // Given
        user.setRole(Role.ROLE_ENFERMEIRO);
        when(userRepository.findByUsername("enfermeiro")).thenReturn(Optional.of(user));

        // When
        UserDetails result = userDetailsService.loadUserByUsername("enfermeiro");

        // Then
        assertThat(result.getAuthorities()).hasSize(1);
        assertThat(result.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_ENFERMEIRO");
    }

    @Test
    @DisplayName("Should return false for isEnabled when user is inactive")
    void shouldReturnFalseForIsEnabledWhenUserIsInactive() {
        // Given
        user.setActive(false);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // When
        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        // Then
        assertThat(result.isEnabled()).isFalse();
    }
}