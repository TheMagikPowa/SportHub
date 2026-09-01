# ER Diagram — SportHub

Diagramma Entità-Relazione ricavato dallo schema Flyway (`V1__CREATE_TABLES.sql`) e verificato per coerenza con le annotazioni JPA presenti nel package `entity`. Notazione: **Mermaid `erDiagram`**.

> Nota sull'ereditarietà: `User → Person → Buyer` è mappata con strategia JPA `@Inheritance(strategy = InheritanceType.JOINED)` (table-per-subclass). A livello relazionale corrisponde a tre tabelle collegate 1:1 da chiave primaria condivisa (PK = FK verso la superclasse): `user` ← `people` ← `buyers`.

```mermaid
erDiagram

    USER ||--|| PEOPLE : "estende (JOINED)"
    PEOPLE ||--o| BUYERS : "estende (JOINED)"

    BUYERS ||--o{ EVENTS : "pubblica"
    BUYERS ||--o{ EVENTS_ANSWERS : "scrive"
    BUYERS ||--o{ ADDRESSES : "possiede"
    BUYERS ||--o{ QUESTIONS_QA : "apre"
    BUYERS ||--o| CART : "ha"
    BUYERS ||--o{ ORDERS : "effettua"

    PEOPLE ||--o{ MESSAGES_QA : "risponde (staff)"

    EVENTS ||--o{ EVENTS_ANSWERS : "riceve commenti"

    QUESTIONS_QA ||--o{ MESSAGES_QA : "riceve risposte"

    CART ||--o{ CART_HAS_PRODUCTS : "contiene"
    PRODUCTS ||--o{ CART_HAS_PRODUCTS : "è nel carrello"

    ORDERS ||--o{ ORDERS_HAS_PRODUCTS : "contiene"
    PRODUCTS ||--o{ ORDERS_HAS_PRODUCTS : "è ordinato"

    USER {
        bigint id PK
        varchar email UK
        varchar password
        timestamp created_time
    }

    PEOPLE {
        bigint user_id PK "FK -> user.id"
        varchar username UK
        varchar name
        varchar surname
        date dob
        enum gender "M, F, OTHER"
        enum role "BUYER, STAFF, ADMIN"
    }

    BUYERS {
        bigint user_people_id PK "FK -> people.user_id"
        tinyint active
    }

    ADDRESSES {
        bigint id PK
        bigint buyers_people_user_id FK
        varchar country
        varchar province
        varchar street
        varchar street_number
        varchar postal_code
        bigint phone_number
    }

    EVENTS {
        bigint id PK
        bigint buyers_user_people_id FK "nullable"
        varchar title
        varchar text
        date event_date
        timestamp created_at
        enum category "EventType"
    }

    EVENTS_ANSWERS {
        bigint id PK
        bigint buyers_user_people_id FK
        bigint event_id FK
        varchar text
        timestamp created_at
    }

    QUESTIONS_QA {
        bigint id PK
        bigint buyers_people_user_id FK
        varchar title
        varchar message
        enum state "OPEN, CLOSED"
        timestamp created_at
    }

    MESSAGES_QA {
        bigint id PK
        bigint question_id FK
        bigint staff_people_user_id FK "nullable"
        varchar text
        timestamp created_at
    }

    CART {
        bigint id PK
        bigint buyers_people_user_id UK "FK -> buyers, MapsId"
        timestamp created_at
        timestamp modified_at
    }

    PRODUCTS {
        bigint id PK
        varchar name
        enum category "ProductCategory"
        enum gender "ProductGender: M,F,UNISEX"
        enum age_category "AgeCategory: KIDS,ADULTS"
        decimal price
        int quantity
        varchar description
    }

    CART_HAS_PRODUCTS {
        bigint id PK
        bigint cart_buyers_people_user_id FK
        bigint products_id FK
        int quantity
    }

    ORDERS {
        bigint id PK
        bigint buyers_people_user_id FK
        timestamp purchase_date
        int discount_percent
        enum status "PROCESSING, SHIPPED, DELIVERED"
        decimal total
    }

    ORDERS_HAS_PRODUCTS {
        bigint id PK
        bigint orders_id FK
        bigint products_id FK
        varchar name
        decimal unit_price
        decimal final_price
        int quantity
    }
```

## Elenco entità, attributi chiave e note

