package br.com.fiap.app.agendamentoService.repository;

import br.com.fiap.app.agendamentoService.entity.Medico;
import br.com.fiap.app.agendamentoService.enums.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    
    Optional<Medico> findByCrm(String crm);
    
    List<Medico> findByEspecialidade(Especialidade especialidade);
    
    List<Medico> findByAtivoTrue();
    
    @Query("SELECT m FROM Medico m WHERE m.user.nome LIKE %:nome%")
    List<Medico> findByNomeContaining(@Param("nome") String nome);
    
    boolean existsByCrm(String crm);
    
    @Query("SELECT m FROM Medico m WHERE m.user.id = :userId")
    Optional<Medico> findByUserId(@Param("userId") Long userId);
}