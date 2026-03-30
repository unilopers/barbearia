package com.grupo6.barbearia_api.service;

import com.grupo6.barbearia_api.model.Fornecedor;
import com.grupo6.barbearia_api.model.StatusFornecedor;
import com.grupo6.barbearia_api.repository.FornecedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;
    private final FornecedorAsyncService fornecedorAsyncService;

    public FornecedorService(FornecedorRepository fornecedorRepository,
                             FornecedorAsyncService fornecedorAsyncService) {
        this.fornecedorRepository = fornecedorRepository;
        this.fornecedorAsyncService = fornecedorAsyncService;
    }

    public List<Fornecedor> listarTodos() {
        return fornecedorRepository.findAll();
    }

    public Optional<Fornecedor> buscarPorId(Long id) {
        return fornecedorRepository.findById(id);
    }

    public Fornecedor criarFornecedor(Fornecedor fornecedor) {
        fornecedor.setStatus(StatusFornecedor.PENDENTE);

        Fornecedor fornecedorSalvo = fornecedorRepository.save(fornecedor);

        fornecedorAsyncService.validarCnpjAsync(fornecedorSalvo);

        return fornecedorSalvo;
    }

    public Optional<Fornecedor> atualizarFornecedor(Long id, Fornecedor fornecedorAtualizado) {
        return fornecedorRepository.findById(id).map(fornecedor -> {
            fornecedor.setNome(fornecedorAtualizado.getNome());
            fornecedor.setCnpj(fornecedorAtualizado.getCnpj());
            fornecedor.setTelefone(fornecedorAtualizado.getTelefone());
            fornecedor.setEmail(fornecedorAtualizado.getEmail());
            fornecedor.setEndereco(fornecedorAtualizado.getEndereco());
            fornecedor.setObservacoes(fornecedorAtualizado.getObservacoes());
            fornecedor.setAtivo(fornecedorAtualizado.isAtivo());

            fornecedor.setStatus(StatusFornecedor.PENDENTE);

            Fornecedor fornecedorSalvo = fornecedorRepository.save(fornecedor);

            fornecedorAsyncService.validarCnpjAsync(fornecedorSalvo);

            return fornecedorSalvo;
        });
    }

    public boolean deletarFornecedor(Long id) {
        if (fornecedorRepository.existsById(id)) {
            fornecedorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}