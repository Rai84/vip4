package com.pi.vip4.service;

import com.pi.vip4.model.EnderecoEntrega;
import com.pi.vip4.model.EnderecoFaturamento;
import com.pi.vip4.model.Produto;
import com.pi.vip4.repository.ProdutoRepository;
import com.pi.vip4.service.details.cliente.CustomClienteDetails;
import com.pi.vip4.service.details.cliente.CustomClienteDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class TesteService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CustomClienteDetailsService customClienteDetailsService;

    public void carregarTelaInicial(Model model, Authentication authentication) {
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
                model.addAttribute("genero", clienteDetails.getGenero());
                model.addAttribute("nascimento", clienteDetails.getDataNascimento());

                EnderecoFaturamento faturamento = clienteDetails.getEnderecoFaturamento();
                model.addAttribute("enderecoFaturamento", faturamento);

                List<EnderecoEntrega> enderecosEntrega = clienteDetails.getEnderecosEntrega();
                model.addAttribute("enderecosEntrega", enderecosEntrega);
            }
        }
    }
}
