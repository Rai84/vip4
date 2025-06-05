document.addEventListener("DOMContentLoaded", () => {
  const cartButton = document.getElementById("cartButton");
  const modalContainer = document.getElementById("modalCarrinhoContainer");

  if (!cartButton || !modalContainer) return;

  // Evento para carregar o modal do carrinho
  cartButton.addEventListener("click", () => {
    fetch("/modal/modal3") // Corrigido para /modal/modal3
      .then((response) => {
        if (response.ok) {
          return response.text(); // Retorna o conteúdo HTML do fragmento
        } else {
          throw new Error("Erro ao carregar o modal carrinho");
        }
      })
      .then((html) => {
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
      })
      .catch((error) => {
        console.error("Erro ao carregar o modal:", error);
      });
  });
});

// Função para enviar produto para o carrinho
async function enviarProdutoParaCarrinho(botao) {
  const produtoId = botao.getAttribute("data-id");
  const clienteId = localStorage.getItem("clienteId"); // Verifica se o cliente está logado
  const quantidade = 1;

  console.log("Tentando adicionar produto ao carrinho...");
  console.log("Produto ID: " + produtoId);
  console.log("Cliente ID (do localStorage): " + clienteId);
  console.log("Quantidade: " + quantidade);

  if (!clienteId) {
    // Se o cliente não estiver logado, adicionar ao carrinho temporário na sessão
    let carrinhoTemp = JSON.parse(sessionStorage.getItem("carrinhoTemp")) || [];
    console.log("Carrinho temporário antes de adicionar: ", carrinhoTemp);

    // Verifica se o produto já existe no carrinho
    const produtoExistente = carrinhoTemp.find((item) => item.id === produtoId);
    if (produtoExistente) {
      produtoExistente.quantidade += quantidade; // Se já existir, atualiza a quantidade
      console.log("Produto existente no carrinho. Atualizando quantidade.");
    } else {
      carrinhoTemp.push({
        id: produtoId,
        nome: botao.getAttribute("data-nome"),
        descricao: botao.getAttribute("data-estoque"),
        preco: botao.getAttribute("data-preco"),
        imagem: botao.getAttribute("data-imagem"),
        quantidade: quantidade,
      });
      console.log("Produto adicionado ao carrinho.");
    }

    // Atualiza o carrinho na sessão
    sessionStorage.setItem("carrinhoTemp", JSON.stringify(carrinhoTemp));
    console.log("Carrinho temporário atualizado:", carrinhoTemp);

    alert("Produto adicionado ao carrinho!");
  } else {
    // Se o cliente estiver logado, envia o produto para o banco de dados
    try {
      const response = await fetch(
        `/api/carrinho/adicionar?clienteId=${clienteId}&produtoId=${produtoId}&quantidade=${quantidade}`,
        {
          method: "POST",
        }
      );

      console.log("Resposta da requisição:", response);

      if (response.ok) {
        alert("Produto adicionado ao carrinho!");

        // Atualiza o carrinho na interface
        fetch("/modal/modal3")
          .then((r) => r.text())
          .then((html) => {
            const modalContainer = document.getElementById(
              "modalCarrinhoContainer"
            );
            if (modalContainer) {
              modalContainer.innerHTML = html;

              const cartModal = document.getElementById("cartModal");
              if (cartModal) {
                cartModal.classList.remove("hidden");
                cartModal.classList.add("flex");
              }
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
}

// Função para fechar o modal
function closeModal() {
  const modalContainer = document.getElementById("modalContainer");
  if (modalContainer) {
    modalContainer.classList.add("hidden");
    modalContainer.innerHTML = ""; // Limpa o conteúdo
  }
}

// Função para abrir o modal de checkout
function abrirModalCheckout() {
  // Fecha o modal do carrinho
  const cartModal = document.getElementById("cartModal");
  if (cartModal) {
    cartModal.classList.add("hidden");
    cartModal.classList.remove("flex");
  }

  // Fecha o modal anterior e carrega o modal de checkout
  closeModal();

  fetch("/cliente/modal-checkout")
    .then((response) => response.text())
    .then((html) => {
      const container = document.getElementById("modalCheckoutContainer");
      container.innerHTML = html;

      setTimeout(() => {
        const modal = container.querySelector("#checkoutModal");
        if (modal) {
          modal.classList.remove("hidden");
          modal.classList.add("flex");
        } else {
          console.error("Modal #checkoutModal não encontrado.");
        }
      }, 0);
    })
    .catch((err) => {
      alert("Erro ao carregar o modal de checkout.");
      console.error(err);
    });
}

// Função para selecionar frete e atualizar o valor
function selecionarFrete(valorFrete) {
  const clienteId = localStorage.getItem("clienteId");
  if (!clienteId) return;

  fetch(
    `/api/carrinho/frete?clienteId=${clienteId}&valor=${parseFloat(
      valorFrete
    )}`,
    {
      method: "PATCH",
    }
  ).then(() => {
    fetch("/modal/modal3")
      .then((r) => r.text())
      .then((html) => {
        const modalContainer = document.getElementById(
          "modalCarrinhoContainer"
        );
        modalContainer.innerHTML = html;
        const cartModal = document.getElementById("cartModal");
        if (cartModal) {
          cartModal.classList.remove("hidden");
          cartModal.classList.add("flex");
        }

        // Atualiza o valor total no menu
        atualizarTotalMenu(clienteId);
      });
  });
}

// Função para atualizar o total no menu
function atualizarTotalMenu(clienteId) {
  const spanTotal = document.getElementById("totalCarrinhoModal");
  if (!clienteId || !spanTotal) return;

  fetch(`/api/carrinho/total?clienteId=${clienteId}`)
    .then((r) => r.json())
    .then((total) => {
      const formatado = total.toFixed(2).replace(".", ",");
      spanTotal.textContent = `R$ ${formatado}`;
    });
}
