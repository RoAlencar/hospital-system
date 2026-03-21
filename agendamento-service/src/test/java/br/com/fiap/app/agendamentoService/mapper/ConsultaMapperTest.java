package br.com.fiap.app.agendamentoService.mapper;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fiap.app.agendamentoService.dto.ConsultaResponseDTO;
import br.com.fiap.app.agendamentoService.dto.EnfermeiroSimpleDTO;
import br.com.fiap.app.agendamentoService.dto.MedicoSimpleDTO;
import br.com.fiap.app.agendamentoService.dto.PacienteSimpleDTO;
import br.com.fiap.app.agendamentoService.entity.Consulta;
import br.com.fiap.app.agendamentoService.entity.Enfermeiro;
import br.com.fiap.app.agendamentoService.entity.Medico;
import br.com.fiap.app.agendamentoService.entity.Paciente;
import br.com.fiap.app.agendamentoService.entity.User;
import br.com.fiap.app.agendamentoService.enums.Especialidade;
import br.com.fiap.app.agendamentoService.enums.StatusConsulta;

@DisplayName("ConsultaMapper Tests")
class ConsultaMapperTest {

    private Medico medico;
    private Paciente paciente;
    private Enfermeiro enfermeiro;
    private Consulta consulta;

    @BeforeEach
    void setUp() {
        User medicoUser = new User();
        medicoUser.setNome("Dr. Carlos");

        medico = new Medico();
        medico.setId(1L);
        medico.setUser(medicoUser);
        medico.setCrm("CRM12345");
        medico.setEspecialidade(Especialidade.CARDIOLOGIA);

        User pacienteUser = new User();
        pacienteUser.setNome("João Silva");

        paciente = new Paciente();
        paciente.setId(2L);
        paciente.setUser(pacienteUser);
        paciente.setCpf("12345678901");

        User enfermeiroUser = new User();
        enfermeiroUser.setNome("Ana Enfermeira");

        enfermeiro = new Enfermeiro();
        enfermeiro.setId(3L);
        enfermeiro.setUser(enfermeiroUser);
        enfermeiro.setCoren("COREN99");

        consulta = new Consulta();
        consulta.setId(10L);
        consulta.setMedico(medico);
        consulta.setPaciente(paciente);
        consulta.setEnfermeiro(enfermeiro);
        consulta.setDataHora(LocalDateTime.now().plusDays(1));
        consulta.setStatus(StatusConsulta.AGENDADA);
        consulta.setMotivo("Rotina");
        consulta.setObservacoes("OK");
        consulta.setDiagnostico("Saudável");
        consulta.setPrescricao("Repouso");
        consulta.setDataCriacao(LocalDateTime.now());
    }

    @Test
    @DisplayName("toDTO should return null when consulta is null")
    void toDTOShouldReturnNullWhenConsultaIsNull() {
        assertThat(ConsultaMapper.toDTO(null)).isNull();
    }