| Entità (tabella) | PK | Attributi principali | Note |
|---|---|---|---|
| `USER` (`user`) | `id` | email (unique), password, created_time | Radice della gerarchia JPA (`@Inheritance JOINED`) |
| `PEOPLE` (`people`) | `user_id` (= FK a `user.id`) | username (unique), name, surname, dob, gender, role | Estende `User`; `role` default `BUYER` via `@PrePersist` |
| `BUYERS` (`buyers`) | `user_people_id` (= FK a `people.user_id`) | active | Estende `Person`; unico ruolo che può acquistare, aprire Q&A, pubblicare eventi |
| `ADDRESSES` (`addresses`) | `id` | country, province, street, street_number, postal_code, phone_number | N:1 verso `Buyer` |
| `EVENTS` (`events`) | `id` | title, text, event_date, created_at, category (EventType) | N:1 verso `Buyer` (autore); FK nullable con `ON DELETE SET NULL` |
| `EVENTS_ANSWERS` (`events_answers`) | `id` | text, created_at | N:1 verso `Event` (cascade ALL + orphanRemoval lato `Event`) e N:1 verso `Buyer` |
| `QUESTIONS_QA` (`questions_qa`) | `id` | title, message, state (OPEN/CLOSED), created_at | N:1 verso `Buyer` (autore della domanda) |
| `MESSAGES_QA` (`messages_qa`) | `id` | text, created_at | N:1 verso `QuestionQA`; N:1 verso `Person` (staff che risponde, nullable) |
| `CART` (`cart`) | `id` (= FK a `buyers.user_people_id`, `@MapsId`) | created_at, modified_at | Relazione **1:1** con `Buyer` (id condiviso) |
| `PRODUCTS` (`products`) | `id` | name, category, gender, age_category, price, quantity, description | Catalogo, indipendente da Buyer |
| `CART_HAS_PRODUCTS` (`cart_has_products`) | `id` | quantity | Tabella di giunzione N:N tra `Cart` e `Product`, con attributo `quantity` |
| `ORDERS` (`orders`) | `id` | purchase_date, discount_percent, status, total | N:1 verso `Buyer` |
| `ORDERS_HAS_PRODUCTS` (`orders_has_products`) | `id` | name, unit_price, final_price, quantity | Tabella di giunzione N:N tra `Order` e `Product`, con snapshot di nome/prezzo al momento dell'ordine |

## Cardinalità delle relazioni

| Relazione | Cardinalità | Implementazione JPA |
|---|---|---|
| User → Person → Buyer | 1:1 (ereditarietà JOINED) | `@Inheritance(strategy = InheritanceType.JOINED)`, `@PrimaryKeyJoinColumn` |
| Buyer → Address | 1:N | `@OneToMany(mappedBy="buyer")` in `Buyer`, `@ManyToOne` in `Address` |
| Buyer → Event | 1:N | `@OneToMany(mappedBy="buyer")` in `Buyer`, `@ManyToOne` in `Event` |
| Event → EventAnswer | 1:N | `@OneToMany(mappedBy="event", cascade=ALL, orphanRemoval=true)` in `Event`, `@ManyToOne(FetchType.LAZY)` in `EventAnswer` |
| Buyer → EventAnswer | 1:N | `@OneToMany(mappedBy="buyer")` in `Buyer`, `@ManyToOne` in `EventAnswer` |
| Buyer → QuestionQA | 1:N | `@ManyToOne` in `QuestionQA` |
| QuestionQA → MessageQA | 1:N | `@OneToMany(mappedBy="question")` in `QuestionQA`, `@ManyToOne` in `MessageQA` |
| Person(staff) → MessageQA | 1:N | `@ManyToOne` in `MessageQA` (campo `staff`) |
| Buyer → Cart | 1:1 | `@OneToOne @MapsId @JoinColumn(unique=true)` in `Cart` |
| Cart ↔ Product | N:N (via `CartHasProducts`) | `CartHasProducts` con due `@ManyToOne` + colonna `quantity` |
| Buyer → Order | 1:N | `@ManyToOne` in `Order` |
| Order ↔ Product | N:N (via `OrderHasProducts`) | `OrderHasProducts` con due `@ManyToOne` + snapshot prezzo/nome |

## Coerenza codice ↔ schema DB

- Tutte le FK definite in `V1__CREATE_TABLES.sql` trovano corrispondenza in una `@JoinColumn` lato entity (nomi colonna coincidenti, es. `buyers_people_user_id`, `cart_buyers_people_user_id`).
- Le policy `ON DELETE` / `ON UPDATE` (CASCADE, SET NULL) definite in SQL sono gestite a livello di database; a livello applicativo `Event.eventAnswers` replica il comportamento CASCADE tramite `cascade = CascadeType.ALL, orphanRemoval = true`.
- Gli `enum` Java (`Role`, `PersonGender`, `EventType`, `ProductCategory`, `ProductGender`, `AgeCategory`, `OrderStatus`, `QuestionStatus`) sono mappati con `@Enumerated(EnumType.STRING)` e corrispondono 1:1 ai valori `ENUM(...)` definiti in SQL.
- `spring.jpa.hibernate.ddl-auto=validate`: Hibernate **non genera** lo schema, si limita a validarlo contro le entity — lo schema versionato via Flyway è la fonte di verità.
- Nota di manutenzione: l'entità `EventBoard` è presente nel codice ma **commentata/non attiva** (tabella `event_board` non creata in SQL) — non è quindi parte del modello dati effettivo.
