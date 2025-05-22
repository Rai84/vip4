package com.pi.vip4.config;

import com.pi.vip4.service.details.CustomClienteDetailsService;
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
@Order(1)
public class SecurityConfigCliente {

        @Bean
        public SecurityFilterChain clienteSecurityFilterChain(HttpSecurity http,
                        @Qualifier("clienteAuthenticationProvider") DaoAuthenticationProvider provider)
                        throws Exception {

                AuthenticationManager authManager = new ProviderManager(List.of(provider));

                http
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/", "/login-cliente", "/logout-cliente",
                                                                "/clientes/**", "/enderecos-entrega/**",
                                                                "/modal-login-cliente", "/produtos/**",
                                                                "/cliente/login-sucesso", // ✅ nova rota liberada
                                                                "/css/**", "/js/**", "/images/**", "/error", "/cliente/new", 
                                                                "/cliente/save")
                                                .permitAll()
                                                .requestMatchers("/cliente/**").hasRole("CLIENTE") // protege tudo de
                                                                                                   // cliente
                                                .anyRequest().permitAll())
                                .formLogin(form -> form
                                                .loginPage("/login-cliente")
                                                .loginProcessingUrl("/login-cliente")
                                                .defaultSuccessUrl("/cliente/login-sucesso", true) // ✅ redireciona após
                                                                                                   // login
                                                .failureUrl("/login-cliente?error=true")
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout-cliente")
                                                .logoutSuccessUrl("/")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID"))
                                .sessionManagement(session -> session
                                                .sessionFixation().newSession())
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