    @Test
    @DisplayName("toDTO should map all fields correctly")
    void toDTOShouldMapAllFieldsCorrectly() {
        ConsultaResponseDTO dto = ConsultaMapper.toDTO(consulta);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getStatus()).isEqualTo(StatusConsulta.AGENDADA);
        assertThat(dto.getMotivo()).isEqualTo("Rotina");
        assertThat(dto.getObservacoes()).isEqualTo("OK");
        assertThat(dto.getDiagnostico()).isEqualTo("Saudável");
        assertThat(dto.getPrescricao()).isEqualTo("Repouso");
        assertThat(dto.getDataHora()).isNotNull();
        assertThat(dto.getDataCriacao()).isNotNull();
    }

    @Test
    @DisplayName("toDTO should map medico nested DTO correctly")
    void toDTOShouldMapMedicoCorrectly() {
        ConsultaResponseDTO dto = ConsultaMapper.toDTO(consulta);

        assertThat(dto.getMedico()).isNotNull();
        assertThat(dto.getMedico().getId()).isEqualTo(1L);
        assertThat(dto.getMedico().getNome()).isEqualTo("Dr. Carlos");
        assertThat(dto.getMedico().getCrm()).isEqualTo("CRM12345");
        assertThat(dto.getMedico().getEspecialidade()).isEqualTo(Especialidade.CARDIOLOGIA);
    }

    @Test
    @DisplayName("toDTO should map paciente nested DTO correctly")
    void toDTOShouldMapPacienteCorrectly() {
        ConsultaResponseDTO dto = ConsultaMapper.toDTO(consulta);

        assertThat(dto.getPaciente()).isNotNull();
        assertThat(dto.getPaciente().getId()).isEqualTo(2L);
        assertThat(dto.getPaciente().getNome()).isEqualTo("João Silva");
        assertThat(dto.getPaciente().getCpf()).isEqualTo("12345678901");
    }

    @Test
    @DisplayName("toDTO should map enfermeiro nested DTO correctly")
    void toDTOShouldMapEnfermeiroCorrectly() {
        ConsultaResponseDTO dto = ConsultaMapper.toDTO(consulta);

        assertThat(dto.getEnfermeiro()).isNotNull();
        assertThat(dto.getEnfermeiro().getId()).isEqualTo(3L);
        assertThat(dto.getEnfermeiro().getNome()).isEqualTo("Ana Enfermeira");
        assertThat(dto.getEnfermeiro().getCoren()).isEqualTo("COREN99");
    }

    @Test
    @DisplayName("toDTO should return null enfermeiro DTO when consulta has no enfermeiro")
    void toDTOShouldReturnNullEnfermeiroDTOWhenNoEnfermeiro() {
        consulta.setEnfermeiro(null);
        ConsultaResponseDTO dto = ConsultaMapper.toDTO(consulta);
        assertThat(dto.getEnfermeiro()).isNull();
    }

    @Test
    @DisplayName("toMedicoSimpleDTO should return null when medico is null")
    void toMedicoSimpleDTOShouldReturnNullWhenNull() {
        assertThat(ConsultaMapper.toMedicoSimpleDTO(null)).isNull();
    }

    @Test
    @DisplayName("toMedicoSimpleDTO should handle null user gracefully")
    void toMedicoSimpleDTOShouldHandleNullUser() {
        medico.setUser(null);
        MedicoSimpleDTO dto = ConsultaMapper.toMedicoSimpleDTO(medico);
        assertThat(dto).isNotNull();
        assertThat(dto.getNome()).isNull();
    }

    @Test
    @DisplayName("toPacienteSimpleDTO should return null when paciente is null")
    void toPacienteSimpleDTOShouldReturnNullWhenNull() {
        assertThat(ConsultaMapper.toPacienteSimpleDTO(null)).isNull();
    }

    @Test
    @DisplayName("toPacienteSimpleDTO should handle null user gracefully")
    void toPacienteSimpleDTOShouldHandleNullUser() {
        paciente.setUser(null);
        PacienteSimpleDTO dto = ConsultaMapper.toPacienteSimpleDTO(paciente);
        assertThat(dto).isNotNull();
        assertThat(dto.getNome()).isNull();
    }

    @Test
    @DisplayName("toEnfermeiroSimpleDTO should return null when enfermeiro is null")
    void toEnfermeiroSimpleDTOShouldReturnNullWhenNull() {
        assertThat(ConsultaMapper.toEnfermeiroSimpleDTO(null)).isNull();
    }

    @Test
    @DisplayName("toEnfermeiroSimpleDTO should handle null user gracefully")
    void toEnfermeiroSimpleDTOShouldHandleNullUser() {
        enfermeiro.setUser(null);
        EnfermeiroSimpleDTO dto = ConsultaMapper.toEnfermeiroSimpleDTO(enfermeiro);
        assertThat(dto).isNotNull();
        assertThat(dto.getNome()).isNull();
    }
}
