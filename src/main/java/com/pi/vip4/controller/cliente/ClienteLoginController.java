package com.pi.vip4.controller.cliente;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import com.pi.vip4.model.Carrinho;
import com.pi.vip4.model.Cliente;
import com.pi.vip4.service.ClienteService; // Import the ClienteService
import com.pi.vip4.service.CarrinhoService; // Import the CarrinhoService

import jakarta.servlet.http.HttpSession;

@Controller
public class ClienteLoginController {

    @org.springframework.beans.factory.annotation.Autowired
    private ClienteService clienteService;
    @org.springframework.beans.factory.annotation.Autowired
    private CarrinhoService carrinhoService;

@PostMapping("/login-cliente")
public String loginCliente(@RequestParam String email, @RequestParam String senha, HttpSession session, Model model) {
    // Lógica de autenticação
    Cliente cliente = clienteService.autenticar(email, senha);
    
    if (cliente != null) {
        session.setAttribute("clienteId", cliente.getId());

        // Se houver um carrinho temporário, mova para o banco de dados
        List<Carrinho> carrinhoTemp = (List<Carrinho>) session.getAttribute("carrinhoTemp");
        if (carrinhoTemp != null && !carrinhoTemp.isEmpty()) {
            carrinhoService.salvarCarrinhoParaCliente(cliente.getId(), carrinhoTemp);
            session.removeAttribute("carrinhoTemp"); // Limpa o carrinho temporário após salvar
        }

        // Redireciona para o modal de checkout
        return "redirect:/cliente/modal-checkout";
    }

    // Se não autenticar, exibe erro de login
    model.addAttribute("erro", "E-mail ou senha inválidos.");
    return "fragments/modal-login-cliente"; // Retorna para a tela de login com erro
    }

}