package com.target.TechTest.adapter.controller;

import com.target.TechTest.adapter.controller.dto.SellPayloadRequest;
import com.target.TechTest.core.entity.Sell;
import com.target.TechTest.core.useCase.ProcessSellUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class sellsController {

    @Autowired
    private final ProcessSellUseCase processSellUseCase;

    @PostMapping("/sell")
    public ResponseEntity<?> sell(@Valid @RequestBody SellPayloadRequest payload) {
        List<Sell> vendas = payload.getVendas().stream()
                .map(dto -> new Sell(dto.getVendedor(), dto.getValor()))
                .toList();

        var resultado = processSellUseCase.processSell(vendas);

        return ResponseEntity.ok(resultado);
    }
}
