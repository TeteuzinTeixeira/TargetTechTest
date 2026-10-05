package com.target.TechTest.adapter.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MovimentProductRequest {

    @NotNull(message = "O código do produto é obrigatório")
    private Long codigoProduto;

    @NotNull(message = "O tipo da movimentação é obrigatório")
    private MovimentType tipo;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser de no mínimo 1")
    private Integer quantidade;
}
