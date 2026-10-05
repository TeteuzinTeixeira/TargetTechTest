package com.target.TechTest.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SellProcessed {
    private String vendedor;
    private BigDecimal totalComissao;
}
