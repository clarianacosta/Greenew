package com.greenew.relatorios.model.dto;

import com.greenew.relatorios.config.dto.ArvoreResponseDTO;
import com.greenew.relatorios.config.dto.TerrenoResponseDTO;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class RecomendacaoRanqueadaDTO {
    private String rank;
    private ArvoreResponseDTO arvore;
    private long quantidadeNecessaria;
    private BigDecimal custoTotalEstimado;
    private double pontuacao; // A pontuação final usada para ordenar
    private double areaTotalNecessariaHectares;
    private List<TerrenoResponseDTO> terrenosCompatíveis;
}