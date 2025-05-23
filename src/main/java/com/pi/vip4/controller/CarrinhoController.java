package com.pi.vip4.controller;

import com.pi.vip4.model.Carrinho;
import com.pi.vip4.model.Cliente;
import com.pi.vip4.model.EnderecoEntrega;
import com.pi.vip4.service.CarrinhoService;
import com.pi.vip4.service.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/carrinho")
public class CarrinhoController {

    @Autowired
    private CarrinhoService carrinhoService;

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/adicionar")
    public Carrinho adicionarAoCarrinho(
            @RequestParam Long clienteId,
            @RequestParam Long produtoId,
            @RequestParam int quantidade) {
        return carrinhoService.adicionarAoCarrinho(clienteId, produtoId, quantidade);
    }

    @DeleteMapping("/remover")
    public void removerItem(@RequestParam Long id) {
        carrinhoService.removerItem(id);
    }

    @PatchMapping("/atualizar")
    public Carrinho atualizarQuantidade(
            @RequestParam Long id,
            @RequestParam int quantidade) {
        return carrinhoService.atualizarQuantidade(id, quantidade);
    }

    @PatchMapping("/frete")
    public void atualizarFrete(@RequestParam Long clienteId,
            @RequestParam(name = "valor", required = true) Double valor) {
        carrinhoService.atualizarFrete(clienteId, valor);
    }

    @GetMapping("/total")
    public Double getTotalCarrinho(@RequestParam Long clienteId) {
        return carrinhoService.calcularTotalComFrete(clienteId);
    }
}
