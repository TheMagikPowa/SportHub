# UI Flows — Definizione dei Flussi Principali

Sintesi dei flussi utente principali dell'interfaccia. Per i diagrammi di dettaglio (sequence/flowchart Mermaid) vedi [`05-flusso-autenticazione.md`](05-flusso-autenticazione.md) e [`06-flussi-principali.md`](06-flussi-principali.md).

## 1. Onboarding (Guest → Buyer autenticato)

```
Home (/) 
  → Registrazione (/register/show-registration) 
  → Submit form 
  → Login (/login) 
  → Submit credenziali 
  → Home autenticata (/) → link a Dashboard
```

Errori gestiti: email già registrata (torna al form con messaggio), credenziali errate (`/login?error`).

## 2. Acquisto (Shopping journey)

```
Store (/products/store) o Ricerca (/products?name=...)
  → Dettaglio prodotto (/products/{id})
  → "Aggiungi al carrello" → POST /cart/add
  → Carrello (/cart) → verifica quantità/totale
  → "Vai al checkout" → GET /cart/checkout
  → Compila indirizzo, spedizione, pagamento → POST /cart/checkout
  → Ordine creato, carrello svuotato → redirect /cart (messaggio di successo)
  → Storico Ordini (/orders) → Dettaglio ordine (/orders/{id})
```

Errori gestiti: carrello vuoto in checkout (torna a `/cart/checkout` con messaggio d'errore), prodotto non trovato.

## 3. Gestione profilo

```
Dashboard (/dashboard/show-dashboard) 
  → Profilo (/profile/detail) — sola visualizzazione
  → Modifica (/profile/update) → POST /profile/save-update → redirect /profile/detail?success
  → Elimina account → POST /profile/delete → invalida sessione → redirect /?accountDeleted
```

## 4. Community — Hub Eventi

```
Hub (/hub) — elenco eventi
  → Crea evento (/hub/create-post) → POST /hub/save-post → redirect /hub
  → Commenta un evento → POST /hub/{eventId}/answer → redirect /hub
  → Elimina un post (proprietario o Staff/Admin) → POST /hub/delete-post → redirect /hub
```

## 5. Assistenza — Q&A

```
Q&A (/qA) — elenco domande e risposte
  → [Buyer] Crea domanda (/qA/create-question) → POST /qA/save-post → redirect /qA
  → [Staff/Admin] Rispondi → POST /qA/{questionId}/answer → redirect /qA
  → [Staff/Admin] Elimina domanda → POST /qA/delete-question → redirect /qA
```

## 6. Back-office — Gestione catalogo (Staff/Admin)

```
POST /admin/products/save     → crea prodotto     → redirect /products
POST /admin/products/update/{id} → aggiorna prodotto → redirect /products
POST /admin/products/delete/{id} → elimina prodotto  → redirect /products
POST /orders/update-status/{id}  → aggiorna stato ordine → redirect /orders/{id}
```

## Pattern trasversali applicati in tutti i flussi

- **Post/Redirect/Get**: ogni operazione di scrittura (`POST`) termina con un `redirect:` per evitare re-invii del form al refresh della pagina.
- **Flash messages**: gli esiti (successo/errore) vengono passati tramite `RedirectAttributes.addFlashAttribute` (es. "Product successfully added to the cart!", "Order placed successfully!") e mostrati nella pagina di destinazione dopo il redirect.
- **Gestione errori uniforme**: eccezioni applicative catturate nei controller popolano `model.errorMessage` e restituiscono `error/errorPage`; eccezioni non gestite vengono intercettate da `GlobalExceptionHandler`.
- **Autorizzazione contestuale**: le azioni riservate (risposta Q&A, cancellazione post/domande) sono nascoste in UI (`sec:authorize`) e comunque riverificate lato server (`@PreAuthorize` o logica di service), secondo il principio "never trust the client".
