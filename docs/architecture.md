# Architettura — Scelte e Motivazioni

## Pattern architetturale: MVC monolitico a livelli

SportHub adotta un'architettura **MVC server-side monolitica** (Spring MVC + Thymeleaf) invece di un'architettura a microservizi o SPA/API REST + frontend separato.

**Motivazione**: per un progetto di dimensioni contenute con un unico dominio applicativo coeso (e-commerce + community), un monolite a livelli riduce la complessità operativa (un solo deployable, nessuna orchestrazione tra servizi) mantenendo comunque una netta separazione delle responsabilità grazie alla suddivisione in package `controllers → service → repository → entity`.

## Separazione in livelli (layered architecture)

```
controllers   -> orchestrazione richiesta HTTP / vista
service       -> logica di business, transazioni, regole di dominio
repository    -> accesso ai dati (Spring Data JPA)
entity        -> modello di dominio persistente (JPA)
config        -> configurazione trasversale (security, bean factory per entity)
converters/dto -> disaccoppiamento tra rappresentazione persistente ed esposta
```

**Motivazione**: ogni livello ha una responsabilità singola e testabile in isolamento. I controller restano "sottili" (thin controllers): non contengono query né regole di business, solo orchestrazione. Questo facilita la manutenzione e riduce il rischio di duplicazione della logica.

## Generic Service Pattern (`GenericService<ID,E,D,C,R>`)

Tutti i service di dominio (es. `ProductService`, `OrderService`, `EventService`) estendono una classe generica astratta `GenericService` parametrizzata su ID, Entity, DTO, Converter e Repository, che fornisce operazioni CRUD comuni (`getAll`, `getById`, `save`, `delete`).

**Motivazione**: evita la duplicazione del codice CRUD boilerplate in ogni service, centralizzando anche il riferimento al `Converter` e all'`ApplicationContext`. I service concreti si concentrano sulla logica specifica di dominio (es. calcolo totale ordine, gestione carrello, autorizzazioni fine-grained).

## Pattern DTO + Converter + costruzione entity da Map (`IMappable`)

Il progetto utilizza **DTO immutabili (Java `record`)** per l'input/output verso le view, convertiti da/verso le entity tramite classi `*Converter` che implementano `GenericConverter<D,E>`. In aggiunta, l'interfaccia `IMappable` (implementata da `GenericEntity`) fornisce un meccanismo basato su reflection per costruire un'entity a partire da una `Map<String,String>` (tipicamente i parametri di una request), usato dai bean `@Scope("prototype")` dichiarati in `EntityContext`.

**Motivazione**:
- i DTO isolano il modello di persistenza (JPA) dai dati effettivamente scambiati con le view, evitando di esporre direttamente le entity (e i relativi problemi di lazy-loading/serializzazione all'interno di Thymeleaf) e permettendo validazioni indipendenti dallo schema DB;
- il meccanismo `fromMap`/`toMap` di `IMappable` offre un modo generico di costruire entity da form dinamici senza dover scrivere un binding manuale per ogni singolo campo, a costo di una minore type-safety in fase di compilazione (mitigata con test).

## Persistenza: Flyway + `ddl-auto=validate`

Lo schema del database è definito e versionato esplicitamente tramite script SQL Flyway (`V1__CREATE_TABLES.sql`, `V2__INSERT_TABLES.sql`), mentre Hibernate è configurato in modalità `validate` (non genera né modifica automaticamente lo schema).

**Motivazione**: separare la definizione dello schema (SQL esplicito, versionato, rivedibile) dalla mappatura ORM riduce il rischio di migrazioni distruttive non intenzionali generate automaticamente da Hibernate (`update`/`create-drop`) e rende il processo di rilascio del database riproducibile e allineato tra ambienti diversi.

## Ereditarietà JPA `User → Person → Buyer` (strategia JOINED)

Il modello utente è modellato con tre tabelle collegate da chiave primaria condivisa: `user` (credenziali), `people` (dati anagrafici + ruolo), `buyers` (dati specifici dell'acquirente, es. flag `active`).

**Motivazione**: la strategia `JOINED` normalizza i dati evitando colonne ridondanti/nullable per ruoli diversi (a differenza di `SINGLE_TABLE`) e consente in prospettiva di aggiungere sottotipi ulteriori (es. `Staff`, `Admin` come entity dedicate) senza toccare lo schema esistente. Il costo (join aggiuntivi in lettura) è considerato accettabile per il volume di dati previsto.

## Sicurezza: Spring Security con login basato su email

L'autenticazione usa `email` come identificativo di login (non uno `username` separato), con password hashate tramite **BCrypt** (strength 12) attraverso `DelegatingPasswordEncoder`. Il caricamento del ruolo avviene dinamicamente da `Person.role` tramite `JpaUserDetailsService`.

**Motivazione**: l'email è naturalmente univoca e già presente come credenziale utente-friendly, evitando di richiedere un ulteriore identificativo tecnico in fase di registrazione. `DelegatingPasswordEncoder` consente in futuro di migrare algoritmo di hashing mantenendo compatibilità con password già salvate.

## Autorizzazione a più livelli

Le regole di autorizzazione **non sono concentrate in un unico punto**, ma distribuite su tre livelli complementari:

1. **Filtro HTTP globale** (`SecurityConfig.securityFilterChain`) per pattern URL grossolani (pubblico vs autenticato, ruoli su rotte specifiche come `/post/delete`);
2. **Annotazioni a livello di metodo controller** (`@PreAuthorize("hasAnyRole(...)")`) per endpoint sensibili (es. risposta/cancellazione Q&A);
3. **Verifica applicativa nel service** (es. `EventService.deleteIfAllowed`) per regole che dipendono dal dato stesso (es. "solo il proprietario del post, oppure Staff/Admin").

**Motivazione**: alcune regole di autorizzazione (in particolare quelle "ownership-based") non sono esprimibili in modo dichiarativo a livello di filtro URL o di semplice `@PreAuthorize` senza accedere all'entity; centralizzarle nel service, vicino alla logica di dominio, mantiene la regola coerente indipendentemente dal punto di ingresso.

## Gestione errori centralizzata

`GlobalExceptionHandler` (`@ControllerAdvice`) intercetta le `RuntimeException` non gestite e restituisce una pagina di errore comune (`error/errorPage.html`), in aggiunta ai blocchi `try/catch` locali nei singoli controller che gestiscono casi applicativi specifici (es. prodotto non trovato, carrello vuoto).

**Motivazione**: garantisce che nessuna eccezione non gestita produca uno stack trace grezzo lato utente, mantenendo comunque la possibilità, nei controller, di gestire in modo mirato i casi d'errore più comuni con messaggi contestuali.

## Aree di miglioramento identificate

- **Duplicazione del frammento navbar** tra le view: candidato a refactor con `th:fragment`/`th:replace` per ridurre la duplicazione HTML.
- **Assenza di redirect post-login differenziato per ruolo**: attualmente tutti gli utenti vengono reindirizzati alla home; una dashboard Admin dedicata potrebbe migliorare l'usabilità per Staff/Admin.
- **Entità `EventBoard`** presente nel codice ma disattivata: da rimuovere o da completare come feature, per evitare confusione nel modello di dominio.
- **Assenza di test automatici** oltre allo scaffolding di base (`SportHubApplicationTests`): da estendere con test unitari sui service e test di integrazione su repository/controller.
