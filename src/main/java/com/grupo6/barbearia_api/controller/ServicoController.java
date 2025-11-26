package com.grupo6.barbearia_api.controller;

import com.grupo6.barbearia_api.model.Servico;
import com.grupo6.barbearia_api.view.ServicoView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    @Autowired
    private ServicoView servicoView;


    private Map<String, Object> criarResposta(String status, String mensagem, Object dados) {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("status", status);
        resposta.put("mensagem", mensagem);
        resposta.put("dados", dados);
        return resposta;
    }


    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            List<Servico> servicos = servicoView.findAll();

            if (servicos.isEmpty()) {
                return ResponseEntity.ok(criarResposta(
                        "sucesso",
                        "Nenhum serviço cadastrado",
                        servicos
                ));
            }
            return ResponseEntity.ok(criarResposta(
                    "sucesso",
                    servicos.size() + ": Serviço(s) encontrado(s)",
                    servicos
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(criarResposta(
                    "erro",
                    "Erro ao listar serviços: " + e.getMessage(),
                    null
            ));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            return servicoView.findById(id).map(servico -> ResponseEntity.ok(criarResposta(
                    "sucesso",
                    "Serviço encontrado com sucesso",
                    servico
            ))).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(criarResposta(
                    "erro",
                    "Serviço com ID " + id + " não encontrado",
                    null
            )));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(criarResposta(
                    "erro",
                    "Erro ao buscar serviço: " + e.getMessage(),
                    null
            ));
        }
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Servico servico) {
        try {
            Servico salvo = servicoView.save(servico);

            return ResponseEntity.status(HttpStatus.CREATED).body(criarResposta(
                    "sucesso",
                    "Serviço '" + salvo.getNome() + "' criado com sucesso",
                    salvo
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(criarResposta(
                    "erro",
                    "Erro ao criar serviço: " + e.getMessage(),
                    null
            ));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Servico servico) {
        try {
            if (!servicoView.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(criarResposta(
                        "erro",
                        "Serviço com ID '" + id + "' não encontrado",
                        null
                ));
            }
            servico.setId(id);
            Servico atualizado = servicoView.save(servico);

            return ResponseEntity.ok(criarResposta(
                    "sucesso",
                    "Serviço com ID '" + id + "' atualizado com sucesso",
                    atualizado
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(criarResposta(
                    "erro",
                    "Erro ao atualizar serviço: " + e.getMessage(),
                    null
            ));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            if (!servicoView.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(criarResposta(
                        "erro",
                        "Serviço com ID '" + id + "' não encontrado",
                        null
                ));
            }

            Servico servico = servicoView.findById(id).get();
            String nome = servico.getNome();

            servicoView.deleteById(id);

            return ResponseEntity.ok(criarResposta(
                    "sucesso",
                    "Serviço '" + nome + "' deletado com sucesso",
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(criarResposta(
                    "erro",
                    "Erro ao deletar serviço: " + e.getMessage(),
                    null
            ));
        }
    }
}