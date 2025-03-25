package com.pi.vip4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.pi.vip4.model.Produto;

import io.micrometer.core.lang.NonNull;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Paginação para buscar todos os produtos
    Page<Produto> findAll(Pageable pageable);

    // Buscar um produto pelo nome
    Produto findByNomeProduto(String nomeProduto);

    // Listar produtos com estoque abaixo de um valor
    List<Produto> findByEstoqueLessThan(int estoque);
}
