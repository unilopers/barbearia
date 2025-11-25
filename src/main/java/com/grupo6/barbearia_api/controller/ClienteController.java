package com.grupo6.barbearia_api.controller;

import com.grupo6.barbearia_api.model.Cliente;
import com.grupo6.barbearia_api.view.ClienteView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteView clienteView;
    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            List<Cliente> clientes = clienteView.findAll();

            if (clientes.isEmpty()) {
                return ResponseEntity.ok(criarResposta(
                        "sucesso",
                        "Nenhum cliente cadastrado",
                        clientes
                ));
            }

            return ResponseEntity.ok(criarResposta(
                    "sucesso",
                    clientes.size() + ": Cliente(s) encontrado(s)",
                    clientes
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(criarResposta(
                    "erro",
                    "Erro ao listar clientes: " + e.getMessage(),
                    null
            ));
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            return clienteView.findById(id)
                    .map(cliente -> ResponseEntity.ok(criarResposta(
                            "sucesso",
                            "Cliente encontrado com sucesso",
                            cliente
                    )))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(criarResposta(
                            "erro",
                            "Cliente com ID '" + id + "' não encontrado",
                            null
                    )));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(criarResposta(
                    "erro",
                    "Erro ao buscar cliente: " + e.getMessage(),
                    null
            ));
        }
    }
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Cliente cliente) {
        try {
            Cliente salvo = clienteView.save(cliente);

            return ResponseEntity.status(HttpStatus.CREATED).body(criarResposta(
                    "sucesso",
                    "Cliente '" + salvo.getNome() + "' criado com sucesso",
                    salvo
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(criarResposta(
                    "erro",
                    "Erro ao criar cliente: " + e.getMessage(),
                    null
            ));
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        try {
            if (!clienteView.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(criarResposta(
                        "erro",
                        "Cliente com ID '" + id + "' não encontrado",
                        null
                ));
            }

            cliente.setId(id);
            Cliente atualizado = clienteView.save(cliente);

            return ResponseEntity.ok(criarResposta(
                    "sucesso",
                    "Cliente com ID '" + id + "' atualizado com sucesso",
                    atualizado
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(criarResposta(
                    "erro",
                    "Erro ao atualizar cliente: " + e.getMessage(),
                    null
            ));
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            if (!clienteView.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(criarResposta(
                        "erro",
                        "Cliente com ID '" + id + "' não encontrado",
                        null
                ));
            }

            Cliente cliente = clienteView.findById(id).get();
            String nome = cliente.getNome();

            clienteView.deleteById(id);

            return ResponseEntity.ok(criarResposta(
                    "sucesso",
                    "Cliente '" + nome + "' deletado com sucesso",
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(criarResposta(
                    "erro",
                    "Erro ao deletar cliente: " + e.getMessage(),
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
