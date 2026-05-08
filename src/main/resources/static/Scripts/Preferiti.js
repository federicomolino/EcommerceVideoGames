function showLoading() {
  document.getElementById('loadingOverlay').classList.remove('d-none');
}

function hideLoading() {
  document.getElementById('loadingOverlay').classList.add('d-none');
}

function delay(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

document.addEventListener('DOMContentLoaded', () => {
let datiPulsante = {};

// Intercetta quando si apre qualsiasi modal
document.addEventListener('show.bs.modal', function(e) {

 const trigger = e.relatedTarget;
  if (!trigger) return;

  if (e.target.id === 'confermaModal') {
    datiPulsante = {
      idGioco: e.relatedTarget.getAttribute('data-id'),
      piattaforma: e.relatedTarget.getAttribute('data-piattaforma'),
      idLista: e.relatedTarget.getAttribute('data-lista'),
      action: trigger.dataset.action
    };
  }

  // 👉 testo dinamico
  const bodyText = trigger.getAttribute('data-body');
  if (bodyText) {
    e.target.querySelector('.modal-body').innerText = bodyText;
  }
});

    document.addEventListener('click', async function(e) {
      const btn = e.target.closest('#confermaBtn');
      if (!btn) return;

      const { idGioco, piattaforma, idLista, action } = datiPulsante;

      let url = '';
      let method = 'POST';

      switch (action) {

        case 'addCarrello':
          url = `/api/v1/carrello/spostaNelCarrello/${idGioco}/${idLista}`;
          if (piattaforma) url += `?piattaforma=${piattaforma}`;
          break;

        case 'removePreferiti':
          <!--Se entra qui idGioco fa riferimento al singolo id della "lista_desideri_gioco" non del gioco -->
          url = `/api/v1/listaDesideri/rimuovi/${idGioco}`;
          method = 'DELETE';
          break;

        default:
          console.error("Azione non riconosciuta");
          return;
      }

      showLoading(); // 👈 parte subito
      const minTime = delay(3000); // 3 secondi

      try {
        const token = await getValidToken();

        const response = await fetch(url, {
          method,
          headers: {
            'Authorization': 'Bearer ' + token
          }
        });

        if (response.status === 401 || response.status === 403) {
          localStorage.removeItem(TOKEN_KEY);
          localStorage.removeItem(TOKEN_EXP_KEY);
        }

        const text = await response.text();

        if (!response.ok) throw new Error(text || 'Errore server');

        const toastEl = document.getElementById('toastCarrello');
        toastEl.querySelector('.toast-body').textContent = text;

        toastEl.classList.remove('bg-danger');
        toastEl.classList.add('bg-success');
        new bootstrap.Toast(toastEl).show();
        await minTime
        hideLoading();
        setTimeout(() => window.location.reload(), 500);

      } catch (error) {
        const toastEl = document.getElementById('toastCarrello');
        toastEl.querySelector('.toast-body').textContent = error.message;

        toastEl.classList.remove('bg-success');
        toastEl.classList.add('bg-danger');
        new bootstrap.Toast(toastEl).show();
      }
    });
});