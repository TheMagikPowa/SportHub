# Diagramma dei Flussi Principali — SportHub

Questo documento presenta almeno un flow chart per ciascuna categoria richiesta: **CRUD generico**, **azione utente (Buyer)** e **azione admin/staff**.

## 1. Flusso CRUD — Gestione Prodotto (Admin)

Esempio di flusso CRUD completo: creazione, validazione, servizio, repository, redirect, messaggio d'esito. Riferimento: `ProductAdminController` + `ProductService` + `ProductRepository`.

```mermaid
flowchart TD
    A["Admin: form 'Nuovo Prodotto'"] --> B["POST /admin/products/save<br/>(ProductDTO)"]
    B --> C{"Validazione dati<br/>(Bean Validation / binding)"}
    C -->|dati non validi| C1["Errore di binding<br/>-> ritorno al form con messaggi"]
    C -->|dati validi| D["ProductAdminController.saveProduct()"]
    D --> E["ProductService.createProduct(productDTO)"]
    E --> F["Costruzione entity Product<br/>(name, price, quantity, description,<br/>pGender, aCategory, category)"]
    F --> G["ProductRepository.save(product)"]
    G --> H[("MySQL: INSERT INTO products")]
    H --> I["redirect:/products"]
    I --> J["Store aggiornato (GET /products/store)<br/>mostra il nuovo prodotto"]

    subgraph UPDATE["Update"]
        U1["Admin: form modifica prodotto"] --> U2["POST /admin/products/update/{id}"]
        U2 --> U3["ProductService.updateProduct(id, dto)"]
        U3 --> U4{"Prodotto trovato?"}
        U4 -->|no| U5["Exception -> redirect /admin/products/edit/{id}?error"]
        U4 -->|sì| U6["Aggiorna campi + save()"]
        U6 --> U7["redirect:/products"]
    end

    subgraph DELETE["Delete"]
        D1["Admin: click 'Elimina'"] --> D2["POST /admin/products/delete/{id}"]
        D2 --> D3["ProductService.deleteProduct(id)"]
        D3 --> D4{"existsById(id)?"}
        D4 -->|no| D5["Exception gestita (log)"]
        D4 -->|sì| D6["ProductRepository.deleteById(id)"]
        D6 --> D7[("MySQL: DELETE")]
        D7 --> D8["redirect:/products"]
    end
```

## 2. Azione Utente (Buyer) — Aggiunta al Carrello e Checkout

```mermaid
flowchart TD
    A["Buyer: pagina prodotto"] --> B["POST /cart/add<br/>(productId, quantity)"]
    B --> C["CartController.addProductToCart()"]
    C --> D["Recupero buyerId da Principal<br/>(email autenticata -> PersonRepository)"]
    D --> E["CartService.addProductToCart(buyerId, productId, quantity)"]
    E --> F["getOrCreateCart(buyerId)<br/>(crea Cart se non esiste)"]
    F --> G{"Prodotto già presente<br/>nel carrello?"}
    G -->|sì| H["Aggiorna quantity esistente<br/>CartHasProductsRepository.save()"]
    G -->|no| I["Crea nuova riga CartHasProducts<br/>CartHasProductsRepository.save()"]
    H --> J["redirect:/cart<br/>flash: 'Product successfully added'"]
    I --> J
    J --> K["GET /cart<br/>mostra items + totale calcolato"]
    K --> L["Buyer: click 'Checkout'"]
    L --> M["GET /cart/checkout<br/>mostra riepilogo carrello"]
    M --> N["Buyer inserisce indirizzo,<br/>destinatario, spedizione, pagamento"]
    N --> O["POST /cart/checkout"]
    O --> P["OrderService.createOrderFromCart(...)"]
    P --> Q{"Carrello vuoto?"}
    Q -->|sì| Q1["Exception 'empty cart'<br/>-> redirect /cart/checkout con errore"]
    Q -->|no| R["Calcolo totale (somma item + costo spedizione)"]
    R --> S["Creazione Order (status=PROCESSING)"]
    S --> T["Creazione OrderHasProducts per ogni item<br/>(snapshot nome/prezzo)"]
    T --> U["cartService.clearCart(buyerId)"]
    U --> V["redirect:/cart<br/>flash: 'Order placed successfully!'"]
```

## 3. Azione Admin/Staff — Gestione Q&A (risposta e moderazione)

```mermaid
flowchart TD
    A["Buyer: apre richiesta assistenza"] --> B["GET /qA/create-question"]
    B --> C["POST /qA/save-post<br/>(QuestionQA: title, message)"]
    C --> D["QuestionQAService.saveQuestion(question, email)"]
    D --> E["Associa Buyer autore, status=OPEN, createTime=now"]
    E --> F[("MySQL: INSERT INTO questions_qa")]
    F --> G["redirect:/qA<br/>flash: 'Request submitted successfully!'"]

    G --> H["Staff/Admin: consulta bacheca Q&A<br/>GET /qA"]
    H --> I{"sec:authorize<br/>hasAnyRole('STAFF','ADMIN')?"}
    I -->|no, Buyer semplice| I1["Vede solo domande/risposte,<br/>nessun pannello di risposta"]
    I -->|sì| J["Vede pannello risposta per ogni domanda"]
    J --> K["POST /qA/{questionId}/answer<br/>(text)"]
    K --> L{"@PreAuthorize<br/>hasAnyRole('STAFF','ADMIN')"}
    L -->|non autorizzato| L1["403 -> /accesso-negato"]
    L -->|autorizzato| M["QuestionQAService.addAnswerToQuestion(<br/>questionId, text, staffEmail)"]
    M --> N["Crea MessageQA (staff, question, text, createdAt)"]
    N --> O[("MySQL: INSERT INTO messages_qa")]
    O --> P["redirect:/qA<br/>risposta visibile nel thread"]

    H --> Q["Staff/Admin: elimina domanda"]
    Q --> R["POST /qA/delete-question<br/>(idQuestion)"]
    R --> S{"@PreAuthorize / SecurityConfig<br/>hasAnyRole('ADMIN','STAFF')"}
    S -->|non autorizzato| S1["403 -> /accesso-negato"]
    S -->|autorizzato| T["QuestionQAService.delete(id)"]
    T --> U[("MySQL: DELETE (CASCADE su messages_qa)")]
    U --> V["redirect:/qA<br/>model.msg = 'Question eliminated'"]
```

## 4. Flusso trasversale — Moderazione Hub Eventi (proprietario vs Admin/Staff)

Esempio aggiuntivo che mostra come una stessa azione (cancellazione) sia autorizzata sia per il Buyer proprietario, sia per ruoli privilegiati, tramite verifica applicativa nel service.

```mermaid
flowchart TD
    A["Utente autenticato: click 'Elimina post'<br/>nella pagina Hub"] --> B["POST /hub/delete-post (idPost)"]
    B --> C["HubController.deletePost()"]
    C --> D["EventService.deleteIfAllowed(id, authentication)"]
    D --> E["Recupero Event by id"]
    E --> F{"authentication ha ROLE_ADMIN<br/>o ROLE_STAFF?"}
    F -->|sì| H["Elimina evento<br/>(cascade su EventAnswer)"]
    F -->|no| G{"authentication.email ==<br/>event.buyer.email?"}
    G -->|sì, è il proprietario| H
    G -->|no| I["AccessDeniedException<br/>'Non puoi cancellare questo post'"]
    H --> J[("MySQL: DELETE FROM events<br/>(+ CASCADE events_answers)")]
    J --> K["redirect:/hub"]
    I --> L["Gestita da GlobalExceptionHandler /<br/>pagina di errore"]
```
