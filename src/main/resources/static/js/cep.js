document.addEventListener('DOMContentLoaded', function() {
    const modal = document.getElementById('modalFormEnderecoEntrega');
  
    if (!modal) return; // Evita erro se o modal não existir na página
  
    const cepInput = modal.querySelector('#cep');
    const logradouro = modal.querySelector('#logradouro');
    const bairro = modal.querySelector('#bairro');
    const cidade = modal.querySelector('#cidade');
    const uf = modal.querySelector('#uf');
    const complemento = modal.querySelector('#complemento');
  
    cepInput.addEventListener('blur', function() {
      const cep = this.value.replace(/\D/g, '');
      console.log('CEP digitado:', cep);
  
      if (cep.length !== 8) {
        alert('CEP inválido! Deve conter 8 números.');
        return;
      }
  
      fetch(`https://viacep.com.br/ws/${cep}/json/`)
        .then(response => {
          if (!response.ok) throw new Error('Erro na consulta do CEP');
          return response.json();
        })
        .then(data => {
          if (data.erro) {
            alert('CEP não encontrado.');
            return;
          }
          logradouro.value = data.logradouro || '';
          bairro.value = data.bairro || '';
          cidade.value = data.localidade || '';
          uf.value = data.uf || '';
          complemento.value = data.complemento || '';
        })
        .catch(err => {
          console.error('Erro na consulta do CEP:', err);
          alert('Não foi possível buscar o endereço pelo CEP.');
        });
    });
  });
  