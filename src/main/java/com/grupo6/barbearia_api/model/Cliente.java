package com.grupo6.barbearia_api.model;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Table(name = "cliente")
@Data

public class Cliente {
    @Id
    private Long id;
    @Column(nullable = false, length = 100)
    private String nome;
    @Column(nullable = false, length = 20)
    private String telefone;
    @Column(unique = true, length = 14)
    private String cpf;
    @Column (name = "data_nascimento")
    private LocalDate data_Nascimento;
    @Lob
    private String observacoes;
    @Column (nullable = false)
    private boolean ativo = true;
    @Column (name = "data_cadastro",
    nullable = false, updatable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();

}
