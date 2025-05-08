package com.pi.vip4.service;

import com.pi.vip4.model.ImgProduto;
import com.pi.vip4.model.Produto;
import com.pi.vip4.repository.ImgProdutoRepository;
import com.pi.vip4.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ImgProdutoRepository imgProdutoRepository;

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + id));
    }

    public Page<Produto> listarProdutosPaginados(Pageable pageable) {
        return produtoRepository.buscarTodosComImagens(pageable);
    }

    private String salvarImagem(MultipartFile imagem, Long idProduto) throws IOException {
        String diretorio = "src/main/resources/static/uploads/";
        File pasta = new File(diretorio);
        if (!pasta.exists())
            pasta.mkdirs();

        File[] arquivos = pasta.listFiles((dir, name) -> name.startsWith(String.format("%03d", idProduto)));
        int numeroImagem = (arquivos != null) ? arquivos.length + 1 : 1;
        String nomeArquivo = String.format("%03d_%03d.jpg", idProduto, numeroImagem);
        Path caminho = Paths.get(diretorio + nomeArquivo);

        Files.copy(imagem.getInputStream(), caminho, StandardCopyOption.REPLACE_EXISTING);
        return "/uploads/" + nomeArquivo;
    }

    @Transactional
    public String salvarNovoProduto(@Valid Produto produto, MultipartFile imagem,
            RedirectAttributes redirectAttributes) {
        try {
            produtoRepository.save(produto);
            if (!imagem.isEmpty()) {
                String imageUrl = salvarImagem(imagem, produto.getId());
                ImgProduto img = new ImgProduto(produto, imageUrl);
                imgProdutoRepository.save(img);
            }
            redirectAttributes.addFlashAttribute("message", "Produto criado com sucesso!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao salvar a imagem.");
        }
        return "redirect:/produtos";
    }

    @Transactional
    public String atualizarProduto(Long id, Produto details, MultipartFile imagem,
            RedirectAttributes redirectAttributes) throws Exception {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new Exception("Produto não encontrado: " + id));

        produto.setNomeProduto(details.getNomeProduto());
        produto.setPreco(details.getPreco());
        produto.setEstoque(details.getEstoque());
        produto.setDescricao(details.getDescricao());
        produto.setAvaliacao(details.getAvaliacao());
        produto.setStatus(details.getEstoque() > 0 && details.isStatus());

        if (imagem != null && !imagem.isEmpty()) {
            String imageUrl = salvarImagem(imagem, produto.getId());
            ImgProduto img = new ImgProduto(produto, imageUrl);
            imgProdutoRepository.save(img);
        }

        produtoRepository.save(produto);
        redirectAttributes.addFlashAttribute("message", "Produto atualizado com sucesso!");
        return "redirect:/produtos";
    }

    @Transactional
    public String excluirProduto(Long produtoId, RedirectAttributes redirectAttributes) throws Exception {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new Exception("Produto não encontrado: " + produtoId));

        List<ImgProduto> imagens = imgProdutoRepository.findByProduto(produto);
        for (ImgProduto img : imagens) {
            File file = new File("src/main/resources/static" + img.getImagemUrl());
            if (file.exists())
                file.delete();
            imgProdutoRepository.delete(img);
        }

        produtoRepository.delete(produto);
        redirectAttributes.addFlashAttribute("message", "Produto e imagens deletados com sucesso!");
        return "redirect:/produtos";
    }

    @Transactional
    public String excluirImagem(Long imgId, RedirectAttributes redirectAttributes) {
        ImgProduto img = imgProdutoRepository.findById(imgId).orElse(null);
        if (img != null) {
            File file = new File("src/main/resources/static" + img.getImagemUrl());
            if (file.exists())
                file.delete();
            imgProdutoRepository.delete(img);
            redirectAttributes.addFlashAttribute("message", "Imagem removida com sucesso!");
            return "redirect:/produtos/edit/" + img.getProduto().getId();
        } else {
            redirectAttributes.addFlashAttribute("error", "Imagem não encontrada!");
            return "redirect:/produtos";
        }
    }

    @Transactional
    public String alternarStatusProduto(Long produtoId, RedirectAttributes redirectAttributes) throws Exception {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new Exception("Produto não encontrado: " + produtoId));
        produto.setStatus(!produto.isStatus());
        produtoRepository.save(produto);
        redirectAttributes.addFlashAttribute("message", "Status do produto alterado com sucesso!");
        return "redirect:/produtos";
    }
}
