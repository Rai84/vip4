package com.pi.vip4.service;

import com.pi.vip4.service.details.usuario.CustomUsuarioDetails;
import com.pi.vip4.service.details.usuario.CustomUsuarioDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PainelService {

    private static final Logger logger = LoggerFactory.getLogger(PainelService.class);

    @Autowired
    private CustomUsuarioDetailsService customUserDetailsService;

    public void carregarDadosDoPainel(Authentication authentication, Model model) {
        logger.info("PainelService: carregando dados para o painel...");

        String email = authentication.getName();
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        if (userDetails instanceof CustomUsuarioDetails customUser) {
            logger.info("Usuário autenticado: ID = {}, Email = {}", customUser.getId(), customUser.getEmail());

            model.addAttribute("userId", customUser.getId());
            model.addAttribute("userEmail", customUser.getEmail());
            model.addAttribute("userName", customUser.getNome());
            model.addAttribute("userType", customUser.getTipo());
        }
    }
}
