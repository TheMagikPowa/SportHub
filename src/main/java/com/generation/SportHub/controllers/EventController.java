package com.generation.SportHub.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.generation.SportHub.entity.Event;
import com.generation.SportHub.entity.EventAnswer;
import com.generation.SportHub.service.EventService;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventService eService;

    public EventController(EventService eventService) {
        this.eService = eventService;
    }
    @GetMapping("/{id}")
        public String getEventDetail(@PathVariable Long id, Model model) {
            Event event = eService.getEventById(id);
            model.addAttribute("event", event);
            model.addAttribute("newAnswer", new EventAnswer()); 
            return "events/detail"; //  file HTML di dettaglio
        }
    @PostMapping
    public String createEvent(@ModelAttribute Event event) {
        eService.createEvent(event);
        return "redirect:/events/board";
    }
// Aggiunta di una risposta ad un evento e reindirizzamento alla pagina di dettaglio
    @PostMapping("/{id}/answers")
    public String addAnswer( @PathVariable Long id, @ModelAttribute EventAnswer answer) {
        eService.addAnswerToEvent(id, answer);
        return "redirect:/events/" + id;
    }
}
