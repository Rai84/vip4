package com.pi.vip4.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.pi.vip4.model.User;
import com.pi.vip4.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // Para comparar senhas

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Tentando autenticar usuário com email: {}", email);

        User user = userRepository.findByEmail(email);
        if (user == null) {
            logger.warn("Usuário não encontrado: {}", email);
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        logger.info("Usuário encontrado: {}", user.getEmail());
        logger.info("Senha armazenada no banco (hash): {}", user.getSenha());

        // Simulação de senha digitada pelo usuário (apenas para teste)
        String senhaDigitada = "123456"; // Substitua pela senha real digitada no login
        boolean senhaCorreta = passwordEncoder.matches(senhaDigitada, user.getSenha());

        if (senhaCorreta) {
            logger.info("A senha digitada está correta!");
        } else {
            logger.warn("A senha digitada está incorreta!");
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getSenha()) // Senha criptografada
                .roles("USER") // Ajuste conforme necessário
                .build();
    }
}
