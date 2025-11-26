package com.grupo6.barbearia_api.view;

import com.grupo6.barbearia_api.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ProdutoView extends JpaRepository<Produto, Long> {}