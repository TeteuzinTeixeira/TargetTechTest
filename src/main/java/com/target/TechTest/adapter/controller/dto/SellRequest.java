package com.target.TechTest.adapter.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SellRequest {

    @Schema(description = "Nome do vendedor responsável", example = "João Silva")
    @NotBlank(message = "O nome do vendedor é obrigatório")
    private String vendedor;

    @Schema(description = "Valor da venda realizada", example = "1200.50")
    @NotNull(message = "O valor da venda é obrigatório")
    @Positive(message = "O valor da venda deve ser maior que zero")
    private BigDecimal valor;
}
