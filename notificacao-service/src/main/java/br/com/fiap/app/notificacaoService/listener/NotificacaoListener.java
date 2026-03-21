package br.com.fiap.app.notificacaoService.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import br.com.fiap.app.notificacaoService.config.RabbitMQConfig;
import br.com.fiap.app.notificacaoService.dto.ConsultaAgendadaEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NotificacaoListener {

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void processarNotificacao(@Payload ConsultaAgendadaEvent event) {
        log.info("[NOTIFICAÇÃO] {}", event);
    }

}
