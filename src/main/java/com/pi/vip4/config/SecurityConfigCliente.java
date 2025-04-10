package com.pi.vip4.config;

import com.pi.vip4.service.CustomClienteDetailsService;
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
@Order(1) // prioridade mais alta
public class SecurityConfigCliente {

    @Bean
    public SecurityFilterChain clienteSecurityFilterChain(HttpSecurity http,
            @Qualifier("clienteAuthenticationProvider") DaoAuthenticationProvider provider) throws Exception {

        AuthenticationManager authManager = new ProviderManager(List.of(provider));

        http
                .securityMatcher("/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login-cliente", "/logout-cliente", "/clientes/new", "/css/**", "/js/**", "/enderecos-entrega/**")
                        .permitAll()
                        .requestMatchers("/cliente/area-restrita/**").hasRole("CLIENTE")
                        .anyRequest().permitAll())
                .formLogin(form -> form
                        .loginPage("/login-cliente")
                        .loginProcessingUrl("/login-cliente")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login-cliente?error=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout-cliente")
                        .logoutSuccessUrl("/login-cliente?logout=true")
                        .permitAll())
                .authenticationManager(authManager);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider clienteAuthenticationProvider(CustomClienteDetailsService clienteService,
            PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(clienteService);
        provider.setPasswordEncoder(encoder);
        return provider;
    }
}
