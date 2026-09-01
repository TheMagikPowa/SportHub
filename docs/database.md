# Database — Descrizione Entità e Motivazioni Progettuali

Database relazionale **MySQL**, schema definito e versionato tramite migrazioni **Flyway** (`src/main/resources/db/V1__CREATE_TABLES.sql`, `V2__INSERT_TABLES.sql`). Per il diagramma ER completo con cardinalità vedi [`02-er-diagram.md`](02-er-diagram.md).

## Entità e motivazioni

### `user` / `people` / `buyers` (gerarchia utente)

Il modello utente è suddiviso in **tre tabelle collegate 1:1 per chiave primaria condivisa**, che rispecchiano la gerarchia JPA `User → Person → Buyer` (strategia `JOINED`):

- **`user`**: dati di autenticazione puri (email, password hashata, data creazione). Rappresenta il concetto minimo di "account".
- **`people`**: dati anagrafici (nome, cognome, data di nascita, genere) e **ruolo applicativo** (`BUYER`, `STAFF`, `ADMIN`). Separare `people` da `user` permette di trattare in modo uniforme utenti con ruoli diversi mantenendo un'unica tabella di credenziali.
- **`buyers`**: attributi specifici del ruolo Buyer (attualmente solo `active`, flag per disabilitare un account cliente senza eliminarlo).

**Perché non un'unica tabella (`SINGLE_TABLE`)?** Con `SINGLE_TABLE` tutte le colonne specifiche di ruolo (es. `active` per i Buyer) sarebbero nullable per Staff/Admin, generando una tabella "sporca" e più difficile da validare a livello di vincoli SQL (`NOT NULL`). La strategia `JOINED` mantiene ogni tabella coerente con il proprio sottotipo, al costo di join aggiuntivi in lettura — trade-off ritenuto accettabile per il volume di dati previsto in un progetto di questa scala.

### `addresses`

Rubrica indirizzi di spedizione collegata al Buyer (relazione 1:N: un Buyer può avere più indirizzi). Include dati di contatto (`phone_number`) necessari per la spedizione. `ON DELETE CASCADE`: se un Buyer viene eliminato, i suoi indirizzi vengono rimossi automaticamente (non ha senso conservare indirizzi orfani).

### `events` / `events_answers` (Hub Eventi)

- **`events`**: post pubblicati da un Buyer nella bacheca community (titolo, testo, data evento, categoria sportiva). La FK verso `buyers` è **nullable con `ON DELETE SET NULL`**: se l'autore elimina il proprio account, il post può restare visibile (contenuto storico della community) senza vincolare la cancellazione dell'utente.
- **`events_answers`**: commenti/risposte a un evento, in relazione N:1 sia con `events` sia con `buyers`. Qui la FK verso `events` è `ON DELETE CASCADE` (i commenti non hanno senso senza il post originale), mentre quella verso `buyers` è anch'essa `CASCADE` nello script SQL.

Questa scelta (nullable + SET NULL sul post, cascade sui commenti) riflette una decisione di prodotto: **il contenuto della bacheca community è considerato persistente e di valore collettivo**, mentre i commenti sono strettamente dipendenti dal post e dall'autore.

### `questions_qa` / `messages_qa` (Q&A / Assistenza)

- **`questions_qa`**: richiesta di assistenza aperta da un Buyer, con stato (`OPEN`/`CLOSED`) per tracciarne l'evasione.
- **`messages_qa`**: risposte fornite da un membro dello Staff (o Admin) a una domanda. La FK verso lo staff (`staff_people_user_id`) è **nullable con `ON DELETE SET NULL`**: se l'operatore che ha risposto viene rimosso dal sistema, la risposta resta comunque consultabile nello storico, solo "orfana" di autore.

Questo modello consente in prospettiva una **conversazione multi-messaggio** per singola domanda (più righe in `messages_qa` per la stessa `question_id`), non solo una risposta singola.

### `cart` / `cart_has_products`

- **`cart`**: un carrello per Buyer, in relazione **1:1** con `buyers` (`buyers_people_user_id` è sia PK sia FK, mappata lato JPA con `@OneToOne @MapsId`). Questa scelta evita la necessità di gestire un ID surrogato separato per il carrello: l'id del carrello coincide con l'id del Buyer.
- **`cart_has_products`**: tabella di giunzione N:N tra `cart` e `products`, con l'attributo aggiuntivo `quantity`. È il classico pattern "tabella associativa con payload" necessario perché la relazione N:N carrello-prodotto porta con sé un dato (la quantità) che non appartiene né al carrello né al prodotto singolarmente.

### `products`

Catalogo prodotti, entità **indipendente** da Buyer/Order (nessuna riga di prodotto viene mai modificata dal processo di acquisto). Contiene attributi di categorizzazione (`category`, `gender`, `age_category`) pensati per abilitare ricerca/filtro lato `ProductController.listProducts`. Vincolo `CHECK (price >= 0)` a garanzia di integrità anche a livello database, non solo applicativo.

### `orders` / `orders_has_products`

