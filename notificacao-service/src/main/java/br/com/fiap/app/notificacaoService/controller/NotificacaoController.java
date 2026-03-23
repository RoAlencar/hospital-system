package br.com.fiap.app.notificacaoService.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.app.notificacaoService.dto.NotificacaoDTO;
import br.com.fiap.app.notificacaoService.service.NotificacaoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notificacoes")
@RequiredArgsConstructor
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<NotificacaoDTO>> getByPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(notificacaoService.getByPacienteId(pacienteId));
    }
}
