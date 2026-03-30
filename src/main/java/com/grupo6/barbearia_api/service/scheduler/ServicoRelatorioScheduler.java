package com.grupo6.barbearia_api.service.scheduler;

import com.grupo6.barbearia_api.model.Servico;
import com.grupo6.barbearia_api.view.ServicoView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServicoRelatorioScheduler {

    private static final Logger log = LoggerFactory.getLogger(ServicoRelatorioScheduler.class);

    @Autowired
    private ServicoView servicoView;

    @Scheduled(cron = "0 * * * * *")
    public void gerarRelatorioConsolidado() {
        log.info("--- [WORKER] INICIANDO CONSOLIDAÇÃO DE RELATÓRIO DE SERVIÇOS ---");

        try {
            List<Servico> servicos = servicoView.findAll();

            if (servicos == null || servicos.isEmpty()) {
                log.info("[WORKER] Nenhum dado encontrado para processar.");
                return;
            }

            long ativos = servicos.stream().filter(Servico::isAtivo).count();
            double precoMedio = servicos.stream()
                    .mapToDouble(s -> s.getPreco().doubleValue())
                    .average()
                    .orElse(0.0);

            StringBuilder sb = new StringBuilder();
            sb.append("\nTOTAL DE SERVICOS: ").append(servicos.size());
            sb.append("\nSERVICOS ATIVOS: ").append(ativos);
            sb.append("\nVALOR MEDIO: R$ ").append(String.format("%.2f", precoMedio));

            log.info("[WORKER] Relatório consolidado com sucesso: {}", sb.toString());

        } catch (Exception e) {
            log.error("[WORKER] Erro crítico no processamento assíncrono: {}", e.getMessage());
            log.error("[WORKER] O erro foi isolado para não afetar a API principal.");
        }

        log.info("--- [WORKER] FINALIZADO ---");
    }
}