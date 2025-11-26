package com.grupo6.barbearia_api.controller;

import com.grupo6.barbearia_api.dto.AgendamentoRequestDTO;
import com.grupo6.barbearia_api.model.Agendamento;
import com.grupo6.barbearia_api.view.AgendamentoView;
import com.grupo6.barbearia_api.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/agendamentos" )
public class AgendamentoController {

    @Autowired
    private AgendamentoView agendamentoView;

    @Autowired
    private AgendamentoService agendamentoService;

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody AgendamentoRequestDTO dto) {
        try {
            // Validações
            if (dto.getClienteId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(criarResposta("error", "O campo 'clienteId' é obrigatório", null));
            }

            if (dto.getFuncionarioId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(criarResposta("error", "O campo 'funcionarioId' é obrigatório", null));
            }

            if (dto.getServicosIds() == null || dto.getServicosIds().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(criarResposta("error", "Selecione pelo menos um serviço", null));
            }

            if (dto.getDataHora() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(criarResposta("error", "O campo 'dataHora' é obrigatório", null));
            }

            Agendamento agendamento = agendamentoService.criarAgendamento(
                    dto.getClienteId(),
                    dto.getFuncionarioId(),
                    dto.getServicosIds(),
                    dto.getDataHora(),
                    dto.getObservacoes()
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(criarResposta(
                            "success",
                            "Agendamento criado com sucesso para " + agendamento.getDataHora(),
                            agendamento
                    ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(criarResposta("error", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(criarResposta(
                            "error",
                            "Erro ao criar agendamento: " + e.getMessage(),
                            null
                    ));
        }
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            List<Agendamento> agendamentos = agendamentoView.findAll();

            if (agendamentos.isEmpty()) {
                return ResponseEntity.ok(criarResposta(
                        "success",
                        "Nenhum agendamento cadastrado",
                        agendamentos
                ));
            }

            return ResponseEntity.ok(criarResposta(
                    "success",
                    agendamentos.size() + " agendamento(s) encontrado(s)",
                    agendamentos
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(criarResposta(
                            "error",
                            "Erro ao listar agendamentos: " + e.getMessage(),
                            null
                    ));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            return agendamentoView.findById(id)
                    .map(agendamento -> ResponseEntity.ok(criarResposta(
                            "success",
                            "Agendamento encontrado com sucesso",
                            agendamento
                    )))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(criarResposta(
                                    "error",
                                    "Agendamento com ID " + id + " não encontrado",
                                    null
                            )));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(criarResposta(
                            "error",
                            "Erro ao buscar agendamento: " + e.getMessage(),
                            null
                    ));
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            Agendamento agendamento = agendamentoView.findById(id)
                    .orElse(null);

            if (agendamento == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(criarResposta(
                                "error",
                                "Agendamento com ID " + id + " não encontrado",
                                null
                        ));
            }

            // Valida o status
            List<String> statusValidos = List.of("AGENDADO", "CONFIRMADO", "REALIZADO", "CANCELADO");
            if (!statusValidos.contains(status.toUpperCase())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(criarResposta(
                                "error",
                                "Status inválido. Use: AGENDADO, CONFIRMADO, REALIZADO ou CANCELADO",
                                null
                        ));
            }

            agendamento.setStatus(status.toUpperCase());
            Agendamento atualizado = agendamentoView.save(agendamento);

            return ResponseEntity.ok(criarResposta(
                    "success",
                    "Status do agendamento atualizado para '" + status.toUpperCase() + "'",
                    atualizado
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(criarResposta(
                            "error",
                            "Erro ao atualizar status: " + e.getMessage(),
                            null
                    ));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            if (!agendamentoView.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(criarResposta(
                                "error",
                                "Agendamento com ID " + id + " não encontrado",
                                null
                        ));
            }

            agendamentoView.deleteById(id);

            return ResponseEntity.ok(criarResposta(
                    "success",
                    "Agendamento deletado com sucesso",
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(criarResposta(
                            "error",
                            "Erro ao deletar agendamento: " + e.getMessage(),
                            null
                    ));
        }
    }

    // Método auxiliar para criar respostas padronizadas
    private Map<String, Object> criarResposta(String status, String mensagem, Object dados) {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("status", status);
        resposta.put("mensagem", mensagem);
        resposta.put("dados", dados);
        return resposta;
    }
}
