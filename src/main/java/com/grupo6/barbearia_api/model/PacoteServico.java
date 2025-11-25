package com.grupo6.barbearia_api.model;


import jakarta.persistence.*;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "pacote_servico")
public class PacoteServico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Lob
    private String descricao;

    @Column(name = "preco_original", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoOriginal;

    @Column(name = "preco_pacote", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoPacote;

    @Column(name = "percentual_desconto", nullable = false, precision = 5, scale = 2)
    private BigDecimal percentualDesconto;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @ManyToMany
    @JoinTable(
            name = "pacote_servico_item",
            joinColumns = @JoinColumn(name = "pacote_servico_id"),
            inverseJoinColumns = @JoinColumn(name = "servico_id")
    )
    private Set<Servico> servicos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPrecoOriginal() {
        return precoOriginal;
    }

    public void setPrecoOriginal(BigDecimal precoOriginal) {
        this.precoOriginal = precoOriginal;
    }

    public BigDecimal getPrecoPacote() {
        return precoPacote;
    }

    public void setPrecoPacote(BigDecimal precoPacote) {
        this.precoPacote = precoPacote;
    }

    public BigDecimal getPercentualDesconto() {
        return percentualDesconto;
    }

    public void setPercentualDesconto(BigDecimal percentualDesconto) {
        this.percentualDesconto = percentualDesconto;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Set<Servico> getServicos() {
        return servicos;
    }

    public void setServicos(Set<Servico> servicos) {
        this.servicos = servicos;
    }
}
