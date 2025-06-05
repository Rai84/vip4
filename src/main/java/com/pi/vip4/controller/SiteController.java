package com.pi.vip4.controller;

import com.pi.vip4.model.Cliente;
import com.pi.vip4.model.EnderecoEntrega;
import com.pi.vip4.model.Carrinho;
import com.pi.vip4.repository.ClienteRepository;
import com.pi.vip4.service.CarrinhoService;
import com.pi.vip4.service.TesteService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SiteController {

    @Autowired
    private TesteService testeService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CarrinhoService carrinhoService;

    @GetMapping("/")
    public String showAllProdutos(Model model, Authentication authentication) {
        testeService.carregarTelaInicial(model, authentication);

        model.addAttribute("endereco", new EnderecoEntrega());

        return "teste";
    }

    @GetMapping("/modal/modal1") // Modal de login
    public String mostrarModalLogin() {
        return "fragments/modal1";
    }

    @GetMapping("/modal/modal4")
    public String getModalEditarCliente(Model model, Authentication authentication) {
        try {
            if (authentication == null) {
                System.out.println("⚠️ authentication está null");
                throw new RuntimeException("Usuário não está autenticado.");
            }

            String email = authentication.getName();
            System.out.println("🔍 E-mail autenticado: " + email);

            Cliente cliente = clienteRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("❌ Cliente não encontrado com e-mail: " + email));

            model.addAttribute("cliente", cliente);
            return "fragments/modal4";

        } catch (Exception e) {
            e.printStackTrace(); // loga tudo no console
            throw e;
        }
    }

    @GetMapping("/modal/modal2") // Modal de cadastro
    public String getModal(Model model, Authentication authentication) {
        if (authentication == null) {
            throw new RuntimeException("Usuário não está autenticado.");
        }

        String email = authentication.getName();
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com e-mail: " + email));

        model.addAttribute("userName", cliente.getNome());
        model.addAttribute("id", cliente.getId());
        model.addAttribute("email", cliente.getEmail());

        return "fragments/modal2";
    }

    @GetMapping("/modal/modal3")
    public String mostrarModalCarrinho(Model model, Authentication authentication, HttpSession session) {
        Long clienteId = (Long) session.getAttribute("clienteId");

        if (clienteId == null) {
            System.out.println("Usuário não autenticado. Carrinho vazio.");
            // Recupera o carrinho temporário da sessão
            List<Carrinho> itensTemp = (List<Carrinho>) session.getAttribute("carrinhoTemp");
            if (itensTemp == null) {
                itensTemp = new ArrayList<>();
            }
            model.addAttribute("itens", itensTemp); // Carrinho temporário
            model.addAttribute("total", 0.0); // Total igual a 0 para não logado
            model.addAttribute("frete", 0.0); // Frete igual a 0 para não logado
            return "fragments/modal3"; // Exibe carrinho vazio ou temporário
        }

        // Caso o cliente esteja logado, carregar os itens do carrinho do cliente
        String email = authentication.getName();
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        List<Carrinho> itens = carrinhoService.getItensPorCliente(cliente.getId());
        double subtotal = itens.stream().mapToDouble(c -> c.getTotal().doubleValue()).sum();
        double frete = itens.stream().findFirst().map(c -> c.getFrete() != null ? c.getFrete() : 0.0).orElse(0.0);
        double total = subtotal + frete;

        model.addAttribute("itens", itens); // Carrinho do cliente logado
        model.addAttribute("total", total);
        model.addAttribute("frete", frete);

        return "fragments/modal3"; // Exibe carrinho do cliente logado
    }

}
