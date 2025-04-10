package com.pi.vip4.service;

import com.pi.vip4.model.Cliente;
import com.pi.vip4.model.User;
import com.pi.vip4.repository.ClienteRepository;
import com.pi.vip4.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UnifiedUserDetailsService implements UserDetailsService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return clienteRepository.findByEmail(email)
                .<UserDetails>map(CustomClienteDetails::new)
                .orElseGet(() -> userRepository.findByEmail(email)
                        .<UserDetails>map(CustomUserDetails::new)
                        .orElseThrow(() -> new UsernameNotFoundException(
                                "Cliente ou Usuário não encontrado com email: " + email)));
    }
}
