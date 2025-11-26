package com.grupo6.barbearia_api.service;

import com.grupo6.barbearia_api.model.Agendamento;
import com.grupo6.barbearia_api.model.Cliente;
import com.grupo6.barbearia_api.model.Funcionario;
import com.grupo6.barbearia_api.model.Servico;
import com.grupo6.barbearia_api.view.AgendamentoView;
import com.grupo6.barbearia_api.view.ClienteView;
import com.grupo6.barbearia_api.view.FuncionarioView;
import com.grupo6.barbearia_api.view.ServicoView;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoView agendamentoView;

    @Autowired
    private ClienteView clienteView;

    @Autowired
    private FuncionarioView funcionarioView;

    @Autowired
    private ServicoView servicoView;

    public Agendamento criarAgendamento(Long clienteId, Long funcionarioId, List<Long> servicosIds, LocalDateTime dataHora, String observacoes) {
        Cliente cliente = clienteView.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com o ID: " + clienteId));

        Funcionario funcionario = funcionarioView.findById(funcionarioId)
                .orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado com o ID: " + funcionarioId));

        List<Servico> servicosEncontrados = servicoView.findAllById(servicosIds);
        if (servicosEncontrados.size() != servicosIds.size()) {
            throw new EntityNotFoundException("Um ou mais serviços não foram encontrados.");
        }
        Set<Servico> servicos = new HashSet<>(servicosEncontrados);

        BigDecimal valorTotal = servicos.stream()
                .map(Servico::getPreco)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Agendamento novoAgendamento = new Agendamento();
        novoAgendamento.setCliente(cliente);
        novoAgendamento.setFuncionario(funcionario);
        novoAgendamento.setServicos(servicos);
        novoAgendamento.setDataHora(dataHora);
        novoAgendamento.setValorTotal(valorTotal);
        novoAgendamento.setStatus("AGENDADO");
        novoAgendamento.setObservacoes(observacoes);
        novoAgendamento.setDataCriacao(LocalDateTime.now());

        return agendamentoView.save(novoAgendamento);
    }
}

