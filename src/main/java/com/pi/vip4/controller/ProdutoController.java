package com.pi.vip4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pi.vip4.model.Produto;
import com.pi.vip4.repository.ProdutoRepository;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

  @Autowired
  private ProdutoRepository produtoRepository;

  // Exibe a lista de produtos
  @GetMapping
  public String getAllProdutos(Model model) {
    List<Produto> produtos = produtoRepository.findAll();
    model.addAttribute("produtos", produtos);
    return "produto-list"; // Nome do arquivo HTML em src/main/resources/templates/
  }

  // Exibe formulário para adicionar novo produto
  @GetMapping("/new")
  public String showCreateProdutoForm(Model model) {
    model.addAttribute("produto", new Produto()); // Cria um novo produto vazio
    return "create-produto-form"; // Formulário para criar produto
  }

  // Salva um novo produto, incluindo a imagem
  @PostMapping("/save")
  public String createProduto(@Valid @ModelAttribute Produto produto, RedirectAttributes redirectAttributes) {
    produtoRepository.save(produto);

    redirectAttributes.addFlashAttribute("message", "Produto criado com sucesso!");
    return "redirect:/produtos"; // Redireciona para a lista
  }

  // Exibe formulário para editar um produto existente
  @GetMapping("/edit/{id}")
  public String showUpdateProdutoForm(@PathVariable("id") Long produtoId, Model model) throws Exception {
    Produto produto = produtoRepository.findById(produtoId)
        .orElseThrow(() -> new Exception("Produto não encontrado: " + produtoId));
    model.addAttribute("produto", produto);
    return "edit-produto-form"; // Formulário para editar produto
  }

  // Atualiza um produto existente, incluindo a imagem
  @PostMapping("/update/{id}")
  public String updateProduto(@PathVariable("id") Long produtoId, @Valid @ModelAttribute Produto produtoDetails,
      RedirectAttributes redirectAttributes) throws Exception {
    Produto produto = produtoRepository.findById(produtoId)
        .orElseThrow(() -> new Exception("Produto não encontrado: " + produtoId));

    // Atualiza os dados do produto
    produto.setNomeProduto(produtoDetails.getNomeProduto());
    produto.setPreco(produtoDetails.getPreco());
    produto.setEstoque(produtoDetails.getEstoque());
    produto.setDescricao(produtoDetails.getDescricao());
    produto.setAvaliacao(produtoDetails.getAvaliacao());

    produtoRepository.save(produto);

    redirectAttributes.addFlashAttribute("message", "Produto atualizado com sucesso!");
    return "redirect:/produtos"; // Redireciona para a lista
  }

  // Deleta um produto
  @GetMapping("/delete/{id}")
  public String deleteProduto(@PathVariable("id") Long produtoId, RedirectAttributes redirectAttributes)
      throws Exception {
    Produto produto = produtoRepository.findById(produtoId)
        .orElseThrow(() -> new Exception("Produto não encontrado: " + produtoId));

    produtoRepository.delete(produto);
    redirectAttributes.addFlashAttribute("message", "Produto deletado com sucesso!");
    return "redirect:/produtos"; // Redireciona para a lista
  }

  // Alterna o status de um produto (Ativar/Desativar)
  @GetMapping("/toggle-status/{id}")
  public String toggleProdutoStatus(@PathVariable("id") Long produtoId, RedirectAttributes redirectAttributes)
      throws Exception {
    Produto produto = produtoRepository.findById(produtoId)
        .orElseThrow(() -> new Exception("Produto não encontrado: " + produtoId));

    // Inverte o status do produto (se for "ativo", vira "inativo", e vice-versa)
    produto.setStatus(!produto.isStatus());
    produtoRepository.save(produto);

    redirectAttributes.addFlashAttribute("message", "Status do produto alterado com sucesso!");
    return "redirect:/produtos"; // Redireciona para a lista de produtos
  }
}
