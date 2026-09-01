# SportHub — Documentazione di Progetto

Indice della documentazione tecnica e di progettazione del progetto **SportHub** (Spring Boot 3 + Thymeleaf + MySQL/Flyway).

## Documenti di progettazione (fase iniziale)

| Documento | Contenuto |
|---|---|
| [01 — Analisi Iniziale](01-analisi-iniziale.md) | Descrizione progetto, obiettivo, target utenti, feature principali/opzionali, flusso principale |
| [02 — ER Diagram](02-er-diagram.md) | Entità, attributi, PK, relazioni e cardinalità (Mermaid), coerenza con annotazioni JPA |
| [03 — Architettura Backend](03-architettura-backend.md) | Diagramma dei package (Controller → Service → Repository → Model → DB, Config/Security) |
| [04 — Navigazione Frontend](04-navigazione-frontend.md) | Mappa delle pagine, differenze User/Admin, template riutilizzabili |
| [05 — Flusso di Autenticazione](05-flusso-autenticazione.md) | Registrazione, login, caricamento ruolo, redirect, protezione pagine |
| [06 — Flussi Principali](06-flussi-principali.md) | Flow chart per CRUD, azione utente, azione admin |

## Documentazione tecnica (`docs/`)

| Documento | Contenuto |
|---|---|
| [architecture.md](architecture.md) | Scelte architetturali e motivazioni |
| [api-routing.md](api-routing.md) | Rotte MVC (controller) e pagine servite |
| [database.md](database.md) | Descrizione entità e motivazioni progettuali del modello dati |
| [ui-flows.md](ui-flows.md) | Definizione sintetica dei flussi UI principali |
| [release-notes.md](release-notes.md) | Funzionalità implementate e note di rilascio |

## Stack tecnologico di riferimento

- **Linguaggio/Framework**: Java 21, Spring Boot 3 (Spring MVC, Spring Data JPA, Spring Security)
- **Template engine**: Thymeleaf (+ `thymeleaf-extras-springsecurity6`)
- **Database**: MySQL, migrazioni gestite con **Flyway**
- **Build**: Maven (`pom.xml`, wrapper `mvnw`)
- **Utility**: Lombok (`@Data`, `@Getter/@Setter`, `@NoArgsConstructor`, ecc.)
- **Sicurezza**: form login basato su sessione, password hashate con BCrypt (strength 12)

## Come consultare questa documentazione

Si consiglia di leggere i documenti nell'ordine dell'indice: dall'analisi di alto livello (cosa fa il progetto e per chi), passando per i modelli dati e architetturali, fino ai flussi applicativi di dettaglio.
