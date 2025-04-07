package com.pi.vip4.controller;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import org.springframework.data.domain.Pageable;

import com.pi.vip4.model.ImgProduto;
import com.pi.vip4.model.Produto;
import com.pi.vip4.repository.ProdutoRepository;
import com.pi.vip4.repository.ImgProdutoRepository;
import com.pi.vip4.service.ProdutoService;

import javax.validation.Valid;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

  @Autowired
  private ProdutoRepository produtoRepository;

  @Autowired
  private ImgProdutoRepository imgProdutoRepository;

  @Autowired
  private ProdutoService produtoService;

   // Salva a imagem no diretório e retorna a URL
  private String salvarImagem(MultipartFile imagem, Long idProduto) throws IOException {
    String diretorio = "src/main/resources/static/uploads/";
    File pasta = new File(diretorio);
    if (!pasta.exists()) {
      pasta.mkdirs();
    }

    // Conta as imagens existentes para o produto e cria o nome do arquivo
    File[] arquivos = pasta.listFiles((dir, name) -> name.startsWith(String.format("%03d", idProduto)));
    int numeroImagem = (arquivos != null) ? arquivos.length + 1 : 1;
    String nomeArquivo = String.format("%03d", idProduto) + String.format("_%03d", numeroImagem) + ".jpg";
    Path caminho = Paths.get(diretorio + nomeArquivo);

    // Salva a imagem no diretório
    Files.copy(imagem.getInputStream(), caminho, StandardCopyOption.REPLACE_EXISTING);

    // Retorna o caminho acessível na aplicação
    return "/uploads/" + nomeArquivo;
  }

  @GetMapping
  public String getAllProdutos(Model model, @RequestParam(defaultValue = "0") int page) {
    Pageable pageable = PageRequest.of(page, 10); // 10 produtos por página
    Page<Produto> produtosPage = produtoRepository.findAll(pageable);

    model.addAttribute("produtos", produtosPage.getContent()); // Envia a lista de produtos
    model.addAttribute("produtosPage", produtosPage); // Para paginação

    return "produto-list";
  }

  @GetMapping("/{idProduto}")
  public String getProduto(@PathVariable Long idProduto, Model model) {
    Produto produto = produtoService.buscarPorId(idProduto); // Serviço que busca o produto
    model.addAttribute("produto", produto);
    return "produto-detalhes"; // Nome do template que exibe o produto
  }

  @GetMapping("/new") // Exibe formulário para adicionar novo produto
  public String showCreateProdutoForm(Model model) {
    model.addAttribute("produto", new Produto()); // Cria um novo produto vazio
    return "create-produto-form"; // Formulário para criar produto
  }

  @PostMapping("/save") // Salva um novo produto, incluindo a imagem
  public String createProduto(@Valid @ModelAttribute Produto produto,
      @RequestParam("imagem") MultipartFile imagem,
      RedirectAttributes redirectAttributes) {
    try {
      // Salva o produto no banco
      produtoRepository.save(produto);

      if (!imagem.isEmpty()) {
        // Salva a imagem e obtém a URL
        String imageUrl = salvarImagem(imagem, produto.getIdProduto());

        // Cria a entidade de imagem e associa ao produto
        ImgProduto imgProduto = new ImgProduto();
        imgProduto.setProduto(produto);
        imgProduto.setImagemUrl(imageUrl);

        // Salva a imagem no banco
        imgProdutoRepository.save(imgProduto);
      }

      // Mensagem de sucesso
      redirectAttributes.addFlashAttribute("message", "Produto criado com sucesso!");
    } catch (IOException e) {
      redirectAttributes.addFlashAttribute("error", "Erro ao salvar a imagem.");
    }

    return "redirect:/produtos";
  }

  @GetMapping("/edit/{id}") // Exibe formulário para editar um produto existente
  public String showUpdateProdutoForm(@PathVariable("id") Long produtoId, Model model) throws Exception {
    Produto produto = produtoRepository.findById(produtoId)
        .orElseThrow(() -> new Exception("Produto não encontrado: " + produtoId));
    model.addAttribute("produto", produto);
    return "edit-produto-form"; // Formulário para editar produto
    
  }

   // Atualiza um produto existente, incluindo a imagem
  @PostMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public String updateProduto(@PathVariable("id") Long produtoId,
      @Valid @ModelAttribute Produto produtoDetails,
      @RequestParam(value = "imagem", required = false) MultipartFile imagem,
      RedirectAttributes redirectAttributes) throws Exception {
    Produto produto = produtoRepository.findById(produtoId)
        .orElseThrow(() -> new Exception("Produto não encontrado: " + produtoId));

    // Atualiza os dados do produto
    produto.setNomeProduto(produtoDetails.getNomeProduto());
    produto.setPreco(produtoDetails.getPreco());
    produto.setEstoque(produtoDetails.getEstoque());
    produto.setDescricao(produtoDetails.getDescricao());
    produto.setAvaliacao(produtoDetails.getAvaliacao());

    // Se uma nova imagem for enviada, salva
    if (imagem != null && !imagem.isEmpty()) {
      String imageUrl = salvarImagem(imagem, produto.getIdProduto());
      ImgProduto imgProduto = new ImgProduto();
      imgProduto.setProduto(produto);
      imgProduto.setImagemUrl(imageUrl);
      imgProdutoRepository.save(imgProduto);
    }

    produtoRepository.save(produto);
    redirectAttributes.addFlashAttribute("message", "Produto atualizado com sucesso!");

    return "redirect:/produtos";
  }

  @GetMapping("/delete/{id}") // Deleta um produto
  public String deleteProduto(@PathVariable("id") Long produtoId, RedirectAttributes redirectAttributes)
      throws Exception {
    Produto produto = produtoRepository.findById(produtoId)
        .orElseThrow(() -> new Exception("Produto não encontrado: " + produtoId));

    // Exclui as imagens associadas ao produto
    List<ImgProduto> imagens = imgProdutoRepository.findByProduto(produto); // Aqui você pode buscar as imagens
                                                                            // associadas ao produto
    if (imagens != null && !imagens.isEmpty()) {
      for (ImgProduto imgProduto : imagens) {
        // Exclui o arquivo físico da imagem
        File imagemArquivo = new File("src/main/resources/static" + imgProduto.getImagemUrl());
        if (imagemArquivo.exists()) {
          imagemArquivo.delete(); // Exclui o arquivo
        }

        // Exclui o registro na tabela ImgProduto
        imgProdutoRepository.delete(imgProduto);
      }
    }

    // Exclui o produto
    produtoRepository.delete(produto);

    // Mensagem de sucesso
    redirectAttributes.addFlashAttribute("message", "Produto e suas imagens deletados com sucesso!");
    return "redirect:/produtos"; // Redireciona para a lista de produtos
  }

  @PostMapping("/delete-image/{id}") // Deleta uma imagem associada a um produto
  public String deleteImage(@PathVariable("id") Long imgId, RedirectAttributes redirectAttributes) {
    ImgProduto imgProduto = imgProdutoRepository.findById(imgId)
        .orElse(null);

    if (imgProduto != null) {
      // Exclui o arquivo físico da imagem
      File imagemArquivo = new File("src/main/resources/static" + imgProduto.getImagemUrl());
      if (imagemArquivo.exists()) {
        imagemArquivo.delete();
      }

      // Exclui o registro da imagem do banco de dados
      imgProdutoRepository.delete(imgProduto);

      redirectAttributes.addFlashAttribute("message", "Imagem removida com sucesso!");
    } else {
      redirectAttributes.addFlashAttribute("error", "Imagem não encontrada!");
    }

    return "redirect:/produtos/edit/" + imgProduto.getProduto().getIdProduto();
  }
  
  @GetMapping("/toggle-status/{id}") // Alterna o status de um produto (Ativar/Desativar)
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
