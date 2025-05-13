package com.pi.vip4.repository;

import com.pi.vip4.model.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {

    List<Carrinho> findByClienteId(Long clienteId);

    Optional<Carrinho> findByClienteIdAndProdutoId(Long clienteId, Long produtoId);

    @Query("SELECT c.frete FROM Carrinho c WHERE c.cliente.id = :clienteId AND c.frete IS NOT NULL")
    Optional<Double> findFreteByClienteId(Long clienteId);

    void deleteByClienteId(Long clienteId);

}
