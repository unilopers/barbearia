package com.grupo6.barbearia_api.controller;

import com.grupo6.barbearia_api.model.Fornecedor;
import com.grupo6.barbearia_api.view.FornecedorView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fornecedores")
public class FornecedorController {

    @Autowired
    private FornecedorView fornecedorView;

    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            List<Fornecedor> fornecedores = fornecedorView.findAll();

            if (fornecedores.isEmpty()) {
                return ResponseEntity.ok(criarResposta(
                        "sucesso",
                        "Nenhum fornecedor cadastrado",
                        fornecedores
                ));
            }

            return ResponseEntity.ok(criarResposta(
                    "sucesso",
                    fornecedores.size() + ": Fornecedor(es) encontrado(s)",
                    fornecedores
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(criarResposta(
                    "erro",
                    "Erro ao listar fornecedores: " + e.getMessage(),
                    null
            ));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            return fornecedorView.findById(id)
                    .map(fornecedor -> ResponseEntity.ok(criarResposta(
                            "sucesso",
                            "Fornecedor encontrado com sucesso",
                            fornecedor
                    )))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(criarResposta(
                            "erro",
                            "Fornecedor com ID '" + id + "' não encontrado",
                            null
                    )));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(criarResposta(
                    "erro",
                    "Erro ao buscar fornecedor: " + e.getMessage(),
                    null
            ));
        }
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Fornecedor fornecedor) {
        try {
            Fornecedor salvo = fornecedorView.save(fornecedor);

            return ResponseEntity.status(HttpStatus.CREATED).body(criarResposta(
                    "sucesso",
                    "Fornecedor '" + salvo.getNome() + "' criado com sucesso",
                    salvo
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(criarResposta(
                    "erro",
                    "Erro ao criar fornecedor: " + e.getMessage(),
                    null
            ));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Fornecedor fornecedor) {
        try {
            if (!fornecedorView.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(criarResposta(
                        "erro",
                        "Fornecedor com ID '" + id + "' não encontrado",
                        null
                ));
            }

            fornecedor.setId(id);
            Fornecedor atualizado = fornecedorView.save(fornecedor);

            return ResponseEntity.ok(criarResposta(
                    "sucesso",
                    "Fornecedor com ID '" + id + "' atualizado com sucesso",
                    atualizado
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(criarResposta(
                    "erro",
                    "Erro ao atualizar fornecedor: " + e.getMessage(),
                    null
            ));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            if (!fornecedorView.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(criarResposta(
                        "erro",
                        "Fornecedor com ID '" + id + "' não encontrado",
                        null
                ));
            }

            Fornecedor fornecedor = fornecedorView.findById(id).get();
            String nome = fornecedor.getNome();

            fornecedorView.deleteById(id);

            return ResponseEntity.ok(criarResposta(
                    "sucesso",
                    "Fornecedor '" + nome + "' deletado com sucesso",
                    null
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(criarResposta(
                    "erro",
                    "Erro ao deletar fornecedor: " + e.getMessage(),
                    null
            ));
        }
    }

    private Map<String, Object> criarResposta(String status, String mensagem, Object dados) {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("status", status);
        resposta.put("mensagem", mensagem);
        resposta.put("dados", dados);
        return resposta;
    }
}
