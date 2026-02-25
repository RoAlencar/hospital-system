package br.com.fiap.app.agendamentoService.controller;

import br.com.fiap.app.agendamentoService.entity.Medico;
import br.com.fiap.app.agendamentoService.enums.Especialidade;
import br.com.fiap.app.agendamentoService.service.MedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
@RequiredArgsConstructor
public class MedicoController {
    
    private final MedicoService medicoService;
    
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<Medico> createMedico(@Valid @RequestBody Medico request) {
        Medico medico = medicoService.createMedico(request);
        return new ResponseEntity<>(medico, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<Medico> getMedicoById(@PathVariable Long id) {
        return ResponseEntity.ok(medicoService.getMedicoById(id));
    }
    
    @GetMapping("/crm/{crm}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<Medico> getMedicoByCrm(@PathVariable String crm) {
        return ResponseEntity.ok(medicoService.getMedicoByCrm(crm));
    }
    
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<Medico> getMedicoByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(medicoService.getMedicoByUserId(userId));
    }
    
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<Medico>> getAllMedicos() {
        return ResponseEntity.ok(medicoService.getAllMedicos());
    }
    
    @GetMapping("/especialidade/{especialidade}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<Medico>> getMedicosByEspecialidade(@PathVariable Especialidade especialidade) {
        return ResponseEntity.ok(medicoService.getMedicosByEspecialidade(especialidade));
    }
    
    @GetMapping("/active")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<Medico>> getActiveMedicos() {
        return ResponseEntity.ok(medicoService.getActiveMedicos());
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<Medico>> searchMedicosByNome(@RequestParam String nome) {
        return ResponseEntity.ok(medicoService.getMedicosByNome(nome));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<Medico> updateMedico(@PathVariable Long id, @RequestBody Medico request) {
        return ResponseEntity.ok(medicoService.updateMedico(id, request));
    }
    
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<Void> deactivateMedico(@PathVariable Long id) {
        medicoService.deactivateMedico(id);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<Void> activateMedico(@PathVariable Long id) {
        medicoService.activateMedico(id);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO')")
    public ResponseEntity<Void> deleteMedico(@PathVariable Long id) {
        medicoService.deleteMedico(id);
        return ResponseEntity.noContent().build();
    }
}