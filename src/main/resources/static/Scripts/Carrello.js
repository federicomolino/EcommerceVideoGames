document.addEventListener("DOMContentLoaded", () => {

     document.querySelectorAll(".frecciaSu").forEach(btn => {
         btn.addEventListener("click", () => {
             const cella = btn.closest("td");
             const quantitaSpan = cella.querySelector(".quantita");

             let valore = parseInt(quantitaSpan.textContent, 10);
             valore += 1;
             quantitaSpan.textContent = valore;

             //Chiamata al backend aumento
             fetch('carrello/aumenta', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ nuovaQuantita: valore })
             })
             .then(async response => {

               //ERRORE BACKEND
               if (!response.ok) {
                 const data = await response.json(); // qui il body c'è
                 alert("Errore: " + data.message);
                 quantitaSpan.textContent = valore - 1;
                 return;
               }

               //SUCCESSO (anche senza body)
               location.reload();

             })
             .catch(err => {
               console.error("Errore fetch:", err);
             });
         });
     });

     document.querySelectorAll(".frecciaGiu").forEach(btn => {
         btn.addEventListener("click", () => {
             const cella = btn.closest("td");
             const quantitaSpan = cella.querySelector(".quantita");

             let valore = parseInt(quantitaSpan.textContent, 10);
             if (valore > 1) {
                valore -= 1;
                quantitaSpan.textContent = valore;
             }

             //Chiamata al backend diminuisci
              fetch('carrello/diminuisci', {
                 method: 'POST',
                 headers: {
                     'Content-Type': 'application/json'
                 },
                 body: JSON.stringify({ nuovaQuantita: valore })
              })
              .then(async response => {

                 //ERRORE BACKEND
                 if (!response.ok) {
                   const data = await response.json(); // qui il body c'è
                   alert("Errore: " + data.message);
                   quantitaSpan.textContent = valore + 1;
                   return;
                 }

                 //SUCCESSO (anche senza body)
                 location.reload();
               })
               .catch(err => {
                 console.error("Errore fetch:", err);
               });
         });
     });

})