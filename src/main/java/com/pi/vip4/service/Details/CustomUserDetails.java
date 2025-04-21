package com.pi.vip4.service.Details;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.pi.vip4.model.User;
import com.pi.vip4.model.User.Tipo;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private String nome; // Adicionado para armazenar o nome do usuário
    private Tipo tipo; // Adicionado para armazenar o tipo do usuário
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.username = user.getEmail(); // O e-mail será usado como username
        this.password = user.getSenha();
        this.nome = user.getNome(); // Pegando o nome do usuário
        this.tipo = user.getTipo(); // Pegando o tipo do usuário
        this.authorities = Collections.emptyList(); // Evita erro se não houver authorities
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return username; // Método para obter o e-mail do usuário
    }

    public String getNome() {
        return nome; // Método para obter o nome do usuário
    }

    public Tipo getTipo() {
        return tipo; // Método para obter o tipo do usuário
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
