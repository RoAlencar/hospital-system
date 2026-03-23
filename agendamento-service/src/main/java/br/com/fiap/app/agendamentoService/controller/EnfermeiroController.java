package br.com.fiap.app.agendamentoService.controller;

import br.com.fiap.app.agendamentoService.dto.EnfermeiroRequestDTO;
import br.com.fiap.app.agendamentoService.dto.EnfermeiroResponseDTO;
import br.com.fiap.app.agendamentoService.entity.Enfermeiro;
import br.com.fiap.app.agendamentoService.mapper.EnfermeiroMapper;
import br.com.fiap.app.agendamentoService.service.EnfermeiroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/enfermeiros")
@RequiredArgsConstructor
public class EnfermeiroController {
    
    private final EnfermeiroService enfermeiroService;
    
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<EnfermeiroResponseDTO> createEnfermeiro(@Valid @RequestBody EnfermeiroRequestDTO request) {
        Enfermeiro enfermeiro = enfermeiroService.createEnfermeiro(EnfermeiroMapper.fromDTO(request));
        return new ResponseEntity<>(EnfermeiroMapper.toDTO(enfermeiro), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<EnfermeiroResponseDTO> getEnfermeiroById(@PathVariable Long id) {
        return ResponseEntity.ok(EnfermeiroMapper.toDTO(enfermeiroService.getEnfermeiroById(id)));
    }

    @GetMapping("/coren/{coren}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<EnfermeiroResponseDTO> getEnfermeiroByCoren(@PathVariable String coren) {
        return ResponseEntity.ok(EnfermeiroMapper.toDTO(enfermeiroService.getEnfermeiroByCoren(coren)));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<EnfermeiroResponseDTO> getEnfermeiroByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(EnfermeiroMapper.toDTO(enfermeiroService.getEnfermeiroByUserId(userId)));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<EnfermeiroResponseDTO>> getAllEnfermeiros() {
        List<EnfermeiroResponseDTO> result = enfermeiroService.getAllEnfermeiros().stream()
                .map(EnfermeiroMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/setor/{setor}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<EnfermeiroResponseDTO>> getEnfermeirosBySetor(@PathVariable String setor) {
        List<EnfermeiroResponseDTO> result = enfermeiroService.getEnfermeirosBySetor(setor).stream()
                .map(EnfermeiroMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/turno/{turno}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<EnfermeiroResponseDTO>> getEnfermeirosByTurno(@PathVariable String turno) {
        List<EnfermeiroResponseDTO> result = enfermeiroService.getEnfermeirosByTurno(turno).stream()
                .map(EnfermeiroMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/setor/{setor}/turno/{turno}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<EnfermeiroResponseDTO>> getEnfermeirosBySetorAndTurno(
            @PathVariable String setor, @PathVariable String turno) {
        List<EnfermeiroResponseDTO> result = enfermeiroService.getEnfermeirosBySetorAndTurno(setor, turno).stream()
                .map(EnfermeiroMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/especializacao/{especializacao}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<EnfermeiroResponseDTO>> getEnfermeirosByEspecializacao(@PathVariable String especializacao) {
        List<EnfermeiroResponseDTO> result = enfermeiroService.getEnfermeirosByEspecializacao(especializacao).stream()
                .map(EnfermeiroMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<EnfermeiroResponseDTO>> getActiveEnfermeiros() {
        List<EnfermeiroResponseDTO> result = enfermeiroService.getActiveEnfermeiros().stream()
                .map(EnfermeiroMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<EnfermeiroResponseDTO>> searchEnfermeirosByNome(@RequestParam String nome) {
        List<EnfermeiroResponseDTO> result = enfermeiroService.getEnfermeirosByNome(nome).stream()
                .map(EnfermeiroMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<EnfermeiroResponseDTO> updateEnfermeiro(@PathVariable Long id, @RequestBody EnfermeiroRequestDTO request) {
        Enfermeiro existing = enfermeiroService.getEnfermeiroById(id);
        EnfermeiroMapper.updateFromDTO(existing, request);
        return ResponseEntity.ok(EnfermeiroMapper.toDTO(enfermeiroService.updateEnfermeiro(id, existing)));
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