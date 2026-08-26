package com.generation.SportHub.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.generation.SportHub.dto.PersonDTO;
import com.generation.SportHub.entity.Person;
import com.generation.SportHub.repository.PersonRepository;
import com.generation.SportHub.service.PersonService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/profile")
public class PersonController {

    private final PersonService pService;
    private final PersonRepository pRepo;

    public PersonController(PersonService pService, PersonRepository pRepo) {
        this.pService = pService;
        this.pRepo = pRepo;
    }
    @GetMapping("/detail")
    public String showMyProfile(Principal principal, Model model) {
        try {
            // principal.getName() restituisce l'email dell'utente loggato
            String email = principal.getName(); 
            
            Person p = pRepo.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new Exception("User not found"));

            model.addAttribute("personDTO", p);

            return "profile/profile"; 

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error/errorPage";
        }
    }

    @GetMapping("/update")
public String showUpdateForm(Principal principal, Model model) {
    try {
        
        String email = principal.getName(); 
        
        Person p = pRepo.findByEmailIgnoreCase(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("personDTO", p);

        return "profile/edit-profile"; // Pagina HTML del form

    } catch (Exception e) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error/errorPage";
    }
}

    @PostMapping("/save-update")
    public String updatePerson(
            Principal principal,
            @ModelAttribute("personDTO") PersonDTO personDTO, 
            Model model) {
        try {
            // Ricaviamo l'utente loggato in modo sicuro
            String email = principal.getName();
            Person currentPerson = pRepo.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

            // Usiamo l'ID sicuro del database
            pService.updatePerson(currentPerson.getId(), personDTO);
            
            return "redirect:/profile/detail?success";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "profile/edit-profile"; 
        }
    }

    @PostMapping("/delete")
        public String deleteProfile(Principal principal, HttpServletRequest request, Model model) {
            try {
                
                String email = principal.getName();
                Person currentPerson = pRepo.findByEmailIgnoreCase(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

                // Eliminiamo l'utente dal database
                pService.deletePerson(currentPerson.getId());

                // Invalido la sessione HTTP per sloggare l'utente
                request.getSession().invalidate();

                // Reindirizziamo alla home con un parametro di conferma
                return "redirect:/?accountDeleted";
                
            } catch (Exception e) {
                model.addAttribute("errorMessage", "Impossibile eliminare il profilo: " + e.getMessage());
                return "error/errorPage";
            }
        }


}



