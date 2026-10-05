package com.target.TechTest.adapter.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockResponse {
    private Long codigoProduto;
    private String descricaoProduto;
    private String tipoMovimentacao;
    private String descricaoMovimentacao;
    private Integer quantidadeMovimentada;
    private Integer quantidadeEstoqueFinal;
}
