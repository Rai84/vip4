package com.pi.vip4.controller;

import com.pi.vip4.model.Produto;
import com.pi.vip4.model.EnderecoEntrega;
import com.pi.vip4.model.EnderecoFaturamento;
import com.pi.vip4.repository.ProdutoRepository;
import com.pi.vip4.service.CustomClienteDetails;
import com.pi.vip4.service.CustomClienteDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class TesteController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CustomClienteDetailsService customClienteDetailsService;

    @GetMapping("/")
    public String showAllProdutos(Model model, Authentication authentication) {
        List<Produto> produtos = produtoRepository.findAll();
        model.addAttribute("produtos", produtos);

        if (authentication != null) {
            String email = authentication.getName();
            UserDetails userDetails = customClienteDetailsService.loadUserByUsername(email);

            if (userDetails instanceof CustomClienteDetails clienteDetails) {
                model.addAttribute("userName", clienteDetails.getNome());
                model.addAttribute("id", clienteDetails.getId());
                model.addAttribute("email", clienteDetails.getEmail());
                model.addAttribute("cpf", clienteDetails.getCpf());
                model.addAttribute("telefone", clienteDetails.getTelefone());

                // Endereço de faturamento
                EnderecoFaturamento faturamento = clienteDetails.getEnderecoFaturamento();
                model.addAttribute("enderecoFaturamento", faturamento);

                // Lista de endereços de entrega
                List<EnderecoEntrega> enderecosEntrega = clienteDetails.getEnderecosEntrega();
                model.addAttribute("enderecosEntrega", enderecosEntrega);
            }
        }

        return "teste";
    }
}
