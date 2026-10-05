package com.target.TechTest.adapter.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class SellPayloadRequest {
    @NotEmpty(message = "A lista de vendas não pode estar vazia")
    @Valid
    private List<SellRequest> vendas;
}
