// Migliora input prezzo (solo estetica)
const prezzo = document.getElementById("prezzo");
prezzo.addEventListener("blur", () => {
    if (prezzo.value && !prezzo.value.includes(",")) {
        prezzo.value = parseFloat(prezzo.value).toFixed(2).replace(".", ",");
    }
});