package com.grupo6.barbearia_api.controller;

import com.grupo6.barbearia_api.model.Produto;
import com.grupo6.barbearia_api.view.ProdutoView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoView produtoView;

    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            List<Produto> produtos = produtoView.findAll();

            if (produtos.isEmpty()) {
                return ResponseEntity.ok(criarResposta(
                        "sucesso",
                        "Nenhum produto cadastrado",
                        produtos
                ));
            }
            return ResponseEntity.ok(criarResposta(
                    "sucesso",
                    produtos.size() + ": Produto(s) encontrado(s)",
                    produtos
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(criarResposta(
                    "erro",
                    "Erro ao listar produtos: " + e.getMessage(),
                    null
            ));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            return produtoView.findById(id).map(produto -> ResponseEntity.ok(criarResposta(
                    "sucesso",
                    "Produto encontrado com sucesso",
                    produto
            ))).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(criarResposta(
                    "erro",
                    "Produto com ID " + id + " não encontrado",
                    null
            )));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(criarResposta(
                    "erro",
                    "Erro ao buscar produto: " + e.getMessage(),
                    null
            ));
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> criar(@RequestBody Produto produto) {
        try {
            Produto salvo = produtoView.save(produto);

            return ResponseEntity.status(HttpStatus.CREATED).body(criarResposta(
                    "sucesso",
                    "Produto '" + salvo.getNome() + "' criado com sucesso",
                    salvo
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(criarResposta(
                    "erro",
                    "Erro ao criar produto: " + e.getMessage(),
                    null
            ));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Produto produto) {
        try {
            if (!produtoView.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(criarResposta(
                        "erro",
                        "Produto com ID '" + id + "' não encontrado",
                        null
                ));
            }
            produto.setId(id);
            Produto atualizado = produtoView.save(produto);

            return ResponseEntity.ok(criarResposta(
                    "sucesso",
                    "Produto com ID '" + id + "' atualizado com sucesso",
                    atualizado
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(criarResposta(
                    "erro",
                    "Erro ao atualizar produto: " + e.getMessage(),
                    null
            ));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            if (!produtoView.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(criarResposta(
                        "erro",
                        "Produto com ID '" + id + "' não encontrado",
                        null
                ));
            }

            Produto produto = produtoView.findById(id).get();
            String nome = produto.getNome();

            produtoView.deleteById(id);

            return ResponseEntity.ok(criarResposta(
                    "sucesso",
                    "Produto '" + nome + "' deletado com sucesso",
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(criarResposta(
                    "erro",
                    "Erro ao deletar produto: " + e.getMessage(),
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