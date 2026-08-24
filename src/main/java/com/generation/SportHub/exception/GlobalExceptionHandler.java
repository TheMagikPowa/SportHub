package com.generation.SportHub.exception;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException ex, Model model) {
        // Inserisce il messaggio dell'eccezione nel Model per poterlo stampare in HTML
        model.addAttribute("errorMessage", ex.getMessage());
        
        // Ritorna il percorso della tua pagina HTML personalizzata
        return "error/errorPage"; 
    }
}

