async function confirmarPedido() {
  const enderecoId = document.getElementById("enderecoSelect").value;
  const formaPagamento = document.getElementById("pagamentoSelect").value;

  const body = {
    enderecoEntrega: { id: enderecoId },
    formaPagamento,
    numeroCartao: document.getElementById("numeroCartao")?.value || null,
    nomeCartao: document.getElementById("nomeCartao")?.value || null,
    codigoSeguranca: document.getElementById("codigoSeguranca")?.value || null,
    dataValidade: document.getElementById("dataValidade")?.value || null,
    parcelas: document.getElementById("parcelas")?.value || null
  };

  const response = await fetch("/pedidos/api/salvar", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body)
  });

  const result = await response.text();

  if (response.ok && result !== "erro") {
    window.location.href = result; // redireciona para /pedidos/{id}
  } else {
    alert("Erro ao finalizar pedido.");
  }
}
