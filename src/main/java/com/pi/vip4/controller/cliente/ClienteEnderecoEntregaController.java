package com.pi.vip4.controller.cliente;

import com.pi.vip4.model.Cliente;
import com.pi.vip4.model.EnderecoEntrega;
import com.pi.vip4.service.ClienteService;
import com.pi.vip4.service.EnderecoEntregaService;
import com.pi.vip4.service.details.cliente.CustomClienteDetails;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cliente/enderecos-entrega")
public class ClienteEnderecoEntregaController {

    @Autowired
    private EnderecoEntregaService enderecoEntregaService;

    @Autowired
    private ClienteService clienteService;

    private Cliente getClienteLogado() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomClienteDetails clienteDetails = (CustomClienteDetails) auth.getPrincipal();
        return clienteService.buscarClientePorId(clienteDetails.getId());
    }

    // Carrega os endereços do cliente e exibe no modal
    @GetMapping("/modal")
    public String mostrarModalEnderecos(Model model) throws Exception {
        Cliente cliente = getClienteLogado();
        model.addAttribute("cliente", cliente);
        model.addAttribute("enderecos", enderecoEntregaService.listarPorCliente(cliente));
        return "fragments/modalEnderecoEntrega :: modalEnderecoEntrega";
    }

    // Abre o modal de formulário para adicionar novo endereço
    @GetMapping("/modal-form")
    public String mostrarModalFormularioEndereco(Model model) throws Exception {
        Cliente cliente = getClienteLogado();
        EnderecoEntrega endereco = new EnderecoEntrega();
        endereco.setCliente(cliente);
        model.addAttribute("endereco", endereco);
        return "fragments/formEnderecoEntregaModal :: formEnderecoEntregaModal";
    }

    // Edita um endereço específico (carregando dados no modal)
    @GetMapping("/modal-edit/{id}")
    public String mostrarModalEdicao(@PathVariable Long id, Model model) throws Exception {
        EnderecoEntrega endereco = enderecoEntregaService.buscarPorId(id);
        Cliente cliente = getClienteLogado();
        if (endereco.getCliente().getId() != cliente.getId()) {
            throw new RuntimeException("Acesso negado");
        }
        model.addAttribute("endereco", endereco);
        return "fragments/formEnderecoEntregaModal :: formEnderecoEntregaModal";
    }

    // Salva endereço (novo ou edição)
    @PostMapping("/save")
    public String salvarEndereco(@Valid @ModelAttribute EnderecoEntrega endereco) throws Exception {
        Cliente cliente = getClienteLogado();
        endereco.setCliente(cliente);
        enderecoEntregaService.salvar(endereco);
        return "redirect:/";
    }

    // Define como endereço padrão
    @PostMapping("/padrao/{id}")
    public String definirEnderecoPadrao(@PathVariable Long id) throws Exception {
        EnderecoEntrega endereco = enderecoEntregaService.buscarPorId(id);
        Cliente cliente = getClienteLogado();
        if (endereco.getCliente().getId() != cliente.getId()) {
            throw new RuntimeException("Acesso negado");
        }
        enderecoEntregaService.definirComoPadrao(endereco);
        return "redirect:/";
    }

    // Remove um endereço
    @GetMapping("/delete/{id}")
    public String excluirEndereco(@PathVariable Long id) throws Exception {
        EnderecoEntrega endereco = enderecoEntregaService.buscarPorId(id);
        Cliente cliente = getClienteLogado();
        if (endereco.getCliente().getId() != cliente.getId()) {
            throw new RuntimeException("Acesso negado");
        }
        enderecoEntregaService.remover(id);
        return "redirect:/";
    }
}
