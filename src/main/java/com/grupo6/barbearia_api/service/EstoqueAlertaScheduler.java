package com.grupo6.barbearia_api.service;

import com.grupo6.barbearia_api.model.Produto;
import com.grupo6.barbearia_api.view.ProdutoView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EstoqueAlertaScheduler {

    private static final Logger log = LoggerFactory.getLogger(EstoqueAlertaScheduler.class);

    private final ProdutoView produtoView;

    public EstoqueAlertaScheduler(ProdutoView produtoView) {
        this.produtoView = produtoView;
    }

    @Scheduled(cron = "0 * * * * *")
    public void verificarAlertasEstoque() {
        log.info("[ESTOQUE-SCHEDULER] Iniciando varredura de alertas de estoque...");

        try {
            List<Produto> produtosAbaixoMinimo = produtoView.findProdutosComEstoqueBaixo();

            if (produtosAbaixoMinimo.isEmpty()) {
                log.info("[ESTOQUE-SCHEDULER] Varredura concluída. Nenhum produto com estoque baixo encontrado.");
                return;
            }

            List<Produto> listaCritica = new ArrayList<>();
            List<Produto> listaAviso = new ArrayList<>();

            for (Produto produto : produtosAbaixoMinimo) {
                if (produto.getQuantidadeEstoque() == 0) {
                    listaCritica.add(produto);
                } else {
                    listaAviso.add(produto);
                }
            }

            if (!listaCritica.isEmpty()) {
                log.error("========================================================");
                log.error("[ESTOQUE-SCHEDULER] ALERTA CRITICO - {} produto(s) SEM ESTOQUE:", listaCritica.size());
                for (Produto p : listaCritica) {
                    log.error("[CRITICO] ID={} | Nome='{}' | Estoque atual={} | Estoque minimo={}",
                            p.getId(), p.getNome(), p.getQuantidadeEstoque(), p.getEstoqueMinimo());
                }
                log.error("========================================================");
            }

            if (!listaAviso.isEmpty()) {
                log.warn("--------------------------------------------------------");
                log.warn("[ESTOQUE-SCHEDULER] AVISO - {} produto(s) com estoque baixo:", listaAviso.size());
                for (Produto p : listaAviso) {
                    log.warn("[WARNING] ID={} | Nome='{}' | Estoque atual={} | Estoque minimo={}",
                            p.getId(), p.getNome(), p.getQuantidadeEstoque(), p.getEstoqueMinimo());
                }
                log.warn("--------------------------------------------------------");
            }

            log.info("[ESTOQUE-SCHEDULER] Varredura concluída. Criticos={} | Avisos={}",
                    listaCritica.size(), listaAviso.size());

        } catch (Exception e) {
            log.error("[ESTOQUE-SCHEDULER] ERRO durante a varredura de estoque. Detalhes: {}", e.getMessage(), e);
            log.error("[ESTOQUE-SCHEDULER] FALLBACK: Verificacao automatica falhou. Recomenda-se verificacao manual do estoque.");
        }
    }
}