package com.grupo6.barbearia_api.model;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Table(name = "cliente")

public class Cliente {
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getData_Nascimento() {
        return data_Nascimento;
    }

    public void setData_Nascimento(LocalDate data_Nascimento) {
        this.data_Nascimento = data_Nascimento;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

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
