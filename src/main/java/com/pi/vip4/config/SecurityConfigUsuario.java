package com.pi.vip4.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.pi.vip4.service.details.usuario.CustomUsuarioDetailsService;

import java.util.List;

@Configuration
@Order(2)
public class SecurityConfigUsuario {

        @Bean
        public SecurityFilterChain userSecurityFilterChain(HttpSecurity http,
                        @Qualifier("userAuthenticationProvider") DaoAuthenticationProvider provider) throws Exception {

                AuthenticationManager authManager = new ProviderManager(List.of(provider));

                http
                                .securityMatcher("/admin/**", "/estoque/**", "/painel", "/login", "/logout")
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/produtos/**", "/uploads/**", "/", "/css/**", "/modal-login-cliente",
                                                                "/js/**")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.GET, "/produtos/**")
                                                .hasAnyRole("ADMIN", "ESTOQUISTA")
                                                .requestMatchers(HttpMethod.POST, "/produtos/**")
                                                .hasAnyRole("ADMIN", "ESTOQUISTA")
                                                .requestMatchers("/admin/**", "/estoque/**")
                                                .hasAnyRole("ADMIN", "ESTOQUISTA")
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
                                .sessionManagement(session -> session
                                                .sessionFixation().newSession())
                                .authenticationManager(authManager);

                return http.build();
        }

        @Bean
        public DaoAuthenticationProvider userAuthenticationProvider(CustomUsuarioDetailsService userService,
                        PasswordEncoder encoder) {
                DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
                provider.setUserDetailsService(userService);
                provider.setPasswordEncoder(encoder);
                return provider;
        }
}
