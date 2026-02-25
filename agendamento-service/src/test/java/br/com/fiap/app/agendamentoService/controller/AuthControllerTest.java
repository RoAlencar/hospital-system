package br.com.fiap.app.agendamentoService.controller;

import br.com.fiap.app.agendamentoService.dto.LoginRequest;
import br.com.fiap.app.agendamentoService.dto.RegisterRequest;
import br.com.fiap.app.agendamentoService.entity.User;
import br.com.fiap.app.agendamentoService.enums.Role;
import br.com.fiap.app.agendamentoService.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController Tests")
class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthController authController;

    private User user;
    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setNome("Test User");
        user.setEmail("test@example.com");
        user.setTelefone("11999999999");
        user.setRole(Role.ROLE_PACIENTE);
        user.setActive(true);

        loginRequest = new LoginRequest("testuser", "password123");

        registerRequest = new RegisterRequest();
        registerRequest.setUsername("newuser");
        registerRequest.setPassword("password123");
        registerRequest.setNome("New User");
        registerRequest.setEmail("newuser@example.com");
        registerRequest.setTelefone("11888888888");
    }

    @Test
    @DisplayName("Should authenticate user successfully")
    void shouldAuthenticateUserSuccessfully() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        // When
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    @DisplayName("Should return 401 when credentials are invalid")
    void shouldReturn401WhenCredentialsAreInvalid() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // When
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(401);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    @DisplayName("Should register new patient successfully")
    void shouldRegisterNewPatientSuccessfully() {
        // Given
        User savedUser = new User();
        savedUser.setId(2L);
        savedUser.setUsername("newuser");
        savedUser.setNome("New User");
        savedUser.setEmail("newuser@example.com");
        savedUser.setTelefone("11888888888");
        savedUser.setRole(Role.ROLE_PACIENTE);
        savedUser.setActive(true);

        when(userService.createUser(any(User.class))).thenReturn(savedUser);

        // When
        ResponseEntity<?> response = authController.registerUser(registerRequest);

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        verify(userService).createUser(any(User.class));
    }

    @Test
    @DisplayName("Should return 400 when registration fails")
    void shouldReturn400WhenRegistrationFails() {
        // Given
        when(userService.createUser(any(User.class)))
                .thenThrow(new RuntimeException("Username já existe"));

        // When
        ResponseEntity<?> response = authController.registerUser(registerRequest);

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        verify(userService).createUser(any(User.class));
    }

    @Test
    @DisplayName("Should return current user info when authenticated")
    void shouldReturnCurrentUserInfoWhenAuthenticated() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(authentication.isAuthenticated()).thenReturn(true);

        // When
        ResponseEntity<?> response = authController.getCurrentUser(authentication);

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    @DisplayName("Should return 401 when trying to access /me without authentication")
    void shouldReturn401WhenTryingToAccessMeWithoutAuthentication() {
        // When
        ResponseEntity<?> response = authController.getCurrentUser(null);

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(401);
    }
}