document.addEventListener("DOMContentLoaded", function() {
    const cartButton = document.getElementById("cartButton");
    const modalContainer = document.getElementById("modalCarrinhoContainer");

    if (!cartButton || !modalContainer) return;

    cartButton.addEventListener("click", function() {
        fetch("/modal-carrinho")
            .then(response => {
                if (response.redirected) {
                    // Foi redirecionado para o login-cliente
                    window.location.href = response.url;
                    return;
                }
                return response.text();
            })
            .then(html => {
                if (html) {
                    modalContainer.innerHTML = html;
                    const cartModal = document.getElementById("cartModal");
                    if (cartModal) {
                        cartModal.classList.remove("hidden");
                        cartModal.classList.add("flex");

                        // Reanexar evento do botão fechar após injetar HTML
                        const closeBtn = document.getElementById("closeModalBtn");
                        if (closeBtn) {
                            closeBtn.addEventListener("click", () => {
                                cartModal.classList.remove("flex");
                                cartModal.classList.add("hidden");
                            });
                        }
                    }
                }
            });
    });
});
