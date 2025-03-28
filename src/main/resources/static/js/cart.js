document.addEventListener("DOMContentLoaded", function() {
    const cartButton = document.getElementById("cartButton");
    const cartModal = document.getElementById("cartModal");
    const closeCart = document.getElementById("closeCart");

    if (!cartButton || !cartModal || !closeCart) {
        console.error("Erro: Elementos do carrinho não encontrados!");
        return;
    }

    cartButton.addEventListener("click", function() {
        console.log("Abrindo modal...");
        cartModal.classList.remove("hidden");
    });

    closeCart.addEventListener("click", function() {
        console.log("Fechando modal...");
        cartModal.classList.add("hidden");
    });
});
