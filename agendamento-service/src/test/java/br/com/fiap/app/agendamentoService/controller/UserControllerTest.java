package br.com.fiap.app.agendamentoService.controller;

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

import br.com.fiap.app.agendamentoService.entity.User;
import br.com.fiap.app.agendamentoService.enums.Role;
import br.com.fiap.app.agendamentoService.service.UserService;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController Tests")
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setNome("Test User");
        user.setEmail("test@example.com");
        user.setTelefone("11999999999");
        user.setRole(Role.ROLE_MEDICO);
        user.setActive(true);
    }

    @Test
    @DisplayName("Should create user and return 201")
    void shouldCreateUserAndReturn201() {
        when(userService.createUser(any(User.class))).thenReturn(user);

        ResponseEntity<User> response = userController.createUser(user);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(user);
        verify(userService).createUser(any(User.class));
    }

    @Test
    @DisplayName("Should create bootstrap admin and return 201")
    void shouldCreateBootstrapAdminAndReturn201() {
        when(userService.createBootstrapAdmin(any(User.class))).thenReturn(user);

        ResponseEntity<User> response = userController.createBootstrapAdmin(user);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(user);
        verify(userService).createBootstrapAdmin(any(User.class));
    }

    @Test
    @DisplayName("Should get user by id and return 200")
    void shouldGetUserByIdAndReturn200() {
        when(userService.getUserById(1L)).thenReturn(user);

        ResponseEntity<User> response = userController.getUserById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(user);
        verify(userService).getUserById(1L);
    }

    @Test
    @DisplayName("Should get user by username and return 200")
    void shouldGetUserByUsernameAndReturn200() {
        when(userService.getUserByUsername("testuser")).thenReturn(user);

        ResponseEntity<User> response = userController.getUserByUsername("testuser");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(user);
        verify(userService).getUserByUsername("testuser");
    }

    @Test
    @DisplayName("Should get all users and return 200")
    void shouldGetAllUsersAndReturn200() {
        List<User> users = Arrays.asList(user);
        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(userService).getAllUsers();
    }

    @Test
    @DisplayName("Should get users by role and return 200")
    void shouldGetUsersByRoleAndReturn200() {
        List<User> users = Arrays.asList(user);
        when(userService.getUsersByRole(Role.ROLE_MEDICO)).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getUsersByRole(Role.ROLE_MEDICO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(userService).getUsersByRole(Role.ROLE_MEDICO);
    }

    @Test
    @DisplayName("Should get active users and return 200")
    void shouldGetActiveUsersAndReturn200() {
        List<User> users = Arrays.asList(user);
        when(userService.getActiveUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getActiveUsers();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(userService).getActiveUsers();
    }

    @Test
    @DisplayName("Should update user and return 200")
    void shouldUpdateUserAndReturn200() {
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(user);

        ResponseEntity<User> response = userController.updateUser(1L, user);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(user);
        verify(userService).updateUser(eq(1L), any(User.class));
    }

    @Test
    @DisplayName("Should deactivate user and return 200")
    void shouldDeactivateUserAndReturn200() {
        doNothing().when(userService).deactivateUser(1L);

        ResponseEntity<Void> response = userController.deactivateUser(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userService).deactivateUser(1L);
    }

    @Test
    @DisplayName("Should activate user and return 200")
    void shouldActivateUserAndReturn200() {
        doNothing().when(userService).activateUser(1L);

        ResponseEntity<Void> response = userController.activateUser(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userService).activateUser(1L);
    }

    @Test
    @DisplayName("Should delete user and return 204")
    void shouldDeleteUserAndReturn204() {
        doNothing().when(userService).deleteUser(1L);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(userService).deleteUser(1L);
    }
}
