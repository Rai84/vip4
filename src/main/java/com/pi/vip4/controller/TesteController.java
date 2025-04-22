package com.pi.vip4.controller;

import com.pi.vip4.model.Cliente;
import com.pi.vip4.repository.ClienteRepository;
import com.pi.vip4.service.TesteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TesteController {

    @Autowired
    private TesteService testeService;
    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/")
    public String showAllProdutos(Model model, Authentication authentication) {
        testeService.carregarTelaInicial(model, authentication);
        return "teste";
    }

    @GetMapping("/modal-login")
    public String mostrarModalLogin() {
        return "fragments/loginModal :: div";
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
            return "fragments/editClienteModal :: div";

        } catch (Exception e) {
            e.printStackTrace(); // loga tudo no console
            throw e;
        }
    }

}
