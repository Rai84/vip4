package com.pi.vip4.controller;

import com.pi.vip4.model.Cliente;
import com.pi.vip4.model.EnderecoEntrega;
import com.pi.vip4.service.ClienteService;
import com.pi.vip4.repository.ClienteRepository;
import com.pi.vip4.service.EnderecoEntregaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/enderecos-entrega")
public class EnderecoEntregaController {

    @Autowired
    private EnderecoEntregaService enderecoEntregaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteRepository clienteRepository;

    // Listagem por ID do cliente (usado no admin)
    @GetMapping("/cliente/{id}")
    public String listarEnderecosPorCliente(@PathVariable Long id, Model model) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        model.addAttribute("cliente", cliente);
        model.addAttribute("enderecos", enderecoEntregaService.listarPorCliente(cliente));
        return "list-endereco-entrega"; // Certifique-se de que esse HTML existe
    }

    // Exibe formulário de criação (usado no admin)
    @GetMapping("/new/{clienteId}")
    public String showCreateForm(@PathVariable Long clienteId, Model model) throws Exception {
        Cliente cliente = clienteService.buscarClientePorId(clienteId);
        EnderecoEntrega endereco = new EnderecoEntrega();
        endereco.setCliente(cliente);
        model.addAttribute("endereco", endereco);
        return "form-endereco-entrega"; // Certifique-se de que esse HTML existe
    }

    // Salva novo ou atualiza endereço (usado no admin e cliente)
    @PostMapping("/save")
    public String saveEndereco(@Valid @ModelAttribute EnderecoEntrega endereco) throws Exception {
        enderecoEntregaService.salvar(endereco);
        return "redirect:/enderecos-entrega/cliente/" + endereco.getCliente().getId();
    }

    // Exibe formulário de edição (para o admin)
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        EnderecoEntrega endereco = enderecoEntregaService.buscarPorId(id);
        model.addAttribute("endereco", endereco);
        return "form-endereco-entrega";
    }

    // Define como padrão (para o admin)
    @PostMapping("/padrao/{id}")
    public String definirPadrao(@PathVariable Long id) {
        EnderecoEntrega endereco = enderecoEntregaService.buscarPorId(id);
        enderecoEntregaService.definirComoPadrao(endereco);
        return "redirect:/enderecos-entrega/cliente/" + endereco.getCliente().getId();
    }

    // Remove endereço (para o admin)
    @GetMapping("/delete/{id}")
    public String removerEndereco(@PathVariable Long id) {
        EnderecoEntrega endereco = enderecoEntregaService.buscarPorId(id);
        Long clienteId = endereco.getCliente().getId();
        enderecoEntregaService.remover(id);
        return "redirect:/enderecos-entrega/cliente/" + clienteId;
    }
}
