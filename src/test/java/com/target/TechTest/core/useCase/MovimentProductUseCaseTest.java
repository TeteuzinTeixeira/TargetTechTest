package com.target.TechTest.core.useCase;

import com.target.TechTest.adapter.controller.dto.MovimentProductRequest;
import com.target.TechTest.adapter.controller.dto.MovimentType;
import com.target.TechTest.adapter.controller.dto.StockResponse;
import com.target.TechTest.adapter.repository.ProductRepository;
import com.target.TechTest.core.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovimentProductUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MovimentProductUseCase movimentProductUseCase;

    @Test
    @DisplayName("Deve realizar movimentação de ENTRADA e somar a quantidade ao estoque")
    void deveRealizarMovimentacaoDeEntradaComSucesso() {
        Long codigoProduto = 101L;
        Product produtoExistente = new Product(codigoProduto, "Caneta Azul", 100);

        MovimentProductRequest request = new MovimentProductRequest(
                codigoProduto,
                MovimentType.ENTRADA,
                "Reposição de estoque",
                50
        );

        when(productRepository.findById(codigoProduto)).thenReturn(Optional.of(produtoExistente));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StockResponse response = movimentProductUseCase.moviment(request);

        assertThat(response).isNotNull();
        assertThat(response.getCodigoProduto()).isEqualTo(codigoProduto);
        assertThat(response.getDescricaoProduto()).isEqualTo("Caneta Azul");
        assertThat(response.getTipoMovimentacao()).isEqualTo("ENTRADA");
        assertThat(response.getDescricaoMovimentacao()).isEqualTo("Reposição de estoque");
        assertThat(response.getQuantidadeMovimentada()).isEqualTo(50);
        assertThat(response.getQuantidadeEstoqueFinal()).isEqualTo(150);

        verify(productRepository).save(produtoExistente);
    }

    @Test
    @DisplayName("Deve realizar movimentação de SAÍDA e subtrair a quantidade do estoque")
    void deveRealizarMovimentacaoDeSaidaComSucesso() {
        Long codigoProduto = 101L;
        Product produtoExistente = new Product(codigoProduto, "Caneta Azul", 100);

        MovimentProductRequest request = new MovimentProductRequest(
                codigoProduto,
                MovimentType.SAIDA,
                "Venda de balcão",
                30
        );

        when(productRepository.findById(codigoProduto)).thenReturn(Optional.of(produtoExistente));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StockResponse response = movimentProductUseCase.moviment(request);

        assertThat(response).isNotNull();
        assertThat(response.getTipoMovimentacao()).isEqualTo("SAIDA");
        assertThat(response.getQuantidadeMovimentada()).isEqualTo(30);
        assertThat(response.getQuantidadeEstoqueFinal()).isEqualTo(70);

        verify(productRepository).save(produtoExistente);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o produto não for encontrado")
    void deveLancarExcecaoQuandoProdutoNaoEncontrado() {
        Long codigoInexistente = 999L;
        MovimentProductRequest request = new MovimentProductRequest(
                codigoInexistente,
                MovimentType.ENTRADA,
                "Entrada",
                10
        );

        when(productRepository.findById(codigoInexistente)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movimentProductUseCase.moviment(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Produto não encontrado com o código: " + codigoInexistente);

        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando a quantidade de SAÍDA for maior que o estoque atual")
    void deveLancarExcecaoQuandoEstoqueForInsuficiente() {
        Long codigoProduto = 101L;
        Product produtoExistente = new Product(codigoProduto, "Caneta Azul", 20);

        MovimentProductRequest request = new MovimentProductRequest(
                codigoProduto,
                MovimentType.SAIDA,
                "Venda em lote",
                50
        );

        when(productRepository.findById(codigoProduto)).thenReturn(Optional.of(produtoExistente));

        assertThatThrownBy(() -> movimentProductUseCase.moviment(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Estoque insuficiente para realizar a saída. Estoque atual: 20");

        verify(productRepository, never()).save(any());
    }
}