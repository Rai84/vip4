package com.pi.vip4.controller;

import com.pi.vip4.model.Cliente;
import com.pi.vip4.model.EnderecoEntrega;
import com.pi.vip4.repository.ClienteRepository;
import com.pi.vip4.repository.EnderecoEntregaRepository;
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
    private EnderecoEntregaRepository enderecoEntregaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/cliente/{clienteId}")
    public String listarEnderecos(@PathVariable Long clienteId, Model model) throws Exception {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new Exception("Cliente não encontrado: " + clienteId));

        List<EnderecoEntrega> enderecos = cliente.getEnderecosEntrega();
        model.addAttribute("enderecos", enderecos);
        model.addAttribute("clienteId", clienteId);
        return "endereco-entrega-list";
    }

    @GetMapping("/novo/{clienteId}")
    public String novoEndereco(@PathVariable Long clienteId, Model model) {
        EnderecoEntrega endereco = new EnderecoEntrega();
        endereco.setCliente(new Cliente());
        endereco.getCliente().setId(clienteId);

        model.addAttribute("enderecoEntrega", endereco);
        return "endereco-entrega-form";
    }

    @PostMapping("/salvar")
    public String salvarEndereco(@Valid @ModelAttribute EnderecoEntrega enderecoEntrega) {
        enderecoEntregaRepository.save(enderecoEntrega);
        return "redirect:/" + enderecoEntrega.getCliente().getId();
    }

    @GetMapping("/editar/{id}")
    public String editarEndereco(@PathVariable Long id, Model model) throws Exception {
        EnderecoEntrega endereco = enderecoEntregaRepository.findById(id)
                .orElseThrow(() -> new Exception("Endereço não encontrado: " + id));
        model.addAttribute("enderecoEntrega", endereco);
        return "endereco-entrega-form";
    }

    @GetMapping("/excluir/{id}")
    public String excluirEndereco(@PathVariable Long id) throws Exception {
        EnderecoEntrega endereco = enderecoEntregaRepository.findById(id)
                .orElseThrow(() -> new Exception("Endereço não encontrado: " + id));
        Long clienteId = endereco.getCliente().getId();
        enderecoEntregaRepository.delete(endereco);
        return "redirect:/enderecos-entrega/cliente/" + clienteId;
    }
}
