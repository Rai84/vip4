package com.pi.vip4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.pi.vip4.model.Produto;
import com.pi.vip4.repository.ProdutoRepository;

import java.util.List;

@Controller
public class Teste {

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping("/teste")
    public String showAllProdutos(Model model) {
        List<Produto> produtos = produtoRepository.findAll();
        model.addAttribute("produtos", produtos);
        return "teste"; // Nome do arquivo HTML (src/main/resources/templates/teste.html)
    }
}
