package com.grupo6.barbearia_api.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AgendamentoRequestDTO {
    private Long clienteId;
    private Long funcionarioId;
    private List<Long> servicosIds;
    private LocalDateTime dataHora;
    private String observacoes;
}

