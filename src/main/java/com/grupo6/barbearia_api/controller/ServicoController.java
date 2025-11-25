package com.grupo6.barbearia_api.Controller;

import com.grupo6.barbearia_api.model.Servico;
import com.grupo6.barbearia_api.view.ServicoView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/servicos")

public class ServicoController {

    @Autowired
    private ServicoView servicoView;

    @GetMapping
    public List<Servico> listar() throws  Exception {
        try {
            return servicoView.findAll();
        } catch (Exception e) {
            throw new Exception("Erro ao tentar listar servicos");
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<Servico> buscarPorId(@PathVariable Long id) throws Exception {
        try {

            return servicoView.findById(id).map(ResponseEntity::ok).
                    orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            throw new Exception("Erro ao tentar buscar servico por id");
        }
    }
        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)

    public Servico criar(@RequestBody Servico servico) throws Exception {
            try {

                return servicoView.save(servico);
            } catch (Exception e) {
                throw new Exception("Erro ao tentar criar servico");
            }
        }
            @PutMapping("/{id}")
            public ResponseEntity<Servico> atualizar (@PathVariable Long id, @RequestBody Servico servico) throws
            Exception
            {
                try {
                    if (!servicoView.existsById(id)) {
                        return ResponseEntity.notFound().build();
                    }
                    servico.setId(id);
                    return ResponseEntity.ok(servicoView.save(servico));
                } catch (Exception e) {
                    throw new Exception("Erro ao tentar atualizar servico");
                }
            }

            @DeleteMapping("/{id}")
            public ResponseEntity<Void> deletar (@PathVariable Long id) throws Exception {
                try {
                    if (!servicoView.existsById(id)) {
                        return ResponseEntity.notFound().build();
                    }
                    servicoView.deleteById(id);
                    return ResponseEntity.noContent().build();
                } catch (Exception e) {
                    throw new Exception("Erro ao tentar deletar servico");
                }
            }
        }