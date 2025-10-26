document.addEventListener('DOMContentLoaded', () => {

    // testo in maiuscolo per codice
    function attivaUppercase(input) {
        input.addEventListener("input", function() {
            this.value = this.value.toUpperCase();
        });
    }

    // testo in maiuscolo per il primo codice
    const testoCodice = document.getElementById('idCodice');
    if (testoCodice) {
        attivaUppercase(testoCodice);
    }

    let parametroIndex = 1; // partiamo sempre da 1

    document.getElementById("addParametroBtn").addEventListener('click', function() {
        const container = document.getElementById('parametroContainer');
        const firstRow = container.querySelector('.parametroRow');

        // cloniamo la riga
        const newRow = firstRow.cloneNode(true);

        // aggiorniamo input con name nuovi e svuotiamo i valori
        newRow.querySelectorAll('input').forEach(input => {
            const baseName = input.getAttribute('name');
            if (baseName) {
                const newName = baseName.replace(/\[\d+\]/, `[${parametroIndex}]`);
                input.setAttribute('name', newName);
            }

            // svuotiamo valori
            if(input.type === 'text') input.value = '';
            if(input.type === 'checkbox') input.checked = false;

            //Attivo maiscolo per il codice
             if (input.id && input.id.includes('idCodice')) {
                attivaUppercase(input);
             }
        });

        // aggiungiamo la nuova riga al container
        container.appendChild(newRow);

        parametroIndex++;
    });
});