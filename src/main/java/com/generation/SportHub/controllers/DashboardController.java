package com.generation.SportHub.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.generation.SportHub.entity.Person;
import com.generation.SportHub.repository.PersonRepository;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final PersonRepository pRepo;

    public DashboardController(PersonRepository pRepo) {
        this.pRepo= pRepo;
    }

    @GetMapping("/show-dashboard")
    public String showUserDashboard(Principal principal, Model model) {
        try {
      
            String email = principal.getName();
            
            // (Opzionale) Se vuoi passare i dati dell'utente alla dashboard, puoi caricarlo dal database:
             Person person = pRepo.findByEmailIgnoreCase(email).orElse(null);
             model.addAttribute("person", person);

            return "dashboard/dashboard"; 
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Impossibile caricare la dashboard: " + e.getMessage());
            return "error/errorPage";
        }
    } 
}
