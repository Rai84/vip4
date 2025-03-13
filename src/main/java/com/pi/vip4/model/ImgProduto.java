package com.pi.vip4.model;

import javax.persistence.*;

@Entity
@Table(name = "img_produto")
public class ImgProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idImagem;

    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    private Produto produto;

    @Column(name = "imagem_url", nullable = false)
    private String imagemUrl;

    // Getters and Setters
    public Long getIdImagem() {
        return idImagem;
    }

    public void setIdImagem(Long idImagem) {
        this.idImagem = idImagem;
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
                "idImagem=" + idImagem +
                ", produto=" + produto.getIdProduto() +
                ", imagemUrl='" + imagemUrl + '\'' +
                '}';
    }
}
