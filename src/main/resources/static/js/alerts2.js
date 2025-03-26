function confirmAction(event, url) {
    const resposta = confirm("Você tem certeza que deseja ativar/desativar este produto?");
    if (resposta) {
        window.location.href = url;  // Redireciona para o link após confirmação
        return true;
    } else {
        event.preventDefault();  // Cancela a ação se o usuário clicar em "Cancelar"
        return false;
    }
}
