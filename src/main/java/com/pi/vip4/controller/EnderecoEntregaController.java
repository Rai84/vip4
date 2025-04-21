package com.pi.vip4.controller;

import com.pi.vip4.model.EnderecoEntrega;
import com.pi.vip4.service.EnderecoEntregaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/enderecos-entrega")
@Validated
public class EnderecoEntregaController {

    @Autowired
    private EnderecoEntregaService enderecoEntregaService;

    @GetMapping("/cliente/{clienteId}")
    public String listarEnderecos(@PathVariable Long clienteId, Model model) throws Exception {
        List<EnderecoEntrega> enderecos = enderecoEntregaService.listarEnderecosDoCliente(clienteId);
        model.addAttribute("enderecos", enderecos);
        model.addAttribute("clienteId", clienteId);
        return "list-endereco-entrega";
    }

    @GetMapping("/novo/{clienteId}")
    public String novoEndereco(@PathVariable Long clienteId, Model model) {
        EnderecoEntrega endereco = enderecoEntregaService.novoEnderecoParaCliente(clienteId);
        model.addAttribute("enderecoEntrega", endereco);
        return "create-endereco-entrega-form";
    }

    @PostMapping("/salvar")
    public String salvarEndereco(@Valid @ModelAttribute EnderecoEntrega enderecoEntrega) {
        Long clienteId = enderecoEntregaService.salvarEndereco(enderecoEntrega);
        return "redirect:/enderecos-entrega/cliente/" + clienteId;
    }

    @GetMapping("/editar/{id}")
    public String editarEndereco(@PathVariable Long id, Model model) throws Exception {
        EnderecoEntrega endereco = enderecoEntregaService.buscarEnderecoPorId(id);
        model.addAttribute("enderecoEntrega", endereco);
        return "create-endereco-entrega-form";
    }

    @GetMapping("/excluir/{id}")
    public String excluirEndereco(@PathVariable Long id) throws Exception {
        Long clienteId = enderecoEntregaService.excluirEndereco(id);
        return "redirect:/enderecos-entrega/cliente/" + clienteId;
    }
}
