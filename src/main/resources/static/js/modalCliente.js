document.addEventListener("DOMContentLoaded", function () {
  const botaoAbrir = document.getElementById("abrirModalCliente");
  const botaoFechar = document.getElementById("fecharModalCliente");
  const modal = document.getElementById("modalCliente");

  botaoAbrir.addEventListener("click", () => {
    modal.classList.remove("hidden");
  });

  botaoFechar.addEventListener("click", () => {
    modal.classList.add("hidden");
  });

  window.addEventListener("click", (event) => {
    if (event.target === modal) {
      modal.classList.add("hidden");
    }
  });
});
