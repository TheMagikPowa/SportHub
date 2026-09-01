# Flusso di Autenticazione — SportHub

Il sistema di autenticazione si basa su **Spring Security** con form-login classico (sessione HTTP, cookie `JSESSIONID`), `DaoAuthenticationProvider` e `UserDetailsService` custom che legge gli utenti dal database tramite JPA.

## 1. Registrazione

```mermaid
sequenceDiagram
    actor U as Utente (Guest)
    participant C as RegistrationController
    participant S as PersonService
    participant PW as PasswordEncoder (BCrypt)
    participant R as PersonRepository
    participant DB as MySQL

    U->>C: GET /register/show-registration
    C-->>U: form registrazione (register/register.html, PersonDTO vuoto)
    U->>C: POST /register/save-registration (PersonDTO)
    C->>S: createNewPerson(personDTO)
    S->>R: existsByEmailIgnoreCase(email)
    R->>DB: SELECT ...
    alt email già registrata
        S-->>C: Exception("Utenza già presente")
        C-->>U: torna a register.html con messaggioErrore
    else email libera
        S->>PW: encode(password)
        S->>S: crea Buyer (role=BUYER, active=true)
        S->>R: save(buyer)
        R->>DB: INSERT INTO user / people / buyers
        S-->>C: Person salvata
        C-->>U: redirect:/login
    end
```

Note:
- Il ruolo di default è **BUYER** (assegnato sia esplicitamente in `PersonService.createNewPerson`, sia come fallback in `Person.initializeRole()` via `@PrePersist`).
- La password non viene mai salvata in chiaro: `PasswordEncoder` (Bcrypt strength 12, tramite `DelegatingPasswordEncoder`) la codifica prima del salvataggio.
- Ogni nuovo Buyer viene creato con `active = true`.

## 2. Login e caricamento del ruolo

```mermaid
sequenceDiagram
    actor U as Utente
    participant SF as Spring Security Filter Chain
    participant AP as DaoAuthenticationProvider
    participant UDS as JpaUserDetailsService
    participant R as PersonRepository
    participant DB as MySQL

    U->>SF: GET /login
    SF-->>U: login/login.html (form: email + password)
    U->>SF: POST /login (email, password)
    SF->>AP: authenticate(email, password)
    AP->>UDS: loadUserByUsername(email)
    UDS->>R: findByEmailIgnoreCase(email)
    R->>DB: SELECT * FROM user JOIN people ...
    alt utente non trovato
        UDS-->>AP: UsernameNotFoundException
        AP-->>SF: AuthenticationException
        SF-->>U: redirect /login?error
    else utente trovato
        UDS-->>AP: UserDetails(email, passwordHash, ROLE_<role>)
        AP->>AP: confronta password hash (BCrypt)
        alt password errata
            AP-->>SF: BadCredentialsException
            SF-->>U: redirect /login?error
        else password corretta
            AP-->>SF: Authentication OK
            SF->>SF: crea sessione (sessionFixation.changeSessionId)
            SF-->>U: redirect "/" (defaultSuccessUrl)
        end
    end
```

Punti chiave dell'implementazione (`SecurityConfig` + `JpaUserDetailsService`):

- **Username parameter**: il form di login usa il campo `email` (`.usernameParameter("email")`) come identificativo, non uno `username` separato.
- **Caricamento ruolo**: `JpaUserDetailsService.loadUserByUsername` recupera l'entity `Person` e costruisce un `UserDetails` di Spring Security con `.roles(person.getRole().name())`, che Spring traduce internamente in un'autorità `ROLE_<NOME>` (es. `ROLE_BUYER`, `ROLE_STAFF`, `ROLE_ADMIN`).
- **Password matching**: `DaoAuthenticationProvider` usa il `PasswordEncoder` (BCrypt) per confrontare la password in chiaro inviata dal form con l'hash salvato in `user.password`.
- **Sessione**: alla login riuscita, Spring Security rigenera l'id di sessione (`sessionFixation().changeSessionId()`) per prevenire session fixation.

## 3. Redirect in base al ruolo

Nel progetto attuale **non esiste un redirect differenziato per ruolo** dopo il login: `defaultSuccessUrl("/", true)` riporta sempre alla home, indipendentemente dal ruolo (`BUYER`, `STAFF`, `ADMIN`). La differenziazione avviene **a valle**, a livello di singola pagina/azione:

- la navbar e le pagine mostrano/nascondono blocchi con `sec:authorize="hasAnyRole('STAFF','ADMIN')"` (es. pannello di risposta nel Q&A);
- le azioni sensibili sono protette a livello di endpoint tramite `@PreAuthorize` (es. `QAController.addAnswer`, `QAController.deleteQuestion`) o tramite regole dichiarate in `SecurityConfig` (`.requestMatchers(...).hasRole("ADMIN")` per `/post/delete`, `.hasAnyRole("ADMIN","STAFF")` per `POST /qA/delete-question`);
- alcune autorizzazioni sono verificate **programmaticamente nel service** (es. `EventService.deleteIfAllowed`, che consente la cancellazione di un evento solo al proprietario oppure a Staff/Admin).

## 4. Protezione delle pagine

```mermaid
flowchart TD
    REQ["Richiesta HTTP in ingresso"] --> CHECK{"URL in whitelist<br/>permitAll?"}
    CHECK -->|sì<br/>'/','/login','/register/**','/store/**','/product/**',<br/>'/homecss/**','/error/**','/res/**', ecc.| ALLOW["Accesso consentito<br/>(anche senza autenticazione)"]
    CHECK -->|no| AUTH{"Utente autenticato?"}
    AUTH -->|no| LOGINPAGE["redirect -> /login"]
    AUTH -->|sì| ROLECHECK{"Regola specifica per il path?<br/>(hasRole/hasAnyRole)"}
    ROLECHECK -->|nessuna regola specifica| ANYAUTH["anyRequest().authenticated()<br/>-> accesso consentito a qualsiasi utente loggato"]
    ROLECHECK -->|regola presente e soddisfatta| ALLOWROLE["Accesso consentito"]
    ROLECHECK -->|regola presente, non soddisfatta| DENY["403 -> /accesso-negato<br/>(AuthenticationController.accessDenied)"]
```

Regole esplicite definite in `SecurityConfig.securityFilterChain`:

| Pattern URL / metodo | Regola |
|---|---|
| `/`, `/login`, `/accesso-negato`, `/homecss/**`, `/error`, `/home/index`, `/store/**`, `/login/**`, `/error/**`, `/product/**`, `/register/**`, `/resources/**`, `/css/**`, `/js/**`, `/res/**`, `/favicon.ico` | `permitAll()` — pubblici |
| `/post/delete` | `hasRole("ADMIN")` |
| `POST /qA/delete-question` | `hasAnyRole("ADMIN","STAFF")` |
| qualsiasi altra richiesta | `authenticated()` — richiede login, nessun vincolo di ruolo aggiuntivo a livello di filtro (i vincoli fini sono nei controller via `@PreAuthorize` o logica di service) |

Altre misure di sicurezza attive:
- **CSRF** abilitato (default Spring Security).
- **Content-Security-Policy** custom (`default-src 'self'`, `script-src 'self' 'unsafe-inline'`, whitelisting di font/stili esterni).
- **Logout**: `POST /logout` invalida la sessione, cancella l'autenticazione e il cookie `JSESSIONID`, redirige a `/login?logout`.
- **Accesso negato**: `.exceptionHandling().accessDeniedPage("/access-denied")` gestito da `AuthenticationController.accessDenied()` → vista `error/errorPage.html`.
