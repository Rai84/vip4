package com.pi.vip4.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pi.vip4.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Paginação para buscar todos os produtos
    

    // Buscar um produto pelo nome
    Produto findByNomeProduto(String nomeProduto);

    // Listar produtos com estoque abaixo de um valor
    List<Produto> findByEstoqueLessThan(int estoque);
}
