package com.pi.vip4.service;

import com.pi.vip4.model.Carrinho;
import com.pi.vip4.model.Cliente;
import com.pi.vip4.model.Produto;
import com.pi.vip4.repository.CarrinhoRepository;
import com.pi.vip4.repository.ClienteRepository;
import com.pi.vip4.repository.ProdutoRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarrinhoService {

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public Carrinho adicionarAoCarrinho(Long clienteId, Long produtoId, int quantidade) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        // Verifica se já existe o item no carrinho
        Optional<Carrinho> itemExistente = carrinhoRepository.findByClienteIdAndProdutoId(clienteId, produtoId);

        Carrinho carrinho;
        if (itemExistente.isPresent()) {
            carrinho = itemExistente.get();
            carrinho.setQuantidade(carrinho.getQuantidade() + quantidade);
        } else {
            carrinho = new Carrinho();
            carrinho.setCliente(cliente);
            carrinho.setProduto(produto);
            carrinho.setQuantidade(quantidade);
        }

        return carrinhoRepository.save(carrinho);
    }

    public List<Carrinho> getItensPorCliente(Long clienteId) {
        return carrinhoRepository.findByClienteId(clienteId);
    }

    public void removerItem(Long id) {
        carrinhoRepository.deleteById(id);
    }

    public Carrinho atualizarQuantidade(Long id, int novaQuantidade) {
        Carrinho carrinho = carrinhoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item do carrinho não encontrado"));

        if (novaQuantidade <= 0) {
            carrinhoRepository.deleteById(id);
            return null;
        }

        carrinho.setQuantidade(novaQuantidade);
        return carrinhoRepository.save(carrinho);
    }

    public void atualizarFrete(Long clienteId, Double valorFrete) {
        List<Carrinho> itens = carrinhoRepository.findByClienteId(clienteId);
        if (itens.isEmpty())
            return;

        Carrinho primeiroItem = itens.get(0);

        // Atualiza mesmo que seja 0.0 — comparando com null ou diferente
        if (primeiroItem.getFrete() == null || !primeiroItem.getFrete().equals(valorFrete)) {
            primeiroItem.setFrete(valorFrete);
            carrinhoRepository.saveAndFlush(primeiroItem); // ✅ força flush
        }
        System.out.println("Frete atualizado para: " + valorFrete);

    }

    public double calcularTotalComFrete(Long clienteId) {
        List<Carrinho> itens = carrinhoRepository.findByClienteId(clienteId);

        double subtotal = itens.stream()
                .mapToDouble(item -> item.getTotal().doubleValue())
                .sum();

        double frete = itens.stream()
                .findFirst()
                .map(c -> c.getFrete() != null ? c.getFrete() : 0.0)
                .orElse(0.0);

        return subtotal + frete;
    }

}
