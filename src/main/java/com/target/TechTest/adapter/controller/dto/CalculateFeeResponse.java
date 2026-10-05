package com.target.TechTest.adapter.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculateFeeResponse {
    private BigDecimal valorOriginal;
    private LocalDate dataVencimento;
    private LocalDate dataCalculo;
    private long diasAtraso;
    private BigDecimal valorMulta;
    private BigDecimal valorTotal;
}
