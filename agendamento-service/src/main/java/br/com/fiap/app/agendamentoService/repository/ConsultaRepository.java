package br.com.fiap.app.agendamentoService.repository;

import br.com.fiap.app.agendamentoService.entity.Consulta;
import br.com.fiap.app.agendamentoService.entity.Medico;
import br.com.fiap.app.agendamentoService.entity.Paciente;
import br.com.fiap.app.agendamentoService.enums.StatusConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    
    List<Consulta> findByMedico(Medico medico);
    
    List<Consulta> findByPaciente(Paciente paciente);
    
    List<Consulta> findByStatus(StatusConsulta status);
    
    @Query("SELECT c FROM Consulta c WHERE c.dataHora BETWEEN :inicio AND :fim")
    List<Consulta> findByPeriodo(@Param("inicio") LocalDateTime inicio, 
                                @Param("fim") LocalDateTime fim);
    
    @Query("SELECT c FROM Consulta c WHERE c.paciente = :paciente AND c.dataHora > :dataAtual ORDER BY c.dataHora ASC")
    List<Consulta> findConsultasFuturasPorPaciente(@Param("paciente") Paciente paciente, 
                                                  @Param("dataAtual") LocalDateTime dataAtual);
    
    @Query("SELECT c FROM Consulta c WHERE c.medico = :medico AND c.dataHora BETWEEN :inicio AND :fim")
    List<Consulta> findByMedicoAndPeriodo(@Param("medico") Medico medico,
                                         @Param("inicio") LocalDateTime inicio,
                                         @Param("fim") LocalDateTime fim);
    
    @Query("SELECT c FROM Consulta c WHERE c.paciente.id = :pacienteId ORDER BY c.dataHora DESC")
    List<Consulta> findHistoricoCompletoPaciente(@Param("pacienteId") Long pacienteId);
    
    @Query("SELECT c FROM Consulta c WHERE c.dataHora > :agora AND c.status IN :statuses")
    List<Consulta> findConsultasParaNotificacao(@Param("agora") LocalDateTime agora,
                                               @Param("statuses") List<StatusConsulta> statuses);
}