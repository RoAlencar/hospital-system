package br.com.fiap.app.agendamentoService.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.app.agendamentoService.dto.ConsultaRequestDTO;
import br.com.fiap.app.agendamentoService.dto.ConsultaResponseDTO;
import br.com.fiap.app.agendamentoService.entity.Consulta;
import br.com.fiap.app.agendamentoService.enums.StatusConsulta;
import br.com.fiap.app.agendamentoService.mapper.ConsultaMapper;
import br.com.fiap.app.agendamentoService.service.ConsultaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/consultas")
@RequiredArgsConstructor
public class ConsultaController {
    
    private final ConsultaService consultaService;
    
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<ConsultaResponseDTO> createConsulta(@Valid @RequestBody ConsultaRequestDTO request) {
        Consulta consulta = consultaService.createConsulta(ConsultaMapper.fromDTO(request));
        return new ResponseEntity<>(ConsultaMapper.toDTO(consulta), HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO') or hasAuthority('ROLE_PACIENTE')")
    public ResponseEntity<ConsultaResponseDTO> getConsultaById(@PathVariable Long id) {
        return ResponseEntity.ok(ConsultaMapper.toDTO(consultaService.getConsultaById(id)));
    }
    
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<ConsultaResponseDTO>> getAllConsultas() {
        return ResponseEntity.ok(consultaService.getAllConsultasDTO());
    }
    
    @GetMapping("/medico/{medicoId}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<ConsultaResponseDTO>> getConsultasByMedico(@PathVariable Long medicoId) {
        return ResponseEntity.ok(consultaService.getConsultasByMedicoDTO(medicoId));
    }
    
    @GetMapping("/paciente/{pacienteId}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO') or (hasAuthority('ROLE_PACIENTE') and @pacienteService.isOwnedByUser(#pacienteId, authentication.principal.id))")
    public ResponseEntity<List<ConsultaResponseDTO>> getConsultasByPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(consultaService.getConsultasByPacienteDTO(pacienteId));
    }
    
    @GetMapping("/paciente/{pacienteId}/futuras")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO') or (hasAuthority('ROLE_PACIENTE') and @pacienteService.isOwnedByUser(#pacienteId, authentication.principal.id))")
    public ResponseEntity<List<ConsultaResponseDTO>> getConsultasFuturasPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(consultaService.getConsultasFuturasPorPacienteDTO(pacienteId));
    }
    
    @GetMapping("/paciente/{pacienteId}/historico")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO') or (hasAuthority('ROLE_PACIENTE') and @pacienteService.isOwnedByUser(#pacienteId, authentication.principal.id))")
    public ResponseEntity<List<ConsultaResponseDTO>> getHistoricoCompletoPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(consultaService.getHistoricoCompletoPacienteDTO(pacienteId));
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<ConsultaResponseDTO>> getConsultasByStatus(@PathVariable StatusConsulta status) {
        return ResponseEntity.ok(consultaService.getConsultasByStatusDTO(status));
    }
    
    @GetMapping("/periodo")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<ConsultaResponseDTO>> getConsultasByPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return ResponseEntity.ok(consultaService.getConsultasByPeriodoDTO(inicio, fim));
    }
    
    @GetMapping("/notificacoes")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<List<ConsultaResponseDTO>> getConsultasParaNotificacao() {
        List<ConsultaResponseDTO> result = consultaService.getConsultasParaNotificacao().stream()
                .map(ConsultaMapper::toDTO)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(result);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<ConsultaResponseDTO> updateConsulta(@PathVariable Long id, @RequestBody ConsultaRequestDTO request) {
        return ResponseEntity.ok(ConsultaMapper.toDTO(consultaService.updateConsulta(id, ConsultaMapper.fromDTO(request))));
    }
    
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<ConsultaResponseDTO> updateStatusConsulta(@PathVariable Long id, @RequestParam StatusConsulta status) {
        return ResponseEntity.ok(ConsultaMapper.toDTO(consultaService.updateStatusConsulta(id, status)));
    }
    
    @PutMapping("/{id}/cancelar")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<Void> cancelarConsulta(@PathVariable Long id, @RequestParam String motivo) {
        consultaService.cancelarConsulta(id, motivo);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MEDICO') or hasAuthority('ROLE_ENFERMEIRO')")
    public ResponseEntity<Void> deleteConsulta(@PathVariable Long id) {
        consultaService.deleteConsulta(id);
        return ResponseEntity.noContent().build();
    }
}