package com.generation.SportHub.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.generation.SportHub.entity.QuestionQA;

@Controller
@RequestMapping("/contact")
public class ContactsController {

    @GetMapping("")
    public String showContacts(Model model) {
        model.addAttribute("questionQA", new QuestionQA()); 
        return "contact/contact";
        
    }
}
