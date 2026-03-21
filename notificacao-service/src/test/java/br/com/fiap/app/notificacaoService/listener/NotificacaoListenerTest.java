package br.com.fiap.app.notificacaoService.listener;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.app.notificacaoService.dto.ConsultaAgendadaEvent;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificacaoListener Tests")
class NotificacaoListenerTest {

    private NotificacaoListener notificacaoListener;

    @BeforeEach
    void setUp() {
        notificacaoListener = new NotificacaoListener();
    }

    @Test
    @DisplayName("Should process notification event without throwing exception")
    void shouldProcessNotificationEventWithoutException() {
        ConsultaAgendadaEvent event = new ConsultaAgendadaEvent(
                1L,
                "Maria da Silva",
                "Dr. João Santos",
                LocalDateTime.now().plusDays(1),
                "AGENDADA",
                "Consulta de rotina"
        );

        assertThatCode(() -> notificacaoListener.processarNotificacao(event))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should process notification event with null fields without throwing exception")
    void shouldProcessNotificationEventWithNullFieldsWithoutException() {
        ConsultaAgendadaEvent event = new ConsultaAgendadaEvent();

        assertThatCode(() -> notificacaoListener.processarNotificacao(event))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should process notification when dataHora is in the past")
    void shouldProcessNotificationWhenDataHoraIsInThePast() {
        ConsultaAgendadaEvent event = new ConsultaAgendadaEvent(
                2L,
                "Carlos Ferreira",
                "Dra. Ana Lima",
                LocalDateTime.now().minusDays(1),
                "CONCLUIDA",
                "Retorno"
        );

        assertThatCode(() -> notificacaoListener.processarNotificacao(event))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should process cancelled consultation event")
    void shouldProcessCancelledConsultationEvent() {
        ConsultaAgendadaEvent event = new ConsultaAgendadaEvent(
                3L,
                "Pedro Souza",
                "Dr. Ricardo Alves",
                LocalDateTime.now().plusHours(2),
                "CANCELADA",
                "Imprevisto do paciente"
        );

        assertThatCode(() -> notificacaoListener.processarNotificacao(event))
                .doesNotThrowAnyException();
    }
}
