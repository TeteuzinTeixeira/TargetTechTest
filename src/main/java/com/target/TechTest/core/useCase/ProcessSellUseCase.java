package com.target.TechTest.core.useCase;

import com.target.TechTest.core.entity.Sell;
import com.target.TechTest.core.entity.SellProcessed;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProcessSellUseCase {

    private static final BigDecimal LIMITE_ISENCAO = new BigDecimal("100.00");
    private static final BigDecimal LIMITE_FAIXA_UM = new BigDecimal("500.00");
    private static final BigDecimal TAXA_UM_POR_CENTO = new BigDecimal("0.01");
    private static final BigDecimal TAXA_CINCO_POR_CENTO = new BigDecimal("0.05");

    public List<SellProcessed> processSell(List<Sell> Sells) {
        if (Sells == null || Sells.isEmpty()) {
            return List.of();
        }

        Map<String, BigDecimal> comissaoAcumuladaPorVendedor = new HashMap<>();

        for (Sell Sell : Sells) {
            if (Sell.getVendedor() == null || Sell.getValor() == null) {
                continue;
            }

            BigDecimal comissaoSell = individualComissionCalculate(Sell.getValor());

            comissaoAcumuladaPorVendedor.merge(
                    Sell.getVendedor(),
                    comissaoSell,
                    BigDecimal::add
            );
        }

        List<SellProcessed> resultado = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : comissaoAcumuladaPorVendedor.entrySet()) {
            BigDecimal totalFormatado = entry.getValue().setScale(2, RoundingMode.HALF_UP);
            resultado.add(new SellProcessed(entry.getKey(), totalFormatado));
        }

        return resultado;
    }

    private BigDecimal individualComissionCalculate(BigDecimal valorVenda) {
        if (valorVenda.compareTo(LIMITE_ISENCAO) < 0) {
            return BigDecimal.ZERO;
        }

        if (valorVenda.compareTo(LIMITE_FAIXA_UM) < 0) {
            return valorVenda.multiply(TAXA_UM_POR_CENTO);
        }

        return valorVenda.multiply(TAXA_CINCO_POR_CENTO);
    }
}
