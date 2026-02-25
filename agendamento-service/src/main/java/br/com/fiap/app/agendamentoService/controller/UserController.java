package br.com.fiap.app.agendamentoService.controller;

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

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<User> createUser(@Valid @RequestBody User request) {
        User user = userService.createUser(request);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
    
    @PostMapping("/bootstrap-admin")
    public ResponseEntity<User> createBootstrapAdmin(@Valid @RequestBody User request) {
        User user = userService.createBootstrapAdmin(request);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO') or (hasAuthority('ROLE_PACIENTE') and #id == authentication.principal.id)")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
    
    @GetMapping("/username/{username}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }
    
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
    @GetMapping("/role/{role}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable Role role) {
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }
    
    @GetMapping("/active")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<User>> getActiveUsers() {
        return ResponseEntity.ok(userService.getActiveUsers());
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO') or (hasAuthority('ROLE_PACIENTE') and #id == authentication.principal.id)")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
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
}