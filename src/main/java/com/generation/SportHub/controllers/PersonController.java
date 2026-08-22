package com.generation.SportHub.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.generation.SportHub.dto.PersonDTO;
import com.generation.SportHub.entity.Person;
import com.generation.SportHub.repository.PersonRepository;
import com.generation.SportHub.service.PersonService;

@Controller
@RequestMapping("/profile")
public class PersonController {

    private final PersonService pService;
    private final PersonRepository pRepo;

    public PersonController(PersonService pService, PersonRepository pRepo) {
        this.pService = pService;
        this.pRepo = pRepo;
    }
    @GetMapping("/edit/{id}")
    public String showFormUpdate (@PathVariable Long id, Model model) {
        try {
            Person p = pRepo.findById(id).orElseThrow(() -> new Exception ("User not found"));

            model.addAttribute("personDTO", p);
            model.addAttribute("pId", id);

            return "editForm"; //TODO da verificare nome della pagina 

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error/errorPage";
            //TODO da verificare il nome della pagina 
        }
    }

    @PostMapping("/update/{id}")
    public String updatePerson(@PathVariable Long id, @ModelAttribute("personDTO") PersonDTO personDTO, Model model) {
        try {
            pService.updatePerson(id, personDTO);
            
            // Reindirizza alla pagina di successo o al profilo, TODO da verificare
            return "redirect:/person/edit/" + id + "?success";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("personId", id);
            return "edit-person-form"; // Ritorna al form mostrando l'errore
        }
    }




}



