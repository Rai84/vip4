package com.pi.vip4.controller.pedido;

import com.pi.vip4.model.Carrinho;
import com.pi.vip4.model.Cliente;
import com.pi.vip4.model.EnderecoEntrega;
import com.pi.vip4.model.ItemPedido;
import com.pi.vip4.model.Pedido;
import com.pi.vip4.model.Pedido.StatusPedido;
import com.pi.vip4.service.ClienteService;
import com.pi.vip4.service.EnderecoEntregaService;
import com.pi.vip4.service.PedidoService;
import com.pi.vip4.service.CarrinhoService;

import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private EnderecoEntregaService enderecoEntregaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CarrinhoService carrinhoService;

    @GetMapping("/novo")
    public String mostrarFormularioNovoPedido(Model model) {
        model.addAttribute("pedido", new Pedido());
        return "formPedido";
    }

    @PostMapping("/salvar")
    public String salvarPedido(@ModelAttribute Pedido pedido, Model model) {
        try {
            Pedido salvo = pedidoService.salvar(pedido);
            model.addAttribute("pedido", salvo);
            return "confirmacaoPedido";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao finalizar pedido: " + e.getMessage());
            return "formPedido";
        }
    }

    @GetMapping
    public String listarPedidos(Model model, HttpSession session) {
        Long clienteId = (Long) session.getAttribute("clienteId");
        if (clienteId == null) {
            return "redirect:/login-cliente";
        }

        List<Pedido> pedidosDoCliente = pedidoService.listarPorClienteId(clienteId);
        model.addAttribute("pedidos", pedidosDoCliente);
        return "listaPedidos";
    }

    @GetMapping("/{id}")
    public String detalhePedido(@PathVariable Long id, Model model) {
        model.addAttribute("pedido", pedidoService.buscarPorId(id));
        return "detalhePedido";
    }

    @GetMapping("/todos")
public String listarTodosPedidos(Model model) {
    List<Pedido> todosPedidos = pedidoService.listarTodos();
    model.addAttribute("pedidos", todosPedidos);
    model.addAttribute("statusList", StatusPedido.values());
    return "listaTodosPedidos"; 
}

@PostMapping("/{id}/status")
@ResponseBody
public String alterarStatusPedido(@PathVariable Long id, @RequestParam String novoStatus) {
    try {
        pedidoService.alterarStatus(id, novoStatus);
        return "sucesso";
    } catch (Exception e) {
        e.printStackTrace();
        return "erro: " + e.getMessage();
    }
}


    @PostMapping("/api/salvar")
    @ResponseBody
    public String salvarPedidoViaApi(@RequestBody Pedido pedido, HttpSession session) {
        try {
            Long clienteId = (Long) session.getAttribute("clienteId");
            if (clienteId == null)
                return "erro";

            Cliente cliente = clienteService.buscarClientePorId(clienteId);
            EnderecoEntrega endereco = enderecoEntregaService.buscarPorId(pedido.getEnderecoEntrega().getId());
            List<Carrinho> carrinhoItens = carrinhoService.getItensPorCliente(clienteId);

            BigDecimal subtotal = carrinhoItens.stream()
                    .map(Carrinho::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            double frete = carrinhoItens.isEmpty() ? 0.0 : carrinhoItens.get(0).getFrete();
            BigDecimal total = subtotal.add(BigDecimal.valueOf(frete));

            pedido.setCliente(cliente);
            pedido.setEnderecoEntrega(endereco);
            pedido.setValorTotal(total);

            List<ItemPedido> itensPedido = carrinhoItens.stream().map(c -> {
                ItemPedido item = new ItemPedido();
                item.setPedido(pedido);
                item.setProduto(c.getProduto());
                item.setQuantidade(c.getQuantidade());
                item.setTotal(c.getTotal());
                return item;
            }).toList();

            pedido.setItens(itensPedido);

            Pedido salvo = pedidoService.salvar(pedido);

            return "/pedidos/" + salvo.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return "erro";
        }
    }
}
