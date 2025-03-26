package com.pi.vip4.model;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduto;

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
    private boolean status; // Ativo ou inativo

    // Relacionamento com a tabela de imagens
    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
    private List<ImgProduto> imagens;

    // Getters and Setters
    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
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
            return List.of(); // Retorna uma lista vazia para evitar NullPointerException
        }
        return imagens.stream()
                .map(ImgProduto::getImagemUrl)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Produto{" +
                "idProduto=" + idProduto +
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
