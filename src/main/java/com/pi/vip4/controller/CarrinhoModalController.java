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
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller // <- IMPORTANTE: não usar @RestController aqui
public class CarrinhoModalController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CarrinhoService carrinhoService;

    @GetMapping("/cliente/modal-checkout")
    public String exibirModalCheckout(Model model, HttpSession session) throws Exception {
        Long clienteId = (Long) session.getAttribute("clienteId");

        if (clienteId == null) {
            return "redirect:/login-cliente";
        }

        Cliente cliente = clienteService.buscarClientePorId(clienteId);
        List<EnderecoEntrega> enderecos = cliente.getEnderecosEntrega();

        List<Carrinho> itens = carrinhoService.listarPorClienteId(clienteId);
        double frete = carrinhoService.obterFreteAtual(clienteId);
        BigDecimal subtotal = carrinhoService.calcularSubtotal(itens);
        BigDecimal total = subtotal.add(BigDecimal.valueOf(frete));

        model.addAttribute("enderecos", enderecos);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("frete", frete);
        model.addAttribute("total", total);
        model.addAttribute("itens", itens);

        return "fragments/checkoutModal :: div"; // este fragmento deve existir
    }
}
