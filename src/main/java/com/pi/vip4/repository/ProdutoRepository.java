package com.pi.vip4.repository;

import com.pi.vip4.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Buscar um produto pelo nome
    Produto findByNomeProduto(String nomeProduto);

    // Listar produtos com estoque abaixo de um valor
    List<Produto> findByEstoqueLessThan(int estoque);

    // Listar apenas produtos ativos e com estoque acima de 0
    List<Produto> findByStatusTrueAndEstoqueGreaterThan(int estoque);

    // Listar produtos com nome semelhante (case insensitive)
    Page<Produto> findByNomeProdutoContainingIgnoreCase(String nomeProduto, Pageable pageable);

    // ✅ Buscar todos os produtos já com suas imagens (resolve problema do lazy
    // loading no HTML)
    @EntityGraph(attributePaths = "imagens")
    @Query("SELECT p FROM Produto p")
    Page<Produto> buscarTodosComImagens(Pageable pageable);
}
