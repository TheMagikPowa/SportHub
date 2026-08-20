package com.generation.SportHub.security;


import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.generation.SportHub.entity.Person;
import com.generation.SportHub.repository.PersonRepository;

/**
 * Adattatore tra il nostro modello JPA {@link Utente} 
 * e il contratto
 * {@link UserDetailsService} richiesto da Spring Security.
 *
 * Da dove arriva {@code UserDetailsService}:
 * - è un'interfaccia del framework Spring Security;
 * - si trova nel package {@code org.springframework.security.core.userdetails};
 * - non è definita nel progetto: viene fornita dalla dipendenza Spring Security.
 *
 * In questa applicazione, il bean {@code JpaUserDetailsService} è la nostra implementazione
 * concreta di quell'interfaccia. Viene iniettata nel {@code DaoAuthenticationProvider}
 * configurato in SecurityConfig, quindi viene chiamata automaticamente durante il login.
 *
 * Questa classe implementa {@code UserDetailsService} perché Spring Security,
 * durante il login, ha bisogno di un componente che sappia partire da uno username
 * e trasformarlo in un oggetto {@link UserDetails} comprensibile al sistema di sicurezza.
 *
 * In pratica questa classe fa da ponte tra due mondi:
 * - il database, dove i dati dell'utente sono salvati come entity JPA;
 * - Spring Security, che non lavora direttamente con le entity ma con il contratto
 *   {@code UserDetails}.
 *
 * La scelta di non restituire direttamente {@link Utente} come principal serve a tenere
 * separato il modello di persistenza dalla logica di autenticazione. In questo modo il
 * codice di sicurezza rimane più pulito, più riutilizzabile e meno dipendente dalla struttura
 * del database.
 */
@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

    
    private final PersonRepository personRepository;

    /**
     * Cerca un utente nel database partendo dallo username e lo trasforma nel formato
     * richiesto da Spring Security.
     *
     * Spring chiama questo metodo durante il login. Qui succede questo:
     * - il repository cerca l'utente nella tabella degli utenti, senza distinguere maiuscole/minuscole;
     * - se l'utente non esiste, viene lanciata una UsernameNotFoundException e il login fallisce;
     * - se l'utente esiste, costruiamo un oggetto UserDetails con i dati che Spring Security usa
     *   per autenticare la persona e per capire quali ruoli ha.
     *
     * I dati restituiti sono:
     * - username: il nome con cui l'utente si autentica;
     * - password hash: la password salvata nel database, già codificata con BCrypt;
     * - roles: il ruolo applicativo dell'utente, convertito nel formato che Spring usa internamente;
     * - disabled: indica se l'account è attivo oppure no.
     *
     * @param username lo username inserito nel form di login
     * @return un oggetto UserDetails pronto per essere usato da Spring Security
     * @throws UsernameNotFoundException se non esiste alcun utente con quello username
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Person utente = personRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("Credenziali non valide"));

        return User.withUsername(utente.getEmail())
                .password(utente.getPassword())
                .roles(utente.getRole().name())
                .build();
    }
}


