# API Routing — Rotte MVC e Pagine

SportHub non espone una REST API pubblica: tutte le rotte elencate sono **endpoint MVC Spring** che restituiscono nomi logici di view Thymeleaf oppure eseguono un `redirect:`. Le tabelle sono raggruppate per controller.

Legenda accesso: **Pub** = pubblico (permitAll) · **Auth** = richiede login (qualsiasi ruolo) · **Buyer** = pensato per Buyer · **Staff/Admin** = ristretto a ruoli specifici.

## AuthenticationController — `/`

| Metodo | Path | Vista / Azione | Accesso |
|---|---|---|---|
| GET | `/` | `home/index` | Pub |
| GET | `/login` | `login/login` | Pub |
| GET | `/accesso-negato` | `error/errorPage` | Pub (invocato da Spring Security su 403) |

## RegistrationController — `/register`

| Metodo | Path | Vista / Azione | Accesso |
|---|---|---|---|
| GET | `/register/show-registration` | `register/register` (form `PersonDTO`) | Pub |
| POST | `/register/save-registration` | crea `Buyer` → `redirect:/login` (o ritorna al form con errore) | Pub |

## ProductController — `/products`

| Metodo | Path | Vista / Azione | Accesso |
|---|---|---|---|
| GET | `/products/store` | `store/store` (catalogo completo) | Pub |
| GET | `/products` | `product-list` (ricerca/filtro per `name`, `pGender`, `aCategory`) | Pub |
| GET | `/products/{id}` | `product/product` (dettaglio) | Pub |

## ProductAdminController — `/admin/products`

| Metodo | Path | Vista / Azione | Accesso |
|---|---|---|---|
| POST | `/admin/products/save` | crea prodotto → `redirect:/products` | Staff/Admin (operativo lato back-office) |
| POST | `/admin/products/delete/{id}` | elimina prodotto → `redirect:/products` | Staff/Admin |
| POST | `/admin/products/update/{id}` | aggiorna prodotto → `redirect:/products` (o `redirect:/admin/products/edit/{id}?error`) | Staff/Admin |

> Nota: a livello di `SecurityConfig` questi path ricadono nella regola generica `anyRequest().authenticated()` — non è definita una regola `hasRole("ADMIN")` esplicita per `/admin/products/**`; è un'area segnalata come possibile hardening futuro (vedi `architecture.md`).

## CartController — `/cart`

| Metodo | Path | Vista / Azione | Accesso |
|---|---|---|---|
| GET | `/cart` | `cart/cart` (contenuto carrello + totale) | Auth (Buyer) |
| POST | `/cart/add` | aggiunge prodotto (`productId`, `quantity`) → `redirect:/cart` | Auth (Buyer) |
| POST | `/cart/remove/{productId}` | rimuove prodotto → `redirect:/cart` | Auth (Buyer) |
| GET | `/cart/checkout` | `checkout/checkout` (riepilogo pre-ordine) | Auth (Buyer) |
| POST | `/cart/checkout` | crea ordine da carrello → `redirect:/cart` (o torna al checkout in caso di errore) | Auth (Buyer) |

## OrderController — `/orders`

| Metodo | Path | Vista / Azione | Accesso |
|---|---|---|---|
| GET | `/orders` | `order/order` (storico ordini del Buyer loggato) | Auth (Buyer) |
| GET | `/orders/{id}` | `order/orderDetail` (dettaglio ordine) | Auth |
| POST | `/orders/update-status/{id}` | aggiorna `OrderStatus` → `redirect:/orders/{id}` | Staff/Admin (operazione gestionale) |

## PersonController — `/profile`

| Metodo | Path | Vista / Azione | Accesso |
|---|---|---|---|
| GET | `/profile/detail` | `profile/profile` (dati profilo utente loggato) | Auth |
| GET | `/profile/update` | `profile/edit-profile` (form modifica) | Auth |
| POST | `/profile/save-update` | aggiorna dati profilo → `redirect:/profile/detail?success` | Auth |
| POST | `/profile/delete` | elimina account, invalida sessione → `redirect:/?accountDeleted` | Auth |

## BuyerController — `/profile` (gestione indirizzi)

| Metodo | Path | Vista / Azione | Accesso |
|---|---|---|---|
| GET | `/profile/buyer{id}` | `buyer/profile` (profilo + rubrica indirizzi) | Auth |
| GET | `/profile/{buyerId}/address/new` | `buyer/address-form` (nuovo indirizzo) | Auth |
| POST | `/profile/{buyerId}/address/save` | salva indirizzo → `redirect:/buyer/profile/{buyerId}` | Auth |

## DashboardController — `/dashboard`

| Metodo | Path | Vista / Azione | Accesso |
|---|---|---|---|
| GET | `/dashboard/show-dashboard` | `dashboard/dashboard` (hub di collegamenti rapidi) | Auth |

## HubController — `/hub`

| Metodo | Path | Vista / Azione | Accesso |
|---|---|---|---|
| GET | `/hub` | `hub/hub` (bacheca eventi) | Auth |
| GET | `/hub/create-post` | `hub/create-post` (form nuovo evento) | Auth (Buyer) |
| POST | `/hub/save-post` | crea evento (associato al Buyer autenticato) → `redirect:/hub` | Auth (Buyer) |
| POST | `/hub/{eventId}/answer` | aggiunge commento a un evento → `redirect:/hub` | Auth (Buyer) |
| POST | `/hub/delete-post` | elimina evento (proprietario oppure Staff/Admin, verifica in `EventService.deleteIfAllowed`) → `redirect:/hub` | Auth (con verifica ownership/ruolo) |

## QAController — `/qA`

| Metodo | Path | Vista / Azione | Accesso |
|---|---|---|---|
| GET | `/qA` | `qna/qna` (elenco domande/risposte) | Auth |
| GET | `/qA/create-question` | `qna/create-qna` (form nuova domanda) | Auth (Buyer) |
| POST | `/qA/save-post` | crea domanda (status `OPEN`) → `redirect:/qA` | Auth (Buyer) |
| POST | `/qA/{questionId}/answer` | risponde a una domanda | Staff/Admin (`@PreAuthorize hasAnyRole('STAFF','ADMIN')`) |
| POST | `/qA/delete-question` | elimina domanda | Staff/Admin (`@PreAuthorize` + regola in `SecurityConfig`) |

## ContactsController — `/contact`

| Metodo | Path | Vista / Azione | Accesso |
|---|---|---|---|
| GET | `/contact` | `contact/contact` (form contatti, `QuestionQA` vuoto) | Pub |

## Rotte di sistema (Spring Security)

| Metodo | Path | Gestore | Note |
|---|---|---|---|
| POST | `/login` | Spring Security `formLogin` filter | non un metodo controller custom; parametro username = `email` |
| POST | `/logout` | Spring Security `logout` filter | invalida sessione, cancella cookie `JSESSIONID`, redirect `/login?logout` |

## Riepilogo pattern di risposta dei controller

Tutti i controller seguono uno schema uniforme:

- **GET** → popola il `Model` e restituisce il nome logico di una vista Thymeleaf;
- **POST** (comandi di scrittura) → invoca il service, poi restituisce `redirect:/percorso` (Post/Redirect/Get) per evitare re-invii accidentali del form;
- gli errori applicativi vengono gestiti con `try/catch` locali (→ `model.addAttribute("errorMessage", ...)` e vista `error/errorPage`) oppure, se non catturati, dal `GlobalExceptionHandler` globale.
