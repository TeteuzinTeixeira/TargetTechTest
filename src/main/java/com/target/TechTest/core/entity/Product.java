package com.target.TechTest.core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @Column(name = "codigo_produto")
    private Long codigoProduto;

    @Column(name = "descricao_produto")
    private String descricaoProduto;

    @Column(name = "estoque")
    private Integer estoque;
}
