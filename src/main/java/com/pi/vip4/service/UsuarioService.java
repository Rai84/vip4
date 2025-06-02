package com.pi.vip4.service;

import com.pi.vip4.model.Usuario;
import com.pi.vip4.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<Usuario> listarUsuarios(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional
    public void salvarNovoUsuario(@Valid Usuario user) {
        user.setSenha(passwordEncoder.encode(user.getSenha()));
        userRepository.save(user);
    }

    public Usuario buscarPorId(Long id) throws Exception {
        return userRepository.findById(id)
                .orElseThrow(() -> new Exception("Usuário não encontrado: " + id));
    }

    @Transactional
    public void atualizarUsuario(Long id, @Valid Usuario dados) throws Exception {
        Usuario user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("Usuário não encontrado: " + id));

        user.setNome(dados.getNome());
        user.setSenha(passwordEncoder.encode(dados.getSenha()));
        user.setTipo(dados.getTipo());
        user.setStatus(dados.isStatus());

        userRepository.save(user);
    }

    @Transactional
    public void excluirUsuario(Long id) throws Exception {
        Usuario user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("Usuário não encontrado: " + id));
        userRepository.delete(user);
    }

    @Transactional
    public void alternarStatus(Long id) throws Exception {
        Usuario user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("Usuário não encontrado: " + id));
        user.setStatus(!user.isStatus());
        userRepository.save(user);
    }
}
