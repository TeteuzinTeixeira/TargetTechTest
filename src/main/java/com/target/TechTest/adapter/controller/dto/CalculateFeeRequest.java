package com.target.TechTest.adapter.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculateFeeRequest {

    @Schema(description = "Valor original da cobrança", example = "100.00")
    @NotNull(message = "O valor é obrigatório")
    @Positive(message = "O valor deve ser maior que zero")
    private BigDecimal valor;

    @Schema(description = "Data de vencimento no formato AAAA-MM-DD", example = "2026-10-01")
    @NotNull(message = "A data de vencimento é obrigatória")
    private LocalDate dataVencimento;
}
