package br.com.fiap.app.agendamentoService.controller;

import br.com.fiap.app.agendamentoService.entity.Paciente;
import br.com.fiap.app.agendamentoService.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {
    
    private final PacienteService pacienteService;
    
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<Paciente> createPaciente(@Valid @RequestBody Paciente request) {
        Paciente paciente = pacienteService.createPaciente(request);
        return new ResponseEntity<>(paciente, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO') or hasAuthority('ROLE_PACIENTE')")
    public ResponseEntity<Paciente> getPacienteById(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.getPacienteById(id));
    }
    
    @GetMapping("/cpf/{cpf}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<Paciente> getPacienteByCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(pacienteService.getPacienteByCpf(cpf));
    }
    
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO') or hasAuthority('ROLE_PACIENTE')")
    public ResponseEntity<Paciente> getPacienteByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(pacienteService.getPacienteByUserId(userId));
    }
    
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<Paciente>> getAllPacientes() {
        return ResponseEntity.ok(pacienteService.getAllPacientes());
    }
    
    @GetMapping("/active")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<Paciente>> getActivePacientes() {
        return ResponseEntity.ok(pacienteService.getActivePacientes());
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<Paciente>> searchPacientesByNome(@RequestParam String nome) {
        return ResponseEntity.ok(pacienteService.getPacientesByNome(nome));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<Paciente> updatePaciente(@PathVariable Long id, @RequestBody Paciente request) {
        return ResponseEntity.ok(pacienteService.updatePaciente(id, request));
    }
    
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<Void> deactivatePaciente(@PathVariable Long id) {
        pacienteService.deactivatePaciente(id);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<Void> activatePaciente(@PathVariable Long id) {
        pacienteService.activatePaciente(id);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO')")
    public ResponseEntity<Void> deletePaciente(@PathVariable Long id) {
        pacienteService.deletePaciente(id);
        return ResponseEntity.noContent().build();
    }
}