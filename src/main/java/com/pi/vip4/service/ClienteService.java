package com.pi.vip4.service;

import com.pi.vip4.model.Cliente;
import com.pi.vip4.model.EnderecoFaturamento;
import com.pi.vip4.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<Cliente> getAllClientesPaginados(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return clienteRepository.findAll(pageable);
    }

    @Transactional
    public void salvarNovoCliente(Cliente cliente) {
        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        clienteRepository.save(cliente);
    }

    public Cliente buscarClientePorId(Long id) throws Exception {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new Exception("Cliente não encontrado: " + id));
        if (cliente.getEnderecoFaturamento() == null) {
            cliente.setEnderecoFaturamento(new EnderecoFaturamento());
        }
        return cliente;
    }

    @Transactional
    public void atualizarCliente(Long id, Cliente clienteDetails) throws Exception {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new Exception("Cliente não encontrado: " + id));

        cliente.setNome(clienteDetails.getNome());
        cliente.setCpf(clienteDetails.getCpf());
        cliente.setEmail(clienteDetails.getEmail());
        cliente.setTelefone(clienteDetails.getTelefone());
        cliente.setGenero(clienteDetails.getGenero());
        cliente.setDataNascimento(clienteDetails.getDataNascimento());

        // Garante que o endereço não seja nulo
        if (clienteDetails.getEnderecoFaturamento() != null) {
            cliente.setEnderecoFaturamento(clienteDetails.getEnderecoFaturamento());
        }

        // Só atualiza a senha se for preenchida
        if (clienteDetails.getSenha() != null && !clienteDetails.getSenha().isBlank()) {
            cliente.setSenha(passwordEncoder.encode(clienteDetails.getSenha()));
        }

        clienteRepository.save(cliente);
    }

    @Transactional
    public void excluirCliente(Long id) throws Exception {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new Exception("Cliente não encontrado: " + id));
        clienteRepository.delete(cliente);
    }
}
