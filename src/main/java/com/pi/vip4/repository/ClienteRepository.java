package com.pi.vip4.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pi.vip4.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Busca um cliente pelo e-mail (usado no login/autenticação)
    @Query("SELECT c FROM Cliente c LEFT JOIN FETCH c.enderecosEntrega LEFT JOIN FETCH c.enderecoFaturamento WHERE c.email = :email")
    Optional<Cliente> findByEmail(@Param("email") String email);

    // save(Cliente cliente) → salva ou atualiza
    // findById(Long id) → busca por ID
    // findAll(Pageable pageable) → retorna página de clientes
    // delete(Cliente cliente) → deleta um cliente
}
