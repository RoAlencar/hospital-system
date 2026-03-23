package br.com.fiap.app.agendamentoService.controller;

import br.com.fiap.app.agendamentoService.dto.MedicoRequestDTO;
import br.com.fiap.app.agendamentoService.dto.MedicoResponseDTO;
import br.com.fiap.app.agendamentoService.entity.Medico;
import br.com.fiap.app.agendamentoService.enums.Especialidade;
import br.com.fiap.app.agendamentoService.mapper.MedicoMapper;
import br.com.fiap.app.agendamentoService.service.MedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medicos")
@RequiredArgsConstructor
public class MedicoController {
    
    private final MedicoService medicoService;
    
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<MedicoResponseDTO> createMedico(@Valid @RequestBody MedicoRequestDTO request) {
        Medico medico = medicoService.createMedico(MedicoMapper.fromDTO(request));
        return new ResponseEntity<>(MedicoMapper.toDTO(medico), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<MedicoResponseDTO> getMedicoById(@PathVariable Long id) {
        return ResponseEntity.ok(MedicoMapper.toDTO(medicoService.getMedicoById(id)));
    }

    @GetMapping("/crm/{crm}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<MedicoResponseDTO> getMedicoByCrm(@PathVariable String crm) {
        return ResponseEntity.ok(MedicoMapper.toDTO(medicoService.getMedicoByCrm(crm)));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<MedicoResponseDTO> getMedicoByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(MedicoMapper.toDTO(medicoService.getMedicoByUserId(userId)));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<MedicoResponseDTO>> getAllMedicos() {
        List<MedicoResponseDTO> result = medicoService.getAllMedicos().stream()
                .map(MedicoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/especialidade/{especialidade}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<MedicoResponseDTO>> getMedicosByEspecialidade(@PathVariable Especialidade especialidade) {
        List<MedicoResponseDTO> result = medicoService.getMedicosByEspecialidade(especialidade).stream()
                .map(MedicoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<MedicoResponseDTO>> getActiveMedicos() {
        List<MedicoResponseDTO> result = medicoService.getActiveMedicos().stream()
                .map(MedicoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<MedicoResponseDTO>> searchMedicosByNome(@RequestParam String nome) {
        List<MedicoResponseDTO> result = medicoService.getMedicosByNome(nome).stream()
                .map(MedicoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<MedicoResponseDTO> updateMedico(@PathVariable Long id, @RequestBody MedicoRequestDTO request) {
        Medico existing = medicoService.getMedicoById(id);
        MedicoMapper.updateFromDTO(existing, request);
        return ResponseEntity.ok(MedicoMapper.toDTO(medicoService.updateMedico(id, existing)));
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