package br.com.fiap.app.notificacaoService.dto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ConsultaAgendadaEvent Tests")
class ConsultaAgendadaEventTest {

    @Test
    @DisplayName("Should create event with no-args constructor")
    void shouldCreateEventWithNoArgsConstructor() {
        ConsultaAgendadaEvent event = new ConsultaAgendadaEvent();

        assertThat(event).isNotNull();
        assertThat(event.getConsultaId()).isNull();
        assertThat(event.getPacienteNome()).isNull();
        assertThat(event.getMedicoNome()).isNull();
        assertThat(event.getDataHora()).isNull();
        assertThat(event.getStatus()).isNull();
        assertThat(event.getMotivo()).isNull();
    }

    @Test
    @DisplayName("Should create event with all-args constructor")
    void shouldCreateEventWithAllArgsConstructor() {
        LocalDateTime dataHora = LocalDateTime.of(2026, 4, 1, 10, 0);
        ConsultaAgendadaEvent event = new ConsultaAgendadaEvent(
                1L,
                "Maria da Silva",
                "Dr. João Santos",
                dataHora,
                "AGENDADA",
                "Consulta de rotina"
        );

        assertThat(event.getConsultaId()).isEqualTo(1L);
        assertThat(event.getPacienteNome()).isEqualTo("Maria da Silva");
        assertThat(event.getMedicoNome()).isEqualTo("Dr. João Santos");
        assertThat(event.getDataHora()).isEqualTo(dataHora);
        assertThat(event.getStatus()).isEqualTo("AGENDADA");
        assertThat(event.getMotivo()).isEqualTo("Consulta de rotina");
    }

    @Test
    @DisplayName("Should set and get fields using setters and getters")
    void shouldSetAndGetFieldsUsingSettersAndGetters() {
        LocalDateTime dataHora = LocalDateTime.of(2026, 5, 15, 14, 30);
        ConsultaAgendadaEvent event = new ConsultaAgendadaEvent();

        event.setConsultaId(10L);
        event.setPacienteNome("Carlos Ferreira");
        event.setMedicoNome("Dra. Ana Lima");
        event.setDataHora(dataHora);
        event.setStatus("CONFIRMADA");
        event.setMotivo("Retorno pós-cirurgia");

        assertThat(event.getConsultaId()).isEqualTo(10L);
        assertThat(event.getPacienteNome()).isEqualTo("Carlos Ferreira");
        assertThat(event.getMedicoNome()).isEqualTo("Dra. Ana Lima");
        assertThat(event.getDataHora()).isEqualTo(dataHora);
        assertThat(event.getStatus()).isEqualTo("CONFIRMADA");
        assertThat(event.getMotivo()).isEqualTo("Retorno pós-cirurgia");
    }

    @Test
    @DisplayName("Should test equality of two events with same data")
    void shouldTestEqualityOfTwoEventsWithSameData() {
        LocalDateTime dataHora = LocalDateTime.of(2026, 4, 1, 10, 0);
        ConsultaAgendadaEvent event1 = new ConsultaAgendadaEvent(1L, "Maria", "Dr. João", dataHora, "AGENDADA", "Rotina");
        ConsultaAgendadaEvent event2 = new ConsultaAgendadaEvent(1L, "Maria", "Dr. João", dataHora, "AGENDADA", "Rotina");

        assertThat(event1).isEqualTo(event2);
        assertThat(event1.hashCode()).isEqualTo(event2.hashCode());
    }

    @Test
    @DisplayName("Should generate toString representation")
    void shouldGenerateToStringRepresentation() {
        ConsultaAgendadaEvent event = new ConsultaAgendadaEvent(
                5L, "Ana Costa", "Dr. Pedro", LocalDateTime.now(), "AGENDADA", "Exame"
        );

        String toString = event.toString();

        assertThat(toString).isNotNull();
        assertThat(toString).contains("ConsultaAgendadaEvent");
        assertThat(toString).contains("Ana Costa");
        assertThat(toString).contains("Dr. Pedro");
    }
}
