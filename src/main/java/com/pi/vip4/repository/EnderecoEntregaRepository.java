package com.pi.vip4.repository;

import com.pi.vip4.model.EnderecoEntrega;
import com.pi.vip4.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EnderecoEntregaRepository extends JpaRepository<EnderecoEntrega, Long> {
    List<EnderecoEntrega> findByCliente(Cliente cliente);
}
