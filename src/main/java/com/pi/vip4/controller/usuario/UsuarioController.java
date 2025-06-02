package com.pi.vip4.controller.usuario;

import com.pi.vip4.model.Usuario;
import com.pi.vip4.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UsuarioController {

  @Autowired
  private UsuarioService userService;

  @GetMapping
  public String getAllUsers(Model model, @RequestParam(defaultValue = "0") int page) {
    Page<Usuario> usersPage = userService.listarUsuarios(PageRequest.of(page, 10));
    model.addAttribute("users", usersPage.getContent());
    model.addAttribute("usersPage", usersPage);
    return "list-user";
  }

  @GetMapping("/new")
  public String showCreateUserForm(Model model) {
    model.addAttribute("user", new Usuario());
    return "create-user-form";
  }

  @PostMapping("/save")
  public String createUser(@Valid @ModelAttribute Usuario user) {
    userService.salvarNovoUsuario(user);
    return "redirect:/users";
  }

  @GetMapping("/edit/{id}")
  public String showUpdateUserForm(@PathVariable("id") Long userId, Model model) throws Exception {
    model.addAttribute("user", userService.buscarPorId(userId));
    return "edit-user-form";
  }

  @PostMapping("/update/{id}")
  public String updateUser(@PathVariable("id") Long userId, @Valid @ModelAttribute Usuario userDetails) throws Exception {
    userService.atualizarUsuario(userId, userDetails);
    return "redirect:/users";
  }

  @GetMapping("/delete/{id}")
  public String deleteUser(@PathVariable("id") Long userId) throws Exception {
    userService.excluirUsuario(userId);
    return "redirect:/users";
  }

  @GetMapping("/toggle-status/{id}")
  public String toggleUserStatus(@PathVariable("id") Long userId) throws Exception {
    userService.alternarStatus(userId);
    return "redirect:/users";
  }
}
