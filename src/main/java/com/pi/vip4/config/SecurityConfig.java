package com.pi.vip4.config;

import com.pi.vip4.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@Order(2) // prioridade menor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http,
            @Qualifier("userAuthenticationProvider") DaoAuthenticationProvider provider) throws Exception {

        AuthenticationManager authManager = new ProviderManager(List.of(provider));

        http
                .securityMatcher("/login", "/logout", "/admin/**", "/estoque/**", "/painel", "/enderecos-entrega/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/logout", "/css/**", "/js/**", "/painel").permitAll()
                        .requestMatchers("/admin/**", "/estoque/**", "/painel").hasAnyRole("ADMIN", "ESTOQUISTA")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/painel", true)
                        .failureUrl("/login?error=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll())
                .authenticationManager(authManager);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider userAuthenticationProvider(CustomUserDetailsService userService,
            PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(encoder);
        return provider;
    }
}
