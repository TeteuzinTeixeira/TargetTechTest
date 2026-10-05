package com.target.TechTest.adapter.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MovimentProductRequest {

    @Schema(description = "Código do produto no estoque", example = "101")
    @NotNull(message = "O código do produto é obrigatório")
    private Long codigoProduto;

    @Schema(description = "Tipo de movimentação (ENTRADA ou SAIDA)", example = "SAIDA")
    @NotNull(message = "O tipo da movimentação é obrigatório")
    private MovimentType tipo;

    @Schema(description = "Descrição ou motivo da movimentação", example = "Venda de balcão")
    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @Schema(description = "Quantidade total de itens movimentados", example = "20")
    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser de no mínimo 1")
    private Integer quantidade;
}