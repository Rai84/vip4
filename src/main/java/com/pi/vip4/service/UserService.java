package com.pi.vip4.service;

import com.pi.vip4.model.User;
import com.pi.vip4.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<User> listarUsuarios(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional
    public void salvarNovoUsuario(@Valid User user) {
        user.setSenha(passwordEncoder.encode(user.getSenha()));
        userRepository.save(user);
    }

    public User buscarPorId(Long id) throws Exception {
        return userRepository.findById(id)
                .orElseThrow(() -> new Exception("Usuário não encontrado: " + id));
    }

    @Transactional
    public void atualizarUsuario(Long id, @Valid User dados) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("Usuário não encontrado: " + id));

        user.setNome(dados.getNome());
        user.setSenha(passwordEncoder.encode(dados.getSenha()));
        user.setTipo(dados.getTipo());
        user.setStatus(dados.isStatus());

        userRepository.save(user);
    }

    @Transactional
    public void excluirUsuario(Long id) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("Usuário não encontrado: " + id));
        userRepository.delete(user);
    }

    @Transactional
    public void alternarStatus(Long id) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("Usuário não encontrado: " + id));
        user.setStatus(!user.isStatus());
        userRepository.save(user);
    }
}
