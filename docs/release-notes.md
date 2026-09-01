# Release Notes — SportHub

## Versione corrente (stato del repository analizzato)

### Funzionalità implementate

**Autenticazione & Account**
- Registrazione nuovo Buyer con validazione email univoca
- Login basato su email + password (Spring Security, BCrypt)
- Logout con invalidazione sessione
- Visualizzazione, modifica ed eliminazione del profilo personale
- Gestione ruoli applicativi: `BUYER`, `STAFF`, `ADMIN`
- Pagina di accesso negato per richieste non autorizzate

**Catalogo & E-commerce**
- Consultazione pubblica del catalogo prodotti (store)
- Ricerca/filtro prodotti per nome, genere, fascia d'età
- Dettaglio prodotto
- Gestione catalogo (creazione, modifica, eliminazione prodotto) lato Staff/Admin
- Carrello: aggiunta, rimozione, calcolo totale
- Checkout con inserimento indirizzo, tipo di spedizione, metodo di pagamento
- Creazione ordine da carrello con snapshot di nome/prezzo prodotto
- Storico ordini personale e dettaglio ordine
- Aggiornamento stato ordine (`PROCESSING → SHIPPED → DELIVERED`) lato gestionale

**Community — Hub Eventi**
- Bacheca eventi sportivi consultabile dagli utenti autenticati
- Creazione di un nuovo evento da parte del Buyer
- Commenti/risposte agli eventi
- Cancellazione evento: consentita al proprietario oppure a Staff/Admin (autorizzazione applicativa)

**Assistenza — Q&A**
- Apertura di una richiesta di assistenza da parte del Buyer
- Consultazione elenco domande/risposte
- Risposta alle domande riservata a Staff/Admin
- Eliminazione domanda riservata a Staff/Admin

**Sicurezza**
- CSRF protection attiva
- Content-Security-Policy configurata
- Password hashate con BCrypt (strength 12)
- Autorizzazioni a più livelli: filtro URL globale, `@PreAuthorize` su singoli endpoint, verifica ownership nel service

**Dati & Infrastruttura**
- Schema database versionato con Flyway (`V1__CREATE_TABLES.sql`)
- Dataset di seed per demo/test (`V2__INSERT_TABLES.sql`)
- Gestione centralizzata delle eccezioni non gestite (`GlobalExceptionHandler`)

### Limitazioni note / debito tecnico

- **Redirect post-login non differenziato per ruolo**: tutti gli utenti vengono reindirizzati alla home dopo il login; non esiste una dashboard Admin dedicata separata dalla dashboard Buyer.
- **Area `/admin/products/**` priva di regola di autorizzazione esplicita** a livello di `SecurityConfig` (ricade nella regola generica `authenticated()`): consigliato l'irrigidimento con `hasAnyRole("ADMIN","STAFF")`.
- **Disallineamento minore** tra l'enum Java `QuestionStatus` (`OPEN`, `SOLVED`) e il valore SQL `ENUM("OPEN","CLOSED")` in `questions_qa.state`: da armonizzare.
- **Entità `EventBoard`** presente nel codice sorgente ma commentata/non attiva: non fa parte del modello dati effettivo, da rimuovere o completare.
- **Duplicazione del markup di navbar** tra le view Thymeleaf: non ancora estratta in un frammento riutilizzabile (`th:fragment`).
- **Gestione stock non automatizzata**: la quantità disponibile di un prodotto (`products.quantity`) non viene decrementata automaticamente alla conferma di un ordine.
- **Assenza di notifiche email** (conferma ordine, nuova risposta Q&A, nuovo commento evento).
- **Copertura di test automatici minima**: presente solo lo scaffolding di base (`SportHubApplicationTests`), da estendere con test unitari e di integrazione.
- **Cascata `orders → buyers` (`ON DELETE CASCADE`)**: l'eliminazione di un account comporta la perdita dello storico ordini associato; da rivalutare per contesti con requisiti di conservazione documentale/fiscale.

### Possibili sviluppi futuri

- Dashboard Admin dedicata con reportistica (vendite, prodotti più venduti, domande aperte)
- Sistema di notifiche (email o in-app)
- Paginazione e ricerca full-text avanzata sul catalogo e sulla bacheca eventi
- Gestione multi-indirizzo con indirizzo di spedizione predefinito, completamente integrata nel flusso di checkout
- Recensioni e valutazioni prodotto
- API REST dedicata per un eventuale frontend disaccoppiato (SPA/mobile)

---

**Nota metodologica**: queste note di rilascio sono state ricavate dall'analisi statica del codice sorgente (controller, service, repository, entity, configurazione di sicurezza e schema SQL) presente nel repository al momento della stesura di questa documentazione, e non da un changelog o da tag di versione formalmente pubblicati nel progetto.
