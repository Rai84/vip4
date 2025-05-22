package com.pi.vip4.service;

import com.pi.vip4.model.Pedido;
import com.pi.vip4.model.Pedido.StatusPedido;
import com.pi.vip4.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private CarrinhoService carrinhoService;

    public Pedido salvar(Pedido pedido) {
        pedido.setNumeroPedido(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        carrinhoService.limparCarrinhoPorCliente(pedido.getCliente().getId());
        return pedidoSalvo;
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> listarPorClienteId(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + id));
    }

    // ✅ Novo método: alterar status
    public void alterarStatus(Long pedidoId, String novoStatus) {
        Pedido pedido = buscarPorId(pedidoId);
        pedido.setStatus(StatusPedido.valueOf(novoStatus));
        pedidoRepository.save(pedido);
    }
 

    
    
}
