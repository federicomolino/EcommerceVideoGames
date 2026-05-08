/*const TOKEN_KEY = "jwt_token";
const TOKEN_EXP_KEY = "jwt_exp";
async function getValidToken()
{
    console.log("🔥 ENTRATO IN getValidToken");
    let token = localStorage.getItem(TOKEN_KEY);
    let exp = localStorage.getItem(TOKEN_EXP_KEY);
    // Se esiste e non è scaduto → riusalo
    if (token && exp && Date.now() < parseInt(exp)) { return token; }
    // Altrimenti → richiedi nuovo token
    const response =
        await fetch('/api/v1/token',
        { method: 'POST',
          credentials: 'same-origin'});

    if (!response.ok) { throw new Error("Errore nel recupero del token"); }
    const data = await response.json();

    console.log("TOKEN RESPONSE:", data);

    const jwt = data.message; // ✔️ QUI È CORRETTO
     // 👉 Salviamo scadenza = ORA + 1 ora
     const expirationTime = Date.now() + (60 * 60 * 1000);
     localStorage.setItem(TOKEN_KEY, token);
     localStorage.setItem(TOKEN_EXP_KEY, expirationTime);
     return jwt;
*/

 async function aggiungiAlCarrello(idGioco, tipo)
 {
    console.log("🔥 ENTRATO IN aggiungiAlCarrello");
     const piattaformaSelect = document.getElementById("piattaformaSelect");
     const piattaformaId = piattaformaSelect ? piattaformaSelect.value : 0;
     let url;
     if (tipo === "carrello")
     {
        url = '/api/v1/carrello/add/' + idGioco;
     }
     else if (tipo === "listaDesideri")
     {
        url = '/api/v1/listaDesideri/add/' + idGioco;
     }
    if (piattaformaId !== null && piattaformaId !== undefined)
    {
        url += '?piattaformaId=' + piattaformaId;
    }
    try {
        const token = await getValidToken();
        const response = await fetch(url, {
            method: 'POST',
            headers: { 'Authorization': 'Bearer ' + token }
        });

        if (response.status === 401 || response.status === 403) {
            localStorage.removeItem(TOKEN_KEY);
            localStorage.removeItem(TOKEN_EXP_KEY);
        }

        const message = await response.text();
        const toastEl = document.getElementById('toastCarrello');
        toastEl.querySelector('.toast-body').textContent = message;
        const toast = new bootstrap.Toast(toastEl);
        if (response.ok)
        {
            toastEl.classList.remove('bg-danger');
            toastEl.classList.add('bg-success'); }
        else
        {
            toastEl.classList.remove('bg-success');
            toastEl.classList.add('bg-danger');
        } toast.show();
    }
    catch (error)
    {
        const toastEl = document.getElementById('toastCarrello');
        toastEl.querySelector('.toast-body').textContent = "Errore di connessione";
        toastEl.classList.remove('bg-success');
        toastEl.classList.add('bg-danger');
        new bootstrap.Toast(toastEl).show();
    }
 }