package com.generation.SportHub.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.generation.SportHub.entity.Event;
import com.generation.SportHub.entity.EventAnswer;
import com.generation.SportHub.service.EventService;

@Controller
@RequestMapping("/hub")
public class HubController {

    private final EventService eService;

    public HubController(EventService eventService) {
        this.eService = eventService;
    }

   @GetMapping("")
    public String showHub(Model model) {
        model.addAttribute("events", eService.getAllEvents());
        return "hub/hub";
    }
    

    @GetMapping("/create-post")
    public String showCreatePost(Model model) {
        model.addAttribute("event", new Event());
        return "hub/create-post";
    }

    @PostMapping("/save-post")
    public String createEvent(@ModelAttribute Event event, @RequestParam Long buyerId) {
//id del buyer perchè nullable = false
        eService.createEvent(event, buyerId);
        return "redirect:/hub";
    }

    @PostMapping("/{eventId}/answer")
    public String addAnswer( @PathVariable Long eventId, @ModelAttribute EventAnswer answer, 
                                @RequestParam Long buyerId) {
        
        eService.addAnswerToEvent(eventId, answer, buyerId);
        
        return "redirect:/hub";
    }

    @PostMapping("/delete-post")
    public String deletePost(@RequestParam("idPost") Long id,
        Authentication authentication) {
            eService.deleteIfAllowed(id, authentication);
            return "redirect:/hub";
}
}
