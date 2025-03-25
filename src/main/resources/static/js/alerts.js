function removerImagem(imgId) {
    if (confirm("Tem certeza que deseja remover esta imagem?")) {
        fetch(`/produtos/delete-image/${imgId}`, { method: "POST", })
        .then(response => {
            if (response.ok) {
                alert("Imagem removida com sucesso!");
                location.reload(); // Atualiza a página para refletir a exclusão
            } else {
                alert("Erro ao remover a imagem.");
            }
        })
        .catch(error => console.error("Erro:", error));
    }
}


function confirmAction() {
    const resposta = confirm("Você tem certeza que deseja ativar/desativar este produto?");
    if (resposta) {
        return true; // Redireciona para o link
    } else 
        return false; // Cancela a ação
    
}