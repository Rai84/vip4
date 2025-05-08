package com.pi.vip4.controller;

import com.pi.vip4.model.Cliente;
import com.pi.vip4.model.Carrinho;
import com.pi.vip4.repository.ClienteRepository;
import com.pi.vip4.service.CarrinhoService;
import com.pi.vip4.service.TesteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class TesteController {

    @Autowired
    private TesteService testeService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CarrinhoService carrinhoService;

    @GetMapping("/")
    public String showAllProdutos(Model model, Authentication authentication) {
        testeService.carregarTelaInicial(model, authentication);
        return "teste";
    }

    @GetMapping("/modal-login-cliente")
    public String mostrarModalLogin() {
        return "fragments/loginModal :: div";
    }

    @GetMapping("/modal-carrinho")
    public String mostrarModalCarrinho(Model model, Authentication authentication) {
        try {
            if (authentication == null) {
                throw new RuntimeException("Usuário não autenticado");
            }

            String email = authentication.getName();
            Cliente cliente = clienteRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

            List<Carrinho> itens = carrinhoService.getItensPorCliente(cliente.getId());

            double subtotal = itens.stream()
                    .mapToDouble(c -> c.getTotal().doubleValue())
                    .sum();

            double frete = itens.stream()
                    .findFirst()
                    .map(c -> c.getFrete() != null ? c.getFrete() : 0.0)
                    .orElse(0.0);

            double total = subtotal + frete;

            model.addAttribute("itens", itens);
            model.addAttribute("total", total);
            model.addAttribute("frete", frete);

            return "fragments/carrinhoModal :: div";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/modal-editar-cliente")
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
            return "fragments/editClienteModal :: modalEditarCliente";

        } catch (Exception e) {
            e.printStackTrace(); // loga tudo no console
            throw e;
        }
    }

    @GetMapping("/modal-cliente")
    public String getModalCliente(Model model, Authentication authentication) {
        if (authentication == null) {
            throw new RuntimeException("Usuário não está autenticado.");
        }

        String email = authentication.getName();
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com e-mail: " + email));

        model.addAttribute("userName", cliente.getNome());
        model.addAttribute("id", cliente.getId());
        model.addAttribute("email", cliente.getEmail());

        return "fragments/ModalCliente :: modalCliente";
    }

}
