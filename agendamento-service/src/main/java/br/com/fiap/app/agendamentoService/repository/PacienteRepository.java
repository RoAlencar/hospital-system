package br.com.fiap.app.agendamentoService.repository;

import br.com.fiap.app.agendamentoService.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    
    Optional<Paciente> findByCpf(String cpf);
    
    List<Paciente> findByAtivoTrue();
    
    @Query("SELECT p FROM Paciente p WHERE p.user.nome LIKE %:nome%")
    List<Paciente> findByNomeContaining(@Param("nome") String nome);
    
    boolean existsByCpf(String cpf);
    
    @Query("SELECT p FROM Paciente p WHERE p.user.id = :userId")
    Optional<Paciente> findByUserId(@Param("userId") Long userId);
    
    Optional<Paciente> findByNumeroCartaoSus(String numeroCartaoSus);
}