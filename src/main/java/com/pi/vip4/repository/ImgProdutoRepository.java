package com.pi.vip4.repository;

import com.pi.vip4.model.ImgProduto;
import com.pi.vip4.model.Produto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImgProdutoRepository extends JpaRepository<ImgProduto, Long> {
    // Método para buscar imagens por produto
    List<ImgProduto> findByProduto(Produto produto);
}
