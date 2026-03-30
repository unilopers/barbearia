package com.grupo6.barbearia_api.service;

import com.grupo6.barbearia_api.model.Fornecedor;
import com.grupo6.barbearia_api.model.StatusFornecedor;
import com.grupo6.barbearia_api.repository.FornecedorRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class FornecedorAsyncService {

    private final FornecedorRepository fornecedorRepository;

    public FornecedorAsyncService(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    @Async
    public void validarCnpjAsync(Fornecedor fornecedor) {
        try {
            Thread.sleep(5000);

            if (fornecedor.getCnpj() != null && fornecedor.getCnpj().endsWith("0001")) {
                fornecedor.setStatus(StatusFornecedor.VALIDADO);
            } else if (fornecedor.getCnpj() != null && fornecedor.getCnpj().endsWith("9999")) {
                fornecedor.setStatus(StatusFornecedor.REJEITADO);
            } else {
                fornecedor.setStatus(StatusFornecedor.PENDENTE);
            }

            fornecedorRepository.save(fornecedor);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}