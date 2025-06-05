package com.pi.vip4.controller.carrinho;

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
import java.util.ArrayList;
import java.util.List;

@Controller
public class CarrinhoModalController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CarrinhoService carrinhoService;

    @GetMapping("/cliente/modal-checkout")
public String exibirModalCheckout(Model model, HttpSession session) throws Exception {
    // Log para verificar se o cliente está autenticado
    Long clienteId = (Long) session.getAttribute("clienteId");
    System.out.println("🔍 Verificando clienteId na sessão: " + clienteId);

    if (clienteId != null) {
        // Se o cliente estiver logado, recupera o cliente do banco de dados
        System.out.println("Cliente autenticado com ID: " + clienteId);

        Cliente cliente = clienteService.buscarClientePorId(clienteId);
        if (cliente != null) {
            System.out.println("Cliente encontrado: " + cliente.getNome());
        } else {
            System.out.println("Erro: Cliente não encontrado com ID: " + clienteId);
        }

        List<EnderecoEntrega> enderecos = cliente.getEnderecosEntrega();
        System.out.println("Endereços de entrega encontrados: " + enderecos.size());

        // Recupera os itens do carrinho e calcula subtotal e total
        List<Carrinho> itens = carrinhoService.listarPorClienteId(clienteId);
        double frete = carrinhoService.obterFreteAtual(clienteId);
        BigDecimal subtotal = carrinhoService.calcularSubtotal(itens);
        BigDecimal total = subtotal.add(BigDecimal.valueOf(frete));

        System.out.println("Subtotal do carrinho: " + subtotal);
        System.out.println("Frete: " + frete);
        System.out.println("Total do carrinho: " + total);

        model.addAttribute("enderecos", enderecos);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("frete", frete);
        model.addAttribute("total", total);
        model.addAttribute("itens", itens);
    } else {
        // Se o usuário não estiver logado, mostramos um carrinho vazio ou temporário
        System.out.println("Usuário não autenticado. Buscando carrinho temporário na sessão...");

        List<Carrinho> itensTemporarios = (List<Carrinho>) session.getAttribute("carrinhoTemp");
        if (itensTemporarios == null) {
            itensTemporarios = new ArrayList<>(); // Se não houver itens, cria uma lista vazia
            System.out.println("Carrinho temporário vazio.");
        } else {
            System.out.println("Carrinho temporário encontrado com " + itensTemporarios.size() + " itens.");
        }

        BigDecimal subtotal = carrinhoService.calcularSubtotal(itensTemporarios);
        double frete = 0.0; // Frete para deslogado é 0
        BigDecimal total = subtotal.add(BigDecimal.valueOf(frete));

        System.out.println("Subtotal do carrinho temporário: " + subtotal);
        System.out.println("Frete do carrinho temporário: " + frete);
        System.out.println("Total do carrinho temporário: " + total);

        model.addAttribute("itens", itensTemporarios);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("frete", frete);
        model.addAttribute("total", total);
    }

    return "fragments/checkoutModal :: div"; // Retorna o fragmento de checkout modal
}

}
