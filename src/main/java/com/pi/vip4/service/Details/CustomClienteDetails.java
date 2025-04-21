package com.pi.vip4.service.Details;

import com.pi.vip4.model.Cliente;
import com.pi.vip4.model.EnderecoEntrega;
import com.pi.vip4.model.EnderecoFaturamento;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CustomClienteDetails implements UserDetails {

    private final Cliente cliente;

    public CustomClienteDetails(Cliente cliente) {
        this.cliente = cliente;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public long getId() {
        return cliente.getId();
    }

    public String getNome() {
        return cliente.getNome();
    }

    public String getCpf() {
        return cliente.getCpf();
    }

    public String getEmail() {
        return cliente.getEmail();
    }

    public String getTelefone() {
        return cliente.getTelefone();
    }

    public Date getDataNascimento() {
        return java.sql.Date.valueOf(cliente.getDataNascimento());
    }

    public String getGenero() {
        return cliente.getGenero();
    }

    public EnderecoFaturamento getEnderecoFaturamento() {
        return cliente.getEnderecoFaturamento();
    }

    public List<EnderecoEntrega> getEnderecosEntrega() {
        return cliente.getEnderecosEntrega();
    }

    public Date getCriadoEm() {
        return cliente.getCriadoEm();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENTE"));
    }

    @Override
    public String getPassword() {
        return cliente.getSenha();
    }

    @Override
    public String getUsername() {
        return cliente.getEmail(); // utilizado para login
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
