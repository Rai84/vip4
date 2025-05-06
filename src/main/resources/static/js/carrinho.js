let totalCarrinho = 0;
let freteSelecionado = 0;
const cartItems = [];

function addToCart(button) {
  const nomeProduto = button.getAttribute("data-nome");
  const precoProduto = parseFloat(button.getAttribute("data-preco").replace(",", "."));
  const imgUrl = button.getAttribute("data-imagem");

  const produtoExistente = cartItems.find(prod => prod.nome === nomeProduto);

  if (produtoExistente) {
    produtoExistente.quantidade++;
  } else {
    cartItems.push({ nome: nomeProduto, preco: precoProduto, imagem: imgUrl, quantidade: 1 });
  }

  totalCarrinho += precoProduto;
  atualizarCarrinho();
  atualizarTotalComFrete();
}

function atualizarCarrinho() {
  const cartContent = document.getElementById("cartContent");
  if (!cartContent) return;

  cartContent.innerHTML = "";

  cartItems.forEach(produto => {
    const html = `
      <div class="flex items-center space-x-4 mb-4">
        <img src="${produto.imagem}" alt="${produto.nome}" class="w-16 h-16 object-cover rounded">
        <div>
          <p class="text-sm font-semibold">${produto.nome}</p>
          <p class="text-gray-600">R$ ${produto.preco.toFixed(2)} x ${produto.quantidade} = <strong>R$ ${(produto.preco * produto.quantidade).toFixed(2)}</strong></p>
        </div>
        <div class="flex items-center space-x-2">
          <button onclick="aumentarQuantidade('${produto.nome}')" class="px-2 py-1 bg-green-500 text-white rounded">+</button>
          <button onclick="removerProduto('${produto.nome}')" class="px-2 py-1 bg-red-500 text-white rounded">-</button>
        </div>
      </div>
    `;
    cartContent.innerHTML += html;
  });
}

function aumentarQuantidade(nome) {
  const produto = cartItems.find(p => p.nome === nome);
  if (produto) {
    produto.quantidade++;
    totalCarrinho += produto.preco;
    atualizarCarrinho();
    atualizarTotalComFrete();
  }
}

function removerProduto(nome) {
  const produto = cartItems.find(p => p.nome === nome);
  if (produto) {
    produto.quantidade--;
    totalCarrinho -= produto.preco;
    if (produto.quantidade === 0) {
      const index = cartItems.indexOf(produto);
      cartItems.splice(index, 1);
    }
    atualizarCarrinho();
    atualizarTotalComFrete();
  }
}

function atualizarTotalComFrete() {
  const totalFinal = totalCarrinho + freteSelecionado;

  const totalValor = document.getElementById("totalValor");
  if (totalValor) totalValor.textContent = `Total: R$ ${totalFinal.toFixed(2)}`;

  const topo = document.getElementById("totalCarrinhoModal");
  if (topo) topo.textContent = `R$ ${totalFinal.toFixed(2)}`;
}

function selecionarFrete(valor) {
  freteSelecionado = parseFloat(valor);
  atualizarTotalComFrete();
}

document.addEventListener("DOMContentLoaded", () => {
  const cartButton = document.getElementById("cartButton");
  const modalContainer = document.getElementById("modalCarrinhoContainer");

  if (!cartButton || !modalContainer) return;

  cartButton.addEventListener("click", () => {
    fetch("/modal-carrinho")
      .then(response => {
        // Detecta se foi redirecionado para o login do cliente
        if (response.redirected && response.url.includes("/login-cliente")) {
          return fetch("/modal-login-cliente")
            .then(r => r.text())
            .then(html => {
              const loginContainer = document.getElementById("modalClienteContainer");
              if (loginContainer) {
                loginContainer.innerHTML = html;

                const loginModal = document.getElementById("modalLogin");
                if (loginModal) {
                  loginModal.classList.remove("hidden");
                  loginModal.classList.add("flex");
                }
              }
            });
        }

        return response.text();
      })
      .then(html => {
        if (!html) return;

        modalContainer.innerHTML = html;

        const cartModal = document.getElementById("cartModal");
        const closeBtn = document.getElementById("closeModalBtn");

        if (cartModal) {
          cartModal.classList.remove("hidden");
          cartModal.classList.add("flex");

          if (closeBtn) {
            closeBtn.addEventListener("click", () => {
              cartModal.classList.remove("flex");
              cartModal.classList.add("hidden");
            });
          }

          cartModal.addEventListener("click", (e) => {
            if (e.target === cartModal) {
              cartModal.classList.remove("flex");
              cartModal.classList.add("hidden");
            }
          });

          atualizarCarrinho();
          atualizarTotalComFrete();
        }
      });
  });
});
