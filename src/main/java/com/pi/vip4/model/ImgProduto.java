package com.pi.vip4.model;

import javax.persistence.*;

@Entity
@Table(name = "imagens_produto")
public class ImgProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idImgProduto;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(name = "imagemUrl")
    private String imagemUrl;

    // Getters and Setters
    public Long getIdImgProduto() {
        return idImgProduto;
    }

    public void setIdImgProduto(Long idImgProduto) {
        this.idImgProduto = idImgProduto;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    @Override
    public String toString() {
        return "ImgProduto{" +
                "idImgProduto=" + idImgProduto +
                ", produto=" + produto +
                ", imagemUrl='" + imagemUrl + '\'' +
                '}';
    }
}
