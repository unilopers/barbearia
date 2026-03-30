package com.grupo6.barbearia_api.view;

import com.grupo6.barbearia_api.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoView extends JpaRepository<Produto, Long> {
    @Query("SELECT p FROM Produto p WHERE p.quantidadeEstoque <= p.estoqueMinimo")
    List<Produto> findProdutosComEstoqueBaixo();
}