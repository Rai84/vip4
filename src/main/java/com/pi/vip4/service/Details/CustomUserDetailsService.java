package com.pi.vip4.service.details;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pi.vip4.model.User;
import com.pi.vip4.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Buscando usuário com o email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("Usuário com o email {} não encontrado", email);
                    return new UsernameNotFoundException("Usuário não encontrado");
                });

        logger.info("Usuário encontrado: ID = {}, Email = {}, Nome = {}", user.getId(), user.getEmail(), user.getNome());
        return new CustomUserDetails(user);
    }
}
