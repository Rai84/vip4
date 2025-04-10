document.addEventListener("DOMContentLoaded", () => {
  const loginModal = document.getElementById("loginModal");
  const fecharLogin = document.getElementById("closeLoginModal");

  document.querySelectorAll(".abrir-login").forEach(elem => {
    elem.addEventListener("click", () => {
      loginModal.classList.remove("hidden");
      loginModal.classList.add("flex");
    });
  });

  if (fecharLogin) {
    fecharLogin.addEventListener("click", () => {
      loginModal.classList.remove("flex");
      loginModal.classList.add("hidden");
    });
  }

  if (loginModal) {
    loginModal.addEventListener("click", (e) => {
      if (e.target === loginModal) {
        loginModal.classList.remove("flex");
        loginModal.classList.add("hidden");
      }
    });
  }
});
