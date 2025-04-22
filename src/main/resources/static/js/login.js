function abrirModalLogin() {
  const container = document.getElementById("modalLoginContainer");
  const content = document.getElementById("modalLoginContent");

  fetch('/modal-login')
    .then(response => response.text())
    .then(html => {
      content.innerHTML += html;
      container.classList.remove("hidden");
    })
    .catch(error => {
      console.error("Erro ao carregar o modal de login:", error);
    });
}

function fecharModalLogin() {
  const container = document.getElementById("modalLoginContainer");
  const content = document.getElementById("modalLoginContent");

  content.innerHTML = '<button onclick="fecharModalLogin()" class="absolute top-2 right-2 text-2xl text-gray-500 hover:text-red-600">&times;</button>';
  container.classList.add("hidden");
}
