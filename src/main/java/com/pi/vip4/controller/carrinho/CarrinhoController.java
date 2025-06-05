package com.pi.vip4.controller.carrinho;

import com.pi.vip4.model.Carrinho;
import com.pi.vip4.model.Produto;
import com.pi.vip4.service.CarrinhoService;
import com.pi.vip4.service.ClienteService;
import com.pi.vip4.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/carrinho")
public class CarrinhoController {

    @Autowired
    private CarrinhoService carrinhoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProdutoRepository produtoRepository;

    @PostMapping("/adicionar")
    public Carrinho adicionarAoCarrinho(@RequestParam Long produtoId,
            @RequestParam int quantidade,
            HttpSession session) {

        // Verifica se o cliente está autenticado
        Long clienteId = (Long) session.getAttribute("clienteId");

        if (clienteId == null) {
            // Se o cliente não estiver autenticado, adicionar ao carrinho temporário na
            // sessão
            System.out.println("Usuário não autenticado. Adicionando produto à sessão.");

            // Recuperar o carrinho temporário da sessão
            List<Carrinho> carrinhoTemp = (List<Carrinho>) session.getAttribute("carrinhoTemp");

            if (carrinhoTemp == null) {
                carrinhoTemp = new ArrayList<>();
            }

            Produto produto = produtoRepository.findById(produtoId)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            // Verifica se o produto já está no carrinho temporário
            boolean produtoExistente = false;
            for (Carrinho item : carrinhoTemp) {
                if (item.getProduto().getId().equals(produtoId)) {
                    item.setQuantidade(item.getQuantidade() + quantidade); // Atualiza a quantidade
                    produtoExistente = true;
                    break;
                }
            }

            if (!produtoExistente) {
                Carrinho novoItem = new Carrinho();
                novoItem.setProduto(produto);
                novoItem.setQuantidade(quantidade);
                carrinhoTemp.add(novoItem);
            }

            // Salva o carrinho temporário na sessão
            session.setAttribute("carrinhoTemp", carrinhoTemp);

            return new Carrinho(); // Retorna o novo item ou o carrinho atualizado
        } else {
            // Se o cliente estiver autenticado, adicionar ao banco de dados
            return carrinhoService.adicionarAoCarrinho(clienteId, produtoId, quantidade);
        }
    }

    @DeleteMapping("/remover")
    public void removerItem(@RequestParam Long id) {
        carrinhoService.removerItem(id);
    }

    @PatchMapping("/atualizar")
    public Carrinho atualizarQuantidade(
            @RequestParam Long id,
            @RequestParam int quantidade) {
        return carrinhoService.atualizarQuantidade(id, quantidade);
    }

    @PatchMapping("/frete")
    public void atualizarFrete(@RequestParam Long clienteId,
            @RequestParam(name = "valor", required = true) Double valor) {
        carrinhoService.atualizarFrete(clienteId, valor);
    }

    @GetMapping("/total")
    public Double getTotalCarrinho(@RequestParam(required = false) Long clienteId) {
        if (clienteId == null) {
            throw new IllegalArgumentException("clienteId não pode ser nulo");
        }
        return carrinhoService.calcularTotalComFrete(clienteId);
    }

}