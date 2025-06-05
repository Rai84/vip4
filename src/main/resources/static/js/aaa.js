function irParaResumo() {
  // Troca de etapas
  document.getElementById('etapa1').classList.add('hidden');
  document.getElementById('etapa2').classList.remove('hidden');

  // Captura o texto do endereço selecionado
  const enderecoSelect = document.getElementById('enderecoSelect');
  const enderecoTexto = enderecoSelect.options[enderecoSelect.selectedIndex]?.text || 'Não informado';
  document.getElementById('resumoEndereco').innerText = enderecoTexto;

  // Captura a forma de pagamento selecionada
  const pagamentoSelect = document.getElementById('pagamentoSelect');
  const pagamentoTexto = pagamentoSelect.options[pagamentoSelect.selectedIndex]?.text || 'Não informado';
  document.getElementById('resumoPagamento').innerText = pagamentoTexto;
}

function voltarParaEtapa1() {
  document.getElementById('etapa2').classList.add('hidden');
  document.getElementById('etapa1').classList.remove('hidden');
}

function mostrarCamposCartao(valor) {
  const campos = document.getElementById('cartaoCampos');
  if (valor === 'CARTAO') {
    campos.classList.remove('hidden');
  } else {
    campos.classList.add('hidden');
  }
}

function fecharCheckoutModal() {
  document.getElementById('checkoutModal').classList.add('hidden');
  voltarParaEtapa1(); // Sempre volta para etapa 1 ao fechar
}
