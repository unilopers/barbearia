package com.grupo6.barbearia_api.service.async;

import com.grupo6.barbearia_api.model.Cliente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ClienteAsyncService {

    private static final Logger log = LoggerFactory.getLogger(ClienteAsyncService.class);

    @Async("clienteTaskExecutor")
    public void enviarEmailBoasVindasAsync(Cliente cliente) {
        log.info("Iniciando processamento assíncrono do cliente ID {}", cliente.getId());

        try {
            String templateHtml = """
                    <html>
                      <body>
                        <h1>Bem-vindo(a), {{nome}}!</h1>
                        <p>Seu cadastro foi realizado com sucesso.</p>
                        <p><strong>Telefone:</strong> {{telefone}}</p>
                        <p><strong>ID:</strong> {{id}}</p>
                      </body>
                    </html>
                    """;

            String htmlFinal = templateHtml
                    .replace("{{nome}}", cliente.getNome() != null ? cliente.getNome() : "Cliente")
                    .replace("{{telefone}}", cliente.getTelefone() != null ? cliente.getTelefone() : "Não informado")
                    .replace("{{id}}", String.valueOf(cliente.getId()));

            Thread.sleep(3000);

            String payload = """
                    {
                      "destinatario": "%s",
                      "assunto": "Boas-vindas",
                      "tipo": "text/html",
                      "conteudo": "%s"
                    }
                    """.formatted(
                    cliente.getEmail() != null ? cliente.getEmail() : "cliente-sem-email@fake.com",
                    htmlFinal.replace("\"", "\\\"").replace("\n", "")
            );

            log.info("Payload montado com sucesso para o cliente ID {}: {}", cliente.getId(), payload);
            log.info("Finalizando processamento assíncrono do cliente ID {}", cliente.getId());

        } catch (Exception e) {
            log.error("Erro no worker assíncrono do cliente ID {}. Fallback: validação manual necessária. Motivo: {}",
                    cliente.getId(), e.getMessage(), e);
        }
    }
}