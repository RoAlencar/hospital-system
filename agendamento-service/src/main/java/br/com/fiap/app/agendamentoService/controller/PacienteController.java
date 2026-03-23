package br.com.fiap.app.agendamentoService.controller;

import br.com.fiap.app.agendamentoService.dto.PacienteRequestDTO;
import br.com.fiap.app.agendamentoService.dto.PacienteResponseDTO;
import br.com.fiap.app.agendamentoService.entity.Paciente;
import br.com.fiap.app.agendamentoService.mapper.PacienteMapper;
import br.com.fiap.app.agendamentoService.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {
    
    private final PacienteService pacienteService;
    
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<PacienteResponseDTO> createPaciente(@Valid @RequestBody PacienteRequestDTO request) {
        Paciente paciente = pacienteService.createPaciente(PacienteMapper.fromDTO(request));
        return new ResponseEntity<>(PacienteMapper.toDTO(paciente), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO') or " +
            "(hasAuthority('ROLE_PACIENTE') and @pacienteService.isOwnedByUser(#id, authentication.principal.id))")
    public ResponseEntity<PacienteResponseDTO> getPacienteById(@PathVariable Long id) {
        return ResponseEntity.ok(PacienteMapper.toDTO(pacienteService.getPacienteById(id)));
    }

    @GetMapping("/cpf/{cpf}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<PacienteResponseDTO> getPacienteByCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(PacienteMapper.toDTO(pacienteService.getPacienteByCpf(cpf)));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO') or hasAuthority('ROLE_PACIENTE')")
    public ResponseEntity<PacienteResponseDTO> getPacienteByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(PacienteMapper.toDTO(pacienteService.getPacienteByUserId(userId)));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<PacienteResponseDTO>> getAllPacientes() {
        List<PacienteResponseDTO> result = pacienteService.getAllPacientes().stream()
                .map(PacienteMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<PacienteResponseDTO>> getActivePacientes() {
        List<PacienteResponseDTO> result = pacienteService.getActivePacientes().stream()
                .map(PacienteMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<PacienteResponseDTO>> searchPacientesByNome(@RequestParam String nome) {
        List<PacienteResponseDTO> result = pacienteService.getPacientesByNome(nome).stream()
                .map(PacienteMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<PacienteResponseDTO> updatePaciente(@PathVariable Long id, @RequestBody PacienteRequestDTO request) {
        Paciente existing = pacienteService.getPacienteById(id);
        PacienteMapper.updateFromDTO(existing, request);
        return ResponseEntity.ok(PacienteMapper.toDTO(pacienteService.updatePaciente(id, existing)));
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