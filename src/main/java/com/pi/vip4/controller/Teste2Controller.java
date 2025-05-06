package com.pi.vip4.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Teste2Controller {

    @GetMapping("/livre")
    @ResponseBody
    public String testeLivre() {
        return "Rota /livre acessada com sucesso!";
    }
}
