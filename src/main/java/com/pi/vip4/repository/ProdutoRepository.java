package com.pi.vip4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.pi.vip4.model.Produto;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    // Exemplo de método personalizado: buscar um produto pelo nome
    Produto findByNomeProduto(String nomeProduto);

    // Você pode adicionar mais métodos personalizados conforme necessário
    // Exemplo: listar produtos com estoque abaixo de um valor
    List<Produto> findByEstoqueLessThan(int estoque);
}
