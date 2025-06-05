async function confirmarPedido() {
  const enderecoId = document.getElementById("enderecoSelect").value;
  const formaPagamento = document.getElementById("pagamentoSelect").value;

  if (!enderecoId) {
    alert("Por favor, selecione um endereço de entrega.");
    return;
  }

  if (!formaPagamento) {
    alert("Por favor, selecione uma forma de pagamento.");
    return;
  }

  // Estrutura básica do corpo
  const body = {
    enderecoEntrega: { id: enderecoId },
    formaPagamento
  };

  // Se for pagamento com cartão, adiciona os campos
  if (formaPagamento === "CARTAO") {
    body.numeroCartao = document.getElementById("numeroCartao").value || null;
    body.nomeCartao = document.getElementById("nomeCartao").value || null;
    body.codigoSeguranca = document.getElementById("codigoSeguranca").value || null;
    body.dataValidade = document.getElementById("dataValidade").value || null;
    body.parcelas = document.getElementById("parcelas").value || null;

    // Validação mínima para dados do cartão
    if (!body.numeroCartao || !body.nomeCartao || !body.codigoSeguranca || !body.dataValidade) {
      alert("Por favor, preencha todos os dados do cartão.");
      return;
    }
  }

  try {
    const response = await fetch("/pedidos/api/salvar", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body)
    });

    const result = await response.text();

    if (response.ok && result !== "erro") {
      window.location.href = result; // redireciona para /pedidos/{id}
    } else {
      console.error("Erro na resposta da API:", result);
      alert("Erro ao finalizar pedido.");
    }
  } catch (error) {
    console.error("Erro ao enviar pedido:", error);
    alert("Erro ao processar o pedido. Tente novamente mais tarde.");
  }

}
