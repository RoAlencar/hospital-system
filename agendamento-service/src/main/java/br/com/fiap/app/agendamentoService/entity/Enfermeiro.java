package br.com.fiap.app.agendamentoService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "enfermeiros")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enfermeiro {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "ID do usuário é obrigatório")
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long userId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @NotBlank(message = "COREN é obrigatório")
    @Column(unique = true, nullable = false, length = 20)
    private String coren;
    
    @Column
    private String setor;
    
    @Column
    private String turno;
    
    @Column
    private String especializacao;
    
    @Column
    private String descricao;
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
    @JsonIgnore
    @OneToMany(mappedBy = "enfermeiro", cascade = CascadeType.ALL)
    private List<Consulta> consultasApoio;
}