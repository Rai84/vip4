// Função que adiciona listener no campo CEP dentro do modal passado (modalElement)
function bindCepAutoFill(modalElement) {
  if (!modalElement) return;

  const cepInput = modalElement.querySelector('#cep');
  if (!cepInput) return;

  cepInput.addEventListener('blur', function() {
    const cep = this.value.replace(/\D/g, '');
    if (cep.length !== 8) {
      alert('CEP inválido! Deve conter 8 números.');
      return;
    }

    fetch(`https://viacep.com.br/ws/${cep}/json/`)
      .then(response => {
        if (!response.ok) throw new Error('Erro na consulta do CEP');
        return response.json();
      })
      .then(data => {
        if (data.erro) {
          alert('CEP não encontrado.');
          return;
        }

        modalElement.querySelector('#logradouro').value = data.logradouro || '';
        modalElement.querySelector('#bairro').value = data.bairro || '';
        modalElement.querySelector('#cidade').value = data.localidade || '';
        modalElement.querySelector('#uf').value = data.uf || '';
        modalElement.querySelector('#complemento').value = data.complemento || '';
      })
      .catch(() => alert('Não foi possível buscar o endereço pelo CEP.'));
  });
}

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

      bindCepAutoFill(modal); // Ativa o CEP no modal carregado
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

      bindCepAutoFill(novoModal); // Ativa o CEP no modal carregado
    })
    .catch(err => console.error("❌ Erro ao carregar modal de edição:", err));
}

// Função única para fechar modais pelo id
function fecharModal(idModal) {
  const modal = document.getElementById(idModal);
  if (modal) {
    modal.classList.add('hidden');
    console.log(`✅ Modal ${idModal} fechado.`);
  } else {
    console.log(`❌ Modal ${idModal} não encontrado.`);
  }
}

function fecharModalEnderecoEntrega() {
  fecharModal('modalEnderecoEntrega');
}

function fecharModalFormEnderecoEntrega() {
  fecharModal('modalFormEnderecoEntrega');
}