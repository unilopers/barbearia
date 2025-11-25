package com.grupo6.barbearia_api.controller;

import com.grupo6.barbearia_api.model.Funcionario;
import com.grupo6.barbearia_api.view.FuncionarioView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioView funcionarioView;

    @GetMapping
    public List<Funcionario> listar() throws Exception {
        try {
            return funcionarioView.findAll();
        } catch (Exception e) {
            throw new Exception("Erro ao listar funcionários");
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> buscarPorId(@PathVariable Long id) throws Exception {
        try {
            return funcionarioView.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            throw new Exception("Erro ao buscar funcionário por ID");
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Funcionario criar(@RequestBody Funcionario funcionario) throws Exception {
        try{
            return funcionarioView.save(funcionario);

        } catch (Exception e) {
            throw new Exception("Erro ao criar funcionário");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Funcionario> atualizar(@PathVariable Long id, @RequestBody Funcionario funcionario) throws Exception {
        try{
            if (!funcionarioView.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            funcionario.setId(id);
            return ResponseEntity.ok(funcionarioView.save(funcionario));
        } catch (Exception e) {
            throw new Exception("Erro ao atualizar funcionário!");
        }
    }

    @DeleteMapping("{/id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) throws Exception {
        try{
            if(!funcionarioView.existsById(id)){
                return ResponseEntity.notFound().build();
            }
            funcionarioView.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new Exception("Erro ao deletar funcionário!");
        }
    }

}
