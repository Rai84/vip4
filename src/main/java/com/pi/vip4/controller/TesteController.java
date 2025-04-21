package com.pi.vip4.controller;

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

    @GetMapping("/")
    public String showAllProdutos(Model model, Authentication authentication) {
        testeService.carregarTelaInicial(model, authentication);
        return "teste";
    }
}
