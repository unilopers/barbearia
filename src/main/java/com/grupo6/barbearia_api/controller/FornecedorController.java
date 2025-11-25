package com.grupo6.barbearia_api.controller;

import com.grupo6.barbearia_api.model.Fornecedor;
import com.grupo6.barbearia_api.view.FornecedorView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fornecedores")
public class FornecedorController {

    @Autowired
    private FornecedorView fornecedorView;

    @GetMapping
    public List <Fornecedor> listar() {
        return fornecedorView.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fornecedor> buscarPorID(@PathVariable Long id) {
        return fornecedorView.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Fornecedor criar(@RequestBody Fornecedor fornecedor) {
        return fornecedorView.save(fornecedor);
    }

    @PutMapping(";{id}")
    public ResponseEntity<Fornecedor> atualizar(@PathVariable Long id, @RequestBody Fornecedor fornecedor) {
        if (!fornecedorView.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        fornecedor.setId(id);
        return ResponseEntity.ok(fornecedorView.save(fornecedor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!fornecedorView.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        fornecedorView.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