- **`orders`**: un ordine per acquisto, con stato (`PROCESSING → SHIPPED → DELIVERED`), sconto applicato e totale.
- **`orders_has_products`**: tabella di giunzione N:N tra `orders` e `products`, ma a differenza di `cart_has_products` **duplica volutamente** `name`, `unit_price` e `final_price` del prodotto al momento dell'acquisto (denormalizzazione intenzionale).

**Motivazione della denormalizzazione**: un ordine deve restare uno **snapshot storico immutabile** di ciò che è stato acquistato, a un prezzo e con un nome che potrebbero cambiare in futuro nel catalogo (`products`). Se `orders_has_products` referenziasse solo `products_id` e il prodotto venisse successivamente rinominato o riprezzato (o eliminato), lo storico ordini risulterebbe alterato o incompleto. Copiare `name`/`unit_price`/`final_price` al momento della creazione dell'ordine garantisce la correttezza storica indipendentemente dalle modifiche successive al catalogo.

## Enumerazioni (mappate `ENUM` SQL ↔ Java `enum` con `EnumType.STRING`)

| Enum | Valori | Utilizzo |
|---|---|---|
| `Role` | `BUYER`, `STAFF`, `ADMIN` | Ruolo applicativo (`people.role`) |
| `PersonGender` | `M`, `F`, `OTHER` | Genere anagrafico (`people.gender`) |
| `EventType` | `CICLYNG`, `FOOTBALL`, `CLIMBING`, `COMBAT_SPORTS`, `WEIGHTLIFTING`, `WATER_SPORTS`, `EQUESTRIAN_SPORTS`, `OTHER` | Categoria evento (`events.category`) |
| `ProductCategory` | `CYCLING`, `FOOTBALL`, `CLIMBING`, `COMBAT_SPORTS`, `WEIGHTLIFTING`, `WATER_SPORTS`, `EQUESTRIAN_SPORTS`, `OTHER` | Categoria prodotto (`products.category`) |
| `ProductGender` | `M`, `F`, `UNISEX` | Genere prodotto (`products.gender`) |
| `AgeCategory` | `KIDS`, `ADULTS` | Fascia d'età prodotto (`products.age_category`) |
| `OrderStatus` | `PROCESSING`, `SHIPPED`, `DELIVERED` | Stato ordine (`orders.status`) |
| `QuestionStatus` | `OPEN`, `SOLVED`/`CLOSED`* | Stato domanda Q&A (`questions_qa.state`) |

\* Nota di disallineamento minore rilevata: l'enum Java `QuestionStatus` definisce `OPEN`/`SOLVED`, mentre lo schema SQL dichiara `ENUM("OPEN","CLOSED")`. Da allineare in una futura revisione dello schema o dell'enum per evitare errori di mapping su valori non coincidenti.

## Politiche di integrità referenziale (riepilogo)

| Relazione | ON DELETE | ON UPDATE | Razionale |
|---|---|---|---|
| `people → user` | CASCADE | CASCADE | L'anagrafica non ha senso senza l'account |
| `buyers → people` | CASCADE | CASCADE | Il ruolo Buyer non ha senso senza la persona |
| `events → buyers` | SET NULL | CASCADE | Contenuto community conservato anche se l'autore viene rimosso |
| `events_answers → events` | CASCADE | CASCADE | I commenti non sopravvivono al post |
| `events_answers → buyers` | CASCADE | CASCADE | Coerenza con la cancellazione dell'utente |
| `questions_qa → buyers` | CASCADE | CASCADE | La richiesta è strettamente personale |
| `messages_qa → questions_qa` | CASCADE | CASCADE | Le risposte non sopravvivono alla domanda |
| `messages_qa → people` (staff) | SET NULL | CASCADE | Lo storico risposte resta anche se l'operatore viene rimosso |
| `addresses → buyers` | CASCADE | CASCADE | Rubrica personale, non condivisa |
| `cart → buyers` | CASCADE | CASCADE | Carrello personale, 1:1 |
| `cart_has_products → cart` | CASCADE | CASCADE | Righe carrello dipendenti dal carrello |
| `cart_has_products → products` | CASCADE | CASCADE | Coerenza referenziale con il catalogo |
| `orders → buyers` | CASCADE | CASCADE | Ordine legato al proprietario (nota: elimina anche lo storico ordini se l'utente viene cancellato — vedi considerazioni sotto) |
| `orders_has_products → orders` | CASCADE | CASCADE | Righe ordine dipendenti dall'ordine |
| `orders_has_products → products` | CASCADE | CASCADE | Coerenza referenziale con il catalogo |

**Considerazione di design**: la cascata `orders → buyers ON DELETE CASCADE` implica che l'eliminazione di un account Buyer (`PersonController.deleteProfile`) comporti anche la perdita dello storico ordini. Per un sistema in produzione con obblighi fiscali/contabili, questa scelta andrebbe rivalutata (es. soft-delete dell'utente o `SET NULL` sull'ordine mantenendo lo snapshot in `orders_has_products`), ma è considerata accettabile nell'ambito del presente progetto didattico/dimostrativo.

## Popolamento iniziale

Il file `V2__INSERT_TABLES.sql` fornisce dati di seed (utenti, prodotti, ecc.) per consentire l'avvio dell'applicazione con un dataset dimostrativo già pronto per test manuali e demo.
