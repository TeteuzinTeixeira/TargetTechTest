package com.target.TechTest.adapter.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SellRequest {
    @NotBlank(message = "O nome do vendedor é obrigatório")
    private String vendedor;

    @NotNull(message = "O valor da venda é obrigatório")
    @Positive(message = "O valor da venda deve ser maior que zero")
    private BigDecimal valor;
}
