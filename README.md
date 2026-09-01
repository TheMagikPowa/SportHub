# SportHub

**SportHub** è una web application e-commerce full-stack dedicata alla vendita di articoli sportivi, sviluppata in **Java 21 / Spring Boot 3** con rendering server-side **Thymeleaf** (architettura MVC monolitica a livelli) e database relazionale **MySQL**, gestito tramite migrazioni **Flyway**.

Oltre alla componente e-commerce (catalogo prodotti, carrello, checkout, storico ordini), la piattaforma integra due moduli community/support:

- **Hub Eventi** — bacheca dove i Buyer pubblicano e commentano eventi sportivi (uscite in bici, tornei, escursioni)
- **Q&A / Assistenza** — sistema di domande e risposte tra Buyer e Staff/Admin

L'applicazione gestisce tre ruoli utente (**BUYER**, **STAFF**, **ADMIN**) con permessi differenziati, autenticazione basata su sessione (Spring Security, form login) e password hashate con BCrypt.

---

## Indice

- [Stack tecnologico](#stack-tecnologico)
- [Architettura](#architettura)
- [Modello dati](#modello-dati)
- [Ruoli e autenticazione](#ruoli-e-autenticazione)
- [Funzionalità](#funzionalità)
- [Rotte principali](#rotte-principali)
- [Avvio del progetto](#avvio-del-progetto)
- [Limitazioni note / debito tecnico](#limitazioni-note--debito-tecnico)
- [Sviluppi futuri](#sviluppi-futuri)
- [Documentazione di progetto](#documentazione-di-progetto)

---

## Stack tecnologico

| Componente | Tecnologia |
|---|---|
| Linguaggio / Framework | Java 21, Spring Boot 3 (Spring MVC, Spring Data JPA, Spring Security) |
| Template engine | Thymeleaf (+ `thymeleaf-extras-springsecurity6`) |
| Database | MySQL, schema versionato con **Flyway** |
| Build | Maven (`pom.xml`, wrapper `mvnw`) |
| Utility | Lombok (`@Data`, `@Getter/@Setter`, `@NoArgsConstructor`, ecc.) |
| Sicurezza | Form login basato su sessione, password hashate con **BCrypt** (strength 12) |

---

## Architettura

SportHub adotta un'architettura **MVC server-side monolitica a livelli**, invece di microservizi o SPA + API REST separata: per un progetto di dimensioni contenute con un dominio coeso (e-commerce + community), questa scelta riduce la complessità operativa mantenendo una netta separazione delle responsabilità.

```
controllers   -> orchestrazione richiesta HTTP / vista
service       -> logica di business, transazioni, regole di dominio
repository    -> accesso ai dati (Spring Data JPA)
entity        -> modello di dominio persistente (JPA)
config        -> configurazione trasversale (security, bean factory per entity)
converters/dto -> disaccoppiamento tra rappresentazione persistente ed esposta
```

**Pattern applicati:**

- **Generic Service Pattern** — `GenericService<ID,E,D,C,R>` fornisce CRUD comune (`getAll`, `getById`, `save`, `delete`) a tutti i service di dominio, evitando boilerplate ripetuto.
- **DTO + Converter** — DTO immutabili (Java `record`) disaccoppiano il modello JPA dai dati scambiati con le view, evitando di esporre direttamente le entity.
- **`IMappable`** — meccanismo basato su reflection per costruire entity da `Map<String,String>` (parametri di request), usato dai bean `@Scope("prototype")` in `EntityContext`.
- **Persistenza Flyway + `ddl-auto=validate`** — lo schema DB è sorgente di verità versionata via SQL esplicito (`V1__CREATE_TABLES.sql`, `V2__INSERT_TABLES.sql`); Hibernate valida soltanto, non genera né modifica lo schema automaticamente.
- **Autorizzazione a più livelli** — filtro HTTP globale (`SecurityConfig`), annotazioni `@PreAuthorize` sui controller, verifica applicativa nel service (per regole ownership-based non esprimibili a livello dichiarativo).
- **Gestione errori centralizzata** — `GlobalExceptionHandler` (`@ControllerAdvice`) intercetta le eccezioni non gestite, in aggiunta ai `try/catch` locali nei controller.

---

## Modello dati

Database MySQL con schema versionato tramite Flyway. Diagramma ER completo in [`docs/02-er-diagram.md`](docs/02-er-diagram.md).

### Gerarchia utente (`User → Person → Buyer`)

Modellata con **ereditarietà JPA `JOINED`**: tre tabelle collegate 1:1 per chiave primaria condivisa.

- **`user`** — credenziali (email, password hashata, data creazione)
- **`people`** — dati anagrafici + ruolo applicativo (`BUYER`, `STAFF`, `ADMIN`)
- **`buyers`** — attributi specifici del ruolo Buyer (flag `active`)

La strategia `JOINED` (invece di `SINGLE_TABLE`) evita colonne nullable per ruoli diversi, mantenendo ogni tabella coerente col proprio sottotipo.

### Altre entità principali

| Entità | Descrizione |
|---|---|
| `addresses` | Rubrica indirizzi di spedizione (1:N con Buyer) |
| `events` / `events_answers` | Hub Eventi — post community e relativi commenti |
| `questions_qa` / `messages_qa` | Sistema Q&A — richieste di assistenza e risposte dello Staff |
| `cart` / `cart_has_products` | Carrello (1:1 con Buyer) e relazione N:N con `products` |
| `products` | Catalogo, indipendente da Buyer/Order |
| `orders` / `orders_has_products` | Ordini, con snapshot storico (denormalizzato) di nome/prezzo prodotto al momento dell'acquisto |

**Nota di design rilevante:** `orders_has_products` duplica volutamente `name`, `unit_price` e `final_price` per garantire che lo storico ordini resti uno snapshot immutabile, indipendente da modifiche successive al catalogo.

---

## Ruoli e autenticazione

L'autenticazione usa **Spring Security** con form-login classico (sessione HTTP), `DaoAuthenticationProvider` e `UserDetailsService` custom (`JpaUserDetailsService`).

- **Identificativo di login**: `email` (non uno `username` separato)
- **Password**: hashate con **BCrypt** (strength 12) via `DelegatingPasswordEncoder`
- **Ruolo**: caricato dinamicamente da `Person.role`, tradotto in autorità Spring Security `ROLE_<NOME>`
- **Sessione**: rigenerazione dell'id sessione al login (`sessionFixation().changeSessionId()`) per prevenire session fixation
- **Registrazione**: ogni nuovo utente è creato come `Buyer` con `role=BUYER`, `active=true` (default anche via `@PrePersist` come fallback)

Non esiste un redirect post-login differenziato per ruolo: tutti gli utenti atterrano sulla home (`defaultSuccessUrl("/", true)`); la differenziazione avviene a valle, tramite `sec:authorize` nelle view e `@PreAuthorize`/verifica applicativa sugli endpoint sensibili.

| Ruolo | Descrizione |
|---|---|
| **Guest** | Naviga home, store, dettaglio prodotto, contatti, registrazione, login |
| **Buyer** | Acquista prodotti, gestisce carrello/ordini/profilo, partecipa a Hub e Q&A |
| **Staff** | Risponde alle richieste Q&A, modera domande |
| **Admin** | Gestisce catalogo prodotti, ordini, modera Hub e Q&A |

Altre misure di sicurezza: **CSRF** attivo, **Content-Security-Policy** custom, gestione centralizzata di logout e accesso negato.

---

## Funzionalità

**Autenticazione & Account**
Registrazione, login/logout, gestione profilo (visualizza/modifica/elimina), ruoli applicativi, pagina di accesso negato.

**Catalogo & E-commerce**
Consultazione pubblica del catalogo, ricerca/filtro (nome, genere, fascia d'età), dettaglio prodotto, gestione catalogo lato Staff/Admin, carrello, checkout (indirizzo, spedizione, pagamento), creazione ordine con snapshot prodotto, storico ordini, aggiornamento stato ordine.

**Community — Hub Eventi**
Bacheca eventi, creazione post, commenti, cancellazione (proprietario oppure Staff/Admin).

**Assistenza — Q&A**
Apertura richieste da parte del Buyer, consultazione elenco, risposta ed eliminazione riservate a Staff/Admin.

**Sicurezza**
CSRF, Content-Security-Policy, BCrypt, autorizzazione a tre livelli (filtro URL, `@PreAuthorize`, verifica ownership nel service).

**Dati & Infrastruttura**
Schema versionato Flyway, dataset di seed per demo/test, gestione centralizzata delle eccezioni.

---

## Rotte principali

Tutte le rotte sono endpoint MVC Spring (nessuna REST API pubblica esposta). Legenda: **Pub** = pubblico · **Auth** = richiede login · **Staff/Admin** = ruoli ristretti.

| Area | Path principali | Accesso |
|---|---|---|
| Home / Auth | `/`, `/login`, `/accesso-negato` | Pub |
| Registrazione | `/register/show-registration`, `/register/save-registration` | Pub |
| Catalogo | `/products/store`, `/products`, `/products/{id}` | Pub |
| Gestione catalogo | `/admin/products/save`, `/update/{id}`, `/delete/{id}` | Staff/Admin |
| Carrello | `/cart`, `/cart/add`, `/cart/remove/{id}`, `/cart/checkout` | Auth (Buyer) |
| Ordini | `/orders`, `/orders/{id}`, `/orders/update-status/{id}` | Auth / Staff/Admin |
| Profilo | `/profile/detail`, `/profile/update`, `/profile/save-update`, `/profile/delete` | Auth |
| Hub Eventi | `/hub`, `/hub/create-post`, `/hub/save-post`, `/hub/{id}/answer`, `/hub/delete-post` | Auth |
| Q&A | `/qA`, `/qA/create-question`, `/qA/save-post`, `/qA/{id}/answer`, `/qA/delete-question` | Auth / Staff/Admin |
| Contatti | `/contact` | Pub |

Tabella completa in [`docs/api-routing.md`](docs/api-routing.md).

---

## Avvio del progetto

### Prerequisiti

- Java 21
- MySQL (istanza locale o raggiungibile)
- Maven (oppure il wrapper incluso `./mvnw`)

### Configurazione

Nel file `application.properties`, verifica/adatta i parametri di connessione al database:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/SportHub?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=<la-tua-password>

spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
```

Crea il database (vuoto) prima del primo avvio — sarà popolato dalle migration Flyway:

```sql
CREATE DATABASE SportHub;
```

### Avvio

```bash
./mvnw spring-boot:run
```

All'avvio, Flyway esegue automaticamente le migrazioni (`V1__CREATE_TABLES.sql`, `V2__INSERT_TABLES.sql`), creando lo schema e popolandolo con un dataset di seed per demo/test. Hibernate, in modalità `validate`, verifica solo la coerenza tra entity JPA e schema esistente.

L'applicazione sarà raggiungibile su `http://localhost:8080`.

---

## Limitazioni note / debito tecnico

- Nessun redirect post-login differenziato per ruolo (tutti gli utenti atterrano sulla home)
- `/admin/products/**` privo di regola di autorizzazione esplicita in `SecurityConfig` (ricade in `authenticated()` generico)
- Disallineamento minore tra l'enum Java `QuestionStatus` (`OPEN`, `SOLVED`) e il valore SQL `ENUM("OPEN","CLOSED")`
- Entità `EventBoard` presente nel codice ma non attiva/non parte del modello dati effettivo
- Duplicazione del markup navbar tra le view (non ancora estratta in `th:fragment`)
- Gestione stock non automatizzata (quantità prodotto non decrementata dopo l'acquisto)
- Assenza di notifiche email
- Copertura di test automatici minima
- Cascata `orders → buyers ON DELETE CASCADE`: l'eliminazione di un account comporta la perdita dello storico ordini associato

## Sviluppi futuri

- Dashboard Admin dedicata con reportistica (vendite, prodotti più venduti, domande aperte)
- Sistema di notifiche (email o in-app)
- Paginazione e ricerca full-text avanzata
- Gestione multi-indirizzo completamente integrata nel checkout
- Recensioni e valutazioni prodotto
- API REST dedicata per un eventuale frontend disaccoppiato (SPA/mobile)

---

## Documentazione di progetto

| Documento | Contenuto |
|---|---|
| [01 — Analisi Iniziale](docs/01-analisi-iniziale.md) | Descrizione progetto, obiettivo, target utenti, feature |
| [02 — ER Diagram](docs/02-er-diagram.md) | Entità, attributi, PK, relazioni e cardinalità |
| [03 — Architettura Backend](docs/03-architettura-backend.md) | Diagramma dei package e flusso di una richiesta |
| [04 — Navigazione Frontend](docs/04-navigazione-frontend.md) | Mappa delle pagine, differenze User/Admin |
| [05 — Flusso di Autenticazione](docs/05-flusso-autenticazione.md) | Registrazione, login, ruolo, protezione pagine |
| [06 — Flussi Principali](docs/06-flussi-principali.md) | Flow chart CRUD, azione utente, azione admin |
| [architecture.md](docs/architecture.md) | Scelte architetturali e motivazioni |
| [api-routing.md](docs/api-routing.md) | Rotte MVC complete |
| [database.md](docs/database.md) | Descrizione entità e motivazioni progettuali |
| [ui-flows.md](docs/ui-flows.md) | Flussi UI principali |
| [release-notes.md](docs/release-notes.md) | Funzionalità implementate e note di rilascio |
