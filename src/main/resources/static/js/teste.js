function abrirModalEditarCliente() {
  const container = document.getElementById("modalClienteContainer");
  
  if (!container) {
    console.error("❌ Container #modalClienteContainer não encontrado no DOM.");
    return;
  }

  fetch("/modal-editar-cliente")
    .then(res => res.text())
    .then(html => {
      container.innerHTML = html;

      const modal = document.getElementById("modalEditarCliente");
      const fechar = document.getElementById("fecharModalEditarCliente");

      if (!modal || !fechar) {
        console.error("❌ Modal ou botão de fechar não encontrados.");
        return;
      }

      modal.classList.remove("hidden");

      fechar.addEventListener("click", () => {
        modal.classList.add("hidden");
      });

      window.addEventListener("click", (e) => {
        if (e.target === modal) {
          modal.classList.add("hidden");
        }
      });
    });
}
