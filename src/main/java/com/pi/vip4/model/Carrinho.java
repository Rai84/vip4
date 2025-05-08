package com.pi.vip4.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @NotNull
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @JsonIgnore
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

    @Column(name = "frete", nullable = false)
    private Double frete = 0.0;

    @PrePersist
    @PreUpdate
    private void calcularTotal() {
        if (produto != null && quantidade != null) {
            this.total = produto.getPreco().multiply(BigDecimal.valueOf(quantidade));
        }
    }

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
        calcularTotal(); // Recalcular sempre que o produto mudar
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
        calcularTotal(); // Recalcular sempre que a quantidade mudar
    }

    public BigDecimal getTotal() {
        return total;
    }

    public Double getFrete() {
        return frete;
    }

    public void setFrete(Double frete) {
        this.frete = frete;
    }

    @Override
    public String toString() {
        return "Carrinho{" +
                "id=" + id +
                ", cliente=" + (cliente != null ? cliente.getNome() : "null") +
                ", produto=" + (produto != null ? produto.getNomeProduto() : "null") +
                ", quantidade=" + quantidade +
                ", total=" + total +
                ", frete=" + frete +
                '}';
    }
}
