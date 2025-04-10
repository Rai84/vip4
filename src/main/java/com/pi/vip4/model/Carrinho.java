package com.pi.vip4.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "carrinho")
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @NotNull
    @Min(value = 1, message = "A quantidade deve ser no mínimo 1")
    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "total", nullable = false)
    private BigDecimal total;

    @PrePersist
    @PreUpdate
    private void calcularTotal() {
        if (produto != null && quantidade != null) {
            this.total = produto.getPreco().multiply(BigDecimal.valueOf(quantidade));
        }
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
        calcularTotal(); // Atualiza o total ao definir o produto
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
        calcularTotal(); // Atualiza o total ao definir a quantidade
    }

    public BigDecimal getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "Carrinho{" +
                "id=" + id +
                ", cliente=" + cliente.getNome() +
                ", produto=" + produto.getNomeProduto() +
                ", quantidade=" + quantidade +
                ", total=" + total +
                '}';
    }
}
