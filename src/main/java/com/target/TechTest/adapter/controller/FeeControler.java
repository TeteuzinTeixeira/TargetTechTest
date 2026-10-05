package com.target.TechTest.adapter.controller;

import com.target.TechTest.adapter.controller.dto.CalculateFeeRequest;
import com.target.TechTest.adapter.controller.dto.CalculateFeeResponse;
import com.target.TechTest.core.useCase.CalculateFeeUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fees")
public class FeeControler {

    private final CalculateFeeUseCase useCase;

    public FeeControler(CalculateFeeUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping("/calcular")
    public ResponseEntity<CalculateFeeResponse> calcular(@Valid @RequestBody CalculateFeeRequest request) {
        CalculateFeeResponse response = useCase.executar(request);
        return ResponseEntity.ok(response);
    }
}
