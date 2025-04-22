package com.pi.vip4.service.Details;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.pi.vip4.model.Cliente;
import com.pi.vip4.model.EnderecoEntrega;
import com.pi.vip4.model.EnderecoFaturamento;
import com.pi.vip4.repository.ClienteRepository;

@Service
public class CustomClienteDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomClienteDetailsService.class);

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Buscando cliente com o email: {}", email);

        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("Cliente com o email {} não encontrado", email);
                    return new UsernameNotFoundException("Cliente não encontrado");
                });

        logger.info("Cliente encontrado: ID = {}, Email = {}, Nome = {}, Senha = {}", cliente.getId(), cliente.getEmail(),
                cliente.getNome(), cliente.getSenha());

        if (cliente.getEnderecoFaturamento() != null) {
            logger.info("Endereço de faturamento: {}, {}, {} - {}",
                    cliente.getEnderecoFaturamento().getLogradouro(),
                    cliente.getEnderecoFaturamento().getNumero(),
                    cliente.getEnderecoFaturamento().getCidade(),
                    cliente.getEnderecoFaturamento().getUf());
        }

        if (cliente.getEnderecosEntrega() != null && !cliente.getEnderecosEntrega().isEmpty()) {
            for (EnderecoEntrega endereco : cliente.getEnderecosEntrega()) {
                logger.info("Endereço de entrega: {}, {}, {} - {}{}",
                        endereco.getLogradouro(),
                        endereco.getNumero(),
                        endereco.getCidade(),
                        endereco.getUf(),
                        endereco.isPadrao() ? " (padrão)" : "");
            }
        }

        return new CustomClienteDetails(cliente);
    }
}
