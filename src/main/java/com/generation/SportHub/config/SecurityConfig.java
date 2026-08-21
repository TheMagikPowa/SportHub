package com.generation.SportHub.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.Map;


/**
 * Configurazione centrale della sicurezza dell'applicazione.
 *
 * Questa classe dice a Spring Security:
 * - quali URL sono pubblici e quali richiedono autenticazione;
 * - quali ruoli possono usare quali pagine o operazioni;
 * - come effettuare il login e il logout;
 * - come leggere gli utenti dal database;
 * - come confrontare le password salvate nel sistema.
 */


//Abilita la sicurezza a livello di metodo, consentendo di utilizzare 
// Abilita la sicurezza anche a livello di metodo.
// Questo significa che in altre classi potrai usare annotazioni come @PreAuthorize
// per bloccare o permettere l'esecuzione di un metodo in base al ruolo dell'utente.
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Costruisce la catena principale di regole della sicurezza HTTP.
     *
     * Questo è il metodo più importante della classe. Qui decidiamo:
     * - quali indirizzi web sono aperti a tutti;
     * - quali indirizzi richiedono un utente loggato;
     * - quali ruoli possono eseguire certe operazioni;
     * - cosa succede quando l'utente fa login, logout o sbaglia permessi.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationProvider authenticationProvider
    ) throws Exception {
        // Colleghiamo il provider personalizzato alla sicurezza web.
        // Questo serve per dire a Spring: "quando qualcuno fa login, usa la nostra logica".
        // La nostra logica recupera l'utente dal database e confronta la password nel modo giusto.
        http
                .authenticationProvider(authenticationProvider)

                // Le regole più specifiche devono stare prima di anyRequest().
                // Esempio: se una pagina è visibile solo agli admin, questa regola va scritta qui.
                // Non basta nascondere un bottone nell'interfaccia: la vera protezione deve stare lato server.
                .authorizeHttpRequests(authorize -> authorize
                        // Queste risorse sono pubbliche.
                        // Significa che anche un visitatore non registrato può aprirle.
                        // Serve per la pagina di login, la pagina di errore e i file statici come CSS e JS.
                        .requestMatchers(
                                "/", "/login", "/register", "/accesso-negato", "/homecss/**","/error",
                                "/home/**", "/login/**", "/register/**", "/res/**", "/css/**", "/js/**",
                                "/favicon.ico"
                        )
                        .permitAll()

                        // Queste operazioni cambiano dati importanti.
                        // Per questo le riserviamo solo a chi ha il ruolo ADMIN.
                        // In generale: "ADMIN" è l'utente con i permessi più ampi.
                        .requestMatchers(
                                "/clienti/nuovo", "/clienti/*/modifica",
                                "/guide/nuova", "/guide/*/modifica",
                                "/tour/nuovo", "/tour/*/modifica",
                                "/partenze/nuova", "/partenze/*/modifica"
                        ).hasRole("ADMIN")

                        // Le richieste POST di solito servono per creare, modificare o cancellare dati.
                        // Quindi anche queste sono riservate all'admin.
                        .requestMatchers(HttpMethod.POST,
                                "/clienti/**", "/guide/**", "/tour/**", "/partenze/**"
                        ).hasRole("ADMIN")

                        // Le prenotazioni possono essere gestite sia dall'admin sia dall'operatore.
                        // "hasAnyRole" significa: basta avere uno dei ruoli elencati.
                        .requestMatchers("/prenotazioni/**")
                        .hasAnyRole("ADMIN", "OPERATORE")

                        // Tutto il resto non è pubblico.
                        // Se una richiesta arriva qui, Spring controlla solo che l'utente sia loggato.
                        .anyRequest().authenticated()
                )

                // Login classico con pagina HTML e sessione lato server.
                // Il browser invia username e password una sola volta,
                // poi Spring crea una sessione e non chiede più di reinserire le credenziali a ogni pagina.
                .formLogin(form -> form
                        // Qui diciamo a Spring di usare la nostra pagina di login personalizzata.
                        // Se non la indicassimo, Spring userebbe una schermata di login predefinita.
                        .loginPage("/login")
                        // Dopo un login corretto, l'utente viene portato alla home.
                        // Il secondo parametro true significa: vai lì sempre, anche se l'utente aveva provato
                        // prima ad aprire una pagina diversa.
                        .defaultSuccessUrl("/", true)
                        // Se username o password sono sbagliati, torniamo alla login con un parametro error.
                        // La pagina può usare quel parametro per mostrare un messaggio all'utente.
                        .failureUrl("/login?error")
                        .permitAll()
                )

                // Logout significa chiudere la sessione dell'utente.
                // Qui diciamo a Spring di pulire tutto ciò che identifica l'utente loggato.
                .logout(logout -> logout
                        // Endpoint che riceve la richiesta di logout.
                        // Quando il browser chiama /logout, Spring esegue questa sequenza di pulizia.
                        .logoutUrl("/logout")
                        // Dopo il logout, l'utente torna alla login con un parametro logout.
                        // La pagina può usare questo parametro per mostrare un messaggio tipo "sei uscito correttamente".
                        .logoutSuccessUrl("/login?logout")
                        // Distrugge la sessione lato server.
                        // Questo è importante perché la vecchia sessione non deve restare valida.
                        .invalidateHttpSession(true)
                        // Rimuove l'informazione di autenticazione associata all'utente.
                        // In pratica Spring "dimentica" chi era loggato.
                        .clearAuthentication(true)
                        // Elimina il cookie di sessione dal browser.
                        // Serve a evitare che il browser continui a usare una sessione vecchia.
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                // Se un utente è loggato ma prova ad aprire una pagina per cui non ha i permessi,
                // non riceve una pagina generica di errore: viene mandato alla pagina "accesso negato".
                .exceptionHandling(exceptions -> exceptions
                        .accessDeniedPage("/accesso-negato")
                )

                // Spring cambia l'identificatore della sessione dopo il login.
                // Questo riduce un attacco chiamato session fixation, cioè il riuso di un ID di sessione noto.
                .sessionManagement(session -> session
                        .sessionFixation(fixation -> fixation.changeSessionId())
                )

                // CSRF resta attivo.
                // Questo protegge i form: impedisce che un sito esterno faccia inviare richieste al posto dell'utente.
                // È molto importante quando l'app usa sessioni e form HTML.
                .csrf(Customizer.withDefaults())

                // Content Security Policy: è una regola di sicurezza del browser.
                // Dice da quali sorgenti il browser può caricare script, stili, immagini e altri contenuti.
                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp.policyDirectives(
                                // 'self' significa "solo da questo stesso sito".
                                // In pratica, blocchiamo contenuti caricati da siti esterni non autorizzati.
                                "default-src 'self'; " +
                                "script-src 'self'; " +
                                "style-src 'self'; " +
                                "img-src 'self' data:; " +
                                "object-src 'none'; " +
                                "base-uri 'self'; " +
                                "frame-ancestors 'none'; " +
                                "form-action 'self'"
                        ))
                );

        return http.build();
    }

        /**
         * Crea il componente che controlla se username e password sono corretti.
         *
         * Quando un utente fa login, Spring non controlla da solo il database.
         * Delegiamo questo compito a DaoAuthenticationProvider, che:
         * - recupera l'utente con JpaUserDetailsService;
         * - confronta la password digitata con quella salvata;
         * - decide se il login può riuscire oppure no.
         */
    /* @Bean
    public AuthenticationProvider authenticationProvider(
            JpaUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
                // Creiamo il provider standard per login con username e password.
                // Il costruttore riceve il servizio che sa come trovare gli utenti nel database.
        DaoAuthenticationProvider provider = 
        new DaoAuthenticationProvider(userDetailsService);
                // Diciamo al provider come deve confrontare la password inserita dall'utente
                // con quella salvata nel database.
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

        /**
         * Definisce come Spring deve salvare e verificare le password.
         *
         * Le password non vengono salvate in chiaro, ma come hash.
         * Un hash è una trasformazione non reversibile: serve per verificare la password,
         * non per recuperarla in testo leggibile.
         *
         * Qui usiamo BCrypt, che è un algoritmo adatto per le password perché è lento apposta.
         * Questo rende più difficile fare attacchi automatici a forza bruta.
         */
    @Bean
    public PasswordEncoder passwordEncoder() {
                // DelegatingPasswordEncoder può gestire più algoritmi diversi.
                // La mappa dice quali codificatori conosce l'app.
        Map<String, PasswordEncoder> encoders = new HashMap<>();
                // BCrypt con strength 12: più il numero sale, più il controllo della password costa tempo.
                // 12 è un compromesso comune tra sicurezza e prestazioni.
                //12 significa che l'algoritmo BCrypt esegue 2^12 (4096) iterazioni
                // di hashing, rendendo più difficile per un attaccante indovinare la password.
        encoders.put("bcrypt", new BCryptPasswordEncoder(12));
                // DelegatingPasswordEncoder usa {bcrypt} come prefisso negli hash.
                // Questo è utile se un domani si volesse cambiare algoritmo senza rompere gli hash già esistenti.
        return new DelegatingPasswordEncoder("bcrypt", encoders);
    }
} 

