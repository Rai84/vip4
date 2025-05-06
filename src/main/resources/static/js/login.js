function abrirModalLogin() {
  const container = document.getElementById("modalLoginContainer");
  const content = document.getElementById("modalLoginContent");

  fetch("/modal-login-cliente")
  .then(res => res.text())
  .then(html => {
    const container = document.getElementById("modalClienteContainer");
    container.innerHTML = html;

    const modal = document.getElementById("modalLogin");
    if (modal) {
      modal.classList.remove("hidden");
      modal.classList.add("flex");
    }
  });

}

function fecharModalLogin() {
  const modal = document.getElementById("modalLogin");
  if (modal) {
    modal.classList.add("hidden");
    modal.classList.remove("flex");
  }
}

