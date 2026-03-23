package br.com.fiap.app.notificacaoService.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import br.com.fiap.app.notificacaoService.dto.ConsultaAgendadaEvent;
import br.com.fiap.app.notificacaoService.dto.NotificacaoDTO;

@Service
public class NotificacaoService {

    private final Map<Long, List<NotificacaoDTO>> store = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    public void registrar(ConsultaAgendadaEvent event) {
        NotificacaoDTO notificacao = new NotificacaoDTO(
                idSequence.getAndIncrement(),
                event.getPacienteId(),
                event.getConsultaId(),
                event.getPacienteNome(),
                event.getMedicoNome(),
                event.getDataHora(),
                event.getStatus(),
                event.getMotivo(),
                LocalDateTime.now()
        );
        store.computeIfAbsent(event.getPacienteId(), k -> new ArrayList<>()).add(notificacao);
    }

    public List<NotificacaoDTO> getByPacienteId(Long pacienteId) {
        return store.getOrDefault(pacienteId, List.of());
    }
}
