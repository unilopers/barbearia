package com.grupo6.barbearia_api.controller;
import com.grupo6.barbearia_api.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.grupo6.barbearia_api.view.ClienteView;
import java.util.List;
@RestController
@RequestMapping("/clientes")

public class ClienteController {
    @Autowired
    private ClienteView clienteView;
    @GetMapping
    public List<Cliente> listar() throws Exception {
        try {
            return clienteView.findAll();
        } catch (Exception e) {
            throw new Exception("Erro ao listar os clientes no banco");
        }
    }
    @GetMapping("/{id}")

    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) throws Exception {
        try {
            return clienteView.findById(id)
                    .map(clienteEncontrado -> ResponseEntity.ok(clienteEncontrado))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            throw new Exception("Erro ao buscar os clientes no banco");
        }
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)

    public Cliente criar(@RequestBody Cliente cliente) throws Exception {
        try {
            return clienteView.save(cliente);
        } catch (Exception e) {
            throw new Exception("Erro ao criar os clientes no banco");
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody Cliente clienteDetalhes) throws Exception {
        try {
            if (!clienteView.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            clienteDetalhes.setId(id);
            Cliente clienteAtualizado = clienteView.save(clienteDetalhes);
            return ResponseEntity.ok(clienteAtualizado);
        } catch (Exception e) {
            throw new Exception("Erro ao atualizar os clientes no banco");
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) throws Exception {
        try {
            if (!clienteView.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            clienteView.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new Exception("Erro ao deletar os clientes no banco");
        }
    }


}
