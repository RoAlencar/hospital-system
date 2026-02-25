package br.com.fiap.app.agendamentoService.entity;

import br.com.fiap.app.agendamentoService.enums.Especialidade;
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
@Table(name = "medicos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medico {
    
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
    
    @NotBlank(message = "CRM é obrigatório")
    @Column(unique = true, nullable = false, length = 20)
    private String crm;
    
    @NotNull(message = "Especialidade é obrigatória")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Especialidade especialidade;
    
    @Column
    private String descricao;
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
    @JsonIgnore
    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL)
    private List<Consulta> consultas;
}