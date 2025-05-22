function irParaResumo() {
    document.getElementById('etapa1').classList.add('hidden');
    document.getElementById('etapa2').classList.remove('hidden');
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
  