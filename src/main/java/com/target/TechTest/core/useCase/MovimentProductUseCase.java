package com.target.TechTest.core.useCase;

import com.target.TechTest.adapter.controller.dto.MovimentProductRequest;
import com.target.TechTest.adapter.controller.dto.MovimentType;
import com.target.TechTest.adapter.controller.dto.StockResponse;
import com.target.TechTest.adapter.repository.ProductRepository;
import com.target.TechTest.core.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class MovimentProductUseCase {

    @Autowired
    private final ProductRepository productRepository;

    public MovimentProductUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public StockResponse moviment(MovimentProductRequest request) {
        Product produto = productRepository.findById(request.getCodigoProduto())
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com o código: " + request.getCodigoProduto()));

        if (request.getTipo() == MovimentType.ENTRADA) {
            produto.setEstoque(produto.getEstoque() + request.getQuantidade());
        }
        if (request.getTipo() == MovimentType.SAIDA) {
            if (produto.getEstoque() < request.getQuantidade()) {
                throw new IllegalArgumentException("Estoque insuficiente para realizar a saída. Estoque atual: " + produto.getEstoque());
            }
            produto.setEstoque(produto.getEstoque() - request.getQuantidade());
        }

        Product produtoAtualizado = productRepository.save(produto);

        return new StockResponse(
                produtoAtualizado.getCodigoProduto(),
                produtoAtualizado.getDescricaoProduto(),
                request.getTipo().name(),
                request.getDescricao(),
                request.getQuantidade(),
                produtoAtualizado.getEstoque()
        );
    }
}
