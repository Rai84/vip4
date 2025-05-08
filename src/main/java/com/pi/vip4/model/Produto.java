package com.pi.vip4.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "nomeProduto", nullable = false)
    private String nomeProduto;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "preco", nullable = false)
    private BigDecimal preco;

    @NotNull
    @Column(name = "estoque", nullable = false)
    private Integer estoque;

    @Size(max = 2000)
    @Column(name = "descricao")
    private String descricao;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "avaliacao", nullable = false)
    private Double avaliacao;

    @Column(name = "status", nullable = false)
    private boolean status;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
    private List<ImgProduto> imagens;

    // ✅ Getters e Setters atualizados
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<ImgProduto> getImagens() {
        return imagens;
    }

    public void setImagens(List<ImgProduto> imagens) {
        this.imagens = imagens;
    }

    public List<String> getImgUrls() {
        if (imagens == null) {
            return List.of();
        }
        return imagens.stream()
                .map(ImgProduto::getImagemUrl)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nomeProduto='" + nomeProduto + '\'' +
                ", preco=" + preco +
                ", estoque=" + estoque +
                ", descricao='" + descricao + '\'' +
                ", avaliacao=" + avaliacao +
                ", status=" + status +
                ", imagens=" + imagens +
                '}';
    }
}
