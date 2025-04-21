package com.pi.vip4.service;

import com.pi.vip4.model.Cliente;
import com.pi.vip4.model.EnderecoEntrega;
import com.pi.vip4.repository.ClienteRepository;
import com.pi.vip4.repository.EnderecoEntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnderecoEntregaService {

    @Autowired
    private EnderecoEntregaRepository enderecoEntregaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public List<EnderecoEntrega> listarEnderecosDoCliente(Long clienteId) throws Exception {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new Exception("Cliente não encontrado: " + clienteId));
        return cliente.getEnderecosEntrega();
    }

    public EnderecoEntrega novoEnderecoParaCliente(Long clienteId) {
        EnderecoEntrega endereco = new EnderecoEntrega();
        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        endereco.setCliente(cliente);
        return endereco;
    }

    public Long salvarEndereco(EnderecoEntrega enderecoEntrega) {
        EnderecoEntrega salvo = enderecoEntregaRepository.save(enderecoEntrega);
        return salvo.getCliente().getId();
    }

    public EnderecoEntrega buscarEnderecoPorId(Long id) throws Exception {
        return enderecoEntregaRepository.findById(id)
                .orElseThrow(() -> new Exception("Endereço não encontrado: " + id));
    }

    public Long excluirEndereco(Long id) throws Exception {
        EnderecoEntrega endereco = enderecoEntregaRepository.findById(id)
                .orElseThrow(() -> new Exception("Endereço não encontrado: " + id));
        Long clienteId = endereco.getCliente().getId();
        enderecoEntregaRepository.delete(endereco);
        return clienteId;
    }
}
