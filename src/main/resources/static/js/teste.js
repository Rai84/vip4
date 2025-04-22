function abrirModalEditarCliente() {
  const container = document.getElementById("modalEditarClienteContainer");
  const content = document.getElementById("modalEditarClienteContent");

  fetch('/modal-editar-cliente')
    .then(response => response.text())
    .then(html => {
      content.innerHTML += html;
      container.classList.remove("hidden");
    })
    .catch(error => {
      console.error("Erro ao carregar o modal de edição:", error);
    });
}

function fecharModalEditarCliente() {
  const container = document.getElementById("modalEditarClienteContainer");
  const content = document.getElementById("modalEditarClienteContent");

  content.innerHTML = '<button onclick="fecharModalEditarCliente()" class="absolute top-2 right-2 text-2xl text-gray-400 hover:text-red-600">&times;</button>';
  container.classList.add("hidden");
}
