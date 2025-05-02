function abrirModalEditarCliente() {
  const container = document.getElementById("modalEditarClienteContainer");
  const content = document.getElementById("modalEditarClienteContent");

  if (!container || !content) {
    console.error("Container ou content do modal de edição não encontrado no DOM.");
    return;
  }

  fetch('/modal-editar-cliente')
    .then(response => response.text())
    .then(html => {
      content.innerHTML = html;
      container.classList.remove("hidden");
    })
    .catch(error => {
      console.error("Erro ao carregar o modal de edição:", error);
    });
}
