package com.target.TechTest.core.useCase;

import com.target.TechTest.core.entity.Sell;
import com.target.TechTest.core.entity.SellProcessed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProcessSellUseCaseTest {

    private ProcessSellUseCase processSellUseCase;

    @BeforeEach
    void setUp() {
        processSellUseCase = new ProcessSellUseCase();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando a entrada de vendas for nula ou vazia")
    void deveRetornarListaVaziaQuandoEntradaForNulaOuVazia() {
        List<SellProcessed> resultadoNulo = processSellUseCase.processSell(null);
        List<SellProcessed> resultadoVazio = processSellUseCase.processSell(List.of());

        assertThat(resultadoNulo).isEmpty();
        assertThat(resultadoVazio).isEmpty();
    }

    @Test
    @DisplayName("Não deve gerar comissão para vendas abaixo de R$ 100,00 (0%)")
    void naoDeveGerarComissaoParaVendasAbaixoDeCemReais() {
        List<Sell> sells = List.of(
                new Sell("João Silva", new BigDecimal("99.99")),
                new Sell("João Silva", new BigDecimal("50.00"))
        );

        List<SellProcessed> resultado = processSellUseCase.processSell(sells);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getVendedor()).isEqualTo("João Silva");
        assertThat(resultado.get(0).getTotalComissao()).isEqualByComparingTo("0.00");
    }

    @Test
    @DisplayName("Deve calcular 1% de comissão para vendas entre R$ 100,00 e R$ 499,99")
    void deveCalcularUmPorCentoDeComissaoParaVendasEntreCemEQuatrocentosENoventaENove() {
        List<Sell> sells = List.of(
                new Sell("Maria Souza", new BigDecimal("100.00")), // 1% = 1.00
                new Sell("Maria Souza", new BigDecimal("300.00"))  // 1% = 3.00
        );

        List<SellProcessed> resultado = processSellUseCase.processSell(sells);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getVendedor()).isEqualTo("Maria Souza");
        assertThat(resultado.get(0).getTotalComissao()).isEqualByComparingTo("4.00");
    }

    @Test
    @DisplayName("Deve calcular 5% de comissão para vendas a partir de R$ 500,00")
    void deveCalcularCincoPorCentoDeComissaoParaVendasApartirDeQuinhentosReais() {
        List<Sell> sells = List.of(
                new Sell("Carlos Oliveira", new BigDecimal("500.00")),  // 5% = 25.00
                new Sell("Carlos Oliveira", new BigDecimal("1000.00"))  // 5% = 50.00
        );

        List<SellProcessed> resultado = processSellUseCase.processSell(sells);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getVendedor()).isEqualTo("Carlos Oliveira");
        assertThat(resultado.get(0).getTotalComissao()).isEqualByComparingTo("75.00");
    }

    @Test
    @DisplayName("Deve calcular e agrupar comissões para múltiplos vendedores e faixas de valores")
    void deveCalcularEAgruparComissoesParaMultiplosVendedoresEFaixas() {
        List<Sell> sells = List.of(
                // João Silva: 0.00 (isento) + 60.025 (5% de 1200.50) = 60.03 arredondado
                new Sell("João Silva", new BigDecimal("80.00")),
                new Sell("João Silva", new BigDecimal("1200.50")),

                // Ana Lima: 3.00 (1% de 300.00)
                new Sell("Ana Lima", new BigDecimal("300.00"))
        );

        List<SellProcessed> resultado = processSellUseCase.processSell(sells);

        assertThat(resultado).hasSize(2);

        SellProcessed joao = resultado.stream()
                .filter(s -> "João Silva".equals(s.getVendedor()))
                .findFirst()
                .orElseThrow();

        SellProcessed ana = resultado.stream()
                .filter(s -> "Ana Lima".equals(s.getVendedor()))
                .findFirst()
                .orElseThrow();

        assertThat(joao.getTotalComissao()).isEqualByComparingTo("60.03");
        assertThat(ana.getTotalComissao()).isEqualByComparingTo("3.00");
    }

    @Test
    @DisplayName("Deve ignorar vendas com vendedor ou valor nulos")
    void deveIgnorarVendasComCamposNulos() {
        List<Sell> sells = List.of(
                new Sell(null, new BigDecimal("500.00")),
                new Sell("João Silva", null),
                new Sell("João Silva", new BigDecimal("200.00")) // 1% = 2.00
        );

        List<SellProcessed> resultado = processSellUseCase.processSell(sells);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getVendedor()).isEqualTo("João Silva");
        assertThat(resultado.get(0).getTotalComissao()).isEqualByComparingTo("2.00");
    }
}