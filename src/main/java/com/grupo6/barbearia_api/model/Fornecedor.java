package com.grupo6.barbeariaapi.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "fornecedor")
@Data
public class fornecedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(unique = true, length = 18)
    private String cnpj;

    @Column(nullable = false, length = 20)
    private String telefone;

    @Column(length = 100)
    private String email;

    @Column)length = 225)
    private String endereço;

    @lomb
    private String observacoes;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDataTime dataCadastro = LocalDataTime.now();
}