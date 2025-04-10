package com.pi.vip4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import com.pi.vip4.model.User;
import com.pi.vip4.repository.UserRepository;
import com.pi.vip4.service.CustomUserDetails;
import com.pi.vip4.service.CustomUserDetailsService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/users")
public class UserController {

  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  @GetMapping
  public String getAllUsers(Model model, @RequestParam(defaultValue = "0") int page) {
    Pageable pageable = PageRequest.of(page, 10);
    Page<User> usersPage = userRepository.findAll(pageable);

    model.addAttribute("users", usersPage.getContent());
    model.addAttribute("usersPage", usersPage);

    return "user-list";
  }

  @GetMapping("/new")
  public String showCreateUserForm(Model model) {
    model.addAttribute("user", new User());
    return "create-user-form";
  }

  @PostMapping("/save")
  public String createUser(@Valid @ModelAttribute User user) {
    user.setSenha(passwordEncoder.encode(user.getSenha()));
    userRepository.save(user);
    return "redirect:/users";
  }

  @GetMapping("/edit/{id}")
  public String showUpdateUserForm(@PathVariable("id") Long userId, Model model) throws Exception {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new Exception("Usuário não encontrado: " + userId));
    model.addAttribute("user", user);
    return "edit-user-form";
  }

  @PostMapping("/update/{id}")
  public String updateUser(@PathVariable("id") Long userId, @Valid @ModelAttribute User userDetails) throws Exception {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new Exception("Usuário não encontrado: " + userId));

    user.setNome(userDetails.getNome());
    user.setSenha(passwordEncoder.encode(userDetails.getSenha()));
    user.setTipo(userDetails.getTipo());
    user.setStatus(userDetails.isStatus());

    userRepository.save(user);
    return "redirect:/users";
  }

  @GetMapping("/delete/{id}")
  public String deleteUser(@PathVariable("id") Long userId) throws Exception {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new Exception("Usuário não encontrado: " + userId));

    userRepository.delete(user);
    return "redirect:/users";
  }

  @GetMapping("/toggle-status/{id}")
  public String toggleUserStatus(@PathVariable("id") Long userId) throws Exception {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new Exception("Usuário não encontrado: " + userId));

    user.setStatus(!user.isStatus());
    userRepository.save(user);

    return "redirect:/users";
  }
}
