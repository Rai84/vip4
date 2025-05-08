document.addEventListener("DOMContentLoaded", () => {
  const cartButton = document.getElementById("cartButton");
  const modalContainer = document.getElementById("modalCarrinhoContainer");

  if (!cartButton || !modalContainer) return;

  cartButton.addEventListener("click", () => {
    fetch("/modal-carrinho")
      .then(response => {
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
        }
      });
  });
});

// ✅ Envia produto para o carrinho
async function enviarProdutoParaCarrinho(botao) {
  const produtoId = botao.getAttribute("data-id");
  const clienteId = localStorage.getItem("clienteId");
  const quantidade = 1;

  if (!clienteId) {
    abrirModalLogin();
    return;
  }

  try {
    const response = await fetch(`/api/carrinho/adicionar?clienteId=${clienteId}&produtoId=${produtoId}&quantidade=${quantidade}`, {
      method: "POST"
    });

    if (response.ok) {
      const carrinhoItem = await response.json();
      alert("Produto adicionado ao carrinho!");

      fetch("/modal-carrinho")
        .then(r => r.text())
        .then(html => {
          const modalContainer = document.getElementById("modalCarrinhoContainer");
          modalContainer.innerHTML = html;
          const cartModal = document.getElementById("cartModal");
          if (cartModal) {
            cartModal.classList.remove("hidden");
            cartModal.classList.add("flex");
          }
        });
    } else {
      const erro = await response.text();
      alert("Erro ao adicionar produto: " + erro);
    }
  } catch (error) {
    alert("Erro ao enviar produto.");
  }
}

// ✅ Remove um item do carrinho
async function removerItem(itemId) {
  try {
    const response = await fetch(`/api/carrinho/remover?id=${itemId}`, {
      method: "DELETE"
    });

    if (response.ok) {
      alert("Item removido do carrinho!");
      fetch("/modal-carrinho")
        .then(r => r.text())
        .then(html => {
          const modalContainer = document.getElementById("modalCarrinhoContainer");
          modalContainer.innerHTML = html;
          const cartModal = document.getElementById("cartModal");
          if (cartModal) {
            cartModal.classList.remove("hidden");
            cartModal.classList.add("flex");
          }
        });
    } else {
      const erro = await response.text();
      alert("Erro ao remover item: " + erro);
    }
  } catch (error) {
    alert("Erro na requisição para remover item.");
  }
}

// ✅ Altera quantidade de item
async function alterarQuantidade(id, novaQuantidade) {
  try {
    const response = await fetch(`/api/carrinho/atualizar?id=${id}&quantidade=${novaQuantidade}`, {
      method: "PATCH"
    });

    if (response.ok) {
      fetch("/modal-carrinho")
        .then(r => r.text())
        .then(html => {
          const modalContainer = document.getElementById("modalCarrinhoContainer");
          modalContainer.innerHTML = html;
          const cartModal = document.getElementById("cartModal");
          if (cartModal) {
            cartModal.classList.remove("hidden");
            cartModal.classList.add("flex");
          }
        });
    } else {
      const erro = await response.text();
      alert("Erro ao atualizar: " + erro);
    }
  } catch (e) {
    alert("Erro na requisição de quantidade.");
  }
}

// ✅ Seleciona frete e atualiza valor no banco
function selecionarFrete(valorFrete) {
  const clienteId = localStorage.getItem("clienteId");
  if (!clienteId) return;

  fetch(`/api/carrinho/frete?clienteId=${clienteId}&valor=${parseFloat(valorFrete)}`, {
    method: "PATCH"
  }).then(() => {
    fetch("/modal-carrinho")
      .then(r => r.text())
      .then(html => {
        const modalContainer = document.getElementById("modalCarrinhoContainer");
        modalContainer.innerHTML = html;
        const cartModal = document.getElementById("cartModal");
        if (cartModal) {
          cartModal.classList.remove("hidden");
          cartModal.classList.add("flex");
        }

        // ✅ Atualiza valor total no menu
        atualizarTotalMenu(clienteId);
      });
  });
}


function atualizarTotalMenu(clienteId) {
  const spanTotal = document.getElementById("totalCarrinhoModal");
  if (!clienteId || !spanTotal) return;

  fetch(`/api/carrinho/total?clienteId=${clienteId}`)
    .then(r => r.json())
    .then(total => {
      const formatado = total.toFixed(2).replace('.', ',');
      spanTotal.textContent = `R$ ${formatado}`;
    });
}



