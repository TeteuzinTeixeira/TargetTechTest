package com.target.TechTest.adapter.controller;

import com.target.TechTest.adapter.controller.dto.MovimentProductRequest;
import com.target.TechTest.adapter.controller.dto.StockResponse;
import com.target.TechTest.core.useCase.MovimentProductUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    @Autowired
    private final MovimentProductUseCase useCase;

    public StockController(MovimentProductUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping("/movimentation")
    public ResponseEntity<StockResponse> movimentar(@Valid @RequestBody MovimentProductRequest request) {
        StockResponse response = useCase.moviment(request);
        return ResponseEntity.ok(response);
    }
}
