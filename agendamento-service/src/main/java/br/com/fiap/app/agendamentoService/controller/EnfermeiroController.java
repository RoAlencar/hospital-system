package br.com.fiap.app.agendamentoService.controller;

import br.com.fiap.app.agendamentoService.entity.Enfermeiro;
import br.com.fiap.app.agendamentoService.service.EnfermeiroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enfermeiros")
@RequiredArgsConstructor
public class EnfermeiroController {
    
    private final EnfermeiroService enfermeiroService;
    
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<Enfermeiro> createEnfermeiro(@Valid @RequestBody Enfermeiro request) {
        Enfermeiro enfermeiro = enfermeiroService.createEnfermeiro(request);
        return new ResponseEntity<>(enfermeiro, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<Enfermeiro> getEnfermeiroById(@PathVariable Long id) {
        return ResponseEntity.ok(enfermeiroService.getEnfermeiroById(id));
    }
    
    @GetMapping("/coren/{coren}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<Enfermeiro> getEnfermeiroByCoren(@PathVariable String coren) {
        return ResponseEntity.ok(enfermeiroService.getEnfermeiroByCoren(coren));
    }
    
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<Enfermeiro> getEnfermeiroByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(enfermeiroService.getEnfermeiroByUserId(userId));
    }
    
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<Enfermeiro>> getAllEnfermeiros() {
        return ResponseEntity.ok(enfermeiroService.getAllEnfermeiros());
    }
    
    @GetMapping("/setor/{setor}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<Enfermeiro>> getEnfermeirosBySetor(@PathVariable String setor) {
        return ResponseEntity.ok(enfermeiroService.getEnfermeirosBySetor(setor));
    }
    
    @GetMapping("/turno/{turno}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<Enfermeiro>> getEnfermeirosByTurno(@PathVariable String turno) {
        return ResponseEntity.ok(enfermeiroService.getEnfermeirosByTurno(turno));
    }
    
    @GetMapping("/setor/{setor}/turno/{turno}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<Enfermeiro>> getEnfermeirosBySetorAndTurno(
            @PathVariable String setor, @PathVariable String turno) {
        return ResponseEntity.ok(enfermeiroService.getEnfermeirosBySetorAndTurno(setor, turno));
    }
    
    @GetMapping("/especializacao/{especializacao}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<Enfermeiro>> getEnfermeirosByEspecializacao(@PathVariable String especializacao) {
        return ResponseEntity.ok(enfermeiroService.getEnfermeirosByEspecializacao(especializacao));
    }
    
    @GetMapping("/active")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<Enfermeiro>> getActiveEnfermeiros() {
        return ResponseEntity.ok(enfermeiroService.getActiveEnfermeiros());
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<Enfermeiro>> searchEnfermeirosByNome(@RequestParam String nome) {
        return ResponseEntity.ok(enfermeiroService.getEnfermeirosByNome(nome));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<Enfermeiro> updateEnfermeiro(@PathVariable Long id, @RequestBody Enfermeiro request) {
        return ResponseEntity.ok(enfermeiroService.updateEnfermeiro(id, request));
    }
    
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<Void> deactivateEnfermeiro(@PathVariable Long id) {
        enfermeiroService.deactivateEnfermeiro(id);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<Void> activateEnfermeiro(@PathVariable Long id) {
        enfermeiroService.activateEnfermeiro(id);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO')")
    public ResponseEntity<Void> deleteEnfermeiro(@PathVariable Long id) {
        enfermeiroService.deleteEnfermeiro(id);
        return ResponseEntity.noContent().build();
    }
}