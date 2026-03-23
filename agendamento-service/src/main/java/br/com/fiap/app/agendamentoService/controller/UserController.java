package br.com.fiap.app.agendamentoService.controller;

import br.com.fiap.app.agendamentoService.dto.UserInfoResponse;
import br.com.fiap.app.agendamentoService.dto.UserRequestDTO;
import br.com.fiap.app.agendamentoService.entity.User;
import br.com.fiap.app.agendamentoService.enums.Role;
import br.com.fiap.app.agendamentoService.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<UserInfoResponse> createUser(@Valid @RequestBody UserRequestDTO request) {
        User user = toEntity(request);
        User saved = userService.createUser(user);
        return new ResponseEntity<>(toResponse(saved), HttpStatus.CREATED);
    }
    
    @PostMapping("/bootstrap-admin")
    public ResponseEntity<UserInfoResponse> createBootstrapAdmin(@Valid @RequestBody UserRequestDTO request) {
        User user = toEntity(request);
        User saved = userService.createBootstrapAdmin(user);
        return new ResponseEntity<>(toResponse(saved), HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO') or (hasAuthority('ROLE_PACIENTE') and #id == authentication.principal.id)")
    public ResponseEntity<UserInfoResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(userService.getUserById(id)));
    }
    
    @GetMapping("/username/{username}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<UserInfoResponse> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(toResponse(userService.getUserByUsername(username)));
    }
    
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<UserInfoResponse>> getAllUsers() {
        List<UserInfoResponse> result = userService.getAllUsers().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/role/{role}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<UserInfoResponse>> getUsersByRole(@PathVariable Role role) {
        List<UserInfoResponse> result = userService.getUsersByRole(role).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/active")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<UserInfoResponse>> getActiveUsers() {
        List<UserInfoResponse> result = userService.getActiveUsers().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO') or (hasAuthority('ROLE_PACIENTE') and #id == authentication.principal.id)")
    public ResponseEntity<UserInfoResponse> updateUser(@PathVariable Long id, @RequestBody UserRequestDTO request) {
        User existing = userService.getUserById(id);
        existing.setUsername(request.getUsername());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            existing.setPassword(request.getPassword());
        }
        existing.setNome(request.getNome());
        existing.setEmail(request.getEmail());
        existing.setTelefone(request.getTelefone());
        if (request.getRole() != null) {
            existing.setRole(request.getRole());
        }
        return ResponseEntity.ok(toResponse(userService.updateUser(id, existing)));
    }
    
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<Void> activateUser(@PathVariable Long id) {
        userService.activateUser(id);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    private User toEntity(UserRequestDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());
        user.setTelefone(dto.getTelefone());
        user.setRole(dto.getRole());
        return user;
    }

    private UserInfoResponse toResponse(User user) {
        return new UserInfoResponse(
                user.getId(),
                user.getUsername(),
                user.getNome(),
                user.getEmail(),
                user.getTelefone(),
                user.getRole(),
                user.getActive()
        );
    }
}