package com.target.TechTest.core.useCase;

import com.target.TechTest.adapter.controller.dto.CalculateFeeRequest;
import com.target.TechTest.adapter.controller.dto.CalculateFeeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class CalculateFeeUseCaseTest {

    private CalculateFeeUseCase calculateFeeUseCase;

    @BeforeEach
    void setUp() {
        calculateFeeUseCase = new CalculateFeeUseCase();
    }

    @Test
    @DisplayName("Não deve cobrar multa nem juros quando a conta vencer no dia atual")
    void naoDeveCobrarMultaQuandoVencerHoje() {
        LocalDate hoje = LocalDate.now();
        BigDecimal valorOriginal = new BigDecimal("100.00");
        CalculateFeeRequest request = new CalculateFeeRequest(valorOriginal, hoje);

        CalculateFeeResponse response = calculateFeeUseCase.executar(request);

        assertThat(response).isNotNull();
        assertThat(response.getValorOriginal()).isEqualByComparingTo("100.00");
        assertThat(response.getDiasAtraso()).isEqualTo(0);
        assertThat(response.getValorMulta()).isEqualByComparingTo("0.00");
        assertThat(response.getValorTotal()).isEqualByComparingTo("100.00");
        assertThat(response.getDataCalculo()).isEqualTo(hoje);
        assertThat(response.getDataVencimento()).isEqualTo(hoje);
    }

    @Test
    @DisplayName("Não deve cobrar multa quando a data de vencimento for futura")
    void naoDeveCobrarMultaQuandoVencimentoForFuturo() {
        LocalDate futuro = LocalDate.now().plusDays(5);
        BigDecimal valorOriginal = new BigDecimal("250.00");
        CalculateFeeRequest request = new CalculateFeeRequest(valorOriginal, futuro);

        CalculateFeeResponse response = calculateFeeUseCase.executar(request);

        assertThat(response.getDiasAtraso()).isEqualTo(0);
        assertThat(response.getValorMulta()).isEqualByComparingTo("0.00");
        assertThat(response.getValorTotal()).isEqualByComparingTo("250.00");
    }

    @Test
    @DisplayName("Deve calcular multa de 2,5% ao dia para pagamento em atraso")
    void deveCalcularMultaDiariaParaPagamentoEmAtraso() {
        LocalDate vencimentoHaQuatroDias = LocalDate.now().minusDays(4);
        BigDecimal valorOriginal = new BigDecimal("100.00");
        CalculateFeeRequest request = new CalculateFeeRequest(valorOriginal, vencimentoHaQuatroDias);

        CalculateFeeResponse response = calculateFeeUseCase.executar(request);

        assertThat(response.getDiasAtraso()).isEqualTo(4);
        assertThat(response.getValorMulta()).isEqualByComparingTo("10.00");
        assertThat(response.getValorTotal()).isEqualByComparingTo("110.00");
    }

    @Test
    @DisplayName("Deve arredondar os valores corretamente para 2 casas decimais")
    void deveArredondarValoresParaDuasCasasDecimais() {
        LocalDate vencimentoHaTresDias = LocalDate.now().minusDays(3);
        BigDecimal valorOriginal = new BigDecimal("123.45");
        CalculateFeeRequest request = new CalculateFeeRequest(valorOriginal, vencimentoHaTresDias);

        CalculateFeeResponse response = calculateFeeUseCase.executar(request);

        assertThat(response.getDiasAtraso()).isEqualTo(3);
        assertThat(response.getValorOriginal()).isEqualByComparingTo("123.45");
        assertThat(response.getValorMulta()).isEqualByComparingTo("9.26");
        assertThat(response.getValorTotal()).isEqualByComparingTo("132.71");
    }
}