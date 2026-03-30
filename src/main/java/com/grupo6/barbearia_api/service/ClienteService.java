package com.grupo6.barbearia_api.service;

import com.grupo6.barbearia_api.model.Cliente;
import com.grupo6.barbearia_api.service.async.ClienteAsyncService;
import com.grupo6.barbearia_api.view.ClienteView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClienteView clienteView;

    @Autowired
    private ClienteAsyncService clienteAsyncService;

    public Cliente criarCliente(Cliente cliente) {
        Cliente clienteSalvo = clienteView.save(cliente);

        clienteAsyncService.enviarEmailBoasVindasAsync(clienteSalvo);

        return clienteSalvo;
    }
}