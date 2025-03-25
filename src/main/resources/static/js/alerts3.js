function confirmAction() {
    const resposta = confirm("Você tem certeza que deseja ativar/desativar este usuário?");
    if (resposta) {
        return true; // Redireciona para o link
    } else {
        return false; // Cancela a ação
    }
}