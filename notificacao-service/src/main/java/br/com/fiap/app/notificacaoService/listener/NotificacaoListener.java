package br.com.fiap.app.notificacaoService.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import br.com.fiap.app.notificacaoService.dto.ConsultaAgendadaEvent;
import br.com.fiap.app.notificacaoService.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificacaoListener {

    private final NotificacaoService notificacaoService;

    @RabbitListener(queues = "${rabbitmq.queue}")
    public void processarNotificacao(@Payload ConsultaAgendadaEvent event) {
        log.info("[NOTIFICAÇÃO] Evento recebido: consultaId={}, paciente={}, status={}",
                event.getConsultaId(), event.getPacienteNome(), event.getStatus());

        notificacaoService.registrar(event);
        enviarEmailStub(event);
        enviarSmsStub(event);
    }

    private void enviarEmailStub(ConsultaAgendadaEvent event) {
        log.info("[EMAIL] Lembrete enviado para paciente '{}' — Consulta #{} em {} ({})",
                event.getPacienteNome(), event.getConsultaId(), event.getDataHora(), event.getStatus());
    }

    private void enviarSmsStub(ConsultaAgendadaEvent event) {
        log.info("[SMS] Lembrete enviado para paciente '{}' — Consulta #{} em {} ({})",
                event.getPacienteNome(), event.getConsultaId(), event.getDataHora(), event.getStatus());
    }

}
