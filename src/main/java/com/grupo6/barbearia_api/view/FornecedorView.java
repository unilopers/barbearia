package com.grupo6.barbearia_api.view;

import com.grupo6.barbearia_api.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FornecedorView extends JpaRepository<Fornecedor, Long> {
}

