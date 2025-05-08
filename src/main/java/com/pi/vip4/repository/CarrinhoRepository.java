package com.pi.vip4.repository;

import com.pi.vip4.model.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {

    List<Carrinho> findByClienteId(Long clienteId);

    Optional<Carrinho> findByClienteIdAndProdutoId(Long clienteId, Long produtoId);
}
