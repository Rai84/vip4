document.addEventListener("DOMContentLoaded", function () {
  const botoesAbrir = document.querySelectorAll(".abrirModalCliente");

  botoesAbrir.forEach(botao => {
    botao.addEventListener("click", () => {
      fetch("/modal-cliente")
        .then(response => response.text())
        .then(html => {
          const container = document.getElementById("modalClienteContainer");
          container.innerHTML = html;

          const modal = document.getElementById("modalCliente");
          const botaoFechar = document.getElementById("fecharModalCliente");

          if (modal && botaoFechar) {
            modal.classList.remove("hidden");

            botaoFechar.addEventListener("click", () => {
              modal.classList.add("hidden");
            });

            window.addEventListener("click", (event) => {
              if (event.target === modal) {
                modal.classList.add("hidden");
              }
            });
          }
        });
    });
  });
});
