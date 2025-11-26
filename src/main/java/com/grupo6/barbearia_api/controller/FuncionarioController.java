package com.grupo6.barbearia_api.controller;

import com.grupo6.barbearia_api.model.Funcionario;
import com.grupo6.barbearia_api.view.FuncionarioView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioView funcionarioView;

    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            List<Funcionario> funcionarios = funcionarioView.findAll();

            if (funcionarios.isEmpty()) {
                return ResponseEntity.ok(criarResposta(
                        "sucesso",
                        "Nenhum funcionário cadastrado",
                        funcionarios
                ));
            }
            return ResponseEntity.ok(criarResposta(
               "sucesso",
               funcionarios.size() + ": Funcionário(s) encontrado(s)",
                    funcionarios
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(criarResposta(
               "erro",
               "Erro ao listar funcionários: " + e.getMessage(),
                    null
            ));
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            return funcionarioView.findById(id).map(funcionario -> ResponseEntity.ok(criarResposta(
                "sucesso",
                    "Funcionário encontrado com sucesso",
                    funcionario
            ))).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(criarResposta(
                    "erro",
                    "Funcionário com ID " + id + " não encontrado",
                    null
            )));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(criarResposta(
                    "erro",
                    "Erro ao buscar funcionário: " + e.getMessage(),
                    null
            ));
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> criar(@RequestBody Funcionario funcionario) {
        try{
            Funcionario salvo = funcionarioView.save(funcionario);

            return ResponseEntity.status(HttpStatus.CREATED).body(criarResposta(
                    "sucesso",
                    "Funcionário '" + salvo.getNome() + "' criado com sucesso",
                    salvo
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(criarResposta(
                    "erro",
                    "Erro ao criar funcionário: " + e.getMessage(),
                    null
            ));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Funcionario funcionario) {
        try{
            if (!funcionarioView.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(criarResposta(
                        "erro",
                        "Funcionário com ID '" + id + "' não encontrado",
                        null
                ));
            }
            funcionario.setId(id);
            Funcionario atualizado = funcionarioView.save(funcionario);

            return ResponseEntity.ok(criarResposta(
                    "sucesso",
                    "Funcionário com ID '" + id + "' atualizado com sucesso",
                    atualizado
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(criarResposta(
                    "erro",
                    "Erro ao atualizar funcionário: " + e.getMessage(),
                    null
            ));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try{
            if(!funcionarioView.existsById(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(criarResposta(
                        "erro",
                        "Funcionário com ID '" + id + "' não encontrado",
                        null
                ));
            }

            Funcionario funcionario = funcionarioView.findById(id).get();
            String nome = funcionario.getNome();

            funcionarioView.deleteById(id);

            return ResponseEntity.ok(criarResposta(
                    "sucesso",
                    "Funcionário '" + nome + "' deletado com sucesso",
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(criarResposta(
                    "erro",
                    "Erro ao deletar funcionário: " + e.getMessage(),
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
