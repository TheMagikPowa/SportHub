package com.generation.SportHub.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Espone soltanto le view di login e accesso negato.
 * L'elaborazione delle credenziali POST /login e il POST /logout sono gestiti
 * direttamente dai filtri di Spring Security, non da metodi controller custom.
 */
@Controller

public class AuthenticationController {

   @GetMapping("/")
    public String home() {
        return "home/index";
    }

    @GetMapping("/login")
    public String login() {
        return "login/login";
    }

   

    /**
     * Mostra la pagina di accesso negato.
     * <p>
     * Viene invocato da Spring Security quando un utente autenticato tenta di
     * accedere a una risorsa per cui non dispone dei permessi necessari.
     * La configurazione dell'URL è definita in {@code SecurityConfig} tramite
     * {@code .exceptionHandling().accessDeniedPage("/accesso-negato")}.
     * </p>
     *
     * @return il nome logico della view Thymeleaf {@code error/access-denied}
     */
    @GetMapping("/accesso-negato")
    public String accessDenied() {
        return "error/errorPage";
    }
}