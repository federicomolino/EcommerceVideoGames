const TOKEN_KEY = "jwt_token";
const TOKEN_EXP_KEY = "jwt_exp";

async function getValidToken() {

    let token = localStorage.getItem(TOKEN_KEY);
    let exp = localStorage.getItem(TOKEN_EXP_KEY);

    // token valido
    if (token && exp && Date.now() < parseInt(exp)) {
        return token;
    }

    // richiesta nuovo token
    const response = await fetch('/api/v1/token', {
        method: 'POST',
        credentials: 'same-origin'
    });

    if (!response.ok) {
        throw new Error("Errore nel recupero del token");
    }

    const data = await response.json();

    const jwt = data.message;

    // scadenza 1 ora
    const expirationTime = Date.now() + (60 * 60 * 1000);

    localStorage.setItem(TOKEN_KEY, jwt);
    localStorage.setItem(TOKEN_EXP_KEY, expirationTime);

    return jwt;
}