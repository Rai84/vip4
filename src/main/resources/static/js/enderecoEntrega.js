// enderecoEntrega.js

// 🔵 Abre o modal de listagem de endereços via fetch
function abrirModalEnderecoEntrega() {
  console.log("📦 Abrindo modal de endereços...");
  fetch('/cliente/enderecos-entrega/modal')
    .then(response => response.text())
    .then(html => {
      document.getElementById("modalEnderecoEntrega")?.remove();

      const temp = document.createElement("div");
      temp.innerHTML = html.trim();

      const modal = temp.querySelector("#modalEnderecoEntrega");
      if (!modal) {
        console.error("❌ Fragmento não contém #modalEnderecoEntrega");
        return;
      }

      document.body.appendChild(modal);
      modal.classList.remove("hidden");
      console.log("✅ Modal de endereços exibido.");
    })
    .catch(err => console.error("❌ Erro ao carregar modal de endereços:", err));
}

// 🟢 Abre o modal para adicionar novo endereço
function abrirModalFormEnderecoEntrega() {
  console.log("➕ Abrindo modal de novo endereço...");
  fetch('/cliente/enderecos-entrega/modal-form')
    .then(response => response.text())
    .then(html => {
      document.getElementById("modalFormEnderecoEntrega")?.remove();

      const temp = document.createElement("div");
      temp.innerHTML = html.trim();

      const modal = temp.querySelector("#modalFormEnderecoEntrega");
      if (!modal) {
        console.error("❌ Fragmento não contém #modalFormEnderecoEntrega");
        return;
      }

      document.body.appendChild(modal);
      modal.classList.remove("hidden");
      console.log("✅ Modal de novo endereço exibido.");

      const fechar = document.getElementById("fecharModalFormEnderecoEntrega");
      if (fechar) {
        fechar.addEventListener("click", () => {
          modal.classList.add("hidden");
        });
      }
    })
    .catch(err => console.error("❌ Erro ao carregar modal de novo endereço:", err));
}

// 🟡 Abre o modal com os dados para editar um endereço específico
function abrirModalEdicaoEndereco(id) {
  console.log("✏️ Abrindo modal de edição para endereço ID:", id);
  fetch(`/cliente/enderecos-entrega/modal-edit/${id}`)
    .then(response => response.text())
    .then(html => {
      document.getElementById("modalFormEnderecoEntrega")?.remove();

      const tempDiv = document.createElement("div");
      tempDiv.innerHTML = html.trim();

      const novoModal = tempDiv.querySelector("#modalFormEnderecoEntrega");
      if (!novoModal) {
        console.error("❌ ModalFormEnderecoEntrega não encontrado no HTML carregado.");
        return;
      }

      document.body.appendChild(novoModal);
      novoModal.classList.remove("hidden");
      console.log("✅ Modal de edição carregado para ID:", id);

      const fechar = document.getElementById("fecharModalFormEnderecoEntrega");
      if (fechar) {
        fechar.addEventListener("click", () => {
          novoModal.classList.add("hidden");
        });
      }
    })
    .catch(err => console.error("❌ Erro ao carregar modal de edição:", err));
}

// ❌ Fecha qualquer modal por ID
function fecharModalById(id) {
  const modal = document.getElementById(id);
  if (modal) {
    modal.classList.add("hidden");
    console.log(`❎ Modal ${id} fechado.`);
  } else {
    console.warn(`⚠️ Modal ${id} não encontrado para fechar.`);
  }
}
