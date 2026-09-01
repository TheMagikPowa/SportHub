# Documento di Analisi Iniziale — SportHub

> Fase del ciclo di vita: **Progettazione**

## 1. Descrizione del progetto

**SportHub** è una web application e-commerce full-stack dedicata alla vendita di articoli sportivi, sviluppata in **Java 21 / Spring Boot 3** con motore di template **Thymeleaf** (rendering server-side, architettura MVC monolitica) e database relazionale **MySQL**, gestito tramite migrazioni **Flyway**.

Oltre alla componente e-commerce (catalogo prodotti, carrello, checkout, storico ordini), la piattaforma integra due moduli community/support:

- **Hub Eventi**: una bacheca dove gli utenti registrati (Buyer) possono pubblicare eventi sportivi (es. uscite in bici, tornei, escursioni) e commentarli;
- **Q&A / Assistenza**: un sistema di domande e risposte in cui i Buyer aprono richieste che vengono evase dallo Staff o dall'Admin.

L'applicazione gestisce tre ruoli utente (**BUYER**, **STAFF**, **ADMIN**) con permessi differenziati, autenticazione basata su sessione (Spring Security, form login) e password hashate con BCrypt.

## 2. Obiettivo del progetto

Realizzare una piattaforma e-commerce verticale per il settore sportivo che permetta:

- alla clientela finale (Buyer) di **navigare il catalogo, acquistare prodotti e interagire con la community**;
- allo Staff/Admin di **gestire il catalogo prodotti, evadere le richieste di assistenza e amministrare gli ordini**;
- di dimostrare l'applicazione di un'architettura **MVC a livelli** (Controller → Service → Repository → Entity/DB), con separazione delle responsabilità, sicurezza basata su ruoli e persistenza gestita tramite JPA/Hibernate su schema versionato con Flyway.

## 3. Target utenti

| Ruolo | Descrizione | Modalità di accesso |
|---|---|---|
| **Visitatore (guest)** | Utente non autenticato | Naviga home, store, dettaglio prodotto, pagina contatti, registrazione, login |
| **Buyer** | Cliente registrato che acquista prodotti | Login con email/password; ruolo assegnato di default in fase di registrazione |
| **Staff** | Operatore che gestisce l'assistenza clienti (Q&A) | Account creato/gestito lato back-office; risponde alle domande dei Buyer |
| **Admin** | Amministratore di sistema | Gestisce catalogo prodotti, ordini, moderazione contenuti (Hub, Q&A) |

## 4. Feature principali

### Area pubblica
- Home page con presentazione del servizio
- Catalogo prodotti (`/products/store`) consultabile senza autenticazione
- Ricerca e filtro prodotti per nome, genere (M/F/Unisex) e fascia d'età (Kids/Adults)
- Dettaglio prodotto (`/products/{id}`)
- Pagina "Contatti"
- Registrazione nuovo account (`/register/show-registration`)
- Login (`/login`)

### Area utente autenticato (Buyer)
- **Dashboard personale** con collegamenti rapidi alle sezioni riservate
- **Gestione profilo**: visualizzazione, modifica ed eliminazione account
- **Carrello**: aggiunta/rimozione prodotti, calcolo totale
- **Checkout**: inserimento indirizzo/destinatario, scelta spedizione e pagamento, generazione ordine
- **Storico ordini**: lista ordini effettuati e dettaglio singolo ordine
- **Hub Eventi**: creazione post evento, consultazione bacheca, risposta ai post, cancellazione dei propri post
- **Q&A**: apertura di richieste di assistenza, consultazione dello storico richieste

### Area Staff / Admin
- **Gestione Q&A**: risposta alle domande dei Buyer, eliminazione domande (Staff + Admin)
- **Gestione catalogo prodotti** (Admin): creazione, modifica ed eliminazione prodotti
- **Gestione ordini**: aggiornamento stato ordine (`PROCESSING → SHIPPED → DELIVERED`)
- **Moderazione Hub**: eliminazione post di terzi (Admin/Staff), oltre alla cancellazione dei propri post da parte del Buyer proprietario

## 5. Feature opzionali / possibili estensioni future

- Gestione avanzata della rubrica indirizzi (multi-indirizzo, indirizzo predefinito) — attualmente presente a livello di controller/servizio ma non ancora esposta in una view dedicata completa
- Sistema di notifiche email (conferma ordine, risposta Q&A, nuovo commento evento)
- Pannello amministrativo dedicato (dashboard Admin separata, reportistica vendite)
- Gestione stock automatica (decremento quantità prodotto dopo l'acquisto)
- Recensioni e valutazioni prodotto
- Ricerca full-text avanzata e paginazione dei risultati
- Gestione categoria evento con calendario/iscrizione partecipanti (oltre al semplice post/commento)

## 6. Flusso principale (User Journey sintetico)

```
1. Visitatore accede alla Home
2. Consulta lo Store / cerca un prodotto
3. Se non registrato → Registrazione → Login
4. Da autenticato: aggiunge prodotti al Carrello
5. Procede al Checkout (indirizzo, spedizione, pagamento)
6. Ordine creato → Carrello svuotato → Storico Ordini aggiornato
7. (Facoltativo) Il Buyer apre un ticket Q&A o pubblica un evento nell'Hub
8. Staff/Admin evade il ticket Q&A o modera i contenuti dell'Hub
9. Admin gestisce il catalogo e aggiorna lo stato degli ordini
```

Il dettaglio dei flussi (autenticazione, CRUD, azioni utente/admin) è descritto nei documenti dedicati (`docs/ui-flows.md` e diagrammi di flusso).

## 7. Vincoli tecnici e scelte di base

- **Persistenza**: Spring Data JPA + Hibernate, schema DB versionato con **Flyway** (`ddl-auto=validate`: lo schema è sorgente di verità, non generato da Hibernate)
- **Sicurezza**: Spring Security con `DaoAuthenticationProvider`, login basato su **email**, password con **BCrypt** (strength 12), CSRF attivo, Content-Security-Policy configurata
- **Presentazione**: Thymeleaf server-side rendering (no SPA/API REST esposta al momento)
- **Mapping dati**: pattern DTO + Converter per disaccoppiare Entity JPA da dati esposti/ricevuti dalle view
