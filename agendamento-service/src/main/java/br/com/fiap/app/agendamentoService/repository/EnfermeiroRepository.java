package br.com.fiap.app.agendamentoService.repository;

import br.com.fiap.app.agendamentoService.entity.Enfermeiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnfermeiroRepository extends JpaRepository<Enfermeiro, Long> {
    
    Optional<Enfermeiro> findByCoren(String coren);
    
    List<Enfermeiro> findBySetor(String setor);
    
    List<Enfermeiro> findByTurno(String turno);
    
    List<Enfermeiro> findByAtivoTrue();
    
    @Query("SELECT e FROM Enfermeiro e WHERE e.user.nome LIKE %:nome%")
    List<Enfermeiro> findByNomeContaining(@Param("nome") String nome);
    
    boolean existsByCoren(String coren);
    
    @Query("SELECT e FROM Enfermeiro e WHERE e.user.id = :userId")
    Optional<Enfermeiro> findByUserId(@Param("userId") Long userId);
    
    List<Enfermeiro> findBySetorAndTurno(String setor, String turno);
    
    @Query("SELECT e FROM Enfermeiro e WHERE e.especializacao = :especializacao AND e.ativo = true")
    List<Enfermeiro> findByEspecializacaoAndAtivoTrue(@Param("especializacao") String especializacao);
}