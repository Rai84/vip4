package com.pi.vip4.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pi.vip4.model.User;
import com.pi.vip4.model.User.Tipo;
import com.pi.vip4.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Use PasswordEncoder ao invés de instanciar BCrypt diretamente

    public CustomUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Tentando autenticar usuário com email: {}", email);

        User user = userRepository.findByEmail(email);
        if (user == null) {
            logger.warn("Usuário não encontrado: {}", email);
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        if (!user.isStatus()) {
            logger.warn("Usuário {} está inativo!", email);
            throw new UsernameNotFoundException("Usuário inativo");
        }

        String role = user.getTipo() == Tipo.ADMIN ? "ADMIN" : "ESTOQUISTA";

        logger.info("Usuário autenticado: {} | Tipo: {} | Status: Ativo", user.getEmail(), role);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getSenha())
                .roles(role)
                .build();
    }
}
