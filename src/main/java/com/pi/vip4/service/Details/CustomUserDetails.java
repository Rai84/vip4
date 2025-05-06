package com.pi.vip4.service.details;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.pi.vip4.model.User;
import com.pi.vip4.model.User.Tipo;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private String nome;
    private Tipo tipo;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.username = user.getEmail(); // E-mail como username
        this.password = user.getSenha();
        this.nome = user.getNome();
        this.tipo = user.getTipo();

        // Corrigido: registra a role com prefixo ROLE_
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + tipo.name()));
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return username;
    }

    public String getNome() {
        return nome;
    }

    public Tipo getTipo() {
        return tipo;
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
