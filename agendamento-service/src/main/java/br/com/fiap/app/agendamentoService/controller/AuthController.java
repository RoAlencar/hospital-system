package br.com.fiap.app.agendamentoService.controller;

import br.com.fiap.app.agendamentoService.dto.LoginRequest;
import br.com.fiap.app.agendamentoService.dto.LoginResponse;
import br.com.fiap.app.agendamentoService.dto.RegisterRequest;
import br.com.fiap.app.agendamentoService.dto.UserInfoResponse;
import br.com.fiap.app.agendamentoService.entity.User;
import br.com.fiap.app.agendamentoService.enums.Role;
import br.com.fiap.app.agendamentoService.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();

            LoginResponse loginResponse = new LoginResponse(
                "Login realizado com sucesso",
                user.getId(),
                user.getUsername(),
                user.getRole().name()
            );

            return ResponseEntity.ok(loginResponse);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Credenciais inválidas");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            // Create new user with PACIENTE role
            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setPassword(registerRequest.getPassword());
            user.setNome(registerRequest.getNome());
            user.setEmail(registerRequest.getEmail());
            user.setTelefone(registerRequest.getTelefone());
            user.setRole(Role.ROLE_PACIENTE);

            User savedUser = userService.createUser(user);

            UserInfoResponse userInfo = new UserInfoResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getNome(),
                savedUser.getEmail(),
                savedUser.getTelefone(),
                savedUser.getRole(),
                savedUser.getActive()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(userInfo);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Erro ao registrar usuário: " + e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = (User) authentication.getPrincipal();
        UserInfoResponse userInfo = new UserInfoResponse(
            user.getId(),
            user.getUsername(),
            user.getNome(),
            user.getEmail(),
            user.getTelefone(),
            user.getRole(),
            user.getActive()
        );

        return ResponseEntity.ok(userInfo);
    }
}