package com.pi.vip4.controller;

import com.pi.vip4.model.Cliente;
import com.pi.vip4.model.EnderecoFaturamento;
import com.pi.vip4.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientes")
@Validated
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String getAllClientes(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Cliente> clientesPage = clienteService.getAllClientesPaginados(page);
        model.addAttribute("clientes", clientesPage.getContent());
        model.addAttribute("clientesPage", clientesPage);
        return "list-cliente";
    }

    @GetMapping("/new")
    public String showCreateClienteForm(Model model) {
        Cliente cliente = new Cliente();
        cliente.setEnderecoFaturamento(new EnderecoFaturamento());
        model.addAttribute("cliente", cliente);
        return "create-cliente-form";
    }

    @PostMapping("/save")
    public String createCliente(@Valid @ModelAttribute Cliente cliente) {
        clienteService.salvarNovoCliente(cliente);
        return "redirect:/login-cliente";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateClienteForm(@PathVariable("id") Long clienteId, Model model) throws Exception {
        Cliente cliente = clienteService.buscarClientePorId(clienteId);
        model.addAttribute("cliente", cliente);
        return "edit-cliente-form";
    }

    @PostMapping("/update/{id}")
    public String updateCliente(@PathVariable("id") Long clienteId, @Valid @ModelAttribute Cliente clienteDetails)
            throws Exception {
        clienteService.atualizarCliente(clienteId, clienteDetails);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteCliente(@PathVariable("id") Long clienteId) throws Exception {
        clienteService.excluirCliente(clienteId);
        return "redirect:/clientes";
    }
}
