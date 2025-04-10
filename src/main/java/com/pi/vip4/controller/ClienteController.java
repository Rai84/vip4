package com.pi.vip4.controller;

import com.pi.vip4.model.Cliente;
import com.pi.vip4.model.EnderecoEntrega;
import com.pi.vip4.model.EnderecoFaturamento;
import com.pi.vip4.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/clientes")
@Validated
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String getAllClientes(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Cliente> clientesPage = clienteRepository.findAll(pageable);

        model.addAttribute("clientes", clientesPage.getContent());
        model.addAttribute("clientesPage", clientesPage);

        return "cliente-list";
    }

    @GetMapping("/new")
    public String showCreateClienteForm(Model model) {
        Cliente cliente = new Cliente();
        cliente.setEnderecoFaturamento(new EnderecoFaturamento()); // necessário para preencher os campos aninhados
        model.addAttribute("cliente", cliente);
        return "create-cliente-form";
    }

    @PostMapping("/save")
    public String createCliente(@Valid @ModelAttribute Cliente cliente) {
        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        clienteRepository.save(cliente);
        return "redirect:/login-cliente";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateClienteForm(@PathVariable("id") Long clienteId, Model model) throws Exception {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new Exception("Cliente não encontrado: " + clienteId));
        if (cliente.getEnderecoFaturamento() == null) {
            cliente.setEnderecoFaturamento(new EnderecoFaturamento()); // evita erro na view
        }
        model.addAttribute("cliente", cliente);
        return "edit-cliente-form";
    }

    @PostMapping("/update/{id}")
    public String updateCliente(@PathVariable("id") Long clienteId, @Valid @ModelAttribute Cliente clienteDetails)
            throws Exception {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new Exception("Cliente não encontrado: " + clienteId));

        cliente.setNome(clienteDetails.getNome());
        cliente.setCpf(clienteDetails.getCpf());
        cliente.setEmail(clienteDetails.getEmail());
        cliente.setTelefone(clienteDetails.getTelefone());
        cliente.setGenero(clienteDetails.getGenero());
        cliente.setDataNascimento(clienteDetails.getDataNascimento());
        cliente.setEnderecoFaturamento(clienteDetails.getEnderecoFaturamento());

        // Evita recriptografar se a senha for igual
        if (!clienteDetails.getSenha().isBlank()) {
            cliente.setSenha(passwordEncoder.encode(clienteDetails.getSenha()));
        }

        clienteRepository.save(cliente);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteCliente(@PathVariable("id") Long clienteId) throws Exception {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new Exception("Cliente não encontrado: " + clienteId));
        clienteRepository.delete(cliente);
        return "redirect:/clientes";
    }
}
