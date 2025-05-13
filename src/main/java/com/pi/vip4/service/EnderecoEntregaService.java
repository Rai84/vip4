package com.pi.vip4.service;

import com.pi.vip4.model.Cliente;
import com.pi.vip4.model.EnderecoEntrega;
import com.pi.vip4.repository.EnderecoEntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnderecoEntregaService {

    @Autowired
    private EnderecoEntregaRepository enderecoEntregaRepository;

    public List<EnderecoEntrega> listarPorCliente(Cliente cliente) {
        return enderecoEntregaRepository.findByCliente(cliente);
    }

    public EnderecoEntrega salvar(EnderecoEntrega endereco) {
        return enderecoEntregaRepository.save(endereco);
    }

    public void remover(Long id) {
        enderecoEntregaRepository.deleteById(id);
    }

    public void definirComoPadrao(EnderecoEntrega endereco) {
        Cliente cliente = endereco.getCliente();
        // Zera o "padrao" de todos os outros do cliente
        List<EnderecoEntrega> enderecos = enderecoEntregaRepository.findByCliente(cliente);
        for (EnderecoEntrega e : enderecos) {
            e.setPadrao(false);
        }
        endereco.setPadrao(true);
        enderecos.add(endereco);
        enderecoEntregaRepository.saveAll(enderecos);
    }

    public EnderecoEntrega buscarPorId(Long id) {
        return enderecoEntregaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado: " + id));
    }
}
