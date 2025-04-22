document.addEventListener("DOMContentLoaded", function () {
  const botoesAbrir = document.querySelectorAll("#abrirModalCliente");
  const botaoFechar = document.getElementById("fecharModalCliente");
  const modal = document.getElementById("modalCliente");

  // Abre com qualquer um dos botões
  botoesAbrir.forEach(botao => {
    botao.addEventListener("click", () => {
      modal.classList.remove("hidden");
    });
  });

  // Fecha com o X
  if (botaoFechar) {
    botaoFechar.addEventListener("click", () => {
      modal.classList.add("hidden");
    });
  }

  // Fecha clicando fora do modal
  window.addEventListener("click", (event) => {
    if (event.target === modal) {
      modal.classList.add("hidden");
    }
  });
});
