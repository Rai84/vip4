package com.pi.vip4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import com.pi.vip4.model.User;
import com.pi.vip4.repository.UserRepository;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder; // Injetando o PasswordEncoder

  
  @GetMapping
    public String getAllUsers(Model model, @RequestParam(defaultValue = "0") int page) {
    Pageable pageable = PageRequest.of(page, 10); // 10 usuários por página
    Page<User> usersPage = userRepository.findAll(pageable);
    
    model.addAttribute("users", usersPage.getContent()); // Lista de usuários para o template
    model.addAttribute("usersPage", usersPage); // Para controle de paginação
    
    return "user-list"; // Nome do arquivo HTML em src/main/resources/templates/
  }


  
  @GetMapping("/new")
  public String showCreateUserForm(Model model) { // Exibe formulário para adicionar novo usuário
    model.addAttribute("user", new User()); // Cria um novo usuário vazio
    return "create-user-form"; // Formulário para criar usuário
  }

  
  @PostMapping("/save")
  public String createUser(@Valid @ModelAttribute User user) { // Salva um novo usuário
    // Criptografa a senha antes de salvar
    user.setSenha(passwordEncoder.encode(user.getSenha())); // Usando o passwordEncoder injetado
    userRepository.save(user);
    return "redirect:/users"; // Redireciona para a lista
  }

  
  @GetMapping("/edit/{id}")
  public String showUpdateUserForm(@PathVariable("id") Long userId, Model model) throws Exception { // Exibe formulário para editar um usuário existente
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new Exception("Usuário não encontrado: " + userId));
    model.addAttribute("user", user);
    return "edit-user-form"; // Formulário para editar usuário
  }

  
  @PostMapping("/update/{id}")
  public String updateUser(@PathVariable("id") Long userId, @Valid @ModelAttribute User userDetails) // Atualiza um usuário existente
      throws Exception {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new Exception("Usuário não encontrado: " + userId));

    // Atualiza os dados do usuário
    user.setNome(userDetails.getNome());
    user.setSenha(passwordEncoder.encode(userDetails.getSenha())); // Criptografa a senha
    user.setTipo(userDetails.getTipo());
    user.setStatus(userDetails.isStatus()); // Atualiza o status

    userRepository.save(user);
    return "redirect:/users"; // Redireciona para a lista
  }

  
  @GetMapping("/delete/{id}")
  public String deleteUser(@PathVariable("id") Long userId) throws Exception { // Deleta um usuário
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new Exception("Usuário não encontrado: " + userId));

    userRepository.delete(user);
    return "redirect:/users"; // Redireciona para a lista
  }

  
  @GetMapping("/toggle-status/{id}")
  public String toggleUserStatus(@PathVariable("id") Long userId) throws Exception { // Alterna o status de um usuário (Ativar/Desativar)
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new Exception("Usuário não encontrado: " + userId));

    // Inverte o status do usuário (se for "ativo", vira "inativo", e vice-versa)
    user.setStatus(!user.isStatus());
    userRepository.save(user);

    return "redirect:/users"; // Redireciona para a lista de usuários
  }
}
