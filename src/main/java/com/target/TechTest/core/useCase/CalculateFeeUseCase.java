package com.target.TechTest.core.useCase;

import com.target.TechTest.adapter.controller.dto.CalculateFeeRequest;
import com.target.TechTest.adapter.controller.dto.CalculateFeeResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class CalculateFeeUseCase {

    private static final BigDecimal TAXA_MULTA_DIARIA = new BigDecimal("0.025");

    public CalculateFeeResponse executar(CalculateFeeRequest request) {
        LocalDate hoje = LocalDate.now();
        LocalDate vencimento = request.getDataVencimento();

        long diasAtraso = 0;
        BigDecimal valorMulta = BigDecimal.ZERO;

        if (vencimento.isBefore(hoje)) {
            diasAtraso = ChronoUnit.DAYS.between(vencimento, hoje);

            BigDecimal percentualTotal = TAXA_MULTA_DIARIA.multiply(BigDecimal.valueOf(diasAtraso));
            valorMulta = request.getValor().multiply(percentualTotal);
        }

        BigDecimal valorTotal = request.getValor().add(valorMulta);

        return new CalculateFeeResponse(
                request.getValor().setScale(2, RoundingMode.HALF_UP),
                vencimento,
                hoje,
                diasAtraso,
                valorMulta.setScale(2, RoundingMode.HALF_UP),
                valorTotal.setScale(2, RoundingMode.HALF_UP)
        );
    }
}
