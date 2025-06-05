package com.pi.vip4.repository;

import com.pi.vip4.model.Pedido;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.enderecoEntrega WHERE p.id = :id")
    Optional<Pedido> findByIdComEndereco(@Param("id") Long id);

    List<Pedido> findByClienteId(Long clienteId);

}
