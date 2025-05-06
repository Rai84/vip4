package com.pi.vip4.controller;

import com.pi.vip4.model.ImgProduto;
import com.pi.vip4.model.Produto;
import com.pi.vip4.repository.ImgProdutoRepository;
import com.pi.vip4.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.List;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

  @Autowired
  private ProdutoService produtoService;

  @Autowired
  private ImgProdutoRepository imgProdutoRepository;

  // ✅ Lista os produtos com imagens (já resolvido com EntityGraph no service)
  @GetMapping
  public String getAllProdutos(Model model, @RequestParam(defaultValue = "0") int page) {
    Page<Produto> produtosPage = produtoService.listarProdutosPaginados(PageRequest.of(page, 10));
    model.addAttribute("produtos", produtosPage.getContent());
    model.addAttribute("produtosPage", produtosPage); // necessário para paginação
    return "list-produto";
  }

  @GetMapping("/{idProduto}")
  public String getProduto(@PathVariable Long idProduto, Model model) {
    Produto produto = produtoService.buscarPorId(idProduto);
    model.addAttribute("produto", produto);
    model.addAttribute("imagens", produto.getImagens()); // útil para mostrar várias imagens
    return "produto-detalhes";
  }

  @GetMapping("/new")
  public String showCreateProdutoForm(Model model) {
    model.addAttribute("produto", new Produto());
    return "create-produto-form";
  }

  @PostMapping("/save")
  public String createProduto(@Valid @ModelAttribute Produto produto,
      @RequestParam("imagem") MultipartFile imagem,
      RedirectAttributes redirectAttributes) {
    return produtoService.salvarNovoProduto(produto, imagem, redirectAttributes);
  }

  @GetMapping("/edit/{id}")
  public String showUpdateProdutoForm(@PathVariable("id") Long produtoId, Model model) throws Exception {
    Produto produto = produtoService.buscarPorId(produtoId);
    model.addAttribute("produto", produto);
    model.addAttribute("imagens", produto.getImagens()); // útil para exibir as imagens no form de edição
    return "edit-produto-form";
  }

  @PostMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public String updateProduto(@PathVariable("id") Long produtoId,
      @Valid @ModelAttribute Produto produtoDetails,
      @RequestParam(value = "imagem", required = false) MultipartFile imagem,
      RedirectAttributes redirectAttributes) throws Exception {
    return produtoService.atualizarProduto(produtoId, produtoDetails, imagem, redirectAttributes);
  }

  @GetMapping("/delete/{id}")
  public String deleteProduto(@PathVariable("id") Long produtoId, RedirectAttributes redirectAttributes)
      throws Exception {
    return produtoService.excluirProduto(produtoId, redirectAttributes);
  }

  @PostMapping("/delete-image/{id}")
  public String deleteImage(@PathVariable("id") Long imgId, RedirectAttributes redirectAttributes) {
    ImgProduto img = imgProdutoRepository.findById(imgId).orElse(null);

    if (img != null) {
      Long produtoId = img.getProduto().getIdProduto(); // pega o id antes de deletar a imagem
      File file = new File("src/main/resources/static" + img.getImagemUrl());
      if (file.exists())
        file.delete();
      imgProdutoRepository.delete(img);
      redirectAttributes.addFlashAttribute("message", "Imagem removida com sucesso!");
      return "redirect:/produtos/edit/" + produtoId;
    } else {
      redirectAttributes.addFlashAttribute("error", "Imagem não encontrada!");
      return "redirect:/produtos";
    }
  }

  @GetMapping("/toggle-status/{id}")
  public String toggleProdutoStatus(@PathVariable("id") Long produtoId, RedirectAttributes redirectAttributes)
      throws Exception {
    return produtoService.alternarStatusProduto(produtoId, redirectAttributes);
  }

  @GetMapping("/produtos/teste-rota")
  @ResponseBody
  public String test() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return "Usuário: " + auth.getName() + " | Roles: " + auth.getAuthorities();
  }
}
