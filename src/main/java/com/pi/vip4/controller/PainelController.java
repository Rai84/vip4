package com.pi.vip4.controller;

import org.springframework.security.core.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pi.vip4.service.CustomUserDetails;
import com.pi.vip4.service.CustomUserDetailsService;

@Controller
@RequestMapping("/painel") // Mapeamento base para o controlador
public class PainelController {

    private static final Logger logger = LoggerFactory.getLogger(PainelController.class);

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // Mapeamento do método
    @GetMapping // Agora sem "/painel", pois já está mapeado no RequestMapping da classe
    public String painel(Model model, Authentication authentication) {
        logger.info("Método painel() chamado.");
        String email = authentication.getName(); // Obtém o nome de usuário (email)

        // Obtém os detalhes do usuário
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        // Adiciona os dados do usuário ao modelo
        if (userDetails instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
            logger.info("Dados do usuário: ID = {}, Email = {}", customUserDetails.getId(),
                    customUserDetails.getEmail());

            // Adicionando os dados ao modelo
            model.addAttribute("userId", customUserDetails.getId());
            model.addAttribute("userEmail", customUserDetails.getEmail());

            // Certifique-se de pegar o nome correto do usuário
            model.addAttribute("userName", customUserDetails.getNome()); // Aqui, use o método correto para pegar o nome
            model.addAttribute("userType", customUserDetails.getTipo()); // Aqui, o tipo do usuário, conforme a sua
                                                                         // classe
        }

        return "painel"; // Nome do template HTML
    }
}
