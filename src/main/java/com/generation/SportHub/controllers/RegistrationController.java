package com.generation.SportHub.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.generation.SportHub.dto.PersonDTO;
import com.generation.SportHub.service.PersonService;

@Controller
@RequestMapping("/register")
public class RegistrationController {


    private final PersonService pService;

    public RegistrationController(PersonService pService) {
        this.pService = pService;
    }
    

    @GetMapping("/show-registration")
    public String register() {
        return "register/register";
}

    @PostMapping("/save-registration") 
    public String registerUser(@ModelAttribute("signupForm") PersonDTO personDTO, Model model) {
        //TODO: 1. recuperare dati dal form 2. mappare i dati rcuperati 3. passare i dati (DTO) al service 

        
        try {
            pService.createNewPerson(personDTO);
            return "operazione conclusa con successo!"; 
            //valutare di reindirizzare a pagina 


        } catch (Exception e) {
            model.addAttribute("messaggioErrore", e.getMessage());
            model.addAttribute("signupForm", personDTO); //così mantiene i dati nel form

            return "register/register";
        }


}


}
