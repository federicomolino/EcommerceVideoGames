document.addEventListener('show.bs.modal', function (e) {

    const trigger = e.relatedTarget;
    if (!trigger) return;

    const bodyText = trigger.dataset.body;

    if (bodyText) {
        e.target.querySelector('.modal-body').innerText = bodyText;
    }

    // salva dati nel modal stesso (NON globale)
    const modal = e.target;
    modal.dataset.action = trigger.dataset.action;
    modal.dataset.id = trigger.dataset.id;
    modal.dataset.piattaforma = trigger.dataset.piattaforma;
    modal.dataset.lista = trigger.dataset.lista;
});

document.getElementById('confermaBtn')
.addEventListener('click', function () {

    const modal = document.getElementById('confermaModal');

    const action = modal.dataset.action;

    console.log("ACTION =", action);

    switch(action) {

        case 'eliminaBatch':
        case 'removePreferiti': {
            modal.querySelector('form')?.submit();
            break;
        }

        case 'addCarrello': {
            aggiungiAlCarrello(
                modal.dataset.id,
                modal.dataset.piattaforma,
                modal.dataset.lista
            );
            break;
        }

        default:
            console.log("Azione non gestita:", action);
    }
});