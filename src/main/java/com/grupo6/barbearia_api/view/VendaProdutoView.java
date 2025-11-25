package com.grupo6.barbearia_api.view;

import com.grupo6.barbearia_api.model.VendaProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendaProdutoView extends JpaRepository<VendaProduto, Long> {
}
